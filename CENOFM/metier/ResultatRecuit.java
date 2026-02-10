package CENOFM.metier;

import java.util.List;

/**
 * Contient : - la meilleure solution - les solutions intermédiaires (toutes les
 * X itérations) - le temps d'exécution - le nombre total d'itérations
 */
public class ResultatRecuit
{

	private final Solution meilleureSolution;
	private final List<Solution> snapshots;
	private final double tempsExecution;
	private final int iterations;

	public ResultatRecuit(Solution meilleure, List<Solution> snapshots, double temps, int iterations)
	{
		this.meilleureSolution = meilleure;
		this.snapshots = snapshots;
		this.tempsExecution = temps;
		this.iterations = iterations;
	}

	public Solution getMeilleureSolution() { return meilleureSolution; }
	public List<Solution> getSnapshots() { return snapshots; }
	public double getTempsExecution() { return tempsExecution; }
	public int getIterations() { return iterations; }
}
