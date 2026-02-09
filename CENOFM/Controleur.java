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
	private ConversionVrpDat conv;
	private RecuitSimuleCVRP recui;
	private DonneesVrp donnee;

	public Controleur() throws Exception
	{
		this.ihm = new FrameMain(this);
		this.lect = new LectureVrp();
		// this.conv = new ConversionVrpDat();
		// this.recui = new RecuitSimuleCVRP();
		this.donnee = new DonneesVrp();
	}

	public static void main(String[] args) throws Exception { new Controleur(); }

	public void extractionDonnee( String txt, int nbV )
	{
		try { 
			System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
			donnee = this.lect.charger(txt, nbV); 
			//System.out.println(donnee);
			System.out.println("ssssssssssssssssssssssss");
		} 
		catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); }  
	}

	public void convertir(String outputPath, int nbVehicules)
	{
		System.out.println(donnee);
		
		try { ConversionVrpDat.convertir(donnee, nbVehicules, outputPath); } 
		catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); }
	}

	public void resoudre(  ) { 
		/* donnee.resoudre(); */ System.out.println("nnnnnnnnnnnnnnnnnn"); }
}
