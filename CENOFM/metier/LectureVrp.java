package CENOFM.metier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class LectureVrp {

	public static class DonneesVrp {
		public int nbClients;
		public int nbVehicules;
		public double bestSolution;
		public int qMax;
		public Noeud depot;
		public List<Noeud> clients = new ArrayList<>();

		public Noeud[] getTableauNoeudsComplet() {
			Noeud[] tab = new Noeud[nbClients + 1];
			tab[0] = depot;
			for (int i = 0; i < clients.size(); i++) {
				tab[i + 1] = clients.get(i);
			}
			return tab;
		}

		public int getNbClients() {
			return nbClients;
		}

		public int getNbVehicules() {
			return nbVehicules;
		}

		public double getBestSolution() {
			return bestSolution;
		}

		public int getqMax() {
			return qMax;
		}

		public Noeud getDepot() {
			return depot;
		}

		public List<Noeud> getClients() {
			return clients;
		}

	}

	public DonneesVrp charger(String cheminFichier, int nbVehicules) throws IOException {
		DonneesVrp donnees = new DonneesVrp();
		donnees.nbVehicules = nbVehicules;
		Scanner sc = new Scanner(new File(cheminFichier));
		sc.useLocale(Locale.US);

		if (!sc.hasNext()) {
			sc.close();
			throw new IOException("Le fichier est vide");
		}

		donnees.nbClients = sc.nextInt();
		donnees.bestSolution = sc.nextDouble();
		donnees.qMax = sc.nextInt();

		double depotX = sc.nextDouble();
		double depotY = sc.nextDouble();
		donnees.depot = new Noeud(0, depotX, depotY, 0);

		for (int i = 0; i < donnees.nbClients; i++) {
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