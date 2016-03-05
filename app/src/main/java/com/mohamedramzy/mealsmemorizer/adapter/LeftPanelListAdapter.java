package com.mohamedramzy.mealsmemorizer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamedramzy.mealsmemorizer.R;
import com.mohamedramzy.mealsmemorizer.utility.LeftPanelItem;

/**
 * Created by mmahfouz on 3/5/2016.
 */
public class LeftPanelListAdapter extends ArrayAdapter<LeftPanelItem> {

    Context mContext;

    public LeftPanelListAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LeftPanelItem item = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.left_panel_list_item, null);

        TextView mTitle = (TextView) view.findViewById(R.id.left_panel_title);
        ImageView iconImageView = (ImageView) view.findViewById(R.id.left_panel_icon);

        mTitle.setText(item.getTitle());
        iconImageView.setImageResource(item.getIcon());

        return view;
    }

}