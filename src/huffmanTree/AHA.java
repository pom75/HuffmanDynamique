package huffmanTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Classe gestionnaire de l'arbre de Humman
 * 
 * @author Steph
 */
public class AHA {

	Arbre racineAha;
	boolean first = true;
	Map<Character, Feuille> index = new HashMap<>();
	Arbre feuilleSpeciale = new Feuille('#', "", 0);
	LinkedList<Arbre> ordreNoeuds = new LinkedList<>();

	public AHA() {
		racineAha = feuilleSpeciale;
		racineAha.pere = null; // Check?
		ordreNoeuds.addLast(feuilleSpeciale);
	}

	// Test si le char est présent dans l'arbre
	public boolean charIsPresent(char c) {
		return index.containsKey(c);
	}

	// Retourn le code(arbre) du caractere
	public String getCodeChar(char charAt) {
		return index.get(charAt).code;
	}

	public String indextoString() {

		StringBuffer sb = new StringBuffer();
		sb.append(">> Index:\n");
		for (int i = ordreNoeuds.size() - 1; i >= 0; i--) {
			sb.append("--> ").append(ordreNoeuds.get(i)).append("\n");

		}
		sb.append("Fin index");
		return sb.toString();
	}

	// TODO : REFAIRE L'ininatialisation des varibles !!!! et l'ajout des nous
	// interne dans la list

	// On donne le char a traiter a la fonction modification
	public void modificationAHA(char c) {
		// §DISStats.nbTotalChar++;

		Arbre q = null;
		Arbre p = null;
		Feuille s = null;

		/*
		 * Si c est un nouveau caractère on crée une nouvelle feuille , un
		 * nouveau noeud qui a comme fils charSpecial et notre char et on
		 * l'ajoute a notre structure
		 */
		if (!this.charIsPresent(c)) {
			// §DISStats.nbCharDiff++;

			// §DISStats.printDebug("Nouveau caractère : " + c);

			p = feuilleSpeciale.pere;
			Arbre buff = feuilleSpeciale;

			s = new Feuille(c, ((Feuille) buff).getCode() + "1", 1);
			index.put(c, s);
			s.posList = ordreNoeuds.size() - 1;

			q = new NoeudInterne(buff, s, 0, buff.code);

			buff.code += 0;
			s.pere = q;

			buff.pere = q;


			ordreNoeuds.addLast(q);
			ordreNoeuds.addLast(s);
			
			// mise à jour spéciale si l'arbre est constitué feuille spéciale
			if (first) {
				first = false;
				racineAha = q;
			} else {
				q.pere = p;
				((NoeudInterne) p).filsGauche = q;
			}
			this.majList(); // Pop Noeuds plutot que retirer?

		}
		// Sinon on prends un pointeur sur la feuille a incrementer
		else {
			// §DISStats.printDebug("Caractere déja présent !");

			// Soit q le feuille correspondant a s dans H
			q = index.get(c);
		}
		// On donne notre neoud courant (pere de c et # ou feuille a inscriment)
		// A la fonction traitement
		this.traitement(q);
	}

	private void traitement(Arbre q) {
		Arbre nouedCourant = q;
		Arbre viole = null;
		boolean swap = false;
		// §DISStats.printDebug(">>> Index avant traitement \n" +
		// this.indextoString());

		/*
		 * On remonte la liste des peres de q dans le but de les inscrémenter
		 */
		while (nouedCourant.pere != null) {
			nouedCourant.poids++;
			/*
			 * Apres l'incrementation , on remonte dans l'odre GDBH pour
			 * controler
			 */
			for (int i = nouedCourant.posList - 1; i > 0; i--) {
				// System.out.println("p est " +
				// p.code+" et de poids : "+p.poids);

				Arbre tmp = ((Arbre) ordreNoeuds.get(i));

				// Si la condition n'est plus respecté
				if (nouedCourant.poids > tmp.poids) {

					viole = tmp;
					// §DISStats.printDebug("!> condition Violeeeee");
				}

			}
			/*
			 * Si il y a plusieur viole , seul le viole avec le noeud le plus
			 * haut dans l'ordre GDBH sera swaper
			 */
			if (viole != null) {
				// On ne doit pas swapper un noeud avec son pere
				if (!nouedCourant.pere.code.contentEquals(viole.code)) {
					swap(nouedCourant, viole);
					// §DISStats.printDebug("JE VIENS DE SWAP");
					swap = true;
					viole = null;
				}

			}
			// On passe au pere
			nouedCourant = nouedCourant.pere;
		}
		// Ne met à jour la liste que si swap réalisé sinon non necessaire
		if (swap) {
			this.majList();
		}
		// ON inscrmente le poids du noeud racine
		nouedCourant.poids++;

		// §DISStats.printDebug(">>> Traitement Index après traitement \n" +
		// §DISStats this.indextoString());

	}

	public String getCodeSpecialChar() {
		// §DISStats.printDebug("Récupération caractère spécial");
		return feuilleSpeciale.code;
	}

	public Arbre finBloc(Arbre q) {
		Arbre rep = q;
		// On parcourt la liste de q a q +1
		for (int i = ordreNoeuds.size() - 1; i >= 0; i--) {
			// Si q < q+1 q := q+1
			if (rep.poids == ((Arbre) ordreNoeuds.get(i)).poids) {
				rep = (Arbre) ordreNoeuds.get(i);
			}
		}
		return rep;
	}

