/*
 * 转帐
 */
package com.database.model;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.DBTool;
import com.database.util.MyDate;
import java.util.Date;

public class Virement
{
/*  public static final short VAssetBuy = 35;

  public static final short VVirementOut = 7;*/
  public String content = null;
  public MyDate date = null;
  public short deposit_from = 0;
  public short deposit_to = 0;
  public int id = 0;
  public short kmid = 0;
  public Date real_date = null;
  public long sum = 0L;
  /*
   * 创建表
   */
    public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE virement(id  integer PRIMARY KEY AUTOINCREMENT,kmid smallint , sum int,deposit_from smallint,deposit_to smallint,date integer,content varchar(80));");
	}
    
	//显示
    /*
	 * 获取行
	 * public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) 
	 * selection 条件
	 * orderBy 排序
	 */
	public static Cursor getRows(String selection, String orderBy)
	{
		return DBTool.database.query("virement", getColumnString(), selection, null, null, null, orderBy);
	}
	/*
     * 获取列
     */
	public static String[] getColumnString()
	{
		String[] column = new String[7];
		column[0] = "id";
		column[1] = "kmid";
		column[2] = "sum";
		column[3] = "deposit_from";
		column[4] = "deposit_to";
		column[5] = "date";
		column[6] = "content";
		return column;
	}
	
	public static String getVirementType(int paramInt)
	{
		String str;
		switch (paramInt)
		{
			default:
				str = "";
				break;
			case 1:
				str = "支出";
				break;
			case 2:
				str = "收入";
				break;
			case 3:
				str = "转账";
				break;
			case 4:
				str = "转投资";
				break;
			case 5:
				str = "回收投资";
				break;
			case 6:
				str = "转出";
				break;
			case 7:
				str = "转入";
				break;
			case 8:
				str = "活转定";
				break;
			case 9:
				str = "定转活";
				break;
			case 10:
				str = "定期转存";
				break;
			case 11:
				str = "存款利息";
				break;
			case 12:
				str = "信用卡还款";
				break;
			case 13:
				str = "信用卡还息";
				break;
			case 14:
				str = "借出";
				break;
			case 15:
				str = "借入";
				break;
			case 16:
				str = "贷款";
				break;
			case 17:
				str = "收欠款";
				break;
			case 18:
				str = "还借款";
				break;
			case 19:
				str = "还贷";
				break;
			case 20:
				str = "借出利息";
				break;
			case 21:
				str = "借入利息";
				break;
			case 22:
				str = "贷款利息";
				break;
			case 23:
				str = "保险费";
				break;
			case 24:
				str = "保险收入";
				break;
			case 25:
				str = "债券购买";
				break;
			case 26:
				str = "债券兑付";
				break;
			case 27:
				str = "债券收益";
				break;
			case 28:
				str = "基金申购";
				break;
			case 29:
				str = "基金赎回";
				break;
			case 30:
				str = "基金分红";
				break;
			case 31:
				str = "出差借款";
				break;
			case 32:
				str = "出差报销";
				break;
			case 33:
				str = "出差还款";
				break;
			case 34:
				str = "项目支出";
				break;
			case 35:
				str = "资产购买";
				break;
			case 36:
				str = "资产出售";
				break;
			case 37:
				str = "社保充值";
				break;
			case 38:
				str = "零整续存";
				break;
			case 39:
				str = "零整支取";
				break;
			case 40:
				str = "项目收入";
				break;
			case 41:
				str = "租赁收入";
				break;
		}
		return str;
	}


	public static boolean isIncome(int paramInt)
	{
		boolean bool;
		switch (paramInt)
		{

			default:
				bool = true;
				break;
			case 1:
			case 3:
			case 5:
			case 8:
			case 12:
			case 13:
			case 14:
			case 18:
			case 19:
			case 21:
			case 22:
			case 23:
			case 25:
			case 28:
			case 33:
			case 34:
			case 35:
			case 38:
				bool = false;
		}
		return bool;
	}
	
	////////////
	public static int insert(int paramInt1, long paramLong, int paramInt2, int paramInt3, Date paramDate)
	{
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("insert into ").append("virement").append(" values(null,").append(paramInt1).append(",").append(paramLong).append(",").append(paramInt2).append(",").append(paramInt3).append(",").append(paramDate.getTime()).append(",'');");
		DBTool.database.execSQL(localStringBuffer.toString());
		return DBTool.getMaxId("virement");
	}
	
	public static void deleteRow(int paramInt)
    {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("delete from ").append("virement").append(" where id=").append(paramInt);
        DBTool.database.execSQL(localStringBuffer.toString());
    }
	public Virement(int paramInt)
	{
		Cursor localCursor = DBTool.database.query("virement", getColumnString(), "id=" + paramInt, null, null, null, null);
		if (localCursor.getCount() != 0)
		{
			localCursor.moveToFirst();
			this.id = paramInt;
			this.kmid = localCursor.getShort(1);
			this.sum = localCursor.getLong(2);
			this.deposit_from = localCursor.getShort(3);
			this.deposit_to = localCursor.getShort(4);
			this.real_date = new Date(localCursor.getLong(5));
			this.date = new MyDate(this.real_date);
			this.content = localCursor.getString(6);
			localCursor.close();
		}
		else
		{
			localCursor.close();
			this.id = 0;
		}
	}


	public void modify(long paramLong, int paramInt, Date paramDate)
	{
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("update ").append("virement").append(" set sum=").append(paramLong).append(",deposit_from=").append(paramInt).append(",date=").append(paramDate.getTime()).append(" where id=").append(this.id);
		DBTool.database.execSQL(localStringBuffer.toString());
	}
	  public void deleteRow()
  {
    deleteRow(this.id);
  }
  public void modify(long paramLong)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("update ").append("virement").append(" set sum=").append(paramLong).append(" where id=").append(this.id);
    DBTool.database.execSQL(localStringBuffer.toString());
  }
  public String delete()
  {
    Object localObject;
    Deposit localDeposit;
    switch (this.kmid)
    {
    case 3:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 37:
    case 38:
    case 39:
    default:
    	
      
      switch (this.kmid)
      {
      case 3:
      case 12:
        localDeposit = new Deposit(this.deposit_from);
        localObject = new Deposit(this.deposit_to);
        if (localDeposit.flag != -1)
        {
          if (((Deposit)localObject).flag != -1)
          {
            if (!((Deposit)localObject).isOverSum(this.sum))
            {
              localDeposit.restoreSum(this.sum);
              ((Deposit)localObject).restoreSum(-this.sum);
            }
            else
            {
              localObject = "余额不足！";
              return (String)localObject;
            }
          }
          else
          {
            localObject = "账户已经不存在,不能操作！";
            return (String)localObject;
          }
        }
        else
          localObject = "账户已经不存在,不能操作！";
        break;
      case 8:
      case 38:
        localObject = new Deposit(this.deposit_from);
        localDeposit = new Deposit(this.deposit_to);
        if (((Deposit)localObject).flag != -1)
        {
          if (!localDeposit.isOverSum(this.sum))
          {
            ((Deposit)localObject).restoreSum(this.sum);
            localDeposit.restoreSum(-this.sum);
            if (localDeposit.sum != 0L)
              break;
            ((Deposit)localObject).deleteRow();
          }
          else
          {
            localObject = "余额不足！";
            return (String)localObject;
          }
        }
        else
          localObject = "账户已经不存在,不能操作！";
        break;
      case 9:
      case 39:
        localObject = new Deposit(this.deposit_from);
        localDeposit = new Deposit(this.deposit_to);
        if (localDeposit.flag != -1)
        {
          if (!localDeposit.isOverSum(this.sum))
          {
            ((Deposit)localObject).restoreSum(this.sum);
            if (((Deposit)localObject).flag == -1)
            {
              ((Deposit)localObject).flag = 0;
              ((Deposit)localObject).save();
            }
            localDeposit.restoreSum(-this.sum);
          }
          else
          {
            localObject = "余额不足！";
            return (String)localObject;
          }
        }
        else
          localObject = "账户已经不存在,不能操作！";
        break;
      case 11:
      case 13:
        localObject = Audit.getAuditByVid(this.id).delete();
        break;
      case 37:
        new Deposit(this.deposit_from).restoreSum(-this.sum);
      }
      deleteRow(this.id);
      localObject = "操作成功完成！";
      break;
    case 1:
    case 2:
      localObject = "不允许直接删除.请删除对应收支流水！";
      break;
    case 4:
    case 5:
      localObject = "请删除对应投资交易流水!";
      break;
    case 14:
    case 15:
    case 16:
    case 17:
    case 18:
    case 19:
    case 20:
    case 21:
    case 22:
      localObject = "请删除对应借贷流水!";
      break;
    case 23:
    case 24:
      localObject = "请删除对应保险操作流水!";
      break;
    case 25:
    case 26:
    case 27:
      localObject = "请删除对应债券交易流水!";
      break;
    case 28:
    case 29:
    case 30:
      localObject = "请删除对应基金交易流水!";
      break;
    case 31:
    case 32:
    case 33:
    case 34:
    case 40:
      localObject = "请删除对应出差流水!";
      break;
    case 35:
    case 36:
    case 41:
      localObject = "请删除对应资产交易流水!";
      break;
    }
    label655: return (String)localObject;
  }
	
	}
