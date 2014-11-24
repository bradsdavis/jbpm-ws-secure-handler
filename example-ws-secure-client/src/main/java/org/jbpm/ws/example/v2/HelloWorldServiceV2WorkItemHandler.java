package org.jbpm.ws.example.v2;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.jbpm.integration.ws.AbstractJaxWSWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldServiceV2WorkItemHandler extends AbstractJaxWSWorkItemHandler<HelloWorldService> {

	public static final Logger LOG = LoggerFactory.getLogger(HelloWorldServiceV2WorkItemHandler.class);
	
	public HelloWorldServiceV2WorkItemHandler() {
		super(HelloWorldService.class);
	}

	/** 
	 * Hooks the web service execution logic to the work item context.
	 */
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		//get the request parameter...
		String first = (String)workItem.getParameter("first");
		String last = (String)workItem.getParameter("last");
		
		try {
			HelloWorldService helloWorldService = getClient(workItem);
			String response = helloWorldService.sayHello(first, last);
			LOG.info("Response: "+response);
			
			//wire in the web service response...
			workItem.getResults().put("response", response);
			
			//complete the work.
			manager.completeWorkItem(workItem.getId(), workItem.getResults());
		}
		catch(Exception e) {
			LOG.error("Exception executing web service call.", e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void enhanceClientProxy(WorkItem workItem, BindingProvider bindingProvider, Client client) {
		Map<String, Object> props = new HashMap<String, Object>();
        props.put("action", "UsernameToken");
        props.put("user", "admin");
        props.put("passwordType", "PasswordText");
        WSS4JOutInterceptor wss4jOut = new WSS4JOutInterceptor(props);
        client.getOutInterceptors().add(wss4jOut);
        bindingProvider.getRequestContext().put("password", "admin");
	}
}
