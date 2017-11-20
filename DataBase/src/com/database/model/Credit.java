package com.database.model;

//借贷

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.Convertor;
import com.database.util.DBTool;
import com.database.util.MyDate;
import com.database.ListAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Credit
{
  public static final int CONTENT_LENGTH = 40;
  public static final byte CREDIT_BORROW = 1;
  public static final String[] CREDIT_CLASS;
  public static final byte CREDIT_CLASS_BORROW = 1;
  public static final byte CREDIT_CLASS_CREDIT = 2;
  public static final byte CREDIT_CLASS_LEND = 0;
  public static final byte CREDIT_DEBJ = 4;
  public static final byte CREDIT_DEBX = 3;
  public static final byte CREDIT_LEND = 0;
  public static final byte CREDIT_NORMAL = 2;
  public static final String[] CREDIT_TYPE = new String[5];
  public static final int NAME_LENGTH = 8;
  public short bankid = 0;
  public String content;
  public MyDate date;
  public int deposit_id;
  public short flag = 0;
  public int id = 0;
  public String name;
  public short rate = 0;
  public Date real_date = null;
  public long sum = 0L;
  public short term = 0;
  public long totalsum = 0L;
  public short totalterm = 0;
  public short type = 0;

  static
  {
    CREDIT_CLASS = new String[4];
    CREDIT_TYPE[0] = "借出";
    CREDIT_TYPE[1] = "借入";
    CREDIT_TYPE[2] = "普通贷款";
    CREDIT_TYPE[3] = "等额本息";
    CREDIT_TYPE[4] = "等额本金";
    CREDIT_CLASS[0] = "借出";
    CREDIT_CLASS[1] = "借入";
    CREDIT_CLASS[2] = "贷款";
    CREDIT_CLASS[3] = "按揭";
  }
    //创建表
    public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE credit(id integer PRIMARY KEY ,type smallint,sum int ,rate smallint,term smallint,totalsum int ,totalterm smallint, name varchar(32), content varchar(80),date integer,bankid smallint,flag smallint,deposit_id int);");
	}
	
    public static void getAdapter(ListAdapter listadapter, List<Integer> index)
	{
		 index.clear();
		 listadapter.clear();
		 ArrayList listdata = new ArrayList();
		 
		 int[] arrayOfInt = new int[3];
		 arrayOfInt[0] = 2147483425;
		 arrayOfInt[1] = 2147483426;
		 arrayOfInt[2] = 2147483427;
		 
		 String[] arrayOfString1 = new String[3];
		 arrayOfString1[0] = "借出";
		 arrayOfString1[1] = "借入";
		 arrayOfString1[2] = "贷款";
		 
		 if (Param.MODE == 2)
		 {
		     arrayOfInt = new int[2];
		     arrayOfInt[0] = 2147483425;
		     arrayOfInt[1] = 2147483426;
		    
			 arrayOfString1 = new String[2];
		     arrayOfString1[0] = "借出";
		     arrayOfString1[1] = "借入";
		 }
		 
		 for (int i = 0; i < arrayOfInt.length; i++)
		 {
		     Cursor localCursor = getRows(getSql(arrayOfInt[i]), "");
		     if (localCursor.getCount() != 0)
		     {
		         long l = 0L;
		         
		         for (localCursor.moveToFirst();!localCursor.isAfterLast();  localCursor.moveToNext())
		         {
		             l += localCursor.getLong(2);
		           
		         }
		         String[] arrayOfString3 = new String[2];
		         arrayOfString3[0] = arrayOfString1[i];
		         arrayOfString3[1] = Convertor.sumToString(l);
		         listdata.add(arrayOfString3);
		         index.add(Integer.valueOf(arrayOfInt[i]));
		         
				 
		         for (localCursor.moveToFirst();!localCursor.isAfterLast(); localCursor.moveToNext())
		         {
		             String[] arrayOfString2 = new String[2];
		             arrayOfString2[0] = localCursor.getString(7);
		             arrayOfString2[1] = Convertor.sumToString(localCursor.getLong(2));
		             listdata.add(arrayOfString2);
		             index.add(Integer.valueOf(localCursor.getInt(0)));
		         }
		         localCursor.close();
		     }
		     else
		     {
		         localCursor.close();
		     }
		 }
		 listadapter.append(listdata);
		 listadapter.notifyDataSetChanged();
	}
	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("credit", getColumnString(), paramString1, null, null, null, paramString2);
	}
	public static String[] getColumnString()
	{
		String[] arrayOfString = new String[13];
		arrayOfString[0] = "id";
		arrayOfString[1] = "type";
		arrayOfString[2] = "sum";
		arrayOfString[3] = "rate";
		arrayOfString[4] = "term";
		arrayOfString[5] = "totalsum";
		arrayOfString[6] = "totalterm";
		arrayOfString[7] = "name";
		arrayOfString[8] = "content";
		arrayOfString[9] = "date";
		arrayOfString[10] = "bankid";
		arrayOfString[11] = "flag";
		arrayOfString[12] = "deposit_id";
		return arrayOfString;
	}
	public static String getSql(int paramInt)
	{
		String str;
		switch (paramInt)
		{
			default:
				str = "";
				break;
			case 2147483425:
				str = "flag>=0 and type=0";
				break;
			case 2147483426:
				str = "flag>=0 and type=1";
				break;
			case 2147483427:
				str = "flag>=0 and type>1";
		}
		return str;
	}
	public static List<String> getCreditTypeList()
    {
        ArrayList localArrayList = new ArrayList();
        int j = CREDIT_TYPE.length;
        if (Param.MODE == 2)
            j = 2;
        for (int i = 0; i < j; i++)
            localArrayList.add(CREDIT_TYPE[i]);
        return localArrayList;
    }
	//增加借出
	public static String insertLend(long paramLong, int paramInt, String paramString1, String paramString2, Date paramDate)
	{
		String str ;
		if (paramString1.length() != 0)
		{

			Deposit localDeposit = new Deposit(paramInt);
			if (!localDeposit.isOverSum(paramLong))
			{	
				localDeposit.addSum(-paramLong);

				int i = insertRow(0, paramLong, 0, 0, 0L, 0, 1, paramString1, paramString2, paramDate, 0);
				if (paramInt >= 0)
				{
					CreditAudit.insert(paramLong, 1, i, paramInt, Virement.insert(14, paramLong, paramInt, i, paramDate), paramDate, paramString2);
					Report.modifyReportSum(new MyDate(paramDate), 305, paramLong);
				}
				Account.addAccountSum(4, paramLong);
				str = "操作成功完成！";
		        return str;
		//		break label127;
			}
			else
			{
			    str = "余额不足！";
		    }
		}
		else
		{
			str = "名字不允许为空！";
		}
		return str;
	}
	//增加借入
	public static String insertBorrow(long paramLong, int paramInt, String paramString1, String paramString2, Date paramDate)
    {
        String str;
        if (paramString1.length() != 0)
        {
            int i = insertRow(1, paramLong, 0, 0, 0L, 0, 1, paramString1, paramString2, paramDate, 0);
            if (paramInt > 0)
            {
                new Deposit(paramInt).addSum(paramLong);
                CreditAudit.insert(paramLong, 2, i, paramInt, Virement.insert(15, paramLong, paramInt, i, paramDate), paramDate, paramString2);
                Report.modifyReportSum(new MyDate(paramDate), 306, paramLong);
            }
            Account.addAccountSum(5, paramLong);
            str = "操作成功完成！";
        }
        else
        {
            str = "名字不允许为空！";
        }
        return str;
    }



	
	static int insertRow(int paramInt1, long paramLong1, int paramInt2, int paramInt3, long paramLong2, int paramInt4, int paramInt5, String paramString1, String paramString2, Date paramDate, int paramInt6)
    {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("insert into ").append("credit").append(" values(null,").append(paramInt1).append(",").append(paramLong1).append(",").append(paramInt2).append(",").append(paramInt3).append(",").append(paramLong2).append(",").append(paramInt4).append(",'").append(paramString1).append("','").append(paramString2).append("',").append(paramDate.getTime()).append(",").append(paramInt6).append(",0,").append(paramInt5).append(");");
        DBTool.database.execSQL(localStringBuffer.toString());
        return DBTool.getMaxId("credit");
    }
	public Credit(int paramInt)
	{
		Cursor localCursor = DBTool.database.query("credit", getColumnString(), "id=" + paramInt, null, null, null, null);
		localCursor.moveToFirst();
		reset(localCursor);
		localCursor.close();
	}
	public void reset(Cursor paramCursor)
	{
		this.id = paramCursor.getInt(0);
		this.type = paramCursor.getShort(1);
		this.sum = paramCursor.getLong(2);
		this.rate = paramCursor.getShort(3);
		this.term = paramCursor.getShort(4);
		this.totalsum = paramCursor.getLong(5);
		this.totalterm = paramCursor.getShort(6);
		this.name = paramCursor.getString(7);
		this.content = paramCursor.getString(8);
		this.real_date = new Date(paramCursor.getLong(9));
		this.date = new MyDate(this.real_date);
		this.bankid = paramCursor.getShort(10);
		this.flag = paramCursor.getShort(11);
		this.deposit_id = paramCursor.getInt(12);
		if (this.deposit_id == 0)
			this.deposit_id = 1;
	}
	public String getTypeName()
	{
		return CREDIT_TYPE[this.type];
	}
	
	public String modifyBorrowOrLend(long paramLong, String paramString1, String paramString2, Date paramDate)
	{
		String str;
		if (paramLong >= 0L)
		{
			if (paramString1.length() != 0)
			{
				setSum(paramLong);
				this.name = paramString1;
				this.content = paramString2;
				if (!this.date.equals(paramDate))
				{
					this.real_date = paramDate;
					this.date = new MyDate(paramDate);
				}
				save();
				str = "操作成功完成！";
			}
			else
			{
				str = "名字不允许为空！";
			}
		}
		else
			str = "金额错误！";
		return str;
	}
	
	void setSum(long paramLong)
	{
		if (this.type != 0)
			Account.addAccountSum(5, paramLong - this.sum);
		else
			Account.addAccountSum(4, paramLong - this.sum);
		this.sum = paramLong;
	}
	
	public void save()
	{
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("update ").append("credit").append(" set").append(" type=").append(this.type).append(",sum=").append(this.sum).append(",rate=").append(this.rate).append(",term=").append(this.term).append(",totalsum=").append(this.totalsum).append(",totalterm=").append(this.totalterm).append(",name='").append(this.name).append("',content='").append(this.content).append("',date=").append(this.real_date.getTime()).append(",bankid=").append(this.bankid).append(",flag=").append(this.flag).append(",deposit_id=").append(this.deposit_id).append(" where id=").append(this.id);
		DBTool.database.execSQL(localStringBuffer.toString());
	}
	
	public String delete()
	{
		String str;
		if (!hasAudit())
		{
			addSum(-this.sum);
			this.flag = -1;
			save();
			str = "操作成功完成！";
		}
		else
		{
			str = "帐户下有流水,不能用*删除!";
		}
		return str;
	}
	
	void addSum(long paramLong)
	{
		if (this.type != 0)
			Account.addAccountSum(5, paramLong);
		else
			Account.addAccountSum(4, paramLong);
		
		this.sum = (paramLong + this.sum);
	}
	
	public boolean hasAudit()
	{
		Cursor localCursor = CreditAudit.getRows("credit_id=" + this.id, null);
		boolean bool;
		if (localCursor.getCount() != 0)
			bool = false;
		else
			bool = true;
		localCursor.close();
		return bool;
	}
    //借出还款
	public String lendReturn(long paramLong1, long paramLong2, int paramInt, Date paramDate, String paramString)
	{
		String str;
		if ((paramLong1 >= 0L) && (paramLong2 >= 0L) && (paramLong1 + paramLong2 > 0L))
		{
			if (this.sum >= paramLong1)
			{
				new Deposit(paramInt).addSum(paramLong1 + paramLong2);
				int i;
				if (paramLong1 > 0L)
				{
					addSum(-paramLong1);
					if (this.sum == 0L)
						this.flag = -1;
					save();
					i = Virement.insert(17, paramLong1, paramInt, this.id, paramDate);
					CreditAudit.insert(paramLong1, 6, this.id, paramInt, i, paramDate, paramString);
					Report.modifyReportSum(new MyDate(paramDate), 304, paramLong1);
				}
				if (paramLong2 > 0L)
				{
					i = Audit.insertSystemAudit(292, paramLong2, paramInt, Virement.insert(20, paramLong2, paramInt, this.id, paramDate), paramDate, "借出利息:" + this.name);
					CreditAudit.insert(paramLong2, 12, this.id, paramInt, i, paramDate, paramString);
				}
				str = "操作成功完成！";
			}
			else
			{
				str = "余额不足！";
			}
		}
		else
			str = "金额错误！";
		return str;
	}
	//借入还款
	public String borrowReturn(long paramLong1, long paramLong2, int paramInt, Date paramDate, String paramString)
	{
		String str;
		if ((paramLong1 >= 0L) && (paramLong2 >= 0L) && (paramLong1 + paramLong2 > 0L))
		{
			Deposit localDeposit = new Deposit(paramInt);
			if ((!localDeposit.isOverSum(paramLong1 + paramLong2)) && (this.sum >= paramLong1))
			{
				localDeposit.addSum(-paramLong1 - paramLong2);
				int i;
				if (paramLong1 > 0L)
				{
					addSum(-paramLong1);
					if (this.sum == 0L)
						this.flag = -1;
					save();
					i = Virement.insert(18, paramLong1, paramInt, this.id, paramDate);
					CreditAudit.insert(paramLong1, 5, this.id, paramInt, i, paramDate, paramString);
					Report.modifyReportSum(new MyDate(paramDate), 307, paramLong1);
				}
				if (paramLong2 > 0L)
				{
					i = Audit.insertSystemAudit(271, paramLong2, paramInt, Virement.insert(21, paramLong2, paramInt, this.id, paramDate), paramDate, "借入利息:" + this.name);
					CreditAudit.insert(paramLong2, 11, this.id, paramInt, i, paramDate, paramString);
				}
				str = "操作成功完成！";
			}
			else
			{
				str = "余额不足！";
			}
		}
		else
		{
			str = "金额错误！";
		}
		return str;
	}
	//借出续借
	public String lendMore(long paramLong, int paramInt, Date paramDate, String paramString)
	{
		Deposit localDeposit = new Deposit(paramInt);
		String str;
		if (!localDeposit.isOverSum(paramLong))
		{
			localDeposit.addSum(-paramLong);
			addSum(paramLong);
			save();
			int i = Virement.insert(14, paramLong, paramInt, this.id, paramDate);
			CreditAudit.insert(paramLong, 1, this.id, paramInt, i, paramDate, paramString);
			Report.modifyReportSum(new MyDate(paramDate), 305, paramLong);
			str = "操作成功完成！";
		}
		else
		{
			str = "余额不足！";
		}
		return str;
	}

	//借入续借
	public String borrowMore(long paramLong, int paramInt, Date paramDate, String paramString)
	{
		new Deposit(paramInt).addSum(paramLong);
		addSum(paramLong);
		save();
		int i = Virement.insert(15, paramLong, paramInt, this.id, paramDate);
		CreditAudit.insert(paramLong, 2, this.id, paramInt, i, paramDate, paramString);
		Report.modifyReportSum(new MyDate(paramDate), 306, paramLong);
		return "操作成功完成！";
	}
	//借出坏帐
	public String lendBad(Date paramDate)
	{
		int i = Audit.insertSystemAudit(274, this.sum, 0, 0, paramDate, "借出坏账:" + this.name);
		CreditAudit.insert(this.sum, 9, this.id, this.deposit_id, i, paramDate, "借出坏账:" + this.name);
		Report.modifyReportSum(new MyDate(paramDate), 304, this.sum);
		addSum(-this.sum);
		this.flag = -1;
		save();
		return "操作成功完成！";
	}
	//借入坏帐
	public String borrowBad(Date paramDate)
	{
		int i = Audit.insertSystemAudit(293, this.sum, 0, 0, paramDate, "借入坏账:" + this.name);
		CreditAudit.insert(this.sum, 8, this.id, this.deposit_id, i, paramDate, "借入坏账:" + this.name);
		Report.modifyReportSum(new MyDate(paramDate), 307, this.sum);
		addSum(-this.sum);
		this.flag = -1;
		save();
		return "操作成功完成！";
	}
	
	public static List<String> getCreditList(String paramString, List<Integer> index)
	{
		index.clear();
		ArrayList localArrayList = new ArrayList();
		Cursor localCursor = getRows(paramString, "id desc");

		for (localCursor.moveToFirst();!localCursor.isAfterLast();localCursor.moveToNext())
		{
			localArrayList.add(localCursor.getString(7));
			index.add(Integer.valueOf(localCursor.getInt(0)));

		}
		localCursor.close();
		return localArrayList;
	}
	public static List<String> getCreditAuditKmList()
	{
		ArrayList localArrayList = new ArrayList();
		localArrayList.add("还款");
		localArrayList.add("续借");
	//	localArrayList.add("坏帐");
		return localArrayList;
	}
	
    }
