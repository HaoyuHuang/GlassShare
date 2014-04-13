package com.glassshare.util;

import java.util.UUID;

public class Utils {
	public static String UUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
