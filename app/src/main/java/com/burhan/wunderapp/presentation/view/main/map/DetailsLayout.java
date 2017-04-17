package com.burhan.wunderapp.presentation.view.main.map;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.burhan.wunderapp.R;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.squareup.picasso.Picasso;

public class DetailsLayout extends CoordinatorLayout {

    @BindView(R.id.cardview) CardView cardViewContainer;
    @BindView(R.id.headerImage) ImageView imageViewPlaceDetails;
    @BindView(R.id.title) TextView textViewTitle;
    @BindView(R.id.description) TextView textViewDescription;
    private Context context;

    public DetailsLayout(final Context context) {
        this(context, null);
        this.context = context;
    }

    public DetailsLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    private void setData(Placemark place) {
        textViewTitle.setText(place.getName());
        textViewDescription.setText(place.getAddress());
        Picasso.with(context).load(place.getProfilePhoto()).into(imageViewPlaceDetails);
    }

    public static Scene showScene(Activity activity, final ViewGroup container, final View sharedView, final String transitionName, final Placemark data) {
        DetailsLayout detailsLayout = (DetailsLayout) activity.getLayoutInflater().inflate(R.layout.item_place, container, false);
        detailsLayout.setData(data);

        TransitionSet set = new ShowDetailsTransitionSet(activity, transitionName, sharedView, detailsLayout);
        Scene scene = new Scene(container, (View) detailsLayout);
        TransitionManager.go(scene, set);
        return scene;
    }

    public static Scene hideScene(Activity activity, final ViewGroup container, final View sharedView, final String transitionName) {
        DetailsLayout detailsLayout = (DetailsLayout) container.findViewById(R.id.bali_details_container);

        TransitionSet set = new HideDetailsTransitionSet(activity, transitionName, sharedView, detailsLayout);
        Scene scene = new Scene(container, (View) detailsLayout);
        TransitionManager.go(scene, set);
        return scene;
    }
}
