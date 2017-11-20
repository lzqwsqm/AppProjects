package com.database.model;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.DBTool;
import com.database.util.MyDate;
import java.util.Date;

public class CreditAudit
{
  public static final byte CREDIT_AUDIT_BORROW = 2;
  public static final byte CREDIT_AUDIT_BORROW_BAD = 8;
  public static final byte CREDIT_AUDIT_BORROW_INTEREST = 11;
  public static final byte CREDIT_AUDIT_BORROW_RETURN = 5;
  public static final byte CREDIT_AUDIT_CREDIT = 3;
  public static final byte CREDIT_AUDIT_CREDIT_AJ = 4;
  public static final byte CREDIT_AUDIT_CREDIT_BAD = 10;
  public static final byte CREDIT_AUDIT_CREDIT_INTEREST = 13;
  public static final byte CREDIT_AUDIT_CREDIT_RETURN = 7;
  public static String[] CREDIT_AUDIT_KM = new String[14];
  public static final byte CREDIT_AUDIT_LEND = 1;
  public static final byte CREDIT_AUDIT_LEND_BAD = 9;
  public static final byte CREDIT_AUDIT_LEND_INTEREST = 12;
  public static final byte CREDIT_AUDIT_LEND_RETURN = 6;
  public String content;
  public short credit_id = 0;
  public MyDate date = null;
  public short deposit_id = 0;
  public int id = 0;
  public Date real_date = null;
  public long sum = 0L;
  public short type = 0;
  public int vid = 0;

