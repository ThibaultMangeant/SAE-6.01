package CENOFM.metier;

import CENOFM.metier.LectureVrp.*;
import CENOFM.metier.Noeud;

import java.io.*;
import java.util.*;

public class ConversionVrpDat {

	public static void main(String[] args) {
		String inputPath = "src/cantines.txt";
		String outputPath = "CPLEX/cantines_test.dat";

		try {
			convertir(inputPath, outputPath);
			System.out.println("Conversion terminée ! Le fichier " + outputPath +
					" est prêt.");
		} catch (IOException e) {
			System.err.println("Erreur : " + e.getMessage());
		}
	}

	public static void convertir(String txt, String outputPath) throws IOException {
		final int NB_VEHICULE = 4;

		LectureVrp lecteur = new LectureVrp();
		LectureVrp.DonneesVrp donnees = lecteur.charger(txt);
		Noeud[] noeuds = donnees.getTableauNoeudsComplet();

		PrintWriter writer = new PrintWriter(new FileWriter(outputPath));

		writer.println("/*********************************************");
		writer.println(" * OPL .dat généré via classe Noeud");
		writer.println(" *********************************************/");
		writer.println();

		writer.println("nbClientDep = " + noeuds.length + ";");
		writer.println("Qmax = " + donnees.qMax + ";");
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
	}
}