package com.umessage.letsgo.assistant.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ZhaoYidong on 2016/12/26.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, BusinessException e) throws BusinessException {
        ModelAndView mav = new ModelAndView();
        mav.addObject("code", e.getCode());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, BusinessException e) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setCode(e.getCode());
        r.setTargetUrl(e.getTargetUrl());
        return r;
    }


}
