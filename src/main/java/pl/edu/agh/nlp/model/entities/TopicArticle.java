package pl.edu.agh.nlp.model.entities;

public class TopicArticle {
	private Integer articleId;
	private Integer topicId;
	private Double weight;

	public TopicArticle() {
		super();
	}

	public TopicArticle(Integer articleId, Integer topicId, Double weight) {
		this.articleId = articleId;
		this.topicId = topicId;
		this.weight = weight;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

}
