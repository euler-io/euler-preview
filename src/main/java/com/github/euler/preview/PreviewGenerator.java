package com.github.euler.preview;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface PreviewGenerator {

    void generate(PreviewContext ctx, MediaType mediaType, InputStream in, PreviewHandler handler) throws IOException;

    void generate(PreviewContext ctx, MediaType mediaType, File in, PreviewHandler handler) throws IOException;

    Set<MediaType> getSupportedTypes();

    Long getTotalPages(PreviewContext ctx, MediaType mediaType, InputStream in) throws IOException;

    Long getTotalPages(PreviewContext ctx, MediaType mediaType, File in) throws IOException;

}