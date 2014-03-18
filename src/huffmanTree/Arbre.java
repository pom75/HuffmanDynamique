package huffmanTree;

public  class Arbre {
	//TODO Abstract

	Arbre pere;
	public int poids;
	public String code;
	public int posList;  // Arbre connait sa position dans la liste (gain de temps!)

	// Mes a jour les code a partir du noeud
	public void miseAJourCode() {
		// mise à jour des noueds fils si existants
		if (!(this instanceof Feuille)) {
			NoeudInterne ni = (NoeudInterne) this;
			ni.filsGauche.code = ni.code + "0";
			ni.filsDroit.code = ni.code + "1";

			// System.out.println(this);
			ni.filsDroit.miseAJourCode();
			ni.filsGauche.miseAJourCode();

		} else
			return;
		// améliorer.

	}


	protected String idName;

	protected String getId() {
		return idName;
	}
	
	// TODO ABSTRACT
	protected String toDot(){
		return null;
	}
	

}