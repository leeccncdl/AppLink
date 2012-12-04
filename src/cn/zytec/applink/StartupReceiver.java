package cn.zytec.applink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub  
        Intent i = new Intent(context,MainActivity.class);  
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        //将intent以startActivity传送给操作系统  
        context.startActivity(i); 

	}

}
