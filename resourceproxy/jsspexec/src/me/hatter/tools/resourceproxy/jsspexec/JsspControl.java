package me.hatter.tools.resourceproxy.jsspexec;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.jsspexec.jsspreader.ExplainedJsspReader;
import me.hatter.tools.resourceproxy.jsspexec.jsspreader.SimpleJsspReader;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;

public class JsspControl {

    private JsspReader          jsspReader;
    private String              jssp;
    private Map<String, Object> context;

    public JsspControl(JsspReader jsspReader) {
        this.jsspReader = jsspReader;
    }

    public JsspControl jssp(String path) {
        jssp = path;
        context = new HashMap<String, Object>();
        return this;
    }

    public JsspControl parameter(String key, Object value) {
        context.put(key, value);
        return this;
    }

    public JsspControl param(String key, Object value) {
        return this.parameter(key, value);
    }

    public JsspControl parameters(Map<String, Object> map) {
        if (map != null) {
            for (String key : map.keySet()) {
                parameter(key, map.get(key));
            }
        }
        return this;
    }

    public JsspControl params(Map<String, Object> map) {
        return this.parameters(map);
    }

    @Override
    public String toString() {
        // JsspExecutor.explainAndReadJssp(resource)
        try {
            if (jsspReader == null) {
                throw new RuntimeException("Jssp reader is null.");
            }
            String explainedJssp;
            Resource source = null;
            if (jsspReader instanceof SimpleJsspReader) {
                source = ((SimpleJsspReader) jsspReader).readResource(jssp);
                explainedJssp = JsspExecutor.explainAndReadJssp(source);
            } else if (jsspReader instanceof ExplainedJsspReader) {
                explainedJssp = ((ExplainedJsspReader) jsspReader).readExplained(jssp);
            } else {
                throw new RuntimeException("Unknow jssp reader type: " + jsspReader.getClass().getName());
            }
            BufferWriter out = new BufferWriter();
            JsspExecutor.executeExplained(new StringReader(explainedJssp), context, null, jsspReader, out, source);
            return out.getBufferedString();
        } catch (Exception e) {
            throw new RuntimeException("Error in explain and execute jssp: " + jssp, e);
        }
    }
}
