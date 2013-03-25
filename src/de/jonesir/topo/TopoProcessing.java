package de.jonesir.topo;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import com.graph.elements.edge.EdgeElement;
import com.graph.elements.vertex.VertexElement;
import com.graph.graphcontroller.Gcontroller;
import com.graph.graphcontroller.impl.GcontrollerImpl;
import com.graph.path.PathElement;
import com.graph.path.algorithms.MultiPathComputationAlgorithm;
import com.graph.path.algorithms.constraints.MultiPathConstraint;
import com.graph.path.algorithms.constraints.multipath.impl.SimpleMultiPathComputationConstraint;
import com.graph.path.algorithms.multipath.impl.SimpleMultiPathComputationAlgorithm;
import com.pcee.architecture.computationmodule.ted.TopologyInformation;

public class TopoProcessing {

	private static Random random = new Random();
	private static MultiPathComputationAlgorithm algo = null;
	private static MultiPathConstraint constraints = null;
	private static Gcontroller graph = null;

	public static void main(String[] args) {
		// for (int i = 0; i < 10; i++)
		// log("" + random.nextInt(10));
		generateTmpTopo();
	}

	public static void generateTmpTopo() {
		TopologyInformation topo = TopologyInformation.getInstance();
		graph = topo.getGraph();
		String[] sd = null;
		for (int i = 0; i < 10; i++) {
			sd = getSourceAndDestination();
			log("Source : " + sd[0] + " -- Destination : " + sd[1]);
		}

		algo = getAlgo();
		Gcontroller newGraph = new GcontrollerImpl();
		constraints = getConstraints(newGraph, sd[0], sd[1]);
		ArrayList<PathElement> paths = computePaths();

	}

	private static String[] getSourceAndDestination() {
		int source, destination;
		ArrayList<String> vertexIDArray = new ArrayList<String>();
		for (String vertexID : graph.getVertexIDSet())
			vertexIDArray.add(vertexID);
		int vertexCount = graph.getVertexIDSet().size();
		source = random.nextInt(vertexCount);
		do {
			destination = random.nextInt(vertexCount);
		} while (source == destination);

		return new String[] { vertexIDArray.get(source), vertexIDArray.get(destination) };
	}

	private static MultiPathComputationAlgorithm getAlgo() {
		return new SimpleMultiPathComputationAlgorithm();
	}

	private static MultiPathConstraint getConstraints(Gcontroller graph, String source, String destination) {
		return new SimpleMultiPathComputationConstraint(new VertexElement(source, graph), new VertexElement(destination, graph), 4, 0);
	}

	private static ArrayList<PathElement> computePaths() {
		return algo.computePath(graph, constraints);
	}

	private static void log(String logString) {
		System.out.println(logString);
	}

}
