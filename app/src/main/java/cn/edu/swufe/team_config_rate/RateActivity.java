package cn.edu.swufe.team_config_rate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RateActivity extends AppCompatActivity implements Runnable{
    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    private String updateDate="";

    EditText rmb;
    TextView show;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.show);

        //将数据保存到myrate.xml
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollarRate = sharedPreferences.getFloat("dollar_rate", 0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate", 0.0f);
        wonRate = sharedPreferences.getFloat("won_rate", 0.0f);
        updateDate=sharedPreferences.getString("update_date","");

        //获取当前系统时间
        Date today=Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr=sdf.format(today);
        Log.i(TAG, "oncreate:sp dollarRate=" + dollarRate);
        Log.i(TAG, "oncreate:sp euroRate=" + euroRate);
        Log.i(TAG, "oncreate:sp wonRate=" + wonRate);
        Log.i(TAG,"oncreate:sp updateDate= "+updateDate);
        Log.i(TAG,"oncreate:sp todayStr= "+todayStr);
        //判断时间
        if(!todayStr.equals(updateDate)){
            Log.i(TAG, "onCreate: 需要更新");
            Thread t=new Thread(this);
            t.start();
        }else{
            Log.i(TAG, "onCreate: 不需要更新");
        }
        //开启子线程
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    Bundle bdl = (Bundle) msg.obj;
                    dollarRate = bdl.getFloat("dollar-rate");
                    euroRate = bdl.getFloat("euro-rate");
                    wonRate = bdl.getFloat("won-rate");
                    Log.i(TAG, "handleMessage: dollarRate:" + dollarRate);
                    Log.i(TAG, "handleMessage: euroRate:" + euroRate);
                    Log.i(TAG, "handleMessage: wonRate:" + wonRate);
                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("update_date",todayStr);
                    editor.putFloat("dollar_rate", dollarRate);
                    editor.putFloat("euro_rate", euroRate);
                    editor.putFloat("won_rate", wonRate);
                    editor.apply();
                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

    }

    public void onClick(View btn) {
        Log.i(TAG, "onClick: ");
        String str = rmb.getText().toString();
        Log.i(TAG, "onClick: get str=" + str);
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        } else {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, "onClick: r=" + r);

        if (btn.getId() == R.id.dollar) {
            show.setText(String.format("%.2f", r * dollarRate));
        } else if (btn.getId() == R.id.euro) {
            show.setText(String.format("%.2f", r * euroRate));
        } else {
            show.setText(String.format("%.2f", r * wonRate));
        }
    }

    public void openOne(View btn) {
        openConfig();
    }

    private void openConfig() {
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);
        Log.i(TAG, "openOne: dollarRate=" + dollarRate);
        Log.i(TAG, "openOne: euroRate=" + euroRate);
        Log.i(TAG, "openOne: wonRate=" + wonRate);
        startActivityForResult(config, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_set) {
            openConfig();
        }else if(item.getItemId()==R.id.open_list){
            //打开列表窗口
            Intent list = new Intent(this, MyList2Activity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar", 0.1f);
            euroRate = bundle.getFloat("key_euro", 0.1f);
            wonRate = bundle.getFloat("key_won", 0.1f);

            Log.i(TAG, "onActivityResult: dollarRate=" + dollarRate);
            Log.i(TAG, "onActivityResult: euroRate=" + euroRate);
            Log.i(TAG, "onActivityResult: wonRate=" + wonRate);

            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate", dollarRate);
            editor.putFloat("euro_rate", euroRate);
            editor.putFloat("won_rate", wonRate);
            editor.commit();
            Log.i(TAG, "onActivityResult: 数据已保存到sharedPreferences");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void run() {
        Log.i(TAG, "run: run()......");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        //获取网络数据
//        URL url = null;
//        try {
//            url = new URL("http://www.usd-cny.com/bankofchina.htm");
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            InputStream in = http.getInputStream();
//            String html = inputStream2String(in);
//            Log.i(TAG, "run:html=" + html);
//            Document doc=Jsoup.parse(html);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Bundle bundle;
        //bundle=getFromBOC();
        bundle=getFromUsdCny();
        Message msg=handler.obtainMessage(5);
        //msg.obj="hello from run()";
        msg.obj=bundle;
        handler.sendMessage(msg);

    }

    /**
     * 从backofchina中获取数据
     * @return
     */
    private Bundle getFromBOC() {
        Bundle bundle=new Bundle();
        Document doc=null;
        try {
            doc= Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element table = tables.get(0);
            Log.i(TAG, "run: table=" + table);
            //获取TD中的数据
            Elements tds = table.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                String str1 = td1.text();
                String val = td2.text();
                Log.i(TAG, "run: " + str1 + "==>" + val);
                float v = 100f / Float.parseFloat(val);
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate", v);
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate", v);
                }else if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate", v);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }


    private Bundle getFromUsdCny() {
        Bundle bundle=new Bundle();
        Document doc=null;
        try {
            doc= Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element table = tables.get(1);
            Log.i(TAG, "run: table=" + table);
            //获取TD中的数据
            Elements tds = table.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                String str1 = td1.text();
                String val = td2.text();
                Log.i(TAG, "run: " + str1 + "==>" + val);
                float v = 100f / Float.parseFloat(val);
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate", v);
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate", v);
                }else if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate", v);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

}