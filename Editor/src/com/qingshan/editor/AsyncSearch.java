package com.qingshan.editor;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.text.Editable;
import android.widget.Toast;
import com.qingshan.widget.QSEditText;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsyncSearch
{
	private boolean ignoreCase = true;
	private ArrayList<int[]> mData = new ArrayList();
	private QSEditor mJecEditor;
	private String mPattern = "";
	private boolean next = true;
	private boolean regexp = false;
	private boolean replaceAll = false;
	private CharSequence replaceText = "";
	private int start = 0;
	
	public void search(String pattern, boolean next, QSEditor mJecEditor) {
        this.replaceAll = false;
        mJecEditor.getEditText().requestFocus();
        this.mJecEditor = mJecEditor;
        this.next = next;
        this.start = next ? mJecEditor.getEditText().getSelectionEnd() : mJecEditor.getEditText().getSelectionStart();
        if (!this.regexp) {
            pattern = escapeMetaChar(pattern);
        }
        this.mPattern = pattern;
        this.mData.clear();
        new SearchTask().execute(new String[0]);
    }
	private static String escapeMetaChar(String pattern) {
        String metachar = ".^$[]*+?|()\\";
        StringBuilder newpat = new StringBuilder();
        int len = pattern.length();
        for (int i = 0; i < len; i++) {
            char c = pattern.charAt(i);
            if (".^$[]*+?|()\\".indexOf(c) >= 0) {
                newpat.append('\\');
            }
            newpat.append(c);
        }
        return newpat.toString();
    }

	private void onSearchFinished(ArrayList<int[]> data)
	{
		int[] ret;
		if (data.size() == 0) {
            int resid;
            if (this.replaceAll) {
                resid = R.string.replace_finish;
            } else {
                resid = R.string.not_found;
            }
            Toast.makeText(this.mJecEditor.getApplicationContext(), this.mJecEditor.getText(resid), 1).show();
        } else if (this.replaceAll) {
            Editable mText = this.mJecEditor.getEditText().getText();
            for (int i = data.size() - 1; i >= 0; i--) {
                ret = (int[]) data.get(i);
                mText.replace(ret[0], ret[1], this.replaceText);
            }
        } else {
            ret = (int[]) data.get(0);
            this.mJecEditor.getEditText().setSelection(ret[0], ret[1]);
            this.mJecEditor.getEditText().scrollBy(this.mJecEditor.getEditText().getScrollX(), this.mJecEditor.getEditText().getScrollY() + 40);
        }
	}

	public void replace(String paramString)
	{
		if (this.mData.size() != 0)
		{
			int[] arrayOfInt = (int[])this.mData.get(0);
			this.mJecEditor.getEditText().getText().replace(arrayOfInt[0], arrayOfInt[1], paramString);
		}
	}

	public void replaceAll(String paramString1, String paramString2, QSEditor paramJecEditor)
	{
		this.replaceAll = true;
		this.replaceText = paramString2;
		this.mJecEditor = paramJecEditor;
		this.next = true;
		this.start = 0;
		if (!this.regexp)
			paramString1 = escapeMetaChar(paramString1);
		this.mPattern = paramString1;
		this.mData.clear();
		new SearchTask().execute(new String[0]);
	}

	

	public void setIgnoreCase(boolean paramBoolean)
	{
		this.ignoreCase = paramBoolean;
	}

	public void setRegExp(boolean paramBoolean)
	{
		this.regexp = paramBoolean;
	}

	private class SearchTask extends AsyncTask<String, Boolean, Boolean> {
        private boolean mCancelled;
        private ProgressDialog mProgressDialog;

        private SearchTask() {
        }

        protected void onPreExecute() {
            this.mCancelled = false;
            this.mProgressDialog = new ProgressDialog(AsyncSearch.this.mJecEditor);
            this.mProgressDialog.setTitle(R.string.spinner_message);
            this.mProgressDialog.setMessage(AsyncSearch.this.mJecEditor.getText(R.string.searching));
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.setProgressStyle(0);
            this.mProgressDialog.setCancelable(true);
            this.mProgressDialog.setOnCancelListener(new OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						SearchTask.this.mCancelled = true;
						SearchTask.this.cancel(false);
					}
				});
            this.mProgressDialog.show();
        }

        protected Boolean doInBackground(String... params) {
            if (isCancelled()) {
                return Boolean.valueOf(true);
            }
            try {
                Pattern pattern;
                if (AsyncSearch.this.ignoreCase) {
                    pattern = Pattern.compile(AsyncSearch.this.mPattern, 74);
                } else {
                    pattern = Pattern.compile(AsyncSearch.this.mPattern);
                }
                Matcher m = pattern.matcher(AsyncSearch.this.mJecEditor.getEditText().getString());
                if (AsyncSearch.this.replaceAll) {
                    while (m.find() && !this.mCancelled) {
                        AsyncSearch.this.mData.add(new int[]{m.start(), m.end()});
                    }
                } else if (AsyncSearch.this.next) {
                    if (m.find(AsyncSearch.this.start)) {
                        AsyncSearch.this.mData.add(new int[]{m.start(), m.end()});
                    }
                } else if (AsyncSearch.this.start <= 0) {
                    return Boolean.valueOf(true);
                } else {
                    while (m.find()) {
                        if (this.mCancelled) {
                            break;
                        } else if (m.end() < AsyncSearch.this.start) {
                            AsyncSearch.this.mData.add(new int[]{m.start(), m.end()});
                        } else if (AsyncSearch.this.mData.size() > 0) {
                            int[] ret = (int[]) AsyncSearch.this.mData.get(AsyncSearch.this.mData.size() - 1);
                            AsyncSearch.this.mData.clear();
                            AsyncSearch.this.mData.add(ret);
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(AsyncSearch.this.mJecEditor.getApplicationContext(), AsyncSearch.this.mJecEditor.getString(R.string.search_error), 1).show();
            }
            return Boolean.valueOf(true);
        }

        protected void onPostExecute(Boolean result) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
            AsyncSearch.this.onSearchFinished(AsyncSearch.this.mData);
        }

        protected void onCancelled() {
            super.onCancelled();
            onPostExecute(Boolean.valueOf(false));
        }
    }
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.AsyncSearch
 * JD-Core Version:    0.6.0
 */
