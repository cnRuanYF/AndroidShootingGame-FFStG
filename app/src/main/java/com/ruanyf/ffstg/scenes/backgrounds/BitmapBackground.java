package com.ruanyf.ffstg.scenes.backgrounds;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 图片背景类
 * Created by Feng on 2017/12/23.
 */
public class BitmapBackground extends Background {

	private Bitmap bitmap;
	float y1, y2; // 两张图的y坐标

	public BitmapBackground(Bitmap bitmap) {
		this.bitmap = bitmap;
		y1 = 0 - bitmap.getHeight();
		y2 = 0;
	}

	public BitmapBackground(Bitmap bitmap, float speed) {
		this(bitmap);
		setSpeed(speed);
	}

	/**
	 * 逻辑操作
	 */
	@Override
	public void doLogic() {
		super.doLogic();
		y1 += getSpeed();
		y2 += getSpeed();
		if (y1 >= 0) {
			y1 = 0 - bitmap.getHeight();
			y2 = 0;
		}
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	@Override
	public void doDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, y1, null);
		canvas.drawBitmap(bitmap, 0, y2, null);
	}

}
