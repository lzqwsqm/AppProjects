package com.qingshan.widget;


import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

public class ForegroundColorSpan extends CharacterStyle
implements UpdateAppearance, ParcelableSpan
{
	private int mColor;

	public ForegroundColorSpan(int paramInt)
	{
		this.mColor = paramInt;
	}

	public ForegroundColorSpan(Parcel paramParcel)
	{
		this.mColor = paramParcel.readInt();
	}

	public int describeContents()
	{
		return 0;
	}

	public int getForegroundColor()
	{
		return this.mColor;
	}

	public int getSpanTypeId()
	{
		return 2;
	}

	public void setColor(int paramInt)
	{
		this.mColor = paramInt;
	}

	public void updateDrawState(TextPaint paramTextPaint)
	{
		paramTextPaint.setColor(this.mColor);
	}

	public void writeToParcel(Parcel paramParcel, int paramInt)
	{
		paramParcel.writeInt(this.mColor);
	}
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.ForegroundColorSpan
 * JD-Core Version:    0.6.0
 */
