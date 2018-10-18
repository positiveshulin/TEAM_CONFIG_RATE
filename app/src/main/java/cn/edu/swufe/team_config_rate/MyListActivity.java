package cn.edu.swufe.team_config_rate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
     ArrayAdapter adapter;
    List<String> data=new ArrayList<String>();
    private String TAG="mylist";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        for(int i=0;i<100;i++){
            data.add("item"+i);
        }
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
        //GridView gredView=findViewById(R.id.myList);//用于多栏显示数据
        ListView listView=findViewById(R.id.myList);
        //String[] list_data = {};
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);//把当前界面用adapter来管理
        listView.setEmptyView(findViewById(R.id.nodata));//界面没有数据时用于提醒用户没有数据
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: position="+position);
        Log.i(TAG, "onItemClick: parent="+parent);
        adapter.remove(parent.getItemAtPosition(position));
        adapter.notifyDataSetChanged();//可以缺省
    }
}
