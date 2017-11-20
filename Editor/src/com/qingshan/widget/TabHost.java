package com.qingshan.widget;
import com.qingshan.editor.R;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.qingshan.colorschemes.ColorScheme;
import com.qingshan.editor.QSEditor;
import com.qingshan.editor.Options;
import com.qingshan.highlight.Highlight;
import com.qingshan.editor.UndoParcel;
import com.qingshan.widget.QSEditText.OnTextChangedListener;
import java.util.ArrayList;
import java.util.Iterator;

public class TabHost extends LinearLayout
{
	public static boolean autoNewTab = true;
	private QSEditText mCurrentEditText = null;
	protected int mCurrentTab = -1;
	private QSEditor mJecEditor;
	protected LocalActivityManager mLocalActivityManager = null;
	
	private TabWidget.OnMenuClickListener mOnMenuClickListener = new TabWidget.OnMenuClickListener() {
		public void onMenuClick(int action, int index) {
			TabHost.autoNewTab = true;
			TabHost.this.iterCloseTab(action, index, TabHost.this.mTabWidget.getTabCount());
		}
	};
	
	private OnTabChangeListener mOnTabChangeListener;
	private OnTabCloseListener mOnTabCloseListener;
	private QSEditText.OnTextChangedListener mOnTextChangedListener = null;
	private HorizontalScrollView mScroller;
	private LinearLayout mTabContent;
	private ArrayList<QSEditText> mTabSpecs = new ArrayList();
	private TabWidget mTabWidget;
    
	public interface OnTabChangeListener {
        void onTabChanged(int i);
    }
	public static abstract interface OnTabCloseListener
	{
		public abstract void onTabClose(int i, int i2, int i3);
	}
	
	static {
        autoNewTab = true;
    }
	
	//构造函数
	public TabHost(Context context) {
        super(context);
        /*this.mTabSpecs = new ArrayList();
        this.mCurrentTab = -1;
        this.mCurrentEditText = null;
        this.mLocalActivityManager = null;
        this.mOnTextChangedListener = null;
        this.mOnMenuClickListener = new TabWidget.OnMenuClickListener() {
            public void onMenuClick(int action, int index) {
                TabHost.autoNewTab = true;
                TabHost.this.iterCloseTab(action, index, TabHost.this.mTabWidget.getTabCount());
            }
        };*/
    }
	
