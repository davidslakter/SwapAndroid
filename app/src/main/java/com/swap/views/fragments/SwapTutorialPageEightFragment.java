package com.swap.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swap.R;
import com.swap.utilities.Utils;
import com.swap.views.activities.HomeActivity;


public class SwapTutorialPageEightFragment extends Fragment {
    /*private TextView textViewContinue;
    private TextView textViewStartUsingSwap;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swap_tutorial_page_eight, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* textViewStartUsingSwap = (TextView) view.findViewById(R.id.textViewStartUsingSwap);
        textViewContinue = (TextView) view.findViewById(R.id.textViewContinue);
        textViewContinue.setOnClickListener(this);
        setFontOnViews();*/
    }
    /*private void setFontOnViews() {
        textViewStartUsingSwap.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
        textViewContinue.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewContinue:
                Intent i = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(i);
                break;
        }
    }*/
}
