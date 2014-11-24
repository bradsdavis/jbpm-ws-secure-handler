package org.jbpm.ws.test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.jbpm.ws.example.v2.HelloWorldService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestV2Execution {

	private static final Logger LOG = LoggerFactory.getLogger(TestV2Execution.class);

	@Test
	public void testWebServiceWithBasicSecurity() throws Exception {
		String wsdl = "http://localhost:8080/example-ws-secure/HelloWorldServiceImplV2?wsdl";
		String localPart = "HelloWorldServiceImplV2";
		String uri = "http://v2.example.ws.jbpm.org/";
		
		String result = testWsWithUserPass(wsdl, localPart, uri, "Brad", "Davis", true);
		
		org.junit.Assert.assertTrue("Hello Brad Davis!".equals(result));
	}

	@Test
	public void testWebServiceNoSecurity() throws Exception {
		String wsdl = "http://localhost:8080/example-ws-secure/HelloWorldServiceImplV2?wsdl";
		String localPart = "HelloWorldServiceImplV2";
		String uri = "http://v2.example.ws.jbpm.org/";
		
		try {
			testWsWithUserPass(wsdl, localPart, uri, "Brad", "Davis", false);
		}
		catch(Exception e) {
			//EXPECT AN EXCEPTION!!
			if(e instanceof SOAPFaultException) {
				String result = e.toString();
				org.junit.Assert.assertTrue(result.contains("An error was discovered processing the <wsse:Security> header"));
				return;
			}
		}
		
		org.junit.Assert.fail("Should have had a 401 exception.");
	}
	

	
	
	public String testWsWithUserPass(String wsdl, String localPart, String uri, String first, String last, boolean secure) throws Exception {
		
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

	        Client c = ClientProxy.getClient(client);
	        Map<String, Object> props = new HashMap<String, Object>();
	        props.put("action", "UsernameToken");
	        props.put("user", "admin");
	        props.put("passwordType", "PasswordText");
	        WSS4JOutInterceptor wss4jOut = new WSS4JOutInterceptor(props);
	        c.getOutInterceptors().add(wss4jOut);
	        ((BindingProvider)client).getRequestContext().put("password", "admin");
		}
		
		

		String result = client.sayHello(first, last);
		LOG.info(result);
		return result;
	}
	
}
