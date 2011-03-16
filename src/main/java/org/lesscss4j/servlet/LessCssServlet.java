/**
 * File: LessCssServlet.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 29, 2010
 * Creation Time: 12:16:55 PM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.lesscss4j.compile.DefaultLessCssCompilerFactory;
import org.lesscss4j.compile.LessCssCompiler;
import org.lesscss4j.parser.DefaultStyleSheetResourceLoader;
import org.lesscss4j.parser.StyleSheetResourceLoader;
import org.lesscss4j.util.Hex;

public class LessCssServlet extends HttpServlet {
    public static final long CACHE_FOREVER = -1;
    public static final String METHOD_HEAD = "HEAD";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String IF_MOD_SINCE = "If-Modified-Since";
    public static final String EXPIRES = "Expires";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String MAX_AGE = "max-age";
    public static final String IF_NONE_MATCH = "If-None-Match";

    /** Init parameter name for the length of time to cache entries before refreshing */
    public static final String CACHE_MILLISECONDS_PARAM_NAME = "cacheMilliseconds";

    /** Init parameter name for the length of time to cache the result in browsers */
    public static final String HTTP_CACHE_MILLISECONDS_PARAM_NAME = "httpCacheMilliseconds";

    /** Init parameter name indicating whether to pretty print the results */
    public static final String PRETTY_PRINT_PARAM_NAME = "prettyPrint";

    /** Request parameter name specifying that the cache should be cleared */
    public static final String CLEAR_CACHE = "clearCache";

    public static final String USE_ETAG = "etagEnabled";

    private ConcurrentMap<String, CacheEntry> _cache = new ConcurrentHashMap<String, CacheEntry>();

    /** Amount of time to wait before checking if a LESS file needs to be recompiled */
    private long _cacheMillis = CACHE_FOREVER;

    /** Amount of time the browser should cache the CSS result */
    private long _httpCacheMillis = 15 * 60 * 1000; // 15 min

    /** Should the compiled CSS be cached? */
    private boolean _cacheEnabled = true;

    /** Use ETags in addition to cache times */
    private boolean _useETag = true;

    /** The compiler to use */
    private LessCssCompiler _lessCompiler;

    private StyleSheetResourceLoader _styleSheetResourceLoader = new DefaultStyleSheetResourceLoader();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Cache entry time
        Integer cacheMillis = getInitParameterInteger(config, CACHE_MILLISECONDS_PARAM_NAME);
        if (cacheMillis != null) {
            setCacheMillis(cacheMillis);
        }

        // Browser cache entry time
        Integer httpCacheMillis = getInitParameterInteger(config, HTTP_CACHE_MILLISECONDS_PARAM_NAME);
        if (httpCacheMillis != null) {
            setHttpCacheMillis(httpCacheMillis);
        }

        Boolean etagEnabled = getInitParameterBoolean(config, USE_ETAG);
        if (etagEnabled != null && !etagEnabled) {
            _useETag = false;
        }

        DefaultLessCssCompilerFactory factory = new DefaultLessCssCompilerFactory();

        Boolean prettyPrint = getInitParameterBoolean(config, PRETTY_PRINT_PARAM_NAME);
        if (prettyPrint != null) {
            factory.setPrettyPrintEnabled(prettyPrint);
        }

        _lessCompiler = factory.create();
    }

    protected Boolean getInitParameterBoolean(ServletConfig config, String name) {
        String value = config.getInitParameter(name);
        if (value != null) {
            return value.equals("true");
        }
        return null;
    }

    protected Integer getInitParameterInteger(ServletConfig config, String name) throws ServletException {
        String value = config.getInitParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException ex) {
                throw new ServletException("Invalid " + name + " value:" + value, ex);
            }
        }
        return null;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String clearCache = request.getParameter(CLEAR_CACHE);
        if (clearCache != null && clearCache.equals("true")) {
            clearCache();
        }

        final String resource = getRequestedResource(request);
        if (resource == null || resource.trim().length() == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        final long now = getTime();

        CacheEntry cacheEntry = getCacheEntry(resource, now);

        if (cacheEntry.getValue() != null) {
            boolean isHeadRequest = METHOD_HEAD.equalsIgnoreCase(request.getMethod());
            if (isHeadRequest || isModified(request, cacheEntry)) {
                response.addDateHeader(LAST_MODIFIED, cacheEntry.getLastUpdate());
                response.addDateHeader(EXPIRES, now + getHttpCacheMillis());
                response.addHeader(CACHE_CONTROL, String.format("%s=%s", MAX_AGE, getHttpCacheMillis() / 1000));
                response.setContentType("text/css; charset=UTF-8");
                response.setContentLength(cacheEntry.getValue().length);

                if (!isHeadRequest) {
                    IOUtils.write(cacheEntry.getValue(), response.getOutputStream());
                }
            }
            else {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected boolean isModified(HttpServletRequest request, CacheEntry cacheEntry) {
        String etag = request.getHeader(IF_NONE_MATCH);
        if (_useETag && etag != null) {
            String md5Sum = cacheEntry.getMd5Sum();
            return !md5Sum.equals(etag);
        }
        else {
            // If the If-Mod-Since header is missing, we get -1 back from
            // getDateHeader and this will always evaluate to true. Round
            // down to the nearest second for a proper compare.
            return request.getDateHeader(IF_MOD_SINCE) < (cacheEntry.getLastUpdate() / 1000 * 1000);
        }
    }

    /**
     * Gets the cache entry from the concurrent map.  Subclasses may override if an alternate cache provider is
     * desired.
     *
     * @param resource
     * @param time
     * @return
     */
    protected CacheEntry getCacheEntry(String resource, long time) {
        CacheEntry newCacheEntry = new CacheEntry();
        newCacheEntry.setPath(resource);
        
        CacheEntry cacheEntry = null;
        if (isCacheEnabled()) {
            cacheEntry = _cache.putIfAbsent(resource, newCacheEntry);
        }

        if (cacheEntry == null) {
            // Caching is disabled or the resource wasn't in the cache,
            //  use the new cache entry we just put there.
            cacheEntry = newCacheEntry;
        }

        maybeRefreshCacheEntry(cacheEntry, time);
        return cacheEntry;
    }

    public void clearCache() {
        _cache.clear();
    }

    public void setCacheEnabled(boolean enabled) {
        _cacheEnabled = enabled;
    }

    public boolean isCacheEnabled() {
        return _cacheEnabled;
    }

    /**
     * Determines if the given cache entry needs to be refreshed and refreshes it if needed
     *
     * @param cacheEntry The entry to check.  The 'path' property must be populated
     * @param time       The current time
     */
    protected void maybeRefreshCacheEntry(CacheEntry cacheEntry, long time) {
        if (shouldRefresh(cacheEntry, time, false)) {
            synchronized (cacheEntry) {
                // Now that we have the lock, see if we still need to refresh.
                // We'll also do the more expensive check on the file timestamp at this time
                if (shouldRefresh(cacheEntry, time, true)) {
                    refreshCacheEntry(time, cacheEntry);
                }
            }
        }
    }

    /**
     * Refreshes the cache entry by compiling the resource.
     *
     * @param time
     * @param cacheEntry
     */
    protected void refreshCacheEntry(long time, CacheEntry cacheEntry) {
        cacheEntry.setValue(compileResource(cacheEntry.getPath()));
        cacheEntry.setLastUpdate(time);
    }

    /**
     * Determines if the cache entry should be refreshed.  This is determined by checking the cache entry's last update
     * time against the given time and the cache settings for this servlet.  It may also check the file timestamp, if
     * possible to see if the file has changed since the last time the cache entry was created.
     *
     * @param cacheEntry         The entry to check
     * @param time               The current time
     * @param checkFileTimestamp If true, attempt to check the resource timestamp by getting the real path to the file
     *                           from the servlet context and getting the file's last modified time.
     * @return True if the cache entry should be refreshed.
     */
    protected boolean shouldRefresh(CacheEntry cacheEntry, long time, boolean checkFileTimestamp) {
        if (cacheEntry.getValue() == null) return true;

        if (cacheEntry.getLastUpdate() != time &&
            getCacheMillis() != CACHE_FOREVER &&
            cacheEntry.getLastUpdate() + getCacheMillis() > time) {

            // Try to see if we can get the timestamp off the resource to see if it's *really* changed
            // todo: this doesn't handle the case of @import-ed files that have changed.
            if (checkFileTimestamp) {
                String fsPath = getServletContext().getRealPath(cacheEntry.getPath());
                if (fsPath != null) {

                    // Last modified of 0 means we couldn't get the time or there was an IO error
                    long lastModified = new File(fsPath).lastModified();
                    if (lastModified > 0L && lastModified <= cacheEntry.getLastUpdate()) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    /** Returns the current time.  In a sub-method to allow for ease of unit testing */
    protected long getTime() {
        return System.currentTimeMillis();
    }

    protected byte[] compileResource(String resource) {
        try {
            URL url = getServletContext().getResource(resource);
            if (url != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                getLessCompiler().compile(getStyleSheetResourceLoader().getResource(url), output, null);
                return output.toByteArray();
            }
            else {
                getServletContext().log("Unable to find resource: " + resource);
            }
        }
        catch (Exception ex) {
            getServletContext().log("Unable to compile resource: " + resource, ex);
        }
        return null;
    }

    public StyleSheetResourceLoader getStyleSheetResourceLoader() {
        return _styleSheetResourceLoader;
    }

    public void setStyleSheetResourceLoader(StyleSheetResourceLoader styleSheetResourceLoader) {
        _styleSheetResourceLoader = styleSheetResourceLoader;
    }

    public long getCacheMillis() {
        return _cacheMillis;
    }

    public void setCacheMillis(long cacheMillis) {
        _cacheMillis = cacheMillis < 0 ? CACHE_FOREVER : cacheMillis;
    }

    public long getHttpCacheMillis() {
        return _httpCacheMillis;
    }

    public void setHttpCacheMillis(long httpCacheMillis) {
        _httpCacheMillis = httpCacheMillis;
    }

    /**
     * Given a request, determines which resource is being requested.  Default implementation just uses the request's
     * pathInfo property.
     *
     * @param request The current request
     * @return The resource to load
     */
    protected String getRequestedResource(HttpServletRequest request) {
        return request.getPathInfo();
    }

    public LessCssCompiler getLessCompiler() {
        return _lessCompiler;
    }

    public void setLessCompiler(LessCssCompiler lessCompiler) {
        _lessCompiler = lessCompiler;
    }

    /** Cache entry stores the compiled LESS code and the last time the entry was updated. */
    public static class CacheEntry {
        private byte[] _value;
        private long _lastUpdate;
        private String _path;
        private String _md5Sum;

        public String getMd5Sum() {
            return _md5Sum;
        }

        public void setMd5Sum(String md5Sum) {
            _md5Sum = md5Sum;
        }

        public String getPath() {
            return _path;
        }

        public void setPath(String path) {
            _path = path;
        }

        public byte[] getValue() {
            return _value;
        }

        public void setValue(byte[] value) {
            _value = value;
            _md5Sum = Hex.md5(_value);
        }

        public long getLastUpdate() {
            return _lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            _lastUpdate = lastUpdate;
        }
    }
}
