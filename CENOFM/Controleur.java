package CENOFM;

import java.io.IOException;

import CENOFM.IHM.FrameMain;
import CENOFM.metier.LectureVrp;
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
		try { this.donnee = this.lect.charger(txt, nbV);  } 
		catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); } 
	}

	public void convertir(String outputPath)
	{
		try { ConversionVrpDat.convertir(donnee, outputPath); } 
		catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); }
	}

	public String resoudre( double temperature, double temperatureMin, double alpha ) { 
		RecuitSimuleCVRP rs = new RecuitSimuleCVRP(this.donnee.clients, this.donnee.depot, this.donnee.qMax);
		return rs.resoudre( temperature, temperatureMin, alpha );
	}
}
