/*
 *欢迎窗口
 *程序启动的第一个窗口
 *主要是初始化程序构建参数
 *由onCreate()创建窗口并创建线程new Thread()来初始化程序构建参数
 *
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
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.database.context.RuntimeInfo;
import com.database.model.Param;
import com.database.util.DBTool;
import com.database.util.MyDate;
import java.util.Date;


public class WelcomeActivity extends Activity
{
    @Override
	/*
	 * 首先调用创建窗口
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        
		// 设置窗口无标题(FEATURE_CUSTOM_TITLE：自定义标题。当需要自定义标题时必须指定。如：标题是一个按钮时FEATURE_INDETERMINATE_PROGRESS：不确定的进度FEATURE_LEFT_ICON：标题栏左侧的图标FEATURE_OPTIONS_PANEL：启用“选项面板”功能，默认已启用。FEATURE_PROGRESS：进度指示器功能FEATURE_RIGHT_ICON:标题栏右侧的图标)
		requestWindowFeature(Window.FEATURE_NO_TITLE);//1
		//设置窗体全屏(FLAG_KEEP_SCREEN_ON设置窗体始终点亮,FLAG_BLUR_BEHIND设置窗体背景模糊)
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		
	    
		//创建图片组件 
		ImageView iv = new ImageView(this);
		//获取窗口大小
		UIAdapter.initParams(this);
		int w = UIAdapter.width;
		int h = UIAdapter.height;
		//添加图片
		iv.setImageDrawable(zoomDrawable(getResources().getDrawable(R.drawable.welcome), w , h ));
		RuntimeInfo.welcome = this;
		//显示布局
		setContentView(iv);
		//创建一个线程作为程序入口由returnToMainActivity()
		new Thread()
		{
			//
			private final Context context = WelcomeActivity.this;
			private final Handler handler = new Handler();
		    public void run()
			{
		    	//没有错误执行
				try
				{
					//暂停
					Thread.sleep(1500L);
					//entry(context,handler);
	
				}
				//如果有错误执行
				catch (Exception localException)
				{
				//	entry(context,handler);
				}
				WelcomeActivity.this.entry(context,handler);
			}
		}
		.start();
	}
	/*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Function.MENU_BANK_CARD_ADD_FIXED, Function.MENU_BANK_CARD_ADD_FIXED);
        requestWindowFeature(1);
        ImageView iv = new ImageView(this);
        UIAdapter.initParams(this);
        iv.setImageDrawable(zoomDrawable(getResources().getDrawable(R.drawable.welcome), UIAdapter.width, UIAdapter.height));
        RuntimeInfo.welcome = this;
        setContentView(iv);
        new AnonymousClass1(this, new Handler()).start();
    }*/
	/* renamed from: com.tomoney.finance.view.WelcomeActivity.1 */
    class AnonymousClass1 extends Thread {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ Handler val$handler;

        AnonymousClass1(Context context, Handler handler) {
            this.val$context = context;
            this.val$handler = handler;
        }

        public void run() {
            try {
                Thread.sleep(1500);
            } catch (Exception e) {
            }
            WelcomeActivity.this.entry(this.val$context, this.val$handler);
        }
    }

	
	@Override 
	//拦截按下事件
	public boolean onKeyDown(int KeyCode, KeyEvent event)
	{
		return true;
	}
    //缩放
    public static Drawable zoomDrawable(Drawable drawable, int width, int height)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = drawableToBitmap(drawable);
        // 创建操作图片用的Matrix对象
		Matrix matrix = new Matrix();
		// 计算缩放比例
		float f;
        if (width >= height)
            f = 9.0F * height / 4.0F / h;
        else
            f = 9.0F * width / 4.0F / w;
		// 设置缩放比例
		matrix.postScale(f, f);
   		int sw = (int)(width / f);
        int sh = (int)(height / f);
        // 建立新的bitmap，其内容是对原bitmap的缩放后的图
		return new BitmapDrawable(Bitmap.createBitmap(bitmap, (w - sw) / 2, (h - sh) / 2, sw, sh, matrix, true));
    }	 
    //转位图
	static Bitmap drawableToBitmap(Drawable drawable)
    {
		// 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
		Bitmap.Config config;
        if (drawable.getOpacity() == PixelFormat.OPAQUE)
            config = Bitmap.Config.RGB_565;
        else
            config = Bitmap.Config.ARGB_8888;
        // 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
   		Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w,h);
        // 把 drawable 内容画到画布中
		drawable.draw(canvas);
        return bitmap;
    }

    /*
     * 入口
     * 先由isFileExist判断数据库文件是否存在
     * 
     * 
     */
    /*void entry(final Context context, final Handler handler)
    {
		//如果文件存在
        if (DBTool.isFileExist(DBTool.getDatabaseFilename(WelcomeActivity.this)))
		{
        	//如果数据库不存在
		    if (DBTool.database == null)
		    	//创建数据库
		        DBTool.database = DBTool.getDatabase(WelcomeActivity.this.getBaseContext());
		    //参数不存在
		    if (RuntimeInfo.param == null)
		    	//获得参数
		        RuntimeInfo.param = new Param();
			//如果参数版本大于等于8  
		    if (RuntimeInfo.param.version >= 8)
		    {
		    	//
		        if ((Param.MODE == 4) || (RuntimeInfo.param.flag[1] != 1))
		        {
		            returnToMain(context, handler);
		        }
		        else
		        {
		      //      FormActivity.function = 1611;
		            Intent localIntent = new Intent();
		    //        localIntent.setClass(this, LoginActivity.class);
		            startActivity(localIntent);
		        }
		    }
		    //如果参数版本小于8
		    else
		        handler.post(new Runnable()
		        {
		            public void run()
		            {
		                final  ProgressDialog dlg = ProgressDialog.show(WelcomeActivity.this, "君子爱财2.0", "数据升级中...", true, false);
		                new Thread()
		                {
		                    public void run()
		                    {
		                    //    DBTool.upgrade(context);
		                        dlg.dismiss();
		                        if ((Param.MODE == 4) || (RuntimeInfo.param.flag[1] != 1))
		                        {
		                                returnToMain(context,handler);
		                        }
		                        else
		                        {
		                     //       FormActivity.function = 1611;
		                            Intent localIntent = new Intent();
		                     //       localIntent.setClass(this.val$context, LoginActivity.class);
		                            WelcomeActivity.this.startActivity(localIntent);
		                        }
		                    }
		                }
		                .start();
		            }
		        });
		    }
        //如果文件不存在
		else
		{
		    handler.post(new Runnable()
		    {
		        public void run()
		        {
					final ProgressDialog dlg = ProgressDialog.show(WelcomeActivity.this, "欢迎使用君子爱财", "首次使用初始化...", true, false);
		            new Thread()
		            {
					
		                public void run()
		                {
		                    if (DBTool.database == null)
		                        DBTool.database = DBTool.getDatabase(WelcomeActivity.this.getBaseContext());
		                    if (RuntimeInfo.param == null)
		                        RuntimeInfo.param = new Param();
		                    //关闭线程
		                    dlg.dismiss();
		                    handler.post(new Runnable()
		                    {
		                        public void run()
		                        {
		                           returnToMainActivity();
		                        }
		                    });
		                }
		            }
		        .start();
		        }
		    });
	    }
    }*/
	void entry(final Context context, final Handler handler) {
        //如果文件存在
		if (DBTool.isFileExist(DBTool.getDatabaseFilename(context))) {
            //如果数据库不存在
			if (DBTool.database == null) {
                //创建数据库
				DBTool.database = DBTool.getDatabase(getBaseContext());
            }
			//参数不存在
            if (RuntimeInfo.param == null) {
                //获得参数
				RuntimeInfo.param = new Param();
            }
            if (RuntimeInfo.param.version < (short) 8) {
                handler.post(new Runnable(){
						
					public void run() {
						//new AnonymousClass1(this.val$context, ProgressDialog.show(this.val$context, "\u541b\u5b50\u7231\u8d222.0", "\u6570\u636e\u5347\u7ea7\u4e2d...", true, false), this.val$handler).start();
						//final ProgressDialog dlg = ProgressDialog.show(WelcomeActivity.this, "欢迎使用君子爱财", "首次使用初始化...", true, false);
						new Thread(){
							final  ProgressDialog dlg = ProgressDialog.show(WelcomeActivity.this, "君子爱财2.0", "数据升级中...", true, false);
							public void run() {
							    //DBTool.upgrade(this.val$context);
							    this.dlg.dismiss();
							    if (Param.MODE == 4 || RuntimeInfo.param.flag[1] != (byte) 1) {
								    WelcomeActivity.this.returnToMain(context, handler);
								    return;
							    }
							    //FormActivity.function = Function.MENU_LOGIN;
							    Intent intent = new Intent();
							    //intent.setClass(context, LoginActivity.class);
							    WelcomeActivity.this.startActivity(intent);
						    }
					    }.start();
					
					}
				});
                
				return;
            } else if (Param.MODE == 4 || RuntimeInfo.param.flag[1] != (byte) 1) {
                returnToMain(context, handler);
                return;
            } else {
              //  FormActivity.function = Function.MENU_LOGIN;
                Intent intent = new Intent();
             //   intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                return;
            }
        }
		//如果文件不存在
        handler.post(new Runnable(){

				
				public void run() {
					//new AnonymousClass1(this.val$context, ProgressDialog.show(this.val$context, "\u541b\u5b50\u7231\u8d222.0", "\u6570\u636e\u5347\u7ea7\u4e2d...", true, false), this.val$handler).start();

					new Thread(){
						final ProgressDialog dlg = ProgressDialog.show(WelcomeActivity.this, "欢迎使用君子爱财", "首次使用初始化...", true, false);
						public void run() {
							if (DBTool.database == null) {
								DBTool.database = DBTool.getDatabase(WelcomeActivity.this.getBaseContext());
							}
							if (RuntimeInfo.param == null) {
								RuntimeInfo.param = new Param();
							}
							dlg.dismiss();
							handler.post(new Runnable() {
							public void run() {
								WelcomeActivity.this.returnToMainActivity();
							}
						});
					}
				}.start();

			}
		});
    }
	/* renamed from: com.tomoney.finance.view.WelcomeActivity.3 */
    /*class AnonymousClass3 implements Runnable {
        private final /* synthetic  Context val$context;
        private final /* synthetic  Handler val$handler;

        /* renamed from: com.tomoney.finance.view.WelcomeActivity.3.1 
        class AnonymousClass1 extends Thread {
            private final /* synthetic  Context val$context;
            private final /* synthetic  ProgressDialog val$dlg;
            private final /* synthetic  Handler val$handler;

            AnonymousClass1(Context context, ProgressDialog progressDialog, Handler handler) {
                this.val$context = context;
                this.val$dlg = progressDialog;
                this.val$handler = handler;
            }

            public void run() {
                DBTool.upgrade(this.val$context);
                this.val$dlg.dismiss();
                if (Param.MODE == 4 || RuntimeInfo.param.flag[1] != (byte) 1) {
                    WelcomeActivity.this.returnToMain(this.val$context, this.val$handler);
                    return;
                }
                FormActivity.function = Function.MENU_LOGIN;
                Intent intent = new Intent();
                intent.setClass(this.val$context, LoginActivity.class);
                WelcomeActivity.this.startActivity(intent);
            }
        }

        AnonymousClass3(Context context, Handler handler) {
            this.val$context = context;
            this.val$handler = handler;
        }

        public void run() {
            new AnonymousClass1(this.val$context, ProgressDialog.show(this.val$context, "\u541b\u5b50\u7231\u8d222.0", "\u6570\u636e\u5347\u7ea7\u4e2d...", true, false), this.val$handler).start();
        }
    }
	//----------
	/* renamed from: com.tomoney.finance.view.WelcomeActivity.2 
    class AnonymousClass2 implements Runnable {
        private final /* synthetic  Context val$context;
        private final /* synthetic  Handler val$handler;

        /* renamed from: com.tomoney.finance.view.WelcomeActivity.2.1 
        class AnonymousClass1 extends Thread {
            private final /* synthetic  ProgressDialog val$dlg;
            private final /* synthetic  Handler val$handler;

            AnonymousClass1(ProgressDialog progressDialog, Handler handler) {
                this.val$dlg = progressDialog;
                this.val$handler = handler;
            }

            public void run() {
                if (DBTool.database == null) {
                    DBTool.database = DBTool.getDatabase(WelcomeActivity.this.getBaseContext());
                }
                if (RuntimeInfo.param == null) {
                    RuntimeInfo.param = new Param();
                }
                this.val$dlg.dismiss();
                this.val$handler.post(new Runnable() {
						public void run() {
							WelcomeActivity.this.returnToMainActivity();
						}
					});
            }
        }

        AnonymousClass2(Context context, Handler handler) {
            this.val$context = context;
            this.val$handler = handler;
        }

        public void run() {
            new AnonymousClass1(ProgressDialog.show(this.val$context, "\u6b22\u8fce\u4f7f\u7528\u541b\u5b50\u7231\u8d22", "\u9996\u6b21\u4f7f\u7528\u521d\u59cb\u5316...", true, false), this.val$handler).start();
        }
    }

	



    /*
     * 时间
     */
    /*void returnToMain(final Context context,final Handler handler)
    {
         final Date localDate = new Date();
         MyDate now = new MyDate(localDate);
        if (((now.getYear() != RuntimeInfo.param.lastdate.getYear()) || (now.getMonth() - RuntimeInfo.param.lastdate.getMonth() <= 1)) && ((now.getYear() != 1 + RuntimeInfo.param.lastdate.getYear()) || (12 + now.getMonth() - RuntimeInfo.param.lastdate.getMonth() <= 1)) && (now.getYear() <= 1 + RuntimeInfo.param.lastdate.getYear()))
        {
            RuntimeInfo.param.initDay(localDate, WelcomeActivity.this, handler);
            handler.post(new Runnable()
            {
                public void run()
                {
                    returnToMainActivity();
                }
            });
        }
        else
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    AlertDialog.Builder localBuilder = new AlertDialog.Builder(context).setTitle("确认信息 ").setMessage("距离上次登陆超过1个月,请确认?");
                    localBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt)
                        {
                            RuntimeInfo.param.initDay(localDate, WelcomeActivity.this, handler);
                            returnToMainActivity();
                        }
                    });
                    localBuilder.setNegativeButton("退出", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt)
                        {
                            finish();
                        }
                    });
                    localBuilder.show();
                }
            });
        }
    }*/
	void returnToMain(final Context context, final Handler handler) {
        final Date now = new Date();
        MyDate fnow = new MyDate(now);
        if ((fnow.getYear() != RuntimeInfo.param.lastdate.getYear() || fnow.getMonth() - RuntimeInfo.param.lastdate.getMonth() <= 1) && ((fnow.getYear() != RuntimeInfo.param.lastdate.getYear() + 1 || (fnow.getMonth() + 12) - RuntimeInfo.param.lastdate.getMonth() <= 1) && fnow.getYear() <= RuntimeInfo.param.lastdate.getYear() + 1)) {
            RuntimeInfo.param.initDay(now, this, handler);
            handler.post(new Runnable() {
                public void run() {
                    WelcomeActivity.this.returnToMainActivity();
                }
            });
            return;
        }
        handler.post(new Runnable()
            {
                public void run()
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context).setTitle("确认信息 ").setMessage("距离上次登陆超过1个月,请确认?");
                    alert.setPositiveButton("确认", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int whichButton) {
								RuntimeInfo.param.initDay(now, context, handler);
								WelcomeActivity.this.returnToMainActivity();
							}
						});
                    alert.setNegativeButton("退出", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int whichButton) {
								WelcomeActivity.this.finish();
							}
						});
                    alert.show();
                }
            });
	}
    
	/* renamed from: com.tomoney.finance.view.WelcomeActivity.4 */
    /*class AnonymousClass4 implements Runnable {
        private final /* synthetic  Context val$context;
        private final /* synthetic  Handler val$handler;
        private final /* synthetic  Date val$now;

        /* renamed from: com.tomoney.finance.view.WelcomeActivity.4.1 
        class AnonymousClass1 implements OnClickListener {
            private final /* synthetic  Context val$context;
            private final /* synthetic  Handler val$handler;
            private final /* synthetic  Date val$now;

            AnonymousClass1(Date date, Context context, Handler handler) {
                this.val$now = date;
                this.val$context = context;
                this.val$handler = handler;
            }

            public void onClick(DialogInterface dialog, int whichButton) {
                RuntimeInfo.param.initDay(this.val$now, this.val$context, this.val$handler);
                WelcomeActivity.this.returnToMainActivity();
            }
        }

        AnonymousClass4(Context context, Date date, Handler handler) {
            this.val$context = context;
            this.val$now = date;
            this.val$handler = handler;
        }

        public void run() {
            Builder alert = new Builder(this.val$context).setTitle("\u786e\u8ba4\u4fe1\u606f ").setMessage("\u8ddd\u79bb\u4e0a\u6b21\u767b\u9646\u8d85\u8fc71\u4e2a\u6708,\u8bf7\u786e\u8ba4?");
            alert.setPositiveButton("\u786e\u8ba4", new AnonymousClass1(this.val$now, this.val$context, this.val$handler));
            alert.setNegativeButton("\u9000\u51fa", new OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						WelcomeActivity.this.finish();
					}
				});
            alert.show();
        }
    }*/

    //转到
    void returnToMainActivity()
    {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

 /* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.WelcomeActivity
 * JD-Core Version:    0.6.0
 */



