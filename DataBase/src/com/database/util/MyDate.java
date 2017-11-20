package com.database.util;



import java.util.Calendar;
import java.util.Date;

public final class MyDate
{
    private int day;
    private int month;
    private int year;

    
	public MyDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.year = (short) calendar.get(1);
        this.month = (byte) (calendar.get(2) + 1);
        this.day = (byte) calendar.get(5);
    }

	
	public MyDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.year = (short) calendar.get(1);
        this.month = (byte) (calendar.get(2) + 1);
        this.day = (byte) calendar.get(5);
    }


	
	public Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, this.year);
        calendar.set(2, this.month - 1);
        calendar.set(5, this.day);
        return calendar.getTime();
    }


	public int getYear()
	{
		return this.year;
	}
	public int getDay()
	{
		return this.day;
	}

	public int getMonth()
	{
		return this.month;
	}

	public int getValue()
	{
		return 512 * (-2000 + this.year) + 32 * this.month + this.day;
	}

	/*public String toString()
	{
		StringBuffer localStringBuffer1 = new StringBuffer();
		StringBuffer localStringBuffer2 = localStringBuffer1.append(this.year).append("-");
		String str;
		if (this.month >= 10)
			str = "";
		else
			str = "0";
		localStringBuffer2 = localStringBuffer2.append(str).append(this.month).append("-");
		if (this.day >= 10)
			str = "";
		else
			str = "0";
		localStringBuffer2.append(str).append(this.day);
		return localStringBuffer1.toString();
	}*/
	public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(this.year).append("-").append(this.month < 10 ? "0" : "").append(this.month).append("-").append(this.day < 10 ? "0" : "").append(this.day);
        return str.toString();
    }


	/*public String toShortString()
	{
		StringBuffer localStringBuffer = new StringBuffer();
		String str1;
		if (this.month >= 10)
			str1 = "";
		else
			str1 = "0";
		StringBuffer localObject = localStringBuffer.append(str1).append(this.month).append("-");
		String str;
		if (this.day >= 10)
			str = "";
		else
			str = "0";
		localObject.append(str).append(this.day);
		return localStringBuffer.toString();
	}*/
	public String toShortString() {
        StringBuffer str = new StringBuffer();
        str.append(this.month < 10 ? "0" : "").append(this.month).append("-").append(this.day < 10 ? "0" : "").append(this.day);
        return str.toString();
    }

	public static MyDate now()
    {
        return new MyDate(new Date());
    }
	public MyDate clone()
	{
		MyDate localFDate = new MyDate();
		localFDate.setYear(this.year);
		localFDate.setMonth(this.month);
		localFDate.setDay(this.day);
		return localFDate;
	}
	public void setDay(int paramInt)
	{
		this.day = paramInt;
	}

	public void setMonth(int paramInt)
	{
		this.month = paramInt;
	}

	public void setYear(int paramInt)
	{
		this.year = paramInt;
	}

	/*public String toMMDDString()
    {
        StringBuffer localStringBuffer1 = new StringBuffer();
        String str;
        if (this.month >= 10)
            str = "";
        else
            str = "0";
        StringBuffer localStringBuffer2 = localStringBuffer1.append(str).append(this.month).append("月");
        if (this.day >= 10)
            str = "";
        else
            str = "0";
        localStringBuffer2.append(str).append(this.day).append("日");
        return localStringBuffer1.toString();
    }*/
	public String toMMDDString() {
        StringBuffer str = new StringBuffer();
        str.append(this.month < 10 ? "0" : "").append(this.month).append("月").append(this.day < 10 ? "0" : "").append(this.day).append("日");
        return str.toString();
    }

	/*
	 * 
	 */
	/*public boolean lessThan(Date date)
	{
	  boolean bool;
	  if ((getYear() >= 1900 + date.getYear()) && ((getYear() != 1900 + date.getYear()) || (getMonth() >= 1 + date.getMonth())) && ((getYear() != 1900 + date.getYear()) || (getMonth() != 1 + date.getMonth()) || (getDay() >= date.getDate())))
	    bool = false;
	  else
	    bool = true;
	  return bool ;
	}*/
	public boolean lessThan(Date date) {
        if (getYear() < date.getYear() + 1900 || ((getYear() == date.getYear() + 1900 && getMonth() < date.getMonth() + 1) || (getYear() == date.getYear() + 1900 && getMonth() == date.getMonth() + 1 && getDay() < date.getDate()))) {
            return true;
        }
        return false;
    }

	
}


