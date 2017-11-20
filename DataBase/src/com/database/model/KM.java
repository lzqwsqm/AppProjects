/*
 * 科目(字典)
 */
package com.database.model;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.DBTool;
import com.database.ListAdapter;
import java.util.ArrayList;
import java.util.List;
//import java.util.List<Ljava.lang.String;>;

public class KM
{
/*  public static final short FLOW_ASSET_IN = 318;

    public static final short REPORT_KM_NUMBER = 319;*/
    public short flag = 0;
    public short id = 0;
    public short keyflag = 0;
    public String name = null;
    public short pid = 0;
    public short rank = 0;
    //创建表
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE km(id smallint PRIMARY KEY, name varchar(16),pid smallint ,flag smallint,keyflag smallint,rank smallint);");
		initData(db);
	}
	/*
	 *先创建一个画布 
	 */
	static void initData(SQLiteDatabase db)
	{
		//创建个范围
		for (int i = 0; i < 600; i++)
			db.execSQL("insert into km values(" + (i + 1) + ",'收入',65535,2,0," + (i + 1) + ")");
		//类型
		addInitKm(db, 1, "支出", 0, 1, 1);
		addInitKm(db, 2, "收入", 0, 1, 1);
		
		//支出父科目
		addInitKm(db, 3, "日常费用", 1, 1, 0);
		addInitKm(db, 4, "生活费用", 1, 1, 0);
		addInitKm(db, 5, "物品购置", 1, 1, 0);
		addInitKm(db, 6, "出行交通", 1, 1, 0);
		addInitKm(db, 7, "休闲娱乐", 1, 1, 0);
		addInitKm(db, 8, "教育培训", 1, 1, 0);
		//支出科目
		int j = 60 + 1;
		addInitKm(db, 60, "食品类", 3, 1, 0);
		int i = j + 1;
		addInitKm(db, j, "蔬菜水果", 3, 1, 0);
		j = i + 1;
		addInitKm(db, i, "水果", 3, 1, 0);
		i = j + 1;
		addInitKm(db, j, "肉蛋鱼", 3, 1, 0);
		j = i + 1;
		addInitKm(db, i, "酒水饮料", 3, 1, 0);
		int k = j + 1;
		addInitKm(db, j, "酱醋调料", 3, 1, 0);
		i = k + 1;
		addInitKm(db, k, "干货零食", 3, 1, 0);
		j = i + 1;
		addInitKm(db, i, "物业", 4, 1, 0);
		i = j + 1;
		addInitKm(db, j, "水电煤气", 4, 1, 0);
		k = i + 1;
		addInitKm(db, i, "有线电视", 4, 1, 0);
		j = k + 1;
		addInitKm(db, k, "手机", 4, 1, 0);
		i = j + 1;
		addInitKm(db, j, "宽带", 4, 1, 0);
		j = i + 1;
		addInitKm(db, i, "固话", 4, 1, 0);
		i = j + 1;
		addInitKm(db, j, "暖气", 4, 1, 0);
		j = i + 1;
		addInitKm(db, i, "维修", 4, 1, 0);
		i = j + 1;
		addInitKm(db, j, "衣服", 5, 1, 0);
		j = i + 1;
		addInitKm(db, i, "电器", 5, 1, 0);
		i = j + 1;
		addInitKm(db, j, "家具", 5, 1, 0);
		k = i + 1;
		addInitKm(db, i, "家居用品", 5, 1, 0);
		j = k + 1;
		addInitKm(db, k, "数码", 5, 1, 0);
		i = j + 1;
		addInitKm(db, j, "器材", 5, 1, 0);
		j = i + 1;
		addInitKm(db, i, "工具", 5, 1, 0);
		i = j + 1;
		addInitKm(db, j, "汽油", 6, 1, 0);
		k = i + 1;
		addInitKm(db, i, "停车费", 6, 1, 0);
		j = k + 1;
		addInitKm(db, k, "出租车", 6, 1, 0);
		i = j + 1;
		addInitKm(db, j, "公交车", 6, 1, 0);
		j = i + 1;
		addInitKm(db, i, "保养维修", 6, 1, 0);
		k = j + 1;
		addInitKm(db, j, "保险税费", 6, 1, 0);
		i = k + 1;
		addInitKm(db, k, "过路过桥", 6, 1, 0);
		j = i + 1;
		addInitKm(db, i, "外出就餐", 7, 1, 0);
		k = j + 1;
		addInitKm(db, j, "旅游", 7, 1, 0);
		i = k + 1;
		addInitKm(db, k, "娱乐", 7, 1, 0);
		j = i + 1;
		addInitKm(db, i, "健身", 7, 1, 0);
		i = j + 1;
		addInitKm(db, j, "书籍", 8, 1, 0);
		j = i + 1;
		addInitKm(db, i, "培训", 8, 1, 0);
		i = j + 1;
		addInitKm(db, j, "考证", 8, 1, 0);
		
		//收入父科目
		addInitKm(db, 403, "==收入==",2, 1, 0);
		//收入科目
		int m = 460 + 1;
		addInitKm(db, 460, "工资", 403, 1, 0);
		int l = m + 1;
		addInitKm(db, m, "补贴", 403, 1, 0);
		m = l + 1;
		addInitKm(db, l, "福利", 403, 1, 0);
		l = m + 1;
		addInitKm(db, m, "奖金", 403, 1, 0);
		
		addSystemKm(db);
		addSystemFlowKm(db);
	}
	
	public static void addInitKm(SQLiteDatabase db, int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4)
	{
		db.execSQL("update km set name='" + paramString + "',pid=" + paramInt2 + ",flag=" + paramInt3 + ",keyflag=" + paramInt4 + " where id=" + paramInt1);
	}
	
	public static void addSystemFlowKm(SQLiteDatabase db)
	{
		addInitKm(db, 301, "投资收益", 0, 1, 1);
		addInitKm(db, 302, "资金流入", 0, 1, 1);
		addInitKm(db, 303, "资金流出", 0, 1, 1);
		addInitKm(db, 304, "债权减少", 302, 1, 1);
		addInitKm(db, 305, "债权增加", 303, 1, 1);
		addInitKm(db, 306, "债务增加", 302, 1, 1);
		addInitKm(db, 307, "偿还债务", 303, 1, 1);
		addInitKm(db, 308, "保险收入", 302, 1, 1);
		addInitKm(db, 309, "保险支出", 303, 1, 1);
		addInitKm(db, 310, "购买债券", 302, 1, 1);
		addInitKm(db, 311, "债券兑付", 303, 1, 1);
		addInitKm(db, 312, "申购基金", 302, 1, 1);
		addInitKm(db, 313, "赎回基金", 303, 1, 1);
		addInitKm(db, 314, "股票收入", 302, 1, 1);
		addInitKm(db, 315, "股票投资", 303, 1, 1);
		addInitKm(db, 316, "回收投资", 302, 1, 1);
		addInitKm(db, 317, "投资支出", 303, 1, 1);
		addInitKm(db, 318, "出售资产", 302, 1, 1);
		addInitKm(db, 319, "购买资产", 303, 1, 1);
	}
	public static void addSystemKm(SQLiteDatabase db)
	{
	//	for (int i = 0; i < 16; i++)
	//		db.execSQL("update km set keyflag=1 where id=" + (-1 + (275 - i)));
	//	for (int i = 0; i < 10; i++)
	//		db.execSQL("update km set keyflag=1 where id=" + (300 - i));
		
//		db.execSQL("update km set keyflag=1 where id=18");
		addInitKm(db, 320, "超市购物", 0, 1, 1);
//		addInitKm(db, 18, "系统科目", 2, 1, 1);
		addInitKm(db, 265, "大宗支出", 18, 1, 1);
		addInitKm(db, 266, "人情支出", 18, 1, 1);
		addInitKm(db, 267, "打牌输了", 18, 1, 1);
		addInitKm(db, 268, "按揭还款", 18, 1, 1);
		addInitKm(db, 269, "银行收费", 18, 1, 1);
		addInitKm(db, 270, "保险缴费", 18, 1, 1);
		addInitKm(db, 271, "贷款利息", 18, 1, 1);
		addInitKm(db, 272, "买彩票", 18, 1, 1);
		addInitKm(db, 273, "项目支出", 18, 1, 1);
		addInitKm(db, 274, "借出核销", 18, 1, 1);
	    //
	    addInitKm(db, 291, "存款利息", 1, 1, 1);
		addInitKm(db, 292, "借出利息", 1, 1, 1);
		addInitKm(db, 293, "贷款核销", 1, 1, 1);
		addInitKm(db, 294, "项目收入", 1, 1, 1);
		addInitKm(db, 295, "保险收益", 1, 1, 1);
		addInitKm(db, 296, "租赁收入", 1, 1, 1);
		addInitKm(db, 297, "人情收入", 1, 1, 1);
		addInitKm(db, 298, "彩票中奖", 1, 1, 1);
		addInitKm(db, 299, "打牌赢了", 1, 1, 1);
	}
	//显示
	public static void fillList(ListAdapter listadapter, List<Integer> index)
	{
        listadapter.clear();
        index.clear();
        index.add(Integer.valueOf(2147483394));
        ArrayList listdata = new ArrayList();
        //标题
	    String[] title = new String[1];
        title[0] = "科目名称";
        listdata.add(title);
        //收入
		KM km = new KM();
        km.reset(1);
        
		String[] sr = new String[1];
        sr[0] = km.name;
        listdata.add(sr);
        index.add(new Integer(km.id));
        //收入父科目
	    Cursor cursor_srf = getRows("flag=1 and keyflag=0 and pid=" + km.id, "rank");
   
		for ( cursor_srf.moveToFirst();!cursor_srf.isAfterLast(); cursor_srf.moveToNext())
        {
            String[] srf = new String[1];
            srf[0] = (" -" + cursor_srf.getString(1));
            listdata.add(srf);
			int i = cursor_srf.getInt(0);
			index.add(new Integer(i));
			//收入科目
            Cursor cursor_srkm = getRows("flag=1 and keyflag=0 and pid=" + i, "rank");

			for ( cursor_srkm.moveToFirst();!cursor_srkm.isAfterLast(); cursor_srkm.moveToNext())
            {
                String[] zckm = new String[1];
                zckm[0] = ("    " + cursor_srkm.getString(1));
                listdata.add(zckm);
		
			   index.add(new Integer(cursor_srkm.getInt(0)));

            }
            cursor_srkm.close();
        }
        cursor_srf.close();
        
		//支出
        km.reset(2);
        String[] zc = new String[1];
        zc[0] = km.name;
        listdata.add(zc);
        index.add(new Integer(km.id));
        //支出父科目
        Cursor cursor_zcf = getRows("flag=1 and keyflag=0 and pid=" + km.id, "rank");
      
        for (  cursor_zcf.moveToFirst();!cursor_zcf.isAfterLast() ;  cursor_zcf.moveToNext())
        {
            String[] zcfkm = new String[1];
            zcfkm[0] = (" -" + cursor_zcf.getString(1));
            listdata.add(zcfkm);
            int i = cursor_zcf.getInt(0);
            index.add(new Integer(i));
            //支出科目
            Cursor cursor_zckm = getRows("flag=1 and keyflag=0 and pid=" + i, "rank");
     
			for ( cursor_zckm.moveToFirst();!cursor_zckm.isAfterLast(); cursor_zckm.moveToNext())
            {
                String[] zckm = new String[1];
                zckm[0] = ("    " + cursor_zckm.getString(1));
                listdata.add(zckm);
                index.add(new Integer(cursor_zckm.getInt(0)));
       
            }
            cursor_zckm.close();
        }
        cursor_zcf.close();
        
		int[] width_layout = new int[2];
        width_layout[0] = 50;
        width_layout[1] = 200;
        listadapter.setLayout(width_layout);
        listadapter.append(listdata);
    }
	
	public KM()
	{
	}



    public KM(int id)
	{
		reset(id);
	}
	
	public void reset(int id)
	{
		Cursor localCursor = DBTool.database.query("km", getColumnString(), "id=" + id, null, null, null, null);
		localCursor.moveToFirst();
		reset(localCursor);
		localCursor.close();
	}
	
	public void reset(Cursor paramCursor)
	{
		this.id = paramCursor.getShort(0);
		this.name = paramCursor.getString(1);
		this.pid = paramCursor.getShort(2);
		this.flag = paramCursor.getShort(3);
		this.keyflag = paramCursor.getShort(4);
		this.rank = paramCursor.getShort(5);
	}
	public static String[] getColumnString()
	{
		String[] column = new String[6];
		column[0] = "id";
		column[1] = "name";
		column[2] = "pid";
		column[3] = "flag";
		column[4] = "keyflag";
		column[5] = "rank";
		return column;
	}
	//增加科目-------------
	public static List<String> getFatherKmListForAddKm(List<Integer> index)
	{
		index.clear();
	//	Cursor localCursor = getRows("id=2 or id=1 or (pid=2 and keyflag=0 and flag<2)", "rank");
		Cursor localCursor = getRows("pid<3 ", "rank");
		ArrayList localArrayList = new ArrayList();
		
		for (localCursor.moveToFirst();!localCursor.isAfterLast()	;localCursor.moveToNext())
		{
			localArrayList.add(localCursor.getString(1));
			index.add(Integer.valueOf(localCursor.getShort(0)));
		
		}
		localCursor.close();
		return localArrayList;
	}
	
	public static String addKm(String paramString, int paramInt)
    {
        String str;
        if (paramString.length() != 0)
        {
            if (!isFull(paramInt))
            {
                Cursor localCursor;
                
				if (paramInt != 1&&paramInt == 2)
                    
					localCursor = DBTool.database.query("km", getColumnString(), "flag=2 and keyflag=0 and id>=203 and id<400", null, null, null, "rank");
                else
				    //收入
                    localCursor = DBTool.database.query("km", getColumnString(), "flag=2 and keyflag=0 and id>=3 and id<202", null, null, null, "rank");
                
				localCursor.moveToFirst();
                int i = localCursor.getShort(0);
                localCursor.close();
                int j = 1;
                if (paramInt == 2 && paramInt == 1)
                    j = 0;
                DBTool.database.execSQL("update km set name='" + paramString + "',pid=" + paramInt + ",flag=" + j + " where id=" + i);
                
				if (paramInt >= 3 && paramInt >= 203)
                    DBTool.database.execSQL("update km set flag=1 where id=" + paramInt);
				str = "操作成功完成！";
            }
            else
            {
                str = "子科目达到限制,无法增加！";
            }
        }
        else
            str = "名字不允许为空！";
        return str;
    }
	
    static boolean isFull(int paramInt)
	{
		boolean bool;
		Cursor localCursor = DBTool.database.query("km", getColumnString(), "keyflag=0 and flag=1 and pid=" + paramInt, null, null, null, null);
		int i = localCursor.getCount();
		localCursor.close();
		if ((paramInt != 1) || (i < 16))
		{
			if (i + 1 < 16)
				bool = false;
			else
				bool = true;
		}
		else
			bool = false;
		return bool;
	}
	//删除-----------------
    public String delete()
	{
		String localObject;
		if ((this.pid != 2)&&(this.pid != 1) || (this.flag != 1))
		{
			Cursor cursor = DBTool.database.query("audit", Audit.getColumnString(), "kmid=" + this.id, null, null, null, null);
			if (cursor.getCount() <= 0)
			{
				cursor.close();
				DBTool.database.execSQL("update km set flag=2,pid=0 where id=" + this.id);
				if ((this.pid >= 3)&& (this.pid >= 203)&& (getChildrenCount(this.pid) == 0))
					DBTool.database.execSQL("update km set flag=0 where id=" + this.pid);
				localObject = "操作成功完成！";
			}
			else
			{
				cursor.close();
				localObject = "存在该科目流水,不允许删除！";
			}
		}
		else
		{
			localObject = "存在子科目,不允许删除！";
		}
		return localObject;
	}
	static int getChildrenCount(int paramInt)
	{
		Cursor localCursor = DBTool.database.query("km", getColumnString(), "keyflag=0 and flag=1 and pid=" + paramInt, null, null, null, null);
		int i = localCursor.getCount();
		localCursor.close();
		return i;
	}
	//修改
	public String modify(String paramString)
	{
		String str;
		if (paramString.length() != 0)
		{
			DBTool.database.execSQL("update km set name='" + paramString + "' where id=" + this.id);
			str = "操作成功完成！";
		}
		else
		{
			str = "名字不允许为空！";
		}
		return str;
	}
	
	//-------------

	public static List<String> getKmList(int paramInt, List<Integer> paramList)
	{
		paramList.clear();
		
		String localObject = " flag=1 and pid=" + paramInt;
		
		if (paramInt == 0)
			localObject = localObject + " and id<" + 3;
		else
			localObject = localObject + " and keyflag=0";
		
		Cursor localCursor = getRows(localObject, "rank");
		
		ArrayList localObject1 = new ArrayList();
	
	    for (localCursor.moveToFirst(); !localCursor.isAfterLast();	localCursor.moveToNext())
		{
			localObject1.add(localCursor.getString(1));
			//将id作为列表索引
			paramList.add(Integer.valueOf(localCursor.getShort(0)));
		
		}
		localCursor.close();
		return localObject1;
	}
	
	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("km", getColumnString(), paramString1, null, null, null, paramString2);
	}
	
    }
