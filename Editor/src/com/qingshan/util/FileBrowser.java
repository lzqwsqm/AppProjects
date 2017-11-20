package com.qingshan.util;
import com.qingshan.editor.R;
import android.content.Context;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.qingshan.editor.EncodingList;
import com.qingshan.widget.QSButton;
import com.stericson.RootTools.RootTools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileBrowser extends ListActivity//ListActivity
{
	private static int OPEN_WITH_CODE = 0;
	private String current_path = "";
	private String default_filename = "";
	private EditText editTextFilename;
	private Spinner encoding_list;
	private FileListAdapter fileListAdapter;
	private ArrayList<File> files;
	private boolean isRoot = false;
	private int lastPos;
	private Spinner linebreakSpinner;
	private Intent mIntent;
	//保存事件
	private View.OnClickListener onSaveBtnClickListener = new View.OnClickListener()
	{
		public void onClick(View paramView)
		{
			String str = FileBrowser.this.editTextFilename.getText().toString().trim();
			if (!"".equals(str))
			{
				FileBrowser.this.mIntent.putExtra("file", FileBrowser.this.current_path + File.separator + str);
				FileBrowser.this.mIntent.putExtra("linebreak", FileBrowser.this.linebreakSpinner.getSelectedItemPosition());
				FileBrowser.this.mIntent.putExtra("encoding", FileBrowser.this.encoding_list.getSelectedItemPosition());
				FileBrowser.this.setResult(-1, FileBrowser.this.mIntent);
				FileBrowser.this.finish();
			}
			else
			{
				Toast.makeText(FileBrowser.this.getApplicationContext(), R.string.filename_is_empty, 1).show();
			}
		}
	};
	private LinearLayout pathButtons;
	private SharedPreferences pref;
	private int request_mode = 0;
	private Button saveButton;
	
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.file_bowsers);
        getListView().setFastScrollEnabled(true);
        registerForContextMenu(getListView());
        this.pathButtons = (LinearLayout) findViewById(R.id.pathButtons);
        this.editTextFilename = (EditText) findViewById(R.id.editTextFilename);
        this.saveButton = (Button) findViewById(R.id.btnSave);
        this.saveButton.setOnClickListener(this.onSaveBtnClickListener);
        this.linebreakSpinner = (Spinner) findViewById(R.id.linebreak_list);
        LinearLayout linebreakLinearLayout = (LinearLayout) findViewById(R.id.linebreakLinearLayout);
        LinearLayout encodingLinearLayout = (LinearLayout) findViewById(R.id.encodingLinearLayout);
        this.encoding_list = (Spinner) findViewById(R.id.encoding_list);
        String[] lists = EncodingList.list;
        lists[0] = getString(R.string.auto_detection);
        this.encoding_list.setAdapter(new ArrayAdapter(this, 17367048, lists));
        File file = Environment.getExternalStorageDirectory();
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        this.current_path = this.pref.getString("last_path", file.getPath());
        this.mIntent = getIntent();
        this.default_filename = this.mIntent.getStringExtra("filename");
        this.request_mode = this.mIntent.getIntExtra("mode", 0);
        this.isRoot = this.mIntent.getBooleanExtra("isRoot", false);
        this.editTextFilename.setText(this.default_filename);
        if (this.request_mode == 0) {
            ((LinearLayout) findViewById(R.id.filenameLinearLayout)).setVisibility(8);
            linebreakLinearLayout.setVisibility(0);
            encodingLinearLayout.setVisibility(0);
        }
        showFileList(new File(this.current_path));
    }
	
	private void showFileList(File path) {
        String curPath;
        boolean z = false;
        try {
            curPath = path.getCanonicalPath();
        } catch (IOException e) {
            curPath = path.getPath().replaceAll("/./", "/");
        }
        setTitle(curPath);
        this.current_path = curPath;
        String[] paths = curPath.split(File.separator);
        StringBuilder mStringBuilder = new StringBuilder();
        this.pathButtons.removeAllViews();
        if (paths.length < 1) {
            paths = new String[]{File.separator};
        }
        for (String p : paths) {
            String p2=p;
            QSButton mButton = new QSButton(this);
            if (p2 == null || "".equals(p2)) {
                p2 = File.separator;
            }
            mButton.setText(p2);
            mStringBuilder.append(File.separator).append(p2);
            mButton.putString("path", mStringBuilder.toString());
            mButton.setOnClickListener(new PathOnClickListener());
            this.pathButtons.addView(mButton);
        }
        if (this.isRoot && !path.canRead() && RootTools.isAccessGiven()) {
            //调试原来是true
			z = true;
        }
        this.files = FileUtil.getFileList(curPath, z);
        if (this.files == null) {
            Toast.makeText(this, R.string.can_not_list_file, 1).show();
            return;
        }
        this.fileListAdapter = new FileListAdapter(this, R.layout.file_list, this.files);
        setListAdapter(this.fileListAdapter);
        if (this.lastPos <= 0) {
            return;
        }
        if (this.files.size() > this.lastPos) {
            setSelection(this.lastPos);
            return;
        }
        int i = this.lastPos - 1;
        this.lastPos = i;
        if (i < this.files.size()) {
            setSelection(this.lastPos);
        }
    }
	
	
	
	
	public void finish()
	{
		this.pref.edit().putString("last_path", this.current_path).commit();
		super.finish();
	}

	
	
    //创建上下文菜单
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, R.string.open_with, 0, R.string.open_with);
        menu.add(0, R.string.rename, 0, R.string.rename);
        menu.add(0, R.string.delete, 0, R.string.delete);
    }
	//上下文菜单单击事件
	public boolean onContextItemSelected(MenuItem item) {
        try {
            int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
            try {
                final File f = (File) this.files.get(position);
                if (f == null) {
                    return false;
                }
                this.lastPos = position;
                switch (item.getItemId()) {
                    case R.string.open_with /*2131230803*/:
                        Uri uri = Uri.fromFile(f);
                        try {
                            if (f.isDirectory()) {
                                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                                intent.setDataAndType(uri, "file/*");
                                startActivity(intent);
                            } else {
                                Intent it = new Intent("android.intent.action.VIEW");
                                it.setDataAndType(uri, "*/*");
                                startActivity(it);
                            }
                        } catch (Exception e) {
                        }
                        return false;
                    case R.string.rename /*2131230804*/:
                        final EditText lineEditText = new EditText(this);
                        lineEditText.setText(f.getName());
                        Builder builder = new Builder(this);
                        builder.setTitle(R.string.rename).setView(lineEditText).setNegativeButton(17039360, null);
                        builder.setPositiveButton(17039370, new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which) {
									try {
										dialog.dismiss();
										String newname = lineEditText.getText().toString().trim();
										String newfile = new StringBuilder(String.valueOf(f.getParent())).append(File.separator).append(newname).toString();
										if ("".equals(newname) || !f.renameTo(new File(newfile))) {
											Toast.makeText(FileBrowser.this, R.string.rename_failed, 1).show();
										} else {
											FileBrowser.this.refresh();
										}
									} catch (Exception e) {
									}
								}
							});
                        builder.show();
                        return true;
                    case R.string.delete /*2131230805*/:
                        new Builder(this).setTitle(R.string.delete).setMessage(R.string.confirm_delete).setNegativeButton(17039360, null).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which) {
									f.delete();
									FileBrowser.this.refresh();
								}
							}).show();
                        return true;
                    default:
                        return super.onContextItemSelected(item);
                }
            } catch (Exception e2) {
                return false;
            }
        } catch (ClassCastException e3) {
            return false;
        }
    }
    //列表单击事件
	protected void onListItemClick(ListView listView, View view, int position, long id) {
        File file = (File) this.fileListAdapter.getItem(position);
        if (file.isDirectory()) {
            showFileList(file);
            return;
        }
        this.mIntent.putExtra("file", file.getPath());
        this.mIntent.putExtra("linebreak", this.linebreakSpinner.getSelectedItemPosition());
        this.mIntent.putExtra("encoding", this.encoding_list.getSelectedItemPosition());
        setResult(-1, this.mIntent);
        finish();
    }

	public void refresh()
	{
		showFileList(new File(this.current_path));
	}

	public void startActivityForResult(Intent intent, int requestCode) {
        if (requestCode == OPEN_WITH_CODE) {
            refresh();
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

	private class PathOnClickListener
    implements View.OnClickListener
	{
		private PathOnClickListener()
		{
		}

		public void onClick(View paramView)
		{
			QSButton localJecButton = (QSButton)paramView;
			FileBrowser.this.showFileList(new File(localJecButton.getString("path")));
		}
	}
	
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.FileBrowser
 * JD-Core Version:    0.6.0
 */
