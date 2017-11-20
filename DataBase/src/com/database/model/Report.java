package com.database.model;

//报表

import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.context.RuntimeInfo;
import com.database.util.Convertor;
import com.database.util.DBTool;
import com.database.util.MyDate;
import com.database.ListAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Report
{
  static final short INTEGER_LENGTH = 8;
  public static final short MonthBudget = 4;
  public static final short MonthReport = 2;
  public static final short NotExist = 5;
  public static int Report_Length = 0;
  public static final short YearBudget = 3;
  public static final short YearReport = 1;
  public String content = null;
  public short date = 0;
  public int id = 0;
  private byte[] report;
  public short type = 0;
  	
	static 
	{
		Report_Length = 2648;
	}

	/*
     * 创建
     * public static SQLiteDatabase create (SQLiteDatabase.CursorFactory factory) 
     */
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE report(id integer PRIMARY KEY ,type smallint,date smallint ,report blob,content varchar(80));");
		initData(db);
	}
	static void initData(SQLiteDatabase db)
	{
		MyDate localFDate = MyDate.now();
		insert((short)1, localFDate);
		insert((short)2, localFDate);
	}
	public static void insert(short type, MyDate dateinput)
	{
		int i = 13 * dateinput.getYear();
		if (type == 2)
			i += dateinput.getMonth();
		byte[] arrayOfByte = new byte[Report_Length];
		Convertor.fromLong(Account.getAccountSum(9) + Account.getAccountSum(10) - Account.getAccountSum(11), arrayOfByte, 2400);
		ContentValues values = new ContentValues();
		values.put("type", Short.valueOf(type));
		values.put("date", Integer.valueOf(i));
		values.put("report", arrayOfByte);
		values.put("content", "");
		DBTool.database.insert("report", "id", values);
	}
	
	//显示
	
	public String getDateString()
	{
		String str_date = this.date / 13 + "年";
	    
		if (this.type == 2)
	    {
	    	 str_date = this.date / 13+"/";
	      String str;
	      if (this.date % 13 >= 10)
	        str = "";
	      else
	        str = "0";
	      str_date =str_date+str+this.date % 13;
	    }
	    return str_date;
	  }
	public void fillReport(ListAdapter listadapter, int paramInt, List<Integer> index)
	{
	    listadapter.clear();
	    index.clear();
	    index.add(Integer.valueOf(2147483394));
	    ArrayList listdata = new ArrayList();
	    String[] tile;
	    String[] data;
	   // String 
	    if (paramInt != 10002)
	    {
	      if (paramInt != 10001)
	      {
	        if (paramInt != 10003)
	        {
	          tile = new String[2];
	          tile[0] = "科目";
	          tile[1] = "发生金额";
	          listdata.add(tile);
	          
	          String selection = "";
	          
	          if (paramInt == 0)
	        	  selection = "id<3 and ";
	          
	          Cursor localObject = KM.getRows(selection + "pid=" + paramInt, "rank");
	          localObject.moveToFirst();
	          while (!localObject.isAfterLast())
	          {
	            int j = localObject.getInt(0);
	            long l = getKmSum(localObject.getShort(0));
	            if ((j <= 2) || (l > 0L))
	            {
	              data  = new String[2];
	              data[0] = localObject.getString(1);
	              data[1] = Convertor.sumToString(l);
	              listdata.add(data);
	              int i;
	              if (l <= 0L)
	                i = -1;
	              else
	                i = j;
	              index.add(Integer.valueOf(i));
	            }
	            ((Cursor)localObject).moveToNext();
	          }
	          if (paramInt == 0)
	          {
	         //   if (Param.MODE == 2)
	          //  {
	              data = new String[2];
	              data[0] = "------";
	              data[1] = "--------";
	              listdata.add(data);
	              index.add(Integer.valueOf(-1));
	          //  }
	            tile = new String[2];
	            tile[0] = "净收入";
	            tile[1] = Convertor.sumToString(getKmSum(1) - getKmSum(2));
	            listdata.add(tile);
	            index.add(Integer.valueOf(-1));
	         //   if (Param.MODE != 2)
	        //    {
	              data = new String[2];
	              data[0] = "------";
	              data[1] = "--------";
	              listdata.add(data);
	              index.add(Integer.valueOf(-1));
	              if (RuntimeInfo.param.flag[11] == 0)
	              {
	            	  tile = new String[2];
	            	  tile[0] = "借贷报表";
	            	  tile[1] = "";
	                listdata.add(tile);
	                index.add(Integer.valueOf(10001));
	              }
	              if (RuntimeInfo.param.flag[10] == 0)
	              {
	                data = new String[2];
	                data[0] = "投资报表";
	                data[1] = ("收益：" + Convertor.sumToString(getKmSum(301)));
	                listdata.add(data);
	                index.add(Integer.valueOf(10002));
	              }
	              if (RuntimeInfo.param.flag[12] == 0)
	              {
	            	  data = new String[2];
	            	  data[0] = "资产报表";
	            	  data[1] = "";
	                listdata.add(data);
	                index.add(Integer.valueOf(10003));
	              }
	         //   }
	          }
	          localObject.close();
	          int[] layout_width = new int[3];
	          layout_width[0] = 20;
	          layout_width[1] = 120;
	          layout_width[2] = 180;
	          listadapter.setLayout(layout_width);
	          listadapter.append(listdata);
	        }
	        else
	        {
	     //     fillAssetReport(listadapter, index);
	        }
	      }
	      else
	      {
	 //       fillCreditReport(listadapter, index);
	      }
	    }
	    else
	    {
	//      fillInvestReport(listadapter, index);
	    }
	  }

	public long getKmSum(int kmpid)
	{
		long kmsum;
		if (this.type != 5)
		{
			if ((kmpid != 301) || (!isCurrentReport()))
				kmsum = Convertor.toLong(this.report, -8 + kmpid * 8, 8);
			else
				kmsum = Account.getAccountSum(10) + Account.getAccountSum(9) - Account.getAccountSum(11) - Convertor.toLong(this.report, 2400, 8);
		}
		else
			kmsum = 0L;
		return kmsum;
	}
	
	/*
	 * 修改数
	 */
	public static void modifyReportSum(MyDate dateinput, int kmid, long sum)
	{
		Report localReport = new Report(2, dateinput);
		localReport.addKmSum(kmid, sum);
		localReport.save();
		localReport = new Report(1, dateinput);
		localReport.addKmSum(kmid, sum);
		localReport.save();
	}
	
	public Report(int type, MyDate dateinput)
	{
		int data = 13 * dateinput.getYear();
		if (type == 2)
			data += dateinput.getMonth();
		Cursor localCursor = DBTool.database.query("report", getColumnString(), "type=" + type + " and date=" + data, null, null, null, null);
		if (localCursor.getCount() != 0)
		{
			localCursor.moveToFirst();
			reset(localCursor);
			localCursor.close();
		}
		else
		{
			this.type = 5;
			localCursor.close();
		}
	}
	
	public static String[] getColumnString()
	{
		String[] arrayOfString = new String[5];
		arrayOfString[0] = "id";
		arrayOfString[1] = "type";
		arrayOfString[2] = "date";
		arrayOfString[3] = "report";
		arrayOfString[4] = "content";
		return arrayOfString;
	}
	
	public void addKmSum(int kmid, long sum)
	{
		if (this.type != 5)
		{
			Convertor.fromLong(sum + getKmSum(kmid), this.report, -8 + kmid * 8);
			
			KM localKM = new KM(kmid);
			
			//
			if (localKM.pid != 3)
			{
				if ((localKM.pid != 302) && (localKM.pid != 303))
				{
		            Convertor.fromLong(sum + getKmSum(localKM.pid), this.report, -8 + 8 * localKM.pid);
					Convertor.fromLong(sum + getKmSum(2), this.report, 8);
				}
				else
				{
					Convertor.fromLong(sum + getKmSum(localKM.pid), this.report, -8 + 8 * localKM.pid);
				}
			}
			else
				Convertor.fromLong(sum + getKmSum(1), this.report, 0);
		}
	}
	
	
	
	public boolean isCurrentReport()
	{
		boolean bool = true;
		if (((this.type != 1) || (this.date != 13 * RuntimeInfo.param.lastdate.getYear())) && ((this.type != 2) || (this.date != 13 * RuntimeInfo.param.lastdate.getYear() + RuntimeInfo.param.lastdate.getMonth())))
			bool = false;
		return bool;
	}
	
	
	public void reset(Cursor paramCursor)
	{
		this.id = paramCursor.getShort(0);
		this.type = paramCursor.getShort(1);
		this.date = paramCursor.getShort(2);
		this.report = paramCursor.getBlob(3);
		this.content = paramCursor.getString(4);
	}

	public void save()
	{
		if (this.type != 5)
		{
			ContentValues localContentValues = new ContentValues();
			localContentValues.put("report", this.report);
			DBTool.database.update("report", localContentValues, "id=" + this.id, null);
		}
	}
	
	
	

	
	public static Report prevReport(int paramInt1, int paramInt2)
	{
		if (paramInt1 != 2)
		{
			if (paramInt1 == 1)
				paramInt2 -= 13;
		}
		else if (paramInt2 % 13 != 0)
			paramInt2--;
		else
			paramInt2 -= 2;
		return new Report(paramInt1, paramInt2);
	}
	
	public Report(int paramInt1, int paramInt2)
	{
		Cursor localCursor = DBTool.database.query("report", getColumnString(), "type=" + paramInt1 + " and date=" + paramInt2, null, null, null, null);
		if (localCursor.getCount() != 0)
		{
			localCursor.moveToFirst();
			reset(localCursor);
			localCursor.close();
		}
		else
		{
			this.type = 5;
			localCursor.close();
		}
	}
	
	}
