package com.brianbett.urembom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

abstract public class SwipeToDelete extends ItemTouchHelper.Callback {
    Context context;
    private final Paint paint;
    private final ColorDrawable backgroundDrawable;
    private final int backgroundColor;
    private Drawable deleteDrawable;
    private final int intrinsicWidth;
    private final int intrinsicHeight;

    public SwipeToDelete(Context context) {
        this.context = context;
        backgroundDrawable=new ColorDrawable();
        paint=new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable= ContextCompat.getDrawable(context,R.drawable.ic_baseline_delete_24);
        intrinsicWidth=deleteDrawable.getIntrinsicWidth();
        intrinsicHeight=deleteDrawable.getIntrinsicHeight();
        backgroundColor= Color.parseColor("#f037a5");

    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.LEFT);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView=viewHolder.itemView;
        int itemHeight=itemView.getHeight();
        boolean isCancelled=dX==0 && !isCurrentlyActive;
        if(isCancelled){
            clearCanvas(c,itemView.getRight()+dX,(float)itemView.getTop(),(float)itemView.getRight(),(float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, false);

        }
        else {
            backgroundDrawable.setColor(backgroundColor);
            backgroundDrawable.setBounds(itemView.getRight()+(int)dX,itemView.getTop(),itemView.getRight(),itemView.getBottom());
            backgroundDrawable.draw(c);
            int deleteIconTop=itemView.getTop()+(itemHeight-intrinsicHeight)/2;
            int deleteIconMargin=(itemHeight-intrinsicHeight)/2;
            int deleteIconLeft=itemView.getRight()-deleteIconMargin-intrinsicWidth;
            int deleteIconRight=itemView.getRight()-deleteIconMargin;
            int deleteIconBottom=deleteIconTop+intrinsicHeight;

            deleteDrawable.setBounds(deleteIconLeft,deleteIconTop,deleteIconRight,deleteIconBottom);
            deleteDrawable.draw(c);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, true);
        }


    }

//    custom method
    private void clearCanvas(Canvas c,Float left,Float top,Float right,Float bottom){
        c.drawRect(left,top,right,bottom,paint);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }




    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.5f;
    }
}
