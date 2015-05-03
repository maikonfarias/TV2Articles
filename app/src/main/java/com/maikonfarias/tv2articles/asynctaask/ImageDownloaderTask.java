package com.maikonfarias.tv2articles.asynctaask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;

import static com.maikonfarias.tv2articles.CustomListAdapter.ViewHolder;
import com.maikonfarias.tv2articles.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	private static final Map<String, Bitmap> imagesCache = new HashMap<String, Bitmap>();
	private static final Map<ImageView,String> articleIdMap = new HashMap<ImageView,String>();
	private final WeakReference<ViewHolder> viewHolderReference;
	private String articleId;
	private static final Executor pool = Executors.newCachedThreadPool();

	public ImageDownloaderTask(ViewHolder viewHolder) {
		viewHolderReference = new WeakReference<ViewHolder>(viewHolder);
	}

	public static void loadImage(ViewHolder viewHolder, String url, String articleId) {

		articleIdMap.put(viewHolder.getImageView(),articleId);

		Bitmap image = imagesCache.get(url);
		if(image != null) {
			ImageView imageView = viewHolder.getImageView();
			imageView.setImageBitmap(image);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				new ImageDownloaderTask(viewHolder)
						.executeOnExecutor(pool, url, articleId);
			} else {
				new ImageDownloaderTask(viewHolder)
						.execute(url, articleId);
			}
		}
	}

	@Override
	// Actual download method, run in the task thread
	protected Bitmap doInBackground(String... params) {
		// params comes from the execute() call: params[0] is the url.
		articleId = params[1];
		Bitmap image = imagesCache.get(params[0]);


		if(image == null) {
			image = downloadBitmap(params[0]);

			imagesCache.put(params[0],image);

		}

		return image;
	}

	@Override
	// Once the image is downloaded, associates it to the imageView
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		ViewHolder viewHolder = viewHolderReference.get();

		if (viewHolder != null) {
			ImageView imageView = viewHolder.getImageView();
			if (bitmap != null) {
				if(articleId.equals(articleIdMap.get(imageView))) {
					imageView.setImageBitmap(bitmap);
				}
			} else {
				imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(R.drawable.list_placeholder));
			}
		}

	}

	static Bitmap downloadBitmap(String url) {
		if(URLUtil.isValidUrl(url)){

			final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			final HttpGet getRequest = new HttpGet(url);
			try {
				HttpResponse response = client.execute(getRequest);
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					Log.w("ImageDownloader", "Error " + statusCode
							+ " while retrieving bitmap from " + url);
					return null;
				}

				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					try {
						inputStream = entity.getContent();
						final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
						return bitmap;
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (Exception e) {
				// Could provide a more explicit error message for IOException or
				// IllegalStateException
				getRequest.abort();
				Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
			} finally {
				if (client != null) {
					client.close();
				}
			}
			return null;
		
		}
		return null;
	}

}