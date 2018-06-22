package com.qingshan.editor;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ((TextView) findViewById(R.id.field_app_name)).setText(R.string.app_name);
        ((TextView) findViewById(R.id.field_version)).setText("v" + QSEditor.version);
    }

}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.About
 * JD-Core Version:    0.6.0
 */
