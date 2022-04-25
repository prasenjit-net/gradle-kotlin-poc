package net.prasenjit.poc.gradle.app;

import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;

public class MyServlet extends HttpServlet {
    private static final String FILTERED_HEADERS = "host connection content-type content-length";
    private CloseableHttpClient client;
    private HttpHost backend;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        client = HttpClients.custom()
                .addRequestInterceptorLast(new BackendRequestInterceptor())
                .addResponseInterceptorLast(new BackendResponseInterceptor())
                .build();
        String backendUri = config.getInitParameter("backendUri");
        try {
            backend = HttpHost.create(backendUri);
        } catch (URISyntaxException e) {
            throw new ServletException("wrong backend uri", e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final ClassicRequestBuilder requestBuilder = ClassicRequestBuilder.create(req.getMethod());

        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            if (FILTERED_HEADERS.contains(name.toLowerCase())) {
                continue;
            }
            final Enumeration<String> values = req.getHeaders(name);
            while (values.hasMoreElements()) {
                final String value = values.nextElement();
                requestBuilder.addHeader(name, value);
            }
        }
        final byte[] bytes = IOUtils.toByteArray(req.getInputStream());
        final String contentType = req.getContentType();
        final ContentType contentType1 = ContentType.parse(contentType);
        HttpEntity bodyEntity = new ByteArrayEntity(bytes, contentType1);
        requestBuilder.setEntity(bodyEntity);

        requestBuilder.setHttpHost(backend);

        String reqUriPath = req.getRequestURI() + "?" + req.getQueryString();
        requestBuilder.setPath(reqUriPath);

        ClassicHttpRequest backendRequest = requestBuilder.build();
        final CloseableHttpResponse backendResponse = client.execute(backendRequest);

        resp.setStatus(backendResponse.getCode());
        resp.setContentType(backendResponse.getEntity().getContentType());
        resp.setCharacterEncoding(backendResponse.getEntity().getContentEncoding());
        final Locale locale = backendResponse.getLocale();
        if (locale != null) {
            resp.setLocale(locale);
        }


        Arrays.stream(backendResponse.getHeaders())
                .filter(header -> !FILTERED_HEADERS.contains(header.getName().toLowerCase()))
                .forEach(h -> resp.addHeader(h.getName(), h.getValue()));

        backendResponse.getEntity().writeTo(resp.getOutputStream());
    }
}
