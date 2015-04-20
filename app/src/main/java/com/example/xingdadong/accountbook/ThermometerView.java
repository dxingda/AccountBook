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
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;


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

    private Paint scalePaint;
    private RectF scaleRect;

    private Paint titlePaint;
    private Path titlePath;

    private Paint logoPaint;
    private Bitmap logo;
    private Matrix logoMatrix;
    private float logoScale;

    private Paint handPaint;
    private Path handPath;
    private Paint handScrewPaint;

    private Paint sectorPaint;
    private Path sectorPath;

    private Paint backgroundPaint;
    // end drawing tools

    private Bitmap background; // holds the cached static part

    // scale configuration
    private static final int totalNicks = 100;
    private static  float degreesPerNick ;//= 360.0f / totalNicks;
    private static final int centerDegree = 40; // the one in the top center (12 o'clock)
    private static final int minDegrees = -30;
    private static final int maxDegrees = 110;

    // hand dynamics -- all are angular expressed in F degrees
    private boolean handInitialized = true;
    private float handPosition = centerDegree;
    private float handTarget = centerDegree;
    private float handVelocity = 0.0f;
    private float handAcceleration = 0.0f;
    private long lastHandMoveTime = -1L;


    final Random rand = new Random();
    private int NumberOfCategory = 10;
    private float[] category = new float[NumberOfCategory];
    private float total = 0;


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

    private String getTitle() {
        return "Buck Buddy";
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

    private void init() {
        handler = new Handler();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        for(int i=0; i<NumberOfCategory;i++)
        {
            category[i] = rand.nextInt(100)+1;
            System.out.println("random category[i]= "+((Float)category[i]).toString());
        }
        for(float x:category)
        {

            total += x;
        }

        degreesPerNick = 360.0f/total;
        System.out.println("degrees per nick="+degreesPerNick);
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
        logo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.logo);
        logoMatrix = new Matrix();
        logoScale = (1.0f / logo.getWidth()) * 0.3f;
        ;
        logoMatrix.setScale(logoScale, logoScale);
        //end logo paint

        float innerSize = 0.2f;

        innerRect = new RectF();
        innerRect.set(rimRect.left + innerSize, rimRect.top + innerSize,
                rimRect.right - innerSize, rimRect.bottom - innerSize);

        backgroundPaint = new Paint();
        backgroundPaint.setFilterBitmap(true);

        sectorPaint = new Paint();
        sectorPath = new Path();


    }

    // in case there is no size specified
    private int getPreferredSize() {
        return 300;
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


    private int nickToDegree(int nick) {
        int rawDegree = ((nick < totalNicks / 2) ? nick : (nick - totalNicks)) * 2;
        int shiftedDegree = rawDegree + centerDegree;
        return shiftedDegree;
    }

    private float degreeToAngle(float degree) {
        return (degree - centerDegree) / 2.0f * degreesPerNick;
    }

    private void drawTitle(Canvas canvas) {
        String title = getTitle();
        canvas.drawTextOnPath(title, titlePath, 0.0f, 0.0f, titlePaint);
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

    private void drawSector(Canvas canvas){


        float radius = 0.4f;
        float degree =0,olddegree=0;
        int red,green,blue,nred,ngreen,nblue;

        for(int i=0; i<=9;i++) {
            red=rand.nextInt(120);
            green=rand.nextInt(105)+150;
            blue=rand.nextInt(105)+150;
            nred=(red+30)%255;
            ngreen=255;
            nblue=255;
            System.out.println("category[i]="+((Float)category[i]).toString());
            sectorPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            sectorPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.8f, 1.0f,
                    Color.rgb(red,green,blue),
                    Color.rgb(nred,ngreen,nblue),
                    Shader.TileMode.CLAMP));

            olddegree = degree;
            degree += category[i]*degreesPerNick/2;
            System.out.println("degree:"+degree+" old degree:"+olddegree);

            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.drawArc(faceRect, olddegree, degree, true, sectorPaint);
            canvas.restore();

        }
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

        drawCenterCircle(canvas);
       // drawLogo(canvas);


        canvas.restore();
/*
        if (handNeedsToMove()) {
            moveHand();
        }
*/
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


        drawRim(backgroundCanvas);
        drawSector(backgroundCanvas);

        drawFace(backgroundCanvas);

        //drawScale(backgroundCanvas);
        //drawTitle(backgroundCanvas);
    }
}
/*
	private boolean handNeedsToMove() {
		return Math.abs(handPosition - handTarget) > 0.01f;
	}
	
	private float getRelativeTemperaturePosition() {
		if (handPosition < centerDegree) {
			return - (centerDegree - handPosition) / (float) (centerDegree - minDegrees);
		} else {
			return (handPosition - centerDegree) / (float) (maxDegrees - centerDegree);
		}
	}
	
	public void setHandTarget(float temperature) {
		if (temperature < minDegrees) {
			temperature = minDegrees;
		} else if (temperature > maxDegrees) {
			temperature = maxDegrees;
		}
		handTarget = temperature;
		handInitialized = true;
		invalidate();
	}

    float previousAngle = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX()/getWidth();
        float y = event.getY()/getHeight();
        Log.d("X", Integer.toString((int) x));
        Log.d("Y", Integer.toString((int) y));

        float angle = cartesianToPolar(1-x, 1-y);
        Log.d("Angle", Integer.toString((int) angle));

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                previousAngle = angle;
                handTarget = 40f + angle/180*100;
                if (handTarget < minDegrees) {
                      handTarget= minDegrees;
                } else if (handTarget > maxDegrees) {
                      handTarget = maxDegrees;
                }

                if (mListener != null) {
                    mListener.onKnobChanged(handTarget);
                }

                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((previousAngle >  150f && angle < 0) ||
                    (previousAngle < -150f && angle > 0)){
                    break;
                } else {
                    previousAngle = angle;
                }
                handTarget = 40f + angle/180*100;
                if (handTarget < minDegrees) {
                    handTarget= minDegrees;
                } else if (handTarget > maxDegrees) {
                    handTarget = maxDegrees;
                }

                if (mListener != null) {mListener.onKnobChanged(handTarget);}

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private float cartesianToPolar(float x, float y) {
        return (float) -Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
    }
}
*/

