package com.database.model;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.DBTool;
import java.util.ArrayList;
import java.util.List;

public class Bank
{
  public static final short TYPE_BANK = 0;
  public static final short TYPE_FUNDS = 1;
  public static final short TYPE_INSURANCE = 3;
  public static final short TYPE_INVEST = 4;
  public static final short TYPE_OTHER = 5;
  public static final short TYPE_SECURITY = 2;
  public String content = null;
  public int id = 0;
  public String name = null;
  public String phone = null;
  public short type = 0;
	
	public static void createDatabase(SQLiteDatabase paramSQLiteDatabase)
	{
		paramSQLiteDatabase.execSQL("CREATE TABLE bank(id integer PRIMARY KEY AUTOINCREMENT,type smallint, name varchar(20),phone varchar(20),content varchar(60));");
		initData(paramSQLiteDatabase);
	}
	static void initData(SQLiteDatabase paramSQLiteDatabase)
	{
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'工商银行','95588','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'建设银行','95533','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'中国银行','95566','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'农业银行','95599','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'招商银行','95555','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'交通银行','95559','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'浦发银行','95528','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'中信银行','95558','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'兴业银行','95561','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'华夏银行','95577','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'民生银行','95568','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,0,'深发展','95501','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,1,'华夏基金','400-818-6666','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,1,'上投摩根','400-8894-888 ','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,1,'广发基金','95105828','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,1,'嘉实基金','400-600-8800','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,1,'交银施罗德','021-61055000','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,2,'中信证券','95558','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,2,'招商证券','400-8888-111','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,2,'广发证券','020-87555888','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,2,'齐鲁证券','0531-95538','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,3,'中国人寿','95519','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,3,'平安保险','95511','')");
		paramSQLiteDatabase.execSQL("insert into bank values(null,3,'太平洋保险','95500 ','')");
	}
	public static List<String> getBankList(String paramString1, String paramString2, List<Integer> paramList)
	{
		paramList.clear();
		Cursor localCursor = DBTool.database.query("bank", getColumnString(), paramString1, null, null, null, paramString2);
		ArrayList localArrayList = new ArrayList();
		localCursor.moveToFirst();
		while (!localCursor.isAfterLast())
		{
			localArrayList.add(localCursor.getString(2));
			paramList.add(Integer.valueOf(localCursor.getInt(0)));
			localCursor.moveToNext();
		}
		localCursor.close();
		return localArrayList;
	}
	public static String[] getColumnString()
	{
		String[] arrayOfString = new String[5];
		arrayOfString[0] = "id";
		arrayOfString[1] = "type";
		arrayOfString[2] = "name";
		arrayOfString[3] = "phone";
		arrayOfString[4] = "content";
		return arrayOfString;
	}
	public Bank(int paramInt)
    {
        Cursor localCursor = DBTool.database.query("bank", getColumnString(), "id=" + paramInt, null, null, null, null);
        localCursor.moveToFirst();
        this.id = paramInt;
        this.type = localCursor.getShort(1);
        this.name = localCursor.getString(2);
        this.phone = localCursor.getString(3);
        this.content = localCursor.getString(4);
        localCursor.close();
    }

	}
/*

  public static String addBank(short paramShort, String paramString1, String paramString2, String paramString3)
  {
    String str;
    if (paramString1.length() != 0)
    {
      if ((paramShort >= 0) && (paramShort <= 5))
      {
        DBTool.database.execSQL("insert into bank values(null," + paramShort + ",'" + paramString1 + "','" + paramString2 + "','" + paramString3 + "')");
        str = "操作成功完成！";
      }
      else
      {
        str = "类型错误！";
      }
    }
    else
      str = "名字不允许为空！";
    return str;
  }



  public static String delete(int paramInt)
  {
    Object localObject = Deposit.getRows("bankid=" + paramInt, "");
    if (((Cursor)localObject).getCount() <= 0)
    {
      ((Cursor)localObject).close();
      localObject = Credit.getRows("bankid=" + paramInt, "");
      if (((Cursor)localObject).getCount() <= 0)
      {
        ((Cursor)localObject).close();
        localObject = InvestAccount.getRows("bankid=" + paramInt, "");
        if (((Cursor)localObject).getCount() <= 0)
        {
          ((Cursor)localObject).close();
          localObject = Insurance.getRows("bankid=" + paramInt, "");
          if (((Cursor)localObject).getCount() <= 0)
          {
            ((Cursor)localObject).close();
            localObject = Funds.getRows("bankid=" + paramInt, "");
            if (((Cursor)localObject).getCount() <= 0)
            {
              ((Cursor)localObject).close();
              DBTool.database.execSQL("delete from bank where id=" + paramInt);
              localObject = "操作成功完成！";
            }
            else
            {
              ((Cursor)localObject).close();
              localObject = "已经被使用，不允许删除！";
            }
          }
          else
          {
            ((Cursor)localObject).close();
            localObject = "已经被使用，不允许删除！";
          }
        }
        else
        {
          ((Cursor)localObject).close();
          localObject = "已经被使用，不允许删除！";
        }
      }
      else
      {
        ((Cursor)localObject).close();
        localObject = "已经被使用，不允许删除！";
      }
    }
    else
    {
      ((Cursor)localObject).close();
      localObject = "已经被使用，不允许删除！";
    }
    return (String)localObject;
  }



  public static String getBankTypeBame(int paramInt)
  {
    return (String)getBankTypeList().get(paramInt);
  }

  public static List<String> getBankTypeList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("银行");
    localArrayList.add("基金");
    localArrayList.add("券商");
    localArrayList.add("保险");
    localArrayList.add("投资公司");
    localArrayList.add("其他");
    return localArrayList;
  }



  public static Cursor getRows(String paramString1, String paramString2)
  {
    return DBTool.database.query("bank", getColumnString(), paramString1, null, null, null, paramString2);
  }


  public String modifyBank(short paramShort, String paramString1, String paramString2, String paramString3)
  {
    String str;
    if (paramString1.length() != 0)
    {
      if ((paramShort >= 0) && (paramShort <= 5))
      {
        DBTool.database.execSQL("update bank set type=" + paramShort + ",name='" + paramString1 + "',phone='" + paramString2 + "',content='" + paramString3 + "' where id=" + this.id);
        str = "操作成功完成！";
      }
      else
      {
        str = "类型错误！";
      }
    }
    else
      str = "名字不允许为空！";
    return str;
  }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Bank
 * JD-Core Version:    0.6.0
 */
