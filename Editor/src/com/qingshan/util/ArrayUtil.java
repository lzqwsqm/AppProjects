package com.qingshan.util;


public class ArrayUtil
{
	/*public static int idealByteArraySize(int paramInt)
	{
		int i = 4;
		while (i < 32)
		{
			if (paramInt > -12 + (1 << i))
			{
				i++;
				continue;
			}
			paramInt = -12 + (1 << i);
		}
		return paramInt;
	}*/
	public static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++)
            if (need <= (1 << i) - 12)
                return (1 << i) - 12;

        return need;
    }
	public static int idealCharArraySize(int need)
	{
		return idealByteArraySize(need * 2) / 2;
	}

	/*public static boolean inArray(int paramInt, int[] paramArrayOfInt)
	{
		int j = 0;
		int i = paramArrayOfInt.length;
		int k = j;
		while (k < i)
		{
			if (paramArrayOfInt[k] != paramInt)
			{
				k++;
				continue;
			}
			j = 1;
		}
		return j;
	}*/
	public static boolean inArray(int value, int[] intArray)
    {
        for(int v : intArray)
        {
            if(v == value)
                return true;
        }
        return false;
    }
	/*public static boolean inArray(String paramString, String[] paramArrayOfString)
	{
		int j = 0;
		int k = paramArrayOfString.length;
		int i = j;
		while (i < k)
		{
			if (paramArrayOfString[i] != paramString)
			{
				i++;
				continue;
			}
			j = 1;
		}
		return j;
	}*/
	public static boolean inArray(String value, String[] strArray)
    {
        for(String v : strArray)
        {
            if(v == value)
                return true;
        }
        return false;
    }
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.ArrayUtil
 * JD-Core Version:    0.6.0
 */
