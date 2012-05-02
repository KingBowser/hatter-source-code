package me.hatter.tools.commons.resource;

import java.io.InputStream;

public interface Resource {

    String getResourceId();

    InputStream openInputStream();

    boolean exists();

    long lastModified();
}
