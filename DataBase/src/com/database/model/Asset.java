/*
 * 资产
 */
package com.database.model;

import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.Convertor;
import com.database.util.DBTool;
import com.database.util.MyDate;
import com.database.ListAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Asset
{
    public static final int AUDIT_PROJECT = 4;
    public static final int EVECTION = 0;
    public static final int PROJECT = 2;
    public String city;
    public String content = null;
    public MyDate date;
    public short flag;
    public int id;
    public String[] projectkm = null;
    public Date real_date;
    public byte[] stat = null;
    public float sum;
    public float nub;
    public float bulky;
    public short type;
    //创建
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE asset(id integer  PRIMARY KEY AUTOINCREMENT,type smallint,nub float,bulky float,sum flaot,date integer,city varchar(32),flag smallint ,stat blob,content varchar(80),projectkm varchar(250));");
	}
	//--------项目显示----因为方法中还有cursor产生错
	public static void getAdapter(ListAdapter listadapter, List<Integer> index)
	{
		 index.clear();
		 listadapter.clear();
		 ArrayList listdata = new ArrayList();
		 //标题
		 String[] til_ass = new String[4];
		 til_ass[0] = "名称";
		 til_ass[1] = "结余数量";
		 til_ass[2] = "结余金额";
		 til_ass[3] = "时间";
		 listdata.add(til_ass);
		 //列表适配器用它排列列表
		 index.add(Integer.valueOf(2147483394));	
		 
		 //查询类型
		 //第一参查询条件
		 //第二参排序ase升序，dese降序
		 Cursor cursor_typlno = AssetKm.getRows(" flag=1 and pid=0", "");	
		 //查询类型总行数
		 int namenumber=cursor_typlno.getCount();	 
		 //关闭查询
		 cursor_typlno.close();	
		 for (int i = 0; i <= namenumber; i++)
		 {
			 
			 //查询类型中包含的项目,注意and前面有一个空格
			 Cursor cursor_asset = getRows("type="+i + " and flag=0", "city asc");
			 //如果不是空
			 if (cursor_asset.getCount() != 0)
			 {
				 
				 
				 for (cursor_asset.moveToFirst();!cursor_asset.isAfterLast(); cursor_asset.moveToNext())
		         {
					 //创建数据字符串集
					 String[] data_ass = new String[4];
					 data_ass[0] = cursor_asset.getString(6);//名称
					 data_ass[1] = Convertor.sumToStringf(cursor_asset.getFloat(2));//数量
					 data_ass[2] = Convertor.sumToStringf(cursor_asset.getFloat(4));//金额
					 data_ass[3] = new MyDate(new Date(cursor_asset.getLong(5))).toShortString();//时间
					 //将数据字符串添加到列表
					 listdata.add(data_ass);
					 //将查询的ID号添加列表序列
					 index.add(Integer.valueOf(cursor_asset.getInt(0)));

		         }
				 AssetKm aKm = new AssetKm(i);
				 float hjnub = 0.0f;
				 float hjsum = 0.0f;
		         for (cursor_asset.moveToFirst();!cursor_asset.isAfterLast();  cursor_asset.moveToNext())
		         {
		        	 //统计
		        	 hjnub += cursor_asset.getFloat(2);
					 hjsum += cursor_asset.getFloat(4);
		           
		         }
				 
		         String[] hj =  new String[4];
				hj[0] = aKm.name.toString()+"合计";
				hj[1] =Convertor.sumToStringf(hjnub);
				hj[2] =Convertor.sumToStringf(hjsum);
				hj[3] = MyDate.now().toShortString();
				listdata.add(hj);				
				index.add(Integer.valueOf(2147483394));	
                //关闭查询 
				cursor_asset.close();
			 }
			 else
			 {
				 //记住一定要关闭查询否则会死机
				 cursor_asset.close();	
			 }
		 }
		 
		 listadapter.append(listdata);
		 listadapter.notifyDataSetChanged();
	}
	public static void getHideAdapter(ListAdapter listadapter, List<Integer> index)
	{
		 index.clear();
		 listadapter.clear();
		 ArrayList listdata = new ArrayList();
		 //标题
		 String[] til_ass = new String[4];
		 til_ass[0] = "名称";
		 til_ass[1] = "结余数量";
		 til_ass[2] = "结余金额";
		 til_ass[3] = "时间";
		 listdata.add(til_ass);
		 //列表适配器用它排列列表
		 index.add(Integer.valueOf(2147483394));	
		Cursor cursor_asset = getRows("type=0 and flag=1", "city asc");
		//如果不是空
		 if (cursor_asset.getCount() != 0)
		 {
			for (cursor_asset.moveToFirst();!cursor_asset.isAfterLast(); cursor_asset.moveToNext())
		    {
				//创建数据字符串集
				String[] data_ass = new String[4];
				data_ass[0] = cursor_asset.getString(6);//名称
				data_ass[1] = Convertor.sumToStringf(cursor_asset.getFloat(2));//数量
				data_ass[2] = Convertor.sumToStringf(cursor_asset.getFloat(4));//金额
				data_ass[3] = new MyDate(new Date(cursor_asset.getLong(5))).toShortString();//时间
				//将数据字符串添加到列表
			    listdata.add(data_ass);
				//将查询的ID号添加列表序列
				index.add(Integer.valueOf(cursor_asset.getInt(0)));

		    }
			
            //关闭查询 
				cursor_asset.close();
		    }
			else
			{
			    //记住一定要关闭查询否则会死机
				cursor_asset.close();	
			}
		
		 
		 listadapter.append(listdata);
		 listadapter.notifyDataSetChanged();
	}
	//获取行
	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("asset", getColumnString(), paramString1, null, null, null, paramString2);
	}

    //获取列
	public static String[] getColumnString()
	{
		String[] column = new String[11];
		column[0] = "id";
		column[1] = "type";
		column[2] = "nub";
		column[3] = "bulky";
		column[4] = "sum";
		column[5] = "date";
		column[6] = "city";
		column[7] = "flag";
		column[8] = "stat";
		column[9] = "content";
		column[10] = "projectkm";
		return column;
	}
	
	
	
	//--------------------
	
	//新增资产
	public static String insertAsset(int type, int flag, String city, float bulky, Date dateinput,String content)
	{
		String note;
		if ((city.length() != 0)||(bulky != 0))
		{
			ContentValues cv = new ContentValues();
			cv.put("type", type);
			cv.put("nub", Float.valueOf(0));
			cv.put("bulky", bulky);
			cv.put("sum", Float.valueOf(0));
			cv.put("date", Long.valueOf(dateinput.getTime()));
			cv.put("city", city);
			cv.put("flag", flag);
			cv.put("stat", new byte[296]);
			cv.put("content", content);
			cv.put("projectkm", getDefaultProjectKm());
			
			DBTool.database.insert("asset", "id", cv);
			note = "操作成功完成！";
		}
		else
		{
			note = "<名称>和<单价>不允许为空！";
		}
		return note;
	}
	static String getDefaultProjectKm()
	{
		return "收入,,,,,,,,,,,,,,,,支出,,,,,,,,,,,,,,,,";
	}
	//修改资产
	public Asset(int id)
	{
		this.id = id;
		Cursor localCursor = DBTool.database.query("asset", getColumnString(), "id=" + id, null, null, null, null);
		localCursor.moveToFirst();
		reset(localCursor);
		localCursor.close();
	}
	void reset(Cursor cursor)
	{
		this.id = cursor.getInt(0);
		this.type = cursor.getShort(1);
		this.nub = cursor.getFloat(2);
		this.bulky = cursor.getFloat(3);
		this.sum = cursor.getFloat(4);
		this.real_date = new Date(cursor.getLong(5));
		this.date = new MyDate(this.real_date);
		this.city = cursor.getString(6);
		this.flag = cursor.getShort(7);
		this.stat = cursor.getBlob(8);
		this.content = cursor.getString(9);
		this.projectkm = Convertor.split(cursor.getString(10), ",");
	}
	
	//修改
	public String modify(int type, String city, int flag, float bulky, Date dateinput, String content)
	{
		String str;
		//如果名称和单价不是0
		if ((city.length() != 0)||(bulky != 0))
		{
			this.type = (short)type;
			this.city = city;
			this.flag = (short)flag;
			this.bulky = bulky;
			this.content = content;
			if (!this.date.equals(dateinput))
			{
				this.real_date = dateinput;
				this.date = new MyDate(dateinput);
			}
			save();
			str = "操作成功完成！";
		}
		else
		{
			str = "<名称>和<单价>不允许为空！";
		}
		return str;
	}
	
	void save()
	{
		ContentValues cv = new ContentValues();
		cv.put("city", this.city);
		cv.put("type", this.type);
		cv.put("nub", this.nub);
		cv.put("bulky", this.bulky);
		cv.put("sum", this.sum);
		cv.put("date", Long.valueOf(this.real_date.getTime()));
		cv.put("flag", this.flag);
		cv.put("stat", this.stat);
		cv.put("content", this.content);
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < 32; i++)
			localStringBuffer.append(this.projectkm[i]).append(",");
		cv.put("projectkm", localStringBuffer.toString());
		
		DBTool.database.update("asset", cv, "id=" + this.id, null);
	}
	
	//删除
	public String delete()
	{
		String note;

		if (isFinished())
		{
			this.flag = (short)(-this.flag);
			save();
		    note = "操作成功完成！";
			return note;
		}
		else
		{
			Cursor localObject = DBTool.database.query("assetaudit", AssetAudit.getColumnString(), "eid=" + this.id, null, null, null, null);
			if (localObject.getCount() > 0)
			{    
			    localObject.close();
			    note = "未结束并且存在出差流水,不允许删除！";
				return note;
			}
			localObject.close();
			DBTool.database.execSQL("delete from asset where id=" + this.id);
		    note = "操作成功完成！";
			return note;
		}

	}
	
	public boolean isFinished()
	{
		boolean bool = true;
		if (this.flag % 2 != 1)
			bool = false;
		return bool;
	}
	
	//记帐-------------
	
	public static boolean hasEvection(String paramString)
	{
		Cursor localCursor = getRows(paramString, null);
		boolean bool = false;
		if (localCursor.getCount() > 0)
			bool = true;
		localCursor.close();
		return bool;
	}
	//获得资产列表
	//FormActivity.addAssetAuditForSelectForm调用
	public static List<String> getAssetList(String sql, List<Integer> index)
	{
		index.clear();
		ArrayList list = new ArrayList();
		Cursor cur = getRows(sql, "city asc");

		for (cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())
		{
			int i=1;
			list.add(cur.getString(6));
			index.add(Integer.valueOf(i/*localCursor.getInt(0)*/));
            i+=1;
		}
		cur.close();
		return list;
	}
	/*public static List<String> getEvectionList(String sql, List<Integer> index) {
        index.clear();
        List<String> list = new ArrayList();
        Cursor cur = getRows(sql, "id desc");
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            list.add(cur.getString(3));
            index.add(Integer.valueOf(cur.getInt(EVECTION)));
            cur.moveToNext();
        }
        cur.close();
        return list;
    }*/

	//获得类型列表
	public List<String> getAssetAuditTypeList()
	{
		ArrayList localArrayList = new ArrayList();
		localArrayList.add("收入");
		localArrayList.add("支出");
		return localArrayList;
	}
	//获得科目列表
	public List<String> getAssetKmList(int paramInt, List<Integer> paramList)
	{
		paramList.clear();
		ArrayList localObject = new ArrayList();
		
		int i = 0;
		if (paramInt == 1)
			i = 16;
		
		for (int j = 0; j < 16; j++)
		{
			if (this.projectkm[(j + i)].length() <= 0)
				continue;
			localObject.add(this.projectkm[(j + i)]);
			paramList.add(Integer.valueOf(5 + (j + i)));
		}
 
		return localObject;
	}
	
	
	//记帐
	
	public String insertAudit(int kmid, float nub,float sum, int depositid, String content, Date dateinput)
    {
        String str;
        if (nub > 0L)
        {
			addSum(kmid, nub, sum);
			AssetAudit.insert(this.id, kmid, nub,sum, depositid, dateinput,content,0);
			save();
			str = "操作成功完成！";
        }
        else
            str = "金额错误！";
        return str;
    }
	
	public static boolean isIncome(int kmid)
	{
		boolean bool;
		if (kmid <= 20)
			bool = true;
		else
			bool = false;
		return bool;
	}

	void addSum(int kmid,float nub, float sum)
	{
		if (!isIncome(kmid))
		{
			this.nub -= nub;
			this.sum -= sum;
		}	
		else
		{
		    this.nub = (nub + this.nub);
			this.sum = (sum + this.sum);
		}
		Convertor.fromFloat(sum + Convertor.toLong(this.stat, kmid * 8, 8), this.stat, kmid * 8);
	}
	
	//--------
	//获得科目名称
	public String getKmName(int kmid)
	{
		String str;
		if (kmid != 1)
			str = this.projectkm[(kmid - 5)];
		else
			str = "项目结束";
		return str;
	}
    //获得资产名称
	public static String getName(int id)
	{
		Asset asset = new Asset(id);
		return asset.city;
	}
	
	public MyDate getEndDate()
    {
        Cursor localCursor = DBTool.database.query("assetaudit", getColumnString(), "eid=" + this.id + " and kmid=" + 1, null, null, null, null);
        if (localCursor.getCount() == 0)
        {
            localCursor.close();
            localCursor = DBTool.database.query("assetaudit", getColumnString(), "eid=" + this.id, null, null, null, "date desc");
            if (localCursor.getCount() == 0);
		}
		else
		{
			localCursor.moveToFirst();
			MyDate localFDate = new MyDate(new Date(localCursor.getLong(5)));
			localCursor.close();
			return localFDate;
		}
		localCursor.close();
		MyDate localFDate = MyDate.now();
		return localFDate;
    }
	}
