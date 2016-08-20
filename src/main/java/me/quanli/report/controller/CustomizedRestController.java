package me.quanli.report.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.MultiValueMap;

public class CustomizedRestController {

    private static final Log LOGGER = LogFactory.getLog(CustomizedRestController.class);

    protected final FormHttpMessageConverter formConverter = new AllEncompassingFormHttpMessageConverter();

    protected Map<String, String> readForm(final HttpServletRequest request) {
        HttpInputMessage inputMessage = new ServletServerHttpRequest(request) {
            @Override
            public InputStream getBody() throws IOException {
                return request.getInputStream();
            }
        };
        Map<String, String> params = new HashMap<String, String>();
        try {
            MultiValueMap<String, String> formParameters = formConverter.read(null, inputMessage);
            params = formParameters.toSingleValueMap();
        } catch (HttpMessageNotReadableException | IOException e) {
            LOGGER.error(e, e);
        }
        return params;
    }

    public static Map<String, String> getParams(ServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        if (request == null) {
            return params;
        }
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            params.put(paramName, paramValue);
        }
        return params;
    }

}
