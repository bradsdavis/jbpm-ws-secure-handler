package org.jbpm.ws.test;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import org.jbpm.ws.example.v1.HelloWorldService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestV1Execution {

	private static final Logger LOG = LoggerFactory.getLogger(TestV1Execution.class);

	@Test
	public void testWebServiceWithBasicSecurity() throws Exception {
		String wsdl = "http://localhost:8080/example-ws-secure/HelloWorldServiceImplV1?wsdl";
		String localPart = "HelloWorldServiceImplV1";
		String uri = "http://v1.example.ws.jbpm.org/";
		
		String result = testWsWithUserPass(wsdl, localPart, uri, "Brad", true);
		
		org.junit.Assert.assertTrue("Hello Brad!".equals(result));
	}

	@Test
	public void testWebServiceNoSecurity() throws Exception {
		String wsdl = "http://localhost:8080/example-ws-secure/HelloWorldServiceImplV1?wsdl";
		String localPart = "HelloWorldServiceImplV1";
		String uri = "http://v1.example.ws.jbpm.org/";
		
		try {
			testWsWithUserPass(wsdl, localPart, uri, "Brad", false);
		}
		catch(Exception e) {
			//EXPECT AN EXCEPTION!!
			if(e instanceof WebServiceException) {
				String result = e.getCause().getMessage();
				org.junit.Assert.assertTrue(result.contains("401: Unauthorized"));
				return;
			}
		}
		
		org.junit.Assert.fail("Should have had a 401 exception.");
	}
	

	
	
	public String testWsWithUserPass(String wsdl, String localPart, String uri, String name, boolean secure) throws Exception {
		
		LOG.info("Creating client: ["+wsdl+"] localPart["+localPart+"] uri["+uri+"]");
		
		URL wsdlURL = new URL(wsdl);
		QName serviceName = new QName(uri, localPart);
		Service service = Service.create(wsdlURL, serviceName);
		LOG.info("Reflecting Type to: "+HelloWorldService.class);
		
		HelloWorldService client = (HelloWorldService)service.getPort(HelloWorldService.class);

		BindingProvider bindingProvider = ((BindingProvider)client);
		
		if(secure) {
			//USER & PASSWORD
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(BindingProvider.USERNAME_PROPERTY, "admin");
			requestContext.put(BindingProvider.PASSWORD_PROPERTY, "admin");
		}

		String result = client.sayHello(name);
		LOG.info(result);
		return result;
	}
	
}
