package com.ruanyf.ffstg.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ruanyf.ffstg.GameState;
import com.ruanyf.ffstg.scenes.backgrounds.Background;
import com.ruanyf.ffstg.scenes.backgrounds.BitmapBackground;
import com.ruanyf.ffstg.scenes.backgrounds.LightfallBackground;
import com.ruanyf.ffstg.scenes.backgrounds.StaffBackground;
import com.ruanyf.ffstg.stages.Stage1;
import com.ruanyf.ffstg.stages.Stage2;
import com.ruanyf.ffstg.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 标题菜单场景
 * Created by Feng on 2017/12/25.
 */
public class StageChooseScene extends Scene {

	private static final String[] STAGE_NAMES = new String[]{"STAGE 1 — 河道战役", "STAGE 2 — 太空战场", "更多关卡敬请期待"};
	private static final String[] MENU_STRINGS = new String[]{"返回主菜单"};
	private static final int STAGE_RIVER = 0;
	private static final int STAGE_SPACE = 1;
	private static final int STAGE_MORE = 2;
	private static final int MENU_BACK = 3;

	private float screenCenterX, screenCenterY;

	private Paint menuPaint, menuFadePaint, strokePaint, strokeFadePaint;
	private Background sceneBackground;
	private Background backgroundS1, backgroundS2, backgroundMore;

	private List<RectF> menuAreas;
	private RectF selectedArea;
	private int selectedMenuIndex; // 已选菜单项索引

