package CENOFM.metier;

public class RecuitTest {
	public static void main(String[] args) {
		final int NB_VEHICULE = 4;

		try {
			LectureVrp lecteur = new LectureVrp();
			DonneesVrp donnees = lecteur.charger("src/cantines.txt", NB_VEHICULE);

			RecuitSimuleCVRP rs = new RecuitSimuleCVRP(donnees.clients, donnees.depot, donnees.qMax);
			rs.resoudre();

		} catch (Exception e) {
			System.err.println("Erreur lors du test de recuit : " + e.getMessage());
			e.printStackTrace();
		}
	}
}