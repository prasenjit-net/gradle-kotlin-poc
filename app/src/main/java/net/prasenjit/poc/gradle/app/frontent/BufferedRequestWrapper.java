package net.prasenjit.poc.gradle.app.frontent;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class BufferedRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] bodyData;
    private BufferedServletInputStream stream;

    public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
        super(req);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        IOUtils.copy(req.getInputStream(), outStream);
        bodyData = outStream.toByteArray();
    }

    public ServletInputStream getInputStream() {
        if (stream == null) {
            stream = new BufferedServletInputStream(new ByteArrayInputStream(bodyData));
        }
        return stream;
    }

    public byte[] getBuffer() {
        return bodyData;
    }

    public void print(StringBuilder stringBuilder) {
        HttpServletRequest request = (HttpServletRequest) this.getRequest();
        stringBuilder.append(request.getMethod()).append(" ").append(request.getRequestURI())
                .append("?").append(request.getQueryString()).append("\n");

        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            stringBuilder.append(name).append(": ");
            final Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                final String value = values.nextElement();
                stringBuilder.append(value).append(" ");
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append("\n").append(new String(getBuffer())).append("\n");
    }
}
