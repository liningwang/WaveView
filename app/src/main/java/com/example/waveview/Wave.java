package com.example.waveview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Wave extends View {

	Paint mPaint;
	Path mPath;
	float waveY = 0;
	int width;
	int height;
	float ctrlX;
	float ctrlY;
	boolean arivW = false;
	int rippleTop = 10;
	int count;
	Canvas drawCanvas;
	Canvas drawCanvas1;
	Bitmap mBitmap;
	Bitmap mBitmap1;
	float WaveBottom;
	boolean waveChange;
	int totalProgress;
	int CurProgress;
	int step;
	Handler han = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (waveChange) {
				waveChange = false;
			} else {
				waveChange = true;
			}
			invalidate();
			// sendEmptyMessageDelayed(123, 2000);
		};
	};

	public Wave(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPaint = new Paint();

		mPath = new Path();
		waveY = 0;
		// ctrlX = 60;
		// ctrlY = waveY - 60f;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		count = (int) Math.ceil((width * 1.0f) / 40);

		drawCanvas = new Canvas();
		drawCanvas1 = new Canvas();
		waveY = (height / 2) + (width / 2);
		WaveBottom = waveY;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		// width = w + 80;
		// height = h;
		// count = (int)Math.ceil((width*1.0f)/40);
	}

	public void setTotal(int progress) {
		totalProgress = progress;
	}
	public void setCurrent(int progress) {
		CurProgress = progress;
		invalidate();
	}
	@SuppressLint({ "DrawAllocation", "NewApi" })
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(CurProgress >= totalProgress) {
			CurProgress = totalProgress;
		}
		step = (int)(CurProgress * 1.0f / totalProgress * width);
		Log.d("wang","step is " + step);
		canvas.drawColor(Color.WHITE);
		int src = canvas.saveLayer(0, 0, width, height, null);
		mPaint.setAntiAlias(true);
		
		canvas.drawColor(Color.GREEN);
		mPath.moveTo(0, WaveBottom);
		// mPath.quadTo(ctrlX, ctrlY, width + 50, waveY);
		mPath.lineTo(width, WaveBottom);
		mPath.lineTo(width, waveY);
		if ((step % 2) != 0) {
			for (int i = 0; i <= count; i++) {
				mPath.rQuadTo(-10, 4, -20, 0);
				mPath.rQuadTo(-10, -4, -20, 0);
			}
		} else {
			for (int i = 0; i <= count; i++) {
				mPath.rQuadTo(-10, -4, -20, 0);
				mPath.rQuadTo(-10, 4, -20, 0);
			}
		}
		mPath.close();
		mPaint.setColor(Color.BLUE);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
//		paint.setShader(new RadialGradient((width/2) + 30, (height / 2) - (width / 2) + 20, width / 2,
//				Color.WHITE, Color.RED, TileMode.MIRROR));
		mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		mBitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		drawCanvas.setBitmap(mBitmap);
		drawCanvas1.setBitmap(mBitmap1);

		mPaint.setColor(Color.RED);

		drawCanvas1.drawCircle(width / 2, height / 2, width / 2, paint);
		paint.setColor(Color.RED);
		drawCanvas.drawPath(mPath, paint);

		// mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		// //如下三种排列，首先绿色背景和蓝色的圆按照DST_IN混合，混合后变成了，绿色的圆。
		// canvas.drawBitmap(mBitmap1, 0, 0, mPaint);
		// //绿色的圆和红色的path按照DST_IN混合，混合后变成了绿色的path和绿色圆的交集。
		// canvas.drawBitmap(mBitmap, 0, 0, mPaint);

		// mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		// //如下三种排列，首先绿色背景和红色的path按照DST_IN混合，混合后变成了，绿色的path。
		// canvas.drawBitmap(mBitmap, 0, 0, mPaint);
		// //绿色的path和蓝色的圆按照DST_IN混合，混合后变成了绿色的path和蓝色的圆的交集。
		// canvas.drawBitmap(mBitmap1, 0, 0, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		// 如下三种排列，首先把红色的path画到绿色背景上，构成了一个的bitmap。
		canvas.drawBitmap(mBitmap, 0, 0, null);
		// 新构成的bitmap和蓝色的圆按照DST_IN方式混合，混合后变成了新构成的bitmap和蓝色圆的交集。
		canvas.drawBitmap(mBitmap1, 0, 0, mPaint);
		paint.setColor(Color.WHITE);
//		paint.setShadowLayer(10, width / 2 , height /2 - width /2 + 30, Color.WHITE);
		paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
		canvas.drawOval(width / 2 , height /2 - width /2 + 30, width / 2 + 20, height /2 - width /2 + 50, 
				paint);
		paint.setMaskFilter(new BlurMaskFilter(3, BlurMaskFilter.Blur.NORMAL));
		canvas.drawOval(width / 2 - 8 , height /2 - width /2 + 22, width / 2, height /2 - width /2 + 30, 
				paint);
		Paint paintText = new Paint();
		paintText.setColor(Color.WHITE);
		paintText.setTextSize(20);
		String text = (int)(CurProgress*1.0f / totalProgress * 100) + "%";
		float textSize = paintText.measureText(text);
		canvas.drawText(text, width / 2 - textSize / 2, 
				height / 2 - (paintText.descent() + paintText.ascent()) / 2 , paintText);
		if(waveY <= ((height / 2) - (width / 2))){
			
		} else {
			waveY = (height / 2 + width / 2) - step;
		}
//		ctrlY += 20;
		if (ctrlX >= width) {
			arivW = true;
		}
		ctrlX = arivW ? ctrlX - 50 : ctrlX + 50;
		mPath.reset();
		mPaint.reset();
		mBitmap.recycle();
		mBitmap1.recycle();
		canvas.restoreToCount(src);
//		han.sendEmptyMessageDelayed(123, 100);
	}
}
