package net.prasenjit.poc.gradle.app;

import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BackendRequestInterceptor implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
        StringBuilder stringBuilder = new StringBuilder("===BACK REQ===\n");
        stringBuilder.append(request.getMethod()).append(" ").append(request.getRequestUri());

        stringBuilder.append("Content-Type: ").append(entity.getContentType()).append("\n")
                .append("Content-Length: ").append(entity.getContentLength()).append("\n")
                .append("Content-Encoding: ").append(entity.getContentEncoding()).append("\n");

        for (Header header : request.getHeaders()) {
            stringBuilder.append(header.getName()).append(": ").append(header.getValue()).append("\n");
        }

        if (BasicClassicHttpRequest.class.isAssignableFrom(request.getClass())) {
            final HttpEntity entity1 = ((BasicClassicHttpRequest) request).getEntity();
            if (entity1.isRepeatable()) {
                final String rBody = EntityUtils.toString(entity1, StandardCharsets.UTF_8);
                stringBuilder.append("\n").append(rBody).append("\n");
            } else {
                stringBuilder.append("\n").append("Body not repeatable").append("\n");
            }
        }


        stringBuilder.append("\n===BACK RESP===\n");
        System.out.println(stringBuilder);
    }
}
