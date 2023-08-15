package com.yupi.project;

import com.example.apiclientsdk.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @BelongsProject: api-gateway
 * @BelongsPackage: com.example.apigateway
 * @Author: liaocy
 * @CreateTime: 2023-08-11  14:54
 * @Description: Ordered可以编排过滤器的优先级
 * @Version: 1.0
 */
@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_WHITE_LIST= Arrays.asList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1. 请求日志
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        log.info("请求唯一标识:"+serverHttpRequest.getId());
        String sourceAddress=serverHttpRequest.getLocalAddress().getHostString();
        log.info("请求路径:"+sourceAddress);
        log.info("请求方法:"+serverHttpRequest.getMethod());
        log.info("请求参数:"+serverHttpRequest.getQueryParams());
        log.info("请求来源地址:"+serverHttpRequest.getRemoteAddress());
//        2. （黑白名单）
        ServerHttpResponse response = exchange.getResponse();
        if(!IP_WHITE_LIST.contains(sourceAddress)){
            return handleNoAuth(response);
        }
//        3. 用户鉴权（判断 accessKey, secretKey 是否合法）
        HttpHeaders headers = serverHttpRequest.getHeaders();
        //从请求头获取api签名
        String accessKey = headers.getFirst("accessKey");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");

        //校验accessKeyc TODO 实际应与数据库中的数据对比
        if (!"jinze".equals(accessKey)) {
            return handleNoAuth(response);
        }
        //校验随机数，给定的随机数规则是5位以下
        if(Long.parseLong(nonce)>10000){
            return handleNoAuth(response);
        }
        //校验时间戳，5min内有效
        long currentTime=System.currentTimeMillis()/1000;
        long FIVE_MINUTES=60*5L;
        if(currentTime-Long.parseLong(timestamp)>=FIVE_MINUTES){
            return handleNoAuth(response);
        }
        //校验签名 TODO 实际secretKey是从数据库获取的
        String realSign = SignUtil.getSign(body, "abcdefg");
        if(!realSign.equals(sign)){
            return handleNoAuth(response);
        }
//        todo: 4. 请求的模拟接口是否存在
//        5. 请求转发，调用模拟接口(响应日志中调用了 148行)
        //Mono<Void> filter=chain.filter(exchange);
//        6. 响应日志
        return handleResponse(exchange,chain);

    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        //body是响应式的
                        if (body instanceof Flux) {
                            //拿到真正的body
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        /*try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }*/
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        //设置响应拦截
        return response.setComplete();
    }
}