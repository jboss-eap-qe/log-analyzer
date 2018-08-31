package org.jboss.qa.log.analyzer.api.js;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.jboss.qa.log.analyzer.Configuration;
import org.jboss.qa.log.analyzer.services.FileService;
import org.jboss.qa.log.analyzer.services.JenkinsService;

@Path("/js-backend")
public class JavascriptBackend {
	
	private static final Logger log = Logger.getLogger(JavascriptBackend.class);
	
	@Inject
	private Configuration configuration;
	
	@Inject
	private FileService fileService;
	
	@Inject
	private JenkinsService jenkinsService;

	@POST
	@Path("/file/info")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FileInfoResponse getFileInfo(FileInfoRequest req) {
		FileInfoResponse res = new FileInfoResponse();

		long numberOfLines = fileService.getNumberOfLines(req.getJenkins(), req.getProject(), req.getBuild(), req.getPath());
		
		res.setSizeOfWindow(configuration.getSizeOfWindow());
		res.setNumberOfLines(numberOfLines);
		
		return res;
	}
	
	@POST
	@Path("/file/chunk")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getFileChunk(FileChunkRequest req) {
		return fileService.getFileChunk(req.getJenkins(), req.getProject(), req.getBuild(), req.getPath(), req.getChunk());
	}
	
	@GET
	@Path("/download-and-prepare-logs")
	@Produces(MediaType.APPLICATION_JSON)
	public DownloadAndPrepareLogsResponse downloadAndPrepareLogs(@QueryParam("url") String url) {
		return jenkinsService.downloadAndPrepareLogs(URI.create(url));
	}
}
