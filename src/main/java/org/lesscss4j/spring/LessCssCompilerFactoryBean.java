/**
 * File: LessCssCompilerFactoryBean.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: May 18, 2010
 * Creation Time: 10:47:16 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.spring;

import org.lesscss4j.compile.LessCssCompiler;
import org.lesscss4j.compile.LessCssCompilerImpl;
import org.lesscss4j.factory.StyleSheetFactory;
import org.lesscss4j.output.PrettyPrintOptions;
import org.lesscss4j.output.StyleSheetWriter;
import org.lesscss4j.output.StyleSheetWriterImpl;
import org.lesscss4j.parser.LessCssStyleSheetParser;
import org.lesscss4j.parser.StyleSheetParser;
import org.lesscss4j.parser.StyleSheetResourceLoader;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * FactoryBean that makes it easier to configure the LessCssCompiler without having to redefine the parser and writer
 * instances created by default by the compiler class.
 */
public class LessCssCompilerFactoryBean extends AbstractFactoryBean {
    private String _defaultEncoding;
    private Integer _initialBufferSize;
    private Integer _readBufferSize;
    private Boolean _prettyPrintEnabled;
    private PrettyPrintOptions _prettyPrintOptions;
    private StyleSheetResourceLoader _styleSheetResourceLoader;

    public StyleSheetResourceLoader getStyleSheetResourceLoader() {
        return _styleSheetResourceLoader;
    }

    public void setStyleSheetResourceLoader(StyleSheetResourceLoader styleSheetResourceLoader) {
        _styleSheetResourceLoader = styleSheetResourceLoader;
    }

    public String getDefaultEncoding() {
        return _defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        _defaultEncoding = defaultEncoding;
    }

    public Integer getInitialBufferSize() {
        return _initialBufferSize;
    }

    public void setInitialBufferSize(Integer initialBufferSize) {
        _initialBufferSize = initialBufferSize;
    }

    public Integer getReadBufferSize() {
        return _readBufferSize;
    }

    public void setReadBufferSize(Integer readBufferSize) {
        _readBufferSize = readBufferSize;
    }

    public Boolean getPrettyPrintEnabled() {
        return _prettyPrintEnabled;
    }

    public void setPrettyPrintEnabled(Boolean prettyPrintEnabled) {
        _prettyPrintEnabled = prettyPrintEnabled;
    }

    public void setCompressionEnabled(Boolean compressionEnabled) {
        setPrettyPrintEnabled(compressionEnabled == null ? null : !compressionEnabled);
    }

    public Boolean isCompressionEnabled() {
        return getPrettyPrintEnabled() == null ? null : !getPrettyPrintEnabled();
    }

    public PrettyPrintOptions getPrettyPrintOptions() {
        return _prettyPrintOptions;
    }

    public void setPrettyPrintOptions(PrettyPrintOptions prettyPrintOptions) {
        _prettyPrintOptions = prettyPrintOptions;
    }

    @Override
    public Class getObjectType() {
        return LessCssCompiler.class;
    }

    @Override
    protected Object createInstance() throws Exception {
        LessCssCompilerImpl compiler = new LessCssCompilerImpl();
        initializeParser(compiler.getStyleSheetParser());
        initializeWriter(compiler.getStyleSheetWriter());
        return compiler;
    }

    protected void initializeWriter(StyleSheetWriter styleSheetWriter) {
        StyleSheetWriterImpl writer = (StyleSheetWriterImpl) styleSheetWriter;
        if (getPrettyPrintEnabled() != null) {
            writer.setPrettyPrintEnabled(getPrettyPrintEnabled());
        }
        if (getPrettyPrintOptions() != null) {
            writer.setPrettyPrintOptions(getPrettyPrintOptions());
        }
        if (getDefaultEncoding() != null) {
            writer.setDefaultEncoding(getDefaultEncoding());
        }
    }

    protected void initializeParser(StyleSheetParser styleSheetParser) {
        LessCssStyleSheetParser parser = (LessCssStyleSheetParser) styleSheetParser;
        if (getDefaultEncoding() != null) {
            parser.setDefaultEncoding(getDefaultEncoding());
        }
        if (getInitialBufferSize() != null) {
            parser.setInitialBufferSize(getInitialBufferSize());
        }
        if (getInitialBufferSize() != null) {
            parser.setInitialBufferSize(getInitialBufferSize());
        }
        if (getStyleSheetResourceLoader() != null) {
            ((StyleSheetFactory) parser.getStyleSheetFactory())
                .setStyleSheetResourceLoader(getStyleSheetResourceLoader());
        }
    }
}
