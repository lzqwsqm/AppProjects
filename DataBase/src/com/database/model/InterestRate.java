package com.database.model;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.Convertor;
import com.database.util.DBTool;
import com.database.ListAdapter;
import java.util.ArrayList;
import java.util.List;
	
public class InterestRate
{
  public short id = 0;
  public short rate = 0;



    public static void createDatabase(SQLiteDatabase paramSQLiteDatabase)
    {
        paramSQLiteDatabase.execSQL("CREATE TABLE interestrate(id smallint ,rate smallint);");
        initData(paramSQLiteDatabase);
    }
	static void initData(SQLiteDatabase paramSQLiteDatabase)
    {
        short[] arrayOfShort = new short[25];
        arrayOfShort[0] = 50;
        arrayOfShort[1] = 285;
        arrayOfShort[2] = 305;
        arrayOfShort[3] = 325;
        arrayOfShort[4] = 415;
        arrayOfShort[5] = 475;
        arrayOfShort[6] = 525;
        for (int i = 1; i <= 7; i++)
            paramSQLiteDatabase.execSQL("insert into interestrate values(" + i + "," + arrayOfShort[(i - 1)] + ")");
        paramSQLiteDatabase.execSQL("insert into interestrate values(11,0)");
        arrayOfShort[13] = 285;
        arrayOfShort[14] = 305;
        arrayOfShort[15] = 325;
        arrayOfShort[16] = 95;
        arrayOfShort[17] = 149;
        arrayOfShort[18] = 585;
        arrayOfShort[19] = 631;
        arrayOfShort[20] = 640;
        arrayOfShort[21] = 665;
        arrayOfShort[22] = 680;
        arrayOfShort[23] = 10;
        for (int i = 13; i <= 23; i++)
            paramSQLiteDatabase.execSQL("insert into interestrate values(" + i + "," + arrayOfShort[i] + ")");
    }
	public InterestRate(int paramInt)
    {
        Cursor localCursor = DBTool.database.query("interestrate", getColumnString(), "id=" + paramInt, null, null, null, null);
        localCursor.moveToFirst();
        this.id = (short)paramInt;
        this.rate = localCursor.getShort(1);
        localCursor.close();
    }
	public static String[] getColumnString()
    {
        String[] arrayOfString = new String[2];
        arrayOfString[0] = "id";
        arrayOfString[1] = "rate";
        return arrayOfString;
    }

    }
/*



  public static void fillList(FinanceAdapter paramFinanceAdapter, List<Integer> paramList)
  {
    Object localObject = getRows("(id>=1 and id<=7) or (id>=13 and id<=23)", null);
    ((Cursor)localObject).moveToFirst();
    paramFinanceAdapter.clear();
    paramList.clear();
    paramList.add(Integer.valueOf(2147483394));
    ArrayList localArrayList = new ArrayList();
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "储种";
    arrayOfString[1] = "利率%";
    localArrayList.add(arrayOfString);
    while (!((Cursor)localObject).isAfterLast())
    {
      int i = ((Cursor)localObject).getInt(0);
      arrayOfString = new String[2];
      arrayOfString[0] = Deposit.getDepositTypeName(i);
      arrayOfString[1] = Convertor.sumToString(((Cursor)localObject).getShort(1));
      localArrayList.add(arrayOfString);
      paramList.add(Integer.valueOf(i));
      ((Cursor)localObject).moveToNext();
    }
    ((Cursor)localObject).close();
    localObject = new int[3];
    localObject[0] = 40;
    localObject[1] = 100;
    localObject[2] = 180;
    paramFinanceAdapter.setLayout(localObject);
    paramFinanceAdapter.append(localArrayList);
  }

  
  public static Cursor getRows(String paramString1, String paramString2)
  {
    return DBTool.database.query("interestrate", getColumnString(), paramString1, null, null, null, paramString2);
  }



  public String modifyInterestRate(int paramInt)
  {
    DBTool.database.execSQL("update interestrate set rate=" + paramInt + " where id=" + this.id);
    return "操作成功完成！";
  }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.InterestRate
 * JD-Core Version:    0.6.0
 */
