package net.prasenjit.poc.gradle.app.frontent;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BufferedServletOutputStream extends ServletOutputStream {

    private final ByteArrayOutputStream baos;

    public BufferedServletOutputStream(ByteArrayOutputStream baos) {
        this.baos = baos;
    }

    @Override
    public void write(int param) throws IOException {
        baos.write(param);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }
}
