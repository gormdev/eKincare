package com.ekincare.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.util.AttributeSet;
import android.view.View;

/**
 * Created by RaviTejaN on 04-11-2016.
 */

public class CutLayout  extends View {

    public CutLayout(Context context) {
        super(context);
    }

    public CutLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth() / 2;

        Path path = new Path();
        path.moveTo( w, 10);
        path.lineTo( 2 * w , 10);
        path.lineTo( 2 * w , w);
        path.lineTo( w , 10);
        path.close();

        Paint p = new Paint();
        p.setColor( Color.RED );

        canvas.drawPath(path, p);
    }
}