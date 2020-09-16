package com.example.chaoticsubway;

import android.content.Context;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//orderedlist에서 역을 받아서 역 리스트에 넣는데, 기존 역과 중복된 게 있다면 기존 역에다가 추가.
//지선일 경우, 여러개 호선을 포함할 경우 노드이다.(인접역이 3개 이상이 아니더라도 노드일 수 있다)
//노드들로 환승역 그래프를 구성한다

//환승역에서 환승역으로 길찾기 완성
//일반 역이 주어졌을 때 인접한 환승역을 구하는 함수 완성
//일반역에서 일반역으로 시간표 바탕 길찾기 ㄱㄱ

public class SubwayGraph {
    class Edge{
        int src, dest, weight;
        Edge(){
            src = dest = weight = 0;
        }
    };

    int V, E;
    Edge edge[];

    SubwayGraph(int v, int e){
        V = v;
        E = e;
        edge = new Edge[e];
        for (int i=0; i<e; ++i) edge[i] = new Edge();
    }

    void BellmanFord(SubwayGraph graph, int src){
        int V = graph.V, E = graph.E;
        int dist[] = new int[V];

        for(int i=0; i<V; ++i)
            dist[i] = Integer.MAX_VALUE;
        dist[src] = 0;

        for(int i=1; i<V; ++i){
            for(int j=0; j<E; ++j){
                int u = graph.edge[j].src;
                int v = graph.edge[j].dest;
                int weight = graph.edge[j].weight;
                if(dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v])
                    dist[v] = dist[u] + weight;
            }
        }
        printArr(dist, V);
    }

    public String[] passedStations = new String[10];

    void SpecificBellmanFord(SubwayGraph graph, int src, int des){
        int V = graph.V, E = graph.E;
        int dist[] = new int[V];

        for(int i=0; i<V; ++i)
            dist[i] = Integer.MAX_VALUE;
        dist[src] = 0;

        for(int i=1; i<V; ++i){
            for(int j=0; j<E; ++j){
                int u = graph.edge[j].src;
                int v = graph.edge[j].dest;
                int weight = graph.edge[j].weight;
                if(dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]){
                    dist[v] = dist[u] + weight;
                    if(v==des){
                        System.out.println(MainActivity.transferStations[u]);
                        System.out.println(MainActivity.transferStations[src]+"에서 " +MainActivity.transferStations[des] + "까지 현재\t" + dist[des]);
                    }
                }
            }
        }
        specificPrintArr(dist, src, des);
    }

    void printArr(int dist[], int V){
        System.out.println("Vertex Distance from Source");
        for(int i=0; i<V; ++i)
            System.out.println(i + "\t\t" + dist[i]);
    }

    void specificPrintArr(int dist[], int src, int des){
        System.out.println(MainActivity.transferStations[src]+"에서 " +MainActivity.transferStations[des] + "까지\t" + dist[des]);
    }

}
