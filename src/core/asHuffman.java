package core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import tools.Dot;
import tools.Files;
import tools.Stats;

/**
 * Classe principale du Compresseur asHuffman
 * 
 * @author adriean
 * 
 */
public class asHuffman {

	/**
	 * Fonction Main, point d'entrée du programme
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		final String USAGE = "Usage:\n" + ""
				+ "  c[ompression] fichier_à_compresser fichier_compressé \n"
				+ "  d[ecompression] fichier_compressé fichier_decompressé\n"
				+ "  t[est] fichier_à_compresser fichier_decompressé \n"
				+ "  chaine chainacompresser fichier_compressé \n"
				+ "  chainetest chainacompresser fichier_compressé \n"
				+ "  [b]enchmark fichierstats fichiers* \n";

		try {

			if (args.length == 0) {
				System.err.println(USAGE);
				System.exit(1);

			} else {
				// BONUX: argument checking!!
								

				// Lance programme dans le mode spécifié par le premier argument
				switch (args[0].toLowerCase()) {

				case "c":
				case "compression":
					System.out
							.println("Lancement AsHuffman en mode compression");
					compressMode(args[1], args[2]);
					break;

				case "d":
				case "decompression":
					System.out
							.println("Lancement AsHuffman en mode decompression");
					decompressMode(args[1], args[2]);
					break;

				case "test":
				case "t":
					System.out.println("Lancement AsHuffman en mode test");
					testMode(args[1], args[2]);
					break;

				case "chaine":
					System.out.println("Lancement AsHuffman en mode chaine");
					compressChaineMode(args[1], args[2], false);
					break;

				case "chainetest":
					System.out
							.println("Lancement AsHuffman en mode chaine test (création arbre pour chaque noued)");
					compressChaineMode(args[1], args[2], true);
					break;

				case "benchmark":
				case "b":
					int nfiles = args.length - 2;
					String[] files = new String[nfiles];
					for (int i = 0; i < nfiles; i++)
						files[i] = args[i + 2];

					System.out
							.println("Lancement en mode benchmark sur les fichiers spécifiés");
					benchmarkMode(args[1], files);
					break;

				case "help":
				case "h":
					System.out.println(USAGE);
					break;
					
				default:
					System.err.println((args[0].equals(egmp)) ? eegg : USAGE);
					System.exit(1);
				}

			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Le nombre de paramètre est incorrect\n"
					+ " Arret du programme\n" + USAGE);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Le fichier d'input spécifié n'existe pas\n"
					+ " Arret du programme");
			System.err.println(e.getMessage());
			// TODO: HERE IOException pourrait venir d'ailleur: tout problème de
			// fichier arrivant en cours de route
			System.exit(1);
		}
	}

	/**
	 * Mode de compression Standard
	 * 
	 * @param inputFile
	 *            fichier en entrée
	 * @param compressedFile
	 *            fichier compressé
	 * @throws IOException
	 */
	private static void compressMode(String inputFile, String compressedFile)
			throws IOException {

		Files.checkFileExists(inputFile);

		System.out.println(">> Compression du fichier " + inputFile + " vers "
				+ compressedFile);

		Stats.lanceChrono();
		Compresseur c = new Compresseur(new File(inputFile), new File(
				compressedFile));
		c.compression();

		Stats.stopChrono();
		Stats.printChrono("compression");
		// §DISStats.printDebug("Fin Compression");
		// §DISStats.printCompressionWriteStats();
		// §DISStats.printStats();

		float tauxCompression = Files
				.compareFileSize(inputFile, compressedFile);
		System.out.println("Taux de compression: "
				+ (100 - (tauxCompression * 100)) + "%");

		// Dot Regenation
		// BONUX: option!!
		Dot.generateArbreGraph(c.getArbreEncodage());
	}

