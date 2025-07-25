package com.jobportal.JobPortal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private PdfInputStreamConverter pdfInputStreamConverter;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Add our custom converter to the beginning of the list
        // This ensures it gets picked up before any default converters
        converters.add(0, pdfInputStreamConverter);
    }
}