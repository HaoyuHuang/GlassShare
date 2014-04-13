package com.glassshare.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public abstract class MediaPacket {

	protected static final String KEY_UUID = "uuid";

	protected static final String KEY_CMD_ID = "cmdId";
	
	protected static final String KEY_CLIENT_ID = "clientId";

	protected static final String KEY_MEDIA_TYPE = "mediaType";
	
	protected static final String KEY_FILE_LENGTH = "fileLength";
	
	protected String uuid;
	
	protected String cliendId;

	protected int cmdId;

	protected String mediaType;
	
	protected long fileLength;
	
	public MediaPacket() {
		
	}
	
	public MediaPacket(String uuid, String cliendId, int cmdId, String mediaType) {
		super();
		this.uuid = uuid;
		this.cliendId = cliendId;
		this.cmdId = cmdId;
		this.mediaType = mediaType;
	}

	public abstract JSONObject toJSON() throws JSONException;
	
	public abstract void fromJSON(String jsonString) throws JSONException;
	
	public abstract Bundle getBundle();
	
	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getUuid() {
		return uuid;
	}

	public int getCmdId() {
		return cmdId;
	}

	public String getMediaType() {
		return mediaType;
	}

}
