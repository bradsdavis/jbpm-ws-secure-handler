package org.jbpm.ws.example.v2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloWorldService {

	@WebMethod
	String sayHello(@WebParam(name="firstName") String firstName, @WebParam(name="lastName") String lastName);
 
}
