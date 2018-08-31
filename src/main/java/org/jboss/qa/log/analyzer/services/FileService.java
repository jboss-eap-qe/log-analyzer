package org.jboss.qa.log.analyzer.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.IOUtils;
import org.jboss.qa.log.analyzer.persistence.model.FileDAO;
import org.jboss.qa.log.analyzer.persistence.model.FileID;

@ApplicationScoped
public class FileService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private WorkspaceService workspaceService;

	public long getNumberOfLines(String jenkins, String project, int build, String path) {
		
		FileID id = new FileID();
		id.setJenkins(jenkins);
		id.setProject(project);
		id.setBuild(build);
		id.setPath(path);
		
		FileDAO fileDAO = em.find(FileDAO.class, id);
		
		if (fileDAO == null) {
			throw new IllegalStateException(String.format("The record with id %s does not exist.", id));
		}
		
		return fileDAO.getNumberOfLines();
		
	}
	
	public String getFileChunk(String jenkins, String project, int build, String path, int chunkNumber) {
		File workspace = workspaceService.getWorkspaceFor(jenkins, project, build, path);
		File chunk = new File(workspace, "chunk" + chunkNumber);
		try (InputStream input = new FileInputStream(chunk)) {
			return IOUtils.toString(input, Charset.forName("UTF-8"));
		} catch (Exception e) {
			throw new IllegalStateException(String.format("Error while loading chunk. Jenkins: %s; project: %s; build: %d; path: %s; chunkNumber: %d", jenkins, project, build, path, chunkNumber), e);
		}
	}
}
