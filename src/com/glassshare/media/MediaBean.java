package com.glassshare.media;


public class MediaBean {

	private MediaType mediaType;

	private String mediaFilePath;

	private boolean isActive = false;

	private String fileName;

	private String extension;

	public MediaType getMediaType() {
		return mediaType;
	}

	public String getMediaExtension() {
		return "image/" + extension;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaFilePath() {
		return mediaFilePath;
	}

	public void setMediaFilePath(String mediaFilePath) {
		this.mediaFilePath = mediaFilePath;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		int index = this.fileName.lastIndexOf(".");
		if (index != -1) {
			this.extension = this.fileName.substring(index + 1);
		}
	}

	public String getExtension() {
		return extension;
	}
}
