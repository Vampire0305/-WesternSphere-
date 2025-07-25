package com.jobportal.JobPortal.security;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfInputStreamConverter extends AbstractHttpMessageConverter<InputStream> {

    public PdfInputStreamConverter() {
        super(MediaType.APPLICATION_PDF);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // This converter supports any class that is a subclass of InputStream
        return InputStream.class.isAssignableFrom(clazz);
    }

    @Override
    protected InputStream readInternal(Class<? extends InputStream> clazz, HttpInputMessage inputMessage) {
        // We don't need to read anything in this use case (download)
        return null;
    }

    @Override
    protected void writeInternal(InputStream inputStream, HttpOutputMessage outputMessage) throws IOException {
        // Copy the bytes from the InputStream directly to the response's OutputStream
        try (InputStream in = inputStream) {
            FileCopyUtils.copy(in, outputMessage.getBody());
        }
    }
}
