package pt.unparallel.fiesta.tps.rest;

import java.util.Set;
import java.util.HashSet;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.unparallel.fiesta.tps.rest.TpiApiTestbedProviderServices;

@ApplicationPath("")
public class JaxRsActivator extends Application {

	final static Logger logger = LoggerFactory.getLogger(JaxRsActivator.class.getName());

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	
	/**
	 * The default constructor
	 */
	public JaxRsActivator() {
		
		singletons.add(new TpiApiTestbedProviderServices());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
