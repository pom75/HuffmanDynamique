package huffmanTree;

public class NoeudInterne extends Arbre {
	public Arbre filsGauche = null;
	public Arbre filsDroit = null;

	private static int nb = 0;

	public NoeudInterne(Arbre ag, Arbre ad, int poids, String code) {
		this.filsGauche = ag;
		this.filsDroit = ad;
		this.code = code;
		this.poids = poids;

		this.idName = "ni" + (++nb);

	}

	public String toString() {
		return "Noued interne:  poids = " + poids + ", code = " + code + " ;";
	}

	public String toDot() {
		// TODO? format (performances?)
		return
		// Description Noeuds
		"    \"" + idName + "\" [ label = \"" + poids + "\" ]; \n    \""

				// Description Lien vers fils
				+ idName + "\" -> \"" + filsGauche.getId() + "\" ;\n    \""
				+ idName + "\" -> \"" + filsDroit.getId() + "\" ;";

	}

}
