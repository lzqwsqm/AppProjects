/*
 * 
 */
package com.database.model;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import com.tomoney.finance.util.Convertor;
import com.database.util.DBTool;
//import com.tomoney.finance.view.FinanceAdapter;
import java.util.ArrayList;
import java.util.List;

public class Account
{
/*	public static final int AccountAsset = 12;

	public static final int Account_Number = 12;*/
	public int id = 0;
	public String name = null;
	public long sum = 0L;

	 /*
     * 创建
     * public static SQLiteDatabase create (SQLiteDatabase.CursorFactory factory) 
     */
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE account(id integer PRIMARY KEY , name varchar(20),sum  int );");
		initData(db);
	}
	static void initData(SQLiteDatabase db)
	{
		db.execSQL("insert into account values(1,'现金',0)");
		db.execSQL("insert into account values(2,'活期',0)");
		db.execSQL("insert into account values(3,'定期',0)");
		db.execSQL("insert into account values(4,'债权',0)");
		db.execSQL("insert into account values(5,'债务',0)");
		db.execSQL("insert into account values(6,'保险领取',0)");
		db.execSQL("insert into account values(7,'保险',0)");
		db.execSQL("insert into account values(8,'保险缴费',0)");
		db.execSQL("insert into account values(9,'投资余额',0)");
		db.execSQL("insert into account values(10,'投资市值',0)");
		db.execSQL("insert into account values(11,'投资成本',0)");
		db.execSQL("insert into account values(12,'固定资产',0)");
	}
	

	public static void addAccountSum(int paramInt, long paramLong)
	{
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("update ").append("account").append(" set sum=sum+").append(paramLong).append(" where id=").append(paramInt);
		DBTool.database.execSQL(localStringBuffer.toString());
	}
	
	public static long getAccountSum(int paramInt)
	{
		Cursor localCursor = getRows("id=" + paramInt, "");
		localCursor.moveToFirst();
		long l = localCursor.getLong(2);
		localCursor.close();
		return l;
	}
    	


	public static String[] getColumnString()
	{
		String[] arrayOfString = new String[3];
		arrayOfString[0] = "id";
		arrayOfString[1] = "name";
		arrayOfString[2] = "sum";
		return arrayOfString;
	}

	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("account", getColumnString(), paramString1, null, null, null, paramString2);
	}
	
    
	
	public Account(int paramInt)
	{
		reset(paramInt);
	}
	public void reset(int paramInt)
	{
		Cursor localCursor = DBTool.database.query("account", getColumnString(), "id=" + paramInt, null, null, null, null);
		localCursor.moveToFirst();
		reset(localCursor);
		localCursor.close();
	}

	public void reset(Cursor paramCursor)
	{
		this.id = paramCursor.getInt(0);
		this.name = paramCursor.getString(1);
		this.sum = paramCursor.getLong(2);
	}
}
/*	public static void assetCheck()
	{
		long[] arrayOfLong = new long[13];
		Cursor localCursor1 = Deposit.getRows("flag >-1 ", null);
		localCursor1.moveToFirst();
		while (!localCursor1.isAfterLast())
		{
			switch (localCursor1.getShort(2))
			{
				case 1:
				case 9:
				case 10:
				case 25:
					arrayOfLong[2] += localCursor1.getLong(3);
					break;
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 13:
				case 14:
				case 15:
					arrayOfLong[3] += localCursor1.getLong(3);
					break;
				case 8:
					if (localCursor1.getLong(3) <= 0L)
						arrayOfLong[5] -= localCursor1.getLong(3);
					else
						arrayOfLong[2] += localCursor1.getLong(3);
					break;
				case 12:
					arrayOfLong[1] += localCursor1.getLong(3);
				case 11:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
			}
			localCursor1.moveToNext();
		}
		localCursor1.close();
		localCursor1 = Credit.getRows("flag>-1", null);
		localCursor1.moveToFirst();
		while (!localCursor1.isAfterLast())
		{
			switch (localCursor1.getShort(1))
			{
				default:
					arrayOfLong[5] += localCursor1.getLong(2);
					break;
				case 0:
					arrayOfLong[4] += localCursor1.getLong(2);
			}
			localCursor1.moveToNext();
		}
		localCursor1.close();
		localCursor1 = Evection.getRows("flag=0", "");
		Evection localEvection = new Evection();
		localCursor1.moveToFirst();
		while (!localCursor1.isAfterLast())
		{
			localEvection.reset(localCursor1);
			arrayOfLong[5] += localEvection.getSum(2) - localEvection.getSum(4);
			localCursor1.moveToNext();
		}
		localCursor1.close();
		long l = 0L;
		Object localObject = Insurance.getRows("flag>=0", null);
		((Cursor)localObject).moveToFirst();
		while (!((Cursor)localObject).isAfterLast())
		{
			l += ((Cursor)localObject).getLong(3);
			((Cursor)localObject).moveToNext();
		}
		((Cursor)localObject).close();
		localObject = new InvestAccount(1);
		((InvestAccount)localObject).setValue(l);
		((InvestAccount)localObject).setSum(0L);
		((InvestAccount)localObject).save();
		l = 0L;
		localObject = Bond.getRows("flag>=0", null);
		((Cursor)localObject).moveToFirst();
		while (!((Cursor)localObject).isAfterLast())
		{
			l += ((Cursor)localObject).getLong(3);
			((Cursor)localObject).moveToNext();
		}
		((Cursor)localObject).close();
		localObject = new InvestAccount(2);
		((InvestAccount)localObject).setValue(l);
		((InvestAccount)localObject).setSum(0L);
		((InvestAccount)localObject).save();
		l = 0L;
		localObject = Funds.getRows("flag>=0", null);
		((Cursor)localObject).moveToFirst();
		while (!((Cursor)localObject).isAfterLast())
		{
			l += Funds.getValue(((Cursor)localObject).getLong(5), ((Cursor)localObject).getLong(4));
			((Cursor)localObject).moveToNext();
		}
		((Cursor)localObject).close();
		localObject = new InvestAccount(3);
		((InvestAccount)localObject).setValue(l);
		((InvestAccount)localObject).setSum(0L);
		((InvestAccount)localObject).save();
		Cursor localCursor2 = InvestAccount.getRows("flag=0", null);
		localCursor2.moveToFirst();
		while (!localCursor2.isAfterLast())
		{
			switch (localCursor2.getShort(2))
			{
				default:
					arrayOfLong[10] += localCursor2.getLong(5) + localCursor2.getLong(3);
					arrayOfLong[11] += localCursor2.getLong(4);
					break;
				case 1:
					arrayOfLong[6] += localCursor2.getLong(3);
					arrayOfLong[7] += localCursor2.getLong(5);
					arrayOfLong[8] += localCursor2.getLong(4);
			}
			localCursor2.moveToNext();
		}
		localCursor2.close();
		localCursor2 = Asset.getRows("flag>=0", null);
		localCursor2.moveToFirst();
		while (!localCursor2.isAfterLast())
		{
			arrayOfLong[12] += localCursor2.getLong(2);
			localCursor2.moveToNext();
		}
		localCursor2.close();
		for (int i = 1; i <= 12; i++)
			DBTool.database.execSQL("update account set sum=" + arrayOfLong[i] + " where id=" + i);
	}



	public static void fillList(FinanceAdapter paramFinanceAdapter, List<Integer> paramList)
	{
		ArrayList localArrayList = new ArrayList();
		Object localObject = new String[2];
		localObject[0] = "账户";
		localObject[1] = "余额";
		localArrayList.add(localObject);
		paramList.clear();
		paramFinanceAdapter.clear();
		paramList.add(Integer.valueOf(2147483394));
		localObject = new Account(1);
		String[] arrayOfString1 = new String[2];
		arrayOfString1[0] = ((Account)localObject).name;
		arrayOfString1[1] = Convertor.sumToString(((Account)localObject).sum);
		localArrayList.add(arrayOfString1);
		long l1 = 0L + ((Account)localObject).sum;
		long l3 = 0L + ((Account)localObject).sum;
		((Account)localObject).reset(2);
		String[] arrayOfString3 = new String[2];
		arrayOfString3[0] = ((Account)localObject).name;
		arrayOfString3[1] = Convertor.sumToString(((Account)localObject).sum);
		localArrayList.add(arrayOfString3);
		l1 += ((Account)localObject).sum;
		l3 += ((Account)localObject).sum;
		((Account)localObject).reset(3);
		arrayOfString3 = new String[2];
		arrayOfString3[0] = ((Account)localObject).name;
		arrayOfString3[1] = Convertor.sumToString(((Account)localObject).sum);
		localArrayList.add(arrayOfString3);
		l1 += ((Account)localObject).sum;
		l3 += ((Account)localObject).sum;
		((Account)localObject).reset(4);
		l3 += ((Account)localObject).sum;
		arrayOfString3 = new String[2];
		arrayOfString3[0] = ((Account)localObject).name;
		arrayOfString3[1] = Convertor.sumToString(((Account)localObject).sum);
		localArrayList.add(arrayOfString3);
		long l4 = l1 + ((Account)localObject).sum;
		((Account)localObject).reset(5);
		String[] arrayOfString2 = new String[2];
		arrayOfString2[0] = ((Account)localObject).name;
		arrayOfString2[1] = Convertor.sumToString(((Account)localObject).sum);
		localArrayList.add(arrayOfString2);
		long l2 = ((Account)localObject).sum;
		((Account)localObject).reset(7);
		String[] arrayOfString4 = new String[2];
		arrayOfString4[0] = "保险价值";
		arrayOfString4[1] = Convertor.sumToString(((Account)localObject).sum);
		localArrayList.add(arrayOfString4);
		l4 += ((Account)localObject).sum;
		((Account)localObject).reset(10);
		arrayOfString4 = new String[2];
		arrayOfString4[0] = "投资市值";
		arrayOfString4[1] = Convertor.sumToString(((Account)localObject).sum);
		localArrayList.add(arrayOfString4);
		l4 += ((Account)localObject).sum;
		l3 += ((Account)localObject).sum;
		((Account)localObject).reset(12);
		arrayOfString4 = new String[2];
		arrayOfString4[0] = ((Account)localObject).name;
		arrayOfString4[1] = Convertor.sumToString(((Account)localObject).sum);
		localArrayList.add(arrayOfString4);
		l4 += ((Account)localObject).sum;
		localObject = new String[2];
		localObject[0] = "总资产";
		localObject[1] = Convertor.sumToString(l4);
		localArrayList.add(localObject);
		localObject = new String[2];
		localObject[0] = "金融资产";
		localObject[1] = Convertor.sumToString(l3);
		localArrayList.add(localObject);
		localObject = new String[2];
		localObject[0] = "净资产";
		localObject[1] = Convertor.sumToString(l4 - l2);
		localArrayList.add(localObject);
		paramFinanceAdapter.append(localArrayList);
	}





	
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Account
 * JD-Core Version:    0.6.0
 */
