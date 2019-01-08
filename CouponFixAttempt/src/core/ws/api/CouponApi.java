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
import com.hagi.couponsystem.exceptions.ApplicationException;
import com.hagi.couponsystem.logic.CouponLogic;

@Path("Coupon")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CouponApi {
	private CouponLogic logic = CouponLogic.getInstance();

	@GET
	@Path("/{couponId}")

	public Coupon getCoupon(@PathParam("couponId") long couponId) throws ApplicationException {
		return logic.getCoupon(couponId);
	}

	@POST
	public void createCoupon(Coupon coupon) throws ApplicationException {
		logic.createCoupon(coupon);
	}

	@PUT
	public void updateCoupon(Coupon coupon) throws ApplicationException {
		logic.updateCoupon(coupon);
	}

	@DELETE
	@Path("/{couponId}")
	public void removeCoupon(@PathParam("couponId") long couponId) throws ApplicationException {
		logic.removeCoupon(couponId);
	}

	@GET
	public Collection<Coupon> getAllCoupons() throws ApplicationException {
		return logic.getAllCoupon();
	}

	@GET
	@Path("/price")
	public Collection<Coupon> getCouponsByPrice(@QueryParam("price") double price) throws ApplicationException {
		return logic.getAllCouponByPrice(price);
	}

	@GET
	@Path("/date")
	public Collection<Coupon> getCouponsByDate(@QueryParam("date") String date) throws ApplicationException {
		System.out.println(date);
		Date parseDate = Date.valueOf(date);
		System.out.println(parseDate);
		return logic.getAllCouponByDate(parseDate);
	}

	@GET
	@Path("/type")
	public Collection<Coupon> getCouponsByType(@QueryParam("type") CouponType type) throws ApplicationException {
		return logic.getAllCouponByType(type);
	}

	@GET
	@Path("/company/{companyId}")
	public Collection<Coupon> getCouponsForCompany(@PathParam("companyId") long companyId) throws ApplicationException {
		return logic.getAllCouponForCompany(companyId);
	}

	@GET
	@Path("/company/{companyId}/type")
	public Collection<Coupon> getCouponsForCompanyByType(@PathParam("companyId") long companyId,
			@QueryParam("type") CouponType type) throws ApplicationException {

		return logic.getAllCouponByTypeForcompany(type, companyId);
	}

	@GET
	@Path("/company/{companyId}/date")
	public Collection<Coupon> getCouponsForCompanyByDate(@PathParam("companyId") long companyId,
			@QueryParam("date") String date) throws ApplicationException {
		Date parseDate = Date.valueOf(date);
		return logic.getAllCouponByDateForCompany(parseDate, companyId);
	}

	@GET
	@Path("/company/{companyId}/price")
	public Collection<Coupon> getCouponsForCompanyByPrice(@PathParam("companyId") long companyId,
			@QueryParam("price") double price) throws ApplicationException {

		return logic.getAllCouponByPriceForCompany(price, companyId);
	}

	@GET
	@Path("/customer/{customerId}")
	public Collection<Coupon> getCouponsForCustomer(@PathParam("customerId") long customerId)
			throws ApplicationException {

		return logic.getAllCouponForCustomer(customerId);
	}

	@GET
	@Path("/customer/{customerId}/type")
	public Collection<Coupon> getCouponsForCustomerByType(@PathParam("customerId") long customerId,
			@QueryParam("type") CouponType type) throws ApplicationException {

		return logic.getAllCouponByTypeForCustomer(type, customerId);
	}

	@GET
	@Path("/customer/{customerId}/date")
	public Collection<Coupon> getCouponsForCustomerByDate(@PathParam("customerId") long customerId,
			@QueryParam("date") String date) throws ApplicationException {
		Date parseDate = Date.valueOf(date);
		return logic.getAllCouponByDateForCustomer(parseDate, customerId);
	}

	@GET
	@Path("/customer/{customerId}/price")
	public Collection<Coupon> getCouponsForCustomerByPrice(@PathParam("customerId") long customerId,
			@QueryParam("price") double price) throws ApplicationException {

		return logic.getAllCouponByPriceForCustomer(price, customerId);
	}

	@GET
	@Path("/purchase/{couponId}/{customerId}")
	public void purchaseCoupon(@PathParam("couponId") long couponId, @PathParam("customerId") long customerId)
			throws ApplicationException {
		logic.purchaseCoupon(couponId, customerId);
	}

}