/*  public FDate(int paramInt1, int paramInt2, int paramInt3)
  {
    this.year = (short)paramInt1;
    this.month = (byte)paramInt2;
    this.day = (byte)paramInt3;
  }



  public FDate(short paramShort)
  {
    this.year = (2000 + paramShort / 512);
    this.month = (byte)(paramShort % 512 / 32);
    this.day = (byte)(paramShort % 32);
  }




  public boolean equals(Date paramDate)
  {
    int i;
    if ((getYear() == 1900 + paramDate.getYear()) && (getMonth() == 1 + paramDate.getMonth()) && (getDay() == paramDate.getDate()))
      i = 1;
    else
      i = 0;
    return i;
  }




  public boolean isBissextile()
  {
    int i;
    if ((this.year % 4 == 0) && ((this.year % 100 != 0) || (this.year % 400 == 0)))
      i = 1;
    else
      i = 0;
    return i;
  }

  public FDate lastDateOfMonth()
  {
    int i = 0;
    if ((this.month != 1) && (this.month != 3) && (this.month != 5) && (this.month != 7) && (this.month != 8) && (this.month != 10) && (this.month != 12))
    {
      if ((this.month != 4) && (this.month != 6) && (this.month != 9) && (this.month != 11))
      {
        if (this.month == 2)
          if (((this.year % 4 != 0) || (this.year % 100 == 0)) && (this.year % 400 != 0))
            i = 28;
          else
            i = 29;
      }
      else
        i = 30;
    }
    else
      i = 31;
    return new FDate(this.year, this.month, i);
  }

  

  public void modifyLastDay()
  {
    if (((getMonth() != 4) && (getMonth() != 6) && (getMonth() != 9) && (getMonth() != 11)) || (getDay() <= 30))
    {
      if ((getMonth() != 2) || (getDay() <= 29) || (!isBissextile()))
      {
        if ((getMonth() == 2) && (getDay() > 28) && (!isBissextile()) && (getDay() > 28))
          setDay(28);
      }
      else
        setDay(29);
    }
    else
      setDay(30);
  }

  public FDate nextDate()
  {
    int k = 1 + this.day;
    int i = this.month;
    int j = this.year;
    if (((k == 29) && (i == 2)) || ((k == 31) && ((i == 4) || (i == 6) || (i == 9) || (i == 11))) || (k == 32))
    {
      i++;
      k = 1;
    }
    if (i > 12)
      i = 1;
    return new FDate(j, i, k);
  }



  public String toNakedString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.year);
    if (this.month < 10)
      localStringBuffer.append("0");
    localStringBuffer.append(this.month);
    if (this.day < 10)
      localStringBuffer.append("0");
    localStringBuffer.append(this.day);
    return localStringBuffer.toString();
  }

 

  public String toShortString(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (this.month >= 10)
      localObject = "";
    else
      localObject = "0";
    Object localObject = localStringBuffer.append((String)localObject).append(this.month).append(paramString);
    String str;
    if (this.day >= 10)
      str = "";
    else
      str = "0";
    ((StringBuffer)localObject).append(str).append(this.day);
    return (String)localStringBuffer.toString();
  }



  public String toYYMMString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str;
    if (-2000 + this.year < 10)
      str = "0";
    else
      str = "";
    localStringBuffer.append(str).append(-2000 + this.year).append("年").append(this.month).append("月");
    return localStringBuffer.toString();
  }

  public String toYYYYMMDDString()
  {
    StringBuffer localStringBuffer1 = new StringBuffer();
    StringBuffer localStringBuffer2 = localStringBuffer1.append(this.year);
    String str;
    if (this.month >= 10)
      str = "";
    else
      str = "0";
    localStringBuffer2 = localStringBuffer2.append(str).append(this.month);
    if (this.day >= 10)
      str = "";
    else
      str = "0";
    localStringBuffer2.append(str).append(this.day);
    return localStringBuffer1.toString();
  }

  public String toYYYYMMString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.year).append("年").append(this.month).append("月");
    return localStringBuffer.toString();
  }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.util.FDate
 * JD-Core Version:    0.6.0
 */

