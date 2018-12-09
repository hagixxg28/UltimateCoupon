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
import com.hagi.couponsystem.exception.facade.FacadeException;
import com.hagi.couponsystem.logic.CompanyLogic;

@Path("Company")
public class CompanyApi {

//	http://localhost:8080/CouponFixAttempt/rest/Company

//
	@GET
	@Path("/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Company getCompany(@PathParam("companyId") long companyId) throws FacadeException {
		CompanyLogic logic = new CompanyLogic();
		return logic.getcompany(companyId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void createCompany(Company company) throws FacadeException {
		CompanyLogic logic = new CompanyLogic();
		logic.createCompany(company);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCompany(Company company) throws FacadeException {
		CompanyLogic logic = new CompanyLogic();
		logic.updateCompany(company);
	}

	@DELETE
	@Path("/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeCompany(@PathParam("companyId") long companyId) throws FacadeException {
		CompanyLogic logic = new CompanyLogic();
		logic.removeCompany(companyId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Company> getAllCompanies() throws FacadeException {
		CompanyLogic logic = new CompanyLogic();
		return logic.getAllCompanies();
	}
}
