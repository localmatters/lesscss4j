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
