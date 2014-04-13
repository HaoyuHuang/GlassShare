package com.glassshare.media;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

public class MediaFactory {
	public static Bitmap readBitmapFromFile(String photoPath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
		return bitmap;
	}

	public static String preprocess(String dcimDir, List<File> paths) {
		String gsDir = dcimDir + "/glassShare";
		File dir = new File(gsDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		for (File file : paths) {
			saveAsPNG(gsDir, file);
		}
		return gsDir;
	}

	public static Bitmap readImageThumnail(String path, int width, int height) {
		return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path),
				width, height, Thumbnails.MINI_KIND);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap getVideoPreview(String videoFilePath) {
		Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoFilePath,
				MediaStore.Images.Thumbnails.MINI_KIND);
		return thumbnail;
	}

	/**
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}

	/**
	 * 
	 * @param bitmap
	 * @param name
	 */
	public static void saveAsPNG(String pngPath, File file) {
		File pngFile = new File(replaceExtensionToPNG(pngPath, file));
		Log.i("png file", pngFile.getAbsolutePath());
		try {
			if (!pngFile.exists()) {
				pngFile.createNewFile();
			}
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			FileOutputStream out = new FileOutputStream(pngFile);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param bitmap
	 * @param path
	 */
	public static void saveAsJPGE(Bitmap bitmap, File file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public static Uri getImageUri(Context inContext, String imagePath) {
		Bitmap inImage = BitmapFactory.decodeFile(imagePath);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public static String replaceExtensionToPNG(String path, File file) {
		int lastIndex = file.getName().lastIndexOf(".");
		if (lastIndex != -1) {
			path = path + File.separator
					+ file.getName().substring(0, lastIndex) + ".png";
		}
		return path;
	}

}
