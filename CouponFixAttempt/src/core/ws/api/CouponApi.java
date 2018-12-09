package core.ws.api;

import java.sql.Date;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.hagi.couponsystem.Enums.CouponType;
import com.hagi.couponsystem.beans.Coupon;
import com.hagi.couponsystem.exception.facade.FacadeException;
import com.hagi.couponsystem.logic.CouponLogic;

@Path("Coupon")
public class CouponApi {

	@GET
	@Path("/{couponId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Coupon getCoupon(@PathParam("couponId") long couponId) throws FacadeException {
		CouponLogic logic = new CouponLogic();
		return logic.getCoupon(couponId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void createCoupon(Coupon coupon) throws FacadeException {
		CouponLogic logic = new CouponLogic();
		logic.createCoupon(coupon);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCoupon(Coupon coupon) throws FacadeException {
		CouponLogic logic = new CouponLogic();
		logic.updateCoupon(coupon);
	}

	@DELETE
	@Path("/{couponId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeCoupon(@PathParam("couponId") long couponId) throws FacadeException {
		CouponLogic logic = new CouponLogic();
		logic.removeCoupon(couponId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getAllCoupons() throws FacadeException {
		CouponLogic logic = new CouponLogic();
		return logic.getAllCoupon();
	}

	@GET
	@Path("/price")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsByPrice(@QueryParam("price") double price) throws FacadeException {
		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponByPrice(price);
	}

	@GET
	@Path("/date")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsByDate(@QueryParam("date") Date date) throws FacadeException {
		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponByDate(date);
	}

	@GET
	@Path("/type")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsByType(@QueryParam("type") CouponType type) throws FacadeException {
		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponByType(type);
	}

	@GET
	@Path("/company/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCompany(@PathParam("companyId") long companyId) throws FacadeException {
		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponForCompany(companyId);
	}

	@GET
	@Path("/company/{companyId}/type")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCompanyByType(@PathParam("companyId") long companyId,
			@QueryParam("type") CouponType type) throws FacadeException {

		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponByTypeForcompany(type, companyId);
	}

	@GET
	@Path("/company/{companyId}/date")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCompanyByDate(@PathParam("companyId") long companyId,
			@QueryParam("date") Date date) throws FacadeException {

		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponByDateForCompany(date, companyId);
	}

	@GET
	@Path("/company/{companyId}/price")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCompanyByPrice(@PathParam("companyId") long companyId,
			@QueryParam("price") double price) throws FacadeException {

		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponByPriceForCompany(price, companyId);
	}

	@GET
	@Path("/customer/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCustomer(@PathParam("customerId") long customerId) throws FacadeException {

		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponForCustomer(customerId);
	}

	@GET
	@Path("/customer/{customerId}/type")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCustomerByType(@PathParam("customerId") long customerId,
			@QueryParam("type") CouponType type) throws FacadeException {

		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponByTypeForCustomer(type, customerId);
	}

	@GET
	@Path("/customer/{customerId}/date")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCustomerByDate(@PathParam("customerId") long customerId,
			@QueryParam("date") Date date) throws FacadeException {

		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponByDateForCustomer(date, customerId);
	}

	@GET
	@Path("/customer/{customerId}/price")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCustomerByPrice(@PathParam("customerId") long customerId,
			@QueryParam("price") double price) throws FacadeException {

		CouponLogic logic = new CouponLogic();
		return logic.getAllCouponByPriceForCustomer(price, customerId);
	}

	@GET
	@Path("/purchase/{couponId}/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void purchaseCoupon(@PathParam("couponId") long couponId, @PathParam("customerId") long customerId)
			throws FacadeException {
		CouponLogic logic = new CouponLogic();
		logic.purchaseCoupon(couponId, customerId);
	}

}
