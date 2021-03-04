package com.github.euler.preview;

import java.util.HashMap;
import java.util.Map;

public class PreviewContext {

    @SuppressWarnings("rawtypes")
    private Map<Class, Object> params = new HashMap<>();

    public <T> void set(Class<T> clazz, T value) {
        params.put(clazz, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        return (T) params.get(clazz);
    }

    public <T> T get(Class<T> clazz, T def) {
        T value = get(clazz);
        return value != null ? value : def;
    }

}
