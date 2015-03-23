package com.development.napptime.pix;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by arni on 3/23/15.
 */
class ShadowView extends LinearLayout {
    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Shadow.onDraw(this, canvas);
    }
}
