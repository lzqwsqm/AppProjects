package com.qingshan.widget;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import com.qingshan.editor.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.MetaKeyKeyListener;
import android.text.method.MovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.Touch;
import android.text.style.ParagraphStyle;
import android.text.style.TabStopSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.View.BaseSavedState;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Toast;
import com.qingshan.colorschemes.ColorScheme;
import com.qingshan.editor.QSEditor;
import com.qingshan.editor.UndoParcel;
import com.qingshan.editor.UndoParcel.TextChange;
import com.qingshan.highlight.Highlight;
import com.qingshan.util.FileUtil;
import com.qingshan.util.TextUtil;
import com.qingshan.util.TimeUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
//import java.nio.channels.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import android.widget.TextView;
/*
*自定义
if(){}
else{}
whil(){}
for(){}

try { //执行的代码，其中可能有异常。一旦发现异常，则立即跳到catch执行。否则不会执行catch里面的内容 }
catch { //除非try里面执行代码发生了异常，否则这里的代码不会执行 }
finally { //不管什么情况都会执行，包括try catch 里面用了return ,可以理解为只要执行了try或者catch，就一定会执行 finally }


*/
public class QSEditText extends EditText
{
	static final Directions DIRS_ALL_LEFT_TO_RIGHT;
	static final Directions DIRS_ALL_RIGHT_TO_LEFT;
	public static final int DIR_LEFT_TO_RIGHT = 1;
	public static final int DIR_RIGHT_TO_LEFT = -1;
	private static final int ID_COPY = 16908321;
	private static final int ID_COPY_URL = 16908323;
	private static final int ID_CUT = 16908320;
	private static final int ID_PASTE = 16908322;
	private static final int ID_SELECT_ALL = 16908319;
	private static final int ID_START_SELECTING_TEXT = 16908328;
	private static final int ID_STOP_SELECTING_TEXT = 16908329;
	private static final int LAST_EDIT_DISTANCE_LIMIT = 20;
	private static final float MAX_TEXT_SIZE = 32.0F;
	private static final int META_SELECTING = 65536;
	private static final float MIN_TEXT_SIZE = 10.0F;
	private static final ParagraphStyle[] NO_PARA_SPANS;
	private static float TAB_INCREMENT = 0.0F;
	private static final String TAG = "QSEditText";
	private static final int TOUCH_DONE_MODE = 7;
	private static final int TOUCH_DRAG_START_MODE = 2;
	public static boolean TOUCH_ZOOM_ENABLED;
	private static boolean USE_SYSTEM_MENU;
	private static boolean mAutoCapitalize;
	private static boolean mDisableSpellCheck;
	private static boolean mHideSoftKeyboard;
	private static Rect sTempRect = new Rect();
	private String current_encoding;
	private String current_ext;
	private int current_linebreak;
	private String current_path;
	private String current_title;
	private boolean hasNewline;
	private int lastPaddingLeft;
	private boolean mAutoIndent;
	private CRC32 mCRC32;
	private String mDateFormat;
	private FastScroller mFastScroller;
	private FlingRunnable mFlingRunnable;
	private Highlight mHighlight;
	private ArrayList<Integer> mLastEditBuffer;
	private int mLastEditIndex;
	private Layout mLayout;
	private Path mLineBreakPath = new Path();
	private int mLineNumX;
	private int mLineNumber;
	private int mLineNumberLength;
	private Paint mLineNumberPaint;
	private int mLineNumberWidth;
	private HashMap<Integer, String> mLineStr;
	private boolean mNoWrapMode;
	private OnTextChangedListener mOnTextChangedListener;
	private UndoParcel mRedoParcel;
	private boolean mShowLineNum = true;
	private boolean mShowWhiteSpace = false;
	private boolean mSupportMultiTouch;
	private Path mTabPath = new Path();
	private Editable mText;
	private TextPaint mTextPaint;
	private float mTextSize;
	private int mTouchMode;
	private UndoParcel mUndoParcel;
	private boolean mUndoRedo;
	private TextWatcher mUndoWatcher;
	
	private VelocityTracker mVelocityTracker;
	private Paint mWhiteSpacePaint;
	private Path[] mWhiteSpacePaths;
	private TextPaint mWorkPaint;
	private float oldDist;
	private int paddingLeft;
	private int realLineNum;
	private float scale;
	private long src_text_crc32;
	private int src_text_length;
	
	static {
        TAB_INCREMENT = 20.0f;
        sTempRect = new Rect();
        TOUCH_ZOOM_ENABLED = true;
        mAutoCapitalize = false;
        mDisableSpellCheck = true;
        USE_SYSTEM_MENU = false;
        NO_PARA_SPANS = new ParagraphStyle[0];
        short[] sArr = new short[DIR_LEFT_TO_RIGHT];
        sArr[0] = Short.MAX_VALUE;
        DIRS_ALL_LEFT_TO_RIGHT = new Directions(sArr);
        sArr = new short[TOUCH_DRAG_START_MODE];
        sArr[DIR_LEFT_TO_RIGHT] = Short.MAX_VALUE;
        DIRS_ALL_RIGHT_TO_LEFT = new Directions(sArr);
    }
	
	public QSEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.mShowWhiteSpace = false;
        //this.mShowLineNum = true;
        //this.mLineBreakPath = new Path();
       // this.mTabPath = new Path();
        Path[] pathArr = new Path[TOUCH_DRAG_START_MODE];
        pathArr[0] = this.mTabPath;
        pathArr[DIR_LEFT_TO_RIGHT] = this.mLineBreakPath;
        this.mWhiteSpacePaths = pathArr;
        this.paddingLeft = 0;
        this.lastPaddingLeft = 0;
        this.realLineNum = 0;
        this.hasNewline = true;
        this.mText = null;
        this.mUndoParcel = new UndoParcel();
        this.mRedoParcel = new UndoParcel();
        this.mUndoRedo = false;
        this.mAutoIndent = false;
        this.mLineStr = new HashMap();
        this.mLineNumber = 0;
        this.mLineNumberWidth = 0;
        this.mLineNumberLength = 0;
        this.mLastEditBuffer = new ArrayList();
        this.mLastEditIndex = DIR_RIGHT_TO_LEFT;
        this.current_encoding = "UTF-8";
        this.current_path = "";
        this.current_ext = "";
        this.current_title = "";
        this.current_linebreak = 0;
        this.mNoWrapMode = false;
        this.mLineNumX = 0;
        this.mDateFormat = "0";
        this.mTouchMode = TOUCH_DONE_MODE;
        this.scale = 0.5f;
        this.mOnTextChangedListener = null;
        this.mUndoWatcher = new TextWatcher() {
           TextChange lastChange;

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!QSEditor.isLoading) {
                   // QSEditText.this.mHighlight.redraw();
                    if (this.lastChange != null) {
                        if (count < UndoParcel.MAX_SIZE) {
                            this.lastChange.newtext = s.subSequence(start, start + count);
                            if (start == this.lastChange.start && ((this.lastChange.oldtext.length() > 0 || this.lastChange.newtext.length() > 0) && !QSEditText.this.equalsCharSequence(this.lastChange.newtext, this.lastChange.oldtext))) {
                                QSEditText.this.mUndoParcel.push(this.lastChange);
                                QSEditText.this.mRedoParcel.removeAll();
                            }
                            QSEditText.this.setUndoRedoButtonStatus();
                        } else {
                            QSEditText.this.mUndoParcel.removeAll();
                            QSEditText.this.mRedoParcel.removeAll();
                        }
                        this.lastChange = null;
                    }
                    int bufSize = QSEditText.this.mLastEditBuffer.size();
                    int lastLoc = 0;
                    if (bufSize != 0) {
                        lastLoc = ((Integer) QSEditText.this.mLastEditBuffer.get(bufSize + QSEditText.DIR_RIGHT_TO_LEFT)).intValue();
                    }
                    if (Math.abs(start - lastLoc) > QSEditText.LAST_EDIT_DISTANCE_LIMIT) {
                        QSEditText.this.mLastEditBuffer.add(Integer.valueOf(start));
                        QSEditText.this.mLastEditIndex = QSEditText.this.mLastEditBuffer.size() + QSEditText.DIR_RIGHT_TO_LEFT;
                        if (QSEditText.this.mOnTextChangedListener != null) {
                            QSEditText.this.mOnTextChangedListener.onTextChanged(QSEditText.this);
                        }
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!QSEditor.isLoading) {
                    if (QSEditText.this.mUndoRedo) {
                        QSEditText.this.mUndoRedo = false;
                    } else if (count < UndoParcel.MAX_SIZE) {
                        this.lastChange = new UndoParcel.TextChange();
                        this.lastChange.start = start;
                        this.lastChange.oldtext = s.subSequence(start, start + count);
                    } else {
                        QSEditText.this.mUndoParcel.removeAll();
                        QSEditText.this.mRedoParcel.removeAll();
                        this.lastChange = null;
                    }
                }
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }
	
	
	
	protected void onDraw(Canvas canvas) {
        this.mLayout = getLayout();
        this.mText = getText();
        super.onDraw(canvas);
        drawView(canvas);
        if (this.mFastScroller != null) {
            this.mFastScroller.draw(canvas);
        }
    }
	
