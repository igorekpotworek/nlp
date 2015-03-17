package pl.edu.agh.nlp.model;

public class Article {
	private Long id;
	private String title;
	private String intro;
	private String text;

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", intro=" + intro + ", text=" + text + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

}