/*  public Credit()
  {
  }



    public static String insertCredit(int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString1, String paramString2, Date paramDate)
    {
        String str;
        if (paramString1.length() != 0)
        {
            int i = insertRow(paramInt1, paramLong, paramInt3, paramInt2, paramLong, paramInt2, paramInt5, paramString1, paramString2, paramDate, paramInt4);
            Account.addAccountSum(5, paramLong);
            if (paramInt5 > 0)
            {
                new Deposit(paramInt5).addSum(paramLong);
                CreditAudit.insert(paramLong, 3, i, paramInt5, Virement.insert(16, paramLong, paramInt5, i, paramDate), paramDate, paramString2);
        //        Report.modifyReportSum(new MyDate(paramDate), 307, paramLong);
            }
            str = "操作成功完成！";
        }
        else
        {
            str = "名字不允许为空！";
        }
        return str;
    }


  public static List<String> getCreditAjTypeList()
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 3; i < CREDIT_TYPE.length; i++)
      localArrayList.add(CREDIT_TYPE[i]);
    return localArrayList;
  }



  public static long getNextMonthPay(int paramInt1, long paramLong, int paramInt2, int paramInt3)
  {
    long l;
    if (paramInt1 != 3)
    {
      if (paramInt1 != 4)
        l = 0L;
      else
        l = monthPayOfCreditDebj(paramLong, paramInt2, paramInt3);
    }
    else
      l = monthPayOfCreditDebx(paramLong, paramInt2, paramInt3);
    return l;
  }




  public static String getTypeName(int paramInt)
  {
    return CREDIT_TYPE[paramInt];
  }

  public static String getTypeShortName(int paramInt)
  {
    String[] arrayOfString = CREDIT_CLASS;
    if (paramInt > 3)
      paramInt = 3;
    return arrayOfString[paramInt];
  }




  public static String insertOldCredit(int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString1, String paramString2, Date paramDate)
  {
    String str;
    if (paramString1.length() != 0)
    {
      insertRow(paramInt1, paramLong, paramInt3, paramInt2, paramLong, paramInt2, paramInt5, paramString1, paramString2, paramDate, paramInt4);
      Account.addAccountSum(5, paramLong);
      str = "操作成功完成！";
    }
    else
    {
      str = "名字不允许为空！";
    }
    return str;
  }

  public static String insertOldCreditAj(int paramInt1, long paramLong1, long paramLong2, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString1, String paramString2, Date paramDate)
  {
    String str;
    if (paramString1.length() != 0)
    {
      insertRow(paramInt1, paramLong1, paramInt4, paramInt2, paramLong2, paramInt3, paramInt6, paramString1, paramString2, paramDate, paramInt5);
      Account.addAccountSum(5, paramLong1);
      DBTool.getMaxId("credit");
      str = "操作成功完成！";
    }
    else
    {
      str = "名字不允许为空！";
    }
    return str;
  }


  public static long monthPayOfCreditDebj(long paramLong, int paramInt1, int paramInt2)
  {
    double d = paramInt1 / 120000.0D;
    return ()(5.0D + 10.0D * (paramLong / paramInt2 + d * paramLong)) / 10L;
  }

  public static long monthPayOfCreditDebx(long paramLong, int paramInt1, int paramInt2)
  {
    double d2 = paramInt1 / 120000.0D;
    double d1 = 1.0D + d2;
    for (int i = 1; i < paramInt2; i++)
      d1 *= (1.0D + d2);
    return ()(5.0D + 10.0D * (d1 * (d2 * paramLong) / (d1 - 1.0D))) / 10L;
  }

  static double monthPayOfCreditDebxDouble(long paramLong, int paramInt1, int paramInt2)
  {
    double d2 = paramInt1 / 120000.0D;
    double d1 = 1.0D + d2;
    for (int i = 1; i < paramInt2; i++)
      d1 *= (1.0D + d2);
    return d1 * (d2 * paramLong) / (d1 - 1.0D);
  }

  public static long totalInterestOfCredit(int paramInt1, long paramLong, int paramInt2, int paramInt3)
  {
    long l;
    if (paramInt1 != 3)
    {
      if (paramInt1 != 4)
        l = 0L;
      else
        l = totalInterestOfCreditDebj(paramLong, paramInt2, paramInt3);
    }
    else
      l = totalInterestOfCreditDebx(paramLong, paramInt2, paramInt3);
    return l;
  }

  public static long totalInterestOfCreditDebj(long paramLong, int paramInt1, int paramInt2)
  {
    return ()(5.0D + 10.0D * (paramInt1 / 120000.0D * (paramLong * (paramInt2 + 1) / 2L))) / 10L;
  }

  public static long totalInterestOfCreditDebx(long paramLong, int paramInt1, int paramInt2)
  {
    return ()(0.5D + monthPayOfCreditDebxDouble(paramLong, paramInt1, paramInt2) * paramInt2) - paramLong;
  }






  public String checkout()
  {
    String str;
    if (this.sum <= 0L)
    {
      this.flag = -1;
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "帐户余额不为0,不能注销！";
    }
    return str;
  }

  public String creditBad(Date paramDate)
  {
    int i = Audit.insertSystemAudit(293, this.sum, 0, 0, paramDate, "贷款坏账:" + this.name);
    CreditAudit.insert(this.sum, 10, this.id, this.deposit_id, i, paramDate, "贷款坏账:" + this.name);
    addSum(-this.sum);
    this.flag = -1;
    save();
    Report.modifyReportSum(new FDate(paramDate), 307, this.sum);
    return "操作成功完成！";
  }

  public String creditReturn(long paramLong1, long paramLong2, int paramInt, Date paramDate, String paramString)
  {
    String str;
    if ((paramLong1 >= 0L) && (paramLong2 >= 0L) && (paramLong1 + paramLong2 > 0L))
    {
      Deposit localDeposit = new Deposit(paramInt);
      if (!localDeposit.isOverSum(paramLong1 + paramLong2))
      {
        localDeposit.addSum(-paramLong1 - paramLong2);
        int i;
        if (this.type != 3)
        {
          if (this.type != 4)
          {
            if (paramLong1 > 0L)
            {
              i = Virement.insert(19, paramLong1, paramInt, this.id, paramDate);
              CreditAudit.insert(paramLong1, 7, this.id, paramInt, i, paramDate, paramString);
            }
            if (paramLong2 > 0L)
            {
              i = Audit.insertSystemAudit(271, paramLong2, paramInt, Virement.insert(22, paramLong2, paramInt, this.id, paramDate), paramDate, "贷款利息:" + this.name);
              CreditAudit.insert(paramLong2, 13, this.id, paramInt, i, paramDate, paramString);
            }
          }
          else
          {
            if (paramLong1 > 0L)
            {
              i = Audit.insertSystemAudit(268, paramLong1, paramInt, Virement.insert(19, paramLong1, paramInt, this.id, paramDate), paramDate, "还贷款本金:" + this.name);
              CreditAudit.insert(paramLong1, 7, this.id, paramInt, i, paramDate, paramString);
            }
            if (paramLong2 > 0L)
            {
              i = Audit.insertSystemAudit(271, paramLong2, paramInt, Virement.insert(22, paramLong2, paramInt, this.id, paramDate), paramDate, "贷款利息:" + this.name);
              CreditAudit.insert(paramLong2, 13, this.id, paramInt, i, paramDate, paramString);
            }
          }
        }
        else
        {
          int j = Virement.insert(19, paramLong1 + paramLong2, paramInt, this.id, paramDate);
          i = Audit.insertSystemAudit(268, paramLong1, paramInt, j, paramDate, "还贷款:" + this.name);
          CreditAudit.insert(paramLong1, 7, this.id, paramInt, i, paramDate, paramString);
          i = Audit.insertSystemAudit(271, paramLong2, paramInt, j, paramDate, "贷款利息:" + this.name);
          CreditAudit.insert(paramLong2, 13, this.id, paramInt, i, paramDate, paramString);
        }
        Report.modifyReportSum(new FDate(paramDate), 307, paramLong1);
        if (this.type != 2)
          this.term = (-1 + this.term);
        addSum(-paramLong1);
        if (this.sum == 0L)
          this.flag = -1;
        save();
        str = "操作成功完成！";
      }
      else
      {
        str = "余额不足！";
      }
    }
    else
    {
      str = "金额错误！";
    }
    return str;
  }


  public Date getAjReturnDateOfNextmonth()
  {
    FDate localFDate = FDate.now();
    if (localFDate.getMonth() >= 12)
    {
      localFDate.setYear(1 + localFDate.getYear());
      localFDate.setMonth(1);
    }
    else
    {
      localFDate.setMonth(1 + localFDate.getMonth());
    }
    localFDate.setDay(this.date.getDay());
    return localFDate.getDate();
  }

  public Date getNextAjReturnDate()
  {
    FDate localFDate = FDate.now();
    if (localFDate.getDay() >= this.date.getDay())
      if (localFDate.getMonth() >= 12)
      {
        localFDate.setYear(1 + localFDate.getYear());
        localFDate.setMonth(1);
      }
      else
      {
        localFDate.setMonth(1 + localFDate.getMonth());
      }
    localFDate.setDay(this.date.getDay());
    return localFDate.getDate();
  }

  public long getNextMonthPay()
  {
    long l;
    if (this.type != 3)
    {
      if (this.type != 4)
        l = 0L;
      else
        l = monthPayOfCreditDebj(this.sum, this.rate, this.term);
    }
    else
      l = monthPayOfCreditDebx(this.sum, this.rate, this.term);
    return l;
  }





  public String modifyCreditAj(int paramInt1, long paramLong1, long paramLong2, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, String paramString1, String paramString2, Date paramDate)
  {
    String str;
    if (paramLong1 >= 0L)
    {
      if (paramString1.length() != 0)
      {
        setSum(paramLong1);
        this.totalsum = paramLong2;
        this.type = (short)paramInt1;
        this.name = paramString1;
        this.term = (short)paramInt2;
        this.totalterm = (short)paramInt3;
        this.rate = (short)paramInt4;
        this.bankid = (short)paramInt5;
        this.content = paramString2;
        int i;
        if (!paramBoolean)
          i = 0;
        else
          i = 1;
        this.flag = (short)i;
        if (!this.date.equals(paramDate))
        {
          this.real_date = paramDate;
          this.date = new FDate(paramDate);
        }
        this.deposit_id = paramInt6;
        save();
        str = "操作成功完成！";
      }
      else
      {
        str = "名字不允许为空！";
      }
    }
    else
      str = "金额错误！";
    return str;
  }

  public String modifyCreditNormal(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, String paramString2, Date paramDate)
  {
    String str;
    if (paramLong >= 0L)
    {
      if (paramString1.length() != 0)
      {
        setSum(paramLong);
        this.name = paramString1;
        short s = (short)paramInt1;
        this.term = s;
        this.totalterm = s;
        this.rate = (short)paramInt2;
        this.bankid = (short)paramInt3;
        this.content = paramString2;
        if (!this.date.equals(paramDate))
        {
          this.real_date = paramDate;
          this.date = new FDate(paramDate);
        }
        this.deposit_id = paramInt4;
        save();
        str = "操作成功完成！";
      }
      else
      {
        str = "名字不允许为空！";
      }
    }
    else
      str = "金额错误！";
    return str;
  }

  public long monthPayCostOfCreditAj()
  {
    long l;
    if (this.type != 3)
    {
      if (this.type != 4)
        l = 0L;
      else
        l = monthPayCostOfCreditDebj();
    }
    else
      l = monthPayCostOfCreditDebx();
    return l;
  }

  public long monthPayCostOfCreditDebj()
  {
    return (5L + ()(10.0D * this.sum / this.term)) / 10L;
  }

  public long monthPayCostOfCreditDebx()
  {
    return getNextMonthPay() - monthPayInterestOfCreditAj();
  }

  public long monthPayInterestOfCreditAj()
  {
    return (5L + ()(this.sum * this.rate / 12.0D) / 1000L) / 10L;
  }





  public long totalInterestOfCredit()
  {
    return totalInterestOfCredit(this.type, this.totalsum, this.rate, this.totalterm);
  }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Credit
 * JD-Core Version:    0.6.0
 */
