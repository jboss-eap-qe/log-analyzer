package org.jboss.qa.log.analyzer;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Configuration {
	
	public int getSizeOfWindow() {
		return 500;
	}
	
	public String getWorkspaceLocation() {
		return System.getProperty("user.home") + "/.log-analyzer/workspace";
	}

}
