package com.github.euler.preview;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImagePreviewGenerator implements PreviewGenerator {

    private static final Set<MediaType> SUPPORTED_TYPES;

    static {
        Set<MediaType> supportedTypes = new HashSet<MediaType>();
        supportedTypes.add(MediaType.image("jpg"));
        supportedTypes.add(MediaType.image("jpeg"));
        supportedTypes.add(MediaType.image("gif"));
        supportedTypes.add(MediaType.image("png"));
        supportedTypes.add(MediaType.image("bmp"));

        SUPPORTED_TYPES = Collections.unmodifiableSet(supportedTypes);
    }

    private ScalrConfig config;

    public ImagePreviewGenerator() {
        this(new ScalrConfig(Scalr.Method.BALANCED, Scalr.Mode.AUTOMATIC, 100, 100, Scalr.OP_ANTIALIAS));
    }

    public ImagePreviewGenerator(ScalrConfig config) {
        super();
        this.config = config;
    }

    @Override
    public void generate(PreviewContext ctx, MediaType mediaType, InputStream in, PreviewHandler handler) throws IOException {
        BufferedImage img = ImageIO.read(in);
        generate(ctx, mediaType, img, handler);
    }

    @Override
    public void generate(PreviewContext ctx, MediaType mediaType, File in, PreviewHandler handler) throws IOException {
        BufferedImage img = ImageIO.read(in);
        generate(ctx, mediaType, img, handler);
    }

    public void generate(PreviewContext ctx, MediaType mediaType, BufferedImage bim, PreviewHandler handler) throws IOException {
        ScalrConfig resizeConfig = ctx.get(ScalrConfig.class, this.config);
        BufferedImage resized = Scalr.resize(bim,
                resizeConfig.getMethod(),
                resizeConfig.getMode(),
                resizeConfig.getWidth(),
                resizeConfig.getHeigth(),
                resizeConfig.getOps());
        handler.handle(resized);
    }

    @Override
    public Set<MediaType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public Long getTotalPages(PreviewContext ctx, MediaType mediaType, InputStream in) {
        return 1l;
    }

    @Override
    public Long getTotalPages(PreviewContext ctx, MediaType mediaType, File in) throws IOException {
        return 1l;
    }
}
