/**
 * File: LessCssServletTest.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 29, 2010
 * Creation Time: 2:19:23 PM
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
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.easymock.EasyMock;

public class LessCssServletTest extends TestCase {
    LessCssServlet _servlet;
    HttpServletRequest _request;
    HttpServletResponse _response;
    ServletContext _servletContext;
    MockServletConfig _servletConfig;
    String _path = "less/tiny.less";
    URL _url;
    byte[] _cssBytes;
    String _cssStr;
    long _systemMillis;

    @Override
    protected void setUp() throws Exception {
        _request = EasyMock.createMock(HttpServletRequest.class);
        _response = EasyMock.createMock(HttpServletResponse.class);
        _servletContext = EasyMock.createMock(ServletContext.class);
        
        _servletConfig = new MockServletConfig();
        _servletConfig.setServletContext(_servletContext);

        _systemMillis = System.currentTimeMillis();

        _servlet = new LessCssServlet() {
            @Override
            protected long getTime() {
                return _systemMillis;
            }
        };

        _url = getClass().getClassLoader().getResource(_path);

        InputStream input = _url.openStream();
        try {
            _cssBytes = IOUtils.toByteArray(input);
            _cssStr = new String(_cssBytes, "UTF-8");
        }
        finally {
            IOUtils.closeQuietly(input);
        }
    }

    protected void doReplay() {
        EasyMock.replay(_request);
        EasyMock.replay(_response);
        EasyMock.replay(_servletContext);
    }

    protected void doVerify() {
        EasyMock.verify(_request);
        EasyMock.verify(_response);
        EasyMock.verify(_servletContext);
    }

    public void testEmptyCacheValidResource() throws IOException, ServletException {

        EasyMock.expect(_request.getPathInfo()).andReturn(_path);
        EasyMock.expect(_request.getMethod()).andReturn("GET");
        EasyMock.expect(_request.getDateHeader(LessCssServlet.IF_MOD_SINCE)).andReturn(-1L);
        EasyMock.expect(_request.getParameter(LessCssServlet.CLEAR_CACHE)).andReturn(null);

        EasyMock.expect(_servletContext.getResource(_path)).andReturn(_url);

        _response.addDateHeader(LessCssServlet.LAST_MODIFIED, _systemMillis);
        _response.addDateHeader(LessCssServlet.EXPIRES, _systemMillis + 900000);
        _response.addHeader(LessCssServlet.CACHE_CONTROL, "max-age=900");
        _response.setContentType("text/css; charset=UTF-8");
        _response.setContentLength(_cssBytes.length);

        MockServletOutputStream responseStream = new MockServletOutputStream();
        EasyMock.expect(_response.getOutputStream()).andReturn(responseStream);

        doReplay();

        _servlet.init(_servletConfig);
        _servlet.service(_request, _response);

        assertEquals(new String(responseStream.getBytes(), "UTF-8"), _cssStr);

        doVerify();
    }

    public void testCachedResource() throws IOException, ServletException {
        testEmptyCacheValidResource();

        EasyMock.reset(_request);
        EasyMock.reset(_response);
        EasyMock.reset(_servletContext);

        EasyMock.expect(_request.getPathInfo()).andReturn(_path);
        EasyMock.expect(_request.getMethod()).andReturn("GET");
        EasyMock.expect(_request.getDateHeader(LessCssServlet.IF_MOD_SINCE)).andReturn(-1L);
        EasyMock.expect(_request.getParameter(LessCssServlet.CLEAR_CACHE)).andReturn(null);


        _response.addDateHeader(LessCssServlet.LAST_MODIFIED, _systemMillis);
        _response.addDateHeader(LessCssServlet.EXPIRES, _systemMillis + 900000);
        _response.addHeader(LessCssServlet.CACHE_CONTROL, "max-age=900");
        _response.setContentType("text/css; charset=UTF-8");
        _response.setContentLength(_cssBytes.length);

        MockServletOutputStream responseStream = new MockServletOutputStream();
        EasyMock.expect(_response.getOutputStream()).andReturn(responseStream);

        doReplay();

        _servlet.init(_servletConfig);
        _servlet.service(_request, _response);

        assertEquals(new String(responseStream.getBytes(), "UTF-8"), _cssStr);
        
        doVerify();
    }

    public void testCachedResourceNeedsRefresh() throws IOException, ServletException {
        _servlet.setCacheMillis(10);

        testEmptyCacheValidResource();

        _systemMillis = _systemMillis - 20;

        EasyMock.reset(_request);
        EasyMock.reset(_response);
        EasyMock.reset(_servletContext);

        EasyMock.expect(_request.getPathInfo()).andReturn(_path);
        EasyMock.expect(_request.getMethod()).andReturn("GET");
        EasyMock.expect(_request.getDateHeader(LessCssServlet.IF_MOD_SINCE)).andReturn(-1L);
        EasyMock.expect(_request.getParameter(LessCssServlet.CLEAR_CACHE)).andReturn(null);

        EasyMock.expect(_servletContext.getRealPath(_path)).andReturn(_path);
        EasyMock.expect(_servletContext.getResource(_path)).andReturn(_url);

        _response.addDateHeader(LessCssServlet.LAST_MODIFIED, _systemMillis);
        _response.addDateHeader(LessCssServlet.EXPIRES, _systemMillis + 900000);
        _response.addHeader(LessCssServlet.CACHE_CONTROL, "max-age=900");
        _response.setContentType("text/css; charset=UTF-8");
        _response.setContentLength(_cssBytes.length);

        MockServletOutputStream responseStream = new MockServletOutputStream();
        EasyMock.expect(_response.getOutputStream()).andReturn(responseStream);

        doReplay();

        _servlet.init(_servletConfig);
        _servlet.service(_request, _response);

        assertEquals(new String(responseStream.getBytes(), "UTF-8"), _cssStr);

        doVerify();
    }

    public void testCachedResourceNotModified() throws IOException, ServletException {
        testEmptyCacheValidResource();

        EasyMock.reset(_request);
        EasyMock.reset(_response);
        EasyMock.reset(_servletContext);

        EasyMock.expect(_request.getPathInfo()).andReturn(_path);
        EasyMock.expect(_request.getMethod()).andReturn("GET");
        EasyMock.expect(_request.getDateHeader(LessCssServlet.IF_MOD_SINCE)).andReturn(_systemMillis + 1000);
        EasyMock.expect(_request.getParameter(LessCssServlet.CLEAR_CACHE)).andReturn(null);


        _response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);

        doReplay();

        _servlet.init(_servletConfig);
        _servlet.service(_request, _response);

        doVerify();
    }

    public void testCachedResourceHeadRequest() throws IOException, ServletException {
        testEmptyCacheValidResource();

        EasyMock.reset(_request);
        EasyMock.reset(_response);
        EasyMock.reset(_servletContext);

        EasyMock.expect(_request.getPathInfo()).andReturn(_path);
        EasyMock.expect(_request.getMethod()).andReturn("HEAD");
        EasyMock.expect(_request.getParameter(LessCssServlet.CLEAR_CACHE)).andReturn(null);


        _response.addDateHeader(LessCssServlet.LAST_MODIFIED, _systemMillis);
        _response.addDateHeader(LessCssServlet.EXPIRES, _systemMillis + 900000);
        _response.addHeader(LessCssServlet.CACHE_CONTROL, "max-age=900");
        _response.setContentType("text/css; charset=UTF-8");
        _response.setContentLength(_cssBytes.length);

        doReplay();

        _servlet.init(_servletConfig);
        _servlet.service(_request, _response);

        doVerify();
    }

    public void testNullPath() throws IOException, ServletException {
        EasyMock.expect(_request.getPathInfo()).andReturn(null);
        EasyMock.expect(_request.getParameter(LessCssServlet.CLEAR_CACHE)).andReturn(null);
        _response.sendError(HttpServletResponse.SC_NOT_FOUND);

        doReplay();

        _servlet.service(_request, _response);

        doVerify();
    }

    public void testEmptyPath() throws IOException, ServletException {
        EasyMock.expect(_request.getPathInfo()).andReturn("  ");
        EasyMock.expect(_request.getParameter(LessCssServlet.CLEAR_CACHE)).andReturn(null);
        _response.sendError(HttpServletResponse.SC_NOT_FOUND);

        doReplay();

        _servlet.service(_request, _response);

        doVerify();
    }

    private static class MockServletConfig implements ServletConfig {
        private String _servletName;
        private ServletContext _servletContext;
        private Map<String, String> _initParameters = new LinkedHashMap<String, String>();

        public String getServletName() {
            return _servletName;
        }

        public ServletContext getServletContext() {
            return _servletContext;
        }

        public void setServletName(String servletName) {
            _servletName = servletName;
        }

        public void setServletContext(ServletContext servletContext) {
            _servletContext = servletContext;
        }

        public void setInitParameter(String name, String value) {
            _initParameters.put(name, value);
        }

        public String getInitParameter(String name) {
            return _initParameters.get(name);
        }

        public Enumeration getInitParameterNames() {
            return new Enumeration() {
                Iterator _iter = _initParameters.keySet().iterator();

                public boolean hasMoreElements() {
                    return _iter.hasNext();
                }

                public Object nextElement() {
                    return _iter.next();
                }
            };
        }
    }

    private static class MockServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream output = new ByteArrayOutputStream();

        @Override
        public void write(int b) throws IOException {
            output.write(b);
        }

        public byte[] getBytes() {
            return output.toByteArray();
        }
    }
}
