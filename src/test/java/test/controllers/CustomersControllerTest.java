//
//

package test.controllers;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import SNET.Core.APIContext;
import SecureNetRestApiSDK.Api.Controllers.CustomersController;
import SecureNetRestApiSDK.Api.Models.Address;
import SecureNetRestApiSDK.Api.Models.Card;
import SecureNetRestApiSDK.Api.Models.DeveloperApplication;
import SecureNetRestApiSDK.Api.Models.InstallmentPaymentPlan;
import SecureNetRestApiSDK.Api.Models.PaymentVaultToken;
import SecureNetRestApiSDK.Api.Models.RecurringPaymentPlan;
import SecureNetRestApiSDK.Api.Models.UserDefinedField;
import SecureNetRestApiSDK.Api.Models.VariablePaymentPlan;
import SecureNetRestApiSDK.Api.Models.Schedule;
import SecureNetRestApiSDK.Api.Requests.AddInstallmentPaymentPlanRequest;
import SecureNetRestApiSDK.Api.Requests.AddPaymentMethodRequest;
import SecureNetRestApiSDK.Api.Requests.AddRecurringPaymentPlanRequest;
import SecureNetRestApiSDK.Api.Requests.AddVariablePaymentPlanRequest;
import SecureNetRestApiSDK.Api.Requests.ChargeRequest;
import SecureNetRestApiSDK.Api.Requests.CreateCustomerRequest;
import SecureNetRestApiSDK.Api.Requests.RemovePaymentMethodRequest;
import SecureNetRestApiSDK.Api.Requests.RetrieveCustomerRequest;
import SecureNetRestApiSDK.Api.Requests.RetrievePaymentAccountRequest;
import SecureNetRestApiSDK.Api.Requests.RetrievePaymentPlanRequst;
import SecureNetRestApiSDK.Api.Requests.UpdateCustomerRequest;
import SecureNetRestApiSDK.Api.Requests.UpdateInstallmentPaymentPlanRequest;
import SecureNetRestApiSDK.Api.Requests.UpdatePaymentMethodRequest;
import SecureNetRestApiSDK.Api.Requests.UpdateRecurringPaymentPlanRequest;
import SecureNetRestApiSDK.Api.Requests.UpdateVariablePaymentPlanRequest;
import SecureNetRestApiSDK.Api.Requests.VaultCustomerAndPaymentMethodRequest;
import SecureNetRestApiSDK.Api.Responses.AddInstallmentPaymentPlanResponse;
import SecureNetRestApiSDK.Api.Responses.AddPaymentMethodResponse;
import SecureNetRestApiSDK.Api.Responses.AddRecurringPaymentPlanResponse;
import SecureNetRestApiSDK.Api.Responses.AddVariablePaymentPlanResponse;
import SecureNetRestApiSDK.Api.Responses.ChargeResponse;
import SecureNetRestApiSDK.Api.Responses.CreateCustomerResponse;
import SecureNetRestApiSDK.Api.Responses.RemovePaymentMethodResponse;
import SecureNetRestApiSDK.Api.Responses.RetrieveCustomerResponse;
import SecureNetRestApiSDK.Api.Responses.RetrievePaymentAccountResponse;
import SecureNetRestApiSDK.Api.Responses.RetrievePaymentPlanResponse;
import SecureNetRestApiSDK.Api.Responses.UpdateCustomerResponse;
import SecureNetRestApiSDK.Api.Responses.UpdateInstallmentPaymentPlanResponse;
import SecureNetRestApiSDK.Api.Responses.UpdatePaymentMethodResponse;
import SecureNetRestApiSDK.Api.Responses.UpdateRecurringPaymentPlanResponse;
import SecureNetRestApiSDK.Api.Responses.UpdateVariablePaymentPlanResponse;
import SecureNetRestApiSDK.Api.Responses.VaultCustomerAndPaymentMethodResponse;

public class CustomersControllerTest {
	
	Properties config ;
	