	public void drawView(Canvas c) {
		//锁
        synchronized (sTempRect) {
            if (c.getClipBounds(sTempRect)) {
                int dtop = sTempRect.top;
                int dbottom = sTempRect.bottom;
                if (this.mLayout != null) {
                    int textLength = this.mText.length();
                    int top = 0;
                    int lineCount = this.mLayout.getLineCount();
                    int bottom = this.mLayout.getLineTop(lineCount);
                    if (dtop > 0) {
                        top = dtop;
                    }
                    if (dbottom < bottom) {
                        bottom = dbottom;
                    }
                    int first = this.mLayout.getLineForVertical(top);
                    int last = this.mLayout.getLineForVertical(bottom);
                    int previousLineBottom = this.mLayout.getLineTop(first);
                    int previousLineEnd = this.mLayout.getLineStart(first);
                    TextPaint paint = this.mTextPaint;
                    ParagraphStyle[] spans = NO_PARA_SPANS;
                    //高亮着色
					this.mHighlight.render(this.mText, this.mLayout.getLineStart(first >= 3 ? first - 3 : 0), this.mLayout.getLineStart(last + 3 > lineCount ? lineCount : last + 3));
                    
					if (this.mShowLineNum || this.mShowWhiteSpace) {
                        int lastline;
                        if (lineCount < DIR_LEFT_TO_RIGHT) {
                            lastline = DIR_LEFT_TO_RIGHT;
                        } else {
                            lastline = lineCount;
                        }
                        if (lastline != this.mLineNumber) {
                            setLineNumberWidth(lastline);
                        }
                        if (this.mNoWrapMode) {
                            this.mLineNumX = this.mLineNumberWidth + getScrollX();
                        } else {
                            this.mLineNumX = this.mLineNumberWidth;
                        }
                        int right = getWidth();
                        int left = getPaddingLeft();
                        if (previousLineEnd <= DIR_LEFT_TO_RIGHT) {
                            this.realLineNum = DIR_LEFT_TO_RIGHT;
                        } else if (previousLineEnd < this.mText.length()) {
                            this.realLineNum = TextUtil.countMatches(this.mText, '\n', 0, previousLineEnd);
                            if (this.mText.charAt(previousLineEnd) != '\n') {
                                this.realLineNum += DIR_LEFT_TO_RIGHT;
                            }
                        } else {
                            return;
                        }
                        this.hasNewline = true;
                        if (last == 0) {
                            c.drawLine((float) this.mLineNumX, (float) top, (float) this.mLineNumX, this.mTextPaint.getTextSize(), this.mLineNumberPaint);
                            if (this.hasNewline) {
                                String lineString = (String) this.mLineStr.get(Integer.valueOf(this.realLineNum));
                                if (lineString == null) {
                                    lineString = "      " + this.realLineNum;
                                    this.mLineStr.put(Integer.valueOf(this.realLineNum), lineString);
                                }
                                c.drawText(lineString, lineString.length() - this.mLineNumberLength, lineString.length(), (float) (this.mLineNumX - this.mLineNumberWidth), this.mTextPaint.getTextSize(), this.mLineNumberPaint);
                                return;
                            }
                            return;
                        }
                        for (int i = first; i <= last; i += DIR_LEFT_TO_RIGHT) {
                            int x;
                            boolean z;
                            int start = previousLineEnd;
                            previousLineEnd = this.mLayout.getLineStart(i + DIR_LEFT_TO_RIGHT);
                            int end = getLineVisibleEnd(i, start, previousLineEnd);
                            int ltop = previousLineBottom;
                            int lbottom = this.mLayout.getLineTop(i + DIR_LEFT_TO_RIGHT);
                            previousLineBottom = lbottom;
                            int lbaseline = lbottom;
                            int dir = this.mLayout.getParagraphDirection(i);
                            if (dir == DIR_LEFT_TO_RIGHT) {
                                x = left;
                            } else {
                                x = right;
                            }
                            Directions directions = DIRS_ALL_LEFT_TO_RIGHT;
                            boolean hasTab = this.mLayout.getLineContainsTab(i);
                            float f = (float) x;
                            TextPaint textPaint = this.mWorkPaint;
                            if (i + DIR_LEFT_TO_RIGHT == last) {
                                z = true;
                            } else {
                                z = false;
                            }
                            drawText(c, start, end, dir, directions, f, ltop, lbaseline, lbottom, paint, textPaint, hasTab, spans, textLength, z);
                        }
                        return;
                    }
                    return;
                }
                return;
            }
        }
    }
	
