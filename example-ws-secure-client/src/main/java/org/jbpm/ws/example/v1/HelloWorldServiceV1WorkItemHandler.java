package org.jbpm.ws.example.v1;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.jbpm.integration.ws.AbstractJaxWSWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldServiceV1WorkItemHandler extends AbstractJaxWSWorkItemHandler<HelloWorldService> {

	public static final Logger LOG = LoggerFactory.getLogger(HelloWorldServiceV1WorkItemHandler.class);
	
	public HelloWorldServiceV1WorkItemHandler() {
		super(HelloWorldService.class);
	}

	/** 
	 * Hooks the web service execution logic to the work item context.
	 */
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		//get the request parameter...
		String name = (String)workItem.getParameter("name");
		
		try {
			HelloWorldService helloWorldService = getClient(workItem);
			String response = helloWorldService.sayHello(name);
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
	
	/**
	 * Used to add request context information prior to executing the web service call.
	 */
	@Override
	protected void enhanceRequestContext(WorkItem workItem, Map<String, Object> requestContext) {
		requestContext.put(BindingProvider.USERNAME_PROPERTY, "admin");
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, "admin");
	}
}
