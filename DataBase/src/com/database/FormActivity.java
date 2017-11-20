/*
 * 记帐窗口
 */
package com.database;


import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
//import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
//import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.view.Window;
//import android.view.View.OnClickListener;
import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
//import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
//import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.database.context.RuntimeInfo;
import com.database.util.MyDate;
import com.database.util.Convertor;
import com.database.util.DBTool;

import com.database.model.Param;
import com.database.model.Deposit;
import com.database.model.Bank;
import com.database.model.InterestRate;

import com.database.model.Evection;
import com.database.model.EvectionAudit;
import com.database.model.KM;
import com.database.model.Audit;
import com.database.model.Credit;
import com.database.model.Asset;
import com.database.model.AssetAudit;
import com.database.model.AssetKm;
import com.database.model.Favor;
import com.database.model.FavorAudit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class FormActivity extends Activity
  //功能
  implements View.OnClickListener
{
    private static final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
//  public static CheckInfoPasswordListenner checkInfoPasswordListenner;
    public static DetailActivity detail;
    public static int function = 0;
    List<Integer> banklist = new ArrayList();
    final Date dateinput = new Date();
    final Date dateinput1 = new Date();
    List<Integer> depositlist = new ArrayList();
	List<Integer> typelist = new ArrayList();
    List<Integer> km1list = new ArrayList();
    List<Integer> km2list = new ArrayList();
    TableLayout layout = null;
    TableLayout.LayoutParams layout_param = null;
    TextView titlebar = null;
    TableRow.LayoutParams tr_label_layout_param = null;
    TableRow.LayoutParams tr_text_layout_param = null;

    static
    {
//      checkInfoPasswordListenner = null;
        detail = null;
    }
	
	
	@Override
	/**
	 *主布局是相对布局添加表格布局
	 *窗口由标题栏和内容和选择键组成
	 */
	//首先调用创建窗口
	public void onCreate(Bundle savedInstanceState)
    {
		//窗口无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setTheme(16973835);
        super.onCreate(savedInstanceState);
   		//滚动视图是指当拥有很多内容，屏幕显示不完时，需要通过滚动跳来显示的视图。ScrollView只支持垂直滚动。
		ScrollView sv = new ScrollView(this);
        setContentView(sv);
        //相对布局
		RelativeLayout localRelativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(WC, WC);
        sv.addView(localRelativeLayout);
        //表格布局
		this.layout = new TableLayout(this);
        localRelativeLayout.addView(this.layout, localLayoutParams);
        //滚动条样式
		this.layout.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//0
        //表格布局设置
		this.layout_param = new TableLayout.LayoutParams(FP, WC);
        this.layout_param.topMargin = 3;
        this.layout_param.leftMargin = 0;
        //文本框位置
		this.tr_text_layout_param = new TableRow.LayoutParams(UIAdapter.getEditWidth(), WC);
        this.tr_text_layout_param.leftMargin = 5;
        this.tr_text_layout_param.rightMargin = 5;
        //标签位置
		this.tr_label_layout_param = new TableRow.LayoutParams(WC, WC);
        this.tr_label_layout_param.leftMargin = 5;
        this.tr_label_layout_param.rightMargin = 0;
        //显示
		displayForm();
    }
	//菜单
	public boolean onPrepareOptionsMenu(Menu paramMenu)
	{
		super.onPrepareOptionsMenu(paramMenu);
		paramMenu.clear();
		int i;
		if (function != 1016)
		{
			i = 0 + 1;
			paramMenu.add(0, 11, 0, "确认");
			i = i + 1;
			paramMenu.add(0, 12, i, "返回");
		}
		else if (this.titlebar.getText().toString().indexOf("超市购物") < 0)
		{
			i = 0 + 1;
			paramMenu.add(0, 18, 0, "全部保存");
			i = i + 1;
			paramMenu.add(0, 20, i, "返回记账");
		}
		else
		{
			i = 0 + 1;
			paramMenu.add(0, 11, 0, "保存");
			i = i + 1;
			paramMenu.add(0, 19, i, "放弃");
		}
		return true;
	}
	//菜单事件
	public boolean onOptionsItemSelected(MenuItem paramMenuItem)
    {
        OnFormAction(paramMenuItem.getItemId());
        return true;
    }
    
	//////////主体显示///////////////
    void displayForm()
    {
		//标题栏
        addTitleBar();
        //内容
		switch (function)
        {
            default:
                noImplementsForm();
                break;
		    case 1151://银行现金设置余额
                depositCashSetSumForm();
                break;
			case 1150://银行菜单开户
				addDepositForm();
				break;
			case 1152://修改开户
                depositModifyForm();
                break;
			case 1161://银行活期存取
			case 3050:
				currentSaveDrawForm();
				break;
			case 1162://银行活期转帐
			case 3051:
				currentVirementForm();
				break;
            case 1163://银行活期收费
            case 3301:
                currentBankFeeForm();
                break;
			case 1164://银行活期计息
			case 3052:
				currentInterestForm();
				break;
            case 1165://银行活期销户
            case 3053:
                currentCheckoutForm();
                break;
            case 1171://银行定期到期支取
                fixedDqzqForm();
                break;
            case 1172://银行定期提前支取
                fixedTqzqForm();
                break;
                //////////
		    case 1250://增加科目
                addKmForm();
                break;
            case 1252://修改科目
                modifyKmForm();
                break;

				////////
            case 1060://生活记帐
                addLiveForm();
                break;
            case 1062:
                modifyAuditForm();
                break;
			///////	
            case 1450://借贷
               addCreditForm();
               break;
			case 1452://借贷修改
				creditModifyForm();
				break;
            case 1461://还款
            case 3035:
                creditReturnForm();
                break;
			case 1462://续借
				borrowOrLendMoreForm();
				break;
			case 1460://记帐菜单借贷记帐
				addCreditAuditForSelectForm();
				break;
		    ////////
			case 1570://新增资产类型
				addAssetTypeForm();
				break;
			case 1572://修改资产类型
				modifyAssetTypeForm();
				break;	
			case 1550://新增资产
				addAssetForm();
				break;
			case 1552://修改资产
				modifyAssetForm();
                break;
			case 1554://隐藏资产
				hideAssetForm();
                break;
			case 1581://显示资产
				showAssetForm();
                break;    
			case 1560://记帐菜单资产记帐
				addAssetAuditForSelectForm();
				break;
			
			case 1562://修改流水
				modifyAssetAuditForm();
				break;	
				
			///////////////	
				
			case 1400://新增人情
				addFavorForm();
				break;
			case 1402://修改人情
				modifyFavorForm();
                break;
              //case 1013:
			case 1410://记帐菜单记帐
				addFavorAuditForSelectForm();
				break;
			case 1412://修改流水
				modifyFavorAuditForm();
				break;
			//////////
			//case 1035:
			case 1350://新增项目
			//case 1137:
				addEvectionForm();
				break;
			case 1352://修改项目
				modifyEvectionForm();
                break;
			//case 1013:
			case 1360://记帐菜单记帐
				addEvectionAuditForSelectForm();
				break;
			case 1361://菜单记帐
				addEvectionAuditForm();
				break;
			case 1362://修改流水
				modifyEvectionAuditForm();
				break;
		    case 1370://增加科目
                addProjectKmForm();
                break;
			case 1372://修改科目
				modifyProjectKmForm();
				break;
			case 1368://结束项目
                addEvectionFinishForm();
                break;
	        }
        //选择按键
		addButtonOkCancel();
    }
	//标题栏
	void addTitleBar()
    {
        this.titlebar = new TextView(this);
        this.titlebar.setBackgroundColor(Color.TRANSPARENT);//0
        this.titlebar.setTextSize(UIAdapter.getTextSize());
        this.titlebar.setGravity(Gravity.CENTER);//17
        LinearLayout localLinearLayout = new LinearLayout(this);
        this.layout.addView(localLinearLayout);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(WC, WC, 1.0F);
        localLinearLayout.addView(this.titlebar, localLayoutParams);
    }

    //选择键
    void addButtonOkCancel()
    {
        Button localButton1 = new Button(this);
        localButton1.setText(" 确 认  ");
        localButton1.setId(11);
        localButton1.setOnClickListener(this);
        Button localButton2 = new Button(this);
        localButton2.setText(" 返 回  ");
        localButton2.setId(12);
        localButton2.setOnClickListener(this);
        LinearLayout localLinearLayout = new LinearLayout(this);
        this.layout.addView(localLinearLayout);
        //确认布局设置
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(WC, WC, 1.0F);
        localLayoutParams.bottomMargin = 10;
        localLayoutParams.topMargin = 10;
        localLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        localLayoutParams.leftMargin = 5;
        localLayoutParams.rightMargin = 0;
        localLinearLayout.addView(localButton1, localLayoutParams);
        //返回布局设置
		localLayoutParams.leftMargin = 5;
        localLayoutParams.rightMargin = 5;
        localLinearLayout.addView(localButton2, localLayoutParams);
    }
	
	//-------内容--------
	void noImplementsForm()
    {
        setTitle("操作提示");
        addTextView("提示：  ", "该功能没有实现！");
    }
	
	void depositCashSetSumForm()
	{
		setTitle("设置现金余额");
		Deposit localDeposit = new Deposit(MainActivity.params[3]);
		addTextView("账户", localDeposit.name);
		addEditDecimal(1073745925, "余额", localDeposit.sum);
	}
	
	void addDepositForm()
	{
		setTitle("添加存款账户");
		addSpinner(1073745922, "类型", Deposit.getDepositTypeList());
		setSpinnerListener(1073745922, new AdapterView.OnItemSelectedListener()
			{
				public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
				{
					while (FormActivity.this.layout.getChildCount() > 3)
						FormActivity.this.layout.removeViewAt(3);
					switch (paramInt)
					{
						case 0://活期
							FormActivity.this.addDepositCurrentForm();
							break;

					    case 1://定期
							FormActivity.this.addSpinner(1073745931, "存期", Deposit.getFixedTypeList(FormActivity.this.km1list));
							FormActivity.this.setSpinnerListener(1073745931, new AdapterView.OnItemSelectedListener()
								{
									public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
									{
										((EditText)FormActivity.this.findViewById(1073745928)).setText(Convertor.sumToString(new InterestRate(((Integer)FormActivity.this.km1list.get(paramInt)).intValue()).rate));
									}

									public void onNothingSelected(AdapterView<?> paramAdapterView)
									{
									}
								});
							FormActivity.this.addEditDecimal(1073745925, "金额");
							FormActivity.this.addEditDecimal(1073745928, "利率", new InterestRate(2).rate);
							FormActivity.this.addSpinner(1073745923, "银行", Bank.getBankList("type=0", null, FormActivity.this.banklist));
							FormActivity.this.addSpinner(1073745924, "账户", FormActivity.this.getDepositList());
							FormActivity.this.addEditDate(1073745929, "开户日期");
							break;



					}
					FormActivity.this.addButtonOkCancel();
				}

				public void onNothingSelected(AdapterView<?> paramAdapterView)
				{
				}
			});
		addEditString(1073745921, "名称", "");
		addDepositCurrentForm();
	
	}
	
	void addDepositCurrentForm()
    {
        addSpinner(1073745931, "活期类型", Deposit.getCurrentTypeList(this.km1list));
        addEditDecimal(1073745925, "金额");
        addSpinner(1073745923, "银行", Bank.getBankList("type=0", null, this.banklist));
        addSpinner(1073745924, "银行账户", getDepositList());
        addEditText(1073745930, "卡/账号", "");
        addEditDate(1073745929, "开户日期");
    }
  void depositModifyForm()
  {
    boolean bool = true;
	int i = 1;
    Deposit localDeposit = new Deposit(MainActivity.params[3]);
    final boolean[] init_status = new boolean[1];
    init_status[0] = bool;
    addEditString(1073745921, "名称", localDeposit.name);
    switch (localDeposit.type)
    {
    case 1://活期
    case 25:
      addSpinner(1073745931, "账户类型", Deposit.getCurrentTypeList(this.km1list));
      if (localDeposit.type == 1)
        i = 0;
		bool =false;
      setSpinnerSelection(1073745931, i);
      addEditDecimal(1073745925, "余额", localDeposit.sum);
      addSpinner(1073745923, "银行", Bank.getBankList("type=0", null, this.banklist));
      setSpinnerSelection(1073745923, this.banklist, localDeposit.bankid);
      addEditDate(1073745929, "开户日期", localDeposit.real_date);
      break;
    case 2://定期
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
      addSpinner(1073745931, "存期", Deposit.getFixedTypeList(this.km1list));
      setSpinnerSelection(1073745931, this.km1list, localDeposit.type);
      setSpinnerListener(1073745931, new AdapterView.OnItemSelectedListener()
      {
        public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          if (init_status[0] == false)
            ((EditText)FormActivity.this.findViewById(1073745928)).setText(Convertor.sumToString(new InterestRate(((Integer)FormActivity.this.km1list.get(paramInt)).intValue()).rate));
          else
            init_status[0] = false;
        }

        public void onNothingSelected(AdapterView<?> paramAdapterView)
        {
        }
      });
      addEditDecimal(1073745925, "余额", localDeposit.sum);
      addEditDecimal(1073745928, "利率", localDeposit.rate);
      if (!localDeposit.isChildDeposit())
      {
        addSpinner(1073745923, "银行", Bank.getBankList("type=0", null, this.banklist));
        setSpinnerSelection(1073745923, this.banklist, localDeposit.bankid);
      }
      addEditDate(1073745929, "开户日期", localDeposit.real_date);
      break;



    }
  }

  void currentSaveDrawForm()
  {
    setTitle("存钱取钱");
    int i;
    if (function != 1161)
      i = ((int[])MainActivity.paramstack.get(0))[3];
    else
      i = MainActivity.params[3];
    addTextView("账户", new Deposit(i).name);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("取钱");
    localArrayList.add("存钱");
    addSpinner(1073745922, "业务", localArrayList);
    addEditDecimal(1073745925, "金额");
    addEditDecimal(1073745944, "收费");
    addEditDate(1073745929, "日期");
  }

  void currentVirementForm()
  {
    setTitle("银行转账");
    int i;
    if (function != 1162)
      i = ((int[])MainActivity.paramstack.get(0))[3];
    else
      i = MainActivity.params[3];
    addTextView("账户", new Deposit(i).name);
    addEditDecimal(1073745925, "金额");
    addSpinner(1073745924, "对方账户", getDepositList());
    addEditDecimal(1073745944, "收费");
    addEditDate(1073745929, "日期");
  }


  void currentBankFeeForm()
  {
    setTitle("银行收费");
    int i;
    if (function != 1163)
      i = ((int[])MainActivity.paramstack.get(0))[3];
    else
      i = MainActivity.params[3];
    addTextView("账户", new Deposit(i).name);
    addEditDecimal(1073745925, "金额");
    addEditString(1073745927, "备注");
    addEditDate(1073745929, "日期");
  }
  


	void currentInterestForm()
	{
		setTitle("活期计息");
		int i;
		if (function != 1164)
			i = ((int[])MainActivity.paramstack.get(0))[3];
		else
			i = MainActivity.params[3];
		addTextView("账户", new Deposit(i).name);
		addEditDecimal(1073745925, "利息");
		addEditDate(1073745929, "日期");
	}


	void currentCheckoutForm()
	{
		setTitle("活期销户");
		int i;
		if (function != 1165)
			i = ((int[])MainActivity.paramstack.get(0))[3];
		else
			i = MainActivity.params[3];
		Deposit localDeposit = new Deposit(i);
		addTextView("账户", localDeposit.name);
		addTextView("余额", Convertor.sumToString(localDeposit.sum));
		addEditDecimal(1073745925, "利息");
		addEditDate(1073745929, "日期");
	}


	void fixedDqzqForm()
	{
		setTitle("到期支取");
		Deposit localDeposit = new Deposit(MainActivity.params[3]);
		addTextView("账户", localDeposit.name);
		addTextView("金额", Convertor.sumToString(localDeposit.sum));
		addEditDecimal(1073745944, "利息", localDeposit.getInterest());
		addEditDate(1073745929, "日期");
	}
	


  void fixedTqzqForm()
  {
    setTitle("提前支取");
    Deposit localDeposit = new Deposit(MainActivity.params[3]);
    addTextView("账户", localDeposit.name);
    addTextView("金额", Convertor.sumToString(localDeposit.sum));
    addEditDecimal(1073745925, "支取金额");
    addEditDecimal(1073745944, "利息");
    addEditDate(1073745929, "日期");
  }
  /////////

    void addKmForm()
    {
        setTitle("添加科目");
        addSpinner(1073745936, "父科目", KM.getFatherKmListForAddKm(this.km1list));
        addEditString(1073745921, "科目名称", "");
    }

    void modifyKmForm()
    {
        setTitle("修改科目");
        addTextView("科目名称", new KM(MainActivity.params[3]).name);
        addEditString(1073745921, "新名称", "");
    }
	
  	void addLiveForm()
    {
        setTitle("生活记账");
	
        addSpinner(1073745935, "类型", KM.getKmList(0,this.typelist));
		setSpinnerListener(1073745935, new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
			{
	           FormActivity.this.setSpinnerList(1073745936, KM.getKmList(((Integer)FormActivity.this.typelist.get(paramInt)).intValue(), FormActivity.this.km1list), 0);

			}

			public void onNothingSelected(AdapterView<?> paramAdapterView)
			{
			}
		});
		
		addSpinner(1073745936, "科目", KM.getKmList(((Integer)this.typelist.get(0)).intValue(), this.km1list));
        setSpinnerListener(1073745936, new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
            {
                FormActivity.this.setSpinnerList(1073745937, KM.getKmList(((Integer)FormActivity.this.km1list.get(paramInt)).intValue(), FormActivity.this.km2list), 0);
            }

            public void onNothingSelected(AdapterView<?> paramAdapterView)
            {
            }
        });
    
    
	    addSpinner(1073745937, "", KM.getKmList(((Integer)this.km1list.get(0)).intValue(), this.km2list));
        addEditDecimal(1073745925, "金额");
        addSpinner(1073745924, "银行账户", getDepositList());
        addEditText(1073745927, "备注", "");
        addEditDate(1073745929, "日期");
    }
  	 void modifyAuditForm()
  	  {
  	    setTitle("修改收支流水");
  	    Audit localAudit = new Audit(MainActivity.params[3]);
  	    final boolean[] init_status = new boolean[1];
  	    init_status[0] = true;
  	    String str;
  	    KM localKM = new KM(localAudit.kmid);
  	    if (localAudit.kmid != 265)
  	    {
  	      if ((localAudit.kmid != 266) && (localAudit.kmid != 297))
  	      {
  	        if (localKM.keyflag != 1)
  	        {
  	          if (localKM.pid != 1)
  	          {
  	            addSpinner(1073745936, "科目", KM.getKmList(1, this.km1list));
  	            setSpinnerSelection(1073745936, this.km1list, localKM.pid);
  	            addSpinner(1073745937, "", KM.getKmList(localKM.pid, this.km2list));
  	            
  	            setSpinnerListener(1073745936, new AdapterView.OnItemSelectedListener()
  	            {
  	              public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  	              {
  	                if (init_status[0] == false)
  	                  FormActivity.this.setSpinnerList(1073745937, KM.getKmList(((Integer)FormActivity.this.km1list.get(paramInt)).intValue(), FormActivity.this.km2list), 0);
  	                else
  	                  init_status[0] = false;
  	              }

  	              public void onNothingSelected(AdapterView<?> paramAdapterView)
  	              {
  	              }
  	            });
  	            setSpinnerSelection(1073745937, this.km2list, localAudit.kmid);
  	          }
  	          else
  	          {
  	            addSpinner(1073745937, "科目", KM.getKmList(1, this.km2list));
  	            setSpinnerSelection(1073745937, this.km2list, localAudit.kmid);
  	          }
  	        }
  	        else
  	        {
  	          if (localKM.id < 275)
  	        	str  = new KM(localKM.pid).name + "->";
  	          else
  	            str = "";
  	          addTextView("科目", str + localKM.name);
  	        }
  	        
  	        addEditDecimal(1073745925, "金额", localAudit.sum);
  	        
  	        if (localKM.keyflag != 1)
  	        {
  	          addSpinner(1073745924, "银行账户", getDepositList());
  	          setSpinnerSelection(1073745924, this.depositlist, localAudit.deposit_id);
  	        }
  	        else
  	        {
  	          addTextView("银行账户", new Deposit(localAudit.deposit_id).name);
  	        }
  	        addEditText(1073745927, "备注", localAudit.content);
  	        addEditDate(1073745929, "日期", localAudit.real_date);
  	      }
  	      else
  	      {
  	        modifyFavorAuditForm(FavorAudit.getIdByAuditId(localAudit.id));
  	      }
  	    }
  	    else
  	      modifyEvectionAuditForm(EvectionAudit.getIdByAuditId(localAudit.id));
  	  }
	/////////////////////
	void addCreditForm()
	{
		setTitle("新增借贷账户");
		addSpinner(1073745922, "类型", Credit.getCreditTypeList());
		setSpinnerListener(1073745922, new AdapterView.OnItemSelectedListener()
			{
				public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
				{
					while (FormActivity.this.layout.getChildCount() > 3)
						FormActivity.this.layout.removeViewAt(3);
					switch (paramInt)
					{
						case 0:
						case 1:
							FormActivity.this.addBorrowOrLendForm();
							break;
				
					}
					FormActivity.this.addButtonOkCancel();
				}

				public void onNothingSelected(AdapterView<?> paramAdapterView)
				{
				}
			});
		addEditString(1073745921, "名称", "");
		addBorrowOrLendForm();

	}
	void creditModifyForm()
    {
    Credit localCredit = new Credit(MainActivity.params[3]);
    setTitle("修改借贷信息");
    addEditString(1073745921, "名称", localCredit.name);
    switch (localCredit.type)
    {
        case 0:
        case 1:
      addEditDecimal(1073745925, "金额", localCredit.sum);
      addEditText(1073745927, "备注", localCredit.content);
      addEditDate(1073745929, "日期", localCredit.real_date);
      break;

    }
  }

	void addBorrowOrLendForm()
    {
        addEditDecimal(1073745925, "金额");
        addSpinner(1073745924, "银行账户", getDepositList());
        addEditText(1073745927, "备注", "");
        addEditDate(1073745929, "日期");
    }
	void creditReturnForm()
	{
		Credit localCredit = new Credit(MainActivity.params[3]);
		if (localCredit.type != 1)
		{
			if (localCredit.type == 0)
	//			setTitle("还贷款");
	//		else
				setTitle("收回借款");
		}
		else
			setTitle("偿还借款");
		addTextView("账户", localCredit.name);
	//	if ((localCredit.type != 1) && (localCredit.type != 0) && (localCredit.type != 2))
		{
	//		addEditDecimal(1073745925, "金额", localCredit.monthPayCostOfCreditAj());
	//		addEditDecimal(1073745944, "利息", localCredit.monthPayInterestOfCreditAj());
		}
	//	else
		{
			addEditDecimal(1073745925, "金额", localCredit.sum);
			addEditDecimal(1073745944, "利息");
		}
		addSpinner(1073745924, "还款账户", getDepositList());
		setSpinnerSelection(1073745924, this.depositlist, localCredit.deposit_id);
		addEditDate(1073745929, "日期");
		addEditText(1073745927, "备注");
	}
	void borrowOrLendMoreForm()
	{
		Credit localCredit = new Credit(MainActivity.params[3]);
		setTitle("续借");
		addTextView("账户", localCredit.name);
		addEditDecimal(1073745925, "金额");
		addSpinner(1073745924, "银行账户", getDepositList());
		addEditDate(1073745929, "日期");
		addEditText(1073745927, "备注");
	}
	
	void addCreditAuditForSelectForm()
	{
		setTitle("借贷记帐");
		
		addSpinner(1073745921, "名称", Credit.getCreditList("flag=0 ", this.banklist));
		final Credit credit = new Credit(((Integer)this.banklist.get(getSpinnerValue(1073745921))).intValue());
		addSpinner(1073745936, "科目", Credit.getCreditAuditKmList());
		setSpinnerListener(1073745936, new AdapterView.OnItemSelectedListener()
			{
				public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
				{
					while (FormActivity.this.layout.getChildCount() > 3)
						FormActivity.this.layout.removeViewAt(3);
				    if (paramInt==0)
					{
						FormActivity.this.addEditDecimal(1073745925, "金额", credit.sum);
						FormActivity.this.addEditDecimal(1073745944, "利息");
						FormActivity.this.addSpinner(1073745924, "银行账户", getDepositList());
						FormActivity.this.addEditText(1073745927, "备注", "");
						FormActivity.this.addEditDate(1073745929, "日期");
					}
					else
		            {
					    FormActivity.this.addEditDecimal(1073745925, "金额");
					    FormActivity.this.addSpinner(1073745924, "银行账户", getDepositList());
					    FormActivity.this.addEditText(1073745927, "备注", "");
					    FormActivity.this.addEditDate(1073745929, "日期");
					}
					FormActivity.this.addButtonOkCancel();
				}

				public void onNothingSelected(AdapterView<?> paramAdapterView)
				{
				}
			});

	}
    ////////
	void addAssetForm()
	{
		setTitle("新增资产");
		
		addSpinner(1073745922, "类型", AssetKm.getAssetTypeList(0,this.typelist));
		addEditString(1073745949, "名称");
		addEditDecimal(1073745925, "单价");
		addEditDate(1073745929, "日期");
		addEditText(1073745927, "备注");
	}
	void modifyAssetForm()
	{
		int i;
		if (MainActivity.paramstack.size() <= 0)
			i = MainActivity.params[3];
		else
			i = ((int[])MainActivity.paramstack.get(0))[3];
		Asset asset = new Asset(i);
		setTitle("修改资产信息");
		addSpinner(1073745922, "类型", AssetKm.getAssetTypeList(0,this.typelist));
		addEditString(1073745949, "名称", asset.city);
		addEditDecimalf(1073745925, "单价",asset.bulky);
		addEditDate(1073745929, "日期", asset.real_date);
		addEditText(1073745927, "备注", asset.content);
	}
	void hideAssetForm()
	{
		
		setTitle("修改资产信息");
		addTextView("提示：  ", "请从<菜单>隐藏管理恢复显示！");
	}
	void showAssetForm()
	{
		
		setTitle("恢复显示");
		addSpinner(1073745922, "恢复到", AssetKm.getAssetTypeList(0,this.typelist));
		
	}
	void addAssetTypeForm()
	{
		setTitle("新增类别");
		addEditString(1073745921, "类别名称", "");
		
	}
	void modifyAssetTypeForm()
	{
		int i = MainActivity.params[3];
		AssetKm asset = new AssetKm(i);
		setTitle("修改资产类别信息");
		addEditString(1073745949, "名称", asset.name);
		
	}
	
	void addAssetAuditForSelectForm()
	{
		setTitle("资产记帐");
		addSpinner(1073745922, "类别", AssetKm.getAssetTypeList(0,this.typelist));
		setSpinnerListener(1073745922, new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
			{
	           FormActivity.this.setSpinnerList(1073745921, Asset.getAssetList( ("type="+(Integer)FormActivity.this.typelist.get(paramInt))+" and flag=0", FormActivity.this.banklist), 0);

			}

			public void onNothingSelected(AdapterView<?> paramAdapterView)
			{
			}
		});
		////
		addSpinner(1073745921, "名称", Asset.getAssetList("type=1 and flag=0", this.banklist));
		setSpinnerListener(1073745921, new AdapterView.OnItemSelectedListener()
		{
			
			public void onItemSelected(AdapterView<?> adapter, View view, int position, long id)
			{
				Asset asset = new Asset(((Integer)FormActivity.this.banklist.get(position)).intValue());
	            FormActivity.this.setSpinnerList(1073745936, asset.getAssetAuditTypeList(), 0);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
		
		Asset asset = new Asset(((Integer)this.banklist.get(0)).intValue());
		addSpinner(1073745936, "类型", asset.getAssetAuditTypeList());
	
		setSpinnerListener(1073745936, new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> adapter, View view, int position, long id)
			{
				FormActivity.this.setSpinnerList(1073745937, new Asset(((Integer)FormActivity.this.banklist.get(FormActivity.this.getSpinnerValue(1073745921))).intValue()).getAssetKmList(position, FormActivity.this.km2list), 0);

			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
        
		addSpinner(1073745937, "", asset.getAssetKmList(0, this.km2list));
		addEditDecimal(1073745925, "金额");
//		addSpinner(1073745924, "银行账户", getDepositList());
		addEditText(1073745927, "备注", "");
		addEditDate(1073745929, "日期");
	}

	void modifyAssetAuditForm()
	{
		modifyAssetAuditForm(MainActivity.params[3]);
	}

	void modifyAssetAuditForm(int paramInt)
	{
		AssetAudit localEvectionAudit = new AssetAudit(paramInt);
		Asset localEvection = new Asset(localEvectionAudit.eid);
		setTitle("修改流水");
		addTextView("名称", localEvection.city);
		if (localEvectionAudit.kmid <= 4)
		{
			addTextView("科目", localEvection.getKmName(localEvectionAudit.kmid));
		}
		else
		{
			int i;
			if (!localEvectionAudit.isIncome())
				i = 1;
			else
				i = 0;
			addSpinner(1073745936, "科目", localEvection.getAssetKmList(i, this.km1list));
			setSpinnerSelection(1073745936, this.km1list, localEvectionAudit.kmid);
		}
	    addEditDecimalf(1073745925, "数量", localEvectionAudit.nub);
		addEditText(1073745927, "备注", localEvectionAudit.content);
		addEditDate(1073745929, "日期", localEvectionAudit.real_date);
	}
	////////////////
	void addFavorForm()
	{
		setTitle("新增好友");
		addEditString(1073745949, "名称");
		addEditDate(1073745929, "日期");
		addEditText(1073745927, "备注");
	}
	void modifyFavorForm()
	{
		int i;
		if (MainActivity.paramstack.size() <= 0)
			i = MainActivity.params[3];
		else
			i = ((int[])MainActivity.paramstack.get(0))[3];
		Favor favor = new Favor(i);
		setTitle("修改人情信息");
		addEditString(1073745949, "名称", favor.city);
		addEditDate(1073745929, "日期", favor.real_date);
		addEditText(1073745927, "备注", favor.content);
	}
	void addFavorAuditForSelectForm()
	{
		setTitle("人情记帐");
		
		addSpinner(1073745921, "名称", Favor.getEvectionList("flag=0 or flag=2", this.banklist));
		setSpinnerListener(1073745921, new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
			{
				Favor localEvection = new Favor(((Integer)FormActivity.this.banklist.get(paramInt)).intValue());
				//if (FormActivity.function == 1014)
					FormActivity.this.setSpinnerList(1073745936, localEvection.getEvectionAuditTypeList(), 0);
			}

			public void onNothingSelected(AdapterView<?> paramAdapterView)
			{
			}
		});
		
		Favor localObject = new Favor(((Integer)this.banklist.get(0)).intValue());
		addSpinner(1073745936, "类型", localObject.getEvectionAuditTypeList());
	
		setSpinnerListener(1073745936, new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
			{
				FormActivity.this.setSpinnerList(1073745937, new Favor(((Integer)FormActivity.this.banklist.get(FormActivity.this.getSpinnerValue(1073745921))).intValue()).getEvectionKmList(paramInt, FormActivity.this.km2list), 0);

			}

			public void onNothingSelected(AdapterView<?> paramAdapterView)
			{
			}
		});
        
		addSpinner(1073745937, "", localObject.getEvectionKmList(0, this.km2list));
		addEditDecimal(1073745925, "金额");
		addSpinner(1073745924, "银行账户", getDepositList());
		addEditText(1073745927, "备注", "");
		addEditDate(1073745929, "日期");
	}
	void modifyFavorAuditForm()
	{
		modifyFavorAuditForm(MainActivity.params[3]);
	}

	void modifyFavorAuditForm(int paramInt)
	{
		FavorAudit localEvectionAudit = new FavorAudit(paramInt);
		Favor localEvection = new Favor(localEvectionAudit.eid);
		setTitle("修改流水");
		addTextView("名称", localEvection.city);
		if (localEvectionAudit.kmid <= 4)
		{
			addTextView("科目", localEvection.getKmName(localEvectionAudit.kmid));
		}
		else
		{
			int i;
			if (!localEvectionAudit.isIncome())
				i = 0;
			else
				i = 1;
			addSpinner(1073745936, "科目", localEvection.getEvectionKmList(i, this.km1list));
			setSpinnerSelection(1073745936, this.km1list, localEvectionAudit.kmid);
		}
		addEditDecimal(1073745925, "金额", localEvectionAudit.sum);
		addSpinner(1073745924, "银行账户", getDepositList());
		setSpinnerSelection(1073745924, this.depositlist, localEvectionAudit.deposit_id);
		addEditText(1073745927, "备注", localEvectionAudit.content);
		addEditDate(1073745929, "日期", localEvectionAudit.real_date);
	}
	///////
	void addEvectionForm()
	{
		setTitle("新增项目");
		addEditString(1073745949, "名称");
		addEditDate(1073745929, "日期");
		addEditText(1073745927, "备注");
	}
	void modifyEvectionForm()
	{
		int i;
		if (MainActivity.paramstack.size() <= 0)
			i = MainActivity.params[3];
		else
			i = ((int[])MainActivity.paramstack.get(0))[3];
		Evection evection = new Evection(i);
		setTitle("修改项目信息");
		addEditString(1073745949, "名称", evection.city);
		addEditDate(1073745929, "日期", evection.real_date);
		addEditText(1073745927, "备注", evection.content);
	}

	void addEvectionAuditForSelectForm()
	{
		setTitle("项目记帐");
		
		addSpinner(1073745921, "名称", Evection.getEvectionList("flag=0 or flag=2", this.banklist));
		setSpinnerListener(1073745921, new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
			{
				Evection localEvection = new Evection(((Integer)FormActivity.this.banklist.get(paramInt)).intValue());
				//if (FormActivity.function == 1014)
					FormActivity.this.setSpinnerList(1073745936, localEvection.getEvectionAuditTypeList(), 0);
			}

			public void onNothingSelected(AdapterView<?> paramAdapterView)
			{
			}
		});
		
		Evection localObject = new Evection(((Integer)this.banklist.get(0)).intValue());
		addSpinner(1073745936, "类型", localObject.getEvectionAuditTypeList());
	
		setSpinnerListener(1073745936, new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
			{
				FormActivity.this.setSpinnerList(1073745937, new Evection(((Integer)FormActivity.this.banklist.get(FormActivity.this.getSpinnerValue(1073745921))).intValue()).getEvectionKmList(paramInt, FormActivity.this.km2list), 0);

			}

			public void onNothingSelected(AdapterView<?> paramAdapterView)
			{
			}
		});
        
		addSpinner(1073745937, "", localObject.getEvectionKmList(0, this.km2list));
		addEditDecimal(1073745925, "金额");
		addSpinner(1073745924, "银行账户", getDepositList());
		addEditText(1073745927, "备注", "");
		addEditDate(1073745929, "日期");
	}

	
	void addEvectionAuditForm()
	{
		final Evection evection = new Evection(((int[])MainActivity.paramstack.get(0))[3]);
		setTitle(evection.city + "记账");
		addSpinner(1073745936, "科目", evection.getEvectionAuditTypeList());
		setSpinnerListener(1073745936, new AdapterView.OnItemSelectedListener()
			{
				public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
				{
					FormActivity.this.setSpinnerList(1073745937, evection.getEvectionKmList(paramInt, FormActivity.this.km2list), 0);
				}

				public void onNothingSelected(AdapterView<?> paramAdapterView)
				{
				}
			});
		addSpinner(1073745937, "", evection.getEvectionKmList(0, this.km2list));
		addEditDecimal(1073745925, "金额");
		addSpinner(1073745924, "银行账户", getDepositList());
		addEditText(1073745927, "备注", "");
		addEditDate(1073745929, "日期");
	}
	void modifyEvectionAuditForm()
	{
		modifyEvectionAuditForm(MainActivity.params[3]);
	}

	void modifyEvectionAuditForm(int paramInt)
	{
		EvectionAudit localEvectionAudit = new EvectionAudit(paramInt);
		Evection localEvection = new Evection(localEvectionAudit.eid);
		setTitle("修改流水");
		addTextView("名称", localEvection.city);
		if (localEvectionAudit.kmid <= 4)
		{
			addTextView("科目", localEvection.getKmName(localEvectionAudit.kmid));
		}
		else
		{
			int i;
			if (!localEvectionAudit.isIncome())
				i = 0;
			else
				i = 1;
			addSpinner(1073745936, "科目", localEvection.getEvectionKmList(i, this.km1list));
			setSpinnerSelection(1073745936, this.km1list, localEvectionAudit.kmid);
		}
		addEditDecimal(1073745925, "金额", localEvectionAudit.sum);
		addSpinner(1073745924, "银行账户", getDepositList());
		setSpinnerSelection(1073745924, this.depositlist, localEvectionAudit.deposit_id);
		addEditText(1073745927, "备注", localEvectionAudit.content);
		addEditDate(1073745929, "日期", localEvectionAudit.real_date);
	}
	
	void addProjectKmForm()
	{

		setTitle("增加项目科目");
		ArrayList localArrayList = new ArrayList();
		localArrayList.add("支出");
		localArrayList.add("收入");
		addSpinner(1073745922, "父科目", localArrayList);
		addEditString(1073745921, "名称");
	}
	
	
	void modifyProjectKmForm()
	{
		setTitle("修改项目科目");
		addTextView("科目名称", new Evection(((int[])MainActivity.paramstack.get(0))[3]).projectkm[(-5 + MainActivity.params[3])]);
		addEditString(1073745921, "新名称", "");
	}
	
	void addEvectionFinishForm()
	{
		Evection localEvection = new Evection(((int[])MainActivity.paramstack.get(0))[3]);
		String str;
        str = "结束项目";
		setTitle(str);
		str = "项目";
		addTextView(str, localEvection.city);
		addTextView("结余", Convertor.sumToString(localEvection.sum));
		addEditText(1073745927, "备注", "");
		addEditDate(1073745929, "日期");
	}

	
	



	/////////内容方法////////////////
	
	void setTitle(String paramString)
    {
        this.titlebar.setText(paramString);
    }
	
    //文本框
	void addTextView(String paramString1, String paramString2)
	{
		TableRow localTableRow = new TableRow(this);
		this.layout.addView(localTableRow, this.layout_param);
		localTableRow.addView(addEditLabel(paramString1), this.tr_label_layout_param);
		TextView localTextView = new TextView(this);
		localTextView.setText(paramString2);
		localTextView.setTextSize(9 * UIAdapter.getTextSize() / 10);
		localTableRow.addView(localTextView, this.tr_text_layout_param);
	}
	
    TextView addEditLabel(String paramString)
    {
        TextView localTextView = new TextView(this);
        localTextView.setText(paramString);
        localTextView.setGravity(5);
        localTextView.setTextSize(-2 + UIAdapter.getTextSize());
        return localTextView;
    }
	//文本输入框
	void addEditString(int paramInt, String paramString)
	{
		addEditString(paramInt, paramString, "");
	}

	void addEditString(int paramInt, String paramString1, String paramString2)
	{
		TableRow localTableRow = new TableRow(this);
		this.layout.addView(localTableRow, this.layout_param);
		localTableRow.addView(addEditLabel(paramInt, paramString1), this.tr_label_layout_param);
		EditText localEditText = new EditText(this);
		//文本输入类型
		localEditText.setInputType(InputType.TYPE_CLASS_TEXT);//1
		localEditText.setText(paramString2);
		localTableRow.addView(localEditText, this.tr_text_layout_param);
		localEditText.setId(paramInt);
	}
	TextView addEditLabel(int paramInt, String paramString)
    {
        TextView localTextView = new TextView(this);
        localTextView.setText(paramString);
        localTextView.setGravity(5);
        localTextView.setTextSize(-2 + UIAdapter.getTextSize());
        localTextView.setId(16781346 + paramInt);
        return localTextView;
    }
	//时间输入框
	void addEditDate(int paramInt, String paramString)
    {
        addEditDate(paramInt, paramString, new Date(), this.dateinput);
   	}

    void addEditDate(int paramInt, String paramString, Date paramDate)
    {
        addEditDate(paramInt, paramString, paramDate, this.dateinput);
    }

    void addEditDate(int paramInt, String paramString,final Date date_field, Date paramDate2)
    {
        TableRow localTableRow = new TableRow(this);
        this.layout.addView(localTableRow, this.layout_param);
        localTableRow.addView(addEditLabel(paramInt, paramString), this.tr_label_layout_param);
        paramDate2.setTime(date_field.getTime());
        paramDate2.setYear(date_field.getYear());
        paramDate2.setMonth(date_field.getMonth());
        paramDate2.setDate(date_field.getDate());
        paramDate2.setHours(date_field.getHours());
        paramDate2.setMinutes(date_field.getMinutes());
        paramDate2.setSeconds(date_field.getSeconds());
        final EditText et = new EditText(this);
		final DatePickerDialog.OnDateSetListener datesetlistener = new DatePickerDialog.OnDateSetListener()
		{
	        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
	        {
	            date_field.setYear(year - 1900);
	            date_field.setMonth(monthOfYear);
	            date_field.setDate(dayOfMonth);
	            et.setText(new MyDate(date_field).toString());
	        }
	    };
		View.OnClickListener onclick = new View.OnClickListener()
        {

            public void onClick(View paramView)
            {
                new DatePickerDialog(FormActivity.this, datesetlistener, 1900 + date_field.getYear(), date_field.getMonth(), date_field.getDate()).show();
            }
        };
        et.setFocusable(false);
        et.setOnClickListener(onclick);
        et.setText(new MyDate(date_field).toString());
        LinearLayout layout = new LinearLayout(this);
        layout.addView(et, new LinearLayout.LayoutParams(UIAdapter.getEditWidth(), -2));
        localTableRow.addView(layout, this.tr_text_layout_param);
        et.setId(paramInt);
    }
	
    //文本输入框
    void addEditText(int paramInt, String paramString)
	{
		addEditText(paramInt, paramString, "");
	}

	void addEditText(int paramInt, String paramString1, String paramString2)
	{
		TableRow localTableRow = new TableRow(this);
		this.layout.addView(localTableRow, this.layout_param);
		localTableRow.addView(addEditLabel(paramInt, paramString1), this.tr_label_layout_param);
		EditText localEditText = new EditText(this);
		localEditText.setInputType(InputType.TYPE_CLASS_TEXT);
		localEditText.setText(paramString2);
		localEditText.setSingleLine(false);
		localTableRow.addView(localEditText, this.tr_text_layout_param);
		localEditText.setId(paramInt);
	}
	//下拉列表
	Spinner addSpinner(int paramInt, String paramString, List<String> paramList)
	{
		TableRow localTableRow = new TableRow(this);
		this.layout.addView(localTableRow, this.layout_param);
		localTableRow.addView(addEditLabel(paramInt, paramString), this.tr_label_layout_param);
		Spinner localSpinner = new Spinner(this);
		if (paramList != null)
		{
			ArrayAdapter localArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, paramList);//17367048
			localArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//17367058
			localSpinner.setAdapter(localArrayAdapter);
		}
		localTableRow.addView(localSpinner, this.tr_text_layout_param);
		localSpinner.setId(paramInt);
		return localSpinner;
	}
	void setSpinnerList(int paramInt1, List<String> paramList, int paramInt2)
	{
		Spinner localSpinner = (Spinner)findViewById(paramInt1);
		if (paramList == null)
			paramList = new ArrayList();
		ArrayAdapter localArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, paramList);//17367048
		localArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		localSpinner.setAdapter(localArrayAdapter);
		if (localArrayAdapter.getCount() > 0)
			localSpinner.setSelection(paramInt2);
	}
	
	void setSpinnerListener(int paramInt, AdapterView.OnItemSelectedListener paramOnItemSelectedListener)
    {
        ((Spinner)findViewById(paramInt)).setOnItemSelectedListener(paramOnItemSelectedListener);
    }

	int getSpinnerValue(int paramInt)
	{
		return ((Spinner)findViewById(paramInt)).getSelectedItemPosition();
	}
	//数字输入框
	void addEditDecimal(int paramInt, String paramString)
	{
		addEditDecimal(paramInt, paramString, "");
	}
	void addEditDecimal4(int paramInt, String paramString)
	{
		addEditDecimal(paramInt, paramString, "");
	}
	
	void addEditDecimal(int paramInt, String paramString, long paramLong)
	{
		addEditDecimal(paramInt, paramString, Convertor.sumToString(paramLong));
	}
	
	
	/***********************
     * 添加数量输入框
     * 
     */
	void addEditDecimalf(int Eid, String paramString, float paramLong)
	{
		addEditDecimal(Eid, paramString, Convertor.sumToStringf(paramLong));
	}
    /***********************
     * 
     */
	void addEditDecimal4(int paramInt, String paramString, float paramLong)
	{
		addEditDecimal(paramInt, paramString, Convertor.sumToStringf(paramLong));
	}

	void addEditDecimal(int paramInt, String paramString1, String paramString2)
	{
		TableRow localTableRow = new TableRow(this);
		this.layout.addView(localTableRow, this.layout_param);
		localTableRow.addView(addEditLabel(paramInt, paramString1), this.tr_label_layout_param);
		final EditText et = new EditText(this);
	//	et.setInputType(8194);//8194
		et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS|InputType.TYPE_CLASS_NUMBER);//8194
		et.addTextChangedListener(new TextWatcher()
		{
			//edit  输入结束呈现在输入框中的信息
			public void afterTextChanged(Editable edit)
			{
			}
            //text  输入框中改变前的字符串信息
			//start 输入框中改变前的字符串的起始位置
			//count 输入框中改变前后的字符串改变数量一般为0
			//after 输入框中改变后的字符串与起始位置的偏移量
			public void beforeTextChanged(CharSequence text, int start, int count, int after)
			{
			}
            /*
			 *text  输入框中改变后的字符串信息
			 *start 输入框中改变后的字符串的起始位置
			 *before 输入框中改变前的字符串的位置 默认为0
			 *count 输入框中改变后的一共输入字符串的数量
			*/
			public void onTextChanged(CharSequence text, int start, int before, int count)
			{
				if (count == 1)
				{
					int i = text.charAt(start);
					if (i != 46)
					{
						if (i != 45)
						{
							if ((i < 48) || (i > 57))
							{
								et.setText(text.subSequence(0, text.length()));
								et.setSelection(-1 + text.length(), -1 + text.length());
							}
						}
						else
						{
							et.setText(text.subSequence(0, -1 + text.length()));
							et.setSelection(-1 + text.length(), -1 + text.length());
						}
					}
					else if ((start == 0) || (text.charAt(start - 1) == '-') || (text.toString().indexOf('.') < start))
					{
						et.setText(text.subSequence(0, -1 + text.length()));
						et.setSelection(-1 + text.length(), -1 + text.length());
					}
				}
			}
		});
		et.setText(paramString2);
		localTableRow.addView(et, this.tr_text_layout_param);
		et.setId(paramInt);
	}
	
	List<String> getDepositList()
	{
		StringBuffer localStringBuffer = new StringBuffer();
		switch (function)
		{
			default:
				localStringBuffer.append("(type=").append(12).append(" or type=").append(1).append(" or type=").append(25).append(" or type=").append(24).append(" or type=").append(9).append(" or type=").append(10).append(" or type=").append(8);
				break;
			case 1129:
				localStringBuffer.append("(type=").append(12).append(" or type=").append(1).append(" or type=").append(25).append(" or type=").append(9).append(" or type=").append(10).append(" or type=").append(8);
				break;
			case 1130:
				localStringBuffer.append(" ( type=").append(24);
				break;
			case 3003:
			case 3010:
			case 3013:
			case 3025:
			case 3026:
				int i = MainActivity.params[3];
				if (MainActivity.paramstack.size() > 0)
					i = ((int[])MainActivity.paramstack.get(0))[3];
				localStringBuffer.append("((type=").append(12).append(" or type=").append(1).append(" or type=").append(25).append(" or type=").append(24).append(" or type=").append(9).append(" or type=").append(10).append(" or type=").append(8).append(") and id<>").append(i);
		}
		localStringBuffer.append(") and flag<>-1");
		return Deposit.getDepositList(localStringBuffer.toString(), " rank", this.depositlist);
	}
	
	void setSpinnerSelection(int paramInt1, List<Integer> paramList, int paramInt2)
	{
		Spinner localSpinner = (Spinner)findViewById(paramInt1);
		if (paramList != null)
		{
			for (int i = 0;i <= paramList.size() ; i++)
			{
				if (i >= paramList.size())
					return;
				if (paramInt2 == ((Integer)paramList.get(i)).intValue())
					break;
			    localSpinner.setSelection(i);
		    }
		}
		else
		{
			localSpinner.setSelection(paramInt2);
		}
	}
	void setSpinnerSelection(int paramInt1, int paramInt2)
    {
        ((Spinner)findViewById(paramInt1)).setSelection(paramInt2);
    }
