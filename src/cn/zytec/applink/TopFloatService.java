package cn.zytec.applink;

import java.io.IOException;
import java.io.OutputStream;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.zytec.applink.animation.SpringAnimation;
import cn.zytec.applink.animation.ZoomAnimation;

public class TopFloatService extends Service implements OnClickListener,
		OnLongClickListener {
	
	private static int centerCircleHeight = 100;
	private static int upMenuHerght = 80;
	private static int leftItemWidth = 48;
	
	//在QuitActivity关闭 service
	public static TopFloatService tfs;
	
	private int sysVersion = Integer.parseInt(VERSION.SDK);
	public static WindowManager wm = null;
	private WindowManager.LayoutParams wmParams = null;
	public static  View view;

	private static ImageView[] iv = new ImageView[10];
	private static TextView[] tv = new TextView[10];
	private static LinearLayout[] linearlayout = new LinearLayout[10];
	
	private LinearLayout upLinearLaout;
	private LinearLayout downLinearLaout;
	
	private boolean isMenuShowing;
	private GestureDetector gestureDetector;

	private ImageView ivCenter;
	private ImageView back;
	private ImageView menu;
	private ImageView home;
//	private ImageView quit;
	private LinearLayout rightCotent;

	private Process mProcess;
	private OutputStream mProcessOs;

	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	
	@Override
	public void onCreate() {
		tfs = this;
		super.onCreate();
		view = LayoutInflater.from(this).inflate(R.layout.floating, null);
		findView();
		createView();
		setListener();
		gestureDetector = new GestureDetector(this, new GestureListener());
		
		if (sysVersion < 14) {
			try {
				mProcess = Runtime.getRuntime().exec("su");
				mProcessOs = mProcess.getOutputStream();
			} catch (IOException e) {
//				 throw new
//				 RuntimeException("Failed to start SUPER USER process",
//				 e);
				rightCotent.setVisibility(View.GONE);
			}
		}

	}
	
	public static void refreshIcon() {
		for (int i = 0; i < App.resolveInfoList.size(); i++) {
			if (App.resolveInfoList.get(i) != null) {
				linearlayout[i].setVisibility(View.VISIBLE);
				iv[i].setImageDrawable(App.resolveInfoList.get(i).activityInfo
						.loadIcon(App.pm));

				tv[i].setText(App.resolveInfoList.get(i).activityInfo
						.loadLabel(App.pm));

			} else {
				linearlayout[i].setVisibility(View.GONE);

			}
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		int position = 99;
		switch (v.getId()) {
		case R.id.app_one_iv:
			position = 0;
			break;
		case R.id.app_two_iv:
			position = 1;
			break;
		case R.id.app_three_iv:
			position = 2;
			break;
		case R.id.app_four_iv:
			position = 3;
			break;
		case R.id.app_five_iv:
			position = 4;
			break;
		case R.id.app_six_iv:
			position = 5;
			break;
		case R.id.app_seven_iv:
			position = 6;
			break;
		case R.id.app_eight_iv:
			position = 7;
			break;
		case R.id.app_nine_iv:
			position = 8;
			break;
		case R.id.app_ten_iv:
			position = 9;
			break;
		case R.id.back_iv:
			btnClick(KeyEvent.KEYCODE_BACK);
			break;
		case R.id.menu_iv:
			btnClick(KeyEvent.KEYCODE_MENU);
			break;
		case R.id.home_iv:
			btnClick(KeyEvent.KEYCODE_HOME);
			//Intent模拟返回Home
//			Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
//
//            mHomeIntent.addCategory(Intent.CATEGORY_HOME);
//            mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            startActivity(mHomeIntent);
			break;
//		case R.id.quit:
//			Intent quitIntent = new Intent(TopFloatService.this,
//					Quit.class);
//			quitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(quitIntent);
//			break;
		default:
			break;
		}
		if(position!=99){
			intent = getDesAppIntent(position);
			if (intent != null) {
				startActivity(intent);
			}
			
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if(isMenuShowing) {
			SpringAnimation.startAnimations(
					this.upLinearLaout, ZoomAnimation.Direction.HIDE);
			SpringAnimation.startAnimations(
					this.downLinearLaout, ZoomAnimation.Direction.HIDE);
			isMenuShowing = false;
		}
		int longClickPosition = 1000;
		boolean iscenterLongClick = false;
		switch (v.getId()) {
		case R.id.center_iv:
			iscenterLongClick = true;
			break;
		case R.id.app_one_iv:
			longClickPosition = 0;
			break;
		case R.id.app_two_iv:
			longClickPosition = 1;
			break;
		case R.id.app_three_iv:
			longClickPosition = 2;
			break;
		case R.id.app_four_iv:
			longClickPosition = 3;
			break;
		case R.id.app_five_iv:
			longClickPosition = 4;
			break;
		case R.id.app_six_iv:
			longClickPosition = 5;
			break;
		case R.id.app_seven_iv:
			longClickPosition = 6;
			break;
		case R.id.app_eight_iv:
			longClickPosition = 7;
			break;
		case R.id.app_nine_iv:
			longClickPosition = 8;
			break;
		case R.id.app_ten_iv:
			longClickPosition = 9;
			break;

		default:
			break;
		}
		//跳转到新的Activity，传递AppName，在新的Activity里匹配应用名称，有对应介绍则显示，无对应介绍做相应提示
		if (!iscenterLongClick && longClickPosition!=1000 && App.resolveInfoList.get(longClickPosition)!=null) {
//			App.removeAppInfo(longClickPosition);
//			refreshIcon();
			
			Intent intent = new Intent(this,Introduce.class);
			intent.putExtra("appName",App.resolveInfoList.get(longClickPosition).activityInfo
					.loadLabel(App.pm));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			startActivity(intent);

		}
		return true;
	}

	protected void btnClick(int keyCode) {
		simulateKey(keyCode);
	}

	protected void simulateKey(int keyCode) {
		if (sysVersion < 14) {
			try {
				String cmd = "input keyevent " + keyCode + "\n";
				mProcessOs.write(cmd.getBytes());
				mProcessOs.flush();
			} catch (IOException e) {
				// throw new RuntimeException("Failed to simulate keyevent", e);
				rightCotent.setVisibility(View.GONE);
			}
		} else {
			try {
				String cmd = "input keyevent " + keyCode + "\n";
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				// throw new RuntimeException("Failed to simulate keyevent", e);
				rightCotent.setVisibility(View.GONE);
			}

		}

	}

	private Intent getDesAppIntent(int appResolveInfoListPosition) {
		Intent intent = null;
		if (App.resolveInfoList.get(0) != null) {
			intent = App.pm.getLaunchIntentForPackage(App.resolveInfoList
					.get(appResolveInfoListPosition).activityInfo.packageName);
		}
		return intent;
	}

	private void findView() {
		ivCenter = (ImageView) view.findViewById(R.id.center_iv);
		iv[0] = (ImageView) view.findViewById(R.id.app_one_iv);
		iv[1] = (ImageView) view.findViewById(R.id.app_two_iv);
		iv[2] = (ImageView) view.findViewById(R.id.app_three_iv);
		iv[3] = (ImageView) view.findViewById(R.id.app_four_iv);
		iv[4] = (ImageView) view.findViewById(R.id.app_five_iv);
		iv[5] = (ImageView) view.findViewById(R.id.app_six_iv);
		iv[6] = (ImageView) view.findViewById(R.id.app_seven_iv);
		iv[7] = (ImageView) view.findViewById(R.id.app_eight_iv);
		iv[8] = (ImageView) view.findViewById(R.id.app_nine_iv);
		iv[9] = (ImageView) view.findViewById(R.id.app_ten_iv);
		tv[0] = (TextView) view.findViewById(R.id.app_one_tv);
		tv[1] = (TextView) view.findViewById(R.id.app_two_tv);
		tv[2] = (TextView) view.findViewById(R.id.app_three_tv);
		tv[3] = (TextView) view.findViewById(R.id.app_four_tv);
		tv[4] = (TextView) view.findViewById(R.id.app_five_tv);
		tv[5] = (TextView) view.findViewById(R.id.app_six_tv);
		tv[6] = (TextView) view.findViewById(R.id.app_seven_tv);
		tv[7] = (TextView) view.findViewById(R.id.app_eight_tv);
		tv[8] = (TextView) view.findViewById(R.id.app_nine_tv);
		tv[9] = (TextView) view.findViewById(R.id.app_ten_tv);
		linearlayout[0] = (LinearLayout) view.findViewById(R.id.app_one_ll);
		linearlayout[1] = (LinearLayout) view.findViewById(R.id.app_two_ll);
		linearlayout[2] = (LinearLayout) view.findViewById(R.id.app_three_ll);
		linearlayout[3] = (LinearLayout) view.findViewById(R.id.app_four_ll);
		linearlayout[4] = (LinearLayout) view.findViewById(R.id.app_five_ll);
		linearlayout[5] = (LinearLayout) view.findViewById(R.id.app_six_ll);
		linearlayout[6] = (LinearLayout) view.findViewById(R.id.app_seven_ll);
		linearlayout[7] = (LinearLayout) view.findViewById(R.id.app_eight_ll);
		linearlayout[8] = (LinearLayout) view.findViewById(R.id.app_nine_ll);
		linearlayout[9] = (LinearLayout) view.findViewById(R.id.app_ten_ll);

		back = (ImageView) view.findViewById(R.id.back_iv);
		menu = (ImageView) view.findViewById(R.id.menu_iv);
		home = (ImageView) view.findViewById(R.id.home_iv);
//		quit = (ImageView) view.findViewById(R.id.quit);

		rightCotent = (LinearLayout) view.findViewById(R.id.right_content_ll);
		
		upLinearLaout =(LinearLayout) view.findViewById(R.id.up_menu_ll);
		downLinearLaout =(LinearLayout) view.findViewById(R.id.down_menu_ll);

	}

	private void createView() {
		wm = (WindowManager) getApplicationContext().getSystemService("window");
		wmParams = App.wmParams;
		// wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		// wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		wmParams.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.alpha = (float) 0.8;

		wm.addView(view, wmParams);

		ivCenter.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float currentIconNum = 0;//Caculate diatance use
				for (int i = 0; i < App.resolveInfoList.size(); i++) {
					if (App.resolveInfoList.get(i) != null) {
						currentIconNum++;
					}
				}
				if (currentIconNum < 6) {
					currentIconNum = 5f;
				}

				gestureDetector.onTouchEvent(event);
				
				x = event.getRawX() - (leftItemWidth * currentIconNum + 32);
				y = event.getRawY() + (centerCircleHeight + upMenuHerght + 10);

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					mTouchStartX = event.getX();
					mTouchStartY = event.getY() + view.getHeight();
					break;
				case MotionEvent.ACTION_MOVE:
					updateViewPosition();
					break;
				case MotionEvent.ACTION_UP:
					if (Math.abs(event.getX() - mTouchStartX) > 30) {
						updateViewPosition();
					}
					mTouchStartX = mTouchStartY = 0;
					break;
				}
				return false;
			}

		});
	}

	private void updateViewPosition() {
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(view, wmParams);
	}

	private void setListener() {
		ivCenter.setOnLongClickListener(this);
		// ivCenter.setOnClickListener(this);
		iv[0].setOnClickListener(this);
		iv[1].setOnClickListener(this);
		iv[2].setOnClickListener(this);
		iv[3].setOnClickListener(this);
		iv[4].setOnClickListener(this);
		iv[5].setOnClickListener(this);
		iv[6].setOnClickListener(this);
		iv[7].setOnClickListener(this);
		iv[8].setOnClickListener(this);
		iv[9].setOnClickListener(this);
		iv[0].setOnLongClickListener(this);
		iv[1].setOnLongClickListener(this);
		iv[2].setOnLongClickListener(this);
		iv[3].setOnLongClickListener(this);
		iv[4].setOnLongClickListener(this);
		iv[5].setOnLongClickListener(this);
		iv[6].setOnLongClickListener(this);
		iv[7].setOnLongClickListener(this);
		iv[8].setOnLongClickListener(this);
		iv[9].setOnLongClickListener(this);
		back.setOnClickListener(this);
		menu.setOnClickListener(this);
//		quit.setOnClickListener(this);
//		menu.setOnLongClickListener(this);
		home.setOnClickListener(this);
		
		//为每个弹出菜单设置自定义监听器
		for (int i = 0; i < upLinearLaout.getChildCount(); i++) {
			upLinearLaout.getChildAt(i).setOnClickListener(
					new SpringMenuLauncher(null,upLinearLaout.getChildAt(i).getId()));
		}
		for (int i = 0; i < downLinearLaout.getChildCount(); i++) {
			downLinearLaout.getChildAt(i).setOnClickListener(
					new SpringMenuLauncher(null,downLinearLaout.getChildAt(i).getId()));
		}
	}
	
	private void showMenus() {
		int currentIconNum = 0;
		for (int i = 0; i < App.resolveInfoList.size(); i++) {
			if (App.resolveInfoList.get(i) != null) {
				currentIconNum++;
			}
		}
		if (currentIconNum <= 4) {
			currentIconNum = 4;
		} else {
			currentIconNum--;
		}
		int MarginLeft = leftItemWidth * currentIconNum;
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(48, 48);
		lp.setMargins(MarginLeft, 42, 0, 0);
		upLinearLaout.getChildAt(0).setLayoutParams(lp);
		
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(48, 48);
		lp2.setMargins(MarginLeft, 0, 0, 0);
		downLinearLaout.getChildAt(0).setLayoutParams(lp2);

		if (!isMenuShowing) {
//			upLinearLaout.setVisibility(View.VISIBLE);
//			downLinearLaout.setVisibility(View.VISIBLE);
			SpringAnimation.startAnimations(
					this.upLinearLaout, ZoomAnimation.Direction.SHOW);
			SpringAnimation.startAnimations(
					this.downLinearLaout, ZoomAnimation.Direction.SHOW);
//			this.imageViewPlus.startAnimation(this.animRotateClockwise);
		} else {
			SpringAnimation.startAnimations(
					this.upLinearLaout, ZoomAnimation.Direction.HIDE);
			SpringAnimation.startAnimations(
					this.downLinearLaout, ZoomAnimation.Direction.HIDE);
//			this.imageViewPlus.startAnimation(this.animRotateAntiClockwise);
//			upLinearLaout.setVisibility(View.GONE);
//			downLinearLaout.setVisibility(View.GONE);
		}
		
		isMenuShowing = !isMenuShowing;
	}

	class GestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			
			showMenus();
//			Intent intent = new Intent(TopFloatService.this,
//					ChoseAppActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			if (intent != null) {
//				startActivity(intent);
//			}
			return super.onDoubleTap(e);

		}
	}
	
	private class SpringMenuLauncher implements OnClickListener {
		private final Class<?> cls;
		private int viewid;

		private SpringMenuLauncher(Class<?> c,int viewId) {
			this.cls = c;
			this.viewid = viewId;
		}

		
		public void onClick(View v) {
			showMenus();
			switch (viewid) {
			case R.id.above_menu_one_iv:

				Intent intent = new Intent(TopFloatService.this,
						ChoseAppActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if (intent != null) {
					startActivity(intent);
				}
				break;
			case R.id.above_menu_two_iv:

				break;
			case R.id.above_menu_three_iv:

				Intent quitIntent = new Intent(TopFloatService.this, Quit.class);
				quitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(quitIntent);
			case R.id.beneath_menu_one_iv:
				for (int i = 0; i < 10; i++) {
					App.resolveInfoList.set(i, null);
				}
				refreshIcon();
				break;
			case R.id.beneath_menu_two_iv:

				break;
			case R.id.beneath_menu_three_iv:

				break;
			default:
				break;
			}

		}
	}
}
