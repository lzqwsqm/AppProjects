/*
 * 导航功能窗口
 */
package com.database;



import android.app.Activity;
import android.content.res.Configuration;
//import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;//
import android.view.View;
import android.view.ViewGroup;
//import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
//import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
//import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.database.context.RuntimeInfo;
import com.database.model.Param;
import java.util.Vector;

public class NaviActivity extends Activity
  implements View.OnClickListener
{
	private static final int FP = ViewGroup.LayoutParams.FILL_PARENT;//-1
	private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;//-2
    int img_width = 0;
    Vector<ImageView> iv = new Vector();
    Vector<String> iv_title = new Vector();
    TableLayout layout = null;
    TableLayout.LayoutParams layout_param = null;
    int number_per_line = 4;
    Vector<TextView> tv = new Vector();
    
	@Override
	//首先调用创建窗口
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(16973835);
		super.onCreate(savedInstanceState);
	//	UIAdapter.initParams(this);
		if (UIAdapter.width >= UIAdapter.height)
			this.img_width = (18 * UIAdapter.width / 120);
		else
			this.img_width = (9 * UIAdapter.width / 40);
	
		initImage();
		this.layout = new TableLayout(this);
		this.layout_param = new TableLayout.LayoutParams(9 * UIAdapter.width / 10, WC);
		setContentView(this.layout);
	    //判断横坚
	    if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)//2横屏
		{
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)//1坚
				this.number_per_line = 4;
		}
		else
			this.number_per_line = 6;
		drawPortrate();
		addButtonOkCancel();
	}
	
	void initImage()
	{
		this.iv.add(new ImageView(this));
		Vector localVector1 = this.iv;
		int k = 0 + 1;
		((ImageView)localVector1.get(0)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837522), this.img_width, this.img_width));
		this.iv_title.add("首页");
		
		this.iv.add(new ImageView(this));
		Vector localVector4 = this.iv;
		int i = k + 1;
		((ImageView)localVector4.get(k)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837506), this.img_width, this.img_width));
		this.iv_title.add("收支");
		this.iv.add(new ImageView(this));
		localVector4 = this.iv;
		k = i + 1;
		((ImageView)localVector4.get(i)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837523), this.img_width, this.img_width));
		this.iv_title.add("报表");
		this.iv.add(new ImageView(this));
		localVector4 = this.iv;
		i = k + 1;
		((ImageView)localVector4.get(k)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837509), this.img_width, this.img_width));
		this.iv_title.add("存款");
		//if ((Param.MODE != 2) && (RuntimeInfo.param.flag[50] == 0))
		//{
			this.iv.add(new ImageView(this));
			Vector localVector3 = this.iv;
			int n = i + 1;
			((ImageView)localVector3.get(i)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837513), this.img_width, this.img_width));
			this.iv_title.add("人情");
			i = n;
		//}
		Vector localVector5;
		int m;
		//if (RuntimeInfo.param.flag[11] == 0)
		//{
			this.iv.add(new ImageView(this));
			localVector5 = this.iv;
			m = i + 1;
			((ImageView)localVector5.get(i)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837508), this.img_width, this.img_width));
			this.iv_title.add("借贷");
			i = m;
		//}
		//if (Param.MODE != 2)
		//{
		//	if (RuntimeInfo.param.flag[10] == 0)
		//	{
				this.iv.add(new ImageView(this));
				localVector5 = this.iv;
				m = i + 1;
				((ImageView)localVector5.get(i)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837516), this.img_width, this.img_width));
				this.iv_title.add("投资");
				i = m;
		//	}
		//	if (RuntimeInfo.param.flag[12] == 0)
		//	{
				this.iv.add(new ImageView(this));
				localVector5 = this.iv;
				m = i + 1;
				((ImageView)localVector5.get(i)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837505), this.img_width, this.img_width));
				this.iv_title.add("资产");
				i = m;
		//	}
		//}
		//if (RuntimeInfo.param.flag[33] == 0)
		//{
			this.iv.add(new ImageView(this));
			localVector5 = this.iv;
			m = i + 1;
			((ImageView)localVector5.get(i)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837504), this.img_width, this.img_width));
			this.iv_title.add("分析");
			i = m;
		//}
		//if (RuntimeInfo.param.flag[6] == 0)
		//{
			this.iv.add(new ImageView(this));
			localVector5 = this.iv;
			m = i + 1;
			((ImageView)localVector5.get(i)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837512), this.img_width, this.img_width));
			this.iv_title.add("项目");
			i = m;
		//}
		this.iv.add(new ImageView(this));
		localVector5 = this.iv;
		m = i + 1;
		((ImageView)localVector5.get(i)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837510), this.img_width, this.img_width));
		this.iv_title.add("字典");
		this.iv.add(new ImageView(this));
		Vector localVector2 = this.iv;
		i = m + 1;
		((ImageView)localVector2.get(m)).setImageDrawable(zoomDrawable(getResources().getDrawable(2130837528), this.img_width, this.img_width));
		this.iv_title.add("系统");
		for (int j = 0; j < this.iv.size(); j++)
			((ImageView)this.iv.get(j)).setOnClickListener(this);
	}
	
	public static Drawable zoomDrawable(Drawable drawable, int width, int height)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap localBitmap = drawableToBitmap(drawable);
		// 创建操作图片用的Matrix对象
		Matrix localMatrix = new Matrix();
		// 计算缩放比例
        float scaleWidth = ((float)width / w); 
        float scaleHeight = ((float)height / h);
        // 设置缩放比例
        localMatrix.postScale(scaleWidth, scaleHeight);
  		// 把bitmap转换成drawable并返回
		return new BitmapDrawable(Bitmap.createBitmap(localBitmap, 0, 0, w, h, localMatrix, true));
    }
	/* drawable 转换成bitmap */
    static Bitmap drawableToBitmap(Drawable drawable)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
		// 取drawable的颜色格式
		Bitmap.Config config;
		if (drawable.getOpacity() == PixelFormat.OPAQUE)
            config = Bitmap.Config.RGB_565;
        else
            config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应bitmap的画布
		Canvas localCanvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
		// 把drawable内容画到画布中
		drawable.draw(localCanvas);
        return bitmap;
    }

	void drawPortrate()
	{
		TableRow localTableRow1 = null;
		TableRow localTableRow2 = null;
		for (int i = 0; i < this.iv.size(); i++)
		{
			if (i % this.number_per_line == 0)
			{
				localTableRow1 = new TableRow(this);
				this.layout.addView(localTableRow1, this.layout_param);
				localTableRow2 = new TableRow(this);
				this.layout.addView(localTableRow2, this.layout_param);
			}
			TableRow.LayoutParams localLayoutParams2 = new TableRow.LayoutParams(this.img_width, this.img_width);
			localLayoutParams2.bottomMargin = 1;
			localLayoutParams2.topMargin = 1;
			localLayoutParams2.rightMargin = 1;
			localLayoutParams2.leftMargin = 1;
			TableRow.LayoutParams localLayoutParams1 = new TableRow.LayoutParams(this.img_width, WC);
			localLayoutParams1.gravity = Gravity.CENTER;//17
			localTableRow1.addView((View)this.iv.get(i), localLayoutParams2);
			addTextView(localTableRow2, (String)this.iv_title.get(i), localLayoutParams1);
		}
	}
	void addTextView(TableRow paramTableRow, String paramString, TableRow.LayoutParams paramLayoutParams)
	{
		TextView tvv = new TextView(this);
		tvv.setText(paramString);
		tvv.setTextColor(Color.WHITE);//1
		tvv.setTextSize(-2 + UIAdapter.getTextSize());
		tvv.setOnClickListener(this);
		paramTableRow.addView(tvv, paramLayoutParams);
		this.tv.add(tvv);
	}
	
	void addButtonOkCancel()
	{
		Button bn_exit = new Button(this);
		bn_exit.setText(" 退出软件 ");
		bn_exit.setId(11);
		bn_exit.setOnClickListener(this);
		Button bn_back = new Button(this);
		bn_back.setText(" 返 回  ");
		bn_back.setId(12);
		bn_back.setOnClickListener(this);
		LinearLayout layout_bn = new LinearLayout(this);
		this.layout.addView(layout_bn);
		LinearLayout.LayoutParams layout_params_bn = new LinearLayout.LayoutParams(WC, WC, 1.0F);
		layout_params_bn.bottomMargin = 10;
		layout_params_bn.topMargin = 10;
		layout_bn.setOrientation(LinearLayout.HORIZONTAL);//0排列方式横
		layout_params_bn.leftMargin = 5;
		layout_params_bn.rightMargin = 0;
		layout_bn.addView(bn_exit, layout_params_bn);
		layout_params_bn.leftMargin = 5;
		layout_params_bn.rightMargin = 5;
		layout_bn.addView(bn_back, layout_params_bn);
	}

	////////////////////
	
	public void onClick(View v)
	{
	    int i =0;
		while (i < this.iv.size())
		{
			if ((this.iv.get(i) != v) && (this.tv.get(i) != v))
			{
				i++;
			}
			else
		    {	
			    int j = i;
			    RuntimeInfo.main.setTabSelectedIndex(j);
			    finish();
			    return;
		    }
		}
		switch (v.getId())
		{
			case 11:
				finish();
				RuntimeInfo.main.backupWhenFinish();
				break;
			case 12:
				finish();
				break;
		}
	}
	
	}
/*


  



 
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.NaviActivity
 * JD-Core Version:    0.6.0
 */

