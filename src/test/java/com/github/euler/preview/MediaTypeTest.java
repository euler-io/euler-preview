package com.github.euler.preview;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MediaTypeTest {

    @Test
    public void testParse() {
        String mimeType = "text/plain";

        MediaType parsed = MediaType.parse(mimeType);
        assertEquals("text", parsed.getType());
        assertEquals("plain", parsed.getSubType());

        mimeType = "image/jpeg";
        parsed = MediaType.parse(mimeType);
        assertEquals("image", parsed.getType());
        assertEquals("jpeg", parsed.getSubType());
    }

    @Test
    public void testParseWithParameters() {
        String mimeType = "text/plain;charset=UTF-8";

        MediaType parsed = MediaType.parse(mimeType);
        assertEquals("text", parsed.getType());
        assertEquals("plain", parsed.getSubType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNoSlash() {
        String mimeType = "text;plain";
        MediaType.parse(mimeType);
    }

}
