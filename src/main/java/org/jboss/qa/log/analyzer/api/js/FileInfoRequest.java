package org.jboss.qa.log.analyzer.api.js;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FileInfoRequest {
	
	@NotNull
	String jenkins;
	
	@NotNull
	String project;
	
	@NotNull
	@Min(1)
	Integer build;
	
	@NotNull
	String path;

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

	public Integer getBuild() {
		return build;
	}

	public void setBuild(Integer build) {
		this.build = build;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + build;
		result = prime * result + ((jenkins == null) ? 0 : jenkins.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		FileInfoRequest other = (FileInfoRequest) obj;
		if (build != other.build)
			return false;
		if (jenkins == null) {
			if (other.jenkins != null)
				return false;
		} else if (!jenkins.equals(other.jenkins))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
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
		return "FileInfoRequest [jenkins=" + jenkins + ", project=" + project + ", build=" + build + ", path=" + path
				+ "]";
	}

}
