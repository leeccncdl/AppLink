package cn.zytec.applink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Introduce extends Activity {

	private TextView introduce;
	private String[] s = {"桃源街道互动服务终端","BankOfChina","教务手册","金融业务偕行工具"};
	private int[] introduceId = {R.string.taoyuan,R.string.boc,R.string.jiaowu,R.string.jinrong};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduce);
		
		Intent intent = getIntent();
		String appName = intent.getStringExtra("appName");
//		System.out.println("appName:" + appName);
		introduce = (TextView) findViewById(R.id.introduce_tv);
		
		for(int i=0;i<s.length;i++) {
			if(appName.equals(s[i])) {
				introduce.setText(introduceId[i]);
			}
		}
				
		

		
	}

}
