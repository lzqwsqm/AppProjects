
package com.qingshan.editor;
import android.graphics.Rect;
import com.qingshan.editor.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
//import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.qingshan.highlight.Highlight;
import com.qingshan.util.ColorPicker;
//import com.jecelyin.util.ColorPicker.OnColorChangedListener;
import com.qingshan.util.FileBrowser;
import com.qingshan.util.FileUtil;
import com.qingshan.util.LinuxShell;
import com.qingshan.widget.QSEditText;
//import com.jecelyin.widget.JecEditText.OnTextChangedListener;
import com.qingshan.widget.QSMenu;
//import com.jecelyin.widget.JecMenu.OnMenuItemSelectedListener;
import com.qingshan.widget.SymbolGrid;
//import com.jecelyin.widget.SymbolGrid.OnSymbolClickListener;
import com.qingshan.widget.TabHost;
//import com.jecelyin.widget.TabHost.OnTabChangeListener;
//import com.jecelyin.widget.TabHost.OnTabCloseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import android.widget.TextView;
//import java.text.*;
/*
调试备忘
 QSEditText.setPath
 //QSEditText.onTouchEvent
 QSEditText.drawView
 QSEditText.unDo()
 FileBrowser.showFileList
 QSEditText.FlingRunnable.run()
*/
/*
 //指定当前类的类名.类的访问权限public.类名为Lcom/jecelyin/editor/JecEditor;类名开头的L表示后面跟随的字符串为一个类
 .class public Lcom/jecelyin/editor/JecEditor;
 //指定当前类的父类
 .super Landroid/app/Activity;
 //指定当前类的源文件名
 .source "JecEditor.java"

 //注释以#号开头
 # annotations
 .annotation system Ldalvik/annotation/MemberClasses;
 value = {
 Lcom/jecelyin/editor/JecEditor$ColorListener;
 }
 .end annotation
 */
