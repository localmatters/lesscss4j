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

package org.localmatters.lesscss4j.compile;

import java.util.LinkedHashMap;
import java.util.Map;

import org.localmatters.lesscss4j.factory.StyleSheetFactory;
import org.localmatters.lesscss4j.model.Declaration;
import org.localmatters.lesscss4j.model.Media;
import org.localmatters.lesscss4j.model.Page;
import org.localmatters.lesscss4j.model.RuleSet;
import org.localmatters.lesscss4j.model.StyleSheet;
import org.localmatters.lesscss4j.model.expression.FunctionExpression;
import org.localmatters.lesscss4j.output.PrettyPrintOptions;
import org.localmatters.lesscss4j.output.StyleSheetWriter;
import org.localmatters.lesscss4j.output.StyleSheetWriterImpl;
import org.localmatters.lesscss4j.parser.LessCssStyleSheetParser;
import org.localmatters.lesscss4j.parser.StyleSheetParser;
import org.localmatters.lesscss4j.parser.StyleSheetResourceLoader;
import org.localmatters.lesscss4j.transform.DeclarationTransformer;
import org.localmatters.lesscss4j.transform.FunctionTransformer;
import org.localmatters.lesscss4j.transform.MediaTransformer;
import org.localmatters.lesscss4j.transform.PageTransformer;
import org.localmatters.lesscss4j.transform.RuleSetTransformer;
import org.localmatters.lesscss4j.transform.StyleSheetTransformer;
import org.localmatters.lesscss4j.transform.Transformer;
import org.localmatters.lesscss4j.transform.function.Darken;
import org.localmatters.lesscss4j.transform.function.Desaturate;
import org.localmatters.lesscss4j.transform.function.Escape;
import org.localmatters.lesscss4j.transform.function.Format;
import org.localmatters.lesscss4j.transform.function.Function;
import org.localmatters.lesscss4j.transform.function.Grayscale;
import org.localmatters.lesscss4j.transform.function.Lighten;
import org.localmatters.lesscss4j.transform.function.Saturate;
import org.localmatters.lesscss4j.transform.function.Spin;
import org.localmatters.lesscss4j.transform.manager.ClassTransformerManager;
import org.localmatters.lesscss4j.transform.manager.TransformerManager;

/**
 * Default implementation of the {@link LessCssCompilerFactory}.  Creates a LessCssCompilerImpl instance and initializes
 * it based on the various properties set on this factory class.  By default, this factory registers the following
 * functions:
 * <p/>
 * <ul>
 * <li>% - printf style function</li>
 * <li>e - escape the given string (i.e. take the contents of the string in quotes and put them
 *         literally in the CSS)</li>
 * </ul>
 */
public class DefaultLessCssCompilerFactory implements LessCssCompilerFactory {
    private String _defaultEncoding;
    private Integer _initialBufferSize;
    private Integer _readBufferSize;
    private Boolean _prettyPrintEnabled;
    private PrettyPrintOptions _prettyPrintOptions;
    private StyleSheetResourceLoader _styleSheetResourceLoader;
    private TransformerManager _transformerManager;
    private Map<String, Function> _functions;

    public DefaultLessCssCompilerFactory() {
        addFunction("%", new Format());
        addFunction("e", new Escape());
        addFunction("lighten", new Lighten());
        addFunction("darken", new Darken());
        addFunction("saturate", new Saturate());
        addFunction("desaturate", new Desaturate());

        Grayscale grayscale = new Grayscale();
        addFunction("grayscale", grayscale);
        addFunction("greyscale", grayscale);

        addFunction("spin", new Spin());
    }

    /**
     * Specify a map of functions to use during compilation.
     * <p/>
     * <strong>Note:</strong> Functions added to this factory are only applied if the default {@link TransformerManager}
     * is used.  If a custom {@link TransformerManager} is used, any functions registered with this factory instance are
     * ignored.
     *
     * @param functions Map of function name to {@link Function} instances.
     */
    public void setFunctions(Map<String, Function> functions) {
        _functions = functions;
    }

    /**
     * Add a function to the compiler configuration.
     * <p/>
     * <strong>Note:</strong> Functions added to this factory are only applied if the default {@link TransformerManager}
     * is used.  If a custom {@link TransformerManager} is used, any functions registered with this factory instance are
     * ignored.
     *
     * @param name     Name of the function
     * @param function The function instance to associate with the given name.
     */
    public void addFunction(String name, Function function) {
        if (_functions == null) {
            _functions = new LinkedHashMap<String, Function>();
        }
        _functions.put(name, function);
    }

    /**
     * Sets a custom {@link TransformerManager} to use during compilation.  Setting this value to non-<code>null</code>
     * will cause any {@link Function} mappings defined via {@link #setFunctions} or {@link #addFunction} to be
     * ignored.
     *
     * @param transformerManager The custom {@link TransformerManager} to use.
     */
    public void setTransformerManager(TransformerManager transformerManager) {
        _transformerManager = transformerManager;
    }

    /**
     * Sets a custom {@link StyleSheetResourceLoader} to use when parsing stylesheets.  If not specified, a default
     * resource loader (i.e. {@link org.localmatters.lesscss4j.parser.DefaultStyleSheetResourceLoader
     * DefaultStyleSheetResourceLoader} will be used.
     *
     * @param styleSheetResourceLoader The custom resource loader to use.
     */
    public void setStyleSheetResourceLoader(StyleSheetResourceLoader styleSheetResourceLoader) {
        _styleSheetResourceLoader = styleSheetResourceLoader;
    }

