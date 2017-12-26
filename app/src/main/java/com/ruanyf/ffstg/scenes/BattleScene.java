package com.ruanyf.ffstg.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ruanyf.ffstg.Bullet;
import com.ruanyf.ffstg.GameState;
import com.ruanyf.ffstg.WeaponType;
import com.ruanyf.ffstg.enemies.Enemy;
import com.ruanyf.ffstg.Player;
import com.ruanyf.ffstg.stages.Stage;
import com.ruanyf.ffstg.utils.CollisionUtil;
import com.ruanyf.ffstg.utils.GameUtil;
import com.ruanyf.ffstg.utils.ScreenUtil;

/**
 * 战斗场景类
 * Created by Feng on 2017/12/23.
 */
public class BattleScene extends Scene {

	private Stage stage;
	private Player player;

	private long score; // 游戏得分
	private boolean isGameover; // 是否游戏结束

	private Paint bigPaint, scorePaint;

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

		canvas.drawText("Score: " + score,
				ScreenUtil.INSTANCE.getScreenWidth() - 20, 30, scorePaint);

		if (isGameover) {
			// TODO 暂时简单绘制，待美化
			canvas.drawText("GAME OVER", ScreenUtil.INSTANCE.getScreenWidth() / 2,
					ScreenUtil.INSTANCE.getScreenHeight() / 2, bigPaint);
		}

		if (stage.isWin()) {
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
			String debugLine1 = "- BattleScene -";
			String debugLine2 = "Step: " + getStep();
			String debugLine3 = "Boss: "
					+ (stage.getBoss() != null ? stage.getBoss().getLife() + " Life" : "Null");
			canvas.drawText(debugLine1, 20, 30, getDebugPaint());
			canvas.drawText(debugLine2, 20, 50, getDebugPaint());
			canvas.drawText(debugLine3, 20, 70, getDebugPaint());
		}

	}

	/**
	 * 场景结束后的操作
	 */
	@Override
	public void onEnded() {
		changeScene(GameState.STAFF);
	}

}
