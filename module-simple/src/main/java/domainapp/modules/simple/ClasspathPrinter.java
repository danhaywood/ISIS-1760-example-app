package domainapp.modules.simple;

import org.apache.log4j.Logger;

public class ClasspathPrinter
{

	private static final Logger log = Logger.getLogger(ClasspathPrinter.class);
	static
	{
		
		log.debug("[Application Manifest Object Creation started]");
    	log.debug("[The Classpath is :" + System.getProperty("java.class.path"));
	}
	
	
}
