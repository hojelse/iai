using System;
using System.Collections.Generic;

class Program
{
    static void Main(string[] args)
    {
        String[] str = Console.ReadLine().Split(" ");
        int V = int.Parse(str[0]);
        int E = int.Parse(str[1]);

        Graph g = new Graph(V);

        for (int i = 0; i < E; i++)
        {
            String[] line = Console.ReadLine().Split(" ");
            g.addEdge(
                int.Parse(line[0]),
                int.Parse(line[1]),
                int.Parse(line[2])
            );
        }

        Graph directedShortestPathsTree = g.dijkstra(3);
        printGraph(directedShortestPathsTree);

        // int[] distTo = g.dijkstra(3);
        // printDistTo(distTo);
    }

    private static void printGraph(Graph graph)
    {
        List<(int, int)>[] adj = graph.adj;

        for (int i = 0; i < adj.Length; i++)
        {
            List<(int, int)> l = adj[i];
            foreach (var (to, weight) in l)
            {
                Console.WriteLine($"{i} -{weight}-> {to}");
            }
        }
    }

  private static void printDistTo(int[] distTo)
    {
        for (int v = 0; v < distTo.Length; v++)
        {
            int dist = distTo[v];
            Console.WriteLine($"{v}: {dist}");
        }
    }
}

class Graph {
    public int V;
    public List<(int, int)>[] adj;

    public Graph(int v) {
        V = v;
        adj = new List<(int, int)>[v];
        for (int i = 0; i < v; i++)
            adj[i] = new List<(int, int)>();
    }

    public void addEdge(int from, int to, int weight) {
        adj[from].Add((to, weight));
    }

    public List<(int, int)> outgoing(int v) {
        // Console.WriteLine($"outgoing({v})");
        return adj[v];
    }

    public Graph dijkstra(int start) {
        Graph paths = new Graph(V); // Directed Single Source Shortests Paths tree

        // Initialize distTo array with "infinite" distances
        int[] distTo = new int[V];
        for (int i = 0; i < distTo.Length; i++)
            distTo[i] = int.MaxValue;

        // Queue of search nodes to be expanded
        PriorityQueue<int,int> queue = new PriorityQueue<int,int>();

        // Initialize datastructures with start node
        distTo[start] = 0;
        queue.Enqueue(start, 0);

        while (queue.Count != 0)
        {
            // Expand neighborhood
            int node = queue.Dequeue();
            foreach (var neighbor in this.outgoing(node))
            {
                // Expand node
                (int to, int weight) = neighbor;

                int pathCostToNeighbor = distTo[node] + weight;

                if (pathCostToNeighbor < distTo[to]) {
                    distTo[to] = pathCostToNeighbor;
                    queue.Enqueue(to, distTo[to]);
                    
                    paths.adj[to] = new List<(int, int)>(); // clear previous path
                    paths.addEdge(to, node, weight);
                }
            }
        }

        return paths;
    }
}


//        2         6     
//   A---------B---------C
//   |         |         |
//   | 1       | 3       | 5
//   |         |         |
//   D---------E---------F---------G
//        4         2         1