	@Before
	public void before() throws Exception{
		InputStream stream  = this.getClass().getResourceAsStream("/config.properties");
		config = new Properties();
		config.load(stream);
	}
	
	
	/**
	 * Unit Tests for Create, Retrieve, Update and Delete Customer requests.
	 * Tests combined in one method to pass the required customer identifier and
	 * to guaranteee the order of operation.
	 * 
	 */
	@Test
	public void secureNetVaultCreateRetrieveAndUpdateCustomerRequestsReturnsSuccessfully()
			throws Exception {
		// Create the Customer
		String customerId = secureNetVaultCreateCustomerRequestReturnsSuccessfully();
		// Retrieve the Customer
		secureNetVaultRetrieveCustomerRequestReturnsSuccessfully(customerId);
		// Update the Customer
		secureNetVaultUpdateCustomerRequestReturnsSuccessfully(customerId);
	}

	/**
	 * Unit Tests for Create, Retrieve, Charge, Update and Delete Payment
	 * Account requests. Tests combined in one method to pass the required
	 * payment method identifier and to guaranteee the order of operation.
	 */
	@Test
	public void secureNetVaultCreateRetrieveChargeUpdateAndDeletePaymentAccountRequestsReturnsSuccessfully()
			throws Exception {
		// Create the Customer
		String customerId = secureNetVaultCreateCustomerRequestReturnsSuccessfully();
		// Create the Payment Account
		String paymentMethodId = secureNetVaultCreatePaymentAccountRequestReturnsSuccessfully(customerId);
		// Retrieve the Payment Account
		secureNetVaultRetrievePaymentAccountRequestReturnsSuccessfully(
				customerId, paymentMethodId);
		// Pay using a Stored Vault Account
		secureNetVaultPayWithStoredVaultAccountRequestReturnsSuccessfully(
				customerId, paymentMethodId);
		// Update the Payment Account
		secureNetVaultUpdatePaymentAccountRequestReturnsSuccessfully(
				customerId, paymentMethodId);
		// Delete the Payment Account
		secureNetVaultDeletePaymentAccountRequestReturnsSuccessfully(
				customerId, paymentMethodId);
	}

	// Delete the Customer
	// TODO

	/**
	 * Successful response returned from an Create Customer and Payment Account
	 * request. https://apidocs.securenet.com/docs/vault.html?lang=csharp#
	 * createcustomerandPayment
	 */
	@Test
	public void secureNetVaultCreateCustomerAndPaymentAccountRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		VaultCustomerAndPaymentMethodRequest request = getVaultCustomerAndPaymentMethodRequest();
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		VaultCustomerAndPaymentMethodResponse response = (VaultCustomerAndPaymentMethodResponse) controller.processRequest(apiContext, request,VaultCustomerAndPaymentMethodResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
	}

