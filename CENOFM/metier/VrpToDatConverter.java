package CENOFM.metier;

import java.io.*;
import java.util.*;

public class VrpToDatConverter {

	public static void main(String[] args) {

		String inputPath = "../src/tai75a.txt";
		String outputPath = "../CPLEX/tai75a_test.dat";

		try {
			convert(inputPath, outputPath);
			System.out.println("Conversion terminée ! Le fichier " + outputPath + " est prêt.");
		} catch (IOException e) {
			System.err.println("Erreur : " + e.getMessage());
		}
	}

	private static void convert(String inputPath, String outputPath) throws IOException {

		final int NB_VEHICULE = 4; // Nombre de véhicules par défaut

		// Lecture du fichier d'entrée
		Scanner sc = new Scanner(new File(inputPath));
		sc.useLocale(Locale.US);

		int nbClients = sc.nextInt();
		double bestSolution = sc.nextDouble();

		int qMax = sc.nextInt();
		double depotX = sc.nextDouble();
		double depotY = sc.nextDouble();

		double[] x = new double[nbClients + 1];
		double[] y = new double[nbClients + 1];
		int[] demandes = new int[nbClients + 1];

		x[0] = depotX; // i du premier dépôt toujours = 0
		y[0] = depotY;
		demandes[0] = 0;

		for (int i = 1; i <= nbClients; i++) {
			int id = sc.nextInt(); // saut de l'ID
			x[i] = sc.nextDouble();
			y[i] = sc.nextDouble();
			demandes[i] = sc.nextInt();
		}

		// Génération du fichier .dat
		PrintWriter writer = new PrintWriter(new FileWriter(outputPath));

		writer.println("/*********************************************");
		writer.println(" * OPL Data generated from Text Format");
		writer.println(" *********************************************/");
		writer.println();

		writer.println("nbClientDep = " + (nbClients + 1) + ";");
		writer.println("Qmax = " + qMax + ";");

		writer.println("nbVehicules = "+ NB_VEHICULE + "; ");
		writer.println();

		writer.print("Demande = [");
		for (int i = 0; i < demandes.length; i++) {
			writer.print(demandes[i] + (i == demandes.length - 1 ? "" : ", "));
		}
		writer.println("];");
		writer.println();

		writer.println("Distance = [");
		for (int i = 0; i <= nbClients; i++) {
			writer.print(" [");
			for (int j = 0; j <= nbClients; j++) {
				double dist = Math.sqrt(Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2));
				writer.printf(Locale.US, "%.2f", dist);
				if (j < nbClients)
					writer.print(" ");
			}
			writer.println(i == nbClients ? "]" : "],");
		}
		writer.println("];");

		writer.close();
		sc.close();
	}
}

// Développement d'API de Translation de Textes Exploitables par CPLEX ou DATTEC pour les intimes