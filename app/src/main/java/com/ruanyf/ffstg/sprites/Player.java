package com.ruanyf.ffstg.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.ruanyf.ffstg.WeaponType;
import com.ruanyf.ffstg.utils.GameUtil;
import com.ruanyf.ffstg.utils.ScreenUtil;

import java.util.List;

/**
 * 玩家类
 * Created by Feng on 2017/12/23.
 */
public class Player extends Sprite {

	private WeaponType weaponType; // 武器种类
	private List<Bullet> bullets; // 弹夹(用于复用子弹节省内存)

	private int fireInterval; // 发射间隔

	private Blast blast; // 爆炸效果

	private boolean isDead;
	private int life;

	/**
	 * 构建玩家Sprite
	 *
	 * @param bitmap 位图对象
	 */
	public Player(Bitmap bitmap) {
		super(bitmap);
		weaponType = WeaponType.NORMAL;
		fireInterval = 20;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean dead) {
		isDead = dead;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(List<Bullet> bullets) {
		this.bullets = bullets;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}

	public int getFireInterval() {
		return fireInterval;
	}

	public void setFireInterval(int fireInterval) {
		this.fireInterval = fireInterval;
	}

	public Blast getBlast() {
		return blast;
	}

	public void setBlast(Blast blast) {
		this.blast = blast;
	}

	/**
	 * 发射普通子弹
	 */
	private void fireNormal() {
		// 遍历所有子弹
		for (Bullet bullet : bullets) {
			if (!bullet.isVisible()) { // 复用已经隐藏的子弹
				bullet.setSpeed(0, -5);
				bullet.setCenterPosition(getX() + getWidth() / 2, getY());
				bullet.setVisible(true);
				break; // 一次只发射一颗,处理一次即可
			}
		}
	}

	/**
	 * 发射3散弹
	 */
	private void fireShotGun3() {
		int fireCount = 0;
		// 遍历所有子弹
		for (Bullet bullet : bullets) {
			if (!bullet.isVisible()) { // 复用已经隐藏的子弹
				fireCount++;
				switch (fireCount) {
					case 1:
						bullet.setSpeed(-1, -4.5f);
						break;
					case 2:
						bullet.setSpeed(0, -5);
						break;
					case 3:
						bullet.setSpeed(1, -4.5f);
						break;
				}
				bullet.setCenterPosition(getX() + getWidth() / 2, getY());
				bullet.setVisible(true);

				// 发射3颗子弹后结束循环
				if (fireCount == 3) {
					break;
				}
			}
		}
	}

	/**
	 * 发射5散弹
	 */
	private void fireShotGun5() {
		int fireCount = 0;
		// 遍历所有子弹
		for (Bullet bullet : bullets) {
			if (!bullet.isVisible()) { // 复用已经隐藏的子弹
				fireCount++;
				switch (fireCount) {
					case 1:
						bullet.setSpeed(-2, -3.75f);
						break;
					case 2:
						bullet.setSpeed(-1, -4.5f);
						break;
					case 3:
						bullet.setSpeed(0, -5);
						break;
					case 4:
						bullet.setSpeed(1, -4.5f);
						break;
					case 5:
						bullet.setSpeed(2, -3.75f);
						break;
				}
				bullet.setCenterPosition(getX() + getWidth() / 2, getY());
				bullet.setVisible(true);

				// 发射3颗子弹后结束循环
				if (fireCount == 5) {
					break;
				}
			}
		}
	}

	/**
	 * 对自身造成伤害
	 *
	 * @param damage 伤害数值
	 */
	public void takeDamage(int damage) {
		// 调试模式
		if(GameUtil.INSTANCE.isInvincibleMode()){
			return;
		}

		// 正常处理
		life -= damage;
		if (life <= 0) {
			life = 0;
			setVisible(false);
			isDead = true;

			// 爆炸效果
			if (blast != null) {
				blast.setCenterPosition(getCenterX(), getCenterY());
				blast.setVisible(true);
			}
		}
	}

	/**
	 * 逻辑操作
	 */
	@Override
	public void doLogic() {
		super.doLogic();

		// 开火的处理
		if (!isDead() && getStep() % fireInterval == 0) {
			if (bullets != null) {
				// 根据不同武器选择不同的开火方式
				switch (weaponType) {
					case NORMAL:
						fireNormal();
						break;
					case SHOTGUN3:
						fireShotGun3();
						break;
					case SHOTGUN5:
						fireShotGun5();
						break;
				}
			}
		}

		// 子弹逻辑
		if (bullets != null) {
			for (Bullet bullet : bullets) {
				bullet.doLogic();
			}
		}

		// 爆炸逻辑
		if (blast != null) {
			blast.doLogic();
		}
	}

	/**
	 * 绘制操作
	 *
	 * @param canvas 画布对象
	 */
	@Override
	public void doDraw(Canvas canvas) {
		super.doDraw(canvas);

		// 子弹绘制
		if (bullets != null) {
			for (Bullet bullet : bullets) {
				bullet.doDraw(canvas);
			}
		}

		// 爆炸绘制
		if (blast != null) {
			blast.doDraw(canvas);
		}
	}

	/**
	 * 越界处理
	 */
	@Override
	public void outOfBounds() {
		if (getX() < 0) {
			setX(0);
		} else if (getX() + getWidth() > ScreenUtil.INSTANCE.getScreenWidth()) {
			setX((int) (ScreenUtil.INSTANCE.getScreenWidth() - getWidth()));
		}
		if (getY() < 0) {
			setY(0);
		} else if (getY() + getHeight() > ScreenUtil.INSTANCE.getScreenHeight()) {
			setY((int) (ScreenUtil.INSTANCE.getScreenHeight() - getHeight()));
		}

	}
}
