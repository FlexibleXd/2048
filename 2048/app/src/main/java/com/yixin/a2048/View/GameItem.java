package com.yixin.a2048.View;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yixin.a2048.application.GameApplication;

/**
 * Created by flexible on 2016/9/1.
 */
public class GameItem extends FrameLayout {
    public int getTvNum() {
        return tvNum;
    }


    public int tvNum;
    private TextView tv;

    public GameItem(Context context, int cardShowNum) {
        super(context);
        this.tvNum = cardShowNum;
        initItem();
    }

    private void initItem() {
//        setBackgroundColor(Color.GRAY);
        tv = new TextView(getContext());
        setTvNum(tvNum);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(Color.parseColor("#CDC5BF"));
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(8,8,8,8);
        addView(tv, lp);
        int LinesNum = GameApplication.mGameLines;
        switch (LinesNum) {
            case 4:
                tv.setTextSize(20);
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    public void setTvNum(int tvNum) {
        this.tvNum = tvNum;
        if (tvNum == 0) {
            tv.setText("");
        } else
            tv.setText("" + tvNum);
        // 设置背景颜色
        switch (tvNum) {
            case 0:
                tv.setBackgroundColor(Color.parseColor("#CDC5BF"));
                break;
            case 2:
                tv.setBackgroundColor(0xffeee5db);
                break;
            case 4:
                tv.setBackgroundColor(0xffeee0ca);
                break;
            case 8:
                tv.setBackgroundColor(0xfff2c17a);
                break;
            case 16:
                tv.setBackgroundColor(0xfff59667);
                break;
            case 32:
                tv.setBackgroundColor(0xfff68c6f);
                break;
            case 64:
                tv.setBackgroundColor(0xfff66e3c);
                break;
            case 128:
                tv.setBackgroundColor(0xffedcf74);
                break;
            case 256:
                tv.setBackgroundColor(0xffedcc64);
                break;
            case 512:
                tv.setBackgroundColor(0xffedc854);
                break;
            case 1024:
                tv.setBackgroundColor(0xffedc54f);
                break;
            case 2048:
                tv.setBackgroundColor(0xffedc32e);
                break;
            default:
                tv.setBackgroundColor(0xff3c4a34);
                break;

        }

    }

   public GameItem getItemView(){
       return  this;
   }
}
