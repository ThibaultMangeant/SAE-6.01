package api.IHM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

//import api.metier.VrpToDatConverter;

public class FrameMain extends JFrame 
{
	//private VrpToDatConverter vrpDatConv;
	public static Color COULEUR = Color.decode("#abcce7");

	//private PanelImport panels;
	private PanelImport panels;

	private int ind;

	public FrameMain()
	{
		//this.vrpDatConv = new VrpToDatConverter();

		this.ind = 0;
		this.panels = new PanelImport(this);
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

	public static void main(String[] args)
	{
		new FrameMain();
	}
}