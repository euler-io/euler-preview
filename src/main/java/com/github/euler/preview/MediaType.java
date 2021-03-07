package com.github.euler.preview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaType {

    private static final Pattern REGEX = Pattern.compile("([^\\/]+)\\/([^;]+).*");

    private String type;
    private String subType;

    public MediaType(String type, String subType) {
        super();
        this.type = type;
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subType == null) ? 0 : subType.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MediaType other = (MediaType) obj;
        if (subType == null) {
            if (other.subType != null)
                return false;
        } else if (!subType.equals(other.subType))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    public static MediaType text(String subType) {
        return new MediaType("text", subType);
    }

    public static MediaType audio(String subType) {
        return new MediaType("audio", subType);
    }

    public static MediaType image(String subType) {
        return new MediaType("image", subType);
    }

    public static MediaType application(String subType) {
        return new MediaType("application", subType);
    }

    public static MediaType parse(String mimeType) {
        Matcher matcher = REGEX.matcher(mimeType);
        if (matcher.matches()) {
            return new MediaType(matcher.group(1), matcher.group(2));
        } else {
            throw new IllegalArgumentException(mimeType + " is not a valid mime type.");
        }
    }

}