	public TabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*this.mTabSpecs = new ArrayList();
        this.mCurrentTab = -1;
        this.mCurrentEditText = null;
        this.mLocalActivityManager = null;
        this.mOnTextChangedListener = null;
        this.mOnMenuClickListener = new TabWidget.OnMenuClickListener() {
            public void onMenuClick(int action, int index) {
                TabHost.autoNewTab = true;
                TabHost.this.iterCloseTab(action, index, TabHost.this.mTabWidget.getTabCount());
            }
        };*/
    }
	
	//引入表
	//QSEditor.onCreate()调用
	public void initTabHost(QSEditor parent) {
        setFocusableInTouchMode(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS/*262144*/);
        this.mJecEditor = parent;
        this.mTabContent = (LinearLayout) this.mJecEditor.findViewById(R.id.editorBodyLinearLayout);
        this.mCurrentTab = -1;
        this.mCurrentEditText = null;
        this.mTabWidget = (TabWidget) findViewById(R.id.tabWidgets);
        this.mTabWidget.setOnMenuClickListener(this.mOnMenuClickListener);
        this.mScroller = (HorizontalScrollView) findViewById(R.id.tabScroller);
        //新建标签
		/*((ImageButton) findViewById(R.id.add_new_tab_btn)).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TabHost.this.addTab("");
				}
			});*/
		ImageButton mNewTabButton = (ImageButton) this.findViewById(R.id.add_new_tab_btn);
        mNewTabButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					addTab("");
				}
			});
    }
	
	/**
     * Add a tab.
     * 
     * @param tabSpec
     *            Specifies how to create the indicator and content.
     */
	//新建表
	//QSEditor.onCreate()调用
	public void addTab(String path) {
        if (this.mTabSpecs.size() > 0) {
            int index = 0;
	        /*迭代器（Iterator）
			 迭代器是一种设计模式，它是一个对象，它可以遍历并选择序列中的对象，而开发人员不需要了解该序列的底层结构。迭代器通常被称为“轻量级”对象，因为创建它的代价小。
			 Java中的Iterator功能比较简单，并且只能单向移动：
			 (1) 使用方法iterator()要求容器返回一个Iterator。第一次调用Iterator的next()方法时，它返回序列的第一个元素。注意：iterator()方法是java.lang.Iterable接口,被Collection继承。
			 (2) 使用next()获得序列中的下一个元素。
			 (3) 使用hasNext()检查序列中是否还有元素。
			 (4) 使用remove()将迭代器新返回的元素删除。
			 Iterator是Java迭代器最简单的实现，为List设计的ListIterator具有更多的功能，它可以从两个方向遍历List，也可以从List中插入和删除元素。
			 迭代器应用：
			 list l = new ArrayList();
			 l.add("aa");
			 l.add("bb");
			 l.add("cc");
			 for (Iterator iter = l.iterator(); iter.hasNext();) {
			 String str = (String)iter.next();
			 System.out.println(str);
			 }
			 /*迭代器用于while循环
			 Iterator iter = l.iterator();
			 while(iter.hasNext()){
			 String str = (String) iter.next();
			 System.out.println(str);
			 }*/
            /*Iterator it = this.mTabSpecs.iterator();
            while (it.hasNext()) {
                QSEditText et = (QSEditText) it.next();
                if ("".equals(et.getPath()) && et.getText().length() == 0) {
                    setCurrentTab(index);
                    return;
                } else if ("".equals(path) || !path.equals(et.getPath())) {
                    index++;
                } else {
                    setCurrentTab(index);
                    return;
                }
            }*/
			for(QSEditText et : mTabSpecs)
            {
                if("".equals(et.getPath()) && et.getText().length() == 0)
                {//空白文档，没有内容时，则不打开新文件
                    setCurrentTab(index);
                    return;
                } else if(!"".equals(path) && path.equals(et.getPath())) {
                    //已经打开了
                    setCurrentTab(index);
                    return;
                }
                index++;
            }
        }
        this.mTabWidget.addView(createIndicatorView());
        this.mTabWidget.setTabSelectionListener(new TabWidget.OnTabSelectionChanged() {
				//选中标签时的事件
			    public void onTabSelectionChanged(int tabIndex) {
					/*if (tabIndex != TabHost.this.mCurrentTab) {
						TabHost.this.setCurrentTab(tabIndex);
						if (TabHost.this.mOnTabChangeListener != null) {
							TabHost.this.mOnTabChangeListener.onTabChanged(tabIndex);
						}
					} else if (TabHost.this.mOnTabCloseListener != null) {
						TabHost.this.mOnTabCloseListener.onTabClose(0, tabIndex, tabIndex);
					}*/
					if(tabIndex != mCurrentTab)
					{
						setCurrentTab(tabIndex);
						if(mOnTabChangeListener != null)
							mOnTabChangeListener.onTabChanged(tabIndex);
					} else {
						//关闭当前标签
						if(mOnTabCloseListener != null)
						{
							mOnTabCloseListener.onTabClose(TabWidget.MENU_ACTION_CLOSE_ONE, tabIndex, tabIndex);
						}
					}
				}
			});
        QSEditText jet = createEditText();
        setEditTextPref(jet);
        this.mTabSpecs.add(jet);
        this.mTabContent.addView(jet);
        setCurrentTab(this.mTabSpecs.size() - 1);
        setTitle(getResources().getString(R.string.new_filename));
    }
	
    
	//addTab调用
	public void setCurrentTab(int index) {
        if (index >= 0 && index < this.mTabSpecs.size() && index != this.mCurrentTab) {
            if (this.mCurrentTab != -1 && this.mCurrentTab < this.mTabSpecs.size()) {
                ((QSEditText) this.mTabSpecs.get(this.mCurrentTab)).hide();
            }
            this.mCurrentTab = index;
            this.mCurrentEditText = (QSEditText) this.mTabSpecs.get(index);
            this.mCurrentEditText.show();
            if (this.mOnTabChangeListener != null) {
                this.mOnTabChangeListener.onTabChanged(this.mCurrentTab);
            }
			//切换到最前
            scrollToSelected();
        }
    }
	
	private void scrollToSelected() {
        this.mScroller.post(new Runnable() {
				public void run() {
					if (TabHost.this.mCurrentTab >= 0) {
						View tab = TabHost.this.mTabWidget.getChildTabViewAt(TabHost.this.mCurrentTab);
						tab.getGlobalVisibleRect(new Rect());
						TabHost.this.mScroller.getGlobalVisibleRect(new Rect());
						int width = tab.getWidth();
						int left = ((TabHost.this.mCurrentTab * width) + (TabHost.this.mCurrentTab * ((LayoutParams) tab.getLayoutParams()).leftMargin)) - ((int) (((float) width) * 0.8f));
						int top = TabHost.this.mScroller.getHeight() + TabHost.this.mScroller.getPaddingTop();
						TabHost.this.mScroller.setSmoothScrollingEnabled(true);
						TabHost.this.mScroller.smoothScrollTo(left, top);
						TabHost.this.mTabWidget.focusCurrentTab(TabHost.this.mCurrentTab);
					}
				}
			});
    }
	
	private View createIndicatorView() {
        return ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.tab_indicators, this.mTabWidget, false);
    }
	//创建文本编框
	private QSEditText createEditText() {
        QSEditText mEditText = (QSEditText) ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.edit_text, this.mTabContent, false).findViewById(R.id.text_content);
        mEditText.setOnTextChangedListener(new QSEditText.OnTextChangedListener() {
				public void onTextChanged(QSEditText mEditText) {
					if (TabHost.this.mOnTextChangedListener != null) {
						TabHost.this.mOnTextChangedListener.onTextChanged(mEditText);
					}
					TabHost.this.setTabStatus(mEditText.isTextChanged());
				}
			});
        return mEditText;
    }
	
	private void setEditTextPref(QSEditText mEditText) {
        boolean z;
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this.mJecEditor);
        mEditText.setTypeface(Options.getFont(mPref.getString("font", "Monospace")));
        mEditText.setTextSize(Float.valueOf(mPref.getString("font_size", "12")).floatValue());
        if (mPref.getBoolean("wordwrap", true)) {
            z = false;
        } else {
            z = true;
        }
        mEditText.setHorizontallyScrolling(z);
        mEditText.setShowLineNum(mPref.getBoolean("show_line_num", true));
        mEditText.setShowWhitespace(mPref.getBoolean("show_tab", false));
        //创建上下文菜单
		this.mJecEditor.registerForContextMenu(mEditText);
        mEditText.setKeepScreenOn(mPref.getBoolean("keep_screen_on", false));
        mEditText.setAutoIndent(mPref.getBoolean("auto_indent", false));
        boolean disablespell = mPref.getBoolean("spellcheck", true);
        QSEditText.setDisableSpellCheck(disablespell);
        QSEditText.setUseSystemMenu(mPref.getBoolean("use_system_menu", false));
        if (disablespell) {
            mEditText.setInputType(mEditText.getInputType() | UndoParcel.MAX_SIZE);
        }
        ColorScheme.set(mPref);
        Highlight.loadColorScheme();
        mEditText.setBackgroundColor(Color.parseColor(ColorScheme.color_backgroup));
        mEditText.setTextColor(Color.parseColor(ColorScheme.color_font));
        mEditText.setAutoCapitalize(mPref.getBoolean("auto_capitalize", false));
        mEditText.init();
    }
	
	//addTab调用
	//设置标题
	public void setTitle(String title) {
        TextView mTitleView = getTabTitleView();
        if (mTitleView != null) {
            mTitleView.setText(title);
            ((QSEditText) this.mTabSpecs.get(this.mCurrentTab)).setTitle(title);
        }
    }
	
	private TextView getTabTitleView() {
        if (this.mCurrentTab < 0) {
            return null;
        }
        View mView = this.mTabWidget.getChildTabViewAt(this.mCurrentTab);
        if (mView != null) {
            return (TextView) mView.findViewById(R.id.title);
        }
        return null;
    }
	
	//QSEditor.onCreate()调用
	public QSEditText getCurrentEditText()
	{
		return this.mCurrentEditText;
	}
	
	
	
	public void setOnTabCloseListener(OnTabCloseListener l) {
        this.mOnTabCloseListener = l;
    }
	public int closeTab(int tabId) {
        this.mTabWidget.removeViewAt(tabId);
        this.mTabContent.removeView((View) this.mTabSpecs.get(tabId));
        this.mTabSpecs.remove(tabId);
        if (tabId != 0) {
            setCurrentTab(tabId - 1);
        } else if (this.mTabWidget.getTabCount() == 0) {
            this.mCurrentTab = -1;
            if (autoNewTab) {
                addTab("");
            }
        } else {
            this.mCurrentTab = -1;
            setCurrentTab(0);
        }
        return this.mCurrentTab;
    }
	
	
