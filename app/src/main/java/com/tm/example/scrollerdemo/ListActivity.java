package com.tm.example.scrollerdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.example.scrollerdemo.view.SlidingMenuLayout;

import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = ListActivity.class.getSimpleName();

    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new MyAdapter(ListActivity.this));
    }

    private static class MyAdapter extends BaseAdapter {
        private ViewHolder viewHolder;
        private Context context;

        private Map<Integer, SlidingMenuLayout> map;
        private int showMenuPosition;

        MyAdapter(Context context) {
            this.context = context;
            map = new HashMap<>();
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, final ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
                viewHolder = new ViewHolder();
                view.setTag(viewHolder);
            } else {
                if (view.getTag() != null && view.getTag() instanceof ViewHolder)
                    viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.tvEdit = (TextView) view.findViewById(R.id.tv_edit);
            viewHolder.tvDelete = (TextView) view.findViewById(R.id.tv_delete);
            viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "edit " + String.valueOf(i), Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "delete " + String.valueOf(i), Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.slidingMenuLayout = (SlidingMenuLayout) view.findViewById(R.id.smlayout);
            viewHolder.slidingMenuLayout.setTag(i);
            map.put(i, viewHolder.slidingMenuLayout);

            viewHolder.slidingMenuLayout.setOnSlidingMenuListener(new SlidingMenuLayout.OnSlidingMenuListener() {

                @Override
                public void onMenuShow() {
                    Log.e(TAG, "========menuShow");
                    showMenuPosition = i;
                }

                @Override
                public void onTouch() {
                    Log.e(TAG, "========onTouch");

                    if (showMenuPosition != i) {
                        for (int key : map.keySet()) {
                            map.get(key).hideMenu();
                        }
                    }
                }

            });

            viewHolder.tvContent = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.tvContent.setText("name" + i);

            return view;
        }
    }

    private static class ViewHolder {
        TextView tvContent;

        SlidingMenuLayout slidingMenuLayout;
        TextView tvEdit;
        TextView tvDelete;
    }
}
