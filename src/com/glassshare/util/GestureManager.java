package com.glassshare.util;

import java.util.HashMap;

import com.glassshare.media.MediaBean;
import com.google.android.glass.touchpad.Gesture;

public class GestureManager {

	private static GestureManager GM;
	
	public static final GestureManager Instance() {
		if (GM == null) {
			GM = new GestureManager();
		}
		return GM;
	}
	
	private GestureManager() {
		
	}
	
	private HashMap<GestureKey, GestureDetectorObserver> gestureManager = new HashMap<GestureKey, GestureDetectorObserver>();

	public void notify(GestureKey gestureKey, Gesture gesture, MediaBean bean) {
		GestureDetectorObserver observer = gestureManager.get(gestureKey);
		if (observer != null) {
			observer.detectGesture(gesture, bean);
		}
	}

	public void registerGestureObserver(GestureKey key,
			GestureDetectorObserver.onDetectGestureListener listener) {
		if (gestureManager.containsKey(key)) {
			GestureDetectorObserver observer = gestureManager.get(key);
			observer.registerGestureListener(listener);
		} else {
			GestureDetectorObserver observer = new GestureDetectorObserver();
			observer.registerGestureListener(listener);
			gestureManager.put(key, observer);
		}
	}
}
