package cn.kwebi.community.advice;

import cn.kwebi.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable ex, Model model){
        if(ex instanceof CustomizeException){
            model.addAttribute("message",ex.getMessage());
        }else{
            model.addAttribute("message","service error");
        }
        return new ModelAndView("error");
    }

}
