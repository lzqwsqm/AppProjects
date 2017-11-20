/*
 *程序主窗口-程序的显示和功能的传递 
 *由onCreate()创建窗口
 *由implements实现窗口功能 
 */

package com.database;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
//import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
//import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//import android.telephony.TelephonyManager;
import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
//import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
//import android.view.SubMenu;
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
//import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
//import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
//import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.database.context.RuntimeInfo;
import com.database.context.Function;
import com.database.model.AssetKm;
import com.database.model.Favor;//人情
import com.database.model.FavorAudit;
import com.database.model.Param;//参数
import com.database.model.Evection;//
import com.database.model.EvectionAudit;
import com.database.model.Deposit;//银行
import com.database.model.Virement;
import com.database.model.Audit;//收支流水
import com.database.model.KM;
import com.database.model.Credit;//借贷
import com.database.model.CreditAudit;
import com.database.model.Asset;//资产
import com.database.model.AssetAudit;
import com.database.model.Report;//资产

import com.database.util.MyDate;//时间
import com.database.util.Convertor;//转换
import com.database.util.DBTool;//数据库工具

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;//序列索引
import java.util.Stack;//栈
import java.util.Vector;//数组


public class MainActivity extends Activity
  //窗口功能
  implements 
  //列表单击功能
  AdapterView.OnItemClickListener, 
  //上下文菜单
  View.OnCreateContextMenuListener, 
  //触摸
  View.OnTouchListener, 
  //手势
  GestureDetector.OnGestureListener
{
    //因为其它地方多次用到在此定义声明,如果只用一次在方法中定义声明
	static final int FILL_ALL = 1;
    static final int FILL_PAGE = 0;

	//全屏
	private static final int FP = ViewGroup.LayoutParams.FILL_PARENT;//-1
	//刚好包括组件
	private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;//-2
	
	public static final int ID_LIST_TITLE = 2147483394;
	public static final int ID_LIST_MORE = 2147483397;
	public static final int ID_LIST_END = 2147483393;

	public static int function = 0;
    public static Vector<Integer> index = new Vector();
    public static int[] params = null;
    //定义堆栈
    public static Stack<int[]> paramstack = new Stack();
    static final Vector<Integer> tab_function = new Vector();
    public static int tab_index = 0;
    static final int tab_selected_index = 2;
    static final Vector<String> tab_title = new Vector();
    Button addauditbutton = null;
    Button backbutton = null;
    FrameLayout bodylayout = null;
    ChartView chartview = null;
    long current_time = 0L;
    int fill_mode = 0;
    public GestureDetector gestureDetector = null;
    boolean inner_app_focus = false;
    ListAdapter listadapter = null;
    ListView lv = null;
    Button menubutton = null;
    TextView titlebar = null;

    static
    {
        tab_index = 0;
        function = 100;
    }
	/*static {
        function = 99;
        params = null;
        paramstack = new Stack();
        index = new Vector();
        tab_title = new Vector();
        tab_function = new Vector();
        tab_index = FILL_PAGE;
    }*/

	
    @Override
	/*
	*主布局线性添加线性(标题)、帖(内容)、线性(工具)
	*窗口由标题内容工具组成
	*由innerDisplay()主体内容显示
	*首先调用创建窗口
	*/
   
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置窗口
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
		RuntimeInfo.main = this;
        
		if (RuntimeInfo.param == null) {
            RuntimeInfo.param = new Param();
        }
        UIAdapter.initParams(this);
        initTab();
        //主布局
		LinearLayout mainlayout = new LinearLayout(this);
	    mainlayout.setOrientation(LinearLayout.VERTICAL);//1垂直排列
	    setContentView(mainlayout);
	 	
		//采用xlm布局
		//setContentView(R.layout.main);
        //LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlayout);
        //mainlayout.setOrientation(LinearLayout.VERTICAL);
        
		//标题栏布局
		mainlayout.addView(initTitleBar(), new LinearLayout.LayoutParams(FP, UIAdapter.getTitleBarHeight()));
        
		//内容布局
		this.bodylayout = new FrameLayout(this);
        this.lv = new ListView(this);
        this.listadapter = new ListAdapter(this);
        this.lv.setAdapter(this.listadapter);
        this.lv.setOnItemClickListener(this);
        this.lv.setOnCreateContextMenuListener(this);
        this.lv.setOnTouchListener(this);
        
		FrameLayout.LayoutParams framelayout = new FrameLayout.LayoutParams(UIAdapter.getTableHeight(), UIAdapter.getTableHeight());
        
		if (isChart(function)) {
            this.chartview = new ChartView(this);
            this.chartview.setOnTouchListener(this);
            this.bodylayout.addView(this.chartview, framelayout);
        } else {
            this.bodylayout.addView(this.lv, framelayout);
        }
		//注册滑屏
        this.gestureDetector = new GestureDetector(this);
        
		mainlayout.addView(this.bodylayout, new LinearLayout.LayoutParams(FP, UIAdapter.getTableHeight()));
        //工具栏
		mainlayout.addView(initToolBar(), new LinearLayout.LayoutParams(FP, UIAdapter.getToolBarHeight()));
        
		if (params == null) {
            params = new int[10];
        }
		
        this.lv.setScrollingCacheEnabled(true);
        this.lv.setDivider(getResources().getDrawable(R.drawable.divider));
        //注册上下文菜单
		registerForContextMenu(this.lv);
        registerForContextMenu(this.addauditbutton);
        registerForContextMenu(this.menubutton);
        registerForContextMenu(this.titlebar);
        //背景
		changeBackground();
        //主体显示
		innerDisplay();
        /*if (RuntimeInfo.param.imei.equals("359616040351058")) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
            RuntimeInfo.param.imei = telephonyManager.getDeviceId();
            RuntimeInfo.param.createRegMd5();
            RuntimeInfo.param.save();
        }
        Handler handler = new Handler();
        if (Param.MODE == 3) {
            RuntimeInfo.param.gatherUserInfo(this, handler, false, null);
        }
        if (Param.MODE == 4) {
            RuntimeInfo.param.version = (short) 8;
            RuntimeInfo.param.save();
        }*/
    }

	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		openContextMenu(this.menubutton);
		return true;
	}
	
	@Override
    public void onDestroy()
	{
		super.onDestroy();
	}
	
	@Override
	public void onPause()
	{
		if (!this.inner_app_focus)
		{
			RuntimeInfo.param.addUsedMinitus(new Date().getTime() - this.current_time);
			RuntimeInfo.param.save();
		}
		super.onPause();
	}
	
	@Override
	public void onResume()
	{
		if (!this.inner_app_focus)
			this.current_time = new Date().getTime();
		this.inner_app_focus = false;
		super.onResume();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasTocus)
	{
	}
	
	@Override
	/*
	 KEYCODE_CALL拨号键5
	 KEYCODE_ENDCALL挂机键6
	 KEYCODE_HOME按键Home3
	 KEYCODE_MENU菜单键82
	 KEYCODE_BACK返回键4
	 KEYCODE_SEARCH搜索键84
	 KEYCODE_CAMERA拍照键27
	 KEYCODE_FOCUS拍照对焦键80
	 KEYCODE_POWER电源键26
	 KEYCODE_NOTIFICATION通知键83
	 KEYCODE_MUTE话筒静音键91
	 KEYCODE_VOLUME_MUTE扬声器静音键164
	 KEYCODE_VOLUME_UP音量增加键24
	 KEYCODE_VOLUME_DOWN音量减小键25
	 控制键
	 键名 描述 键值
	 KEYCODE_ENTER回车键66
	 KEYCODE_ESCAPEESC键111
	 KEYCODE_DPAD_CENTER导航键 确定键23
	 KEYCODE_DPAD_UP导航键 向上19
	 KEYCODE_DPAD_DOWN导航键 向下20
	 KEYCODE_DPAD_LEFT导航键 向左21
	 KEYCODE_DPAD_RIGHT导航键 向右22
	 KEYCODE_MOVE_HOME光标移动到开始键122
	 KEYCODE_MOVE_END光标移动到末尾键123
	 KEYCODE_PAGE_UP向上翻页键92
	 KEYCODE_PAGE_DOWN向下翻页键93
	 KEYCODE_DEL退格键67
	 KEYCODE_FORWARD_DEL删除键112
	 KEYCODE_INSERT插入键124
	 KEYCODE_TABTab键61
	 KEYCODE_NUM_LOCK小键盘锁143
	 KEYCODE_CAPS_LOCK大写锁定键115
	 KEYCODE_BREAKBreak/Pause键121
	 KEYCODE_SCROLL_LOCK滚动锁定键116
	 KEYCODE_ZOOM_IN放大键168
	 KEYCODE_ZOOM_OUT缩小键169
	 组合键
	 键名 描述
	 KEYCODE_ALT_LEFTAlt+Left
	 KEYCODE_ALT_RIGHTAlt+Right
	 KEYCODE_CTRL_LEFTControl+Left
	 KEYCODE_CTRL_RIGHTControl+Right
	 KEYCODE_SHIFT_LEFTShift+Left
	 KEYCODE_SHIFT_RIGHTShift+Right
	 基本
	 键名 描述 键值
	 KEYCODE_0按键'0'7
	 KEYCODE_1按键'1'8
	 KEYCODE_2按键'2'9
	 KEYCODE_3按键'3'10
	 KEYCODE_4按键'4'11
	 KEYCODE_5按键'5'12
	 KEYCODE_6按键'6'13
	 KEYCODE_7按键'7'14
	 KEYCODE_8按键'8'15
	 KEYCODE_9按键'9'16
	 KEYCODE_A按键'A'29
	 KEYCODE_B按键'B'30
	 KEYCODE_C按键'C'31
	 KEYCODE_D按键'D'32
	 KEYCODE_E按键'E'33
	 KEYCODE_F按键'F'34
	 KEYCODE_G按键'G'35
	 KEYCODE_H按键'H'36
	 KEYCODE_I按键'I'37
	 KEYCODE_J按键'J'38
	 KEYCODE_K按键'K'39
	 KEYCODE_L按键'L'40
	 KEYCODE_M按键'M'41
	 KEYCODE_N按键'N'42
	 KEYCODE_O按键'O'43
	 KEYCODE_P按键'P'44
	 KEYCODE_Q按键'Q'45
	 KEYCODE_R按键'R'46
	 KEYCODE_S按键'S'47
	 KEYCODE_T按键'T'48
	 KEYCODE_U按键'U'49
	 KEYCODE_V按键'V'50
	 KEYCODE_W按键'W'51
	 KEYCODE_X按键'X'52
	 KEYCODE_Y按键'Y'53
	 KEYCODE_Z按键'Z'54
	 符号
	 键名 描述
	 KEYCODE_PLUS按键'+'
	 KEYCODE_MINUS按键'-'
	 KEYCODE_STAR按键'*'
	 KEYCODE_SLASH按键'/'
	 KEYCODE_EQUALS按键'='
	 KEYCODE_AT按键'@'
	 KEYCODE_POUND按键'#'
	 KEYCODE_APOSTROPHE按键''' (单引号)
	 KEYCODE_BACKSLASH按键'\'
	 KEYCODE_COMMA按键','
	 KEYCODE_PERIOD按键'.'
	 KEYCODE_LEFT_BRACKET按键'['
	 KEYCODE_RIGHT_BRACKET按键']'
	 KEYCODE_SEMICOLON按键';'
	 KEYCODE_GRAVE按键'`'
	 KEYCODE_SPACE空格键
	 小键盘
	 键名 描述
	 KEYCODE_NUMPAD_0小键盘按键'0'
	 KEYCODE_NUMPAD_1小键盘按键'1'
	 KEYCODE_NUMPAD_2小键盘按键'2'
	 KEYCODE_NUMPAD_3小键盘按键'3'
	 KEYCODE_NUMPAD_4小键盘按键'4'
	 KEYCODE_NUMPAD_5小键盘按键'5'
	 KEYCODE_NUMPAD_6小键盘按键'6'
	 KEYCODE_NUMPAD_7小键盘按键'7'
	 KEYCODE_NUMPAD_8小键盘按键'8'
	 KEYCODE_NUMPAD_9小键盘按键'9'
	 KEYCODE_NUMPAD_ADD小键盘按键'+'
	 KEYCODE_NUMPAD_SUBTRACT小键盘按键'-'
	 KEYCODE_NUMPAD_MULTIPLY小键盘按键'*'
	 KEYCODE_NUMPAD_DIVIDE小键盘按键'/'
	 KEYCODE_NUMPAD_EQUALS小键盘按键'='
	 KEYCODE_NUMPAD_COMMA小键盘按键','
	 KEYCODE_NUMPAD_DOT小键盘按键'.'
	 KEYCODE_NUMPAD_LEFT_PAREN小键盘按键'('
	 KEYCODE_NUMPAD_RIGHT_PAREN小键盘按键')'
	 KEYCODE_NUMPAD_ENTER小键盘按键回车
	 功能键
	 键名 描述
	 KEYCODE_F1按键F1
	 KEYCODE_F2按键F2
	 KEYCODE_F3按键F3
	 KEYCODE_F4按键F4
	 KEYCODE_F5按键F5
	 KEYCODE_F6按键F6
	 KEYCODE_F7按键F7
	 KEYCODE_F8按键F8
	 KEYCODE_F9按键F9
	 KEYCODE_F10按键F10
	 KEYCODE_F11按键F11
	 KEYCODE_F12按键F12
	 多媒体键
	 键名 描述
	 KEYCODE_MEDIA_PLAY多媒体键 播放
	 KEYCODE_MEDIA_STOP多媒体键 停止
	 KEYCODE_MEDIA_PAUSE多媒体键 暂停
	 KEYCODE_MEDIA_PLAY_PAUSE多媒体键 播放/暂停
	 KEYCODE_MEDIA_FAST_FORWARD多媒体键 快进
	 KEYCODE_MEDIA_REWIND多媒体键 快退
	 KEYCODE_MEDIA_NEXT多媒体键 下一首
	 KEYCODE_MEDIA_PREVIOUS多媒体键 上一首
	 KEYCODE_MEDIA_CLOSE多媒体键 关闭
	 KEYCODE_MEDIA_EJECT多媒体键 弹出
	 KEYCODE_MEDIA_RECORD多媒体键 录音
	 手柄按键
	 键名 描述
	 KEYCODE_BUTTON_1通用游戏手柄按钮 #1
	 KEYCODE_BUTTON_2通用游戏手柄按钮 #2
	 KEYCODE_BUTTON_3通用游戏手柄按钮 #3
	 KEYCODE_BUTTON_4通用游戏手柄按钮 #4
	 KEYCODE_BUTTON_5通用游戏手柄按钮 #5
	 KEYCODE_BUTTON_6通用游戏手柄按钮 #6
	 KEYCODE_BUTTON_7通用游戏手柄按钮 #7
	 KEYCODE_BUTTON_8通用游戏手柄按钮 #8
	 KEYCODE_BUTTON_9通用游戏手柄按钮 #9
	 KEYCODE_BUTTON_10通用游戏手柄按钮 #10
	 KEYCODE_BUTTON_11通用游戏手柄按钮 #11
	 KEYCODE_BUTTON_12通用游戏手柄按钮 #12
	 KEYCODE_BUTTON_13通用游戏手柄按钮 #13
	 KEYCODE_BUTTON_14通用游戏手柄按钮 #14
	 KEYCODE_BUTTON_15通用游戏手柄按钮 #15
	 KEYCODE_BUTTON_16通用游戏手柄按钮 #16
	 KEYCODE_BUTTON_A游戏手柄按钮 A
	 KEYCODE_BUTTON_B游戏手柄按钮 B
	 KEYCODE_BUTTON_C游戏手柄按钮 C
	 KEYCODE_BUTTON_X游戏手柄按钮 X
	 KEYCODE_BUTTON_Y游戏手柄按钮 Y
	 KEYCODE_BUTTON_Z游戏手柄按钮 Z
	 KEYCODE_BUTTON_L1游戏手柄按钮 L1
	 KEYCODE_BUTTON_L2游戏手柄按钮 L2
	 KEYCODE_BUTTON_R1游戏手柄按钮 R1
	 KEYCODE_BUTTON_R2游戏手柄按钮 R2
	 KEYCODE_BUTTON_MODE游戏手柄按钮 Mode
	 KEYCODE_BUTTON_SELECT游戏手柄按钮 Select
	 KEYCODE_BUTTON_START游戏手柄按钮 Start
	 KEYCODE_BUTTON_THUMBLLeft Thumb Button
	 KEYCODE_BUTTON_THUMBRRight Thumb Button
	 待查
	 键名 描述
	 KEYCODE_NUM按键Number modifier
	 KEYCODE_INFO按键Info
	 KEYCODE_APP_SWITCH按键App switch
	 KEYCODE_BOOKMARK按键Bookmark
	 KEYCODE_AVR_INPUT按键A/V Receiver input
	 KEYCODE_AVR_POWER按键A/V Receiver power
	 KEYCODE_CAPTIONS按键Toggle captions
	 KEYCODE_CHANNEL_DOWN按键Channel down
	 KEYCODE_CHANNEL_UP按键Channel up
	 KEYCODE_CLEAR按键Clear
	 KEYCODE_DVR按键DVR
	 KEYCODE_ENVELOPE按键Envelope special function
	 KEYCODE_EXPLORER按键Explorer special function
	 KEYCODE_FORWARD按键Forward
	 KEYCODE_FORWARD_DEL按键Forward Delete
	 KEYCODE_FUNCTION按键Function modifier
	 KEYCODE_GUIDE按键Guide
	 KEYCODE_HEADSETHOOK按键Headset Hook
	 KEYCODE_META_LEFT按键Left Meta modifier
	 KEYCODE_META_RIGHT按键Right Meta modifier
	 KEYCODE_PICTSYMBOLS按键Picture Symbols modifier
	 KEYCODE_PROG_BLUE按键Blue “programmable”
	 KEYCODE_PROG_GREEN按键Green “programmable”
	 KEYCODE_PROG_RED按键Red “programmable”
	 KEYCODE_PROG_YELLOW按键Yellow “programmable”
	 KEYCODE_SETTINGS按键Settings
	 KEYCODE_SOFT_LEFT按键Soft Left
	 KEYCODE_SOFT_RIGHT按键Soft Right
	 KEYCODE_STB_INPUT按键Set-top-box input
	 KEYCODE_STB_POWER按键Set-top-box power
	 KEYCODE_SWITCH_CHARSET按键Switch Charset modifier
	 KEYCODE_SYM按键Symbol modifier
	 KEYCODE_SYSRQ按键System Request / Print Screen
	 KEYCODE_TV按键TV
	 KEYCODE_TV_INPUT按键TV input
	 KEYCODE_TV_POWER按键TV power
	 KEYCODE_WINDOW按键Window
	 KEYCODE_UNKNOWN未知按键
	 
	 
	*/
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK /*4*/:
                cancelAction();
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT /*21*/:
                moveLeft();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT /*22*/:
                moveRight();
                return true;
            default:
                return false;
        }
    }

	
	////////////////////

	public void initTab()
	{
	    tab_title.removeAllElements();
	    tab_function.removeAllElements();
	    
	    tab_title.add("首页");
	    tab_function.add(Integer.valueOf(100));
	    tab_title.add("收支");
	    tab_function.add(Integer.valueOf(105));
	    tab_title.add("报表");
	    tab_function.add(Integer.valueOf(110));
	 
	    tab_title.add("银行");
	    tab_function.add(Integer.valueOf(115));

	  //  if ((Param.MODE != 2) && (RuntimeInfo.param.flag[50] == 0))
	  //  {
	        tab_title.add("人情");
	        tab_function.add(Integer.valueOf(140));
	 //   }
	    if (RuntimeInfo.param.flag[11] == 0)
	    {
	        tab_title.add("借贷");
	        tab_function.add(Integer.valueOf(145));
	    }
	  //  if (Param.MODE != 2)
	  //  {
	        if (RuntimeInfo.param.flag[10] == 0)
	        {
	            tab_title.add("投资");
	            tab_function.add(Integer.valueOf(150));
	        }
	        if (RuntimeInfo.param.flag[12] == 0)
	        {
	            tab_title.add("资产");
	            tab_function.add(Integer.valueOf(155));
	        }
	  //  }
	    if (RuntimeInfo.param.flag[33] == 0)
	    {
	        tab_title.add("分析");
	        tab_function.add(Integer.valueOf(120));
	    }
	    if (RuntimeInfo.param.flag[6] == 0)
	    {
	        tab_title.add("出差");
	        tab_function.add(Integer.valueOf(135));
	    }
	    tab_title.add("字典");
	    tab_function.add(Integer.valueOf(125));
	    tab_title.add("系统");
	    tab_function.add(Integer.valueOf(130));
	    
	    if (tab_index > -1 + tab_function.size())
	        tab_index = -1 + tab_function.size();
    }
    /*
     * 标题栏
     */
	
	LinearLayout initTitleBar() {
        LinearLayout title_bar = new LinearLayout(this);
        this.titlebar = new TextView(this);
        this.titlebar.setGravity(Gravity.CENTER);
        this.titlebar.setText("君子爱财");
        this.titlebar.setTextSize((float) UIAdapter.getTitlebartextSize());
        this.titlebar.setTextColor(FP);
        ImageView left = new ImageView(this);
        left.setImageDrawable(getResources().getDrawable(R.drawable.leftarrow));
        ImageView right = new ImageView(this);
        right.setImageDrawable(getResources().getDrawable(R.drawable.rightarrow));
        left.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					ImageView iv = (ImageView) v;
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						iv.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.leftarrowtouched));
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						iv.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.leftarrow));
					}
					return false;
				}
			});
        left.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					MainActivity.this.moveLeft();
				}
			});
        right.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					ImageView iv = (ImageView) v;
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						iv.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.rightarrowtouched));
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						iv.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.rightarrow));
					}
					return false;
				}
			});
        right.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					MainActivity.this.moveRight();
				}
			});
		LinearLayout.LayoutParams layoutparam1 = new LinearLayout.LayoutParams(UIAdapter.width / 4, UIAdapter.getTitleBarHeight());
		LinearLayout.LayoutParams layoutparam2 = new LinearLayout.LayoutParams(UIAdapter.width / tab_selected_index, UIAdapter.getTitleBarHeight());
		LinearLayout.LayoutParams layoutparam3 = new LinearLayout.LayoutParams(UIAdapter.width / 4, UIAdapter.getTitleBarHeight());
        title_bar.addView(left, layoutparam1);
        title_bar.addView(this.titlebar, layoutparam2);
        title_bar.addView(right, layoutparam3);
        return title_bar;
    }

	
	void moveLeft()
    {
        tab_index = -1 + tab_index;
        if (tab_index < 0)
            tab_index = -1 + tab_function.size();
        display(((Integer)tab_function.get(tab_index)).intValue());
    }

    void moveRight()
    {
        tab_index = 1 + tab_index;
        if (tab_index >= tab_function.size())
            tab_index = 0;
        display(((Integer)tab_function.get(tab_index)).intValue());
    }
	
	
	public void display(int newfunction) {
        if (isChart(newfunction) && !isChart(function)) {
            this.chartview = new ChartView(this);
            this.chartview.setOnTouchListener(this);
            this.bodylayout.removeView(this.lv);
            this.bodylayout.addView(this.chartview);
        } else if (!isChart(newfunction) && isChart(function)) {
            this.bodylayout.removeView(this.chartview);
            this.bodylayout.addView(this.lv);
            this.chartview = null;
        }
        paramstack.clear();
        function = newfunction;
        params = new int[10];
        innerDisplay();
    }

	
	
	LinearLayout initToolBar() {
        LinearLayout toolbar = new LinearLayout(this);
        OnTouchListener touchlistener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (RuntimeInfo.param.flag[51] == 0) {
                        v.setBackgroundResource(R.drawable.titlebgtouched);
                    } else {
                        v.setBackgroundResource(R.drawable.titlebgblacktouched);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        };
        TextView navi = new TextView(this);
        navi.setText("导航");
        navi.setTextSize((float) UIAdapter.getTextSize());
        navi.setTextColor(Color.WHITE);
        navi.setGravity(Gravity.CENTER);
        navi.setBackgroundColor(Color.TRANSPARENT);
        navi.setOnTouchListener(touchlistener);
        this.menubutton = new Button(this);
        this.menubutton.setText("菜单");
        this.menubutton.setTextSize((float) UIAdapter.getTextSize());
        this.menubutton.setBackgroundColor(Color.TRANSPARENT);
        this.menubutton.setOnTouchListener(touchlistener);
        this.menubutton.setTextColor(Color.WHITE);
        this.menubutton.setGravity(Gravity.CENTER);
        this.backbutton = new Button(this);
        this.backbutton.setText("退出");
        this.backbutton.setTextSize((float) UIAdapter.getTextSize());
        this.backbutton.setTextColor(Color.WHITE);
        this.backbutton.setBackgroundColor(Color.TRANSPARENT);
        this.backbutton.setGravity(Gravity.CENTER);
        this.backbutton.setOnTouchListener(touchlistener);
        this.addauditbutton = new Button(this);
        this.addauditbutton.setText("记帐");
        this.addauditbutton.setTextSize((float) UIAdapter.getTextSize());
        this.addauditbutton.setTextColor(Color.WHITE);
        this.addauditbutton.setBackgroundColor(Color.TRANSPARENT);
        this.addauditbutton.setGravity(Gravity.CENTER);
        this.addauditbutton.setOnTouchListener(touchlistener);
        this.menubutton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					MainActivity.this.openContextMenu(MainActivity.this.menubutton);
				}
			});
        this.addauditbutton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					MainActivity.this.openContextMenu(MainActivity.this.addauditbutton);
				}
			});
        navi.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					MainActivity.this.returnToNavi();
				}
			});
        this.backbutton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					MainActivity.this.cancelAction();
				}
			});
        LinearLayout.LayoutParams layoutparam = new LinearLayout.LayoutParams(UIAdapter.width / 4, UIAdapter.getToolBarHeight());
        toolbar.addView(navi, layoutparam);
        toolbar.addView(this.menubutton, layoutparam);
        toolbar.addView(this.addauditbutton, layoutparam);
        toolbar.addView(this.backbutton, layoutparam);
        return toolbar;
    }

	
  	void returnToNavi()
	{
		Intent intent = new Intent();
		intent.setClass(this, NaviActivity.class);
		startActivity(intent);
	}

	/*void cancelAction()
	{
		if ((function != 113) || (paramstack.size() != 1))
		{
	/*	     if ((function != 164) || (paramstack.size() != 1))
		     {
		         if (function != 127)
		         {*/
		            /* if (function != 100)
		             {
			             if (paramstack.size() != 0)
			             {
				             popParams();
			             }
			             else
			             {
				             tab_index = 0;
				             display(100);
			             }
		            }
		            else
			            backupWhenFinish();
		/*	}
		 else
		 {
		 InvestAccount localInvestAccount = new InvestAccount(((int[])paramstack.get(0))[3]);
		 popParams();
		 if (localInvestAccount.type <= 4)
		 innerDisplay();
		 else
		 returnToDetail(109);
		 }
		 }
		 else
		 {
		 popParams();
		 returnToDetail(104);
		 }*/
		/* }
		 else
		 {
		     popParams();
		     returnToDetail(101);
		 }
	}*/
	void cancelAction() {
        if (function == Function.LIST_VIREMENT_AUDIT && paramstack.size() == 1) {
            popParams();
            returnToDetail(Function.LIST_CURRENT_REPORT);
        } else if (function == Function.LIST_CREDIT_ACCOUNT_AUDIT && paramstack.size() == 1) {
            popParams();
            returnToDetail(Function.LIST_ABOUT);
        } else if (function == Function.LIST_INVEST_AUDIT) {
         //   InvestAccount investaccount = new InvestAccount(((int[]) paramstack.get(0))[3]);
            popParams();
            /*if (investaccount.type > (short) 4) {
                returnToDetail(Function.LIST_KM_STAT);
            } else {
                innerDisplay();
            }*/
        } else if (function == Function.LIST_MAIN_PAGE) {
            backupWhenFinish();
        } else if (paramstack.size() == 0) {
            tab_index = 0;
            display(Function.LIST_MAIN_PAGE);
        } else {
            popParams();
        }
    }

	//移除栈顶项
	public static void popParams()
	{
		params = (int[])paramstack.pop();//
		function = params[9];
		RuntimeInfo.main.innerDisplay();
	}

	
	public void backupWhenFinish() {
        if (RuntimeInfo.param.flag[tab_selected_index] != (byte) 1) {
            finish();
        } else if (RuntimeInfo.param.flag[16] == (byte) 1) {
            new AlertDialog.Builder(this).setTitle("退出操作").setMessage("是否备份?").setPositiveButton("备份", new DialogInterface.OnClickListener()
				{ 
					public void onClick(DialogInterface dialog, int whichButton) {
						//DBTool.dataBackupLocal(this.val$content, RuntimeInfo.param.flag[17], "jzac_backup.db", true, "");
						MainActivity.this.finish();
					
				}

				}).setNegativeButton("不备份", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int whichButton) {
						MainActivity.this.finish();
					}

				}).show();
        } else if (RuntimeInfo.param.flag[16] == (byte) 2) {
          //  DBTool.dataBackupLocal(this, RuntimeInfo.param.flag[17], "jzac_backup.db", true, "");
            finish();
        } else {
            finish();
        }
    }

	
  	
	boolean isChart(int functionid) {
        if (functionid == Function.DETAIL_STOCK 
		|| functionid == Function.DETAIL_STOCK 
		|| functionid == Function.CONTENT_ANALYSIS_SZDBT 
		|| functionid == Function.CONTENT_ANALYSIS_ZCFXT 
		|| functionid == Function.CONTENT_ANALYSIS_SRFXT 
		|| functionid == Function.CONTENT_ANALYSIS_ZCBHT) {
            return true;
        }
        return false;
    }

	
   
	public void changeBackground() {
        LinearLayout toolbar = (LinearLayout) this.addauditbutton.getParent();
        LinearLayout title_bar = (LinearLayout) this.titlebar.getParent();
        if (RuntimeInfo.param.flag[51] == 0) {
            toolbar.setBackgroundResource(R.drawable.titlebg);
            title_bar.setBackgroundResource(R.drawable.titlebg);
            this.lv.setBackgroundColor(Color.WHITE);
            this.lv.setCacheColorHint(Color.WHITE);
            return;
        }
        toolbar.setBackgroundResource(R.drawable.titlebgblack);
        title_bar.setBackgroundResource(R.drawable.titlebgblack);
        this.lv.setBackgroundColor(Color.BLACK);//黑色-16777216
        this.lv.setCacheColorHint(Color.BLACK);
    }



 /////////////主体显示////////////////
	/*
	 *列表显示是由列表适配器listadapter负责 
	 *列表内容由ListRowView根据序列index显示
	 */
    /*private void innerDisplay()
	{
		index.clear();
		if ((paramstack.size() != 0) || (function != 100))
			this.backbutton.setText("返回");
		else
			this.backbutton.setText("退出");
		fillListData(1);
	}*/
	private void innerDisplay() {
        index.clear();
        if (paramstack.size() == 0 && function == 100) {
            this.backbutton.setText("退出");
        } else {
            this.backbutton.setText("返回");
        }
        fillListData(FILL_ALL);
    }

	

  
    private void fillListData(int paramInt)
    {
        this.fill_mode = paramInt;
        switch (function)
        {
			case 100://首页
                mainPageList();
                break;

			case 115://银行列表
				newDepositList();
				break;
			case 116://转帐流水
				virementAuditList();
				break;
				////////
			case 105://收支流水
				//	virementAuditList();
				auditList();
				break;
			
			case 110:
		  //  case 122:
		 //   case 123:
		      reportByKmList();
		      break;
			case 125://科目列表
                kmList();
                break;	
			
			case 120:
		    case 121:
		    case 122:
		    case 123:
		    case 124:
		      analysisChart(function);
		      break;	
                
                
                
                ////////
			case 145://借贷
                creditList();
                break;
			
			case 146:
			case 164:
				creditAuditList();
				break;
	
				
	        
	        case 150://投资
           //    investAccountList();
                break;
            
            //////
            case 157://科目列表
                assetTypeList();
                break;	
           
            case 155://资产
               assetList();
               break;
            case 158://资产隐藏列表
                assetHideList();
                break;	   
			case 156://资产流水
				assetAuditList();
				break;
			case 159://资产总流水	
				assetAuditZList();
				break;
				///////
			case 140://人情
                favorList();
                break;
	        case 141://项目流水
				favorAuditList();
				break;
		    ////////////
			case 135://项目列表
		//	case 155:
				evectionList();
				break;	
			case 136://项目流水
				evectionAuditList();
				break;
			case 137://项目科目
				projectKmList();
				break;
		    case 138://项目统计
				evectionStatList();
				break;
		    
		    case 130://系统
		        aboutList();
		        break;
				
        }
    }
	/*private void fillListData(int fill_mode) {
        this.fill_mode = fill_mode;
        switch (function) {
            case Function.LIST_MAIN_PAGE /*99*
                mainPageList();
            case Function.LIST_AUDIT /*100*
            case Function.LIST_AUDIT_SUPER_MARKET /*149*
            case Function.LIST_AUDIT_IN /*150*
            case Function.LIST_AUDIT_OUT /*151*
            case Function.LIST_AUDIT_FAVOR /*152*
            case Function.LIST_AUDIT_LOTTERY /*153*
            case Function.LIST_AUDIT_MAJIANG /*154*
                auditList();
            case Function.LIST_CURRENT_REPORT /*101*
            case Function.LIST_MONTH_REPORT_CONTENT /*122*
            case Function.LIST_YEAR_REPORT_CONTENT /*123*
                reportByKmList();
            case Function.LIST_DEPOSIT /*102*
                newDepositList();
            case Function.LIST_KM /*103*
                kmList();
            case Function.LIST_ABOUT /*104*
                aboutList();
            case Function.LIST_BANK /*105*
                bankList();
            case Function.LIST_INTEREST_RATE /*106*
                interestRateList();
            case Function.LIST_ASSET_DEBT /*107*
                assetDebtList();
            case Function.LIST_FIND_AUDIT /*108*
                findAuditList();
            case Function.LIST_KM_STAT /*109*
                kmStatList();
            case Function.LIST_PLAN /*110*
                planList();
            case Function.DETAIL_STOCK /*111*
            case Function.CONTENT_ANALYSIS_SZDBT /*139*
            case Function.CONTENT_ANALYSIS_ZCFXT /*140*
            case Function.CONTENT_ANALYSIS_SRFXT /*141*
            case Function.CONTENT_ANALYSIS_ZCBHT /*142*
                analysisChart(function);
            case Function.LIST_CURRENT_CARD_CHILDREN /*112*
                currentCardChildrenList();
            case Function.LIST_VIREMENT_AUDIT /*113*
                virementAuditList();
            case Function.LIST_CREDIT /*114*
                creditList();
            case Function.LIST_CREDIT_AUDIT /*115*
            case Function.LIST_CREDIT_ACCOUNT_AUDIT /*164*
                creditAuditList();
            case Function.LIST_EVECTION /*116*
            case Function.LIST_AUDIT_PROJECT /*155*
                evectionList();
            case Function.LIST_EVECTION_KM /*117*
                evectionKmList();
            case Function.LIST_EVECTION_AUDIT /*118*
                evectionAuditList();
            case Function.LIST_EVECTION_STAT /*119*
                evectionStatList();
            case Function.LIST_MONTH_REPORT /*120*
                monthReportList();
            case Function.LIST_YEAR_REPORT /*121*
                yearReportList();
            case Function.LIST_INVEST /*124*
                investAccountList();
            case Function.LIST_ASSET /*125*
                assetList();
            case Function.LIST_INVEST_TYPE /*126*
                investTypeList();
            case Function.LIST_INVEST_AUDIT /*127*
                investAuditList();
            case Function.LIST_INVEST_PROFIT /*128*
                investProfitList();
            case Function.LIST_STOCK /*129*
                stockList();
            case Function.LIST_FUNDS /*130*
                fundsList();
            case Function.LIST_BOND /*131*
                bondList();
            case Function.LIST_INSURANCE /*132*
                insuranceList();
            case Function.LIST_BUDGET /*133*
                budgetList();
            case Function.LIST_BUDGET_CONTENT /*134*
                budgetByKmList();
            case Function.LIST_MEMO /*135*
                memoList();
            case Function.LIST_ASSET_AUDIT /*136*
                assetAuditList();
            case Function.LIST_KM_AUDIT /*137*
                kmAuditList();
            case Function.LIST_FAVOR /*144*
                favorList();
            case Function.LIST_FAVOR_TYPE /*145*
                favorTypeList();
            case Function.LIST_FRIEND /*146*
                friendList();
            case Function.LIST_PROJECT_KM /*147*
                projectKmList();
            case Function.LIST_AUDIT_SUPER_MARKET_ORDER /*148*
                auditSuperMarketList();
            case Function.LIST_FRIEND_TYPE /*159*
                friendTypeList();
            case Function.LIST_FIXED_CARD_CHILDREN /*160*
                fixedCardChildrenList();
            case Function.LIST_SAFE /*161*
                safeList();
            case Function.LIST_SAFE_TYPE /*162*
                safeTypeList();
            case Function.LIST_DEPOSIT_RANK /*163*
                depositRankList();
            case Function.LIST_FAVOR_STAT /*165*
                favorStatList();
            case Function.LIST_FAVOR_STAT_AUDIT /*166
                favorStatAuditList();
            default:
        }
    }*/

    
	void mainPageList()
	{
		setTitle("君子爱财");
	//	if (Param.MODE != 2)
	//		Memo.retrieve();
		RuntimeInfo.param.fillMainPageList(this.listadapter, index);
	}
	
	void auditList()
	{
		setTitle("收支流水");
		String str = "";
		switch (function)
		{
			case 110:
				str = "vid>=0";
				break;

		}
		fillListPage(Audit.getRows(str, "date desc"));
	}
	
	void newDepositList()
	{
		setTitle("资金管理");
		ListAdapter lisadp = this.listadapter;
		int[] lay = new int[4];
		lay[0] = 20;
		lay[1] = 100;
		lay[2] = 100;
		lay[3] = 100;
		lisadp.setLayout(lay);
		Deposit.getAdapter(this.listadapter, index);
	}
	
	void virementAuditList()
	{
		int i = ((int[])paramstack.get(0))[3];
		setTitle("转账流水");
		fillListPage(Virement.getRows("deposit_from=" + i + " or ((kmid=" + 3 + " or kmid=" + 12 + " or kmid=" + 11 + " or kmid=" + 9 + " or kmid=" + 39 + ") and deposit_to=" + i + ")", "date desc"));
	}
	void kmList()
    {
        setTitle("科目字典");
        KM.fillList(this.listadapter, index);
    }
	void analysisChart(int function)
	{
	    switch (function)
	    {
	    case 120:
	      setTitle("资产负债图");
	      break;
	    case 121:
	      setTitle("收支对比图");
	      break;
	    case 122:
	      setTitle("支出结构图");
	      break;
	    case 123:
	      setTitle("收入结构图");
	      break;
	    case 124:
	      setTitle("资产变化图");
	    }
	    this.chartview.display(UIAdapter.width, UIAdapter.getTableHeight(), function);
	}
	
	void reportByKmList()
	  {
	    Report localReport = null;
	    //if (function != 122)
	  //  {
	  //    if (function != 123)
	  //    {
	        if (function == 110)
	          localReport = new Report(2, MyDate.now());
	     // }
	     // else
	    //    localReport = new Report(((int[])paramstack.get(0))[3]);
	  //  }
	  //  else
	  //    localReport = new Report(((int[])paramstack.get(0))[3]);
	    setTitle(localReport.getDateString() + "报表");
	    int[] arrayOfInt = params;
	    int i = 0;
	    arrayOfInt[0] = i;
	    if ((paramstack.size() > 1) || ((paramstack.size() == 1) && (((int[])paramstack.get(-1 + paramstack.size()))[9] == 110)))
	    {
	      arrayOfInt = params;
	      i = ((int[])paramstack.get(-1 + paramstack.size()))[3];
	      arrayOfInt[0] = i;
	    }
	    localReport.fillReport(this.listadapter, i, index);
	  }
	
	//////////////
	void creditList()
	{
		setTitle("借贷管理");
		ListAdapter listadapter = this.listadapter;
		int[] width_layout = new int[4];
		width_layout[0] = 20;
		width_layout[1] = 80;
		width_layout[2] = 100;
		width_layout[3] = 120;
		listadapter.setLayout(width_layout);
		Credit.getAdapter(this.listadapter, index);
	}
	void creditAuditList()
	{
		setTitle("借贷流水");
		String str = "";
		if (function == 146)
			str = "credit_id=" + ((int[])paramstack.get(0))[3];
		fillListPage(CreditAudit.getRows(str, "date desc"));
	}
	/////
	void assetList()
	{
		setTitle("资产管理");
		ListAdapter listadapter = this.listadapter;
		int[] width_layout = new int[5];
		width_layout[0] = 10;
		width_layout[1] = 90;
		width_layout[2] = 80;
		width_layout[3] = 80;
		width_layout[4] = 60;
		listadapter.setLayout(width_layout);
		Asset.getAdapter(this.listadapter, index);
				
	}
	
	void assetHideList()
	{
		setTitle("隐藏管理");
		ListAdapter listadapter = this.listadapter;
		int[] width_layout = new int[5];
		width_layout[0] = 10;
		width_layout[1] = 90;
		width_layout[2] = 80;
		width_layout[3] = 80;
		width_layout[4] = 60;
		listadapter.setLayout(width_layout);
		Asset.getHideAdapter(this.listadapter, index);
				
	}
	
	void assetAuditList()
    {
		setTitle(new Asset(((int[])paramstack.get(0))[3]).city);		
		fillListPage(AssetAudit.getRows("eid=" + ((int[])paramstack.peek())[3], "date desc"));
   }
	void assetAuditZList()
    {
		setTitle("资产流水");		
		fillListPage(AssetAudit.getRows("", "date desc"));
   }
	void assetTypeList()
    {
        setTitle("资产类型");
        AssetKm.fillList(this.listadapter, index);
    }
	////////
	void favorList()
	{
		setTitle("人情管理");
		Cursor localCursor = Favor.getRows("flag>=0 and flag<4", "date desc");
		fillListPage(localCursor);
	}
	void favorAuditList()
    {
        setTitle(new Favor(((int[])paramstack.get(0))[3]).city);
        fillListPage(FavorAudit.getRows("eid=" + ((int[])paramstack.peek())[3], "date desc"));
    }
	//////////////////////
	void evectionList()
	{
		setTitle("项目管理");
		Cursor localCursor = Evection.getRows("flag>=0 and flag<4", "date desc");
		fillListPage(localCursor);
	}
	
	void evectionAuditList()
    {
        setTitle(new Evection(((int[])paramstack.get(0))[3]).city);
        fillListPage(EvectionAudit.getRows("eid=" + ((int[])paramstack.peek())[3], "date desc"));
    }

	void projectKmList()
	{
		Evection localEvection = new Evection(((int[])paramstack.get(0))[3]);
		setTitle("项目收支科目");
		localEvection.fillProjectKmList(this.listadapter, index);
	}
	
	void evectionStatList()
	{
		Evection localEvection = new Evection(((int[])paramstack.get(0))[3]);
		setTitle("项目统计");
		localEvection.fillStatList(this.listadapter, index);
	}
	/////////
	void aboutList()
	{
	    setTitle("系统功能 ");
	    Param.fillAboutList(this.listadapter, index);
	}
	
	void setTitle(String paramString)
	{
		this.titlebar.setText(paramString);
	}

   /* private void fillListPage(Cursor cursor)
    {
        params[2] = ((-1 + cursor.getCount()) / Param.NUMBER_PER_PAGE);
        cursor.moveToFirst();
        int j = Param.NUMBER_PER_PAGE;
        if (this.fill_mode != 0)
            j = Param.NUMBER_PER_PAGE * (1 + params[0]);
        else
            for (int i = 0; i < params[0] * Param.NUMBER_PER_PAGE; i++)
            	cursor.moveToNext();
        ArrayList listdata = new ArrayList();
        
		if ((!cursor.isFirst()) && (cursor.getCount() != 0))
        {
            index.remove(-1 + index.size());
            this.listadapter.remove(-1 + this.listadapter.getCount());
        }
        else
        {
            index.clear();
            this.listadapter.clear();
            index.add(Integer.valueOf(2147483394));
        }
    
		String[] tile;
		String[] data;
		int[] layout_width;
		String zf = null;
		
        switch (function)
        {

            default:
                data = new String[1];
                data[0] = "此页没有定义";
                listdata.add(data);
                index.add(Integer.valueOf(2147483394));
                break;
			//收支流水
            case 105:
			
				 tile = new String[3];
				 tile[0] = "日期";
				 tile[1] = "科目";
				 tile[2] = "金额";
				 listdata.add(tile);
				 
				// this.listadapter = ListAdapter(this);
				 layout_width = new int[4];
				 layout_width[0] = 20;
				 layout_width[1] = 70;
				 layout_width[2] = 110;
				 layout_width[3] = 120;
				 listadapter.setLayout(layout_width);
				 for (int pag_aud = 0;(pag_aud < j) && (!cursor.isAfterLast());pag_aud++)
				 {
				     data = new String[3];
				     data[0] = new MyDate(new Date(cursor.getLong(5))).toShortString();
				     data[1] = Audit.getKm2Name(cursor.getShort(1), cursor.getInt(0));
	                 String zf_aud;
			         if (cursor.getShort(1) != 320)
				     {
				         if (cursor.getShort(1) < 275)
				             zf = "-";
				         else
				             zf = "+";
				     }
				     else
				         zf_aud = "";
				     data[2] = (zf + Convertor.sumToString(cursor.getLong(2)));
				     listdata.add(data);
				     index.add(Integer.valueOf(cursor.getInt(0)));
				     cursor.moveToNext();
				 }
				

				
			    break;
			case 116://银行转帐流水
				tile = new String[3];
				tile[0] = "日期";
				tile[1] = "科目";
				tile[2] = "金额";
				listdata.add(tile);
		
		       // ListAdapter adp_vir = this.listadapter;
		        int[] lay_vir = new int[4];
		        lay_vir[0] = 20;
		        lay_vir[1] = 80;
		        lay_vir[2] = 100;
		        lay_vir[3] = 120;
		        listadapter.setLayout(lay_vir);
		
		        int k = ((int[])paramstack.get(0))[3];
		
		        for (int pag_vir = 0;(pag_vir < j) && (!cursor.isAfterLast()); pag_vir++)
		        {
			        data = new String[3];
			        data[0] = new MyDate(new Date(cursor.getLong(5))).toShortString();
			        data[1] = Virement.getVirementType(cursor.getShort(1));
			        String zf_vir;
					if ((k == cursor.getShort(3)) && (!Virement.isIncome(cursor.getShort(1))))
                       zf = "-";
			        else
                       zf= "+";
					data[2] = (zf + Convertor.sumToString(cursor.getLong(2)));
			        listdata.add(data);
			        index.add(Integer.valueOf(cursor.getInt(0)));
			        cursor.moveToNext();
	            }
			    break;
			case 149://借贷总流水
			case 146://借贷分流水
			
				 String[] til_creA = new String[3];
				 til_creA[0] = "日期";
				 til_creA[1] = "科目";
				 til_creA[2] = "金额";
				 listdata.add(til_creA);
				 
			     ListAdapter adp_creA = this.listadapter;
				 int[] lay_creA= new int[4];
				 lay_creA[0] = 20;
				 lay_creA[1] = 80;
				 lay_creA[2] = 100;
				 lay_creA[3] = 120;
				 adp_creA.setLayout(lay_creA);
				 
				 for (int pag_creA = 0;(pag_creA < j) && (!cursor.isAfterLast());pag_creA++)
				 {
				     String[] dat_creA = new String[3];
				     dat_creA[0] = new MyDate(new Date(cursor.getLong(6))).toShortString();
				     dat_creA[1] = CreditAudit.getTypeName(cursor.getShort(1));
				     dat_creA[2] = Convertor.sumToString(cursor.getLong(2));
				     listdata.add(dat_creA);
				     index.add(Integer.valueOf(cursor.getInt(0)));
				     cursor.moveToNext();
				 }
				break;
			
			
			case 156://资产流水列表
			    //标题
			        tile = new String[4];
			        tile[0] = "日期";
			        tile[1] = "科目";
			        tile[2] = "数量";
			        tile[3] = "金额";
	                listdata.add(tile);
	      
	            //    ListAdapter adapter_assA = this.listadapter;
	        
	                layout_width = new int[5];
	                layout_width[0] = 10;
	                layout_width[1] = 60;
	                layout_width[2] = 80;
	                layout_width[3] = 80;
	                layout_width[4] = 90;
			 
			        listadapter.setLayout(layout_width);
	        
			        Asset asset = new Asset(((int[])paramstack.get(0))[3]);
	         
	       
	                for (int count_assA=0; (count_assA < j) && (!cursor.isAfterLast());count_assA++)
			        {
			    	
	                    int m = cursor.getInt(2);
	                    
				        if (m != 1)
	                        if (!AssetAudit.isIncome(m))
	                            zf = "-";
	                        else
	                            zf = "+";
	                    data = new String[4];
	                    data[0] = new MyDate(new Date(cursor.getLong(6))).toShortString();
	                    data[1] = asset.getKmName(m);
	                    data[2] = (zf + Convertor.sumToStringf(cursor.getFloat(3)));
	                    data[3] = (zf + Convertor.sumToStringf(cursor.getFloat(4)));
	                    listdata.add(data);
	                    index.add(Integer.valueOf(cursor.getInt(0)));
	                    cursor.moveToNext();
			        }
	                break;
			case 159://资产流水总列表
			    //标题
			        tile = new String[4];
			        tile[0] = "日期";
			        tile[1] = "科目";
			        tile[2] = "数量";
			        tile[3] = "金额";
	                listdata.add(tile);
	      
	                layout_width = new int[5];
	                layout_width[0] = 10;
	                layout_width[1] = 60;
	                layout_width[2] = 80;
	                layout_width[3] = 80;
	                layout_width[4] = 90;
			 
			        listadapter.setLayout(layout_width);
       
	                for (int count_assAZ=0; (count_assAZ < j) && (!cursor.isAfterLast());count_assAZ++)
			        {
	                	int m = cursor.getInt(2);
	                    
				        if (m != 1)
	                        if (!AssetAudit.isIncome(m))
	                            zf = "-";
	                        else
	                            zf = "+";
	                    data = new String[4];
	                    data[0] = new MyDate(new Date(cursor.getLong(6))).toShortString();
	                    data[1] = Asset.getName(cursor.getInt(1));
	                    data[2] = (zf + Convertor.sumToStringf(cursor.getFloat(3)));
	                    data[3] = (zf + Convertor.sumToStringf(cursor.getFloat(4)));
	                    listdata.add(data);
	                    index.add(Integer.valueOf(cursor.getInt(0)));
	                    cursor.moveToNext();
			        }
	                break;
			case 140://人情列表
			       tile = new String[3];
			       tile[0] = "名称";
			       tile[1] = "结余金额";
			       tile[2] = "时间";
	               listdata.add(tile);
	                    
	              // ListAdapter adapter_evention = this.listadapter;
	               
				   layout_width = new int[4];
				   layout_width[0] = 20;
				   layout_width[1] = 110;
				   layout_width[2] = 100;
				   layout_width[3] = 90;
	               listadapter.setLayout(layout_width);
	               
				   for (int count_evention = 0; (count_evention < j) && (!cursor.isAfterLast());count_evention++ )
	               {
	                  data = new String[3];
	                  data[0] = cursor.getString(3);
	                  data[1] =Convertor.sumToString(cursor.getLong(1));
	                  data[2] = new MyDate(new Date(cursor.getLong(2))).toShortString();
	                  listdata.add(data);
	                  index.add(Integer.valueOf(cursor.getInt(0)));
	                  cursor.moveToNext();
	              }
				  break;
			case 141://人情流水
			      tile = new String[3];
			      tile[0] = "日期";
			      tile[1] = "科目";
			      tile[2] = "金额";
	              listdata.add(tile);
	                    
	             // ListAdapter adapter_ea = this.listadapter;
	              layout_width = new int[4];
	              layout_width[0] = 20;
	              layout_width[1] = 80;
	              layout_width[2] = 100;
	              layout_width[3] = 120;
	              listadapter.setLayout(layout_width);
	              
				  
	              Favor favor = new Favor(((int[])paramstack.get(0))[3]);
	               
	              for (int count_eventionaudit=0; (count_eventionaudit < j) && (!cursor.isAfterLast());count_eventionaudit++)
	              {
	                  int kmid = cursor.getInt(2);
	                  
					  if (kmid != 1)
	                      if (!FavorAudit.isIncome(kmid))
	                          zf = "-";
	                        else
	                          zf = "+";
	                  data = new String[3];
	                  data[0] = new MyDate(new Date(cursor.getLong(5))).toShortString();
	                  data[1] = favor.getKmName(kmid);
	                  data[2] = (zf + Convertor.sumToString(cursor.getLong(3)));
	                  listdata.add(data);
	                  index.add(Integer.valueOf(cursor.getInt(0)));
	                  cursor.moveToNext();
	              }      
	              break;        
			case 135://项目列表
		       tile = new String[3];
		       tile[0] = "名称";
		       tile[1] = "结余金额";
		       tile[2] = "时间";
               listdata.add(tile);
                    
              // ListAdapter adapter_evention = this.listadapter;
               
			   layout_width = new int[4];
			   layout_width[0] = 20;
			   layout_width[1] = 110;
			   layout_width[2] = 100;
			   layout_width[3] = 90;
               listadapter.setLayout(layout_width);
               
			   for (int count_evention = 0; (count_evention < j) && (!cursor.isAfterLast());count_evention++ )
               {
                  data = new String[3];
                  data[0] = cursor.getString(3);
                  data[1] =Convertor.sumToString(cursor.getLong(1));
                  data[2] = new MyDate(new Date(cursor.getLong(2))).toShortString();
                  listdata.add(data);
                  index.add(Integer.valueOf(cursor.getInt(0)));
                  cursor.moveToNext();
              }
			  break;
		  case 136://项目流水
		      tile = new String[3];
		      tile[0] = "日期";
		      tile[1] = "科目";
		      tile[2] = "金额";
              listdata.add(tile);
                    
             // ListAdapter adapter_ea = this.listadapter;
              layout_width = new int[4];
              layout_width[0] = 20;
              layout_width[1] = 80;
              layout_width[2] = 100;
              layout_width[3] = 120;
              listadapter.setLayout(layout_width);
              
			  
              Evection evection = new Evection(((int[])paramstack.get(0))[3]);
               
              for (int count_eventionaudit=0; (count_eventionaudit < j) && (!cursor.isAfterLast());count_eventionaudit++)
              {
                  int kmid = cursor.getInt(2);
                  
				  if (kmid != 1)
                      if (!EvectionAudit.isIncome(kmid))
                          zf = "-";
                        else
                          zf = "+";
                  data = new String[3];
                  data[0] = new MyDate(new Date(cursor.getLong(5))).toShortString();
                  data[1] = evection.getKmName(kmid);
                  data[2] = (zf + Convertor.sumToString(cursor.getLong(3)));
                  listdata.add(data);
                  index.add(Integer.valueOf(cursor.getInt(0)));
                  cursor.moveToNext();
              }      
              break;      
                  
          }
		  
/*    for (String[] arrayOfString2 = 0; ; arrayOfString2++)
    {
      if ((arrayOfString2 >= arrayOfString1) || (paramCursor.isAfterLast()))
      {
        if ((paramCursor.isAfterLast()) || (paramCursor.getCount() <= 0))
        {
          if (paramCursor.getCount() > Param.NUMBER_PER_PAGE)
          {
            arrayOfString1 = new String[1];
            arrayOfString1[0] = "没有了";
            localArrayList.add(arrayOfString1);
            index.add(Integer.valueOf(2147483393));
          }
        }
        else
        {
          arrayOfString1 = new String[1];
          arrayOfString1[0] = "查看更多...";
          localArrayList.add(arrayOfString1);
          index.add(Integer.valueOf(2147483397));
        }*/
     /*   cursor.close();
        this.listadapter.append(listdata);

    }*/
	private void fillListPage(Cursor cur) {
        int i;
        String[] obj;
        params[tab_selected_index] = (cur.getCount() -1) / Param.NUMBER_PER_PAGE;
        cur.moveToFirst();
        int row_count = Param.NUMBER_PER_PAGE;
        if (this.fill_mode == 0) {
            for (i = 0; i < params[FILL_PAGE] * Param.NUMBER_PER_PAGE; i++) {
                cur.moveToNext();
            }
        } else {
            row_count = Param.NUMBER_PER_PAGE * (params[FILL_PAGE] + FILL_ALL);
        }
        List<String[]> listdata = new ArrayList();
        if (cur.isFirst() || cur.getCount() == 0) {
            index.clear();
            this.listadapter.clear();
            index.add(Integer.valueOf(ID_LIST_TITLE));
        } else {
            index.remove(index.size() -1);
            this.listadapter.remove(this.listadapter.getCount() -1);
        }
        Object obj2;
        switch (function) {
            case Function.LIST_AUDIT /*100*/:
            case Function.LIST_FIND_AUDIT /*108*/:
            case Function.LIST_KM_AUDIT /*137*/:
            case Function.LIST_AUDIT_SUPER_MARKET_ORDER /*148*/:
            case Function.LIST_AUDIT_SUPER_MARKET /*149*/:
            case Function.LIST_AUDIT_IN /*150*/:
            case Function.LIST_AUDIT_OUT /*151*/:
            case Function.LIST_AUDIT_FAVOR /*152*/:
            case Function.LIST_AUDIT_LOTTERY /*153*/:
            case Function.LIST_AUDIT_MAJIANG /*154*/:
               /* if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u65e5\u671f", "\u79d1\u76ee", "\u91d1\u989d"});
                }
                this.listadapter.setLayout(new int[]{20, 70, Function.LIST_PLAN, Function.LIST_MONTH_REPORT});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    obj2 = new String[3];
                    obj2[FILL_PAGE] = new FDate(new Date(cur.getLong(5))).toShortString();
                    obj2[FILL_ALL] = Audit.getKm2Name(cur.getShort(FILL_ALL), cur.getInt(FILL_PAGE));
                    obj = cur.getShort(FILL_ALL) == KM.KM_ZC_SUPER_MARKET ? "" : (cur.getShort(FILL_ALL) < KM.KM_INCOME_BASE || cur.getShort(FILL_ALL) > KM.MAX_KM_NUMBER) ? "-" : "+";
                    obj2[tab_selected_index] = new StringBuilder(String.valueOf(obj)).append(Convertor.sumToString(cur.getLong(tab_selected_index))).toString();
                    listdata.add(obj2);
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_DEPOSIT /*102*/:
            case Function.LIST_CURRENT_CARD_CHILDREN /*112*/:
            case Function.LIST_FIXED_CARD_CHILDREN /*160*/:
               /* if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u7b80\u79f0", "\u7c7b\u578b", "\u4f59\u989d"});
                }
                this.listadapter.setLayout(new int[]{10, 100, 100, Function.LIST_PLAN});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    Object obj3 = new String[3];
                    obj3[FILL_PAGE] = cur.getString(FILL_ALL);
                    obj3[FILL_ALL] = Deposit.getDepositTypeName(cur.getShort(tab_selected_index));
                    long totalSum = (cur.getShort(tab_selected_index) == (short) 25 && function == Function.LIST_DEPOSIT) ? Deposit.getTotalSum(cur.getInt(FILL_PAGE)) : cur.getLong(3);
                    obj3[tab_selected_index] = Convertor.sumToString(totalSum);
                    listdata.add(obj3);
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_BANK /*105*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u673a\u6784\u540d\u79f0", "\u7c7b\u578b", "\u7535\u8bdd"});
                }
                this.listadapter.setLayout(new int[]{20, Function.LIST_MONTH_REPORT, 80, 100});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{cur.getString(tab_selected_index), Bank.getBankTypeBame(cur.getShort(FILL_ALL)), cur.getString(3)});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_VIREMENT_AUDIT /*113*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u65e5\u671f", "\u79d1\u76ee", "\u91d1\u989d"});
                }
                this.listadapter.setLayout(new int[]{20, 80, 100, Function.LIST_MONTH_REPORT});
                short deposit_id = ((int[]) paramstack.get(FILL_PAGE))[3];
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    obj2 = new String[3];
                    obj2[FILL_PAGE] = new FDate(new Date(cur.getLong(5))).toShortString();
                    obj2[FILL_ALL] = Virement.getVirementType(cur.getShort(FILL_ALL));
                    obj = (deposit_id != cur.getShort(3) || Virement.isIncome(cur.getShort(FILL_ALL))) ? "+" : "-";
                    obj2[tab_selected_index] = new StringBuilder(String.valueOf(obj)).append(Convertor.sumToString(cur.getLong(tab_selected_index))).toString();
                    listdata.add(obj2);
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_CREDIT /*114*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u540d\u79f0", "\u7c7b\u578b", "\u91d1\u989d"});
                }
                this.listadapter.setLayout(new int[]{20, 100, 80, Function.LIST_MONTH_REPORT});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{cur.getString(7), Credit.getTypeShortName(cur.getShort(FILL_ALL)), Convertor.sumToString(cur.getLong(tab_selected_index))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_CREDIT_AUDIT /*115*/:
            case Function.LIST_CREDIT_ACCOUNT_AUDIT /*164*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u65e5\u671f", "\u79d1\u76ee", "\u91d1\u989d"});
                }
                this.listadapter.setLayout(new int[]{20, 80, 100, Function.LIST_MONTH_REPORT});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{new FDate(new Date(cur.getLong(6))).toShortString(), CreditAudit.getTypeName(cur.getShort(FILL_ALL)), Convertor.sumToString(cur.getLong(tab_selected_index))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_EVECTION /*116*/:
            case Function.LIST_AUDIT_PROJECT /*155*/:
                /*if (this.listadapter.getCount() == 0) {
                    if (function == Function.LIST_EVECTION) {
                        listdata.add(new String[]{"\u540d\u79f0", "\u7ed3\u4f59\u91d1\u989d", "\u65f6\u95f4"});
                    } else {
                        listdata.add(new String[]{"\u540d\u79f0", "\u603b\u652f\u51fa", "\u65f6\u95f4"});
                    }
                }
                this.listadapter.setLayout(new int[]{20, Function.LIST_PLAN, 100, 90});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{cur.getString(3), Convertor.sumToString(cur.getLong(FILL_ALL)), new FDate(new Date(cur.getLong(tab_selected_index))).toShortString()});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case 156/*Function.LIST_EVECTION_AUDIT*/ /*118*/:
                if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"日期", "科目", "数量","金额"});
                }
                this.listadapter.setLayout(new int[]{10, 60, 80,80,90 /*Function.LIST_MONTH_REPORT*/});
                String prefix = "";
                Asset evection = new Asset(((int[]) paramstack.get(FILL_PAGE))[3]);
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    int kmid = cur.getInt(tab_selected_index);
                    if (kmid != FILL_ALL) {
                        if (AssetAudit.isIncome(kmid)) {
                            prefix = "+";
                        } else {
                            prefix = "-";
                        }
                    }
                    listdata.add(new String[]{new MyDate(new Date(cur.getLong(6))).toShortString(),
					             evection.getKmName(kmid), 
					             new StringBuilder(String.valueOf(prefix)).append(Convertor.sumToStringf(cur.getFloat(3))).toString(),
					             new StringBuilder(String.valueOf(prefix)).append(Convertor.sumToStringf(cur.getFloat(4))).toString()});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }
				
                break;
			
            case Function.LIST_MONTH_REPORT /*120*/:
            case Function.LIST_YEAR_REPORT /*121*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u65e5\u671f", "\u6536\u5165", "\u652f\u51fa"});
                }
                this.listadapter.setLayout(new int[]{20, 80, 100, Function.LIST_MONTH_REPORT});
                Report report = new Report();
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    report.reset(cur);
                    listdata.add(new String[]{report.getDateString(), Convertor.sumToString(report.getKmSum(FILL_ALL)), Convertor.sumToString(report.getKmSum(tab_selected_index))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_INVEST /*124*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u540d\u79f0", "\u54c1\u79cd", "\u603b\u5e02\u503c"});
                }
                this.listadapter.setLayout(new int[]{20, 100, 80, Function.LIST_MONTH_REPORT});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    short type = cur.getShort(tab_selected_index);
                    if ((type == (short) 1 && RuntimeInfo.param.flag[34] == FILL_ALL) || ((type == (short) 2 && RuntimeInfo.param.flag[35] == FILL_ALL) || (type == (short) 3 && RuntimeInfo.param.flag[36] == FILL_ALL))) {
                        cur.moveToNext();
                    } else {
                        if (type == (short) 1) {
                            listdata.add(new String[]{cur.getString(FILL_ALL), new InvestType(type).name, Convertor.sumToString(cur.getLong(5))});
                        } else {
                            listdata.add(new String[]{cur.getString(FILL_ALL), new InvestType(type).name, Convertor.sumToString(cur.getLong(3) + cur.getLong(5))});
                        }
                        index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                        cur.moveToNext();
                    }
                }*/
                break;
            case Function.LIST_ASSET /*125*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(ne1w String[]{"\u540d\u79f0", "\u5e02\u503c", "\u8d2d\u4e70\u65e5\u671f"});
                }
                this.listadapter.setLayout(new int[]{20, 100, 100, 100});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{cur.getString(5), Convertor.sumToString(cur.getLong(tab_selected_index)), new FDate(new Date(cur.getLong(4))).toString()});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_INVEST_AUDIT /*127*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u65e5\u671f", "\u4ea4\u6613", "\u91d1\u989d"});
                }
                this.listadapter.setLayout(new int[]{20, 80, 100, Function.LIST_MONTH_REPORT});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{new FDate(new Date(cur.getLong(7))).toShortString(), InvestAudit.getTypeName(cur.getShort(FILL_ALL)), Convertor.sumToString(cur.getLong(6))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_INVEST_PROFIT /*128*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u540d\u79f0", "\u6295\u5165", "\u76c8\u5229"});
                }
                this.listadapter.setLayout(new int[]{20, 80, Function.LIST_MONTH_REPORT, 100});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{InvestProfit.getStockName(cur), Convertor.sumToString(cur.getLong(3)), Convertor.sumToString(cur.getLong(4) - cur.getLong(3))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_FUNDS /*130*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u540d\u79f0", "\u51c0\u503c", "\u4efd\u989d"});
                }
                this.listadapter.setLayout(new int[]{10, Function.LIST_PLAN, 100, 100});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    String name = cur.getString(3);
                    if (name.length() > 6) {
                        name = name.substring(FILL_PAGE, 6);
                    }
                    if (name.length() == 0) {
                        name = cur.getString(tab_selected_index);
                    }
                    listdata.add(new String[]{name, Convertor.sumToString4(cur.getLong(5)), Convertor.sumToString(cur.getLong(4))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_BOND /*131*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u540d\u79f0", "\u9762\u503c", "\u5229\u7387"});
                }
                this.listadapter.setLayout(new int[]{20, 100, Function.LIST_MONTH_REPORT, 80});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{cur.getString(tab_selected_index), Convertor.sumToString(cur.getLong(3)), Convertor.sumToString3((long) cur.getShort(7))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_INSURANCE /*132*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u540d\u79f0", "\u4fdd\u989d", "\u516c\u53f8"});
                }
                this.listadapter.setLayout(new int[]{20, 80, Function.LIST_MONTH_REPORT, 100});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{cur.getString(tab_selected_index), Convertor.sumToString(cur.getLong(7)), new Bank(cur.getShort(10)).name});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_BUDGET /*133*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u65f6\u95f4", "\u9884\u7b97\u6536\u5165", "\u9884\u7b97\u652f\u51fa"});
                }
                this.listadapter.setLayout(new int[]{10, Function.LIST_PLAN, 90, 90});
                Budget report2 = new Budget();
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    report2.reset(cur);
                    listdata.add(new String[]{report2.getDateString(), Convertor.sumToString(report2.getKmSum(FILL_ALL)), Convertor.sumToString(report2.getKmSum(tab_selected_index))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_MEMO /*135*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u65e5\u671f", "\u7c7b\u578b", "\u91d1\u989d"});
                }
                this.listadapter.setLayout(new int[]{20, 70, Function.LIST_PLAN, Function.LIST_MONTH_REPORT});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{new FDate(new Date(cur.getLong(6))).toShortString(), Memo.getTypeName(cur.getShort(FILL_ALL), cur.getShort(5)), Convertor.sumToString(Memo.getSum(cur.getShort(FILL_ALL), cur.getInt(tab_selected_index), cur.getLong(3)))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_ASSET_AUDIT /*136*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u65e5\u671f", "\u79d1\u76ee", "\u91d1\u989d"});
                }
                this.listadapter.setLayout(new int[]{20, 70, Function.LIST_PLAN, Function.LIST_MONTH_REPORT});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{new FDate(new Date(cur.getLong(5))).toShortString(), Virement.getVirementType(cur.getShort(FILL_ALL)), Convertor.sumToString(cur.getLong(tab_selected_index))});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
				
                break;
            case Function.LIST_FAVOR /*144*/:
            case Function.LIST_FAVOR_STAT_AUDIT /*166*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u65e5\u671f", "\u597d\u53cb", "\u79d1\u76ee/\u91d1\u989d"});
                }
                this.listadapter.setLayout(new int[]{10, 70, Function.LIST_MONTH_REPORT, Function.LIST_MONTH_REPORT});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    String stringBuilder;
                    obj2 = new String[3];
                    obj2[FILL_PAGE] = new FDate(new Date(cur.getLong(5))).toShortString();
                    obj2[FILL_ALL] = new Friend(cur.getInt(FILL_ALL)).name;
                    if (cur.getInt(4) <= 0) {
                        stringBuilder = new StringBuilder(String.valueOf(new FavorType(cur.getInt(tab_selected_index) % 16).name)).append(cur.getInt(tab_selected_index) < 16 ? "(\u652f)" : "(\u6536)").toString();
                    } else {
                        stringBuilder = new StringBuilder(String.valueOf(cur.getInt(tab_selected_index) < 16 ? "-" : "+")).append(Convertor.sumToString((long) cur.getInt(3))).toString();
                    }
                    obj2[tab_selected_index] = stringBuilder;
                    listdata.add(obj2);
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_FRIEND /*146*/:
                /*if (this.listadapter.getCount() == 0) {
                    listdata.add(new String[]{"\u540d\u5b57", "\u5206\u7ec4", "\u7535\u8bdd"});
                }
                this.listadapter.setLayout(new int[]{20, 100, 50, Function.LIST_AUDIT_IN});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    listdata.add(new String[]{cur.getString(FILL_ALL), Friend.getTypeName(cur.getInt(tab_selected_index)), cur.getString(3)});
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            case Function.LIST_SAFE /*161*/:
                /*if (this.listadapter.getCount() == 0) {
                    obj = new String[tab_selected_index];
                    obj[FILL_PAGE] = "\u5206\u7ec4";
                    obj[FILL_ALL] = "\u5e10\u6237\u6807\u9898";
                    listdata.add(obj);
                }
                this.listadapter.setLayout(new int[]{20, 80, 220});
                for (i = FILL_PAGE; i < row_count && !cur.isAfterLast(); i += FILL_ALL) {
                    obj = new String[tab_selected_index];
                    obj[FILL_PAGE] = Safe.getTypeName(cur.getShort(FILL_ALL));
                    obj[FILL_ALL] = Safe.decrypt(cur.getString(tab_selected_index));
                    listdata.add(obj);
                    index.add(Integer.valueOf(cur.getInt(FILL_PAGE)));
                    cur.moveToNext();
                }*/
                break;
            default:
                obj = new String[1];
				 obj[0] = "此页没有定义";
                listdata.add(obj);
                index.add(Integer.valueOf(ID_LIST_TITLE));
                break;
        }
        if (!cur.isAfterLast() && cur.getCount() > 0) {
            obj = new String[1];
            obj[0] = "查看更多...";
            listdata.add(obj);
            index.add(Integer.valueOf(ID_LIST_MORE));
        } else if (cur.getCount() > Param.NUMBER_PER_PAGE) {
            obj = new String[1];
            obj[0] = "没有了";
            listdata.add(obj);
            index.add(Integer.valueOf(ID_LIST_END));
        }
        cur.close();
        this.listadapter.append(listdata);
    }

	
     ///////////窗口功能//////////////
    
    //@Override
	/*列表单击事件----------------------------
	* parent 相当于listview Y适配器的一个指针，可以通过它来获得Y里装着的一切东西，再通俗点就是说告诉你，你点的是Y，不是X - -
	* view 是你点b item的view的句柄，就是你可以用这个view，来获得b里的控件的id后操作控件
	* position 是b在Y适配器里的位置（生成listview时，适配器一个一个的做item，然后把他们按顺序排好队，在放到listview里，意思就是这个b是第position号做好的）
	*id 是b在listview Y里的第几行的位置（很明显是第2行），大部分时候position和id的值是一样的
	*
	*列表单击事件
	*/
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if (position < index.size())
		{
			params[1] = position;
			params[3] = ((Integer)index.get(position)).intValue();
			if (params[3] == 2147483413)
				params[3] = 1;
			if ((params[3] != -1) && (params[3] <= 2147483392))
				switch (function)
				{
					default:
						break;
		            case 115://银行列表
                        Deposit localDeposit = new Deposit(params[3]);
                        if (localDeposit.type != 25)
                        {
                            if (localDeposit.type != 26)
                                returnToDetail(115);
                            else
                                displayNewList(160);
                        }
                        else
                            displayNewList(112);
                        break;
					//------
		            case 105:
		           // case 108:
		           // case 137:
		           // case 149:
		           // case 150:
		          //  case 151:
		          //  case 152:
		           // case 153:
		           // case 154:
		              //如果不是超市购物
		              if (new Audit(params[3]).kmid != 320)
		                returnToDetail(105);
		              else
		                displayNewList(148);
		              break; 
                        //
                    case 145://借贷列表
                        returnToDetail(145);
                        break;
					case 146://借贷流水
					case 164:
						returnToDetail(146);
						break;
					//------	
					case 155://资产列表
						displayNewList(156);
						break;
					case 156://资产流水
						returnToDetail(156);
						break;
					//------
					case 140://人情列表
						displayNewList(141);
						break;
					case 141://人情流水
						returnToDetail(141);
						break;
						//////
					case 135://项目列表
				//	case 155:
						displayNewList(136);
						break;
					case 136://项目流水
						returnToDetail(136);
						break;
						
					case 125://字典
					case 106:
					case 110:
					case 117:
					case 126:
					//case 145:
					case 157://资产类型
					case 158://隐藏列表
					case 137://项目科目
					case 159:
					case 162:
						openContextMenu(this.titlebar);
						break;

	
				}
		 }
	}
	/*public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (position < index.size()) {
            params[FILL_ALL] = position;
            params[3] = ((Integer) index.get(position)).intValue();
            if (params[3] == ID_DEPOSIT_CASH) {
                params[3] = FILL_ALL;
            }
            if (params[3] != FP && params[3] <= ID_LIST_FIRST) {
                switch (function) {
                    case Function.LIST_MAIN_PAGE /*99*
                        if (params[3] > 0 && params[3] < ID_LIST_FIRST) {
                            returnToDetail(Function.LIST_CREDIT_AUDIT);
                        }
                    case Function.LIST_AUDIT /*100*
                    case Function.LIST_FIND_AUDIT /*108*
                    case Function.LIST_KM_AUDIT /*137*
                    case Function.LIST_AUDIT_SUPER_MARKET /*149*
                    case Function.LIST_AUDIT_IN /*150*
                    case Function.LIST_AUDIT_OUT /*151*
                    case Function.LIST_AUDIT_FAVOR /*152*
                    case Function.LIST_AUDIT_LOTTERY /*153*
                    case Function.LIST_AUDIT_MAJIANG /*154*
                        if (new Audit(params[3]).kmid == KM.KM_ZC_SUPER_MARKET) {
                            displayNewList(Function.LIST_AUDIT_SUPER_MARKET_ORDER);
                        } else {
                            returnToDetail(Function.LIST_DEPOSIT);
                        }
                    case Function.LIST_CURRENT_REPORT /*101*
                    case Function.LIST_MONTH_REPORT_CONTENT /*122*
                    case Function.LIST_YEAR_REPORT_CONTENT /*123*
                    case Function.LIST_BUDGET_CONTENT /*134*
                        if ((params[FILL_PAGE] == 0 || params[FILL_PAGE] == tab_selected_index) && params[3] != FP) {
                            displayNewList(function);
                        }
                    case Function.LIST_DEPOSIT /*102*
                        Deposit deposit = new Deposit(params[3]);
                        if (deposit.type == (short) 25) {
                            displayNewList(Function.LIST_CURRENT_CARD_CHILDREN);
                        } else if (deposit.type == (short) 26) {
                            displayNewList(Function.LIST_FIXED_CARD_CHILDREN);
                        } else {
                            returnToDetail(Function.LIST_CURRENT_REPORT);
                        }
                    case Function.LIST_KM /*103*
                    case Function.LIST_INTEREST_RATE /*106*
                    case Function.LIST_PLAN /*110*
                    case Function.LIST_EVECTION_KM /*117*
                    case Function.LIST_INVEST_TYPE /*126*
                    case Function.LIST_FAVOR_TYPE /*145*
                    case Function.LIST_PROJECT_KM /*147*
                    case Function.LIST_FRIEND_TYPE /*159*
                    case Function.LIST_SAFE_TYPE /*162*
                        openContextMenu(this.titlebar);
                    case Function.LIST_BANK /*105*
                        returnToDetail(100);
                    case Function.LIST_KM_STAT /*109*
                        expandKmStat();
                    case Function.LIST_CURRENT_CARD_CHILDREN /*112*
                    case Function.LIST_FIXED_CARD_CHILDREN /*160*
                        returnToDetail(Function.LIST_CURRENT_REPORT);
                    case Function.LIST_VIREMENT_AUDIT /*113*
                        returnToDetail(Function.LIST_KM);
                    case Function.LIST_CREDIT /*114*
                        returnToDetail(Function.LIST_ABOUT);
                    case Function.LIST_CREDIT_AUDIT /*115*
                    case Function.LIST_CREDIT_ACCOUNT_AUDIT /*164*
                        returnToDetail(Function.LIST_BANK);
                    case Function.LIST_EVECTION /*116*
                    case Function.LIST_AUDIT_PROJECT /*155*
                        displayNewList(Function.LIST_EVECTION_AUDIT);
                    case Function.LIST_EVECTION_AUDIT /*118*
                        returnToDetail(Function.LIST_INTEREST_RATE);
                    case Function.LIST_MONTH_REPORT /*120*
                        displayNewList(Function.LIST_MONTH_REPORT_CONTENT);
                    case Function.LIST_YEAR_REPORT /*121*
                        displayNewList(Function.LIST_YEAR_REPORT_CONTENT);
                    case Function.LIST_INVEST /*124*
                        switch (new InvestAccount(params[3]).type) {
                            case FILL_ALL /*1*
                                displayNewList(Function.LIST_INSURANCE);
                            case tab_selected_index /*2*
                                displayNewList(Function.LIST_BOND);
                            case Param.TRY_MONTH /*3*
                                displayNewList(Function.LIST_FUNDS);
                            case Param.SOFT_ID /*4*
                                displayNewList(Function.LIST_STOCK);
                            default:
                                returnToDetail(Function.LIST_KM_STAT);
                        }
                    case Function.LIST_ASSET /*125*
                        returnToDetail(Function.LIST_ASSET_DEBT);
                    case Function.LIST_INVEST_AUDIT /*127*
                        returnToDetail(Function.LIST_PLAN);
                    case Function.LIST_INVEST_PROFIT /*128*
                        returnToDetail(Function.LIST_MONTH_REPORT_CONTENT);
                    case Function.LIST_STOCK /*129*
                        returnToDetail(Function.DETAIL_STOCK);
                    case Function.LIST_FUNDS /*130*
                        returnToDetail(Function.LIST_CURRENT_CARD_CHILDREN);
                    case Function.LIST_BOND /*131*
                        returnToDetail(Function.LIST_VIREMENT_AUDIT);
                    case Function.LIST_INSURANCE /*132*
                        returnToDetail(Function.LIST_CREDIT);
                    case Function.LIST_BUDGET /*133*
                        displayNewList(Function.LIST_BUDGET_CONTENT);
                    case Function.LIST_MEMO /*135*
                        returnToDetail(Function.LIST_CREDIT_AUDIT);
                    case Function.LIST_ASSET_AUDIT /*136*
                        returnToDetail(Function.LIST_EVECTION);
                    case Function.LIST_FAVOR /*144*
                    case Function.LIST_FAVOR_STAT_AUDIT /*166*
                        returnToDetail(Function.LIST_MONTH_REPORT);
                    case Function.LIST_FRIEND /*146*
                        returnToDetail(Function.LIST_YEAR_REPORT);
                    case Function.LIST_AUDIT_SUPER_MARKET_ORDER /*148*
                        returnToDetail(Function.LIST_EVECTION_STAT);
                    case Function.LIST_SAFE /*161*
                        returnToDetail(Function.LIST_YEAR_REPORT_CONTENT);
                    case Function.LIST_FAVOR_STAT /*165*
                        displayNewList(Function.LIST_FAVOR_STAT_AUDIT);
                    default:
                }
            }
        }
    }*/

	@Override
	
	/*
	 *创建上下文ContextMenu菜单的步骤：
     *1、 覆盖Activity的onCreateContextMenu()方法，调用Menu的add方法添加菜单项
     *2、 覆盖onContexItemSelected()方法，响应菜单单击事件
     *3、 调用registerForContexMenu()方法为视力注册上下文菜单
     *
     *参数说明：
     *menu：需要显示的快捷菜单
     *v：V是用户选择的界面元素
     *menuInfo：menuInfo是所选择界面元素的额外信息
     *说明：这个onCreateContextMenu与onCreateOptionsMenu函数不一样，onCreateOptionsMenu函数仅在选项菜单第一次启动时被调用一次，而onCreateContextMenu函数在每次启动都将会被调用一次。
	 *
	 *上下菜单
	 */
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		int i = 0;
		int j;
		int n;
		int k;
		if (v != this.addauditbutton)
		{	
			if (v != this.menubutton)
			{
				if (v != this.titlebar)
				{
					if (menuInfo != null)
					{				
						if ((index.size() <= 0) || (((AdapterView.AdapterContextMenuInfo)menuInfo).position >= index.size()))						
						    return;
						params[3] = ((Integer)index.get(((AdapterView.AdapterContextMenuInfo)menuInfo).position)).intValue();
						if ((params[3] >= 2147483397) || (params[3] < 0))
							return;
					}
					//长按列表
					switch (function)
					{
						default:
							break;
						//资产列表上下文菜单	
						case 155:
							j = i + 1;
							menu.add(0, 1552, i, "修改信息");//由onContextItemSelected响应Form处理
							i = j + 1;
							menu.add(0, 1553, j, "删除");//由onContextItemSelected响应Main处理
							j = i + 1;
							menu.add(0, 1554, i, "隐藏");//由onContextItemSelected响应Form处理
							break;
						//人情列表上下文菜单	
						case 140:
							j = i + 1;
							menu.add(0, 1402, i, "修改信息");//由onContextItemSelected响应Form处理
							i = j + 1;
							menu.add(0, 1403, j, "删除");//由onContextItemSelected响应Main处理
							break;	
						//项目列表上下文菜单	
						case 135:
							j = i + 1;
							menu.add(0, 1352, i, "修改信息");//由onContextItemSelected响应Form处理
							i = j + 1;
							menu.add(0, 1353, j, "删除");//由onContextItemSelected响应Main处理
							break;

					}
					return;
				}
				//标题(单击列表)
				switch (function)
				{
					default:
						break;
					//字典列表	
				    case 125:
                        if (params[3] >= 3)
                        {
                           j = i + 1;
                           menu.add(0, 1029, i, "上移");
                           i = j + 1;
                           menu.add(0, 1030, j, "下移");
                        }
                        if (new KM(params[3]).pid > 2)
                        {
                            j = i + 1;
                            menu.add(0, 1031, i, "更改父科目");
                            i = j;
                        }
                        if (params[3] >= 3)
                        {
                            j = i + 1;
                            menu.add(0, 1100, i, "合并到");
                            i = j;
                        }
                        if (params[3] < 3)
                             break;
                        j = i + 1;
                        menu.add(0, 1252, i, "修改");
                        menu.add(0, 1253, j, "删除");
                        break;
				    
                    //资产类型列表由列表单击事件处理
				    case 157:
						j = i + 1;
						menu.add(0, 1572, i, "修改");
						i = j + 1;
						menu.add(0, 1573, j, "删除");
						break;
				    case 158:
						j = i + 1;
						menu.add(0, 1581, i, "恢复");
						
						break;
                    //项目科目列表
					case 137:
						j = i + 1;
						menu.add(0, 1372, i, "修改");
						i = j + 1;
						menu.add(0, 1373, j, "删除");
						break;


				}
				return;
			}
            //菜单(工具栏)
		    switch (function)
		    {
	
			    default:
				    break;
				case 115://银行列表
                    j = i + 1;
                    menu.add(0, 1150, i, "开户");
                    i = j + 1;
					menu.add(0, 1004, j, "登记已有");
                    j = i + 1;
                    menu.add(0, 1038, i, "排序界面");
                    i =j + 1;
                    menu.add(0, 1008, j, "利率表");
                    break;
				
				case 125://科目列表
					j = i + 1;
					menu.add(0, 1250, i, "增加科目");
                    break;
					
				case 145://借袋列表
					j = i + 1;
					menu.add(0, 1450, i, "新增");
					i = j + 1;
					menu.add(0, 1027, j, "登记已有");
					j = i + 1;
					menu.add(0, 1459, i, "借贷流水");
					break;
				////////////	
				case 155://资产列表
					j = i + 1;
					menu.add(0, 1557, i, "类别管理");
					i = j + 1;
					menu.add(0, 1550, j, "新增资产");
					j = i + 1;
					menu.add(0, 1558, i, "隐藏管理");
					i = j + 1;
					menu.add(0, 1559, j, "资产流水");
					
					break;
					
				case 157://资产类型
					j = i + 1;
					menu.add(0, 1570, i, "新增类别");
					break;
				case 156://资产流水
					j = i + 1;
					menu.add(0, 1040, i, "结束资产");
					i = j + 1;
					menu.add(0, 1041, j, "统计");
					j = i + 1;
					menu.add(0, 1121, i, "收支科目");
					i = j + 1;
					menu.add(0, 1569, j, "资产信息");
					break;
				//--------
				case 140://项目列表
					j = i + 1;
					menu.add(0, 1400, i, "新增好友");
					break;
				//////	
				case 135://项目列表
					j = i + 1;
					menu.add(0, 1350, i, "新增项目");
					break;
		
				case 136://项目流水
					int i1 = i + 1;
					menu.add(0, 1361, i, "记账");
					i = i1 + 1;
					String str;
					str = "结束项目";
					menu.add(0, 1368, i1, str);
					n = i + 1;
					menu.add(0, 1365, i, "统计");
					i = n + 1;
				    String str1;
					str1 = "收支科目";
					menu.add(0, 1364, n, str1);
					n = i + 1;
					menu.add(0, 1369, i, "项目信息");
					i = n + 1;
					break;
				case 137://项目科目
                    n = i + 1;
                    menu.add(0, 1370, i, "增加科目");
                    break;
				
				
				case 130://系统
				      j = i + 1;
				     // addParamConfigSubMenu(menu, i);
				    //  i = j + 1;
				    //  addDataManageSubMenu(paramContextMenu, j);
				    //  j = i + 1;
				    //  addPasswordManageSubMenu(paramContextMenu, i);
				      if (((RuntimeInfo.param.flag[49] != 0) || (Param.ISMM)) && (Param.MODE != 4))
				      {
				        i = j;
				      }
				      else
				      {
				        i = j + 1;
				        menu.add(0, 1076, j, "注册");
				      }
				      if (!Param.ISMM)
				      {
				        j = i + 1;
				        menu.add(0, 1093, i, "检查更新");
				        i = j;
				      }
				      j = i + 1;
				      menu.add(0, 1092, i, "帮助");
				      break;
		    
		    }
            return;
		}
		
        //记帐(工具栏)
		k = i + 1;
		menu.add(0, 1060, i, "生活记帐");
		i = k + 1;
		menu.add(0, 1410, k, "人情记帐");
		k = i + 1;
		menu.add(0, 1460, k, "借贷记帐");
		i = k + 1;
		menu.add(0, 1560, i, "资产记帐");
		
		if (RuntimeInfo.param.flag[6] == 0)
        {
			k = i + 1;
			menu.add(0, 1360, i, "项目记帐");
			i = k;
        }
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		return menuAction(item.getItemId());
	}
	public boolean menuAction(int itemId)
	{
		boolean bool = false;
		switch (itemId)
		{
			default:
				returnToForm(itemId);
				break;
			case 1459://借贷总流水
				displayNewList(149);
				break;
			///////////
			case 1557://资产类别列表
				displayNewList(157);
				break;
			case 1558://资产隐藏列表
				displayNewList(158);
				break;
			case 1559://资产总流水列表
				displayNewList(159);
				break;
			case 1560://记帐菜单资产记帐
				if (Asset.hasEvection("flag=0"))
					returnToForm(itemId);
				else
					showNote("请先在资产模块下:新增修改'类别'和'资产'!");
				break;	
			case 1569://资产信息
				returnToDetail(155);
				break;
				
			/////////////	
			case 1360://记帐菜单记帐
				if (Evection.hasEvection("flag=0 or flag=2"))
					returnToForm(itemId);
				else
					showNote("请先在出差模块下新增出差/项目!");
				break;
			case 1369://项目信息
				returnToDetail(135);
				break;
				
			//////////	
			case 1002://科目
			case 1032:
			case 1034:
			case 1055:
			case 1072:
			case 1553://资产
			case 1573://资产类型	
			case 1403://人情
			case 1373://项目科目
			case 1353://项目
			case 1619:
			case 1624:
			case 1631:
				deleteConfirmAction(itemId);
				break;
			case 1364://科目
				displayNewList(137);
				break;
			case 1365://统计
				displayNewList(138);
				break;	
		/*	case 1:
			//	backupWhenFinish();
				break;
	;
			case 1023:
			case 1042:*/
		}
		return bool;
	}

	void returnToForm(int itemId)
	{
		String localObject = RuntimeInfo.param.checkVersion();
		if (localObject != "软件信息错误,请联系作者！")
		{
			this.inner_app_focus = true;
			FormActivity.function = itemId;
			Intent intent = new Intent();
			intent.setClass(this, FormActivity.class);
			startActivity(intent);
		}
		else
		{
			showNote(localObject);
		}
	}
	
	
	void returnToDetail(int itemId)
	{
		this.inner_app_focus = true;
		DetailActivity.function = itemId;
		Intent localIntent = new Intent();
		localIntent.setClass(this, DetailActivity.class);
		startActivity(localIntent);
	}



	public void showNote(final String err)
	{
		new AlertDialog.Builder(this).setTitle("提示信息 ").setMessage(err).setPositiveButton("确认", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface paramDialogInterface, int paramInt)
			{
				if ((err == "操作成功完成！") || (err == null))
					MainActivity.this.redisplay();
			}
		}).show();
	}
	private void redisplay()
	{
		params = new int[10];
		innerDisplay();
	}
	
	void deleteConfirmAction(final int menuItemId)
    {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this).setTitle("确认删除 ").setMessage("确认要删除吗?");
        localBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
                MainActivity.this.deleteAction(menuItemId);
            }
        });
        localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
            }
        });
        localBuilder.show();
    }
  
	void deleteAction(int paramInt)
	{
		String str = null;
		switch (paramInt)
		{
			case 1002://科目
				str = new KM(params[3]).delete();
				break;
				//////
			case 1553://资产
				str = new Asset(params[3]).delete();
				if (str != "操作成功完成！")
					break;
				refresh();
				//	popParams();
				break;
			case 1573://资产类型
				str = new AssetKm(params[3]).delete();
				if (str != "操作成功完成！")
					break;
				refresh();
				//	popParams();
				break;
			//////////
			case 1403://项目
				str = new Favor(params[3]).delete();
				if (str != "操作成功完成！")
					break;
				refresh();
		//		popParams();
				break;
				/////
			case 1353://项目
				str = new Evection(params[3]).delete();
				if (str != "操作成功完成！")
					break;
				refresh();
		//		popParams();
				break;
			case 1373://项目科目
				str = new Evection(((int[])paramstack.get(0))[3]).deleteProjectKm(params[3]);
				break;
	

		}
		showNote(str);
	}
	
	public void displayNewList(int paramInt)
	{
		pushParams();
		function = paramInt;
		params = new int[10];
		innerDisplay();
	}
	//把项压入栈顶
	static void pushParams()
	{
		params[9] = function;
		paramstack.push(params);
	}

	/*
	 * 滑屏事件
	 */
	
	public boolean onTouch(View v, MotionEvent event)
	{
		return this.gestureDetector.onTouchEvent(event);
	}
    //---------------------------------	
	boolean onDoubleTap(MotionEvent event)
	{
		return false;
	}

	boolean onDoubleTapEvent(MotionEvent event)
	{
		return false;
	}
    
	// 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
	public boolean onDown(MotionEvent event)
	{
		boolean bool;
		if (!isChart(function))
			bool = false;
		else
			bool = true;
		return bool;
	}
	
	// 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
    // e1：第1个ACTION_DOWN MotionEvent
    // e2：最后一个ACTION_MOVE MotionEvent
    // velocityX：X轴上的移动速度，像素/秒
    // velocityY：Y轴上的移动速度，像素/秒
    // 触发条件 ：
    // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		boolean bool = false;
		if ((Math.abs(e1.getY() - e2.getY()) < UIAdapter.height / 2) && (Math.abs(e1.getX() - e2.getX()) > UIAdapter.width / 3))
		{
			if (e1.getX() - e2.getX() >= 0.0F)
				tab_index = 1 + tab_index;
			else
				tab_index = -1 + tab_index;
			if (tab_index < tab_function.size())
			{
				if (tab_index < 0)
					tab_index = -1 + tab_function.size();
			}
			else
				tab_index = 0;
			display(((Integer)tab_function.get(tab_index)).intValue());
			bool = true;
		}
		return bool;
	}


    // 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
	public void onLongPress(MotionEvent event)
	{
	}
	
	// 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float paramFloat1, float paramFloat2)
	{
		return false;
	}
    
	// 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
    // 注意和onDown()的区别，强调的是没有松开或者拖动的状态
	public void onShowPress(MotionEvent event)
	{
	}

	boolean onSingleTapConfirmed(MotionEvent event)
	{
		return false;
	}
	
	// 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
	public boolean onSingleTapUp(MotionEvent event)
	{
		return false;
	}
	
//-----------------------------

	//外部调用///////////////////
	public void refresh()
    {
        innerDisplay();
    }
	
    public void setTabSelectedIndex(int paramInt)
    {
        if (paramInt != tab_index)
        {
             tab_index = paramInt;
             display(((Integer)tab_function.get(tab_index)).intValue());
        }
    }


	}
/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.MainActivity
 * JD-Core Version:    0.6.0
 */

