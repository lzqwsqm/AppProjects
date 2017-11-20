package com.database;



import android.app.Activity;
import android.view.Display;
import android.view.WindowManager;

public class UIAdapter
{
    public static int height;
    public static int width = 320;

    static
    {
		height = 480;
    }

	public static void initParams(Activity activity)
	{
		Display localDisplay = activity.getWindowManager().getDefaultDisplay();
		width = localDisplay.getWidth();
		height = localDisplay.getHeight();
	}

	public static int getTitleBarHeight()
	{
		int i = 32;
		switch (height)
		{
			default:
				i = 40;
				break;
			case 360:
			case 400:
				i = 36;
				break;
			case 480:
				i = 40;
				break;
			case 640:
				i = 50;
				break;
			case 800:
			case 854:
			case 960:
				i = 60;
				break;
			case 240:
			case 320:
		}
		return i;
	}

	public static int getTitlebartextSize()
	{
		return 11 * getTextSize() / 10;
	}
	public static int getTextSize()
	{
		int i;
		if ((com.database.context.RuntimeInfo.param.flag[3] >= 12) && (com.database.context.RuntimeInfo.param.flag[3] <= 50))
			i = com.database.context.RuntimeInfo.param.flag[3];
		else
			i = getDefaultTextSize();
		return i;
	}

	public static int getDefaultTextSize()
	{
		int i = 16;
		switch (height)
		{
			default:
				i = 18;
				break;
			case 360:
			case 400:
				i = 18;
				break;
			case 480:
				i = 18;
				break;
			case 640:
				i = 22;
				break;
			case 800:
			case 854:
			case 960:
				i = 26;
				break;
			case 240:
			case 320:
		}
		return i;
	}

    public static int getTableHeight()
	{
		return height - getToolBarHeight() - getTitleBarHeight();
	}

	public static int getToolBarHeight()
	{
		return getTitleBarHeight();
	}
    //列表布局宽度
	public static int[] getTableRowLayout(int[] width_layout)
	{
		//如果宽度不是320
		if (width != 320)
		{
			//获得传递过来的宽度个数
			int[] w_l = new int[width_layout.length];
			for (int i = 0; i < w_l.length; i++)
				//将各个宽度乘以实际宽度除以320也就是按比例适配
				w_l[i] = (width_layout[i] * width / 320);
		    int	j = -1 + w_l.length;
			w_l[j] = (10 + w_l[j]);
			width_layout = w_l;
		}
		return width_layout;
	}



    public static int getDetailTextSize()
    {
        return getTextSize();
    }

    public static int getEditWidth()
    {
        return 5 * width / 8;
    }












}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.UIAdapter
 * JD-Core Version:    0.6.0
 */

