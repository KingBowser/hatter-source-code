package me.hatter.tools.resourceproxy.jsspserver.util;

import me.hatter.tools.commons.resource.Resource;
import me.hatter.tools.commons.resource.Resources;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;

public class JsspResource {

    private Resource resource;
    private Boolean  exists = null;
    private Resource explained;
    private String   explainedContent;

    synchronized public boolean exists() {
        if (exists == null) {
            exists = resource.exists();
        }
        return exists.booleanValue();
    }

    public JsspResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getExplained() {
        return explained;
    }

    public void setExplained(Resource explained) {
        this.explained = explained;
    }

    public String getExplainedContent() {
        return getExplainedContent(false);
    }

    synchronized public String getExplainedContent(boolean update) {
        if (update) {
            explained = null;
            explainedContent = null;
        }
        if (explainedContent == null) {
            if (explained == null) {
                explained = JsspExecutor.tryExplainJssp(resource);
            }
            explainedContent = Resources.readToString(explained, ContentTypes.UTF8_CHARSET);
        }
        return explainedContent;
    }

    synchronized public String getExplainedContentOnAir() {
        explained = JsspExecutor.tryExplainJsspOnAir(resource);
        explainedContent = Resources.readToString(explained, ContentTypes.UTF8_CHARSET);
        return explainedContent;
    }
}
