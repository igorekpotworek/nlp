package pl.edu.agh.nlp.model.entities;

public class Article {
	private String title;
	private String intro;
	private String text;

	@Override
	public String toString() {
		return "Article [title=" + title + ", intro=" + intro + ", text="
				+ text + "]";
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
