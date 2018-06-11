package renderEngine.terrains.test;

import util.dataStructures.Node;

public class TerrainQuadtree extends Node{

	private static int rootNodes = 8;

	public static int getRootNodes() {
		return rootNodes;
	}

	public static void setRootNodes(int rootNodes) {
		TerrainQuadtree.rootNodes = rootNodes;
	}
}
