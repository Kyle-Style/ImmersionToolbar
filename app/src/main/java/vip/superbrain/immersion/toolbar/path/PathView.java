package vip.superbrain.immersion.toolbar.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PathView extends View {

    final String CONTENT = "只是因为在人群中多看了你一眼";
    Path path = new Path();
    Paint paint;
    RectF rectF = new RectF(100, 0, 600, 600);

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
//        path.addOval(rectF, Path.Direction.CCW);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(1);
        path.addArc(rectF, -120, -120);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawColor(Color.WHITE);
        //
        paint.setTextSize(40);
        //
        //从右边开始绘制
        paint.setTextAlign(Paint.Align.RIGHT);//设置Paint文字对齐


        //沿着路径绘制字符串
        paint.setStyle(Paint.Style.FILL);
        canvas.drawTextOnPath(CONTENT, path, 0, 0, paint);
        //绘制路径
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        rectF.left = 0F;
//        rectF.right = MeasureSpec.getSize(widthMeasureSpec);
//        rectF.top = 0F;
//        rectF.bottom = MeasureSpec.getSize(heightMeasureSpec);
//        path.addOval(rectF, Path.Direction.CCW);
        invalidate();
    }
}
