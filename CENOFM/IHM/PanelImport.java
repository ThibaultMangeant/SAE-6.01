package CENOFM.IHM;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class PanelImport extends JPanel implements ActionListener
{
	private FrameMain frame;
	private JTextArea txtVrp;
	private JScrollPane sp;

	private JButton btnImporter;
	private JButton btnConvertir;
	private JButton btnRecuit;

	public static Color CFondBtn = Color.decode("#9abddb");
	public static Color CTexte = Color.decode("#1c4587");
	public static Color CFond = Color.decode("#abcce7");
	private Font ft;

	private String dernierDossier = null;

	public PanelImport(FrameMain fm)
	{
		this.frame = fm;
		this.setLayout(new BorderLayout());
		this.setLocation(10, 10);
		this.setSize(800, 500);

		this.btnImporter = styliserBouton("Importer");
		this.btnConvertir = styliserBouton("Convertir en dat");
		this.btnRecuit = styliserBouton("Recuit simulé");

		this.txtVrp = new JTextArea("Importer un fichier correspondant a la structure demander.");

		Border border = BorderFactory.createLineBorder(CFond);
		border = BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.txtVrp.setWrapStyleWord(true);
		this.txtVrp.setBorder(border);
		this.txtVrp.setEditable(false);

		JLabel titreLbl = new JLabel("Convertisseur et recuit simulé");
		titreLbl.setFont(new Font("Montserrat", Font.BOLD, 24));

		titreLbl.setForeground(CFondBtn); // Couleur du texte
		this.setBackground(null); // Couleur du fond


		JPanel panelBtn = new JPanel(new GridLayout(1, 3, 0, 3));
		this.ft = new Font("Montserrat", Font.BOLD, 18);
		btnImporter.setFont(this.ft);
		btnConvertir.setFont(this.ft);
		btnRecuit.setFont(this.ft);
		panelBtn.add(this.btnImporter);
		panelBtn.add(this.btnConvertir);
		panelBtn.add(this.btnRecuit);
		this.add(panelBtn, BorderLayout.NORTH);

		this.sp = new JScrollPane();
		this.sp.setViewportView(this.txtVrp);

		this.add(this.sp, BorderLayout.CENTER);

		this.btnImporter.addActionListener(this);
		this.btnConvertir.addActionListener(this);
		this.btnRecuit.addActionListener(this);
	}

	public static JButton styliserBouton(String txt)
	{
		JButton btn = new JButton(txt);

		btn.setBorder(BorderFactory.createLineBorder(CFond.darker(), 2));
		btn.setBackground(CFondBtn);
		btn.setFocusable(false);
		btn.setForeground(CTexte);

		Dimension dim = new Dimension(100, 50);
		btn.setSize(dim);
		btn.setPreferredSize(dim);

		return btn;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnImporter)
		{
			String cheminFichier1 = this.selectionnerFichier("Choisissez un fichier txt");
			if (cheminFichier1 == null) return;
			if (cheminFichier1 != null)
			{
				try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier1)))
				{
					int nbV = NombreVehi();
					this.frame.extractionDonnee(this.txtVrp.getText(), nbV);

					this.txtVrp.read(br, null);
					this.txtVrp.setCaretPosition(0); // revenir en haut du texte

				} catch (IOException ex)
				{
					JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (e.getSource() == this.btnConvertir)
		{
			String cheminSortie = enregistrerNouvFichier("Enregistrer le fichier DAT", ".dat");
			if (cheminSortie != null) { this.frame.convertir(this.txtVrp.getText(), cheminSortie); }
		}
 
		if (e.getSource() == this.btnRecuit) { this.frame.resoudre(  ); }

	}
	
	private int NombreVehi()
	{
		JTextArea champ = new JTextArea();
		champ.setFont(new Font("Montserrat", Font.PLAIN, 14));

		Object[] options = { "Valider" }; // ✅ seul bouton disponible

		while (true)
		{
			int result = JOptionPane.showOptionDialog(this, champ, "Entrez le nombre de véhicules (entier positif)",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0] // bouton
			);

			try
			{
				int valeur = Integer.parseInt(champ.getText().trim());
				if (valeur > 0) { return valeur; /* nombre valide */ }
			} catch (NumberFormatException ignored) {  }

			// Valeur invalide → message et redemande
			JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre entier strictement positif.",
					"Valeur invalide", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String selectionnerFichier(String titre)
	{
		FileDialog dialogueFichier = new FileDialog((JFrame) null, titre, FileDialog.LOAD);

		// Réouvrir dans le dernier dossier utilisé
		if (dernierDossier != null) { dialogueFichier.setDirectory(dernierDossier); }
		dialogueFichier.setFile("*.txt");
		dialogueFichier.setVisible(true);

		String nomFichier = dialogueFichier.getFile();
		String dossier = dialogueFichier.getDirectory();

		if (nomFichier != null && dossier != null)
		{
			// Autoriser uniquement les .txt
			if (!nomFichier.toLowerCase().endsWith(".txt"))
			{
				JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fichier .txt", "Fichier invalide", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			// Mémorisation du dossier
			dernierDossier = dossier;
			return new File(dossier, nomFichier).getAbsolutePath();
		}

		return null;
	}

	private String enregistrerNouvFichier(String titre, String extension)
	{
		FileDialog dialogueFichier = new FileDialog((JFrame) null, titre, FileDialog.SAVE);

		// Ouvrir dans le dernier dossier utilisé
		if (dernierDossier != null) { dialogueFichier.setDirectory(dernierDossier); }

		// Suggestion d’extension (visuelle)
		dialogueFichier.setFile("*" + extension);
		dialogueFichier.setVisible(true);

		String nomFichier = dialogueFichier.getFile();
		String dossier = dialogueFichier.getDirectory();

		if (nomFichier != null && dossier != null)
		{
			// Ajouter l’extension si oubliée
			if (!nomFichier.toLowerCase().endsWith(extension)) { nomFichier += extension; }
			File fichier = new File(dossier, nomFichier);

			// Confirmation si le fichier existe
			if (fichier.exists())
			{
				int choix = JOptionPane.showConfirmDialog(this, "Le fichier existe déjà. Voulez-vous l’écraser ?",
						"Confirmation", JOptionPane.YES_NO_OPTION);

				if (choix != JOptionPane.YES_OPTION) { return null; }
			}
			// Mémoriser le dossier
			dernierDossier = dossier;
			// RETOUR DU CHEMIN COMPLET
			return fichier.getAbsolutePath();
		}
		return null;
	}

}