package CENOFM.metier;

import java.io.IOException;

public class ConversionTest {
	public static void main(String[] args) {
		final int NB_VEHICULE = 4;

		String inputPath = "src/cantines.txt";
		String outputPath = "CPLEX/cantines_test.dat";
/* 
		try {
			DonneesVrp donnees = new LectureVrp().charger(inputPath, NB_VEHICULE);
			ConversionVrpDat.convertir(donnees, outputPath);
			System.out.println("Conversion terminée ! Le fichier " + outputPath +
					" est prêt.");
		} catch (IOException e) {
			System.err.println("Erreur lors du test de la conversion : " + e.getMessage());
		}*/
	}
}
