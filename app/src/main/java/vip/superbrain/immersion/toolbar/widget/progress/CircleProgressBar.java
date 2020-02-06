package vip.superbrain.immersion.toolbar.widget.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import vip.superbrain.immersion.toolbar.R;

public class CircleProgressBar extends ProgressBar {

    public static final int DEFAULT_CAPACITY = 8;

    public static final String TAG = CircleProgressBar.class.getSimpleName();

    public interface OnProgressChangeForTextListener {

        /**
         * 体提供外部在view绘制前根据进度修改文字的入口
         *
         * @param progressBar 控件
         * @param progress    进度，默认是[0, 100]
         */
        void onBeforeProgressChanged(final CircleProgressBar progressBar, int progress, int max);
    }

    // 外层圆环背景
    private int mOuterBgColor;
    // 外层圆环宽度
    private float mOuterBgWidth;
    // 进度颜色
    private int mProgressColor;
    // 进度宽度
    private float mProgressWidth;
    // 中间填充颜色
    private int mFillColor;
    // 文字颜色
    private int mTextColor;
    // 文字字体大小
    private float mTextSize;
    // 文字行间距
    private float mTextGap;

    private float mRadius;
    // 图形画笔
    private Paint mPaint;
    // 文字画笔
    private Paint mTextPaint;
    // 填充画笔
    private Paint mFillPaint;


    private PointF centerPoint = new PointF(0, 0);
    float[][] textWidthHeight = new float[DEFAULT_CAPACITY][2];
    float[][] textLeftTop = new float[DEFAULT_CAPACITY][2];
    float maxHeight = 0;
    float startY = 0;
    RectF progressRectF = new RectF();

    private Status mStatus = Status.Loading;
    private Status mBeforeFinishStatus = mStatus;

    private float mVisualProgress = 0;
    private float mSavedProgress = 0;
    private boolean supportAnimation = true;
    private boolean autoAnimation = false;
    final ValueAnimator valueAnimator = ValueAnimator.ofFloat();

    // 要显示的文字内容，默认居中显示，支持多行和设置行高
    private List<String> mContent = new ArrayList<>();

    private OnProgressChangeForTextListener mProgressChangeForTextListener;

