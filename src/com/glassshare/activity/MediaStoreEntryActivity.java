package com.glassshare.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.glassshare.R;
import com.glassshare.service.GlassShareService;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class MediaStoreEntryActivity extends Activity implements
		GestureDetector.BaseListener {

	private boolean mResumed;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// unbindService(this);
			openOptionsMenu();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// Nothing to do here.
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemPhoto:
			Intent intent = new Intent(this, MediaStoreActivity.class);
			intent.putExtra(MediaStoreActivity.EXTRA_PHOTO_STORE, true);
			startActivity(intent);
			return true;
		case R.id.menuItemVideo:
			intent = new Intent(this, MediaStoreActivity.class);
			intent.putExtra(MediaStoreActivity.EXTRA_VIDEO_STORE, true);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		finish();
	}

	@Override
	public void openOptionsMenu() {
		if (mResumed) {
			super.openOptionsMenu();
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.setGroupVisible(R.id.menuGroupMedia, true);
		menu.findItem(R.id.menuItemPhoto).setVisible(true);
		menu.findItem(R.id.menuItemVideo).setVisible(true);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.live_card);
		// bindService(new Intent(this, GlassShareService.class), mConnection,
		// 0);
		openOptionsMenu();
		Log.i(GlassShareService.LIVE_CARD_TAG, "media store entry");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mResumed = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mResumed = true;
		openOptionsMenu();
	}

	@Override
	public boolean onGesture(Gesture gesture) {
		if (gesture == Gesture.TAP) {
			// Intent intent = new Intent();
			// intent.setClass(this, MediaStoreActivity.class);
			// startActivity(intent);
		}
		return false;
	}

}
