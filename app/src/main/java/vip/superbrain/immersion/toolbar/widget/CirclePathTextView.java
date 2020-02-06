package vip.superbrain.immersion.toolbar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

import vip.superbrain.immersion.toolbar.R;

public class CirclePathTextView extends View {

    public static final String TAG = CirclePathTextView.class.getSimpleName();

    private int startAngle = 0;
    private int endAngle = 360;
    private float radius = 0;
    private StringBuilder textSB = new StringBuilder();
    private int textColor = Color.RED;
    private float textSize = 36F;

    private Paint mPaint = new Paint();
    private Paint mBackgroundPaint = new Paint();

    public CirclePathTextView(Context context) {
        this(context, null);
    }

    public CirclePathTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePathTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePathTextView);
        if (typedArray != null) {
            this.startAngle = typedArray.getInt(R.styleable.CirclePathTextView_startAngle, 0);
            this.endAngle = typedArray.getInt(R.styleable.CirclePathTextView_endAngle, 0);
            this.radius = typedArray.getDimension(R.styleable.CirclePathTextView_radius, 0F);
            this.textSB.append(typedArray.getString(R.styleable.CirclePathTextView_text));
            this.textColor = typedArray.getInt(R.styleable.CirclePathTextView_textColor, 0);
            this.textSize = typedArray.getDimension(R.styleable.CirclePathTextView_textSize, 14F);
            //
            mPaint.setColor(this.textColor);
            mPaint.setTextSize(this.textSize);
        }
        mPaint.setAntiAlias(true);

        int[] colors = {Color.RED, Color.WHITE, Color.RED};
        float[] positions = {0f, 0.5f, 0f};
        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, positions);
        mBackgroundPaint.setShader(sweepGradient);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStrokeCap(Paint.Cap.SQUARE);
        mBackgroundPaint.setStrokeWidth(textSize);
//        mBackgroundPaint.setColor(Color.parseColor("#F1824E"));
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int contentLength = textSB.length();
        log("length " + contentLength + "   startAngle  " + startAngle + "   endAngle   " + endAngle + "   radius   " + radius + "   textSize   " + textSize + "   textSB   " + textSB);
        if (contentLength < 1) {
            return;
        }
        BigDecimal startAngleBD = new BigDecimal(startAngle);
        BigDecimal endAngleBD = new BigDecimal(endAngle);
        float averageAndAngle = endAngleBD.subtract(startAngleBD).divide(new BigDecimal((contentLength - 1) + ""), 10, BigDecimal.ROUND_HALF_EVEN).floatValue();
        log("averageAndAngleBD   " + averageAndAngle);
        float fontHeight = getFontHeight(mPaint);
        float halWidth = getWidth() / 2F;
        float halHeight = getHeight() / 2F;
        canvas.drawCircle(halWidth, halHeight, 20, mPaint);
        log("fontHeight   " + fontHeight);
        canvas.drawArc(new RectF(halWidth - radius, halHeight - radius, halWidth + radius, halHeight + radius), 90 - startAngle, -(contentLength - 1) * averageAndAngle, false, mBackgroundPaint);
        canvas.rotate(startAngle, halWidth, halHeight);
        canvas.drawLine(halWidth, halHeight, halWidth + radius + 400, halHeight, mPaint);
        float textWidth = 0;
        for (int position = 0; position < contentLength; position++) {
            log("position   " + position + "    content " + textSB.substring(position, position + 1));
            String text = textSB.substring(position, position + 1);
            textWidth = getTextWidth(mPaint, text);
            canvas.drawText(text, halWidth - radius - textWidth, halHeight + fontHeight / 2, mPaint);
            canvas.drawLine(halWidth, halHeight, halWidth - radius, halHeight, mPaint);
            canvas.rotate(averageAndAngle, halWidth, halHeight);
        }
    }

    private void log(String msg) {
        Log.e(TAG, msg);
    }

    private float getFontHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top;
        return height / 2;
    }

    private float getTextWidth(Paint paint, String text) {
        return paint.measureText(text) / 2;
    }
}