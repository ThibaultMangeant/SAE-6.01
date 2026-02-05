package CENOFM.metier;

import java.io.*;
import java.util.*;

public class ConversionVrpDat {
/* 
	public static void main(String[] args) {
		String inputPath = "../../src/tai75a.txt";
		String outputPath = "../../CPLEX/tai75a_test.dat";

		try {
			convertir(inputPath, outputPath);
			System.out.println("Conversion terminée ! Le fichier " + outputPath + " est prêt.");
		} catch (IOException e) {
			System.err.println("Erreur : " + e.getMessage());
		}
	}
*/
	public void convertir(String txt, String outputPath) throws IOException {
		final int NB_VEHICULE = 4;

		Scanner sc = new Scanner(txt);
		sc.useLocale(Locale.US);

		int nbClients = sc.nextInt();
		double bestSolution = sc.nextDouble();   // bestSolution il est jamais utiliser ?
		int qMax = sc.nextInt();

		double depotX = sc.nextDouble();
		double depotY = sc.nextDouble();

		Noeud[] noeuds = new Noeud[nbClients + 1];

		noeuds[0] = new Noeud(0, depotX, depotY, 0);

		for (int i = 1; i <= nbClients; i++) {
			int id = sc.nextInt();
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			int demande = sc.nextInt();
			noeuds[i] = new Noeud(id, x, y, demande);
		}

		// --- Génération du fichier .dat ---
		PrintWriter writer = new PrintWriter(new FileWriter(outputPath));

		writer.println("/*********************************************");
		writer.println(" * OPL .dat généré via classe Noeud");
		writer.println(" *********************************************/");
		writer.println();

		writer.println("nbClientDep = " + noeuds.length + ";");
		writer.println("Qmax = " + qMax + ";");
		writer.println("nbVehicules = " + NB_VEHICULE + ";");
		writer.println();

		writer.print("Demande = [");
		for (int i = 0; i < noeuds.length; i++) {
			writer.print(noeuds[i].demande + (i == noeuds.length - 1 ? "" : ", "));
		}
		writer.println("];");
		writer.println();

		writer.println("Distance = [");
		for (int i = 0; i < noeuds.length; i++) {
			writer.print(" [");
			for (int j = 0; j < noeuds.length; j++) {
				double dist = noeuds[i].distance(noeuds[j]);

				writer.printf(Locale.US, "%.2f", dist);
				if (j < noeuds.length - 1)
					writer.print(" ");
			}
			writer.println(i == noeuds.length - 1 ? "]" : "],");
		}
		writer.println("];");

		writer.close();
		sc.close();
	}
}