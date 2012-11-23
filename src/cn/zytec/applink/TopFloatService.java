package cn.zytec.applink;

import java.io.IOException;
import java.io.OutputStream;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
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

public class TopFloatService extends Service implements OnClickListener,
		OnLongClickListener {

	WindowManager wm = null;
	WindowManager.LayoutParams wmParams = null;
	View view;
	static ImageView[] iv = new ImageView[4];

	private ImageView back;
	private ImageView menu;
	private ImageView home;
	private LinearLayout rightCotent;

	private Process mProcess;
	private OutputStream mProcessOs;

	ImageView ivCenter;
	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;

	public static void refreshIcon() {
		for (int i = 0; i < App.resolveInfoList.size(); i++) {
			if (App.resolveInfoList.get(i) != null) {
				iv[i].setImageDrawable(App.resolveInfoList.get(i).activityInfo
						.loadIcon(App.pm));
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		view = LayoutInflater.from(this).inflate(R.layout.floating, null);
		findView();
		createView();
		setListener();
		try {
			mProcess = Runtime.getRuntime().exec("su");
			mProcessOs = mProcess.getOutputStream();
		} catch (IOException e) {
			// throw new RuntimeException("Failed to start SUPER USER process",
			// e);

			rightCotent.setVisibility(View.GONE);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.app_one_iv:
			intent = getDesAppIntent(0);
			break;
		case R.id.app_two_iv:
			intent = getDesAppIntent(1);
			break;
		case R.id.app_three_iv:
			intent = getDesAppIntent(2);
			break;
		case R.id.app_four_iv:
			intent = getDesAppIntent(3);
			break;
		case R.id.back_iv:
			btnClick(KeyEvent.KEYCODE_BACK);
			break;
		case R.id.menu_iv:
			btnClick(KeyEvent.KEYCODE_MENU);
			break;
		case R.id.home_iv:
			btnClick(KeyEvent.KEYCODE_HOME);
			break;
		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	
	@Override
	public boolean onLongClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.center_iv:
			intent = new Intent(TopFloatService.this, ChoseAppActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (intent != null) {
				startActivity(intent);
			}
			break;
		case R.id.app_four_iv:
			wm.removeView(view);
			this.stopSelf();
			App.exit();
			break;
		default:
			break;
		}
		return false;
	}

	protected void btnClick(int keyCode) {
		simulateKey(keyCode);
	}

	protected void simulateKey(int keyCode) {
		try {
			String cmd = "input keyevent " + keyCode + "\n";
			mProcessOs.write(cmd.getBytes());
			mProcessOs.flush();
		} catch (IOException e) {
			throw new RuntimeException("Failed to simulate keyevent", e);
//			rightCotent.setVisibility(View.GONE);
		}
	}

	private Intent getDesAppIntent(int appResolveInfoListPosition) {
		Intent intent = null;
		if (App.resolveInfoList.get(1) != null) {
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

		back = (ImageView) view.findViewById(R.id.back_iv);
		menu = (ImageView) view.findViewById(R.id.menu_iv);
		home = (ImageView) view.findViewById(R.id.home_iv);

		rightCotent = (LinearLayout) view.findViewById(R.id.right_content_ll);

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
				x = event.getRawX() - (64 * 4 + 32);
				y = event.getRawY() + (100);
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
		iv[3].setOnLongClickListener(this);
		iv[0].setOnClickListener(this);
		iv[1].setOnClickListener(this);
		iv[2].setOnClickListener(this);
		iv[3].setOnClickListener(this);
		back.setOnClickListener(this);
		menu.setOnClickListener(this);
		home.setOnClickListener(this);
	}

}
