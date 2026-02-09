package CENOFM.IHM;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import CENOFM.Controleur;

public class FrameMain extends JFrame 
{
	private Controleur ct;
	private PanelImport panels;

	public FrameMain(Controleur contr)
	{
		this.ct = contr;
		this.panels = new PanelImport(this);

		this.setLayout(new BorderLayout());
		this.setLocation(10, 10);
		this.setSize(1000, 800);

		this.setTitle("CENOFM");
		this.setVisible(true);

		this.add(this.panels);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void extractionDonnee( String txt, int nbV ) { this.ct.extractionDonnee( txt, nbV );}
	public void convertir( String outputPath) { this.ct.convertir( outputPath); }
	public void resoudre(  ) { this.ct.resoudre(  ); }

	public static void main(String[] args) { new FrameMain(null); }
}