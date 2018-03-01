package com.newindiaawards.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.newindiaawards.R;
import com.newindiaawards.model.ImageItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Mahesh on 21/11/17.
 */

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList();

    int selected = 20;
    String mode;

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<ImageItem> data,String mode) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.mode = mode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.imageSelected = (ImageView) row.findViewById(R.id.imageSelected);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem item = data.get(position);


        if(mode.equalsIgnoreCase("landscape")){
            Picasso.with(context).load(new File(item.getImageString())).fit().into(holder.image);
        }else{
            Picasso.with(context).load(new File(item.getImageString())).rotate(90f).fit().into(holder.image);
        }


        if(position==selected)
        {
            holder.imageSelected.setImageResource(R.drawable.image_selected);

        }
        else{

            holder.imageSelected.setImageResource(R.drawable.default_select);
        }



        return row;
    }

    public void selectedPosition(int postion) {

        selected = postion;
    }

    public int getSelectedPostion() {

       return selected;
    }

    static class ViewHolder {
        ImageView image;
        ImageView imageSelected;
    }
}