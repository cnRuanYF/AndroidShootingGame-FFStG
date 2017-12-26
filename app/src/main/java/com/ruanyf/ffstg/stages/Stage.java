package com.ruanyf.ffstg.stages;

import android.graphics.Canvas;

import com.ruanyf.ffstg.scenes.backgrounds.Background;
import com.ruanyf.ffstg.sprites.enemies.Enemy;
import com.ruanyf.ffstg.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 战斗关卡
 * Created by Feng on 2017/12/23.
 */
public abstract class Stage {

	private float screenWidth, screenHeight;
	private long step,bossStep;
	private Background background;

	private List<Enemy> enemies;
	private Enemy boss;

	private boolean isWin;

	public Stage() {
		screenWidth = ScreenUtil.INSTANCE.getScreenWidth();
		screenHeight = ScreenUtil.INSTANCE.getScreenHeight();
		enemies = new ArrayList<>();
	}

	public float getScreenWidth() {
		return screenWidth;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public long getBossStep() {
		return bossStep;
	}

	public void setBossStep(long bossStep) {
		this.bossStep = bossStep;
	}

	public Background getBackground() {
		return background;
	}

	public void setBackground(Background background) {
		this.background = background;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public Enemy getBoss() {
		return boss;
	}

	public void setBoss(Enemy boss) {
		this.boss = boss;
	}

	public boolean isWin() {
		return isWin;
	}

	public void setWin(boolean win) {
		isWin = win;
	}

	/**
	 * 进度控制
	 */
	public abstract void onProgress();

	/**
	 * 逻辑操作
	 */
	public void doLogic() {
		step++;
		background.doLogic();
		onProgress();

		// 执行敌机逻辑
		if (enemies != null) {
			for (Enemy enemy : enemies) {
				enemy.doLogic();
			}
		}

		// 执行Boss逻辑
		if (boss != null) {
			boss.doLogic();
		}
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	public void doDraw(Canvas canvas) {
		background.doDraw(canvas);

		// 执行敌机绘制
		if (enemies != null) {
			for (Enemy enemy : enemies) {
				enemy.doDraw(canvas);
			}
		}

		// 执行Boss绘制
		if (boss != null) {
			boss.doDraw(canvas);
		}
	}


}
