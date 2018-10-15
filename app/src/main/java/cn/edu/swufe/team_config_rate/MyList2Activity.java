package cn.edu.swufe.team_config_rate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable,AdapterView.OnItemClickListener ,AdapterView.OnItemLongClickListener{
    private String TAG="mylist2";
    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 用于初始化的数据，要用显示列表汉的
    private SimpleAdapter listItemAdapter; // 适配器
    private List<HashMap<String, String>> list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        //自定义的adapter
//        MyAdapter myAdapter=new MyAdapter(this,R.layout.list_item,listItems);
//        this.setListAdapter(myAdapter);
        //调用系统的adapter
        this.setListAdapter(listItemAdapter);
        Thread t=new Thread(this);
        t.start();

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 7){
                   list1 = (List<HashMap<String, String>>)msg.obj;
                            listItemAdapter = new SimpleAdapter(MyList2Activity.this, list1,
                            R.layout.list_item, // ListItem的XML布局实现
                            new String[] { "ItemTitle", "ItemDetail" },
                            new int[] { R.id.itemTitle, R.id.itemDetail });
                    setListAdapter(listItemAdapter);
                    Log.i("handler","reset list...");
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
        //长按事件处理
        getListView().setOnItemLongClickListener(this);

    }
    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate：" + i); // 标题文字
            map.put("ItemDetail", "detail" + i); // 详情描述
            //key不能重复
            listItems.add(map);
            //往list列表里面放入map数据
        }
// 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems
                R.layout.list_item, // ListItem的XML布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
                //控件和key数据对应
        );
    }

    @Override
    public void run() {
            Log.i("thread","run.....");
            List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
            Document doc=null;
            try {
                doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
                Elements tables=doc.getElementsByTag("table");
                Element table2=tables.get(1);
                Elements tds=table2.getElementsByTag("td");
                for (int i = 0; i < tds.size(); i+=8) {
                    Element td1 = tds.get(i);
                    Element td2 = tds.get(i+5);
                    String str1 = td1.text();
                    String str2 = td2.text();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ItemTitle", str1);
                    map.put("ItemDetail", str2);
                    retList.add(map);//careful
                    Log.i("td",str1 + "=>" + str2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message msg = handler.obtainMessage(7);
            msg.obj = retList;
            handler.sendMessage(msg);
            Log.i("thread","sendMessage.....");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: parent="+parent);
        Log.i(TAG, "onItemClick: view="+view);
        Log.i(TAG, "onItemClick: position="+position);
        Log.i(TAG, "onItemClick: long="+id);
        HashMap<String,String> map= (HashMap<String, String>) getListView().getItemAtPosition(position);
        String titleStr=map.get("ItemTitle");
        String detailStr=map.get("ItemDetail");
        Log.i(TAG, "onItemClick: titleStr="+titleStr);
        Log.i(TAG, "onItemClick: detailStr="+detailStr);
        TextView title=view.findViewById(R.id.itemTitle);
        TextView detail=view.findViewById(R.id.itemDetail);
        String title2=String.valueOf(title.getText());
        String detail2=String.valueOf(detail.getText());
        Log.i(TAG, "onItemClick: title2="+title2);
        Log.i(TAG, "onItemClick: detail2="+detail2);

        //打来新的页面传入数据
        Intent rateCalc=new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateCalc);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: long is running and position="+position);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("温馨提示")
                .setMessage("确定要删除选中汇率数据吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list1.remove(position);//删除对应position的数据
                        listItemAdapter.notifyDataSetChanged();
                    }
                }
        ).setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}

