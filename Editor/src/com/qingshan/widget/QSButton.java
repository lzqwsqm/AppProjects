package com.qingshan.widget;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.HashMap;

public class QSButton extends Button
{
	private HashMap<String, String> mStrHashMap = new HashMap();

	public QSButton(Context context)
	{
		super(context);
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -2);
		localLayoutParams.weight = 1.0F;
		setLayoutParams(localLayoutParams);
	}

	public String getString(String key)
	{
		return (String)this.mStrHashMap.get(key);
	}

	/*public void putString(String paramString1, String paramString2)
	{
		this.mStrHashMap.put(paramString1, paramString2);
	}*/
	public void putString(String key, String value)
    {
        mStrHashMap.put(key, value);
    }
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.JecButton
 * JD-Core Version:    0.6.0
 */

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.JecImageButton
 * JD-Core Version:    0.6.0
 */
