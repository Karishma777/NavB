package com.newindiaawards.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newindiaawards.R;
import com.newindiaawards.model.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Mahesh on 19/09/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<CategoryModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView image;


        public MyViewHolder(View view) {
            super(view);

            name=(TextView) view.findViewById(R.id.categoryTitle);
            image=(ImageView) view.findViewById(R.id.categoryImage);

        }
    }

    public CategoryAdapter(List<CategoryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CategoryModel model = list.get(position);
        holder.name.setText(model.getName());
       // Picasso.with(context).load(model.getImageUrl()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}