    LinearInterpolator interpolator = new LinearInterpolator();

    ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float progressTemp = (float) animation.getAnimatedValue();
            if (progressTemp < 0) {
                return;
            }
            mVisualProgress = progressTemp;
            invalidate();
            if (mVisualProgress == getProgress()) {
            }
        }
    };

    public OnProgressChangeForTextListener getOnProgressChangeForTextListener() {
        return mProgressChangeForTextListener;
    }

    public void setOnProgressChangeForTextListener(OnProgressChangeForTextListener onProgressChangeForTextListener) {
        this.mProgressChangeForTextListener = onProgressChangeForTextListener;
    }

    public void setContent(List<String> content) {
        if (content == null || content.size() == 0) {
            this.mContent.clear();
        }
        this.mContent = content;
        invalidate();
    }

    public void setOuterBgColor(int outerBgColor) {
        this.mOuterBgColor = outerBgColor;
        invalidate();
    }

    public void setOuterBgWidth(float outerBgWidth) {
        this.mOuterBgWidth = outerBgWidth;
        invalidate();
    }

    public void setProgressColor(int progressColor) {
        this.mProgressColor = progressColor;
        invalidate();
    }

    public void setProgressWidth(float progressWidth) {
        this.mProgressWidth = progressWidth;
        invalidate();
    }

    public void setFillColor(int centerColor) {
        this.mFillColor = centerColor;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
        invalidate();
    }

    public void setTextGap(float textGap) {
        this.mTextGap = textGap;
        invalidate();
    }

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        // 外圆的高度
        mOuterBgWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_outerBgWidth, dp2px(context, 4.0f));
        // 进度条的高度
        mProgressWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_progressWidth, dp2px(context, 2f));
        // 外圆的默认半径
        mRadius = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_default_radius, dp2px(context, 25f));
        // 外圆的默认颜色
        mOuterBgColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_outerBgColor, Color.parseColor("#D9433C"));
        // 进度条的颜色
        mProgressColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_progressColor, Color.parseColor("#B8F9CC7F"));
        // 填充颜色
        mFillColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_fillColor, Color.parseColor("#EA544D"));
        // 文字颜色
        mTextColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_textColor, Color.parseColor("#F9CC7F"));
        // 文字字体大小
        mTextSize = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_textSize, dp2px(context, 14f));
        // 文字行间距
        mTextGap = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_textLineGap, dp2px(context, 3f));
        typedArray.recycle();
        setPaint();
    }

    private void setPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动，绘制出来的图要更加柔和清晰
        mPaint.setStyle(Paint.Style.STROKE);//设置填充样式
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔笔刷类型
        //
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);//抗锯齿
        mTextPaint.setDither(true);//防抖动，绘制出来的图要更加柔和清晰
        mTextPaint.setStyle(Paint.Style.FILL);//设置填充样式
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        //
        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);//抗锯齿
        mFillPaint.setDither(true);//防抖动，绘制出来的图要更加柔和清晰
        mFillPaint.setStyle(Paint.Style.FILL);//设置填充样式
        mFillPaint.setColor(mFillColor);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        float paintHeight = Math.max(mProgressWidth, mOuterBgWidth);//比较两数，取最大值
        if (heightMode != MeasureSpec.EXACTLY) {
            //如果用户没有精确指出宽高时，我们就要测量整个View所需要分配的高度了，测量自定义圆形View设置的上下内边距+圆形view的直径+圆形描边边框的高度
            int exceptHeight = (int) (getPaddingTop() + getPaddingBottom() + mRadius * 2 + paintHeight);
            //然后再将测量后的值作为精确值传给父类，告诉他我需要这么大的空间，你给我分配吧
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight, MeasureSpec.EXACTLY);
        }
        if (widthMode != MeasureSpec.EXACTLY) {
            //这里在自定义属性中没有设置圆形边框的宽度，所以这里直接用高度代替
            int exceptWidth = (int) (getPaddingLeft() + getPaddingRight() + mRadius * 2 + paintHeight);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(exceptWidth, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initCenterParameter();
        beforeDraw();
        doDraw(canvas);
        drawText(canvas);
    }

    private void initCenterParameter() {
        centerPoint.x = getWidth() / 2 + getPaddingLeft();
        centerPoint.y = getHeight() / 2 + getPaddingLeft();
    }

    private void beforeDraw() {
        if (mProgressChangeForTextListener != null) {
            mProgressChangeForTextListener.onBeforeProgressChanged(this, getProgress(), getMax());
        }
    }

    private void drawText(Canvas canvas) {
        canvas.save();
        int contentRows = mContent.size();
        if (textWidthHeight.length < contentRows) {
            float[][] newArray = new float[textWidthHeight.length * 2][2];
            System.arraycopy(textWidthHeight, 0, newArray, 0, textWidthHeight.length);
            textWidthHeight = newArray;
        } else {
        }
        if (mContent != null) {
            for (int position = 0; position < contentRows; position++) {
                getTextWidthHeight(textWidthHeight[position], mContent.get(position), mTextPaint);
                if (maxHeight < textWidthHeight[position][1]) {
                    maxHeight = textWidthHeight[position][1];
                }
            }
            if (contentRows == 1) {
                startY = (getHeight() + maxHeight) / 2 + getPaddingTop();
            } else {
                startY = (getHeight() - (contentRows - 1) * (mTextGap + maxHeight)) / 2 + getPaddingTop() + maxHeight / 2;
            }
            for (int position = 0; position < contentRows; position++) {
                textLeftTop[position][0] = (getWidth() - getPaddingLeft() - textWidthHeight[position][0]) / 2;
                textLeftTop[position][1] = startY + ((position) * (maxHeight + mTextGap));
                canvas.drawText(mContent.get(position), textLeftTop[position][0], textLeftTop[position][1], mTextPaint);
            }
        }
        canvas.restore();
    }

    private void doDraw(Canvas canvas) {
        canvas.save();
        if (getProgress(supportAnimation) == getMax()) {
            mStatus = Status.Finish;
        } else {
            if (getProgress(supportAnimation) == 0) {
                mStatus = Status.Waiting;
                mBeforeFinishStatus = Status.Waiting;
            } else {
                mBeforeFinishStatus = Status.Loading;
                mStatus = mBeforeFinishStatus;
            }
        }
        // 绘制外部圆环
        mRadius = Math.max(getWidth(), getHeight()) / 2 - mOuterBgWidth / 2;
        mPaint.setColor(mOuterBgColor);
        mPaint.setStrokeWidth(mOuterBgWidth);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
        // 绘制内部填充
        mRadius = checkSize(Math.max(getWidth(), getHeight()) / 2 - mOuterBgWidth);
        mFillPaint.setColor(mFillColor);
        canvas.drawCircle(centerPoint.x, centerPoint.y, mRadius, mFillPaint);
        //
        if (mStatus == Status.Loading || mStatus == Status.Finish) {
            mPaint.setColor(mProgressColor);
            mPaint.setStrokeWidth(checkSize(mProgressWidth));
            float sweepAngle = getProgress(supportAnimation) * 1.0f / getMax() * 360;
            float margin = mOuterBgWidth;
            progressRectF.left = margin - mProgressWidth / 2;
            progressRectF.top = margin - mProgressWidth / 2;
            progressRectF.right = progressRectF.left + mRadius * 2 + mProgressWidth;
            progressRectF.bottom = progressRectF.top + mRadius * 2 + mProgressWidth;
            canvas.drawArc(progressRectF, -90, sweepAngle, false, mPaint);
        } else {
        }
        canvas.restore();
    }

    @Override
    public synchronized void setProgress(int desProgress) {
        mSavedProgress = getProgress();
        super.setProgress(desProgress);
        if (!supportAnimation) {
        } else {
            mVisualProgress = desProgress;
            if (autoAnimation) {
                startAnimation(mSavedProgress, mVisualProgress);
            }
        }
    }

    public void startAnimation() {
        float to = mVisualProgress;
        valueAnimator.cancel();
        valueAnimator.setFloatValues(mSavedProgress, to);
        valueAnimator.setDuration((long) Math.abs(to - mSavedProgress) * 1000);
        valueAnimator.start();
    }

    public void startAnimation(float from, float to) {
        valueAnimator.cancel();
        valueAnimator.setFloatValues(from, to);
        valueAnimator.setDuration((long) Math.abs(to - from) * 1000);
        valueAnimator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelAnimation();
        super.onDetachedFromWindow();
    }

    private void initAnimation() {
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(listener);
    }

    private void cancelAnimation() {
        valueAnimator.removeAllUpdateListeners();
    }

    private float getProgress(boolean animate) {
        if (animate) {
            return mVisualProgress;
        } else {
            return getProgress();
        }
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        if (mStatus == status) return;
        mStatus = status;
        mBeforeFinishStatus = status;
        invalidate();
    }

    public enum Status {
        Waiting,
        Pause,
        Loading,
        Error,
        Finish
    }

    float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private void getTextWidthHeight(@NonNull float[] textWidthHeight, String text, Paint paint) {
        if (TextUtils.isEmpty(text) || paint == null) {
        } else {
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            textWidthHeight[0] = rect.width();
            textWidthHeight[1] = rect.height();
        }
    }

    /**
     * 检查绘制相关参数，以免造成1像素的差异
     */
    private float checkSize(float size) {
        if (size * 10 % 10 == 0) {
        } else {
            size += 1;
        }
        return size;
    }
}