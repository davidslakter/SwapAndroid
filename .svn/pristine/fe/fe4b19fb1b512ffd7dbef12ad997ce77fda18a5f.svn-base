package com.swap.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.Gson;
import com.swap.models.Users;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by anjali on 19-07-2017.
 */

public class Utils {


    // S3

    private static AmazonS3Client sS3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static TransferUtility sTransferUtility;

    static String filepath = null;
    static Bitmap bm = null;
    public static String realPath;


    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void hideSoftKeyboard(@NonNull Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static String changeDateFormet(String date) {
        String oldFormat = "E MMM dd HH:mm:ss Z yyyy";
        String newFormat = "dd MMMM yyyy";

        String formatedDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
        formatedDate = timeFormat.format(myDate);
        return formatedDate;
    }

    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;
            if (addresses != null && !addresses.isEmpty()) {
                //AppPref mPref=new AppPref(context);
               /* mPref.setStringValue(AppConstant.USER_ADDRESSLINE1,addresses.get(0).getAddressLine(0));
                mPref.setStringValue(AppConstant.USER_ADDRESSLINE2,addresses.get(0).getAddressLine(0));
                mPref.setStringValue(AppConstant.USER_CITY,addresses.get(0).getLocality());
                mPref.setStringValue(AppConstant.USER_STATE,addresses.get(0).getAdminArea());
                mPref.setStringValue(AppConstant.USER_ZIPCODE,addresses.get(0).getPostalCode());*/
                // mPref.setStringValue(AppConstant.USERCON,addresses.get(0).getCountryName());
                String countryCode = addresses.get(0).getCountryCode();
                //return addresses.get(0).getCountryName();
                return countryCode;
            }
            return "";
        } catch (IOException ignored) {
            //do something
        }
        return "";
    }

    public static void turnGPSOn(Context context) {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);

        String provider = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { // if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);

        }
    }

    public static Bitmap onSelectFromGalleryResult(Context context, Intent data) {


        if (data != null) {
            try {

                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(context, data.getData());
                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(context, data.getData());
                    // SDK > 19 (Android 4.4)
                else
                    realPath = String.valueOf(RealPathUtil.getRealPathFromURI_API19(context, data.getData()));
                filepath = getRealPathFromURI(context, realPath);
                //bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                bm = compressImage(context, filepath);
                //profileImage.setImageBitmap(bm);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bm;
    }

    private static String getRealPathFromURI(Context context, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static Bitmap compressImage(Context context, String imageUri) {

        String filePath = getRealPathFromURI(context, imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledBitmap;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    public static Bitmap onCaptureImageResult(Context context, Uri outPutFileUri) {

        String uri = outPutFileUri.getPath();
        filepath = uri.toString();
        if (filepath!=null){
            realPath =filepath;
        }
        try {
            bm = compressImage(context, uri);
            if (bm != null) {
                return bm;
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } catch (OutOfMemoryError oe) {
            Log.e("OException", oe.toString());
        }

        return null;
    }

    public static void fillMap(Map<String, Object> map, TransferObserver observer, boolean isChecked) {
        int progress = (int) ((double) observer.getBytesTransferred() * 100 / observer
                .getBytesTotal());
        /*map.put("id", observer.getId());
        map.put("checked", isChecked);*/
        map.put("fileName", observer.getAbsoluteFilePath());
       /* map.put("progress", progress);
        map.put("bytes",
                getBytesString(observer.getBytesTransferred()) + "/"
                        + getBytesString(observer.getBytesTotal()));
        map.put("state", observer.getState());
        map.put("percentage", progress + "%");*/
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = new TransferUtility(getS3Client(context.getApplicationContext()),
                    context.getApplicationContext());
        }

        return sTransferUtility;
    }

    public static AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
            sS3Client.setRegion(Region.getRegion(Regions.fromName("us-east-1")));
        }
        return sS3Client;
    }

    private static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    "us-east-1:d0d2cc0d-a727-4228-91b7-f1e7aba06e5c",
                    Regions.fromName("us-east-1"));
        }
        return sCredProvider;
    }


    public static void saveUserDateInPreferences(Context context, Users mUsers, String key) {
        Gson gson = new Gson();
        String stringUserDate = gson.toJson(mUsers);
        Preferences.save(context, key, stringUserDate);
    }


    public static Users getUserDateFromPreferences(Context context, String key) {
        String userDate = Preferences.get(context, key);
        Gson gson = new Gson();
        // gson.fromJson(userDate,Users.class);
        return gson.fromJson(userDate, Users.class);
    }

    public static long getDateInterval(Date oldDate, Date newDate) {
        long dateIntevalInMilis = newDate.getTime() - oldDate.getTime();
        return dateIntevalInMilis;
    }

    public static String getRelativeTime(Date date) {
        try {
            Calendar today = null;
            Calendar that_day = null;
            today = Calendar.getInstance();
            that_day = Calendar.getInstance();
            that_day.setTime(date);
            Long diff = today.getTimeInMillis() - that_day.getTimeInMillis();
            Long days = diff / (24 * 60 * 60 * 1000);
            if (days < 1) {
                return DateUtils.getRelativeTimeSpanString(date.getTime()).toString();
            } else if (days == 1) {
                return days + " day ago";
            } else {
                return days + " days ago";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getlongtoago(long createdAt) {
        DateFormat userDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
        Date date = null;
        date = new Date(createdAt);
        String crdate1 = dateFormatNeeded.format(date);

        // Date Calculation
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        crdate1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);

        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());

        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate1);
            current = dateFormat.parse(currenttime);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = current.getTime() - CreatedAt.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                time = diffDays + " day ago ";
            } else {
                time = diffDays + " days ago ";
            }
        } else {
            if (diffHours > 0) {
                if (diffHours == 1) {
                    time = diffHours + " hr ago";
                } else {
                    time = diffHours + " hrs ago";
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1) {
                        time = diffMinutes + " min ago";
                    } else {
                        time = diffMinutes + " mins ago";
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = diffSeconds + " secs ago";
                    }
                }

            }

        }
        return time;
    }
    public static String timeAgo(double time) {
        try {
            long time_ago = (long) time;
            long cur_time = (Calendar.getInstance().getTimeInMillis()) / 1000;
            long time_elapsed = cur_time - time_ago;
            //long seconds = time_elapsed;
            int minutes = Math.round(time_elapsed / 60);
            int hours = Math.round(time_elapsed / 3600);
            int days = Math.round(time_elapsed / 86400);
            int weeks = Math.round(time_elapsed / 604800);
            int months = Math.round(time_elapsed / 2600640);
            int years = Math.round(time_elapsed / 31207680);

            Date nowDate=new Date();
            Date yourPassDate = null;//you have a date here.
            yourPassDate.setTime(time_ago);

            final long diff = nowDate.getTime() - yourPassDate.getTime();
            long seconds = diff;
            // Seconds
            /*if (seconds < 60) {
                return "just now";
            }
            //Minutes
            else*/ if (minutes <= 60) {
                if (minutes == 1) {
                    return "one minute ago";
                } else {
                    return minutes + " minutes ago";
                }
            }
            //Hours
            else if (hours <= 24) {
                if (hours == 1) {
                    return "an hour ago";
                } else {
                    return hours + " hrs ago";
                }
            }
            //Days
            else if (days <= 7) {
                if (days == 1) {
                    return "yesterday";
                } else {
                    return days + " days ago";
                }
            }
            //Weeks
            else if (weeks <= 4.3) {
                if (weeks == 1) {
                    return "a week ago";
                } else {
                    return weeks + " weeks ago";
                }
            }
            //Months
            else if (months <= 12) {
                if (months == 1) {
                    return "a month ago";
                } else {
                    return months + " months ago";
                }
            }
            //Years
            else {
                if (years == 1) {
                    return "one year ago";
                } else {
                    return years + " years ago";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Typeface setFont(Context context, String path){
        //Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "lantinghei.ttf");
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  path);
        return custom_font;
    }

    @SuppressWarnings("deprecation")
    public static void clearWebViewCookies(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager cookie_sync_manager = CookieSyncManager.createInstance(context.getApplicationContext());
            cookie_sync_manager.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookie_sync_manager.stopSync();
            cookie_sync_manager.sync();
        } else {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        }
    }

    public void getPlayStoreLink(Context context){
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {

            InputStream is = context.getAssets().open("raw/client_secrets.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public  static void spaceRemove(EditText editText){
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[] { filter });
    }

    public static boolean checkImagePermissions(Context context){
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        } else return false;
    }
}
