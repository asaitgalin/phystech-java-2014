package ru.phystech.java2.asaitgalin.db.table.impl.xml;

import ru.phystech.java2.asaitgalin.db.table.api.exceptions.ColumnFormatException;
import ru.phystech.java2.asaitgalin.db.table.impl.DatabaseTableTypes;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.text.ParseException;

public class XMLReader implements AutoCloseable {
    private static final String PARSE_ERROR = "xmlreader: incorrect xml input string";
    private XMLStreamReader reader;

    public XMLReader(String data) throws ParseException {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        try {
            reader = xif.createXMLStreamReader(new StringReader(data));
            if (!reader.hasNext()) {
                throw new ParseException("xmlreader: input string is empty", 0);
            }
            if (reader.next() == XMLStreamConstants.START_ELEMENT) {
                if (!reader.getName().getLocalPart().equals("row")) {
                    throw new ParseException(PARSE_ERROR, 0);
                }
            }
        } catch (XMLStreamException e) {
            throw new ParseException("xmlreader: failed to create XMLStreamReader, msg: " + e.getMessage(), 0);
        }
    }

    public Object readValue(Class<?> type) throws ParseException, ColumnFormatException {
        Object retValue;
        try {
            // <col>
            if (reader.next() != XMLStreamConstants.START_ELEMENT || !reader.getName().getLocalPart().equals("col")) {
                throw new ParseException(PARSE_ERROR, 0);
            }
            if (reader.next() == XMLStreamConstants.CHARACTERS) {
                try {
                    retValue = DatabaseTableTypes.parseValueWithClass(reader.getText(), type);
                } catch (NumberFormatException nfe) {
                    throw new ColumnFormatException(nfe);
                }
            } else {
                if (reader.getName().getLocalPart().equals("null")) {
                    // skip null tag
                    if (reader.next() != XMLStreamConstants.END_ELEMENT) {
                        throw new ParseException(PARSE_ERROR, 0);
                    }
                    retValue = null;
                } else {
                    throw new ParseException(PARSE_ERROR, 0);
                }
            }
            // </col>
            if (reader.next() != XMLStreamConstants.END_ELEMENT) {
                throw new ParseException(PARSE_ERROR, 0);
            }

        } catch (XMLStreamException e) {
            throw new ParseException("xmlreader: stream error, msg: " + e.getMessage(), 0);
        }
        return retValue;
    }

    @Override
    public void close() throws ParseException {
        try {
            int nextElement = reader.next();
            // document should end with </row> or EOF
            if (nextElement != XMLStreamConstants.END_ELEMENT && nextElement != XMLStreamConstants.END_DOCUMENT) {
                throw new ParseException(PARSE_ERROR, 0);
            }
            reader.close();
        } catch (XMLStreamException e) {
            throw new ParseException("xmlreader: stream error, msg: " + e.getMessage(), 0);
        }
    }
}