	/*
	//声明方法
	.method public drawView(Landroid/graphics/Canvas;)V
    //指定局部变量的总个数
	.locals 36
    //指定方法参的数(p命名方法)
	.param p1, "c"    # Landroid/graphics/Canvas;
     //代码开始
    .prologue
	//指定该处指令在源代码中的行号
    .line 881
	//读取静态字段的值为对象类型(v命名方法)
    sget-object v5, Lcom/jecelyin/widget/JecEditText;->sTempRect:Landroid/graphics/Rect;
    //指定对象获取锁
    monitor-enter v5

    .line 883
	//try开始
    :try_start_0
    sget-object v3, Lcom/jecelyin/widget/JecEditText;->sTempRect:Landroid/graphics/Rect;
    //将p1赋值给v0
    move-object/from16 v0, p1
    //调用实例虚拟方法
    invoke-virtual {v0, v3}, Landroid/graphics/Canvas;->getClipBounds(Landroid/graphics/Rect;)Z
    //将上一个invoke类型操作的单字非对象结果赋给v3
    move-result v3
    //如果不等于v3跳到cond_1
    if-nez v3, :cond_1

    .line 885
	//释放指定的对象的锁
    monitor-exit v5

    .line 1020
    :cond_0
    :goto_0
	//函数从一个void方法返回
    return-void

    .line 888
    :cond_1
    sget-object v3, Lcom/jecelyin/widget/JecEditText;->sTempRect:Landroid/graphics/Rect;

    iget v0, v3, Landroid/graphics/Rect;->top:I

    move/from16 v23, v0

    .line 889
	//声明的变量作用域在.local与.endlocal之间
    .local v23, "dtop":I
    sget-object v3, Lcom/jecelyin/widget/JecEditText;->sTempRect:Landroid/graphics/Rect;

    iget v0, v3, Landroid/graphics/Rect;->bottom:I

    move/from16 v22, v0

    .line 881
    .local v22, "dbottom":I
    monitor-exit v5
	//try结束
    :try_end_0
	//指定处理到的异常类型与catch标号
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    .line 891
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    if-eqz v3, :cond_0

    .line 894
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mText:Landroid/text/Editable;

    invoke-interface {v3}, Landroid/text/Editable;->length()I

    move-result v19

    .line 896
    .local v19, "textLength":I
    const/16 v34, 0x0

    .line 897
    .local v34, "top":I
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    invoke-virtual {v3}, Landroid/text/Layout;->getLineCount()I

    move-result v29

    .line 898
    .local v29, "lineCount":I
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    move/from16 v0, v29

    invoke-virtual {v3, v0}, Landroid/text/Layout;->getLineTop(I)I

    move-result v21

    .line 900
    .local v21, "bottom":I
    move/from16 v0, v23

    move/from16 v1, v34

    if-le v0, v1, :cond_2

    .line 902
    move/from16 v34, v23

    .line 904
    :cond_2
    move/from16 v0, v22

    move/from16 v1, v21

    if-ge v0, v1, :cond_3

    .line 906
    move/from16 v21, v22

    .line 909
    :cond_3
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    move/from16 v0, v34

    invoke-virtual {v3, v0}, Landroid/text/Layout;->getLineForVertical(I)I

    move-result v24

    .line 910
    .local v24, "first":I
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    move/from16 v0, v21

    invoke-virtual {v3, v0}, Landroid/text/Layout;->getLineForVertical(I)I

    move-result v26

    .line 912
    .local v26, "last":I
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    move/from16 v0, v24

    invoke-virtual {v3, v0}, Landroid/text/Layout;->getLineTop(I)I

    move-result v30

    .line 913
    .local v30, "previousLineBottom":I
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    move/from16 v0, v24

    invoke-virtual {v3, v0}, Landroid/text/Layout;->getLineStart(I)I

    move-result v31

    .line 915
    .local v31, "previousLineEnd":I
    move-object/from16 v0, p0

    iget-object v15, v0, Lcom/jecelyin/widget/JecEditText;->mTextPaint:Landroid/text/TextPaint;

    .line 917
    .local v15, "paint":Landroid/text/TextPaint;
    sget-object v18, Lcom/jecelyin/widget/JecEditText;->NO_PARA_SPANS:[Landroid/text/style/ParagraphStyle;

    .line 921
    .local v18, "spans":[Landroid/text/style/ParagraphStyle;
    move-object/from16 v0, p0

    iget-object v5, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    const/4 v3, 0x3

    move/from16 v0, v24

    if-lt v0, v3, :cond_8

    add-int/lit8 v3, v24, -0x3

    :goto_1
    invoke-virtual {v5, v3}, Landroid/text/Layout;->getLineStart(I)I

    move-result v32

    .line 922
    .local v32, "previousLineEnd2":I
    move-object/from16 v0, p0

    iget-object v5, v0, Lcom/jecelyin/widget/JecEditText;->mHighlight:Lcom/jecelyin/highlight/Highlight;

    move-object/from16 v0, p0

    iget-object v6, v0, Lcom/jecelyin/widget/JecEditText;->mText:Landroid/text/Editable;

    move-object/from16 v0, p0

    iget-object v11, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    add-int/lit8 v3, v26, 0x3

    move/from16 v0, v29

    if-le v3, v0, :cond_9

    move/from16 v3, v29

    :goto_2
    invoke-virtual {v11, v3}, Landroid/text/Layout;->getLineStart(I)I

    move-result v3

    move/from16 v0, v32

    invoke-virtual {v5, v6, v0, v3}, Lcom/jecelyin/highlight/Highlight;->render(Landroid/text/Editable;II)Z

    .line 924
    move-object/from16 v0, p0

    iget-boolean v3, v0, Lcom/jecelyin/widget/JecEditText;->mShowLineNum:Z

    if-nez v3, :cond_4

    move-object/from16 v0, p0

    iget-boolean v3, v0, Lcom/jecelyin/widget/JecEditText;->mShowWhiteSpace:Z

    if-eqz v3, :cond_0

    .line 930
    :cond_4
    const/4 v3, 0x1

    move/from16 v0, v29

    if-ge v0, v3, :cond_a

    const/16 v27, 0x1

    .line 931
    .local v27, "lastline":I
    :goto_3
    move-object/from16 v0, p0

    iget v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumber:I

    move/from16 v0, v27

    if-eq v0, v3, :cond_5

    .line 933
    move-object/from16 v0, p0

    move/from16 v1, v27

    invoke-direct {v0, v1}, Lcom/jecelyin/widget/JecEditText;->setLineNumberWidth(I)V

    .line 936
    :cond_5
    move-object/from16 v0, p0

    iget-boolean v3, v0, Lcom/jecelyin/widget/JecEditText;->mNoWrapMode:Z

    if-eqz v3, :cond_b

    .line 938
    move-object/from16 v0, p0

    iget v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumberWidth:I

    invoke-virtual/range {p0 .. p0}, Lcom/jecelyin/widget/JecEditText;->getScrollX()I

    move-result v5

    add-int/2addr v3, v5

    move-object/from16 v0, p0

    iput v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumX:I

    .line 944
    :goto_4
    invoke-virtual/range {p0 .. p0}, Lcom/jecelyin/widget/JecEditText;->getWidth()I

    move-result v33

    .line 945
    .local v33, "right":I
    invoke-virtual/range {p0 .. p0}, Lcom/jecelyin/widget/JecEditText;->getPaddingLeft()I

    move-result v28

    .line 947
    .local v28, "left":I
    const/4 v3, 0x1

    move/from16 v0, v31

    if-le v0, v3, :cond_c

    .line 949
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mText:Landroid/text/Editable;

    invoke-interface {v3}, Landroid/text/Editable;->length()I

    move-result v3

    move/from16 v0, v31

    if-ge v0, v3, :cond_0

    .line 951
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mText:Landroid/text/Editable;

    const/16 v5, 0xa

    const/4 v6, 0x0

    move/from16 v0, v31

    invoke-static {v3, v5, v6, v0}, Lcom/jecelyin/util/TextUtil;->countMatches(Ljava/lang/CharSequence;CII)I

    move-result v3

    move-object/from16 v0, p0

    iput v3, v0, Lcom/jecelyin/widget/JecEditText;->realLineNum:I

    .line 955
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mText:Landroid/text/Editable;

    move/from16 v0, v31

    invoke-interface {v3, v0}, Landroid/text/Editable;->charAt(I)C

    move-result v3

    const/16 v5, 0xa

    if-eq v3, v5, :cond_6

    .line 957
    move-object/from16 v0, p0

    iget v3, v0, Lcom/jecelyin/widget/JecEditText;->realLineNum:I

    add-int/lit8 v3, v3, 0x1

    move-object/from16 v0, p0

    iput v3, v0, Lcom/jecelyin/widget/JecEditText;->realLineNum:I

    .line 964
    :cond_6
    :goto_5
    const/4 v3, 0x1

    move-object/from16 v0, p0

    iput-boolean v3, v0, Lcom/jecelyin/widget/JecEditText;->hasNewline:Z

    .line 967
    if-nez v26, :cond_d

    .line 969
    move-object/from16 v0, p0

    iget v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumX:I

    int-to-float v4, v3

    move/from16 v0, v34

    int-to-float v5, v0

    move-object/from16 v0, p0

    iget v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumX:I

    int-to-float v6, v3

    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mTextPaint:Landroid/text/TextPaint;

    invoke-virtual {v3}, Landroid/text/TextPaint;->getTextSize()F

    move-result v7

    move-object/from16 v0, p0

    iget-object v8, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumberPaint:Landroid/graphics/Paint;

    move-object/from16 v3, p1

    invoke-virtual/range {v3 .. v8}, Landroid/graphics/Canvas;->drawLine(FFFFLandroid/graphics/Paint;)V

    .line 970
    move-object/from16 v0, p0

    iget-boolean v3, v0, Lcom/jecelyin/widget/JecEditText;->hasNewline:Z

    if-eqz v3, :cond_0

    .line 972
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineStr:Ljava/util/HashMap;

    move-object/from16 v0, p0

    iget v5, v0, Lcom/jecelyin/widget/JecEditText;->realLineNum:I

    invoke-static {v5}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    invoke-virtual {v3, v5}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/String;

    .line 973
    .local v4, "lineString":Ljava/lang/String;
    if-nez v4, :cond_7

    .line 975
    new-instance v3, Ljava/lang/StringBuilder;

    const-string v5, "      "

    invoke-direct {v3, v5}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    move-object/from16 v0, p0

    iget v5, v0, Lcom/jecelyin/widget/JecEditText;->realLineNum:I

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    .line 976
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineStr:Ljava/util/HashMap;

    move-object/from16 v0, p0

    iget v5, v0, Lcom/jecelyin/widget/JecEditText;->realLineNum:I

    invoke-static {v5}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    invoke-virtual {v3, v5, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    .line 978
    :cond_7
    invoke-virtual {v4}, Ljava/lang/String;->length()I

    move-result v3

    move-object/from16 v0, p0

    iget v5, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumberLength:I

    sub-int v5, v3, v5

    invoke-virtual {v4}, Ljava/lang/String;->length()I

    move-result v6

    move-object/from16 v0, p0

    iget v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumX:I

    move-object/from16 v0, p0

    iget v11, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumberWidth:I

    sub-int/2addr v3, v11

    int-to-float v7, v3

    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mTextPaint:Landroid/text/TextPaint;

    invoke-virtual {v3}, Landroid/text/TextPaint;->getTextSize()F

    move-result v8

    move-object/from16 v0, p0

    iget-object v9, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumberPaint:Landroid/graphics/Paint;

    move-object/from16 v3, p1

    invoke-virtual/range {v3 .. v9}, Landroid/graphics/Canvas;->drawText(Ljava/lang/String;IIFFLandroid/graphics/Paint;)V

    goto/16 :goto_0

    .line 881
    .end local v4    # "lineString":Ljava/lang/String;
    .end local v15    # "paint":Landroid/text/TextPaint;
    .end local v18    # "spans":[Landroid/text/style/ParagraphStyle;
    .end local v19    # "textLength":I
    .end local v21    # "bottom":I
    .end local v22    # "dbottom":I
    .end local v23    # "dtop":I
    .end local v24    # "first":I
    .end local v26    # "last":I
    .end local v27    # "lastline":I
    .end local v28    # "left":I
    .end local v29    # "lineCount":I
    .end local v30    # "previousLineBottom":I
    .end local v31    # "previousLineEnd":I
    .end local v32    # "previousLineEnd2":I
    .end local v33    # "right":I
    .end local v34    # "top":I
    :catchall_0
    move-exception v3

    :try_start_1
    monitor-exit v5
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    throw v3

    .line 921
    .restart local v15    # "paint":Landroid/text/TextPaint;
    .restart local v18    # "spans":[Landroid/text/style/ParagraphStyle;
    .restart local v19    # "textLength":I
    .restart local v21    # "bottom":I
    .restart local v22    # "dbottom":I
    .restart local v23    # "dtop":I
    .restart local v24    # "first":I
    .restart local v26    # "last":I
    .restart local v29    # "lineCount":I
    .restart local v30    # "previousLineBottom":I
    .restart local v31    # "previousLineEnd":I
    .restart local v34    # "top":I
    :cond_8
    const/4 v3, 0x0

    goto/16 :goto_1

    .line 922
    .restart local v32    # "previousLineEnd2":I
    :cond_9
    add-int/lit8 v3, v26, 0x3

    goto/16 :goto_2

    :cond_a
    move/from16 v27, v29

    .line 930
    goto/16 :goto_3

    .line 941
    .restart local v27    # "lastline":I
    :cond_b
    move-object/from16 v0, p0

    iget v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumberWidth:I

    move-object/from16 v0, p0

    iput v3, v0, Lcom/jecelyin/widget/JecEditText;->mLineNumX:I

    goto/16 :goto_4

    .line 961
    .restart local v28    # "left":I
    .restart local v33    # "right":I
    :cond_c
    const/4 v3, 0x1

    move-object/from16 v0, p0

    iput v3, v0, Lcom/jecelyin/widget/JecEditText;->realLineNum:I

    goto/16 :goto_5

    .line 986
    :cond_d
    move/from16 v25, v24

    .local v25, "i":I
    :goto_6
    move/from16 v0, v25

    move/from16 v1, v26

    if-gt v0, v1, :cond_0

    .line 988
    move/from16 v7, v31

    .line 990
    .local v7, "start":I
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    add-int/lit8 v5, v25, 0x1

    invoke-virtual {v3, v5}, Landroid/text/Layout;->getLineStart(I)I

    move-result v31

    .line 991
    move-object/from16 v0, p0

    move/from16 v1, v25

    move/from16 v2, v31

    invoke-direct {v0, v1, v7, v2}, Lcom/jecelyin/widget/JecEditText;->getLineVisibleEnd(III)I

    move-result v8

    .line 993
    .local v8, "end":I
    move/from16 v12, v30

    .line 994
    .local v12, "ltop":I
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    add-int/lit8 v5, v25, 0x1

    invoke-virtual {v3, v5}, Landroid/text/Layout;->getLineTop(I)I

    move-result v14

    .line 995
    .local v14, "lbottom":I
    move/from16 v30, v14

    .line 996
    move v13, v14

    .line 998
    .local v13, "lbaseline":I
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    move/from16 v0, v25

    invoke-virtual {v3, v0}, Landroid/text/Layout;->getParagraphDirection(I)I

    move-result v9

    .line 1003
    .local v9, "dir":I
    const/4 v3, 0x1

    if-ne v9, v3, :cond_e

    .line 1005
    move/from16 v35, v28

    .line 1013
    .local v35, "x":I
    :goto_7
    sget-object v10, Lcom/jecelyin/widget/JecEditText;->DIRS_ALL_LEFT_TO_RIGHT:Lcom/jecelyin/widget/JecEditText$Directions;

    .line 1016
    .local v10, "directions":Lcom/jecelyin/widget/JecEditText$Directions;
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/jecelyin/widget/JecEditText;->mLayout:Landroid/text/Layout;

    move/from16 v0, v25

    invoke-virtual {v3, v0}, Landroid/text/Layout;->getLineContainsTab(I)Z

    move-result v17

    .line 1017
    .local v17, "hasTab":Z
    move/from16 v0, v35

    int-to-float v11, v0

    move-object/from16 v0, p0

    iget-object v0, v0, Lcom/jecelyin/widget/JecEditText;->mWorkPaint:Landroid/text/TextPaint;

    move-object/from16 v16, v0

    add-int/lit8 v3, v25, 0x1

    move/from16 v0, v26

    if-ne v3, v0, :cond_f

    const/16 v20, 0x1

    :goto_8
    move-object/from16 v5, p0

    move-object/from16 v6, p1

    invoke-direct/range {v5 .. v20}, Lcom/jecelyin/widget/JecEditText;->drawText(Landroid/graphics/Canvas;IIILcom/jecelyin/widget/JecEditText$Directions;FIIILandroid/text/TextPaint;Landroid/text/TextPaint;Z[Ljava/lang/Object;IZ)V

    .line 986
    add-int/lit8 v25, v25, 0x1

    goto :goto_6

    .line 1008
    .end local v10    # "directions":Lcom/jecelyin/widget/JecEditText$Directions;
    .end local v17    # "hasTab":Z
    .end local v35    # "x":I
    :cond_e
    move/from16 v35, v33

    .restart local v35    # "x":I
    goto :goto_7

    .line 1017
    .restart local v10    # "directions":Lcom/jecelyin/widget/JecEditText$Directions;
    .restart local v17    # "hasTab":Z
    :cond_f
    const/16 v20, 0x0

    goto :goto_8
	
.end method*/
	
