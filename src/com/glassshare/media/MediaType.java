package com.glassshare.media;

public enum MediaType {

	NULL(0, "null"), PHOTO(1, "photo"), VIDEO(2, "video");

	int id;
	String description;

	MediaType(int id, String description) {
		this.id = id;
		this.description = description;
	}

}
