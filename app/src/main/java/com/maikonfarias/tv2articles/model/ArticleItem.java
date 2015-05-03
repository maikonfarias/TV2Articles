package com.maikonfarias.tv2articles.model;

import java.io.Serializable;

public class ArticleItem implements Serializable {

	private String title;
	private String url;
	private String hasVideo;
	private String identifier;
	private String isBreaking;
	private String isExternal;
	private String isLive;
	private String modified;
	private String publicUrl;
	private String smallTeaserImage;
	private String teaserImage;

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

	public String getHasVideo() {
		return hasVideo;
	}

	public void setHasVideo(String hasVideo) {
		this.hasVideo = hasVideo;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getIsBreaking() {
		return isBreaking;
	}

	public void setIsBreaking(String isBreaking) {
		this.isBreaking = isBreaking;
	}

	public String getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(String isExternal) {
		this.isExternal = isExternal;
	}

	public String getIsLive() {
		return isLive;
	}

	public void setIsLive(String isLive) {
		this.isLive = isLive;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
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
}
