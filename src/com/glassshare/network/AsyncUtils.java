package com.glassshare.network;

import java.io.IOException;

import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

/**
 * @author Haoyu
 * 
 */
public class AsyncUtils {

	// private Executor executor = null;

	private HttpUser httpUser;

	private static AsyncUtils asyncUtils;

	private TcpUser tcpUser;

	public static final int MAX_RETRY_COUNT = 3;

	public static final AsyncUtils getInstance() {
		if (asyncUtils == null) {
			asyncUtils = new AsyncUtils();
		}
		return asyncUtils;
	}

	private AsyncUtils() {
		// executor = Executors.newFixedThreadPool(1);
		httpUser = HttpUser.getInstance();
		tcpUser = TcpUser.Instance();
	}

	/**
	 * @param method
	 * @param photo
	 * @param fileName
	 * @param description
	 * @param format
	 * @param listener
	 */
	public void publishPhotoByHttp(final String method, final byte[] photo,
			final String fileName, final String description,
			final String format, final RequestListener listener) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					String resp = httpUser.publishPhotoByHttp(method, photo,
							fileName, description, format);
					if (resp != null) {
						MediaResponsePacket mrp = new MediaResponsePacket();
						mrp.fromJSON(resp);
						if (listener != null) {
							listener.onComplete(mrp);
						}
					}
				} catch (Throwable e) {
					if (listener != null) {
						listener.onFault(e);
					}
				}
				return null;
			}
		};
		task.execute(new Void[] {});
	}

	public void publishPhotoByTCP(final MediaPacket packet,
			final String fileName, final String description,
			final RequestListener listener) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					String resp = tcpUser.prepareUploadPhotoByTCP(fileName,
							description, packet);
					if (resp != null) {
						MediaResponsePacket mediaResponsePacket = new MediaResponsePacket();
						mediaResponsePacket.fromJSON(resp);
						if (listener != null) {
							listener.onComplete(mediaResponsePacket);
						}
					} else {
						if (listener != null) {
							listener.onNetworkError(new NetworkError(
									NetworkError.ERROR_NO_CONNECTION,
									"no connection", ""));
						}
					}
				} catch (JSONException e) {
					listener.onFault(e);
				}
				return null;
			}
		};
		task.execute(new Void[] {});
	}

	public void publishVideoByTCP(final String fileName,
			final String description, final RequestListener listener) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					String resp = tcpUser.uploadPhotoByTCP(fileName,
							description);
					if (resp != null) {
						MediaResponsePacket mediaResponsePacket = new MediaResponsePacket();
						mediaResponsePacket.fromJSON(resp);
						if (listener != null) {
							listener.onComplete(mediaResponsePacket);
						}
					} else {
						if (listener != null) {
							listener.onNetworkError(new NetworkError(
									NetworkError.ERROR_NO_CONNECTION,
									"no connection", ""));
						}
					}
				} catch (JSONException e) {
					listener.onFault(e);
				}
				return null;
			}
		};
		task.execute(new Void[] {});
	}

	public void getServerIpAddress(final Context context, final String data,
			final RequestListener requestListener) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					String resp = new UdpUtils().broadcast(context, data);
					if (resp != null) {
						if (requestListener != null) {
							MediaResponsePacket mediaResponsePacket = new MediaResponsePacket();
							mediaResponsePacket.fromJSON(resp);
							requestListener.onComplete(mediaResponsePacket);
						}
					}
				} catch (IOException e) {
					if (requestListener != null) {
						requestListener.onNetworkError(e);
					}
				} catch (JSONException e) {
					if (requestListener != null) {
						requestListener.onFault(e);
					}
				}
				return null;
			}
		};
		task.execute(new Void[] {});
	}

	public void sendMediaPacketByHttp(final MediaPacket packet,
			final RequestListener requestListener) {
		AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					String resp = httpUser.request("", packet.getBundle());
					if (resp != null) {
						MediaResponsePacket responsePacket = new MediaResponsePacket();
						responsePacket.fromJSON(resp);
						if (requestListener != null) {
							requestListener.onComplete(responsePacket);
						}
					} else {
						if (requestListener != null) {
							requestListener.onNetworkError(new NetworkError(
									NetworkError.ERROR_NO_CONNECTION,
									"no connection", ""));
						}
					}
				} catch (JSONException e) {
					if (requestListener != null) {
						requestListener.onFault(e);
					}
				}
				return null;
			}
		};
		async.execute(new Void[] {});
	}

	public void sendMediaPacket(final MediaPacket packet,
			final RequestListener requestListener) {
		AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					String resp = tcpUser.sendMediaPacket(packet);
					if (resp != null) {
						MediaResponsePacket responsePacket = new MediaResponsePacket();
						responsePacket.fromJSON(resp);
						if (requestListener != null) {
							requestListener.onComplete(responsePacket);
						}
					} else {
						if (requestListener != null) {
							requestListener.onNetworkError(new NetworkError(
									NetworkError.ERROR_NO_CONNECTION,
									"no connection", ""));
						}
					}
				} catch (JSONException e) {
					if (requestListener != null) {
						requestListener.onFault(e);
					}
				}
				return null;
			}
		};
		async.execute(new Void[] {});
	}
}
