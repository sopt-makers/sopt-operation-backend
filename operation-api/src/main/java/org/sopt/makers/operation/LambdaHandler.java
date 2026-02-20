package org.sopt.makers.operation;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * AWS Lambda 진입점 핸들러
 *
 * API Gateway 요청을 Spring Boot Controller로 라우팅합니다.
 * SnapStart를 통해 Cold Start 시간을 최소화합니다.
 */
public class LambdaHandler implements RequestStreamHandler {

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(OperationApplication.class);


// ✨ 이미지, 폰트만 바이너리 처리 (JS/CSS 제외)
            SpringBootLambdaContainerHandler.getContainerConfig().addBinaryContentTypes(
                    // 이미지
                    "image/png",
                    "image/jpeg",
                    "image/gif",
                    "image/svg+xml",
                    "image/webp",
                    "image/x-icon",

                    // 폰트
                    "font/woff",
                    "font/woff2",
                    "application/font-woff",
                    "application/font-woff2",

                    // 기타 바이너리
                    "application/octet-stream",
                    "application/pdf"
            );

        } catch (ContainerInitializationException e) {
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not initialize Lambda handler", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}
