package org.jbpm.integration.ws;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a JAX-WS Proxy service, with hooks for enhancing the binding provider.
 * 
 * @author bradsdavis
 *
 * @param <T> Java Interface representing Web Service
 */
public abstract class AbstractJaxWSWorkItemHandler<T> implements WorkItemHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractJaxWSWorkItemHandler.class);
	private T client;
	protected final Class<T> clz;
	
	public AbstractJaxWSWorkItemHandler(Class<T> clz) {
		this.clz = clz;
	}
	
	protected Class<T> getClientType() {
		return this.clz;
	}
	
	/**
	 * Creates a client from the WSDL and QName for the Web Service.
	 * By overriding the protected methods, you can add WSSecurity support, or extract necessary properties from system properties, work item values, etc.
	 * 
	 * @see QName, {@link Service}, {@link BindingProvider}
	 * @param workItem
	 * @return
	 * @throws WSExecutionException
	 */
	protected T getClient(WorkItem workItem) throws WSExecutionException {
		if(client==null)
		{
			String wsdl = getWsdlURL(workItem);
			String namespaceURI = getNamespaceURI(workItem);
			String localPart = getLocalPart(workItem);
			
			try {
				if(LOG.isDebugEnabled()) {
					LOG.debug("Creating client: ["+wsdl+"] localPart["+localPart+"] uri["+namespaceURI+"]");
				}
				
				URL wsdlURL = new URL(wsdl);
				QName serviceName = new QName(namespaceURI, localPart);
				Service service = Service.create(wsdlURL, serviceName);
				LOG.info("Reflecting Type to: "+getClientType());
				
				client = (T)service.getPort(getClientType());

				BindingProvider bindingProvider = ((BindingProvider)client); 
				enhanceBindingProvider(workItem, bindingProvider);
				enhanceRequestContext(workItem, bindingProvider.getRequestContext());
				enhanceResponseContext(workItem, bindingProvider.getResponseContext());
				
			} catch(Exception e)
			{
				throw new WSExecutionException("Exception creating service:"+wsdl, e);
			}
		}
		return client;
	}
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		//by default, do nothing.
	}

	/**
	 * Maps the WSDL address for client execution for {@link Service}.
	 * @see Service
	 * @param workItem
	 * @return
	 */
	protected String getWsdlURL(WorkItem workItem) {
		return (String)workItem.getParameter("wsdl");
	}
	
	/**
	 * Maps the NamespaceURI for client execution for {@link QName}.
	 * @see QName
	 * @param workItem
	 * @return
	 */
	protected String getNamespaceURI(WorkItem workItem) {
		return (String)workItem.getParameter("namespaceURI");
	}
	
	/**
	 * Maps the LocalPart for client execution for {@link QName}.
	 * @see QName
	 * @param workItem
	 * @return
	 */
	protected String getLocalPart(WorkItem workItem) {
		return (String)workItem.getParameter("localPart");
	}
	
	/**
	 * Used to add binding information prior to / after executing the web service call.
	 * @see BindingProvider
	 */
	protected void enhanceBindingProvider(WorkItem workItem, BindingProvider bindingProvider) {}
	

	/**
	 * Used to add request context information prior to executing the web service call.
	 * @see BindingProvider
	 */
	protected void enhanceRequestContext(WorkItem workItem, Map<String, Object> requestContext) {}
	

	/**
	 * Used to add response context information after to executing the web service call.
	 * @see BindingProvider
	 */
	protected void enhanceResponseContext(WorkItem workItem, Map<String, Object> responseContext) {}
}
