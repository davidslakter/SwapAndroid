package com.swap.views.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.swap.R;
import com.swap.utilities.Preferences;
import com.swap.utilities.Utils;

import io.branch.referral.util.LinkProperties;


public class SwapLinkFragment extends Fragment implements View.OnClickListener {
    private TextView textViewSwapLink;
    private TextView textViewCopyLink;
    String swapLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swap_link, container, false);
        setHasOptionsMenu(true);
        findViewById(view);
        shareLink();
        return view;
    }

    private void shareLink() {
        LinkProperties linkProperties = new LinkProperties()
                .addTag("myTag1")
                //.setAlias("myCustomMonsterLink") // In case you need to white label your link
                .setFeature("mySharefeature1")
                .setStage("1")
                .addControlParameter("$android_deeplink_path", "Swap/view/");
        //final String monsterName = myMonsterObject_.getTitle();
    }

    private void findViewById(View view) {
        textViewSwapLink = (TextView) view.findViewById(R.id.textViewSwapLink);
        textViewCopyLink = (TextView) view.findViewById(R.id.textViewCopyLink);
        String username = Preferences.get(getActivity(), Preferences.USERNAME);
        textViewSwapLink.setText("getswap.me/" + username);
        swapLink = textViewSwapLink.getText().toString().trim();
        textViewCopyLink.setOnClickListener(this);
        setFontOnViews();
    }

    private void setFontOnViews() {
        textViewSwapLink.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
        textViewCopyLink.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewCopyLink:
                copyLink();
                break;
        }
    }

    private void copyLink() {
        swapLink = textViewSwapLink.getText().toString().trim();
        if (!swapLink.isEmpty() && swapLink.length() > 0) {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", swapLink);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "Swap link copied", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_right_icon:
                if (!swapLink.isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Swap with me!  " + swapLink);
                    getActivity().startActivity(shareIntent);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.action_right_icon).setIcon(R.drawable.ic_share_link); // You can change the state of the menu item here if you call getActivity().supportInvalidateOptionsMenu(); somewhere in your code
    }
}
