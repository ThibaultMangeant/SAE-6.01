import java.util.*;

// Représente une solution complète (ensemble de tournées)
class Solution {
    List<List<Noeud>> tournees;
    double distanceTotale;

    public Solution() {
        this.tournees = new ArrayList<>();
        this.distanceTotale = 0;
    }

    // Copie profonde de la solution pour tester des modifications
    public Solution copie() {
        Solution nouvelle = new Solution();
        for (List<Noeud> tournee : this.tournees) {
            nouvelle.tournees.add(new ArrayList<>(tournee));
        }
        nouvelle.distanceTotale = this.distanceTotale;
        return nouvelle;
    }
}

public class RecuitSimuleCVRP {
    private List<Noeud> Noeuds;
    private Noeud depot;
    private int qMax;
    private Random random = new Random();

    public RecuitSimuleCVRP(List<Noeud> Noeuds, Noeud depot, int qMax) {
        this.Noeuds = Noeuds;
        this.depot = depot;
        this.qMax = qMax;
    }

    // --- ALGORITHME PRINCIPAL ---
    public void resoudre() {
        // Paramètres du recuit
        double temperature = 1000.0;
        double temperatureMin = 0.01;
        double alpha = 0.999; // Refroidissement lent
        int iterationsParPalier = 500;

        // 1. Solution initiale (Gloutonne : un véhicule par Noeud)
        Solution actuelle = genererSolutionInitiale();
        calculerDistanceTotale(actuelle);
        Solution meilleure = actuelle.copie();

        System.out.println("Distance initiale : " + actuelle.distanceTotale);

        // 2. Boucle du Recuit
        while (temperature > temperatureMin) {
            for (int i = 0; i < iterationsParPalier; i++) {
                // Créer un voisin par une modification aléatoire
                Solution voisin = genererVoisin(actuelle);
                
                double delta = voisin.distanceTotale - actuelle.distanceTotale;

                // Critère d'acceptation (Metropolis)
                if (delta < 0 || Math.exp(-delta / temperature) > random.nextDouble()) {
                    actuelle = voisin;
                    if (actuelle.distanceTotale < meilleure.distanceTotale) {
                        meilleure = actuelle.copie();
                    }
                }
            }
            temperature *= alpha; // Refroidissement
        }

        afficherResultats(meilleure);
    }

    // --- OPÉRATEURS DE VOISINAGE ---
    private Solution genererVoisin(Solution actuelle) {
        Solution voisin = actuelle.copie();
        int typeMouvement = random.nextInt(2);

        if (typeMouvement == 0) {
            mouvementEchange(voisin); // Échange deux Noeuds entre deux tournées
        } else {
            mouvementDeplacement(voisin); // Déplace un Noeud vers une autre tournée
        }

        calculerDistanceTotale(voisin);
        return voisin;
    }

    // Échange deux Noeuds de positions aléatoires
    private void mouvementEchange(Solution sol) {
        int t1 = random.nextInt(sol.tournees.size());
        int t2 = random.nextInt(sol.tournees.size());
        
        List<Noeud> r1 = sol.tournees.get(t1);
        List<Noeud> r2 = sol.tournees.get(t2);

        if (r1.isEmpty() || r2.isEmpty()) return;

        int i1 = random.nextInt(r1.size());
        int i2 = random.nextInt(r2.size());

        Noeud c1 = r1.get(i1);
        Noeud c2 = r2.get(i2);

        // Vérification capacité avant d'échanger
        if (calculerDemande(r1) - c1.demande + c2.demande <= qMax &&
            calculerDemande(r2) - c2.demande + c1.demande <= qMax) {
            r1.set(i1, c2);
            r2.set(i2, c1);
        }
    }

    // Déplace un Noeud d'une tournée à une autre
    private void mouvementDeplacement(Solution sol) {
        int t1 = random.nextInt(sol.tournees.size());
        int t2 = random.nextInt(sol.tournees.size());
        
        List<Noeud> depart = sol.tournees.get(t1);
        List<Noeud> arrivee = sol.tournees.get(t2);

        if (depart.isEmpty()) return;

        int index = random.nextInt(depart.size());
        Noeud c = depart.get(index);

        // Vérification capacité de la tournée d'arrivée
        if (calculerDemande(arrivee) + c.demande <= qMax) {
            depart.remove(index);
            arrivee.add(c);
            // Nettoyage si une tournée devient vide
            if (depart.isEmpty() && sol.tournees.size() > 1) {
                sol.tournees.remove(t1);
            }
        }
    }

    // --- OUTILS DE CALCUL ---
    private Solution genererSolutionInitiale() {
        Solution sol = new Solution();
        for (Noeud c : Noeuds) {
            List<Noeud> nouvelleTournee = new ArrayList<>();
            nouvelleTournee.add(c);
            sol.tournees.add(nouvelleTournee);
        }
        return sol;
    }

    private void calculerDistanceTotale(Solution sol) {
        double d = 0;
        for (List<Noeud> t : sol.tournees) {
            if (t.isEmpty()) continue;
            d += distance(depot, t.get(0));
            for (int i = 0; i < t.size() - 1; i++) {
                d += distance(t.get(i), t.get(i + 1));
            }
            d += distance(t.get(t.size() - 1), depot);
        }
        sol.distanceTotale = d;
    }

    private double distance(Noeud a, Noeud b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private int calculerDemande(List<Noeud> tournee) {
        int total = 0;
        for (Noeud c : tournee) total += c.demande;
        return total;
    }

    private void afficherResultats(Solution s) {
        System.out.println("\n--- MEILLEURE SOLUTION TROUVÉE ---");
        System.out.println("Distance totale : " + String.format("%.2f", s.distanceTotale));
        System.out.println("Nombre de véhicules : " + s.tournees.size());
        for (int i = 0; i < s.tournees.size(); i++) {
            System.out.print("Véhicule " + (i + 1) + " : Dépôt");
            for (Noeud c : s.tournees.get(i)) System.out.print(" -> " + c.id);
            System.out.println(" -> Dépôt");
        }
    }
}