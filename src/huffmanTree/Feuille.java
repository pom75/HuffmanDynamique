package huffmanTree;

public class Feuille extends Arbre {
	public char lettre;

	public Feuille(char c, String code, int poids) {
		this.code = code;
		this.lettre = c;
		this.poids = poids;
		switch (c) {
		case '\n':
			this.idName = "\\n";
			break;
		case '"':
			this.idName = "\\\"";
			break;
		default:
			this.idName = String.valueOf(c);
			// ou Character.toString(char). (mieux que hack ""+c)
			// Id dot: n'importe quoi entre quotes, (avec quote échapée)
		}
	}

	public String getCode() {
		return code;
	}

	public String toString() {
		// pas performant mais bon.....
		if (lettre == '\n')
			return "Feuille '\\n', poids = " + poids + ", code = " + code;
		return "Feuille '" + lettre + "', poids = " + poids + ", code = "
				+ code;
	}

	private static final String dotTemplate = "     \"%1$s\" [shape=record, label = \"<f0> %1$s | <f1>  %2$s  |<f2> %3$d\" ] ;";
	// lettre, code, poids
	// TODO: color
	
	

	public String toDot() {
		return String.format(dotTemplate, idName, code, poids);
		//TODO idname, plutot que lettre pour avoir code échapé
	}
	// TODO fix ketter format
	// TODO: échapper cars!

}
