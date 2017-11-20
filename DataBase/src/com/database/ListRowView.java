package com.database;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.database.context.RuntimeInfo;

//import com.tomoney.finance.model.Deposit;

public class ListRowView extends LinearLayout
{
 	private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;//-2
    public int top_margin = 2;

	//构造函数
    public ListRowView(Context context, String[] listdata, int[] width_layout, int index)
    {
        super(context);
        //如果序列小于
        if (index <= 2147483408)
            //根据序列
        	switch (index)
            {
        	    //如果没有根据MainActivity.function
                default:
                    switch (MainActivity.function)
                    {
                        default:
                            addSubView(context, listdata, width_layout);
                            break;
                        case 100://首页
                        case 115://银行
                        case 145://借贷
                            //判断个数
							if (listdata.length != 2)
                            {
                                addSubView(context, listdata, width_layout);
                                break;
                            }
                            addSubViewOfTwoCols(context, listdata, width_layout);
                            break;
                        case 105://收支流水
                        case 108:
                      //  case 137:
                        case 149:
                        case 150:
                        case 151:
                        case 152:
                        case 153:
                        case 154:
                            addSubViewOfAudit(context, listdata, width_layout);
                            break;
                        case 163:
                            addSubViewOfDepositRank(context, listdata, width_layout, index);
					}
					break;
				case 2147483393://首页
					addSubViewOfMainPageLast(context, listdata, width_layout);
					break;
				//标题	
				case 2147483394:
					addTileorToolView(context, listdata, width_layout);
					break;
				case 2147483395://首页第一行
					addSubViewOfMainPageFirst(context, listdata, width_layout);
					break;
				case 2147483396:
					addSubViewOfAboutRegThanks(context, listdata, width_layout);
					break;
				case 2147483397:
					addSubViewOfListMore(context, listdata, width_layout);
					break;
			
            
            }
		else
			addSubViewOfSplitor(context, listdata, width_layout);
    }

	void addSubView(Context context, String[] listdata, int[] width_layout)
	{
		int[] layout_w = UIAdapter.getTableRowLayout(width_layout);
		//设置水平排列方式
		setOrientation(LinearLayout.HORIZONTAL);//0
		
		for (int i = 0; i < listdata.length; i++)
		{
			int j = layout_w[(i + 1)];
			
			if ((i == -1 + listdata.length) && (1 + listdata.length < layout_w.length))
				for (int k = i + 2; k < layout_w.length; k++)
					j += layout_w[k];
			
			LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(j, WC);
			//布局位置
			if (i == 0)
				//如果不是一个各和第一个大于320/8=40
				if ((listdata.length != 1) || (layout_w[0] >= UIAdapter.width / 8))
					localLayoutParams.leftMargin = layout_w[0];
				else
					localLayoutParams.leftMargin = (UIAdapter.width / 8);
			j = this.top_margin;
			//上
			localLayoutParams.bottomMargin = j;
			//下
			localLayoutParams.topMargin = j;
			addView(addTextView(context, listdata[i]), localLayoutParams);
		}
	}
	//添加文本
	TextView addTextView(Context context, String string)
	{
		TextView tv = new TextView(context);
		tv.setTextSize(UIAdapter.getTextSize());
		tv.setText(string);
		if (RuntimeInfo.param.flag[51] != 0)
			tv.setTextColor(Color.WHITE);//0
		else
			tv.setTextColor(Color.BLACK);//16777216
		return tv;
	}
	//标题和工具列
	void addTileorToolView(Context context, String[] listdata, int[] width_layout)
	{
		int[] layout_w = UIAdapter.getTableRowLayout(width_layout);
		setOrientation(LinearLayout.HORIZONTAL);//0
		Resources localResources = getResources();
		int i1;
		if (RuntimeInfo.param.flag[51] != 0)
			i1 = 2130837520;
		else
			i1 = R.drawable.listbox_blue;

		setBackgroundDrawable(NaviActivity.zoomDrawable(localResources.getDrawable(i1), UIAdapter.width, 14 * UIAdapter.getTextSize() / 10));
		for (int i = 0; i < listdata.length; i++)
		{
			int j = layout_w[(i + 1)];
			if ((i == -1 + listdata.length) && (1 + listdata.length < layout_w.length))
				for (int k = i + 2; k < layout_w.length; k++)
					j += layout_w[k];
			LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(j, WC);
			if (i == 0)
				if ((listdata.length != 1) || (layout_w[0] >= UIAdapter.width / 8))
					localLayoutParams.leftMargin = layout_w[0];
				else
					localLayoutParams.leftMargin = (UIAdapter.width / 8);
			j = this.top_margin;
			localLayoutParams.bottomMargin = j;
			localLayoutParams.topMargin = j;
			addView(addTextView(context, listdata[i]), localLayoutParams);
		}
	}
	//首页第一行
	void addSubViewOfMainPageFirst(Context paramContext, String[] paramArrayOfString, int[] paramArrayOfInt)
	{
		int[] localObject = new int[paramArrayOfInt.length];
		localObject[0] = paramArrayOfInt[0];
		localObject[1] = (2 * paramArrayOfInt[1] / 3);
		paramArrayOfInt[2] += paramArrayOfInt[1] / 2;
		UIAdapter.getTableRowLayout(localObject);
		setOrientation(0);
		for (int j = 0; j < paramArrayOfString.length; j++)
		{
			LinearLayout.LayoutParams  localObject1 = new LinearLayout.LayoutParams(-2, -2);
			if (j != 0)
			{
				if (j == 1)
					addSmile(paramContext);
			}
			else
				localObject1.leftMargin = (UIAdapter.width / 8);
			int i = this.top_margin;
			localObject1.bottomMargin = i;
			localObject1.topMargin = i;
			addView(addTextView(paramContext, paramArrayOfString[j]), localObject1);
		}
	}
	//首页
	void addSubViewOfMainPageLast(Context paramContext, String[] paramArrayOfString, int[] paramArrayOfInt)
	{
		int[] arrayOfInt = new int[paramArrayOfInt.length];
		arrayOfInt[0] = paramArrayOfInt[0];
		paramArrayOfInt[1] /= 2;
		paramArrayOfInt[2] += paramArrayOfInt[1] / 2;
		addSubView(paramContext, paramArrayOfString, arrayOfInt);
	}

