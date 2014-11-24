package org.jbpm.ws.example.v2;

import javax.jws.WebService;

import org.apache.cxf.interceptor.InInterceptors;

/**
 * See the article: http://depressedprogrammer.wordpress.com/2007/07/31/cxf-ws-security-using-jsr-181-interceptor-annotations-xfire-migration/
 * @author bradsdavis
 *
 */
@InInterceptors(interceptors="org.jbpm.ws.example.auth.WSSecurityInterceptor")
@WebService(name="HelloWorldServiceImplV2", serviceName="HelloWorldServiceImplV2", endpointInterface="org.jbpm.ws.example.v2.HelloWorldService")
public class HelloWorldServiceImpl implements HelloWorldService {

	@Override
	public String sayHello(String firstName, String lastName) {
		return "Hello "+firstName+" "+lastName+"!";
	}

}
