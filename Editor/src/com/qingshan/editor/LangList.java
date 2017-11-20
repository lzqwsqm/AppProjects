package com.qingshan.editor;


import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.qingshan.highlight.Highlight;
import com.qingshan.widget.QSEditText;
import java.util.ArrayList;
import java.util.List;

public class LangList
{
	private ArrayList<String[]> list;
	private DialogInterface.OnClickListener mClickEvent = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which) {
			LangList.this.mJecEditor.getEditText().setCurrentFileExt(((String[]) LangList.this.list.get(which))[1]);
		}
	};
	private QSEditor mJecEditor;

	public LangList(QSEditor mJecEditor)
	{
		this.mJecEditor = mJecEditor;
        this.list = Highlight.getLangList();
        new Builder(mJecEditor).setTitle(R.string.highlight).setAdapter(new DialogListAdapter(mJecEditor, R.layout.dialog_list_row, this.list), this.mClickEvent).show();
	}

	private class DialogListAdapter extends ArrayAdapter<String[]> {

        private class ViewHolder {
            public TextView text;

            private ViewHolder() {
            }
        }

        public DialogListAdapter(Context context, int textViewResourceId, ArrayList<String[]> list) {
            super(context, textViewResourceId, list);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(LangList.this.mJecEditor, R.layout.dialog_list_row, null);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.textView1);
                view.setTag(holder);
            }
            holder.text.setText(((String[]) getItem(position))[0]);
            return view;
        }
    }
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.LangList
 * JD-Core Version:    0.6.0
 */
