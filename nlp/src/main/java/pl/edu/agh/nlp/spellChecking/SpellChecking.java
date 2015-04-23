package pl.edu.agh.nlp.spellChecking;

import java.io.IOException;
import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.language.Polish;
import org.languagetool.rules.RuleMatch;

public class SpellChecking {
	public static void main(String[] args) throws IOException {
		JLanguageTool langTool = new JLanguageTool(new Polish());
		langTool.activateDefaultPatternRules();
		List<RuleMatch> matches = langTool.check("");

		for (RuleMatch match : matches) {
			System.out.println("Potential error at line " + match.getLine() + ", column " + match.getColumn() + ": " + match.getMessage());
			System.out.println("Suggested correction: " + match.getSuggestedReplacements());
		}
	}
}
