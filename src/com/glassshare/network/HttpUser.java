package com.glassshare.network;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Haoyu
 * 
 * 		User singleton class
 *
 */
public class HttpUser {

	public static final String KEY_MAIL = "mail";
	
	private String mail;
	
	/** server url */
	private final String SERVER_URL = "http://192.168.2.2:6001";
	/** Json */
	public static final String RESPONSE_FORMAT_JSON = "json";
	private final String LOG_TAG_REQUEST = "request";
	private final String LOG_TAG_RESPONSE = "response";
	
	private static HttpUser user = new HttpUser();

	public static HttpUser getInstance() {
		if (user == null)
			user = new HttpUser();
		return user;
	}

	private HttpUser() {

	}

	public boolean logout(Context context) {
		HttpUtils.clearCookies(context);
		return true;
	}

	public static final class UserAccessToken {
		private static final String USER_PREFERENCE_NAME = "user_access_token";

		private static final String USER_PREFERENCE_MAIL = "mail";

		/**
		 * @param context
		 * @param user
		 */
		public static void keepAccessToken(Context context, HttpUser user) {
			SharedPreferences pref = context.getSharedPreferences(
					USER_PREFERENCE_NAME, Context.MODE_APPEND);
			Editor editor = pref.edit();
			editor.putString(USER_PREFERENCE_MAIL, user.getMail());
			editor.commit();
		}
		
		/**
		 * @param context
		 */
		public static void clear(Context context) {
			SharedPreferences pref = context.getSharedPreferences(
					USER_PREFERENCE_NAME, Context.MODE_APPEND);
			Editor editor = pref.edit();
			editor.clear();
			editor.commit();
		}

		/**
		 * @param context
		 */
		public static void readAccessToken(Context context) {
			SharedPreferences pref = context.getSharedPreferences(
					USER_PREFERENCE_NAME, Context.MODE_APPEND);
			user.setMail(pref.getString(USER_PREFERENCE_MAIL, ""));
		}
	}

	public void init(String mail) {
		this.mail = mail;
	}

	/**
	 * 
	 * 
	 * @param fileName
	 *            the photo fileName
	 * @return contentType
	 * @throws RuntimeException
	 *             suffix of file is not supported
	 * */
	private String parseContentType(String fileName) {
		String contentType = "image/jpg";
		fileName = fileName.toLowerCase(Locale.US);
		if (fileName.endsWith(".jpg"))
			contentType = "image/jpg";
		else if (fileName.endsWith(".png"))
			contentType = "image/png";
		else if (fileName.endsWith(".jpeg"))
			contentType = "image/jpeg";
		else if (fileName.endsWith(".gif"))
			contentType = "image/gif";
		else if (fileName.endsWith(".bmp"))
			contentType = "image/bmp";
		else
			throw new RuntimeException(fileName);
		return contentType;
	}

	/**
	 * 
	 * 
	 * @param photo
	 * @param fileName
	 * @param description
	 * @param format
	 *            JSON
	 * @return
	 */
	public String publishPhotoByHttp(String method, byte[] photo, String fileName,
			String description, String format) {
		Bundle params = new Bundle();
		params.putString("method", method);
		String contentType = parseContentType(fileName);
		params.putString("format", format);
		return HttpUtils.uploadFile(SERVER_URL + method, params, "upload",
				fileName, contentType, photo);
	}

	/**
	 * 
	 */
	private void logRequest(Bundle params) {
		if (params != null) {
			String method = params.getString("method");
			if (method != null) {
				StringBuffer sb = new StringBuffer();
				sb.append("method=").append(method).append("&")
						.append(params.toString());
				Log.i(LOG_TAG_REQUEST, sb.toString());
			} else {
				Log.i(LOG_TAG_REQUEST, params.toString());
			}
		}
	}

	/**
	 * 
	 * @param response
	 */
	private void logResponse(String method, String response) {
		if (method != null && response != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("method=").append(method).append("&").append(response);
			Log.i(LOG_TAG_RESPONSE, sb.toString());
		}
	}
	
	/**
	 * 
	 * 
	 * @param parameters
	 * 
	 * @return
	 */
	public String request(String action, Bundle parameters) {
		parameters.putString("format", RESPONSE_FORMAT_JSON);
		logRequest(parameters);
		String response = HttpUtils
				.openUrl(SERVER_URL + action, "POST", parameters);
		logResponse(parameters.getString("method"), response);
		return response;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
}
