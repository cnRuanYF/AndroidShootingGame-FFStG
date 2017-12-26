package com.ruanyf.ffstg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ruanyf.ffstg.scenes.BattleScene;
import com.ruanyf.ffstg.scenes.Scene;
import com.ruanyf.ffstg.scenes.SplashScene;
import com.ruanyf.ffstg.scenes.StaffScene;
import com.ruanyf.ffstg.scenes.TitleScene;
import com.ruanyf.ffstg.stages.Stage1;
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
	private int frameSkipped;

	private long step;

	private float screenWidth, screenHeight, screenScale; // 屏幕适配相关

	private boolean isRunning;

	private SurfaceHolder holder;

	private Canvas canvas;
	private Paint debugPaint;

	private GestureDetector gestureDetector;

	private Scene scene;
	private BattleScene battleScene;

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

				switch (GameUtil.INSTANCE.getGameState()) {
					case BATTLE:
						// distanceX/Y值为上次onScroll触发是的坐标 - 本次触发时的坐标，所以取负值
						battleScene.getPlayer().move(-distanceX / screenScale, -distanceY / screenScale);
						break;
					default:
						break;
				}

				return super.onScroll(e1, e2, distanceX, distanceY);
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {

				// TODO Test
				init();

				return super.onDoubleTap(e);
			}
		});

		init(); // 初始化
	}

	/**
	 * 初始化
	 */
	private void init() {
//		scene = new SplashScene();
		scene = new TitleScene();
//		scene = new StaffScene();
		scene.addCallback(this); // 添加回调监听
	}

	/**
	 * 逻辑操作
	 */
	public void doLogic() {
		step++;

		// 根据当前场景执行逻辑
		if (GameUtil.INSTANCE.getGameState() == GameState.BATTLE) {
			battleScene.doLogic();
		} else {
			scene.doLogic();
		}
	}

	/**
	 * 绘制操作
	 */
	public void doDraw() {
		canvas = holder.lockCanvas(); // 获取锁定画布
		if (canvas != null) {
			canvas.save();
			canvas.scale(screenScale, screenScale); // 根据缩放比例改变画布坐标系

			// 根据当前场景执行绘制
			if (GameUtil.INSTANCE.getGameState() == GameState.BATTLE) {
				if (battleScene != null) {
					battleScene.doDraw(canvas);
				}
			} else {
				if (scene != null) {
					scene.doDraw(canvas);
				}
			}

			// 开启调试的情况下绘制调试信息
			if (GameUtil.INSTANCE.isDebug()) {

				String debugLine1 = "-- DEBUG MODE --";
				String debugLine2 = "FrameCount(GameView.Step): " + step;
				String debugLine3, debugLine4;

				if (GameUtil.INSTANCE.isFrameSkipEnable()) {
					debugLine3 = "FrameSkipped: " + frameSkipped;
					debugLine4 = "Ave.FPS: " + FPS * (step - frameSkipped) / step + " / " + FPS;
				} else {
					debugLine3 = "DesignFPS: " + FPS;
					debugLine4 = "";
				}

				canvas.drawText(debugLine1, 20, screenHeight - 10, debugPaint);
				canvas.drawText(debugLine2, 20, screenHeight - 30, debugPaint);
				canvas.drawText(debugLine3, 20, screenHeight - 50, debugPaint);
				canvas.drawText(debugLine4, 20, screenHeight - 70, debugPaint);

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
		battleScene = null;
		scene = null;
		if (GameUtil.INSTANCE.getGameState() == GameState.BATTLE) {

			// TODO 默认战斗
			List<Bullet> bullets = new ArrayList<>();
			for (int i = 0; i < 100; i++) {
				Bullet bullet = new Bullet(GameUtil.INSTANCE.getBitmap("bullet1.png"));
				bullets.add(bullet);
			}
			Player testPlayer = new Player(GameUtil.INSTANCE.getBitmap("player_green.png"));
			testPlayer.setVisible(true);
			testPlayer.setCenterPosition(screenWidth / 2, screenHeight - 200);
			testPlayer.setBullets(bullets);
			Bitmap blastBmp = GameUtil.INSTANCE.getBitmap("blast_green_middle.png");
			testPlayer.setBlast(new Blast(blastBmp, blastBmp.getWidth() / 15, blastBmp.getHeight()));
			battleScene = new BattleScene(new Stage1());
			battleScene.setPlayer(testPlayer);

			battleScene.addCallback(this); // 添加回调监听
		} else {
			switch (GameUtil.INSTANCE.getGameState()) {
				case SPLASH:
					scene = new SplashScene();
					break;
				case TITLE:
					scene = new TitleScene();
					break;
				case STAFF:
					scene = new StaffScene();
					break;
			}
			scene.addCallback(this); // 添加回调监听
		}
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
				doLogic(); // 进更新逻辑不进行绘制
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
