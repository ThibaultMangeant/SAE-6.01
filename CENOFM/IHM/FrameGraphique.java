package CENOFM.IHM;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;

import java.util.List;
import CENOFM.metier.Noeud;

public class FrameGraphique
{
	public FrameGraphique(int nbArcs, List<Noeud> clients)
	{
		System.out.println("Nombre de clients : " + clients.size());
		int nbClients = clients.size();

		Node node;
		Node[] graphNodes = new Node[nbClients];

		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		SingleGraph graph = new SingleGraph("CENOFM");
		graph.setAttribute("ui.stylesheet", "node { shape:circle; fill-color:white; stroke-mode:plain; stroke-width:2; padding:10; stroke-color:black; text-mode:normal; text-background-color:white; text-alignment:center; text-size:16; size-mode:fit; }");

		for (Noeud client : clients)
		{
			if (client.getId() == 1)
				node = graph.addNode("Dépôt");
			else
				node = graph.addNode("" + (client.getId() - 1));

			node.setAttribute("ui.label", node.getId());
			System.out.println("Création du noeud : " + node.getId());
			graphNodes[client.getId() - 1] = node;
		}

		for (int i = 0 ; i < graph.getNodeCount() ; i++)
		{
			int j = i + 1;

			if (j >= graph.getNodeCount())
				j = 0;
			String edgeId = "Arc " + i + "-" + j;
			if (!graph.getNode(graphNodes[i].getId()).hasEdgeBetween(graphNodes[j].getId()))
			{
				graph.addEdge(edgeId, graphNodes[i].getId(), graphNodes[j].getId(), true);
			}
		}

		Viewer viewer = graph.display();
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
	}
}
