package com.brianbett.urembom;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class Loader {
    private final Context context;
    private final View popupView;
    private final View parentView;
    private final PopupWindow popupWindow;


    public Loader(Context context, View popupView, View parentView,PopupWindow popupWindow) {
        this.context = context;
        this.popupView = popupView;
        this.parentView = parentView;
        this.popupWindow=popupWindow;
//        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
    }
    public void showLoader() {

        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        TextView loadingText =popupView.findViewById(R.id.loading_text);
        CardView loadingRing=popupView.findViewById(R.id.loader);

        loadingText.startAnimation(AnimationUtils.loadAnimation(context,R.anim.loader_text_animation));

    }
    public  void dismissLoader(){
        popupWindow.dismiss();
    }


}
