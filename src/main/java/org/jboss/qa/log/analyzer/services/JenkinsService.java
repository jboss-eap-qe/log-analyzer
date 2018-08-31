package org.jboss.qa.log.analyzer.services;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.qa.log.analyzer.Configuration;
import org.jboss.qa.log.analyzer.api.js.DownloadAndPrepareLogsResponse;
import org.jboss.qa.log.analyzer.api.js.FileInfoResponse;
import org.jboss.qa.log.analyzer.api.js.JenkinsResponse;
import org.jboss.qa.log.analyzer.persistence.model.FileDAO;
import org.jboss.qa.log.analyzer.persistence.model.FileID;

@ApplicationScoped
public class JenkinsService {
	
	private static final Logger log = Logger.getLogger(JenkinsService.class);
	
	private static final Pattern extractProjectPattern = Pattern.compile("(^[^ ]+).*");
	
	@Inject
	private Configuration configuration;
	
	@Inject
	private WorkspaceService workspaceService;
	
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public DownloadAndPrepareLogsResponse downloadAndPrepareLogs(URI uri) {
		
		JenkinsResponse jenkinsResponse = readBuildMeta(uri);
		
		String jenkins = uri.getHost();
		String project = extractProject(jenkinsResponse.getFullDisplayName());
		int build = jenkinsResponse.getNumber();
		
		Optional<DownloadAndPrepareLogsResponse> downloadedLogs = getDownloadedLogs(jenkins, project, build);
		if (downloadedLogs.isPresent()) {
			return downloadedLogs.get();
		}
		
		List<FileDAO> fileDAOList = downloadAndPrepareLogs(jenkins, project, build);
		fileDAOList.forEach(this::persistFileDAO);
		
		return generateResponse(jenkins, project, build, fileDAOList);
		
	}
	
	private JenkinsResponse readBuildMeta(URI uri) {
		URI targetUri = UriBuilder.fromUri(uri).path("api/json").build();
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(targetUri);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		
		if (response.getStatus() != 200) {
			throw new IllegalStateException("Jenkins returned reponse " + response.getStatus());
		}
		
		return response.readEntity(JenkinsResponse.class);
	}
	
	private String extractProject(String fullDisplayName) {
		Matcher matcher = extractProjectPattern.matcher(fullDisplayName);
		
		if (!matcher.matches()) {
			throw new IllegalStateException("FullDisplayName does not conform to the pattern");
		}
		
		return matcher.group(1);
	}
	
	private List<FileDAO> downloadAndPrepareLogs(String jenkins, String project, int build) {
		List<FileDAO> result = new ArrayList<FileDAO>();
		
		URI uri = URI.create(String.format("https://%s/job/%s/%d/consoleText", jenkins, project, build));
		File workspace = workspaceService.getWorkspaceFor(jenkins, project, build, "consoleText");
		File full = new File(workspace, "full");
		
		try (InputStream input = uri.toURL().openStream(); OutputStream output = new FileOutputStream(full)) {
			IOUtils.copy(input, output);
		} catch (Exception e) {
			throw new IllegalStateException("Error while downloading consoleText from: " + uri, e);
		}
		
		long numberOfLines = splitFileToChunks(full, workspace);
		full.delete();
		
		FileID fileID = new FileID();
		fileID.setJenkins(jenkins);
		fileID.setProject(project);
		fileID.setBuild(build);
		fileID.setPath("consoleText");
		
		FileDAO fileDAO = new FileDAO();
		fileDAO.setFileID(fileID);
		fileDAO.setNumberOfLines(numberOfLines);
		
		result.add(fileDAO);
		
		return result;
	}
	
	private long splitFileToChunks(File f, File target) {
		
		int sizeOfWindow = configuration.getSizeOfWindow();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			
			String line;
			int numberOfLines = 0;
			int chunksCounter = 0;
			PrintWriter chunkOut = null;
			
			try {
				while ((line = reader.readLine()) != null) {
					numberOfLines++;
					
					if (chunkOut == null) {
						File chunkFile = new File(target, "chunk" + chunksCounter++);
						chunkOut = new PrintWriter(new FileOutputStream(chunkFile));
					}
					
					chunkOut.println(line);
					
					if (numberOfLines % sizeOfWindow == 0) {
						chunkOut.close();
						chunkOut = null;
					}
					
				}
			} finally {
				if (chunkOut != null) {
					chunkOut.close();
				}
			}
			
			return numberOfLines;
			
		} catch (Exception e) {
			throw new IllegalStateException("Error while splitting file to chunks: " + f.getAbsolutePath(), e);
		}
	}
	
	private void persistFileDAO(FileDAO fileDAO) {
		em.persist(fileDAO);
	}
	
	private Optional<DownloadAndPrepareLogsResponse> getDownloadedLogs(String jenkins, String project, int build) {
		Query query = em.createQuery("SELECT f from FileDAO f WHERE f.id.jenkins = :jenkins AND f.id.project = :project AND f.id.build = :build");
		query.setParameter("jenkins", jenkins);
		query.setParameter("project", project);
		query.setParameter("build", build);
		
		List<FileDAO> resultList = query.getResultList();
		
		if (resultList.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(generateResponse(jenkins, project, build, resultList));
	}
	
	private DownloadAndPrepareLogsResponse generateResponse(String jenkins, String project, int build, List<FileDAO> fileDAOList) {
		DownloadAndPrepareLogsResponse resp = new DownloadAndPrepareLogsResponse();
		resp.setJenkins(jenkins);
		resp.setProject(project);
		resp.setBuild(build);
		
		int sizeOfWindow = configuration.getSizeOfWindow();
		
		for (FileDAO fileDAO : fileDAOList) {
			FileInfoResponse fileInfoResponse = new FileInfoResponse();
			fileInfoResponse.setNumberOfLines(fileDAO.getNumberOfLines());
			fileInfoResponse.setSizeOfWindow(sizeOfWindow);
			
			resp.getFiles().put(fileDAO.getFileID().getPath(), fileInfoResponse);
		}
		
		return resp;
	}
	
}
