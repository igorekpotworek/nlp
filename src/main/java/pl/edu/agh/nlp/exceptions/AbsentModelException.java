package pl.edu.agh.nlp.exceptions;

public class AbsentModelException extends Exception {

	private static final long serialVersionUID = 1L;

	public AbsentModelException() {
		super();
	}

	public AbsentModelException(String bodyExceptionMessage) {
		super(bodyExceptionMessage);
	}

	public AbsentModelException(Exception e) {
		super(e);
	}
}
