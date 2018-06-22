package com.qingshan.editor;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

public class Donate extends Activity
{
	private String html_url = "http://www.jecelyin.com/donate.html";

	public static Intent getWebIntent()
	{
		return new Intent("android.intent.action.VIEW", Uri.parse("http://www.jecelyin.com/donate.html"));
	}

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate);
        ((WebView) findViewById(R.id.donateWebView)).loadUrl(this.html_url);
    }

}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.Donate
 * JD-Core Version:    0.6.0
 */
