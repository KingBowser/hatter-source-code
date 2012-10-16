package me.hatter.tools.commons.xml;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.EntityResolver;

public class XmlParser implements Closeable {

    private InputStream    inputStream;
    private Document       document;
    private EntityResolver entityResolver;

    public XmlParser(InputStream inputStream) {
        this(inputStream, null);
    }

    public XmlParser(InputStream inputStream, EntityResolver entityResolver) {
        this.inputStream = inputStream;
        this.entityResolver = entityResolver;
    }

    public DocumentBuilder getDocumentBuilder() {
        try {
            DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domfac.newDocumentBuilder();
            if (entityResolver != null) {
                builder.setEntityResolver(entityResolver);
            }
            return builder;
        } catch (Exception e) {
            throw resolveException(e);
        }
    }

    public XPath getXpath() {
        XPathFactory xpathf = XPathFactory.newInstance();
        return xpathf.newXPath();
    }

    public Document updateDocument() {
        try {
            return (document = getDocumentBuilder().parse(inputStream));
        } catch (Exception e) {
            throw resolveException(e);
        }
    }

    public Document parseDoument() {
        if (document == null) {
            updateDocument();
        }
        return document;
    }

    public NodeList parseXpathNodes(String xpath) {
        try {
            return (NodeList) getXpath().evaluate(xpath, parseDoument(), XPathConstants.NODESET);
        } catch (Exception e) {
            throw resolveException(e);
        }
    }

    public Node parseXpathNode(String xpath) {
        try {
            return (Node) getXpath().evaluate(xpath, parseDoument(), XPathConstants.NODE);
        } catch (Exception e) {
            throw resolveException(e);
        }
    }

    public NodeList parseXpathNodes(String xpath, Object item) {
        try {
            return (NodeList) getXpath().evaluate(xpath, item, XPathConstants.NODESET);
        } catch (Exception e) {
            throw resolveException(e);
        }
    }

    public Node parseXpathNode(String xpath, Object item) {
        try {
            return (Node) getXpath().evaluate(xpath, item, XPathConstants.NODE);
        } catch (Exception e) {
            throw resolveException(e);
        }
    }

    public NodeList parseXpath(String xpath, QName returnType) {
        try {
            return (NodeList) getXpath().evaluate(xpath, parseDoument(), returnType);
        } catch (Exception e) {
            throw resolveException(e);
        }
    }

    public NodeList parseXpath(String xpath, Object item, QName returnType) {
        try {
            return (NodeList) getXpath().evaluate(xpath, item, returnType);
        } catch (Exception e) {
            throw resolveException(e);
        }
    }

    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public String serializer() {
        parseDoument();
        DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer = domImplLS.createLSSerializer();
        LSOutput lsOutput = domImplLS.createLSOutput();
        lsOutput.setEncoding(document.getInputEncoding());
        Writer writer = new StringWriter();
        lsOutput.setCharacterStream(writer);
        lsSerializer.write(document, lsOutput);
        return writer.toString();
    }

    private RuntimeException resolveException(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(e);
    }
}
