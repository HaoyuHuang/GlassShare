package com.glassshare.network;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoRequestPacket extends MediaRequestPacket {

	private static final String KEY_FILE_NAME = "fileName";

	private static final String KEY_DESCRIPTION = "description";

	private String fileName;

	private String description;

	public VideoRequestPacket(String uuid, String cliendId, int cmdId,
			String mediaType, String fileName, String description) {
		super(uuid, cliendId, cmdId, mediaType);
		this.fileName = fileName;
		this.description = description;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = super.toJSON();
		json.put(KEY_FILE_NAME, fileName);
		json.put(KEY_DESCRIPTION, description);
		return json;
	}

	@Override
	public void fromJSON(String jsonString) throws JSONException {
		super.fromJSON(jsonString);
	}

}
