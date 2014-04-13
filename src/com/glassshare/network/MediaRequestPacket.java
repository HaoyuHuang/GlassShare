package com.glassshare.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class MediaRequestPacket extends MediaPacket {

	public MediaRequestPacket() {

	}

	public MediaRequestPacket(String uuid, String cliendId, int cmdId,
			String mediaType) {
		super(uuid, cliendId, cmdId, mediaType);
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put(KEY_UUID, uuid);
		obj.put(KEY_CMD_ID, cmdId);
		obj.put(KEY_MEDIA_TYPE, mediaType);
		obj.put(KEY_CLIENT_ID, cliendId);
		obj.put(KEY_FILE_LENGTH, fileLength);
		return obj;
	}

	@Override
	public void fromJSON(String jsonString) throws JSONException {
		if (jsonString != null) {
			JSONObject obj = new JSONObject(jsonString);
			uuid = obj.optString(KEY_UUID);
			cliendId = obj.optString(KEY_CLIENT_ID);
			cmdId = obj.optInt(KEY_CMD_ID);
			mediaType = obj.optString(KEY_MEDIA_TYPE);
		}
	}

	@Override
	public Bundle getBundle() {
		Bundle bundle = new Bundle();
		bundle.putString(KEY_UUID, uuid);
		bundle.putInt(KEY_CMD_ID, cmdId);
		bundle.putString(KEY_MEDIA_TYPE, mediaType);
		bundle.putString(KEY_CLIENT_ID, cliendId);
		return bundle;
	}

}
