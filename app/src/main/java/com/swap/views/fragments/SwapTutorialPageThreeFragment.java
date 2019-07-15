package com.swap.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swap.R;
import com.swap.utilities.Utils;


public class SwapTutorialPageThreeFragment extends Fragment {

   /* private TextView textViewSwap;
    private TextView textViewMediasTheAreHigh;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_swap_tutorial_page_three, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* textViewSwap = (TextView) view.findViewById(R.id.textViewSwap);
        textViewMediasTheAreHigh = (TextView) view.findViewById(R.id.textViewMediasTheAreHigh);
        setFontOnViews();*/
    }
   /* private void setFontOnViews() {
        textViewSwap.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
        textViewMediasTheAreHigh.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
    }*/
}
