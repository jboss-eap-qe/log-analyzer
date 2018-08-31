package org.jboss.qa.log.analyzer.api.js;

public class FileInfoResponse {
	
	long numberOfLines;
	
	long sizeOfWindow;

	public long getNumberOfLines() {
		return numberOfLines;
	}

	public void setNumberOfLines(long numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

	public long getSizeOfWindow() {
		return sizeOfWindow;
	}

	public void setSizeOfWindow(long sizeOfWindow) {
		this.sizeOfWindow = sizeOfWindow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (numberOfLines ^ (numberOfLines >>> 32));
		result = prime * result + (int) (sizeOfWindow ^ (sizeOfWindow >>> 32));
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
		FileInfoResponse other = (FileInfoResponse) obj;
		if (numberOfLines != other.numberOfLines)
			return false;
		if (sizeOfWindow != other.sizeOfWindow)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileInfoResponse [numberOfLines=" + numberOfLines + ", sizeOfWindow=" + sizeOfWindow + "]";
	}

}
