package com.yixin.a2048.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout;
import android.widget.Toast;

import com.yixin.a2048.activity.MainActivity;
import com.yixin.a2048.application.GameApplication;
import com.yixin.a2048.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flexible on 2016/9/1.
 */
public class GameView extends GridLayout implements View.OnTouchListener {

    private List<Point> mBlanks;
    private GameItem[][] item;
    private int[][] oldItem;
    private int lins;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private int keyNum = -1;
    private List<Integer> keyList = new ArrayList<Integer>();
    private int goalScore;

    public GameView(Context context) {
        super(context);
        initView();
    }

    public void initView() {
        removeAllViews();
        GameApplication.Score = 0;
        goalScore = GameApplication.mSp.getInt(GameApplication.KEY_GAME_GOAL, 2048);
        lins = GameApplication.mSp.getInt(GameApplication.KEY_GAME_LINES, 4);
        setColumnCount(lins);
        setRowCount(lins);
        setOnTouchListener(this);
        int w = ScreenUtils.getScreenWidth(getContext());
        GameApplication.mItemSize = w / lins;
        item = new GameItem[lins][lins];
        oldItem = new int[lins][lins];
        mBlanks = new ArrayList<Point>();
        for (int i = 0; i < lins; i++) {
            for (int j = 0; j < lins; j++) {
                item[i][j] = new GameItem(getContext(), 0);
                addView(item[i][j], GameApplication.mItemSize, GameApplication.mItemSize);
            }
        }
        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum() {
        getBlanks();
        if (mBlanks.size() > 0) {
            int sj = (int) (Math.random() * mBlanks.size());
            item[mBlanks.get(sj).x][mBlanks.get(sj).y].setTvNum(Math.random() > 0.2d ? 2 : 4);
            animationMove(item[mBlanks.get(sj).x][mBlanks.get(sj).y]);
        }
    }


    /**
     * 动画
     */

    private void animationMove(GameItem gameItem) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);
        gameItem.startAnimation(scaleAnimation);
    }

    /**
     * 保存历史记录
     */
    private void saveHistoryMatrix() {
        for (int i = 0; i < lins; i++) {
            for (int j = 0; j < lins; j++) {
                oldItem[i][j] = item[i][j].getTvNum();
            }
        }
    }

    /**
     * 获取空格
     */
    public void getBlanks() {
        mBlanks.clear();
        for (int i = 0; i < lins; i++) {
            for (int j = 0; j < lins; j++) {
                if (item[i][j].getTvNum() == 0) {
                    mBlanks.add(new Point(i, j));
                }
            }
        }
    }


    private int getDeviceDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return (int) metrics.density;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        saveHistoryMatrix();

        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = motionEvent.getX();
                startY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                endX = motionEvent.getX();
                endY = motionEvent.getY();
                int density = getDeviceDensity();
                float offsetX = endX - startX;
                float offsetY = endY - startY;
                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    if (offsetX > density * 5)
                        moveRight();
                    else if (Math.abs(offsetX) > density * 5)
                        moveLeft();
                } else {
                    if (offsetY > density * 5)
                        moveBottom();
                    else if (Math.abs(offsetY) > density * 5)
                        moveTop();
                }
                if (ifSuccess() == 0) {
                    if (GameApplication.mSp.getInt(GameApplication.KEY_HIGH_SCROE, 0) < GameApplication.Score)
                        GameApplication.mSp.edit().putInt(GameApplication.KEY_HIGH_SCROE, GameApplication.Score).commit();
                    new AlertDialog.Builder(getContext()).setTitle("茹大兄弟，你果真随我!!")
                            .setMessage("智商得到了升华!!!!")
                            .show();
                    setOnTouchListener(null);
                } else if (ifSuccess() == 1) {
                    new AlertDialog.Builder(getContext()).setTitle("你觉不觉得晚上应该多睡一会了！！")
                            .setMessage("一点都不随我了!!").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                            .show();
                    setOnTouchListener(null);
                }
                if (ifMove()) {
                    addRandomNum();
                    MainActivity.setScore();
                }
                break;
            default:
                break;

        }

        return true;
    }

    /**
     * 判断是否成功
     * <p>
     * 0：成功   1：失败  2：正常
     */
    private int ifSuccess() {
        for (int a = 0; a < lins; a++) {
            for (int b = 0; b < lins; b++) {
                if (item[a][b].getTvNum() == goalScore) {
                    return 0;
                }
            }
        }
        getBlanks();
        if (mBlanks.size() == 0) {
            for (int a = 0; a < lins; a++) {
                for (int b = 0; b < lins; b++) {
                    if (a < lins - 1) {
                        if (item[a][b].getTvNum() == item[a + 1][b].getTvNum()) {
                            return 2;
                        }
                    }

                    if (b < lins - 1) {
                        if (item[a][b].getTvNum() == item[a][b + 1].getTvNum()) {
                            return 2;
                        }
                    }
                }
            }
            return 1;

        }
        return 2;
    }

    /**
     * 判断触摸事件完成后 是否改变了布局
     */
    private boolean ifMove() {
        for (int a = 0; a < lins; a++) {
            for (int b = 0; b < lins; b++) {
                if (item[a][b].getTvNum() == oldItem[a][b]) {

                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private void moveLeft() {
        for (int i = 0; i < lins; i++) {
            for (int j = 0; j < lins; j++) {
                int cardNum = item[i][j].getTvNum();
                if (cardNum != 0) {
                    if (keyNum == -1) {
                        keyNum = cardNum;
                    } else {
                        if (keyNum == cardNum) {
                            keyList.add(keyNum * 2);
                            GameApplication.Score += keyNum * 2;
                            keyNum = -1;
                        } else {
                            keyList.add(keyNum);
                            keyNum = cardNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (keyNum != -1)
                keyList.add(keyNum);
            for (int j = 0; j < keyList.size(); j++) {
                item[i][j].setTvNum(keyList.get(j));
            }
            for (int m = keyList.size(); m < lins; m++) {
                item[i][m].setTvNum(0);
            }
            keyList.clear();
            keyNum = -1;
        }
    }

    private void moveBottom() {
        for (int i = 0; i < lins; i++) {
            for (int j = lins - 1; j >= 0; j--) {
                int cardNum = item[j][i].getTvNum();
                if (cardNum != 0) {
                    if (keyNum == -1) {
                        keyNum = cardNum;
                    } else {
                        if (keyNum == cardNum) {
                            keyList.add(keyNum * 2);
                            GameApplication.Score += keyNum * 2;
                            keyNum = -1;
                        } else {
                            keyList.add(keyNum);
                            keyNum = cardNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (keyNum != -1)
                keyList.add(keyNum);

            int index = keyList.size() - 1;
            for (int j = lins - keyList.size(); j < lins; j++) {
                item[j][i].setTvNum(keyList.get(index));
                index--;
            }

            for (int m = 0; m < lins - keyList.size(); m++) {
                item[m][i].setTvNum(0);
            }
            keyList.clear();
            keyNum = -1;

        }
    }

    private void moveTop() {
        for (int i = 0; i < lins; i++) {
            for (int j = 0; j < lins; j++) {
                int cardNum = item[j][i].getTvNum();
                if (cardNum != 0) {
                    if (keyNum == -1) {
                        keyNum = cardNum;
                    } else {
                        if (keyNum == cardNum) {
                            keyList.add(keyNum * 2);
                            GameApplication.Score += keyNum * 2;
                            keyNum = -1;
                        } else {
                            keyList.add(keyNum);
                            keyNum = cardNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (keyNum != -1)
                keyList.add(keyNum);
            for (int j = 0; j < keyList.size(); j++) {
                item[j][i].setTvNum(keyList.get(j));
            }
            for (int m = keyList.size(); m < lins; m++) {
                item[m][i].setTvNum(0);
            }
            keyList.clear();
            keyNum = -1;
        }


    }

    private void moveRight() {
        for (int i = 0; i < lins; i++) {
            for (int j = lins - 1; j >= 0; j--) {
                int cardNum = item[i][j].getTvNum();
                if (cardNum != 0) {
                    if (keyNum == -1) {
                        keyNum = cardNum;
                    } else {
                        if (keyNum == cardNum) {
                            keyList.add(keyNum * 2);
                            GameApplication.Score += keyNum * 2;
                            keyNum = -1;
                        } else {
                            keyList.add(keyNum);
                            keyNum = cardNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (keyNum != -1)
                keyList.add(keyNum);

            int index = keyList.size() - 1;
            for (int j = lins - keyList.size(); j < lins; j++) {
                item[i][j].setTvNum(keyList.get(index));
                index--;
            }
            for (int m = 0; m < lins - keyList.size(); m++) {
                item[i][m].setTvNum(0);
            }
            keyList.clear();
            keyNum = -1;

        }
    }

    /**
     * 撤销
     */
    public void recall() {
        int index = 0;
        if (oldItem == null || oldItem.length == 0 || (oldItem.length == 1 && oldItem[0].length == 0)) {
            for (int a = 0; a < lins; a++) {
                for (int b = 0; b < lins; b++) {
                    if (item[a][b].getTvNum() != oldItem[a][b]) {
                        item[a][b].setTvNum(oldItem[a][b]);
                        index = 2;
                    }
                }
            }
            if (index == 0) {
                Toast.makeText(getContext(), "只能撤销一步", Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getContext(), "还未进行过操作", Toast.LENGTH_LONG).show();
        }
    }
}

