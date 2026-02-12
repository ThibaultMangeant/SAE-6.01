package CENOFM.IHM;

import org.graphstream.graph.implementations.SingleGraph;
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

		Viewer viewer = graph.display(false);
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
	}

	private void creationNodes(SingleGraph graph, List<List<Noeud>> solution)
	{
		Node node;

		/* Création des noeuds */

		// Ajout du dépôt
		if (graph.getNode("0") == null)
		{
			node = graph.addNode("0");
			node.setAttribute("xyz", 0, 0, 0);
			node.setAttribute("ui.label", "Dépôt");
			this.colorerNode(node, "#b7b7b7");
		}

		// Ajout des clients
		for (List<Noeud> clientList : solution)
		{
			for (Noeud client : clientList)
			{
				node = graph.addNode("" + client.getId());
				node.setAttribute("xyz", client.getX(), client.getY(), 0);
				node.setAttribute("ui.label", node.getId());
			}
		}
	}

	private void creationArcs(SingleGraph graph, List<List<Noeud>> solution)
	{
		// Création des arcs
		for (int vehicule = 0 ; vehicule < solution.size(); vehicule++)
		{
			List<Noeud> clientList = solution.get(vehicule);

			for (int i = -1; i <= clientList.size(); i++)
			{
				// Arc entre le dépôt et le premier client
				if (i == -1)
				{
					graph.addEdge("Arc 0-" + clientList.get(i + 1).getId(), "0", "" + clientList.get(i + 1).getId(), true);
					this.colorerArc(graph.getNode("0"), graph.getNode("" + clientList.get(i + 1).getId()), this.couleurs.get(vehicule % this.couleurs.size()));
				}

				// Arcs entre les clients
				Noeud client1 = (i == -1 || i == clientList.size()) ? null : clientList.get(i);
				Noeud client2 = (i + 1 < clientList.size()) ? clientList.get(i + 1) : null;

				if ((client1 != null) && (client2 != null) && !graph.getNode("" + client1.getId()).hasEdgeBetween("" + client2.getId()))
				{
					String edgeId = "Arc " + client1.getId() + "-" + client2.getId();
					graph.addEdge(edgeId, graph.getNode("" + client1.getId()).getId(), graph.getNode("" + client2.getId()).getId(), true);
					this.colorerArc(graph.getNode("" + client1.getId()), graph.getNode("" + client2.getId()), this.couleurs.get(vehicule % this.couleurs.size()));
				}

				// Arc entre le dernier client et le dépôt
				if (i == clientList.size())
				{
					graph.addEdge("Arc " + clientList.get(i - 1).getId() + "-0", "" + clientList.get(i - 1).getId(), "0", true);
					this.colorerArc(graph.getNode("" + clientList.get(i - 1).getId()), graph.getNode("0"), this.couleurs.get(vehicule % this.couleurs.size()));
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
