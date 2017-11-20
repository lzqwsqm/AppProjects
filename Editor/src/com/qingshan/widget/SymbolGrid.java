package com.qingshan.widget;
import com.qingshan.editor.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class SymbolGrid extends RelativeLayout
{
	private ImageView closeButton;
	private int mBottom;
	private ArrayList<String> mButtons;
	private LinearLayout mDrager;
	private GridView mGridView;
	private int mLeft;
	private OnSymbolClickListener mListener;
	private int mRight;
	private int mTop;

	public SymbolGrid(Context context) {
        super(context);
    }

	public SymbolGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        View mView = inflate(context, R.layout.symbol_grid, this);
        this.closeButton = (ImageView) mView.findViewById(R.id.iv_close);
        this.mGridView = (GridView) mView.findViewById(R.id.gridview);
        this.mDrager = (LinearLayout) mView.findViewById(R.id.drag);
        appendToolbarButton();
    }

	private void appendToolbarButton() {
        this.mButtons = new ArrayList();
        this.mButtons.add("{");
        this.mButtons.add("}");
        this.mButtons.add("<");
        this.mButtons.add(">");
        this.mButtons.add(";");
        this.mButtons.add("\"");
        this.mButtons.add("(");
        this.mButtons.add(")");
        this.mButtons.add("/");
        this.mButtons.add("\\");
        this.mButtons.add("'");
        this.mButtons.add("%");
        this.mButtons.add("[");
        this.mButtons.add("]");
        this.mButtons.add("|");
        this.mButtons.add("#");
        this.mButtons.add("=");
        this.mButtons.add("$");
        this.mButtons.add(":");
        this.mButtons.add(",");
        this.mButtons.add("&");
        this.mButtons.add("?");
        this.mButtons.add("\t");
        this.mButtons.add("\n");
        this.mButtons.add("!");
        this.mButtons.add("@");
        this.mButtons.add("^");
        this.mButtons.add("*");
        this.mButtons.add("_");
        this.mButtons.add("+");
        this.closeButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					SymbolGrid.this.setVisibility(8);
				}
			});
        this.mGridView.setAdapter(new GridAdapter(getContext(), this.mButtons));
        this.mGridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
					if (view instanceof TextView) {
						String txt = ((TextView) view).getText().toString();
						if ("\\n".equals(txt)) {
							txt = "\n";
						} else if ("\\t".equals(txt)) {
							txt = "\t";
						}
						SymbolGrid.this.mListener.OnClick(txt);
					}
				}
			});
        this.mDrager.setOnTouchListener(new OnTouchListener() {
				private int lastX;
				private int lastY;

				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN://TabWidget.MENU_ACTION_CLOSE_ONE /*0*/:
							this.lastX = (int) event.getRawX();
							this.lastY = (int) event.getRawY();
							break;
						case MotionEvent.ACTION_MOVE://TestHandler.ACTION_HIDE /*2*/:
							int dx = ((int) event.getRawX()) - this.lastX;
							int dy = ((int) event.getRawY()) - this.lastY;
							SymbolGrid.this.mLeft = SymbolGrid.this.getLeft() + dx;
							SymbolGrid.this.mTop = SymbolGrid.this.getTop() + dy;
							SymbolGrid.this.mRight = SymbolGrid.this.getRight() + dx;
							SymbolGrid.this.mBottom = SymbolGrid.this.getBottom() + dy;
							SymbolGrid.this.layout(SymbolGrid.this.mLeft, SymbolGrid.this.mTop, SymbolGrid.this.mRight, SymbolGrid.this.mBottom);
							this.lastX = (int) event.getRawX();
							this.lastY = (int) event.getRawY();
							break;
					}
					return true;
				}
			});
    }

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!(this.mLeft == 0 || this.mTop == 0 || this.mRight == 0 || this.mBottom == 0)) {
            offsetLeftAndRight(this.mLeft - l);
            offsetTopAndBottom(this.mTop - t);
        }
        super.onLayout(changed, this.mLeft, this.mTop, this.mRight, this.mBottom);
    }

	public void setClickListener(OnSymbolClickListener paramOnSymbolClickListener)
	{
		this.mListener = paramOnSymbolClickListener;
	}

	public void setVisibility(int visibility) {
        if (visibility == 8) {
            visibility = 4;
        }
        super.setVisibility(visibility);
    }
	

	private class GridAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> mData;

        public GridAdapter(Context context, List<String> data) {
            this.mData = data;
            this.mContext = context;
        }

        public int getCount() {
            return this.mData.size();
        }

        public Object getItem(int position) {
            return this.mData.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView != null) {
                return convertView;
            }
            String symbol = (String) this.mData.get(position);
            if ("\t".equals(symbol)) {
                symbol = "\\t";
            } else if ("\n".equals(symbol)) {
                symbol = "\\n";
            }
            TextView tv = new TextView(this.mContext);
            tv.setTextAppearance(this.mContext, R.style.symbolgrid_text);
            tv.setGravity(17);
            tv.setText(symbol);
            return tv;
        }
    }

	public static abstract interface OnSymbolClickListener
	{
		public abstract void OnClick(String paramString);
	}
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.SymbolGrid
 * JD-Core Version:    0.6.0
 */
