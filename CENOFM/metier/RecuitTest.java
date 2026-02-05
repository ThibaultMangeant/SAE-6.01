package CENOFM.metier;

public class RecuitTest {
	public static void main(String[] args) {
		try {
			LectureVrp lecteur = new LectureVrp();
			LectureVrp.DonneesVrp donnees = lecteur.charger("src/cantines.txt");

			RecuitSimuleCVRP rs = new RecuitSimuleCVRP(donnees.clients, donnees.depot, donnees.qMax);
			rs.resoudre();

		} catch (Exception e) {
			System.err.println("Erreur lors du test : " + e.getMessage());
			e.printStackTrace();
		}
	}
}