	void addSubViewOfTwoCols(Context paramContext, String[] paramArrayOfString, int[] paramArrayOfInt)
	{
		UIAdapter.getTableRowLayout(paramArrayOfInt);
		setOrientation(0);
		for (int i = 0; i < paramArrayOfString.length; i++)
		{
			LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(3 * UIAdapter.width / 8, -2);
			if (i == 0)
				localLayoutParams.leftMargin = (UIAdapter.width / 8);
			int j = this.top_margin;
			localLayoutParams.bottomMargin = j;
			localLayoutParams.topMargin = j;
			TextView localTextView = addTextView(paramContext, paramArrayOfString[i]);
			if (i == 1)
				localTextView.setGravity(5);
			addView(localTextView, localLayoutParams);
		}
	}
	//银行
	void addSubViewOfSplitor(Context paramContext, String[] paramArrayOfString, int[] paramArrayOfInt)
	{
		UIAdapter.getTableRowLayout(paramArrayOfInt);
		setOrientation(0);
		Resources localResources = getResources();
		int i1;
		if (RuntimeInfo.param.flag[51] != 0)
			i1 = 2130837520;
		else
			i1 = R.drawable.listbox_blue;

		setBackgroundDrawable(NaviActivity.zoomDrawable(localResources.getDrawable(i1), UIAdapter.width, 14 * UIAdapter.getTextSize() / 10));

		for (int i = 0; i < paramArrayOfString.length; i++)
		{
			int j = 3 * UIAdapter.width / 8;
			LinearLayout.LayoutParams localLayoutParams;
			if (paramArrayOfString.length != 1)
				localLayoutParams = new LinearLayout.LayoutParams(j, -2);
			else
				localLayoutParams = new LinearLayout.LayoutParams(7 * UIAdapter.width / 8, -2);
			if (i == 0)
				localLayoutParams.leftMargin = (UIAdapter.width / 16);
			int k = this.top_margin;
			localLayoutParams.bottomMargin = k;
			localLayoutParams.topMargin = k;
			TextView localTextView = addTextView(paramContext, paramArrayOfString[i]);
			if (paramArrayOfString.length != 1)
			{
				if (i != 1)
					localTextView.setGravity(3);
				else
					localTextView.setGravity(5);
			}
			else
				localTextView.setGravity(17);
			addView(localTextView, localLayoutParams);
		}
	}

	///////////////
	Button addButton(Context paramContext, String paramString)
	{
		Button localButton = new Button(paramContext);
		localButton.setPadding(0, 0, 0, 0);
		localButton.setTextSize(UIAdapter.getTextSize());
		localButton.setText(paramString);
		if (RuntimeInfo.param.flag[51] != 0)
			localButton.setTextColor(Color.WHITE);
		else
			localButton.setTextColor(-16777216);
		return localButton;
	}

	void addSmile(Context paramContext)
	{
		ImageView localImageView = new ImageView(paramContext);
		localImageView.setImageDrawable(getResources().getDrawable(2130837526));
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(UIAdapter.getTextSize(), UIAdapter.getTextSize());
		localLayoutParams.rightMargin = 3;
		localLayoutParams.leftMargin = 3;
		localLayoutParams.gravity = 16;
		addView(localImageView, localLayoutParams);
	}



