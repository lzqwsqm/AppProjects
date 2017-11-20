package com.database;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
//import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
//import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
//import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.database.context.RuntimeInfo;
import com.database.model.Deposit;
import com.database.model.Bank;

import com.database.model.Evection;
import com.database.model.EvectionAudit;
import com.database.model.Credit;
import com.database.model.CreditAudit;
import com.database.model.Asset;
import com.database.model.AssetAudit;
import com.database.model.Favor;
import com.database.model.FavorAudit;
import com.database.model.Audit;
//import com.database.model.Friend;
//import com.database.model.Insurance;
//import com.database.model.InvestAudit;

import com.database.util.Convertor;
/*
import com.tomoney.finance.util.FDate;*/
import java.util.Date;
import java.util.Vector;

public class DetailActivity extends Activity
  implements View.OnClickListener
{
 	private static final int FP = ViewGroup.LayoutParams.FILL_PARENT;//-1
	private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;//-2
    static int function = 0;
    TableLayout layout = null;
    TableLayout.LayoutParams layout_param = null;
    Button menubutton = null;
    TextView titlebar = null;
    TableRow.LayoutParams tr_layout_param = null;
    
	@Override
	//首先调用创建窗口
	public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(16973835);
        super.onCreate(savedInstanceState);
        this.layout = new TableLayout(this);
        ScrollView localScrollView = new ScrollView(this);
        setContentView(localScrollView);
        RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(WC, WC);
        localScrollView.addView(this.layout, localLayoutParams);
        
		this.layout_param = new TableLayout.LayoutParams(WC, WC);
        this.layout_param.topMargin = 5;
        this.layout_param.leftMargin = 13;
       
	    this.tr_layout_param = new TableRow.LayoutParams(UIAdapter.getEditWidth(), WC);
        this.tr_layout_param.leftMargin = 10;
        displayDetail();
    }
	
	public boolean onPrepareOptionsMenu(Menu paramMenu)
	{
		openContextMenu(this.menubutton);
		return true;
	}
    
	public boolean onOptionsItemSelected(MenuItem paramMenuItem)
    {
       return commandAction(paramMenuItem.getItemId());
    }


	public void onWindowFocusChanged(boolean paramBoolean)
	{
	}
	//////////////////
    void displayDetail()
    {
        addTitleBar();
        switch (function)
        {
			case 115://银行
                depositDetail();
                break;
			///////
			case 105:
			      auditDetail();
			      break;  
                /////
			case 145://借贷
				creditDetail();
				break;
			case 146:
				creditAuditDetail();
				break;
			//-------	
			case 155://资产列表
				assetDetail();
				break;		
			case 156://资产流水
				assetAuditDetail();
				break;
			//---
			case 141://人情流水
				favorAuditDetail();
				break;	
            //-----
		    case 135://项目
				evectionDetail();
				break;
			case 136://项目流水
				evectionAuditDetail();
				break;

		}
    }
	void addTitleBar()
	{
		this.titlebar = new TextView(this);
		this.titlebar.setBackgroundColor(Color.TRANSPARENT);//0
		this.titlebar.setTextSize(UIAdapter.getDetailTextSize());
		this.titlebar.setGravity(Gravity.CENTER);//17
		LinearLayout localLinearLayout = new LinearLayout(this);
		this.layout.addView(localLinearLayout);
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(WC, WC, 1.0F);
		localLinearLayout.addView(this.titlebar, localLayoutParams);
	}
	void depositDetail()
	{
		setTitle("账户信息");
		Deposit localObject = new Deposit(MainActivity.params[3]);
		addTextView("名称", localObject.name);
		addTextView("储种", localObject.getDepositTypeName());
		switch (localObject.type)
		{
			case 1://活期			
				addTextDecimal("余额", localObject.sum);
				addTextView("卡/账号", localObject.getDisplayCardno());
				break;
			case 2://都是定期
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				addTextDecimal("余额", localObject.sum);
				addTextDecimal("利率", localObject.rate);
				addTextDecimal("到期利息", localObject.getInterest());
				addTextView("到期日期", localObject.getAtTermDate().toString());
				break;	
			case 12://现金
				addTextDecimal("余额", localObject.sum);
				break;

		}
		if (localObject.bankid > 0)
			addTextView("银行", new Bank(localObject.bankid).name);
		if (localObject.type != 12)
		{
			addButtonOkCancel();
		}
		else
		{
			String[] cmd = new String[3];
			cmd[0] = "设置余额";
			cmd[1] = "转账流水";
			cmd[2] = "返 回";
			int[] id = new int[3];
			id[0] = 1151;
			id[1] = 1160;
			id[2] = 12;
			addButtons(cmd, id);
		}
	}
	
	void auditDetail()
	{
	    setTitle("流水信息");
	    Audit audit = new Audit(MainActivity.params[3]);
	    switch (audit.kmid)
	    {
	    case 265:
	     // addTextView("项目", new Evection(new EvectionAudit(EvectionAudit.getIdByAuditId(((Audit)localObject).id)).eid).city);
	      break;
	    case 266:
	    case 297:
	    //  addTextView("好友", new Friend(new Favor(Favor.getIdByAuditId(((Audit)localObject).id)).friendid).name);
	      break;
	    case 268:
	    case 271:
	    case 274:
	    case 292:
	    case 293:
	      addTextView("借贷", new Credit(new CreditAudit(CreditAudit.getIdByAuditId(audit.id)).credit_id).name);
	      break;
	    case 270:
	    case 295:
	     // addTextView("保险", new Insurance(new InvestAudit(InvestAudit.getIdByAuditId(((Audit)localObject).id)).stockid).name);
	      break;
	    case 273:
	    case 294:
	      addTextView("出差", new Evection(new EvectionAudit(EvectionAudit.getIdByAuditId(audit.id)).eid).city);
	    }
	    addTextView("科目", audit.getKm1Name());
	    addTextView("", audit.getKm2Name());
	    addTextDecimal("金额", audit.sum);
	    if (audit.deposit_id > 0)
	      addTextView("支付账户", new Deposit(audit.deposit_id).name);
	    addTextView("日期", audit.date.toString());
	    addTextView("备注", audit.content);
	    int[] arrayOfInt;
	    if ((audit.kmid != 273) && (audit.kmid != 294) && (audit.kmid != 270) && (audit.kmid != 295) && (audit.kmid != 271) && (audit.kmid != 292) && (audit.kmid != 274) && (audit.kmid != 293) && (audit.kmid != 268))
	    {
	    	String[] localObject = new String[3];
	      localObject[0] = "修 改";
	      localObject[1] = "删 除";
	      localObject[2] = "返 回";
	      arrayOfInt = new int[3];
	      arrayOfInt[0] = 1062;
	      arrayOfInt[1] = 1063;
	      arrayOfInt[2] = 12;
	      addButtons(localObject, arrayOfInt);
	    }
	    else
	    {
	    	String[]localObject = new String[2];
	      localObject[0] = "删 除";
	      localObject[1] = "返 回";
	      arrayOfInt = new int[2];
	      arrayOfInt[0] = 1063;
	      arrayOfInt[1] = 12;
	      addButtons(localObject, arrayOfInt);
	    }
	  }

	
	void creditDetail()
	{
		setTitle("借贷明细信息");
		Credit localCredit = new Credit(MainActivity.params[3]);
		addTextView("名称", localCredit.name);
		addTextView("类型", localCredit.getTypeName());
		addTextDecimal("金额", localCredit.sum);
		if (localCredit.bankid == 0)
			localCredit.bankid = 1;
		switch (localCredit.type)
		{
			case 0:
			case 1:
				addTextView("备注", localCredit.content);
				addTextView("日期", localCredit.date.toString());
				break;

		}
		addButtonOkCancel();
	}
    void creditAuditDetail()
    {
        setTitle("借贷流水信息");
        Object localObject = new CreditAudit(MainActivity.params[3]);
        addTextView("类型", ((CreditAudit)localObject).getTypeName());
        addTextView("借贷帐户", new Credit(((CreditAudit)localObject).credit_id).name);
        addTextDecimal("金额", ((CreditAudit)localObject).sum);
        if (((CreditAudit)localObject).deposit_id > 0)
            addTextView("支付帐户", new Deposit(((CreditAudit)localObject).deposit_id).name);
        addTextView("日期", ((CreditAudit)localObject).date.toString());
        addTextView("备注", ((CreditAudit)localObject).content);
        String[] arrayOfString = new String[2];
        arrayOfString[0] = "删 除";
        arrayOfString[1] = "返 回";
        int []localObject1 = new int[2];
        localObject1[0] = 3085;
        localObject1[1] = 12;
        addButtons(arrayOfString, localObject1);
    }
	//----------
	void assetDetail()
	{
		Asset localObject = new Asset(((int[])MainActivity.paramstack.get(0))[3]);
	    setTitle("项目信息");
		addTextView("名称", localObject.city);
		addTextDecimal4("单价", localObject.bulky);
		addTextView("开始日期", localObject.date.toString());
		if (localObject.isFinished())
			addTextView("结束日期", localObject.getEndDate().toString());
		addTextView("备注", localObject.content);
		String[] bn = new String[3];
		bn[0] = "修 改";
		bn[1] = "删 除";
		bn[2] = "返 回";
		int[] bn_id = new int[3];
		bn_id[0] = 1552;
		bn_id[1] = 1553;
		bn_id[2] = 12;
		addButtons(bn, bn_id);
	}
	void assetAuditDetail()
	{
		AssetAudit localObject2 = new AssetAudit(MainActivity.params[3]);
		Asset localObject1 = new Asset(localObject2.eid);
		setTitle("流水信息");
		addTextView("项目", localObject1.city);
		addTextView("科目", localObject1.getKmName(localObject2.kmid));
		addTextDecimalf("数量", localObject2.nub);
		addTextDecimal4("金额", localObject2.sum);
		if (localObject2.deposit_id > 0)
			addTextView("支付帐户", new Deposit(localObject2.deposit_id).name);
		addTextView("日期", localObject2.date.toString());
		addTextView("备注", localObject2.content);
		if (!localObject1.isFinished())
		{
			String[] cmd = new String[3];
			cmd[0] = "修 改";
			cmd[1] = "删 除";
			cmd[2] = "返 回";
			int[] id = new int[3];
			id[0] = 1562;
			id[1] = 1563;
			id[2] = 12;
			addButtons(cmd, id);
		}
		else if (localObject2.kmid != 1)
		{
			String[] cmd = new String[1];
			cmd[0] = "返 回";
			int[] id = new int[1];
			id[0] = 12;
			addButtons(cmd,id);
		}
		else
		{
			String[] cmd = new String[2];
			cmd[0] = "删 除";
			cmd[1] = "返 回";
			int[] id = new int[2];
			id[0] = 3077;
			id[1] = 12;
			addButtons(cmd, id);
		}
	}
