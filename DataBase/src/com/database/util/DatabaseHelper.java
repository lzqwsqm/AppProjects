/*
 * 自定义数据库
 */
package com.database.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.database.model.Param;//
import com.database.model.Deposit;//
import com.database.model.Account;
import com.database.model.Bank;
import com.database.model.Virement;
import com.database.model.InterestRate;
import com.database.model.Evection;//
import com.database.model.EvectionAudit;

import com.database.model.Audit;
import com.database.model.KM;
import com.database.model.Report;

import com.database.model.Credit;
import com.database.model.CreditAudit;
import com.database.model.Asset;
import com.database.model.AssetAudit;
import com.database.model.AssetKm;
import com.database.model.Favor;
import com.database.model.FavorAudit;

class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 3;
    private Context context;
    
    //构造函数
	//DBTool.getDatabase调用
    DatabaseHelper(Context context, String dbfilename) {
        super(context, dbfilename, null, DATABASE_VERSION);
        this.context = context;
    }

	
	public void onCreate(SQLiteDatabase db)
	{
		DBTool.database = db;
		   if (!kmExist(db))
        createTables(db);
	}
	
	boolean kmExist(SQLiteDatabase db) {
        boolean res = false;
        Cursor cursor = DBTool.database.query("sqlite_master", new String[]{"type", "name"}, "name='km'", null, null, null, "");
        if (cursor.getCount() > 0) {
            res = true;
        }
        cursor.close();
        return res;
    }

	/*
	 * 创建表
	 */
    void createTables(SQLiteDatabase db)
    {
	    Param.createDatabase(db, this.context);
		Account.createDatabase(db);//必须在Deposit前创建因Deposit初始化时写入值
		Deposit.createDatabase(db);//银行
		Virement.createDatabase(db);
		Bank.createDatabase(db);
		InterestRate.createDatabase(db);
		
		Evection.createDatabase(db);//项目
		EvectionAudit.createDatabase(db);
		Audit.createDatabase(db);//收支流水
		KM.createDatabase(db);//科目(字典)
		Report.createDatabase(db);//报表
	    Credit.createDatabase(db);//借贷
		CreditAudit.createDatabase(db);
		Asset.createDatabase(db);//资产
		AssetAudit.createDatabase(db);
		AssetKm.createDatabase(db);
		Favor.createDatabase(db);//人情
		FavorAudit.createDatabase(db);
  }

  void deleteTables(SQLiteDatabase db)
  {
    String[] arrayOfString = new String[25];
    arrayOfString[0] = "km";
    arrayOfString[1] = "deposit";
    arrayOfString[2] = "bank";
    arrayOfString[3] = "interestrate";
    arrayOfString[4] = "account";
    arrayOfString[5] = "audit";
    arrayOfString[6] = "report";
    arrayOfString[7] = "plan";
    arrayOfString[8] = "virement";
    arrayOfString[9] = "param";
    arrayOfString[10] = "credit";
    arrayOfString[11] = "creditaudit";
    arrayOfString[12] = "evection";
    arrayOfString[13] = "evectionaudit";
    arrayOfString[14] = "evectionkm";
    arrayOfString[15] = "asset";
    arrayOfString[16] = "investtype";
    arrayOfString[17] = "investaccount";
    arrayOfString[18] = "investaudit";
    arrayOfString[19] = "investprofit";
    arrayOfString[20] = "stock";
    arrayOfString[21] = "funds";
    arrayOfString[22] = "bond";
    arrayOfString[23] = "insurance";
    arrayOfString[24] = "memo";
    int i = 0;
    while (true)
    {
      if (i >= arrayOfString.length)
        return;
      try
      {
        db.execSQL("Drop TABLE " + arrayOfString[i]);
        i++;
      }
      catch (Exception localException)
      {
        while (true)
          localException.printStackTrace();
            }
        }
    }

 


    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
    }

    public void rebuild()
    {
        SQLiteDatabase db = getWritableDatabase();
        deleteTables(db);
        createTables(db);
    }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.util.DatabaseHelper
 * JD-Core Version:    0.6.0
 */
