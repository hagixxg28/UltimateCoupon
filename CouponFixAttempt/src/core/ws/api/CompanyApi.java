package core.ws.api;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.hagi.couponsystem.beans.Company;
import com.hagi.couponsystem.exceptions.ApplicationException;
import com.hagi.couponsystem.logic.CompanyLogic;

@Path("Company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyApi {
	private CompanyLogic logic = CompanyLogic.getInstance();

//	http://localhost:8080/CouponFixAttempt/rest/Company

//
	@GET
	@Path("/{companyId}")
	public Company getCompany(@PathParam("companyId") long companyId) throws ApplicationException {
		return logic.getcompany(companyId);
	}

	@POST
	public void createCompany(Company company) throws ApplicationException {
		logic.createCompany(company);
	}

	@PUT
	public void updateCompany(Company company) throws ApplicationException {
		logic.updateCompany(company);
	}

	@DELETE
	@Path("/{companyId}")
	public void removeCompany(@PathParam("companyId") long companyId) throws ApplicationException {
		logic.removeCompany(companyId);
	}

	@GET
	public Collection<Company> getAllCompanies() throws ApplicationException {
		return logic.getAllCompanies();
	}
}
