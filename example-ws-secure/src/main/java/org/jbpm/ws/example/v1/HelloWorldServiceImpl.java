package org.jbpm.ws.example.v1;

import javax.jws.WebService;

import org.apache.cxf.interceptor.InInterceptors;

/**
 * Uses HTTP Basic Authentication.
 * @author bradsdavis
 *
 */
@InInterceptors(interceptors="org.jbpm.ws.example.auth.BasicAuthInterceptor")
@WebService(name="HelloWorldServiceImplV1", serviceName="HelloWorldServiceImplV1", endpointInterface="org.jbpm.ws.example.v1.HelloWorldService")
public class HelloWorldServiceImpl implements HelloWorldService {

	@Override
	public String sayHello(String name) {
		return "Hello "+name+"!";
	}

}
