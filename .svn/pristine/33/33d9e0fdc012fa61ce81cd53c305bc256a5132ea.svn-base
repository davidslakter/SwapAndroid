package com.swap.views.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swap.R;
import com.swap.adapters.SwapsAdapter;
import com.swap.models.SwapHistory;
import com.swap.models.SwapUser;
import com.swap.utilities.Preferences;
import com.swap.utilities.Utils;

import java.util.ArrayList;

public class SwappedFragment extends Fragment {

    RecyclerView recyclerView;
    private SwapsAdapter swapsAdapter;
    private ArrayList<SwapHistory> swapsUserList = new ArrayList<>();
    SwapHistory swaps;
    TextView textViewNoHistoryFound;
    String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swaps, container, false);
        setHasOptionsMenu(true);
        findViewById(view);
        return view;
    }

    private void setFontOnViews() {
        textViewNoHistoryFound.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
    }

    private void findViewById(View view) {
        textViewNoHistoryFound = (TextView) view.findViewById(R.id.textViewNoHistoryFound);
        textViewNoHistoryFound.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setFontOnViews();
        prepareUserData();
    }

    private void prepareUserData() {
        username = Preferences.get(getActivity(), Preferences.USERNAME);
        if (!username.isEmpty())
            new getUserSwapsListDate().execute();


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_right_icon).setVisible(false); // You can change the state of the menu item here if you call getActivity().supportInvalidateOptionsMenu(); somewhere in your code
    }

    private class getUserSwapsListDate extends AsyncTask<Object, Object, ArrayList<SwapHistory>> {
        ProgressDialog pDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getActivity().getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<SwapHistory> doInBackground(Object... objects) {
            return SwapUser.getSwappedHistory(getActivity(), username);
        }

        @Override
        protected void onPostExecute(ArrayList<SwapHistory> swapHistories) {
            super.onPostExecute(swapHistories);

            if (swapHistories != null && swapHistories.size() != 0) {
                // true for swap and it will be false for swapped
                swapsAdapter = new SwapsAdapter(swapHistories, getActivity(), false);
                recyclerView.setAdapter(swapsAdapter);
                textViewNoHistoryFound.setVisibility(View.GONE);
            } else {
                textViewNoHistoryFound.setVisibility(View.VISIBLE);
            }
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.cancel();
            }
        }
    }
}
