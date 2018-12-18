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
import com.hagi.couponsystem.exceptions.ApplicationException;
import com.hagi.couponsystem.logic.CustomerLogic;

//http://localhost:8080/rest/customer
@Path("customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CusotmerApi {

	private CustomerLogic logic = CustomerLogic.getInstance();

	@GET
	@Path("/{customerId}")
	public Customer getCustomer(@PathParam("customerId") long customerId) throws ApplicationException {
		return logic.getCustomer(customerId);
	}

	@POST
	public void createCustomer(Customer customer) throws ApplicationException {
		logic.createCustomer(customer);
	}

	@PUT
	public void updateCustomer(Customer customer) throws ApplicationException {
		logic.updateCustomer(customer);
	}

	@DELETE
	@Path("/{customerId}")

	public void removeCustomer(@PathParam("customerId") long customerId) throws ApplicationException {
		logic.removeCustomer(customerId);
	}

	@GET
	public Collection<Customer> getAllCustomers() throws ApplicationException {
		return logic.getAllCustomers();
	}
}
