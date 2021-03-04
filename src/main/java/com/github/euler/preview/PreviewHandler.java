package com.github.euler.preview;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface PreviewHandler {

    void handle(BufferedImage bim) throws IOException;

}
