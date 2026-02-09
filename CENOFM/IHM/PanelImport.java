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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class PanelImport extends JPanel implements ActionListener {
	private FrameMain frame;
	private JPanel panelBtn;
	private JPanel panelCentre;
	private JTextArea txtVrp;
	private JScrollPane sp;
	private JTextArea txtResoudre;
	private JScrollPane spResoudre;

	private JButton btnImporter;
	private JButton btnConvertir;
	private JButton btnRecuit;

	public static Color CFondBtn = Color.decode("#9abddb");
	public static Color CTexte = Color.decode("#1c4587");
	public static Color CFond = Color.decode("#abcce7");
	private Font ft;

	private String dernierDossier = null;
	private int nbVehicules;
	private double tempInit;
	private double seuilArret;
	private double alpha;

	public PanelImport(FrameMain fm) {
		this.frame = fm;
		this.setLayout(new BorderLayout());
		this.setLocation(10, 10);
		this.setSize(800, 500);

		this.btnImporter = styliserBouton("Importer");
		this.btnConvertir = styliserBouton("Convertir en dat");
		this.btnRecuit = styliserBouton("Recuit simulé");

		this.txtVrp = new JTextArea("Importer un fichier correspondant a la structure demander.");
		this.txtResoudre = new JTextArea("  ");

		Border border = BorderFactory.createLineBorder(CFond);
		border = BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.txtVrp.setWrapStyleWord(true);
		this.txtVrp.setBorder(border);
		this.txtVrp.setEditable(false);
		this.txtResoudre.setWrapStyleWord(true);
		this.txtResoudre.setBorder(border);
		this.txtResoudre.setEditable(false);

		this.panelBtn = new JPanel(new GridLayout(1, 3, 0, 3));
		this.ft = new Font("Montserrat", Font.BOLD, 18);
		this.btnImporter.setFont(this.ft);
		this.btnConvertir.setFont(this.ft);
		this.btnRecuit.setFont(this.ft);
		this.panelBtn.add(this.btnImporter);
		this.panelBtn.add(this.btnConvertir);
		this.panelBtn.add(this.btnRecuit);
		this.add(this.panelBtn, BorderLayout.NORTH);

		this.panelCentre = new JPanel(new GridLayout(1, 2, 0, 3));
		this.sp = new JScrollPane();
		this.sp.setViewportView(this.txtVrp);
		this.spResoudre = new JScrollPane();
		this.txtResoudre = new JTextArea("Ja passe !");
		this.spResoudre.setViewportView(this.txtResoudre);

		this.panelCentre.add(this.sp);
		this.add(this.panelCentre, BorderLayout.CENTER);

		this.btnImporter.addActionListener(this);
		this.btnRecuit.setEnabled(false);
		this.btnConvertir.setEnabled(false);
	}

	public static JButton styliserBouton(String txt) {
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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnImporter) {
			String cheminFichier1 = this.selectionnerFichier("Choisissez un fichier txt");
			if (cheminFichier1 == null)
				return;
			if (cheminFichier1 != null) {
				try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier1))) {
					this.txtVrp.read(br, null);
					this.txtVrp.setCaretPosition(0); // revenir en haut du texte
					this.frame.extractionDonnee(this.txtVrp.getText(), NombreVehi());
					if (this.panelCentre.isAncestorOf(this.spResoudre))
					{
						this.panelCentre.remove(this.spResoudre);
						this.txtResoudre.setText("");
						this.panelCentre.revalidate();
						this.panelCentre.repaint();
					}
					this.btnConvertir.addActionListener(this);
					this.btnRecuit.addActionListener(this);
					this.btnRecuit.setEnabled(true);
					this.btnConvertir.setEnabled(true);
				} catch (IOException ex)
				{
					JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (e.getSource() == this.btnConvertir) {
			String cheminSortie = enregistrerNouvFichier("Enregistrer le fichier DAT", ".dat");
			if (cheminSortie != null) { this.frame.convertir(cheminSortie); }
		}

		if (e.getSource() == this.btnRecuit)
		{
			// Si annuler → STOP
			if (!paramRecuit()) { return; }

			this.txtResoudre.setText("" + this.frame.resoudre( this.tempInit, this.seuilArret, this.alpha ));
			this.txtResoudre.setCaretPosition(0);

			if (!this.panelCentre.isAncestorOf(this.spResoudre)) { this.panelCentre.add(this.spResoudre); }

			this.panelCentre.revalidate();
			this.panelCentre.repaint();
		}

	}

	private int NombreVehi()
	{
		JTextField champ = new JTextField(10);
		champ.setFont(new Font("Montserrat", Font.PLAIN, 14));

		Object[] contenu = { "Entrez le nombre de véhicules (entier positif) :", champ };

		while (true)
		{
			int result = JOptionPane.showConfirmDialog(
					this,
					contenu,
					"Nombre de véhicules",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE
			);

			if (result != JOptionPane.OK_OPTION) { return -1;  }

			try
			{
				this.nbVehicules = Integer.parseInt(champ.getText().trim());
				if (this.nbVehicules > 0) { return this.nbVehicules; }
			} catch (NumberFormatException ignored) { }

			JOptionPane.showMessageDialog(
					this,
					"Veuillez entrer un nombre entier strictement positif.",
					"Valeur invalide",
					JOptionPane.ERROR_MESSAGE
			);
			champ.setText("");
			champ.requestFocusInWindow();
		}
	}

	private String selectionnerFichier(String titre) {
		FileDialog dialogueFichier = new FileDialog((JFrame) null, titre, FileDialog.LOAD);

		// Réouvrir dans le dernier dossier utilisé
		if (dernierDossier != null) { dialogueFichier.setDirectory(dernierDossier); }
		dialogueFichier.setFile("*.txt");
		dialogueFichier.setVisible(true);

		String nomFichier = dialogueFichier.getFile();
		String dossier = dialogueFichier.getDirectory();

		if (nomFichier != null && dossier != null) {
			// Autoriser uniquement les .txt
			if (!nomFichier.toLowerCase().endsWith(".txt")) {
				JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fichier .txt", "Fichier invalide", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			// Mémorisation du dossier
			dernierDossier = dossier;
			return new File(dossier, nomFichier).getAbsolutePath();
		}
		return null;
	}

	private String enregistrerNouvFichier(String titre, String extension) {
		FileDialog dialogueFichier = new FileDialog((JFrame) null, titre, FileDialog.SAVE);

		// Ouvrir dans le dernier dossier utilisé
		if (dernierDossier != null) { dialogueFichier.setDirectory(dernierDossier); }

		// Suggestion d’extension (visuelle)
		dialogueFichier.setFile("*" + extension);
		dialogueFichier.setVisible(true);

		String nomFichier = dialogueFichier.getFile();
		String dossier = dialogueFichier.getDirectory();

		if (nomFichier != null && dossier != null) {
			// Ajouter l’extension si oubliée
			if (!nomFichier.toLowerCase().endsWith(extension)) { nomFichier += extension; }
			File fichier = new File(dossier, nomFichier);

			// Confirmation si le fichier existe
			if (fichier.exists())
			{
				int choix = JOptionPane.showConfirmDialog(this, "Le fichier existe déjà. Voulez-vous l’écraser ?", "Confirmation", JOptionPane.YES_NO_OPTION);
				if (choix != JOptionPane.YES_OPTION) { return null; }
			}
			// Mémoriser le dossier
			dernierDossier = dossier;
			// RETOUR DU CHEMIN COMPLET
			return fichier.getAbsolutePath();
		}
		return null;
	}

	private boolean paramRecuit()
	{
		JTextField ch1 = new JTextField(10);
		JTextField ch2 = new JTextField(10);
		JTextField ch3 = new JTextField(10);

		Font f = new Font("Montserrat", Font.PLAIN, 14);
		ch1.setFont(f);
		ch2.setFont(f);
		ch3.setFont(f);

		Object[] contenu = { 	"Température initiale (> 0) :", ch1, 
								"Seuil d'arrêt (> 0) :", ch2,
								"Alpha (entre 0.1 et 0.9) :", ch3 };

		while (true)
		{
			int result = JOptionPane.showConfirmDialog(this, contenu, "Paramètres du recuit simulé", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

			// Annuler → stop immédiat
			if (result != JOptionPane.OK_OPTION) { return false; }

			boolean erreur = false;
			StringBuilder message = new StringBuilder("Veuillez corriger les champs suivants :\n");

			Double tInit = null;
			Double seuil = null;
			Double a = null;

			// Validation ch1
			try
			{
				tInit = Double.parseDouble(ch1.getText().trim());
				if (tInit <= 0) { throw new IllegalArgumentException(); }
			} catch (Exception e)
			{
				erreur = true;
				message.append("• Température initiale (> 0)\n");
				ch1.setText("");
			}

			// Validation ch2
			try
			{
				seuil = Double.parseDouble(ch2.getText().trim());
				if (seuil <= 0) { throw new IllegalArgumentException(); }
			} catch (Exception e)
			{
				erreur = true;
				message.append("• Seuil d'arrêt (> 0)\n");
				ch2.setText("");
			}

			// Validation ch3
			try
			{
				a = Double.parseDouble(ch3.getText().trim());
				if (a < 0.1 || a > 0.9) { throw new IllegalArgumentException(); }
			} catch (Exception e)
			{
				erreur = true;
				message.append("• Alpha (entre 0.1 et 0.9)\n");
				ch3.setText("");
			}

			// ❌ Erreurs → on reboucle
			if (erreur)
			{
				JOptionPane.showMessageDialog(this, message.toString(), "Valeurs invalides", JOptionPane.ERROR_MESSAGE);

				if (ch1.getText().isEmpty()){ ch1.requestFocusInWindow(); }
				else if (ch2.getText().isEmpty()) { ch2.requestFocusInWindow(); }
				else { ch3.requestFocusInWindow(); }
					

				continue;
			}

			// ✅ Tout est bon → on stocke et on QUITTE
			this.tempInit = tInit;
			this.seuilArret = seuil;
			this.alpha = a;

			return true; // ⬅️ FERMETURE DÉFINITIVE
		}
	}
}