	void addSubViewOfAboutRegThanks(Context paramContext, String[] paramArrayOfString, int[] paramArrayOfInt)
	{
		addSmile(paramContext);
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(UIAdapter.width, -2);
		localLayoutParams.leftMargin = 2;
		int i = this.top_margin;
		localLayoutParams.bottomMargin = i;
		localLayoutParams.topMargin = i;
		addView(addTextView(paramContext, paramArrayOfString[0]), localLayoutParams);
	}

	void addSubViewOfAudit(Context paramContext, String[] paramArrayOfString, int[] paramArrayOfInt)
	{
		int[] arrayOfInt = UIAdapter.getTableRowLayout(paramArrayOfInt);
		setOrientation(0);
		int i = 0;
		if (paramArrayOfString[(-1 + paramArrayOfString.length)].charAt(0) != '+')
		{
			if (paramArrayOfString[(-1 + paramArrayOfString.length)].charAt(0) != '-')
			{
				ImageView localImageView = new ImageView(paramContext);
				localImageView.setImageDrawable(getResources().getDrawable(2130837527));
				LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(UIAdapter.getTextSize(), UIAdapter.getTextSize());
				localLayoutParams1.rightMargin = 0;
				localLayoutParams1.leftMargin = 0;
				localLayoutParams1.gravity = 16;
				addView(localImageView, localLayoutParams1);
				paramArrayOfString[(-1 + paramArrayOfString.length)] = ("-" + paramArrayOfString[(-1 + paramArrayOfString.length)]);
			}
			else
			{
				i = 1;
			}
		}
		else
			addSmile(paramContext);
		for (int j = 0; j < paramArrayOfString.length; j++)
		{
			int m = arrayOfInt[(j + 1)];
			if ((j == -1 + paramArrayOfString.length) && (1 + paramArrayOfString.length < arrayOfInt.length))
				for (int k = j + 2; k < arrayOfInt.length; k++)
					m += arrayOfInt[k];
			LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(m, -2);
			if ((j != 0) || (i == 0))
				localLayoutParams2.leftMargin = 0;
			else
				localLayoutParams2.leftMargin = arrayOfInt[0];
			m = this.top_margin;
			localLayoutParams2.bottomMargin = m;
			localLayoutParams2.topMargin = m;
			addView(addTextView(paramContext, paramArrayOfString[j]), localLayoutParams2);
		}
	}

	void addSubViewOfDepositRank(Context paramContext, String[] paramArrayOfString, int[] paramArrayOfInt,final int paramInt)
	{
		int[] arrayOfInt = UIAdapter.getTableRowLayout(paramArrayOfInt);
		setOrientation(0);
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(arrayOfInt[1], -2);
		localLayoutParams.leftMargin = arrayOfInt[0];
		int i = this.top_margin;
		localLayoutParams.bottomMargin = i;
		localLayoutParams.topMargin = i;
		addView(addTextView(paramContext, paramArrayOfString[0]), localLayoutParams);
		localLayoutParams = new LinearLayout.LayoutParams(60, 2 * UIAdapter.getTextSize());
		localLayoutParams.leftMargin = this.top_margin;
		localLayoutParams.topMargin = 1;
		localLayoutParams.bottomMargin = 0;
		Button localButton = addButton(paramContext, "↑");
		addView(localButton, localLayoutParams);
		localButton.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View paramView)
				{
					//   if (new Deposit(this.val$id).moveUp())
					//     RuntimeInfo.main.refresh();
				}
			});
		localLayoutParams = new LinearLayout.LayoutParams(60, 2 * UIAdapter.getTextSize());
		localLayoutParams.leftMargin = this.top_margin;
		localLayoutParams.topMargin = 1;
		localLayoutParams.bottomMargin = 0;
		localButton = addButton(paramContext, "↓");
		addView(localButton, localLayoutParams);
		localButton.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View paramView)
				{
					//   if (new Deposit(this.val$id).moveDown())
					//     RuntimeInfo.main.refresh();
				}
			});
	}

	void addSubViewOfListMore(Context paramContext, String[] paramArrayOfString, int[] paramArrayOfInt)
	{
		UIAdapter.getTableRowLayout(paramArrayOfInt);
		setOrientation(0);
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(UIAdapter.width, -2);
		int i = this.top_margin;
		localLayoutParams.bottomMargin = i;
		localLayoutParams.topMargin = i;
		TextView localTextView = addTextView(paramContext, paramArrayOfString[0]);
		localTextView.setGravity(17);
		addView(localTextView, localLayoutParams);
		localTextView.setOnTouchListener(new View.OnTouchListener()
			{
				public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
				{
					//  if (paramMotionEvent.getAction() == 1)
					//  RuntimeInfo.main.listMore();
					return true;
				}
			});
	}






	
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.FinanceRowView
 * JD-Core Version:    0.6.0
 */

