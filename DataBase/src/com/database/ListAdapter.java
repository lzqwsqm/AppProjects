package com.database;



import android.R.integer;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;
import java.util.Vector;

public class ListAdapter extends BaseAdapter
{
    Context context;
    Vector<String[]> listdata = null;
    int[] width_layout;

	//构造函数
    public ListAdapter(Context context)
    {
        this.context = context;
        this.listdata = new Vector();
    }
    //将指定 的所有元素追加到此向量的末尾，按照指定集合的迭代器所返回的顺序追加这些元素
    public void append(List<String[]> listdata)
    {
        this.listdata.addAll(listdata);
        notifyDataSetChanged();
    }
    //从此向量中移除所有元素。
    public void clear()
    {
        this.listdata.clear();
    }

//	@Override
	//设置像layout中填充的条目数量
    public int getCount()
    {
        return this.listdata.size();
    }

  //  @Override
    public Object getItem(int position)
    {
        return this.listdata.get(position);
    }

//    @Override
    public long getItemId(int position)
    {
        return position;
    }

//	@Override
	/**
	 * position就是位置从0开始
	 * convertView是Spinner,ListView中每一项要显示的view
	 * parent就是父窗体了，也就是Spinner,ListView,GridView了
	 * 通常return 的view也就是convertView
	 * 绘制的内容均在此实现
	 */
    
	public View getView(int position, View convertView, ViewGroup parent)
    {
        int index = 0;
        if (position < MainActivity.index.size())
            index = ((Integer)MainActivity.index.get(position)).intValue();
        return new ListRowView(this.context, (String[])getItem(position), this.width_layout, index);
    }

    public void remove(int index)
    {
        if (index < this.listdata.size())
			this.listdata.remove(index);
    }

    public void setLayout(int[] width_layout)
    {
        this.width_layout = width_layout;
    }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.FinanceAdapter
 * JD-Core Version:    0.6.0
 */

