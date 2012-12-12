package cn.zytec.applink;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoseAppActivity extends Activity {

	private List<ResolveInfo> mApps;
	private GridView mGrid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.chose_app);
		mApps = getNotSysApp();

		mGrid = (GridView) findViewById(R.id.myGrid);
		mGrid.setAdapter(new GridViewAdapter());

		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				System.out.println(position);
				App.addAppInfo(mApps.get(position));
				TopFloatService.refreshIcon();
			}

		});
	}

	private class GridViewAdapter extends BaseAdapter {

		public GridViewAdapter() {
		}

		@Override
		public int getCount() {
			return mApps.size();
		}

		@Override
		public Object getItem(int position) {
			return mApps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder vh = null;
			ResolveInfo info = mApps.get(position);
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = View.inflate(ChoseAppActivity.this,
						R.layout.chose_app_gridview_item, null);
				vh.iv = (ImageView) convertView
						.findViewById(R.id.gridview_item_iv);
				vh.tv = (TextView) convertView.findViewById(R.id.grid_view_tv);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.iv.setImageDrawable(info.activityInfo
					.loadIcon(getPackageManager()));
//			System.out.println("----"+info.activityInfo.loadLabel(getPackageManager())+"----");
			vh.tv.setText(info.activityInfo.loadLabel(getPackageManager()));
			return convertView;
		}

	}

	private class ViewHolder {
		private TextView tv;
		private ImageView iv;

	}

	private List<ResolveInfo> getNotSysApp() {
		List<ResolveInfo> allApps = null;

		List<ResolveInfo> notSysApps = new ArrayList<ResolveInfo>();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		allApps = getPackageManager().queryIntentActivities(mainIntent, 0);
		ResolveInfo rsInfo = null;
		for (int i = 0; i < allApps.size(); i++) {
			rsInfo = allApps.get(i);
			// 判断是否为非系统预装的应用 (大于0为系统预装应用，小于等于0为非系统应用)
			if ((rsInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				notSysApps.add(rsInfo);
			}
		}
		return notSysApps;
	}
}
