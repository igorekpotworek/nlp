package pl.edu.agh.nlp.model.entities;

public class TopicWord {
	private Integer topicId;
	private String word;
	private Double weight;

	public TopicWord(Integer topicId, String word, Double weight) {
		this.topicId = topicId;
		this.word = word;
		this.weight = weight;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

}