	/**
	 * 构造
	 */
	public StageChooseScene() {
		screenCenterX = getScreenWidth() / 2;
		screenCenterY = getScreenHeight() / 2;
		sceneBackground = new LightfallBackground();
		setFadeSpeed(8);

		backgroundS1 = new BitmapBackground(GameUtil.INSTANCE.getBitmap("background_river_480.png"));
		backgroundS2 = new BitmapBackground(GameUtil.INSTANCE.getBitmap("background_space_480.png"));
		backgroundMore = new StaffBackground();
		backgroundS1.setSpeed(1);
		backgroundS2.setSpeed(1);
		backgroundMore.setSpeed(1);

		// 菜单绘制相关
		menuPaint = new Paint();
		menuPaint.setAntiAlias(true);
		menuPaint.setTextAlign(Paint.Align.CENTER);
		menuPaint.setColor(Color.WHITE);
		menuPaint.setShadowLayer(4, 0, 0, Color.BLACK);

		menuFadePaint = new Paint();
		menuFadePaint.setAntiAlias(true);
		menuFadePaint.setTextAlign(Paint.Align.CENTER);
		menuFadePaint.setColor(Color.WHITE);
		menuFadePaint.setShadowLayer(1, 0, 0, Color.WHITE);

		strokePaint = new Paint();
		strokePaint.setAntiAlias(true);
		strokePaint.setStyle(Paint.Style.STROKE);
		strokePaint.setStrokeWidth(2);
		strokePaint.setColor(Color.WHITE);
		strokePaint.setAlpha(128);
		strokePaint.setShadowLayer(4, 0, 0, Color.WHITE);

		strokeFadePaint = new Paint();
		strokeFadePaint.setAntiAlias(true);
		strokeFadePaint.setStyle(Paint.Style.STROKE);
		strokeFadePaint.setStrokeWidth(2);
		strokeFadePaint.setColor(Color.WHITE);
		strokeFadePaint.setShadowLayer(4, 0, 0, Color.WHITE);

		menuAreas = new ArrayList<>();
		for (int i = 0; i < STAGE_NAMES.length; i++) { // 关卡选择区
			RectF rectF = new RectF();
			rectF.set(64, 128 + i * 176,
					getScreenWidth() - 64, 256 + i * 176);
			menuAreas.add(rectF);
		}
		for (int i = STAGE_NAMES.length; i < STAGE_NAMES.length + MENU_STRINGS.length; i++) { // 菜单选择区
			RectF rectF = new RectF();
			rectF.set(screenCenterX - 80, getScreenHeight() - 328 + i * 64,
					screenCenterX + 80, getScreenHeight() - 296 + i * 64);
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
			for (int i = 0; i < STAGE_NAMES.length + MENU_STRINGS.length; i++) {
				if (menuAreas.get(i).contains(x, y)) {
					setFading(true);
					selectedMenuIndex = i;
					selectedArea = new RectF(menuAreas.get(i));
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
		sceneBackground.doLogic();

		// 关卡预览图
		backgroundS1.doLogic();
		backgroundS2.doLogic();
		backgroundMore.doLogic();

		// 菜单选中的效果
		if (isFading() && selectedMenuIndex > -1) {
			menuFadePaint.setTextScaleX(menuFadePaint.getTextScaleX() * 1.02f);
			menuFadePaint.setAlpha(255 - getFadeOpacity());
			menuFadePaint.setShadowLayer(getFadeOpacity() / 8, 0, 0, Color.WHITE);
		}

		// 关卡选中的效果
		if (isFading() && selectedMenuIndex > -1 && selectedMenuIndex < STAGE_NAMES.length) {
			selectedArea.set(selectedArea.left - 1, selectedArea.top - 1,
					selectedArea.right + 1, selectedArea.bottom + 1);
			strokeFadePaint.setAlpha(128 - getFadeOpacity()/2);
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
		sceneBackground.doDraw(canvas);

		menuPaint.setTextSize(32);
		menuPaint.setColor(Color.WHITE);
		canvas.drawText("— 选择关卡 —", screenCenterX, 80, menuPaint);

		// 关卡图片
		canvas.save();
		canvas.clipRect(menuAreas.get(0));
		backgroundS1.doDraw(canvas);
		canvas.restore();
		canvas.save();
		canvas.clipRect(menuAreas.get(1));
		backgroundS2.doDraw(canvas);
		canvas.restore();
		canvas.save();
		canvas.clipRect(menuAreas.get(2));
		backgroundMore.doDraw(canvas);
		canvas.restore();

		// 关卡名
		menuPaint.setTextSize(24);
		menuPaint.setColor(Color.WHITE);
		menuPaint.setAlpha(192);
		for (int i = 0; i < STAGE_NAMES.length; i++) {
			canvas.drawText(STAGE_NAMES[i], screenCenterX, menuAreas.get(i).bottom - 16, menuPaint);
		}
		for (int i = 0; i < STAGE_NAMES.length; i++) {
			canvas.drawRect(menuAreas.get(i), strokePaint);
		}

		// 菜单项
		menuPaint.setTextSize(32);
		menuPaint.setColor(Color.WHITE);
		for (int i = 0; i < MENU_STRINGS.length; i++) {
			canvas.drawText(MENU_STRINGS[i], screenCenterX, menuAreas.get(STAGE_NAMES.length + i).bottom - 4, menuPaint);
		}

		// 菜单选中特效
		if (isFading() && selectedMenuIndex > -1) {
			if (selectedMenuIndex < STAGE_NAMES.length) { // 选中关卡
				menuFadePaint.setTextSize(24);
				canvas.drawText(STAGE_NAMES[selectedMenuIndex],
						screenCenterX, menuAreas.get(selectedMenuIndex).bottom - 16, menuFadePaint);
				canvas.drawRect(selectedArea, strokeFadePaint);
			} else { // 选中菜单
				menuFadePaint.setTextSize(32);
				canvas.drawText(MENU_STRINGS[selectedMenuIndex - STAGE_NAMES.length],
						screenCenterX, menuAreas.get(selectedMenuIndex).bottom - 4, menuFadePaint);
			}
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
			case STAGE_RIVER: // 关卡1
				GameUtil.INSTANCE.setCurrentStage(new Stage1());
				changeScene(GameState.BATTLE);
				break;
			case STAGE_SPACE: // 关卡2
				GameUtil.INSTANCE.setCurrentStage(new Stage2());
				changeScene(GameState.BATTLE);
				break;
			case STAGE_MORE: // 更多关卡
				changeScene(GameState.STAFF);
				break;
			case MENU_BACK: // 返回
				changeScene(GameState.TITLE);
				break;
		}
	}

}
