package com.nsoni.doordash;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nsoni.doordash.model.RestaurantDetail;

/**
 * A fragment representing a single Restaurant detail screen.
 * This fragment is either contained in a {@link RestaurantsListActivity}
 * in two-pane mode (on tablets) or a {@link RestaurantDetailActivity}
 * on handsets.
 */
public class RestaurantDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private RestaurantDetail mRestaurantDetail;
    private CollapsingToolbarLayout mAppBarLayout;
    private ImageView mAppBarImage;
    private RestaurantDetailViewModel mRestaurantDetailViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RestaurantDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        final TextView name = rootView.findViewById(R.id.name);

        final Activity activity = getActivity();
        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {

            int restaurantId = getArguments().getInt(ARG_ITEM_ID);

            if (activity instanceof RestaurantDetailActivity) {
                mAppBarLayout = activity.findViewById(R.id.toolbar_layout);
                mAppBarImage = activity.findViewById(R.id.toolbar_image);
            }
            setupObserver(name, activity, restaurantId);
        }

        return rootView;
    }

    private void setupObserver(final TextView name, final Activity mActivity, int restaurantId) {
        mRestaurantDetailViewModel = ViewModelProviders.of(getActivity()).get(RestaurantDetailViewModel.class);
        mRestaurantDetailViewModel.getRestaurantDetail(restaurantId).observe(this, new Observer<RestaurantDetail>() {
            @Override
            public void onChanged(@Nullable RestaurantDetail restaurantDetail) {
                mRestaurantDetail = restaurantDetail;
                name.setText(restaurantDetail.getName());
                if (mActivity instanceof RestaurantDetailActivity) {
                    Glide
                            .with(mActivity)
                            .load(RestaurantDetailFragment.this.mRestaurantDetail.getCoverImgUrl())
                            .into(RestaurantDetailFragment.this.mAppBarImage);
                }
            }
        });
    }
}
