package com.example.xingdadong.accountbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



public final class ThermometerView extends View {

    private static final String TAG = ThermometerView.class.getSimpleName();


    private Handler handler;

    // drawing tools
    private RectF rimRect;
    private Paint rimPaint, innerrimPaint;
    private Paint rimCirclePaint;

    private RectF faceRect;
    private Bitmap faceTexture;
    private Paint facePaint;
    private Paint rimShadowPaint;

    private RectF innerRect;

    private Paint logoPaint;
    private Bitmap logo;
    private Matrix logoMatrix;
    private float logoScale;

    private Paint sectorPaint;

    private Paint backgroundPaint;
    // end drawing tools

    private Bitmap background; // holds the cached static part

    // scale configuration
    private static  float degreesPerNick ;//= 360.0f / totalNicks;


    private int NumberOfCategory = 9;  private float total = 0;
    private float[] category = new float[NumberOfCategory];
    private float[] temp = {25.0f,65.0f,45.0f,15.0f,105.0f,70.0f,50.0f,125.0f,200.f};
    private int[] array_color =
            {
                    0x02FF6347,//1
                    0x02FFA500,//2
                    0x02FFD700,//3
                    0x029ACD32,//4
                    0x0290EE90,//5
                    0x0200FFFF,//6
                    0x0200CED1,//7
                    0x026A5ACD,//8
                    0x028A2BE2,//9
            };
    private int[] array_color2 =
            {
                    0xFFFF6347,//1
                    0xFFFFA500,//2
                    0xFFFFD700,//3
                    0xFF9ACD32,//4
                    0xFF90EE90,//5
                    0xFF00FFFF,//6
                    0xFF00CED1,//7
                    0xFF6A5ACD,//8
                    0xFF8A2BE2,//9
            };



    public ThermometerView(Context context) {
        super(context);
        init();
    }

    public ThermometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThermometerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private RotaryKnobListener mListener;

    public interface RotaryKnobListener {
        public void onKnobChanged(float arg);
    }

    public void setKnobListener(RotaryKnobListener l) {
        mListener = l;
    }

    private SensorManager getSensorManager() {
        return (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //attachToSensor();
    }

    @Override
    protected void onDetachedFromWindow() {
        //detachFromSensor();
        super.onDetachedFromWindow();
    }
/*
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superState = bundle.getParcelable("superState");
        super.onRestoreInstanceState(superState);

        handInitialized = bundle.getBoolean("handInitialized");
        handPosition = bundle.getFloat("handPosition");
        handTarget = bundle.getFloat("handTarget");
        handVelocity = bundle.getFloat("handVelocity");
        handAcceleration = bundle.getFloat("handAcceleration");
        lastHandMoveTime = bundle.getLong("lastHandMoveTime");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        Bundle state = new Bundle();
        state.putParcelable("superState", superState);
        state.putBoolean("handInitialized", handInitialized);
        state.putFloat("handPosition", handPosition);
        state.putFloat("handTarget", handTarget);
        state.putFloat("handVelocity", handVelocity);
        state.putFloat("handAcceleration", handAcceleration);
        state.putLong("lastHandMoveTime", lastHandMoveTime);
        return state;
    }
*/
    private void init() {
        handler = new Handler();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        for(int i=0; i<NumberOfCategory;i++)
        {
            category[i] = temp[i] ;
        }
        for(float x:category)
        {

            total += x;
        }

        initDrawingTools();
    }

    private void initDrawingTools() {
        rimRect = new RectF(0.1f, 0.1f, 0.9f, 0.9f);

        // the linear gradient is a bit skewed for realism
        rimPaint = new Paint();
        rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        rimPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.8f, 0.8f,
                Color.rgb(0xf0, 0xf5, 0xf0),
                Color.rgb(0x30, 0x31, 0x30),
                Shader.TileMode.MIRROR));

        innerrimPaint = new Paint();
        innerrimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        innerrimPaint.setShader(new LinearGradient(0.40f, 0.3f, 0.8f, 0.8f,
                Color.rgb(0xf0, 0xf5, 0xf0),
                Color.rgb(0x30, 0x31, 0x30),
                Shader.TileMode.CLAMP));

        //inner and outer circle paint
        rimCirclePaint = new Paint();
        rimCirclePaint.setAntiAlias(true);
        rimCirclePaint.setStyle(Paint.Style.STROKE);
        rimCirclePaint.setColor(Color.argb(0x4f, 0x33, 0x36, 0x33));
        rimCirclePaint.setStrokeWidth(0.005f);


        //begin texture
        float rimSize = 0.02f;
        faceRect = new RectF();
        faceRect.set(rimRect.left + rimSize, rimRect.top + rimSize,
                rimRect.right - rimSize, rimRect.bottom - rimSize);

