package CENOFM.metier;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class LectureVrp {

	public DonneesVrp charger(String cheminFichier, int nbVehicules) throws IOException {
		
		DonneesVrp donnees = new DonneesVrp(); 
		donnees.setNbVehicules(nbVehicules);
		Scanner sc = new Scanner(cheminFichier);
		sc.useLocale(Locale.US);

		if (!sc.hasNext())
		{
			sc.close();
			throw new IOException("Le fichier est vide");
		}

		donnees.setNbClients(sc.nextInt());
		donnees.setBestSolution(sc.nextDouble());
		donnees.setqMax(sc.nextInt());

		double depotX = sc.nextDouble();
		double depotY = sc.nextDouble();
		donnees.setDepot(new Noeud(0, depotX, depotY, 0));
		for (int i = 0; i < donnees.getNbClients(); i++)
		{
			int id = sc.nextInt();
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			int demande = sc.nextInt();
			donnees.getClients().add(new Noeud(id, x, y, demande));
		}

		sc.close();
		return donnees;
	}
}