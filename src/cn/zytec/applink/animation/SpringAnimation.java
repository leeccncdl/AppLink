package cn.zytec.applink.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import cn.zytec.applink.ImageButtonExtend;
import cn.zytec.applink.R;

public class SpringAnimation extends ZoomAnimation {

	private static int yOffset = 100;
	public static final int DURATION = 300;

	public SpringAnimation(Direction direction, long duration, View view) {
		super(direction, duration, new View[] { view });
	}

	public static void startAnimations(ViewGroup viewgroup,
			ZoomAnimation.Direction direction) {


		switch (direction) {
		case HIDE:
			startShrinkAnimations(viewgroup);
			break;
		case SHOW:
			startEnlargeAnimations(viewgroup);
			break;
		}
	}

	private static void startEnlargeAnimations(ViewGroup viewgroup) {
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			if (viewgroup.getChildAt(i) instanceof ImageButtonExtend) {
				ImageButtonExtend inoutimagebutton = (ImageButtonExtend) viewgroup
						.getChildAt(i);
				SpringAnimation animation = new SpringAnimation(
						ZoomAnimation.Direction.HIDE, DURATION,
						inoutimagebutton);
				animation.setStartOffset((i * 200)
						/ (-1 + viewgroup.getChildCount()));
				animation.setInterpolator(new OvershootInterpolator(4F));
				inoutimagebutton.startAnimation(animation);
			}
		}
	}

	private static void startShrinkAnimations(ViewGroup viewgroup) {
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			if (viewgroup.getChildAt(i) instanceof ImageButtonExtend) {
				ImageButtonExtend inoutimagebutton = (ImageButtonExtend) viewgroup
						.getChildAt(i);
				SpringAnimation animation = new SpringAnimation(
						ZoomAnimation.Direction.SHOW, DURATION,
						inoutimagebutton);
				animation.setStartOffset((100 * ((-1 + viewgroup
						.getChildCount()) - i))
						/ (-1 + viewgroup.getChildCount()));
				animation.setInterpolator(new AnticipateOvershootInterpolator(
						2F));
				inoutimagebutton.startAnimation(animation);
			}
		}
	}

	@Override
	protected void addShrinkAnimation(View[] views) {
		// TODO Auto-generated method stub
		float xDistance = 0.0f;
		float yDistance = yOffset;
		if (views[0].getId() == R.id.beneath_menu_one_iv
				|| views[0].getId() == R.id.beneath_menu_two_iv
				|| views[0].getId() == R.id.beneath_menu_three_iv) {
			yDistance = -yOffset;
		}
		switch (views[0].getId()) {
		case R.id.above_menu_one_iv:
		case R.id.beneath_menu_one_iv:
			xDistance = 104.0f;
			break;
		case R.id.above_menu_three_iv:
		case R.id.beneath_menu_three_iv:
			xDistance = -104.0f;
		default:
			break;
		}
		addAnimation(new TranslateAnimation(xDistance, 0F, yDistance, 0F));
	}

	@Override
	protected void addEnlargeAnimation(View[] views) {
		// TODO Auto-generated method stub
		float xDistance = 0.0f;
		float yDistance = yOffset;
		if (views[0].getId() == R.id.beneath_menu_one_iv
				|| views[0].getId() == R.id.beneath_menu_two_iv
				|| views[0].getId() == R.id.beneath_menu_three_iv) {
			yDistance = -yOffset;
		}
		switch (views[0].getId()) {
		case R.id.above_menu_one_iv:
		case R.id.beneath_menu_one_iv:
			xDistance = 104.0f;
			break;
		case R.id.above_menu_three_iv:
		case R.id.beneath_menu_three_iv:
			xDistance = -104.0f;
		default:
			break;
		}
		addAnimation(new TranslateAnimation(0F, xDistance, 0F, yDistance));
	}
}