        faceTexture = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.plastic);
        BitmapShader paperShader = new BitmapShader(faceTexture,
                Shader.TileMode.MIRROR,
                Shader.TileMode.MIRROR);
        Matrix paperMatrix = new Matrix();
        facePaint = new Paint();
        facePaint.setFilterBitmap(true);
        paperMatrix.setScale(1.0f / faceTexture.getWidth(),
                1.0f / faceTexture.getHeight());
        paperShader.setLocalMatrix(paperMatrix);
        facePaint.setStyle(Paint.Style.FILL);
        facePaint.setShader(paperShader);
        //end texture


        //shadow paint
        rimShadowPaint = new Paint();
        rimShadowPaint.setShader(new RadialGradient(0.5f, 0.5f, faceRect.width() / 2.0f,
                new int[]{0x00000000, 0x00000500, 0x50000500},
                new float[]{0.96f, 0.96f, 0.99f},
                Shader.TileMode.MIRROR));
        rimShadowPaint.setStyle(Paint.Style.FILL);
        //end shadow paint

        //logo paint
        logoPaint = new Paint();
        logoPaint.setFilterBitmap(true);
        logo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.w_logo2);
        logoMatrix = new Matrix();
        logoScale = (1.0f / logo.getWidth()) * 0.3f;
        logoMatrix.setScale(logoScale, logoScale);
        //end logo paint

        float innerSize = 0.2f;

        innerRect = new RectF();
        innerRect.set(rimRect.left + innerSize, rimRect.top + innerSize,
                rimRect.right - innerSize, rimRect.bottom - innerSize);

        backgroundPaint = new Paint();
        backgroundPaint.setFilterBitmap(true);

        sectorPaint = new Paint();


    }

    private void drawRim(Canvas canvas) {
        // first, draw the metallic body
        canvas.drawOval(rimRect, rimPaint);
        // now the outer rim circle
        canvas.drawOval(rimRect, rimCirclePaint);
    }

    private void drawFace(Canvas canvas) {
        //fill texture
       // canvas.drawOval(faceRect, facePaint);
        // draw the inner rim circle
        canvas.drawOval(faceRect, rimCirclePaint);
        // draw the rim shadow inside the face
        canvas.drawOval(faceRect, rimShadowPaint);
    }


   private void drawLogo(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(0.5f - logo.getWidth() * logoScale / 2.0f,
                0.5f - logo.getHeight() * logoScale / 2.0f);

        int color = 0x00000000;
        float position = 0;//getRelativeTemperaturePosition();
        if (position < 0) {
            color |= (int) ((0xf0) * -position); // blue
        } else {
            color |= ((int) ((0xf0) * position)) << 16; // red
        }
        //Log.d(TAG, "*** " + Integer.toHexString(color));
        LightingColorFilter logoFilter = new LightingColorFilter(0xff338822, color);
        logoPaint.setColorFilter(logoFilter);

        canvas.drawBitmap(logo, logoMatrix, logoPaint);
        canvas.restore();
    }

    private void drawCenterCircle(Canvas canvas) {

            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.drawCircle(0.5f, 0.5f, 0.2f, innerrimPaint);
            // draw the inner rim circle
            canvas.drawOval(innerRect, rimCirclePaint);

            canvas.restore();
    }


    private int nickToDegree(int nick) {
        int rawDegree = ((nick < (int)total / 2) ? nick : (nick - (int)total)) * 2;
        return rawDegree;
    }

    private void drawSector(Canvas canvas){


        float degree =0;
        degreesPerNick = 180.0f/total;

        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        for(int i=0; i<9;i++) {
            sectorPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

            sectorPaint.setShader(new LinearGradient(0.45f, 0.0f, 0.9f, 1.0f,
                    array_color[i],
                    array_color2[i],
                    Shader.TileMode.CLAMP));
            float sweep= (360*category[i]/total);
            canvas.drawArc(faceRect, degree,sweep, true, sectorPaint);
            degree += sweep;
        }
        canvas.restore();

    }

    private void drawBackground(Canvas canvas) {
        if (background == null) {
            Log.w(TAG, "Background not created");
        } else {
            canvas.drawBitmap(background, 0, 0, backgroundPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        float scale = (float) getWidth();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scale, scale);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        drawCenterCircle(canvas);

        canvas.restore();

        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        drawLogo(canvas);
        canvas.restore();


        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "Size changed to " + w + "x" + h);

        regenerateBackground();
    }

    private void regenerateBackground() {
        // free the old bitmap
        if (background != null) {
            background.recycle();
        }

        background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas backgroundCanvas = new Canvas(background);
        float scale = (float) getWidth();
        backgroundCanvas.scale(scale, scale);


        backgroundCanvas.save(Canvas.MATRIX_SAVE_FLAG);

        drawRim(backgroundCanvas);

        backgroundCanvas.restore();


        backgroundCanvas.save(Canvas.MATRIX_SAVE_FLAG);

        drawSector(backgroundCanvas);

        backgroundCanvas.restore();

        backgroundCanvas.save(Canvas.MATRIX_SAVE_FLAG);
        drawFace(backgroundCanvas);
        backgroundCanvas.restore();


    }
}
