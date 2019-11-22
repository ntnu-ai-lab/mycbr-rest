package no.ntnu.mycbr.rest.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyDefaultHandler implements HttpRequestHandler {
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void handleRequest (HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	logger.info("in default handler method: "+request.getMethod()+" url: "+request.getRequestURI());
	PrintWriter writer = response.getWriter();
	writer.write("response from MyDefaultHandler method: "+request.getMethod()+", uri: "+request.getRequestURI());
    }
}
