package com.forbit.sultanr.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.forbit.sultanr.R;
import com.forbit.sultanr.models.Location;
import com.forbit.sultanr.models.RData;
import com.forbit.sultanr.models.Span;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sohel on 1/27/2018.
 */

public class MyUtil {
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final String DATE_FORMAT2 = "dd-MM-yy";
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String MONTH_YEAR = "MMM-yyyy";
    private static final String D_FORMAT = "yyyy-MM-dd";
    private static final String T_FORMAT = "hh:mm a";

    private static char[] eng_arr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static char[] bangla_arr = {'০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'};


    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getMonthYear(Date date) {
        DateFormat df = new SimpleDateFormat(MONTH_YEAR);
        return df.format(date);
    }

    public static String getStringDate3(Date date) {
        DateFormat df = new SimpleDateFormat(TIME_FORMAT);
        return df.format(date);
    }

    public static String getTime(Date date) {
        DateFormat df = new SimpleDateFormat(T_FORMAT);
        return df.format(date);
    }


    public static long getServerTime(String dateStr) {
        DateFormat df = new SimpleDateFormat(TIME_FORMAT);
        try {
            return df.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getCurrentSpanNumber() {
        DateFormat df = new SimpleDateFormat(TIME_FORMAT);
        String dateStr = df.format(new Date());

        return Integer.parseInt(dateStr.split(" ")[1].split(":")[0]);
    }


    public static String convertToBangla(String number) {
        String values = "";
        char[] charArr = number.toCharArray();

        for (int i = 0; i < charArr.length; i++) {

            char c = 0;

            for (int j = 0; j < eng_arr.length; j++) {
                if (charArr[i] == eng_arr[j]) {
                    c = bangla_arr[j];
                    //values.concat(String.valueOf(bangla_arr[j]));
                    break;
                } else {
                    c = charArr[i];
                }
            }

            values = values.concat(String.valueOf(c));
        }

        return values;


    }


    public static String getServerTimeStr(long time) {
        DateFormat df = new SimpleDateFormat(TIME_FORMAT);
        return df.format(new Date(time));
    }

    public static List<Span> getSpanList(Context context) {

        String[] hourArr = context.getResources().getStringArray(R.array.hour_array);

        List<Span> spanList = new ArrayList<>();


        for (int i = 0; i < 24; i++) {
            Span span = new Span(i);
            span.setTime(hourArr[i]);
            spanList.add(span);
        }

        return spanList;


    }

    public static double getDistanceFrom(List<RData> myLatLongList) {

        double distance = 0;

        LatLng firstLoc = null;
        LatLng nextLoc = null;

        for (int i = 0; i < myLatLongList.size(); i++) {
            //myLatLongList.add(new MyLatLong(x));

            Log.d("TIMEChecker", myLatLongList.get(i).getDevice_time());

            if (i == 0) {
                firstLoc = new LatLng(myLatLongList.get(i).getLat(), myLatLongList.get(i).getLng());
            } else {
                nextLoc = new LatLng(myLatLongList.get(i).getLat(), myLatLongList.get(i).getLng());

                double x = Haversine.distance(firstLoc.latitude, firstLoc.longitude, nextLoc.latitude, nextLoc.longitude);
                //Log.d("UUUUUUUUU",x+"");

                if (Math.abs(x) > 2) {
                    distance = distance + x;
                }

                firstLoc = nextLoc;
            }
        }

        return distance;


    }

    public static double getDistance(List<Location> locationList) {

        double distance = 0;
        LatLng firstLoc = null;
        LatLng nextLoc = null;
        for (int i = 0; i < locationList.size(); i++) {
            if (i == 0) {
                firstLoc = new LatLng(locationList.get(i).getGeo().getLat(), locationList.get(i).getGeo().getLng());
            } else {
                nextLoc = new LatLng(locationList.get(i).getGeo().getLat(), locationList.get(i).getGeo().getLng());
                double x = Haversine.distance(firstLoc.latitude, firstLoc.longitude, nextLoc.latitude, nextLoc.longitude);
                distance = distance + x;
                firstLoc = nextLoc;
            }
        }
        return distance;
    }

    public static double getDistance(List<Location> locationList, int vehicleType) {
        List<Location> correctList = new ArrayList<>();
        if (vehicleType < 5) {
            for (Location x : locationList) {
                if (x.getGeo().getAcc().equals("ON")) {
                    correctList.add(x);
                }
            }
        } else {
            correctList = locationList;
        }
        double distance = 0;
        LatLng firstLoc = null;
        LatLng nextLoc = null;
        for (int i = 0; i < correctList.size(); i++) {
            if (i == 0) {
                firstLoc = new LatLng(correctList.get(i).getGeo().getLat(), correctList.get(i).getGeo().getLng());
            } else {
                nextLoc = new LatLng(correctList.get(i).getGeo().getLat(), correctList.get(i).getGeo().getLng());
                double x = Haversine.distance(firstLoc.latitude, firstLoc.longitude, nextLoc.latitude, nextLoc.longitude);
                distance = distance + x;
                firstLoc = nextLoc;
            }
        }
        return distance;
    }

    public static String getStringDate(Date date) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(date);
    }

    public static Date getDate(String dateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.parse(dateStr);
    }

    public static String getStringDate2(Date date) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT2);
        return df.format(date);
    }

    public static int getDuration(long fDate, long sDate) {
        long diff = fDate - sDate;
        return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1);
    }

    public static long getBeginingTime(long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);


        int year = calendar.get(Calendar.YEAR);
        int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(year, month, dayofmonth, 0, 0, 0);


        return calendar.getTimeInMillis();
    }

    public static long getDayAfter(long date, int dayAfter) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        int year = calendar.get(Calendar.YEAR);
        int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(year, month, dayofmonth + dayAfter, 0, 0, 0);


        return calendar.getTimeInMillis();
    }

    public static String getTwoDecimalFormat(double value) {

        return String.format("%.2f", value);

    }

    public static long getEndingTime(long time) {

        long beginingTime = getBeginingTime(time);

        return beginingTime + (23 * 60 * 60 + 59 * 60 + 59) * 1000;

    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public static String decodeString(String string) {
        return string.replace(",", ".");
    }


    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }


    public static byte[] getScaleByteArray(Context c, Uri uri, int width, int height) {
        byte[] byteArray = null;
        try {
            Bitmap nb = BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri));
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(nb, width, height, false);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public static List<Integer> getScreenDimension(Context context) {

        List<Integer> retList = new ArrayList<>();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        retList.add(width);
        retList.add(height);

        return retList;
    }

    public static Bitmap getScaledBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);

    }

    public static byte[] getByteArray(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);

        return stream.toByteArray();

    }

    public static int getFullHeight(ViewGroup layout) {
        int specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
        int specHeight = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);


        layout.measure(specWidth, specHeight);
        int totalHeight = 0;//layout.getMeasuredHeight();
        int initialVisibility = layout.getVisibility();
        layout.setVisibility(View.VISIBLE);
        int numberOfChildren = layout.getChildCount();
        for (int i = 0; i < numberOfChildren; i++) {
            View child = layout.getChildAt(i);

            /*int desiredWidth = View.MeasureSpec.makeMeasureSpec(layout.getWidth(),
                    View.MeasureSpec.AT_MOST);
            child.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight+=child.getMeasuredHeight();*/

            if (child instanceof ViewGroup) {
                totalHeight += getFullHeight((ViewGroup) child);
            } else {
                int desiredWidth = View.MeasureSpec.makeMeasureSpec(layout.getWidth(),
                        View.MeasureSpec.AT_MOST);
                child.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += child.getMeasuredHeight();
            }

        }
        layout.setVisibility(initialVisibility);
        return totalHeight;
    }

    public static String getAddress(Context context, LatLng latLng) {
        String address = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    public static boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile("(^(\\+88|0088)?(01){1}[3456789]{1}(\\d){8})$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static String getReqDate(Date date) {
        DateFormat df = new SimpleDateFormat(D_FORMAT);
        return df.format(date);
    }


}
