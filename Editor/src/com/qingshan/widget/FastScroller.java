package com.qingshan.widget;
import com.qingshan.editor.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Layout;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.SectionIndexer;

class FastScroller
{
	private static int MIN_PAGES = 0;
	private static final int STATE_DRAGGING = 3;
	private static final int STATE_EXIT = 4;
	private static final int STATE_NONE = 0;
	private static final int STATE_VISIBLE = 2;
	private boolean mChangedBounds;
	private Handler mHandler = new Handler();
	private int mItemCount = -1;
	private EditText mList;
	private boolean mLongList;
	private Paint mPaint;
	private ScrollFade mScrollFade;
	private SectionIndexer mSectionIndexer;
	private Object[] mSections;
	private int mState;
	private Drawable mThumbDrawable;
	private int mThumbH;
	private int mThumbW;
	private int mThumbY;
	private int mVisibleItem;
	static {
        MIN_PAGES = 1;
    }
	public FastScroller(Context context, EditText listView) {
        //this.mItemCount = -1;
        //this.mHandler = new Handler();
        this.mList = listView;
        init(context);
    }
	private void init(Context context) {
        useThumbDrawable(context, context.getResources().getDrawable(R.drawable.scrollbar_handle_accelerated_anim2));
        getSectionsFromIndexer();
        this.mScrollFade = new ScrollFade();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextAlign(Align.CENTER);
        TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.textColorPrimary/*16842806*/});
        this.mPaint.setColor(ta.getColorStateList(ta.getIndex(STATE_NONE)).getDefaultColor());
        this.mPaint.setStyle(Style.FILL_AND_STROKE);
        this.mState = STATE_NONE;
    }
	
	private void useThumbDrawable(Context context, Drawable drawable) {
        this.mThumbDrawable = drawable;
        this.mThumbW = context.getResources().getDimensionPixelSize(R.dimen.fastscroll_thumb_width);
        this.mThumbH = context.getResources().getDimensionPixelSize(R.dimen.fastscroll_thumb_height);
        this.mChangedBounds = true;
    }
	private void getSectionsFromIndexer() {
        this.mSectionIndexer = null;
        this.mSections = new String[]{" "};
    }
	
	public void draw(Canvas canvas) {
        if (this.mState != 0) {
            int y = this.mThumbY + this.mList.getScrollY();
            int viewWidth = this.mList.getWidth();
            ScrollFade scrollFade = this.mScrollFade;
            int x = this.mList.getScrollX();
            int alpha = -1;
            if (this.mState == STATE_EXIT) {
                alpha = scrollFade.getAlpha();
                if (alpha < ScrollFade.ALPHA_MAX / 2/*104/*R.styleable.Theme_textViewStyle*/) {
                    this.mThumbDrawable.setAlpha(alpha * 2/*STATE_VISIBLE*/);
                }
                this.mThumbDrawable.setBounds(viewWidth - ((this.mThumbW * alpha) / ScrollFade.ALPHA_MAX /*208*/), STATE_NONE, viewWidth, this.mThumbH);
                this.mChangedBounds = true;
            }
            canvas.translate((float) x, (float) y);
            this.mThumbDrawable.draw(canvas);
            canvas.translate((float) (-x), (float) (-y));
            if (alpha == 0) {
                setState(STATE_NONE);
            } else {
                this.mList.invalidate(viewWidth - this.mThumbW, y, viewWidth, this.mThumbH + y);
            }
        }
    }
	private void cancelFling() {
        MotionEvent cancelFling = MotionEvent.obtain(0, 0, STATE_DRAGGING, 0.0f, 0.0f, STATE_NONE);
        this.mList.onTouchEvent(cancelFling);
        cancelFling.recycle();
    }

	

	

	

	private void scrollTo(float position) {
        try {
            int offset = this.mList.getLayout().getLineStart((int) (((float) this.mList.getLineCount()) * position));
            this.mList.setSelection(offset, offset);
        } catch (Exception e) {
        }
    }

	

	

	SectionIndexer getSectionIndexer()
	{
		return this.mSectionIndexer;
	}

	Object[] getSections()
	{
		if (this.mSections == null)
			getSectionsFromIndexer();
		return this.mSections;
	}

	

	boolean isPointInside(float x, float y)
	{
		/*boolean i;
		if ((paramFloat1 <= this.mList.getWidth() - this.mThumbW) || (paramFloat2 < this.mThumbY) || (paramFloat2 > this.mThumbY + this.mThumbH))
			i = false;
		else
			i = true;
		return i;*/
		return x > mList.getWidth() - mThumbW && y >= mThumbY && y <= mThumbY + mThumbH;
	}
	
	boolean isVisible()
	{
		/*boolean i;
		if (this.mState != 0)
			i = true;
		else
			i = false;
		return i;*/
		return !(mState == STATE_NONE);
	}

	boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.mState <= STATE_NONE/*0*/ || ev.getAction() != MotionEvent.ACTION_DOWN/*0*/ || !isPointInside(ev.getX(), ev.getY())) {
            return false;
        }
        setState(STATE_DRAGGING);
        return true;
    }
	
	void onScroll(EditText view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.mItemCount != totalItemCount && visibleItemCount > 0) {
            this.mItemCount = totalItemCount;
            this.mLongList = this.mItemCount / visibleItemCount >= MIN_PAGES;
        }
        if (this.mLongList) {
            if (totalItemCount - visibleItemCount > 0 && this.mState != STATE_DRAGGING) {
                this.mThumbY = ((this.mList.getHeight() - this.mThumbH) * firstVisibleItem) / (totalItemCount - visibleItemCount);
                if (this.mChangedBounds) {
                    resetThumbPos();
                    this.mChangedBounds = false;
                }
            }
            if (firstVisibleItem != this.mVisibleItem) {
                this.mVisibleItem = firstVisibleItem;
                if (this.mState != STATE_DRAGGING) {
                    setState(STATE_VISIBLE);
                    this.mHandler.postDelayed(this.mScrollFade, 1500);
                }
            }
        } else if (this.mState != 0) {
            setState(STATE_NONE);
        }
    }

	void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setBounds(w - this.mThumbW, STATE_NONE, w, this.mThumbH);
        }
    }

	

	
	void stop()
	{
		setState(STATE_NONE);
	}

	public class ScrollFade implements Runnable {
        static final int ALPHA_MAX = 208;
        static final long FADE_DURATION = 200;
        long mFadeDuration;
        long mStartTime;

        void startFade() {
            this.mFadeDuration = FADE_DURATION;
            this.mStartTime = SystemClock.uptimeMillis();
            FastScroller.this.setState(FastScroller.STATE_EXIT);
        }

        int getAlpha() {
            if (FastScroller.this.getState() != FastScroller.STATE_EXIT) {
                return ALPHA_MAX;
            }
            long now = SystemClock.uptimeMillis();
            if (now > this.mStartTime + this.mFadeDuration) {
                return FastScroller.STATE_NONE;
            }
            return (int) (208 - (((now - this.mStartTime) * 208) / this.mFadeDuration));
        }

        public void run() {
            if (FastScroller.this.getState() != FastScroller.STATE_EXIT) {
                startFade();
            } else if (getAlpha() > 0) {
                FastScroller.this.mList.invalidate();
            } else {
                FastScroller.this.setState(FastScroller.STATE_NONE);
            }
        }
    }
	public void setState(int state) {
        switch (state) {
            case STATE_NONE /*0*/:
                this.mHandler.removeCallbacks(this.mScrollFade);
                this.mList.invalidate();
                break;
            case STATE_VISIBLE /*2*/:
                if (this.mState != STATE_VISIBLE) {
                    resetThumbPos();
                    break;
                }
                break;
            case STATE_DRAGGING /*3*/:
                break;
            case STATE_EXIT /*4*/:
                int viewWidth = this.mList.getWidth();
                this.mList.invalidate(viewWidth - this.mThumbW, this.mThumbY, viewWidth, this.mThumbY + this.mThumbH);
                break;
        }
        this.mHandler.removeCallbacks(this.mScrollFade);
        this.mState = state;
    }
	public int getState()
	{
		return this.mState;
	}
	private void resetThumbPos() {
        int viewWidth = this.mList.getWidth();
        this.mThumbDrawable.setBounds(viewWidth - this.mThumbW, STATE_NONE, viewWidth, this.mThumbH);
        this.mThumbDrawable.setAlpha(208);
    }
	
	boolean onTouchEvent(MotionEvent me) {
        if (this.mState == 0) {
            return false;
        }
        int action = me.getAction();
        if (action == MotionEvent.ACTION_DOWN/*0*/) {
            if (!isPointInside(me.getX(), me.getY())) {
                return false;
            }
            setState(STATE_DRAGGING);
            if (this.mSections == null) {
                getSectionsFromIndexer();
            }
            cancelFling();
            return true;
        } else if (action == MotionEvent.ACTION_UP/*1*/) {
            if (this.mState != STATE_DRAGGING) {
                return false;
            }
            setState(STATE_VISIBLE);
            Handler handler = this.mHandler;
            handler.removeCallbacks(this.mScrollFade);
            handler.postDelayed(this.mScrollFade, 1000);
            return true;
        } else if (action != MotionEvent.ACTION_MOVE/*STATE_VISIBLE */|| this.mState != STATE_DRAGGING) {
            return false;
        } else {
            int viewHeight = this.mList.getHeight();
            int newThumbY = ((int) me.getY()) - (this.mThumbH / STATE_VISIBLE);
            if (newThumbY < 0) {
                newThumbY = STATE_NONE;
            } else if (this.mThumbH + newThumbY > viewHeight) {
                newThumbY = viewHeight - this.mThumbH;
            }
            if (Math.abs(this.mThumbY - newThumbY) < STATE_VISIBLE) {
                return true;
            }
            this.mThumbY = newThumbY;
            scrollTo(((float) this.mThumbY) / ((float) (viewHeight - this.mThumbH)));
            return true;
        }
    }
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.FastScroller
 * JD-Core Version:    0.6.0
 */
