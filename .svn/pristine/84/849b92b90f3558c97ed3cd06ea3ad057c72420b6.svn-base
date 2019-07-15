package com.swap.views.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swap.R;
import com.swap.adapters.NotificationSectionAdapter;
import com.swap.models.DataModel;
import com.swap.models.SwapRequest;
import com.swap.models.SwapUser;
import com.swap.utilities.Preferences;
import com.swap.utilities.Utils;

import java.util.ArrayList;
import java.util.List;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class NotificationFragment extends Fragment {

    private SectionedRecyclerViewAdapter sectionAdapter;
    RecyclerView recyclerView;
    List<DataModel> allSampleData;
    TextView tvNoSwapRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        setHasOptionsMenu(true);
        findViewById(view);
        allSampleData = new ArrayList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
    }

    public void getData() {
        if (Utils.isNetworkConnected(getActivity())) {
            new SwapRequestAsyncTask().execute();
        }
    }

    private void findViewById(View view) {
        sectionAdapter = new SectionedRecyclerViewAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvNoSwapRequest = (TextView) view.findViewById(R.id.tvNoSwapRequest);
        setFontOnViews();
    }
    private void setFontOnViews() {
        tvNoSwapRequest.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_right_icon).setVisible(false); // You can change the state of the menu item here if you call getActivity().supportInvalidateOptionsMenu(); somewhere in your code
    }

    ArrayList<SwapRequest> swapRequestList;
    ArrayList<SwapRequest> swapPendingRequestList;

    private class SwapRequestAsyncTask extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            String username = Preferences.get(getActivity(), Preferences.USERNAME);
            swapRequestList = SwapUser.getRequestedSwaps(getActivity(), username);
            swapPendingRequestList = SwapUser.getPendingSentSwapRequests(getActivity(), username);

            return "";
        }

        protected void onPostExecute(String str) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.cancel();
            }
            populateData();
            updateView();
        }
    }

    private void updateView() {
        try {
            if (allSampleData != null && allSampleData.size() > 0) {
                NotificationSectionAdapter adapter = new NotificationSectionAdapter(allSampleData, getActivity(), this);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                tvNoSwapRequest.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                tvNoSwapRequest.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }
    }

    private void populateData() {
        try {
            allSampleData.clear();
            if (swapRequestList != null && swapRequestList.size() > 0) {
                DataModel dataModel = new DataModel();
                dataModel.setHeaderTitle(getString(R.string.swapRequest));
                dataModel.setAllItemsInSection(swapRequestList);
                allSampleData.add(dataModel);
            }
            if (swapPendingRequestList != null && swapPendingRequestList.size() > 0) {
                DataModel dataModel = new DataModel();
                dataModel.setHeaderTitle(getString(R.string.sentRequest));
                dataModel.setAllItemsInSection(swapPendingRequestList);
                allSampleData.add(dataModel);
            }
        } catch (Exception e) {
            // Ignore error.
        }
    }
}
