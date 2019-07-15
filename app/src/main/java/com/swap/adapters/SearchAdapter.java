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

import com.amazonaws.services.cognitoidentityprovider.model.AttributeType;
import com.amazonaws.services.cognitoidentityprovider.model.UserType;
import com.squareup.picasso.Picasso;
import com.swap.R;
import com.swap.models.SwapUser;
import com.swap.models.Users;
import com.swap.utilities.RoundedImageView;
import com.swap.utilities.Utils;
import com.swap.views.activities.SwapProfileActivity;

import java.util.List;

import static com.swap.utilities.Constants.KEY_FROM_SWAP_HISTORY;
import static com.swap.utilities.Constants.KEY_USER_NAME;

/**
 * Created by anjali on 09-08-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.DataObjectHolder> {
    private List<UserType> searchAdapterList;
    Context context;

    public SearchAdapter(List<UserType> searchUserList, Context context) {
        this.searchAdapterList = searchUserList;
        this.context = context;
    }

    @Override
    public SearchAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_search, parent, false);

        SearchAdapter.DataObjectHolder dataObjectHolder = new SearchAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }


    @Override
    public void onBindViewHolder(SearchAdapter.DataObjectHolder holder, int position) {
        String attName = "";
        String attValuePicture = "";
        String attValueProfile = "";

        final UserType search = searchAdapterList.get(position);
        holder.textViewSearchUser.setText(search.getUsername());

        for (AttributeType attribute : search.getAttributes()) {
            attName = attribute.getName();
            if (attName.equals("picture")) {
                attValuePicture = attribute.getValue();
            }
            if (attName.equals("profile")) {
                attValueProfile = attribute.getValue();
            }
        }
        if (!attValuePicture.equals("")) {
            Picasso.with(context).load(attValuePicture).error(R.drawable.profile_picture_default_icon).into(holder.profilePicRoundedImageView);
            Log.d("getUserValue", attName + ": " + attValuePicture);
        }

        if (!attValueProfile.equals("")) {
            if (attValueProfile.equals("IS_VERIFIED")) {
                holder.imgViewVerified.setVisibility(View.VISIBLE);
            } else {
                holder.imgViewVerified.setVisibility(View.GONE);
            }
            Log.d("getUserValue", attName + ": " + attValueProfile);
        }

        //Picasso.with(context).load(search.g).error(R.drawable.profile_picture_default_icon).into(holder.profilePicRoundedImageView);
        holder.relativeLayoutRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context, SwapProfileActivity.class);
                context.startActivity(intent);*/
                new GetUserInfoTask(search.getUsername()).execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchAdapterList.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView textViewSearchUser;
        RelativeLayout relativeLayoutRaw;
        RoundedImageView profilePicRoundedImageView;
        ImageView imgViewVerified;

        public DataObjectHolder(View itemView) {
            super(itemView);
            textViewSearchUser = (TextView) itemView.findViewById(R.id.textViewSearchUser);
            relativeLayoutRaw = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutRaw);
            profilePicRoundedImageView = (RoundedImageView) itemView.findViewById(R.id.imageViewSearchUser);
            imgViewVerified = (ImageView) itemView.findViewById(R.id.imgViewVerified);
            //Set font on view//
            textViewSearchUser.setTypeface(Utils.setFont(context, "lantinghei.ttf"));
        }
    }

    private class GetUserInfoTask extends AsyncTask<Void, Void, Users> {
        ProgressDialog pDialog = new ProgressDialog(context);
        String username;

        public GetUserInfoTask(String username) {
            this.username = username;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(context.getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Users doInBackground(Void... voids) {
            return SwapUser.getUser(context, username);
        }

        @Override
        protected void onPostExecute(Users users) {
            super.onPostExecute(users);
            if (users != null) {
                Intent intent = new Intent(context, SwapProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_USER_NAME, users);
                KEY_FROM_SWAP_HISTORY = false;
                intent.putExtras(bundle);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "This user data is not available", Toast.LENGTH_LONG).show();
            }
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.cancel();
            }
        }
    }
}