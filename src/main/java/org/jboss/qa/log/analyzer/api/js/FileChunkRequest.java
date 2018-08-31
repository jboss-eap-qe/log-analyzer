package org.jboss.qa.log.analyzer.api.js;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FileChunkRequest {
	
	@NotNull
	private String jenkins;
	
	@NotNull
	private String project;
	
	@NotNull
	@Min(1)
	private Integer build;
	
	@NotNull
	private String path;
	
	@NotNull
	@Min(0)
	private Integer chunk;

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

	public Integer getChunk() {
		return chunk;
	}

	public void setChunk(Integer chunk) {
		this.chunk = chunk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((build == null) ? 0 : build.hashCode());
		result = prime * result + ((chunk == null) ? 0 : chunk.hashCode());
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
		FileChunkRequest other = (FileChunkRequest) obj;
		if (build == null) {
			if (other.build != null)
				return false;
		} else if (!build.equals(other.build))
			return false;
		if (chunk == null) {
			if (other.chunk != null)
				return false;
		} else if (!chunk.equals(other.chunk))
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
		return "FileChunkRequest [jenkins=" + jenkins + ", project=" + project + ", build=" + build + ", path=" + path
				+ ", chunk=" + chunk + "]";
	}
	
	

}
