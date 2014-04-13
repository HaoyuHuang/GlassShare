package com.glassshare.network;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * @author Haoyu
 * 
 */
public abstract class RequestParam {

	public abstract Bundle getParams() throws NetworkError;

	public void checkNullParams(String... params) throws NetworkError {

		for (String param : params) {
			if (TextUtils.isEmpty(param)) {
				String errorMsg = "required parameter MUST NOT be null";
				throw new NetworkError(NetworkError.ERROR_CODE_NULL_PARAMETER,
						errorMsg, errorMsg);
			}
		}

	}
}
