package CENOFM.metier;

import java.io.*;
import java.util.*;

public class ConversionVrpDat {

	public static void convertir( DonneesVrp donnees, String outputPath ) throws IOException {

		PrintWriter writer = new PrintWriter( new FileWriter( outputPath ) );

		writer.println( "/*********************************************" 	);
		writer.println( " * OPL .dat généré via classe Noeud" 				);
		writer.println( " *********************************************/" 	);
		writer.println();

		writer.println( "nbClientDep = " 	+ donnees.getTableauNoeudsComplet().length + ";" );
		writer.println( "Qmax = " 			+ donnees.getqMax() + ";" );
		writer.println( "nbVehicules = " 	+ donnees.getNbVehicules() + ";" );
		writer.println();

		writer.print( "Demande = [" );
		for ( int i = 0; i < donnees.getTableauNoeudsComplet().length; i++ )
		{
			writer.print( donnees.getNoeud(i).demande + ( i == donnees.getTableauNoeudsComplet().length - 1 ? "" : ", " ) );
		}
		writer.println( "];" );
		writer.println();

		writer.println( "Distance = [" );
		for ( int i = 0; i < donnees.getTableauNoeudsComplet().length; i++ )
		{
			writer.print( " [" );
			for ( int j = 0; j < donnees.getTableauNoeudsComplet().length; j++ )
			{
				double dist = donnees.getNoeud(i).distance( donnees.getNoeud(j) );
				writer.printf( Locale.US, "%.2f", dist );
				if ( j < donnees.getTableauNoeudsComplet().length - 1 ) { writer.print(" "); }
			}
			writer.println( i == donnees.getTableauNoeudsComplet().length - 1 ? "]" : "]," );
		}
		writer.println( "];" );
		writer.close();
	}
}