package com.glassshare.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.glassshare.activity.MediaStoreEntryActivity;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;
import com.google.android.glass.timeline.TimelineManager;

public class GlassShareService extends Service {

	public static final String LIVE_CARD_TAG = "glassShare";
	
	private TimelineManager mTimelineManager;
	private LiveCard mLiveCard;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mTimelineManager = TimelineManager.from(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mLiveCard == null) {
			mLiveCard = mTimelineManager.createLiveCard(LIVE_CARD_TAG);
			Intent menuIntent = new Intent(this, MediaStoreEntryActivity.class);
			menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent,
					0));
			mLiveCard.publish(PublishMode.REVEAL);
			Log.i(LIVE_CARD_TAG, "start media store activity");
		} else {
			// TODO(alainv): Jump to the LiveCard when API is available.
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (mLiveCard != null && mLiveCard.isPublished()) {
			mLiveCard.unpublish();
			mLiveCard = null;
		}
		super.onDestroy();
	}

}
