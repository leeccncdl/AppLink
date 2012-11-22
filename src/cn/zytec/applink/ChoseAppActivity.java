package cn.zytec.applink;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
		
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
        
        mGrid = (GridView) findViewById(R.id.myGrid);
        mGrid.setAdapter(new GridViewAdapter());
    
//        mGrid.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				System.out.println(position);
//				App.addAppInfo(mApps.get(position));
//				TopFloatService.refreshIcon();
//			}
//        	
//        });
	}

	private class GridViewAdapter extends BaseAdapter {

		public GridViewAdapter () {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			ResolveInfo info = mApps.get(position);
            if (convertView == null) {
            	vh = new ViewHolder();
				convertView = View.inflate(ChoseAppActivity.this,R.layout.chose_app_gridview_item, null);
				vh.iv = (ImageView) convertView.
				findViewById(R.id.gridview_item_iv);
				vh.tv = (TextView) convertView.
				findViewById(R.id.grid_view_tv);
				convertView.setTag(vh);
            } else {
            	vh = (ViewHolder) convertView.getTag();
            }
            vh.iv.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
            vh.tv.setText(info.activityInfo.loadLabel(getPackageManager()));
            //GridView的item不响应，现在这监听了            
            convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					System.out.println(position);
					App.addAppInfo(mApps.get(position));
					TopFloatService.refreshIcon();
					
				}
			});
            return convertView;
		}
		
	}
	
	private class ViewHolder{
		private TextView tv;
		private ImageView iv;

	}
}
