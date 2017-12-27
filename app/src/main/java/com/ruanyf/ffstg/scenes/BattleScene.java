package com.ruanyf.ffstg.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ruanyf.ffstg.sprites.Bullet;
import com.ruanyf.ffstg.GameState;
import com.ruanyf.ffstg.WeaponType;
import com.ruanyf.ffstg.sprites.enemies.Enemy;
import com.ruanyf.ffstg.sprites.Player;
import com.ruanyf.ffstg.stages.Stage;
import com.ruanyf.ffstg.stages.Stage1;
import com.ruanyf.ffstg.stages.Stage2;
import com.ruanyf.ffstg.utils.CollisionUtil;
import com.ruanyf.ffstg.utils.GameUtil;
import com.ruanyf.ffstg.utils.ScreenUtil;

import java.text.NumberFormat;

/**
 * 战斗场景类
 * Created by Feng on 2017/12/23.
 */
public class BattleScene extends Scene {

	private Stage stage;
	private Player player;

	private long score; // 游戏得分
	private boolean isGameover; // 是否游戏结束

	private Paint bigPaint, scorePaint, progressBarPaint, progressTextPaint;
	private float progress;

	public BattleScene() {

		// 画笔初始化
		bigPaint = new Paint();
		bigPaint.setColor(Color.WHITE);
		bigPaint.setTextSize(48);
		bigPaint.setAntiAlias(true);
		bigPaint.setTextAlign(Paint.Align.CENTER);
		bigPaint.setShadowLayer(5, 0, 0, Color.RED);

		scorePaint = new Paint();
		scorePaint.setColor(Color.CYAN);
		scorePaint.setTextSize(24);
		scorePaint.setAntiAlias(true);
		scorePaint.setTextAlign(Paint.Align.RIGHT);
		scorePaint.setShadowLayer(2, 0, 0, Color.BLACK);

		progressBarPaint = new Paint();
		progressBarPaint.setAntiAlias(true);
		progressTextPaint = new Paint();
		progressTextPaint.setAntiAlias(true);
		progressTextPaint.setTextSize(14);
		progressTextPaint.setColor(Color.WHITE);
		progressTextPaint.setAlpha(192);
	}

