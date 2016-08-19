package me.quanli.commons.controller;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = { "me.quanli.report.controller" })
public class ReportExceptionHandler {

    private static final Log LOGGER = LogFactory.getLog(ReportExceptionHandler.class);

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) {
        LOGGER.error(e, e);
        String message = ExceptionUtils.getRootCauseMessage(e);
        message = message == null ? "internal server error" : message;
        return message;
    }

}