	/**
	 * Successful response returned from a Storing Acount After Charge request.
	 * https://apidocs.securenet.com/docs/vault.html?lang=csharp#storedaccount
	 */
	@Test
	public void secureNetVaultStoreAccountAfterChargeRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setDeveloperApplication(getDeveloperApplication());
		request.setCard(getCard());
		request.setAddToVault(true);
		request.setAmount(11d);
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(),response.getSuccess());
	}

	/**
	 * Unit Tests for Creating a Payment Account, Creating an Installment Plan,
	 * Updating the Installment Plan, Retrieving the Installment Plan, and
	 * deleting the Installment Plan requests. Tests combined in one method to
	 * pass the required payment method identifier, the plan identifier and to
	 * guaranteee the order of operation.
	 */
	@Test
	public void recurringBillingCreateRetrieveUpdateAndDeleteInstallmentPlanRequestsReturnsSuccessfully()
			throws Exception {
		// Create the Customer
		String customerId = secureNetVaultCreateCustomerRequestReturnsSuccessfully();
		// Create the Payment Account
		String paymentMethodId = secureNetVaultCreatePaymentAccountRequestReturnsSuccessfully(customerId);
		// Create the Installment Plan
		String planId = recurringBillingCreateInstallmentPlanRequestReturnsSuccessfully(
				customerId, paymentMethodId);
		// Retrieve the Installment Plan
		recurringBillingRetrievePaymentPlanRequestReturnsSuccessfully(
				customerId, planId);
		// Update the Installment Plan
		recurringBillingUpdateInstallmentPlanrequestReturnsSuccessfully(
				customerId, planId);
		// Delete the Payment Account
		/*secureNetVaultDeletePaymentAccountRequestReturnsSuccessfully(
				customerId, paymentMethodId);*/
	}

	/**
	 * Unit Tests for Creating a Payment Account, Creating a Recurring Payment
	 * Plan, Updating the Recurring Payment Plan, Retrieving the Recurring
	 * Payment Plan, and deleting the Recurring Payment Plan requests. Tests
	 * combined in one method to pass the required payment method identifier,
	 * the plan identifier and to guaranteee the order of operation.
	 */
	@Test
	public void recurringBillingCreateRetrieveUpdateAndDeleteRecurringPaymentPlanRequestsReturnsSuccessfully()
			throws Exception {
		// Create the Customer
		String customerId = secureNetVaultCreateCustomerRequestReturnsSuccessfully();
		// Create the Payment Account
		String paymentMethodId = secureNetVaultCreatePaymentAccountRequestReturnsSuccessfully(customerId);
		// Create the Recurring Payment Plan
		String planId = recurringBillingCreateRecurringPaymentPlanRequestReturnsSuccessfully(
				customerId, paymentMethodId);
		// Retrieve the Recurring Payment Plan
		recurringBillingRetrievePaymentPlanRequestReturnsSuccessfully(
				customerId, planId);
		// Update the Recurring Payment Plan
		recurringBillingUpdateRecurringPaymentPlanRequestReturnsSuccessfully(
				customerId, planId);
		// Delete the Payment Account
		/*secureNetVaultDeletePaymentAccountRequestReturnsSuccessfully(
				customerId, paymentMethodId);*/
	}

	/**
	 * Unit Tests for Creating a Payment Account, Creating a Variable Payment
	 * Plan, Updating the Variable Payment Plan, Retrieving the Variable
	 * Payment Plan, and deleting the Variable Payment Plan requests. Tests
	 * combined in one method to pass the required payment method identifier,
	 * the plan identifier and to guaranteee the order of operation.
	 */
	@Test
	public void recurringBillingCreateRetrieveUpdateAndDeleteVariablePaymentPlanRequestsReturnsSuccessfully()
			throws Exception {
		// Create the Customer
		String customerId = secureNetVaultCreateCustomerRequestReturnsSuccessfully();
		// Create the Payment Account
		String paymentMethodId = secureNetVaultCreatePaymentAccountRequestReturnsSuccessfully(customerId);
		// Create the Variable Payment Plan
		String planId = recurringBillingCreateVariablePaymentPlanRequestReturnsSuccessfully(
				customerId, paymentMethodId);
		// Retrieve the Variable Payment Plan
		recurringBillingRetrievePaymentPlanRequestReturnsSuccessfully(
				customerId, planId);
		// Update the Variable Payment Plan
		recurringBillingUpdateVariablePaymentPlanRequestReturnsSuccessfully(
				customerId, planId);
		// Delete the Payment Account
		/*secureNetVaultDeletePaymentAccountRequestReturnsSuccessfully(
				customerId, paymentMethodId);*/
	}
	
	/**
	 * Successful response returned from a Create Customer request.
	 * https://apidocs.securenet.com/docs/vault.html?lang=csharp#createcustomer
	 */

	private String secureNetVaultCreateCustomerRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		CreateCustomerRequest request = getCreateCustomerRequestObject();
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		CreateCustomerResponse response = (CreateCustomerResponse) controller
				.processRequest(apiContext, request,
						CreateCustomerResponse.class);
		// Assertz
		Assert.assertNotNull(response.toResponseString(),response.getCustomerId());
		return response.getCustomerId();
	}

	/**
	 * Successful response returned from a Pay with Stored Vault Account
	 * request.
	 * https://apidocs.securenet.com/docs/vault.html?lang=csharp#payaccount
	 */
	private void secureNetVaultPayWithStoredVaultAccountRequestReturnsSuccessfully(
			String customerId, String paymentMethodId) throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setDeveloperApplication(getDeveloperApplication());
		request.setPaymentVaultToken(getPaymentVaultToken(customerId,paymentMethodId));
		request.setAmount(200D);
		request.setAddToVault(true);
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		ChargeResponse chargeResponse = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		Assert.assertNotNull(chargeResponse.toResponseString(), chargeResponse.getTransaction());
		// Assert
	}

	/**
	 * Successful response returned from a Retrieve Customer request.
	 * https://apidocs
	 * .securenet.com/docs/vault.html?lang=csharp#retrievecustomer
	 */
	private void secureNetVaultRetrieveCustomerRequestReturnsSuccessfully(
			String customerId) throws Exception {
		// Arrange
		RetrieveCustomerRequest request = new RetrieveCustomerRequest();
		request.setCustomerId(customerId);
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		RetrieveCustomerResponse response = (RetrieveCustomerResponse) controller
				.processRequest(apiContext, request,
						RetrieveCustomerResponse.class);
		Assert.assertEquals(response.toResponseString(),customerId, response.getCustomerId());
		// Assert
	}

	/**
	 * Successful response returned from an Update Customer request.
	 * https://apidocs.securenet.com/docs/vault.html?lang=csharp#updatecustomer
	 */
	private void secureNetVaultUpdateCustomerRequestReturnsSuccessfully(
			String customerId) throws Exception {
		// Arrange
		UpdateCustomerRequest request = new UpdateCustomerRequest();
		request.setCustomerId(customerId);
		request.setFirstName("new Name");
		request.setDeveloperApplication(getDeveloperApplication());
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		UpdateCustomerResponse response = (UpdateCustomerResponse) controller
				.processRequest(apiContext, request,
						UpdateCustomerResponse.class);
		Assert.assertEquals(response.toResponseString(),"new Name", response.getVaultCustomer()
				.getFirstName());
		// Assert
	}

	// Delete the Customer
	// TODO
	/**
	 * Successful response returned from a Create Payment Account request.
	 * https://apidocs.securenet.com/docs/vault.html?lang=csharp#createaccount
	 */
	private String secureNetVaultCreatePaymentAccountRequestReturnsSuccessfully(
			String customerId) throws Exception {
		// Arrange
		AddPaymentMethodRequest request = new AddPaymentMethodRequest();
		request.setCustomerId(customerId);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setCard(getCard());
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		AddPaymentMethodResponse response = (AddPaymentMethodResponse) controller
				.processRequest(apiContext, request,
						AddPaymentMethodResponse.class);
		// Assert
		Assert.assertNotNull(response.toResponseString(),response.getVaultPaymentMethod().getPaymentId());
		return response.getVaultPaymentMethod().getPaymentId();
	}

	/**
	 * Successful response returned from a Retrieve Payment Account request.
	 * https://apidocs.securenet.com/docs/vault.html?lang=csharp#retrieveaccount
	 */

	private void secureNetVaultRetrievePaymentAccountRequestReturnsSuccessfully(
			String customerId, String paymentMethodId) throws Exception {
		// Arrange
		RetrievePaymentAccountRequest request = new RetrievePaymentAccountRequest();
		request.setCustomerId(customerId);
		request.setPaymentMethodId(paymentMethodId);
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		RetrievePaymentAccountResponse response = (RetrievePaymentAccountResponse) controller.processRequest(apiContext, request,RetrievePaymentAccountResponse.class);
		Assert.assertEquals(response.toResponseString(), paymentMethodId, response.getVaultPaymentMethod().getPaymentId());
		// Assert
	}

	/**
	 * Successful response returned from an Update Payment Account request.
	 * https://apidocs.securenet.com/docs/vault.html?lang=csharp#updateaccount
	 */
	private void secureNetVaultUpdatePaymentAccountRequestReturnsSuccessfully(
			String customerId, String paymentMethodId) throws Exception {
		// Arrange
		UpdatePaymentMethodRequest request = new UpdatePaymentMethodRequest();
		request.setCustomerId(customerId);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setPaymentMethodId(paymentMethodId);
		request.setPrimary(true);
		Card card = getCard();
		card.setCvv("222");
		request.setCard(card);
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		UpdatePaymentMethodResponse response = (UpdatePaymentMethodResponse) controller.processRequest(apiContext, request,UpdatePaymentMethodResponse.class);
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		// Assert
	}

	/**
	 * Successful response returned from an Delete Payment Account request.
	 * https://apidocs.securenet.com/docs/vault.html?lang=csharp#removeaccount
	 */
	private void secureNetVaultDeletePaymentAccountRequestReturnsSuccessfully(
			String customerId, String paymentMethodId) throws Exception {
		// Arrange
		RemovePaymentMethodRequest request = new RemovePaymentMethodRequest();
		request.setCustomerId(customerId);
		request.setPaymentMethodId(paymentMethodId);
		request.setDeveloperApplication(getDeveloperApplication());
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		RemovePaymentMethodResponse response = (RemovePaymentMethodResponse) controller.processRequest(apiContext, request,RemovePaymentMethodResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(),response.getSuccess());
	}

	// Delete the Customer
	// TODO
	/**
	 * Successful response returned from a Create Recurring Payment Plan
	 * request.
	 * https://apidocs.securenet.com/docs/recurringbilling.html?lang=csharp
	 * #recurring
	 */
	private String recurringBillingCreateRecurringPaymentPlanRequestReturnsSuccessfully(
			String customerId, String paymentMethodId) throws Exception {
		// Arrange
		AddRecurringPaymentPlanRequest request = new AddRecurringPaymentPlanRequest();
		request.setCustomerId(customerId);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setPlan(getRecurringPaymentPlan(paymentMethodId));
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		AddRecurringPaymentPlanResponse response = (AddRecurringPaymentPlanResponse) controller
				.processRequest(apiContext, request,
						AddRecurringPaymentPlanResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getStoredRecurringPaymentPlan().getSoftDescriptor(), request.getPlan().getSoftDescriptor());

		return response.getPlanId();
	}

	private RecurringPaymentPlan getRecurringPaymentPlan(String paymentMethodId) {
		RecurringPaymentPlan plan = new RecurringPaymentPlan();
		plan.setCycleType("monthly");
        plan.setDayOfTheMonth(1);
        plan.setDayOfTheWeek(1);
        plan.setMonth(6);
        plan.setFrequency(10);
        plan.setAmount(22.95);
        plan.setStartDate(DateUtils.addDays(new Date(), 4));
        plan.setEndDate(DateUtils.addYears(new Date(), 2));
        plan.setMaxRetries(4);
        plan.setPrimaryPaymentMethodId(paymentMethodId);
        plan.setNotes("This is a recurring plan");
        plan.setActive(true);
		plan.setSoftDescriptor("Rec Plan SoftDesc");
		return plan;
	}

	/**
	 * Successful response returned from an Update Recurring Payment Plan
	 * request.
	 * https://apidocs.securenet.com/docs/recurringbilling.html?lang=csharp
	 * #updaterecurring
	 */
	private void recurringBillingUpdateRecurringPaymentPlanRequestReturnsSuccessfully(
			String customerId, String planId) throws Exception {
		// Arrange
		UpdateRecurringPaymentPlanRequest request = new UpdateRecurringPaymentPlanRequest();
		request.setCustomerId(customerId);
		request.setPlanId(planId);
		request.setIncludeRawObjects(true);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setIncludeRequest(true);
		request.setPlan(getRecurringPaymentPlan(null));
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		UpdateRecurringPaymentPlanResponse response = (UpdateRecurringPaymentPlanResponse) controller.processRequest(apiContext, request,UpdateRecurringPaymentPlanResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getStoredRecurringPaymentPlan().getSoftDescriptor(), request.getPlan().getSoftDescriptor());
	}

	// Delete the Customer
	// TODO
	/**
	 * Successful response returned from an Create Installment Plan request.
	 * https://apidocs.securenet.com/docs/recurringbilling.html?lang=csharp#
	 * installment
	 */
	private String recurringBillingCreateInstallmentPlanRequestReturnsSuccessfully(
			String customerId, String paymentMethodId) throws Exception {
		// Arrange
		AddInstallmentPaymentPlanRequest request = new AddInstallmentPaymentPlanRequest();
		request.setCustomerId(customerId);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setPlan(getInstallmentPlan(paymentMethodId));
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		AddInstallmentPaymentPlanResponse response = (AddInstallmentPaymentPlanResponse) controller
				.processRequest(apiContext, request,
						AddInstallmentPaymentPlanResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getStoredInstallmentPaymentPlan().getSoftDescriptor(), request.getPlan().getSoftDescriptor());

		return response.getPlanId();
	}

	private InstallmentPaymentPlan getInstallmentPlan(String paymentMethodId) {
		InstallmentPaymentPlan plan = new InstallmentPaymentPlan();
		plan.setCycleType("monthly");
        plan.setDayOfTheMonth(1);
        plan.setDayOfTheWeek(1);
        plan.setFrequency(1);
        plan.setNumberOfPayments(12);
        plan.setInstallmentAmount(276.95);
        plan.setStartDate(DateUtils.addDays(new Date(), 4));
        plan.setEndDate(DateUtils.addYears(new Date(), 1));
        plan.setMaxRetries(4);
        plan.setNotes("This is a installment plan");
        plan.setActive(true);
        plan.setPrimaryPaymentMethodId(paymentMethodId);
		plan.setSoftDescriptor("Inst Plan SoftDesc");
		return plan;
	}

	private VariablePaymentPlan getVariablePaymentPlan(String paymentMethodId) {
		VariablePaymentPlan plan = new VariablePaymentPlan();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		try {
			plan.setPlanStartDate(format.parse("07/02/2017"));
			plan.setPlanEndDate(format.parse("08/05/2018"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		plan.setMaxRetries(4);
		plan.setPrimaryPaymentMethodId(paymentMethodId);
		plan.setNotes("This is a variable plan");
		plan.setSoftDescriptor("Var Plan SoftDesc");
		plan.setScheduledPayments(getSchedule());
		return plan;
	}

	/**
	 * Successful response returned from an Update Installment Plan request.
	 * https://apidocs.securenet.com/docs/recurringbilling.html?lang=csharp#
	 * updateinstallment
	 */
	private void recurringBillingUpdateInstallmentPlanrequestReturnsSuccessfully(
			String customerId, String planId) throws Exception {
		// Arrange
		UpdateInstallmentPaymentPlanRequest request = new UpdateInstallmentPaymentPlanRequest();
		request.setCustomerId(customerId);
		request.setPlanId(planId);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setPlan(getInstallmentPlan(null));
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		UpdateInstallmentPaymentPlanResponse response = (UpdateInstallmentPaymentPlanResponse) controller.processRequest(apiContext, request,UpdateInstallmentPaymentPlanResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getStoredInstallmentPaymentPlan().getSoftDescriptor(), request.getPlan().getSoftDescriptor());
	}

	/**
	 * Successful response returned from an Create Variable Payment Plan
	 * request.
	 * https://apidocs.securenet.com/docs/recurringbilling.html?lang=csharp
	 * #variable
	 */
	private String recurringBillingCreateVariablePaymentPlanRequestReturnsSuccessfully(
			String customerId, String paymentMethodId) throws Exception {
		// Arrange
		AddVariablePaymentPlanRequest request = new AddVariablePaymentPlanRequest();
		request.setCustomerId(customerId);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setPlan(getVariablePaymentPlan(paymentMethodId));
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		AddVariablePaymentPlanResponse response = (AddVariablePaymentPlanResponse) controller
				.processRequest(apiContext, request,
						AddVariablePaymentPlanResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getStoredVariablePaymentPlan().getSoftDescriptor(), request.getPlan().getSoftDescriptor());

		return response.getPlanId();
	}

	/**
	 * Successful response returned from an Update Variable Payment Plan
	 * request.
	 * https://apidocs.securenet.com/docs/recurringbilling.html?lang=csharp
	 * #updatevariable
	 */
	private void recurringBillingUpdateVariablePaymentPlanRequestReturnsSuccessfully(
			String customerId, String planId) throws Exception {
		// Arrange
		UpdateVariablePaymentPlanRequest request = new UpdateVariablePaymentPlanRequest();
		request.setCustomerId(customerId);
		request.setPlanId(planId);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setPlan(getVariablePaymentPlan("1"));
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		UpdateVariablePaymentPlanResponse response = (UpdateVariablePaymentPlanResponse) controller.processRequest(apiContext, request,UpdateVariablePaymentPlanResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getStoredVariablePaymentPlan().getSoftDescriptor(), request.getPlan().getSoftDescriptor());
	}

	/**
	 * Successful response returned from a Retrieve Payment Plan request.
	 * https:/
	 * /apidocs.securenet.com/docs/recurringbilling.html?lang=csharp#retrieve
	 */
	private void recurringBillingRetrievePaymentPlanRequestReturnsSuccessfully(
			String customerId, String planId) throws Exception {
		// Arrange
		RetrievePaymentPlanRequst request = new RetrievePaymentPlanRequst();
		request.setCustomerId(customerId);
		request.setPlanId(planId);
		APIContext apiContext = new APIContext();
		CustomersController controller = new CustomersController();
		// Act
		/* [UNSUPPORTED] 'var' as type is unsupported "var" */
		RetrievePaymentPlanResponse response = (RetrievePaymentPlanResponse) controller.processRequest(apiContext, request,RetrievePaymentPlanResponse.class);
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		// Assert
	}

	private VaultCustomerAndPaymentMethodRequest getVaultCustomerAndPaymentMethodRequest(){
		VaultCustomerAndPaymentMethodRequest customerRequest = new VaultCustomerAndPaymentMethodRequest();
		customerRequest.setFirstName("Test");
		customerRequest.setLastName("Tester");
		customerRequest.setPhoneNumber("512-122-1211");
		customerRequest.setEmailAddress("some@emailaddress.com");
		customerRequest.setSendEmailReceipts(true);
		customerRequest.setNotes("test notes");
		customerRequest.setCompany("Test Company");
		
		customerRequest.setCard(getCard());
		customerRequest.setPrimary(true);

		customerRequest.setAddress(getAddress());
		customerRequest.setDeveloperApplication(getDeveloperApplication());

		UserDefinedField udf = new UserDefinedField();
		udf.setUdfName("UDF1");
		udf.setUdfValue("UDF1vAL");
		List<UserDefinedField> usfList = new ArrayList<UserDefinedField>();
		usfList.add(udf);
		customerRequest.setUserDefinedFields(usfList);
		return customerRequest;
	}

	private CreateCustomerRequest getCreateCustomerRequestObject() {
		CreateCustomerRequest customerRequest = new CreateCustomerRequest();
		customerRequest.setFirstName("Test"+String.valueOf(Math.random()));
		customerRequest.setLastName("Tester");
		customerRequest.setPhoneNumber("512-122-1211");
		customerRequest.setEmailAddress("some@emailaddress.com");
		customerRequest.setSendEmailReceipts(true);
		customerRequest.setNotes("test notes");
		customerRequest.setCompany("Test Company");

		customerRequest.setAddress(getAddress());
		customerRequest.setDeveloperApplication(getDeveloperApplication());

		UserDefinedField udf = new UserDefinedField();
		udf.setUdfName("UDF1");
		udf.setUdfValue("UDF1vAL");
		List<UserDefinedField> usfList = new ArrayList<UserDefinedField>();
		usfList.add(udf);
		customerRequest.setUserDefinedFields(usfList);
		return customerRequest;
	}

	private Address getAddress() {
		Address address = new Address();
		address.setCity("Austin");
		address.setCountry("US");
		address.setLine1("123 Main St.");
		address.setState("TX");
		address.setZip("78759");
		return address;
	}

	private DeveloperApplication getDeveloperApplication() {
		DeveloperApplication devApp = new DeveloperApplication();
		devApp.setDeveloperId(Integer.parseInt(config.getProperty("developerId")));
		devApp.setVersion(config.getProperty("versionId"));
		return devApp;
	}
	
	private Card getCard(){
		Card card = new Card();
		card.setAddress(getAddress());
		card.setCvv("123");
		card.setExpirationDate("07/2018");
		card.setNumber("5555111111111111");
		return card;
	}
	
	private PaymentVaultToken getPaymentVaultToken(String customerId, String paymentMethodId) {
		PaymentVaultToken paymentVaultToken = new PaymentVaultToken();
		paymentVaultToken.setCustomerId(customerId);
		paymentVaultToken.setPaymentMethodId(paymentMethodId);
		paymentVaultToken.setPaymentType("CREDIT_CARD");
		return paymentVaultToken;
	}

	private List<Schedule> getSchedule() {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		List<Schedule> list = new ArrayList<Schedule>();
		Schedule schedule = new Schedule();
		schedule.setInstallmentDate("08/05/2017");
		schedule.setAmount(4);
		try {
			schedule.setPaymentdate(format.parse("08/06/2017"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		schedule.setScheduleId(1093920);
		list.add(schedule);
		return list;
	}

}
