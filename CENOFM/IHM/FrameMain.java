package CENOFM.IHM;

import java.awt.BorderLayout;
import javax.swing.JFrame;

//import api.metier.VrpToDatConverter;

public class FrameMain extends JFrame 
{
	//private VrpToDatConverter vrpDatConv;

	private PanelImport panels;

	public FrameMain()
	{
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

	public static void main(String[] args) { new FrameMain(); }
}