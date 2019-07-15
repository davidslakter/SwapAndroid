package com.swap.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swap.R;
import com.swap.views.activities.HomeActivity;


public class SwapTutorialPageTenFragment extends Fragment implements View.OnClickListener {

    ImageView imageViewUseTheApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_swap_tutorial_page_ten, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageViewUseTheApp =(ImageView)view.findViewById(R.id.imageViewUseTheApp);
        imageViewUseTheApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(getActivity(), HomeActivity.class);
        getActivity().startActivity(i);
    }
}
