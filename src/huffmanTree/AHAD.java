package huffmanTree;

import java.io.IOException;

/**
 * 
 * @author Steph
 */
public class AHAD extends AHA {
	Arbre currentP;
	int nbBitNewChar;
	String buffChar;

	public AHAD() {
		super();
		currentP = racineAha;
		nbBitNewChar = 8; // On le met a 7 par défaut car au début c'est forcément
		buffChar = ""; // WTF: utilise vrai buff non?
	}

	/**
	 * Est ce que l'on doit encore lire des caractère
	 */

	//retourn le charactere decodé 
	public Character decode(int bit)// , BufferedOutputStream ecriture)
			throws IOException {

		/*
		 * Si on n'a pas de nouveau caractere 
		 */
		if (nbBitNewChar == 0) {
			//On navigue dans l'arbre
			Feuille c = this.naviguerAHA(bit);
			//Si on est pas encore tomber sur une feuille
			if (c == null) {
				// doit lire charactère de plus
				return null;
			//Si on est sur une feuille qui est le char special , les 8 prochain
			//bits seront un nouveau caractere
			} else if (this.isSpecial(c)) {
				nbBitNewChar = 8;
				// §DISStats.printCharIOD("Caractere spécial !");
			} else {
				// Sinon on est tombé sur une feuille qui est une lettre donc 
				//on l'ecrit et on l'ajoute a la strcuture

				// ecriture.write(c.lettre); // CHECK encodage...
				// incrémente feuille
				this.modificationAHA(c.lettre);

				return c.lettre;
			}

		} else {
			//On recupere les 8 prochain bit pour decoder le nouveau char 
			nbBitNewChar--;
			buffChar += bit;
			//Si on a recupere les 8 prochains bit on decode notre char
			if (nbBitNewChar == 0) {
				char l = (char) Integer.parseInt(buffChar, 2);
				// System.out.println("J'écris 2: " + l);
				// T ecriture.write(l);
				//On l'ajoute a la structure
				this.modificationAHA(l); // ajoute feuille
				this.resetPosition(); // Hack. (ne sert que premiere fois)
				buffChar = "";
				//On le retourne pour lecriture
				return l;
			}
		}
		return null;
	}

	private void resetPosition() {
		this.currentP = this.racineAha;
	}

	/**
	 * Renvoi le char si feuille Atteinte
	 * 
	 * @param bit
	 * @return
	 */
	private Feuille naviguerAHA(int bit) {
		// Poisition courante jamais dans une feuille
		// System.out.println(currentP); //D
		if (bit == 0) {

			currentP = ((NoeudInterne) currentP).filsGauche;
		} else {
			currentP = ((NoeudInterne) currentP).filsDroit;
		}

		if (currentP instanceof Feuille) {
			Feuille tmp = (Feuille) currentP;
			currentP = racineAha; // revient au début arbre
			return tmp;

		} else {
			return null;
		}

	}

	private boolean isSpecial(Feuille f) {
		return f == this.feuilleSpeciale;
	}
}
