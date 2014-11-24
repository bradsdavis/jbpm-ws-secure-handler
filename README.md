* **Build and deploy example-ws-secure to a clean server**
	* This should create the Web Service registered at: 
		* http://localhost:8080/example-ws-secure/HelloWorldServiceImplV1?wsdl 
	* The web service requires the HTTP Header AuthorizationPolicy, with username: admin and password: admin
	* The web service will result in a 401 UNAUTHORIZED result when the username and password aren't provided.

* **Build and Maven install: jaxws-base-handler**
	* This project contains an abstract Work Item Handler that can be extended for Web Service / Secure Web Service Invocations.

* **Build and Maven Install: example-ws-secure-client**
	* This project provides an example of leveraging jaxws-base-handler to execute Web Service / Secure Web Service Invocations.
	* This project's classes were generaged by the CXF Plugin that is commented out in the pom.xml.  
		* This can be leveraged as an example for creating Web Service Clients from WSDL files.
	* The class org.jbpm.ws.example.v1.HelloWorldServiceV1WorkItemHandler extends the AbstractJaxWSWorkItemHandler<HelloWorldService>
	* The method enhanceRequestContext is used to setup the request context, in this case, adding username and password for the HTTP BASIC Authentication of the example-ws-secure Web Service.
	* Other methods may be overriden such as getWsdlURL, getNamespaceURI, getLocalPart to direct the Work Item Handler on where to retrieve the required parameters.
		* You may override these methods, for example, to get the WSDL address from a System Property rather than from the Work Item's context, for example. 
