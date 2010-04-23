/**
 * File: StyleSheetWriterImpl.java
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
import java.util.List;

import org.lesscss4j.model.expression.Expression;
import org.lesscss4j.model.BodyElement;
import org.lesscss4j.model.Declaration;
import org.lesscss4j.model.Media;
import org.lesscss4j.model.RuleSet;
import org.lesscss4j.model.Selector;
import org.lesscss4j.model.StyleSheet;

public class StyleSheetWriterImpl implements StyleSheetWriter {
    private boolean _prettyPrintEnabled = false;
    private String _defaultEncoding = "UTF-8";
    private String _newline = "\n";
    private PrettyPrintOptions _prettyPrintOptions;

    public PrettyPrintOptions getPrettyPrintOptions() {
        if (_prettyPrintOptions == null) {
            _prettyPrintOptions = new PrettyPrintOptions();
        }
        return _prettyPrintOptions;
    }

    public void setPrettyPrintOptions(PrettyPrintOptions prettyPrintOptions) {
        _prettyPrintOptions = prettyPrintOptions;
    }

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

    protected void writeBreak(Writer writer, int indent) throws IOException {
        if (isPrettyPrintEnabled()) {
            writer.write(getNewline());
            if (indent > 0) {
                writeIndent(writer, indent);
            }
        }
    }

    protected void writeBreak(Writer writer) throws IOException {
        writeBreak(writer, 0);
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

    private void writeSeparator(Writer writer, String separator) throws IOException {
        writer.write(separator);
        writeSpace(writer);
    }

    protected void writeIndent(Writer writer, int level) throws IOException {
        for (int idx = 0; idx < level; idx++) {
            for (int jdx = 0; jdx < getPrettyPrintOptions().getIndentSize(); jdx++) {
                writer.write(' ');
            }
        }
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

        writeBodyElements(writer, styleSheet.getBodyElements(), 0);
    }

    protected void writeBodyElements(Writer writer, List<BodyElement> bodyElements, int indent) throws IOException {
        for (int i = 0, bodyElementsSize = bodyElements.size(); i < bodyElementsSize; i++) {
            BodyElement element = bodyElements.get(i);
            if (i > 0 && isPrettyPrintEnabled() && getPrettyPrintOptions().isLineBetweenRuleSets()) {
                writeBreak(writer);
            }
            writeIndent(writer, indent);
            if (element instanceof Media) {
                writer.write("@media ");

                boolean first = true;
                for (String medium : ((Media) element).getMediums()) {
                    if (!first) {
                        writeSeparator(writer, ",");
                    }
                    writer.write(medium);
                    first = false;
                }
                writeOpeningBrace(writer, indent, null);
                writeBreak(writer, indent);
                writeBodyElements(writer, ((Media) element).getBodyElements(), indent + 1);
                writer.write("}");
                writeBreak(writer, indent);
            }
            else if (element instanceof RuleSet) {
                writeRuleSet(writer, (RuleSet) element, indent);
            }
            // todo: page
        }
    }


    protected void writeRuleSet(Writer writer, RuleSet ruleSet, int indent) throws IOException {
        // Don't write rule sets with empty bodies
        List<Declaration> declarations = ruleSet.getDeclarations();
        if (declarations == null || declarations.size() == 0) {
            return;
        }

        for (int idx = 0, selectorsSize = ruleSet.getSelectors().size(); idx < selectorsSize; idx++) {
            Selector selector = ruleSet.getSelectors().get(idx);
            if (idx > 0) {
                writeSeparator(writer, ",");
            }
            writer.write(selector.getText());
        }

        writeOpeningBrace(writer, indent, declarations);
        writeDeclarationBraceSpace(writer, declarations);

        boolean oneLineDeclarationList = isOneLineDeclarationList(declarations);
        for (int idx = 0, declarationSize = declarations.size(); idx < declarationSize; idx++) {
            if (idx > 0) {
                writeBreak(writer);
            }
            Declaration declaration = declarations.get(idx);

            int declarationIndent = indent + 1;
            if (oneLineDeclarationList) {
                declarationIndent = 0;
            }
            writeDeclaration(writer, declaration, declarationIndent);
        }

        writeDeclarationBraceSpace(writer, declarations);

        if (!oneLineDeclarationList) {
            writeIndent(writer, indent);
        }
        writer.write("}");
        writeBreak(writer);
    }

    protected void writeOpeningBrace(Writer writer, int indent, List<Declaration> declarations) throws IOException {
        if (isPrettyPrintEnabled() &&
            getPrettyPrintOptions().isOpeningBraceOnNewLine() &&
            !isOneLineDeclarationList(declarations)) {
            writeBreak(writer, indent);
        }
        else {
            writeSpace(writer);
        }
        writer.write('{');
    }

    protected void writeDeclaration(Writer writer, Declaration declaration, int indent) throws IOException {
        writeIndent(writer, indent);
        if (declaration.isStar()) {
            writer.write('*');
        }
        writer.write(declaration.getProperty());
        writer.write(':');
        writeSpace(writer);
        for (Object value : declaration.getValues()) {
            if (value instanceof Expression) {
                value = ((Expression)value).evaluate(null);
            }
            writer.write(value.toString());
        }
        if (declaration.isImportant()) {
            writeSpace(writer);
            writer.write("!important");
        }
        writeSemi(writer, false);
    }

    protected void writeDeclarationBraceSpace(Writer writer, List<Declaration> declarations) throws IOException {
        if (isOneLineDeclarationList(declarations)) {
            writeSpace(writer);
        }
        else {
            writeBreak(writer);
        }
    }

    private boolean isOneLineDeclarationList(List<Declaration> declarations) {
        return declarations != null &&
               isPrettyPrintEnabled() &&
               getPrettyPrintOptions().isSingleDeclarationOnOneLine() &&
               declarations.size() <= 1;
    }
}
