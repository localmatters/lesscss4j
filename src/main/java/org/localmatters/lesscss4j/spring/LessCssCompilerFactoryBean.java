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
package org.localmatters.lesscss4j.spring;

import org.localmatters.lesscss4j.compile.DefaultLessCssCompilerFactory;
import org.localmatters.lesscss4j.compile.LessCssCompiler;
import org.localmatters.lesscss4j.compile.LessCssCompilerImpl;
import org.localmatters.lesscss4j.output.PrettyPrintOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * FactoryBean that makes it easier to configure the LessCssCompiler without having to redefine the parser and writer
 * instances created by default by the compiler class.
 */
public class LessCssCompilerFactoryBean extends DefaultLessCssCompilerFactory implements FactoryBean, InitializingBean {
    private LessCssCompilerImpl _compilerInstance;

    public void afterPropertiesSet() throws Exception {
        getObject();
    }

    public Class getObjectType() {
        return LessCssCompiler.class;
    }

    @Override
    public void setDefaultEncoding(String defaultEncoding) {
        super.setDefaultEncoding(defaultEncoding);
        maybeReinitialize();
    }

    @Override
    public void setInitialBufferSize(Integer initialBufferSize) {
        super.setInitialBufferSize(initialBufferSize);
        maybeReinitialize();
    }

    @Override
    public void setReadBufferSize(Integer readBufferSize) {
        super.setReadBufferSize(readBufferSize);
        maybeReinitialize();
    }

    @Override
    public void setPrettyPrintEnabled(Boolean prettyPrintEnabled) {
        super.setPrettyPrintEnabled(prettyPrintEnabled);
        maybeReinitialize();
    }

    @Override
    public void setPrettyPrintOptions(PrettyPrintOptions prettyPrintOptions) {
        super.setPrettyPrintOptions(prettyPrintOptions);
        maybeReinitialize();
    }

    @Override
    public void setCompressionEnabled(Boolean compressionEnabled) {
        super.setCompressionEnabled(compressionEnabled);
        maybeReinitialize();
    }

    public Object getObject() throws Exception {
        if (_compilerInstance == null) {
            _compilerInstance = (LessCssCompilerImpl) create();
        }
        return _compilerInstance;
    }

    public boolean isSingleton() {
        return true;
    }

    private void maybeReinitialize() {
        if (isSingleton() && _compilerInstance != null) {
            initializeCompiler(_compilerInstance);
        }
    }


}
