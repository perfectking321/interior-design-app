package com.interiordesign.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Custom error controller to override Whitelabel page and show full stack trace.
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        Object message = request.getAttribute("javax.servlet.error.message");
        Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");

        // Fallback for older attribute key
        if (throwable == null) {
            throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        }

        String stackTrace = "No stack trace";
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            stackTrace = sw.toString();
        }

        model.addAttribute("status", status);
        model.addAttribute("message", message != null ? message.toString() : "N/A");
        model.addAttribute("stackTrace", stackTrace);

        return "error-page";
    }
}
