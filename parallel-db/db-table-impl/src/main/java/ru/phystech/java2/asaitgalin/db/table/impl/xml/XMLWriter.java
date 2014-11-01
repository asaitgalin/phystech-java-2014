package ru.phystech.java2.asaitgalin.db.table.impl.xml;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;

public class XMLWriter implements AutoCloseable {
    StringWriter stringWriter;
    XMLStreamWriter writer;

    public XMLWriter() {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        stringWriter = new StringWriter();
        try {
            writer = factory.createXMLStreamWriter(stringWriter);
            writer.writeStartElement("row");
        } catch (XMLStreamException e) {
            throw new RuntimeException("xmlwriter: failed to create XMLStreamWriter", e);
        }
    }

    public void writeValue(Object value) {
        try {
            if (value == null) {
                writer.writeStartElement("null");
                writer.writeEndElement();
            } else {
                writer.writeStartElement("col");
                writer.writeCharacters(value.toString());
                writer.writeEndElement();
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException("xmlwriter: failed to write value", e);
        }
    }

    @Override
    public void close() {
        try {
            writer.writeEndElement();
            writer.flush();
        } catch (XMLStreamException e) {
            throw new RuntimeException("xmlwriter: failed to finalize file", e);
        }
    }

    @Override
    public String toString() {
        return stringWriter.toString();
    }
}
