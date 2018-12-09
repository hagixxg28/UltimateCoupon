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

import com.hagi.couponsystem.beans.Customer;
import com.hagi.couponsystem.exception.facade.FacadeException;
import com.hagi.couponsystem.logic.CustomerLogic;

//http://localhost:8080/rest/customer
@Path("customer")
public class CusotmerApi {

	@GET
	@Path("/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@PathParam("customerId") long customerId) throws FacadeException {
		CustomerLogic logic = new CustomerLogic();
		return logic.getCustomer(customerId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void createCustomer(Customer customer) throws FacadeException {
		CustomerLogic logic = new CustomerLogic();
		logic.createCustomer(customer);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCustomer(Customer customer) throws FacadeException {
		CustomerLogic logic = new CustomerLogic();
		logic.updateCustomer(customer);
	}

	@DELETE
	@Path("/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeCustomer(@PathParam("customerId") long customerId) throws FacadeException {
		CustomerLogic logic = new CustomerLogic();
		logic.removeCustomer(customerId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Customer> getAllCustomers() throws FacadeException {
		CustomerLogic logic = new CustomerLogic();
		return logic.getAllCustomers();
	}
}
