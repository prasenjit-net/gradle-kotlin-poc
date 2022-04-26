package net.prasenjit.poc.gradle.app.frontent;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;

public class BufferedServletInputStream extends ServletInputStream {

    ByteArrayInputStream bais;
    private boolean finished = false;

    public BufferedServletInputStream(ByteArrayInputStream bais) {
        this.bais = bais;
    }

    public int available() {
        return bais.available();
    }

    public int read() {
        return bais.read();
    }

    public int read(byte[] buf, int off, int len) {
        final int read = bais.read(buf, off, len);
        if (read < 0) {
            this.finished = true;
        }
        return read;
    }

    @Override
    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
    }
}
