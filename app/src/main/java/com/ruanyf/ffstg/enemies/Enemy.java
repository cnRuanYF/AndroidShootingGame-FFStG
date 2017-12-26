package com.ruanyf.ffstg.enemies;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.ruanyf.ffstg.Blast;
import com.ruanyf.ffstg.Bullet;
import com.ruanyf.ffstg.Sprite;
import com.ruanyf.ffstg.WeaponType;

import java.util.List;

/**
 * 敌人类
 * Created by Feng on 2017/12/23.
 */
public class Enemy extends Sprite {

	private WeaponType weaponType; // 武器种类
	private List<Bullet> bullets; // 弹夹(用于复用子弹节省内存)

	private int fireInterval; // 发射间隔

	private Blast blast; // 爆炸效果

	private boolean isDead;
	private int life;

	/**
	 * 构建敌人Sprite
	 *
	 * @param bitmap 位图对象
	 */
	public Enemy(Bitmap bitmap) {
		super(bitmap);
		weaponType = WeaponType.NORMAL;
		fireInterval = 30;
	}

	public Enemy(Bitmap bitmap, WeaponType weaponType) {
		this(bitmap);
		this.weaponType = weaponType;
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
				bullet.setSpeed(0, 5);
				bullet.setCenterPosition(getX() + getWidth() / 2, getY() + getHeight());
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
						bullet.setSpeed(-1, 4.5f);
						break;
					case 2:
						bullet.setSpeed(0, 5);
						break;
					case 3:
						bullet.setSpeed(1, 4.5f);
						break;
				}
				bullet.setCenterPosition(getX() + getWidth() / 2, getY() + getHeight());
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
						bullet.setSpeed(-2, 3.75f);
						break;
					case 2:
						bullet.setSpeed(-1, 4.5f);
						break;
					case 3:
						bullet.setSpeed(0, 5);
						break;
					case 4:
						bullet.setSpeed(1, 4.5f);
						break;
					case 5:
						bullet.setSpeed(2, 3.75f);
						break;
				}
				bullet.setCenterPosition(getX() + getWidth() / 2, getY() + getHeight());
				bullet.setVisible(true);

				// 发射3颗子弹后结束循环
				if (fireCount == 5) {
					break;
				}
			}
		}
	}

	/**
	 * 是否可复用
	 *
	 * @return 如果可复用返回true
	 */
	public boolean isReusable() {
		// 自身显示时不可复用
		if (isVisible()) {
			return false;
		}

		// 还有子弹显示时不可复用
		if (bullets != null) {
			for (Bullet bullet : bullets) {
				if (bullet.isVisible()) {
					return false;
				}
			}
		}

		// 爆炸效果显示时不可复用
		if (blast != null && blast.isVisible()) {
			return false;
		}

		return true;
	}

	/**
	 * 对自身造成伤害
	 *
	 * @param damage 伤害数值
	 */
	public void takeDamage(int damage) {
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

		// 开火的处理(可见时才能开火)
		if (isVisible() && getStep() % fireInterval == 0) {
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

}
