package com.example.chaoticsubway;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StationManager {
//    private static StationManager instance = null;
    private Context context;

    private static String nodeStationsPath = "transferstations.csv";
    private static String nodeStationsLinesPath = "transferstations_line.csv";

    public static int vertex = 58;
    public static int edge = 206;
    public SubwayGraph subwayGraph = new SubwayGraph(vertex, edge); //길찾기용 그래프
    public static String[] nodeStations = new String[vertex]; //환승역(노드)
    public static String[] allStations; //일반역

    public StationManager(Context contextFromApp) {
        context = contextFromApp;
        try {
            SetNodeStations(nodeStations, nodeStationsPath);
            SetGraphEdges(nodeStations, nodeStationsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void init(Context contextFromApp){
//        if(context == null)
//            context = contextFromApp;
//    }
//
//    private Context getContext(){
//        return context;
//    }
//
//    public static Context get(){
//        return getInstance().getContext();
//    }
//
//    public static StationManager getInstance() {
//        return instance == null ? (instance = new StationManager()): instance;
//    }

    private void SetNodeStations(String[] nodeStations, String transferStationsPath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(transferStationsPath)));
        String line;
        int lineNum = 0;
        while((line = br.readLine()) != null){
            String[] arr = line.split(",");
            nodeStations[lineNum] = arr[0];
            lineNum++;
        }
    }

    private void SetGraphEdges(String[] transferStations, String transferStationsPath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(transferStationsPath)));
        String line;
        int edgeNum = 0;
        while((line = br.readLine()) != null){
            String[] arr = line.split(",");
            for(int j=1; j<arr.length; j++){
                subwayGraph.edge[edgeNum].src = GetVertexNum(arr[0]);
                subwayGraph.edge[edgeNum].dest = GetVertexNum(arr[j]);
                subwayGraph.edge[edgeNum].weight = 1; //웨이트 역 사이 길이로 반영할지?
                edgeNum++;
            }
        }
    }

    private int GetVertexNum(String string){
        for(int i = 0; i< nodeStations.length; i++){
            if(nodeStations[i].equals(string)) return i;
        }
        return 0;
    }

    public static Station getStationFromNum(int num) throws IOException {
        //Parse down CSV(which consists line numbers of Node stations)
        BufferedReader br = new BufferedReader(new InputStreamReader(MainActivity.context.getAssets().open(nodeStationsLinesPath)));
        String line;
        int lineNum = 0;
        while((line = br.readLine()) != null){
            if(lineNum == num) { //When reached target row
                String[] arr = line.split(",");
                ArrayList<Integer> tempList = new ArrayList<Integer>();
                for(int i=1; i<arr.length; i++) tempList.add(Integer.parseInt(arr[i]));
                return new Station(arr[0], tempList);
            }
            lineNum++;
        }
        return null;
    }




//    public String[] GetAdjacentNodeStations(){ //Return list of nodestations adjacent from a station
//
//    }

}
