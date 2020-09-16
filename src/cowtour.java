/*
ID: eddycyu
LANG: JAVA
TASK: cowtour
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class cowtour {

    final static double INF = Double.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("cowtour.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("cowtour.out")))) {
            // number of pastures
            final int N = Integer.parseInt(f.readLine());

            // x-y coordinates for each pasture
            final int[] X = new int[N];
            final int[] Y = new int[N];
            for (int n = 0; n < N; n++) {
                String[] tokens = f.readLine().split(" ");
                X[n] = Integer.parseInt(tokens[0]);
                Y[n] = Integer.parseInt(tokens[1]);
            }

            // adjacency matrix ([0][0] = upper left corner)
            boolean[][] adj = new boolean[N][N];
            for (int y = 0; y < N; y++) {
                final String[] tokens = f.readLine().split("");
                for (int x = 0; x < N; x++) {
                    if (tokens[x].equals("1")) {
                        adj[x][y] = true;
                        adj[y][x] = true;
                    }
                }
            }

            // compute distance for each path (edges) between adjacent pastures (vertices)
            double[][] graph = new double[N][N]; // value = distance between two adjacent pastures
            for (int y = 0; y < N; y++) {
                for (int x = 0; x < N; x++) {
                    graph[x][y] = INF;
                }
            }
            for (int y = 1; y < N; y++) {
                for (int x = 0; x < y; x++) {
                    if (adj[x][y]) {
                        final double distance = getDistance(x, y, X, Y);
                        graph[x][y] = distance;
                        graph[y][x] = distance;
                    }
                }
            }

            // group the pastures into distinct fields
            final List<Set<Integer>> fields = getFields(N, adj);

            // use modified Floyd-Marshall algorithm to find shortest path between all pair of pastures
            final double[][] shortestPaths = findShortestPaths(graph);

            // keep track of the largest diameter before combining any fields
            final double maxDiameterBeforeCombine = getDiameter(shortestPaths);

            /*
             * At this point, we know:
             * (1) all distinct fields
             * (2) the shortest paths for each pair of pastures
             * (3) the max diameter before combining any fields
             *
             * We can now connect two pastures from two different fields and compute the combined path:
             *
             *   combinedDistance =
             *      [the longest shortest path from pasture A in field 1 to any other pasture in field 1]
             *      + [the longest shortest path from pasture B in field 2 to any other pasture in field 2]
             *      + [the distance from pasture A to pasture B]
             *
             * The solution will be the larger of:
             * (1) min(combinedDistance)
             * (2) max diameter before combining any fields
             */

            // iterate through all the fields and compute
            double minDiameter = INF;
            for (int i = 0; i < fields.size() - 1; i++) {
                for (int j = 1; j < fields.size(); j++) {
                    if (i != j) {
                        final Set<Integer> field1 = fields.get(i);
                        final Set<Integer> field2 = fields.get(j);
                        for (int y : field1) {
                            final double pasture1Distance = getLongestShortestPathForPasture(y, shortestPaths);
                            for (int x : field2) {
                                // compute combined distance if pasture(x) and pasture(y) are joined
                                final double pasture2Distance = getLongestShortestPathForPasture(x, shortestPaths);
                                final double newPathDistance = getDistance(x, y, X, Y);
                                final double combinedDistance = pasture1Distance + pasture2Distance + newPathDistance;
                                minDiameter = Math.min(minDiameter, combinedDistance);
                            }
                        }
                    }
                }
            }
            minDiameter = Math.max(minDiameter, maxDiameterBeforeCombine);

            // output result
            out.printf("%.6f\n", minDiameter);
        }
    }

    private static double getDistance(int pasture1, int pasture2, int[] X, int[] Y) {
        final int x1 = X[pasture1];
        final int y1 = Y[pasture1];
        final int x2 = X[pasture2];
        final int y2 = Y[pasture2];
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static List<Set<Integer>> getFields(int N, boolean[][] adj) {
        // group the pastures into distinct fields
        final List<Set<Integer>> fields = new ArrayList<>();
        final Set<Integer> assigned = new HashSet<>();
        Set<Integer> field = new HashSet<>();
        for (int n = 0; n < N; n++) {
            findConnectedPastures(n, N, adj, field);
            if (field.isEmpty() && !assigned.contains(n)) {
                // n is the only pasture in the field
                field.add(n);
            }
            if (!field.isEmpty()) {
                fields.add(field);
                assigned.addAll(field);
                field = new HashSet<>();
            }
        }
        return fields;
    }

    private static void findConnectedPastures(int n, int N, boolean[][] adj, Set<Integer> field) {
        // find all pastures connected to pasture(n) by some path
        for (int i = 0; i < N; i++) {
            if ((i != n) && adj[n][i]) {
                if (!field.contains(i)) {
                    field.add(n);
                    field.add(i);
                    findConnectedPastures(i, N, adj, field);
                }
                // remove pastures from adjacency matrix
                adj[n][i] = false;
                adj[i][n] = false;
            }
        }
    }

    private static double[][] findShortestPaths(double[][] graph) {
        // make copy of graph for storing distance
        final int V = graph.length;
        final double[][] distance = new double[V][V];
        for (int i = 0; i < V; i++) {
            System.arraycopy(graph[i], 0, distance[i], 0, V);
        }

        // compute shortest path for all pairs of vertices; assume no cycles
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if ((i != j)
                            && (distance[i][k] != INF) && (distance[k][j] != INF)
                            && (distance[i][k] + distance[k][j]) < distance[i][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }
        return distance;
    }

    private static double getDiameter(double[][] shortestPaths) {
        // diameter is the longest distance of all the shortest paths
        double diameter = 0;
        for (int i = 1; i < shortestPaths.length; i++) {
            for (int j = 0; j < i; j++) {
                if (shortestPaths[i][j] != INF) {
                    diameter = Math.max(diameter, shortestPaths[i][j]);
                }
            }
        }
        return diameter;
    }

    private static double getLongestShortestPathForPasture(int pasture, double[][] shortestPaths) {
        double longest = 0;
        for (int i = 0; i < shortestPaths.length; i++) {
            if (shortestPaths[pasture][i] != INF) {
                longest = Math.max(longest, shortestPaths[pasture][i]);
            }
        }
        return longest;
    }
}