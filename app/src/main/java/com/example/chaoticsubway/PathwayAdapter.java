package com.example.chaoticsubway;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Text;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PathwayAdapter extends BaseAdapter {
    private final List<Pathway> mData;
    public PathwayAdapter(List<Pathway> data){
        mData = data;
    }

    @Override
    public int getCount() { //Num of items
        return mData.size();
    }

    @Override
    public Object getItem(int position) { //Position_th item
        return mData.get(position); //추후 DB ID로 수정
    }

    @Override
    public long getItemId(int position) { //Position_th id
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //Position_th item's view
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pathway, parent, false);

        //Get view ids
        TextView depTime = (TextView)convertView.findViewById(R.id.depTime);
        ImageView depLineCircle = (ImageView)convertView.findViewById(R.id.depLineCircle);
        TextView depStationName = (TextView)convertView.findViewById(R.id.depStationName);

        TextView arrTime = (TextView)convertView.findViewById(R.id.arrTime);
        ImageView arrLineCircle = (ImageView)convertView.findViewById(R.id.arrLineCircle);
        TextView arrStationName = (TextView)convertView.findViewById(R.id.arrStationName);

        //짧게수정
        Drawable drawable;
        switch(mData.get(position).getDepStation().getLine().get(0)){
            case 1:
                drawable = parent.getResources().getDrawable(R.drawable.line1);
                break;
            case 2:
                drawable = parent.getResources().getDrawable(R.drawable.line2);
                break;
            case 3:
                drawable = parent.getResources().getDrawable(R.drawable.line3);
                break;
            case 4:
                drawable = parent.getResources().getDrawable(R.drawable.line4);
                break;
            case 5:
                drawable = parent.getResources().getDrawable(R.drawable.line5);
                break;
            case 6:
                drawable = parent.getResources().getDrawable(R.drawable.line6);
                break;
            case 7:
                drawable = parent.getResources().getDrawable(R.drawable.line7);
                break;
            case 8:
                drawable = parent.getResources().getDrawable(R.drawable.line8);
                break;
            case 9:
                drawable = parent.getResources().getDrawable(R.drawable.line9);
                break;
            default:
                drawable = parent.getResources().getDrawable(R.drawable.ic_launcher_background);
                break;
        }

        //Data from current position(index)
        Pathway pathway = mData.get(position);
        System.out.println(pathway.toString());

        //Set data to view
        depTime.setText(pathway.getDepTime());
        depStationName.setText(pathway.getDepStation().getStationName());
        depLineCircle.setImageDrawable(drawable);

        arrTime.setText(pathway.getArrTime());
        arrStationName.setText(pathway.getArrStation().getStationName());
        arrLineCircle.setImageDrawable(drawable);

        return convertView;
    }
}