package com.glassshare.util;

import java.util.ArrayList;
import java.util.List;

import com.glassshare.media.MediaBean;
import com.google.android.glass.touchpad.Gesture;

/**
 * @author Haoyu
 * 
 */
public class GestureDetectorObserver {

	public interface onDetectGestureListener {
		public void onSwipeLeft(MediaBean bean);

		public void onSwipeRight(MediaBean bean);

		public void onSwipeDown(MediaBean bean);

		public void onTap(MediaBean bean);
		
		public void onTwoSwipeLeft(MediaBean bean);
		
		public void onTwoSwipeRight(MediaBean bean);
	}

	private List<onDetectGestureListener> gestureListeners = new ArrayList<GestureDetectorObserver.onDetectGestureListener>();

	public boolean detectGesture(Gesture gesture, MediaBean bean) {
		if (gesture == Gesture.SWIPE_LEFT) {
			onSwipeLeft(bean);
			return true;
		} else if (gesture == Gesture.SWIPE_RIGHT) {
			onSwipeRight(bean);
			return true;
		} else if (gesture == Gesture.SWIPE_DOWN) {
			onSwipeDown(bean);
			return true;
		} else if (gesture == Gesture.TAP) {
			onTap(bean);
		} else if (gesture == Gesture.TWO_SWIPE_LEFT) {
			onTwoSwipeLeft(bean);
		} else if (gesture == Gesture.TWO_SWIPE_RIGHT) {
			onTwoSwipeRight(bean);
		}
		return false;
	}

	void registerGestureListener(onDetectGestureListener listener) {
		if (listener != null) {
			this.gestureListeners.add(listener);
		}
	}

	private void onTap(MediaBean bean) {
		for (onDetectGestureListener listener : gestureListeners) {
			listener.onTap(bean);
		}
	}

	private void onSwipeDown(MediaBean bean) {
		for (onDetectGestureListener listener : gestureListeners) {
			listener.onSwipeDown(bean);
		}
	}

	private void onSwipeRight(MediaBean bean) {
		for (onDetectGestureListener listener : gestureListeners) {
			listener.onSwipeRight(bean);
		}
	}
	
	private void onSwipeLeft(MediaBean bean) {
		for (onDetectGestureListener listener : gestureListeners) {
			listener.onSwipeLeft(bean);
		}
	}
	
	private void onTwoSwipeRight(MediaBean bean) {
		for (onDetectGestureListener listener : gestureListeners) {
			listener.onTwoSwipeRight(bean);
		}
	}
	
	private void onTwoSwipeLeft(MediaBean bean) {
		for (onDetectGestureListener listener : gestureListeners) {
			listener.onTwoSwipeLeft(bean);
		}
	}
}
