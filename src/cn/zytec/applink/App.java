package cn.zytec.applink;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.WindowManager;

public class App extends Application {

	public static WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
	public static PackageManager pm;
	private static int num = 4;
	public static List<ResolveInfo> resolveInfoList = new ArrayList<ResolveInfo>(
			num);
	private static int currentPosition = 0;

	public static void addAppInfo(ResolveInfo appInfo) {
		if (resolveInfoList.contains(appInfo)) {
			return;
		}
		if (currentPosition == num) {
			currentPosition = 0;
		}
		resolveInfoList.set(currentPosition, appInfo);
		currentPosition++;
	}

	@Override
	public void onCreate() {
		pm = getPackageManager();
		for (int i = 0; i < num; i++) {
			resolveInfoList.add(null);
		}
		super.onCreate();
	}
	
	public static void exit() {
		System.exit(0);
	}
}