/*



  



 







  public static String[] getKmList(Cursor paramCursor, List<Integer> paramList)
  {
    paramList.clear();
    if (paramCursor.getCount() != 0)
    {
      arrayOfString = new String[paramCursor.getCount()];
      paramCursor.moveToFirst();
      int j;
      for (int i = 0; !paramCursor.isAfterLast(); i = j)
      {
        j = i + 1;
        arrayOfString[i] = paramCursor.getString(1);
        paramList.add(Integer.valueOf(paramCursor.getShort(0)));
        paramCursor.moveToNext();
      }
    }
    String[] arrayOfString = null;
    return arrayOfString;
  }

  public static List<String> getKmListForFindAudit(int paramInt, List<Integer> paramList)
  {
    paramList.clear();
    ArrayList localArrayList = new ArrayList();
    Object localObject;
    if (paramInt == 0)
    {
      localObject = new KM(1);
      localArrayList.add(((KM)localObject).name);
      paramList.add(Integer.valueOf(((KM)localObject).id));
      ((KM)localObject).reset(2);
      localArrayList.add(((KM)localObject).name);
      paramList.add(Integer.valueOf(((KM)localObject).id));
      localObject = "flag=1 and pid=2";
    }
    else
    {
      localObject = new KM(paramInt);
      localArrayList.add(((KM)localObject).name);
      paramList.add(Integer.valueOf(((KM)localObject).id));
      localObject = "flag=1 and pid=" + paramInt;
    }
    Cursor localCursor = getRows((String)localObject, "rank");
    localCursor.moveToFirst();
    while (!localCursor.isAfterLast())
    {
      int i = localCursor.getShort(0);
      localArrayList.add("->" + localCursor.getString(1));
      paramList.add(Integer.valueOf(i));
      localCursor.moveToNext();
    }
    localCursor.close();
    return (List<String>)localArrayList;
  }

  public static List<String> getKmListForMemo(int paramInt, List<Integer> paramList)
  {
    paramList.clear();
    ArrayList localArrayList = new ArrayList();
    if (paramInt == 0)
    {
      localObject = new KM(1);
      localArrayList.add(((KM)localObject).name);
      paramList.add(Integer.valueOf(((KM)localObject).id));
      localObject = "flag=1 and keyflag=0 and pid=2";
    }
    else
    {
      localObject = "flag=1 and keyflag=0 and pid=" + paramInt;
    }
    Object localObject = getRows((String)localObject, "rank");
    ((Cursor)localObject).moveToFirst();
    while (!((Cursor)localObject).isAfterLast())
    {
      localArrayList.add("  " + ((Cursor)localObject).getString(1));
      paramList.add(Integer.valueOf(((Cursor)localObject).getShort(0)));
      ((Cursor)localObject).moveToNext();
    }
    ((Cursor)localObject).close();
    return (List<String>)localArrayList;
  }

  public static List<String> getLotteryKmList(int paramInt, List<Integer> paramList)
  {
    paramList.clear();
    ArrayList localArrayList = new ArrayList();
    KM localKM = new KM(272);
    localArrayList.add(localKM.name);
    paramList.add(Integer.valueOf(localKM.id));
    localKM.reset(298);
    localArrayList.add(localKM.name);
    paramList.add(Integer.valueOf(localKM.id));
    return localArrayList;
  }

  public static List<String> getMajiangKmList(int paramInt, List<Integer> paramList)
  {
    paramList.clear();
    ArrayList localArrayList = new ArrayList();
    KM localKM = new KM(299);
    localArrayList.add(localKM.name);
    paramList.add(Integer.valueOf(localKM.id));
    localKM.reset(267);
    localArrayList.add(localKM.name);
    paramList.add(Integer.valueOf(localKM.id));
    return localArrayList;
  }

  public static String getNameByKmid(int paramInt)
  {
    Cursor localCursor = DBTool.database.query("km", getColumnString(), "id=" + paramInt, null, null, null, null);
    localCursor.moveToFirst();
    String str = localCursor.getString(1);
    localCursor.close();
    return str;
  }


  public static String getSelectSql(int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Object localObject;
    if (paramInt != 1)
    {
      if (paramInt != 2)
      {
        if (new KM(paramInt).pid != 2)
        {
          localObject = "kmid=" + paramInt;
        }
        else
        {
          localStringBuffer.append("kmid in(");
          localObject = DBTool.database.query("km", getColumnString(), "flag=1 and pid=" + paramInt, null, null, null, null);
          ((Cursor)localObject).moveToFirst();
          while (!((Cursor)localObject).isLast())
          {
            localStringBuffer.append(((Cursor)localObject).getShort(0)).append(",");
            ((Cursor)localObject).moveToNext();
          }
          localStringBuffer.deleteCharAt(-1 + localStringBuffer.length());
          localStringBuffer.append(")");
          ((Cursor)localObject).close();
          localObject = localStringBuffer.toString();
        }
      }
      else
        localObject = "(kmid<275 or kmid=320)";
    }
    else
      localObject = "(kmid>=275 and kmid<301)";
    return (String)localObject;
  }



  public String changeFather(int paramInt)
  {
    String str;
    if (paramInt != this.pid)
    {
      if (!isFull(paramInt))
      {
        DBTool.database.execSQL("update km set pid=" + paramInt + " where id=" + this.id);
        if ((this.pid >= 3) && (getChildrenCount(this.pid) == 0))
          DBTool.database.execSQL("update km set flag=0 where id=" + this.pid);
        DBTool.database.execSQL("update km set flag=1 where id=" + paramInt);
        Cursor localCursor = DBTool.database.query("report", Report.getColumnString(), null, null, null, null, null);
        localCursor.moveToFirst();
        Report localReport = new Report();
        while (!localCursor.isAfterLast())
        {
          localReport.reset(localCursor);
          long l = localReport.getKmSum(this.id);
          localReport.addSingleKmSum(this.pid, -l);
          localReport.addSingleKmSum(paramInt, l);
          localReport.save();
          localCursor.moveToNext();
        }
        localCursor.close();
        str = "操作成功完成！";
      }
      else
      {
        str = "子科目达到限制,无法增加！";
      }
    }
    else
      str = "与原来父科目相同！";
    return str;
  }


  public String mergeTo(int paramInt)
  {
    if (this.pid == 2)
    {
      if (new KM(paramInt).pid > 2)
        break label248;
      if (getChildrenCount(this.id) + getChildrenCount(paramInt) <= 16)
        DBTool.database.execSQL("update km set pid=" + paramInt + " where pid=" + this.id);
    }
    else
    {
      localObject = Report.getRows("", "");
      ((Cursor)localObject).moveToFirst();
      Report localReport = new Report();
      while (!((Cursor)localObject).isAfterLast())
      {
        localReport.reset((Cursor)localObject);
        localReport.setKmSum(paramInt, localReport.getKmSum(this.id) + localReport.getKmSum(paramInt));
        localReport.setKmSum(this.id, 0L);
        localReport.save();
        ((Cursor)localObject).moveToNext();
      }
      ((Cursor)localObject).close();
      if (this.pid >= 2)
        DBTool.database.execSQL("update audit set kmid=" + paramInt + " where kmid=" + this.id);
      DBTool.database.execSQL("update km set flag=2,pid=0 where id=" + this.id);
      localObject = "操作成功完成！";
      break label252;
    }
    Object localObject = "子科目之和超出限制(16个)，无法合并！";
    break label252;
    label248: localObject = "级别不同 ，不能合并！";
    label252: return (String)localObject;
  }



  public void moveDown()
  {
    Cursor localCursor = DBTool.database.query("km", getColumnString(), "pid=" + this.pid + " and rank>" + this.rank, null, null, null, "rank");
    if (localCursor.getCount() != 0)
    {
      localCursor.moveToFirst();
      int i = localCursor.getShort(5);
      int j = localCursor.getShort(0);
      localCursor.close();
      DBTool.database.execSQL("update km set rank=" + i + " where id=" + this.id);
      DBTool.database.execSQL("update km set rank=" + this.rank + " where id=" + j);
    }
    else
    {
      localCursor.close();
    }
  }

  public void moveUp()
  {
    Cursor localCursor = DBTool.database.query("km", getColumnString(), "pid=" + this.pid + " and rank<" + this.rank, null, null, null, "rank desc");
    if (localCursor.getCount() != 0)
    {
      localCursor.moveToFirst();
      int j = localCursor.getShort(5);
      int i = localCursor.getShort(0);
      localCursor.close();
      DBTool.database.execSQL("update km set rank=" + j + " where id=" + this.id);
      DBTool.database.execSQL("update km set rank=" + this.rank + " where id=" + i);
    }
    else
    {
      localCursor.close();
    }
  }



}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.KM
 * JD-Core Version:    0.6.0
 */