	private void drawText(Canvas canvas, int start, int end, int dir, Directions directions, float x, int top, int y, int bottom, TextPaint paint, TextPaint workPaint, boolean hasTabs, Object[] parspans, int textLength, boolean islastline) {
        if (this.mShowLineNum) {
            int i;
            float f = (float) this.mLineNumX;
            float f2 = (float) top;
            float f3 = (float) this.mLineNumX;
            if (islastline) {
                i = (bottom - top) + bottom;
            } else {
                i = bottom;
            }
            canvas.drawLine(f, f2, f3, (float) i, this.mLineNumberPaint);
            if (this.hasNewline) {
                String lineString = (String) this.mLineStr.get(Integer.valueOf(this.realLineNum));
                if (lineString == null) {
                    lineString = "      " + this.realLineNum;
                    this.mLineStr.put(Integer.valueOf(this.realLineNum), lineString);
                }
                canvas.drawText(lineString, lineString.length() - this.mLineNumberLength, lineString.length(), (float) ((this.mLineNumX - this.mLineNumberWidth) + DIR_LEFT_TO_RIGHT), (float) (y - 2), this.mLineNumberPaint);
                this.realLineNum += DIR_LEFT_TO_RIGHT;
                this.hasNewline = false;
            }
        }
        float h = 0.0f;
        int here = 0;
        for (int i2 = 0; i2 < directions.mDirections.length; i2 += DIR_LEFT_TO_RIGHT) {
            int there = here + directions.mDirections[i2];
            if (there > end - start) {
                there = end - start;
            }
            int segstart = here;
            int j = hasTabs ? here : there;
            while (j <= there && start + j <= end) {
                char at = start + j == end ? '\u0000' : this.mText.charAt(start + j);
                if (j == there || at == '\t') {
                    boolean z;
                    CharSequence charSequence = this.mText;
                    int i3 = start + segstart;
                    int i4 = start + j;
                    boolean z2 = (i2 & DIR_LEFT_TO_RIGHT) != 0;
                    float f4 = x + h;
                    if (start + j == end || hasTabs) {
                        z = true;
                    } else {
                        z = false;
                    }
                    h += Styled.drawText(null, charSequence, i3, i4, dir, z2, f4, top, y, bottom, paint, workPaint, z);
                    if (j != there && at == '\t' && this.mShowWhiteSpace) {
                        if (x + h > ((float) this.mLineNumX)) {
                            canvas.translate(x + h, (float) y);
                            canvas.drawPath(this.mWhiteSpacePaths[0], this.mWhiteSpacePaint);
                            canvas.translate((-x) - h, (float) (-y));
                        }
                        h = ((float) dir) * nextTabPos(this.mText, start, end, ((float) dir) * h, parspans);
                    } else if (j == there && end < textLength && this.mText.charAt(end) == '\n') {
                        if (this.mShowWhiteSpace && x + h > ((float) this.mLineNumX)) {
                            canvas.translate(x + h, (float) y);
                            canvas.drawPath(this.mWhiteSpacePaths[DIR_LEFT_TO_RIGHT], this.mWhiteSpacePaint);
                            canvas.translate((-x) - h, (float) (-y));
                        }
                        this.hasNewline = true;
                    }
                    segstart = j + DIR_LEFT_TO_RIGHT;
                }
                j += DIR_LEFT_TO_RIGHT;
            }
            here = there;
        }
    }
	
    
	//TabHost.addTab调用
	public String getPath()
	{
		return this.current_path;
	}
	//TabHost.setCurrentTab调用
	public void hide()
	{
		setVisibility(8);
	}
	//TabHost.setCurrentTab调用
	public void show()
	{
		setVisibility(0);
		if (this.mOnTextChangedListener != null)
			this.mOnTextChangedListener.onTextChanged(this);
	}
	//TabHost.setTitle调用
	public void setTitle(String title) {
        this.current_title = title;
    }
	//TabHost.setEditTextPref调用
	public void setShowLineNum(boolean b) {
        int left;
        this.mShowLineNum = b;
        if (this.mShowLineNum) {
            left = this.paddingLeft + this.mLineNumberWidth;
        } else {
            left = this.paddingLeft;
        }
        setPaddingLeft(left);
    }
	//setShowLineNum调用
	public void setPaddingLeft(int padding) {
        if (this.lastPaddingLeft != padding) {
            if (padding < this.paddingLeft) {
                padding = this.paddingLeft;
            }
            this.lastPaddingLeft = padding;
            setPadding(padding, 0, getPaddingRight(), getPaddingBottom());
        }
    }
	//TabHost.setEditTextPref调用
	public void setShowWhitespace(boolean b)
	{
		this.mShowWhiteSpace = b;
	}
	//TabHost.setEditTextPref调用
	public void setAutoIndent(boolean paramBoolean)
	{
		this.mAutoIndent = paramBoolean;
	}
	//TabHost.setEditTextPref调用
	public static void setDisableSpellCheck(boolean paramBoolean)
	{
		mDisableSpellCheck = paramBoolean;
	}
	//TabHost.setEditTextPref调用
	public static void setUseSystemMenu(boolean paramBoolean)
	{
		USE_SYSTEM_MENU = paramBoolean;
	}
	//TabHost.setEditTextPref调用
	public void setAutoCapitalize(boolean paramBoolean)
	{
		mAutoCapitalize = paramBoolean;
		if (mAutoCapitalize)
			setInputType(0x4000 | getInputType());
	}
	//TabHost.setEditTextPref调用
	public void init()
	{
		this.mCRC32 = new CRC32();
		this.mHighlight = new Highlight();
		this.mWorkPaint = new TextPaint();
		this.mTextPaint = getPaint();
		this.mLineNumberPaint = new Paint(1);
		this.mWhiteSpacePaint = new Paint(1);
		setImeOptions(268435462);
		this.paddingLeft = getPaddingLeft();
		this.mFastScroller = new FastScroller(getContext(), this);
		//即给EditText增加监听器
		addTextChangedListener(this.mUndoWatcher);
		clearFocus();
		this.mTextSize = this.mTextPaint.getTextSize();
		this.mLineNumberPaint.setTextSize(this.mTextSize - 2.0F);
		this.mLineNumberPaint.setTypeface(Typeface.MONOSPACE);
		this.mLineNumberPaint.setStrokeWidth(1.0F);
		this.mLineNumberPaint.setColor(Color.parseColor(ColorScheme.color_font));
		this.mWhiteSpacePaint.setStrokeWidth(0.75F);
		this.mWhiteSpacePaint.setStyle(Paint.Style.STROKE);
		this.mWhiteSpacePaint.setColor(-7829368);
		this.mLineBreakPath.reset();
		float f2 = this.mTextPaint.measureText("L");
		float f1 = this.mTextPaint.descent() - this.mTextPaint.ascent();
		this.mLineBreakPath.moveTo(f2 * 0.6F, 0.0F);
		this.mLineBreakPath.lineTo(f2 * 0.6F, 0.7F * -f1);
		this.mLineBreakPath.moveTo(f2 * 0.6F, 0.0F);
		this.mLineBreakPath.lineTo(f2 * 0.25F, 0.3F * -f1);
		this.mLineBreakPath.moveTo(f2 * 0.6F, 0.0F);
		this.mLineBreakPath.lineTo(0.95F * f2, 0.3F * -f1);
		this.mTabPath.reset();
		f2 = this.mTextPaint.measureText("\t\t");
		f1 = this.mTextPaint.descent() - this.mTextPaint.ascent();
		this.mTabPath.moveTo(0.0F, 0.5F * -f1);
		this.mTabPath.lineTo(0.1F * f2, 0.35F * -f1);
		this.mTabPath.lineTo(0.0F, 0.2F * -f1);
		this.mTabPath.moveTo(f2 * 0.15F, 0.5F * -f1);
		this.mTabPath.lineTo(f2 * 0.25F, 0.35F * -f1);
		this.mTabPath.lineTo(f2 * 0.15F, 0.2F * -f1);
		this.mSupportMultiTouch = getContext().getPackageManager().hasSystemFeature("android.hardware.touchscreen.multitouch");
	
	}
	//QSEditor.saveConfirm调用
	public boolean isTextChanged() {
        if (this.src_text_length != getText().length()) {
            return true;
        }
        this.mCRC32.reset();
        byte[] bytes = getString().getBytes();
        this.mCRC32.update(bytes, 0, bytes.length);
        if (this.src_text_crc32 == this.mCRC32.getValue()) {
            return false;
        }
        return true;
    }
	//isTextChanged调用
	public String getString() {
        String text = "";
        try {
            text = getText().toString();
        } catch (OutOfMemoryError e) {
            Toast.makeText(getContext(), R.string.out_of_memory, 0).show();
        }
        return text;
    }
	//QSEditor.saveConfirm调用
	public String getTitle()
	{
		return this.current_title;
	}
	public static abstract interface OnTextChangedListener
	{
		public abstract void onTextChanged(QSEditText paramJecEditText);
	}
	//QSEditor.onCreate()调用
	public boolean canRedo()
	{
		return this.mRedoParcel.canUndo();
	}
    //QSEditor.onCreate()调用
	public boolean canUndo()
	{
		return this.mUndoParcel.canUndo();
	}
	//QSEditor.onEditLocationChanged调用
	public boolean isCanBackEditLocation()
	{
		boolean i = false;
		if ((this.mLastEditIndex >= 1) && (this.mLastEditIndex < this.mLastEditBuffer.size()))
			i = true;
		return i;
	}
    //QSEditor.onEditLocationChanged调用
	public boolean isCanForwardEditLocation()
	{
		boolean i;
		if (this.mLastEditIndex < -1 + this.mLastEditBuffer.size())
			i = true;
		else
			i = false;
		return i;
	}
	//QSEditor.onCreate()调用
	public String getCurrentFileExt()
	{
		return this.current_ext;
	}
	//QSEditor.onCreate()调用
	public boolean gotoBackEditLocation() {
        if (this.mLastEditIndex < DIR_LEFT_TO_RIGHT) {
            return false;
        }
        this.mLastEditIndex += DIR_RIGHT_TO_LEFT;
        int offset = ((Integer) this.mLastEditBuffer.get(this.mLastEditIndex)).intValue();
        setSelection(offset, offset);
        return true;
    }
//QSEditor.onCreate()调用
	public boolean gotoForwardEditLocation() {
        if (this.mLastEditIndex >= this.mLastEditBuffer.size()) {
            return false;
        }
        this.mLastEditIndex += DIR_LEFT_TO_RIGHT;
        int offset = ((Integer) this.mLastEditBuffer.get(this.mLastEditIndex)).intValue();
        setSelection(offset, offset);
        return true;
    }
	public void setOnTextChangedListener(OnTextChangedListener paramOnTextChangedListener)
	{
		this.mOnTextChangedListener = paramOnTextChangedListener;
	}
	public String getEncoding()
	{
		return this.current_encoding;
	}
	public int getLineBreak()
	{
		return this.current_linebreak;
	}
	//QSEditor.save调用
	public void setTextFinger()
	{
		this.src_text_length = getText().length();
		byte[] arrayOfByte = getString().getBytes();
		this.mCRC32.reset();
		this.mCRC32.update(arrayOfByte, 0, arrayOfByte.length);
		this.src_text_crc32 = this.mCRC32.getValue();
	}
	//QSEditor.onNewIntent调用
	public void setText2(CharSequence text) {
        try {
            super.setText(text);
        } catch (OutOfMemoryError e) {
            Toast.makeText(getContext(), R.string.out_of_memory, 0).show();
        }
    }
	private void setLineNumberWidth(int paramInt)
	{
		this.mLineNumberWidth = (int)this.mLineNumberPaint.measureText(paramInt + "|");
		this.mLineNumber = paramInt;
		this.mLineNumberLength = Integer.toString(paramInt).length();
		setShowLineNum(this.mShowLineNum);
	}
	//drawView调用
	private int getLineVisibleEnd(int line, int start, int end) {
        CharSequence text = getText();
        if (line == getLineCount() + DIR_RIGHT_TO_LEFT) {
            return end;
        }
        if (end < DIR_LEFT_TO_RIGHT) {
            return 0;
        }
        while (end > start) {
            try {
                char ch = text.charAt(end + DIR_RIGHT_TO_LEFT);
                if (ch != '\n') {
                    if (ch != ' ' && ch != '\t') {
                        break;
                    }
                    end += DIR_RIGHT_TO_LEFT;
                } else {
                    return end + DIR_RIGHT_TO_LEFT;
                }
            } catch (Exception e) {
                return end;
            }
        }
        return end;
    }
	
	
	//drawText调用
	static float nextTabPos(CharSequence text, int start, int end, float h, Object[] tabs) {
        float nh = Float.MAX_VALUE;
        boolean alltabs = false;
        if (text instanceof Spanned) {
            if (tabs == null) {
                tabs = ((Spanned) text).getSpans(start, end, TabStopSpan.class);
                alltabs = true;
            }
            int i = 0;
            while (i < tabs.length) {
                if (alltabs || (tabs[i] instanceof TabStopSpan)) {
                    int where = ((TabStopSpan) tabs[i]).getTabStop();
                    if (((float) where) < nh && ((float) where) > h) {
                        nh = (float) where;
                    }
                }
                i += DIR_LEFT_TO_RIGHT;
            }
            if (nh != Float.MAX_VALUE) {
                return nh;
            }
        }
        return ((float) ((int) ((TAB_INCREMENT + h) / TAB_INCREMENT))) * TAB_INCREMENT;
    }
	//QSEditor.onActivityResult
	public void setPath(String path) {
        if (!"".equals(path)) {
            this.current_path = path;
            if (new File(this.current_path).length() / 1024 > ((long) Highlight.getLimitFileSize())) {
                Toast.makeText(getContext(), getResources().getString(R.string.highlight_stop_msg), DIR_LEFT_TO_RIGHT).show();
            } else {
                setCurrentFileExt(FileUtil.getExt(path));
            }
        }
    }
	//LangList.mClickEvent调用
	public void setCurrentFileExt(String ext) {
        this.current_ext = ext;
       // this.mHighlight.redraw();
        this.mHighlight.setSyntaxType(this.current_ext);
    }
	
	
    // 长按弹出文本选择框menu的关键方法：可以选择复制、剪切等等功能，视该textview的具体实现而定
	 // 如果希望不弹出这个menu界面，只要把这个方法返回空就ok
	     @Override
	     protected MovementMethod getDefaultMovementMethod() {
		         // TODO Auto-generated method stub
		         return super.getDefaultMovementMethod();
		     }
	// textview的点击捕捉
	 // 如果双击textview选中了具体文字，则使cursor可见
	    // int cursorStart = -1;
	 