	public BattleScene(Stage stage) {
		this();
		this.stage = stage;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * 逻辑操作
	 */
	@Override
	public void doLogic() {
		super.doLogic();

		// 游戏未结束才执行
		if (!isGameover) {
			stage.doLogic();
			player.doLogic();

			// TODO test
			if (getStep() % 600 == 0) {
				player.setWeaponType(WeaponType.NORMAL);
			} else if ((getStep() + 200) % 600 == 0) {
				player.setWeaponType(WeaponType.SHOTGUN3);
			} else if ((getStep() + 400) % 600 == 0) {
				player.setWeaponType(WeaponType.SHOTGUN5);
			}

			// 敌机碰撞检测
			if (stage.getEnemies() != null) {
				// 遍历所有敌机
				for (Enemy enemy : stage.getEnemies()) {

					// 敌机与玩家是否碰撞
					if (CollisionUtil.circleCollision(enemy, player)) {
						enemy.takeDamage(100); // TODO 伤害待考量
						player.takeDamage(100); // TODO 伤害待考量
					}

					// 敌机子弹是否与玩家碰撞
					if (enemy.getBullets() != null) {
						for (Bullet bullet : enemy.getBullets()) {
							if (CollisionUtil.circleDotCollision(player, bullet)) {
								bullet.setVisible(false);
								player.takeDamage(1); // TODO 不同子弹不同伤害
							}
						}
					}

					// 玩家子弹是否与敌机碰撞
					for (Bullet bullet : player.getBullets()) {
						if (CollisionUtil.circleDotCollision(enemy, bullet)) {
							bullet.setVisible(false);
							enemy.takeDamage(1); // TODO 不同子弹不同伤害
							if (enemy.isDead()) {
								score += 5; // TODO 不同敌机加分不同
							}
						}
					}
				}
			}

			// Boss碰撞检测（打败后不进行碰撞检测）
			if (stage.getBoss() != null && !stage.isWin()) {

				// Boss与玩家是否碰撞
				if (CollisionUtil.circleCollision(stage.getBoss(), player)) {
					player.takeDamage(100); // TODO 伤害待考量
				}

				// Boss子弹是否与玩家碰撞
				if (stage.getBoss().getBullets() != null) {
					for (Bullet bullet : stage.getBoss().getBullets()) {
						if (CollisionUtil.circleDotCollision(player, bullet)) {
							bullet.setVisible(false);
							player.takeDamage(1); // TODO 不同子弹不同伤害
						}
					}
				}

				// Boss子弹是否与敌机碰撞
				for (Bullet bullet : player.getBullets()) {
					if (CollisionUtil.circleDotCollision(stage.getBoss(), bullet)) {
						bullet.setVisible(false);
						stage.getBoss().takeDamage(1); // TODO 不同子弹不同伤害
						if (stage.getBoss().isDead()) {
							score += 1000; // TODO 不同敌机加分不同
						}
					}
				}

			}

			// 过关检测
			if (stage.getBoss() != null
					&& stage.getBoss().isDead()
					&& !stage.getBoss().getBlast().isVisible()
					&& !player.isDead()) {
				stage.setWin(true);
				setFading(true);
			}

			// 玩家死亡检测(播放完爆炸动画)
			if (player.isDead() && !player.getBlast().isVisible()) {
				isGameover = true;
				setFading(true);
			}

			// 游戏进度条逻辑
			if (stage.getStep() < stage.getBossStep()) {
				// Boss未出现的情形，指示关卡进度
				progress = (float) stage.getStep() / stage.getBossStep();
				progressBarPaint.setColor(Color.argb(192,
						(int) (255 * progress), (int) (255 * (1 - progress)), (int) (255 * (1 - progress))));
			} else {
				// 否则指示Boss血量 (平滑处理)
				float realProgress = (float) stage.getBoss().getLifeCurrent() / stage.getBoss().getLifeTotal();
				progress = progress - (progress - realProgress) * 0.1f;
				progressBarPaint.setColor(Color.RED);
				progressBarPaint.setAlpha(192);
			}
			progressTextPaint.setTextAlign(progress > 0.5f ? Paint.Align.RIGHT : Paint.Align.LEFT);

		}

	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	@Override
	public void doDraw(Canvas canvas) {

		stage.doDraw(canvas);
		player.doDraw(canvas);

		// 得分
		canvas.drawText("Score: " + score,
				ScreenUtil.INSTANCE.getScreenWidth() - 24, 48, scorePaint);

		// 游戏进度条绘制
		canvas.drawRect(0, 0, getScreenWidth() * progress, 18f * (255 - getFadeOpacity()) / 255, progressBarPaint);
		progressBarPaint.setColor(Color.BLACK);
		progressBarPaint.setAlpha(192);
		canvas.drawRect(getScreenWidth() * progress, 0, getScreenWidth(), 18f * (255 - getFadeOpacity()) / 255, progressBarPaint);
		String progressText = (stage.getStep() < stage.getBossStep() ? " PROGRESS: " : " BOSSLIFE: ")
				+ NumberFormat.getPercentInstance().format(progress) + " ";
		canvas.drawText(progressText, getScreenWidth() * progress, 15f * (255 - getFadeOpacity()) / 255, progressTextPaint);

		// 游戏胜利或失败的绘制
		if (isGameover) {
			// TODO 暂时简单绘制，待美化
			canvas.drawText("GAME OVER", ScreenUtil.INSTANCE.getScreenWidth() / 2,
					ScreenUtil.INSTANCE.getScreenHeight() / 2, bigPaint);
		} else if (stage.isWin()) {
			// TODO 暂时简单绘制，待美化
			bigPaint.setShadowLayer(5, 0, 0, Color.GREEN);
			canvas.drawText("YOU WIN", ScreenUtil.INSTANCE.getScreenWidth() / 2,
					ScreenUtil.INSTANCE.getScreenHeight() / 2, bigPaint);
		}

		// 进场效果渐变层
		if (isFading()) {
			canvas.drawColor(Color.argb(getFadeOpacity(), 0, 0, 0));
		}

		// 开启调试的情况下绘制调试信息
		if (GameUtil.INSTANCE.isDebug()) {
			String bossInfo = "BossLife: " + (stage.getBoss() != null ?
					stage.getBoss().getLifeCurrent() + " / " + stage.getBoss().getLifeTotal() : "?");
			canvas.drawText(bossInfo, 20, 100, getDebugTextPaint());
		}

	}

	/**
	 * 场景结束后的操作
	 */
	@Override
	public void onEnded() {
		if (isGameover) {
			changeScene(GameState.TITLE);
		} else if (stage instanceof Stage1) {
			GameUtil.INSTANCE.setCurrentStage(new Stage2());
			changeScene(GameState.BATTLE);
		} else {
			changeScene(GameState.STAFF);
		}
	}

}
