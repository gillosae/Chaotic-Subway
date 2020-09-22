package com.example.chaoticsubway;

import java.util.ArrayList;
import java.util.List;

//List of nodes(for route searching)
public class Route {
    private List<Spot> spots = new ArrayList<Spot>(); //Forks, Terminal stations

    public Route(List<Spot> spots) {
        this.spots = spots;
    }

    public List<Pathway> GetPathways(){ //Conversion for Pathway view -> 그냥 Route로 생성되게 수정하는게 나을듯
        List<Pathway> pathways = new ArrayList<Pathway>();
        for (int i = 0; i< spots.size()-1; i++){
            pathways.add(new Pathway(spots.get(i).getStation(), spots.get(i+1).getStation(), null));
        }
        return pathways;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public int GetTransferNum(){ //Get total number of transfers in current route
        return spots.size() - 1;
    }

    //public long GetFee

    //

}