public class QSEditor extends Activity
{
	/*
	 # static fields
	 //声明静态字段.field指令后面跟随着的是访问权限.修饰关键字描述字段的其它属性指令的最后是字段名与类型
	 .field private static final ACTION_EDIT_SCRIPT:Ljava/lang/String; = "com.googlecode.android_scripting.action.EDIT_SCRIPT"*/
    private static final String ACTION_EDIT_SCRIPT = "com.googlecode.android_scripting.action.EDIT_SCRIPT";
	private static final String EXTRA_SCRIPT_CONTENT = "com.googlecode.android_scripting.extra.SCRIPT_CONTENT";
	private static final String EXTRA_SCRIPT_PATH = "com.googlecode.android_scripting.extra.SCRIPT_PATH";
	public static final int FILE_BROWSER_OPEN_CODE = 0;
	public static final int FILE_BROWSER_SAVEAS_CODE = 1;
	public static final String PREF_HISTORY = "history";
	private static final String PREF_LAST_FILE = "last_files";
	private static final String SYNTAX_SIGN = "23";
	private static final String TAG = "JecEditor";
	public static String TEMP_PATH;
	private static boolean fullScreen;
	private static boolean hideToolbar;
	public static boolean isFinish;
	public static boolean isLoading;
	public static boolean isRoot;
	public static String version = "";
	//实例字段
	/*# instance fields
	.field public MAX_HIGHLIGHT_FILESIZE:I
    */
	
	
	public int MAX_HIGHLIGHT_FILESIZE = 400;
	private boolean autosave = false;
	private boolean back_button_exit = true;
//--
	private EditText findEditText;
	private LinearLayout findLayout;
	private ImageButton last_edit_back;
	private Drawable last_edit_back_d;
	private Drawable last_edit_back_s;
	private ImageButton last_edit_forward;
	private Drawable last_edit_forward_d;
	private Drawable last_edit_forward_s;
	private AsyncSearch mAsyncSearch;
	private QSEditText mEditText;
	/*.field private mLastFiles:Ljava/util/ArrayList;
	 .annotation system Ldalvik/annotation/Signature;
	 value = {
	 "Ljava/util/ArrayList",
	 "<",
	 "Ljava/lang/String;",
	 ">;"
	 }
	 .end annotation
	 .end field


	 */
	private ArrayList<String> mLastFiles = new ArrayList();
	private QSMenu mMenu;
	//---
	private SharedPreferences mPref;
	private SymbolGrid mSymbolGrid;
	private TabHost mTabHost;
//--
	private ImageButton previewBtn;
	private ImageButton redoBtn;
	private Drawable redo_can_drawable;
	private Drawable redo_no_drawable;
	private EditText replaceEditText;
	private LinearLayout replaceLayout;
	private Button replaceShowButton;
	private HorizontalScrollView toolbar;
	private ImageButton undoBtn;
	private Drawable undo_can_drawable;
	private Drawable undo_no_drawable;
	//private ActionMode mActionMode;
	/*
	 # direct methods
	 //声明直接方法
	 .method static constructor <clinit>()V
	 .locals 2

	 .prologue
	 const/4 v1, 0x0

	 .line 86
	 const-string v0, ""

	 sput-object v0, Lcom/jecelyin/editor/JecEditor;->version:Ljava/lang/String;

	 .line 87
	 const-string v0, ""

	 sput-object v0, Lcom/jecelyin/editor/JecEditor;->TEMP_PATH:Ljava/lang/String;

	 .line 100
	 sput-boolean v1, Lcom/jecelyin/editor/JecEditor;->isLoading:Z

	 .line 101
	 sput-boolean v1, Lcom/jecelyin/editor/JecEditor;->fullScreen:Z

	 .line 102
	 sput-boolean v1, Lcom/jecelyin/editor/JecEditor;->hideToolbar:Z

	 .line 103
	 sput-boolean v1, Lcom/jecelyin/editor/JecEditor;->isRoot:Z

	 .line 104
	 sput-boolean v1, Lcom/jecelyin/editor/JecEditor;->isFinish:Z

	 .line 78
	 return-void
	 .end method
	*/
	static
	{
		TEMP_PATH = "";
		isLoading = false;
		fullScreen = false;
		hideToolbar = false;
		isRoot = false;
		isFinish = false;
	}
	
	
	/*
	
	 Activity生命周期的几个过程，我们就来说一说这几个过程。
	 1.启动Activity：系统会先调用onCreate方法，然后调用onStart方法，最后调用onResume，Activity进入运行状态。
	 2.当前Activity被其他Activity覆盖其上或被锁屏：系统会调用onPause方法，暂停当前Activity的执行。
	 3.当前Activity由被覆盖状态回到前台或解锁屏：系统会调用onResume方法，再次进入运行状态。
	 4.当前Activity转到新的Activity界面或按Home键回到主屏，自身退居后台：系统会先调用onPause方法，然后调用onStop方法，进入停滞状态。
	 5.用户后退回到此Activity：系统会先调用onRestart方法，然后调用onStart方法，最后调用onResume方法，再次进入运行状态。
	 6.当前Activity处于被覆盖状态或者后台不可见状态，即第2步和第4步，系统内存不足，杀死当前Activity，而后用户退回当前Activity：再次调用onCreate方法、onStart方法、onResume方法，进入运行状态。
	 7.用户退出当前Activity：系统先调用onPause方法，然后调用onStop方法，最后调用onDestory方法，结束当前Activity。
	--------------------------
	 Activity创建后被调用的第一个方法，一般用来完成所有的静态设置，如中创建views，ListView绑定数据，如调用：setContentView(int layoutResID)
	 findViewById(int id)（ps：如果是fragment中的控件，该方法返回null，fragment是在该方法之后执行的）
	 Bundle参数可以用来恢复之前保持的状态数据，横竖屏切换的时候则会用到。
	 该方法调用之后一般会自动调用onStart()方法
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        boolean showMenu;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        isFinish = false;
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), FILE_BROWSER_OPEN_CODE).versionName;
        } catch (Exception e) {
        }
		//
        initEnv();
        //加入表
		this.mTabHost = (TabHost) findViewById(R.id.tabs);
        this.mTabHost.initTabHost(this);
        this.mTabHost.addTab("");
        
		this.mEditText = this.mTabHost.getCurrentEditText();
        //mEditText.setTextIsSelectable(true);
		/*

		最近项目要求屏蔽EditText 长按出来的ActionMode菜单，但是要保留选择文本功能。
		这个屏蔽百度会出现各种方法，这里说一下我的思路：
		1.屏蔽百度可知setCustomSelectionActionModeCallback即可,*/
		mEditText.setCustomSelectionActionModeCallback(new Callback() { 
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) { 
					return false;
				} 
				@Override
				public void onDestroyActionMode(ActionMode mode) {  
				} 
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) { 
					//这里可以添加自己的菜单选项（前提是要返回true的）
					
					
					/*try {
						Field mEditor = TextView.class.getDeclaredField("mEditor");//找到 TextView中的成员变量mEditor  
						mEditor.setAccessible(true); 
						Object object= mEditor.get(mEditText);//根具持有对象拿到mEditor变量里的值 （android.widget.Editor类的实例）
                        //--------------------显示选择控制工具------------------------------//
						Class mClass=Class.forName("android.widget.Editor");//拿到隐藏类Editor； 
						Method  method=mClass.getDeclaredMethod("getSelectionController");//取得方法  getSelectionController 
						method.setAccessible(true);//取消访问私有方法的合法性检查     
						Object resultobject=method.invoke(object);//调用方法，返回SelectionModifierCursorController类的实例

						Method show=resultobject.getClass().getDeclaredMethod("show");//查找 SelectionModifierCursorController类中的show方法
						show.invoke(resultobject);//执行SelectionModifierCursorController类的实例的show方法
						mEditText.setHasTransientState(true); 

						//--------------------忽略最后一次TouchUP事件-----------------------------------------------//
						Field  mSelectionActionMode=mClass.getDeclaredField("mDiscardNextActionUp");//查找变量Editor类中mDiscardNextActionUp
						mSelectionActionMode.setAccessible(true); 
						mSelectionActionMode.set(object,true);//赋值为true 

					} catch (Exception e) { 
						e.printStackTrace();
					}*/
					return true;//return false 隐藏actionMod菜单*/
					//return onCreateActionMode(mode,menu);
				} 
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) { 
					return false;
				}
			});
		//查找布局
		this.findLayout = (LinearLayout) findViewById(R.id.findlinearLayout);
        //替换布局
		this.replaceLayout = (LinearLayout) findViewById(R.id.replace_linearLayout);
        //查找按钮
		this.replaceShowButton = (Button) findViewById(R.id.show_replace_button);
        //查找输入框
		this.findEditText = (EditText) findViewById(R.id.find_editText);
        //替换输入框
		this.replaceEditText = (EditText) findViewById(R.id.replace_editText);
        
		this.previewBtn = (ImageButton) findViewById(R.id.preview);
        this.toolbar = (HorizontalScrollView) findViewById(R.id.toolbar);
        this.last_edit_back = (ImageButton) findViewById(R.id.last_edit_back);
        this.last_edit_forward = (ImageButton) findViewById(R.id.last_edit_forward);
        this.undo_can_drawable = getResources().getDrawable(R.drawable.undo_sel2);
        this.undo_no_drawable = getResources().getDrawable(R.drawable.undo_no2);
        this.redo_can_drawable = getResources().getDrawable(R.drawable.redo_sel2);
        this.redo_no_drawable = getResources().getDrawable(R.drawable.redo_no2);
        this.last_edit_back_d = getResources().getDrawable(R.drawable.back_edit_location_d2);
        this.last_edit_back_s = getResources().getDrawable(R.drawable.back_edit_location_s2);
        this.last_edit_forward_d = getResources().getDrawable(R.drawable.forward_edit_location_d2);
        this.last_edit_forward_s = getResources().getDrawable(R.drawable.forward_edit_location_s2);
        this.findEditText.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI/*268435462*/);
        this.replaceEditText.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI/*268435462*/);
        this.mMenu = new QSMenu(this);
        this.mMenu.setOnMenuItemSelectedListener(this.mOnMenuItemSelectedListener);
        if (Build.VERSION.SDK_INT > 10) {
            showMenu = true;
        } else {
            showMenu = false;
        }
		//菜单按钮及事件
        ImageButton menuButton = (ImageButton) findViewById(R.id.menu);
        if (showMenu) {
            menuButton.setVisibility(FILE_BROWSER_OPEN_CODE);
            menuButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						QSEditor.this.closeOptionsMenu();
						QSEditor.this.openOptionsMenu();
						QSEditor.this.mMenu.show();
					}
				});
        }
        this.mPref = PreferenceManager.getDefaultSharedPreferences(this);
        this.mAsyncSearch = new AsyncSearch();
        init_highlight();
        this.mTabHost.setOnTextChangedListener(new QSEditText.OnTextChangedListener() {
				public void onTextChanged(QSEditText editText) {
					QSEditor.this.onEditLocationChanged(editText);
					if (editText.canUndo()) {
						QSEditor.this.undoBtn.setImageDrawable(QSEditor.this.undo_can_drawable);
					} else {
						QSEditor.this.undoBtn.setImageDrawable(QSEditor.this.undo_no_drawable);
					}
					if (editText.canRedo()) {
						QSEditor.this.redoBtn.setImageDrawable(QSEditor.this.redo_can_drawable);
					} else {
						QSEditor.this.redoBtn.setImageDrawable(QSEditor.this.redo_no_drawable);
					}
				}
			});
			//切换事件
        this.mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
				public void onTabChanged(int tabId) {
					QSEditor.this.mEditText = QSEditor.this.mTabHost.getCurrentEditText();
					QSEditor.this.switchPreviewButton(Highlight.getNameByExt(QSEditor.this.mEditText.getCurrentFileExt()));
				}
			});
		//标签关闭事件
        this.mTabHost.setOnTabCloseListener(new TabHost.OnTabCloseListener() {
				public void onTabClose(final int action, final int startIndex, final int curIndex)
				{
					QSEditor.this.saveConfirm(new Runnable()
						{
							public void run()
							{
								int lastId = QSEditor.this.mTabHost.closeTab(curIndex);
								if ((!QSEditor.isFinish) || (lastId != -1))
								{
									if (action != 0)
										QSEditor.this.mTabHost.iterCloseTab(action, startIndex, curIndex);
								}
								else
									QSEditor.this.finish();
							}
						});
				}
			});
				
        this.last_edit_back.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (QSEditor.this.mEditText.isCanBackEditLocation()) {
						QSEditor.this.mEditText.gotoBackEditLocation();
						QSEditor.this.onEditLocationChanged(QSEditor.this.mEditText);
					}
				}
			});
        this.last_edit_forward.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (QSEditor.this.mEditText.isCanForwardEditLocation()) {
						QSEditor.this.mEditText.gotoForwardEditLocation();
						QSEditor.this.onEditLocationChanged(QSEditor.this.mEditText);
					}
				}
			});
        this.mSymbolGrid = (SymbolGrid) findViewById(R.id.symbolGrid1);
        this.mSymbolGrid.setClickListener(new SymbolGrid.OnSymbolClickListener() {
				public void OnClick(String symbol) {
					QSEditor.this.insert_text(symbol);
				}
			});
        ((ImageButton) findViewById(R.id.symbol)).setOnClickListener(new  View.OnClickListener() {
				public void onClick(View v) {
					QSEditor.this.mSymbolGrid.setVisibility(QSEditor.FILE_BROWSER_OPEN_CODE);
				}
			});
        bindEvent();
        if (!version.equals(this.mPref.getString("version", "-1"))) {
            Help.showChangesLog(this);
            this.mPref.edit().putString("version", version).commit();
        }
        onNewIntent(getIntent());
    }
    
	/*
	 //声明方法
	 .method public onCreate(Landroid/os/Bundle;)V
	 //指定局部变量的总个数
	 .locals 10
	 //指定方法参的数(p命名方法)
	 .param p1, "savedInstanceState"    # Landroid/os/Bundle;
	 //代码开始
	 .prologue
	 
	 //数据定义.将0x10000006数值赋值给v9寄存器
	 const v9, 0x10000006
     //将0x0数值符号扩展为32位赋值给v6寄存器
	 const/4 v6, 0x0
	 
     //指定该处指令在源代码中的行号
	 .line 147
	 //调用实例类方法
	 invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V

	 .line 148
	 const v5, 0x7f030007

	 //调用实例虚拟方法
	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->setContentView(I)V

	 .line 149
	 //设置boolean静态字段的值
	 sput-boolean v6, Lcom/jecelyin/editor/JecEditor;->isFinish:Z

	 .line 152
	 //try异常代码块开始
	 :try_start_0
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getPackageManager()Landroid/content/pm/PackageManager;

	 //将上一个invoke类型指令操作的对象结果赋给v5寄存器
	 move-result-object v5

	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getPackageName()Ljava/lang/String;

	 move-result-object v7

	 const/4 v8, 0x0

	 invoke-virtual {v5, v7, v8}, Landroid/content/pm/PackageManager;->getPackageInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;

	 move-result-object v1

	 .line 153
	 .local v1, "packageInfo":Landroid/content/pm/PackageInfo;
	 //读取object静态字段的值(v命名方法)
	 iget-object v5, v1, Landroid/content/pm/PackageInfo;->versionName:Ljava/lang/String;

	 sput-object v5, Lcom/jecelyin/editor/JecEditor;->version:Ljava/lang/String;
	 //try异常代码块结束
	 :try_end_0
	 //指定处理到的异常类型与catch标号
	 .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

	 .line 159
	 //变量结束
	 .end local v1    # "packageInfo":Landroid/content/pm/PackageInfo;
	 //无条件跳转开始处标号
	 :goto_0
	 //调用实例直接方法
	 invoke-direct {p0}, Lcom/jecelyin/editor/JecEditor;->initEnv()V

	 .line 161
	 const v5, 0x7f060128

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 //实例操作.将v5寄存器中的对象引用转换成指定类型
	 check-cast v5, Lcom/jecelyin/widget/TabHost;
     
	 //设置object实例字段的值
	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mTabHost:Lcom/jecelyin/widget/TabHost;

	 .line 162
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mTabHost:Lcom/jecelyin/widget/TabHost;

	 invoke-virtual {v5, p0}, Lcom/jecelyin/widget/TabHost;->initTabHost(Lcom/jecelyin/editor/JecEditor;)V

	 .line 163
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mTabHost:Lcom/jecelyin/widget/TabHost;

	 const-string v7, ""

	 invoke-virtual {v5, v7}, Lcom/jecelyin/widget/TabHost;->addTab(Ljava/lang/String;)V

	 .line 164
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mTabHost:Lcom/jecelyin/widget/TabHost;

	 invoke-virtual {v5}, Lcom/jecelyin/widget/TabHost;->getCurrentEditText()Lcom/jecelyin/widget/JecEditText;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mEditText:Lcom/jecelyin/widget/JecEditText;

	 .line 165
	 const v5, 0x7f060138

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Landroid/widget/LinearLayout;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->findLayout:Landroid/widget/LinearLayout;

	 .line 166
	 const v5, 0x7f06013d

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Landroid/widget/LinearLayout;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->replaceLayout:Landroid/widget/LinearLayout;

	 .line 167
	 const v5, 0x7f06013c

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Landroid/widget/Button;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->replaceShowButton:Landroid/widget/Button;

	 .line 168
	 const v5, 0x7f060139

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Landroid/widget/EditText;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->findEditText:Landroid/widget/EditText;

	 .line 169
	 const v5, 0x7f06013e

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Landroid/widget/EditText;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->replaceEditText:Landroid/widget/EditText;

	 .line 170
	 const v5, 0x7f060132

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Landroid/widget/ImageButton;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->previewBtn:Landroid/widget/ImageButton;

	 .line 171
	 const v5, 0x7f06012c

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Landroid/widget/HorizontalScrollView;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->toolbar:Landroid/widget/HorizontalScrollView;

	 .line 172
	 const v5, 0x7f060134

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Landroid/widget/ImageButton;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->last_edit_back:Landroid/widget/ImageButton;

	 .line 173
	 const v5, 0x7f060135

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Landroid/widget/ImageButton;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->last_edit_forward:Landroid/widget/ImageButton;

	 .line 174
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getResources()Landroid/content/res/Resources;

	 move-result-object v5

	 const v7, 0x7f02005e

	 invoke-virtual {v5, v7}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->undo_can_drawable:Landroid/graphics/drawable/Drawable;

	 .line 175
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getResources()Landroid/content/res/Resources;

	 move-result-object v5

	 const v7, 0x7f02005c

	 invoke-virtual {v5, v7}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->undo_no_drawable:Landroid/graphics/drawable/Drawable;

	 .line 176
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getResources()Landroid/content/res/Resources;

	 move-result-object v5

	 const v7, 0x7f02003e

	 invoke-virtual {v5, v7}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->redo_can_drawable:Landroid/graphics/drawable/Drawable;

	 .line 177
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getResources()Landroid/content/res/Resources;

	 move-result-object v5

	 const v7, 0x7f02003c

	 invoke-virtual {v5, v7}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->redo_no_drawable:Landroid/graphics/drawable/Drawable;

	 .line 179
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getResources()Landroid/content/res/Resources;

	 move-result-object v5

	 const v7, 0x7f020004

	 invoke-virtual {v5, v7}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->last_edit_back_d:Landroid/graphics/drawable/Drawable;

	 .line 180
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getResources()Landroid/content/res/Resources;

	 move-result-object v5

	 const v7, 0x7f020006

	 invoke-virtual {v5, v7}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->last_edit_back_s:Landroid/graphics/drawable/Drawable;

	 .line 181
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getResources()Landroid/content/res/Resources;

	 move-result-object v5

	 const v7, 0x7f020018

	 invoke-virtual {v5, v7}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->last_edit_forward_d:Landroid/graphics/drawable/Drawable;

	 .line 182
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getResources()Landroid/content/res/Resources;

	 move-result-object v5

	 const v7, 0x7f02001a

	 invoke-virtual {v5, v7}, Landroid/content/res/Resources;->getDrawable(I)Landroid/graphics/drawable/Drawable;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->last_edit_forward_s:Landroid/graphics/drawable/Drawable;

	 .line 184
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->findEditText:Landroid/widget/EditText;

	 invoke-virtual {v5, v9}, Landroid/widget/EditText;->setImeOptions(I)V

	 .line 185
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->replaceEditText:Landroid/widget/EditText;

	 invoke-virtual {v5, v9}, Landroid/widget/EditText;->setImeOptions(I)V

	 .line 193
	 new-instance v5, Lcom/jecelyin/widget/JecMenu;

	 invoke-direct {v5, p0}, Lcom/jecelyin/widget/JecMenu;-><init>(Landroid/content/Context;)V

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mMenu:Lcom/jecelyin/widget/JecMenu;

	 .line 194
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mMenu:Lcom/jecelyin/widget/JecMenu;

	 iget-object v7, p0, Lcom/jecelyin/editor/JecEditor;->mOnMenuItemSelectedListener:Lcom/jecelyin/widget/JecMenu$OnMenuItemSelectedListener;

	 invoke-virtual {v5, v7}, Lcom/jecelyin/widget/JecMenu;->setOnMenuItemSelectedListener(Lcom/jecelyin/widget/JecMenu$OnMenuItemSelectedListener;)V

	 .line 196
	 sget v5, Landroid/os/Build$VERSION;->SDK_INT:I

	 const/16 v7, 0xa

	 if-le v5, v7, :cond_2

	 const/4 v3, 0x1

	 .line 197
	 .local v3, "showMenu":Z
	 :goto_1
	 const v5, 0x7f060137

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v0

	 check-cast v0, Landroid/widget/ImageButton;

	 .line 198
	 .local v0, "menuButton":Landroid/widget/ImageButton;
	 if-eqz v3, :cond_0

	 .line 200
	 invoke-virtual {v0, v6}, Landroid/widget/ImageButton;->setVisibility(I)V

	 .line 201
	 new-instance v5, Lcom/jecelyin/editor/JecEditor$8;

	 invoke-direct {v5, p0}, Lcom/jecelyin/editor/JecEditor$8;-><init>(Lcom/jecelyin/editor/JecEditor;)V

	 invoke-virtual {v0, v5}, Landroid/widget/ImageButton;->setOnClickListener(Landroid/view/View$OnClickListener;)V

	 .line 213
	 :cond_0
	 invoke-static {p0}, Landroid/preference/PreferenceManager;->getDefaultSharedPreferences(Landroid/content/Context;)Landroid/content/SharedPreferences;

	 move-result-object v5

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mPref:Landroid/content/SharedPreferences;

	 .line 216
	 new-instance v5, Lcom/jecelyin/editor/AsyncSearch;

	 invoke-direct {v5}, Lcom/jecelyin/editor/AsyncSearch;-><init>()V

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mAsyncSearch:Lcom/jecelyin/editor/AsyncSearch;

	 .line 219
	 invoke-direct {p0}, Lcom/jecelyin/editor/JecEditor;->init_highlight()V

	 .line 222
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mTabHost:Lcom/jecelyin/widget/TabHost;

	 new-instance v6, Lcom/jecelyin/editor/JecEditor$9;

	 invoke-direct {v6, p0}, Lcom/jecelyin/editor/JecEditor$9;-><init>(Lcom/jecelyin/editor/JecEditor;)V

	 invoke-virtual {v5, v6}, Lcom/jecelyin/widget/TabHost;->setOnTextChangedListener(Lcom/jecelyin/widget/JecEditText$OnTextChangedListener;)V

	 .line 246
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mTabHost:Lcom/jecelyin/widget/TabHost;

	 new-instance v6, Lcom/jecelyin/editor/JecEditor$10;

	 invoke-direct {v6, p0}, Lcom/jecelyin/editor/JecEditor$10;-><init>(Lcom/jecelyin/editor/JecEditor;)V

	 invoke-virtual {v5, v6}, Lcom/jecelyin/widget/TabHost;->setOnTabChangedListener(Lcom/jecelyin/widget/TabHost$OnTabChangeListener;)V

	 .line 257
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mTabHost:Lcom/jecelyin/widget/TabHost;

	 new-instance v6, Lcom/jecelyin/editor/JecEditor$11;

	 invoke-direct {v6, p0}, Lcom/jecelyin/editor/JecEditor$11;-><init>(Lcom/jecelyin/editor/JecEditor;)V

	 invoke-virtual {v5, v6}, Lcom/jecelyin/widget/TabHost;->setOnTabCloseListener(Lcom/jecelyin/widget/TabHost$OnTabCloseListener;)V

	 .line 280
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->last_edit_back:Landroid/widget/ImageButton;

	 new-instance v6, Lcom/jecelyin/editor/JecEditor$12;

	 invoke-direct {v6, p0}, Lcom/jecelyin/editor/JecEditor$12;-><init>(Lcom/jecelyin/editor/JecEditor;)V

	 invoke-virtual {v5, v6}, Landroid/widget/ImageButton;->setOnClickListener(Landroid/view/View$OnClickListener;)V

	 .line 295
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->last_edit_forward:Landroid/widget/ImageButton;

	 new-instance v6, Lcom/jecelyin/editor/JecEditor$13;

	 invoke-direct {v6, p0}, Lcom/jecelyin/editor/JecEditor$13;-><init>(Lcom/jecelyin/editor/JecEditor;)V

	 invoke-virtual {v5, v6}, Landroid/widget/ImageButton;->setOnClickListener(Landroid/view/View$OnClickListener;)V

	 .line 312
	 const v5, 0x7f060141

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v5

	 check-cast v5, Lcom/jecelyin/widget/SymbolGrid;

	 iput-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mSymbolGrid:Lcom/jecelyin/widget/SymbolGrid;

	 .line 314
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mSymbolGrid:Lcom/jecelyin/widget/SymbolGrid;

	 new-instance v6, Lcom/jecelyin/editor/JecEditor$14;

	 invoke-direct {v6, p0}, Lcom/jecelyin/editor/JecEditor$14;-><init>(Lcom/jecelyin/editor/JecEditor;)V

	 invoke-virtual {v5, v6}, Lcom/jecelyin/widget/SymbolGrid;->setClickListener(Lcom/jecelyin/widget/SymbolGrid$OnSymbolClickListener;)V

	 .line 330
	 const v5, 0x7f060133

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->findViewById(I)Landroid/view/View;

	 move-result-object v4

	 check-cast v4, Landroid/widget/ImageButton;

	 .line 331
	 .local v4, "symbolButton":Landroid/widget/ImageButton;
	 new-instance v5, Lcom/jecelyin/editor/JecEditor$15;

	 invoke-direct {v5, p0}, Lcom/jecelyin/editor/JecEditor$15;-><init>(Lcom/jecelyin/editor/JecEditor;)V

	 invoke-virtual {v4, v5}, Landroid/widget/ImageButton;->setOnClickListener(Landroid/view/View$OnClickListener;)V

	 .line 340
	 invoke-direct {p0}, Lcom/jecelyin/editor/JecEditor;->bindEvent()V

	 .line 343
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mPref:Landroid/content/SharedPreferences;

	 const-string v6, "version"

	 const-string v7, "-1"

	 invoke-interface {v5, v6, v7}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

	 move-result-object v2

	 .line 344
	 .local v2, "prefVer":Ljava/lang/String;
	 sget-object v5, Lcom/jecelyin/editor/JecEditor;->version:Ljava/lang/String;

	 invoke-virtual {v5, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

	 move-result v5

	 if-nez v5, :cond_1

	 .line 346
	 invoke-static {p0}, Lcom/jecelyin/editor/Help;->showChangesLog(Landroid/content/Context;)V

	 .line 347
	 iget-object v5, p0, Lcom/jecelyin/editor/JecEditor;->mPref:Landroid/content/SharedPreferences;

	 invoke-interface {v5}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

	 move-result-object v5

	 const-string v6, "version"

	 sget-object v7, Lcom/jecelyin/editor/JecEditor;->version:Ljava/lang/String;

	 invoke-interface {v5, v6, v7}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

	 move-result-object v5

	 invoke-interface {v5}, Landroid/content/SharedPreferences$Editor;->commit()Z

	 .line 349
	 :cond_1
	 invoke-virtual {p0}, Lcom/jecelyin/editor/JecEditor;->getIntent()Landroid/content/Intent;

	 move-result-object v5

	 invoke-virtual {p0, v5}, Lcom/jecelyin/editor/JecEditor;->onNewIntent(Landroid/content/Intent;)V

	 .line 350
	 return-void

	 .end local v0    # "menuButton":Landroid/widget/ImageButton;
	 .end local v2    # "prefVer":Ljava/lang/String;
	 .end local v3    # "showMenu":Z
	 .end local v4    # "symbolButton":Landroid/widget/ImageButton;
	 :cond_2
	 move v3, v6

	 .line 196
	 goto/16 :goto_1

	 .line 154
	 :catch_0
	 move-exception v5

	 goto/16 :goto_0
	 .end method
	*/
	private void initEnv() {
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                TEMP_PATH = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/.TextEditor").toString();
            } else {
                TEMP_PATH = new StringBuilder(String.valueOf(getFilesDir().getAbsolutePath())).append("/.TextEditor").toString();
            }
            File temp = new File(TEMP_PATH);
            if (!(temp.isDirectory() || temp.mkdir())) {
                alert(R.string.can_not_create_temp_path);
            }
            /*String synfilestr = TEMP_PATH + "/version";
            if (new File(synfilestr).isFile()) {
                if (!SYNTAX_SIGN.equals(Highlight.readFile(synfilestr, "utf-8"))) {
                    if (unpackSyntax()) {
                        FileUtil.writeFile(synfilestr, SYNTAX_SIGN, "utf-8", false);
                    } else {
                        alert(R.string.can_not_create_synfile);
                    }
                }
            } else if (unpackSyntax()) {
                FileUtil.writeFile(synfilestr, SYNTAX_SIGN, "utf-8", false);
            } else {
                alert(R.string.can_not_create_synfile);
            }*/
            Highlight.init();
        } catch (Exception e) {
            printException(e);
        }
    }
	
	
	//initEnv()调用
	public void alert(int msg) {
        new Builder(this).setMessage(msg).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					QSEditor.this.finish();
				}
			}).show();
    }
	//initEnv()调用
	public boolean unpackSyntax() {
        try {
            ZipInputStream zin = new ZipInputStream(getAssets().open("syntax.zip"));
            while (true) {
                ZipEntry ze = zin.getNextEntry();
                if (ze == null) {
                    zin.close();
                    return true;
                }
                String name = ze.getName();
                if (ze.isDirectory()) {
                    File file = new File(TEMP_PATH + File.separator + name);
                    if (!(file.exists() || file.mkdir())) {
                        return false;
                    }
                }
                FileOutputStream fout = new FileOutputStream(TEMP_PATH + File.separator + name);
                byte[] buf = new byte[4096];
                while (true) {
                    int len = zin.read(buf);
                    if (len <= 0) {
                        break;
                    }
                    fout.write(buf, FILE_BROWSER_OPEN_CODE, len);
                }
                buf = null;
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	//onCreate调用
	//load_options调用
	private void init_highlight() {
        int limitSize;
        Highlight.setEnabled(this.mPref.getBoolean("enable_highlight", true));
        try {
            limitSize = Integer.valueOf(this.mPref.getString("highlight_limit", Integer.toString(this.MAX_HIGHLIGHT_FILESIZE))).intValue();
        } catch (Exception e) {
            limitSize = this.MAX_HIGHLIGHT_FILESIZE;
            printException(e);
        }
        Highlight.setLimitFileSize(limitSize);
    }
	//onCreate调用
	private void bindEvent() {
		//打开事件
        ((ImageButton) findViewById(R.id.btn_open)).setOnClickListener(this.onBtnOpenClicked);
        //保存事件
		((ImageButton) findViewById(R.id.btn_save)).setOnClickListener(this.onBtnSaveClicked);
        //后退事件
		bindUndoButtonClickEvent();
        //前进事件
		bindRedoButtonClickEvent();
        //查找
		this.replaceShowButton.setOnClickListener(this.replaceShowClickListener);
        ImageButton findBack = (ImageButton) findViewById(R.id.find_back_imageButton);
        ((ImageButton) findViewById(R.id.find_next_imageButton)).setOnClickListener(this.findButtonClickListener);
        findBack.setOnClickListener(this.findButtonClickListener);
        //替换
		Button replaceAllButton = (Button) findViewById(R.id.replace_all_button);
        ((Button) findViewById(R.id.replace_button)).setOnClickListener(this.replaceClickListener);
        replaceAllButton.setOnClickListener(this.replaceClickListener);
        this.previewBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if ("".equals(QSEditor.this.mEditText.getPath()) || QSEditor.this.mEditText.isTextChanged()) {
						Toast.makeText(QSEditor.this, R.string.preview_msg, QSEditor.FILE_BROWSER_SAVEAS_CODE).show();
						return;
					}
					try {
						Uri uri = Uri.fromFile(new File(QSEditor.this.mEditText.getPath()));
						Intent intent = new Intent("android.intent.action.VIEW");
						intent.setDataAndType(uri, "text/html");
						QSEditor.this.startActivity(intent);
					} catch (Exception e) {
					}
				}
			});
        ((ImageButton) findViewById(R.id.color)).setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					new ColorPicker(QSEditor.this, new ColorListener(), "edittext", QSEditor.this.getString(R.string.insert_color), -16711936).show();
				}
			});
    }
    //打开
	//bindEvent()调用
	private View.OnClickListener onBtnOpenClicked = new View.OnClickListener()
	{
		public void onClick(View paramView)
		{
			QSEditor.this.openFileBrowser(0, "");
		}
	};
	private void openFileBrowser(int mode, String filename) {
        openFileBrowser(mode, filename, new Runnable() {
				public void run() {
				}
			});
    }

    private void openFileBrowser(int mode, String filename, Runnable mRunnable) {
        this.fileBrowserCallbackRunnable = mRunnable;
        Intent intent = new Intent();
        intent.putExtra("filename", filename);
        intent.putExtra("mode", mode);
        intent.putExtra("isRoot", isRoot);
        intent.setClass(this, FileBrowser.class);
        //调用这个activity，关闭后可以接收到返回值
		//这里采用startActivityForResult来做跳转，此处的0为一个依据，可以写其他的值，但一定要>=0
		//2.重写onActivityResult方法，用来接收B回传的数据。
		//3.在B中回传数据时采用setResult方法，并且之后要调用finish方法。
		startActivityForResult(intent, mode);
    }
	
	
	 private Runnable fileBrowserCallbackRunnable = new Runnable()
	 {
	 public void run()
	 {
	 }
	 };
	 
	 //保存
	//bindEvent()调用
	private View.OnClickListener onBtnSaveClicked = new View.OnClickListener()
	{
		public void onClick(View paramView)
		{
			if (!"".equals(QSEditor.this.mEditText.getPath()))
				QSEditor.this.save();
			else
				QSEditor.this.openFileBrowser(1, "Untitled.txt");
		}
	};
	//
	private void save()
	{
		save(this.mEditText.getEncoding(), this.mEditText.getLineBreak());
	}
	private void save(String encoding, int linebreak) {
        //比较基本数据类型，如果两个值相同，则结果为true
		if (!"".equals(this.mEditText.getPath()) && !isLoading) {
            boolean ok;
            String content = this.mEditText.getString();
            if (linebreak == 2) {
                //即把源字符串中的某一字符或字符串全部换成指定的字符或字符串
				content = content.replaceAll("\r\n|\r", "\n");
            } else if (linebreak == 3) {
                content = content.replaceAll("\r\n|\r", "\r");
            }
            if ("".equals(encoding)) {
                encoding = "utf-8";
            }
            String failMsg = "";
            try {
                ok = FileUtil.writeFile(this.mEditText.getPath(), content, encoding, isRoot);
            } catch (Exception e) {
                failMsg = e.getMessage();
                ok = false;
            }
            if (ok) {
                this.mEditText.setTextFinger();
                this.mTabHost.setTabStatus(false);
                Toast.makeText(this, R.string.save_succ, FILE_BROWSER_SAVEAS_CODE).show();
                return;
            }
            Toast.makeText(this, getString(R.string.save_failed) + failMsg, FILE_BROWSER_SAVEAS_CODE).show();
        }
    }
	//后退事件
	//bindEvent()调用
	private void bindUndoButtonClickEvent()
	{
		this.undoBtn = ((ImageButton)findViewById(2131099952));
		this.undoBtn.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View paramView)
				{
					QSEditor.this.mEditText.unDo();
				}
			});
	}
	//bindEvent()调用
	private void bindRedoButtonClickEvent()
	{
		this.redoBtn = ((ImageButton)findViewById(2131099953));
		this.redoBtn.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View paramView)
				{
					QSEditor.this.mEditText.reDo();
				}
			});
	}
	//bindEvent()调用
	private View.OnClickListener replaceShowClickListener = new View.OnClickListener()
	{
		public void onClick(View paramView)
		{
			QSEditor.this.replaceLayout.setVisibility(0);
			paramView.setVisibility(8);
			QSEditor.this.replaceEditText.requestFocus();
		}
	};
	//bindEvent()调用
	private View.OnClickListener findButtonClickListener = new View.OnClickListener()
	{
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.find_next_imageButton /*2131099962*/:
					QSEditor.this.find("next");
					break;
				case R.id.find_back_imageButton /*2131099963*/:
					QSEditor.this.find("back");
					break;
				default:
				break;
			}
		}
	};
	//bindEvent()调用
	private View.OnClickListener replaceClickListener = new View.OnClickListener()
	{
		public void onClick(View v) {
			String searchText = QSEditor.this.findEditText.getText().toString();
			String replaceText = QSEditor.this.replaceEditText.getText().toString();
			if (!"".equals(searchText)) {
				switch (v.getId()) {
					case R.id.replace_button /*2131099967*/:
						QSEditor.this.mAsyncSearch.replace(replaceText);
						break;
					case R.id.replace_all_button /*2131099968*/:
						QSEditor.this.mAsyncSearch.replaceAll(searchText, replaceText, QSEditor.this);
						break;
					default:
					break;
					
				}
			}
		}
	};
	/*
	 利用已有的Acivity去处理别的Intent时，你就可以利用onNewIntent来处理。在onNewIntent里面就会获得新的Intent.
	 在该Activity的实例已经存在于Task和Back stack中(或者通俗的说可以通过按返回键返回到该Activity )时,当使用intent来再次启动该Activity的时候,如果此次启动不创建该Activity的新实例,则系统会调用原有实例的onNewIntent()方法来处理此intent.

	 　　且在下面情况下系统不会创建该Activity的新实例:

	 　　1,如果该Activity在Manifest中的android:launchMode定义为singleTask或者singleInstance.

	 　　2,如果该Activity在Manifest中的android:launchMode定义为singleTop且该实例位于Back stack的栈顶.

	 　　3,如果该Activity在Manifest中的android:launchMode定义为singleTop,且上述intent包含Intent.FLAG_ACTIVITY_CLEAR_TOP标志.

	 　　4,如果上述intent中包含 Intent.FLAG_ACTIVITY_CLEAR_TOP 标志和且包含 Intent.FLAG_ACTIVITY_SINGLE_TOP 标志.

	 　　5,如果上述intent中包含 Intent.FLAG_ACTIVITY_SINGLE_TOP 标志且该实例位于Back stack的栈顶.

	 　　上述情况满足其一,则系统将不会创建该Activity的新实例.

	 　　根据现有实例所处的状态不同onNewIntent()方法的调用时机也不同,总的说如果系统调用onNewIntent()方法则系统会在onResume()方法执行之前调用它
	*/
	protected void onNewIntent(Intent mIntent) {
        if (!isLoading) {
			//&&可以用作逻辑与的运算符，表示逻辑与（and），当运算符两边的表达式的结果都为true时，整个运算结果才为true，否则，只要有一方为false，则结果为false。
			//&&还具有短路的功能，即如果第一个表达式为false，则不再计算第二个表达式，例如，对于if(str != null && !str.equals(“”))表达式，当str为null时，后面的表达式不会执行，所以不会出现NullPointerException
			//||可以作逻辑或运算符，表示逻辑或（or），当运算符有一边为true时，整个运算结果为true！
            if (mIntent == null || !("android.intent.action.VIEW".equals(mIntent.getAction()) || "android.intent.action.EDIT".equals(mIntent.getAction()))) {
                Bundle extras;
                if (mIntent != null && "android.intent.action.SEND".equals(mIntent.getAction()) && mIntent.getExtras() != null) {
                    extras = mIntent.getExtras();
                    CharSequence text = extras.getCharSequence("android.intent.extra.TEXT");
                    if (text != null) {
                        this.mTabHost.addTab("");
                        this.mEditText.setText2(text.toString());
                        return;
                    }
                    Object stream = extras.get("android.intent.extra.STREAM");
                    if (stream != null && (stream instanceof Uri)) {
                        readFileToEditText(((Uri) stream).getPath());
                    }
                } else if (mIntent == null || !ACTION_EDIT_SCRIPT.equals(mIntent.getAction()) || mIntent.getExtras() == null) {
                    if (this.mPref.getBoolean("open_last_file", false)) {
                        Map<String, ?> map = getSharedPreferences(PREF_LAST_FILE, FILE_BROWSER_OPEN_CODE).getAll();
                        if (map.size() > 0) {
                            for (Entry<String, ?> entry : map.entrySet()) {
                                Object val = entry.getValue();
                                if (val instanceof String) {
                                    this.mLastFiles.add((String) val);
                                }
                            }
                            loadLastOpenFiles();
                        }
                    }
                } else {
                    extras = mIntent.getExtras();
                    String path = extras.getString(EXTRA_SCRIPT_PATH);
                    CharSequence contents = extras.getCharSequence(EXTRA_SCRIPT_CONTENT);
                    if (contents != null) {
                        this.mTabHost.addTab("");
                        this.mEditText.setText2(contents);
                    } else if (path != null) {
                        readFileToEditText(path);
                    }
                }
            } else if (mIntent.getScheme().equals("content")) {
                try {
                    InputStream attachment = getContentResolver().openInputStream(mIntent.getData());
                    BufferedReader br = new BufferedReader(new InputStreamReader(attachment), 16384);
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String text2 = br.readLine();
                        if (text2 == null) {
                            attachment.close();
                            br.close();
                            this.mTabHost.addTab("");
                            this.mEditText.setText2(sb.toString());
                            sb.setLength(FILE_BROWSER_OPEN_CODE);
                            return;
                        }
                        sb.append(text2).append("\n");
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), FILE_BROWSER_SAVEAS_CODE).show();
                }
            } else if (mIntent.getScheme().equals("file")) {
                Uri mUri = mIntent.getData();
                String open_path = mUri != null ? mUri.getPath() : "";
                if (!"".equals(open_path) && open_path != null) {
                    readFileToEditText(open_path);
                }
            }
        }
    }
	
	
	
	//onCreate调用
	public void saveConfirm(final Runnable mRunnable) {
        if (this.mEditText.isTextChanged()) {
            String filename = "".equals(this.mEditText.getPath()) ? this.mEditText.getTitle() : this.mEditText.getPath();
            String string = getString(R.string.save_changes_to);
            Object[] objArr = new Object[FILE_BROWSER_SAVEAS_CODE];
            objArr[FILE_BROWSER_OPEN_CODE] = filename;
            new Builder(this).setTitle(R.string.save_changes)
			.setMessage(String.format(string, objArr))
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which) {
						if ("".equals(QSEditor.this.mEditText.getPath())) {
							QSEditor.this.openFileBrowser(QSEditor.FILE_BROWSER_SAVEAS_CODE, QSEditor.this.getString(R.string.new_filename), mRunnable);
							return;
						}
						QSEditor.this.save();
						dialog.dismiss();
						mRunnable.run();
				    }
				})
				.setNeutralButton(R.string.no, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramDialogInterface, int paramInt)
					{
						paramDialogInterface.dismiss();
						mRunnable.run();
					}
				})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						QSEditor.isFinish = false;
					}
				}).show();
            return;
        }
        mRunnable.run();
    }
	
	//onCreate调用
	private QSMenu.OnMenuItemSelectedListener mOnMenuItemSelectedListener = new QSMenu.OnMenuItemSelectedListener()
	{
		public boolean onMenuItemSelected(int id, View v) {
			switch (id) {
				case R.id.menu_reopen /*重新打开2131099899*/:
					HistoryList historyList = new HistoryList(QSEditor.this);
					break;
				case R.id.menu_pipe /*打开方式2131099900*/:
					String ext = QSEditor.this.mEditText.getCurrentFileExt();
					final String[] items = ("py".equals(ext) || "pl".equals(ext) || "lua".equals(ext) || "sh".equals(ext) || "js".equals(ext) || "tcl".equals(ext)) ? new String[]{QSEditor.this.getString(R.string.view), QSEditor.this.getString(R.string.share), QSEditor.this.getString(R.string.run_in_sl4a_terminal), QSEditor.this.getString(R.string.run_in_sl4a_background)} : new String[]{QSEditor.this.getString(R.string.view), QSEditor.this.getString(R.string.share)};
					Builder builder = new Builder(QSEditor.this);
					builder.setTitle(R.string.open_mode);
					builder.setItems(items, new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent();
								String file;
								if (QSEditor.this.getString(R.string.view).equals(items[which])) {
									file = QSEditor.this.mEditText.getPath();
									if ("".equals(file)) {
										Toast.makeText(QSEditor.this, R.string.preview_msg, QSEditor.FILE_BROWSER_SAVEAS_CODE).show();
										return;
									}
									Uri uri = Uri.parse("file://" + file);
									intent.setAction("android.intent.action.VIEW");
									intent.setDataAndType(uri, "*/*");
								} else if (QSEditor.this.getString(R.string.share).equals(items[which])) {
									String text;
									intent.setAction("android.intent.action.SEND");
									intent.setType("text/plain");
									int selstart = QSEditor.this.mEditText.getSelectionStart();
									int selend = QSEditor.this.mEditText.getSelectionEnd();
									if (selend != selstart) {
										text = QSEditor.this.mEditText.getText().subSequence(selstart, selend).toString();
									} else {
										text = QSEditor.this.mEditText.getString();
									}
									intent.putExtra("android.intent.extra.TEXT", text);
								} else if (QSEditor.this.getString(R.string.run_in_sl4a_background).equals(items[which]) || QSEditor.this.getString(R.string.run_in_sl4a_terminal).equals(items[which])) {
									file = QSEditor.this.mEditText.getPath();
									if ("".equals(file)) {
										Toast.makeText(QSEditor.this, R.string.run_before_save_file, QSEditor.FILE_BROWSER_SAVEAS_CODE).show();
										return;
									}
									intent.setComponent(new ComponentName("com.googlecode.android_scripting", "com.googlecode.android_scripting.activity.ScriptingLayerServiceLauncher"));
									if (QSEditor.this.getString(R.string.run_in_sl4a_terminal).equals(items[which])) {
										intent.setAction("com.googlecode.android_scripting.action.LAUNCH_FOREGROUND_SCRIPT");
										intent.putExtra(QSEditor.EXTRA_SCRIPT_PATH, file);
									} else {
										intent.setAction("com.googlecode.android_scripting.action.LAUNCH_BACKGROUND_SCRIPT");
										intent.putExtra(QSEditor.EXTRA_SCRIPT_PATH, file);
									}
								}
								try {
									QSEditor.this.startActivity(intent);
								} catch (Exception e) {
									Toast.makeText(QSEditor.this, "Exception: " + e.getMessage(), QSEditor.FILE_BROWSER_SAVEAS_CODE).show();
								}
							}
						
						});
					builder.show();
					break;
				case R.id.menu_saveas /*保存2131099901*/:
					QSEditor.this.openFileBrowser(QSEditor.FILE_BROWSER_SAVEAS_CODE, QSEditor.this.mEditText.getTitle());
					break;
				case R.id.menu_highlight /*高亮与着色2131099902*/:
					LangList langList = new LangList(QSEditor.this);
					break;
				case R.id.menu_encoding /*编码2131099903*/:
					EncodingList encodingList = new EncodingList(QSEditor.this);
					break;
				case R.id.menu_search_replace /*查找替换2131099904*/:
					QSEditor.this.findLayout.setVisibility(QSEditor.FILE_BROWSER_OPEN_CODE);
					QSEditor.this.replaceShowButton.setVisibility(QSEditor.FILE_BROWSER_OPEN_CODE);
					break;
				case R.id.menu_preferences /*参数设置2131099905*/:
					QSEditor.this.startActivity(new Intent(QSEditor.this, Options.class));
					break;
				case R.id.menu_exit /*退出2131099906*/:
					QSEditor.this.onFinish();
					break;
			}
			return true;
		}
	};
	
	
		
	//onCreate调用
	protected void onEditLocationChanged(QSEditText paramJecEditText)
	{
		if (!paramJecEditText.isCanBackEditLocation())
			this.last_edit_back.setImageDrawable(this.last_edit_back_d);
		else
			this.last_edit_back.setImageDrawable(this.last_edit_back_s);
		if (!paramJecEditText.isCanForwardEditLocation())
			this.last_edit_forward.setImageDrawable(this.last_edit_forward_d);
		else
			this.last_edit_forward.setImageDrawable(this.last_edit_forward_s);
	}
	//onCreate调用
	private void switchPreviewButton(String paramString)
	{
		if (!paramString.toUpperCase().startsWith("HTML"))
			this.previewBtn.setVisibility(8);
		else
			this.previewBtn.setVisibility(0);
	}
	
	public static void printException(Exception paramException)
	{
		Log.d("QSEditor", paramException.getMessage());
	}
	//onNewIntent调用
	public void readFileToEditText(String path) {
        String[] selinfo = getSharedPreferences(PREF_HISTORY, FILE_BROWSER_OPEN_CODE).getString(path, "").split(",");
        int linebreak = FILE_BROWSER_OPEN_CODE;
        String encoding = "";
        int selstart = FILE_BROWSER_OPEN_CODE;
        int selend = FILE_BROWSER_OPEN_CODE;
        if (selinfo.length >= 5) {
            linebreak = Integer.valueOf(selinfo[3]).intValue();
            encoding = selinfo[4];
            selstart = Integer.valueOf(selinfo[FILE_BROWSER_OPEN_CODE]).intValue();
            selend = Integer.valueOf(selinfo[FILE_BROWSER_SAVEAS_CODE]).intValue();
        }
        readFileToEditText(path, "", linebreak, selstart, selend);
    }

    public void readFileToEditText(String path, String encoding, int lineBreak, int selstart, int selend) {
        if (!"".equals(path)) {
            this.mTabHost.addTab(path);
            AsyncReadFile asyncReadFile = new AsyncReadFile(this, path, encoding, lineBreak, selstart, selend);
        }
    }
	//onLoaded()调用
	private void loadLastOpenFiles()
	{
		if (this.mLastFiles.size() >= 1)
			readFileToEditText((String)this.mLastFiles.remove(0));
	}
	//当activity关闭后，如果有返回值，则会在这个方法内接收
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (-1 == resultCode) {
            final String path;
            final int lineBreak;
            int encoding;
            final String charset;
            switch (requestCode) {
                case FILE_BROWSER_OPEN_CODE /*0*/:
                    path = data.getStringExtra("file");
                    lineBreak = data.getIntExtra("linebreak", FILE_BROWSER_OPEN_CODE);
                    encoding = data.getIntExtra("encoding", FILE_BROWSER_OPEN_CODE);
                    if (encoding < FILE_BROWSER_SAVEAS_CODE) {
                        charset = "";
                    } else {
                        charset = EncodingList.list[encoding];
                    }
                    readFileToEditText(path, charset, lineBreak, FILE_BROWSER_OPEN_CODE, FILE_BROWSER_OPEN_CODE);
                    break;
                case FILE_BROWSER_SAVEAS_CODE /*1*/:
                    isLoading = false;
                    path = data.getStringExtra("file");
                    lineBreak = data.getIntExtra("linebreak", FILE_BROWSER_OPEN_CODE);
                    encoding = data.getIntExtra("encoding", FILE_BROWSER_OPEN_CODE);
                    if (encoding < FILE_BROWSER_SAVEAS_CODE) {
                        charset = "";
                    } else {
                        charset = EncodingList.list[encoding];
                    }
                    final File file = new File(path);
                    if (!file.exists()) {
                        this.mEditText.setPath(path);
                        setTitle(file.getName());
                        save(charset, lineBreak);
                        break;
                    }
                    new Builder(this).setMessage(getText(R.string.overwrite_confirm)).setPositiveButton(17039379, new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which) {
								QSEditor.this.mEditText.setPath(path);
								QSEditor.this.setTitle(file.getName());
								QSEditor.this.save(charset, lineBreak);
							}
						})
						.setNegativeButton(17039369, null)
						.show();
                    break;
            }
            this.fileBrowserCallbackRunnable.run();
        }
    }
	
	//-------
	//当键盘按下时 
	//首先触发dispatchKeyEvent
	//然后触发onUserInteraction
	//再次onKeyDown
	//如果按下紧接着松开，则是俩步
	//紧跟着触发dispatchKeyEvent
	//然后触发onUserInteraction
	//再次onKeyUp
	public boolean dispatchKeyEvent(KeyEvent event) {
        int keycode = event.getKeyCode();
        if (!((event.getMetaState() & 4104) != 0) || event.getAction() != 0 || keycode != 47) {
            return super.dispatchKeyEvent(event);
        }
        save();
        return true;
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4://TestHandler.ACTION_PDISPLAY /*4*/:
                if (!isLoading) {
                    if (this.mSymbolGrid.isShown()) {
                        this.mSymbolGrid.setVisibility(8);
                        return true;
                    } else if (this.findLayout.getVisibility() == 0) {
                        this.findLayout.setVisibility(8);
                        this.replaceLayout.setVisibility(8);
                        return true;
                    } else if (!this.back_button_exit) {
                        return true;
                    } else {
                        onFinish();
                        return true;
                    }
                }
                break;
            case 24://R.styleable.View_fadingEdge /*24*/:
                if (!hideToolbar) {
                    this.toolbar.setVisibility(8);
                    hideToolbar = true;
                    Toast.makeText(this, R.string.volume_up_toolbar_msg, FILE_BROWSER_SAVEAS_CODE).show();
                    return true;
                } else if (fullScreen) {
                    return true;
                } else {
                    getWindow().addFlags(1024);
                    getWindow().setFlags(1024, 1024);
                    fullScreen = true;
                    Toast.makeText(this, R.string.volume_up_fullscreen_msg, FILE_BROWSER_SAVEAS_CODE).show();
                    return true;
                }
            case 25://R.styleable.View_fadingEdgeLength /*25*/:
                if (hideToolbar) {
                    this.toolbar.setVisibility(FILE_BROWSER_OPEN_CODE);
                    hideToolbar = false;
                    return true;
                } else if (!fullScreen) {
                    return true;
                } else {
                    getWindow().clearFlags(1024);
                    fullScreen = false;
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }
	public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 84://R.styleable.Theme_listViewWhiteStyle /*84*/:
                if (this.findLayout.getVisibility() == 8) {
                    this.findLayout.setVisibility(FILE_BROWSER_OPEN_CODE);
                }
                find("next");
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
	public void onFinish() {
        isFinish = true;
        saveHistory();
        this.mTabHost.setAutoNewTab(false);
        int count = this.mTabHost.getTabCount();
        if (count < FILE_BROWSER_SAVEAS_CODE) {
            finish();
            return;
        }
        this.mTabHost.setCurrentTab(count - 1);
        this.mTabHost.iterCloseTab(3, FILE_BROWSER_OPEN_CODE, count);
    }
	private void saveHistory() {
        Editor editor;
        if (!(this.mEditText.getPath() == null || "".equals(this.mEditText.getPath()))) {
            int selstart = this.mEditText.getSelectionStart();
            int selend = this.mEditText.getSelectionEnd();
            editor = getSharedPreferences(PREF_HISTORY, FILE_BROWSER_OPEN_CODE).edit();
            editor.putString(this.mEditText.getPath(), String.format("%d,%d,%d,%d,%s", new Object[]{Integer.valueOf(selstart), Integer.valueOf(selend), Long.valueOf(System.currentTimeMillis()), Integer.valueOf(this.mEditText.getLineBreak()), this.mEditText.getEncoding()}));
            editor.commit();
        }
        editor = getSharedPreferences(PREF_LAST_FILE, FILE_BROWSER_OPEN_CODE).edit();
        editor.clear();
        ArrayList<String> paths = this.mTabHost.getAllPath();
        int size = paths.size();
        for (int i = FILE_BROWSER_OPEN_CODE; i < size; i += FILE_BROWSER_SAVEAS_CODE) {
            String path = (String) paths.get(i);
            editor.putString(path, path);
        }
        editor.commit();
    }
	//findButtonClickListener 调用
	//onKeyUp调用
	public void find(String direction) {
        String keyword = this.findEditText.getText().toString();
        if (!"".equals(keyword)) {
            if ("back".equals(direction)) {
                this.mAsyncSearch.search(keyword, false, this);
            } else {
                this.mAsyncSearch.search(keyword, true, this);
            }
        }
    }
	
	public QSEditText getEditText()
	{
		return this.mTabHost.getCurrentEditText();
	}
	//onResume()调用
	private void load_options() {
        isRoot = this.mPref.getBoolean("get_root", false);
        if (isRoot && !LinuxShell.canRoot()) {
            isRoot = false;
            Toast.makeText(this, "Root Fail", FILE_BROWSER_SAVEAS_CODE).show();
        }
        QSEditText.TOUCH_ZOOM_ENABLED = this.mPref.getBoolean("touch_zoom", true);
        boolean mHideSoftKeyboard = this.mPref.getBoolean("hide_soft_Keyboard", false);
        if (mHideSoftKeyboard && this.mEditText != null) {
            this.mEditText.showIME(false);
        }
        QSEditText.setHideKeyboard(mHideSoftKeyboard);
        String screen_orientation = this.mPref.getString("screen_orientation", "auto");
        if ("portrait".equals(screen_orientation)) {
            setRequestedOrientation(FILE_BROWSER_SAVEAS_CODE);
        } else if ("landscape".equals(screen_orientation)) {
            setRequestedOrientation(FILE_BROWSER_OPEN_CODE);
        }
        this.autosave = this.mPref.getBoolean("autosave", false);
        this.mAsyncSearch.setIgnoreCase(this.mPref.getBoolean("search_ignore_case", true));
        this.mAsyncSearch.setRegExp(this.mPref.getBoolean("search_regex", false));
        this.back_button_exit = this.mPref.getBoolean("back_button_exit", true);
        init_highlight();
        String dateformat = this.mPref.getString("custom_date_format", "0");
        String sysformat = this.mPref.getString("sys_format", "0");
        if (this.mPref.getBoolean("custom_format", false)) {
            this.mEditText.setDateFormat(dateformat);
        } else {
            this.mEditText.setDateFormat(sysformat);
        }
    }
	//调用该方法后，Activity处于前台可见、可交互状态，Activity处于running状态
	protected void onResume()
	{
		super.onResume();
		load_options();
		isLoading = false;
	}
	//Activity资源被系统回收之前执行的最后一个方法，调用finish()或者系统临时销毁Activity的时候调用，可以使用isFinishing()进行判断是正常销毁还是异常情况。
	//用户改变设置（屏幕方向、语言、输入设备等）当前Activity实例会被销毁，然后重新创建一个新的实例；
	//异常情况，该方法可能不会执行，Activity直接被killed掉
	public void onDestroy()
	{
		super.onDestroy();
		System.runFinalizersOnExit(true);
		System.exit(0);
	}
	//Activity完全不可见的时候执行该方法，比如：新的Activity启动并覆盖了当前的activity，或者返回到前一个Activity。当前Activity处于Stopped，随时可能被系统销毁，也有可能被重新启动调到前台。
	//异常情况，该方法可能不会执行，Activity直接被killed掉
	protected void onStop()
	{
		if (!isFinish)
			saveHistory();
		if ((this.autosave) && (this.mEditText.isTextChanged()))
			save();
		super.onStop();
	}
	//-------菜单---------
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.mMenu.show();
        return true;
    }
	//当用户打开panel的菜单时被调用。当菜单样式切换时也会调用。例如，从图标菜单切换至扩展菜单。返回true允许菜单打开，返回false阻止菜单打开。
	public boolean onMenuOpened(int featureId, Menu menu) {
        this.mMenu.show();
        return false;
    }
	/*保存Activity的附件信息
	 onSaveInstanceState和onRestoreInstanceState基本作用
	 onSaveInstanceState是用来保存UI状态的，你可以使用它保存你所想保存的东西，在Activity杀死之前，它一般在onStop或者onPause之前触发，onRestoreInstanceState则是在onResume之前触发回复状态，至于复写这个方法后onCreate方法是否会被调用。
	 1.Activity被杀死了，onCreate会被调用，且onRestoreInstanceState 在 onResume之前恢复上次保存的信息。
	 2.Activity没被杀死，onCreate不会被调用，但onRestoreInstanceState 仍然会被调用，在 onResume之前恢复上次保存的信息。
	 onSaveInstanceState和onRestoreInstanceState 是一对兄弟，一个负责存储，一个负责取出.“不一定”是成对的被调用的。
	 Activity的 onSaveInstanceState() 和 onRestoreInstanceState()并不是生命周期方法，它们不同于 onCreate()、onPause()等生命周期方法，它们并不一定会被触发。当应用遇到意外情况（如：内存不足、用户直接按Home键）由系统销毁一个Activity时，onSaveInstanceState() 会被调用。但是当用户主动去销毁一个Activity时，例如在应用中按返回键，onSaveInstanceState()就不会被调用。因为在这种情况下，用户的行为决定了不需要保存Activity的状态。通常onSaveInstanceState()只适合用于保存一些临时性的状态，而onPause()适合用于数据的持久化保存。
	 在activity被杀掉之前调用保存每个实例的状态,以保证该状态可以在onCreate(Bundle)或者onRestoreInstanceState(Bundle) (传入的Bundle参数是由onSaveInstanceState封装好的)中恢复。这个方法在一个activity被杀死前调用，当该activity在将来某个时刻回来时可以恢复其先前状态。 　　 例如，如果activity B启用后位于activity A的前端，在某个时刻activity A因为系统回收资源的问题要被杀掉，A通过onSaveInstanceState将有机会保存其用户界面状态，使得将来用户返回到activity A时能通过onCreate(Bundle)或者onRestoreInstanceState(Bundle)恢复界面的状态。
	 　　关于onSaveInstanceState ()，是在函数里面保存一些View有用的数据到一个Parcelable对象并返回。在Activity的onSaveInstanceState(Bundle outState)中调用View的onSaveInstanceState ()，返回Parcelable对象，
	 　　接着用Bundle的putParcelable方法保存在Bundle savedInstanceState中。
	 　　当系统调用Activity的的onRestoreInstanceState(Bundle savedInstanceState)时， 同过Bundle的getParcelable方法得到Parcelable对象，然后把该Parcelable对象传给View的onRestoreInstanceState (Parcelable state)。在的View的onRestoreInstanceState中从Parcelable读取保存的数据以便View使用。
	 　　这就是onSaveInstanceState() 和 onRestoreInstanceState() 两个函数的基本作用和用法。
	 2. onSaveInstanceState() 什么时候调用
	 先看Application Fundamentals上的一段话：
	 Android calls onSaveInstanceState() before the activity becomes vulnerable to being destroyed by the system, but does not bother calling it when the instance is actually being destroyed by a user action (suchas pressing the BACK key)
	 从这句话可以知道，当某个activity变得“容易”被系统销毁时，该activity的onSaveInstanceState就会被执行，除非该activity是被用户主动销毁的，例如当用户按BACK键的时候。
	 注意上面的双引号，何为“容易”？言下之意就是该activity还没有被销毁，而仅仅是一种可能性。这种可能性有哪些？通过重写一个activity的所有生命周期的onXXX方法，包括onSaveInstanceState和onRestoreInstanceState方法，我们可以清楚地知道当某个activity（假定为activity A）显示在当前task的最上层时，其onSaveInstanceState方法会在什么时候被执行，有这么几种情况：
	 1、当用户按下HOME键时
	 这是显而易见的，系统不知道你按下HOME后要运行多少其他的程序，自然也不知道activity A是否会被销毁，故系统会调用onSaveInstanceState，让用户有机会保存某些非永久性的数据。以下几种情况的分析都遵循该原则
	 2、长按HOME键，选择运行其他的程序时。
	 3、按下电源按键（关闭屏幕显示）时。
	 4、从activity A中启动一个新的activity时。
	 5、屏幕方向切换时，例如从竖屏切换到横屏时
	 在屏幕切换之前，系统会销毁activity A，在屏幕切换之后系统又会自动地创建activity A，所以onSaveInstanceState一定会被执行
	 总而言之，onSaveInstanceState的调用遵循一个重要原则，即当系统“未经你许可”时销毁了你的activity，则onSaveInstanceState会被系统调用，这是系统的责任，因为它必须要提供一个机会让你保存你的数据（当然你不保存那就随便你了）。
	 　
	 3. onRestoreInstanceState()什么时候调用
	 至于onRestoreInstanceState方法，需要注意的是，onSaveInstanceState方法和onRestoreInstanceState方法“不一定”是成对的被调用的。
	 onRestoreInstanceState被调用的前提是，activity A“确实”被系统销毁了，而如果仅仅是停留在有这种可能性的情况下，则该方法不会被调用，例如，当正在显示activity A的时候，用户按下HOME键回到主界面，然后用户紧接着又返回到activity A，这种情况下activity A一般不会因为内存的原因被系统销毁，故activity A的onRestoreInstanceState方法不会被执行.此处也说明以上二者，大多数情况下不成对被使用。
	 另外，onRestoreInstanceState的bundle参数也会传递到onCreate方法中，你也可以选择在onCreate方法中做数据还原.
	 onRestoreInstanceState()在onStart() 和 onPostCreate(Bundle)之间调用。
	 4. onSaveInstanceState()方法的默认实现 　　如果我们没有覆写onSaveInstanceState()方法, 此方法的默认实现会自动保存activity中的某些状态数据, 比如activity中各种UI控件的状态.。android应用框架中定义的几乎所有UI控件都恰当的实现了onSaveInstanceState()方法,因此当activity被摧毁和重建时, 这些UI控件会自动保存和恢复状态数据. 比如EditText控件会自动保存和恢复输入的数据,而CheckBox控件会自动保存和恢复选中状态.开发者只需要为这些控件指定一个唯一的ID(通过设置android:id属性即可), 剩余的事情就可以自动完成了.如果没有为控件指定ID, 则这个控件就不会进行自动的数据保存和恢复操作。
	 　　由上所述, 如果我们需要覆写onSaveInstanceState()方法, 一般会在第一行代码中调用该方法的默认实现:super.onSaveInstanceState(outState)。
	 5. 是否需要重写onSaveInstanceState()方法
	 既然该方法的默认实现可以自动的保存UI控件的状态数据, 那什么时候需要覆写该方法呢?
	 　　如果需要保存额外的数据时, 就需要覆写onSaveInstanceState()方法。大家需要注意的是：onSaveInstanceState()方法只适合保存瞬态数据, 比如UI控件的状态, 成员变量的值等，而不应该用来保存持久化数据，持久化数据应该当用户离开当前的activity时，在onPause()中保存（比如将数据保存到数据库或文件中）。说到这里，还要说一点的就是在onPause()中不适合用来保存比较费时的数据，所以这点要理解。
	 　　由于onSaveInstanceState()方法方法不一定会被调用, 因此不适合在该方法中保存持久化数据, 例如向数据库中插入记录等. 保存持久化数据的操作应该放在onPause()中。若是永久性值，则在onPause()中保存；若大量，则另开线程吧，别阻塞UI线程。
	 　　6. 引发activity销毁和重建的其它情况
	 　　除了系统处于内存不足的原因会摧毁activity之外, 某些系统设置的改变也会导致activity的摧毁和重建. 例如改变屏幕方向(见上例), 改变设备语言设定, 键盘弹出等。
	 　　另外，当屏幕的方向发生了改变， Activity会被摧毁并且被重新创建，如果你想在Activity被摧毁前缓存一些数据，并且在Activity被重新创建后恢复缓存的数据。可以重写Activity的 onSaveInstanceState() 和 onRestoreInstanceState()方法，如下代码所示：
	 */
	protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (this.autosave && this.mEditText.isTextChanged()) {
            save();
        }
    }
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Exception e) {
            printException(e);
        }
    }

	//finish调用
	public void onLoaded() {
        Toast.makeText(getApplicationContext(), getString(R.string.encoding) + ": " + this.mEditText.getEncoding(), FILE_BROWSER_SAVEAS_CODE).show();
        this.mEditText.resetUndoStatus();
        setTitle(new File(this.mEditText.getPath()).getName());
        switchPreviewButton(Highlight.getNameByExt(this.mEditText.getCurrentFileExt()));
        String[] selinfo = getSharedPreferences(PREF_HISTORY, FILE_BROWSER_OPEN_CODE).getString(this.mEditText.getPath(), "").split(",");
        if (selinfo.length >= 3) {
            this.mEditText.setSelection(Integer.valueOf(selinfo[FILE_BROWSER_OPEN_CODE]).intValue(), Integer.valueOf(selinfo[FILE_BROWSER_SAVEAS_CODE]).intValue());
        }
        saveHistory();
        loadLastOpenFiles();
    }
	public void setTitle(String paramString)
	{
		super.setTitle(paramString);
		this.mTabHost.setTitle(paramString);
	}
	 
	 public void scrollToTop()
	 {
	     this.mEditText.scrollTo(0, 0);
	 }
	 
     //EncodingList调用
	public void setEncoding(String encoding) {
        try {
            this.mEditText.setText2(new String(this.mEditText.getString().getBytes(this.mEditText.getEncoding()), encoding));
            this.mEditText.setEncoding(encoding);
        } catch (UnsupportedEncodingException e) {
            printException(e);
        } catch (OutOfMemoryError e2) {
            Toast.makeText(this, R.string.out_of_memory, FILE_BROWSER_OPEN_CODE).show();
        }
    }

	 
	 
	class ColorListener implements ColorPicker.OnColorChangedListener
    {
        @Override
        public void onColorChanged(String key, String color)
        {
            insert_text(color);
        }

    }
	public void insert_text(String paramString)
	{
		if (this.mEditText != null)
		{
			int i = this.mEditText.getSelectionStart();
			int j = this.mEditText.getSelectionEnd();
			this.mEditText.getText().replace(Math.min(i, j), Math.max(i, j), paramString, 0, paramString.length());
		}
	}
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.JecEditor
 * JD-Core Version:    0.6.0
 */