/*  public Report()
  {
    this.type = 5;
  }

  public Report(int paramInt)
  {
    this.id = paramInt;
    Cursor localCursor = DBTool.database.query("report", getColumnString(), "id=" + paramInt, null, null, null, null);
    if (localCursor.getCount() != 0)
    {
      localCursor.moveToFirst();
      reset(localCursor);
      localCursor.close();
    }
    else
    {
      this.type = 5;
      localCursor.close();
    }
  }



  public Report(Cursor paramCursor)
  {
    reset(paramCursor);
  }




  public static List<String> getReportList(int paramInt, List<Integer> paramList)
  {
    paramList.clear();
    Cursor localCursor = getRows("type=" + paramInt, "date desc");
    Report localReport = new Report();
    ArrayList localArrayList = new ArrayList();
    localCursor.moveToFirst();
    while (!localCursor.isAfterLast())
    {
      localReport.reset(localCursor);
      localArrayList.add(localReport.getDateString());
      paramList.add(Integer.valueOf(localReport.id));
      localCursor.moveToNext();
    }
    localCursor.close();
    return localArrayList;
  }

  public static Cursor getRows(String paramString1, String paramString2)
  {
    return DBTool.database.query("report", getColumnString(), paramString1, null, null, null, paramString2);
  }


  public static void initReport(FDate paramFDate, Date paramDate)
  {
    int m = paramFDate.getYear();
    int i = paramFDate.getMonth();
    int j = 1900 + paramDate.getYear();
    int k = 1 + paramDate.getMonth();
    FDate localFDate = paramFDate.clone();
    while ((m != j) || (i != k))
    {
      new Report(2, localFDate).saveAccountSum();
      i++;
      if (i == 13)
      {
        new Report(1, localFDate).saveAccountSum();
        m++;
        i = 1;
        localFDate.setYear((short)m);
        insert(1, localFDate);
      }
      localFDate.setMonth((byte)i);
      insert(2, localFDate);
    }
  }





  public void addSingleKmSum(int paramInt, long paramLong)
  {
    Convertor.fromLong(paramLong + getKmSum(paramInt), this.report, -8 + paramInt * 8);
  }

  public void fillAssetReport(FinanceAdapter paramFinanceAdapter, List<Integer> paramList)
  {
    paramFinanceAdapter.clear();
    paramList.clear();
    paramList.add(Integer.valueOf(2147483394));
    ArrayList localArrayList = new ArrayList();
    Object localObject = new String[2];
    localObject[0] = "科目";
    localObject[1] = "发生金额";
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "资产报表:";
    localObject[1] = "";
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "资产购买";
    localObject[1] = Convertor.sumToString(getKmSum(319));
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "资产出售";
    localObject[1] = Convertor.sumToString(getKmSum(318));
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "租赁收入";
    localObject[1] = Convertor.sumToString(getKmSum(296));
    localArrayList.add(localObject);
    localObject = new int[3];
    localObject[0] = 20;
    localObject[1] = 120;
    localObject[2] = 180;
    paramFinanceAdapter.setLayout(localObject);
    paramFinanceAdapter.append(localArrayList);
  }

  public void fillCreditReport(FinanceAdapter paramFinanceAdapter, List<Integer> paramList)
  {
    paramFinanceAdapter.clear();
    paramList.clear();
    paramList.add(Integer.valueOf(2147483394));
    ArrayList localArrayList = new ArrayList();
    Object localObject = new String[2];
    localObject[0] = "科目";
    localObject[1] = "发生金额";
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "债务报表:";
    localObject[1] = "";
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "债务新增";
    localObject[1] = Convertor.sumToString(getKmSum(306));
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "债务减少";
    localObject[1] = Convertor.sumToString(getKmSum(307));
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "利息支出";
    localObject[1] = Convertor.sumToString(getKmSum(271));
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "债权报表:";
    localObject[1] = "";
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "债权新增";
    localObject[1] = Convertor.sumToString(getKmSum(305));
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "债权减少";
    localObject[1] = Convertor.sumToString(getKmSum(304));
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "利息收入";
    localObject[1] = Convertor.sumToString(getKmSum(292));
    localArrayList.add(localObject);
    localObject = new int[3];
    localObject[0] = 20;
    localObject[1] = 120;
    localObject[2] = 180;
    paramFinanceAdapter.setLayout(localObject);
    paramFinanceAdapter.append(localArrayList);
  }

  public void fillInvestReport(FinanceAdapter paramFinanceAdapter, List<Integer> paramList)
  {
    paramFinanceAdapter.clear();
    paramList.clear();
    paramList.add(Integer.valueOf(2147483394));
    ArrayList localArrayList = new ArrayList();
    Object localObject = new String[2];
    localObject[0] = "科目";
    localObject[1] = "发生金额";
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "投资报表";
    localObject[1] = "";
    localArrayList.add(localObject);
    if (RuntimeInfo.param.flag[36] == 0)
    {
      localObject = new String[2];
      localObject[0] = "基金申购";
      localObject[1] = Convertor.sumToString(getKmSum(313));
      localArrayList.add(localObject);
      localObject = new String[2];
      localObject[0] = "基金赎回";
      localObject[1] = Convertor.sumToString(getKmSum(312));
      localArrayList.add(localObject);
    }
    localObject = new String[2];
    localObject[0] = "股票投入";
    localObject[1] = Convertor.sumToString(getKmSum(315));
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "股票转出";
    localObject[1] = Convertor.sumToString(getKmSum(314));
    localArrayList.add(localObject);
    if (RuntimeInfo.param.flag[35] == 0)
    {
      localObject = new String[2];
      localObject[0] = "债券购买";
      localObject[1] = Convertor.sumToString(getKmSum(311));
      localArrayList.add(localObject);
      localObject = new String[2];
      localObject[0] = "债券出售";
      localObject[1] = Convertor.sumToString(getKmSum(310));
      localArrayList.add(localObject);
    }
    localObject = new String[2];
    localObject[0] = "其他支出";
    localObject[1] = Convertor.sumToString(getKmSum(317));
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "其他收回";
    localObject[1] = Convertor.sumToString(getKmSum(316));
    localArrayList.add(localObject);
    if (RuntimeInfo.param.flag[34] == 0)
    {
      localObject = new String[2];
      localObject[0] = "保险支出";
      localObject[1] = Convertor.sumToString(getKmSum(270));
      localArrayList.add(localObject);
      localObject = new String[2];
      localObject[0] = "保险收入";
      localObject[1] = Convertor.sumToString(getKmSum(295));
      localArrayList.add(localObject);
    }
    localObject = new String[2];
    localObject[0] = "------";
    localObject[1] = "";
    localArrayList.add(localObject);
    localObject = new String[2];
    localObject[0] = "本期收益";
    localObject[1] = Convertor.sumToString(getKmSum(301));
    localArrayList.add(localObject);
    localObject = new int[3];
    localObject[0] = 20;
    localObject[1] = 120;
    localObject[2] = 180;
    paramFinanceAdapter.setLayout(localObject);
    paramFinanceAdapter.append(localArrayList);
  }

  
  public long getAccountSum(int paramInt)
  {
    long l;
    if (this.type != 5)
      l = Convertor.toLong(this.report, 2552 + 8 * (paramInt - 1), 8);
    else
      l = 0L;
    return l;
  }

  


  public byte[] getReport()
  {
    return this.report;
  }

  public String getShortDateString()
  {
    String str = new StringBuilder().append(this.date / 13).toString().substring(2) + "/";
    if (this.type == 2)
    {
      if (this.date % 13 < 10)
        str = str + "0";
      str = str + this.date % 13;
    }
    return str;
  }


  HashMap<String, String> newHashMap(String paramString1, String paramString2)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("name", paramString1);
    localHashMap.put("sum", paramString2);
    return localHashMap;
  }


  void saveAccountSum()
  {
    for (int i = 1; i <= 12; i++)
      setAccountSum(i, Account.getAccountSum(i));
    setKmSum(301, (int)(Account.getAccountSum(9) + Account.getAccountSum(10) - Account.getAccountSum(11) - Convertor.toLong(this.report, 2400, 8)));
    save();
  }

  public void setAccountSum(int paramInt, long paramLong)
  {
    if (this.type != 5)
      Convertor.fromLong(paramLong, this.report, 2552 + 8 * (paramInt - 1));
  }

  public void setKmSum(int paramInt, long paramLong)
  {
    if (this.type != 5)
      Convertor.fromLong(paramLong, this.report, -8 + paramInt * 8);
  }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Report
 * JD-Core Version:    0.6.0
 */
