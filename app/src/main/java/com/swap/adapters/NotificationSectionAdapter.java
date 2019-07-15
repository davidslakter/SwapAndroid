package com.swap.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.swap.R;
import com.swap.models.DataModel;
import com.swap.models.SwapRequest;
import com.swap.models.SwapUser;
import com.swap.models.Users;
import com.swap.utilities.Preferences;
import com.swap.utilities.RoundedImageView;
import com.swap.utilities.SectionedRecyclerViewAdapter;
import com.swap.utilities.Utils;
import com.swap.views.fragments.NotificationFragment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationSectionAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private List<DataModel> allData;
    Context context;
    NotificationFragment fragment;
    private int ACTION_ACCEPT = 1;
    private int ACTION_DENY = 2;
    private int ACTION_SWAP = 3;
    private int ACTION_USER_IMAGE = 4;

    public NotificationSectionAdapter(List<DataModel> data, Context context, NotificationFragment fragment) {
        this.allData = data;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public int getSectionCount() {
        return allData.size();
    }

    @Override
    public int getItemCount(int section) {
        return allData.get(section).getAllItemsInSection().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        String sectionName = allData.get(section).getHeaderTitle();
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.sectionTitle.setText(sectionName);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {

        DataModel dataModel = allData.get(section);
        ArrayList<SwapRequest> itemsInSection = dataModel.getAllItemsInSection();
        SwapRequest swapRequest = itemsInSection.get(relativePosition);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.textViewUsername.setTag(swapRequest);
        if (swapRequest.getSent_at() != 0) {
            long date1 = (long) swapRequest.getSent_at();
            Date date = new Date(date1);
            itemViewHolder.textViewTime.setText(Utils.getRelativeTime(date));
        }
        //itemViewHolder.imageViewUserPicture.setImageResource(imgPlaceholderResId);
        //Picasso.with(context).load(swapRequest.ge).into(itemViewHolder.imageViewUserPicture);

        if (dataModel.getHeaderTitle().equals(context.getString(R.string.sentRequest))) {
            itemViewHolder.buttonAccept.setVisibility(View.GONE);
            itemViewHolder.buttonDeny.setVisibility(View.GONE);
            itemViewHolder.textViewUsername.setText(swapRequest.getRequested());
            if (swapRequest.isStatus() && swapRequest.isRequested_user_has_responded_to_request()) {
                itemViewHolder.buttonSwap.setVisibility(View.VISIBLE);
            }
            if (Utils.isNetworkConnected(context)) {
                new AcceptDenyAsyncTask(swapRequest.getRequested(), ACTION_USER_IMAGE, itemViewHolder.imageViewUserPicture).execute();
            }
        } else {
            itemViewHolder.textViewUsername.setText(swapRequest.getSender());
            itemViewHolder.buttonAccept.setVisibility(View.VISIBLE);
            itemViewHolder.buttonDeny.setVisibility(View.VISIBLE);
            itemViewHolder.buttonSwap.setVisibility(View.GONE);
            if (Utils.isNetworkConnected(context)) {
                new AcceptDenyAsyncTask(swapRequest.getSender(), ACTION_USER_IMAGE, itemViewHolder.imageViewUserPicture).execute();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, boolean header) {
        View v;
        if (header) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_section_header, parent, false);
            return new SectionViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_section_item, parent, false);
            return new ItemViewHolder(v);
        }
    }

    // SectionViewHolder Class for Sections
    public class SectionViewHolder extends RecyclerView.ViewHolder {
        private final TextView sectionTitle;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionTitle = (TextView) itemView.findViewById(R.id.textViewHeader);
            sectionTitle.setTypeface(Utils.setFont(context, "lantinghei.ttf"));
        }
    }

    // ItemViewHolder Class for Items in each Section
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final RoundedImageView imageViewUserPicture;
        private final TextView textViewUsername;
        private final TextView textViewTime;
        Button buttonAccept;
        Button buttonDeny;
        Button buttonSwap;

        public ItemViewHolder(View view) {
            super(view);
            rootView = view;
            imageViewUserPicture = (RoundedImageView) view.findViewById(R.id.imageViewUserPicture);
            textViewUsername = (TextView) view.findViewById(R.id.textViewUsername);
            textViewTime = (TextView) view.findViewById(R.id.textViewTime);
            buttonAccept = (Button) view.findViewById(R.id.buttonAccept);
            buttonDeny = (Button) view.findViewById(R.id.buttonDeny);
            buttonSwap = (Button) view.findViewById(R.id.buttonSwap);
            buttonSwap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwapRequest swapRequest = (SwapRequest) textViewUsername.getTag();
                    new AcceptDenyAsyncTask(swapRequest.getRequested(), ACTION_SWAP, null).execute();

                }
            });
            buttonDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwapRequest swapRequest = (SwapRequest) textViewUsername.getTag();
                    new AcceptDenyAsyncTask(swapRequest.getSender(), ACTION_DENY, null).execute();
                }
            });
            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwapRequest swapRequest = (SwapRequest) textViewUsername.getTag();
                    new AcceptDenyAsyncTask(swapRequest.getSender(), ACTION_ACCEPT, null).execute();
                }
            });

            //Set font for all views//
            textViewUsername.setTypeface(Utils.setFont(context, "lantinghei.ttf"));
            textViewTime.setTypeface(Utils.setFont(context, "lantinghei.ttf"));
            buttonAccept.setTypeface(Utils.setFont(context, "lantinghei.ttf"));
            buttonDeny.setTypeface(Utils.setFont(context, "lantinghei.ttf"));
            buttonSwap.setTypeface(Utils.setFont(context, "lantinghei.ttf"));
        }
    }

    private class AcceptDenyAsyncTask extends AsyncTask<Void, Void, Users> {
        String userName;
        int action;
        boolean responseStatus;
        Users users;
        RoundedImageView swapUserProfilePic;

        public AcceptDenyAsyncTask(String userName, int action, RoundedImageView imageViewUserPicture) {
            this.userName = userName;
            this.action = action;
            this.swapUserProfilePic = imageViewUserPicture;
        }

        ProgressDialog pDialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (action != ACTION_USER_IMAGE) {
                pDialog.setMessage(context.getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        protected Users doInBackground(Void... params) {
            if (action == ACTION_ACCEPT) {
                users = SwapUser.getUser(context, userName);
                SwapUser.performActionOnSwapRequestFromUser(context, userName, true);
            } else if (action == ACTION_DENY) {
                SwapUser.performActionOnSwapRequestFromUser(context, userName, false);
            } else if (action == ACTION_SWAP) {
                // send third variable to true to start social network follow process
                responseStatus = SwapUser.swap(context, userName, true);
                if (responseStatus) {
                    SwapUser.confirmSwapRequestToUser(context, userName);
                    Users swap = SwapUser.getUser(context, Preferences.get(context, Preferences.USERNAME));
                    Users swapped = SwapUser.getUser(context, userName);
                    SwapUser.giveSwapPointsToUsersWhoSwapped(context, swap, swapped);
                }
            } else if (action == ACTION_USER_IMAGE) {
                users = SwapUser.getUser(context, userName);
            }

            return users;
        }

        protected void onPostExecute(Users user) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.cancel();
            }
            if (action == ACTION_USER_IMAGE && user != null && swapUserProfilePic != null) {
                Picasso.with(context).load(user.getProfile_picture_url()).error(R.drawable.profile_picture_default_icon).into(swapUserProfilePic);
            }
            if (action != ACTION_USER_IMAGE && fragment != null) {
                fragment.getData();
            }
            if (action == ACTION_ACCEPT && user != null) {
                //Users users=SwapUser.getUser(context,Preferences.get(context,Preferences.USERNAME));
                SwapUser.sendNotifcationOfSwapRequestAcceptanceToUser(user);
            }

            /*if(!responseStatus)
            {
                Toast.makeText(context, "Can not perform swap", Toast.LENGTH_SHORT).show();
            }*/
        }
    }
}