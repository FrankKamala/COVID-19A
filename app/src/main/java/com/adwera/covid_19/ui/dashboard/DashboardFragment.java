package com.adwera.covid_19.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adwera.covid_19.R;
import com.adwera.covid_19.adapter.CountryAdapter;
import com.adwera.covid_19.models.Country;
import com.adwera.covid_19.network.CoronaApi;
import com.adwera.covid_19.network.CoronaClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private DashboardViewModel dashboardViewModel;
    RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        mRecyclerView = root.findViewById(R.id.countries_recycler_view);
        getAffectedCountries();
        return root;
    }

    public void getAffectedCountries(){
        CoronaApi coronaApi = CoronaClient.getClient();
        Call<List<Country>> countryCall = coronaApi.allCountriesAffected();
        countryCall.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                List<Country> countryList = response.body();
                Log.d(TAG, "onResponse: "+ countryList.get(0).getCountry());
                CountryAdapter adapter = new CountryAdapter(response.body(), getContext());
                mRecyclerView.setAdapter(adapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                mRecyclerView.setLayoutManager(gridLayoutManager);
                mRecyclerView.setHasFixedSize(true);
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());

            }
        });
    }

}