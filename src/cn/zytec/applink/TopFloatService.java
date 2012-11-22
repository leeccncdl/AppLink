package cn.zytec.applink;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class TopFloatService extends Service implements OnClickListener {

	WindowManager wm = null;
	WindowManager.LayoutParams wmParams = null;
	View view;
	static ImageView[] iv = new ImageView[4];
	ImageView ivCenter;
//	private float mTouchStartX;
//	private float mTouchStartY;
//	private float x;
//	private float y;
	
	public static void refreshIcon() {
//		System.out.println("refresh icon!");
		for(int i=0;i<App.resolveInfoList.size();i++) {
			if(App.resolveInfoList.get(i)!=null) {
				iv[i].setImageDrawable(App.resolveInfoList.get(i).activityInfo.loadIcon(App.pm));
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
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.center_iv:
			intent = new Intent(TopFloatService.this,ChoseAppActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			break;
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
		default:
			break;
		}
		if(intent != null) {		
			startActivity(intent);
		}
	}
	
	private Intent getDesAppIntent(int appResolveInfoListPosition) {
		Intent intent = null;
		if(App.resolveInfoList.get(1) != null) {
			intent = App.pm.getLaunchIntentForPackage(App.resolveInfoList.get(appResolveInfoListPosition).activityInfo.packageName);
		}
		return intent;
	}
	
	private void findView() {	
		ivCenter = (ImageView) view.findViewById(R.id.center_iv);
		iv[0] = (ImageView) view.findViewById(R.id.app_one_iv);
		iv[1] = (ImageView) view.findViewById(R.id.app_two_iv);
		iv[2] = (ImageView) view.findViewById(R.id.app_three_iv);
		iv[3]= (ImageView) view.findViewById(R.id.app_four_iv);
	}

	private void createView() {
		wm = (WindowManager) getApplicationContext().getSystemService("window");
		wmParams = App.wmParams;
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.alpha = (float) 0.6;

		wm.addView(view, wmParams);

//		view.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				System.out.println("Click");
//
//			}
//		});


		// view.setOnTouchListener(new OnTouchListener() {
		// public boolean onTouch(View v, MotionEvent event) {
		// x = event.getRawX();
		// y = event.getRawY()-25 ;
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// mTouchStartX = event.getX();
		// mTouchStartY = event.getY()+view.getHeight()/2;
		// break;
		// case MotionEvent.ACTION_MOVE:
		// updateViewPosition();
		// break;
		// case MotionEvent.ACTION_UP:
		// updateViewPosition();
		// mTouchStartX = mTouchStartY = 0;
		// break;
		// }
		// return true;
		// }
		//
		// });
	}

//	private void updateViewPosition() {
//		wmParams.x = (int) (x - mTouchStartX);
//		wmParams.y = (int) (y - mTouchStartY);
//		wm.updateViewLayout(view, wmParams);
//	}
	private void setListener() {
		ivCenter.setOnClickListener(this);
		iv[0].setOnClickListener(this);
		iv[1].setOnClickListener(this);
		iv[2].setOnClickListener(this);
		iv[3].setOnClickListener(this);
	}

}
