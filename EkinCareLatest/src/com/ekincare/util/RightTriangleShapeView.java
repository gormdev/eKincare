package com.ekincare.util;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class RightTriangleShapeView extends View {

    public RightTriangleShapeView(Context context) {
        super(context);
    }

    public RightTriangleShapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RightTriangleShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth() / 2 ;

        Path path = new Path();
       /* path.moveTo( w, 0);
        path.lineTo( 2 * w , 0);
        path.lineTo( 2 * w , getHeight());
        path.lineTo( w , 0);*/
        path.moveTo( 0,getHeight());
        path.lineTo( getWidth() , getHeight());
        path.lineTo( 0 , 0);
        path.lineTo(0,getHeight());
        path.close();

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setColor( Color.parseColor("#FFFFFF") );

        canvas.drawPath(path, p);
    }
}