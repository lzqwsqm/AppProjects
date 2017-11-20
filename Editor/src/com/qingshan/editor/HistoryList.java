package com.qingshan.editor;


import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HistoryList
{
	private ArrayList<FileInfo> historyFileList;
	
	private DialogInterface.OnClickListener mClickEvent = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which) {
			final String path = ((FileInfo) HistoryList.this.historyFileList.get(which)).path;
			if (new File(path).exists()) {
				HistoryList.this.mJecEditor.saveConfirm(new Runnable() {
						public void run() {
							HistoryList.this.mJecEditor.readFileToEditText(path);
						}
					});
			} else {
				Toast.makeText(HistoryList.this.mJecEditor.getApplicationContext(), HistoryList.this.mJecEditor.getString(R.string.file_not_exists), 1);
			}
		}
	};
	private QSEditor mJecEditor;

	public HistoryList(QSEditor mJecEditor)
	{
		this.mJecEditor = mJecEditor;
        this.historyFileList = getFileList();
        new Builder(mJecEditor).setTitle(R.string.history).setAdapter(new HistoryListAdapter(mJecEditor, R.layout.dialog_list_row, this.historyFileList), this.mClickEvent).show();
	}

	private ArrayList<FileInfo> getFileList() {
        SharedPreferences sp = this.mJecEditor.getSharedPreferences(QSEditor.PREF_HISTORY, 0);
        ArrayList<FileInfo> fl = new ArrayList();
        for (Entry<String, ?> entry : sp.getAll().entrySet()) {
            Object val = entry.getValue();
            if (val instanceof String) {
                String[] vals = ((String) val).split(",");
                if (vals.length >= 3) {
                    try {
                        FileInfo fi = new FileInfo();
                        fi.path = (String) entry.getKey();
                        fi.sel_start = Integer.parseInt(vals[0]);
                        fi.sel_end = Integer.parseInt(vals[1]);
                        fi.access_time = Long.parseLong(vals[2]);
                        fl.add(fi);
                    } catch (Exception e) {
                    }
                }
            }
        }
        if (fl.size() == 0) {
            return fl;
        }
        Collections.sort(fl, new Comparator<FileInfo>() {
				public int compare(FileInfo object1, FileInfo object2) {
					if (object2.access_time < object1.access_time) {
						return -1;
					}
					if (object2.access_time > object1.access_time) {
						return 1;
					}
					return 0;
				}
			});
        int historymax = fl.size();
        if (historymax > 20) {
            historymax = 20;
        }
        ArrayList<FileInfo> items = new ArrayList();
        int max = fl.size();
        for (int i = 0; i < max; i++) {
            if (i >= historymax) {
                sp.edit().remove(((FileInfo) fl.get(i)).path);
            } else {
                items.add((FileInfo) fl.get(i));
            }
        }
        sp.edit().commit();
        return items;
	}

	private class FileInfo {
        long access_time;
        String path;
        int sel_end;
        int sel_start;

        private FileInfo() {
            this.path = "";
            this.access_time = 0;
            this.sel_start = 0;
            this.sel_end = 0;
        }
    }

	private class HistoryListAdapter extends ArrayAdapter<FileInfo> {

        private class ViewHolder {
            public TextView path;

            private ViewHolder() {
            }
        }

        public HistoryListAdapter(Context context, int textViewResourceId, List<FileInfo> objects) {
            super(context, textViewResourceId, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(HistoryList.this.mJecEditor, R.layout.dialog_list_row, null);
                holder = new ViewHolder();
                holder.path = (TextView) view.findViewById(R.id.textView1);
                view.setTag(holder);
            }
            holder.path.setText(((FileInfo) getItem(position)).path);
            return view;
        }
    }
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.HistoryList
 * JD-Core Version:    0.6.0
 */
