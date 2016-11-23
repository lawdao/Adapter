package example.fussen.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import example.fussen.adapter.adapter.MyAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = (ListView) findViewById(R.id.listview);

        ArrayList<String> list = new ArrayList<>();


        for (int i = 0; i < 100; i++) {
            list.add("我是第一种条目" + i);
        }

        listView.setAdapter(new MyAdapter(list));
    }
}
