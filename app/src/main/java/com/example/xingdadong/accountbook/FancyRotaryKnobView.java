package com.example.xingdadong.accountbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by kevin on 7/5/2014.
 */
public class FancyRotaryKnobView extends ImageView {


    private float theta_old=0f;

    private RotaryKnobListener listener;
    private Bitmap background;

    private float startAngle = 60.0f;
    private float stopAngle = 300.0f;
    private float offAngle = startAngle - 15.0f;

    private float angle = offAngle;

    private Paint lightOnPaint, lightOffPaint, mPaint;
    private Paint rimPaint, rimCirclePaint;
    private RectF rimRect, rimRect_inner;

    public interface RotaryKnobListener {
        public void onKnobChanged(int arg);
    }

    public void setKnobListener(RotaryKnobListener l )
    {
        listener = l;
    }

    public FancyRotaryKnobView(Context context) {
        super(context);
        initialize();
    }

    public FancyRotaryKnobView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    public FancyRotaryKnobView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initialize();
    }


    public void initialize()
    {
        // This layer setting is very important, or some drawing may not show up
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        float space = 0.02f;

        rimRect = new RectF(0.2f, 0.2f, 0.8f, 0.8f);
        rimRect_inner = new RectF(0.2f + space, 0.2f + space, 0.8f - space, 0.8f - space);

        rimPaint = new Paint();
        rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        int[] colors = {Color.rgb(0xc0, 0xc5, 0xc0),
                Color.rgb(0xf0, 0xf5, 0xf0), Color.rgb(0xc0, 0xc5, 0xc0),
                Color.rgb(0x40, 0x41, 0x40), Color.rgb(0xc0, 0xc5, 0xc0),
                Color.rgb(0xf0, 0xf5, 0xf0), Color.rgb(0xc0, 0xc5, 0xc0),
                Color.rgb(0x40, 0x41, 0x40), Color.rgb(0xc0, 0xc5, 0xc0),
        };
        float[] positions = {0.0f, 0.125f, 0.25f, 0.375f, 0.5f, 0.625f, 0.75f, 0.875f, 1.0f};
       // rimPaint.setShader(new SweepGradient(0.5f, 0.5f, colors, positions));


        rimPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f,
                Color.rgb(0xf0, 0xf5, 0xf0),
                Color.rgb(0x30, 0x31, 0x30),
                Shader.TileMode.CLAMP));



        rimCirclePaint = new Paint();
        rimCirclePaint.setAntiAlias(true);
        rimCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        int[] colors_inner = {Color.rgb(0xb0, 0xb5, 0xb0),
                Color.rgb(0xe0, 0xe5, 0xe0), Color.rgb(0xb0, 0xb5, 0xb0),
                Color.rgb(0x40, 0x41, 0x40), Color.rgb(0xb0, 0xb5, 0xb0),
                Color.rgb(0xe0, 0xe5, 0xe0), Color.rgb(0xb0, 0xb5, 0xb0),
                Color.rgb(0x40, 0x41, 0x40), Color.rgb(0xb0, 0xb5, 0xb0),
        };
        float[] positions_inner = {0.0f, 0.125f, 0.25f, 0.375f, 0.5f, 0.625f, 0.75f, 0.875f, 1.0f};
        rimCirclePaint.setShader(new SweepGradient(0.5f, 0.5f, colors_inner, positions_inner));
        //rimCirclePaint.setStyle(Paint.Style.STROKE);
        //rimCirclePaint.setColor(Color.GRAY);
        //rimCirclePaint.setStrokeWidth(0.005f);


        lightOnPaint = new Paint();
        lightOnPaint.setStyle(Paint.Style.STROKE);
        lightOnPaint.setColor(Color.GREEN);
        lightOnPaint.setStrokeWidth(0.02f);
        lightOnPaint.setAntiAlias(true);
        lightOnPaint.setTextSize(0.025f);
        lightOnPaint.setTypeface(Typeface.SANS_SERIF);
        lightOnPaint.setTextAlign(Paint.Align.CENTER);


        lightOffPaint = new Paint();
        lightOffPaint.setStyle(Paint.Style.STROKE);
        lightOffPaint.setColor(Color.GRAY);
        lightOffPaint.setStrokeWidth(0.02f);
        lightOffPaint.setAntiAlias(true);
        lightOffPaint.setTextSize(0.025f);
        lightOffPaint.setTypeface(Typeface.SANS_SERIF);
        lightOffPaint.setTextAlign(Paint.Align.CENTER);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(40);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setLinearText(true);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 800;
        int desiredHeight = 800;
        int size;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int chosenWidth = chooseDimension(widthMode, widthSize, desiredWidth);
        int chosenHeight = chooseDimension(heightMode, heightSize, desiredHeight);

        size  = Math.min(chosenWidth, chosenHeight);

        Log.d("size", Integer.toString(size));
        setMeasuredDimension(size, size);
    }

    private int chooseDimension(int mode, int size, int desiredSize) {
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            return size;
        } else { // (mode == MeasureSpec.UNSPECIFIED)
            return desiredSize;
        }
    }


    private float getTheta(float x, float y)
    {
        float sx = x - (getWidth() / 2.0f);
        float sy = y - (getHeight() / 2.0f);

        float length = (float)Math.sqrt( sx*sx + sy*sy);
        float nx = sx / length;
        float ny = sy / length;
        float theta = (float)Math.atan2( ny, nx );

        final float rad2deg = (float)(180.0/Math.PI);
        float thetaDeg = theta*rad2deg;



        // thetaDeg is in the range of (-180, 180)
        // Use the (0, -1) as the starting angle
        //if (theta <= 90) return theta + 270.0f;
        //else return theta + 270.0f - 360.0f;

        return (thetaDeg < 0) ? thetaDeg + 360.0f : thetaDeg;
    }



    private void notifyListener(int arg)
    {
        if (null!=listener)
            listener.onKnobChanged(arg);
    }

    @Override
    protected void onDraw(Canvas c) {
        int width = getWidth();
        int height = getHeight();
        mPaint.setTextSize(width*0.02f*2);
        c.drawText("Off", width*0.25f, height*0.78f, mPaint);

        Matrix matrix = new Matrix();
        matrix.setRotate(angle, width/2, height/2);
        c.drawBitmap(background, matrix, new Paint());
        c.scale(width, width);

        drawLights(c);

        super.onDraw(c);
    }


    final private int DEGREEPERLIGHT = 15;

    private void drawLights(Canvas canvas){
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        int total = (int) (stopAngle - startAngle)/DEGREEPERLIGHT ;
        canvas.rotate(startAngle, 0.5f, 0.5f);
        float current = startAngle;
        for (int i=0; i <= total; i++ ){
            if (angle >= current) {
                canvas.drawLine(0.5f, 0.90f, 0.5f, 0.85f, lightOnPaint);
            }
            else {
                canvas.drawLine(0.5f, 0.90f, 0.5f, 0.85f, lightOffPaint);
            }
            canvas.rotate(DEGREEPERLIGHT, 0.5f, 0.5f);
            current += DEGREEPERLIGHT;
        }

        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        regenerateBackground();
    }

    private void regenerateBackground() {
        float width = (float) getWidth();
        float height = (float) getHeight();

        background = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas backgroundCanvas = new Canvas(background);

        float scale = (float) getWidth();
        backgroundCanvas.scale(scale, scale);


        Bitmap image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher);
        backgroundCanvas.drawBitmap(image, null, rimRect, new Paint());

        drawRim(backgroundCanvas);
        drawIndicator(backgroundCanvas);
    }

    private void drawRim(Canvas canvas){
        canvas.drawOval(rimRect, rimPaint);
        canvas.drawOval(rimRect_inner, rimCirclePaint);
    }

    private void drawIndicator(Canvas canvas){
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(0.0f , 0.5f, 0.5f);
        canvas.drawLine(0.5f, 0.78f, 0.5f, 0.73f, lightOnPaint);
        canvas.restore();
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float theta = getTheta(x,y);



        switch(event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                theta_old = theta;
                break;
            case MotionEvent.ACTION_MOVE:
                float delta_theta = theta - theta_old;
                theta_old = theta;
                int direction = (delta_theta > 0) ? 1 : -1;
                angle += 3*direction;

                if (angle < 0) angle = angle + 360;
                angle -= ((int)(angle /360)) * 360;

                angle = Math.min(angle, stopAngle);
                angle = Math.max(angle, offAngle);


                Log.d("Angle", Float.toString(angle));

                invalidate();
                notifyListener((int) angle);
                break;
        }
        return true;
    }

}
