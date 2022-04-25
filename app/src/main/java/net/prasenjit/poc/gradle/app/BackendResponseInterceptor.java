package net.prasenjit.poc.gradle.app;

import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BackendResponseInterceptor implements HttpResponseInterceptor {
    @Override
    public void process(HttpResponse response, EntityDetails entity, HttpContext context) throws HttpException, IOException {
        StringBuilder stringBuilder = new StringBuilder("===BACK RESP===\n");
        stringBuilder.append(context.getProtocolVersion().toString())
                .append(" ").append(response.getCode())
                .append(" ").append(response.getReasonPhrase()).append("\n");

        for (Header header : response.getHeaders()) {
            stringBuilder.append(header.getName()).append(": ").append(header.getValue()).append("\n");
        }

        if (BasicClassicHttpResponse.class.isAssignableFrom(response.getClass())) {
            final HttpEntity entity1 = ((BasicClassicHttpResponse) response).getEntity();
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
