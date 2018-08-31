package org.jboss.qa.log.analyzer.api.js;

public class JenkinsResponse {

	private String fullDisplayName;
	
	private int number;

	public String getFullDisplayName() {
		return fullDisplayName;
	}

	public void setFullDisplayName(String fullDisplayName) {
		this.fullDisplayName = fullDisplayName;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullDisplayName == null) ? 0 : fullDisplayName.hashCode());
		result = prime * result + number;
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
		JenkinsResponse other = (JenkinsResponse) obj;
		if (fullDisplayName == null) {
			if (other.fullDisplayName != null)
				return false;
		} else if (!fullDisplayName.equals(other.fullDisplayName))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JenkinsResponse [fullDisplayName=" + fullDisplayName + ", number=" + number + "]";
	}
}
