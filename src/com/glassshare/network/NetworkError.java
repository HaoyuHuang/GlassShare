package com.glassshare.network;

/**
 * @author Haoyu
 *
 */
public class NetworkError extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static final int ERROR_CODE_NULL_PARAMETER = -1;

	public static final int ERROR_REFRESH_DATA = -2;
	
	public static final int ERROR_NO_CONNECTION = -3;

	private int errorCode;

	private String orgResponse;

	public NetworkError() {
		super();
	}

	public NetworkError(NetworkError error) {
		this.errorCode = error.getErrorCode();
		this.orgResponse = error.getOrgResponse();
	}

	public NetworkError(String errorMessage) {
		super(errorMessage);
	}

	public NetworkError(int errorCode, String errorMessage, String orgResponse) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.orgResponse = orgResponse;
	}

	public String getOrgResponse() {
		return orgResponse;
	}

	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		return "errorCode:" + this.errorCode + "\nerrorMessage:"
				+ this.getMessage() + "\norgResponse:" + this.orgResponse;
	}

	public static String interpretErrorMessage(int errorCode,
			String errorMessage) {
		switch (errorCode) {
		case 300:
			errorMessage = "";
			break;
		default:
			break;
		}
		return errorMessage;
	}
}
