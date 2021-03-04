package com.github.euler.preview;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class EulerPreview {

    private final PreviewConfig config;

    public EulerPreview(PreviewConfig config) {
        super();
        this.config = config;
    }

    public void generate(PreviewContext ctx, MediaType mediaType, InputStream in, PreviewHandler handler) throws IOException {
        PreviewGenerator generator = config.get(mediaType);
        generator.generate(ctx, mediaType, in, handler);
    }

    public void generate(PreviewContext ctx, MediaType mediaType, File in, PreviewHandler handler) throws IOException {
        PreviewGenerator generator = config.get(mediaType);
        generator.generate(ctx, mediaType, in, handler);
    }

    public boolean isSupported(MediaType mediaType) {
        return config.isSupported(mediaType);
    }

    public PreviewInfo getInfo(PreviewContext ctx, MediaType mediaType, InputStream in) throws IOException {
        boolean supported = isSupported(mediaType);
        Long totalPages;
        if (supported) {
            PreviewGenerator generator = config.get(mediaType);
            totalPages = generator.getTotalPages(ctx, mediaType, in);
        } else {
            totalPages = null;
        }
        return new PreviewInfo(supported, totalPages);
    }

    public PreviewInfo getInfo(PreviewContext ctx, MediaType mediaType, File in) throws IOException {
        boolean supported = isSupported(mediaType);
        Long totalPages;
        if (supported) {
            PreviewGenerator generator = config.get(mediaType);
            totalPages = generator.getTotalPages(ctx, mediaType, in);
        } else {
            totalPages = null;
        }
        return new PreviewInfo(supported, totalPages);
    }

    public PreviewConfig getConfig() {
        return config;
    }

}
