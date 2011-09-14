package org.cellularautomaton.sample.wireworldscripted;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.cellularautomaton.CellularAutomaton;
import org.cellularautomaton.space.FileSpaceBuilder;

public class WireWorldScriptedAutomatonFactory {

	public static CellularAutomaton<Character> createAutomaton() {
		StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);
		out.println("[config]");
		out.println("states=X_o.");
		out.println("[rule]");
		out.println("(0,0)=_ & (-1,-1)+(-1,+0)+(-1,+1)+(+0,-1)+(+0,+1)+(+1,-1)+(+1,+0)+(+1,+1)=1o : o");
		out.println("(0,0)=_ & (-1,-1)+(-1,+0)+(-1,+1)+(+0,-1)+(+0,+1)+(+1,-1)+(+1,+0)+(+1,+1)=2o : o");
		out.println("(0,0)=o:.");
		out.println("(0,0)=.:_");
		out.println("[cells]");
		out.println("XXXXXXXXXXXXXXX");
		out.println("XXXXX__XX____XX");
		out.println("_____X___XXXXoX");
		out.println("XXXXX__XX___.XX");
		out.println("XXXXXXXXXXXXXXX");
		out.println("XXXXX__XX____XX");
		out.println("______X__XXXXoX");
		out.println("XXXXX__XX___.XX");
		out.println("XXXXXXXXXXXXXXX");
		out.close();

		FileSpaceBuilder builder = new FileSpaceBuilder();
		builder.createSpaceFromString(sw.getBuffer().toString());

		CellularAutomaton<Character> automaton = new CellularAutomaton<Character>(
				builder.getSpaceOfCell());

		return automaton;
	}
}
