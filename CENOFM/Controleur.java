package CENOFM;

import java.io.IOException;

import CENOFM.IHM.FrameMain;
import CENOFM.metier.LectureTxt;
import CENOFM.metier.ConversionVrpDat;
import CENOFM.metier.RecuitSimuleCVRP;

public class Controleur {
	private FrameMain ihm;
	private LectureTxt lect;
	private ConversionVrpDat conv;
	private RecuitSimuleCVRP recui;

	public Controleur() throws Exception
	{
		this.ihm = new FrameMain(this);
		this.lect = new LectureTxt();
	}

	public static void main(String[] args) throws Exception
	{
		new Controleur();
	}

	public void extractionDonnee( String txt, int nbV )
	{
		System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
		/* 
		try
		{
			this.lect.lectureTxt(txt, nbV);

			this.conv = new ConversionVrpDat();
			//this.recui = new RecuitSimuleCVRP();
		} catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); }*/
	}

	public void convertir(String txt, String outputPath)
	{
		try {
			this.conv.convertir(txt, outputPath);
		} catch (IOException e) { System.err.println("Erreur : " + e.getMessage()); }
	}
}
