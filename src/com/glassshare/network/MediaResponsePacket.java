package com.glassshare.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class MediaResponsePacket extends MediaPacket {

	private static final String KEY_STATE = "state";

	private int state;

	public MediaResponsePacket() {

	}

	public MediaResponsePacket(String uuid, String cliendId, int cmdId,
			String mediaType, int state) {
		super(uuid, cliendId, cmdId, mediaType);
		this.state = state;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put(KEY_UUID, uuid);
		obj.put(KEY_CMD_ID, cmdId);
		obj.put(KEY_MEDIA_TYPE, mediaType);
		obj.put(KEY_CLIENT_ID, cliendId);
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
			state = obj.optInt(KEY_STATE);
		}
	}

	public int getState() {
		return state;
	}

	@Override
	public Bundle getBundle() {
		// TODO Auto-generated method stub
		return null;
	}

}
