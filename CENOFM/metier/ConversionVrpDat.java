package CENOFM.metier;

import java.io.*;
import java.util.*;

public class ConversionVrpDat {

	public static void convertir(String txt, int nbVehicules, String outputPath) throws IOException {

		LectureVrp lecteur = new LectureVrp();
		DonneesVrp donnees = lecteur.charger(txt, nbVehicules);
		Noeud[] noeuds = donnees.getTableauNoeudsComplet();

		PrintWriter writer = new PrintWriter(new FileWriter(outputPath));

		writer.println("/*********************************************");
		writer.println(" * OPL .dat généré via classe Noeud");
		writer.println(" *********************************************/");
		writer.println();

		writer.println("nbClientDep = " + noeuds.length + ";");
		writer.println("Qmax = " + donnees.qMax + ";");
		writer.println("nbVehicules = " + donnees.nbVehicules + ";");
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