package com.swap.utilities;

import android.content.Context;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;

import com.swap.views.activities.LoginActivity;

/**
 * Created by anjali on 21-08-2017.
 */

public class EmojiExcludeFilter implements InputFilter {

    Context mContext;

    public EmojiExcludeFilter(Context context) {
        this.mContext = context;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {

            int type = Character.getType(source.charAt(i));
            if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL ||type==Character.SPACE_SEPARATOR) {
                return "";
            }
        }
        return null;
    }
}
