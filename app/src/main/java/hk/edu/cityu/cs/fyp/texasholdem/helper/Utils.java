package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.content.Context;

public class Utils {

    public static int getDrawableResByString(Context context, String cardStr) {
        return context.getResources().getIdentifier(cardStr, "drawable", context.getPackageName());
    }

}
