package com.yixin.a2048.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yixin.a2048.R;
import com.yixin.a2048.SwipeBackLayout.SwipeBackActivityBase;
import com.yixin.a2048.SwipeBackLayout.SwipeBackActivityHelper;
import com.yixin.a2048.SwipeBackLayout.SwipeBackLayout;
import com.yixin.a2048.application.GameApplication;
import com.yixin.a2048.utils.ScreenUtils;
import com.yixin.a2048.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.yixin.a2048.R.id.toolbar;

/**
 * Created by flexibleXd on 2016/9/18.
 */

public class SzActivity extends AppCompatActivity implements View.OnClickListener, SwipeBackActivityBase {

    private TextView chooseNd;
    private SwipeBackActivityHelper mHelper;
    private TextView chooseHigh;
    private int w;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sz);
        initView();
    }

    private void initView() {
        Toolbar toolBar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        chooseNd = (TextView) findViewById(R.id.choose_difficult);
        chooseNd.setText(GameApplication.mSp.getInt(GameApplication.KEY_GAME_LINES, 4) + "*" + GameApplication.mSp.getInt(GameApplication.KEY_GAME_LINES, 2048));
        chooseNd.setOnClickListener(this);
        chooseHigh = (TextView) findViewById(R.id.choose_high);
        chooseHigh.setOnClickListener(this);
        chooseHigh.setText(GameApplication.mSp.getInt(GameApplication.KEY_GAME_GOAL, 2048) + "");
        w = ScreenUtils.getScreenWidth(this);
        btn = (Button) findViewById(R.id.btn_ok);
        btn.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_difficult:

//                int h = ScreenUtils.getScreenHeight(this);
                ListView lv = new ListView(this);
                lv.setBackgroundColor(Color.WHITE);
                List<String> data = new ArrayList();
                data.add("3*3");
                data.add("4*4");
                data.add("5*5");
                ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
                lv.setAdapter(adapter);

                final PopupWindow difficult = new PopupWindow();
                difficult.setContentView(lv);
                difficult.setHeight(w / 3);
                difficult.setWidth(w / 3);
                difficult.setBackgroundDrawable(null);
                difficult.setOutsideTouchable(true);
                difficult.showAsDropDown(chooseNd);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String data = parent.getItemAtPosition(position).toString();
                        chooseNd.setText(data);
                        GameApplication.mGameLines = Integer.valueOf(data.substring(0, 1));
                        GameApplication.mSp.edit().putInt(GameApplication.KEY_GAME_LINES, Integer.valueOf(data.substring(0, 1))).commit();
                        difficult.dismiss();
                    }
                });
                break;

            case R.id.choose_high:

//                int h = ScreenUtils.getScreenHeight(this);
                ListView lv1 = new ListView(this);
                lv1.setBackgroundColor(Color.WHITE);
                List<String> data1 = new ArrayList();
                data1.add("2048");
                data1.add("4096");
                data1.add("8192");
                ListAdapter adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data1);
                lv1.setAdapter(adapter1);

                final PopupWindow high = new PopupWindow();
                high.setContentView(lv1);
                high.setHeight(w / 3);
                high.setWidth(w / 3);
                high.setBackgroundDrawable(null);
                high.setOutsideTouchable(true);
                high.showAsDropDown(chooseHigh);
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String data = parent.getItemAtPosition(position).toString();
                        chooseHigh.setText(data);
                        GameApplication.mGameGoal = Integer.valueOf(data);
                        GameApplication.mSp.edit().putInt(GameApplication.KEY_GAME_GOAL, Integer.valueOf(data)).commit();
                        high.dismiss();
                    }
                });
                break;
            case R.id.btn_ok:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("key", "1");
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
