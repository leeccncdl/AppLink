package cn.zytec.applink;

import java.util.ArrayList;
import java.util.List;
import android.app.Application;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.WindowManager;

public class App extends Application {

	public static String password = "allrunzy";
	public static WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
	public static PackageManager pm;
	private static int num = 10;
	public static List<ResolveInfo> resolveInfoList = new ArrayList<ResolveInfo>(
			num);
	private static int currentPosition = 0;

	public static void addAppInfo(ResolveInfo appInfo) {

		for(int i=0;i<resolveInfoList.size();i++) {
			if(resolveInfoList.get(i)!=null && resolveInfoList.get(i).activityInfo.packageName.equals(appInfo.activityInfo.packageName)) {
				return ;
			}
		}
		
		if (currentPosition == num) {
			currentPosition = 0;
		}
		resolveInfoList.set(currentPosition, appInfo);
		currentPosition++;
	}
	
	public static void removeAppInfo(int position) {
		if(resolveInfoList.get(0)!=null && position!=1000) {
			resolveInfoList.remove(resolveInfoList.get(position));
			resolveInfoList.add(null);
			currentPosition--;
		}
	}

	@Override
	public void onCreate() {
		pm = getPackageManager();
		for (int i = 0; i < num; i++) {
			resolveInfoList.add(null);
		}
		super.onCreate();
	}
	
	public static PackageManager getPM() {
		return pm;
	}
	
	public static void exit() {
		System.exit(0);
	}
}
