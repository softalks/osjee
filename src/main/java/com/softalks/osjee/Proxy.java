package com.softalks.osjee;

import javax.servlet.ServletException;

import org.apache.felix.http.proxy.AbstractProxyServlet;
import org.osgi.framework.BundleContext;

public class Proxy extends AbstractProxyServlet {

	private static final long serialVersionUID = 1L;

	protected BundleContext getBundleContext() throws ServletException {
		Object context = getServletContext().getAttribute(BundleContext.class.getName());
		if (context instanceof BundleContext) {
			return (BundleContext) context;
		}
		throw new ServletException("Missing servlet context attribute: " + BundleContext.class.getName());
	}

}