	    /* @Override
	     public boolean onTouchEvent(MotionEvent event) {
		         boolean flag = super.onTouchEvent(event);
		         if (event.getAction() == MotionEvent.ACTION_DOWN && hasSelection()) {
			             if (cursorStart == -1) {// 由于点击选中文字后，再点击其他位置，第一次点击时显示的hasSelection依然为true，这样一来cursor会依然还在，为了避免这种情况，我这里多对selectionStart进行了一次验证
				                 setCursorVisible(true);
				                 cursorStart = getSelectionStart();
				             } else {
				                 setCursorVisible(false);
				                 cursorStart = -1;
				             }
			         }
		         return flag;
		     }*/
			 
	public boolean onTouchEvent(MotionEvent event) {
		
		//跟踪触摸屏事件（flinging事件和其他gestures手势事件）的速率。用addMovement(MotionEvent)函数将Motion event加入到VelocityTracker类实例中.你可以使用getXVelocity() 或getXVelocity()获得横向和竖向的速率到速率时，但是使用它们之前请先调用computeCurrentVelocity(int)来初始化速率的单位 
		//当你需要跟踪触摸屏事件的速度的时候,使用obtain()方法来获得VelocityTracker类的一个实例对象
		//在onTouchEvent回调函数中，使用addMovement(MotionEvent)函数将当前的移动事件传递给VelocityTracker对象
		//使用computeCurrentVelocity  (int units)函数来计算当前的速度，使用 getXVelocity  ()、 getYVelocity  ()函数来获得当前的速度
		if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
        
		if (this.mFastScroller != null && (this.mFastScroller.onTouchEvent(event) || this.mFastScroller.onInterceptTouchEvent(event))) {
            return true;
        }
		
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://TabWidget.MENU_ACTION_CLOSE_ONE /*0*/
                   
				
				if (TOUCH_ZOOM_ENABLED) {
                    this.mTouchMode = TOUCH_DRAG_START_MODE;
                    this.oldDist = calc_spacing(event);
                }
                if (this.mFlingRunnable != null) {
                    this.mFlingRunnable.endFling();
                    cancelLongPress();
                    break;
                }
                break;
            case MotionEvent.ACTION_UP://DIR_LEFT_TO_RIGHT /*1*/
                
				this.mTouchMode = TOUCH_DONE_MODE;
                int mMinimumVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
                this.mVelocityTracker.computeCurrentVelocity(1000, (float) ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity());
                int initialVelocity = (int) this.mVelocityTracker.getYVelocity();
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    try {
                        if (this.mFlingRunnable == null) {
                            this.mFlingRunnable = new FlingRunnable(getContext());
                        }
                        this.mHighlight.stop();
                        this.mFlingRunnable.start(this, -initialVelocity);
                    } catch (Exception e) {
                    }
                } else {
                    this.mHighlight.redraw();
                }
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    break;
                }
                break;
            case MotionEvent.ACTION_MOVE://TOUCH_DRAG_START_MODE /*2*/
                
				if (TOUCH_ZOOM_ENABLED && this.mTouchMode == TOUCH_DRAG_START_MODE && this.mSupportMultiTouch && event.getPointerCount() >= TOUCH_DRAG_START_MODE) {
                    cancelLongPress();
                    float newDist = calc_spacing(event);
                    if (Math.abs(newDist - this.oldDist) > MIN_TEXT_SIZE) {
                        if (newDist > this.oldDist) {
                            zoomOut();
                        } else if (newDist < this.oldDist) {
                            zoomIn();
                        }
                        this.oldDist = newDist;
                        break;
                    }
                }
                break;
        }
        try {
            return super.onTouchEvent(event);
        } catch (Exception e2) {
            return true;
        }
    }
	 
	
	

	
	private float calc_spacing(MotionEvent event) {
        if (event.getPointerCount() < TOUCH_DRAG_START_MODE) {
            return 0.0f;
        }
        float x = event.getX(0) - event.getX(DIR_LEFT_TO_RIGHT);
        float y = event.getY(0) - event.getY(DIR_LEFT_TO_RIGHT);
        return FloatMath.sqrt((x * x) + (y * y));
    }
	
	private boolean equalsCharSequence(CharSequence s1, CharSequence s2) {
        if (s1 == null || s2 == null || s1.length() != s2.length()) {
            return false;
        }
        return s1.toString().equals(s2.toString());
    }
	private void setUndoRedoButtonStatus()
	{
		if (this.mOnTextChangedListener != null)
			this.mOnTextChangedListener.onTextChanged(this);
	}
	
	

	

	

	
	
	
	
    //QSEditor.load_options调用
	public static void setHideKeyboard(boolean paramBoolean)
	{
		mHideSoftKeyboard = paramBoolean;
	}
	//QSEditor.load_options调用
	public void setDateFormat(String paramString)
	{
		this.mDateFormat = paramString;
	}
	//QSEditor.load_options调用
	public void showIME(boolean show) {
        setHideKeyboard(!show);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService("input_method");
        if (getResources().getConfiguration().hardKeyboardHidden != TOUCH_DRAG_START_MODE) {
            show = false;
        }
        if (show) {
            int type = 131073;
            if (mAutoCapitalize) {
                type = 131073 | 16384;
            }
            if (mDisableSpellCheck) {
                type |= -524289;
            }
            setInputType(type);
            if (imm != null) {
                imm.showSoftInput(this, 0);
                return;
            }
            return;
        }
        setRawInputType(0);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }
	

	

	

	

	