//QSEditor.onCreate()调用
	public void setOnTextChangedListener(OnTextChangedListener l)
	{
		this.mOnTextChangedListener = l;
	}
	
//QSEditor.onCreate()调用
	public void setOnTabChangedListener(OnTabChangeListener l) {
        this.mOnTabChangeListener = l;
    }
	
	public void setTabStatus(boolean ischanged) {
        TextView mTitleView = getTabTitleView();
        if (mTitleView != null) {
            CharSequence text = mTitleView.getText();
            if (ischanged) {
                SpannableString span = new SpannableString(text);
                span.setSpan(new ForegroundColorSpan(Color.RED/*-65536*/), 0, text.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE/*33*/);
                mTitleView.setText(span);
                return;
            }
            mTitleView.setText(text.toString());
        }
    }
	
	public void iterCloseTab(int action, int index, int count) {
        int start = action == 2 ? index + 1 : 0;
        /*do {
            count--;
            if (count >= start) {
                if (action != 1) {
                    break;
                }
            } else {
                return;
            }
        } while (count == index);
        if (this.mOnTabCloseListener != null) {
            this.mOnTabCloseListener.onTabClose(action, index, count);
        }*/
		while(--count >= start)
        {
            if(action == TabWidget.MENU_ACTION_CLOSE_OTHER && count==index)
                continue;
            if(mOnTabCloseListener != null)
            {
                mOnTabCloseListener.onTabClose(action, index, count);
            }
            break;
        }
    }
	
	public void setAutoNewTab(boolean newtab) {
        autoNewTab = newtab;
    }
	public ArrayList<String> getAllPath() {
        ArrayList<String> ret = new ArrayList();
        Iterator it = this.mTabSpecs.iterator();
        while (it.hasNext()) {
            ret.add(((QSEditText) it.next()).getPath());
        }
        return ret;
    }
	

	 public int getTabCount()
	 {
	 return this.mTabWidget.getTabCount();
	 }


	
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.TabHost
 * JD-Core Version:    0.6.0
 */
