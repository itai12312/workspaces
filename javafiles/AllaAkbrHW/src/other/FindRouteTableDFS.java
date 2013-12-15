import java.util.Arrays;


public class FindRouteTableDFS {

	public static long[] f(Graph graph, long source, long target, int maxPath) {
		long[] routeNodes = new long[maxPath];
		long[][] nodeEdges = new long[maxPath][];
		long[] usedEdge = new long[maxPath];
		int[] visited = new int[(int) graph.getDomain().size()];
		int lastNode = 0;
		routeNodes[0] = source;
		nodeEdges[0] = graph.edgesFrom(source);
		usedEdge[0] = 0;
		for (int i = 0; i < visited.length; i++) {
			visited[i] = 0;
		}
		
		boolean foundDeeper;
		while (lastNode > -1) {
			if (routeNodes[lastNode] == target)   //target reached
				return Arrays.copyOfRange(routeNodes,0,lastNode+1);
			
			//try to move deeper
			foundDeeper = false;
			if (lastNode < maxPath-1) {
				while (usedEdge[lastNode] < nodeEdges[lastNode].length) {
					long nextNode = nodeEdges[lastNode][(int) usedEdge[lastNode]];
					if (visited[(int) nextNode] != 1) {
						//found unvisited deeper node
						lastNode++;
						routeNodes[lastNode] = nextNode;
						visited[(int) nextNode] = 1;
						nodeEdges[lastNode] = graph.edgesFrom(nextNode);
						usedEdge[lastNode] = 0;
						foundDeeper = true;
						break;
					}
					usedEdge[lastNode]++;
				}
			}
			
			if (!foundDeeper) {
				//didn't found a deeper node, backtrack
				lastNode--;
				if (lastNode > 0)
					usedEdge[lastNode]++;
			}
		}
		
		return null;
	}

}
