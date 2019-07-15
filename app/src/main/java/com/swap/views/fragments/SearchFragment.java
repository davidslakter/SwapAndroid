package com.swap.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.cognitoidentityprovider.model.UserType;
import com.swap.R;
import com.swap.adapters.SearchAdapter;
import com.swap.models.SwapUser;
import com.swap.utilities.Preferences;
import com.swap.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    RecyclerView recyclerViewSearch;
    private SearchAdapter searchAdapter;
    private List<UserType> searchUserList = new ArrayList<>();
    EditText editTextSearch;
    TextView textViewSearch;
    String searchKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        findVieById(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void findVieById(View view) {
        recyclerViewSearch = (RecyclerView) view.findViewById(R.id.recyclerViewSearch);
        searchAdapter = new SearchAdapter(searchUserList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewSearch.setLayoutManager(mLayoutManager);
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSearch.setAdapter(searchAdapter);
        editTextSearch = (EditText) view.findViewById(R.id.editTextSearch);
        editTextSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, R.drawable.ic_cancel, 0);
        textViewSearch = (TextView) view.findViewById(R.id.textViewSearch);


        editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextSearch.getRight() - editTextSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (!editTextSearch.getText().toString().trim().isEmpty()) {
                            editTextSearch.setText("");
                        }

                        return true;
                    }
                }
                return false;

            }
        });
        setFontOnViews();
        //prepareUserData();
        editTextSearchTextChangedListener();
    }

    private void setFontOnViews() {
        editTextSearch.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
        textViewSearch.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
    }

    private void editTextSearchTextChangedListener() {
        editTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchKey = editTextSearch.getText().toString().trim();
                if (!searchKey.isEmpty() && searchKey.length() > 0) {
                    textViewSearch.setText(searchKey);
                    if (Utils.isNetworkConnected(getActivity())) {
                        String username = Preferences.get(getActivity(), Preferences.USERNAME);
                        if (!username.isEmpty()) {
                            new GetUserInfoTask().execute(username);
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_SHORT);
                    }
                }/* else {
                    textViewSearch.setText("");
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private class GetUserInfoTask extends AsyncTask<Object, Object, List<UserType>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showWaitDialog("Loading");
        }

        protected List<UserType> doInBackground(Object... params) {
            return SwapUser.getCognitoUserList(searchKey);
        }

        @Override
        protected void onPostExecute(List<UserType> userTypes) {
            super.onPostExecute(userTypes);
            if (userTypes != null && userTypes.size() != 0) {
                // setUserData(result);
                Log.d("result :", String.valueOf(userTypes));
                searchAdapter = new SearchAdapter(userTypes, getActivity());
                recyclerViewSearch.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                // prepareUserData();
                //  Toast.makeText(EditProfileActivity.this, "user " + result.getUsername(), Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getActivity(), "Some thing wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

}
