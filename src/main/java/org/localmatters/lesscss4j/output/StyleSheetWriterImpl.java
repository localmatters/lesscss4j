/*
   Copyright 2010-present Local Matters, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.localmatters.lesscss4j.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.localmatters.lesscss4j.error.ErrorHandler;
import org.localmatters.lesscss4j.model.BodyElement;
import org.localmatters.lesscss4j.model.Declaration;
import org.localmatters.lesscss4j.model.DeclarationElement;
import org.localmatters.lesscss4j.model.Keyframes;
import org.localmatters.lesscss4j.model.Media;
import org.localmatters.lesscss4j.model.Page;
import org.localmatters.lesscss4j.model.RuleSet;
import org.localmatters.lesscss4j.model.Selector;
import org.localmatters.lesscss4j.model.StyleSheet;

// todo: It might make sense to break this up into separate writers for each type of element in the stylesheet
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

    public void write(OutputStream output, StyleSheet styleSheet, ErrorHandler errorHandler) throws IOException {
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
            IOUtils.closeQuietly(writer);
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
        if (isPrettyPrintEnabled()) {
            for (int idx = 0; idx < level; idx++) {
                for (int jdx = 0; jdx < getPrettyPrintOptions().getIndentSize(); jdx++) {
                    writer.write(' ');
                }
            }
        }
    }

    protected void write(Writer writer, StyleSheet styleSheet) throws IOException {
        writeCharset(writer, styleSheet);
        writeImports(writer, styleSheet);

        writeBodyElements(writer, styleSheet.getBodyElements(), 0);
    }

    private void writeImports(Writer writer, StyleSheet styleSheet) throws IOException {
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
    }

    private void writeCharset(Writer writer, StyleSheet styleSheet) throws IOException {
        if (styleSheet.getCharset() != null && styleSheet.getCharset().length() > 0) {
            writer.write("@charset '");
            writer.write(styleSheet.getCharset());
            writeSemi(writer);
        }
    }

    protected void writeBodyElements(Writer writer,
                                     List<BodyElement> bodyElements,
                                     int indent) throws IOException {
        if (bodyElements == null) return;
        
        for (int i = 0, bodyElementsSize = bodyElements.size(); i < bodyElementsSize; i++) {
            BodyElement element = bodyElements.get(i);
            if (i > 0 && isPrettyPrintEnabled() && getPrettyPrintOptions().isLineBetweenRuleSets()) {
                writeBreak(writer);
            }
            writeIndent(writer, indent);
            if (element instanceof Media) {
                writeMedia(writer, (Media) element, indent);
            }
            else if (element instanceof Page) {
                writePage(writer, (Page)element, indent);
            }
            else if (element instanceof Keyframes) {
                writeKeyframes(writer, (Keyframes)element, indent);
            }
            else if (element instanceof RuleSet) {
                writeRuleSet(writer, (RuleSet) element, indent);
            }
        }
    }

    protected void writePage(Writer writer, Page page, int indent) throws IOException {
        List<DeclarationElement> declarations = page.getDeclarations();
        if (declarations == null || declarations.size() == 0) {
            return;
        }

        writer.write("@page");

        if (page.getPseudoPage() != null) {
            writer.write(" :");
            writer.write(page.getPseudoPage());
        }

        writeOpeningBrace(writer, indent, declarations);
        writeBreak(writer, indent);

        writeDeclarations(writer, declarations, indent);

        writeDeclarationBraceSpace(writer, declarations);

        if (!isOneLineDeclarationList(declarations)) {
            writeIndent(writer, indent);
        }
        writeClosingBrace(writer, indent);

    }

    protected void writeMedia(Writer writer, Media media, int indent) throws IOException {

        writer.write("@media ");


        boolean first = true;
        for (String medium : media.getMediums()) {
            if (!first) {
                writeSeparator(writer, ",");
            }
            writer.write(medium);
            first = false;
        }
        writeOpeningBrace(writer, indent, null);
        writeBreak(writer, indent);

        writeBodyElements(writer, media.getBodyElements(), indent + 1);

        writeClosingBrace(writer, indent);
    }

    protected void writeKeyframes(Writer writer, Keyframes media, int indent) throws IOException {

        writer.write(media.getName());


        writeOpeningBrace(writer, indent, null);
        writeBreak(writer, indent);

        writeBodyElements(writer, media.getBodyElements(), indent + 1);

        writeClosingBrace(writer, indent);
    }

    protected void writeRuleSet(Writer writer, RuleSet ruleSet, int indent) throws IOException {
        // Don't write rule sets with empty bodies
        List<DeclarationElement> declarations = ruleSet.getDeclarations();
        if (declarations == null || declarations.size() == 0) {
            return;
        }

        if (ruleSet.getArguments().size() > 0) {
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

        writeDeclarations(writer, declarations, indent);

        writeDeclarationBraceSpace(writer, declarations);

        if (!isOneLineDeclarationList(declarations)) {
            writeIndent(writer, indent);
        }
        writeClosingBrace(writer, 0);

    }

    private void writeDeclarations(Writer writer,
                                   List<DeclarationElement> declarations,
                                   int indent) throws IOException {
        boolean oneLineDeclarationList = isOneLineDeclarationList(declarations);

        boolean first = true;
        for (DeclarationElement declaration : declarations) {
            if (declaration instanceof Declaration) {
                if (!first) {
                    writeBreak(writer);
                }

                int declarationIndent = indent + 1;
                if (oneLineDeclarationList) {
                    declarationIndent = 0;
                }
                writeDeclaration(writer, (Declaration) declaration, declarationIndent);

                first = false;
            }
        }
    }

    protected void writeOpeningBrace(Writer writer, int indent, List<DeclarationElement> declarations) throws IOException {
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

    protected void writeClosingBrace(Writer writer, int indent) throws IOException {
        writer.write("}");
        writeBreak(writer, indent);
    }

    protected void writeDeclaration(Writer writer, Declaration declaration, int indent) throws IOException {
        writeIndent(writer, indent);
        writer.write(declaration.getProperty());
        writer.write(':');
        writeSpace(writer);
        for (Object value : declaration.getValues()) {
            writer.write(value.toString());
        }
        if (declaration.isImportant()) {
            writeSpace(writer);
            writer.write("!important");
        }
        writeSemi(writer, false);
    }

    protected void writeDeclarationBraceSpace(Writer writer, List<DeclarationElement> declarations) throws IOException {
        if (isOneLineDeclarationList(declarations)) {
            writeSpace(writer);
        }
        else {
            writeBreak(writer);
        }
    }

    private boolean isOneLineDeclarationList(List<DeclarationElement> declarations) {
        return declarations != null &&
               isPrettyPrintEnabled() &&
               getPrettyPrintOptions().isSingleDeclarationOnOneLine() &&
               declarations.size() <= 1;
    }
}
