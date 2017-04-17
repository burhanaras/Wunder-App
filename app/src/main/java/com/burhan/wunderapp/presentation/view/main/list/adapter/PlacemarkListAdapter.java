package com.burhan.wunderapp.presentation.view.main.list.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.burhan.wunderapp.R;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BURHAN on 4/17/2017.
 */

public class PlacemarkListAdapter extends RecyclerView.Adapter<PlacemarkListAdapter.MainViewHolder> {
    LayoutInflater inflater;
    private Context context;
    List<Placemark> items= new ArrayList<>();

    public PlacemarkListAdapter(Context context, List<Placemark> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        items = new ArrayList<Placemark>(list);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<Placemark> placemarks) {
        items.clear();
        items = placemarks;
        for(int i=0;i<items.size();i++){
            notifyItemChanged(i);
        }
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        TextView mainText, subText;
        @BindView(R.id.profile_image)
        ImageView profieImage;

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mainText = (TextView) itemView.findViewById(R.id.mainText);
            subText = (TextView) itemView.findViewById(R.id.subText);
        }

        public void bindData(Placemark placemark) {
            mainText.setText(placemark.getName());
            subText.setText(placemark.getAddress());
            Picasso.with(context).load(placemark.getProfilePhoto()).into(profieImage);
        }
    }
}