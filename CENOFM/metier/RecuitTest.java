package CENOFM.metier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class RecuitTest {
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("../src/cantines.txt"));
			sc.useLocale(Locale.US);

			int nbNoeuds = sc.nextInt();
			double bestConnu = sc.nextDouble();
			int qMax = sc.nextInt();

			Noeud depot = new Noeud(0, sc.nextDouble(), sc.nextDouble(), 0);

			List<Noeud> Noeuds = new ArrayList<>();
			for (int i = 0; i < nbNoeuds; i++) {
				int id = sc.nextInt();
				double x = sc.nextDouble();
				double y = sc.nextDouble();
				int demande = sc.nextInt();
				Noeuds.add(new Noeud(id, x, y, demande));
			}

			RecuitSimuleCVRP rs = new RecuitSimuleCVRP(Noeuds, depot, qMax);
			rs.resoudre();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}