package com.github.euler.preview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreviewConfig {

    private List<PreviewGenerator> generators = new ArrayList<>();

    public void add(PreviewGenerator generator) {
        this.generators.add(generator);
    }

    public boolean isSupported(MediaType mediaType) {
        return get(mediaType) != null;
    }

    public PreviewGenerator get(MediaType mediaType) {
        return generators.stream()
                .filter(g -> g.getSupportedTypes().contains(mediaType))
                .findFirst()
                .orElse(null);
    }

    public List<PreviewGenerator> getGenerators() {
        return Collections.unmodifiableList(generators);
    }

}
