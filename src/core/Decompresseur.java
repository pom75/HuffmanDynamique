package core;

import huffmanTree.AHA;
import huffmanTree.AHAD;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class Decompresseur {
	private PrintWriter outS; // BufferedOutputStream outS;
	private BitInputStream inS;
	private AHAD arbreDecodage = null;

	public Decompresseur(String nomI, String nomO) throws FileNotFoundException {
		this(new File(nomI), new File(nomO));
	}
	public Decompresseur(File nomI, File nomO) throws FileNotFoundException {
		// ouverture du flux de lecture du fichier a decompresser
		this.inS = new BitInputStream(new BufferedInputStream(
				new FileInputStream(nomI)));

		// ouverture du flux d'écriture du ficher decompresser
		this.outS = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(nomO), Charset
						.forName("UTF-8").newEncoder()));
		this.arbreDecodage = new AHAD();
	}

	
	
	public void decompression() throws IOException {

		// On parcourt tous les characteres du text a comrpesser
		int bit = this.inS.readBit();
		while (bit != -1) {

			// §10Stats.printCharIOD("Lecteur du bit  : "+bit);

			// On decode les bits et on ecrit le caractere decodé
			Character a = arbreDecodage.decode(bit);

			if (a != null) {
				outS.append(a);
				// §10Stats.printCharIOD("Ecriture du caractere  : "+a);
			}

			// Lecture nouveau
			bit = this.inS.readBit();

		}
		outS.flush();

		this.outS.close(); // fermeture du flux
		this.inS.close(); // fermeture du flux
	}

	public AHA getArbreDecodage() {
		return arbreDecodage;
	}
	
	public String getEncodageTable(){
		return arbreDecodage.hashToCsv();
	}
}