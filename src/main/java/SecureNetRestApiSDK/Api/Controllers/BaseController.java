//
//

package SecureNetRestApiSDK.Api.Controllers;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import SNET.Core.APIContext;
import SNET.Core.Helper;
import SecureNetRestApiSDK.Api.Requests.SecureNetRequest;
import SecureNetRestApiSDK.Api.Responses.SecureNetResponse;

import com.google.gson.Gson;

public abstract class BaseController {
	public SecureNetResponse processRequest(APIContext apiContext, SecureNetRequest secureNetRequest, Class<? extends SecureNetResponse> responseClazz) throws Exception {
    	try{
	        if (secureNetRequest == null) {
	            throw new IllegalArgumentException("secureNetRequest");
	        }
	
	        String payLoad =new Gson().toJson(secureNetRequest);
	
	        Properties config = new Properties();
	        Dictionary<String, String> props = new Hashtable<String, String>();
	
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream stream =  cl.getResourceAsStream("config.properties");
		if (stream == null) {
		   ClassLoader applicationClassLoader = BaseController.class.getClassLoader();
		   stream = applicationClassLoader.getResourceAsStream("config.properties");
		}

		if (stream == null) {
		   throw new IllegalArgumentException("config.properties not found");
		}
	        config.load(stream);
	
	        props.put("secureNetId", config.getProperty("secureNetId"));
	        props.put("secureKey", config.getProperty("secureKey"));
	        props.put("mode", config.getProperty("mode"));
	        props.put("endpoint", config.getProperty("endpoint"));
	        props.put("timeout", config.getProperty("timeout"));
	        props.put("connectionTimeout", config.getProperty("connectionTimeout"));
	        props.put("developerId", config.getProperty("developerId"));
	        props.put("versionId", config.getProperty("versionId"));
	        apiContext.setConfig(props);
	        String response = Helper.configureAndExecute(apiContext, secureNetRequest.getMethod(), secureNetRequest.getUri(), payLoad);
  	      	return (SecureNetResponse) new Gson().fromJson(response,responseClazz);
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    		throw ex;
    	}
    }
}
