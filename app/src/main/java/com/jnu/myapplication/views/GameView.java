package com.jnu.myapplication.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.jnu.myapplication.R;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private float touchedX;
    private float touchedY;
    private boolean isTouched=false;
    private int ball=0;
    Bitmap court = BitmapFactory.decodeResource(getResources(), R.drawable.court);

    public GameView(Context context) {
        super(context);
        initView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(MotionEvent.ACTION_UP==event.getAction())
        {
            touchedX = event.getRawX();
            touchedY = event.getRawY();
            Log.i("testtouched",""+touchedX);
            isTouched = true;
        }
        Log.i("test","move action");
        return true;
    }

    private void initView()
    {
        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);
    }

    private SurfaceHolder surfaceHolder;
    private DrawThread drawThread=null;

    private ArrayList<Spriter> spriterArrayList=new ArrayList<>();
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        for(int i=0;i<10;++i)
        {
            Spriter spriter=new Spriter(this.getContext());
            spriter.setX(i*50+130);
            spriter.setY(50);
            spriter.setDirection((float) (Math.PI*0.5));
            spriterArrayList.add(spriter);
        }

        drawThread=new DrawThread();
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        drawThread.stopThread();
    }


    class DrawThread extends Thread {
        private boolean isDrawing=true;

        public void stopThread()
        {
            isDrawing=false;

            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            int save=0;
            int score=0;
            while(isDrawing)
            {
                Canvas canvas =null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawBitmap(court,0f,0f,new Paint());

                    if(isTouched) {
                        float tempX = touchedX;
                        float tempY = touchedY;
                        isTouched = false;
                        Spriter oldspriter = spriterArrayList.get(ball);
                        if(oldspriter.isTouched(tempX, tempY)&& (tempY>=400)) {
                            save++;
                            //还原
                            oldspriter.setY(50);
                            ball=(int)(Math.random()*5);
                        }
                    }
                    Spriter spriter=spriterArrayList.get(ball);
                    spriter.move(canvas.getHeight(), canvas.getWidth());
                    spriter.draw(canvas);

                    if (spriter.y>=770) {
                        score++;
                        Spriter oldspriter = spriterArrayList.get(ball);
                        oldspriter.setY(50);
                        ball=(int)(Math.random()*5);
                    }

                    Paint textPaint = new Paint();
                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(60);
                    canvas.drawText("        GoalKeeper Game",36,80,textPaint);
                    canvas.drawText("  Click the football to save",36,140,textPaint);
                    textPaint.setTextSize(40);
                    canvas.drawText("Saves: "+save,40,200,textPaint);
                    canvas.drawText("Opponent Scores:"+score,350,200,textPaint);

//                    for (Spriter spriter: spriterArrayList) {
//                        spriter.move(canvas.getHeight(), canvas.getWidth());
//                    }
//                    for (Spriter spriter: spriterArrayList) {
//                        spriter.draw(canvas);
//                    }
                }
                catch(Exception e)
                {

                }
                finally {
                    if(null!=canvas)surfaceHolder.unlockCanvasAndPost(canvas);
                }

                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //drawing
            }
        }
    }
}