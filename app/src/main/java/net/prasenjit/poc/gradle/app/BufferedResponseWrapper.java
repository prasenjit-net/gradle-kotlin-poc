package net.prasenjit.poc.gradle.app;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class BufferedResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private boolean flushed = false;
    private PrintWriter printWriter;
    private BufferedServletOutputStream bufferedStream;
    private HttpServletRequest request;

    public BufferedResponseWrapper(HttpServletRequest request, HttpServletResponse response) {
        super(response);
        this.request = request;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (bufferedStream == null) {
            bufferedStream = new BufferedServletOutputStream(outputStream);
        }
        return bufferedStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (printWriter == null) {
            printWriter = new PrintWriter(outputStream);
        }
        return printWriter;
    }

    public void writePendingResponse() throws IOException {
        if (!flushed) {
            if (printWriter != null) {
                printWriter.flush();
            } else if (bufferedStream != null) {
                bufferedStream.flush();
            }
            IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), super.getOutputStream());
        }
        flushed = true;
    }

    public void print(StringBuilder stringBuilder) {
        HttpServletResponse response = (HttpServletResponse) this.getResponse();
        stringBuilder.append(request.getProtocol()).append(" ").append(response.getStatus()).append("\n");

        Collection<String> headerNames = response.getHeaderNames();
        for (String name : headerNames) {
            stringBuilder.append(name).append(": ");
            Collection<String> values = response.getHeaders(name);
            final String completeHeader = StringUtils.join(values, " ");
            stringBuilder.append(completeHeader).append("\n");
        }

        stringBuilder.append("\n").append(new String(getBuffer())).append("\n");
    }

    private byte[] getBuffer() {
        return this.outputStream.toByteArray();
    }
}