/*	//------------------
	


	 /*	public String insertIncome(int kmid, long sum, int depositid, String paramString, Date dateinput)
	 {
	 //	new Deposit(depositid).addSum(sum);
	 addSum(kmid, sum);
	 //获得转帐最大（最后）id
	 //		int i = Virement.insert(40, sum, depositid, this.id, dateinput);
	 int i = 0;
	 AssetAudit.insert(this.id, kmid, sum, depositid, i, paramString, dateinput);
	 save();
	 return "操作成功完成！";
	 }

	 public String insertOutcome(int paramInt1, long paramLong, int paramInt2, String paramString, Date paramDate)
	 {
	 Deposit localDeposit = new Deposit(paramInt2);
	 String str;
	 if (!localDeposit.isOverSum(paramLong))
	 {
	 localDeposit.addSum(-paramLong);
	 addSum(paramInt1, paramLong);
	 //		int i = Virement.insert(34, paramLong, paramInt2, this.id, paramDate);
	 int i = 0;
	 //		Audit.insert(273, l, 0, paramDate, str2 + this.city);
	 AssetAudit.insert(this.id, paramInt1, paramLong, paramInt2, i, paramString, paramDate);
	 save();
	 str = "操作成功完成！";
	 }
	 else
	 {
	 str = "余额不足！";
	 }
	 return str;
	 }



	











	public void fillProjectKmList(ListAdapter paramFinanceAdapter, List<Integer> paramList)
	{
		paramList.clear();
		paramFinanceAdapter.clear();
		int[] localObject = new int[2];
		localObject[0] = 60;
		localObject[1] = 260;

		paramFinanceAdapter.setLayout(localObject);
		paramList.add(Integer.valueOf(2147483394));

		ArrayList arraylist = new ArrayList();
		String[] arrayOfString1 = new String[1];
		arrayOfString1[0] = "科目名称";
		arraylist.add(arrayOfString1);

		int j;
		//	if (!isProject())
		j = 16;
		//	else
		j = 32;
		for (int i = 0; i < j; i++)
		{
			//	if (isProject())
			if (i != 0)
			{
				if (i == 16)
				{
					String[] arrayOfString2 = new String[1];
					arrayOfString2[0] = "收入";
					arraylist.add(arrayOfString2);
					paramList.add(Integer.valueOf(-1));
				}
			}
			else
			{
				String[] arrayOfString2 = new String[1];
				arrayOfString2[0] = "支出";
				arraylist.add(arrayOfString2);
				paramList.add(Integer.valueOf(-1));
			}
			if (this.projectkm[i].length() <= 0)
				continue;
			String[] arrayOfString2 = new String[1];
			arrayOfString2[0] = ("    " + this.projectkm[i]);
			arraylist.add(arrayOfString2);
			paramList.add(Integer.valueOf(i + 5));
		}
		paramFinanceAdapter.append(arraylist);
	}

	public String addProjectkm(int paramInt, String paramString)
	{
		String str;
		if (paramString.length() != 0)
		{
			if (paramString.length() <= 7)
			{
				int i = 0;
				if (paramInt == 1)
					i = 16;
				int j = 0;
				while (j < 16)
				{
					if (this.projectkm[(j + i)].length() != 0)
					{
						j++;
						continue;
					}
					this.projectkm[(j + i)] = paramString;
					save();
					str = "操作成功完成！";
					return str;
					//		break label92;
				}
				str = "科目数达到上限!";
			}
			else
			{
				str = "名称不能太长!";
			}
		}
		else
			str = "名称不能为空!";
		return str;
	}
	public String modifyProjectKm(int paramInt, String paramString)
	{
		String str;
		if (paramString.length() != 0)
		{
			if (paramString.length() <= 7)
			{
				this.projectkm[(paramInt - 5)] = paramString;
				save();
				str = "操作成功完成！";
			}
			else
			{
				str = "名称不能太长!";
			}
		}
		else
			str = "名称不能为空!";
		return str;
	}
	public void fillStatList(ListAdapter paramFinanceAdapter, List<Integer> paramList)
	{
		paramList.clear();
		paramList.add(Integer.valueOf(2147483394));
		paramFinanceAdapter.clear();
		ArrayList localArrayList = new ArrayList();
		String[] arrayOfString1 = new String[2];
		arrayOfString1[0] = "科目";
		arrayOfString1[1] = "金额";
		localArrayList.add(arrayOfString1);
		long l1 = 0L;
		for (int j = 5; j < 21; j++)
		{
			long l4 = getSum(j);
			if (l4 == 0L)
				continue;
			l1 += l4;
			String[] arrayOfString8 = new String[2];
			arrayOfString8[0] = getKmName(j);
			arrayOfString8[1] = Convertor.sumToString(l4);
			localArrayList.add(arrayOfString8);
		}

		if (l1 > 0L)
		{
			//	if (isAuditProject())
			//	{
			//		arrayOfString4 = new String[2];
			//		arrayOfString4[0] = "------";
			//		arrayOfString4[1] = "------------";
			//		localArrayList.add(arrayOfString4);
			//	}
			String[] arrayOfString4 = new String[2];
			arrayOfString4[0] = "支出合计";
			arrayOfString4[1] = Convertor.sumToString(l1);
			localArrayList.add(arrayOfString4);
			//	if (!isAuditProject())
			//	{
			String[] arrayOfString2 = new String[2];
			arrayOfString2[0] = "------";
			arrayOfString2[1] = "------------";
			localArrayList.add(arrayOfString2);
			//		}
		}
		//	if (!isEvection())
		{
			//		if (isProject())
			{
				long l2 = 0L;
				for (int k = 21; k < 37; k++)
				{
					long l5 = getSum(k);
					if (l5 == 0L)
						continue;
					l2 += l5;
					String[] arrayOfString7 = new String[2];
					arrayOfString7[0] = getKmName(k);
					arrayOfString7[1] = Convertor.sumToString(l5);
					localArrayList.add(arrayOfString7);
				}
				if (l2 > 0L)
				{
					String[] arrayOfString5 = new String[2];
					arrayOfString5[0] = "收入合计";
					arrayOfString5[1] = Convertor.sumToString(l2);
					localArrayList.add(arrayOfString5);
					String[] arrayOfString3 = new String[2];
					arrayOfString3[0] = "------";
					arrayOfString3[1] = "------------";
					localArrayList.add(arrayOfString3);
				}
			}
		}
		/*	else
		 for (int i = 2; i < 5; i++)
		 {
		 long l3 = getSum(i);
		 if (l3 == 0L)
		 continue;
		 String[] arrayOfString6 = new String[2];
		 arrayOfString6[0] = getKmName(i);
		 arrayOfString6[1] = Convertor.sumToString(l3);
		 localArrayList.add(arrayOfString6);
		 }*/
		//	if (!isAuditProject())
		//	{
