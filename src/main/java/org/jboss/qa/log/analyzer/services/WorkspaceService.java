package org.jboss.qa.log.analyzer.services;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.qa.log.analyzer.Configuration;

@ApplicationScoped
public class WorkspaceService {
	
	@Inject
	private Configuration configuration;
	
	public File getWorkspaceFor(String jenkins, String project, int build, String path) {
		File parent = getWorkspaceFile();
		StringBuilder filePath = new StringBuilder(parent.getAbsolutePath())
				.append(File.separator).append(jenkins)
				.append(File.separator).append(project)
				.append(File.separator).append(build)
				.append(File.separator).append(path);
		File f = new File(filePath.toString());
		
		if (!f.exists() && !f.mkdirs()) {
			throw new IllegalStateException("Creating of directory failed: " + f.getAbsolutePath());
		}
		return f;
	}
	
	private File getWorkspaceFile() {
		File f = new File(configuration.getWorkspaceLocation());
		if (!f.exists() && !f.mkdirs()) {
			throw new IllegalStateException("Creating of directory failed: " + f.getAbsolutePath());
		}
		return f;
	}

}
