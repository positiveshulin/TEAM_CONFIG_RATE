package cn.edu.swufe.team_config_rate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        List<String> list1=new ArrayList<String>();
        for(int i=0;i<100;i++){
            list1.add("item"+i);
        }
        //GridView gredView=findViewById(R.id.myList);//用于多栏显示数据
        ListView listView=findViewById(R.id.myList);
        String[] list_data = {};
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
        listView.setAdapter(adapter);//把当前界面用adapter来管理
        listView.setEmptyView(findViewById(R.id.nodata));//界面没有数据时用于提醒用户没有数据
    }
}
