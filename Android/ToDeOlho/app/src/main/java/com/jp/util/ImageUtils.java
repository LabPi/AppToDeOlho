package com.jp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * @author Ricardo Lecheta
 * 
 */
public class ImageUtils {

	private static final String TAG = ImageUtils.class.getName();

	public static Bitmap getResizedImage(Uri uriFile, int width, int height) {
		try {
			// Carrega a imagem original em memória
			Bitmap bitmap = BitmapFactory.decodeFile(uriFile.getPath());

			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			float scaleX = (float) width / bitmap.getWidth();
			float scaleY = (float) height / bitmap.getHeight();

			Matrix matrix = new Matrix();
			matrix.setScale(scaleX, scaleY);

			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h,
					matrix, true);

			resizedBitmap = fixMatrix(uriFile, resizedBitmap);

			// Limpa a memória do arquivo original (o grande que fizemos resize)
			bitmap.recycle();
			bitmap = null;

			// Retorna a imagem com o resize
			return resizedBitmap;
		} catch (RuntimeException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}

	public static Bitmap getResizedImage2(Uri uriFile, int width, int height) {
		try {
			// Configura o BitmapFactory para apenas ler o tamanho da imagem
			// (sem carregá-la em memória)
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// Faz o decode da imagem
			BitmapFactory.decodeFile(uriFile.getPath(), opts);
			// Lê a largura e altura do arquivo
			int w = opts.outWidth;
			int h = opts.outHeight;

			// Fator de escala
			opts.inSampleSize = Math.min(w / width, h / height);
			// Agora deixa carregar o bitmap completo
			opts.inJustDecodeBounds = false;

			Bitmap bitmap = BitmapFactory.decodeFile(uriFile.getPath(), opts);

			Bitmap newBitmap = fixMatrix(uriFile, bitmap);

			bitmap.recycle();

			return newBitmap;
		} catch (RuntimeException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}

	public static Bitmap loadResizedBitmap(String filename, int width,
			int height, boolean exact) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		if (options.outHeight > 0 && options.outWidth > 0) {
			options.inJustDecodeBounds = false;
			options.inSampleSize = 2;
			while (options.outWidth / options.inSampleSize > width
					&& options.outHeight / options.inSampleSize > height) {
				options.inSampleSize++;
			}
			options.inSampleSize--;

			bitmap = BitmapFactory.decodeFile(filename, options);
			if (bitmap != null && exact) {
				bitmap = Bitmap
						.createScaledBitmap(bitmap, width, height, false);
			}
		}
		return bitmap;
	}

	private static Bitmap fixMatrix(Uri uriFile, Bitmap bitmap)
			throws IOException {
		Matrix matrix = new Matrix();

		// Classe para ler tags escritas no JPEG
		/**
		 * Para utilizar esta classe precisa de Android 2.2 ou superior
		 */
		ExifInterface exif = new ExifInterface(uriFile.getPath());

		// Lê a orientação que foi salva a foto
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);

		boolean fix = false;

		// Rotate bitmap
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_180:
			matrix.postRotate(180);
			fix = true;
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			matrix.postRotate(90);
			fix = true;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			matrix.postRotate(270);
			fix = true;
			break;
		default:
			// ORIENTATION_ROTATE_0
			fix = false;
			break;
		}

		if (!fix) {
			return bitmap;
		}

		// Corrige a orientação (passa a matrix)
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		bitmap.recycle();
		bitmap = null;

		return newBitmap;
	}
}
