package com.maikonfarias.tv2articles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maikonfarias.tv2articles.asynctaask.ImageDownloaderTask;
import com.maikonfarias.tv2articles.model.ArticleItem;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

	private ArrayList<ArticleItem> listData;

	private LayoutInflater layoutInflater;

	private Context mContext;
	
	public CustomListAdapter(Context context, ArrayList<ArticleItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.fragment_article_list_row, null);
			holder = new ViewHolder();
			holder.setHeadlineView((TextView) convertView.findViewById(R.id.title));
			holder.setReportedDateView((TextView) convertView.findViewById(R.id.date));
			holder.setImageView((ImageView) convertView.findViewById(R.id.thumbImage));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}




		ArticleItem newsItem = listData.get(position);
		holder.getHeadlineView().setText(newsItem.getTitle());
		holder.reportedDateView.setText(newsItem.getModifiedTimeAgo());

		if(newsItem.getHasVideo() == "true") {
			convertView.findViewById(R.id.videoImage).setVisibility(View.VISIBLE);
		} else {
			convertView.findViewById(R.id.videoImage).setVisibility(View.GONE);
		}

		if(newsItem.getIsLive() == "true") {
			convertView.findViewById(R.id.live).setVisibility(View.VISIBLE);
		} else {
			convertView.findViewById(R.id.live).setVisibility(View.GONE);
		}

		if(newsItem.getIsBreaking() == "true") {
			convertView.findViewById(R.id.breaking).setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.row).setBackgroundColor(Color.YELLOW);
		} else {
			convertView.findViewById(R.id.breaking).setVisibility(View.GONE);
			convertView.findViewById(R.id.row).setBackgroundColor(0);
		}

		holder.getImageView().setImageResource(R.drawable.list_placeholder);

		if(newsItem.getSmallTeaserImage() == null
				|| newsItem.getSmallTeaserImage().equals("null")) {

			holder.imageView.setVisibility(View.GONE);
		} else if (holder.getImageView() != null) {
			holder.imageView.setVisibility(View.VISIBLE);
			ImageDownloaderTask.loadImage(holder, newsItem.getSmallTeaserImage(), newsItem.getIdentifier());
		}

		return convertView;
	}

	public static class ViewHolder {
		private TextView headlineView;
		private TextView reportedDateView;
		private ImageView imageView;

		public TextView getHeadlineView() {
			return headlineView;
		}

		public void setHeadlineView(TextView headlineView) {
			this.headlineView = headlineView;
		}

		public TextView getReportedDateView() {
			return reportedDateView;
		}

		public void setReportedDateView(TextView reportedDateView) {
			this.reportedDateView = reportedDateView;
		}

		public ImageView getImageView() {
			return imageView;
		}

		public void setImageView(ImageView imageView) {
			this.imageView = imageView;
		}
	}
}
