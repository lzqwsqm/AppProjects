package com.qingshan.widget;
import com.qingshan.editor.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

//顶部标签
public class TabWidget extends LinearLayout
{
	public static final int MENU_ACTION_CLOSE_ALL = 3;
	public static final int MENU_ACTION_CLOSE_ONE = 0;
	public static final int MENU_ACTION_CLOSE_OTHER = 1;
	public static final int MENU_ACTION_CLOSE_RIGHT = 2;
	private int mMenuCurrentIndex;
	//private OnClickListener mOnClickListener;
	private OnClickListener mOnClickListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			int i = TabWidget.this.indexOfChild(v);
			if (i >= 0)
			{
				TabWidget.this.setCurrentTab(i);
				TabWidget.this.mSelectionChangedListener.onTabSelectionChanged(i);
			}
		}
	};
	private OnMenuClickListener mOnMenuClickListener;
	private PopupWindow mPopup;
	private int mSelectedTab = 0;
	private OnTabSelectionChanged mSelectionChangedListener;
	
	interface OnMenuClickListener {
        void onMenuClick(int i, int i2);
    }
	
	interface OnTabSelectionChanged {
        void onTabSelectionChanged(int i);
    }
	
	//构造函数
	public TabWidget(Context context) {
        super(context);
        /*this.mSelectedTab = MENU_ACTION_CLOSE_ONE;
        this.mOnClickListener = new OnClickListener() {
            public void onClick(View v) {
                int index = TabWidget.this.indexOfChild(v);
                if (index >= 0) {
                    TabWidget.this.setCurrentTab(index);
                    TabWidget.this.mSelectionChangedListener.onTabSelectionChanged(index);
                }
            }
        };*/
        initTabWidget(context);
    }

	public TabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*this.mSelectedTab = MENU_ACTION_CLOSE_ONE;
        this.mOnClickListener = new OnClickListener() {
            public void onClick(View v) {
                int index = TabWidget.this.indexOfChild(v);
                if (index >= 0) {
                    TabWidget.this.setCurrentTab(index);
                    TabWidget.this.mSelectionChangedListener.onTabSelectionChanged(index);
                }
            }
        };*/
        initTabWidget(context);
    }

	private void initTabWidget(Context context) {
        // Deal with focus, as we don't want the focus to go by default
        // to a tab other than the current tab
		setFocusable(true);
        View mView = LayoutInflater.from(context).inflate(R.layout.tab_menu, null);
        //不能这么用，一定要传正确的context，不然无法点其它地方关闭菜单
        //mPopup = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		this.mPopup = new PopupWindow(context);
        this.mPopup.setContentView(mView);
		mPopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.mPopup.setInputMethodMode(MENU_ACTION_CLOSE_RIGHT);
        this.mPopup.setOutsideTouchable(true);
        this.mPopup.setFocusable(true);
        this.mPopup.setBackgroundDrawable(new BitmapDrawable());
        LinearLayout linearLayout_closeothers = (LinearLayout) mView.findViewById(R.id.linearLayout_closeothers);
        LinearLayout linearLayout_closeright = (LinearLayout) mView.findViewById(R.id.linearLayout_closeright);
        ((LinearLayout) mView.findViewById(R.id.linearLayout_closeall)).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TabWidget.this.hideMenu();
					if (TabWidget.this.mOnMenuClickListener != null) {
						TabWidget.this.mOnMenuClickListener.onMenuClick(TabWidget.MENU_ACTION_CLOSE_ALL, TabWidget.this.mMenuCurrentIndex);
					}
				}
			});
        linearLayout_closeothers.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TabWidget.this.hideMenu();
					if (TabWidget.this.mOnMenuClickListener != null) {
						TabWidget.this.mOnMenuClickListener.onMenuClick(TabWidget.MENU_ACTION_CLOSE_OTHER, TabWidget.this.mMenuCurrentIndex);
					}
				}
			});
        linearLayout_closeright.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TabWidget.this.hideMenu();
					if (TabWidget.this.mOnMenuClickListener != null) {
						TabWidget.this.mOnMenuClickListener.onMenuClick(TabWidget.MENU_ACTION_CLOSE_RIGHT, TabWidget.this.mMenuCurrentIndex);
					}
				}
			});
    }
	
	//TabHost.scrollToSelected调用
	public View getChildTabViewAt(int index) {
        return getChildAt(index);
    }
	//TabHost.scrollToSelected调用
	public void focusCurrentTab(int index) {
        setCurrentTab(index);
    }
	//focusCurrentTab调用
	public void setCurrentTab(int index) {
        if (index >= 0 && index < getTabCount()) {
            getChildTabViewAt(this.mSelectedTab).setSelected(false);
            getChildTabViewAt(this.mSelectedTab).setBackgroundResource(R.drawable.tab_indicator);
            this.mSelectedTab = index;
            getChildTabViewAt(this.mSelectedTab).setSelected(true);
            getChildTabViewAt(this.mSelectedTab).setBackgroundResource(R.drawable.tab_indicator_current);
            if (this.mSelectedTab == 0) {
                LayoutParams lp = (LayoutParams) getChildTabViewAt(this.mSelectedTab).getLayoutParams();
                lp.leftMargin = 0;
                getChildTabViewAt(this.mSelectedTab).setLayoutParams(lp);
            }
        }
    }
	//setCurrentTab调用
	public int getTabCount()
	{
		return getChildCount();
	}
	
	
	//TabHost.initTabHost调用
	public void setOnMenuClickListener(OnMenuClickListener l) {
        this.mOnMenuClickListener = l;
    }
	//TabHost.initTabHost调用
	void setTabSelectionListener(OnTabSelectionChanged listener) {
        this.mSelectionChangedListener = listener;
    }
	
	//initTabWidget调用
	public void hideMenu()
	{
		if (this.mPopup.isShowing())
			this.mPopup.dismiss();
	}
	private void showTabMenu(View v, int index) {
        this.mMenuCurrentIndex = index;
        this.mPopup.showAsDropDown(v, 0, -8);
    }
    
	@Override
	//添加控件
	public void addView(View child) {
        child.setFocusable(true);
        child.setClickable(true);
        if (getTabCount() == 0) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.leftMargin = 0;
            child.setLayoutParams(lp);
        }
        super.addView(child);
        child.setOnClickListener(this.mOnClickListener);
        child.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View v) {
					int index = TabWidget.this.indexOfChild(v);
					if (index >= 0) {
						TabWidget.this.showTabMenu(v, index);
					}
					return false;
				}
			});
    }
   @Override
	

	protected void dispatchDraw(Canvas paramCanvas) {
        int count = getTabCount();
        if (count >= MENU_ACTION_CLOSE_OTHER && this.mSelectedTab >= 0) {
            View mView;
            long drawingTime = getDrawingTime();
            for (int i = count - 1; i >= 0; i--) {
                if (i != this.mSelectedTab) {
                    mView = getChildAt(i);
                    if (mView != null) {
                        drawChild(paramCanvas, mView, drawingTime);
                    }
                }
            }
		   //覆盖在其它tab上面，即是将它置于最前
            mView = getChildAt(this.mSelectedTab);
            if (mView != null) {
                drawChild(paramCanvas, mView, drawingTime);
            }
        }
    }

	

	

	
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    @Override
	public void removeViewAt(int tabId) {
        this.mSelectedTab = MENU_ACTION_CLOSE_ONE;
        super.removeViewAt(tabId);
    }
	

	

	

	

	
	
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.TabWidget
 * JD-Core Version:    0.6.0
 */
