package pl.edu.agh.nlp.model;

public class Artykul {
	private String tytul;
	private String wstep;
	private String tekst;

	public String getTytul() {
		return tytul;
	}

	public void setTytul(String tytul) {
		this.tytul = tytul;
	}

	public String getWstep() {
		return wstep;
	}

	public void setWstep(String wstep) {
		this.wstep = wstep;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	@Override
	public String toString() {
		return "Artykul [tytul=" + tytul + ", wstep=" + wstep + ", tekst="
				+ tekst + "]";
	}

}
