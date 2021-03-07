package com.github.euler.preview;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class OutputStreamPreviewHandler implements PreviewHandler {

    private final OutputStream out;
    private final String formatName;

    public OutputStreamPreviewHandler(OutputStream out, String formatName) {
        super();
        this.out = out;
        this.formatName = formatName;
    }

    @Override
    public void handle(BufferedImage im) throws IOException {
        ImageIO.write(im, formatName, out);
    }

}