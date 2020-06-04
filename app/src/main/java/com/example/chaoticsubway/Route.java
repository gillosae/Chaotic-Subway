package com.example.chaoticsubway;

import java.util.ArrayList;
import java.util.List;

//List of nodes(for route searching)
public class Route {
    private List<Node> nodes = new ArrayList<Node>(); //Forks, Terminal stations

    public Route(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Pathway> GetPathways(){ //Conversion for Pathway view -> 그냥 Route로 생성되게 수정하는게 나을듯
        List<Pathway> pathways = new ArrayList<Pathway>();
        for (int i=0; i<nodes.size()-1; i++){
            pathways.add(new Pathway(nodes.get(i).getStation(), nodes.get(i+1).getStation()));
        }
        return pathways;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int GetTransferNum(){ //Get total number of transfers in current route
        return nodes.size() - 1;
    }

    //public long GetFee

    //

}