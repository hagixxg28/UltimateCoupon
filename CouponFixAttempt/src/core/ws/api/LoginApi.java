package core.ws.api;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.hagi.couponsystem.Enums.ClientType;
import com.hagi.couponsystem.Enums.ErrorTypes;
import com.hagi.couponsystem.Utils.LoginUtil;
import com.hagi.couponsystem.exceptions.ApplicationException;

@Path("Login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginApi {

	@Context
	private HttpServletRequest req;

	@Context
	private HttpServletResponse res;

	@GET
	@Path("/{userId}")
	public void getCompany(@PathParam("userId") long userId, @QueryParam("password") String password,
			@QueryParam("type") ClientType type) throws ApplicationException {
		if (LoginUtil.logIn(userId, password, type)) {
			HttpSession session = req.getSession(false);

			if (session != null) {
				session.invalidate();
			}
			session = req.getSession();
			String userIdToString = String.valueOf(userId);
			Cookie cookie = new Cookie("id", userIdToString);
			res.addCookie(cookie);
			res.encodeRedirectURL("/home");
		}
		throw new ApplicationException(ErrorTypes.FAILED_TO_LOGIN);
	}
}
