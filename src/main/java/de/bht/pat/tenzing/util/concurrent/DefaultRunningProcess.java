package de.bht.pat.tenzing.util.concurrent;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.common.util.concurrent.Service;

import java.io.IOException;
import java.io.InputStream;

final class DefaultRunningProcess implements RunningProcess {

    private static final CharMatcher LINE_BREAKS = CharMatcher.anyOf("\r\n");

    private final Process process;
    private final String command;

    public DefaultRunningProcess(ProcessBuilder builder) throws IOException {
        this.process = builder.start();
        this.command = Joiner.on(' ').join(builder.command());
    }

    private int waitFor() throws IOException {
        final int exitValue;

        try {
            exitValue = process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }

        return exitValue;
    }

    private IOException fail(int exitValue) throws IOException {
        final byte[] bytes = ByteStreams.toByteArray(process.getErrorStream());

        final String message;

        if (bytes.length == 0) {
            message = String.format("%s failed with exit code %s", this, exitValue);
        } else {
            message = LINE_BREAKS.trimTrailingFrom(new String(bytes, Charsets.UTF_8));
        }

        return new IOException(message);
    }

    @Override
    public void await() throws IOException {
        try {
            int exitValue = waitFor();
            if (exitValue != 0) {
                throw fail(exitValue);
            }
        } finally {
            Thread.interrupted();
            cleanup();
        }
    }

    @Override
    public String getResult() throws IOException {
        try {
            int exitValue = waitFor();
            if (exitValue == 0) {
                final InputStream stdin = process.getInputStream();
                final byte[] bytes = ByteStreams.toByteArray(stdin);
                return new String(bytes, Charsets.UTF_8);
            } else {
                throw fail(exitValue);
            }
        } finally {
            Thread.interrupted();
            cleanup();
        }
    }

    @Override
    public <V> V apply(Function<String, V> function) throws IOException {
        return function.apply(getResult());
    }

    @Override
    public void cancel() {
        cleanup();
    }

    private void cleanup() {
        Closeables.closeQuietly(process.getInputStream());
        Closeables.closeQuietly(process.getOutputStream());
        Closeables.closeQuietly(process.getErrorStream());
        process.destroy();
    }

    private Service.State currentState() {
        try {
            switch (process.exitValue()) {
                case 0:
                    return Service.State.TERMINATED;
                default:
                    return Service.State.FAILED;
            }
        } catch (IllegalThreadStateException e) {
            return Service.State.RUNNING;
        }
    }

    @Override
    public String toString() {
        return command + " [" + currentState().name().toLowerCase() + "]";
    }

}
