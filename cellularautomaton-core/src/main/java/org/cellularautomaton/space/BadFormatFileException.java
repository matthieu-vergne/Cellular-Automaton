package org.cellularautomaton.space;

@SuppressWarnings("serial")
public class BadFormatFileException extends RuntimeException {

	public BadFormatFileException(String reason) {
		super(reason);
	}
}
