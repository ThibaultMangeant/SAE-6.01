package CENOFM.metier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class LectureVrp {

	public DonneesVrp charger(String cheminFichier, int nbVehicules) throws IOException {
		
		DonneesVrp donnees = new DonneesVrp(); 
		donnees.nbVehicules = nbVehicules;
		Scanner sc = new Scanner(cheminFichier);
		sc.useLocale(Locale.US);

		if (!sc.hasNext())
		{
			sc.close();
			throw new IOException("Le fichier est vide");
		}

		donnees.nbClients = sc.nextInt();
		donnees.bestSolution = sc.nextDouble();
		donnees.qMax = sc.nextInt();

		double depotX = sc.nextDouble();
		double depotY = sc.nextDouble();
		donnees.depot = new Noeud(0, depotX, depotY, 0);

		for (int i = 0; i < donnees.nbClients; i++)
		{
			int id = sc.nextInt();
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			int demande = sc.nextInt();
			donnees.clients.add(new Noeud(id, x, y, demande));
		}

		sc.close();
		return donnees;
	}
}