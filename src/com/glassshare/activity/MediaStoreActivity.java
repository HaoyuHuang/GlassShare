package com.glassshare.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.VideoView;

import com.glassshare.media.MediaBean;
import com.glassshare.media.MediaLoader;
import com.glassshare.media.MediaType;
import com.glassshare.service.NetworkService;
import com.glassshare.util.GestureDetectorObserver;
import com.glassshare.util.GestureKey;
import com.glassshare.util.GestureManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollView;

public class MediaStoreActivity extends Activity implements
		GestureDetector.BaseListener {

	public static final String EXTRA_COUNT = "count";
	public static final String EXTRA_INITIAL_VALUE = "initial_value";
	public static final String EXTRA_SELECTED_VALUE = "selected_value";

	public static final String EXTRA_PHOTO_STORE = "photoStore";
	public static final String EXTRA_VIDEO_STORE = "videoStore";

	public static final String EXTRA_VIDEO_UPLOADED_LIST = "videoList";

	public static final int skipInterval = 1000;

	private List<MediaBean> mMediaList;
	private MediaScrollAdapter mMediaScrollAdapter;

	private AudioManager mAudioManager;
	private GestureDetector mDetector;
	private CardScrollView mView;

	private NetworkService mNetworkService;

	private ArrayList<String> uploadedFiles = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mNetworkService = new NetworkService();
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		mMediaList = new ArrayList<MediaBean>();
		boolean isPhotoStore = getIntent().getBooleanExtra(EXTRA_PHOTO_STORE,
				false);
		boolean isVideoStore = getIntent().getBooleanExtra(EXTRA_VIDEO_STORE,
				false);
		if (getIntent().getStringArrayListExtra(EXTRA_VIDEO_UPLOADED_LIST) != null) {
			uploadedFiles = getIntent().getStringArrayListExtra(
					EXTRA_VIDEO_UPLOADED_LIST);
		}
		if (isPhotoStore) {
			MediaLoader.preloadMedias(this, mMediaList, MediaType.PHOTO);
			mMediaScrollAdapter = new MediaScrollAdapter(this, mMediaList,
					mNetworkService, MediaType.PHOTO);
		} else if (isVideoStore) {
			MediaLoader.preloadMedias(this, mMediaList, MediaType.VIDEO);
			mMediaScrollAdapter = new MediaScrollAdapter(this, mMediaList,
					mNetworkService, MediaType.VIDEO);
		}

		mView = new CardScrollView(this) {
			@Override
			public final boolean dispatchGenericFocusedEvent(MotionEvent event) {
				if (mDetector.onMotionEvent(event)) {
					return true;
				}
				return super.dispatchGenericFocusedEvent(event);
			}
		};
		mView.setAdapter(mMediaScrollAdapter);
		setContentView(mView);

		mDetector = new GestureDetector(this).setBaseListener(this);
		GestureManager.Instance().registerGestureObserver(GestureKey.TIME_LINE,
				new GestureDetectorObserver.onDetectGestureListener() {

					@Override
					public void onSwipeLeft(MediaBean bean) {
						switch (bean.getMediaType()) {
						case PHOTO:
							break;
						case VIDEO:
							break;
						case NULL:
							break;
						default:
							break;
						}
					}

					@Override
					public void onSwipeRight(MediaBean bean) {
						switch (bean.getMediaType()) {
						case PHOTO:
							break;
						case VIDEO:
							break;
						case NULL:
							break;
						default:
							break;
						}
					}

					@Override
					public void onSwipeDown(MediaBean bean) {
						switch (bean.getMediaType()) {
						case PHOTO:
							break;
						case VIDEO:
							break;
						case NULL:
							break;
						default:
							break;
						}
					}

					@Override
					public void onTap(MediaBean bean) {
						// TODO Auto-generated method stub
						switch (bean.getMediaType()) {
						case PHOTO:
							Log.i("glassShare", "tap");
							if (!uploadedFiles.contains(bean.getFileName())) {
								uploadedFiles.add(bean.getFileName());
								mNetworkService.publishPhotoByTCP(bean,
										bean.getFileName(), "", null);
							}
							break;
						case VIDEO:
							if (!uploadedFiles.contains(bean.getFileName())) {
								uploadedFiles.add(bean.getFileName());
								mNetworkService.prepareUploadVideo(bean, "",
										null);
							}
							Log.i("glassShare", "videoTap");
							Intent i = new Intent();
							i.setAction("com.google.glass.action.VIDEOPLAYER");
							i.putExtra("video_url", bean.getMediaFilePath());
							i.putStringArrayListExtra(
									EXTRA_VIDEO_UPLOADED_LIST, uploadedFiles);
							startActivity(i);
							break;
						case NULL:
							break;
						default:
							break;
						}
					}

					@Override
					public void onTwoSwipeLeft(MediaBean bean) {
						switch (bean.getMediaType()) {
						case PHOTO:
							break;
						case VIDEO:
							// moveFast();
							break;
						case NULL:
							break;
						default:
							break;
						}
					}

					@Override
					public void onTwoSwipeRight(MediaBean bean) {
						switch (bean.getMediaType()) {
						case PHOTO:
							break;
						case VIDEO:
							// moveBefore();
							break;
						case NULL:
							break;
						default:
							break;
						}
					}

				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mView.deactivate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mView.activate();
		mView.setSelection(getIntent().getIntExtra(EXTRA_INITIAL_VALUE, 0));
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mDetector.onMotionEvent(event);
	}

	@Override
	public boolean onGesture(Gesture gesture) {
		GestureManager.Instance().notify(GestureKey.TIME_LINE, gesture,
				mMediaScrollAdapter.getCurrentMediaBean());
		return false;
	}

	private void moveBefore() {
		VideoView videoView = (VideoView) mMediaScrollAdapter.getCurrentView();
		if (videoView.getCurrentPosition() - skipInterval < 0) {
			videoView.seekTo(0);
		} else {
			videoView.seekTo(videoView.getCurrentPosition() - skipInterval);
		}
	}

	private void moveFast() {
		VideoView videoView = (VideoView) mMediaScrollAdapter.getCurrentView();
		if (videoView.getCurrentPosition() + skipInterval >= videoView
				.getDuration()) {
			videoView.seekTo(videoView.getDuration());
		} else {
			videoView.seekTo(videoView.getCurrentPosition() + skipInterval);
		}
	}

}