/*
scalePaint = new Paint();
		scalePaint.setStyle(Paint.Style.STROKE);
		scalePaint.setColor(0x9f004d0f);
		scalePaint.setStrokeWidth(0.005f);
		scalePaint.setAntiAlias(true);

		scalePaint.setTextSize(0.045f);
		scalePaint.setTypeface(Typeface.SANS_SERIF);
		scalePaint.setTextScaleX(0.8f);
		scalePaint.setTextAlign(Paint.Align.CENTER);
        scalePaint.setLinearText(true); // added by Kevin to solve a display problem

		float scalePosition = 0.10f;
		scaleRect = new RectF();
		scaleRect.set(faceRect.left + scalePosition, faceRect.top + scalePosition,
					  faceRect.right - scalePosition, faceRect.bottom - scalePosition);

		titlePaint = new Paint();
		titlePaint.setColor(0xaf946109);
		titlePaint.setAntiAlias(true);
		titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
		titlePaint.setTextAlign(Paint.Align.CENTER);
		titlePaint.setTextSize(0.05f);
		titlePaint.setTextScaleX(0.8f);

		titlePath = new Path();
		titlePath.addArc(new RectF(0.24f, 0.24f, 0.76f, 0.76f), -180.0f, -180.0f);



		handPaint = new Paint();
		handPaint.setAntiAlias(true);
		handPaint.setColor(0xff392f2c);
		handPaint.setShadowLayer(0.01f, -0.005f, -0.005f, 0x7f000000);
		handPaint.setStyle(Paint.Style.FILL);

		handPath = new Path();
		handPath.moveTo(0.5f, 0.5f + 0.2f);
		handPath.lineTo(0.5f - 0.010f, 0.5f + 0.2f - 0.007f);
		handPath.lineTo(0.5f - 0.002f, 0.5f - 0.32f);
		handPath.lineTo(0.5f + 0.002f, 0.5f - 0.32f);
		handPath.lineTo(0.5f + 0.010f, 0.5f + 0.2f - 0.007f);
		handPath.lineTo(0.5f, 0.5f + 0.2f);
		handPath.addCircle(0.5f, 0.5f, 0.025f, Path.Direction.CW);

		handScrewPaint = new Paint();
		handScrewPaint.setAntiAlias(true);
		handScrewPaint.setColor(0xffffffff);
		handScrewPaint.setStyle(Paint.Style.FILL);


			private void drawScale(Canvas canvas) {
		canvas.drawOval(scaleRect, scalePaint);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);

		for (int i = 0; i < totalNicks; ++i) {
			float y1 = scaleRect.top;
			float y2 = y1 - 0.020f;

			canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

			if (i % 5 == 0) {
				int value = nickToDegree(i);

				if (value >= minDegrees && value <= maxDegrees) {
					String valueString = Integer.toString(value);
					canvas.drawText(valueString, 0.5f, y2 - 0.015f, scalePaint);
				}
			}

			canvas.rotate(degreesPerNick, 0.5f, 0.5f);
		}
		canvas.restore();
	}


		private void drawHand(Canvas canvas) {
		if (handInitialized) {
			float handAngle = degreeToAngle(handPosition);
			canvas.save(Canvas.MATRIX_SAVE_FLAG);
			canvas.rotate(handAngle, 0.5f, 0.5f);
			canvas.drawPath(handPath, handPaint);
			canvas.restore();

			canvas.drawCircle(0.5f, 0.5f, 0.01f, handScrewPaint);
		}
	}

	private void moveHand() {
		if (! handNeedsToMove()) {
			return;
		}

		if (lastHandMoveTime != -1L) {
			long currentTime = System.currentTimeMillis();
			float delta = (currentTime - lastHandMoveTime) / 1000.0f;

			float direction = Math.signum(handVelocity);
			if (Math.abs(handVelocity) < 90.0f) {
				handAcceleration = 5.0f * (handTarget - handPosition);
			} else {
				handAcceleration = 0.0f;
			}
			handPosition += handVelocity * delta;
			handVelocity += handAcceleration * delta;
			if ((handTarget - handPosition) * direction < 0.01f * direction) {
				handPosition = handTarget;
				handVelocity = 0.0f;
				handAcceleration = 0.0f;
				lastHandMoveTime = -1L;
			} else {
				lastHandMoveTime = System.currentTimeMillis();
			}
			invalidate();
		} else {
			lastHandMoveTime = System.currentTimeMillis();
			moveHand();
		}
	}






 */