/*




  public static String getNameOfDepositTo(int paramInt1, int paramInt2)
  {
    String str;
    switch (paramInt1)
    {
    case 37:
    default:
      str = "";
      break;
    case 1:
    case 2:
      str = "";
      break;
    case 3:
      str = new Deposit(paramInt2).name;
      break;
    case 4:
    case 5:
      str = new InvestAccount(paramInt2).name;
      break;
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
      str = new Deposit(paramInt2).name;
      break;
    case 14:
    case 15:
    case 16:
    case 17:
    case 18:
    case 19:
    case 20:
    case 21:
    case 22:
      str = new Credit(paramInt2).name;
      break;
    case 23:
    case 24:
      str = new Insurance(paramInt2).name;
      break;
    case 25:
    case 26:
    case 27:
      str = new Funds(paramInt2).name;
      break;
    case 28:
    case 29:
    case 30:
      str = new Bond(paramInt2).name;
      break;
    case 31:
    case 32:
    case 33:
    case 34:
    case 40:
      str = new Evection(paramInt2).city;
      break;
    case 35:
    case 36:
    case 41:
      str = new Asset(paramInt2).name;
      break;
    case 38:
    case 39:
      str = new Credit(paramInt2).name;
    }
    return str;
  }



 

  public String deleteAssetAudit()
  {
    Deposit localDeposit = new Deposit(this.deposit_from);
    Object localObject = new Asset(this.deposit_to);
    if (localDeposit.flag != -1)
    {
      switch (this.kmid)
      {
      default:
        localObject = "不是资产交易流水!";
        break;
      case 35:
        if (((Asset)localObject).flag != -1)
        {
          ((Asset)localObject).delete();
          localObject = "操作成功完成！";
          break label199;
        }
        localObject = "账户已经不存在,不能操作！";
        break;
      case 36:
        if (!localDeposit.isOverSum(this.sum))
        {
          localDeposit.restoreSum(-this.sum);
          ((Asset)localObject).flag = 0;
          ((Asset)localObject).save();
          Account.addAccountSum(12, ((Asset)localObject).value);
          Report.modifyReportSum(this.date, 318, -this.sum);
        }
        else
        {
          localObject = "余额不足！";
        }
        break;
      case 41:
        Audit.getAuditByVid(this.id).deleteWithForce();
      }
      deleteRow(this.id);
      localObject = "操作成功完成！";
    }
    else
    {
      localObject = "账户已经不存在,不能操作！";
    }
    label199: return (String)localObject;
  }


  public String getNameOfDepositTo()
  {
    return getNameOfDepositTo(this.kmid, this.deposit_to);
  }

  public String getVirementType()
  {
    return getVirementType(this.kmid);
  }



}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Virement
 * JD-Core Version:    0.6.0
 */
