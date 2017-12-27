package com.ruanyf.ffstg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ruanyf.ffstg.scenes.BattleScene;
import com.ruanyf.ffstg.scenes.Scene;
import com.ruanyf.ffstg.scenes.SplashScene;
import com.ruanyf.ffstg.scenes.StaffScene;
import com.ruanyf.ffstg.scenes.StageChooseScene;
import com.ruanyf.ffstg.scenes.TitleScene;
import com.ruanyf.ffstg.sprites.Blast;
import com.ruanyf.ffstg.sprites.Bullet;
import com.ruanyf.ffstg.sprites.Player;
import com.ruanyf.ffstg.utils.GameUtil;
import com.ruanyf.ffstg.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏主视图
 * Created by Feng on 2017/12/23.
 */
public class GameView extends SurfaceView implements Scene.Callback, SurfaceHolder.Callback, Runnable {

	public static final int FPS = 60; // 设计帧率
	public static final int FRAME_PERIOD = 1000 / FPS; // 帧周期
	private long frameCount, frameSkipped; // 帧统计

	private float screenWidth, screenHeight, screenScale; // 屏幕适配相关

	private boolean isRunning;

	private SurfaceHolder holder;
	private Canvas canvas;
	private Paint debugPaint;

	private GestureDetector gestureDetector;

	private Scene currentScene;

