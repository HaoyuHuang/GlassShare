package com.glassshare.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.glassshare.media.MediaLoader;

public class TcpUser {

	private boolean configured = false;

	private boolean keepAlive;

	private static TcpUser TCP_USER;

	public static TcpUser Instance() {
		if (TCP_USER == null) {
			TCP_USER = new TcpUser();
		}
		return TCP_USER;
	}

	public void config() {
		if (configured) {
			return;
		}
		configured = true;
	}

	public String prepareUploadPhotoByTCP(String fileName, String description,
			MediaPacket packet) {
		byte[] bytes = MediaLoader.fileToByteArray(new File(fileName));
		packet.setFileLength(bytes.length);
		this.sendMediaPacket(packet);
		return this.uploadPhotoByTCP(bytes, description);
	}
	
	public String uploadPhotoByTCP(byte[] bytes, String description) {
		String resp = null;
		Socket clientSocket = null;
		try {
			clientSocket = new Socket();
			clientSocket.setKeepAlive(keepAlive);
			clientSocket.setSoTimeout(5000);
			Log.i("tag", bytes.length + "");
			clientSocket.setSendBufferSize(bytes.length);
			Log.i("tag", "" + clientSocket.getSendBufferSize());
			if (clientSocket != null) {
				InetSocketAddress address = new InetSocketAddress(
						UdpUtils.SERVER_IP, UdpUtils.SERVER_DATA_PORT);
				try {
					clientSocket.connect(address, 5000);
					BufferedOutputStream bos = new BufferedOutputStream(
							clientSocket.getOutputStream());
					for (int i = 0; i < bytes.length; i += 65535) {
						int end = bytes.length - i > 65535 ? 65535
								: bytes.length - i;
						bos.write(Arrays.copyOfRange(bytes, i, i + end));
					}
					bos.close();
					BufferedInputStream bis = new BufferedInputStream(
							clientSocket.getInputStream());
					int data = -1;
					StringBuilder builder = new StringBuilder();
					while ((data = bis.read()) != -1) {
						builder.append(data);
					}
					resp = builder.toString();
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	public String uploadPhotoByTCP(String fileName, String description) {
		String resp = null;
		Socket clientSocket = null;
		byte[] bytes = MediaLoader.fileToByteArray(new File(fileName));
		try {
			clientSocket = new Socket();
			clientSocket.setKeepAlive(keepAlive);
			clientSocket.setSoTimeout(5000);
			Log.i("tag", bytes.length + "");
			clientSocket.setSendBufferSize(bytes.length);
			Log.i("tag", "" + clientSocket.getSendBufferSize());
			if (clientSocket != null) {
				InetSocketAddress address = new InetSocketAddress(
						UdpUtils.SERVER_IP, UdpUtils.SERVER_DATA_PORT);
				try {
					clientSocket.connect(address, 5000);
					BufferedOutputStream bos = new BufferedOutputStream(
							clientSocket.getOutputStream());
					for (int i = 0; i < bytes.length; i += 65535) {
						int end = bytes.length - i > 65535 ? 65535
								: bytes.length - i;
						bos.write(Arrays.copyOfRange(bytes, i, i + end));
					}
					bos.close();
					BufferedInputStream bis = new BufferedInputStream(
							clientSocket.getInputStream());
					int data = -1;
					StringBuilder builder = new StringBuilder();
					while ((data = bis.read()) != -1) {
						builder.append(data);
					}
					resp = builder.toString();
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	public String sendMediaPacket(MediaPacket packet) {
		String resp = null;
		Socket clientSocket = null;
		try {
			JSONObject json = packet.toJSON();
			Log.i("request", packet.toJSON().toString());
			clientSocket = new Socket();
			clientSocket.setKeepAlive(keepAlive);
			clientSocket.setTcpNoDelay(true);
			clientSocket.setSendBufferSize(json.toString().length());
			clientSocket.setSoTimeout(5000);
			if (clientSocket != null) {
				InetSocketAddress address = new InetSocketAddress(
						UdpUtils.SERVER_IP, UdpUtils.SERVER_CMD_PORT);
				clientSocket.connect(address, 5000);
				BufferedOutputStream bos = new BufferedOutputStream(
						clientSocket.getOutputStream());

				bos.write(packet.toJSON().toString().getBytes());
				bos.close();
				BufferedInputStream bis = new BufferedInputStream(
						clientSocket.getInputStream());
				int data = -1;
				StringBuilder builder = new StringBuilder();
				while ((data = bis.read()) != -1) {
					builder.append(data);
				}
				resp = builder.toString();
				Log.i("response", builder.toString());
				clientSocket.close();
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
}
