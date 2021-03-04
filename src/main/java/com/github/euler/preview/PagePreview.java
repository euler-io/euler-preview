package com.github.euler.preview;

public class PagePreview {

    private final int initialPage;
    private final int finalPage;

    public PagePreview(int initialPage, int finalPage) {
        super();
        this.initialPage = initialPage;
        this.finalPage = finalPage;
    }

    public PagePreview(int page) {
        this(page, page + 1);
    }

    public PagePreview() {
        this(0, 1);
    }

    public int getInitialPage() {
        return initialPage;
    }

    public int getFinalPage() {
        return finalPage;
    }

}
