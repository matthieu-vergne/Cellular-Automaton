package org.cellularautomaton.space;

@SuppressWarnings("serial")
public class BadFileContentException extends RuntimeException {

	public BadFileContentException(String reason) {
		super(reason);
	}
}
