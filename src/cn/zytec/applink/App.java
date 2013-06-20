package cn.zytec.applink;

import java.util.ArrayList;
import java.util.List;
import android.app.Application;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.WindowManager;

public class App extends Application {

	/** 
	* @Fields password : 退出时的密码 
	*/ 
	public static String password = "allrunzy";
	
	/** 
	* @Fields wmParams : WindowManager的布局参数
	*/ 
	public static WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
	
	/** 
	* @Fields pm : 包管理器 
	*/ 
	public static PackageManager pm;
	/** 
	* @Fields MAXAPPNUM : 最大包括应用数目 
	*/ 
	private static int MAXAPPNUM = 10;
	/** 
	* @Fields resolveInfoList : 保存显示应用的信息列表 
	*/ 
	public static List<ResolveInfo> resolveInfoList = new ArrayList<ResolveInfo>(
			MAXAPPNUM);
	/** 
	* @Fields currentPosition : 当前列表索引值
	*/ 
	private static int currentPosition = 0;

	/** 
	* @Title: addAppInfo 
	* @Description: 添加应用信息到列表中
	* @param @param appInfo  应用信息对象
	* @return void 
	* @throws 
	*/
	public static void addAppInfo(ResolveInfo appInfo) {
		//遍历列表，避免重复添加
		for(int i=0;i<resolveInfoList.size();i++) {
			if(resolveInfoList.get(i)!=null && resolveInfoList.get(i).activityInfo.packageName.equals(appInfo.activityInfo.packageName)) {
				return ;
			}
		}
		
		//如果列表已满，从列表前出入新的应用信息
		if (currentPosition == MAXAPPNUM) {
			currentPosition = 0;
		}
		resolveInfoList.set(currentPosition, appInfo);
		currentPosition++;
	}
	
	/** 
	* @Title: removeAppInfo 
	* @Description: 移除应用信息
	* @param position 目标索引值   
	* @return void 
	* @throws 
	*/
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
		for (int i = 0; i < MAXAPPNUM; i++) {
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
