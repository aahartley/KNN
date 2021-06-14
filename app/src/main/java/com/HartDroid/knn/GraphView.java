package com.HartDroid.knn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GraphView extends View {
    Paint paint = new Paint();
    Paint paint2 = new Paint();
    boolean erase = false;
    boolean drawSlope = false;
    List<People> dataset = new ArrayList<>();
    double smallestX = 0;
    double largestX = 0;
    double smallestY = 0;
    People people;




    public GraphView(Context context) {
        super(context);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (erase) {
            canvas.drawColor(Color.WHITE);

            drawSlope = false;
            smallestX = 0;
            smallestY = 0;
            erase(false);
        } else {
            //canvas.drawColor(Color.WHITE);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(5.0f);
            paint2.setColor(Color.RED);
            paint2.setStrokeWidth(4.0f);
            for (People p : dataset) {
                System.out.println("drawing dots" + p.toString());
                System.out.println((getWidth() / 2.0f + p.getAge() ));
                canvas.drawCircle((float) (getWidth() / 2.0f + p.getClasses()*40 ), (float) (getHeight() / 2.0f - p.getAge()*40 ), 10.0f, paint2);
            }
            if(people!=null){
                canvas.drawCircle((float) (getWidth() / 2.0f + people.getClasses()*40 ), (float) (getHeight() / 2.0f - people.getAge()*40 ), 10.0f, paint);
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
        }
    }

    protected void setList(List<People> dataset) {
        this.dataset = dataset;
        invalidate();

    }
    protected void setQuery(People people){
        this.people = people;
        invalidate();
    }




    protected void erase(boolean erase) {
        this.erase = erase;
        invalidate();
    }
}
