package com.database.model;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.Convertor;
import com.database.util.DBTool;
import com.database.util.MyDate;
import com.database.ListAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Deposit
{
 /* public static final short Credit12 = 19;

  public static final short TYPE_SOCIAL_INSRUANCE = 7;*/
  public short bankid = 0;
  public short billday = 0;
  public long billsum = 0L;
  String cardno = null;
  public MyDate date = null;
  public int deposit_id;
  public short flag = 0;
  public short freeofposnumber = 0;
  public int id = 0;
  public long maxsum = 0L;
  public String name = null;
  public short posnumber = 0;
  public int rank = 0;
  public short rate = 0;
  public Date real_date = null;
  public short returnday = 0;
  public long sum = 0L;
  public short type = 0;

    //
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE deposit(id integer PRIMARY KEY AUTOINCREMENT, name varchar(20),type smallint,sum int,maxsum int,date integer,bankid smallint,rank int ,flag smallint,cardno varchar(32),rate smallint,billday smallint,returnday smallint,freeofposnumber smallint,posnumber smallint, billsum int,deposit_id int);");
		initData(db);
	}
	//
	static void initData(SQLiteDatabase db)
	{
		db.execSQL("insert into deposit values(null,'现金',12,50000000,0,0,0,0,0,'',0,0,0,0,0,0,0)");
		Account.addAccountSum(1, 50000000L);
	}
	
	//显示列表
	public static void getAdapter(ListAdapter adapter, List<Integer> list)
	{
		list.clear();
		adapter.clear();
		
		ArrayList localArrayList = new ArrayList();
		String[] localObject = new String[2];
		localObject[0] = "现金";
		localObject[1] = Convertor.sumToString(new Deposit(1).sum);
		localArrayList.add(localObject);
		list.add(Integer.valueOf(2147483413));
		
		int[] layout = new int[2];
		layout[0] = 2147483414;
	//	layout[1] = 2147483415;
		layout[1] = 2147483416;
	//	layout[2] = 2147483425;
	//	layout[4] = 2147483417;
//		layout[3] = 2147483424;
		
		String[] deposittype = new String[2];
		deposittype[0] = "活期/银行卡";
//		deposittype[1] = "信用卡";
		deposittype[1] = "定期/一本通";
	//	deposittype[2] = "电子支付";
//		deposittype[4] = "购物券";
//		deposittype[3] = "社保帐户";
		
		for (int i = 0; i < layout.length; i++)
		{
			Cursor localCursor = getRows(getSql(layout[i]), "rank asc");
			if (localCursor.getCount() != 0)
			{
				long l1 = 0L;
				
				for (localCursor.moveToFirst();!localCursor.isAfterLast();localCursor.moveToNext())
				{
					long l3;
					if ((localCursor.getShort(2) != 25) && (localCursor.getShort(2) != 26))
						l3 = localCursor.getLong(3);
					else
						l3 = getTotalSum(localCursor.getInt(0));
					l1 += l3;
					
				}
				
				String[] arrayOfString3 = new String[2];
				arrayOfString3[0] = deposittype[i];
				arrayOfString3[1] = Convertor.sumToString(l1);
				localArrayList.add(arrayOfString3);
				list.add(Integer.valueOf(layout[i]));
				
				
				for (localCursor.moveToFirst();!localCursor.isAfterLast();localCursor.moveToNext())
				{
					int j = localCursor.getShort(2);
					String[] arrayOfString2;
					long l2 = 0L;
					if ((j != 1) && (j != 25) && (j != 8) && (j != 9) && (j != 10) && (j != 24))
					{
						arrayOfString2 = new String[3];
						arrayOfString2[0] = localCursor.getString(1);
						arrayOfString2[1] = getDepositTypeName(localCursor.getShort(2));
						if ((j != 25) && (j != 26))
							l2 = localCursor.getLong(3);
						else
							l2 = getTotalSum(localCursor.getInt(0));
						arrayOfString2[2] = Convertor.sumToString(l2);
						localArrayList.add(arrayOfString2);
					}
					else
					{
						String[] arrayOfString4 = new String[2];
						arrayOfString4[0] = localCursor.getString(1);
						if ((l2 != 25) && (l2 != 26))
							l2 = localCursor.getLong(3);
						else
							l2 = getTotalSum(localCursor.getInt(0));
						arrayOfString4[1] = Convertor.sumToString(l2);
						localArrayList.add(arrayOfString4);
					}
					list.add(Integer.valueOf(localCursor.getInt(0)));
					
				}
				localCursor.close();
			}
			else
			{
				localCursor.close();
			}
		}
	    adapter.append(localArrayList);
		adapter.notifyDataSetChanged();
	}
	
	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("deposit", getColumnString(), paramString1, null, null, null, paramString2);
	}
	public static String[] getColumnString()
	{
		String[] arrayOfString = new String[17];
		arrayOfString[0] = "id";
		arrayOfString[1] = "name";
		arrayOfString[2] = "type";
		arrayOfString[3] = "sum";
		arrayOfString[4] = "maxsum";
		arrayOfString[5] = "date";
		arrayOfString[6] = "bankid";
		arrayOfString[7] = "rank";
		arrayOfString[8] = "flag";
		arrayOfString[9] = "cardno";
		arrayOfString[10] = "rate";
		arrayOfString[11] = "billday";
		arrayOfString[12] = "returnday";
		arrayOfString[13] = "freeofposnumber";
		arrayOfString[14] = "posnumber";
		arrayOfString[15] = "billsum";
		arrayOfString[16] = "deposit_id";
		return arrayOfString;
	}
	
	public Deposit(int paramInt)
	{
		this.id = paramInt;
		Cursor localCursor = DBTool.database.query("deposit", getColumnString(), "id=" + paramInt, null, null, null, null);
		localCursor.moveToFirst();
		reset(localCursor);
		localCursor.close();
	}
	void reset(int paramInt)
	{
		this.id = paramInt;
		Cursor localCursor = DBTool.database.query("deposit", getColumnString(), "id=" + paramInt, null, null, null, null);
		localCursor.moveToFirst();
		reset(localCursor);
		localCursor.close();
	}

	public void reset(Cursor paramCursor)
	{
		this.id = paramCursor.getInt(0);
		this.name = paramCursor.getString(1);
		this.type = paramCursor.getShort(2);
		this.sum = paramCursor.getLong(3);
		this.maxsum = paramCursor.getLong(4);
		this.real_date = new Date(paramCursor.getLong(5));
		this.date = new MyDate(this.real_date);
		this.bankid = paramCursor.getShort(6);
		this.rank = paramCursor.getInt(7);
		this.flag = paramCursor.getShort(8);
		this.cardno = paramCursor.getString(9);
		this.rate = paramCursor.getShort(10);
		this.billday = paramCursor.getShort(11);
		this.returnday = paramCursor.getShort(12);
		this.freeofposnumber = paramCursor.getShort(13);
		this.posnumber = paramCursor.getShort(14);
		this.billsum = paramCursor.getLong(15);
		this.deposit_id = paramCursor.getInt(16);
		if (this.deposit_id == 0)
			this.deposit_id = 1;
	}

	

	public static String getSql(int paramInt)
	{
		String str;
		switch (paramInt)
		{
	
			default:
				str = "";
				break;
			case 2147483413://现金
				str = "type=12";
				break;
			case 2147483414://活期
				str = "flag>-1 and (type=1 or type=25)";
				break;
		//	case 2147483415:
		//		str = "flag>-1 and type=8";
		//		break;
			case 2147483416://定期
				str = "flag>-1 and ((type>=2 and type<=7) or (type>=13 and type<=15) or type=26)";
				break;
		/*	case 2147483417:
				str = "flag>-1 and type=9";
				break;
			case 2147483424:
				str = "flag>-1 and type=24";
				break;
			case 2147483425://电子帐户
				str = "flag>-1 and type=10";
		        break;*/
		}
		return str;
	}
	
	public static long getTotalSum(int paramInt)
	{
		Cursor localCursor = getRows("(flag=2 and deposit_id=" + paramInt + ") or id=" + paramInt, null);
		long l = 0L;
		localCursor.moveToFirst();
		while (!localCursor.isAfterLast())
		{
			l += localCursor.getLong(3);
			localCursor.moveToNext();
		}
		return l;
	}
	
	public static String getDepositTypeName(int paramInt)
	{
		String[] arrayOfString = new String[26];
		arrayOfString[0] = "活期";
		arrayOfString[1] = "定期3月";
		arrayOfString[2] = "定期6月";
		arrayOfString[3] = "定期1年";
		arrayOfString[4] = "定期2年";
		arrayOfString[5] = "定期3年";
		arrayOfString[6] = "定期5年";
		arrayOfString[7] = "信用卡";
		arrayOfString[8] = "购物券";
		arrayOfString[9] = "电子支付";
		arrayOfString[10] = "利息税";
		arrayOfString[11] = "现金";
		arrayOfString[12] = "零整1年";
		arrayOfString[13] = "零整3年";
		arrayOfString[14] = "零整5年";
		arrayOfString[15] = "1天通知";
		arrayOfString[16] = "7天通知";
		arrayOfString[17] = "6月贷款";
		arrayOfString[18] = "1年贷款";
		arrayOfString[19] = "3年贷款";
		arrayOfString[20] = "5年贷款";
		arrayOfString[21] = "10年贷款";
		arrayOfString[22] = "印花税";
		arrayOfString[23] = "社保";
		arrayOfString[24] = "银行卡";
		arrayOfString[25] = "定期一本通";
		return arrayOfString[(paramInt - 1)];
	}
	//------------------
	public static List<String> getCurrentTypeList(List<Integer> paramList)
	{
		paramList.clear();
		ArrayList localArrayList = new ArrayList();
		localArrayList.add("活期存折");
		localArrayList.add("银行卡");
		paramList.add(Integer.valueOf(0));
		paramList.add(Integer.valueOf(1));
		return localArrayList;
	}
	
	public static List<String> getDepositTypeList()
	{
		ArrayList localArrayList = new ArrayList();
		localArrayList.add("活期/银行卡");
	//	localArrayList.add("信用卡");
		localArrayList.add("定期");
	//	localArrayList.add("定期一本通");
	//	localArrayList.add("零整");
	//	localArrayList.add("电子支付");
	//	localArrayList.add("购物券");
	//	localArrayList.add("社保帐户");
		return localArrayList;
	}
	public static List<String> getFixedTypeList(List<Integer> paramList)
	{
		paramList.clear();
		ArrayList localArrayList = new ArrayList();
		for (int i = 2; i <= 7; i++)
		{
			localArrayList.add(getDepositTypeName(i));
			paramList.add(Integer.valueOf(i));
		}
		return localArrayList;
	}
	//活期
	public static String insertCurrent(int paramInt1, long paramLong, int paramInt2, String paramString1, String paramString2, Date paramDate, int paramInt3)
	{
		String str = "";
		if (paramLong >= 0L)
		{
			if (paramString1.length() != 0)
			{
				if (paramInt3 > 0)
				{
					Deposit localDeposit = new Deposit(paramInt3);
					if (!localDeposit.isOverSum(paramLong))
				    {
				        localDeposit.addSum(-paramLong);
						int i;
					    if (paramInt1 != 0)
						    i = 25;
					    else
						    i = 1;
					    int j = insertRow(paramString1, i, paramLong, 0L, paramDate, paramInt2, 0, paramString2, 0, 0, 0, 0, 0, 0);
					    if (paramInt3 > 0)
						    Virement.insert(3, paramLong, paramInt3, j, paramDate);
					     Account.addAccountSum(2, paramLong);
					     str = "操作成功完成！";
				    }
				    else
					{
				        str = "余额不足！";
			        }
				}
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
	

	//定期
	public static String insertFixed(int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, String paramString, Date paramDate)
    {
        String str = "";
        if (paramLong >= 0L)
        {
            if (paramString.length() != 0)
            {
                if (paramInt4 > 0)
                {
                    Deposit localDeposit = new Deposit(paramInt4);
                    if (!localDeposit.isOverSum(paramLong))
					{
			            localDeposit.addSum(-paramLong);
                        int i = insertRow(paramString, paramInt1, paramLong, 0L, paramDate, paramInt3, 0, "", paramInt2, 0, 0, 0, 0, 0);
                        if (paramInt4 > 0)
                            Virement.insert(8, paramLong, paramInt4, i, paramDate);
                        Account.addAccountSum(3, paramLong);
                        str = "操作成功完成！";
			
			        }
					else
					{
                        str = "余额不足！";
		            }
		        }
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

	static int insertRow(String paramString1, int paramInt1, long paramLong1, long paramLong2, Date paramDate, int paramInt2, int paramInt3, String paramString2, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
	{
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("insert into ").append("deposit").append(" values(null,'").append(paramString1).append("',").append(paramInt1).append(",").append(paramLong1).append(",").append(paramLong2).append(",").append(paramDate.getTime()).append(",").append(paramInt2).append(",").append(0).append(",").append(paramInt3).append(",'").append(paramString2).append("',").append(paramInt4).append(",").append(paramInt5).append(",").append(paramInt6).append(",").append(paramInt7).append(",").append(paramInt8).append(",0,").append(paramInt9).append(")");
		DBTool.database.execSQL(localStringBuffer.toString());
		int i = DBTool.getMaxId("deposit");
		DBTool.database.execSQL("update deposit set rank=" + i + " where id=" + i);
		return i;
	}
	
	public boolean isOverSum(long paramLong)
    {
        boolean bool = true;
        if (((this.type != 8) || (paramLong - this.sum <= this.maxsum)) && ((this.type == 8) || (this.sum >= paramLong)))
            bool = false;
        return bool;
    }
	public void addSum(long paramLong)
	{
		if ((paramLong < 0L) && (this.type == 8))
			this.posnumber = (short)(1 + this.posnumber);
		setSum(paramLong + this.sum);
	}
	private void setSum(long paramLong)
    {
        if (this.type != 8)
        {
            if (this.type != 24)
            {
                Account.addAccountSum(getAccountType(), paramLong - this.sum);
                this.sum = paramLong;
            }
            else
            {
                this.sum = paramLong;
            }
        }
        else
        {
            if (this.sum <= 0L)
                Account.addAccountSum(5, this.sum);
            else
                Account.addAccountSum(2, -this.sum);
            this.sum = paramLong;
            if (this.sum <= 0L)
                Account.addAccountSum(5, -this.sum);
            else
                Account.addAccountSum(2, this.sum);
        }
        save();
    }
    public void save()
    {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("update ").append("deposit").append(" set name='").append(this.name).append("',type=").append(this.type).append(",sum=").append(this.sum).append(",maxsum=").append(this.maxsum).append(",date=").append(this.real_date.getTime()).append(",bankid=").append(this.bankid).append(",rank=").append(this.rank).append(",flag=").append(this.flag).append(",cardno='").append(this.cardno).append("',rate=").append(this.rate).append(",billday=").append(this.billday).append(",returnday=").append(this.returnday).append(",freeofposnumber=").append(this.freeofposnumber).append(",posnumber=").append(this.posnumber).append(",billsum=").append(this.billsum).append(",deposit_id=").append(this.deposit_id).append(" where id=").append(this.id);
        DBTool.database.execSQL(localStringBuffer.toString());
    }
	//--------------
    public String getDepositTypeName()
    {
        return getDepositTypeName(this.type);
    }
	public String getDisplayCardno()
	{
		String str;
		if (this.cardno.length() != 0)
		{
			str = "****";
			if (this.cardno.length() > 4)
				str = this.cardno.substring(0, 4) + "*********";
		}
		else
		{
			str = "";
		}
		return str;
	}
	public int getInterest()
    {
        int i;
		//默认7
        if (this.type > 2)
            i = getInterest(this.type, this.rate, this.maxsum);
        else
            i = getInterest(this.type, this.rate, this.sum);
        return i;
    }
 
   public static int getInterest(int paramInt1, int paramInt2, long paramLong)
   {
       float f = (float)(paramLong * paramInt2 / 10000L);
       switch (paramInt1)
       {
            case 2:
                f /= 4.0F;
                break;
    case 3:
      f /= 2.0F;
      break;
    case 5:
      f *= 2.0F;
      break;
    case 6:
      f *= 3.0F;
      break;
    case 7:
      f *= 5.0F;
      break;

          }
    return (int)(0.5D + f);
    }
    
	public MyDate getAtTermDate()
    {
        return getAtTermDate(this.type, this.date);
    }
    
	public static MyDate getAtTermDate(int paramInt, MyDate paramFDate)
    {
        MyDate localFDate = paramFDate.clone();
        int i = paramFDate.getMonth();
        switch (paramInt)
        {
    case 2:
      i += 3;
      break;
    case 3:
      i += 6;
      break;
    case 4:
    case 13:
      localFDate.setYear((short)(1 + localFDate.getYear()));
      break;
    case 5:
      localFDate.setYear((short)(2 + localFDate.getYear()));
      break;
    case 6:
    case 14:
      localFDate.setYear((short)(3 + localFDate.getYear()));
      break;
    case 7:
    case 15:
      localFDate.setYear((short)(5 + localFDate.getYear()));
      break;
	case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    }
    if (i <= 12)
    {
      localFDate.setMonth((byte)i);
    }
    else
    {
      localFDate.setYear((short)(localFDate.getYear() + i / 12));
      localFDate.setMonth((byte)(i % 12));
    }
    return localFDate;
    }
	
	public boolean isFatherDeposit()
	{
		boolean bool;
		if ((this.type != 25) && (this.type != 26))
			bool = false;
		else
			bool = true;
		return bool;
	}
	public boolean isChildDeposit()
	{
		boolean bool;
		if (this.flag != 2)
			bool = false;
		else
			bool = true;
		return bool;
	}

	public String modifyCurrent(int paramInt1, long paramLong, int paramInt2, String paramString, Date paramDate)
    {
    String str;
    if (paramLong >= 0L)
    {
      if (paramString.length() != 0)
      {
        if ((paramInt1 != 0) || (!isFatherDeposit()) || (!hasChildren()))
        {
          if ((isFatherDeposit()) && (this.bankid != paramInt2))
            DBTool.database.execSQL("update deposit set bankid=" + paramInt2 + " where flag=2 and deposit_id=" + this.id);
          short s;
          if (paramInt1 != 0)
            s = 25;
          else
            s = 1;
          this.type = s;
          setSum(paramLong);
          this.bankid = (short)paramInt2;
          this.name = paramString;
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
          str = "存在子帐户,不允许设置为活折！";
        }
      }
      else
        str = "名字不允许为空！";
    }
    else
      str = "金额错误！";
    return str;
  }

  public String modifyFixed(int paramInt1, long paramLong, int paramInt2, int paramInt3, String paramString, Date paramDate)
  {
    String str;
    if (paramLong >= 0L)
    {
      if (paramString.length() != 0)
      {
        setSum(paramLong);
        this.type = (short)paramInt1;
        this.bankid = (short)paramInt3;
        this.rate = (short)paramInt2;
        this.name = paramString;
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
  

  //
    public boolean hasChildren()
    {
		boolean  bool ;
        if ((this.type == 25) || (this.type == 26))
        {
            Cursor localCursor = DBTool.database.query("deposit", getColumnString(), "flag=2 and deposit_id=" + this.id, null, null, null, null);
            if (localCursor.getCount() <= 0)
			{
			
			  	bool = false;
				localCursor.close();
				return bool;
            }
			bool = true;
			localCursor.close();
			return bool;
		}
        else
        {
			bool = false;
		}
        return bool;
    }
	
	public String delete()
	{
		String str;
		if ((!isFatherDeposit()) || (!hasChildren()))
		{
			if (!hasAudit())
			{
				setSum(0L);
				this.flag = -1;
				save();
				str = "操作成功完成！";
			}
			else
			{
				str = "帐户下有流水,不能用*删除!";
			}
		}
		else
			str = "存在子帐户,不允许设置为活折！";
		return str;
	}
	public boolean hasAudit()
	{
		Cursor localCursor = Virement.getRows("deposit_from=" + this.id + " or ((kmid=" + 3 + " or kmid=" + 12 + " or kmid=" + 11 + " or kmid=" + 9 + " or kmid=" + 39 + ") and deposit_to=" + this.id + ")", null);
		boolean bool;
		if (localCursor.getCount() != 0)
			bool = false;
		else
			bool = true;
		localCursor.close();
		return bool;
	}
	public String cashSetSum(long paramLong)
	{
		String str;
		if (paramLong >= 0L)
		{
			setSum(paramLong);
			save();
			str = "操作成功完成！";
		}
		else
		{
			str = "金额错误！";
		}
		return str;
	}
	//存钱
	public String currentSave(long paramLong1, long paramLong2, Date paramDate)
	{
		Object localObject;
		if (paramLong1 > 0L)
		{
			localObject = new Deposit(1);
			if (!((Deposit)localObject).isOverSum(paramLong1))
			{
				((Deposit)localObject).addSum(-paramLong1);
				addSum(paramLong1);
				Virement.insert(3, paramLong1, 1, this.id, paramDate);
				if (paramLong2 > 0L)
					Audit.insert(269, paramLong2, this.id, paramDate, "存款手续费:" + this.name);
				localObject = "操作成功完成！";
			}
			else
			{
				localObject = "现金不足！";
			}
		}
		else
		{
			localObject = "金额错误！";
		}
		return (String)localObject;
	}
	//取钱
	public String currentDraw(long paramLong1, long paramLong2, Date paramDate)
	{
		Object localObject;
		if (paramLong1 > 0L)
		{
			localObject = new Deposit(1);
			if (!isOverSum(paramLong1))
			{
				((Deposit)localObject).addSum(paramLong1);
				addSum(-paramLong1);
				Virement.insert(3, paramLong1, this.id, 1, paramDate);
				if (paramLong2 > 0L)
					Audit.insert(269, paramLong2, this.id, paramDate, "取款手续费:" + this.name);
				localObject = "操作成功完成！";
			}
			else
			{
				localObject = "余额不足！";
			}
		}
		else
		{
			localObject = "金额错误！";
		}
		return (String)localObject;
	}
	//活期转帐
	public String currentVirement(long paramLong1, long paramLong2, int paramInt, Date paramDate)
    {
        String note;
        if (paramLong1 > 0L)
        {
            Deposit localObject = new Deposit(paramInt);
            if (!isOverSum(paramLong1))
            {
                localObject.addSum(paramLong1);
                addSum(-paramLong1);
                Virement.insert(3, paramLong1, this.id, paramInt, paramDate);
                if (paramLong2 > 0L)
                Audit.insert(269, paramLong2, this.id, paramDate, "转帐手续费:" + this.name);
                note = "操作成功完成！";
            }
            else
            {
                note = "余额不足！";
            }
        }
        else
        {
             note = "金额错误！";
        }
        return note;
    }
	public String currentBankFee(long paramLong, Date paramDate, String paramString)
	{
		return Audit.insert(269, paramLong, this.id, paramDate, "银行收费:" + paramString);
	}
	
	public String currentInterest(long paramLong, Date paramDate)
	{
		String str;
		if (paramLong > 0L)
		{
			Audit.insert(291, paramLong, this.id, paramDate, "活期利息：" + this.name);
			str = "操作成功完成！";
		}
		else
		{
			str = "金额错误！";
		}
		return str;
	}
	public String currentCheckout(long paramLong, Date paramDate)
	{
		String str;
		if (paramLong >= 0L)
		{
			if (!hasChildren())
			{
				new Deposit(1).addSum(paramLong + this.sum);
				Virement.insert(3, paramLong + this.sum, this.id, 1, paramDate);
				setSum(0L);
				this.flag = -1;
				save();
				str = "操作成功完成！";
			}
			else
			{
				str = "存在子帐户,不允许设置为活折！";
			}
		}
		else
			str = "金额错误！";
		return str;
	}
	//定期到期支取
  public String fixedDqzq(int paramInt, Date paramDate)
  {
    String str;
    if (this.sum > 0L)
    {
      Deposit localDeposit = new Deposit(1);
      if ((this.flag == 2) && (localDeposit.type == 25))
        localDeposit.reset(this.deposit_id);
      localDeposit.addSum(this.sum + paramInt);
      if (paramInt > 0)
      {
        int i = Virement.insert(11, paramInt, this.id, localDeposit.id, paramDate);
        Audit.insertSystemAudit(291, paramInt, localDeposit.id, i, paramDate, "定期利息：" + this.name);
      }
      Virement.insert(9, this.sum, this.id, localDeposit.id, paramDate);
      setSum(0L);
      this.flag = -1;
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "金额错误！";
    }
    return str;
  }
  public String fixedTqzq(long paramLong, int paramInt, Date paramDate)
  {
    Object localObject;
    if (paramLong > 0L)
    {
      localObject = new Deposit(1);
      if ((this.flag == 2) && (((Deposit)localObject).type == 25))
        ((Deposit)localObject).reset(this.deposit_id);
      ((Deposit)localObject).addSum(paramLong + paramInt);
      addSum(-paramLong);
      if (this.sum == 0L)
      {
        this.flag = -1;
        save();
      }
      if (paramInt > 0)
      {
        int i = Virement.insert(11, paramInt, this.id, ((Deposit)localObject).id, paramDate);
        Audit.insertSystemAudit(291, paramInt, this.id, i, paramDate, "定期利息：" + this.name);
      }
      Virement.insert(9, paramLong, this.id, ((Deposit)localObject).id, paramDate);
      localObject = "操作成功完成！";
    }
    else
    {
      localObject = "金额错误！";
    }
    return (String)localObject;
  }

	//////////////////////////
	public static List<String> getDepositList(String paramString1, String paramString2, List<Integer> paramList)
	{
		paramList.clear();
		Cursor localCursor = DBTool.database.query("deposit", getColumnString(), paramString1, null, null, null, paramString2);
		ArrayList localArrayList = new ArrayList();
		localCursor.moveToFirst();
		while (!localCursor.isAfterLast())
		{
			localArrayList.add(localCursor.getString(1) + "  " + Convertor.sumToString(localCursor.getLong(3)));
			paramList.add(Integer.valueOf(localCursor.getInt(0)));
			localCursor.moveToNext();
		}
		localCursor.close();
		return localArrayList;
	}


	
	int getAccountType()
	{
		int i;
		switch (this.type)
		{
			default:
				i = 3;
				break;
			case 1:
			case 9:
			case 10:
			case 25:
				i = 2;
				break;
			case 8:
				i = 5;
				break;
			case 12:
				i = 1;
		}
		return i;
	}

	public void restoreSum(long paramLong)
	{
		if ((paramLong < 0L) && (this.type == 8))
			this.posnumber = (short)(-1 + this.posnumber);
		setSum(paramLong + this.sum);
	}
	public void modifySum(long paramLong)
	{
		setSum(paramLong + this.sum);
	}
	
	public static void dealCreditCardBillAndPos(MyDate paramFDate1, MyDate paramFDate2)
    {
	    Cursor localCursor = DBTool.database.query("deposit", getColumnString(), "flag>-1 and type=8", null, null, null, null);
	    localCursor.moveToFirst();
	    Deposit localDeposit = new Deposit();
	    while (!localCursor.isAfterLast())
	    {
	      if (isCreatedayBetweenTwoDates(new MyDate(new Date(localCursor.getLong(5))), paramFDate1, paramFDate2))
	      {
	        localDeposit.reset(localCursor);
	        localDeposit.posnumber = 0;
	        localDeposit.save();
	      }
	      if (isBilldayBetweenTwoDates(localCursor.getShort(11), paramFDate1, paramFDate2))
	      {
	        localDeposit.reset(localCursor);
	        localDeposit.billsum = localDeposit.sum;
	        localDeposit.save();
	      }
	      localCursor.moveToNext();
	    }
	    localCursor.close();
	  }
	
	public Deposit()
	{
	}
	
	 
	 public static boolean isBilldayBetweenTwoDates(int paramInt, MyDate paramFDate1, MyDate paramFDate2)
	  {
	    boolean bool = true;
	    MyDate localFDate = paramFDate1.clone();
	    if (paramFDate1.getDay() > paramInt)
	      if (paramFDate1.getMonth() != 12)
	      {
	        localFDate.setMonth((byte)(1 + paramFDate1.getMonth()));
	      }
	      else
	      {
	        localFDate.setMonth(1);
	        localFDate.setYear((short)(1 + paramFDate1.getYear()));
	      }
	    localFDate.setDay((byte)paramInt);
	    if (localFDate.getValue() > paramFDate2.getValue())
	      bool = false;
	    return bool;
	  }

	  public static boolean isCreatedayBetweenTwoDates(MyDate paramFDate1, MyDate paramFDate2, MyDate paramFDate3)
	  {
	    paramFDate1.setYear(paramFDate2.getYear());
	    if (paramFDate1.getValue() < paramFDate2.getValue())
	      paramFDate1.setYear((short)(1 + paramFDate2.getYear()));
	    boolean bool;
	    if (paramFDate1.getValue() > paramFDate3.getValue())
	      bool = false;
	    else
	      bool  = true;
	    return bool;
	  }
	  public void deleteRow()
	  {
	    DBTool.database.execSQL("delete from deposit where id=" + this.id);
	  }
    }
 /*
 
   public String modifyCashCard(long paramLong, String paramString, Date paramDate)
  {
    String str;
    if (paramLong >= 0L)
    {
      if (paramString.length() != 0)
      {
        setSum(paramLong);
        this.name = paramString;
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
  	//电子帐户
	public static String insertCashCard(long paramLong, String paramString1, String paramString2, Date paramDate)
    {
        String str;
        if (paramLong >= 0L)
        {
            if (paramString1.length() != 0)
            {
                insertRow(paramString1, 10, paramLong, 0L, paramDate, 0, 0, paramString2, 0, 0, 0, 0, 0, 0);
                Account.addAccountSum(2, paramLong);
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
 
	//
	public static String insertFixedCard(int paramInt, String paramString, Date paramDate)
    {
        String str;
        if (paramString.length() != 0)
        {
            insertRow(paramString, 26, 0L, 0L, paramDate, paramInt, 0, "", 0, 0, 0, 0, 0, 0);
            str = "操作成功完成！";
        }
        else
        {
            str = "名字不允许为空！";
        }
        return str;
    }


  

 


 


  public static List<String> getDepositFixedAndLzTypeList(List<Integer> paramList)
  {
    paramList.clear();
    ArrayList localArrayList = new ArrayList();
    for (int i = 2; i <= 7; i++)
    {
      localArrayList.add(getDepositTypeName(i));
      paramList.add(Integer.valueOf(i));
    }
    for (i = 13; i <= 15; i++)
    {
      localArrayList.add(getDepositTypeName(i));
      paramList.add(Integer.valueOf(i));
    }
    return localArrayList;
  }

 



 

  public static long getIncomeTax(long paramLong1, long paramLong2)
  {
    long[] arrayOfLong2 = new long[9];
    long[] arrayOfLong3 = new long[9];
    long[] arrayOfLong1 = new long[9];
    long l = paramLong1 - paramLong2;
    arrayOfLong2[0] = 0L;
    arrayOfLong3[0] = 3L;
    arrayOfLong1[0] = 0L;
    arrayOfLong2[1] = 1500L;
    arrayOfLong3[1] = 10L;
    arrayOfLong1[1] = 105L;
    arrayOfLong2[2] = 4500L;
    arrayOfLong3[2] = 20L;
    arrayOfLong1[2] = 555L;
    arrayOfLong2[3] = 9000L;
    arrayOfLong3[3] = 25L;
    arrayOfLong1[3] = 1005L;
    arrayOfLong2[4] = 35000L;
    arrayOfLong3[4] = 30L;
    arrayOfLong1[4] = 2755L;
    arrayOfLong2[5] = 55000L;
    arrayOfLong3[5] = 35L;
    arrayOfLong1[5] = 5505L;
    arrayOfLong2[6] = 80000L;
    arrayOfLong3[6] = 45L;
    arrayOfLong1[6] = 13505L;
    int i = 6;
    int j = 0;
    while (j < 6)
    {
      if ((l <= 100L * arrayOfLong2[j]) || (l > 100L * arrayOfLong2[(j + 1)]))
      {
        j++;
        continue;
      }
      i = j;
    }
    l = (50L + l * arrayOfLong3[i]) / 100L - 100L * arrayOfLong1[i];
    if (l < 0L)
      l = 0L;
    return (int)l;
  }


  public static List<String> getLzTypeList(List<Integer> paramList)
  {
    paramList.clear();
    ArrayList localArrayList = new ArrayList();
    for (int i = 13; i <= 15; i++)
    {
      localArrayList.add(getDepositTypeName(i));
      paramList.add(Integer.valueOf(i));
    }
    return localArrayList;
  }

  public static void getRankAdapter(FinanceAdapter paramFinanceAdapter, List<Integer> paramList)
  {
    paramList.clear();
    paramFinanceAdapter.clear();
    ArrayList localArrayList = new ArrayList();
    Object localObject = new String[1];
    localObject[0] = "账户名称";
    localArrayList.add(localObject);
    paramList.add(Integer.valueOf(2147483394));
    localObject = getRows(getSqlOfRank(), "rank asc");
    ((Cursor)localObject).moveToFirst();
    while (!((Cursor)localObject).isAfterLast())
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = ((Cursor)localObject).getString(1);
      localArrayList.add(arrayOfString);
      paramList.add(Integer.valueOf(((Cursor)localObject).getInt(0)));
      ((Cursor)localObject).moveToNext();
    }
    ((Cursor)localObject).close();
    paramFinanceAdapter.append(localArrayList);
    paramFinanceAdapter.notifyDataSetChanged();
  }



  static String getSqlOfRank()
  {
    return "flag>=0 ";
  }


 

  
  public static String insertCreditCard(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString1, String paramString2, Date paramDate)
  {
    String str;
    if (paramString1.length() != 0)
    {
      insertRow(paramString1, 8, paramLong1, paramLong2, paramDate, paramInt4, 0, paramString2, 0, paramInt1, paramInt2, paramInt3, 0, paramInt5);
      if (paramLong1 <= 0L)
        Account.addAccountSum(5, -paramLong1);
      else
        Account.addAccountSum(2, paramLong1);
      str = "操作成功完成！";
    }
    else
    {
      str = "名字不允许为空！";
    }
    return str;
  }




  public static String insertLz(int paramInt1, long paramLong1, long paramLong2, int paramInt2, int paramInt3, int paramInt4, String paramString, Date paramDate)
  {
    String str;
    if (paramLong1 > 0L)
    {
      if (paramString.length() != 0)
      {
        if (paramInt4 > 0)
        {
          Deposit localDeposit = new Deposit(paramInt4);
          if (!localDeposit.isOverSum(paramLong1))
            localDeposit.addSum(-paramLong1);
        }
        else
        {
          int i = insertRow(paramString, paramInt1, paramLong1, paramLong2, paramDate, paramInt3, 0, "", paramInt2, 0, 0, 0, 0, 0);
          if (paramInt4 > 0)
            Virement.insert(38, paramLong1, paramInt4, i, paramDate);
          Account.addAccountSum(3, paramLong1);
          str = "操作成功完成！";
          break label125;
        }
        str = "余额不足！";
      }
      else
      {
        str = "名字不允许为空！";
      }
    }
    else
      str = "金额错误！";
    label125: return str;
  }

  public static String insertPurchaseCard(long paramLong, boolean paramBoolean, int paramInt, String paramString, Date paramDate)
  {
    String str;
    if (paramLong > 0L)
    {
      if (paramString.length() != 0)
      {
        long l = paramLong;
        if (paramBoolean)
          l = 0L;
        int i = insertRow(paramString, 9, l, 0L, paramDate, 0, 0, "", 0, 0, 0, 0, 0, 0);
        if (!paramBoolean)
          Account.addAccountSum(2, paramLong);
        else
          Audit.insert(paramInt, paramLong, i, paramDate, "购物券：" + paramString);
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


  public static String insertSocialInsurance(long paramLong, String paramString, Date paramDate)
  {
    String str;
    if (paramLong >= 0L)
    {
      if (paramString.length() != 0)
      {
        insertRow(paramString, 24, paramLong, 0L, paramDate, 0, 0, "", 0, 0, 0, 0, 0, 0);
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

 

  public String FixedCardCheckout()
  {
    String str;
    if (!hasChildren())
    {
      this.flag = -1;
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "存在子帐户,不允许设置为活折！";
    }
    return str;
  }


  public String cashCardCheckout()
  {
    String str;
    if (this.sum == 0L)
    {
      this.flag = -1;
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "余额不为0，不能注销！";
    }
    return str;
  }

  public String cashCardSave(long paramLong, int paramInt, Date paramDate)
  {
    Object localObject;
    if (paramLong > 0L)
    {
      localObject = new Deposit(paramInt);
      if (!((Deposit)localObject).isOverSum(paramLong))
      {
        ((Deposit)localObject).addSum(-paramLong);
        addSum(paramLong);
        Virement.insert(3, paramLong, paramInt, this.id, paramDate);
        localObject = "操作成功完成！";
      }
      else
      {
        localObject = "余额不足！";
      }
    }
    else
    {
      localObject = "金额错误！";
    }
    return (String)localObject;
  }

  public String cashCardVirement(long paramLong, int paramInt, Date paramDate)
  {
    String str;
    if (paramLong > 0L)
    {
      if (!isOverSum(paramLong))
      {
        new Deposit(paramInt).addSum(paramLong);
        addSum(-paramLong);
        Virement.insert(3, paramLong, this.id, paramInt, paramDate);
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

  

  public String creditCardCheckout()
  {
    String str;
    if ((this.billsum == 0L) && (this.sum == 0L))
    {
      this.flag = -1;
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "余额不为0，不能注销！";
    }
    return str;
  }

  public String creditCardGetCash(long paramLong1, long paramLong2, Date paramDate)
  {
    String str;
    if (paramLong1 > 0L)
    {
      new Deposit(1).addSum(paramLong1);
      addSum(-paramLong1);
      Virement.insert(3, paramLong1, this.id, 1, paramDate);
      if (paramLong2 > 0L)
        Audit.insert(269, paramLong2, this.id, paramDate, "取现手续费:" + this.name);
      str = "操作成功完成！";
    }
    else
    {
      str = "金额错误！";
    }
    return str;
  }

  public String creditCardReturn(long paramLong, int paramInt, Date paramDate)
  {
    Object localObject;
    if (paramLong > 0L)
    {
      localObject = new Deposit(paramInt);
      if (!((Deposit)localObject).isOverSum(paramLong))
      {
        ((Deposit)localObject).addSum(-paramLong);
        addSum(paramLong);
        this.billsum = (paramLong + this.billsum);
        if (this.billsum > 0L)
          this.billsum = 0L;
        save();
        Virement.insert(12, paramLong, paramInt, this.id, paramDate);
        localObject = "操作成功完成！";
      }
      else
      {
        localObject = "余额不足！";
      }
    }
    else
    {
      localObject = "金额错误！";
    }
    return (String)localObject;
  }

  public String creditCardReturninterest(long paramLong, Date paramDate)
  {
    String str;
    if (paramLong > 0L)
    {
      addSum(-paramLong);
      int i = Virement.insert(13, paramLong, this.id, this.id, paramDate);
      Audit.insertSystemAudit(271, paramLong, this.id, i, paramDate, "信用卡罚息:" + this.name);
      str = "操作成功完成！";
    }
    else
    {
      str = "金额错误！";
    }
    return str;
  }










  



  public String fixedZq(int paramInt, Date paramDate)
  {
    String str;
    if (paramInt > 0)
    {
      Audit.insert(291, paramInt, this.id, paramDate, "定期利息：" + this.name);
      reset(this.id);
      this.date = getAtTermDate();
      this.real_date = this.date.getDate();
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "金额错误！";
    }
    return str;
  }



  public String getCardno()
  {
    return this.cardno;
  }




  public Date getNextLzSaveDate()
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
    if (localFDate.getMonth() != 2)
    {
      if ((localFDate.getMonth() != 4) && (localFDate.getMonth() != 6) && (localFDate.getMonth() != 9) && (localFDate.getMonth() != 11))
        localFDate.setDay(31);
      else
        localFDate.setDay(20);
    }
    else if (((localFDate.getYear() % 4 != 0) || (localFDate.getYear() % 100 == 0)) && (localFDate.getYear() % 400 != 0))
      localFDate.setDay(28);
    else
      localFDate.setDay(29);
    return localFDate.getDate();
  }

  public Date getNextReturnDateOfCreditCard()
  {
    FDate localFDate = FDate.now();
    if (localFDate.getDay() >= this.returnday)
      if (localFDate.getMonth() >= 12)
      {
        localFDate.setYear(1 + localFDate.getYear());
        localFDate.setMonth(1);
      }
      else
      {
        localFDate.setMonth(1 + localFDate.getMonth());
      }
    localFDate.setDay(this.returnday);
    return localFDate.getDate();
  }

  public Date getReturnDateOfNextmonthForCreditCard()
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
    localFDate.setDay(this.returnday);
    return localFDate.getDate();
  }




  public String insertFixedOfCurrentCard(int paramInt1, int paramInt2, long paramLong, int paramInt3, String paramString, Date paramDate)
  {
    String str;
    if (paramLong > 0L)
    {
      if (paramString.length() != 0)
      {
        if (paramInt1 == 0)
        {
          if (!isOverSum(paramLong))
            addSum(-paramLong);
        }
        else
        {
          int i = insertRow(paramString, paramInt2, paramLong, 0L, paramDate, this.bankid, 2, "", paramInt3, 0, 0, 0, 0, this.id);
          if (paramInt1 == 0)
            Virement.insert(8, paramLong, this.id, i, paramDate);
          Account.addAccountSum(3, paramLong);
          str = "操作成功完成！";
          break label119;
        }
        str = "余额不足！";
      }
      else
      {
        str = "名字不允许为空！";
      }
    }
    else
      str = "金额错误！";
    label119: return str;
  }

  public String insertFixedOfFixedCard(int paramInt1, long paramLong, int paramInt2, int paramInt3, String paramString, Date paramDate)
  {
    String str;
    if (paramLong > 0L)
    {
      if (paramString.length() != 0)
      {
        if (paramInt3 > 0)
        {
          Deposit localDeposit = new Deposit(paramInt3);
          if (!localDeposit.isOverSum(paramLong))
            localDeposit.addSum(-paramLong);
        }
        else
        {
          int i = insertRow(paramString, paramInt1, paramLong, 0L, paramDate, this.bankid, 2, "", paramInt2, 0, 0, 0, 0, this.id);
          if (paramInt3 > 0)
            Virement.insert(8, paramLong, paramInt3, i, paramDate);
          Account.addAccountSum(3, paramLong);
          str = "操作成功完成！";
          break label132;
        }
        str = "余额不足！";
      }
      else
      {
        str = "名字不允许为空！";
      }
    }
    else
      str = "金额错误！";
    label132: return str;
  }

  public String insertLzOfCurrentCard(int paramInt1, int paramInt2, long paramLong1, long paramLong2, int paramInt3, String paramString, Date paramDate)
  {
    String str;
    if (paramLong1 > 0L)
    {
      if (paramString.length() != 0)
      {
        if (paramInt1 == 0)
        {
          if (!isOverSum(paramLong1))
            addSum(-paramLong1);
        }
        else
        {
          int i = insertRow(paramString, paramInt2, paramLong1, paramLong2, paramDate, this.bankid, 2, "", paramInt3, 0, 0, 0, 0, this.id);
          if (paramInt1 == 0)
            Virement.insert(8, paramLong1, this.id, i, paramDate);
          Account.addAccountSum(3, paramLong1);
          str = "操作成功完成！";
          break label118;
        }
        str = "余额不足！";
      }
      else
      {
        str = "名字不允许为空！";
      }
    }
    else
      str = "金额错误！";
    label118: return str;
  }



  public String lzDqzq(int paramInt, Date paramDate)
  {
    String str;
    if (this.sum > 0L)
    {
      if (this.flag != 2)
        this.deposit_id = 1;
      new Deposit(this.deposit_id).addSum(this.sum + paramInt);
      if (paramInt > 0)
      {
        int i = Virement.insert(11, paramInt, this.id, this.deposit_id, paramDate);
        Audit.insertSystemAudit(291, paramInt, this.id, i, paramDate, "零整利息：" + this.name);
      }
      Virement.insert(39, this.sum, this.id, this.deposit_id, paramDate);
      setSum(0L);
      this.flag = -1;
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "金额错误！";
    }
    return str;
  }

  public String lzSave(long paramLong, int paramInt, Date paramDate)
  {
    Object localObject;
    if (paramLong > 0L)
    {
      localObject = new Deposit(paramInt);
      if (!((Deposit)localObject).isOverSum(paramLong))
      {
        ((Deposit)localObject).addSum(-paramLong);
        addSum(paramLong);
        Virement.insert(38, paramLong, paramInt, this.id, paramDate);
        localObject = "操作成功完成！";
      }
      else
      {
        localObject = "余额不足！";
      }
    }
    else
    {
      localObject = "金额错误！";
    }
    return (String)localObject;
  }

  public String lzTqzq(int paramInt, Date paramDate)
  {
    String str;
    if (paramInt > 0)
    {
      if (this.flag != 2)
        this.deposit_id = 1;
      new Deposit(this.deposit_id).addSum(this.sum + paramInt);
      if (paramInt > 0)
      {
        int i = Virement.insert(11, paramInt, this.id, this.deposit_id, paramDate);
        Audit.insertSystemAudit(291, paramInt, this.id, i, paramDate, "零整利息：" + this.name);
      }
      Virement.insert(39, this.sum, this.id, this.deposit_id, paramDate);
      setSum(0L);
      this.flag = -1;
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "金额错误！";
    }
    return str;
  }



  public String modifyCreditCard(long paramLong1, int paramInt1, long paramLong2, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, String paramString, Date paramDate)
  {
    String str;
    if (paramInt1 <= 0)
    {
      if ((paramInt1 >= 0) || (paramInt1 >= paramLong1))
      {
        if (paramString.length() != 0)
        {
          setSum(paramLong1);
          this.maxsum = paramLong2;
          this.billsum = paramInt1;
          this.bankid = (short)paramInt6;
          this.returnday = (short)paramInt2;
          this.billday = (short)paramInt3;
          this.posnumber = (short)paramInt4;
          this.freeofposnumber = (short)paramInt5;
          this.name = paramString;
          this.deposit_id = paramInt7;
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
          save();
          str = "操作成功完成！";
        }
        else
        {
          str = "名字不允许为空！";
        }
      }
      else
        str = "总金额不能大于帐单金额!";
    }
    else
      str = "帐单金额不能大于0!";
    return str;
  }



  public String modifyFixedCard(int paramInt, String paramString, Date paramDate)
  {
    String str;
    if (paramString.length() != 0)
    {
      this.bankid = (short)paramInt;
      this.name = paramString;
      if (!this.date.equals(paramDate))
      {
        this.real_date = paramDate;
        this.date = new FDate(paramDate);
      }
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "名字不允许为空！";
    }
    return str;
  }

  public String modifyLz(int paramInt1, long paramLong1, long paramLong2, int paramInt2, int paramInt3, String paramString, Date paramDate)
  {
    String str;
    if (paramLong1 >= 0L)
    {
      if (paramString.length() != 0)
      {
        setSum(paramLong1);
        this.maxsum = paramLong2;
        this.type = (short)paramInt1;
        this.bankid = (short)paramInt3;
        this.rate = (short)paramInt2;
        this.name = paramString;
        if (!this.date.equals(paramDate))
        {
          this.real_date = paramDate;
          this.date = new FDate(paramDate);
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



  public boolean moveDown()
  {
    Cursor localCursor = DBTool.database.query("deposit", getColumnString(), getSqlOfRank() + " and rank>" + this.rank, null, null, null, "rank");
    int i;
    if (localCursor.getCount() != 0)
    {
      localCursor.moveToFirst();
      int j = localCursor.getInt(7);
      int k = localCursor.getInt(0);
      localCursor.close();
      DBTool.database.execSQL("update deposit set rank=" + j + " where id=" + this.id);
      DBTool.database.execSQL("update deposit set rank=" + this.rank + " where id=" + k);
      i = 1;
    }
    else
    {
      i.close();
      i = 0;
    }
    return i;
  }

  public boolean moveUp()
  {
    Cursor localCursor = DBTool.database.query("deposit", getColumnString(), getSqlOfRank() + " and rank<" + this.rank, null, null, null, "rank desc");
    int i;
    if (localCursor.getCount() != 0)
    {
      localCursor.moveToFirst();
      i = localCursor.getInt(7);
      int j = localCursor.getInt(0);
      localCursor.close();
      DBTool.database.execSQL("update deposit set rank=" + i + " where id=" + this.id);
      DBTool.database.execSQL("update deposit set rank=" + this.rank + " where id=" + j);
      i = 1;
    }
    else
    {
      localCursor.close();
      i = 0;
    }
    return i;
  }

  public String purchaseCardCheckout()
  {
    setSum(0L);
    this.flag = -1;
    save();
    return "操作成功完成！";
  }

  public String purchaseCardDiscount(long paramLong1, long paramLong2, int paramInt, Date paramDate)
  {
    String str;
    if ((paramLong2 > 0L) && (paramLong1 > 0L))
    {
      if (paramLong1 <= this.sum)
      {
        new Deposit(paramInt).addSum(paramLong2);
        addSum(-paramLong1);
        if (paramLong2 == 0L)
        {
          this.flag = -1;
          save();
        }
        Virement.insert(3, paramLong2, this.id, paramInt, paramDate);
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





  public void setCardno(String paramString)
  {
    this.cardno = paramString;
  }

  public String setFixedOfFixedCard(int paramInt)
  {
    Object localObject = new Deposit(paramInt);
    if (((Deposit)localObject).flag == 0)
    {
      ((Deposit)localObject).flag = 2;
      ((Deposit)localObject).deposit_id = this.id;
      ((Deposit)localObject).save();
      localObject = "操作成功完成！";
    }
    else
    {
      localObject = "不能增加此存款";
    }
    return (String)localObject;
  }

  public String socialInsuranceSave(long paramLong, Date paramDate)
  {
    String str;
    if (paramLong > 0L)
    {
      addSum(paramLong);
      Virement.insert(37, paramLong, this.id, 1, paramDate);
      str = "操作成功完成！";
    }
    else
    {
      str = "金额错误！";
    }
    return str;
  }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Deposit
 * JD-Core Version:    0.6.0
 */
