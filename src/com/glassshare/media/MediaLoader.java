package com.glassshare.media;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class MediaLoader {

	public static final String LOG_TAG = "mediaLoader";

	public static void logger(String message) {
		Log.i(LOG_TAG, message);
	}

	public static void preloadMedias(Context context, List<MediaBean> list,
			MediaType type) {
		if (isExternalStorageMounted()) {
			File storageDir = Environment.getExternalStorageDirectory();
			File cameraDir = new File(storageDir.getAbsolutePath()
					+ "/DCIM/Camera");
			if (cameraDir != null) {
				getFile(cameraDir, list, type);

			}
		}
	}

	public static void getFile(File dir, List<MediaBean> list, MediaType type) {
		if (dir != null) {
			if (dir.isDirectory()) {
				for (File file : dir.listFiles()) {
					if (file.isDirectory()) {
						getFile(file, list, type);
					} else if (file.isFile()) {
						MediaType t = getMediaType(file.getName());
						if (t.equals(type)) {
							MediaBean bean = new MediaBean();
							bean.setMediaFilePath(file.getAbsolutePath());
							bean.setFileName(file.getName());
							bean.setMediaType(type);
							list.add(bean);
						}
						Log.i("file", file.getAbsolutePath());
					}
				}
			} else if (dir.isFile()) {
				Log.i("file", dir.getAbsolutePath());
			}
		}
	}

	/**
	 * 
	 * 
	 * @param fileName
	 *            the photo fileName
	 * @return contentType
	 * @throws RuntimeException
	 *             suffix of file is not supported
	 * */
	public static MediaType getMediaType(String fileName) {
		MediaType type = MediaType.NULL;
		fileName = fileName.toLowerCase(Locale.US);
		if (fileName.endsWith(".jpg") || fileName.endsWith(".png")
				|| fileName.endsWith(".jpeg") || fileName.endsWith(".gif")
				|| fileName.endsWith(".bmp")) {
			type = MediaType.PHOTO;
		} else if (fileName.endsWith(".mp4")) {
			type = MediaType.VIDEO;
		}
		return type;
	}

	public static boolean isExternalStorageMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * @param b
	 * @param OutputFile
	 * @return
	 */
	public static File getFileFromBytes(byte[] b, String OutputFile) {
		BufferedOutputStream bos = null;
		File file = null;
		try {
			file = new File(OutputFile);
			FileOutputStream fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(b);
		} catch (Exception e) {

		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {

				}
			}
		}
		return file;
	}

	public static byte[] fileToByteArray(File file) {
		try {
			return streamToByteArray(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			logger(e.getMessage());
			return null;
		}
	}

	public static byte[] streamToByteArray(InputStream inputStream) {
		byte[] content = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(inputStream);

		try {
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = bis.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}

			content = baos.toByteArray();
			if (content.length == 0) {
				content = null;
			}

			baos.close();
			bis.close();
		} catch (IOException e) {
			logger(e.getMessage());
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					logger(e.getMessage());
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					logger(e.getMessage());
				}
			}
		}
		return content;
	}
}
