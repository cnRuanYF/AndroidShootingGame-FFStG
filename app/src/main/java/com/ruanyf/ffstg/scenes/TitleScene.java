package com.ruanyf.ffstg.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.ruanyf.ffstg.GameState;
import com.ruanyf.ffstg.MainActivity;
import com.ruanyf.ffstg.scenes.backgrounds.Background;
import com.ruanyf.ffstg.scenes.backgrounds.LightfallBackground;
import com.ruanyf.ffstg.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 标题菜单场景
 * Created by Feng on 2017/12/25.
 */
public class TitleScene extends Scene {

	private static final String[] MENU_STRINGS = new String[]{
			"开始游戏", "天梯排行", "创作名单", "游戏设置", "退出游戏"};
	private static final int MENU_START = 0;
	private static final int MENU_RANK = 1;
	private static final int MENU_STAFF = 2;
	private static final int MENU_SETTING = 3;
	private static final int MENU_QUIT = 4;

	private float screenCenterX, screenCenterY, menuY;

	private Shader logoShineShader;
	private Paint logoPaint, logoShinePaint, menuPaint, menuFadePaint;
	private List<Paint> logoPaints;
	private Background background;

	private List<RectF> menuAreas;
	private int selectedMenuIndex; // 已选菜单项索引

	/**
	 * 构造
	 */
	public TitleScene() {
		screenCenterX = getScreenWidth() / 2;
		screenCenterY = getScreenHeight() / 2;
		menuY = screenCenterY + 48;
		background = new LightfallBackground();
		setFadeSpeed(8);

		// Logo绘制相关初始化
		logoPaint = new Paint();
		logoPaint.setAntiAlias(true);
		logoPaint.setTextAlign(Paint.Align.CENTER);
		logoPaint.setColor(Color.WHITE);
		logoPaint.setShadowLayer(4, 0, 0, Color.BLACK);

		logoShinePaint = new Paint();
		logoShinePaint.setAntiAlias(true);
		logoShinePaint.setTextAlign(Paint.Align.CENTER);
		logoShinePaint.setColor(Color.BLACK);
		logoShinePaint.setShadowLayer(8, 0, 0, Color.BLACK);

		logoPaints = new ArrayList<>();
		logoPaints.add(logoPaint);
		logoPaints.add(logoShinePaint);
		logoPaints.add(logoShinePaint); // 发光层绘制两遍

		// 菜单绘制相关
		menuPaint = new Paint();
		menuPaint.setAntiAlias(true);
		menuPaint.setTextAlign(Paint.Align.CENTER);
		menuPaint.setColor(Color.WHITE);
		menuPaint.setShadowLayer(4, 0, 0, Color.BLACK);

		menuFadePaint = new Paint();
		menuFadePaint.setAntiAlias(true);
		menuFadePaint.setTextAlign(Paint.Align.CENTER);
		menuFadePaint.setTextSize(32);
		menuFadePaint.setColor(Color.WHITE);
		menuFadePaint.setShadowLayer(1, 0, 0, Color.WHITE);

		menuAreas = new ArrayList<>();
		for (int i = 0; i < MENU_STRINGS.length; i++) {
			RectF rectF = new RectF();
			rectF.set(screenCenterX - 64, menuY - 28 + i * 64,
					screenCenterX + 64, menuY + 4 + i * 64);
			menuAreas.add(rectF);
		}

		selectedMenuIndex = -1;
	}

	/**
	 * 单击被确认的操作
	 */
	@Override
	public void detectSingleTap(float x, float y) {
		if (!isFading()) { // 防止渐变过程再次选择别的项目
			for (int i = 0; i < MENU_STRINGS.length; i++) {
				if (menuAreas.get(i).contains(x, y)) {
					if (i == MENU_RANK) {
						break; // 排行榜暂不可用
					} else if (i == MENU_SETTING) {
						GameUtil.INSTANCE.showGameSetting();
					} else {
						setFading(true);
						selectedMenuIndex = i;
					}
					break;
				}
			}

		}
	}

	/**
	 * 逻辑操作
	 */
	@Override
	public void doLogic() {
		super.doLogic();

		// 背景
		background.doLogic();

		// 游戏LOGO
		int shineOffset = (int) ((getStep() * 5) % getScreenWidth());
		logoShineShader = new LinearGradient(shineOffset - 128, shineOffset - 128,
				shineOffset, shineOffset,
				new int[]{Color.argb(0, 255, 255, 255),
						Color.argb(255, 255, 255, 255),
						Color.argb(0, 255, 255, 255)},
				new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
		logoShinePaint.setShader(logoShineShader);

		// 菜单选中的效果
		if (isFading() && selectedMenuIndex > -1) {
			menuFadePaint.setTextScaleX(menuFadePaint.getTextScaleX() * 1.02f);
			menuFadePaint.setAlpha(255 - getFadeOpacity());
			menuFadePaint.setShadowLayer(getFadeOpacity() / 8, 0, 0, Color.WHITE);
		}
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	@Override
	public void doDraw(Canvas canvas) {

		// 绘制背景
//		canvas.drawColor(Color.BLACK);
		background.doDraw(canvas);

		// 绘制游戏LOGO
		for (Paint paint : logoPaints) {
			paint.setTextSize(100);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(4);
			canvas.drawText("FFStG", screenCenterX, 220, paint);
			paint.setStyle(Paint.Style.FILL);
			paint.setTextSize(20);
			canvas.drawText("Feng's Flight Shooting Game", screenCenterX, 244, paint);
		}

		// 绘制菜单
		menuPaint.setTextSize(32);
		for (int i = 0; i < MENU_STRINGS.length; i++) {
			menuPaint.setColor(Color.WHITE);
			if (i == MENU_RANK) {
				menuPaint.setAlpha(128); // 排行榜暂不可用
			}
			canvas.drawText(MENU_STRINGS[i], screenCenterX, menuY + i * 64, menuPaint);
		}

		// 菜单选中特效
		if (isFading() && selectedMenuIndex > -1) {
			canvas.drawText(MENU_STRINGS[selectedMenuIndex],
					screenCenterX, menuY + selectedMenuIndex * 64, menuFadePaint);
		}

		// 版权信息
		menuPaint.setTextSize(16);
		menuPaint.setAlpha(128);
		canvas.drawText("Copyright © 2017 Yaofeng Ruan",
				screenCenterX, getScreenHeight() - 48, menuPaint);

		// 绘制淡入淡出覆盖层
		if (isFading()) {
			canvas.drawColor(Color.argb(getFadeOpacity(), 0, 0, 0));
		}

		// 开启调试的情况下绘制调试信息
		if (GameUtil.INSTANCE.isDebug()) {
			for (RectF r : menuAreas) {
				canvas.drawRect(r, getDebugShapePaint());
			}
		}
	}

	/**
	 * 场景结束后的操作
	 */
	@Override
	public void onEnded() {
		switch (selectedMenuIndex) {
			case MENU_START: // 开始游戏
				changeScene(GameState.CHOOSESTAGE);
				break;
			case MENU_STAFF: // 制作人名单
				changeScene(GameState.STAFF);
				break;
			case MENU_QUIT: // 结束游戏
				((MainActivity) GameUtil.INSTANCE.getContext()).finish();
				break;
		}
	}

}
