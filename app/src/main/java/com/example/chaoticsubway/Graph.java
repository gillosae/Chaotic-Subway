package com.example.chaoticsubway;

import java.util.*;
import java.lang.*;
import java.io.*;

class Graph {
    class Edge { //Weighted edge
        int src, dest, weight;
        Edge()
        {
            src = dest = weight = 0;
        }
    };

    int V, E;
    Edge edge[];

    public Graph(int v, int e) // Graph with V vertices and E edges
    {
        V = v;
        E = e;
        edge = new Edge[e];
        for (int i = 0; i < e; ++i)
            edge[i] = new Edge();
    }

    public void BellmanFord(Graph graph, int src)
    {
        int V = graph.V, E = graph.E;
        int dist[] = new int[V];

        //Initialize distances from src to all other vertices as infinite
        for (int i = 0; i < V; ++i)
            dist[i] = Integer.MAX_VALUE;
        dist[src] = 0;

        //Relax all edges |V| - 1 times
        //A simple shortest path from src to any other vertex can have at-most |V| - 1 edges
        for (int i = 1; i < V; ++i) {
            for (int j = 0; j < E; ++j) {
                int u = graph.edge[j].src;
                int v = graph.edge[j].dest;
                int weight = graph.edge[j].weight;
                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v])
                    dist[v] = dist[u] + weight;
            }
        }
        printArr(dist, V);
    }

    private void printArr(int dist[], int V)
    {
        System.out.println("Vertex Distance from Source");
        for (int i = 0; i < V; ++i)
            System.out.println(i + "\t\t" + dist[i]);
    }


}