package com.github.euler.preview;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

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
        BufferedImage resized = Scalr.resize(bim, config.getMethod(), config.getMode(), config.getWidth(), config.getHeigth(), config.getOps());
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

    public static class ScalrConfig {

        private Scalr.Method method;
        private Scalr.Mode mode;
        private int width;
        private int heigth;
        private BufferedImageOp[] ops;

        public ScalrConfig(Method method, Mode mode, int width, int heigth, BufferedImageOp... ops) {
            super();
            this.method = method;
            this.mode = mode;
            this.width = width;
            this.heigth = heigth;
            this.ops = ops;
        }

        public Scalr.Method getMethod() {
            return method;
        }

        public void setMethod(Scalr.Method method) {
            this.method = method;
        }

        public Scalr.Mode getMode() {
            return mode;
        }

        public void setMode(Scalr.Mode mode) {
            this.mode = mode;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeigth() {
            return heigth;
        }

        public void setHeigth(int heigth) {
            this.heigth = heigth;
        }

        public BufferedImageOp[] getOps() {
            return ops;
        }

        public void setOps(BufferedImageOp... ops) {
            this.ops = ops;
        }

    }
}