  static
  {
    CREDIT_AUDIT_KM[0] = "借出";
    CREDIT_AUDIT_KM[1] = "借出";
    CREDIT_AUDIT_KM[2] = "借入";
    CREDIT_AUDIT_KM[3] = "贷款";
    CREDIT_AUDIT_KM[4] = "按揭贷款";
    CREDIT_AUDIT_KM[5] = "还借款";
    CREDIT_AUDIT_KM[6] = "收欠款";
    CREDIT_AUDIT_KM[7] = "还贷款";
    CREDIT_AUDIT_KM[8] = "借入核销";
    CREDIT_AUDIT_KM[9] = "借出核销";
    CREDIT_AUDIT_KM[10] = "贷款核销";
    CREDIT_AUDIT_KM[11] = "借入利息";
    CREDIT_AUDIT_KM[12] = "借出利息";
    CREDIT_AUDIT_KM[13] = "贷款利息";
  }
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE creditaudit(id integer PRIMARY KEY ,type smallint,sum int ,credit_id smallint,deposit_id smallint,vid int,date integer,content varchar(80));");
	}
	//

	public static String insert(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Date paramDate, String paramString)
	{
		String localObject;
		if (paramLong > 0L)
		{
			StringBuffer localObject1 = new StringBuffer();
			localObject1.append("insert into ").append("creditaudit").append(" values(null,").append(paramInt1).append(",").append(paramLong).append(",").append(paramInt2).append(",").append(paramInt3).append(",").append(paramInt4).append(",").append(paramDate.getTime()).append(",'").append(paramString).append("');");
			DBTool.database.execSQL(localObject1.toString());
			localObject = "操作成功完成！";
		}
		else
		{
			localObject = "金额错误！";
		}
		return localObject;
	}
	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("creditaudit", getColumnString(), paramString1, null, null, null, paramString2);
	}
	public static String[] getColumnString()
	{
		String[] arrayOfString = new String[8];
		arrayOfString[0] = "id";
		arrayOfString[1] = "type";
		arrayOfString[2] = "sum";
		arrayOfString[3] = "credit_id";
		arrayOfString[4] = "deposit_id";
		arrayOfString[5] = "vid";
		arrayOfString[6] = "date";
		arrayOfString[7] = "content";
		return arrayOfString;
	}
	public static String getTypeName(int paramInt)
	{
		return CREDIT_AUDIT_KM[paramInt];
	}
	public CreditAudit(int paramInt)
	{
		Cursor localCursor = DBTool.database.query("creditaudit", getColumnString(), "id=" + paramInt, null, null, null, null);
		localCursor.moveToFirst();
		this.id = paramInt;
		this.type = localCursor.getShort(1);
		this.sum = localCursor.getLong(2);
		this.credit_id = localCursor.getShort(3);
		this.deposit_id = localCursor.getShort(4);
		this.vid = localCursor.getInt(5);
		this.real_date = new Date(localCursor.getLong(6));
		this.date = new MyDate(this.real_date);
		this.content = localCursor.getString(7);
		localCursor.close();
	}
	
	public String getTypeName()
	{
		return CREDIT_AUDIT_KM[this.type];
	}

	public String delete()
	{
		Deposit localDeposit = new Deposit(this.deposit_id);
		Object localObject = new Credit(this.credit_id);
		switch (this.type)
		{
			case 1://
				if (((Credit)localObject).sum >= this.sum)
				{
					localDeposit.restoreSum(this.sum);
					((Credit)localObject).addSum(-this.sum);
					Report.modifyReportSum(this.date, 305, -this.sum);
				}
				else
				{
					localObject = "余额不足！";
				}
				break;
			case 2:
	//		case 3:
	//		case 4:
				if ((!localDeposit.isOverSum(this.sum)) && (((Credit)localObject).sum >= this.sum))
				{
					localDeposit.restoreSum(-this.sum);
					((Credit)localObject).addSum(-this.sum);
					Report.modifyReportSum(this.date, 306, -this.sum);
				}
				else
				{
					localObject = "余额不足！";
				}
				break;
			case 5:
				localDeposit.restoreSum(this.sum);
				((Credit)localObject).addSum(this.sum);
				Report.modifyReportSum(this.date, 307, -this.sum);
				break;
			case 6:
				if (!localDeposit.isOverSum(this.sum))
				{
					localDeposit.restoreSum(-this.sum);
					((Credit)localObject).addSum(this.sum);
					Report.modifyReportSum(this.date, 304, -this.sum);
				}
				else
				{
					localObject = "余额不足！";
				}
				break;
		/*	case 7:
				switch (((Credit)localObject).type)
				{
					case 2:
						localDeposit.restoreSum(this.sum);
						((Credit)localObject).addSum(this.sum);
						break;
					case 3:
					case 4:
						new Audit(this.vid).deleteWithForce();
						this.vid = 0;
						((Credit)localObject).addSum(this.sum);
				}
				Report.modifyReportSum(this.date, 307, -this.sum);
				break;*/
			case 8:
			case 9:
	//		case 10:
				new Audit(this.vid).deleteWithoutVirement();
				this.vid = 0;
				((Credit)localObject).addSum(this.sum);
				if (this.type != 9)
					Report.modifyReportSum(this.date, 307, -this.sum);
				else
					Report.modifyReportSum(this.date, 304, -this.sum);
				break;
			case 11:
	//		case 13:
				new Audit(this.vid).deleteWithForce();
				this.vid = 0;
				break;
			case 12:
				if (localDeposit.isOverSum(this.sum))
				{
				    localObject = "余额不足！";
			//		break label617;
				}
				else
				{
				    new Audit(this.vid).deleteWithForce();
				    this.vid = 0;
		        }
				break;
		}

		if (((Credit)localObject).sum != 0L)
			((Credit)localObject).flag = 0;
		else
			((Credit)localObject).flag = -1;
		((Credit)localObject).save();
		if (this.vid > 0)
			Virement.deleteRow(this.vid);
		localObject = new StringBuffer();
		((StringBuffer)localObject).append("delete from ").append("creditaudit").append(" where id=").append(this.id);
		DBTool.database.execSQL(((StringBuffer)localObject).toString());
		localObject = "操作成功完成！";
	//	break label621;
	//	label617: localObject = "余额不足！";
		return (String)localObject;
	}
	 public static int getIdByAuditId(int paramInt)
	  {
	    int i = 0;
	    Cursor localCursor = getRows("vid=" + paramInt, null);
	    if (localCursor.getCount() != 0)
	    {
	      localCursor.moveToFirst();
	      i = localCursor.getInt(i);
	      localCursor.close();
	    }
	    else
	    {
	      localCursor.close();
	    }
	    return i;
	  }
	 public static String deleteByAuditId(int paramInt)
	  {
	    int i = getIdByAuditId(paramInt);
	    String str;
	    if (i != 0)
	      str = new CreditAudit(i).delete();
	    else
	      str = "借贷流水不存在!";
	    return str;
	  }

}
/*  

 

  

 






}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.CreditAudit
 * JD-Core Version:    0.6.0
 */
