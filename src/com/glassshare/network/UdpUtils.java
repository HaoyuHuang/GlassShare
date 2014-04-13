package com.glassshare.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class UdpUtils {

	public static final int SERVER_CONNECT_PORT = 6000;
	
	public static final int SERVER_DATA_PORT = 6002;
	
	public static final int SERVER_CMD_PORT = 6001;

	public static final String CHAR_SET = "utf-8";
	
	static String SERVER_IP = "192.168.2.2";

	InetAddress getBroadcastAddress(Context mContext) throws IOException {
		WifiManager wifi = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow
		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	public String broadcast(Context mContext, String data)
			throws IOException {
		DatagramSocket socket = new DatagramSocket(SERVER_CONNECT_PORT);
		socket.setBroadcast(true);
		DatagramPacket packet = new DatagramPacket(data.getBytes(),
				data.length(), getBroadcastAddress(mContext), SERVER_CONNECT_PORT);
		socket.send(packet);

		byte[] buf = new byte[1024];
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		String resp = new String(buf, CHAR_SET);
		SERVER_IP = socket.getInetAddress().getHostAddress();
		Log.i("Udp", SERVER_IP);
		socket.close();
		return resp;
	}
}
