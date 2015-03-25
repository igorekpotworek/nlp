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
		List<RuleMatch> matches = langTool
				.check("Janowicz do Irvingu przyby³ wprost z Indian Wells, gdzie przegra³ w I rundzie turnieju ATP Masters 1000 z Diego Schwartzmanem. £odzianin mecz z Rogerem-Vasselinem, którego w przesz³oœci pokonywa³ cztery razy, w tym trzykrotnie w g³ównym cyklu, rozpocz¹³ bardzo s³abo - dwukrotnie zosta³ prze³amany i przegrywa³ 2:5.");

		for (RuleMatch match : matches) {
			System.out.println("Potential error at line " + match.getLine() + ", column " + match.getColumn() + ": "
					+ match.getMessage());
			System.out.println("Suggested correction: " + match.getSuggestedReplacements());
		}
	}
}
