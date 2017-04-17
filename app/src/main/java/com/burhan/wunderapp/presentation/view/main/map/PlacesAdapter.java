package com.burhan.wunderapp.presentation.view.main.map;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.burhan.wunderapp.R;
import com.burhan.wunderapp.common.transitions.TransitionUtils;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.burhan.wunderapp.presentation.model.dummy.DummyImageProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.BaliViewHolder> {

    private final OnPlaceClickListener listener;
    private Context context;
    private List<Placemark> placeMarks = new ArrayList<>();

    PlacesAdapter(OnPlaceClickListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    public BaliViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new BaliViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bali_place, parent, false));
    }

    @Override
    public void onBindViewHolder(final BaliViewHolder holder, final int position) {
        Placemark placemark= placeMarks.get(position);

        holder.title.setText(placemark.getAddress());
        holder.openingHours.setText("12:00 - 18:00");
        holder.price.setText("40 $");
        Picasso.with(context).load(DummyImageProvider.profileImage(position)).error(R.drawable.one).into(holder.placePhoto);
        holder.root.setOnClickListener(view -> listener.onPlaceClicked(holder.root, TransitionUtils.getRecyclerViewTransitionName(position), position));
    }

    @Override
    public int getItemCount() {
        return placeMarks.size();
    }


    public void setPlacesMarks(List<Placemark> placemarks) {
        this.placeMarks = placemarks;
        for (int i = 0; i < placeMarks.size(); i++) {
            notifyItemInserted(i);
        }
    }

    interface OnPlaceClickListener {
        void onPlaceClicked(View sharedImage, String transitionName, final int position);
    }

    public class BaliViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.price) TextView price;
        @BindView(R.id.opening_hours) TextView openingHours;
        @BindView(R.id.root) CardView root;
        @BindView(R.id.headerImage) ImageView placePhoto;

        BaliViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
