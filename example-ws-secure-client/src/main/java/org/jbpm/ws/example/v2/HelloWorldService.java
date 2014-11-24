package org.jbpm.ws.example.v2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.11
 * 2014-11-24T01:15:35.838-05:00
 * Generated source version: 2.7.11
 * 
 */
@WebService(targetNamespace = "http://v2.example.ws.jbpm.org/", name = "HelloWorldService")
@XmlSeeAlso({ObjectFactory.class})
public interface HelloWorldService {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://v2.example.ws.jbpm.org/", className = "org.jbpm.ws.example.v2.SayHello")
    @WebMethod
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://v2.example.ws.jbpm.org/", className = "org.jbpm.ws.example.v2.SayHelloResponse")
    public java.lang.String sayHello(
        @WebParam(name = "firstName", targetNamespace = "")
        java.lang.String firstName,
        @WebParam(name = "lastName", targetNamespace = "")
        java.lang.String lastName
    );
}
