package com.glassshare.service;

import java.util.HashSet;

import org.json.JSONException;

import android.content.Context;

import com.glassshare.media.MediaBean;
import com.glassshare.media.MediaType;
import com.glassshare.network.AsyncUtils;
import com.glassshare.network.MediaRequestPacket;
import com.glassshare.network.PhotoRequestPacket;
import com.glassshare.network.RequestListener;
import com.glassshare.network.VideoRequestPacket;
import com.glassshare.util.Utils;

public class NetworkService {

	public static final int CLIENT_BROADCAST_IP_ADDRESS = 0;

	public static final int CLIENT_CONNECT_REQUEST = 2;

	public static final int CLIENT_PREPARE_UPLOAD_PHOTO_REQUEST = 200;

	public static final int CLIENT_PREPARE_UPLOAD_VIDEO_REQUEST = 300;

	public static final int CLIENT_PLAY_VIDEO = 301;

	public static final int CLIENT_PAUSE_VIDEO = 302;

	public static final int CLIENT_STOP_VIDEO = 303;

	public static final int CLIENT_FAST_VIDEO = 304;

	public static final int CLIENT_SLOW_VIDEO = 305;
	
	private static HashSet<String> set = new HashSet<String>();

	/**
	 * @param context
	 * @param requestListener
	 */
	public void getServerIp(Context context, RequestListener requestListener) {
		try {
			MediaRequestPacket packet = new MediaRequestPacket(Utils.UUID(),
					"", CLIENT_BROADCAST_IP_ADDRESS, MediaType.NULL.toString());
			AsyncUtils.getInstance().getServerIpAddress(context,
					packet.toJSON().toString(), requestListener);
		} catch (JSONException e) {
			if (requestListener != null) {
				requestListener.onFault(e);
			}
		}
	}

	/**
	 * @param requestListener
	 */
	public void connect(final RequestListener requestListener) {
		MediaRequestPacket packet = new MediaRequestPacket(Utils.UUID(), "",
				CLIENT_CONNECT_REQUEST, MediaType.NULL.toString());
		AsyncUtils.getInstance().sendMediaPacket(packet, requestListener);
	}

	/**
	 * @param requestListener
	 */
	public void httpConnect(final RequestListener requestListener) {
		MediaRequestPacket packet = new MediaRequestPacket(Utils.UUID(), "",
				CLIENT_CONNECT_REQUEST, MediaType.NULL.toString());
		AsyncUtils.getInstance().sendMediaPacketByHttp(packet, requestListener);
	}

	/**
	 * @param fileName
	 * @param description
	 * @param requestListener
	 */
	public void prepareUploadPhoto(MediaBean mediaBean, String description,
			final RequestListener requestListener) {
		if (!set.contains(mediaBean.getFileName())) {
			set.add(mediaBean.getFileName());
			PhotoRequestPacket prp = new PhotoRequestPacket(Utils.UUID(), "",
					CLIENT_PREPARE_UPLOAD_PHOTO_REQUEST,
					mediaBean.getMediaExtension(), mediaBean.getFileName(),
					description);
			AsyncUtils.getInstance().sendMediaPacket(prp, requestListener);
		}
	}

	/**
	 * @param fileName
	 * @param description
	 * @param listener
	 */
	public void publishPhotoByTCP(final MediaBean bean, final String fileName,
			final String description, final RequestListener listener) {
		if (!set.contains(bean.getFileName())) {
			set.add(bean.getFileName());
			PhotoRequestPacket packet = new PhotoRequestPacket(Utils.UUID(),
					"", CLIENT_PREPARE_UPLOAD_PHOTO_REQUEST,
					bean.getMediaExtension(), fileName, description);
			AsyncUtils.getInstance().publishPhotoByTCP(packet,
					bean.getMediaFilePath(), description, listener);
		}
	}

	/**
	 * @param fileName
	 * @param description
	 * @param requestListener
	 */
	public void prepareUploadVideo(MediaBean bean, String description,
			final RequestListener requestListener) {
		if (!set.contains(bean.getFileName())) {
			set.add(bean.getFileName());
			VideoRequestPacket packet = new VideoRequestPacket(Utils.UUID(),
					"", CLIENT_PREPARE_UPLOAD_VIDEO_REQUEST,
					bean.getMediaExtension(), bean.getFileName(), description);
			AsyncUtils.getInstance().sendMediaPacket(packet, requestListener);
		}
	}

	/**
	 * @param fileName
	 * @param description
	 * @param listener
	 */
	public void publishVideoByTCP(final String fileName,
			final String description, final RequestListener listener) {
		AsyncUtils.getInstance().publishVideoByTCP(fileName, description,
				listener);
	}

	/**
	 * @param listener
	 */
	public void playVideo(MediaBean bean, final RequestListener listener) {
		MediaRequestPacket packet = new MediaRequestPacket(Utils.UUID(), "",
				CLIENT_PLAY_VIDEO, bean.getMediaExtension());
		AsyncUtils.getInstance().sendMediaPacket(packet, listener);
	}

	/**
	 * @param requestListener
	 */
	public void pauseVideo(MediaBean bean, final RequestListener requestListener) {
		MediaRequestPacket packet = new MediaRequestPacket(Utils.UUID(), "",
				CLIENT_PAUSE_VIDEO, bean.getMediaExtension());
		AsyncUtils.getInstance().sendMediaPacket(packet, requestListener);
	}

	/**
	 * @param requestListener
	 */
	public void stopVideo(MediaBean bean, final RequestListener requestListener) {
		MediaRequestPacket packet = new MediaRequestPacket(Utils.UUID(), "",
				CLIENT_STOP_VIDEO, bean.getMediaExtension());
		AsyncUtils.getInstance().sendMediaPacket(packet, requestListener);
	}

	/**
	 * @param requestListener
	 */
	public void moveFastVideo(MediaBean bean,
			final RequestListener requestListener) {
		MediaRequestPacket packet = new MediaRequestPacket(Utils.UUID(), "",
				CLIENT_FAST_VIDEO, bean.getMediaExtension());
		AsyncUtils.getInstance().sendMediaPacket(packet, requestListener);
	}

	/**
	 * @param requestListener
	 */
	public void moveSlowVideo(MediaBean bean,
			final RequestListener requestListener) {
		MediaRequestPacket packet = new MediaRequestPacket(Utils.UUID(), "",
				CLIENT_SLOW_VIDEO, bean.getMediaExtension());
		AsyncUtils.getInstance().sendMediaPacket(packet, requestListener);
	}
}