	/**
	 * Echange les n.pere , echange les n.pere.Fils , echange les n.code
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	public void swap(Arbre n1, Arbre n2) {
		// §DISStats.nbSwap++;
		// §DISlong tstart = System.currentTimeMillis();
		// §DISStats.printDebug("Swap entre " + n1 + " et " + n2);

		/*
		 * Code plus lisible mais beugé.... NoeudInterne p1 = (NoeudInterne)
		 * n1.pere; NoeudInterne p2 = (NoeudInterne) n2.pere;
		 * 
		 * Boolean n1agauche = p1.filsGauche == n1; Boolean n2agauche =
		 * p2.filsGauche == n2;
		 * 
		 * 
		 * if(n1agauche){ p1.filsGauche = n2; }else { p1.filsDroit = n2; }
		 * n2.pere = p1;
		 * 
		 * if(n2agauche){ p2.filsGauche = n1; }else { p2.filsDroit = n1; }
		 * n2.pere = p1;
		 * 
		 * p1.miseAJourCode(); n2.miseAJourCode();
		 */

		String s1 = n1.code;
		String s2 = n2.code;
		Arbre buff1 = new Arbre();
		buff1.code = n1.code;
		buff1.pere = n1.pere;
		Arbre buff2 = n2;
		buff2.code = n2.code;
		buff2.pere = n2.pere;

		Arbre buff3 = n1.pere;
		Arbre buff4 = n2.pere;

		if (((NoeudInterne) n2.pere).filsDroit.code.contentEquals(n2.code)) {
			if (((NoeudInterne) buff1.pere).filsDroit.code
					.contentEquals(n1.code)) {
				((NoeudInterne) n1.pere).filsDroit = n2;
			} else {
				((NoeudInterne) n1.pere).filsGauche = n2;
			}

			((NoeudInterne) n2.pere).filsDroit = n1;

		} else {
			if (((NoeudInterne) buff1.pere).filsDroit.code
					.contentEquals(n1.code)) {
				((NoeudInterne) n1.pere).filsDroit = n2;
			} else {
				((NoeudInterne) n1.pere).filsGauche = n2;
			}
			((NoeudInterne) n2.pere).filsGauche = n1;
		}

		n2.code = s1;
		n1.code = s2;
		n2.pere = buff3;
		n1.pere = buff4;

		// On met a jour les codes des fils des noeuds echangés
		n1.miseAJourCode();
		n2.miseAJourCode();

		// §DISStats.addTimeSwap(System.currentTimeMillis() - tstart);
	}

	/**
	 * MAJ de la list ordonée des noeuds On met a jour la liste grace a un
	 * parcourt en largeur de l'arbre
	 */
	private void majList() {

		// §DISlong tstart = System.currentTimeMillis();
		// §DISStats.nbMajList++;

		// §DISStats.printDebug(">>> Traitement avant mise à jour \n"
		// §DISStats + this.indextoString());
		ordreNoeuds.clear();
		LinkedList<Arbre> buffList = new LinkedList<Arbre>();
		buffList.addLast(racineAha);

		while (!buffList.isEmpty()) {
			Arbre n = buffList.pop();

			ordreNoeuds.addLast(n);
			n.posList = ordreNoeuds.size() - 1;
			if (n instanceof NoeudInterne) {
				buffList.addLast(((NoeudInterne) n).filsDroit);
				buffList.addLast(((NoeudInterne) n).filsGauche);
			}
		}

		// §DISStats.printDebug(">>> Traitement après \n" +
		// this.indextoString());

		// §DISStats.addTimeList(System.currentTimeMillis() - tstart);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Arbre:\n").append(racineAha);
		sb.append("Index:\n").append(index);
		sb.append("orderNoeudPoids").append("toAdapt");

		return sb.toString();
	}

	// TODO
	/**
	 * génère un arbre de l'arbre d'encodage
	 * 
	 * @return code Dot pour décrire le graphe
	 */
	public String toDot() {

		StringBuffer sb = new StringBuffer();

		// Prélude
		sb.append("digraph  myAHa { \n");

		// Parcours Proffondeur
		LinkedList<Arbre> fileNoeuds = new LinkedList<Arbre>();

		fileNoeuds.addLast(racineAha);
		while (!fileNoeuds.isEmpty()) {
			Arbre noeudCourrant = fileNoeuds.pop();

			// affiche Noeud Courrant
			sb.append(noeudCourrant.toDot()).append("\n");

			// Ajoute Noeuds Fils à la file (si noued interne
			if (noeudCourrant instanceof NoeudInterne) {
				fileNoeuds.addLast(((NoeudInterne) noeudCourrant).filsGauche);
				fileNoeuds.addLast(((NoeudInterne) noeudCourrant).filsDroit);
			}

		}

		// Fin
		sb.append("}");

		return sb.toString();
	}

	public String hashToCsv() {
		StringBuffer sb = new StringBuffer("Code courrant: \n");

		for (Feuille f : index.values()) {
			sb.append(f.lettre).append(":").append(f.code).append(":")
					.append(f.poids).append("\n");
		}

		return sb.toString();
	}

}
