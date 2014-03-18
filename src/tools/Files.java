package tools;

import java.io.File;
import java.io.IOException;

/**
 * Tools méthodes.
 * 
 * @author adriean
 * 
 */
public class Files {

	/**
	 * Calcul ratio compression
	 * 
	 * @param fn1
	 *            fichier original
	 * @param fn2
	 *            fichier compression
	 * @return taille fichier 1 rapporté au fichier 2
	 */
	public static float compareFileSize(String fn1, String fn2) {

		File f1 = new java.io.File(fn1);
		float l1 = f1.length();

		File f2 = new java.io.File(fn2);
		float l2 = f2.length();

		return l2 / l1;

	}

	/**
	 * Teste existance fichier
	 * 
	 * @param filename
	 *            nom du fichier
	 * @throws IOException
	 *             lancé si fichier n'existe pas
	 */
	public static void checkFileExists(String filename) throws IOException {
		File f1 = new File(filename);
		if (!f1.exists())
			throw new IOException("File " + filename + " doesn't Exists");
	}

}
