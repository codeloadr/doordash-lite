package com.nsoni.doordash;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nsoni.doordash.model.Restaurant;

import java.util.List;

public class RestaurantsRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String RESTAURANT_STATUS_TYPE_OPEN = "open";
    private static final String RESTAURANT_STATUS_TYPE_CLOSED = "Closed";
    private static final int FOOTER_ITEM_SIZE = 1;
    private static final int VIEW_TYPE_REST = 0;
    private static final int VIEW_TYPE_FOOTER = 1;
    private final RestaurantsListActivity mParentActivity;
    private final List<Restaurant> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Restaurant item = (Restaurant) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putInt(RestaurantDetailFragment.ARG_ITEM_ID, item.getId());
                RestaurantDetailFragment fragment = new RestaurantDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RestaurantDetailActivity.class);
                intent.putExtra(RestaurantDetailFragment.ARG_ITEM_ID, item.getId());

                context.startActivity(intent);
            }
        }
    };

    RestaurantsRecyclerViewAdapter(RestaurantsListActivity parent,
                                   List<Restaurant> items,
                                   boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_REST) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.restaurant_item_content, parent, false);
            return new RestaurantViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_footer, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RestaurantViewHolder) {
            RestaurantViewHolder restaurantViewHolder = (RestaurantViewHolder) holder;
            Restaurant restaurant = mValues.get(position);
            restaurantViewHolder.mName.setText(restaurant.getBusiness().getName());
            restaurantViewHolder.mDesc.setText(restaurant.getDescription());

            if (RESTAURANT_STATUS_TYPE_OPEN.equals(restaurant.getStatusType())) {
                restaurantViewHolder.mTime.setText(restaurant.getStatus());
            } else {
                restaurantViewHolder.mTime.setText(RESTAURANT_STATUS_TYPE_CLOSED);
            }

            Glide.with(restaurantViewHolder.mImageView.getContext())
                    .load(restaurant.getCoverImgUrl())
                    .into(restaurantViewHolder.mImageView);

            holder.itemView.setTag(restaurant);
            holder.itemView.setOnClickListener(mOnClickListener);


            String favKey = restaurant.getBusiness().getName().replace(" ", "-");
            if (Favorites.isFavorite(holder.itemView.getContext(), favKey)) {
                restaurantViewHolder.mFavBtn.setImageDrawable(holder.itemView.getContext().getDrawable(android.R.drawable.btn_star_big_on));
            } else {
                restaurantViewHolder.mFavBtn.setImageDrawable(holder.itemView.getContext().getDrawable(android.R.drawable.btn_star_big_off));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size() + FOOTER_ITEM_SIZE;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mValues.size()) {
            return VIEW_TYPE_REST;
        } else {
            return VIEW_TYPE_FOOTER;
        }
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImageView;
        final TextView mName;
        final TextView mDesc;
        final TextView mTime;
        final ImageView mFavBtn;

        RestaurantViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.image);
            mName = view.findViewById(R.id.name);
            mDesc = view.findViewById(R.id.desc);
            mTime = view.findViewById(R.id.time);
            mFavBtn = view.findViewById(R.id.fav);
            mFavBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String favKey = mName.getText().toString().replace(" ", "-");
                    Favorites.toggleFav(v.getContext(), favKey);
                    if (Favorites.isFavorite(v.getContext(), favKey)) {
                        mFavBtn.setImageDrawable(v.getContext().getDrawable(android.R.drawable.btn_star_big_on));
                    } else {
                        mFavBtn.setImageDrawable(v.getContext().getDrawable(android.R.drawable.btn_star_big_off));
                    }
                }
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        final Button mNext;
        final Button mPrev;

        FooterViewHolder(final View view) {
            super(view);
            mNext = view.findViewById(R.id.next);
            final RestaurantsListViewModel viewModel = ViewModelProviders.of(mParentActivity).get(RestaurantsListViewModel.class);
            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.next();
                    mParentActivity.getRestaurantRecyclerView().getLayoutManager().scrollToPosition(0);
                    mParentActivity.showProgressSpinner();
                }
            });
            mPrev = view.findViewById(R.id.prev);
            mPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.previous();
                    mParentActivity.getRestaurantRecyclerView().getLayoutManager().scrollToPosition(0);
                    mParentActivity.showProgressSpinner();
                }
            });
        }
    }
}
