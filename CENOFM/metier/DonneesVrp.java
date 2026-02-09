package CENOFM.metier;

import java.util.ArrayList;
import java.util.List;


public class DonneesVrp {
	public int nbClients;
	public int nbVehicules;
	public double bestSolution;
	public int qMax;
	public Noeud depot;
	public List<Noeud> clients = new ArrayList<>();

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

	public DonneesVrp getDonneesVrp() {
		return this;
	}

	public Noeud[] getTableauNoeudsComplet() {
		Noeud[] tab = new Noeud[nbClients + 1];
		tab[0] = depot;
		for (int i = 0; i < clients.size(); i++) {
			tab[i + 1] = clients.get(i);
		}
		return tab;
	}

	public Noeud getNoeud(int i) {
		if (i == 0)
			return depot;
		else
			return clients.get(i - 1);
	}

	@Override
	public String toString() {
		return "DonneesVrp [nbClients=" + nbClients + ", nbVehicules=" + nbVehicules + ", bestSolution=" + bestSolution
				+ ", qMax=" + qMax + ", depot=" + depot + ", clients=" + clients + "]";
	}

}