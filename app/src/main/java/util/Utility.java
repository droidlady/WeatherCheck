package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utility {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String changeDateformat(String Date) {
        String dayFromDate = "";
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date;
        try {
            date = dateformat.parse(Date);
            DateFormat dayFormat = new SimpleDateFormat("EEEE");
            dayFromDate = dayFormat.format(date);
            Log.d("pratibha", "----------:: " + dayFromDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dayFromDate;
    }
}
