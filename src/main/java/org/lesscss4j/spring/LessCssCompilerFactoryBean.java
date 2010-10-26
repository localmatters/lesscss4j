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
import org.lesscss4j.transform.manager.TransformerManager;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * FactoryBean that makes it easier to configure the LessCssCompiler without having to redefine the parser and writer
 * instances created by default by the compiler class.
 */
public class LessCssCompilerFactoryBean implements FactoryBean, InitializingBean {
    private String _defaultEncoding;
    private Integer _initialBufferSize;
    private Integer _readBufferSize;
    private Boolean _prettyPrintEnabled;
    private PrettyPrintOptions _prettyPrintOptions;
    private StyleSheetResourceLoader _styleSheetResourceLoader;
    private TransformerManager _transformerManager;
    private LessCssCompilerImpl _compilerInstance;

    public TransformerManager getTransformerManager() {
        return _transformerManager;
    }

    public void setTransformerManager(TransformerManager transformerManager) {
        _transformerManager = transformerManager;
    }

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
        maybeReinitialize();
    }

    public Integer getInitialBufferSize() {
        return _initialBufferSize;
    }

    public void setInitialBufferSize(Integer initialBufferSize) {
        _initialBufferSize = initialBufferSize;
        maybeReinitialize();
    }

    public Integer getReadBufferSize() {
        return _readBufferSize;
    }

    public void setReadBufferSize(Integer readBufferSize) {
        _readBufferSize = readBufferSize;
        maybeReinitialize();
    }

    public Boolean getPrettyPrintEnabled() {
        return _prettyPrintEnabled;
    }

    public void setPrettyPrintEnabled(Boolean prettyPrintEnabled) {
        _prettyPrintEnabled = prettyPrintEnabled;
        maybeReinitialize();
    }

    public void setCompressionEnabled(Boolean compressionEnabled) {
        setPrettyPrintEnabled(compressionEnabled == null ? null : !compressionEnabled);
        maybeReinitialize();
    }

    public Boolean isCompressionEnabled() {
        return getPrettyPrintEnabled() == null ? null : !getPrettyPrintEnabled();
    }

    public PrettyPrintOptions getPrettyPrintOptions() {
        return _prettyPrintOptions;
    }

    public void setPrettyPrintOptions(PrettyPrintOptions prettyPrintOptions) {
        _prettyPrintOptions = prettyPrintOptions;
        maybeReinitialize();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        getObject();
    }

    @Override
    public Class getObjectType() {
        return LessCssCompiler.class;
    }


    @Override
    public Object getObject() throws Exception {
        if (_compilerInstance == null) {
            _compilerInstance = (LessCssCompilerImpl) createInstance();
        }
        return _compilerInstance;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    protected Object createInstance() throws Exception {
        LessCssCompilerImpl compiler = new LessCssCompilerImpl();
        initializeCompiler(compiler);
        return compiler;
    }

    private void maybeReinitialize() {
        if (isSingleton() && _compilerInstance != null) {
            initializeCompiler(_compilerInstance);
        }
    }

    private void initializeCompiler(LessCssCompilerImpl compiler) {
        initializeParser(compiler.getStyleSheetParser());
        initializeWriter(compiler.getStyleSheetWriter());
        initializeTransformerManager(compiler);
    }

    protected void initializeTransformerManager(LessCssCompilerImpl compiler) {
        TransformerManager transformerManager = getTransformerManager();
        if (transformerManager != null) {
            compiler.setTransformerManager(transformerManager);
        }
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
