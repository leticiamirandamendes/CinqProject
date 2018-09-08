package com.example.letic.cinqproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.letic.cinqproject.adapters.ItemListAdapter;
import com.example.letic.cinqproject.api.ApiUtils;
import com.example.letic.cinqproject.api.RestAPI;
import com.example.letic.cinqproject.models.ItemList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private RestAPI restAPI;
    private List<ItemList> itemList;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getActivity().findViewById(R.id.recyclerViewList);
        restAPI = ApiUtils.getSOService();
        itemList = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading_items));
        progressDialog.setCancelable(false);
        progressDialog.show();
        getItemList();
    }

    public void getItemList(){
        restAPI.getList().enqueue(new Callback<List<ItemList>>() {
            @Override
            public void onResponse(Call<List<ItemList>> call, Response<List<ItemList>> response) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                ItemListAdapter mAdapter = new ItemListAdapter(getContext());
                mRecyclerView.setAdapter(mAdapter);
                if(response.body()!=null)
                    mAdapter.setItemList(response.body());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ItemList>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), getString(R.string.error_default), Toast.LENGTH_LONG).show();
            }
        });
    }
}
