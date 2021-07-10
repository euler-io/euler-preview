package com.github.euler.preview;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFBoxPreviewGenerator implements PreviewGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final Set<MediaType> SUPPORTED_TYPES;

    static {
        Set<MediaType> supportedTypes = new HashSet<MediaType>();
        supportedTypes.add(MediaType.application("pdf"));

        SUPPORTED_TYPES = Collections.unmodifiableSet(supportedTypes);
    }

    private Config config;

    public PDFBoxPreviewGenerator(Config config) {
        super();
        this.config = config;
    }

    public PDFBoxPreviewGenerator() {
        this(new Config());
    }

    @Override
    public void generate(PreviewContext ctx, MediaType mediaType, File in, PreviewHandler handler) throws IOException {
        PDDocument document = null;
        try {
            document = loadDocument(ctx, in);
            generate(ctx, mediaType, document, handler);
        } catch (InvalidPasswordException e) {
            LOGGER.warn("Impossible to generate the preview: {}.", e.getMessage());
        } finally {
            closeDocument(document);
        }
    }

    @Override
    public void generate(PreviewContext ctx, MediaType mediaType, InputStream in, PreviewHandler handler) throws IOException {
        PDDocument document = null;
        try {
            document = loadDocument(ctx, in);
            generate(ctx, mediaType, document, handler);
        } catch (InvalidPasswordException e) {
            LOGGER.warn("Impossible to generate the preview: {}.", e.getMessage());
        } finally {
            closeDocument(document);
        }
    }

    private void generate(PreviewContext ctx, MediaType mediaType, PDDocument document, PreviewHandler handler) throws IOException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        PagePreview pages = getPages(ctx, document);
        for (int page = pages.getInitialPage(); page < pages.getFinalPage(); page++) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, config.getDpi(), config.getImageType());
            handler.handle(bim);
        }
    }

    protected PDDocument loadDocument(PreviewContext ctx, InputStream in) throws InvalidPasswordException, IOException {
        return PDDocument.load(in);
    }

    protected PDDocument loadDocument(PreviewContext ctx, File in) throws InvalidPasswordException, IOException {
        return PDDocument.load(in);
    }

    protected void closeDocument(PDDocument document) throws IOException {
        if (document != null) {
            document.close();
        }
    }

    @Override
    public Long getTotalPages(PreviewContext ctx, MediaType mediaType, InputStream in) throws IOException {
        PDDocument document = null;
        try {
            document = loadDocument(ctx, in);
            return (long) document.getNumberOfPages();
        } finally {
            closeDocument(document);
        }

    }

    @Override
    public Long getTotalPages(PreviewContext ctx, MediaType mediaType, File in) throws IOException {
        PDDocument document = null;
        try {
            document = loadDocument(ctx, in);
            return (long) document.getNumberOfPages();
        } finally {
            closeDocument(document);
        }

    }

    private PagePreview getPages(PreviewContext ctx, PDDocument document) {
        PagePreview pagePreview = ctx.get(PagePreview.class);
        if (pagePreview != null) {
            return pagePreview;
        } else {
            int maxPages = config.getMaxPages();
            int numberOfPages = document.getNumberOfPages();
            if (maxPages > 0) {
                numberOfPages = Math.min(numberOfPages, maxPages);
            }
            int initialPage = config.getInitialPage();
            return new PagePreview(initialPage, numberOfPages);
        }
    }

    @Override
    public Set<MediaType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    public static class Config {

        private static final int DEFAULT_MAX_PAGES = 2;
        private static final float DEFAULT_IMAGE_DPI = 100f;
        private static final int DEFAULT_INITIAL_PAGE = 0;

        private int initialPage = DEFAULT_INITIAL_PAGE;
        private int maxPages = DEFAULT_MAX_PAGES;
        private float dpi = DEFAULT_IMAGE_DPI;
        private ImageType imageType = ImageType.RGB;

        public int getMaxPages() {
            return maxPages;
        }

        public void setMaxPages(int maxPages) {
            this.maxPages = maxPages;
        }

        public float getDpi() {
            return dpi;
        }

        public void setDpi(float dpi) {
            this.dpi = dpi;
        }

        public int getInitialPage() {
            return initialPage;
        }

        public void setInitialPage(int initialPage) {
            this.initialPage = initialPage;
        }

        public ImageType getImageType() {
            return imageType;
        }

        public void setImageType(ImageType imageType) {
            this.imageType = imageType;
        }

    }

}
