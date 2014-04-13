package com.glassshare.network;

/**
 * @author Haoyu
 *
 */
public interface RequestListener {
	
	public void onComplete(MediaPacket packet);

	
	public void onNetworkError(Exception error);

	
	public void onFault(Throwable fault);
}
