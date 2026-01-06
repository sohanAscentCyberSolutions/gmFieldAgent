package com.example.md3.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.example.md3.R;
import com.example.md3.baseApplication.BaseApplication;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public final class ViewUtils {
    public static final int RIGHT_LEFT_STYLE = 1;
    public static final int BOTTOM_UP_STYLE = 2;

    public static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, String message, int toastLength) {
        if (context != null) {
            Toast.makeText(context, message, toastLength).show();
        }
    }

    public static String getCurrentDate(){
        Date d = new Date();
        CharSequence s  = DateFormat.format("MM-dd-yyyy", d.getTime());
        return ViewUtils.formateDateFromstring("MM-dd-yyyy","dd-MM-yyyy",s.toString());
    }



    public static void showSnackbar(View parentView, String message) {

        try {

            if (parentView.isShown()) {
                Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);
                if (snackbar.getView().getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams();
//                    layoutParams.setAnchorId(R.id.bottomNavigation); //Id for your bottomNavBar or TabLayout
                    layoutParams.anchorGravity = Gravity.TOP;
                    layoutParams.gravity = Gravity.TOP;

                    snackbar.getView().setLayoutParams(layoutParams);

                }
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.setTextColor(Color.parseColor("#FFFFFF"));
                snackbar.show();
            }
        }catch (Exception e){
            showToast(BaseApplication.Companion.getContext(),message);

        }

    }

    public static String formatAmount2DecimalDoubleToString(Double  amount){
      return  String.format(Locale.US,"%.2f", amount);
    }

    public static String formatAmount3DecimalDoubleToString(Double  amount){
        return  String.format(Locale.US,"%.3f", amount);
    }




    public static void showSnackbarAtTop(View parentView, String message) {
        try {

            if (parentView.isShown()) {
                Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);


              ViewGroup.LayoutParams params = snackbar.getView().getLayoutParams();
                if (params instanceof CoordinatorLayout.LayoutParams) {
                    ((CoordinatorLayout.LayoutParams) params).gravity = Gravity.TOP;
                } else {
                    ((FrameLayout.LayoutParams) params).gravity = Gravity.TOP;
                }




                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.parseColor("#174F79"));

                snackbar.show();
            }
        }catch (Exception e){
            showToast(BaseApplication.Companion.getContext(),message);

        }
    }

    public static void showAlert(Activity activity, String title, String message, String positiveMessage,
                                 DialogInterface.OnClickListener listener, boolean isCancellable) {
        if (activity != null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            if (!title.equals("")) {
                alert.setTitle(title);
            }
            alert.setMessage(message);
            alert.setPositiveButton(positiveMessage, listener);
            alert.setCancelable(isCancellable);
            alert.show();
        }
    }





    public static void showAlertWithOptions(Activity activity, String title, String message,
                                            String positiveMessage, String negativeMessage,
                                            DialogInterface.OnClickListener positiveListener,
                                            DialogInterface.OnClickListener negativeListener, boolean isCancellable) {
        if (activity != null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            if (!title.equals("")) {
                alert.setTitle(title);
            }
            alert.setMessage(message);
            alert.setPositiveButton(positiveMessage, positiveListener);
            alert.setNegativeButton(negativeMessage, negativeListener);
            alert.setCancelable(isCancellable);
            alert.show();
        }
    }







    public interface PopupCallback {
        void onClick(View popupView, PopupWindow popupWindow);
    }

    public static void hideKeyBoard(Activity activity){
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

    public static void hideKeyBoard(Activity activity, View viewGroup){
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(viewGroup.getWindowToken(), 0);
            }
        }
    }


    public static Snackbar setIcon(Snackbar snackbar, Drawable drawable, @ColorInt int colorTint) {
        snackbar.setAction(" ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing
            }
        });

        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action);
        textView.setText("");
        textView.setTextSize(16F);
        drawable.setTint(colorTint);
        drawable.setTintMode(PorterDuff.Mode.SRC_ATOP);
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        return snackbar;
    }

    private static Snackbar withColor(Snackbar snackbar, @ColorInt int colorInt) {
        snackbar.getView().setBackgroundColor(colorInt);
        return snackbar;
    }

    public static void showSnackBar(Context context, CoordinatorLayout layout, String text, Drawable drawable, @ColorInt int colorTint) {
        Snackbar snackbar = Snackbar.make(layout, text, Snackbar.ANIMATION_MODE_SLIDE);

        if (drawable != null) {
            setIcon(snackbar, drawable, ContextCompat.getColor(context, R.color.md_theme_error));
        }

        withColor(snackbar, colorTint);
        snackbar.setTextColor(ContextCompat.getColor(context, R.color.md_theme_error));

        snackbar.getView().setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        snackbar.show();
    }




    public static void unAlphaPopupBackground(View view, Context context) {
        if (view.isShown()) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                view.setAlpha(1.0f);
            } else {
                view.setForeground(ContextCompat.getDrawable(context, android.R.color.transparent));
            }
        }
    }

    public static void blockTouchEvent(Activity activity){
        if (activity != null) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public static void unBlockTouchEvent(Activity activity) {
        if (activity != null) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public static void callThisNumber(String phoneNumber,Activity activity){
        if(!TextUtils.isEmpty(phoneNumber)){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+phoneNumber));
            activity.startActivity(intent);
        }

    }


    public static void emailIntent(String email,Activity activity){
        if(!TextUtils.isEmpty(email)){
            Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
            intent.setData(Uri.parse("mailto:"+email)); // or just "mailto:" for blank
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;// this will make such that when user returns to your app, your app is displayed, instead of the email app.
           activity.startActivity(intent);
        }

    }


    public static void phoneIntent(String phone,Activity activity){
        if(!TextUtils.isEmpty(phone)){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+phone));
            activity.startActivity(intent);
        }

    }


    public static void whatsAppIntent(String phone,Activity activity,boolean isSaudi){
        if(!TextUtils.isEmpty(phone)){
            if(isSaudi){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=966"+phone));
                activity.startActivity(intent);
            }else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91"+phone));
                activity.startActivity(intent);
            }

        }

    }
    public static void whatsAppIntent(String phone,Activity activity,String extention){
        if(!TextUtils.isEmpty(phone)){
            extention = extention.replace("+","").trim();
            Log.d("checkDatass",extention+phone);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+extention+phone));
            activity.startActivity(intent);
        }

    }





    public static void optionForWhatsAppAndPhone(String number,Activity activity,boolean isSaudiNumber){
        if(!TextUtils.isEmpty(number)){

            ViewUtils.showAlertWithOptions(activity
                    , "", "Select Action", "Call", "Whats App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                         ViewUtils.phoneIntent(number,activity);

                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ViewUtils.whatsAppIntent(number,activity,isSaudiNumber);
                        }
                    },true);



        }

    }





    public static void openMapIntent(Activity activity,String lat,String lon){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lon));
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        activity.startActivity(intent);



    }
    public  static void showKeyboard(Context context){
        if(context!=null){
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager!=null){
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

        }

    }

    public static void closeKeyboard(Context context){
        if(context!=null){
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager!=null){
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }

        }

    }


    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = null;

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.ENGLISH);



        if(inputDate == null){
            return "";
        }else {
            try {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);

            } catch (ParseException e) {
                Log.d("dateerror",e+"");

            }

            return  outputDate;
        }




    }


    public static boolean checkPermissionForCameraAndGallery(Activity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
     public static boolean checkPermissionForBluetooth(Activity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    public static  boolean checkPermissionForFile(Activity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    public static  void openSettings(Activity activity) {
        if(activity!=null){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivityForResult(intent, 101);
        }

    }



    public static void addImageAtEndOfText(String text, int imageID, Context context, TextView textView){

        String mainText = text + "  ";
        SpannableStringBuilder ssb = new SpannableStringBuilder(mainText);
        ssb.setSpan(new ImageSpan(context, imageID), mainText.length()-1, mainText.length(), 0);
        textView.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    public static void openLinkOnChromeTabs(String url,Context context){

        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void openLinkOnChromeTabsInApp(String url,Context context){

        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            builder.setStartAnimations(context, R.anim.enter_from_right, R.anim.exit_to_left);
            builder.setExitAnimations(context, R.anim.exit_to_right, R.anim.enter_from_left);
            String packageName = "com.android.chrome";
            if (packageName != null) {
                customTabsIntent.intent.setPackage(packageName);
                customTabsIntent.launchUrl(context, Uri.parse(url));
            } else {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String capitalizeFirstLetter(final String line) {
        try {
            return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
        }catch (Exception e){
            return line;
        }

    }


    public static CharSequence toHTML(final String html) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return  Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
            } else {
                return  Html.fromHtml(html);
            }
        }catch (Exception e){
            return html;
        }


    }

}

