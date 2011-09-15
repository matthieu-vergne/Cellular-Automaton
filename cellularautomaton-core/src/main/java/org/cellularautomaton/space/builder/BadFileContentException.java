package org.cellularautomaton.space.builder;

@SuppressWarnings("serial")
public class BadFileContentException extends RuntimeException {

	public BadFileContentException(String reason) {
		super(reason);
	}
}
