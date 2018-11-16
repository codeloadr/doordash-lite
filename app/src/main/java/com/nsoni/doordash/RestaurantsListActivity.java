package com.nsoni.doordash;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.nsoni.doordash.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Restaurants. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RestaurantDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RestaurantsListActivity extends BaseActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private RecyclerView mRestaurantRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        setupToolbar();

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRestaurantRecyclerView = findViewById(R.id.item_list);
        setupRecyclerView(mRestaurantRecyclerView);

        observeViewModel();
    }

    private void observeViewModel() {
        RestaurantsListViewModel restaurantListViewModel = ViewModelProviders.of(this).get(RestaurantsListViewModel.class);

        restaurantListViewModel.getRestaurants().observe(this, new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(@Nullable List<Restaurant> restaurants) {
                mRestaurantList.clear();
                mRestaurantList.addAll(restaurants);
                mRestaurantRecyclerView.getAdapter().notifyDataSetChanged();
                hideProgressSpinner();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RestaurantsRecyclerViewAdapter(this, mRestaurantList, mTwoPane));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public RecyclerView getRestaurantRecyclerView() {
        return mRestaurantRecyclerView;
    }

    public void showProgressSpinner() {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
    }

    public void hideProgressSpinner() {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

}
