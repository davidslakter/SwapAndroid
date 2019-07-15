package com.swap.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.swap.R;
import com.swap.utilities.RoundedImageView;

/**
 * Created by anjali on 21-07-2017.
 */

public class SelectPictureAdapter extends PagerAdapter {
    private static final int numberOfPages = 4;
    String[] sliderSelectPictureArray;
    Context context;
    LayoutInflater layoutInflater;

    public SelectPictureAdapter(String[] sliderSelectPictureArray, Context context) {

        this.sliderSelectPictureArray = sliderSelectPictureArray;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

   /* @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if(position==0)
            fragment = new SelectPictureFragment();
        else if(position==1)
            fragment = new SelectPictureFragment();
        else if (position==2)
            fragment = new SelectPictureFragment();
        else if (position==3)
            fragment = new SelectPictureFragment();

        return fragment;
    }*/

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.select_picture_item, container, false);

        RoundedImageView imageView = (RoundedImageView) itemView.findViewById(R.id.imageView);
        //imageView.setImageResource(sliderSelectPictureArray[position]);
        if(sliderSelectPictureArray.length>0 && !sliderSelectPictureArray[position].isEmpty())
            Picasso.with(context).load(sliderSelectPictureArray[position]).error(R.drawable.profile_picture_default_icon).into(imageView);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return sliderSelectPictureArray.length;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
