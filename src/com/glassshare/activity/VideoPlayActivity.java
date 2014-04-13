package com.glassshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore.Video;

public class VideoPlayActivity extends Activity {
	private Video video;
	public void hah() {
		Intent i = new Intent();
		i.setAction("com.google.glass.action.VIDEOPLAYER");
		i.putExtra("video_url", "https://m.youtube.com/watch?v=5bWSgFnoCOk"); 
		startActivity(i);    
	}
}
