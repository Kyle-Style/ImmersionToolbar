package vip.superbrain.immersion.lib.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ImmersionToolbar extends ConstraintLayout {

    private static final int DEFAULT_STATUS_BAR_COLOR = Color.TRANSPARENT;
    private static final String DEFAULT_TITLE = "只是因为在人群中多看了你一眼";
    private static final int DEFAULT_TITLE_COLOR = Color.BLACK;
    private static final int DEFAULT_TITLE_SIZE = 0;
    private static final int DEFAULT_BOTTOM_LINE_COLOR = Color.BLACK;
    private static final int DEFAULT_BOTTOM_LINE_HEIGHT = 1;
    private static final boolean DEFAULT_IS_SUPPORT_IMMERSION = true;
    private static final boolean DEFAULT_IS_SUPPORT_BOTTOM_LINE = true;

    int statusBarBg = DEFAULT_STATUS_BAR_COLOR;
    Paint statusBarPaint = new Paint();
    Paint titlePaint = new Paint();
    Paint bottomLinePaint = new Paint();

    boolean isSupportImmersion = DEFAULT_IS_SUPPORT_IMMERSION;
    String title = DEFAULT_TITLE;
    int titleSize = 50;
    int titleColor = DEFAULT_TITLE_COLOR;
    boolean isSupportBottomLine = DEFAULT_IS_SUPPORT_BOTTOM_LINE;
    int bottomLineHeight = DEFAULT_BOTTOM_LINE_HEIGHT;
    int bottomLineColor = DEFAULT_BOTTOM_LINE_COLOR;


    public ImmersionToolbar(Context context) {
        this(context, null);
    }

    public ImmersionToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImmersionToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        statusBarPaint.setAntiAlias(true);
        titlePaint.setAntiAlias(true);
        bottomLinePaint.setAntiAlias(true);
        //
        if (isSupportImmersion) {
            statusBarPaint.setColor(statusBarBg);
        }
        titlePaint.setColor(titleColor);
        titlePaint.setTextSize(titleSize);
        bottomLinePaint.setColor(bottomLineColor);
    }

    private void initStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Toolbar_Style, defStyleAttr, 0);
        if (typedArray != null) {
            int styleCount = typedArray.getIndexCount();
            for (int i = 0; i < styleCount; i++) {
                int index = typedArray.getIndex(i);
                if (index == R.styleable.Toolbar_Style_statusBarBg) {
                    statusBarBg = typedArray.getColor(index, DEFAULT_STATUS_BAR_COLOR);
                } else if (index == R.styleable.Toolbar_Style_isSupportImmersion) {
                    isSupportImmersion = typedArray.getBoolean(index, DEFAULT_IS_SUPPORT_IMMERSION);
                } else if (index == R.styleable.Toolbar_Style_title) {
                    title = typedArray.getString(index);
                } else if (index == R.styleable.Toolbar_Style_titleColor) {
                    titleColor = typedArray.getInt(index, DEFAULT_TITLE_COLOR);
                } else if (index == R.styleable.Toolbar_Style_titleSize) {
                    titleSize = typedArray.getInt(index, DEFAULT_TITLE_SIZE);
                } else if (index == R.styleable.Toolbar_Style_bottomLineColor) {
                    isSupportBottomLine = typedArray.getBoolean(index, DEFAULT_IS_SUPPORT_BOTTOM_LINE);
                } else if (index == R.styleable.Toolbar_Style_bottomLineColor) {
                    bottomLineColor = typedArray.getInt(index, DEFAULT_BOTTOM_LINE_COLOR);
                } else if (index == R.styleable.Toolbar_Style_bottomLineHeight) {
                    bottomLineHeight = typedArray.getInt(index, DEFAULT_BOTTOM_LINE_HEIGHT);
                }
            }
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int realHeight = (int) getResources().getDimension(R.dimen.default_toolbar_height);
        if (isSupportImmersion) {
            int statusHeight = getStatusBarHeight();
            realHeight += getStatusBarHeight();
            setPadding(getPaddingLeft(), statusHeight, getPaddingRight(), isSupportBottomLine ? getPaddingBottom() + bottomLineHeight : getPaddingBottom());
        }
        if (isSupportBottomLine) {
            realHeight += bottomLineHeight;
        }
        setMeasuredDimension(widthSize, realHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isSupportImmersion) {
            canvas.drawRect(0, 0, getWidth(), getStatusBarHeight(), statusBarPaint);
        }
        drawTitle(canvas, title);
        drawBottomLine(canvas);
    }

    private void drawTitle(Canvas canvas, @NonNull String title) {
        if (!TextUtils.isEmpty(title)) {
            int textHeight = 0;
            int textWidth = 0;
            Rect textRect = new Rect();
            titlePaint.getTextBounds(title, 0, title.length(), textRect);
            textHeight = textRect.bottom - textRect.top;
            textWidth = textRect.right - textRect.left;
            int toolbarBottomHeight = (int) getResources().getDimension(R.dimen.default_toolbar_height);
            canvas.drawText(title, (getWidth() - textWidth) / 2, (getHeight() - (toolbarBottomHeight - textHeight) / 2), titlePaint);
        }
    }

    private void drawBottomLine(Canvas canvas) {
        if (isSupportBottomLine) {
            int realHeightAbove = (int) getResources().getDimension(R.dimen.default_toolbar_height);
            if (isSupportImmersion) {
                realHeightAbove += getStatusBarHeight();
            }
            canvas.drawRect(0, realHeightAbove, getWidth(), getHeight(), bottomLinePaint);
        }
    }

    private int getStatusBarHeight() {
        int height = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
}