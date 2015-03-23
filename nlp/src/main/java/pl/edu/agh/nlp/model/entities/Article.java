package pl.edu.agh.nlp.model.entities;

import java.io.Serializable;

public class Article implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Category implements Serializable {
		POLITICS(0), TECH(1), SPORT(2), HEALTH(3), FINANCE(4);
		private final int value;

		private Category(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	private String title;
	private String intro;
	private String text;
	private Long id;
	private Category category;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Article [title=" + title + ", intro=" + intro + ", text=" + text + ", id=" + id + ", category=" + category + "]";
	}

}
