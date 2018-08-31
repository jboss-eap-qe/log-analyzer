package org.jboss.qa.log.analyzer.persistence.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class FileDAO {
	
	@EmbeddedId
	private FileID id;

	private long numberOfLines;

	public FileID getFileID() {
		return id;
	}

	public void setFileID(FileID fileID) {
		this.id = fileID;
	}

	public long getNumberOfLines() {
		return numberOfLines;
	}

	public void setNumberOfLines(long numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (numberOfLines ^ (numberOfLines >>> 32));
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
		FileDAO other = (FileDAO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (numberOfLines != other.numberOfLines)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileDAO [fileID=" + id + ", numberOfLines=" + numberOfLines + "]";
	}
	
}
