/*
 * 系统参数及首页
 */
package com.database.model;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.os.Handler;
import android.telephony.TelephonyManager;
import com.database.util.DBTool;
import com.database.util.MyDate;
import com.database.UIAdapter;
import com.database.ListAdapter;
import com.database.context.Config;
import com.database.util.Convertor;
import com.database.context.RuntimeInfo;
import com.database.context.Function;
 //import com.tomoney.finance.view.MainActivity;

 
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Param
{
	public static final int Flag_Add_Old = 8;
    public static final int Flag_Aj_Return_Is_Outcome = 14;
    public static final int Flag_Analysis = 33;
    public static final int Flag_Asset = 12;
    public static final int Flag_Audit_Number = 0;
    public static final int Flag_Audit_Pos = 60;
    public static final int Flag_Audit_Project = 57;
    public static final int Flag_Background = 51;
    public static final int Flag_Backup_Driver = 17;
    public static final int Flag_Backup_Use_InfoPwd = 4;
    public static final int Flag_Bond = 35;
    public static final int Flag_Chart_Type = 52;
    public static final int Flag_Check_Update = 53;
    public static final int Flag_Credit = 11;
    public static final int Flag_Evection = 6;
    public static final int Flag_Exit_Save = 16;
    public static final int Flag_Favor = 50;
    public static final int Flag_Funds = 36;
    public static final int Flag_Hang_Up_Exit = 7;
    public static final int Flag_InfoPwd = 2;
    public static final int Flag_Insurance = 34;
    public static final int Flag_Insurance_Is_Outcome = 15;
    public static final int Flag_Invest = 10;
    public static final int Flag_Invest_Is_Inout = 13;
    public static final int Flag_Lottery = 58;
    public static final int Flag_Main_Page_Adage = 28;
    public static final int Flag_Main_Page_All_Asset = 40;
    public static final int Flag_Main_Page_Cash = 22;
    public static final int Flag_Main_Page_Cur_Inout = 18;
    public static final int Flag_Main_Page_Cur_Invest_Profit = 19;
    public static final int Flag_Main_Page_Cur_Week_Outcome = 56;
    public static final int Flag_Main_Page_Finance_Asset = 62;
    public static final int Flag_Main_Page_Font_Size = 32;
    public static final int Flag_Main_Page_Insurance_Value = 61;
    public static final int Flag_Main_Page_Next_Memo = 29;
    public static final int Flag_Main_Page_Prev_Income = 20;
    public static final int Flag_Main_Page_Prev_Invest_Profita = 21;
    public static final int Flag_Main_Page_Pure_Asset = 41;
    public static final int Flag_Main_Page_Today_Outcome = 55;
    public static final int Flag_Main_Page_Total_Asset = 27;
    public static final int Flag_Main_Page_Total_Debt = 24;
    public static final int Flag_Main_Page_Total_Deposit = 23;
    public static final int Flag_Main_Page_Total_Invest = 26;
    public static final int Flag_Main_Page_Total_Loan = 25;
    public static final int Flag_Majiang = 59;
    public static final int Flag_Memo_Alert = 54;
    public static final int Flag_Network_Type = 30;
    public static final int Flag_Plan_Alert = 9;
    public static final int Flag_Purchase_Card_As_Income = 37;
    public static final int Flag_Purchase_Card_Income_Km = 38;
    public static final int Flag_Purchase_Card_Income_Km_1 = 39;
    public static final int Flag_Pwd = 1;
    public static final int Flag_Reg = 49;
    public static final int Flag_Tax = 5;
    public static final int Flag_Text_Size = 3;
    public static final int Flag_Tomoney_Pwd_Save_Flag = 31;
    public static final int Flag_Used_Days = 43;
    public static final int Flag_Used_Minutes = 45;
    public static boolean ISMM = false;
    public static int MODE = 0;
    public static final int MODE_AUDIT = 2;
    public static final int MODE_DEBUG = 4;
    public static final int MODE_REGISTER = 3;
    public static int NUMBER_PER_PAGE = 0;
    public static final int SOFT_ID = 4;
    public static final int SOFT_UPDATE_VERSION = 13;
    public static final int TRY_MONTH = 3;
    public static final int VERSION = 8;
    public static final int VERSION_10 = 1;
    public static final int VERSION_15 = 2;
    public static final int VERSION_16 = 6;
    public static final int VERSION_20 = 8;
    public MyDate expireddate;
    public byte[] flag;
    public String imei;
    public byte[] infopwd;
    public MyDate lastdate;
    public byte[] pwd;
    public Date real_expireddate;
    public Date real_lastdate;
    public byte[] regmd5;
    public String serial;
    public String tomoney_pwd;
    public String tomoney_user;
    public short version;

	static
	{
		ISMM = false;
		MODE = 3;
		NUMBER_PER_PAGE = 50;

	}
    
	 /*
     * 创建
     * public static SQLiteDatabase create (SQLiteDatabase.CursorFactory factory) 
     */
    public static void createDatabase(SQLiteDatabase db, Context context)
	{
		db.execSQL("CREATE TABLE param(lastdate integer , expireddate integer,imei varchar(16),serial varchar(20),pwd blob,infopwd blob,version smallint,regmd5 blob,flag blob,tomoney_user vchar(50),tomoney_pwd vchar(32));");
		initData(db, context);
	}
    /*
     * 初始化数据
     */
    /*static void initData(SQLiteDatabase db, Context context)
	{
	    Date lastdate = new Date();
	    Date expireddate = new Date();
	    int i;
	    //小于等于28
	    if (expireddate.getDate() <= 28)
	    {
	        i = 3 + expireddate.getMonth();
	        
	        if (i <= 12)
	        {
	        	expireddate.setMonth(i);
	        }
	        else
	        {
	        	expireddate.setYear(1 + expireddate.getYear());
	        	expireddate.setMonth(i - 12);
	        }
	    }
	    else
	    {
	        i = 1 + (3 + expireddate.getMonth());
	        expireddate.setDate(1);
	        if (i <= 12)
	        {
	        	expireddate.setMonth(i);
	        }
	        else
	        {
	        	expireddate.setYear(1 + expireddate.getYear());
	        	expireddate.setMonth(i - 12);
	        }
	    }
	    String str = ((TelephonyManager)context.getSystemService("phone")).getDeviceId();
	    if ((str == null) || (str.length() == 0))
	        str = "352345678901234";
	    byte[] arrayOfByte = new byte[100];
	    arrayOfByte[0] = 10;
	    ContentValues cv = new ContentValues();
	    cv.put("lastdate", Long.valueOf(lastdate.getTime()));
	    cv.put("expireddate", Long.valueOf(expireddate.getTime()));
	    cv.put("imei", str);
	    cv.put("serial", "");
	    cv.put("pwd", "pwd");
	    cv.put("infopwd", "infopwd");
	    cv.put("version", Integer.valueOf(8));
	    cv.put("regmd5", "");
	    cv.put("flag", arrayOfByte);
	    cv.put("tomoney_user", "");
	    cv.put("tomoney_pwd", "");
	    db.insert("param", null, cv);
	    
	    Param param = new Param();
	    param.createRegMd5();
	    param.save();
	}*/
	static void initData(SQLiteDatabase db, Context context) {
        Date today = new Date();
        Date expireday = new Date();
        int mm;
        if (expireday.getDate() > 28/*Flag_Main_Page_Adage*/) {
            mm = (expireday.getMonth() + 3/*TRY_MONTH*/) + 1/*VERSION_10*/;
            expireday.setDate(1/*VERSION_10*/);
            if (mm > Flag_Asset) {
                expireday.setYear(expireday.getYear() + 1/*VERSION_10*/);
                expireday.setMonth(mm - 12);
            } else {
                expireday.setMonth(mm);
            }
        } else {
            mm = expireday.getMonth() + TRY_MONTH;
            if (mm > Flag_Asset) {
                expireday.setYear(expireday.getYear() + VERSION_10);
                expireday.setMonth(mm - 12);
            } else {
                expireday.setMonth(mm);
            }
        }
        String imei = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (imei == null || imei.length() == 0) {
            imei = "352345678901234";
        }
        byte[] flags = new byte[100];
        flags[Flag_Audit_Number] = (byte) 10;
        ContentValues values = new ContentValues();
        values.put("lastdate", Long.valueOf(today.getTime()));
        values.put("expireddate", Long.valueOf(expireday.getTime()));
        values.put("imei", imei);
        values.put("serial", "");
        values.put("pwd", "pwd");
        values.put("infopwd", "infopwd");
        values.put("version", Integer.valueOf(VERSION_20));
        values.put("regmd5", "");
        values.put("flag", flags);
        values.put("tomoney_user", "");
        values.put("tomoney_pwd", "");
        db.insert(Config.PARAM, null, values);
        Param param = new Param();
        param.createRegMd5();
        param.save();
    }


	public void createRegMd5() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.imei);
        buf.append(this.expireddate.getYear());
        buf.append(this.expireddate.getMonth());
        buf.append(this.expireddate.getDay());
        buf.append(this.flag[Flag_Reg]);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buf.toString().getBytes());
            this.regmd5 = md.digest();
        } catch (Exception e) {
        }
    }


	public void save()
	{
		save(DBTool.database);
	}

	public void save(SQLiteDatabase db)
	{
		ContentValues cv = new ContentValues();
		cv.put("lastdate", Long.valueOf(this.lastdate.getDate().getTime()));
		cv.put("expireddate", Long.valueOf(this.expireddate.getDate().getTime()));
		cv.put("imei", this.imei);
		cv.put("serial", this.serial);
		cv.put("pwd", this.pwd);
		cv.put("infopwd", this.infopwd);
		cv.put("version", Short.valueOf(this.version));
		cv.put("regmd5", this.regmd5);
		cv.put("flag", this.flag);
		cv.put("tomoney_user", this.tomoney_user);
		cv.put("tomoney_pwd", this.tomoney_pwd);
		db.update("param", cv, null, null);
	}
