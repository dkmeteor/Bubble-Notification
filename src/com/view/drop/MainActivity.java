package com.view.drop;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.view.drop.DropCover.OnDragCompeteListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        CoverManager.getInstance().init(this);

        ListView mList = (ListView) findViewById(R.id.list);
        mList.setAdapter(new DemoAdapter());
        
        CoverManager.getInstance().setMaxDragDistance(100);
        CoverManager.getInstance().setExplosionTime(300);
    }

    class DemoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 99;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_list_item, null);
            WaterDrop drop = (WaterDrop) convertView.findViewById(R.id.drop);
            drop.setOnDragCompeteListener(new OnDragCompeteListener() {

                @Override
                public void onDrag() {
                    Toast.makeText(MainActivity.this, "remove:" + position, Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }
}
