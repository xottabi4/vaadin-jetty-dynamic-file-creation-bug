package org.vaadin.example;

import com.vaadin.flow.server.InputStreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class EventInputStreamFactory implements InputStreamFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EventInputStreamFactory.class);

    @Override
    public InputStream createInputStream() {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            prepareZip(byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            LOG.error("Could not prepare zip package to download.", e);
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    private void prepareZip(
            ByteArrayOutputStream byteArrayOutputStream
    ) throws IOException {

        try (final ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            processSignal(zipOutputStream);
        }
    }

    private void processSignal(ZipOutputStream zipOutputStream) throws IOException {
        final String source = "Some payload";
        final int sequenceId = 1;
        final long timeIn = Instant.now().toEpochMilli();
        final String extension = "game";
        final String fileName = "%s_#%d.%s".formatted(timeIn, sequenceId, extension);

        zipOutputStream.putNextEntry(new ZipEntry(fileName));
        zipOutputStream.write(source.getBytes(StandardCharsets.UTF_8));
        zipOutputStream.closeEntry();
    }

}