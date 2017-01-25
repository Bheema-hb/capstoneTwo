package com.sakhatech.parkme.Activity.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.sakhatech.parkme.Activity.home.model.Menu;
import com.sakhatech.spotizen.R;

import java.util.ArrayList;

/**
 * Created by Bheema.
 */
public class NavigationMenuAdapter extends BaseAdapter {
    private final Context mContext;
    LayoutInflater inflater = null;
    ArrayList<Menu> menus = new ArrayList<>();

    public NavigationMenuAdapter(Context context, ArrayList<Menu> menus) {
        mContext = context;
        this.menus = menus;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Menu getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.menu_view, null);
        if (getItem(position).menuId == 6) {
            view.findViewById(R.id.menu_divider_bottom).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.menu_divider_bottom).setVisibility(View.GONE);
        }
        ((TextView) view.findViewById(R.id.menu_name)).setText(getItem(position).name);
        if (getItem(position).menuId == 7) {
            view.findViewById(R.id.menu_image).setVisibility(View.GONE);
            view.findViewById(R.id.menu_name).setVisibility(View.GONE);
            view.findViewById(R.id.general).setVisibility(View.VISIBLE);
        } else {
            ((ImageView) view.findViewById(R.id.menu_image)).setImageResource(getItem(position).resId);
        }

        return view;

    }

    public void setMenus(ArrayList<Menu> menus) {
        this.menus = menus;
    }
}
