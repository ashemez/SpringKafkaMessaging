package com.endpoint.SpringKafkaMessaging;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MessagingErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");

        if (exception != null) {
            exception.printStackTrace();
        }

        //return String.format("<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
        //                + "<div>Exception Message: <b>%s</b></div><body></html>",
        //        statusCode, exception==null? "N/A": exception.getMessage());
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
