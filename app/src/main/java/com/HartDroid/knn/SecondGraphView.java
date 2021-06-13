package com.HartDroid.knn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SecondGraphView extends View {

    Paint paint = new Paint();
    Paint paint2= new Paint();
    private boolean erase=false;
    private boolean drawCluster=false;
    private double smallestX=0;
    private double largestX=0;
    private double smallestY=0;
    private double largestY=0;
    private double x=0;
    private double y=0;
    private double touchX=0;
    private double touchY=0;
    private boolean touch=false;
    Touch t;
    Touch guess;
    List<Touch> touches = new ArrayList<>();
    List<Float> xs = new ArrayList<>();
    List<Float> ys = new ArrayList<>();
    List<Touch> cluster1= new ArrayList<>();
    List<Touch> cluster2 = new ArrayList<>();
    List<Touch> cluster3 = new ArrayList<>();

    public SecondGraphView(Context context) {
        super(context);
    }

    public SecondGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SecondGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                Touch t = new Touch(event.getX(),event.getY());
                System.out.println(t.x +" "+t.y);
                xs.add(event.getX());
                ys.add(event.getY());
                touches.add(t);
                touch=true;
                SecondActivity.tV5.setText(MessageFormat.format("Number of dots: {0}\nX: {1} Y: {2}", xs.size(), event.getX()-getWidth()/2.0f, getHeight()/2.0f-event.getY()));
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (erase){
            canvas.drawColor(Color.WHITE);
            touches.clear();
            t=null;
            guess=null;
            xs.clear();
            ys.clear();
            cluster1.clear();
            cluster2.clear();
            cluster3.clear();
            drawCluster=false;
            smallestX=0;
            smallestY=0;
            canvas.drawLine(0, getHeight() / 2.0f, getWidth(), getHeight() / 2.0f, paint);
            canvas.drawLine(0,getHeight()/2.0f,25,getHeight()/2.0f+25,paint);
            canvas.drawLine(0,getHeight()/2.0f,25,getHeight()/2.0f-25,paint);

            canvas.drawLine(getWidth(),getHeight()/2.0f,getWidth()-25,getHeight()/2.0f+25,paint);
            canvas.drawLine(getWidth(),getHeight()/2.0f,getWidth()-25,getHeight()/2.0f-25,paint);

            canvas.drawLine(getWidth() / 2.0f, 0, getWidth() / 2.0f, getHeight(), paint);
            canvas.drawLine(getWidth()/2.0f-25,25,getWidth()/2.0f,0,paint);
            canvas.drawLine(getWidth()/2.0f+25,25,getWidth()/2.0f,0,paint);
            canvas.drawLine(getWidth()/2.0f-25,getHeight()-25,getWidth()/2.0f,getHeight(),paint);
            canvas.drawLine(getWidth()/2.0f+25,getHeight()-25,getWidth()/2.0f,getHeight(),paint);
            paint2.setTextSize(50.0f);
            paint2.setColor(Color.RED);
            canvas.drawText("X",getWidth()-30,getHeight()/2.0f-30,paint2);
            canvas.drawText("Y",getWidth()/2.0f+30,30,paint2);
            erase(false);

        }
        else {
            //canvas.drawColor(Color.WHITE);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(5.0f);
            paint2.setColor(Color.RED);
            paint2.setStrokeWidth(4.0f);


            if(touches!=null){
                for(Touch t: touches) {
                    canvas.drawCircle((float)t.x, (float) t.y, 10.0f, paint2);
                }
            }
            if(drawCluster) {
                for (Touch t : cluster1) {
                    paint2.setColor(Color.BLUE);
                    System.out.println("drawing red dots" + t.toString());
                    canvas.drawCircle((float) (t.getX()), (float) (t.getY()), 10.0f, paint2);
                }
                for (Touch t : cluster2) {
                    paint2.setColor(Color.RED);
                    System.out.println("drawing blue dots" + t.toString());
                    canvas.drawCircle((float) (t.getX()), (float) (t.getY()), 10.0f, paint2);
                }
                if (!cluster3.isEmpty()) {
                    for (Touch t : cluster3) {
                        paint2.setColor(Color.GREEN);
                        System.out.println("drawing green dots" + t.toString());
                        canvas.drawCircle((float) (t.getX()), (float) (t.getY()), 10.0f, paint2);
                    }
                }
            }
            if(t!=null){
                paint2.setColor(Color.BLACK);
                canvas.drawCircle((float)t.x, (float) t.y, 10.0f, paint2);
                paint2.setColor(Color.RED);

            }
            if(guess!=null){
                paint2.setColor(Color.BLACK);
                canvas.drawCircle((float) (getWidth() / 2.0f + guess.getX() ), (float) (getHeight() / 2.0f - guess.getY() ), 10.0f, paint2);
                paint2.setColor(Color.RED);
            }

            canvas.drawLine(0, getHeight() / 2.0f, getWidth(), getHeight() / 2.0f, paint);
            canvas.drawLine(0,getHeight()/2.0f,25,getHeight()/2.0f+25,paint);
            canvas.drawLine(0,getHeight()/2.0f,25,getHeight()/2.0f-25,paint);

            canvas.drawLine(getWidth(),getHeight()/2.0f,getWidth()-25,getHeight()/2.0f+25,paint);
            canvas.drawLine(getWidth(),getHeight()/2.0f,getWidth()-25,getHeight()/2.0f-25,paint);

            canvas.drawLine(getWidth() / 2.0f, 0, getWidth() / 2.0f, getHeight(), paint);
            canvas.drawLine(getWidth()/2.0f-25,25,getWidth()/2.0f,0,paint);
            canvas.drawLine(getWidth()/2.0f+25,25,getWidth()/2.0f,0,paint);
            canvas.drawLine(getWidth()/2.0f-25,getHeight()-25,getWidth()/2.0f,getHeight(),paint);
            canvas.drawLine(getWidth()/2.0f+25,getHeight()-25,getWidth()/2.0f,getHeight(),paint);
            paint2.setTextSize(50.0f);
            paint2.setColor(Color.RED);
            canvas.drawText("X",getWidth()-30,getHeight()/2.0f-30,paint2);
            canvas.drawText("Y",getWidth()/2.0f+30,30,paint2);



            // System.out.println(getWidth() + " " + getHeight());
        }
    }


    protected void erase(boolean erase){
        this.erase = erase;
        invalidate();
    }
    protected  List<Touch> getTouches(){
        return touches;
    }
    protected void setCluster1List(List<Touch> cluster1){
        this.cluster1 = cluster1;
        invalidate();
    }
    protected void setCluster2List(List<Touch> cluster2){
        this.cluster2 = cluster2;
        invalidate();
    }

    protected void setDrawCluster(){
        this.drawCluster = true;
        invalidate();
    }
    protected Touch getCluster1(){
        return touches.get(0);
    }
    protected Touch getCluster2(){
        return touches.get(1);
    }
    protected void setQuery(Touch t){
        this.t = t;
        invalidate();
    }
    protected void setGuess(Touch guess){this.guess=guess;invalidate();}





}
class Touch {
    double x;
    double y;
    double distance;
    String label;
    public Touch(double x, double y){
        this.x=x;
        this.y=y;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y=y;
    }
    public void setDistance(double distance){
        this.distance = distance;
    }
    public double getDistance(){return distance;}
    public void setLabel(String label){this.label=label;}
    public String getLabel(){return label;}
    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", distance="+distance+
                '}';
    }

}

