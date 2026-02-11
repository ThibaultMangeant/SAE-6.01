package CENOFM;

import java.io.IOException;

import CENOFM.IHM.FrameMain;
import CENOFM.IHM.FrameGraphique;
import CENOFM.metier.LectureVrp;
import CENOFM.metier.ConversionVrpDat;
import CENOFM.metier.RecuitSimuleCVRP;
import CENOFM.metier.ResultatRecuit;
import CENOFM.metier.Solution;
import CENOFM.metier.DonneesVrp;

public class Controleur
{
	private FrameMain ihm;
	private LectureVrp lect;
	private DonneesVrp donnee;

	public Controleur() throws Exception
	{
		this.ihm = new FrameMain(this);
		this.lect = new LectureVrp();
		this.donnee = new DonneesVrp();
	}

	public static void main(String[] args) throws Exception { new Controleur(); }

	public void extractionDonnee( String txt, int nbV )
	{
		try { this.donnee = this.lect.charger(txt, nbV); }
		catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); } 
	}

	public void convertir(String outputPath)
	{
		try { ConversionVrpDat.convertir(donnee, outputPath); } 
		catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); }
	}

	public String resoudre(double temperature, double temperatureMin, double alpha, int nbIttArret)
	{
		int interval = 50000;

		RecuitSimuleCVRP rs = new RecuitSimuleCVRP(this.donnee);
		ResultatRecuit r = rs.resoudre(temperature, temperatureMin, alpha, nbIttArret, interval);

		StringBuilder texte = new StringBuilder();

		int compteur = 0;

		for (Solution s : r.getSnapshots())
		{
			if (compteur == 0)
				texte.append("===== Solution Initiale =====\n");
			else
				texte.append("===== Solution à l'itération ").append(compteur).append(" =====\n");

			texte.append(rs.formatterSolution(s)).append("\n");

			compteur += interval;
		}

		texte.append("===== Meilleure Solution Finale =====\n");
		texte.append(rs.formatterSolution(r.getMeilleureSolution()));
		texte.append("\nTemps d'exécution : ").append(r.getTempsExecution()).append(" secondes\n");

		// ===== GRAPH =====
		new FrameGraphique(r.getTourneesFinales());

		return texte.toString();
	}

}
