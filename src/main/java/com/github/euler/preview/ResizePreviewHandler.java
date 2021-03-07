package com.github.euler.preview;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.imgscalr.Scalr;

public class ResizePreviewHandler implements PreviewHandler {

    private final ScalrConfig config;
    private final PreviewHandler delegate;
    private final boolean forceResize;

    public ResizePreviewHandler(ScalrConfig config, PreviewHandler delegate, boolean forceResize) {
        super();
        this.config = config;
        this.delegate = delegate;
        this.forceResize = forceResize;
    }

    public ResizePreviewHandler(ScalrConfig config, PreviewHandler delegate) {
        this(config, delegate, true);
    }

    @Override
    public void handle(BufferedImage bim) throws IOException {

        BufferedImage resized;
        if (forceResize || bim.getHeight() != config.getHeigth() || bim.getWidth() != config.getWidth()) {
            resized = Scalr.resize(bim, config.getMethod(), config.getMode(), config.getWidth(), config.getHeigth(), config.getOps());
        } else {
            resized = bim;
        }

        delegate.handle(resized);
    }

}
