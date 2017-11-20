package com.database.util;

//转换

import java.util.Vector;

public final class Convertor
{

	/******************************
	 * 功能：将float转为字符串
	 */
	public static String sumToStringf(float paramLong)
	{
		
		return Float.toString(paramLong);
	}
	
	/****************************************
     *功能:将字符串转为float 
     */
	public static float stringToSumf(String paramString)
    {
    
        return Float.valueOf(paramString);
    }
	/******************************
	 * 功能：将float转为字节
	 */
	public static void fromFloat(Float paramLong, byte[] paramArrayOfByte, int paramInt)
    {
        for (int i = 0; i < 8; i++)
			paramArrayOfByte[(paramInt + i)] = (byte)(int)(0x7fc00000 & Float.floatToIntBits(paramLong) >> 8 * (7 - i));
    }
	
	
	public static long toLong(byte[] b1, int offset, int length) {
        int i;
        long n = 0;
        long flag = 0;
        byte[] b = new byte[length];
        for (i = 0; i < length; i++) {
            b[i] = b1[offset + i];
        }
        if (b[0] >= 0/*null*/) {
            for (i = 0; i < b.length; i++) {
                if (b[i] < 0/*null*/) {
                    n = ((256 * n) + 256) + ((long) b[i]);
                } else {
                    n = (256 * n) + ((long) b[i]);
                }
            }
        } else {
            flag = 1;
            for (i = 0; i < b.length; i++) {
                if ((b[i] ^ -1) < 0) {
                    n = ((256 * n) + 256) + ((long) (b[i] ^ -1));
                } else {
                    n = (256 * n) + ((long) (b[i] ^ -1));
                }
            }
        }
        if (flag == 1) {
            return (-n) - 1;
        }
        return n;
    }

	

	/******************************
	 * 将long转为字符串
	 */
	
	public static String sumToString(long sum) {
        StringBuffer str = new StringBuffer();
        long abs_sum = sum;
        if (sum < 0) {
            abs_sum = 0 - abs_sum;
        }
        str.append(abs_sum / 100);
        int a = (int) (abs_sum % 100);
        if (a >= 10) {
            str.append(".").append(a);
        } else if (a >= 0) {
            str.append(".0").append(a);
        }
        if (sum < 0) {
            str.insert(0, "-");
        }
        return str.toString();
    }
	public static String sumToString3(long sum) {
        StringBuffer str = new StringBuffer();
        long abs_sum = sum;
        if (sum < 0) {
            abs_sum = 0 - abs_sum;
        }
        str.append(abs_sum / 1000);
        int a = (int) (abs_sum % 1000);
        str.append(".");
        if (a < 10) {
            str.append("00");
        } else if (a < 100) {
            str.append("0");
        }
        str.append(a);
        if (sum < 0) {
            str.insert(0, "-");
        }
        return str.toString();
    }
	public static String sumToString4(long sum) {
        StringBuffer str = new StringBuffer();
        long abs_sum = sum;
        if (sum < 0) {
            abs_sum = 0 - abs_sum;
        }
        str.append(abs_sum / 10000);
        int a = (int) (abs_sum % 10000);
        str.append(".");
        if (a < 10) {
            str.append("000");
        } else if (a < 100) {
            str.append("00");
        }else if (a < 1000) {
            str.append("0");
        }
        str.append(a);
        if (sum < 0) {
            str.insert(0, "-");
        }
        return str.toString();
    }

