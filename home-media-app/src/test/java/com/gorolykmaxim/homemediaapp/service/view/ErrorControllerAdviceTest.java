package com.gorolykmaxim.homemediaapp.service.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class ErrorControllerAdviceTest {

    private ErrorControllerAdvice advice;

    @Before
    public void setUp() throws Exception {
        advice = new ErrorControllerAdvice();
    }

    @Test
    public void showError() {
        RuntimeException exception = new RuntimeException("message");
        StringWriter expectedExceptionWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(expectedExceptionWriter);
        exception.printStackTrace(printWriter);
        ModelAndView modelAndView = advice.showError(exception);
        Assert.assertEquals("error", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(exception.getMessage(), model.get("error"));
        Assert.assertEquals(expectedExceptionWriter.toString(), model.get("stackTrace"));
    }
}
