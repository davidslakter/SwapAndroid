package com.swap.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.swap.R;
import com.swap.models.SwapHistory;
import com.swap.models.SwapUser;
import com.swap.models.Users;
import com.swap.utilities.Preferences;
import com.swap.utilities.RoundedImageView;
import com.swap.utilities.Utils;
import com.swap.views.activities.SwapProfileActivity;

import java.util.List;

import static com.swap.utilities.Constants.KEY_FROM_SWAP_HISTORY;
import static com.swap.utilities.Constants.KEY_USER_NAME;

public class SwapsAdapter extends RecyclerView.Adapter<SwapsAdapter.DataObjectHolder> {

    private List<SwapHistory> swapsList;
    private Context context;
    private boolean isSwap;

    public SwapsAdapter(List<SwapHistory> swapsUserList, Context context, boolean isSwap) {
        this.swapsList = swapsUserList;
        this.context = context;
        this.isSwap = isSwap;
    }

    @Override
    public SwapsAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_swaps, parent, false);

        SwapsAdapter.DataObjectHolder dataObjectHolder = new SwapsAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final SwapsAdapter.DataObjectHolder holder, int position) {

        final SwapHistory swaps = swapsList.get(position);

        holder.imageViewRedit.setVisibility(View.GONE);
        holder.imageViewSpotify.setVisibility(View.GONE);
        holder.imageViewCall.setVisibility(View.GONE);
        holder.imageViewEmail.setVisibility(View.GONE);
        holder.imageViewInstagram.setVisibility(View.GONE);
        holder.imageViewGithub.setVisibility(View.GONE);
        holder.imageViewVimeo.setVisibility(View.GONE);
        holder.imageViewTwitter.setVisibility(View.GONE);
        holder.imageViewYoutube.setVisibility(View.GONE);
        holder.imageViewSoundCloud.setVisibility(View.GONE);
        holder.imageViewPinterest.setVisibility(View.GONE);
        String userName;
        if (isSwap) {
            userName = swaps.getSwapped();
            holder.textViewSwapUserName.setText(swaps.getSwapped());
        } else {
            userName = swaps.getSwap();
            holder.textViewSwapUserName.setText(swaps.getSwap());
        }

       //String dateTime = Utils.timeAgo(swaps.getTime());
       String dateTime = Utils.getlongtoago((long)swaps.getTime());
        Log.d("dateTime: ",dateTime);
        holder.textViewSwapTime.setText(dateTime);
        if (Utils.isNetworkConnected(context)) {
            new GetUserInfoTask(userName, holder.imageViewSwapUserProfilePic, holder.imageViewRedit,
                    holder.imageViewSpotify, holder.imageViewCall, holder.imageViewEmail, holder.imageViewInstagram, holder.imageViewGithub
                    , holder.imageViewVimeo, holder.imageViewTwitter, holder.imageViewYoutube, holder.imageViewSoundCloud, holder.imageViewPinterest, false).execute();
        } else {
            Toast.makeText(context, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
        }
        holder.relativeLayoutRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(context)) {
                    String name;
                    if (isSwap) {
                        name = swaps.getSwapped();
                    } else {
                        name = swaps.getSwap();
                    }
                    new GetUserInfoTask(name, holder.imageViewSwapUserProfilePic, holder.imageViewRedit,
                            holder.imageViewSpotify, holder.imageViewCall, holder.imageViewEmail, holder.imageViewInstagram, holder.imageViewGithub
                            , holder.imageViewVimeo, holder.imageViewTwitter, holder.imageViewYoutube, holder.imageViewSoundCloud, holder.imageViewPinterest, true).execute();
                } else {
                    Toast.makeText(context, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return swapsList.size();
    }


    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView textViewSwapUserName;
        TextView textViewSwapTime;
        RelativeLayout relativeLayoutRaw;
        RoundedImageView imageViewSwapUserProfilePic;
        ImageView imageViewRedit;
        ImageView imageViewSpotify;
        ImageView imageViewCall;
        ImageView imageViewEmail;
        ImageView imageViewInstagram;
        ImageView imageViewGithub;
        ImageView imageViewVimeo;
        ImageView imageViewTwitter;
        ImageView imageViewYoutube;
        ImageView imageViewSoundCloud;
        ImageView imageViewPinterest;

        public DataObjectHolder(View itemView) {
            super(itemView);

            textViewSwapUserName = (TextView) itemView.findViewById(R.id.textViewSwapUserName);
            textViewSwapTime = (TextView) itemView.findViewById(R.id.textViewSwapTime);
            relativeLayoutRaw = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutRaw);
            imageViewSwapUserProfilePic = (RoundedImageView) itemView.findViewById(R.id.imageViewSwapUserProfilePic);
            imageViewRedit = (ImageView) itemView.findViewById(R.id.imageViewRedit);
            imageViewSpotify = (ImageView) itemView.findViewById(R.id.imageViewSpotify);
            imageViewCall = (ImageView) itemView.findViewById(R.id.imageViewCall);
            imageViewEmail = (ImageView) itemView.findViewById(R.id.imageViewEmail);
            imageViewInstagram = (ImageView) itemView.findViewById(R.id.imageViewInstagram);
            imageViewGithub = (ImageView) itemView.findViewById(R.id.imageViewGithub);
            imageViewVimeo = (ImageView) itemView.findViewById(R.id.imageViewVimeo);
            imageViewTwitter = (ImageView) itemView.findViewById(R.id.imageViewTwitter);
            imageViewYoutube = (ImageView) itemView.findViewById(R.id.imageViewYoutube);
            imageViewSoundCloud = (ImageView) itemView.findViewById(R.id.imageViewSoundCloud);
            imageViewPinterest = (ImageView) itemView.findViewById(R.id.imageViewPinterest);

            //Set font on view//
            textViewSwapUserName.setTypeface(Utils.setFont(context, "lantinghei.ttf"));
            textViewSwapTime.setTypeface(Utils.setFont(context, "lantinghei.ttf"));
        }
    }

    private class GetUserInfoTask extends AsyncTask<Void, Void, Users> {


        String swapsUser;
        RoundedImageView swapUserProfilePic;
        ProgressDialog pDialog = new ProgressDialog(context);
        boolean IsNavigate;
        ImageView imageViewRedit;
        ImageView imageViewSpotify;
        ImageView imageViewCall;
        ImageView imageViewEmail;
        ImageView imageViewInstagram;
        ImageView imageViewGithub;
        ImageView imageViewVimeo;
        ImageView imageViewTwitter;
        ImageView imageViewYoutube;
        ImageView imageViewSoundCloud;
        ImageView imageViewPinterest;

        public GetUserInfoTask(String swapped, RoundedImageView imageViewSwapUserProfilePic, ImageView imageViewRedit, ImageView imageViewSpotify, ImageView imageViewCall, ImageView imageViewEmail, ImageView imageViewInstagram, ImageView imageViewGithub, ImageView imageViewVimeo, ImageView imageViewTwitter, ImageView imageViewYoutube, ImageView imageViewSoundCloud, ImageView imageViewPinterest, boolean IsNavigate) {
            this.swapsUser = swapped;
            this.swapUserProfilePic = imageViewSwapUserProfilePic;
            this.IsNavigate = IsNavigate;

            this.imageViewRedit = imageViewRedit;
            this.imageViewSpotify = imageViewSpotify;
            this.imageViewCall = imageViewCall;
            this.imageViewEmail = imageViewEmail;
            this.imageViewInstagram = imageViewInstagram;
            this.imageViewGithub = imageViewGithub;
            this.imageViewVimeo = imageViewVimeo;
            this.imageViewTwitter = imageViewTwitter;
            this.imageViewYoutube = imageViewYoutube;
            this.imageViewSoundCloud = imageViewSoundCloud;
            this.imageViewPinterest = imageViewPinterest;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (IsNavigate) {
                pDialog.setMessage(context.getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            }

        }

        protected Users doInBackground(Void... params) {
            return SwapUser.getUser(context, swapsUser);
        }

        protected void onPostExecute(Users user) {

            if (user != null) {
                Users users = user;

                Utils.saveUserDateInPreferences(context, null, Preferences.USER_SWAPS);
                Picasso.with(context).load(users.getProfile_picture_url()).error(R.drawable.profile_picture_default_icon).into(swapUserProfilePic);
                if (users.getWillShareEmail()) {
                    //imageViewEmail.setImageResource(R.drawable.email_blue);
                    imageViewEmail.setVisibility(View.VISIBLE);
                }
                if (users.getWillShareGitHub()) {
                    // imageViewGithub.setImageResource(R.drawable.github_blue);
                    imageViewGithub.setVisibility(View.VISIBLE);
                }
                if (users.getWillShareInstagram()) {
                    // imageViewInstagram.setImageResource(R.drawable.instagram_blue);
                    imageViewInstagram.setVisibility(View.VISIBLE);
                }
                if (users.getWillSharePhone()) {
                    // imageViewCall.setImageResource(R.drawable.phone_blue);
                    imageViewCall.setVisibility(View.VISIBLE);
                }
                if (users.getWillSharePinterest()) {
                    //imageViewPinterest.setImageResource(R.drawable.pinterest_blue);
                    imageViewPinterest.setVisibility(View.VISIBLE);
                }
                if (users.getWillShareReddit()) {
                    //imageViewRedit.setImageResource(R.drawable.redit_blue);
                    imageViewRedit.setVisibility(View.VISIBLE);
                }
                if (users.getWillShareSoundCloud()) {
                    // imageViewSoundCloud.setImageResource(R.drawable.soundcloud_blue);
                    imageViewSoundCloud.setVisibility(View.VISIBLE);
                }
                if (users.getWillShareSpotify()) {
                    // imageViewSpotify.setImageResource(R.drawable.spotify_blue);
                    imageViewSpotify.setVisibility(View.VISIBLE);
                }
                if (users.getWillShareTwitter()) {
                    // imageViewTwitter.setImageResource(R.drawable.twitter_blue);
                    imageViewTwitter.setVisibility(View.VISIBLE);
                }
                if (users.getWillShareVimeo()) {
                    //imageViewVimeo.setImageResource(R.drawable.vimeo_blue);
                    imageViewVimeo.setVisibility(View.VISIBLE);
                }
                if (users.getWillShareYouTube()) {
                    //imageViewYoutube.setImageResource(R.drawable.youtube_blue);
                    imageViewYoutube.setVisibility(View.VISIBLE);
                }
                if (IsNavigate) {

                    //Utils.saveUserDateInPreferences(context, user, Preferences.USER_SWAPS);
                    Intent intent = new Intent(context, SwapProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_USER_NAME, user);
                    KEY_FROM_SWAP_HISTORY=true;
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.cancel();
            }
        }
    }
}