//---
	void favorAuditDetail()
	{
		FavorAudit localObject2 = new FavorAudit(MainActivity.params[3]);
		Favor localObject1 = new Favor(localObject2.eid);
		setTitle("流水信息");
		addTextView("项目", localObject1.city);
		addTextView("科目", localObject1.getKmName(localObject2.kmid));
		//addTextDecimal("数量", localObject2.nub);
		addTextDecimal("金额", localObject2.sum);
		if (localObject2.deposit_id > 0)
			addTextView("支付帐户", new Deposit(localObject2.deposit_id).name);
		addTextView("日期", localObject2.date.toString());
		addTextView("备注", localObject2.content);
		if (!localObject1.isFinished())
		{
			String[] cmd = new String[3];
			cmd[0] = "修 改";
			cmd[1] = "删 除";
			cmd[2] = "返 回";
			int[] id = new int[3];
			id[0] = 1412;
			id[1] = 1413;
			id[2] = 12;
			addButtons(cmd, id);
		}
		else if (localObject2.kmid != 1)
		{
			String[] cmd = new String[1];
			cmd[0] = "返 回";
			int[] id = new int[1];
			id[0] = 12;
			addButtons(cmd,id);
		}
		else
		{
			String[] cmd = new String[2];
			cmd[0] = "删 除";
			cmd[1] = "返 回";
			int[] id = new int[2];
			id[0] = 3077;
			id[1] = 12;
			addButtons(cmd, id);
		}
	}
	//-------------
	void evectionDetail()
	{
		Evection localObject = new Evection(((int[])MainActivity.paramstack.get(0))[3]);
	//	if (!localObject.isEvection())
		{
			setTitle("项目信息");
			addTextView("名称", localObject.city);
		}
	//	else
		{
	//		setTitle("出差信息");
	//		addTextView("城市", ((Evection)localObject).city);
		}
		addTextView("开始日期", localObject.date.toString());
		if (localObject.isFinished())
			addTextView("结束日期", localObject.getEndDate().toString());
		addTextView("备注", localObject.content);
		String[] bn = new String[3];
		bn[0] = "修 改";
		bn[1] = "删 除";
		bn[2] = "返 回";
		int[] bn_id = new int[3];
		bn_id[0] = 1352;
		bn_id[1] = 1353;
		bn_id[2] = 12;
		addButtons(bn, bn_id);
	}
	void evectionAuditDetail()
	{
		EvectionAudit localObject2 = new EvectionAudit(MainActivity.params[3]);
		Evection localObject1 = new Evection(((EvectionAudit)localObject2).eid);
		setTitle("流水信息");
	//	if (!((Evection)localObject1).isEvection())
	//	{
	//		if (!((Evection)localObject1).isAuditProject())
				addTextView("项目", localObject1.city);
	//		else
	//			addTextView("大宗支出", localObject1.city);
	//	}
	//	else
	//		addTextView("出差", ((Evection)localObject1).city);
		addTextView("科目", localObject1.getKmName(localObject2.kmid));
		addTextDecimal("金额", localObject2.sum);
		if (localObject2.deposit_id > 0)
			addTextView("支付帐户", new Deposit(localObject2.deposit_id).name);
		addTextView("日期", localObject2.date.toString());
		addTextView("备注", localObject2.content);
		if (!localObject1.isFinished())
		{
			String[] cmd = new String[3];
			cmd[0] = "修 改";
			cmd[1] = "删 除";
			cmd[2] = "返 回";
			int[] id = new int[3];
			id[0] = 1362;
			id[1] = 1363;
			id[2] = 12;
			addButtons(cmd, id);
		}
		else if (localObject2.kmid != 1)
		{
			String[] cmd = new String[1];
			cmd[0] = "返 回";
			int[] id = new int[1];
			id[0] = 12;
			addButtons(cmd,id);
		}
		else
		{
			String[] cmd = new String[2];
			cmd[0] = "删 除";
			cmd[1] = "返 回";
			int[] id = new int[2];
			id[0] = 3039;
			id[1] = 12;
			addButtons(cmd, id);
		}
	}
	//---------------
	void setTitle(String paramString)
	{
		this.titlebar.setText(paramString);
	}
	

	void addButtons(String[] paramArrayOfString, int[] paramArrayOfInt)
	{
		LinearLayout localLinearLayout = new LinearLayout(this);
		this.layout.addView(localLinearLayout);
		for (int i = 0; i < paramArrayOfInt.length; i++)
		{
			Button localButton = new Button(this);
			localButton.setText(paramArrayOfString[i]);
			localButton.setId(paramArrayOfInt[i]);
			localButton.setOnClickListener(this);
			LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2, 1.0F);
			localLayoutParams.bottomMargin = 6;
			localLayoutParams.topMargin = 6;
			localLinearLayout.setOrientation(0);
			localLayoutParams.leftMargin = 5;
			localLayoutParams.rightMargin = 5;
			localLinearLayout.addView(localButton, localLayoutParams);
		}
	}
	
	void addButtonOkCancel()
    {
        LinearLayout localLinearLayout = new LinearLayout(this);
        this.layout.addView(localLinearLayout);
        this.menubutton = new Button(this);
        this.menubutton.setText("菜 单");
        this.menubutton.setId(11);
        this.menubutton.setOnClickListener(this);
        registerForContextMenu(this.menubutton);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(WC, WC, 1.0F);
        localLayoutParams.bottomMargin = 6;
        localLayoutParams.topMargin = 6;
        localLinearLayout.setOrientation(LinearLayout.HORIZONTAL);//0
        localLayoutParams.leftMargin = 5;
        localLayoutParams.rightMargin = 5;
        localLinearLayout.addView(this.menubutton, localLayoutParams);
        Button localButton = new Button(this);
        localButton.setText("返 回");
        localButton.setId(12);
        localButton.setOnClickListener(this);
        localLayoutParams.bottomMargin = 6;
        localLayoutParams.topMargin = 6;
        localLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        localLayoutParams.leftMargin = 5;
        localLayoutParams.rightMargin = 5;
        localLinearLayout.addView(localButton, localLayoutParams);
    }

	void addTextView(String paramString1, String paramString2)
	{
		TableRow localTableRow = new TableRow(this);
		this.layout.addView(localTableRow, this.layout_param);
		localTableRow.addView(addLabel(paramString1));
		TextView localTextView = new TextView(this);
		localTextView.setText(paramString2);
		localTextView.setTextSize(UIAdapter.getDetailTextSize());
		localTableRow.addView(localTextView, this.tr_layout_param);
	}
	TextView addLabel(String paramString)
	{
		TextView localTextView = new TextView(this);
		localTextView.setText(paramString);
		localTextView.setTextSize(UIAdapter.getDetailTextSize());
		localTextView.setGravity(Gravity.RIGHT);
		return localTextView;
	}
	void addTextDecimal(String paramString1, String paramString2)
	{
		TableRow localTableRow = new TableRow(this);
		this.layout.addView(localTableRow, this.layout_param);
		localTableRow.addView(addLabel(paramString1));
		TextView localTextView = new TextView(this);
		localTextView.setText(paramString2);
		localTextView.setTextSize(UIAdapter.getDetailTextSize());
		localTableRow.addView(localTextView, this.tr_layout_param);
	}
	
	
	void addTextDecimal(String paramString, long paramLong)
	{
		addTextDecimal(paramString, Convertor.sumToString(paramLong));
	}
	
	void addTextDecimalf(String paramString, float paramLong)
	{
		addTextDecimal(paramString, Convertor.sumToStringf(paramLong));
	}
	void addTextDecimal4(String paramString, float paramLong)
	{
		addTextDecimal(paramString, Convertor.sumToStringf(paramLong));
	}
	
	/////////////////////////
	public void onClick(View paramView)
	{
		switch (paramView.getId())
		{
			default:
				commandAction(paramView.getId());
				break;
			case 11:
				openContextMenu(this.menubutton);
				break;
			case 12:
				returnToView();
				break;
		}
	}
	public boolean commandAction(int paramInt)
	{
		boolean bool = true;
		switch (paramInt)
		{
			default:
				returnToForm(paramInt);
				break;
			case 17:
		//		returnToView();
				break;
			case 109:
			case 1153://银行列表
			case 1453://借贷列表
			case 1553://资产列表
			case 3048:
			case 3058:
			case 3064:
			case 3067:
			case 3071:
				deleteConfirmAction("删除仅在登记已有输入错误的情况下使用,否则可能会引起帐务混乱,确认删除?", paramInt);
				break;
			case 1007:
			case 1063://删除收支流水
			case 1115:
			case 1144:
			case 1363://删除项目
			case 1413://删除人情流水
			case 1621:
			case 3039:
			case 3049:
			case 3075:
			case 1563://资产流水
			case 3084:
			case 3085://借贷流水
			case 3087:
			case 3089:
				deleteConfirmAction("确认要删除吗?", paramInt);
				break;
			case 1023:
				bool = false;
				break;
			case 1461://借贷流水
				RuntimeInfo.main.displayNewList(146);
				finish();
				break;
			case 1125:
		//		deleteConfirmAction("确认要暴力删除?后果自负", paramInt);
				break;
			case 2001:
		//		if (RuntimeInfo.param.flag[2] != 0)
		//			returnToForm(paramInt);
		//		else
		//			showNote("请先设置隐私密码！");
				break;
			case 3016:
			case 3027:
			case 3034:
			case 3037:
			case 3046:
	//			checkoutConfirmAction("确认要注销帐户吗?");
				break;
			case 3024:
	//			checkoutConfirmAction("确认要作废帐户吗?");
				break;
			case 1160:
				RuntimeInfo.main.displayNewList(116);
				finish();
				break;
			case 1463://借贷坏帐
			case 3036:
				creditBadAction();
				break;
			case 3038:
				if (!new EvectionAudit(MainActivity.params[3]).canBeModified())
					showNote("出差已经结束,流水不能修改/删除！");
				else
					returnToForm(paramInt);
				break;
			case 3045:
	//			RuntimeInfo.main.displayNewList(127);
				finish();
				break;
			case 3072:
	//			checkoutConfirmAction("确认要终止保单吗?");
	            break;
		}
	
		return bool;
	}
	
	void deleteConfirmAction(String paramString,final int cmd)
	{
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this).setTitle("确认删除 ").setMessage(paramString);
		localBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface paramDialogInterface, int paramInt)
				{
					DetailActivity.this.deleteAction(cmd);
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
		String str = "";
	//	if (paramInt != 1125)
			switch (function)
			{
				default:
					break;
				case 115://删除银行
					str = new Deposit(MainActivity.params[3]).delete();
                    break;
                //    
				case 105:
			        str = new Audit(MainActivity.params[3]).delete();
			        break;
                    
                    //---------
				case 145://借贷列表
					str = new Credit(MainActivity.params[3]).delete();
					break;
				case 146://借贷流水
					str = new CreditAudit(MainActivity.params[3]).delete();
					break;
				//---------	
				case 155://删除资产
					str = new Asset(((int[])MainActivity.paramstack.get(0))[3]).delete();
					if (str != "操作成功完成！")
						break;
					MainActivity.popParams();	
                    break;
				case 156://删除资产流水
					str = new AssetAudit(MainActivity.params[3]).delete();
					break;
			   //---
				case 141://删除项目流水
					str = new FavorAudit(MainActivity.params[3]).delete();
					break;
					//--------
				case 135://删除项目
					str = new Evection(((int[])MainActivity.paramstack.get(0))[3]).delete();
					if (str != "操作成功完成！")
						break;
					MainActivity.popParams();
					break;
			    case 136://删除项目流水
					str = new EvectionAudit(MainActivity.params[3]).delete();
					break;

		

			}
		//	else
		//	str = new Audit(MainActivity.params[3]).deleteWithForce();
		returnToView(str);
	}
	
	void returnToForm(int paramInt)
	{
		FormActivity.detail = this;
		FormActivity.function = paramInt;
		Intent localIntent = new Intent();
		localIntent.setClass(this, FormActivity.class);
		startActivity(localIntent);
	}
	
	void returnToView(String paramString)
    {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this).setTitle("提示信息 ").setMessage(paramString);
        if (paramString != "操作成功完成！")
            localBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramDialogInterface, int paramInt)
					{
					}
				});
        else
            localBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramDialogInterface, int paramInt)
					{
						RuntimeInfo.main.refresh();
						DetailActivity.this.returnToView();
					}
				});
		localBuilder.show();
    }
    void showNote(String paramString)
	{
		new AlertDialog.Builder(this).setTitle("提示信息 ").setMessage(paramString).setPositiveButton("确认", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface paramDialogInterface, int paramInt)
				{
				}
			}).show();
	}
	void creditBadAction()
	{
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this).setTitle("确认信息 ").setMessage("确认要进行坏账操作吗?");
		localBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface paramDialogInterface, int paramInt)
				{
					String note = null;
					Credit localObject = new Credit(MainActivity.params[3]);
					if (localObject.type != 1)
					{
						if (localObject.type == 0)
						//	localObject = ((Credit)localObject).creditBad(new Date());
					//	else
							note = localObject.lendBad(new Date());
					}
					else
						note = localObject.borrowBad(new Date());
					DetailActivity.this.returnToView(note);
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

//public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) 

	public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo)
	{
		int k = 0;
		int m;
		int i1;
		int j;
		switch (function)
		{
			case 115:
				Deposit localDeposit = new Deposit(MainActivity.params[3]);
				switch (localDeposit.type)
				{
					case 1://
						if (localDeposit.isFatherDeposit())
							break;
						m = k + 1;
						paramContextMenu.add(0, 1160, k, "转账流水");
						k = m + 1;
						paramContextMenu.add(0, 1161, m, "存取");
						i1 = k + 1;
						paramContextMenu.add(0, 1162, k, "转账");
						m = i1 + 1;
						paramContextMenu.add(0, 1163, i1, "银行收费");
						k = m + 1;
						paramContextMenu.add(0, 1164, m, "计息");
						m = k + 1;
						paramContextMenu.add(0, 1165, k, "销户");
						k = m + 1;
						paramContextMenu.add(0, 2001, m, "卡/账号");
			//			k = k;
						break;
					case 2://定期
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						m = k + 1;
						paramContextMenu.add(0, 1171, k, "到期支取");
						k = m + 1;
						paramContextMenu.add(0, 1172, m, "提前支取");
						m = k;
						break;
				}
				if (localDeposit.type != 12)
				{
					int i = k + 1;
					paramContextMenu.add(0, 1152, k, "修改信息");
					if (RuntimeInfo.param.flag[8] != 0)
						break;
					k = i + 1;
					paramContextMenu.add(0, 1153, i, "*删除");
				}
				break;
			case 145:
				Credit localCredit = new Credit(MainActivity.params[3]);
				switch (localCredit.type)
				{
					case 0:
					case 1:
						if (localCredit.sum <= 0L)
						{
							j = k + 1;
							paramContextMenu.add(0, 1462, k, "续借");
							k = j + 1;
							paramContextMenu.add(0, 3034, j, "注销");
						}
						else
						{
							m = k + 1;
							paramContextMenu.add(0, 1461, k, "还款");
							j = m + 1;
							paramContextMenu.add(0, 1462, m, "续借");
							k = j + 1;
							paramContextMenu.add(0, 1463, j, "坏帐");
							//k = k;
						}
						break;

				}
				j = k + 1;
				paramContextMenu.add(0, 1461, k, "借贷流水");
				k = j + 1;
				paramContextMenu.add(0, 1452, j, "修改信息");
				if (RuntimeInfo.param.flag[8] == 0)
				{
					j = k + 1;
					paramContextMenu.add(0, 1453, k, "*删除");
				}
				break;

		}
		
	}
	public boolean onContextItemSelected(MenuItem paramMenuItem)
	{
		return commandAction(paramMenuItem.getItemId());
	}

	
	void returnToView()
	{
		finish();
	}
	


	
	//外部调用////////////////////////
	public void redisplay()
	{
		this.layout.removeAllViews();
		displayDetail();
	}
	
	}		
/* 







 





/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.DetailActivity
 * JD-Core Version:    0.6.0
 */
