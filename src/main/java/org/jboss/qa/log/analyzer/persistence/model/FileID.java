package org.jboss.qa.log.analyzer.persistence.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class FileID implements Serializable {
	
	private static final long serialVersionUID = 4147059079894439857L;

	private String jenkins;
	
	private String project;
	
	private int build;

	private String path;

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		FileID other = (FileID) obj;
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
		return "FileID [jenkins=" + jenkins + ", project=" + project + ", build=" + build + ", path=" + path + "]";
	}

}
