//
//

package test.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import SNET.Core.APIContext;
import SecureNetRestApiSDK.Api.Controllers.PaymentsController;
import SecureNetRestApiSDK.Api.Models.AdditionalTerminalInfo;
import SecureNetRestApiSDK.Api.Models.Address;
import SecureNetRestApiSDK.Api.Models.Card;
import SecureNetRestApiSDK.Api.Models.Check;
import SecureNetRestApiSDK.Api.Models.DeveloperApplication;
import SecureNetRestApiSDK.Api.Models.ExtendedInformation;
import SecureNetRestApiSDK.Api.Requests.AuthorizeRequest;
import SecureNetRestApiSDK.Api.Requests.ChargeRequest;
import SecureNetRestApiSDK.Api.Requests.CreditRequest;
import SecureNetRestApiSDK.Api.Requests.PriorAuthCaptureRequest;
import SecureNetRestApiSDK.Api.Requests.RefundRequest;
import SecureNetRestApiSDK.Api.Requests.VoidRequest;
import SecureNetRestApiSDK.Api.Responses.AuthorizeResponse;
import SecureNetRestApiSDK.Api.Responses.ChargeResponse;
import SecureNetRestApiSDK.Api.Responses.CreditResponse;
import SecureNetRestApiSDK.Api.Responses.PriorAuthCaptureResponse;
import SecureNetRestApiSDK.Api.Responses.RefundResponse;
import SecureNetRestApiSDK.Api.Responses.VoidResponse;
import test.HelperTest;

public class PaymentsControllerTest {
	
	Properties config ;
	HelperTest helper;
	
