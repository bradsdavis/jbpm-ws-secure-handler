package org.jbpm.ws.example.v2;

import javax.jws.WebService;

@WebService(name="HelloWorldServiceImplV2", serviceName="HelloWorldServiceImplV2", endpointInterface="org.jbpm.ws.example.v2.HelloWorldService")
public class HelloWorldServiceImpl implements HelloWorldService {

	@Override
	public String sayHello(String firstName, String lastName) {
		return "Hello "+firstName+" "+lastName+"!";
	}

}
