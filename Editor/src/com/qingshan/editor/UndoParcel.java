package com.qingshan.editor;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;

public class UndoParcel
implements Parcelable
{
	//public static final Creator<UndoParcel> CREATOR;
	public static final int MAX_SIZE = 512*1024;
	private ArrayList<TextChange> mBuffer;
	private int mCurrentSize = 0;
	
	/*static {
        CREATOR = new Creator<UndoParcel>() {
            public UndoParcel createFromParcel(Parcel in) {
                return new UndoParcel(null);
            }

            public UndoParcel[] newArray(int size) {
                return new UndoParcel[size];
            }
        };
		
    }*/
	public static final Parcelable.Creator<UndoParcel> CREATOR
	= new Parcelable.Creator<UndoParcel>() {
        public UndoParcel createFromParcel(Parcel in) {
            return new UndoParcel(in);
        }

        public UndoParcel[] newArray(int size) {
            return new UndoParcel[size];
        }
    };
	
	public static class OnUndoStatusChange
	{
		public void run(boolean canUndo, boolean canRedo) {
        }
	}

	public static class TextChange
	{
		public CharSequence newtext;
		public CharSequence oldtext;
		public int start;
	}
	
	public int describeContents()
	{
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flag) {
        out.writeInt(this.mBuffer.size());
        /*Iterator it = this.mBuffer.iterator();
        while (it.hasNext()) {
            TextChange item = (TextChange) it.next();
            out.writeInt(item.start);
            out.writeString(item.oldtext.toString());
            out.writeString(item.newtext.toString());
        }*/
		for( TextChange item : mBuffer ){
            out.writeInt(item.start);
            out.writeString(item.oldtext.toString());
            out.writeString(item.newtext.toString());
        }
    }
	
	

	/**
     * createFromParcel初始化时必须要这个函数
     * @param in
     */
	private UndoParcel(Parcel in) {
        this.mCurrentSize = 0;
        int len = in.readInt();
		while( in.dataAvail() > 0 && len-- > 0){
        /*while (in.dataAvail() > 0) {
            int len2 = len - 1;
            if (len <= 0) {
                len = len2;
                return;
            }*/
            TextChange item = new TextChange();
            item.start = in.readInt();
            item.oldtext = in.readString();
            item.newtext = in.readString();
            if (item.newtext == null) {
                item.newtext = "";
            }
            if (item.oldtext == null) {
                item.oldtext = "";
            }
            this.mBuffer.add(item);
            this.mCurrentSize += item.newtext.length() + item.oldtext.length();
           // len = len2;
        }
    }
	
    
	
	public UndoParcel() {
       // this.mCurrentSize = 0;
        this.mBuffer = new ArrayList();
        this.mCurrentSize = 0;
    }
	
	public TextChange pop() {
        int size = this.mBuffer.size();
        if (size <= 0) {
            return null;
        }
        TextChange item = (TextChange) this.mBuffer.get(size - 1);
        this.mBuffer.remove(size - 1);
        this.mCurrentSize -= item.newtext.length() + item.oldtext.length();
        return item;
    }
	
	
	public boolean removeLast() {
        if (this.mBuffer.size() <= 0) {
            return false;
        }
        TextChange item = (TextChange) this.mBuffer.get(0);
        this.mBuffer.remove(0);
        this.mCurrentSize -= item.newtext.length() + item.oldtext.length();
        return true;
    }
	
	
	public void push(TextChange item) {
        if (item.newtext == null) {
            item.newtext = "";
        }
        if (item.oldtext == null) {
            item.oldtext = "";
        }
        int delta = item.newtext.length() + item.oldtext.length();
        /*if (delta < MAX_SIZE) {
            this.mCurrentSize += delta;
            this.mBuffer.add(item);
            while (this.mCurrentSize > MAX_SIZE) {
                if (!removeLast()) {
                    return;
                }
            }
            return;
        }
        removeAll();*/
		if ( delta < MAX_SIZE ){
            mCurrentSize += delta;
            mBuffer.add(item);
            while( mCurrentSize > MAX_SIZE ){
                if ( !removeLast() ){
                    break;
                }
            }
        }else{
            removeAll();
        }
    }
	
	
	public void clean()
	{
		this.mBuffer.clear();
		this.mCurrentSize = 0;
	}
	public void removeAll()
	{
		this.mBuffer.removeAll(this.mBuffer);
		this.mCurrentSize = 0;
	}
	
	public boolean canUndo()
	{
		/*boolean bool;
		if (this.mBuffer.size() <= 0)
			bool = false;
		else
			bool = true;
		return bool;*/
		return mBuffer.size()>0;
	}
	
	

	

	public ArrayList<TextChange> getBuffer()
	{
		return this.mBuffer;
	}

	
	
	
	

	

	

	

	
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.UndoParcel
 * JD-Core Version:    0.6.0
 */