	public GameView(Context context) {
		super(context);

		// 获取SurfaceHolder并添加回调监听
		holder = getHolder();
		holder.addCallback(this);

		debugPaint = new Paint();
		debugPaint.setAntiAlias(true);
		debugPaint.setTextSize(16);
		debugPaint.setShadowLayer(2, 0, 0, Color.BLACK);
		debugPaint.setColor(Color.YELLOW);

		// 屏幕适配相关
		screenScale = ScreenUtil.INSTANCE.getScreenScale();
		screenWidth = ScreenUtil.INSTANCE.getScreenWidth();
		screenHeight = ScreenUtil.INSTANCE.getScreenHeight();

		setLongClickable(true); // 使手势监听生效
		gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				// distanceX/Y值为上次onScroll触发时的坐标 - 本次触发时的坐标，所以取负值

				// 防止切换场景时移动导致空对象异常
				if (currentScene != null) {
					switch (GameUtil.INSTANCE.getGameState()) {
						case BATTLE:
							if (((BattleScene) currentScene).getPlayer() != null) {
								((BattleScene) currentScene).getPlayer().move(-distanceX / screenScale, -distanceY / screenScale);
							}
							break;
						case STAFF:
							currentScene.setStep(currentScene.getStep() + (long) (distanceY / screenScale / 2));
							if (currentScene.getStep() < 0) {
								currentScene.setStep(0);
							}
							break;
						default:
							break;
					}
				}
				return super.onScroll(e1, e2, distanceX, distanceY);
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				currentScene.detectSingleTap(e.getX() / screenScale, e.getY() / screenScale);
				return super.onSingleTapConfirmed(e);
			}

//			@Override
//			public boolean onDoubleTap(MotionEvent e) {
//				init(); // TODO Test
//				return super.onDoubleTap(e);
//			}
		});

		init(); // 初始化
	}

	/**
	 * 初始化
	 */
	private void init() {
		GameUtil.INSTANCE.setGameState(GameState.SPLASH);
		currentScene = new SplashScene();
//		GameUtil.INSTANCE.setGameState(GameState.TITLE);
//		currentScene = new TitleScene();
//		GameUtil.INSTANCE.setGameState(GameState.CHOOSESTAGE);
//		currentScene = new StageChooseScene();
		currentScene.addCallback(this); // 添加回调监听
	}

	/**
	 * 逻辑操作
	 */
	public void doLogic() {
		frameCount++;

		// 执行当前场景逻辑
		if (currentScene != null) {
			currentScene.doLogic();
		}
	}

	/**
	 * 绘制操作
	 */
	public void doDraw() {
		// 判断系统版本
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			canvas = holder.lockHardwareCanvas(); // 获取硬件加速画布
		} else {
			canvas = holder.lockCanvas(); // 获取锁定画布
		}
		if (canvas != null) {
			canvas.save();
			canvas.scale(screenScale, screenScale); // 根据缩放比例改变画布坐标系

			// 绘制当前场景
			if (currentScene != null) {
				currentScene.doDraw(canvas);
			}

			// 绘制FPS信息
			if (GameUtil.INSTANCE.isShowFPS()) {
				String fpsStr1, fpsStr2;
				if (GameUtil.INSTANCE.isFrameSkipEnable()) {
					fpsStr1 = "AverageFPS: " + FPS * (frameCount - frameSkipped) / frameCount + " / " + FPS;
					fpsStr2 = "FrameSkipped: " + frameSkipped + " / " + frameCount;
				} else {
					fpsStr1 = "DesignFPS: " + FPS;
					fpsStr2 = "FrameCount: " + frameCount;
				}
				canvas.drawText(fpsStr1, 20, screenHeight - 40, debugPaint);
				canvas.drawText(fpsStr2, 20, screenHeight - 20, debugPaint);
			}

			// Debug
			if (GameUtil.INSTANCE.isDebug()) {
				canvas.drawText("GameState: " + GameUtil.INSTANCE.getGameState(), 20, 40, debugPaint);
				canvas.drawText("Step: " + currentScene.getStep(), 20, 60, debugPaint);
				canvas.drawText("FadeOpacity: " + currentScene.getFadeOpacity(), 20, 80, debugPaint);
			}

			canvas.restore(); // 还原画布坐标系
			holder.unlockCanvasAndPost(canvas); // 解锁画布
		}

	}

	/* --------------------------------
		Scene场景回调相关
	 -------------------------------- */

	@Override
	public void onSceneChanged() {
		currentScene = null;
		switch (GameUtil.INSTANCE.getGameState()) {
			case BATTLE:
				// TODO 玩家属性暂时不变
				List<Bullet> bullets = new ArrayList<>();
				for (int i = 0; i < 100; i++) {
					Bullet bullet = new Bullet(GameUtil.INSTANCE.getBitmap("bullet_up_blue_middle.png"));
					bullets.add(bullet);
				}
				Player testPlayer = new Player(GameUtil.INSTANCE.getBitmap("player_green.png"));
				testPlayer.setVisible(true);
				testPlayer.setCenterPosition(screenWidth / 2, screenHeight - 200);
				testPlayer.setBullets(bullets);
				Bitmap blastBmp = GameUtil.INSTANCE.getBitmap("blast_green_middle.png");
				testPlayer.setBlast(new Blast(blastBmp, blastBmp.getWidth() / 15, blastBmp.getHeight()));
				currentScene = new BattleScene(GameUtil.INSTANCE.getCurrentStage());
				((BattleScene) currentScene).setPlayer(testPlayer);
				break;
			case SPLASH:
				currentScene = new SplashScene();
				break;
			case TITLE:
				currentScene = new TitleScene();
				break;
			case CHOOSESTAGE:
				currentScene = new StageChooseScene();
				break;
			case STAFF:
				currentScene = new StaffScene();
				break;
		}
		currentScene.addCallback(this); // 添加回调监听
	}

	/* --------------------------------
		SurfaceHolder相关
	 -------------------------------- */

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		isRunning = true;
		new Thread(this).start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isRunning = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/* --------------------------------
		线程相关
	 -------------------------------- */

	@Override
	public void run() {
		while (isRunning) {
			// 获取每一轮开始时间
			long startTime = System.currentTimeMillis();
			doLogic();
			doDraw();
			// 根据帧周期和结束时间计算休眠时间
			int sleepTime = FRAME_PERIOD - (int) (System.currentTimeMillis() - startTime);

			// 若启用跳帧 & 休眠时间<0，代表绘制时间过长，进行跳帧，只到需要休眠
			while (GameUtil.INSTANCE.isFrameSkipEnable() && sleepTime < 0) {
				doLogic(); // 仅更新逻辑不进行绘制
				frameSkipped++;
				sleepTime += FRAME_PERIOD;
			}

			// 若休眠时间>0，则正常休眠
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
