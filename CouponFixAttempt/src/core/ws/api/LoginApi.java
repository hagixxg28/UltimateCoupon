package core.ws.api;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Utils.LoginUtil;
import com.hagi.couponsystem.beans.loginDetails;
import com.hagi.couponsystem.exceptions.ApplicationException;

@Path("Login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginApi {

	@Context
	private HttpServletRequest req;

	@Context
	private HttpServletResponse res;

	@POST
	public void login(loginDetails details) throws ApplicationException {

		if (LoginUtil.logIn(details.getId(), details.getPassword(), details.getType())) {
			HttpSession session = req.getSession(false);

			if (session != null) {
				session.invalidate();
			}
			session = req.getSession();
			String userIdToString = String.valueOf(details.getId());
			Cookie cookie = new Cookie("id", userIdToString);
			res.addCookie(cookie);
			return;
		}
		throw new ApplicationException(ErrorTypes.FAILED_TO_LOGIN);
	}

	@DELETE
	public void logOut() {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		Cookie cookie = new Cookie("id", null);
		res.addCookie(cookie);
	}

}
