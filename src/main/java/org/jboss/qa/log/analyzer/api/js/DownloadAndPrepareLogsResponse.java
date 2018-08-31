package org.jboss.qa.log.analyzer.api.js;

import java.util.HashMap;
import java.util.Map;

public class DownloadAndPrepareLogsResponse {
	
	private String jenkins;
	
	private String project;
	
	private int build;
	
	private Map<String, FileInfoResponse> files = new HashMap<>();

	public String getJenkins() {
		return jenkins;
	}

	public void setJenkins(String jenkins) {
		this.jenkins = jenkins;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public int getBuild() {
		return build;
	}

	public void setBuild(int build) {
		this.build = build;
	}

	public Map<String, FileInfoResponse> getFiles() {
		return files;
	}

	public void setFiles(Map<String, FileInfoResponse> files) {
		this.files = files;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + build;
		result = prime * result + ((files == null) ? 0 : files.hashCode());
		result = prime * result + ((jenkins == null) ? 0 : jenkins.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DownloadAndPrepareLogsResponse other = (DownloadAndPrepareLogsResponse) obj;
		if (build != other.build)
			return false;
		if (files == null) {
			if (other.files != null)
				return false;
		} else if (!files.equals(other.files))
			return false;
		if (jenkins == null) {
			if (other.jenkins != null)
				return false;
		} else if (!jenkins.equals(other.jenkins))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DownloadAndPrepareLogsResponse [jenkins=" + jenkins + ", project=" + project + ", build=" + build
				+ ", files=" + files + "]";
	}

}
