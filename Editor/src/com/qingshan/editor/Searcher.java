package com.qingshan.editor;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Searcher
{
	private Matcher mMatcher;
	private String text = "";

	public int[] find(String keyword, int start) {
        int offset = this.text.indexOf(keyword, start);
        if (offset == -1) {
            return null;
        }
        int end = offset + keyword.length();
        return new int[]{offset, end};
    }


	public int[] find(String keyword1, String keyword2, int start) {
        int offset = this.text.indexOf(keyword1, start);
        if (offset == -1) {
            return null;
        }
        int end;
        int offset2 = this.text.indexOf(keyword2, (keyword1.length() + offset) + 1);
        if (offset2 == -1) {
            end = this.text.length();
        } else {
            end = offset2 + keyword2.length();
        }
        return new int[]{offset, end};
    }


	public int[] findMatch(int start, int end) {
        if (!this.mMatcher.find(start) || this.mMatcher.end() > end) {
            return null;
        }
        return new int[]{this.mMatcher.start(), this.mMatcher.end()};
    }


	public Matcher getMatcher()
	{
		return this.mMatcher;
	}

	public String getString(int start, int end) {
        return this.text.substring(start, end);
    }


	public int getTextLength()
	{
		return this.text.length();
	}

	public boolean isInclude(int end, String include) {
        if (this.text.substring(end - include.length(), end) == include) {
            return true;
        }
        return false;
    }


	public void prepare(String pattern) {
        this.mMatcher = Pattern.compile(pattern, 2).matcher(this.text);
    }


	public void setText(String mText) {
        this.text = mText;
    }

	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.Searcher
 * JD-Core Version:    0.6.0
 */
