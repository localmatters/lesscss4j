/**
 * File: DefaultStyleSheetWriter.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 21, 2010
 * Creation Time: 12:23:54 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;

import org.lesscss4j.model.BodyElement;
import org.lesscss4j.model.Media;
import org.lesscss4j.model.StyleSheet;

public class DefaultStyleSheetWriter implements StyleSheetWriter {
    private boolean _prettyPrintEnabled = false;
    private String _defaultEncoding = "UTF-8";
    private String _newline = "\n";

    public String getNewline() {
        return _newline;
    }

    public void setNewline(String newline) {
        _newline = newline;
    }

    public String getDefaultEncoding() {
        return _defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        _defaultEncoding = defaultEncoding;
    }

    public boolean isPrettyPrintEnabled() {
        return _prettyPrintEnabled;
    }

    public void setPrettyPrintEnabled(boolean prettyPrintEnabled) {
        _prettyPrintEnabled = prettyPrintEnabled;
    }

    public void write(OutputStream output, StyleSheet styleSheet) throws IOException {
        String encoding = styleSheet.getCharset();
        if (encoding == null || encoding.length() == 0) {
            encoding = getDefaultEncoding();
        }

        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(output, encoding));
            write(writer, styleSheet);
        }
        finally {
            try {
                if (writer != null) writer.close();
            }
            catch (IOException ex) { /* no op */ }
        }
    }

    protected void writeBreak(Writer writer) throws IOException {
        if (isPrettyPrintEnabled()) {
            writer.write(getNewline());
        }
    }

    protected void writeSpace(Writer writer) throws IOException {
        if (isPrettyPrintEnabled()) {
            writer.write(" ");
        }
    }

    protected void writeSemi(Writer writer, boolean withBreak) throws IOException {
        writer.write(";");
        if (withBreak) {
            writeBreak(writer);
        }
    }

    protected void writeSemi(Writer writer) throws IOException {
        writeSemi(writer, true);
    }

    protected void write(Writer writer, StyleSheet styleSheet) throws IOException {
        if (styleSheet.getCharset() != null && styleSheet.getCharset().length() > 0) {
            writer.write("@charset '");
            writer.write(styleSheet.getCharset());
            writeSemi(writer);
        }

        for (String importElement : styleSheet.getImports()) {
            writer.write("@import ");
            if ("'\"".indexOf(importElement.charAt(0)) < 0 && !importElement.startsWith("url")) {
                writer.write("'");
                writer.write(importElement);
                writer.write("'");
            }
            else {
                writer.write(importElement);
            }
            writeSemi(writer);
        }

        writeBodyElements(writer, styleSheet.getBodyElements());
    }

    protected void writeBodyElements(Writer writer, Collection<BodyElement> bodyElements) throws IOException {
        for (BodyElement element : bodyElements) {
            // todo: ruleset
            if (element instanceof Media) {
                writer.write("@media ");

                boolean first = true;
                for (String medium : ((Media) element).getMediums()) {
                    if (!first) {
                        writer.write(",");
                        writeSpace(writer);
                    }
                    writer.write(medium);
                    first = false;
                }
                writer.write("{");
                writeBreak(writer);
                writeBodyElements(writer, ((Media) element).getBodyElements());
                writer.write("}");
                writeBreak(writer);
            }
            // todo: page
        }
    }

}