    /**
     * Sets the default encoding to use if the stylesheet doesn't provide one via the @charset directive.  This will
     * also be the encoding to use when writing out the compiled CSS.  If a stylesheet provides a @charset directive,
     * that encoding is also used when writing the CSS file.
     *
     * @param defaultEncoding The default encoding to use.  UTF-8 by default.
     */
    public void setDefaultEncoding(String defaultEncoding) {
        _defaultEncoding = defaultEncoding;
    }

    /**
     * Sets the size of the initial buffer created when reading stylesheets.  Defaults to 1024.  If the stylesheet is
     * larger than this value, the buffer size is doubled until the entire stylesheet can be read into memory.
     *
     * @param initialBufferSize The initial size of the buffer.
     */
    public void setInitialBufferSize(Integer initialBufferSize) {
        _initialBufferSize = initialBufferSize;
    }

    /**
     * Sets the number of bytes to read at a time from disk when populating the buffer.  Defaults to 1024.
     *
     * @param readBufferSize The number of bytes to read at a time.
     */
    public void setReadBufferSize(Integer readBufferSize) {
        _readBufferSize = readBufferSize;
    }

    /**
     * Set whether the resulting CSS is compressed or output in a human readable form.
     *
     * @param prettyPrintEnabled Set to true if pretty printing should be enabled.  If null, the default of the
     */
    public void setPrettyPrintEnabled(Boolean prettyPrintEnabled) {
        _prettyPrintEnabled = prettyPrintEnabled;
    }

    /**
     * Alias for {@link #setPrettyPrintEnabled setPrettyPrintEnabled(false)}
     *
     * @param compressionEnabled If non-null and set to true, enable compression (i.e. disable pretty printing)
     */
    public void setCompressionEnabled(Boolean compressionEnabled) {
        setPrettyPrintEnabled(compressionEnabled == null ? null : !compressionEnabled);
    }

    /**
     * Sets the options that control how pretty-printed CSS is generated.
     *
     * @param prettyPrintOptions Options for pretty-printing CSS.
     */
    public void setPrettyPrintOptions(PrettyPrintOptions prettyPrintOptions) {
        _prettyPrintOptions = prettyPrintOptions;
    }

    /**
     * Creates the initialized compiler
     */
    public LessCssCompiler create() {
        LessCssCompilerImpl compiler = new LessCssCompilerImpl();
        initializeCompiler(compiler);
        return compiler;
    }

    /**
     * Initialize the given compiler to it's initial state.
     *
     * @param compiler The compiler to initialize.
     */
    protected void initializeCompiler(LessCssCompilerImpl compiler) {
        initializeParser(compiler.getStyleSheetParser());
        initializeWriter(compiler.getStyleSheetWriter());
        TransformerManager transformerManager = _transformerManager;
        if (transformerManager == null) {
            transformerManager = createDefaultTransformManager();
        }
        compiler.setTransformerManager(transformerManager);
    }


    /**
     * Creates the default {@link TransformerManager} if a custom one has not been provided.
     *
     * @return The default {@link TransformerManager} instance.
     */
    protected TransformerManager createDefaultTransformManager() {
        ClassTransformerManager transformerManager = new ClassTransformerManager();
        transformerManager.setClassTransformerMap(createDefaultClassTransformerMap());
        return transformerManager;
    }

    /**
     * Create the default mapping between {@link org.localmatters.lesscss4j.model.AbstractElement AbstractElement} subclass and
     * {@link Transformer}.
     */
    protected Map<Class, Transformer> createDefaultClassTransformerMap() {
        Map<Class, Transformer> transformerMap = new LinkedHashMap<Class, Transformer>();
        transformerMap.put(Declaration.class, new DeclarationTransformer());
        transformerMap.put(RuleSet.class, new RuleSetTransformer());
        transformerMap.put(Page.class, new PageTransformer());
        transformerMap.put(Media.class, new MediaTransformer());
        transformerMap.put(StyleSheet.class, new StyleSheetTransformer());
        transformerMap.put(FunctionExpression.class, createFunctionTransformer());
        return transformerMap;
    }

    /**
     * Creates the default {@link FunctionTransformer} used to evaluate {@link FunctionExpression} instances.
     */
    protected Transformer createFunctionTransformer() {
        FunctionTransformer transformer = new FunctionTransformer();
        transformer.setFunctionMap(_functions);
        return transformer;
    }

    protected void initializeWriter(StyleSheetWriter styleSheetWriter) {
        StyleSheetWriterImpl writer = (StyleSheetWriterImpl) styleSheetWriter;
        if (_prettyPrintEnabled != null) {
            writer.setPrettyPrintEnabled(_prettyPrintEnabled);
        }
        if (_prettyPrintOptions != null) {
            writer.setPrettyPrintOptions(_prettyPrintOptions);
        }
        if (_defaultEncoding != null) {
            writer.setDefaultEncoding(_defaultEncoding);
        }
    }

    protected void initializeParser(StyleSheetParser styleSheetParser) {
        LessCssStyleSheetParser parser = (LessCssStyleSheetParser) styleSheetParser;
        if (_defaultEncoding != null) {
            parser.setDefaultEncoding(_defaultEncoding);
        }
        if (_initialBufferSize != null) {
            parser.setInitialBufferSize(_initialBufferSize);
        }
        if (_readBufferSize != null) {
            parser.setInitialBufferSize(_readBufferSize);
        }
        if (_styleSheetResourceLoader != null) {
            ((StyleSheetFactory) parser.getStyleSheetFactory()).setStyleSheetResourceLoader(_styleSheetResourceLoader);
        }
    }
}
