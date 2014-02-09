package me.hatter.tools.markdowndocs.assets;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class Asset {

    private String      template;

    private Set<String> resources = new LinkedHashSet<String>();

    public Asset(String template, Collection<String> resources) {
        this.template = template;
        this.resources.addAll(resources);
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Set<String> getResources() {
        return resources;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources;
    }
}