/*
	

	

	//public int getLineVisibleEnd(int paramInt)
	//{
	//	return getLineVisibleEnd(paramInt, this.mLayout.getLineStart(paramInt), this.mLayout.getLineStart(paramInt + 1));
	//}

	
	
*/
	

	public int getVisibleHeight() {
        int b = getBottom();
        int t = getTop();
        return ((b - t) - getExtendedPaddingBottom()) - getExtendedPaddingTop();
    }

	

	public boolean gotoLine(int line) {
        if (line < DIR_LEFT_TO_RIGHT) {
            return false;
        }
        int count = 0;
        int strlen = this.mText.length();
        for (int index = 0; index < strlen; index += DIR_LEFT_TO_RIGHT) {
            if (this.mText.charAt(index) == '\n') {
                count += DIR_LEFT_TO_RIGHT;
                if (count == line) {
                    Selection.setSelection(this.mText, index, index);
                    return true;
                }
            }
        }
        return false;
    }

	
	
	/*@Override
	 protected void onCreateContextMenu(ContextMenu menu) {
		 super.onCreateContextMenu(menu);
		 if (isInputMethodTarget()) {
			 menu.removeItem(android.R.id.switchInputMethod);
		 }
    }*/
	
	protected void onCreateContextMenu(ContextMenu menu) {
        if (USE_SYSTEM_MENU) {
            super.onCreateContextMenu(menu);
        }
        QSEditText jecEditText;
        if (isFocused()) {
            jecEditText = this;
            MenuHandler handler = new MenuHandler();
            boolean selection = getSelectionStart() != getSelectionEnd();
            if (!USE_SYSTEM_MENU) {
                int name;
                if (canSelectAll()) {
                    menu.add(0, ID_SELECT_ALL, 0, R.string.selectAll).setOnMenuItemClickListener(handler).setAlphabeticShortcut('a');
                }
                if (canSelectText()) {
                    if (MetaKeyKeyListener.getMetaState(this.mText, META_SELECTING) != 0) {
                        menu.add(0, ID_STOP_SELECTING_TEXT, 0, R.string.stopSelectingText).setOnMenuItemClickListener(handler);
                    } else {
                        menu.add(0, ID_START_SELECTING_TEXT, 0, R.string.selectText).setOnMenuItemClickListener(handler);
                    }
                }
                if (canCut()) {
                    if (selection) {
                        name = R.string.cut;
                    } else {
                        name = R.string.cutAll;
                    }
                    menu.add(0, ID_CUT, 0, name).setOnMenuItemClickListener(handler).setAlphabeticShortcut('x');
                }
                if (canCopy()) {
                    if (selection) {
                        name = R.string.copy;
                    } else {
                        name = R.string.copyAll;
                    }
                    menu.add(0, ID_COPY, 0, name).setOnMenuItemClickListener(handler).setAlphabeticShortcut('c');
                }
                if (canPaste()) {
                    menu.add(0, ID_PASTE, 0, R.string.paste).setOnMenuItemClickListener(handler).setAlphabeticShortcut('v');
                }
                if (this.mText instanceof Spanned) {
                    int selStart = getSelectionStart();
                    int selEnd = getSelectionEnd();
                    if (((URLSpan[]) this.mText.getSpans(Math.min(selStart, selEnd), Math.max(selStart, selEnd), URLSpan.class)).length == DIR_LEFT_TO_RIGHT) {
                        menu.add(0, ID_COPY_URL, 0, R.string.copyUrl).setOnMenuItemClickListener(handler);
                    }
                }
            }
            menu.add(0, R.id.duplicate_line, 0, selection ? R.string.duplicate_selected_text : R.string.duplicate_line).setOnMenuItemClickListener(handler);
            menu.add(0, R.id.to_lower, 0, R.string.to_lower).setOnMenuItemClickListener(handler);
            menu.add(0, R.id.to_upper, 0, R.string.to_upper).setOnMenuItemClickListener(handler);
            menu.add(0, R.id.go_to_begin, 0, R.string.go_to_begin).setOnMenuItemClickListener(handler);
            menu.add(0, R.id.go_to_end, 0, R.string.go_to_end).setOnMenuItemClickListener(handler);
            menu.add(0, R.id.goto_line, 0, R.string.goto_line).setOnMenuItemClickListener(handler);
            menu.add(0, R.id.insert_datetime, 0, new StringBuilder(String.valueOf(getResources().getString(R.string.insert_datetime))).append(TimeUtil.getDateByFormat(this.mDateFormat)).toString()).setOnMenuItemClickListener(handler);
            if (mHideSoftKeyboard) {
                menu.add(0, R.id.show_ime, 0, R.string.show_ime).setOnMenuItemClickListener(handler);
            } else {
                menu.add(0, R.id.hide_ime, 0, R.string.hide_ime).setOnMenuItemClickListener(handler);
            }
            menu.add(0, R.id.doc_stat, 0, R.string.doc_stat).setOnMenuItemClickListener(handler);
            menu.setHeaderTitle(R.string.editTextMenuTitle);
        } else if (isFocusable() && getKeyListener() != null && !USE_SYSTEM_MENU && canCopy()) {
            jecEditText = this;
            menu.add(0, ID_COPY, 0, R.string.copyAll).setOnMenuItemClickListener(new MenuHandler()).setAlphabeticShortcut('c');
            menu.setHeaderTitle(R.string.editTextMenuTitle);
        }
		
    }
	
	private boolean canSelectText() {
        if (!(this.mText instanceof Spannable) || this.mText.length() == 0 || getMovementMethod() == null || !getMovementMethod().canSelectArbitrarily()) {
            return false;
        }
        return true;
    }
	
	
	 //Android键盘事件的响应
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 29://R.styleable.View_nextFocusDown /*29*/:
                if (canSelectAll()) {
                    return onTextContextMenuItem(ID_SELECT_ALL);
                }
                break;
            case 31://R.styleable.View_longClickable /*31*/:
                if (canCopy()) {
                    return onTextContextMenuItem(ID_COPY);
                }
                break;
            case 50://R.styleable.Theme_expandableListPreferredChildIndicatorLeft /*50*/:
                if (canPaste()) {
                    return onTextContextMenuItem(ID_PASTE);
                }
                break;
            case 52://R.styleable.Theme_windowBackground /*52*/:
                if (canCut()) {
                    return onTextContextMenuItem(ID_CUT);
                }
                break;
        }
        return super.onKeyShortcut(keyCode, event);
    }
	private boolean canCopy()
	{
		Boolean i = false;
		if ((!(getTransformationMethod() instanceof PasswordTransformationMethod)) && (this.mText.length() > 0) && (getSelectionStart() >= 0))
			i = true;
		return i;
	}

	private boolean canCut()
	{
		boolean i = false;
		if ((!(getTransformationMethod() instanceof PasswordTransformationMethod)) && (this.mText.length() > 0) && (getSelectionStart() >= 0) && ((this.mText instanceof Editable)) && (getKeyListener() != null))
			i = true;
		return i;
	}

	private boolean canPaste()
	{
		boolean i;
		if ((!(this.mText instanceof Editable)) || (getKeyListener() == null) || (getSelectionStart() < 0) || (getSelectionEnd() < 0) || (!((ClipboardManager)getContext().getSystemService("clipboard")).hasText()))
			i = false;
		else
			i = true;
		return i;
	}

	private boolean canSelectAll()
	{
		boolean i;
		if ((!(this.mText instanceof Spannable)) || (this.mText.length() == 0) || (getMovementMethod() == null) || (!getMovementMethod().canSelectArbitrarily()))
			i = false;
		else
			i = true;
		return i;
	}
	// 当按返回键取消文字复制时，使cursor再次不可见
     /*@Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         boolean flag = super.onKeyDown(keyCode, event);
 
         setCursorVisible(false);
         cursorStart = -1;
         return flag;
     }*/
 
 
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result = super.onKeyDown(keyCode, event);
        if (this.mAutoIndent && keyCode == 66) {
            Editable mEditable = this.mText;
            if (mEditable != null) {
                int start = getSelectionStart();
                int end = getSelectionEnd();
                if (start == end) {
                    int prev = start - 2;
                    while (prev >= 0 && mEditable.charAt(prev) != '\n') {
                        prev += DIR_RIGHT_TO_LEFT;
                    }
                    prev += DIR_LEFT_TO_RIGHT;
                    int pos = prev;
                    while (true) {
                        if (mEditable.charAt(pos) != ' ' && mEditable.charAt(pos) != '\t' && mEditable.charAt(pos) != '\u3000') {
                            break;
                        }
                        pos += DIR_LEFT_TO_RIGHT;
                    }
                    int len = pos - prev;
                    if (len > 0) {
                        try {
                            char[] dest = new char[len];
                            mEditable.getChars(prev, pos, dest, 0);
                            mEditable.replace(start, end, new String(dest));
                            setSelection(start + len);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return result;
    }
	public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof JecSaveState) {
            JecSaveState mJecSaveState = (JecSaveState) state;
            super.onRestoreInstanceState(mJecSaveState.getSuperState());
            this.mUndoParcel = mJecSaveState.mUndoParcelState;
            this.mRedoParcel = mJecSaveState.mRedoParcelState;
            setUndoRedoButtonStatus();
            return;
        }
        super.onRestoreInstanceState(state);
    }
	
	// 监听ScroView的滑动情况，比如滑动了多少距离，是否滑到布局的顶部或者底部
	protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	{
		super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
		if ((this.mFastScroller != null) && (this.mLayout != null))
		{
			int i = getVisibleHeight();
			int j = this.mLayout.getHeight();
			this.mFastScroller.onScroll(this, paramInt2, i, j);
		}
	}
	 //view的大小发生改变是被系统调用
	protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	{
		if (this.mFastScroller != null)
			this.mFastScroller.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
	}

	/** Called when a context menu option for the text view is selected. Currently  
	 * this will be one of: {@link android.R.id#selectAll}, 
	 * {@link android.R.id#startSelectingText}, {@link android.R.id#stopSelectingText}, 
	 * {@link android.R.id#cut}, {@link android.R.id#copy}, 
	 * {@link android.R.id#paste}, {@link android.R.id#copyUrl}, 
	 * or {@link android.R.id#switchInputMethod}. 
	 */  
	/*public boolean onTextContextMenuItem(int id) {  

		int selStart = getSelectionStart();  
		int selEnd = getSelectionEnd();  

		if (!isFocused()) {  
			selStart = 0;  
			selEnd = mText.length();  
		}  

		int min = Math.min(selStart, selEnd);  
		int max = Math.max(selStart, selEnd);  

		if (min < 0) { min = 0;}  
		if (max < 0) {max = 0; }  

		ClipboardManager clip = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);  

		switch (id) {  
			case ID_SELECT_ALL:  
				Selection.setSelection((Spannable) mText, 0,  
									   mText.length());  
				return true;  

			case ID_START_SELECTING_TEXT:  
				MetaKeyKeyListener.startSelecting(this, (Spannable) mText);  
				return true;  

			case ID_STOP_SELECTING_TEXT:  
				MetaKeyKeyListener.stopSelecting(this, (Spannable) mText);  
				Selection.setSelection((Spannable) mText, getSelectionEnd());  
				return true;  
			case ID_CUT:  
				MetaKeyKeyListener.stopSelecting(this, (Spannable) mText);  
				if (min == max) {  
					min = 0;  
					max = mText.length();  
				}  

				clip.setText(mTransformed.subSequence(min, max));  
				((Editable) mText).delete(min, max);  
				return true;  
			case ID_COPY:  
				MetaKeyKeyListener.stopSelecting(this, (Spannable) mText);  

				if (min == max) {  
					min = 0;  
					max = mText.length();  
				}  
				clip.setText(mTransformed.subSequence(min, max));  
				return true;  

			case ID_PASTE:  
				MetaKeyKeyListener.stopSelecting(this, (Spannable) mText);  
				CharSequence paste = clip.getText();  
				if (paste != null) {  
					Selection.setSelection((Spannable) mText, max);  
					((Editable) mText).replace(min, max, paste);  
				}  

				return true;  

			case ID_COPY_URL:  

				MetaKeyKeyListener.stopSelecting(this, (Spannable) mText);  
				URLSpan[] urls = ((Spanned) mText).getSpans(min, max,URLSpan.class);  
				if (urls.length == 1) {  
					clip.setText(urls[0].getURL());  
				}  
				return true;  

			case ID_SWITCH_INPUT_METHOD:  
				InputMethodManager imm = InputMethodManager.peekInstance();  
				if (imm != null) {  
					imm.showInputMethodPicker();  
				}  

				return true;  

			case ID_ADD_TO_DICTIONARY:  
				String word = getWordForDictionary();  
				if (word != null) {  
					Intent i = new Intent("com.android.settings.USER_DICTIONARY_INSERT");  
					i.putExtra("word", word);  
					getContext().startActivity(i);  
				}  

				return true;  
		}  

		return false;  
	}  */
// 点击menu中的选定item的具体处理方法，捕捉点击文本复制、剪切等按钮的动作
	 // 如果要在点击复制按钮之后取消该textview的cursor可见性的具体监听写在这里
	    /* @Override
	     public boolean onTextContextMenuItem(int id) {
		         setCursorVisible(true);
		         boolean flag;
		         if (id != android.R.id.switchInputMethod) {
			             flag = super.onTextContextMenuItem(id);
			         } else {
			             setCursorVisible(false);
			             return false;
			         }
		         if (id == android.R.id.copy) {
			             setCursorVisible(false);
			             cursorStart = -1;
			         }
		         return flag;
		     }*/
	
	public boolean onTextContextMenuItem(int id) {
        setCursorVisible(true);
		int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        //是否具有接受焦点的资格
		if (!isFocused()) {
            selStart = 0;
            selEnd = this.mText != null ? this.mText.length() : 0;
        }
        int min = Math.min(selStart, selEnd);
        int max = Math.max(selStart, selEnd);
        if (min < 0) {
            min = 0;
        }
        if (max < 0) {
            max = 0;
        }
		
        ClipboardManager clip = (ClipboardManager) getContext().getSystemService("clipboard");
        switch (id) {
            case ID_SELECT_ALL: /*16908319*/
                Selection.setSelection(this.mText, 0, this.mText.length());
                return true;
           
			case ID_CUT: /*16908320*/
                if (!super.onTextContextMenuItem(id)) {
                    if (min == max) {
                        min = 0;
                        max = this.mText.length();
                    }
                    clip.setText(this.mText.subSequence(min, max));
                    this.mText.delete(min, max);
                }
                return true;
            case ID_COPY: /*16908321*/
                if (!super.onTextContextMenuItem(id)) {
                    if (min == max) {
                        min = 0;
                        max = this.mText.length();
                    }
                    clip.setText(this.mText.subSequence(min, max));
                }
                return true;
            case ID_PASTE: /*16908322*/
                if (!super.onTextContextMenuItem(id)) {
                    CharSequence paste = clip.getText();
                    if (paste != null) {
                        Selection.setSelection(this.mText, max);
                        this.mText.replace(min, max, paste);
                    }
                }
                return true;
            case ID_COPY_URL: /*16908323*/
                if (!super.onTextContextMenuItem(id)) {
                    URLSpan[] urls = (URLSpan[]) this.mText.getSpans(min, max, URLSpan.class);
                    if (urls.length == DIR_LEFT_TO_RIGHT) {
                        clip.setText(urls[0].getURL());
                    }
                }
                return true;
            case R.id.show_ime: /*2131099891*/
                showIME(true);
                break;
            case R.id.to_lower: /*2131099892*/
            case R.id.to_upper: /*2131099893*/
                int start = getSelectionStart();
                int end = getSelectionEnd();
                if (start != end) {
                    try {
                        Editable mText2 = getText();
                        char[] dest = new char[(end - start)];
                        mText2.getChars(start, end, dest, 0);
                        if (id != 2131099892) {
                            mText2.replace(start, end, new String(dest).toUpperCase());
                            break;
                        }
                        mText2.replace(start, end, new String(dest).toLowerCase());
                        break;
                    } catch (Exception e) {
                        break;
                    }
                }
                break;
            case R.id.goto_line: /*2131099894*/
                final EditText lineEditText = new EditText(getContext());
                lineEditText.setInputType(TOUCH_DRAG_START_MODE);
                Builder builder = new Builder(getContext());
                builder.setTitle(R.string.goto_line).setView(lineEditText).setNegativeButton(17039360, null);
                builder.setPositiveButton(17039370, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which) {
							try {
								if (QSEditText.this.gotoLine(Integer.valueOf(lineEditText.getText().toString()).intValue())) {
									dialog.dismiss();
								} else {
									Toast.makeText(QSEditText.this.getContext(), R.string.can_not_gotoline, QSEditText.DIR_LEFT_TO_RIGHT).show();
								}
							} catch (Exception e) {
							}
						}
					});
                builder.show();
                break;
            case R.id.go_to_end: /*2131099896*/
                int len = getText().length();
                setSelection(len, len);
                break;
            case R.id.go_to_begin: /*2131099897*/
                break;
            case R.id.hide_ime: /*2131099898*/
                showIME(false);
                break;
            case R.id.insert_datetime: /*2131099907*/
                String text = TimeUtil.getDateByFormat(this.mDateFormat);
                getText().replace(min, max, text, 0, text.length());
                break;
            case R.id.duplicate_line: /*2131099908*/
                int selectionStart;
				int selectionEnd;
				CharSequence dateByFormat;
				
			    if (selStart == selEnd) {
                    while (true) {
                        selectionStart = selStart + DIR_RIGHT_TO_LEFT;
                        if (selectionStart >= 0 && this.mText.charAt(selectionStart) != '\n') {
                            selStart = selectionStart;
                        } else {
                            max = this.mText.length();
                            while (true) {
                                selectionEnd = selEnd + DIR_LEFT_TO_RIGHT;
                                if (selEnd < max && this.mText.charAt(selectionEnd) != '\n') {
                                    selEnd = selectionEnd;
                                } else {
                                    if (selectionStart < 0) {
                                        selectionStart = 0;
                                    }
                                    if (selectionEnd >= max) {
                                        selectionEnd = max + DIR_RIGHT_TO_LEFT;
                                    }
                                    if (selectionStart != selectionEnd && selectionEnd >= DIR_LEFT_TO_RIGHT) {
                                        dateByFormat = this.mText.subSequence(selectionStart, selectionEnd);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    dateByFormat = this.mText.subSequence(min, max);
                    selectionEnd = max;
                }
                getText().replace(selectionEnd, selectionEnd, dateByFormat, 0, dateByFormat.length());
                break;
			/*CharSequence text2;
                int offset;
                if (selStart == selEnd) {
                    int textlen;
                    int e2;
                    int s = selStart;
                    int e3 = selEnd;
                    do {
                        s += DIR_RIGHT_TO_LEFT;
                        if (s < 0) {
                        }
                        textlen = this.mText.length();
                        e2 = e3;
                        while (true) {
                            e3 = e2 + DIR_LEFT_TO_RIGHT;
                            if (e2 < textlen && this.mText.charAt(e3) != '\n') {
                                e2 = e3;
                            } else {
                                if (s < 0) {
                                    s = 0;
                                }
                                if (e3 >= textlen) {
                                    e3 = textlen + DIR_RIGHT_TO_LEFT;
                                }
                                if (s != e3 && e3 >= DIR_LEFT_TO_RIGHT) {
                                    text2 = this.mText.subSequence(s, e3);
                                    offset = e3;
                                }
                            }
                        }
                    } while (this.mText.charAt(s) != '\n');
                    /*textlen = this.mText.length();
                    e2 = e3;
                    while (true) {
                        e3 = e2 + DIR_LEFT_TO_RIGHT;
                        if (e2 < textlen) {
                            e2 = e3;
                            break;
                        }
                        if (s < 0) {
                            s = 0;
                        }
                        if (e3 >= textlen) {
                            e3 = textlen + DIR_RIGHT_TO_LEFT;
                        }
                        text2 = this.mText.subSequence(s, e3);
                        offset = e3;
                        break;
                    }
                }
                text2 = this.mText.subSequence(min, max);
                offset = max;
                getText().replace(offset, offset, text2, 0, text2.length());*/
               // break;
            case R.id.doc_stat: /*2131099909*/
                Context context = getContext();
                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (Pattern.compile("\\w+").matcher(this.mText).find()) {
                    i += DIR_LEFT_TO_RIGHT;
                }
                sb.append(context.getString(R.string.filename)).append("\t\t").append(getPath()).append("\n\n").append(context.getString(R.string.total_chars)).append("\t\t").append(this.mText.length()).append("\n").append(context.getString(R.string.total_words)).append("\t\t").append(i).append("\n").append(context.getString(R.string.total_lines)).append("\t\t").append(TextUtil.countMatches(this.mText, '\n', 0, this.mText.length() + DIR_RIGHT_TO_LEFT) + DIR_LEFT_TO_RIGHT);
                new Builder(context).setTitle(R.string.doc_stat).setMessage(sb.toString()).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
                break;
        
			default:
				try {
					Field mEditor = TextView.class.getDeclaredField("mEditor");//找到 TextView中的成员变量mEditor  
					mEditor.setAccessible(true); 
					Object object= mEditor.get(QSEditText.this);//根具持有对象拿到mEditor变量里的值 （android.widget.Editor类的实例）
					//--------------------显示选择控制工具------------------------------//
					Class mClass=Class.forName("android.widget.Editor");//拿到隐藏类Editor； 
					Method  method=mClass.getDeclaredMethod("getSelectionController");//取得方法  getSelectionController 
					method.setAccessible(true);//取消访问私有方法的合法性检查     
					Object resultobject=method.invoke(object);//调用方法，返回SelectionModifierCursorController类的实例

					Method show=resultobject.getClass().getDeclaredMethod("show");//查找 SelectionModifierCursorController类中的show方法
					show.invoke(resultobject);//执行SelectionModifierCursorController类的实例的show方法
					QSEditText.this.setHasTransientState(true); 

					//--------------------忽略最后一次TouchUP事件-----------------------------------------------//
					Field  mSelectionActionMode=mClass.getDeclaredField("mDiscardNextActionUp");//查找变量Editor类中mDiscardNextActionUp
					mSelectionActionMode.setAccessible(true); 
					mSelectionActionMode.set(object,true);//赋值为true 

				} catch (Exception e) { 
					e.printStackTrace();
				}
				//Selection.setSelection((Spannable) mText, getSelectionEnd()); 
				break;
		}
		
		
        return super.onTextContextMenuItem(id);
    }
	

	
	

	public void resetUndoStatus()
	{
		this.mRedoParcel.clean();
		this.mUndoParcel.clean();
		setUndoRedoButtonStatus();
		this.mLastEditBuffer.clear();
	}

	

	
	

	

	public void setEncoding(String paramString)
	{
		this.current_encoding = paramString;
	}

	public void setHorizontallyScrolling(boolean paramBoolean)
	{
		this.mNoWrapMode = paramBoolean;
		super.setHorizontallyScrolling(paramBoolean);
	}

	public void setLineBreak(int paramInt)
	{
		this.current_linebreak = paramInt;
	}

    //QSEditor.bindEvent()调用
	public void unDo()
	{
		UndoParcel.TextChange localTextChange = this.mUndoParcel.pop();
		if (localTextChange != null)
		{
			Editable localEditable = getText();
			this.mUndoRedo = true;
			localEditable.replace(localTextChange.start, localTextChange.start + localTextChange.newtext.length(), localTextChange.oldtext);
			Selection.setSelection(localEditable, localTextChange.start + localTextChange.oldtext.length());
			this.mRedoParcel.push(localTextChange);
			setUndoRedoButtonStatus();
		}
	}
	//QSEditor.bindEvent()调用
	public void reDo()
	{
		UndoParcel.TextChange localTextChange = this.mRedoParcel.pop();
		if (localTextChange != null)
		{
			Editable localEditable = getText();
			this.mUndoRedo = true;
			localEditable.replace(localTextChange.start, localTextChange.start + localTextChange.oldtext.length(), localTextChange.newtext);
			Selection.setSelection(localEditable, localTextChange.start + localTextChange.newtext.length());
			this.mUndoParcel.push(localTextChange);
			setUndoRedoButtonStatus();
		}
	}
	protected void zoomIn()
	{
		this.mTextSize -= this.scale;
		if (this.mTextSize < 10.0F)
			this.mTextSize = 10.0F;
		setTextSize(this.mTextSize);
		this.mLineNumberPaint.setTextSize(this.mTextSize - 2.0F);
	}

	protected void zoomOut()
	{
		this.mTextSize += this.scale;
		if (this.mTextSize > 32.0F)
			this.mTextSize = 32.0F;
		setTextSize(this.mTextSize);
		this.mLineNumberPaint.setTextSize(this.mTextSize - 2.0F);
	}
//------
	public static class Directions
	{
		private short[] mDirections;

		Directions(short[] paramArrayOfShort)
		{
			this.mDirections = paramArrayOfShort;
		}
	}
//-----
	private static class FlingRunnable implements Runnable {
        static final int TOUCH_MODE_FLING = 3;
        static final int TOUCH_MODE_REST = -1;
        private int mLastFlingY;
        private final Scroller mScroller;
        int mTouchMode;
        private QSEditText mWidget;

        FlingRunnable(Context context) {
            this.mTouchMode = TOUCH_MODE_REST;
            this.mWidget = null;
            this.mScroller = new Scroller(context);
        }

        void start(QSEditText parent, int initialVelocity) {
            this.mWidget = parent;
            int initialX = parent.getScrollX();
            int initialY = parent.getScrollY();
            this.mLastFlingY = initialY;
            this.mScroller.fling(initialX, initialY, 0, initialVelocity, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            this.mTouchMode = TOUCH_MODE_FLING;
            this.mWidget.post(this);
        }

        private void endFling() {
            this.mTouchMode = TOUCH_MODE_REST;
            if (this.mWidget != null) {
                try {
                    this.mWidget.removeCallbacks(this);
                    this.mWidget.moveCursorToVisibleOffset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.mWidget = null;
            }
        }

        public void run() {
            switch (this.mTouchMode) {
                case TOUCH_MODE_FLING /*3*/:
                    Scroller scroller = this.mScroller;
                    boolean more = scroller.computeScrollOffset();
                    int x = scroller.getCurrX();
                    int y = scroller.getCurrY();
                    Layout layout = this.mWidget.getLayout();
                    if (layout != null) {
                        int padding;
                        try {
                            padding = this.mWidget.getTotalPaddingTop() + this.mWidget.getTotalPaddingBottom();
                        } catch (Exception e) {
                            padding = 0;
                        }
                        y = Math.max(Math.min(y, layout.getHeight() - (this.mWidget.getHeight() - padding)), 0);
                        Touch.scrollTo(this.mWidget, layout, x, y);
                        int delta = this.mLastFlingY - y;
                        if (Math.abs(delta) <= TOUCH_MODE_FLING) {
                          //  this.mWidget.mHighlight.redraw();
                        }
                        if (!more || delta == 0) {
                            endFling();
                            return;
                        }
                        this.mWidget.invalidate();
                        this.mLastFlingY = y;
                        this.mWidget.post(this);
                    }
					break;
                default:
				break;
            }
        }
    }
//------
	private static class JecSaveState extends BaseSavedState {
        UndoParcel mRedoParcelState;
        UndoParcel mUndoParcelState;

        JecSaveState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelable(this.mUndoParcelState, 0);
            out.writeParcelable(this.mRedoParcelState, 0);
        }

        private JecSaveState(Parcel in) {
            super(in);
            this.mUndoParcelState = (UndoParcel) in.readParcelable(UndoParcel.class.getClassLoader());
            this.mRedoParcelState = (UndoParcel) in.readParcelable(UndoParcel.class.getClassLoader());
        }
    }
//-------
	private class MenuHandler implements OnMenuItemClickListener {
        private MenuHandler() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            return QSEditText.this.onTextContextMenuItem(item.getItemId());
        }
    }
	
	
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.JecEditText
 * JD-Core Version:    0.6.0
 */
