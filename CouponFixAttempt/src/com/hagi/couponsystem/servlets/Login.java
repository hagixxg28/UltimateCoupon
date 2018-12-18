package com.hagi.couponsystem.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hagi.couponsystem.Enums.ClientType;
import com.hagi.couponsystem.Utils.LoginUtil;
import com.hagi.couponsystem.exceptions.ApplicationException;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Login() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long id = Long.valueOf(request.getParameter("id"));
		String password = request.getParameter("password");
		String type = request.getParameter("type");
		ClientType parseType;
		try {

			parseType = ClientType.parseType(type);
			if (LoginUtil.logIn(id, password, parseType)) {
				HttpSession session = request.getSession(false);
				if (session != null) {
					session.invalidate();
				}
				session = request.getSession();
				session.setAttribute("id", id);
				response.encodeRedirectURL("/home");
			}

		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
