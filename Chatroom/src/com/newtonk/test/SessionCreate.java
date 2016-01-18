package com.newtonk.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionCreate extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse arg1)
			throws ServletException, IOException {
		req.getSession().setAttribute("constants_username", "tq");
		System.out.println("OK");
	}
}
