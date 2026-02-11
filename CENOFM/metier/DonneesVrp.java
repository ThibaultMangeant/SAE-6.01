package CENOFM.metier;

import java.util.ArrayList;
import java.util.List;

public class DonneesVrp {
	private int 			nbClients;
	private int 			nbVehicules;
	private double 			bestSolution;
	private int 			qMax;
	private Noeud 			depot;
	private List<Noeud> 	clients = new ArrayList<>();

	public int 			getNbClients	() 							{ return nbClients; 					}
	public void 		setNbClients	(int nbClients) 			{ this.nbClients = nbClients; 			}
	public int 			getNbVehicules	() 							{ return nbVehicules; 					}
	public void 		setNbVehicules	(int nbVehicules) 			{ this.nbVehicules = nbVehicules; 		}
	public double 		getBestSolution	() 							{ return bestSolution; 					}
	public void 		setBestSolution	(double bestSolution) 		{ this.bestSolution = bestSolution; 	}
	public int 			getqMax			() 							{ return qMax; 							}
	public void 		setqMax			(int qMax) 					{ this.qMax = qMax; 					}
	public Noeud 		getDepot		() 							{ return depot; 						}
	public void 		setDepot		(Noeud depot) 				{ this.depot = depot; 					}
	public List<Noeud> 	getClients		() 							{ return clients; 						}
	public void 		setClients		( List<Noeud> clients ) 	{ this.clients = clients; 				}

	public Noeud[] getTableauNoeudsComplet() {
		Noeud[] tab 	= new Noeud[nbClients + 1];
		tab[0] 			= depot;
		for ( int i = 0; i < clients.size(); i++ ) { tab[i + 1] = clients.get(i); }
		return tab;
	}

	public Noeud getNoeud( int i ) {
		if ( i == 0 ) { return depot; }
		else { return clients.get( i - 1 ); }
	}

	@Override
	public String toString() {
		return 	"DonneesVrp [nbClients=" + nbClients + ", nbVehicules=" + nbVehicules + 
				", bestSolution=" + bestSolution + ", qMax=" + qMax + ", depot=" + depot + 
				", clients=" + clients + "]";
	}

}