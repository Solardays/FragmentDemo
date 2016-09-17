package com.jc.fragmentdemo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.jc.fragmentdemo.R;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by cc on 2016/9/14.
 * 屏幕类工具类,屏幕像素，px和dp互转，关闭软键盘，设置图文内容，
 */
public class ScreenUtils {

    /**
     * 屏幕宽px
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 屏幕高px
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * dip to px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px to dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 关闭软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 设置图文内容
     *
     * @param context
     * @param target
     * @param htmlSource
     * @param linkColor
     * @param textPainFlag
     */
    public static void setTextViewFromHtml(final Context context, TextView target, String htmlSource, final int
            linkColor, final int textPainFlag) {
        target.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence charSequence = Html.fromHtml(htmlSource, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                return drawable;
            }
        }, null);

        SpannableString spanStr = new SpannableString(charSequence);

        URLSpan[] urlspans = spanStr.getSpans(0, spanStr.length(), URLSpan.class);
        for (URLSpan urlspan : urlspans) {
            Log.d("url", "url=" + urlspan.getURL());
            spanStr.setSpan(new URLSpan(urlspan.getURL()) {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setFlags(textPainFlag);
                    ds.setColor(linkColor);
                }

                @Override
                public void onClick(View widget) {
                    try {
                        URI uri = new URI(getURL());
                        if ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme())) {
                        } else if ("tel".equalsIgnoreCase(uri.getScheme())) {
                        } else if ("mailto".equalsIgnoreCase(uri.getScheme())) {
                        } else if ("page".equalsIgnoreCase(uri.getScheme())) {
                            // TODO Activity 跳转
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }

            }, spanStr.getSpanStart(urlspan), spanStr.getSpanEnd(urlspan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        target.setText(spanStr);
    }


}
