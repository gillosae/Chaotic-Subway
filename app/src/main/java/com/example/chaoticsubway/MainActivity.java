package com.example.chaoticsubway;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public Map<Integer, List<Station>> stationsByLine = new HashMap<Integer, List<Station>>(); //Line No., Stations
    static List<Station> nodeStations = new ArrayList<Station>();

    private static BufferedReader br = null;
    private LocalTime localTime;

    private List<PointF> pointAs = new ArrayList<PointF>();
    private List<PointF> pointBs = new ArrayList<PointF>();
    private PointF pointA = new PointF(100, 100);
    private PointF pointB = new PointF(1000, 1000);

    private HorizontalScrollView scrollView;
    private LineView lineView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Horizontal ScrollView
        scrollView = (HorizontalScrollView)findViewById(R.id.map_container);
        scrollView.requestDisallowInterceptTouchEvent(true);

        //LineView
        lineView = (LineView)findViewById(R.id.lineview);
        lineView.setPointA(pointA);
        lineView.setPointB(pointB);
        lineView.draw();

        ParseStation();
        GetNodeStations();
        RouteSearch(6, 16);

    }

    private void ParseStation(){
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open("station.csv")));
            String line="";
            boolean isFirstLine = true;
            while((line = br.readLine()) != null){
                if(isFirstLine){
                    isFirstLine = false;
                    continue;
                }
                String array[] = line.split(","); //line, name, between, accumulated
                System.out.println(array[1]);
                int tempLine = Integer.parseInt(array[0]);
                if(stationsByLine.containsKey(tempLine)){
                    stationsByLine.get(tempLine).add(new Station(array[1], tempLine));
                }else{
                    List<Station> station = new ArrayList<Station>();
                    station.add(new Station(array[1], tempLine));
                    stationsByLine.put(tempLine, station);
                }
                System.out.println(stationsByLine.get(tempLine).size());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GetNodeStations(){
        for (int i=2; i<=9; i++){
            System.out.println(i);
            List<Station> tempList = stationsByLine.get(i);
            for (Station station : tempList) {
                if(station.isNode()){
                    nodeStations.add(station);
                }
            }
        }
    }

    private void MakeEdge(Graph graph, int i, int src, int dest, int weight){
        graph.edge[i].src = src-1;
        graph.edge[i].dest = dest-1;
        graph.edge[i].weight = weight;
    }

    private void RouteSearch(int V, int E){ //Vertice Num, Edge Num
        Graph graph = new Graph(V, E);
        MakeEdge(graph, 0, 1, 2, 1);
        MakeEdge(graph, 1, 2, 3, 8);
        MakeEdge(graph, 2, 1, 4, 2);
        MakeEdge(graph, 3, 2, 4, 3);
        MakeEdge(graph, 4, 3, 4, 8);
        MakeEdge(graph, 5, 4, 5, 11);
        MakeEdge(graph, 6, 4, 6, 10);
        MakeEdge(graph, 7, 5, 7, 2);
        MakeEdge(graph, 8, 1, 2, 1);
        MakeEdge(graph, 9, 2, 3, 8);
        MakeEdge(graph, 10, 4, 1, 2);
        MakeEdge(graph, 11, 4, 2,3);
        MakeEdge(graph, 12, 4, 3,8);
        MakeEdge(graph, 13, 5, 4,11);
        MakeEdge(graph, 14, 6, 4,10);
        MakeEdge(graph, 15, 7, 5, 2);
        //1:총신대입구, 2:동작, 3:충무로, 4:고속터미널, 5:가락시장, 6:석촌

        graph.BellmanFord(graph, 0);
    }

}
