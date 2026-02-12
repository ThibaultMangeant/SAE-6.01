package CENOFM.metier;

import java.util.*;

public class ResultatRecuit
{
	private final Solution 				meilleureSolution;
	private final List<Solution> 		snapshots;
	private final double 				tempsExecution;
	private final int 					iterations;
	private final List<List<Noeud>> 	tourneesFinales;

	public ResultatRecuit(Solution meilleure, List<Solution> snapshots, double temps, int iterations)
	{
		this.meilleureSolution 	= meilleure;
		this.snapshots 			= snapshots;
		this.tempsExecution 	= temps;
		this.iterations 		= iterations;
		this.tourneesFinales 	= meilleure.getTournees();
	}

	public Solution 			getMeilleureSolution	() { return meilleureSolution; 	}
	public List<Solution> 		getSnapshots			() { return snapshots; 			}
	public double 				getTempsExecution		() { return tempsExecution; 	}
	public int 					getIterations			() { return iterations; 		}
	public List<List<Noeud>> 	getTourneesFinales		() { return tourneesFinales; 	}
}
