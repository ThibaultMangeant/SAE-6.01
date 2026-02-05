package CENOFM;

import java.io.IOException;

import CENOFM.IHM.FrameMain;
import CENOFM.metier.ConversionVrpDat;

public class Controleur {
	private FrameMain ihm;
	private ConversionVrpDat metier;

	public Controleur() throws Exception
	{
		this.ihm = new FrameMain(this);
		this.metier = new ConversionVrpDat();
	}

	public static void main(String[] args) throws Exception
	{
		new Controleur();
	}

	public void convertir(String txt, String outputPath)
	{
		try {
			this.metier.convertir(txt, outputPath);
		} catch (IOException e) {
			System.err.println("Erreur : " + e.getMessage());
		}
	}
}