	/**
	 * Compression à partir d'une chaine. avec évenutuel création de graphe
	 * lettre à lettre)
	 * 
	 * @param chaine
	 *            chaine à compresser
	 * @param compressedFile
	 *            fichier compressé
	 * @param testMode
	 *            mode test(création graphe)
	 * @throws IOException
	 */
	private static void compressChaineMode(final String chaine,
			final String compressedFile, boolean testMode) throws IOException {

		Stats.lanceChrono();
		Compresseur c = new Compresseur(chaine, new File(compressedFile));
		if (testMode)
			c.compressionTest();
		else
			c.compression();

		Stats.stopChrono();
		Stats.printChrono("compression");
		if (testMode)
			System.out.println(c.getEncodageTable());

		// Dot Regenation
		Dot.generateArbreGraph(c.getArbreEncodage());

	}

	/**
	 * Mode de décompression standard
	 * 
	 * @param compressedFile
	 *            fichier à décompresser
	 * @param outPutFile
	 *            Fichier décompressé
	 * @throws IOException
	 */
	private static void decompressMode(String compressedFile, String outPutFile)
			throws IOException {

		Files.checkFileExists(compressedFile);
		System.out.println(">> DeCompression du fichier " + compressedFile
				+ " vers " + outPutFile);

		// §DISStats.printDebug("Début Decompression");
		Stats.lanceChrono();
		Decompresseur d = new Decompresseur(compressedFile, outPutFile);
		d.decompression();

		Stats.stopChrono();
		Stats.printChrono("decompression");
		// §DISStats.printStats();

		// Dot Regenation
		Dot.generateArbreGraph(d.getArbreDecodage());

	}

	/**
	 * Mode Test précisant fichier intermédiaire
	 * 
	 * @param inputFile
	 *            fichier à compresser
	 * @param compressedFile
	 *            fichier compressé
	 * @param outPutFile
	 *            fichier décompressé
	 * @throws IOException
	 */
	private static void testMode(String inputFile, String compressedFile,
			String outPutFile) throws IOException {
		Files.checkFileExists(inputFile);
		compressMode(inputFile, compressedFile);
		decompressMode(compressedFile, outPutFile);
	}

	/**
	 * Mode Test (sans préciser fichier de compression)
	 * 
	 * @param inputFile
	 *            fichier à compresser
	 * @param outPutFile
	 *            fichier décompressé
	 * @throws IOException
	 */
	private static void testMode(String inputFile, String outPutFile)
			throws IOException {
		testMode(inputFile, "/tmp/compress", outPutFile);
		// marchera que sur unix.
	}

	/**
	 * Mode pour calculer les performance sur un ensemble de fichier
	 * 
	 * @param statfile
	 *            le fichier dans lequel stocker les stats
	 * @param files
	 *            le nom des fichier à tester
	 * @throws IOException
	 */
	private static void benchmarkMode(String statfile, String[] files)
			throws IOException {
		// Teste existance fichier
		for (String file : files)
			Files.checkFileExists(file);

		PrintWriter pw = new PrintWriter(new File(statfile));
		// BONUX: entete, avec performances normalement
		pw.println("|filename|size (o)|tempsCompression (s)|tauxCompression (%)|tempsdecompression(s)|");

		for (int i = 0; i < files.length; i++) {
			File f = new File(files[i]);
			File tmpC = new File("/tmp/COM");
			File tmpD = new File("/tmp/DECOM");

			pw.append("|").append(files[i]).append("|")
					.append(String.valueOf(f.length())).append("|");

			// compression
			Stats.lanceChrono();
			Compresseur c = new Compresseur(f, tmpC);
			c.compression();
			Stats.stopChrono();
			pw.print(Stats.getChrono());
			pw.print("|");
			pw.print(Files.compareFileSize(files[i], "/tmp/COM"));
			pw.print("|");

			// decompression
			Stats.lanceChrono();
			Decompresseur d = new Decompresseur(tmpC, tmpD);
			d.decompression();
			Stats.stopChrono();
			pw.print(Stats.getChrono());
			pw.println("|");
			System.out.println("Performances calculées pour le fichier"
					+ files[i]);
		}
		pw.close();
		System.out.println("Fin du calcul des statistiques sur les "
				+ files.length + " fichiers");

	}

	private static String egmp = "troll",
			eegg = "Attention, coeur application conçu par un Clown, bozo";

}
