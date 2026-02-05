package CENOFM.IHM;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import CENOFM.Controleur;

public class FrameMain extends JFrame 
{
	private Controleur ct;
	//private VrpToDatConverter vrpDatConv;

	private PanelImport panels;

	public FrameMain(Controleur contr)
	{
		this.ct = contr;
		//this.vrpDatConv = new VrpToDatConverter();

		this.panels = new PanelImport(this);

		this.setLayout(new BorderLayout());
		//this.addPanelCorrespondant();
		this.setLocation(10, 10);
		this.setSize(1000, 800);

		this.setTitle("CENOFM");
		this.setVisible(true);

		this.add(this.panels);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.pack();
	}

	public String getTextDat()
	{
		return null;//this.vrpDatConv.convert();
	}

	public String getRes()
	{
		//return this.vrpDatConv.getRes();
		return null;
	}

	public void convertir(String txt, String outputPath) { this.ct.convertir(txt, outputPath); }

	public static void main(String[] args) { new FrameMain(null); }
}