/*		String[] localObject = new String[2];
		localObject[0] = "目前结余";
		localObject[1] = Convertor.sumToString(this.sum);
		localArrayList.add(localObject);
		//	}

		int[] localObject1 = new int[3];
		localObject1[0] = 40;
		localObject1[1] = 100;
		localObject1[2] = 180;
		paramFinanceAdapter.setLayout(localObject1);
		paramFinanceAdapter.append(localArrayList);
	}

	public long getSum(int paramInt)
	{
		return Convertor.toLong(this.stat, paramInt * 8, 8);
	}
	public String deleteProjectKm(int paramInt)
	{
		String str;
		if (!kmIsUsed(paramInt))
		{
			this.projectkm[(paramInt - 5)] = "";
			save();
			str = "操作成功完成！";
		}
		else
		{
			str = "科目被使用,不能删除!";
		}
		return str;
	}
	boolean kmIsUsed(int paramInt)
	{
		Cursor localCursor = EvectionAudit.getRows("eid=" + this.id + " and kmid=" + paramInt, null);
		boolean bool = false;
		if (localCursor.getCount() > 0)
			bool = true;
		localCursor.close();
		return bool;
	}
	public String finish(String paramString, Date paramDate)
	{
		String str;
//		if (!isAuditProject())
		str = evectionFinish(paramString, paramDate);
//		else
//			str = auditProjectFinish(paramString, paramDate);
		return str;
	}
	String evectionFinish(String paramString, Date paramDate)
	{
		String str1;
		//	if (getSum(2) == getSum(4))
		//	{
		long l;
		String str2;
		if (this.sum <= 0L)
		{
			if (this.sum < 0L)
			{
				l = -this.sum;
				//	if (!isEvection())
				str2 = "项目结余:";
				//	else
				//		str2 = "出差结余:";
				Audit.insert(273, l, 0, paramDate, str2 + this.city);
			}
		}
		else
		{
			l = this.sum;
			//	if (!isEvection())
			str2 = "项目结余:";
			//	else
			//		str2 = "出差结余:";
			Audit.insert(294, l, 0, paramDate, str2 + this.city);
		}
		int i = 0;
		if (this.sum != 0L)
			i = DBTool.getMaxId("audit");
		EvectionAudit.insert(this.id, 1, this.sum, 0, i, paramString, paramDate);
		this.flag = (short)(1 + this.flag);
		save();
		str1 = "操作成功完成！";
//		}
//		else
//		{
//			str1 = "借款没有还清,不能结束！";
//		}
		return str1;
	}

}
/*



/*








  String auditProjectFinish(String paramString, Date paramDate)
  {
    this.flag = (1 + this.flag);
    save();
    return "操作成功完成！";
  }














 



  public String insertAuditProjectOutcome(int paramInt1, long paramLong, int paramInt2, String paramString, Date paramDate)
  {
    Deposit localDeposit = new Deposit(paramInt2);
    String str;
    if (!localDeposit.isOverSum(paramLong))
    {
      localDeposit.addSum(-paramLong);
      addSum(paramInt1, paramLong);
      int i = Audit.insertSystemAudit(265, paramLong, paramInt2, Virement.insert(34, paramLong, paramInt2, this.id, paramDate), paramDate, paramString);
      EvectionAudit.insert(this.id, paramInt1, paramLong, paramInt2, i, paramString, paramDate);
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "余额不足！";
    }
    return str;
  }

  public String insertBorrow(long paramLong, int paramInt, String paramString, Date paramDate)
  {
    new Deposit(paramInt).addSum(paramLong);
    addSum(2, paramLong);
    int i = Virement.insert(31, paramLong, paramInt, this.id, paramDate);
    Account.addAccountSum(5, paramLong);
    EvectionAudit.insert(this.id, 2, paramLong, paramInt, i, paramString, paramDate);
    save();
    return "操作成功完成！";
  }


  public String insertReimburse(long paramLong, int paramInt, String paramString, Date paramDate)
  {
    new Deposit(paramInt).addSum(paramLong);
    addSum(3, paramLong);
    int i = Virement.insert(32, paramLong, paramInt, this.id, paramDate);
    EvectionAudit.insert(this.id, 3, paramLong, paramInt, i, paramString, paramDate);
    save();
    return "操作成功完成！";
  }

  public String insertReturn(long paramLong, int paramInt, String paramString, Date paramDate)
  {
    Deposit localDeposit = new Deposit(paramInt);
    String str;
    if (!localDeposit.isOverSum(paramLong))
    {
      localDeposit.addSum(-paramLong);
      addSum(4, paramLong);
      Account.addAccountSum(5, -paramLong);
      int i = Virement.insert(33, paramLong, paramInt, this.id, paramDate);
      EvectionAudit.insert(this.id, 4, paramLong, paramInt, i, paramString, paramDate);
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "余额不足！";
    }
    return str;
  }

  public boolean isAuditProject()
  {
    int i;
    if ((this.flag == 4) || (this.flag == 5) || (this.flag == -5))
      i = 1;
    else
      i = 0;
    return i;
  }

  public boolean isEvection()
  {
    int i = 1;
    if ((this.flag != 0) && (this.flag != i) && (this.flag != -1))
      i = 0;
    return i;
  }

 

  public boolean isProject()
  {
    int i;
    if ((this.flag == 2) || (this.flag == 3) || (this.flag == -3))
      i = 1;
    else
      i = 0;
    return i;
  }


  }





}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Evection
 * JD-Core Version:    0.6.0
 */

