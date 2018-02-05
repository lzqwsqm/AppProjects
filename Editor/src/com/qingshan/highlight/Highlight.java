package com.qingshan.highlight;


import android.graphics.Color;
import android.text.Editable;
import android.util.Log;
import com.qingshan.colorschemes.ColorScheme;
import com.qingshan.editor.QSEditor;
import com.qingshan.util.FileUtil;
import com.qingshan.widget.ForegroundColorSpan;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import com.qingshan.editor.Searcher;

public class Highlight
{
	public static final int GROUP_ATTR_NAME_ID = 6;
	public static final int GROUP_COMMENT_ID = 2;
	public static final int GROUP_FUNCTION_ID = 5;
	public static final int GROUP_KEYWORD_ID = 4;
	public static final int GROUP_STRING_ID = 3;
	public static final int GROUP_TAG_ID = 1;
	private static final String TAG = "Highlight";
	private static int color_attr_name;
	private static int color_comment;
	private static int color_function;
	private static int color_keyword;
	private static int color_string;
	private static int color_tag;
	private static HashMap<String, String[]> langTab;
	private static boolean mEnabled;
	private static int mLimitFileSize;
	private static ArrayList<ForegroundColorSpan> mSpans;
	private static ArrayList<String[]> nameTab;
	private int mEndOffset = -1;
	private String mExt = "";
	private int mStartOffset = -1;
	private boolean mStop = true;
	private static Searcher mSearcher;
     
	static
	{
		//System.loadLibrary("highlight");
		mEnabled = true;
		mLimitFileSize = 0;
		mSpans = new ArrayList();
	}
	//初始化
	//QSEditText.init()调用
	public Highlight() {
        this.mExt = "";
        this.mEndOffset = -1;
        this.mStartOffset = -1;
        this.mStop = true;
    }
	//初始化
	//QSEditor.initEnv()调用
	public static void init()
	{
		loadLang();
		loadColorScheme();
	}
	public static String getNameByExt(String ext) {
        if (langTab == null) {
            loadLang();
            return "";
        }
        String[] info = (String[]) langTab.get(ext);
        if (info == null) {
            return "";
        }
        return info[0];
    }
	//QSEditor.init_highlight()调用
	public static void setEnabled(boolean paramBoolean)
	{
		mEnabled = paramBoolean;
	}
	//QSEditor.init_highlight()调用
	public static void setLimitFileSize(int paramInt)
	{
		mLimitFileSize = paramInt;
	}
	public static ArrayList<String[]> getLangList()
	{
		return nameTab;
	}
	//QSEditText.setPath调用
	public static int getLimitFileSize()
	{
		return mLimitFileSize;
	}

	
    //高亮着实现核心代码
    //QSEditText.drawView调用
	public boolean render(Editable mText, int startOffset, int endOffset) {
        if (!mEnabled || langTab == null) {
            return false;
        }
		//QSEditText.onTouchEvent调用redraw()控制
        if (this.mStop || "".equals(this.mExt)) {
            return false;
        }
        /*if (this.mStartOffset <= startOffset && this.mEndOffset >= endOffset) {
		 return false;
		 }*/
		//由菜单高亮着色控制
        String[] lang = (String[]) langTab.get(this.mExt);
        if (lang == null) {
            return false;
        }
        this.mStop = true;
        this.mStartOffset = startOffset;
        this.mEndOffset = endOffset;
        //截取字符串 关键代码
		int[] ret = jni_parse(mText.subSequence(0, endOffset).toString(), QSEditor.TEMP_PATH + File.separator + lang[GROUP_TAG_ID]);
        //int[] ret = new int[]{3,0,5};
		if (ret == null) {
            this.mStop = false;
            return false;
        }
        int len = ret.length;
        if (len < GROUP_TAG_ID || ((float) len) % 3.0f != 0.0f) {
            this.mStop = false;
            return false;
        }
        int index = 0;
        int bufLen = mSpans.size();
        Iterator it = mSpans.iterator();
        while (it.hasNext()) {
            mText.removeSpan((ForegroundColorSpan) it.next());
        }
        int i = 0;
        while (i < len) {
            int color;
            switch (ret[i]) {
                case GROUP_TAG_ID: //1
                    color = color_tag;
                    break;
                case GROUP_COMMENT_ID: //2
                    color = color_comment;
                    break;
                case GROUP_STRING_ID: //3
                    color = color_string;
                    break;
                case GROUP_KEYWORD_ID: //4
                    color = color_keyword;
                    break;
                case GROUP_FUNCTION_ID: //5
                    color = color_function;
                    break;
                case GROUP_ATTR_NAME_ID: //6
                    color = color_attr_name;
                    break;
                default:
                    Log.d(TAG, "\u83b7\u53d6\u989c\u8272group id\u5931\u8d25");
                    this.mStop = false;
                    return false;
            }
            i += GROUP_TAG_ID;
            int start = ret[i];
            i += GROUP_TAG_ID;
            int end = ret[i];
            if (end >= startOffset) {
                ForegroundColorSpan fcs;
                if (index >= bufLen) {
                    fcs = new ForegroundColorSpan(color);
                    mSpans.add(fcs);
                } else {
                    fcs = (ForegroundColorSpan) mSpans.get(index);
                    fcs.setColor(color);
                }
                index += GROUP_TAG_ID;
                try {
                    mText.setSpan(fcs, start, end, 33);
                } catch (Exception e) {
                }
            }
            i += GROUP_TAG_ID;
        }
        ret = null;
        this.mStop = false;
        return false;
    }