/*	void addEditInteger(int paramInt, String paramString)
	{
		TableRow localTableRow = new TableRow(this);
		this.layout.addView(localTableRow, this.layout_param);
		localTableRow.addView(addEditLabel(paramInt, paramString), this.tr_label_layout_param);
		EditText localEditText = new EditText(this);
		localEditText.setInputType(3);
		localTableRow.addView(localEditText, this.tr_text_layout_param);
		localEditText.setId(paramInt);
	}*/
    /////////////////////////	
  	public void onClick(View paramView)
	{
		OnFormAction(paramView.getId());
	}
	void OnFormAction(int paramInt)
	{
		switch (paramInt)
		{
			case 11:
				Date localDate = new Date();
				this.dateinput.setHours(localDate.getHours());
				this.dateinput.setMinutes(localDate.getMinutes());
				this.dateinput.setSeconds(localDate.getSeconds());
				this.dateinput1.setHours(localDate.getHours());
				this.dateinput1.setMinutes(localDate.getMinutes());
				this.dateinput1.setSeconds(localDate.getSeconds());
				formAction();
				break;
			case 12:
				if (function >= 2000)
					returnToDetail();
				else
					returnToView();
				break;
			case 18:
		//		saveAuditPosAction();
				break;
			case 19:
		//		cancelAuditPosAction();
				break;
			case 20:
		//		backToAuditPosAction();
                break;
		}
	}
	
	void formAction()
	{
		switch (function)
		{
			case 1151://银行现金设置余额
                depositCashSetSumAction();
                break;
			case 1150://银行菜单开户
				addDepositAction();
				break;
			case 1152://修改开户
                depositModifyAction();
                break;
			case 1161://银行帐户存取
			case 3050:
				currentSaveDrawAction();
				break;
			case 1162://银行活期转帐
			case 3051:
				currentVirementAction();
				break;
			case 1163://银行活期收费
            case 3301:
                currentBankFeeAction();
                break;
			case 1164://银行活期计息
			case 3052:
				currentInterestAction();
				break;	
			case 1165://银行活期销户
            case 3053:
                currentCheckoutAction();
                break;
		    case 3017://银行定期到期支取
                fixedDqzqAction();
                break;	
			case 3018://银行定期提前支取
                fixedTqzqAction();
                break;
				///////
			case 1250://增加科目
                addKmAction();
                break;
            case 1252://修改科目
                modifyKmAction();
                break;

			////////
		    case 1060://生活记帐
               addliveAction();
               break;
		    case 1062:
                modifyAuditAction(); 
                break;  
			////////
		    case 1450://借贷
				addCreditAction();
				break;
			case 1452://借贷修改
				creditModifyAction();
				break;
			case 1461://还款
            case 3035:
                creditReturnAction();
                break;
			case 1462://续借
				borrowOrLendMoreAction();
				break;
			case 1460://记帐菜单借贷记帐
				addCreditAuditForSelectAction();
				break;
			//////
			case 1570://新增资产类别
				addAssetTypeAction();   
				break;
			case 1572://修改资产类别
				modifyAssetTypeAction();   
				break;
			case 1550://新增资产
				addAssetAction();
				break;
			case 1552://修改资产
				modifyAssetAction();
                break;
			case 1554://隐藏资产
				hideAssetAction();
                break; 
			case 1581://显示资产
				showAssetAction();
                break;
   			case 1560://记帐菜单资产记帐
				addAssetAuditForSelectAction();
				break;	
			
			case 1562://修改资产流水
				modifyAssetAuditAction();
				break;	
			//////////
				
			case 1400://新增人情
				addFavorAction();
				break;
			case 1402://修改人情
				modifyFavorAction();
                break;
             // case 1013:
            case 1410://记帐菜单记帐
                addFavorAuditForSelectAction();
                break;
            case 1412://修改流水
				modifyFavorAuditAction();
				break;
				////////
			//case 1035:
			case 1350://新增项目
			//case 1137:
				addEvectionAction();
				break;
			case 1352://修改项目
				modifyEvectionAction();
                break;		
           // case 1013:
            case 1360://记帐菜单记帐
                addEvectionAuditForSelectAction();
                break;
			case 1361://菜单记帐
				addEvectionAuditAction();
				break;	
			case 1362://修改流水
				modifyEvectionAuditAction();
				break;
			case 1370://增加科目
                addProjectKmAction();
                break;
			case 1372://修改科目
				modifyProjectKmAction();
				break;
			case 1368://结束项目
                addEvectionFinishAction();
                break;
		}
	}
	
	void depositCashSetSumAction()
	{
		returnToDetail(new Deposit(MainActivity.params[3]).cashSetSum(getEditDecimal(1073745925)));
	}
	
    void addDepositAction()
    {
        int i = getSpinnerValue(1073745922);
        Object localObject = getEditString(1073745921);
        String str1 = null;
        long l1;
        int k;
        int n;
        int m;
        switch (i)
        {
            case 0://活期
                l1 = getEditDecimal(1073745925);
                k = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
                n = ((Integer)this.km1list.get(getSpinnerValue(1073745931))).intValue();
                String str2 = getEditString(1073745930);
                str1 = Deposit.insertCurrent(n, l1, ((Integer)this.banklist.get(getSpinnerValue(1073745923))).intValue(), (String)localObject, str2, this.dateinput, k);
                break;

         // case 2:
	        case 1://定期
                n = ((Integer)this.km1list.get(getSpinnerValue(1073745931))).intValue();
                l1 = getEditDecimal(1073745925);
                k = (int)getEditDecimal(1073745928);
                m = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
                str1 = Deposit.insertFixed(n, l1, k, ((Integer)this.banklist.get(getSpinnerValue(1073745923))).intValue(), m, (String)localObject, this.dateinput);
                break;



        }
        returnToView(str1);
    }

    void depositModifyAction()
    {
        Deposit localDeposit = new Deposit(MainActivity.params[3]);
    
	    String str1 = getEditString(1073745921);
        long l1 = getEditDecimal(1073745925);
        String str2 = null;
        int k;
        int m;
        String str4;
        String str5;
        switch (localDeposit.type)
        {
            case 1://活折
            case 25://活卡
                str2 = localDeposit.modifyCurrent(getSpinnerValue(1073745931), l1, ((Integer)this.banklist.get(getSpinnerValue(1073745923))).intValue(), str1, this.dateinput);
                break;
            case 2://定期
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                k = ((Integer)this.km1list.get(getSpinnerValue(1073745931))).intValue();
                int i = (int)getEditDecimal(1073745928);
                int j = localDeposit.bankid;
                if (!localDeposit.isChildDeposit())
                j = ((Integer)this.banklist.get(getSpinnerValue(1073745923))).intValue();
                Date localDate1 = this.dateinput;
                str2 = localDeposit.modifyFixed(k, l1, i, j, str1, localDate1);
                break;


        }
        returnToDetail(str2);
    }
    void currentSaveDrawAction()
  {
    int i;
    if (function != 1161)
      i = ((int[])MainActivity.paramstack.get(0))[3];
    else
      i = MainActivity.params[3];
    Deposit localDeposit = new Deposit(i);
    int j = getSpinnerValue(1073745922);
    long l1 = getEditDecimal(1073745925);
    long l2 = getEditDecimal(1073745944);
    String str = null;
    //判断业务
	if (j != 0)
    {
      if (j == 1)
        str = localDeposit.currentSave(l1, l2, this.dateinput);
    }
    else
      str = localDeposit.currentDraw(l1, l2, this.dateinput);
    if (function != 1161)
      returnToView(str);
    else
      returnToDetail(str);
  }
  void currentVirementAction()
  {
	  int i;
    if (function != 1162)
      i = ((int[])MainActivity.paramstack.get(0))[3];
    else
      i = MainActivity.params[3];
    Deposit localDeposit = new Deposit(i);
    int j = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
    String str = localDeposit.currentVirement(getEditDecimal(1073745925), getEditDecimal(1073745944), j, this.dateinput);
    if (function != 1162)
      returnToView(str);
    else
      returnToDetail(str);
  }

   void currentBankFeeAction()
  {
    int i;
    if (function != 1163)
      i = ((int[])MainActivity.paramstack.get(0))[3];
    else
      i = MainActivity.params[3];
    Deposit localDeposit = new Deposit(i);
    long l = getEditDecimal(1073745925);
    String str2 = getEditString(1073745927);
    String str1 = localDeposit.currentBankFee(l, this.dateinput, str2);
    if (function != 1163)
      returnToView(str1);
    else
      returnToDetail(str1);
  }
  
    void currentInterestAction()
	{
	    int i;
		if (function != 1164)
			i = ((int[])MainActivity.paramstack.get(0))[3];
		else
			i = MainActivity.params[3];
		String str = new Deposit(i).currentInterest(getEditDecimal(1073745925), this.dateinput);
		if (function != 1164)
			returnToView(str);
		else
			returnToDetail(str);
	}
	
	void currentCheckoutAction()
	{
		int i;
		if (function != 1165)
			i = ((int[])MainActivity.paramstack.get(0))[3];
		else
			i = MainActivity.params[3];
		String str = new Deposit(i).currentCheckout(getEditDecimal(1073745925), this.dateinput);
		if ((str == "操作成功完成！") && (function == 1165))
			MainActivity.popParams();
		returnToView(str);
	}
	
	void fixedDqzqAction()
	{
		returnToView(new Deposit(MainActivity.params[3]).fixedDqzq((int)getEditDecimal(1073745944), this.dateinput));
	}
	void fixedTqzqAction()
  {
    Deposit localDeposit = new Deposit(MainActivity.params[3]);
    String str = localDeposit.fixedTqzq(getEditDecimal(1073745925), (int)getEditDecimal(1073745944), this.dateinput);
    if (localDeposit.flag != -1)
      returnToDetail(str);
    else
      returnToView(str);
  }
  ///////
    void addKmAction()
    {
        returnToView(KM.addKm(getEditString(1073745921), ((Integer)this.km1list.get(getSpinnerValue(1073745936))).intValue()));
    }
    void modifyKmAction()
    {
        returnToView(new KM(MainActivity.params[3]).modify(getEditString(1073745921)));
    }

 
	void addliveAction()
	{
		//科目
		int kmid = ((Integer)this.km2list.get(getSpinnerValue(1073745937))).intValue();
		//金额
		long sum = getEditDecimal(1073745925);
		//银行
		final int deposit_id = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
		//备注
		String content = getEditString(1073745927);
		//记录
		String str2 = Audit.insert(kmid, sum, deposit_id, this.dateinput, content);
		if (str2 != "操作成功完成！")
		{
			returnToView(str2);
		}
		else
		{
			String str1 = "";
	//		str2 = Plan.getPlanAlert(j, this.dateinput);
			
	//		if (str2.length() <= 0)
	//			str1 = Budget.getBudgetAlert(j, this.dateinput);
	//		else
	//			str1 = str2 + "\n";
	//		if (str1.length() > 0)
	//			str1 = str1 + "\n";
			String str5 = str1 + "记账成功,是否继续?";
			new AlertDialog.Builder(this).setTitle("提示信息 ").setMessage(str5).setPositiveButton("继续记账", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramDialogInterface, int paramInt)
					{
						FormActivity.this.setEditDecimal(1073745925, 0L);
						FormActivity.this.setEditString(1073745927, "");
						Deposit localDeposit = new Deposit(deposit_id);
						Spinner localSpinner = (Spinner)FormActivity.this.findViewById(1073745924);
						ArrayAdapter localArrayAdapter = (ArrayAdapter)localSpinner.getAdapter();
						localArrayAdapter.remove(localSpinner.getSelectedItem().toString());
						localArrayAdapter.insert(localDeposit.name + "  " + Convertor.sumToString(localDeposit.sum), localSpinner.getSelectedItemPosition());
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramDialogInterface, int paramInt)
					{
						FormActivity.this.returnToView();
					}
				}).show();
			RuntimeInfo.main.refresh();
		}
	}
	
	void modifyAuditAction()
	  {
	    Audit localAudit = new Audit(MainActivity.params[3]);
	    int i = localAudit.kmid;
	    Object localObject = new KM(localAudit.kmid);
	    if (((KM)localObject).keyflag != 1)
	      i = ((Integer)this.km2list.get(getSpinnerValue(1073745937))).intValue();
	    long l = getEditDecimal(1073745925);
	    int j = localAudit.deposit_id;
	    if (((KM)localObject).keyflag != 1)
	      j = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
	    localObject = getEditString(1073745927);
	    returnToDetail(localAudit.modify(i, l, j, this.dateinput, (String)localObject));
	  }

	/////////
	
  void addCreditAction()
  {
    String str3 = null;
    int i = getSpinnerValue(1073745922);
    long l = getEditDecimal(1073745925);
    String str1 = getEditString(1073745921);
    String str2 = getEditString(1073745927);
    int j = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
    switch (i)
    {
    case 0:
      str3 = Credit.insertLend(l, j, str1, str2, this.dateinput);
      break;
    case 1:
      str3 = Credit.insertBorrow(l, j, str1, str2, this.dateinput);
      break;

	}
    returnToView(str3);
  }
    void creditModifyAction()
	{
		Credit localCredit = new Credit(MainActivity.params[3]);
		String str1 = getEditString(1073745921);
		long l1 = getEditDecimal(1073745925);
		String str2 = null;
		String str3;
		switch (localCredit.type)
		{
			case 0:
			case 1:
				str2 = localCredit.modifyBorrowOrLend(l1, str1, getEditString(1073745927), this.dateinput);
				break;

		}
		returnToDetail(str2);
	}
	void creditReturnAction()
	{
		long l1 = getEditDecimal(1073745925);
		long l2 = getEditDecimal(1073745944);
		int i = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
		String str2 = getEditString(1073745927);
		Credit localCredit = new Credit(MainActivity.params[3]);
//		String str1 = localCredit.name;
		String str3 =null;
		if (localCredit.type != 1)
		{
			if (localCredit.type == 0)
	//			str1 = localCredit.creditReturn(l1, l2, i, this.dateinput, str2);
	//		else
				str3 = localCredit.lendReturn(l1, l2, i, this.dateinput, str2);
		}
		else
			str3 = localCredit.borrowReturn(l1, l2, i, this.dateinput, str2);
		
		if (localCredit.flag != -1)
			returnToDetail(str3);
		else
			returnToView(str3);
	}
	void borrowOrLendMoreAction()
	{
		long l = getEditDecimal(1073745925);
		int i = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
		String str2 = getEditString(1073745927);
		String str1 = null;
		Credit localCredit = new Credit(MainActivity.params[3]);
		if (localCredit.type != 1)
		{
			if (localCredit.type == 0)
				str1 = localCredit.lendMore(l, i, this.dateinput, str2);
		}
		else
			str1 = localCredit.borrowMore(l, i, this.dateinput, str2);
		returnToDetail(str1);
	}

	void addCreditAuditForSelectAction()
	{
		Credit credit = new Credit(((Integer)this.banklist.get(getSpinnerValue(1073745921))).intValue());
	    final int i1 = getSpinnerValue(1073745936);
		long l1;
		long l2 ;
	    final int i ;
		String str2;
		String str3 =null;
		switch(credit.type)
		{
			default :
			    switch (i1)
				{
				    default :
						l1 = getEditDecimal(1073745925);//
						 i = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
						str2 = getEditString(1073745927);
						str3 = credit.borrowMore(l1, i, this.dateinput, str2);
					    break;
					case 0:
						l1 = getEditDecimal(1073745925);//
						l2 = getEditDecimal(1073745944);
						i = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
						str2 = getEditString(1073745927);
					  	str3 = credit.borrowReturn(l1, l2, i, this.dateinput, str2);
					    break;
				}
			    break;
			
			case 0:
			    switch (i1)
				{
				    default :
						 l1 = getEditDecimal(1073745925);//
						 i = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
						str2 = getEditString(1073745927);
						str3 = credit.lendMore(l1, i, this.dateinput, str2);
					    break;
					case 0:
						l1 = getEditDecimal(1073745925);//
						l2 = getEditDecimal(1073745944);
						i = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
						str2 = getEditString(1073745927);
						str3 = credit.lendReturn(l1, l2, i, this.dateinput, str2);
					    break;
				}
			    break;
			
		}
		if (str3 != "操作成功完成！")
        {
            returnToView(str3);
        }
        else
        {
            new AlertDialog.Builder(this).setTitle("提示信息 ").setMessage("记账成功,是否继续?").setPositiveButton("继续记账", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
						Credit credit = new Credit(((Integer)FormActivity.this.banklist.get(getSpinnerValue(1073745921))).intValue());
						if (i1 ==0)
							FormActivity.this.setEditDecimal(1073745925, credit.sum);
					    else
					        FormActivity.this.setEditDecimal(1073745925, 0L);
						
						FormActivity.this.setEditString(1073745927, "");
						Deposit localDeposit = new Deposit(i);
						Spinner localSpinner = (Spinner)FormActivity.this.findViewById(1073745924);
						ArrayAdapter localArrayAdapter = (ArrayAdapter)localSpinner.getAdapter();
						localArrayAdapter.remove(localSpinner.getSelectedItem().toString());
						localArrayAdapter.insert(localDeposit.name + "  " + Convertor.sumToString(localDeposit.sum), localSpinner.getSelectedItemPosition());
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramDialogInterface, int paramInt)
					{
						FormActivity.this.returnToView();
					}
				}).show();
			RuntimeInfo.main.refresh();
		}
	}
	
	//////////
	void addAssetAction()
    {
		int type = ((Integer)this.typelist.get(getSpinnerValue(1073745922))).intValue();
		String city = getEditString(1073745949);
        float bulky = getEditDecimalf(1073745925);
		String content = getEditString(1073745927);
        int flag = 0;
        String note = Asset.insertAsset(type, flag, city, bulky, this.dateinput, content);
        returnToView(note);
    }
	
	void modifyAssetAction()
    {
		int id;
		if (MainActivity.paramstack.size() <= 0)
			id = MainActivity.params[3];
		else
			id = ((int[])MainActivity.paramstack.get(0))[3];
        Asset asset = new Asset(id);
        int type = ((Integer)this.typelist.get(getSpinnerValue(1073745922))).intValue();
        String city = getEditString(1073745949);
        int flag = asset.flag;
        float bulky = getEditDecimalf(1073745925);
	    String content = getEditString(1073745927);
        String note = asset.modify(type,city,flag, bulky, this.dateinput, content);
        if (MainActivity.paramstack.size() <= 0)
			returnToView(note);
        else
			returnToDetail(note);
    }
	void hideAssetAction()
    {
		int id;
		if (MainActivity.paramstack.size() <= 0)
			id = MainActivity.params[3];
		else
			id = ((int[])MainActivity.paramstack.get(0))[3];
        Asset asset = new Asset(id);
        int type = 0;
        String city = asset.city;
		int flag = asset.flag+1;
        float bulky = asset.bulky;
	    String content = asset.content;
        String note = asset.modify(type,city,flag, bulky, this.dateinput, content);
        if (MainActivity.paramstack.size() <= 0)
			returnToView(note);
        else
			returnToDetail(note);
    }
	
	void showAssetAction()
    {
		
        Asset asset = new Asset(MainActivity.params[3]);
        int type = ((Integer)this.typelist.get(getSpinnerValue(1073745922))).intValue();
        String city = asset.city;
        int flag = asset.flag-1;
        float bulky = asset.bulky;
	    String content = asset.content;
        String note = asset.modify(type,city,flag, bulky, this.dateinput, content);
   		returnToView(note);
 
    }
	void addAssetTypeAction()
	
    {
		String name = getEditString(1073745921);
		String note = AssetKm.addKm(name);
        returnToView(note);
    }
	
	void modifyAssetTypeAction()
    {
		int id = MainActivity.params[3];
        AssetKm asset = new AssetKm(id);
        String name = getEditString(1073745949);
        String note = asset.modify(name);
  		returnToView(note);
       
    }
	
	void addAssetAuditForSelectAction()
    {
        Asset asset = new Asset(((Integer)this.banklist.get(getSpinnerValue(1073745921))).intValue());
        int kmid = ((Integer)this.km2list.get(getSpinnerValue(1073745937))).intValue();
        float nub = getEditDecimalf(1073745925);
        float sum = (float)(nub*asset.bulky);
		final int depositid = 0; 
		//((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
        String content = getEditString(1073745927);
        String note = asset.insertAudit(kmid, nub,sum, depositid, content, this.dateinput);

		if (note != "操作成功完成！")
        {
            returnToView(note);
        }
        else
        {
            new AlertDialog.Builder(this).setTitle("提示信息 ").setMessage("记账成功,是否继续?").setPositiveButton("继续记账", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
						FormActivity.this.setEditDecimal(1073745925, 0L);
						FormActivity.this.setEditString(1073745927, "");
					
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramDialogInterface, int paramInt)
					{
						FormActivity.this.returnToView();
					}
				}).show();
			RuntimeInfo.main.refresh();
		}
    }
	
	
	
	void modifyAssetAuditAction()
	{
		int i = MainActivity.params[3];	
		AssetAudit localEvectionAudit = new AssetAudit(i);
		Asset asset = new Asset(localEvectionAudit.eid);
		int kmid = localEvectionAudit.kmid;
		if (localEvectionAudit.kmid > 4)
			kmid = ((Integer)this.km1list.get(getSpinnerValue(1073745936))).intValue();
		float nub = getEditDecimalf(1073745925);
		float sum = (float)(nub*asset.bulky);
				int depositid = 0;
		//((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
		String content = getEditString(1073745927);
		returnToDetail(localEvectionAudit.modify(kmid,nub,sum, depositid, content, this.dateinput));
	}
	
	
    //////////////
	void addFavorAction()
    {
        String name = getEditString(1073745949);
        String content = getEditString(1073745927);
        int flag = 2;
       //     i = 2;
        returnToView(Favor.insertEvection(flag, name, this.dateinput, content));
    }
	void modifyFavorAction()
    {
		int i;
		if (MainActivity.paramstack.size() <= 0)
			i = MainActivity.params[3];
		else
			i = ((int[])MainActivity.paramstack.get(0))[3];
        Favor favor = new Favor(i);
        String name = getEditString(1073745949);
        String content = getEditString(1073745927);
        String note = favor.modify(name, this.dateinput, content);
        if (MainActivity.paramstack.size() <= 0)
             returnToView(note);
        else
             returnToDetail(note);
    }
	
	void addFavorAuditForSelectAction()
    {
        Favor evection = new Favor(((Integer)this.banklist.get(getSpinnerValue(1073745921))).intValue());
        int kmid = ((Integer)this.km2list.get(getSpinnerValue(1073745937))).intValue();
        long sum = getEditDecimal(1073745925);
        final int depositid = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
        String content = getEditString(1073745927);
        String note = evection.insertAudit(kmid, sum , depositid, content, this.dateinput);
        
		if (note != "操作成功完成！")
        {
            returnToView(note);
        }
        else
        {
            new AlertDialog.Builder(this).setTitle("提示信息 ").setMessage("记账成功,是否继续?").setPositiveButton("继续记账", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
                       FormActivity.this.setEditDecimal(1073745925, 0L);
                       FormActivity.this.setEditString(1073745927, "");
                       Deposit localDeposit = new Deposit(depositid);
                       Spinner localSpinner = (Spinner)FormActivity.this.findViewById(1073745924);
                       ArrayAdapter localArrayAdapter = (ArrayAdapter)localSpinner.getAdapter();
                       localArrayAdapter.remove(localSpinner.getSelectedItem().toString());
                       localArrayAdapter.insert(localDeposit.name + "  " + Convertor.sumToString(localDeposit.sum), localSpinner.getSelectedItemPosition());
                   }
               }).setNegativeButton("返回", new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface paramDialogInterface, int paramInt)
                   {
                       FormActivity.this.returnToView();
                   }
               }).show();
           RuntimeInfo.main.refresh();
       }
    }
	void modifyFavorAuditAction()
	{
		int i = MainActivity.params[3];
		//if (function == 3002)
		//	i = EvectionAudit.getIdByAuditId(MainActivity.params[3]);
		FavorAudit localEvectionAudit = new FavorAudit(i);
		int k = localEvectionAudit.kmid;
		if (localEvectionAudit.kmid > 4)
			k = ((Integer)this.km1list.get(getSpinnerValue(1073745936))).intValue();
		int j = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
		String str = getEditString(1073745927);
		returnToDetail(localEvectionAudit.modify(k, getEditDecimal(1073745925), j, str, this.dateinput));
	}
	////////
	void addEvectionAction()
    {
        String name = getEditString(1073745949);
        String content = getEditString(1073745927);
        int flag = 2;
       //     i = 2;
        returnToView(Evection.insertEvection(flag, name, this.dateinput, content));
    }
	
	void modifyEvectionAction()
    {
		int i;
		if (MainActivity.paramstack.size() <= 0)
			i = MainActivity.params[3];
		else
			i = ((int[])MainActivity.paramstack.get(0))[3];
        Evection evection = new Evection(i);
        String str1 = getEditString(1073745949);
        String str2 = getEditString(1073745927);
        String note = evection.modify(str1, this.dateinput, str2);
        if (MainActivity.paramstack.size() <= 0)
             returnToView(note);
        else
             returnToDetail(note);
    }
	void addEvectionAuditForSelectAction()
    {
        Evection evection = new Evection(((Integer)this.banklist.get(getSpinnerValue(1073745921))).intValue());
        int kmid = ((Integer)this.km2list.get(getSpinnerValue(1073745937))).intValue();
        long sum = getEditDecimal(1073745925);
        final int depositid = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
        String str = getEditString(1073745927);
        String note = evection.insertAudit(kmid, sum, depositid, str, this.dateinput);
        
		if (note != "操作成功完成！")
        {
            returnToView(note);
        }
        else
        {
            new AlertDialog.Builder(this).setTitle("提示信息 ").setMessage("记账成功,是否继续?").setPositiveButton("继续记账", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
                       FormActivity.this.setEditDecimal(1073745925, 0L);
                       FormActivity.this.setEditString(1073745927, "");
                       Deposit localDeposit = new Deposit(depositid);
                       Spinner localSpinner = (Spinner)FormActivity.this.findViewById(1073745924);
                       ArrayAdapter localArrayAdapter = (ArrayAdapter)localSpinner.getAdapter();
                       localArrayAdapter.remove(localSpinner.getSelectedItem().toString());
                       localArrayAdapter.insert(localDeposit.name + "  " + Convertor.sumToString(localDeposit.sum), localSpinner.getSelectedItemPosition());
                   }
               }).setNegativeButton("返回", new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface paramDialogInterface, int paramInt)
                   {
                       FormActivity.this.returnToView();
                   }
               }).show();
           RuntimeInfo.main.refresh();
       }
    }
	void addEvectionAuditAction()
	{
		Evection localObject = new Evection(((int[])MainActivity.paramstack.get(0))[3]);
		int j = ((Integer)this.km2list.get(getSpinnerValue(1073745937))).intValue();
		final int depositid = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
		String str = getEditString(1073745927);
		String note = localObject.insertAudit(j, getEditDecimal(1073745925), depositid, str, this.dateinput);
		if (note != "操作成功完成！")
		{
			returnToView(note);
		}
		else
		{
			new AlertDialog.Builder(this).setTitle("提示信息 ").setMessage("记账成功,是否继续?").setPositiveButton("继续记账", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramDialogInterface, int paramInt)
					{
						FormActivity.this.setEditDecimal(1073745925, 0L);
						FormActivity.this.setEditString(1073745927, "");
						Deposit localDeposit = new Deposit(depositid);
						Spinner localSpinner = (Spinner)FormActivity.this.findViewById(1073745924);
						ArrayAdapter localArrayAdapter = (ArrayAdapter)localSpinner.getAdapter();
						localArrayAdapter.remove(localSpinner.getSelectedItem().toString());
						localArrayAdapter.insert(localDeposit.name + "  " + Convertor.sumToString(localDeposit.sum), localSpinner.getSelectedItemPosition());
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramDialogInterface, int paramInt)
					{
						FormActivity.this.returnToView();
					}
				}).show();
			RuntimeInfo.main.refresh();
		}
	}
	
	void modifyEvectionAuditAction()
	{
		int i = MainActivity.params[3];
		if (function == 3002)
			i = EvectionAudit.getIdByAuditId(MainActivity.params[3]);
		EvectionAudit localEvectionAudit = new EvectionAudit(i);
		int k = localEvectionAudit.kmid;
		if (localEvectionAudit.kmid > 4)
			k = ((Integer)this.km1list.get(getSpinnerValue(1073745936))).intValue();
		int j = ((Integer)this.depositlist.get(getSpinnerValue(1073745924))).intValue();
		String str = getEditString(1073745927);
		returnToDetail(localEvectionAudit.modify(k, getEditDecimal(1073745925), j, str, this.dateinput));
	}
	void addProjectKmAction()
	{
		Evection localEvection = new Evection(((int[])MainActivity.paramstack.get(0))[3]);
		String str = getEditString(1073745921);
		int i = 0;
//		if (localEvection.isProject())
		i = getSpinnerValue(1073745922);
		returnToView(localEvection.addProjectkm(i, str));
	}
	
	void modifyProjectKmAction()
	{
		returnToView(new Evection(((int[])MainActivity.paramstack.get(0))[3]).modifyProjectKm(MainActivity.params[3], getEditString(1073745921)));
	}
	
	void addEvectionFinishAction()
	{
		returnToView(new Evection(((int[])MainActivity.paramstack.get(0))[3]).finish(getEditString(1073745927), this.dateinput));
	}
	////////////////////////////
	String getEditString(int paramInt)
	{
		return ((EditText)findViewById(paramInt)).getText().toString();
	}
	
	
	/************************************
	 * 功能:获得文本框中数据
	 */
	float getEditDecimalf(int paramInt)
    {
        EditText localEditText = (EditText)findViewById(paramInt);
        float f;
        if (localEditText.getText().toString().length() != 0)
            f = Convertor.stringToSumf(localEditText.getText().toString());
        else
            f = 0L;
        return f;
    }
	long getEditDecimal(int paramInt)
    {
        EditText localEditText = (EditText)findViewById(paramInt);
        
        long l;
        if (localEditText.getText().toString().length() != 0)
            l = Convertor.stringToSum(localEditText.getText().toString());
        else
            l = 0L;
        return l;
    }
	long getEditDecimal4(int paramInt)
	{
		EditText localEditText = (EditText)findViewById(paramInt);
		long l;
		if (localEditText.getText().toString().length() != 0)
			l = Convertor.stringToSum4(localEditText.getText().toString());
		else
			l = 0L;
		return l;
	}

    void setEditDecimal(int paramInt, long paramLong)
    {
         EditText localEditText = (EditText)findViewById(paramInt);
         if (paramLong != 0L)
             localEditText.setText(Convertor.sumToString(paramLong));
         else
            localEditText.setText("");
    }
    
    void setEditString(int paramInt, String paramString)
    {
        ((EditText)findViewById(paramInt)).setText(paramString);
    }

    long getEditInteger(int paramInt)
    {
        EditText localEditText = (EditText)findViewById(paramInt);
        long l;
        if (localEditText.getText().toString().length() != 0)
           l = Long.parseLong(localEditText.getText().toString());
        else
           l = 0L;
        return l;
    }
    /////////////
	
    void returnToDetail()
	{
		finish();
	}
	void returnToDetail(String paramString)
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
						FormActivity.detail.redisplay();
						RuntimeInfo.main.refresh();
						FormActivity.this.returnToDetail();
					}
				});
		localBuilder.show();
	}
	
	void returnToView()
    {
        if (detail != null)
        {
            detail.finish();
            detail = null;
        }
        finish();
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
            else if ((Param.MODE == 4) || (function != 1075) || (RuntimeInfo.param.flag[1] != 1))
                     localBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener()
                     {
                         public void onClick(DialogInterface paramDialogInterface, int paramInt)
                         {
                             RuntimeInfo.main.refresh();
                             FormActivity.this.returnToView();
                         }
                     });
                 else
                     localBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener()
                     {
                         public void onClick(DialogInterface paramDialogInterface, int paramInt)
                         {
                             FormActivity.this.returnToLoginForm();
                         }
                     });
        localBuilder.show();
    }
    
    void returnToLoginForm()
    {
        Intent localIntent = new Intent();
 //     localIntent.setClass(this, LoginActivity.class);
        startActivity(localIntent);
        finish();
        RuntimeInfo.main.finish();
    }
    
//------------------------	

//-------------------------------
	
    }
/*









/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.FormActivity
 * JD-Core Version:    0.6.0
 */
