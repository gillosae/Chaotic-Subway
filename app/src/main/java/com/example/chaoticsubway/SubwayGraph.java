package com.example.chaoticsubway;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
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

    public String[] routeStations;

    SubwayGraph(int v, int e){
        V = v;
        E = e;
        edge = new Edge[e];
        for (int i=0; i<e; ++i) edge[i] = new Edge();
        routeStations = new String[V];
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    void SpecificBellmanFord(SubwayGraph graph, int src, int des){
        int V = graph.V, E = graph.E;
        int dist[] = new int[V]; //Distance from source station to particular station

//        ArrayList<String>[] routeStations = new ArrayList[V]; //Possible routes from source station to particular station
//        for (int i = 0; i < V; i++) routeStations[i] = new ArrayList<String>(); //Initialize

        for(int i=0; i<V; ++i) dist[i] = Integer.MAX_VALUE; //Initialize all distance to max value
        dist[src] = 0; //Except source station itself
        routeStations[src] = Integer.toString(src);
//        routeStations[src].add(Integer.toString(src));

        for(int i=1; i<V; ++i){
            for(int j=0; j<E; ++j){
                int u = graph.edge[j].src;
                int v = graph.edge[j].dest;
                int weight = graph.edge[j].weight;
                if(dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]){ // =< ?
                    dist[v] = dist[u] + weight;
                    routeStations[v] = routeStations[u].toString() + " " + v;
                    if(v==des){
//                        System.out.println(String.join("-", routeStations[v]));
                        System.out.println(routeStations[v]);
                        System.out.println(subwayNumToName(routeStations[v]));
                        System.out.println(StationManager.nodeStations[src] + "에서 " + StationManager.nodeStations[des] + "까지 현재 \t" + dist[des]);
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
        System.out.println(StationManager.nodeStations[src]+"에서 " + StationManager.nodeStations[des] + "까지\t" + dist[des]);
    }

    public String subwayNumToName(String numString){
        String[] splited = numString.split("\\s+");
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<splited.length; i++){
            sb.append(StationManager.nodeStations[Integer.parseInt(splited[i])]);
            sb.append(" ");
        }
        return sb.toString();
    }

    //All Node Stations passing through containing all line numbers
    public ArrayList<Station> getNodeRoute(String numString) throws IOException {
        ArrayList<Station> nodeRoute = new ArrayList<Station>();

        //Split String of Node's CSV row num.
        String[] splited = numString.split("\\s+");

        System.out.println("+++++++++++++++GetNodeRoute");
        for(int i=0; i<splited.length; i++){
            nodeRoute.add(StationManager.getStationFromNum(Integer.parseInt(splited[i])));
            System.out.println(nodeRoute.get(i));
        }
        return nodeRoute;
    }

    //All Node stations with specific line numbers passing through
    public ArrayList<Station> getCleanRoute(ArrayList<Station> nodeRoute){
        ArrayList<Station> cleanRoute = new ArrayList<Station>();
        System.out.println("+++++++++++++++GetCleanRoute");
        for(int i=0; i<nodeRoute.size()-1; i++){
            nodeRoute.get(i).getLine().retainAll(nodeRoute.get(i+1).getLine());
            cleanRoute.add(nodeRoute.get(i));
            System.out.println(nodeRoute.get(i));
        }
        nodeRoute.get(nodeRoute.size()-1).getLine().retainAll(nodeRoute.get(nodeRoute.size()-2).getLine());
        cleanRoute.add(nodeRoute.get(nodeRoute.size()-1));
        System.out.println(nodeRoute.get(nodeRoute.size()-1));

        return cleanRoute;
    }

    //All 'Transferring' stations passing through
    public ArrayList<Station> getSimpleRoute(ArrayList<Station> nodeRoute){
        ArrayList<Station> cleanRoute = getCleanRoute(nodeRoute);
        ArrayList<Station> simpleRoute = new ArrayList<Station>();
        simpleRoute.add(cleanRoute.get(0));
        for(int i=1; i<cleanRoute.size()-2; i++){
            if(cleanRoute.get(i).getLine().get(0) != cleanRoute.get(i-1).getLine().get(0))
                simpleRoute.add(cleanRoute.get(i));
        }
        simpleRoute.add(cleanRoute.get(cleanRoute.size()-1));
        System.out.println("+++++++++++++++GetSimpleRoute");
        System.out.println(simpleRoute);
        return simpleRoute;
    }

}
