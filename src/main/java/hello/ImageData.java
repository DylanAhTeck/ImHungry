package hello;

public class ImageData {
	protected String thumbnailUrl;
	protected String name;
	protected String contentUrl;
	protected Integer width;
	protected Integer height;

	public ImageData() {
		this.thumbnailUrl = null;
		this.name = null;
		this.contentUrl = null;
		this.width = null;
		this.height = null;
	}
	
	public ImageData(String thumbnailUrl, String name, String contentUrl, Integer width, Integer height) {
		this.thumbnailUrl = thumbnailUrl;
		this.name = name;
		this.contentUrl = contentUrl;
		this.width = width;
		this.height = height;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
}
