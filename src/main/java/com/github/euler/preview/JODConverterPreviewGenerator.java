package com.github.euler.preview;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFamily;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.job.ConversionJobWithOptionalSourceFormatUnspecified;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.LocalConverter.Builder;
import org.jodconverter.local.filter.PageCounterFilter;
import org.jodconverter.local.filter.PagesSelectorFilter;

public class JODConverterPreviewGenerator implements PreviewGenerator {

    private Set<MediaType> supportedTypes;

    private final Config config;
    private final OfficeManager officeManager;

    public JODConverterPreviewGenerator(Config config, OfficeManager officeManager) {
        super();
        this.config = config;
        this.officeManager = officeManager;
        initTypes();
    }

    public JODConverterPreviewGenerator(OfficeManager officeManager) {
        this(new Config(), officeManager);
    }

    private void initTypes() {
        supportedTypes = new HashSet<>();
        for (DocumentFamily df : DocumentFamily.values()) {
            Set<DocumentFormat> outputFormats = DefaultDocumentFormatRegistry.getOutputFormats(df);
            for (DocumentFormat docFormat : outputFormats) {
                String mt = docFormat.getMediaType();
                String[] split = mt.split("/");
                MediaType mediaType = new MediaType(split[0], split[1]);
                supportedTypes.add(mediaType);
            }
        }
    }

    @Override
    public void generate(PreviewContext ctx, MediaType mediaType, File in, PreviewHandler handler) throws IOException {
        DocumentConverter converter = getConverter(ctx);
        Objects.requireNonNull(converter, () -> DocumentConverter.class.getName() + " is required to use this converter.");
        ConversionJobWithOptionalSourceFormatUnspecified job = converter.convert(in);
        generate(ctx, mediaType, job, handler);
    }

    @Override
    public void generate(PreviewContext ctx, MediaType mediaType, InputStream in, PreviewHandler handler) throws IOException {
        DocumentConverter converter = getConverter(ctx);
        Objects.requireNonNull(converter, () -> DocumentConverter.class.getName() + " is required to use this converter.");
        ConversionJobWithOptionalSourceFormatUnspecified job = converter.convert(in, false);
        generate(ctx, mediaType, job, handler);
    }

    public void generate(PreviewContext ctx, MediaType mediaType, ConversionJobWithOptionalSourceFormatUnspecified in, PreviewHandler handler) throws IOException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            in.as(DefaultDocumentFormatRegistry.getFormatByMediaType(mediaType.toString()))
                    .to(out)
                    .as(DefaultDocumentFormatRegistry.PNG)
                    .execute();

            BufferedImage bim = ImageIO.read(new ByteArrayInputStream(out.toByteArray()));
            handler.handle(bim);
        } catch (OfficeException e) {
            throw new RuntimeException(e);
        }
    }

    protected DocumentConverter getConverter(PreviewContext ctx) {
        Objects.requireNonNull(officeManager, () -> OfficeManager.class.getName() + " is required to use this converter.");
        Builder builder = LocalConverter.builder()
                .officeManager(officeManager)
                .storeProperties(config.getProperties(ctx));

        PagePreview pagePreview = ctx.get(PagePreview.class);
        if (pagePreview != null && pagePreview.getInitialPage() == (pagePreview.getFinalPage() - 1)) {
            builder.filterChain(new PagesSelectorFilter(pagePreview.getFinalPage()));
        }
        return builder.build();
    }

    @Override
    public Long getTotalPages(PreviewContext ctx, MediaType mediaType, InputStream in) {
        return null;
    }

    @Override
    public Long getTotalPages(PreviewContext ctx, MediaType mediaType, File in) throws IOException {
        PageCounterFilter filter = new PageCounterFilter();
        LocalConverter converter = LocalConverter.builder()
                .officeManager(officeManager)
                .storeProperties(config.getProperties(ctx))
                .filterChain(filter)
                .build();

        File tmp = File.createTempFile("jod", ".tmp");
        try {
            converter.convert(in)
                    .as(DefaultDocumentFormatRegistry.getFormatByMediaType(mediaType.toString()))
                    .to(tmp)
                    .as(DefaultDocumentFormatRegistry.PNG)
                    .execute();
        } catch (OfficeException e) {
            throw new RuntimeException(e);
        }
        tmp.delete();
        return (long) filter.getPageCount();
    }

    @Override
    public Set<MediaType> getSupportedTypes() {
        return supportedTypes;
    }

    public void addSupportedType(MediaType mediaType) {
        supportedTypes.add(mediaType);
    }

    public static class Config {

        private static final int DEFAULT_MAX_PAGES = 2;
        private static final int DEFAULT_INITIAL_PAGE = 0;

        private int initialPage = DEFAULT_INITIAL_PAGE;
        private int finalPage = DEFAULT_MAX_PAGES;

        public int getFinalPage() {
            return finalPage;
        }

        public void setFinalPage(int finalPage) {
            this.finalPage = finalPage;
        }

        public int getInitialPage() {
            return initialPage;
        }

        public void setInitialPage(int initialPage) {
            this.initialPage = initialPage;
        }

        private Map<String, Object> getProperties(PreviewContext ctx) {
            PagePreview pagePreview = ctx.get(PagePreview.class);
            String pageRange;
            if (pagePreview != null) {
                pageRange = (pagePreview.getInitialPage() + 1) + "-" + (pagePreview.getFinalPage() + 1);
            } else {
                pageRange = (initialPage + 1) + "-" + (finalPage > 0 ? finalPage : "");
            }
            Map<String, Object> filterData = new HashMap<>();
            filterData.put("PageRange", pageRange);
            Map<String, Object> customProperties = new HashMap<>();
            customProperties.put("FilterData", filterData);
            return customProperties;
        }

    }

}