	//private static native int[] jni_parse(String paramString1, String paramString2);
	private static  int[] jni_parse(String str, String str2){
		if(str==null){
			return null;
		}
		//String st ="import";
		//st.indexOf(str,0);
		//mSearcher.setText(st);
		//int[] a1=mSearcher.find(str,0);
		
		
		
		
		
		int[] ai=new int[3];
		ai[0]=3;
		ai[1]=0;//a1[0];
		ai[2]=6;//a1[1];
		return ai;
	}
	
	
	//TabHost.setEditTextPref调用
	public static void loadColorScheme()
	{
		color_tag = Color.parseColor(ColorScheme.color_tag);
		color_string = Color.parseColor(ColorScheme.color_string);
		color_keyword = Color.parseColor(ColorScheme.color_keyword);
		color_function = Color.parseColor(ColorScheme.color_function);
		color_comment = Color.parseColor(ColorScheme.color_comment);
		color_attr_name = Color.parseColor(ColorScheme.color_attr_name);
	}

	public static boolean loadLang() {
        String langfile = QSEditor.TEMP_PATH + "/lang.conf";
        if (!new File(langfile).isFile()) {
            return false;
        }
        langTab = new HashMap();
        nameTab = new ArrayList();
        try {
            String[] lines = new String(readFile(langfile), "utf-8").split("\n");
            int length = lines.length;
            for (int i = 0; i < length; i += GROUP_TAG_ID) {
                String line = lines[i].trim();
                if (!line.startsWith("#")) {
                    String[] cols = line.split(":");
                    String name = cols[0].trim();
                    String synfile = cols[GROUP_TAG_ID].trim();
                    String[] exts = cols[GROUP_COMMENT_ID].trim().split("\\s+");
                    ArrayList arrayList = nameTab;
                    Object[] obj = new String[GROUP_COMMENT_ID];
                    obj[0] = name;
                    obj[GROUP_TAG_ID] = exts[0];
                    arrayList.add(obj);
                    int length2 = exts.length;
                    for (int i2 = 0; i2 < length2; i2 += GROUP_TAG_ID) {
                        String ext = exts[i2];
                        HashMap hashMap = langTab;
                        Object[] obj2 = new String[GROUP_COMMENT_ID];
                        obj2[0] = name;
                        obj2[GROUP_TAG_ID] = synfile;
                        hashMap.put(ext, obj2);
                    }
                }
            }
            byte[] mByte = null;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

	public static String readFile(String file, String encoding) {
        try {
            return new String(readFile(file), encoding);
        } catch (Exception e) {
            try {
                return FileUtil.ReadFile(file, encoding);
            } catch (Exception e2) {
                return "";
            }
        }
    }

	public static byte[] readFile(String file)
	{
		//return read_file(paramString);
		return readFile(new File(file));
	}
	public static byte[] readFile(File filename) {
        try {
            
			return readFile(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
            return null;
        }
    }
	public static byte[] readFile(InputStream fis){
	
		try {
		int length = 0;
		length = fis.available();
		byte[] bs = new byte[length];
		fis.read(bs);
		return bs;
		} catch (IOException e) {
            return null;
        }
		
	}
	//private static native byte[] read_file(String paramString);

	

	
	//QSEditText.setCurrentFileExt调用
	//QSEditText.onTouchEvent调用
	public void redraw()
	{
		this.mStop = false;
		this.mEndOffset = -1;
		this.mStartOffset = -1;
	}
	//QSEditText.setCurrentFileExt调用
	public void setSyntaxType(String paramString)
	{
		this.mExt = paramString;
	}
	

	
	//QSEditText.onTouchEvent调用
	public void stop()
	{
		this.mStop = true;
	}
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.highlight.Highlight
 * JD-Core Version:    0.6.0
 */
