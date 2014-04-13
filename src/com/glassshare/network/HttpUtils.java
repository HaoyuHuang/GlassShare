package com.glassshare.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * @author Haoyu
 * 
 */
public final class HttpUtils {

	public static final String LOG_TAG = "GlassReminder";

	public static final String ENCODE_UTF_8 = "utf-8";

	/**
	 * log message using the default logger
	 * 
	 * @param message
	 */
	public static void logger(String message) {
		Log.i(LOG_TAG, message);
	}

	/**
	 * encode parameters into key-value pairs in http get format
	 * 
	 * @param parameters
	 * @return the encoded key-value pairs
	 */
	public static String encodeUrl(Bundle parameters) {
		if (parameters == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet()) {
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			sb.append(key + "=" + URLEncoder.encode(parameters.getString(key)));
		}
		return sb.toString();
	}

	/**
	 * decode the key-value pair in http get format into Bundle
	 * 
	 * @param s
	 *            key-value pair in http get format
	 * @return bundle
	 */
	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			params.putString("url", s);
			String array[] = s.split("&");
			for (String parameter : array) {
				String p[] = parameter.split("=");
				if (p.length > 1) {
					params.putString(p[0], URLDecoder.decode(p[1]));
				}
			}
		}
		return params;
	}

	/**
	 * @param url
	 * @param method GET/POST
	 * @param params
	 * @return
	 */
	public static String openUrl(String url, String method, Bundle params) {
		if (method.equals("GET")) {
			url = url + "?" + encodeUrl(params);
		}
		String response = "";
		try {
			Log.d(LOG_TAG, method + " URL: " + url);
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();

			if (!method.equals("GET")) {
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				conn.setDoOutput(true);
				conn.setRequestProperty(
						"Accept",
						"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-silverlight, */*");
				conn.setRequestProperty("Referer", url);
				conn.setRequestProperty("Accept-Language", "zh-cn");
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
				conn.setRequestProperty("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; InfoPath.1; CIBA)");
				conn.setRequestProperty("connection", "keep-alive");
				conn.connect();
				conn.getOutputStream().write(
						encodeUrl(params).getBytes("UTF-8"));
			}
			InputStream is = null;
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				is = conn.getInputStream();
			} else {
				is = conn.getErrorStream();
			}
			response = read(is);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return response;
	}

	/**
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 */
	public static HttpURLConnection openConn(String url, String method,
			Bundle params) {
		if (method.equals("GET")) {
			url = url + "?" + encodeUrl(params);
		}
		try {
			Log.d(LOG_TAG, method + " URL: " + url);
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();

			if (!method.equals("GET")) {
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				conn.setDoOutput(true);
				conn.setRequestProperty(
						"Accept",
						"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-silverlight, */*");
				conn.setRequestProperty("Referer", url);
				conn.setRequestProperty("Accept-Language", "zh-cn");
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
				conn.setRequestProperty("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; InfoPath.1; CIBA)");
				conn.setRequestProperty("connection", "keep-alive");
				conn.connect();
				conn.getOutputStream().write(
						encodeUrl(params).getBytes("UTF-8"));
			}
			return conn;
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static String read(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}

	/**
	 * 
	 * @param reqUrl
	 * 
	 * @param parameters
	 * 
	 * @param fileParamName
	 * 
	 * @param filename
	 * 
	 * @param contentType
	 * 
	 * @param data
	 * 
	 * @return
	 * */
	public static String uploadFile(String reqUrl, Bundle parameters,
			String fileParamName, String filename, String contentType,
			byte[] data) {
		HttpURLConnection urlConn = null;
		try {
			urlConn = sendFormdata(reqUrl, parameters, fileParamName, filename,
					contentType, data);
			Log.i("send", "ok");
			String responseContent = read(urlConn.getInputStream());
			return responseContent;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
	}

	/**
	 * @param reqUrl
	 * @param parameters
	 * @param fileParamName
	 * @param filename
	 * @param contentType
	 * @param data
	 * @return
	 */
	private static HttpURLConnection sendFormdata(String reqUrl,
			Bundle parameters, String fileParamName, String filename,
			String contentType, byte[] data) {
		HttpURLConnection urlConn = null;
		try {
			Log.i("startSend", "okok");
			URL url = new URL(reqUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setConnectTimeout(5000);
			urlConn.setReadTimeout(5000);
			urlConn.setDoOutput(true);
			urlConn.setRequestProperty(
					"Accept",
					"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-silverlight, */*");
			urlConn.setRequestProperty("Referer", reqUrl);
			urlConn.setRequestProperty("Accept-Language", "zh-cn");
			urlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			urlConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			urlConn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; InfoPath.1; CIBA)");
			urlConn.setRequestProperty("connection", "keep-alive");

			String boundary = "-----------------------------114975832116442893661388290519"; // delimiter
			urlConn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			boundary = "--" + boundary;
			StringBuffer params = new StringBuffer();
			if (parameters != null) {
				for (Iterator<String> iter = parameters.keySet().iterator(); iter
						.hasNext();) {
					String name = iter.next();
					String value = parameters.getString(name);
					params.append(boundary + "\r\n");
					params.append("Content-Disposition: form-data; name=\""
							+ name + "\"\r\n\r\n");
					// params.append(URLEncoder.encode(value, "UTF-8"));
					params.append(value);
					params.append("\r\n");
				}
			}

			StringBuilder sb = new StringBuilder();
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + fileParamName
					+ "\"; filename=\"" + filename + "\"\r\n");
			sb.append("Content-Type: " + contentType + "\r\n\r\n");
			byte[] fileDiv = sb.toString().getBytes();
			byte[] endData = ("\r\n" + boundary + "--\r\n").getBytes();
			byte[] ps = params.toString().getBytes();
			Log.i("startSend2", "okok");
			urlConn.connect();
			OutputStream os = urlConn.getOutputStream();
			Log.i("startSend3", "okok");
			os.write(ps);
			os.write(fileDiv);
			os.write(data);
			os.write(endData);
			Log.i("output", params.toString() + sb.toString() + data + "\r\n"
					+ boundary + "--\r\n");
			os.flush();
			os.close();
			Log.i("startSend4", "okok");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
		return urlConn;
	}

	public static void clearCookies(Context context) {
		@SuppressWarnings("unused")
		CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	/**
	 * @param response
	 * @throws NetworkException
	 */
	public static void checkResponse(String response) throws NetworkError {
		if (response != null) {
			if (response.indexOf("error_code") < 0) {
				return;
			}
			NetworkError error = null;
			error = parseJson(response);
			if (error != null) {
				throw new NetworkError(error);
			}
		}
	}

	/**
	 * @param jsonResponse
	 * @return
	 */
	private static NetworkError parseJson(String jsonResponse) {
		try {
			JSONObject json = new JSONObject(jsonResponse);

			int errorCode = json.getInt("error_code");
			String errorMessage = json.getString("error_msg");
			errorMessage = NetworkError.interpretErrorMessage(errorCode,
					errorMessage);
			return new NetworkError(errorCode, errorMessage, jsonResponse);
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static NetworkError parseNetworkError(String response) {
		if (response.indexOf("error_code") < 0)
			return null;
		return parseJson(response);
	}

	/**
	 * MD5 encoding the input string
	 * 
	 * @param string
	 *            input string
	 * @return md5 encoded string
	 */
	public static String md5(String string) {
		if (string == null || string.trim().length() < 1) {
			return null;
		}
		try {
			return getMD5(string.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static String getMD5(byte[] source) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			StringBuffer result = new StringBuffer();
			for (byte b : md5.digest(source)) {
				result.append(Integer.toHexString((b & 0xf0) >>> 4));
				result.append(Integer.toHexString(b & 0x0f));
			}
			return result.toString();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static boolean isNetworkConnected(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobileState = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		State wifiState = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		if (mobileState == State.DISCONNECTED
				&& wifiState == State.DISCONNECTED) {
			return false;
		}
		return true;
	}
}