//-----------------------
	
	//系统初始化
	
    public Param() {
        this.lastdate = null;
        this.expireddate = null;
        this.real_lastdate = null;
        this.real_expireddate = null;
        this.imei = null;
        this.serial = null;
        this.pwd = null;
        this.infopwd = null;
        this.regmd5 = null;
        this.flag = null;
        this.tomoney_user = null;
        this.tomoney_pwd = null;
        reset(DBTool.database);
    }


	/*public void reset(SQLiteDatabase db)
	{
		Cursor localCursor = db.query("param", getColumnString(), null, null, null, null, null);
		localCursor.moveToFirst();
		this.real_lastdate = new Date(localCursor.getLong(0));
		this.lastdate = new MyDate(this.real_lastdate);
		this.real_expireddate = new Date(localCursor.getLong(1));
		this.expireddate = new MyDate(this.real_expireddate);
		this.imei = localCursor.getString(2);
		this.serial = localCursor.getString(3);
		this.pwd = localCursor.getBlob(4);
		this.infopwd = localCursor.getBlob(5);
		this.version = localCursor.getShort(6);
		this.regmd5 = localCursor.getBlob(7);
		this.flag = localCursor.getBlob(8);
		this.tomoney_user = localCursor.getString(9);
		this.tomoney_pwd = localCursor.getString(10);
		localCursor.close();
		if ((this.flag[3] < 12) || (this.flag[3] > 50))
			this.flag[3] = (byte)UIAdapter.getDefaultTextSize();
	}*/
	public void reset(SQLiteDatabase db) {
        Cursor cur = db.query(Config.PARAM, getColumnString(), null, null, null, null, null);
        cur.moveToFirst();
        this.real_lastdate = new Date(cur.getLong(Flag_Audit_Number));
        this.lastdate = new MyDate(this.real_lastdate);
        this.real_expireddate = new Date(cur.getLong(1));
        this.expireddate = new MyDate(this.real_expireddate);
        this.imei = cur.getString(VERSION_15);
        this.serial = cur.getString(TRY_MONTH);
        this.pwd = cur.getBlob(SOFT_ID);
        this.infopwd = cur.getBlob(Flag_Tax);
        this.version = cur.getShort(VERSION_16);
        this.regmd5 = cur.getBlob(Flag_Hang_Up_Exit);
        this.flag = cur.getBlob(VERSION_20);
        this.tomoney_user = cur.getString(Flag_Plan_Alert);
        this.tomoney_pwd = cur.getString(Flag_Invest);
        cur.close();
        if (this.flag[3] < Flag_Asset || this.flag[3] > Flag_Favor) {
            this.flag[3] = (byte) UIAdapter.getDefaultTextSize();
        }
    }



    public static String[] getColumnString()
    {
        String[] column = new String[11];
        column[0] = "lastdate";
        column[1] = "expireddate";
        column[2] = "imei";
        column[3] = "serial";
        column[4] = "pwd";
        column[5] = "infopwd";
        column[6] = "version";
        column[7] = "regmd5";
        column[8] = "flag";
        column[9] = "tomoney_user";
        column[10] = "tomoney_pwd";
        return column;
    }
    
    /*
     * 
     */
    /*public void initDay(Date date, Context context, Handler handler)
    {
      if (this.lastdate.lessThan(date))
      {
        RuntimeInfo.param.addUsedDays();
        Deposit.dealCreditCardBillAndPos(this.lastdate, new MyDate(date));
     //   Report.initReport(this.lastdate, paramDate);
     //   if (MODE != 2)
      //    Memo.autoAudit(this.lastdate);
        this.real_lastdate = date;
        this.lastdate = new MyDate(this.real_lastdate);
        save();
        if ((MODE == 3) && (checkVersion() == "此功能需注册,请访问官网！"))
          MODE = 2;
        if (MODE == 4)
          ISMM = false;
      }
      else
      {
        if ((MODE == 3) && (checkVersion() == "此功能需注册,请访问官网！"))
          MODE = 2;
        if (MODE == 4)
          ISMM = false;
      }
    }*/
	
    public void initDay(Date new_date, Context context, Handler handler) {
        if (this.lastdate.lessThan(new_date)) {
            RuntimeInfo.param.addUsedDays();
            Deposit.dealCreditCardBillAndPos(this.lastdate, new MyDate(new_date));
           // Report.initReport(this.lastdate, new_date);
           // if (MODE != VERSION_15) {
            //    Memo.autoAudit(this.lastdate);
           // }
            this.real_lastdate = new_date;
            this.lastdate = new MyDate(this.real_lastdate);
            save();
            if (MODE == TRY_MONTH && checkVersion() == Function.ERROR_SOFT_EXPIRED) {
                MODE = VERSION_15;
            }
            if (MODE == SOFT_ID) {
                ISMM = false;
                return;
            }
            return;
        }
        if (MODE == TRY_MONTH && checkVersion() == Function.ERROR_SOFT_EXPIRED) {
            MODE = VERSION_15;
        }
        if (MODE == SOFT_ID) {
            ISMM = false;
        }
    }

    public void addUsedDays()
    {
      Convertor.fromShort((short)(int)(1L + Convertor.toLong(this.flag, 43, 2)), this.flag, 43);
    }

    public void addUsedMinitus(long paramLong)
    {
      Convertor.fromInt((int)(Convertor.toLong(this.flag, 45, 4) + paramLong / 1000L), this.flag, 45);
    }
    
    
    //首页显示------------------------
    public void fillMainPageList(ListAdapter listadapter, List<Integer> index)
    {
        listadapter.clear();
        index.clear();
        index.add(Integer.valueOf(2147483395));
        ArrayList listdata = new ArrayList();

		String[] fir = new String[2];
        fir[0] = MyDate.now().toMMDDString();
        fir[1] = getRandomBless();
        listdata.add(fir);

	    Report report = new Report(2, MyDate.now());
		//    report = Report.prevReport(2, report.date);

	    if ((this.flag[55] == 0) || (this.flag[56] == 0) || (this.flag[18] == 0) || (this.flag[20] == 0) || ((MODE != 2) && ((this.flag[19] == 0) || (this.flag[20] == 0))))
        {
            String[] zjsz = new String[1];
            zjsz[0] = "最近收支";
            addMainPageLine(listdata, index, zjsz, 2147483409);
        }
        long l;
        if (this.flag[55] == 0)
        {
            l = new Date().getTime();
            l = l - l % 86400000L - 28800000L;
            String[] jrzc = new String[2];
            jrzc[0] = "今日支出";
            jrzc[1] = Convertor.sumToString(Audit.getOutcomeAfter(l));
            addMainPageLine(listdata, index, jrzc, -1);
            String[] jrsr = new String[2];
            jrsr[0] = "今日收入";
            jrsr[1] = Convertor.sumToString(Audit.getIncomeAfter(l));
            addMainPageLine(listdata, index, jrsr, -1);
		}

		/*    if (this.flag[56] == 0)
		 {
		 localObject3 = new Date();
		 l = ((Date)localObject3).getTime();
		 int i;
		 if (((Date)localObject3).getDay() != 0)
		 i = -1 + ((Date)localObject3).getDay();
		 else
		 i = 6;
		 l -= 3600000 * (i * 24);
		 l = l - l % 86400000L - 28800000L;
		 String[] arrayOfString3 = new String[2];
		 arrayOfString3[0] = "本周支出";
		 arrayOfString3[1] = Convertor.sumToString(Audit.getOutcomeAfter(l));
		 addMainPageLine(localArrayList, paramList, arrayOfString3, -1);
		 }
		 String[] arrayOfString2;*/
		if (this.flag[18] == 0)
		{
			if (report.getKmSum(1) > 0L)
			{
				String[] arrayOfString2 = new String[2];
				arrayOfString2[0] = "本月收入";
				arrayOfString2[1] = Convertor.sumToString(report.getKmSum(1));
				addMainPageLine(listdata, index, arrayOfString2, -1);
			}
			
			if (report.getKmSum(2) > 0L)
			{
				String[] byzc = new String[2];
				byzc[0] = "本月支出";
				byzc[1] = Convertor.sumToString(report.getKmSum(2));
				addMainPageLine(listdata, index, byzc, -1);
			}
		}
		/*    if ((MODE != 2) && (this.flag[19] == 0))
		 {
		 arrayOfString2 = new String[2];
		 arrayOfString2[0] = "本月收益";
		 arrayOfString2[1] = Convertor.sumToString(((Report)localObject2).getKmSum(301));
		 addMainPageLine(localArrayList, paramList, arrayOfString2, -1);
		 }
		 if ((((Report)localObject1).type != 5) && (this.flag[20] == 0))
		 {
		 localObject2 = new String[2];
		 localObject2[0] = "上月净收入";
		 localObject2[1] = Convertor.sumToString(((Report)localObject1).getKmSum(1) - ((Report)localObject1).getKmSum(2));
		 addMainPageLine(localArrayList, paramList, localObject2, -1);
		 }
		 if ((MODE != 2) && (((Report)localObject1).type != 5) && (this.flag[21] == 0))
		 {
		 localObject2 = new String[2];
		 localObject2[0] = "上月收益";
		 localObject2[1] = Convertor.sumToString(((Report)localObject1).getKmSum(301));
		 addMainPageLine(localArrayList, paramList, localObject2, -1);
		 }*/
		if ((this.flag[22] == 0) || (this.flag[23] == 0) || (this.flag[24] == 0) || (this.flag[25] == 0) || (this.flag[40] == 0) || (this.flag[41] == 0) || ((MODE != 2) && ((this.flag[26] == 0) || (this.flag[27] == 0) || (this.flag[27] == 0))))
		{
			String[] zczk = new String[1];
			zczk[0] = "资产状况";
			addMainPageLine(listdata, index, zczk, 2147483410);
		}
		if (this.flag[22] == 0)
		{
			String[] dqxj = new String[2];
			dqxj[0] = "当前现金";
			dqxj[1] = Convertor.sumToString(Account.getAccountSum(1));
			addMainPageLine(listdata, index, dqxj, -1);
		}
		if (this.flag[23] == 0)
		{
			String[] zck = new String[2];
			zck[0] = "总存款";
			zck[1] = Convertor.sumToString(Account.getAccountSum(2) + Account.getAccountSum(3));
			addMainPageLine(listdata, index, zck, -1);
		}
		/*    if (MODE != 2)
		 {
		 if ((this.flag[26] == 0) && (Account.getAccountSum(10) > 0L))
		 {
		 localObject1 = new String[2];
		 localObject1[0] = "投资";
		 localObject1[1] = Convertor.sumToString(Account.getAccountSum(10));
		 addMainPageLine(localArrayList, paramList, localObject1, -1);
		 }
		 if ((this.flag[61] == 0) && (Account.getAccountSum(7) > 0L))
		 {
		 localObject1 = new String[2];
		 localObject1[0] = "保险";
		 localObject1[1] = Convertor.sumToString(Account.getAccountSum(7));
		 addMainPageLine(localArrayList, paramList, localObject1, -1);
		 }
		 if ((this.flag[27] == 0) && (Account.getAccountSum(12) > 0L))
		 {
		 localObject1 = new String[2];
		 localObject1[0] = "不动产";
		 localObject1[1] = Convertor.sumToString(Account.getAccountSum(12));
		 addMainPageLine(localArrayList, paramList, localObject1, -1);
		 }
		 }*/
		if ((this.flag[25] == 0) && (Account.getAccountSum(4) > 0L))
		{
			String[] zq = new String[2];
			zq[0] = "债权";
			zq[1] = Convertor.sumToString(Account.getAccountSum(4));
			addMainPageLine(listdata, index, zq, -1);
		}
		if ((this.flag[24] == 0) && (Account.getAccountSum(5) > 0L))
		{
			String[] zw = new String[2];
			zw[0] = "债务";
			zw[1] = Convertor.sumToString(Account.getAccountSum(5));
			addMainPageLine(listdata, index, zw, -1);
		}
		if (this.flag[40] == 0)
		{
			String[] zzc = new String[2];
			zzc[0] = "总资产";
			zzc[1] = Convertor.sumToString(Account.getAccountSum(1) + Account.getAccountSum(2) + Account.getAccountSum(3) + Account.getAccountSum(4) + Account.getAccountSum(10) + Account.getAccountSum(7) + Account.getAccountSum(12));
			addMainPageLine(listdata, index, zzc, -1);
		}
		if (this.flag[62] == 0)
		{
			String[] jrozc = new String[2];
			jrozc[0] = "金融资产";
			jrozc[1] = Convertor.sumToString(Account.getAccountSum(1) + Account.getAccountSum(2) + Account.getAccountSum(3) + Account.getAccountSum(4) + Account.getAccountSum(10));
			addMainPageLine(listdata, index, jrozc, -1);
		}
		if (this.flag[41] == 0)
		{
			String[] jzc = new String[2];
			jzc[0] = "净资产";
			jzc[1] = Convertor.sumToString(Account.getAccountSum(1) + Account.getAccountSum(2) + Account.getAccountSum(3) + Account.getAccountSum(4) + Account.getAccountSum(10) + Account.getAccountSum(7) + Account.getAccountSum(12) - Account.getAccountSum(5));
			addMainPageLine(listdata, index, jzc, -1);
		}

		/*    if ((MODE != 2) && (this.flag[29] == 0))
		 {
		 localObject2 = new Date();
		 ((Date)localObject2).setHours(0);
		 ((Date)localObject2).setMinutes(0);
		 ((Date)localObject2).setSeconds(0);
		 Memo.retrieve();
		 localObject1 = new Memo();
		 localObject2 = Memo.getRows("sum>0 and date>" + ((Date)localObject2).getTime(), "date asc");
		 ((Cursor)localObject2).moveToFirst();
		 if (!((Cursor)localObject2).isAfterLast())
		 {
		 arrayOfString2 = new String[1];
		 arrayOfString2[0] = "最新备忘";
		 addMainPageLine(localArrayList, paramList, arrayOfString2, 2147483411);
		 ((Memo)localObject1).reset((Cursor)localObject2);
		 arrayOfString2 = new String[2];
		 arrayOfString2[0] = ((Memo)localObject1).getTypeName();
		 arrayOfString2[1] = ((Memo)localObject1).date.toShortString("/");
		 addMainPageLine(localArrayList, paramList, arrayOfString2, ((Memo)localObject1).id);
		 }
		 ((Cursor)localObject2).moveToNext();
		 if (!((Cursor)localObject2).isAfterLast())
		 {
		 ((Memo)localObject1).reset((Cursor)localObject2);
		 arrayOfString2 = new String[2];
		 arrayOfString2[0] = ((Memo)localObject1).getTypeName();
		 arrayOfString2[1] = ((Memo)localObject1).date.toShortString("/");
		 addMainPageLine(localArrayList, paramList, arrayOfString2, ((Memo)localObject1).id);
		 }
		 ((Cursor)localObject2).moveToNext();
		 if (!((Cursor)localObject2).isAfterLast())
		 {
		 ((Memo)localObject1).reset((Cursor)localObject2);
		 arrayOfString2 = new String[2];
		 arrayOfString2[0] = ((Memo)localObject1).getTypeName();
		 arrayOfString2[1] = ((Memo)localObject1).date.toShortString("/");
		 addMainPageLine(localArrayList, paramList, arrayOfString2, ((Memo)localObject1).id);
		 }
		 ((Cursor)localObject2).close();
		 }*/

		if (this.flag[28] == 0)
		{
			String[] jy = new String[1];
			jy[0] = "理财箴言";
			addMainPageLine(listdata, index, jy, 2147483412);
			String[] lcjy = new String[1];
			lcjy[0] = getRandomAdage();
			listdata.add(lcjy);
		}
        int[] width_layout = new int[3];
        width_layout[0] = 20;
        width_layout[1] = 120;
        width_layout[2] = 180;
        listadapter.setLayout(width_layout);
        listadapter.append(listdata);
    }

    public static String getRandomBless()
    {
		String[] localObject = Config.bless;
		byte i = Config.main_page_title;
		Config.main_page_title = (byte)(i + 1);

		String localObject1 = localObject[i];
		if (Config.main_page_title == Config.bless.length)
			Config.main_page_title = 0;
		return localObject1;
    }

	public void addMainPageLine(List<String[]> listdata, List<Integer> index, String[] paramArrayOfString, int paramInt)
	{
		listdata.add(paramArrayOfString);
		index.add(Integer.valueOf(paramInt));
	}
	public static String getRandomAdage()
    {
        int i = new Random().nextInt(Config.adage.length);
        return Config.adage[i];
    }
	
	
	public String checkVersion()
	{
		String str;
		if (MODE != 4)
		{
			if (checkRegmd5())
			{
				if ((this.lastdate.getValue() <= this.expireddate.getValue()) || (this.flag[49] != 0))
					str = "操作成功完成！";
				else
					str = "此功能需注册,请访问官网！";
			}
			else
				str = "软件信息错误,请联系作者！";
		}
		else
			str = "操作成功完成！";
		return str;
	}
	public boolean checkRegmd5() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.imei);
        buf.append(this.expireddate.getYear());
        buf.append(this.expireddate.getMonth());
        buf.append(this.expireddate.getDay());
        buf.append(this.flag[Flag_Reg]);
        byte[] md5_ver = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buf.toString().getBytes());
            md5_ver = md.digest();
            if (md5_ver.length != this.regmd5.length) {
                return false;
            }
            for (int i = Flag_Audit_Number; i < md5_ver.length; i += VERSION_10) {
                if (md5_ver[i] != this.regmd5[i]) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
 
	  /////////////////
	public static void fillAboutList(ListAdapter listadapter, List<Integer> index)
	  {
		listadapter.clear();
		index.clear();
		index.add(Integer.valueOf(2147483394));
	    ArrayList localArrayList = new ArrayList();
	    
	    String[] localObject = new String[1];
	    localObject[0] = "";
	    localArrayList.add(localObject);
	    
	    if (MODE != 3)
	    {
	      if (MODE != 4)
	      {
	        if (MODE != 2)
	        {
	          localObject = new String[1];
	          localObject[0] = "君子爱财 2.0";
	          localArrayList.add(localObject);
	        }
	        else
	        {
	          localObject = new String[1];
	          localObject[0] = "君子爱财 2.0记帐版";
	          localArrayList.add(localObject);
	        }
	      }
	      else
	      {
	        localObject = new String[1];
	        localObject[0] = "君子爱财 2.0 DEBUG";
	        localArrayList.add(localObject);
	      }
	    }
	    else
	    {
	      localObject = new String[1];
	      localObject[0] = "君子爱财 2.0完整版";
	      localArrayList.add(localObject);
	    }
	    index.add(Integer.valueOf(-1));
	    
	    localObject = new String[1];
	    localObject[0] = "新浪微博: 君子爱财_conte";
	    localArrayList.add(localObject);
	    index.add(Integer.valueOf(-1));
	    
	    localObject = new String[1];
	    localObject[0] = "官网: www.tomoney.com.cn";
	    localArrayList.add(localObject);
	    index.add(Integer.valueOf(-1));
	    
	    if (RuntimeInfo.param.flag[49] == 1)
	    {
	      localObject = new String[1];
	      localObject[0] = "  已注册,感谢支持!";
	      localArrayList.add(localObject);
	      index.add(Integer.valueOf(2147483396));
	    }
	    
	    int[] layout_about = new int[2];
	    layout_about[0] = 20;
	    layout_about[1] = 300;
	    listadapter.setLayout(layout_about);
	    listadapter.append(localArrayList);
	  }
}	


/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Param
 * JD-Core Version:    0.6.0
 */

