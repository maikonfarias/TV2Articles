package com.maikonfarias.tv2articles.model;

import android.text.format.DateUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArticleItem implements Serializable {

	private String title;
	private String url;
	private Boolean hasVideo;
	private String identifier;
	private Boolean isBreaking;
	private Boolean isExternal;
	private Boolean isLive;
	private Long modified;
	private String publicUrl;
	private String smallTeaserImage;
	private String teaserImage;
	private String category;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getHasVideo() {
		return hasVideo;
	}

	public void setHasVideo(Boolean hasVideo) {
		this.hasVideo = hasVideo;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Boolean getIsBreaking() {
		return isBreaking;
	}

	public void setIsBreaking(Boolean isBreaking) {
		this.isBreaking = isBreaking;
	}

	public Boolean getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public Long getModified() {
		return modified;
	}

	public Date getModifieldDate() {
		try {
			return new Date(timesLong);
		}
		catch(Exception e) {
			return new Date();
		}
	}

	public String getModifiedTimeAgo() {
		Long timeArticle = getModifieldDate().getTime();
		Long timeSystem = System.currentTimeMillis();
		CharSequence timeAgoString = DateUtils.getRelativeTimeSpanString(timeArticle, timeSystem, DateUtils.SECOND_IN_MILLIS);
		return  timeAgoString.toString();
	}

	public void setModified(Long modified) {
		this.modified = modified;
	}

	public String getPublicUrl() {
		return publicUrl;
	}

	public void setPublicUrl(String publicUrl) {
		this.publicUrl = publicUrl;
	}

	public String getSmallTeaserImage() {
		return smallTeaserImage;
	}

	public void setSmallTeaserImage(String smallTeaserImage) {
		this.smallTeaserImage = smallTeaserImage;
	}

	public String getTeaserImage() {
		return teaserImage;
	}

	public void setTeaserImage(String teaserImage) {
		this.teaserImage = teaserImage;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
