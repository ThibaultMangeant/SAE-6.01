package CENOFM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CENOFM.IHM.FrameMain;
import CENOFM.IHM.FrameGraphique;
import CENOFM.metier.LectureVrp;
import CENOFM.metier.Noeud;
import CENOFM.metier.ConversionVrpDat;
import CENOFM.metier.RecuitSimuleCVRP;
import CENOFM.metier.DonneesVrp;

public class Controleur {
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
		try
		{
			this.donnee = this.lect.charger(txt, nbV);

			List<Noeud> clientsv1 = new ArrayList<Noeud>();
			clientsv1.add(this.donnee.getDepot());
			clientsv1.add(new Noeud(2, 0, 5, 0));
			clientsv1.add(new Noeud(4, 0, 2, 0));
			clientsv1.add(new Noeud(3, 3, 5, 0));
			clientsv1.add(new Noeud(10, 3, 2, 0));

			List<Noeud> clientsv2 = new ArrayList<Noeud>();
			clientsv2.add(this.donnee.getDepot());
			clientsv2.add(new Noeud(5, 0, 0, 0));
			clientsv2.add(new Noeud(6, 0, 0, 0));
			clientsv2.add(new Noeud(8, 0, 0, 0));

			List<Noeud> clientsv3 = new ArrayList<Noeud>();
			clientsv3.add(this.donnee.getDepot());
			clientsv3.add(new Noeud(1, 0, 0, 0));
			clientsv3.add(new Noeud(7, 0, 0, 0));
			clientsv3.add(new Noeud(9, 0, 0, 0));

			List<List<Noeud>> solution = new ArrayList<>();
			solution.add(clientsv1);
			solution.add(clientsv2);
			solution.add(clientsv3);

			new FrameGraphique(solution);
		}
		catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); } 
	}

	public void convertir(String outputPath)
	{
		try { ConversionVrpDat.convertir(donnee, outputPath); } 
		catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); }
	}

	public String resoudre( double temperature, double temperatureMin, double alpha ) { 
		RecuitSimuleCVRP rs = new RecuitSimuleCVRP(this.donnee.getClients(), this.donnee.getDepot(), this.donnee.getqMax());
		return rs.resoudre( temperature, temperatureMin, alpha );
	}
}
