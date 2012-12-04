package cn.zytec.applink;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Quit extends Activity implements OnClickListener {

	private EditText pwd;
	private Button ok;
	private Button cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quit);
		
		findView();
		addListener();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_ok_bt:
			String password = pwd.getText().toString();
			if(password.equals(App.password)) {
				TopFloatService.wm.removeView(TopFloatService.view);
				TopFloatService.tfs.stopSelf();
				App.exit();
			} else {
				pwd.setText("");
			}
			break;
		case R.id.quit_cancel:
			finish();
			break;
		default:
			break;
		}
		
	}
	private void addListener() {
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
	}

	private void findView() {
		pwd = (EditText) findViewById(R.id.quit_password_et);
		ok = (Button) findViewById(R.id.quit_ok_bt);
		cancel = (Button) findViewById(R.id.quit_cancel);
	}

	
}
