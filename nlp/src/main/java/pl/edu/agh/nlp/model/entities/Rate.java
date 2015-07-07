package pl.edu.agh.nlp.model.entities;

public class Rate {
	private Integer userId;
	private Integer articleId;
	private Double rating;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "UserArticle [userId=" + userId + ", articleId=" + articleId + ", rating=" + rating + "]";
	}

}
