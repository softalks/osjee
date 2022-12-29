package com.softalks.osjee;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.apache.felix.http.proxy.ProxyListener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.startlevel.BundleStartLevel;

import com.softalks.dsu.Server;

@WebListener
@SuppressWarnings("deprecation")
public class Listener extends ProxyListener implements Consumer<BundleContext> {
	
	private Server server;
	private ServletContext servletContext;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		servletContext = event.getServletContext();
		server = new Server(this, "/contexts" + servletContext.getContextPath());
		ServletContext context = event.getServletContext();
		String attributeName = server.getClass().getName();
		context.setAttribute(attributeName, server);
		super.contextInitialized(event);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
		server.stop();
	}

	@Override
	public void accept(BundleContext bundleContext) {
		String id = "org.apache.felix.http.bridge";
		Bundle bundle = bundleContext.getBundle(id);
		if (bundle == null) {
			try (InputStream is = getClass().getClassLoader().getResourceAsStream(id + "-4.2.2.jar")) {
				bundle = bundleContext.installBundle(id, is);
				bundle.adapt(BundleStartLevel.class).setStartLevel(1);
				bundle.start();
			} catch (BundleException | IOException e) {
				throw new IllegalStateException(e);
			}
		}
		servletContext.setAttribute(BundleContext.class.getName(), bundleContext);
	}

}