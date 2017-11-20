package com.database;



import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import java.net.URLEncoder;

public class ChartView extends RelativeLayout
{
	//全屏
	private static final int FP = ViewGroup.LayoutParams.FILL_PARENT;//-1
	//刚好包括组件
	private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;//-2
  static final int[] color;
  static final int[] color1;
  public static final int[] color2;
  Context context;
  int function;
  int screen_mode = 0;

  static
  {
    int[] arrayOfInt = new int[9];
    arrayOfInt[0] = -1473516;
    arrayOfInt[1] = -16742177;
    arrayOfInt[2] = -255;
    arrayOfInt[3] = -65281;
    arrayOfInt[4] = -6632193;
    arrayOfInt[5] = -16760703;
    arrayOfInt[6] = -14614531;
    arrayOfInt[7] = -16711684;
    arrayOfInt[8] = -6606080;
    color = arrayOfInt;
    arrayOfInt = new int[9];
    arrayOfInt[0] = -5680119;
    arrayOfInt[1] = -16754529;
    arrayOfInt[2] = -5263615;
    arrayOfInt[3] = -5308241;
    arrayOfInt[4] = -11887441;
    arrayOfInt[5] = -16773071;
    arrayOfInt[6] = -15683667;
    arrayOfInt[7] = -16732244;
    arrayOfInt[8] = -11857152;
    color1 = arrayOfInt;
    arrayOfInt = new int[9];
    arrayOfInt[0] = -10616832;
    arrayOfInt[1] = -16752895;
    arrayOfInt[2] = -10526975;
    arrayOfInt[3] = -16621731;
    arrayOfInt[4] = -10551201;
    arrayOfInt[5] = -16106401;
    arrayOfInt[6] = -16773119;
    arrayOfInt[7] = -16752804;
    arrayOfInt[8] = -16055552;
    color2 = arrayOfInt;
  }

  public ChartView(Context context)
  {
    super(context);
    this.context = context;
  }

  public void display(int width, int height, int function)
  {
    this.function = function;
    if (getResources().getConfiguration().orientation == 2)
      
    	this.screen_mode = 1;
    setBackgroundColor(Color.WHITE);//-1
    removeAllViews();
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
    ChartCanvas localChartCanvas = new ChartCanvas(this.context, width, height, function);
    addView(localChartCanvas, localLayoutParams);
    localChartCanvas.setId(1);
  }

 
}
 

/* Location:           D:\备份文件\手机备份\可用文件\反编译\APK反编译\ApkDecompiler\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.ChartView
 * JD-Core Version:    0.6.0
 */