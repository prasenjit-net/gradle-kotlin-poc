package net.prasenjit.poc.gradle.app.frontent;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoggingFilter extends HttpFilter {

    private boolean dumpRequest;
    private boolean dumpResponse;

    public void init(FilterConfig filterConfig) throws ServletException {
        dumpRequest = Boolean.parseBoolean(filterConfig.getInitParameter("dumpRequest"));
        dumpResponse = Boolean.parseBoolean(filterConfig.getInitParameter("dumpResponse"));
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        BufferedRequestWrapper requestWrapper = new BufferedRequestWrapper(req);
        BufferedResponseWrapper responseWrapper = new BufferedResponseWrapper(req, res);
        chain.doFilter(requestWrapper, responseWrapper);
        responseWrapper.writePendingResponse();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("====REQUEST====\n");
        requestWrapper.print(stringBuilder);
        stringBuilder.append("\n\n====END====\n\n");
        stringBuilder.append("====RESPONSE====\n");
        responseWrapper.print(stringBuilder);
        stringBuilder.append("\n\n====END====\n\n");
        System.out.println(stringBuilder);
    }
}
