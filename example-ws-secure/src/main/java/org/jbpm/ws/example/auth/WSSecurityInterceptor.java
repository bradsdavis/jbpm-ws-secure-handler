package org.jbpm.ws.example.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSSecurityInterceptor extends AbstractPhaseInterceptor {
	 
    public WSSecurityInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }
    public WSSecurityInterceptor(String s) {
        super(Phase.PRE_PROTOCOL);
    }
 
	@Override
	public void handleMessage(Message message) throws Fault {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        props.put(WSHandlerConstants.PW_CALLBACK_REF, new UsernamePasswordCallback());
 
        WSS4JInInterceptor wss4jInHandler = new WSS4JInInterceptor(props);
        message.getInterceptorChain().add(wss4jInHandler);
        message.getInterceptorChain().add(new SAAJInInterceptor());		
	}
	
	public static class UsernamePasswordCallback implements CallbackHandler {
		private static final Logger LOG = LoggerFactory.getLogger(UsernamePasswordCallback.class);
		
		public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
			LOG.info("Calling UsernamePasswordCallback!");
			WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
			
			if ("admin".equals(pc.getIdentifier())) {
				pc.setPassword("admin");
			}
		}
	}
}