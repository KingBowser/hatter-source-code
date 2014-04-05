package me.hatter.tools.resourceproxy.jsspexec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.commons.resource.impl.FileResource;
import me.hatter.tools.resourceproxy.jsspexec.util.BufferWriter;

public class JsspExecutorTest {

    public static void main(String[] args) throws IOException {
        JsspExecutor.initJsspWork();
        Map<String, Object> context = new HashMap<String, Object>();
        BufferWriter bw = new BufferWriter();
        File f = File.createTempFile("test", "jssp");
        f.deleteOnExit();
        FileWriter fw = new FileWriter(f);
        fw.write("<%=Array.prototype.forEach%>\n");
        fw.write("<%var a=[1,2,3];a.forEach(function(x,y,z){out.write(x+' ---- '+y+' ---- '+z+'\\n');});%>");
        // fw.write("<%var ks = []; for (var k in Array.prototype){ks.push(k);}%><%=ks.join(',')%>\n");
        fw.write("--END--\n");
        fw.flush();
        fw.close();

        JsspExecutor.executeJssp(new FileResource(f), context, null, null, bw);

        System.out.println(bw.getBufferedString());
    }
}
