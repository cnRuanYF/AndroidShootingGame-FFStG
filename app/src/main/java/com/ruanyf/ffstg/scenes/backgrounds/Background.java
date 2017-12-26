package com.ruanyf.ffstg.scenes.backgrounds;

import android.graphics.Canvas;

/**
 * 背景类
 * Created by Feng on 2017/12/26.
 */
public abstract class Background {

	private long step; // 进度
	private float speed; // 速度

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * 逻辑操作
	 */
	public void doLogic() {
		step++;
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	public abstract void doDraw(Canvas canvas);

}
