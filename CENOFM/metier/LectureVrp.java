package CENOFM.metier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class LectureVrp
{

	public DonneesVrp charger( String contenuFichier, int nbVehicules ) throws IOException
	{
		DonneesVrp donnees = new DonneesVrp();
		donnees.setNbVehicules( nbVehicules );

		// ------------------------------
		// Nettoyage du contenu : supprime lignes vides et trim
		// ------------------------------
		List<String> 	lines 	= new ArrayList<>();
		Scanner 		scRaw 	= new Scanner( contenuFichier );
		while ( scRaw.hasNextLine() )
		{
			String line = scRaw.nextLine().trim();
			if ( !line.isEmpty() ) { lines.add( line ); }
		}
		scRaw.close();

		// Scanner sur le texte nettoyé
		String 		cleanedContent 	= String.join( "\n", lines );
		Scanner 	sc 				= new Scanner( cleanedContent );
		sc.useLocale( Locale.US );

		try
		{
			// Vérification fichier vide
			if ( !sc.hasNext() ) { sc.close(); throw new IOException( "Le fichier est vide" ); }
			// ------------------------------
			// En-tête : nbClients, bestSolution, Qmax
			// ------------------------------
			if ( !sc.hasNextInt() ) { sc.close(); throw new IOException( "nbClients manquant ou invalide" ); }
			int nbClients = sc.nextInt();
			if ( nbClients <= 0 ) { sc.close(); throw new IOException( "nbClients doit être positif" ); }
			donnees.setNbClients(nbClients);

			if ( !sc.hasNextDouble() ) { sc.close(); throw new IOException( "bestSolution manquante ou invalide" ); }
			double bestSolution = sc.nextDouble();
			donnees.setBestSolution(bestSolution);

			if ( !sc.hasNextInt() ) { sc.close(); throw new IOException( "qMax manquant ou invalide" ); }
			int qMax = sc.nextInt();
			if ( qMax <= 0 ) { sc.close(); throw new IOException( "qMax doit être positif" ); }
			donnees.setqMax(qMax);

			// ------------------------------
			// Dépôt : x y (optionnel 3e valeur pour compatibilité)
			// ------------------------------
			if ( !sc.hasNextDouble() ) { sc.close(); throw new IOException( "Coordonnée X du dépôt manquante ou invalide" ); }
			double depotX = sc.nextDouble();

			if ( !sc.hasNextDouble() ) { sc.close(); throw new IOException( "Coordonnée Y du dépôt manquante ou invalide" ); }
			double depotY = sc.nextDouble();

			donnees.setDepot( new Noeud( 0, depotX, depotY, 0 ) );

			// ------------------------------
			// Clients : id x y demande
			// ------------------------------
			for ( int i = 0; i < nbClients; i++ )
			{
				if ( !sc.hasNextInt() ) 		{ break; }
				int 	id 			= sc.nextInt();
				if ( !sc.hasNextDouble() ) 		{ break; }
				double 	x 			= sc.nextDouble();
				if ( !sc.hasNextDouble() ) 		{ break; }
				double 	y 			= sc.nextDouble();
				if ( !sc.hasNextInt() ) 		{ break; }
				int 	demande 	= sc.nextInt();
				donnees.getClients().add(new Noeud( id, x, y, demande ) );
			}

		} catch ( Exception e )
		{
			sc.close();
			throw new IOException( "Erreur lors de la lecture du fichier : " + e.getMessage() );
		}
		sc.close();
		return donnees;
	}
}
