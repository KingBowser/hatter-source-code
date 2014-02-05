package me.hatter.tools.commons.resource;

import java.io.InputStream;

public interface Resource {

    int hashCode();

    boolean equals(Object obj);

    String toString();

    String getResId();

    boolean exists();

    long lastModified();

    InputStream openInputStream();
    
    Object getRaw();
}
