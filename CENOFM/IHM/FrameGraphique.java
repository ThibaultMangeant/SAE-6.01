package CENOFM.IHM;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;

import java.util.List;

import CENOFM.metier.Noeud;

public class FrameGraphique
{
	private List<String> couleurs;
	public FrameGraphique(List<List<Noeud>> solution)
	{
		this.couleurs = List.of("#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#800000", "#008000", "#000080", "#808000");

		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		SingleGraph graph = new SingleGraph("CENOFM");
		graph.setAttribute("ui.stylesheet", "node { shape:circle; fill-color:white; stroke-mode:plain; stroke-width:1; padding:5; stroke-color:black; text-mode:normal; text-background-color:white; text-alignment:center; text-size:16; size-mode:fit; }");

		this.creationNodes(graph, solution);
		this.creationArcs (graph, solution);

		Viewer viewer = graph.display();
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
	}

	private void creationNodes(SingleGraph graph, List<List<Noeud>> solution)
	{
		Node node;

		// Création des noeuds
		for (List<Noeud> clientList : solution)
		{
			for (Noeud client : clientList)
			{
				if (client.getId() == 0)
					if (graph.getNode("0") == null)
						node = graph.addNode("" + client.getId());
					else
						node = null;
				else
					node = graph.addNode("" + client.getId());

				if (node != null)
				{
					if (client.getId() == 0)
					{
						node.setAttribute("ui.label", "Dépôt");
						this.colorerNode(node, "#b7b7b7");
					}
					else
						node.setAttribute("ui.label", node.getId());
				}
			}
		}
	}

	private void creationArcs(SingleGraph graph, List<List<Noeud>> solution)
	{
		// Création des arcs
		for (int vehicule = 0 ; vehicule < solution.size(); vehicule++)
		{
			List<Noeud> clientList = solution.get(vehicule);

			for (int i = 0; i < clientList.size(); i++)
			{
				Noeud client1 = clientList.get(i);
				Noeud client2 = (i + 1 < clientList.size()) ? clientList.get(i + 1) : clientList.get(0);

				if (!graph.getNode("" + client1.getId()).hasEdgeBetween("" + client2.getId()))
				{
					String edgeId = "Arc " + client1.getId() + "-" + client2.getId();
					Edge edge = graph.addEdge(edgeId, graph.getNode("" + client1.getId()).getId(), graph.getNode("" + client2.getId()).getId(), true);
					this.colorerArc(graph.getNode("" + client1.getId()), graph.getNode("" + client2.getId()), this.couleurs.get(vehicule % this.couleurs.size()));
					edge.setAttribute("ui.label", String.format("%.2f", client1.distance(client2)));
				}
			}
		}
	}

	private void colorerNode(Node node, String color)
	{
		node.addAttribute("ui.style", "fill-color:" + color + "; stroke-color:" + color + ";");
	}

	private void colorerArc(Node n1, Node n2, String color)
	{
		if (n1.hasEdgeBetween(n2.getId()))
		{
			n1.getEdgeBetween(n2.getId()).addAttribute("ui.style", "fill-color:" + color + ";  size:3;");
		}
	}
}