	@Before
	public void before() throws Exception{
		InputStream stream  = this.getClass().getResourceAsStream("/config.properties");
		config = new Properties();
		config.load(stream);
		helper = new HelperTest();
	}
	/**
	 * Unit Tests for an AuthorizationOnly request and a subsequent
	 * PriorAuthCapture request. Tests combined in one method to pass the
	 * required transaction identifier and guaranteee the order of operation.
	 */
	@Test
	public void creditCardPresentAuthorizationOnlyAndPriorAuthCaptureRequestsReturnsSuccessfully()
			throws Exception {
		// Arrange
		int transactionId = creditCardPresentAuthorizationOnlyRequestReturnsSuccessfully();
		PriorAuthCaptureRequest request = new PriorAuthCaptureRequest();
		request.setAmount(20d);
		request.setTransactionId(transactionId);
		request.setDeveloperApplication(getDeveloperApplication());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		PriorAuthCaptureResponse response = (PriorAuthCaptureResponse) controller.processRequest(apiContext, request, PriorAuthCaptureResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
	}

	/**
	 * Successful response returned from a Credit Card Present Authorization
	 * Only request.
	 * https://apidocs.securenet.com/docs/creditcardpresent.html?lang
	 * =JSON#authonly
	 */
	private int creditCardPresentAuthorizationOnlyRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		AuthorizeRequest request = new AuthorizeRequest();
		request.setCard(getCard());
		request.setAddToVault(true);
		request.setAmount(20d);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setExtendedInformation(getExtendedInformation());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		AuthorizeResponse response = (AuthorizeResponse) controller.processRequest(apiContext, request,AuthorizeResponse.class);
		// Assert

		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());
		return response.getTransaction().getTransactionId();
	}


	/**
	 * Successful response returned from a Credit Card Present Charge request.
	 * https
	 * ://apidocs.securenet.com/docs/creditcardpresent.html?lang=JSON#charge
	 */
	@Test
	public void creditCardPresentChargeRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setAddToVault(true);
		request.setAmount(20d);
		request.setCard(getCard());
		request.setDeveloperApplication(getDeveloperApplication());
		request.setExtendedInformation(getExtendedInformation());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());
	}

	/**
	 * Unit Tests for an IncludeTip AuthorizationOnly request and a subsequent
	 * PriorAuthCapture request. Tests combined in one method to pass the
	 * required transaction identifier and guaranteee the order of operation.
	 */
	@Test
	public void creditCardPresentIncludeTipAuthorizationOnlyAndPriorAuthCaptureRequestsReturnsSuccessfully()
			throws Exception {
		int transactionId = creditCardPresentIncludeTipAuthorizationOnlyRequestReturnsSuccessfully();
		// Arrange
		PriorAuthCaptureRequest request = new PriorAuthCaptureRequest();
		request.setAmount(20d);
		request.setTransactionId(transactionId);
		request.setDeveloperApplication(getDeveloperApplication());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		PriorAuthCaptureResponse response = (PriorAuthCaptureResponse) controller.processRequest(apiContext, request,PriorAuthCaptureResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
	}

	/**
	 * Successful response returned from a Credit Card Present Include Tip
	 * AuthorizationOnly request.
	 * https://apidocs.securenet.com/docs/creditcardpresent
	 * .html?lang=JSON#includetip
	 */
	private int creditCardPresentIncludeTipAuthorizationOnlyRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		AuthorizeRequest request = new AuthorizeRequest();
		request.setCard(getCard());
		request.setAmount(20d);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setExtendedInformation(getExtendedInformation());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		AuthorizeResponse response = (AuthorizeResponse) controller.processRequest(apiContext, request,AuthorizeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());

		return response.getTransaction().getTransactionId();
	}


	/**
	 * Successful response returned from a Credit Card Present Charge request
	 * that includes the address.
	 * https://apidocs.securenet.com/docs/creditcardpresent
	 * .html?lang=JSON#includeaddress
	 */
	@Test
	public void creditCardPresentChargeRequestIncludingAddressReturnsSuccessfully()
			throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setCard(getCard());
		request.setDeveloperApplication(getDeveloperApplication());
		request.setAmount(20d);
		request.setExtendedInformation(getExtendedInformation());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());
	}

	/**
	 * Unit Tests for an AuthorizationOnly request and a subsequent
	 * PriorAuthCapture request. Tests combined in one method to pass the
	 * required transaction identifier and guaranteee the order of operation.
	 */
	@Test
	public void creditCardNotPresentAuthorizationOnlyAndPriorAuthCaptureRequestsReturnsSuccessfully()
			throws Exception {
		// Arrange
		int transactionId = creditCardNotPresentAuthorizationOnlyRequestReturnsSuccessfully();
		PriorAuthCaptureRequest request = new PriorAuthCaptureRequest();
		request.setAmount(10d);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setTransactionId(transactionId);
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		PriorAuthCaptureResponse response = (PriorAuthCaptureResponse) controller.processRequest(apiContext, request,PriorAuthCaptureResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
	}

	/**
	 * Successful response returned from a Credit Card Not Present Authorization
	 * Only request.
	 * https://apidocs.securenet.com/docs/creditcardnotpresent.html
	 * ?lang=JSON#authonly
	 */
	private int creditCardNotPresentAuthorizationOnlyRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		AuthorizeRequest request = new AuthorizeRequest();
		request.setDeveloperApplication(getDeveloperApplication());
		request.setCard(getCard());
		request.setAddToVault(true);
		request.setAmount(20d);
		request.setExtendedInformation(getExtendedInformation());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		AuthorizeResponse response = (AuthorizeResponse) controller.processRequest(apiContext, request,AuthorizeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());

		return response.getTransaction().getTransactionId();
	}


	/**
	 * Successful response returned from a Charge-Authorization and Capture
	 * request.
	 * https://apidocs.securenet.com/docs/creditcardnotpresent.html?lang
	 * =JSON#charge
	 */
	@Test
	public void creditCardNotPresentChargeAuthorizationAndCaptureRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setCard(getCard());
		request.setAddToVault(true);
		request.setAmount(100d);
		request.setDeveloperApplication(getDeveloperApplication());
		ExtendedInformation extendedInfo = getExtendedInformation();
		extendedInfo.setTypeOfGoods("PHYSICAL");
		request.setExtendedInformation(extendedInfo);
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());
	}

	/**
	 * Successful response returned from an Include Address request.
	 * https://apidocs
	 * .securenet.com/docs/creditcardnotpresent.html?lang=JSON#includeaddress
	 */
	@Test
	public void creditCardNotPresentIncludeAddresRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setCard(getCard());
		request.setAmount(80d);
		request.setDeveloperApplication(getDeveloperApplication());
		ExtendedInformation extendedInfo = getExtendedInformation();
		extendedInfo.setTypeOfGoods("PHYSICAL");
		request.setExtendedInformation(extendedInfo);
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());
	}

	/**
	 * Successful response returned from an Charge using Tokenization request.
	 * https://apidocs.securenet.com/docs/creditcardnotpresent.html?lang=csharp#
	 * tokenization
	 */
	private void creditCardNotPresentChargeUsingTokenizationRequestReturnsSuccessfully(
			String token) throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setCard(getCard());
		request.setAmount(80d);
		request.setDeveloperApplication(getDeveloperApplication());
		ExtendedInformation extendedInfo = getExtendedInformation();
		extendedInfo.setTypeOfGoods("PHYSICAL");
		request.setExtendedInformation(extendedInfo);
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());
	}

	/**
	 * Successful response returned from an ACH Pay By Check Charge request.
	 * https://apidocs.securenet.com/docs/ach.html?lang=csharp#charge
	 */
	@Test
	public void achPayByCheckChargeRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setCheck(getCheck());
		request.setAmount(100d);
		request.setDeveloperApplication(getDeveloperApplication());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
	}

	/**
	 * Successful response returned from an ACH POS Charge request.
	 * https://apidocs.securenet.com/docs/ach.html?lang=csharp#chargepos
	 */
	@Test
	public void achChargeAccountUsingPOSRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();

		ExtendedInformation extendedInfo = new ExtendedInformation();
		AdditionalTerminalInfo additionalInfo = new AdditionalTerminalInfo();
		additionalInfo.setStoreNumber("452");
		additionalInfo.setTerminalCity("Austin");
		additionalInfo.setTerminalId("1234");
		additionalInfo.setTerminalLocation("Office");
		additionalInfo.setTerminalState("TX");
		extendedInfo.setAdditionalTerminalInfo(additionalInfo);
		
		Check check = getCheck();
		check.setCheckType("POINT_OF_SALE");
		check.setVerification("NONE");
		request.setCheck(check);
		
		request.setExtendedInformation(extendedInfo);
		request.setAmount(11d);
		request.setDeveloperApplication(getDeveloperApplication());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());
	}

	/**
	 * Successful response returned from an ACH Add Billing Address Charge
	 * request.
	 * https://apidocs.securenet.com/docs/ach.html?lang=csharp#billaddress
	 */
	@Test
	public void achAddBillingAddressChargeRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		
		Check check = getCheck();
		check.setAddress(getAddress());
		
		request.setCheck(check);
		request.setAmount(11d);
		request.setDeveloperApplication(getDeveloperApplication());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
	}

	/**
	 * Successful response returned from an ACH Paying By Check with
	 * Verification Charge request.
	 * https://apidocs.securenet.com/docs/ach.html?lang=csharp#verification
	 */
	@Test
	public void achPayingByCheckWithVerificationChargeRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		
		Check check = getCheck();
		check.setVerification("ACH_PROVIDER");
		
		request.setCheck(check);
		request.setAmount(11d);
		request.setDeveloperApplication(getDeveloperApplication());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
	}

	/**
	 * Successful response returned from a Credit An Account request.
	 * https://apidocs.securenet.com/docs/credits.html?lang=csharp
	 */
	@Test
	public void creditsCreditAnAccountRequestReturnsSuccessfully()
			throws Exception {
		// Arrange
		CreditRequest request = new CreditRequest();
		request.setCard(getCard());
		request.setAmount(1.05d);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setExtendedInformation(getExtendedInformation());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		CreditResponse  response = (CreditResponse) controller.processRequest(apiContext, request,CreditResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());
	}

	/**
	 * Unit Tests for a Charge request and a subsequent Refund request. Tests
	 * combined in one method to pass the required transaction identifier and
	 * guaranteee the order of operation.
	 */
	@Test
	public void refundsChargeAndRefundRequestsReturnsSuccessfully()
			throws Exception {
		int transactionId = refundsChargeRequestReturnsSuccessfully();
		RefundRequest request = new RefundRequest();
		request.setDeveloperApplication(getDeveloperApplication());
		request.setTransactionId(transactionId);
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		RefundResponse response = (RefundResponse) controller.processRequest(apiContext, request,RefundResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
	}

	/**
	 * Successful response returned from a Credit Card Present Charge request.
	 * https
	 * ://apidocs.securenet.com/docs/creditcardpresent.html?lang=JSON#charge
	 */
	private int refundsChargeRequestReturnsSuccessfully() throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setCard(getCard());
		request.setAmount(10d);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setExtendedInformation(getExtendedInformation());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());

		return response.getTransaction().getTransactionId();
	}

	/**
	 * Unit Tests for a Chrage request and a subsequent Void request. Tests
	 * combined in one method to pass the required transaction identifier and
	 * guaranteee the order of operation.
	 */
	@Test
	public void voidChargeAndVoidRequestsReturnsSuccessfully()
			throws Exception {
		int transactionId = voidChargeRequestReturnsSuccessfully();
		VoidRequest request = new VoidRequest();
		request.setTransactionId(transactionId);
		request.setDeveloperApplication(getDeveloperApplication());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		VoidResponse response = (VoidResponse) controller.processRequest(apiContext, request,VoidResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
	}

	/**
	 * Successful response returned from a Credit Card Present Charge request.
	 * https
	 * ://apidocs.securenet.com/docs/creditcardpresent.html?lang=JSON#charge
	 */
	private int voidChargeRequestReturnsSuccessfully() throws Exception {
		// Arrange
		ChargeRequest request = new ChargeRequest();
		request.setCard(getCard());
		request.setAmount(10d);
		request.setDeveloperApplication(getDeveloperApplication());
		request.setExtendedInformation(getExtendedInformation());
		APIContext apiContext = new APIContext();
		PaymentsController controller = new PaymentsController();
		// Act
		ChargeResponse response = (ChargeResponse) controller.processRequest(apiContext, request,ChargeResponse.class);
		// Assert
		Assert.assertTrue(response.toResponseString(), response.getSuccess());
		Assert.assertEquals(response.getTransaction().getSoftDescriptor(), helper.getResponseSoftDescriptor());
		Assert.assertEquals(response.getTransaction().getDynamicMCC(), helper.getResponseDynamicMCC());

		return response.getTransaction().getTransactionId();
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
		card.setNumber("4111111111111111");
		return card;
	}
	
	private Check getCheck() {
		Check check = new Check();
		check.setFirstName("Bruce");
		check.setLastName("Wayne");
		check.setRoutingNumber("222371863");
		check.setAccountNumber("123456");
		return check;
	}

	private ExtendedInformation getExtendedInformation() {
		ExtendedInformation extendedInfo = new ExtendedInformation();
		extendedInfo.setSoftDescriptor(helper.getRequestSoftDescriptor());
		extendedInfo.setDynamicMCC(helper.getRequestDynamicMCC());
		return extendedInfo;
	}
}
