package pl.edu.agh.nlp.spark;

import java.io.IOException;
import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.language.Polish;
import org.languagetool.rules.RuleMatch;

public class Test1 {
	public static void main(String[] args) throws IOException {
		JLanguageTool langTool = new JLanguageTool(new Polish());
		langTool.activateDefaultPatternRules();
		List<RuleMatch> matches = langTool
				.check("Janowicz do Irvingu przyby� wprost z Indian Wells, gdzie przegra� w I rundzie turnieju ATP Masters 1000 z Diego Schwartzmanem. �odzianin mecz z Rogerem-Vasselinem, kt�rego w przesz�o�ci pokonywa� cztery razy, w tym trzykrotnie w g��wnym cyklu, rozpocz�� bardzo s�abo - dwukrotnie zosta� prze�amany i przegrywa� 2:5.");

		for (RuleMatch match : matches) {
			System.out.println("Potential error at line " + match.getLine() + ", column " + match.getColumn() + ": " + match.getMessage());
			System.out.println("Suggested correction: " + match.getSuggestedReplacements());
		}
	}
}