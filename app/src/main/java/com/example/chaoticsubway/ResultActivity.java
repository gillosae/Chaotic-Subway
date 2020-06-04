package com.example.chaoticsubway;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Data
        ArrayList<Pathway> data = new ArrayList<>();
        data.add(new Pathway(new Station("상도", 7), new Station("대림", 7)));
        data.add(new Pathway(new Station("대림", 2), new Station("신촌", 2)));

        //Adapter
        PathwayAdapter adapter = new PathwayAdapter(data);

        //Link View and Adapter
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ResultActivity.this, position + " 번째 아이템 선택", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
