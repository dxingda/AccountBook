package com.example.xingdadong.accountbook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Date;

/**
 * Created by Dios on 4/22/2015.
 */
public class WeeklyChartView extends View {

    private long dayInfo;
    Paint paint = new Paint();

    public WeeklyChartView(Context context){
        super(context);
        dayInfo = System.currentTimeMillis();
    }

    public WeeklyChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dayInfo = System.currentTimeMillis();
    }

    public WeeklyChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        dayInfo = System.currentTimeMillis();
    }



    @Override
    public void onDraw(Canvas canvas){

        float scaleX=getWidth();
        float scaleY=getHeight();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scaleX,scaleY);
        drawChart(canvas);
        drawData(canvas, dayInfo);
        canvas.restore();
    }

    void drawChart(Canvas canvas){

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        paint.setStrokeWidth(0.0f);
        paint.setColor(0xFFF1F1F1);
        canvas.drawRect(0.003f, 0.01f, 0.997f, 0.99f, paint );
        paint.setColor(Color.BLACK);
        canvas.drawLine(0.02f,0.9f,0.98f,0.9f,paint);

        canvas.drawLine(0.0714f, 0.88f, 0.0714f, 0.91f, paint);
        canvas.drawLine(0.2142f,0.88f,0.2142f,0.91f,paint);
        canvas.drawLine(0.357f,0.88f,0.357f,0.91f,paint);
        canvas.drawLine(0.4998f,0.88f,0.4998f,0.91f,paint);
        canvas.drawLine(0.6426f,0.88f,0.6426f,0.91f,paint);
        canvas.drawLine(0.7854f,0.88f,0.7854f,0.91f,paint);
        canvas.drawLine(0.9282f,0.88f,0.9282f,0.91f,paint);

    }

    void drawData(Canvas canvas, long date){
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        paint.setStrokeWidth(0.01f);
        int days = Activity_ViewPage.data.getSize();
        if (days==0){
            canvas.drawLine(0.0714f,0.9f,0.9282f,0.9f,paint);
        }else {
            paint.setColor(Color.MAGENTA);
            float max = 0;
            float sum;

            int k;
            int i;

            for (long j = date; j < date+7 * 24 * 60 * 60 * 1000; j+= 24 * 60 * 60 * 1000) {
                sum = 0;
                for (k=0; k < days; k++) {
                    if ((long) Activity_ViewPage.data.getItem(k).get("date") >= j &&
                            (long) Activity_ViewPage.data.getItem(k).get("date") < j + 1 * 24 * 60 * 60 * 1000) {
                        if ((float) Activity_ViewPage.data.getItem(k).get("amount") > 0) {
                            sum += (float) Activity_ViewPage.data.getItem(k).get("amount");
                        }
                    }
                }
                if (max < sum)
                    max = sum;
            }


            float total;
            float xStart = 0.0714f;
            float xEnd = 0.0714f;
            float yStart = 0.9f;
            float yEnd = 0.9f;
            for (long j = date; j <date+ 7 * 24 * 60 * 60 * 1000; j = j + 1 * 24 * 60 * 60 * 1000) {
                total = 0;
                yEnd = 0.9f;
                for (i=0; i < days; i++) {
                    if ((long) Activity_ViewPage.data.getItem(i).get("date") >= j && (long) Activity_ViewPage.data.getItem(i).get("date") < j + 1 * 24 * 60 * 60 * 1000) {
                        if ((float) Activity_ViewPage.data.getItem(i).get("amount") > 0) {
                            total += (float) Activity_ViewPage.data.getItem(i).get("amount");
                        }
                    }
                }
                if ((total / max) > 0) {
                    if (j == date) {
                        yStart = 0.9f - (total / max * 0.8f);
                    }
                    yEnd = 0.9f - (total / max * 0.8f);
                }
                canvas.drawLine(xStart, yStart, xEnd, yEnd, paint);
                xStart = xEnd;
                xEnd += 0.1428f;
                yStart = yEnd;
            }
        }
    }

    public void setDay(Date date){
        dayInfo = date.getTime();
        invalidate();
    }

    public long getDay() { return dayInfo; }

}
