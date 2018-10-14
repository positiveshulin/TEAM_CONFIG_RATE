package cn.edu.swufe.team_config_rate;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.edu.swufe.team_config_rate.R;

public class TeamActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "TeamActivity";
    TextView FinalAScore,FinalBScore;//显示得分
    Button btn0,btn1,btn2,btn3,btn12,btn22,btn32;//BUTTON
    int AScore=0,BScore=0;//分数累计
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        btn0= (Button) findViewById(R.id.btn0);
        btn1= (Button) findViewById(R.id.btn1);
        btn2= (Button) findViewById(R.id.btn2);
        btn3= (Button) findViewById(R.id.btn3);
        btn12= (Button) findViewById(R.id.btn12);
        btn22= (Button) findViewById(R.id.btn22);
        btn32= (Button) findViewById(R.id.btn32);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn12.setOnClickListener(this);
        btn22.setOnClickListener(this);
        btn32.setOnClickListener(this);
        FinalAScore= (TextView) findViewById(R.id.score);
        FinalBScore= (TextView) findViewById(R.id.score2);

    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn0){
            showAScore(0);
            AScore=0;
            showBScore(0);
            BScore=0;

        }
        if(v.getId()==R.id.btn1) addAScore(1);
        else if(v.getId()==R.id.btn2) addAScore(2);
        else if(v.getId()==R.id.btn3) addAScore(3);
        else if(v.getId()==R.id.btn12) addBScore(1);
        else if(v.getId()==R.id.btn22) addBScore(2);
        else if(v.getId()==R.id.btn32)  addBScore(3);

    }
    private void addAScore(int newScore){
        AScore=AScore+newScore;
        showAScore(AScore);
    }
    private void addBScore(int newScore){
        BScore=BScore+newScore;
        showBScore(BScore);
    }
    private void showAScore(int totalScore){
        FinalAScore.setText(String.valueOf(totalScore));
    }
    private void showBScore(int totalScore){
        FinalBScore.setText(String.valueOf(totalScore));
    }


    //activity 生命周期
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea = ((TextView)findViewById(R.id.score)).getText().toString();
        String scoreb = ((TextView)findViewById(R.id.score2)).getText().toString();
        Log.i(TAG, "onSaveInstanceState: ");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");
        Log.i(TAG, "onRestoreInstanceState: ");
        ((TextView)findViewById(R.id.score)).setText(scorea);
        ((TextView)findViewById(R.id.score2)).setText(scoreb);
    }

}
