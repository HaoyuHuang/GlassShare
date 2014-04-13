package com.glassshare.activity;

import java.util.List;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.glassshare.R;
import com.glassshare.media.MediaBean;
import com.glassshare.media.MediaFactory;
import com.glassshare.media.MediaType;
import com.glassshare.service.NetworkService;
import com.google.android.glass.widget.CardScrollAdapter;

public class MediaScrollAdapter extends CardScrollAdapter {

	private List<MediaBean> mMediaList;
	private Context mContext;
	private MediaBean currentMediaBean;
	private View currentView;
	private NetworkService mNetworkService;
	private MediaType mediaType;

	public MediaScrollAdapter(Context context, List<MediaBean> list,
			NetworkService networkService, MediaType mediaType) {
		super();
		this.mContext = context;
		this.mMediaList = list;
		mNetworkService = networkService;
		this.mediaType = mediaType;
	}

	@Override
	public int findIdPosition(Object arg0) {
		// TODO Auto-generated method stub
		return findItemPosition(arg0);
	}

	@Override
	public int findItemPosition(Object arg0) {
		// TODO Auto-generated method stub
		return mMediaList.indexOf(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMediaList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mMediaList.get(arg0);
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		currentMediaBean = mMediaList.get(index);
		switch (currentMediaBean.getMediaType()) {
		case PHOTO:
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.photo_timeline, parent);
				final ImageView view = (ImageView) convertView
						.findViewById(R.id.image);
				view.setScaleType(ImageView.ScaleType.CENTER_CROP);
				// view.setImageBitmap(MediaFactory.decodeSampledBitmapFromFile(
				// currentMediaBean.getMediaFilePath(), 100, 100));
				view.setImageBitmap(MediaFactory.readImageThumnail(
						currentMediaBean.getMediaFilePath(), 100, 100));
				currentView = view;
			}
		case VIDEO:
			if (convertView == null) { // if it's not recycled, initialize some
				// attributes
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.photo_timeline, parent);
				final ImageView view = (ImageView) convertView
						.findViewById(R.id.image);
				view.setScaleType(ImageView.ScaleType.CENTER_CROP);
				view.setImageBitmap(MediaFactory
						.getVideoPreview(currentMediaBean.getMediaFilePath()));
				currentView = view;
			}
		default:
			break;
		}
		return setItemOnCard(this, convertView);
	}

	public List<MediaBean> getMediaList() {
		return mMediaList;
	}

	public MediaBean getCurrentMediaBean() {
		return currentMediaBean;
	}

	public View getCurrentView() {
		return currentView;
	}

	public void reset() {
		currentView.setBackgroundDrawable(null);
	}
}
