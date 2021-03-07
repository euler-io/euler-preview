package com.github.euler.preview;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FilePreviewHandler implements PreviewHandler {

    private final File out;
    private final String formatName;

    public FilePreviewHandler(File out, String formatName) {
        super();
        this.out = out;
        this.formatName = formatName;
    }

    @Override
    public void handle(BufferedImage bim) throws IOException {
        ImageIO.write(bim, formatName.toUpperCase(), out);
    }

}