	/*public static String[] split(String paramString1, String paramString2)
	{
		Vector localVector = new Vector();

		int j = 0;
		for (int i = paramString1.indexOf(paramString2); (i < paramString1.length()) && (i != -1); i = paramString1.indexOf(paramString2, i + paramString2.length()))
		{
			localVector.addElement(paramString1.substring(j, i));
			j = i + paramString2.length();
		}
		localVector.addElement(paramString1.substring(j + 1 - paramString2.length()));
		String[] arrayOfString = new String[localVector.size()];
		for (int k = 0; k < localVector.size(); k++)
			arrayOfString[k] = ((String)localVector.elementAt(k));
		return arrayOfString;
	}*/
	public static String[] split(String original, String regex) {
        Vector<String> v = new Vector();
        String[] str = null;
        int index = 0;
        int startIndex = original.indexOf(regex);
        while (startIndex < original.length() && startIndex != -1) {
            v.addElement(original.substring(index, startIndex));
            index = startIndex + regex.length();
            startIndex = original.indexOf(regex, regex.length() + startIndex);
        }
        v.addElement(original.substring((index + 1) - regex.length()));
        str = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            str[i] = (String) v.elementAt(i);
        }
        return str;
    }

    
	
	
	
	public static long stringToSum(String str) {
        str = str.trim();
        if (str.length() == 0) {
            return 0;
        }
        int flag = 0;
        if (str.indexOf("-") == 0) {
            flag = 1;
        }
        int position = str.indexOf(".");
        if (position < 0) {
            return Long.parseLong(str) * 100;
        }
        if (position == str.length() - 1) {
            return Long.parseLong(str.substring(0, str.length() - 1)) * 100;
        }
        long value;
        if (flag == position || position == 0) {
            value = 0;
        } else {
            value = Long.parseLong(str.substring(0, position)) * 100;
        }
        if (flag == 1) {
            value = -value;
        }
        if (str.length() - position > 2) {
            value += Long.parseLong(str.substring(position + 1, position + 3));
        } else if (str.length() - position == 2) {
            value += Long.parseLong(str.substring(position + 1, position + 2)) * 10;
        }
        if (flag == 1) {
            return -value;
        }
        return value;
    }

	
	
	
	
	public static void fromLong(long paramLong, byte[] paramArrayOfByte, int paramInt)
    {
        for (int i = 0; i < 8; i++)
			paramArrayOfByte[(paramInt + i)] = (byte)(int)(0xFF & paramLong >> 8 * (7 - i));
    }


	
    
	/*public static String sumToString4(long paramLong)
	{
		StringBuffer localStringBuffer = new StringBuffer();
		long l = paramLong;
		if (paramLong < 0L)
			l = 0L - l;
		localStringBuffer.append(l / 10000L);
		int i = (int)(l % 10000L);
		localStringBuffer.append(".");
		if (i >= 10)
		{
			if (i >= 100)
			{
				if (i < 1000)
					localStringBuffer.append("0");
			}
			else
				localStringBuffer.append("00");
		}
		else
			localStringBuffer.append("000");
		localStringBuffer.append(i);

		if (paramLong < 0L)
			localStringBuffer.insert(0, "-");
		return localStringBuffer.toString();
	}*/
	public static long stringToSum3(String str) {
        str = str.trim();
        if (str.length() == 0) {
            return 0;
        }
        int flag = 0;
        if (str.indexOf("-") >= 0) {
            flag = 1;
        }
        int position = str.indexOf(".");
        if (position < 0 || position == str.length() - 1) {
            return Long.parseLong(str) * 1000;
        }
        long value;
        if (flag == position || position == 0) {
            value = 0;
        } else {
            value = Long.parseLong(str.substring(0, position)) * 1000;
        }
        if (value < 0) {
            value = -value;
        }
        if (str.length() - position > 3) {
            value += Long.parseLong(str.substring(position + 1, position + 4));
        } else if (str.length() - position == 3) {
            value += Long.parseLong(str.substring(position + 1, position + 3)) * 10;
        } else if (str.length() - position == 2) {
            value += Long.parseLong(str.substring(position + 1, position + 2)) * 100;
        }
        if (flag == 1) {
            return -value;
        }
        return value;
    }

	public static long stringToSum4(String paramString)
    {
        String str = paramString.trim();
        long l;
        if (str.length() != 0)
        {
            int j = str.indexOf("-");
            int i = 0;
            if (j >= 0)
                i = 1;
            int k = str.indexOf(".");
            if ((k >= 0) && (k != -1 + str.length()))
            {
                if ((i != k) && (k != 0))
                    l = 10000L * Long.parseLong(str.substring(0, k));
                else
                    l = 0L;
                if (l < 0L)
                    l = -l;
                if (str.length() - k <= 4)
                {
                    if (str.length() - k != 4)
                    {
                        if (str.length() - k != 3)
                        {
                            if (str.length() - k == 2)
                                l += 1000L * Long.parseLong(str.substring(k + 1, k + 2));
                        }
                        else
                            l += 100L * Long.parseLong(str.substring(k + 1, k + 3));
                    }
                    else
                        l += 10L * Long.parseLong(str.substring(k + 1, k + 4));
                }
                else
                    l += Long.parseLong(str.substring(k + 1, k + 5));
                if (i == 1)
                    l = -l;
            }
            else
            {
                l = 10000L * Long.parseLong(str);
            }
        }
        else
        {
            l = 0L;
        }
        return l;
	}
	
	 
	public static void fromShort(short paramShort, byte[] paramArrayOfByte, int paramInt)
	  {
	    for (int i = 0; i < 2; i++)
	      paramArrayOfByte[(paramInt + i)] = (byte)(0xFF & paramShort >> 8 * (1 - i));
	  }
	
	 public static void fromInt(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
	  {
	    for (int i = 0; i < 4; i++)
	      paramArrayOfByte[(paramInt2 + i)] = (byte)(0xFF & paramInt1 >> 8 * (3 - i));
	  }
}
 /*
public static void fromShort(short paramShort, byte[] paramArrayOfByte)
	  {
	    for (int i = 0; i < 2; i++)
	      paramArrayOfByte[i] = (byte)(0xFF & paramShort >> 8 * (1 - i));
	  }
	public static void fromLong(long paramLong, byte[] paramArrayOfByte)
    {
        for (int i = 0; i < 8; i++)
            paramArrayOfByte[i] = (byte)(int)(0xFF & paramLong >> 8 * (7 - i));
    }
	public static long toLong(byte[] paramArrayOfByte)
	{
		long l1 = 0L;
		long l2 = 0L;
		if (paramArrayOfByte[0] < 0)
		{
			l2 = 1L;
			for (int i = 0; i < paramArrayOfByte.length; i++)
				if ((0xFFFFFFFF ^ paramArrayOfByte[i]) >= 0)
					l1 = l1 * 256L + (0xFFFFFFFF ^ paramArrayOfByte[i]);
				else
					l1 = 256L + l1 * 256L + (0xFFFFFFFF ^ paramArrayOfByte[i]);
		}
		for (int i = 0; i < paramArrayOfByte.length; i++)
			if (paramArrayOfByte[i] >= 0)
				l1 = l1 * 256L + paramArrayOfByte[i];
			else
				l1 = 256L + l1 * 256L + paramArrayOfByte[i];
		if (l2 == 1L)
			l1 = -l1 - 1L;
		return l1;
	}
	
 public static String URLEncode(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramString.length(); i++)
      switch (paramString.charAt(i))
      {
      default:
        localStringBuffer.append(paramString.charAt(i));
        break;
      case ' ':
        localStringBuffer.append("%20");
        break;
      case '#':
        localStringBuffer.append("%23");
        break;
      case '%':
        localStringBuffer.append("%25");
        break;
      case '&':
        localStringBuffer.append("%26");
        break;
      case '\'':
        localStringBuffer.append("%27");
        break;
      case '+':
        localStringBuffer.append("%2b");
        break;
      case '.':
        localStringBuffer.append("%2E");
        break;
      case '/':
        localStringBuffer.append("%2F");
        break;
      case '<':
        localStringBuffer.append("%3c");
        break;
      case '>':
        localStringBuffer.append("%3e");
        break;
      case '[':
        localStringBuffer.append("%5b");
        break;
      case '\\':
        localStringBuffer.append("%5c");
        break;
      case ']':
        localStringBuffer.append("%5d");
        break;
      case '^':
        localStringBuffer.append("%5e");
        break;
      case '{':
        localStringBuffer.append("%7b");
        break;
      case '}':
        localStringBuffer.append("%7d");
        break;
      case '~':
        localStringBuffer.append("%73");
      }
    return localStringBuffer.toString();
  }

  public static void copy(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
  {
    if (paramInt1 + paramInt3 > paramArrayOfByte1.length)
      paramInt3 = paramArrayOfByte1.length - paramInt1;
    if (paramInt2 + paramInt3 > paramArrayOfByte2.length)
      paramInt3 = paramArrayOfByte2.length - paramInt2;
    for (int i = 0; i < paramInt3; i++)
      paramArrayOfByte2[(paramInt2 + i)] = paramArrayOfByte1[(paramInt1 + i)];
  }

  public static void fromDate(FDate paramFDate, byte[] paramArrayOfByte, int paramInt)
  {
    int j;
    if (paramFDate != null)
      j = (short)paramFDate.getValue();
    for (int i = 0; ; i++)
    {
      if (i >= 2)
        return;
      paramArrayOfByte[(paramInt + i)] = (byte)(0xFF & j >> 8 * (1 - i));
    }
  }

  public static void fromDate(short paramShort, byte[] paramArrayOfByte, int paramInt)
  {
    for (int i = 0; i < 2; i++)
      paramArrayOfByte[(paramInt + i)] = (byte)(0xFF & paramShort >> 8 * (1 - i));
  }

  public static void fromInt(int paramInt, byte[] paramArrayOfByte)
  {
    for (int i = 0; i < 4; i++)
      paramArrayOfByte[i] = (byte)(0xFF & paramInt >> 8 * (3 - i));
  }

 

 

 

  

  public static void fromString(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    if (paramString != null)
    {
      int k = paramString.length();
      if (paramInt + k * 2 > paramArrayOfByte.length)
        k = (paramArrayOfByte.length - paramInt) / 2;
      for (int i = 0; i < k; i++)
      {
        int j = paramString.charAt(i);
        paramArrayOfByte[(paramInt + i * 2)] = (byte)(j >> 8);
        paramArrayOfByte[(1 + (paramInt + i * 2))] = (byte)j;
      }
      if (paramArrayOfByte.length > paramInt + k * 2)
        paramArrayOfByte[(paramInt + k * 2)] = -1;
      if (paramArrayOfByte.length > 1 + (paramInt + k * 2))
        paramArrayOfByte[(1 + (paramInt + k * 2))] = 0;
    }
  }

  public static void fromString(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramString != null)
    {
      int j = paramString.length();
      if (paramInt1 + j * 2 > paramArrayOfByte.length)
        j = (paramArrayOfByte.length - paramInt1) / 2;
      for (int i = 0; i < j; i++)
      {
        int k = paramString.charAt(i);
        paramArrayOfByte[(paramInt1 + i * 2)] = (byte)(k >> 8);
        paramArrayOfByte[(1 + (paramInt1 + i * 2))] = (byte)k;
      }
      if ((j * 2 < paramInt2) && (paramArrayOfByte.length > paramInt1 + j * 2))
        paramArrayOfByte[(paramInt1 + j * 2)] = -1;
      if ((j * 2 < paramInt2) && (paramArrayOfByte.length > 1 + (paramInt1 + j * 2)))
        paramArrayOfByte[(1 + (paramInt1 + j * 2))] = 0;
    }
  }

  public static byte[] fromString(String paramString)
  {
    if (paramString != null)
    {
      arrayOfByte = new byte[2 * paramString.length()];
      for (int i = 0; i < paramString.length(); i++)
      {
        int j = paramString.charAt(i);
        arrayOfByte[(i * 2)] = (byte)(j >> 8);
        arrayOfByte[(1 + i * 2)] = (byte)j;
      }
    }
    byte[] arrayOfByte = null;
    return arrayOfByte;
  }

  public static boolean isDecimal(String paramString)
  {
    int i = 0;
    if (paramString.length() != 0)
    {
      int j = paramString.charAt(i);
      if ((paramString.length() != 1) || ((j >= 48) && (j <= 57)))
      {
        int k = 0;
        while (true)
          if (k < paramString.length())
          {
            j = paramString.charAt(k);
            if (((j == 45) && (k > 0)) || ((j != 45) && (j != 46) && ((j < 48) || (j > 57))))
              break;
            k++;
            continue;
          }
          else
          {
            i = 1;
          }
      }
    }
    return i;
  }

  public static boolean isInteger(String paramString)
  {
    int i = 0;
    if (paramString.length() != 0)
    {
      int j = 0;
      while (true)
        if (j < paramString.length())
        {
          int k = paramString.charAt(j);
          if ((k < 48) || (k > 57))
            break;
          j++;
          continue;
        }
        else
        {
          i = 1;
        }
    }
    return i;
  }




 

 



  



  public static long toAbsLong(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    long l = 0L;
    byte[] arrayOfByte = new byte[paramInt2];
    for (int i = 0; i < paramInt2; i++)
      arrayOfByte[i] = paramArrayOfByte[(paramInt1 + i)];
    for (i = 0; i < arrayOfByte.length; i++)
      if (arrayOfByte[i] >= 0)
        l = l * 256L + arrayOfByte[i];
      else
        l = 256L + l * 256L + arrayOfByte[i];
    return l;
  }

  public static String toHexString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int j = 0; j < paramArrayOfByte.length; j++)
    {
      int i = paramArrayOfByte[j];
      if (i < 0)
        i += 256;
      localStringBuffer.append("0123456789ABCDEF".charAt(i / 16));
      localStringBuffer.append("0123456789ABCDEF".charAt(i % 16));
    }
    return localStringBuffer.toString();
  }




  public static String toString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer;
    int i;
    if ((paramArrayOfByte != null) && (paramArrayOfByte[0] != -1))
    {
      localStringBuffer = new StringBuffer("");
      i = 0;
    }
    String str;
    while (true)
      if ((i >= paramArrayOfByte.length) || ((paramArrayOfByte[i] == -1) && ((i + 1 == paramArrayOfByte.length) || (paramArrayOfByte[(i + 1)] == 0))))
      {
        str = localStringBuffer.toString();
      }
      else
      {
        int k = paramArrayOfByte[str];
        int j = paramArrayOfByte[(str + 1)];
        if (k < 0)
          k = (short)(k + 256);
        if (j < 0)
          j = (short)(j + 256);
        localStringBuffer.append((char)(j + k * 256));
        str += 2;
        continue;
        str = "";
      }
    return str;
  }

  public static String toString(byte[] paramArrayOfByte, int paramInt)
  {
    String str2;
    if (paramInt < paramArrayOfByte.length)
    {
      if (paramArrayOfByte[paramInt] != -1)
      {
        StringBuffer localStringBuffer = new StringBuffer("");
        for (int k = paramInt; ; k += 2)
        {
          if ((k >= paramArrayOfByte.length) || ((paramArrayOfByte[k] == -1) && ((k + 1 == paramArrayOfByte.length) || (paramArrayOfByte[(k + 1)] == 0))))
          {
            String str1 = localStringBuffer.toString();
            break;
          }
          int i = paramArrayOfByte[k];
          int j = paramArrayOfByte[(k + 1)];
          if (i < 0)
            i = (short)(i + 256);
          if (j < 0)
            j = (short)(j + 256);
          localStringBuffer.append((char)(j + i * 256));
        }
      }
      str2 = "";
    }
    else
    {
      str2 = "";
    }
    return str2;
  }

  public static String toString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    Object localObject;
    if (paramInt1 < paramArrayOfByte.length)
    {
      if (paramArrayOfByte[paramInt1] != -1)
      {
        localObject = new StringBuffer("");
        int m = paramInt2 + paramInt1;
        if (m > paramArrayOfByte.length)
          m = paramArrayOfByte.length;
        if ((m - paramInt1) % 2 == 1)
          m--;
        for (int k = paramInt1; ; k += 2)
        {
          if ((k >= m) || ((paramArrayOfByte[k] == -1) && ((k + 1 == paramArrayOfByte.length) || (paramArrayOfByte[(k + 1)] == 0))))
          {
            localObject = ((StringBuffer)localObject).toString();
            break;
          }
          int j = paramArrayOfByte[k];
          int i = paramArrayOfByte[(k + 1)];
          if (j < 0)
            j = (short)(j + 256);
          if (i < 0)
            i = (short)(i + 256);
          ((StringBuffer)localObject).append((char)(i + j * 256));
        }
      }
      localObject = "";
    }
    else
    {
      localObject = "";
    }
    return (String)localObject;
  }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.util.Convertor
 * JD-Core Version:    0.6.0
 */

