package com.qingshan.editor;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.qingshan.highlight.Highlight;
import com.qingshan.util.LinuxShell;
import com.qingshan.util.TimerUtil;
import com.qingshan.widget.QSEditText;
import com.stericson.RootTools.RootTools;
import java.io.File;
import java.io.FileNotFoundException;
import com.qingshan.util.FileUtil;
import org.mozilla.charsetdetector.Encoding;
import org.mozilla.charsetdetector.*;

public class AsyncReadFile
{
	public static final int RESULT_FAIL = 1;
	public static final int RESULT_OK = 0;
	private static final String TAG = "AsyncReadFile";
	private static boolean isRoot = false;
	private QSEditor mJecEditor;
	private ProgressDialog mProgressDialog;
	private ReadHandler mReadHandler;
	private int mSelEnd = 0;
	private int mSelStart = 0;
	
	//QSEditText.readFileToEditText调用
	public AsyncReadFile(final QSEditor mJecEditor, final String path, final String encoding, final int lineBreak, int selStart, int selEnd) {
        //this.mSelStart = RESULT_OK;
        //this.mSelEnd = RESULT_OK;
        QSEditor.isLoading = true;
        this.mJecEditor = mJecEditor;
        this.mSelStart = selStart;
        this.mSelEnd = selEnd;
        isRoot = QSEditor.isRoot;
        this.mReadHandler = new ReadHandler(this);
        showProgress();
       // new Thread(new AnonymousClass1(encoding, path, lineBreak, mJecEditor)).start();
    
		new Thread(new Runnable()
			{
				public void run()
				{
					String readFile;
					//Object obj = AsyncReadFile.RESULT_FAIL;
					Message obtainMessage = AsyncReadFile.this.mReadHandler.obtainMessage();
					Bundle bundle = new Bundle();
					String str = encoding;
					try {
						File file = new File(path);
						String absolutePath = file.getAbsolutePath();
						String str2 = QSEditor.TEMP_PATH + "/root_file_buffer.tmp";
						boolean root = false;
						if (!file.canRead() && AsyncReadFile.isRoot && RootTools.isAccessGiven()) {
							RootTools.copyFile(LinuxShell.getCmdPath(absolutePath), str2, true, true);
							RootTools.sendShell("busybox chmod 777 " + str2, 1000);
							file = new File(str2);
							absolutePath = str2;
							root = true;
						} /*else {
							obj = AsyncReadFile.RESULT_OK;
						}*/
						if ("".equals(str)) {
							str = AsyncReadFile.this.getEncoding(absolutePath);
						}
						//将所有的英文字符转换为大写
						if ("GB18030".equals(str.toUpperCase())) {
							str = "GBK";
						}
						if (file.isFile()) {
							//调试
							readFile = Highlight.readFile(absolutePath, str);
							if (lineBreak == 2) {
								readFile = readFile.replaceAll("\r\n|\r", "\n");
							} else if (lineBreak == 3) {
								readFile = readFile.replaceAll("\r\n|\r", "\r");
							}
							if (root/*obj != null*/) {
								LinuxShell.execute("rm -rf " + str2);
							}
							obtainMessage.what = AsyncReadFile.RESULT_OK;
							bundle.putString("data", readFile);
						} else {
							obtainMessage.what = AsyncReadFile.RESULT_FAIL;
							bundle.putString("error", AsyncReadFile.this.mJecEditor.getString(R.string.can_not_open_file));
						}
						
					/*}catch (FileNotFoundException e){
						e.printStackTrace();
						String errorMsg = e.getMessage();
						if(errorMsg.contains("Permission denied")){
							//EditorSettings.setBoolean("get_root", true);
							errorMsg = "mJecEditor.getString(R.string.try_root)";
							try
							{
								RootTools.sendShell("busybox ls " + path, 1000);
							} catch (Exception e1)
							{
								e1.printStackTrace();
							}
						}
						obtainMessage.what = RESULT_FAIL;
						bundle.putString("error", errorMsg);
						obtainMessage.setData(bundle);
						obtainMessage.sendToTarget();*/
					}catch (Exception e)
					{
						//while (true)
						//{
						String str4 = e.getMessage();
						obtainMessage.what = 1;
						bundle.putString("error", str4);
						//bundle.putString("path", path);
						//bundle.putString("encoding", str);
						//bundle.putInt("lineBreak", lineBreak);
						obtainMessage.setData(bundle);
						obtainMessage.sendToTarget();
						//}
					}
					catch (OutOfMemoryError e)
					{//内存不足
						//while (true)
						//{
						String errorMsg = mJecEditor.getString(R.string.out_of_memory/*2131230795*/);
						obtainMessage.what = 1;
						bundle.putString("error", errorMsg);
						//bundle.putString("path", path);
						//bundle.putString("encoding", str);
						//bundle.putInt("lineBreak", lineBreak);
						obtainMessage.setData(bundle);
						obtainMessage.sendToTarget();
						//}
					}
					finally
					{
						bundle.putString("path", path);
						bundle.putString("encoding", str);
						bundle.putInt("lineBreak", lineBreak);
						obtainMessage.setData(bundle);
						obtainMessage.sendToTarget();
					}
					//throw localObject1;
				}
			}).start();
	}
	/*public AsyncReadFile(final JecEditor mJecEditor, final String path, final String encoding, final int lineBreak, int selStart, int selEnd)
    {
        JecEditor.isLoading = true;
        // mJecEditor.text_content.requestFocus();
        this.mJecEditor = mJecEditor;
        mSelStart = selStart > 0 ? selStart : 0;
        mSelEnd = selEnd > 0 ? selEnd : 0;
        mReadHandler  = new ReadHandler(AsyncReadFile.this);
        showProgress();

        mEncoding = encoding;

        Thread thread = new Thread(new Runnable() {

				@Override
				public void run()
				{
					int what = RESULT_OK;
					AsyncResult result = new AsyncResult();
					try
					{
						String fileString = path;
						File file = new File(fileString);
						fileString = file.getAbsolutePath();

						String tempFile = JecEditor.TEMP_PATH + "/"+file.getName()+".tmp";
						//Log.d(TAG, "write:"+file.canWrite()+" rooted:"+isRoot+" access:"+RootTools.isAccessGiven());
						boolean root = false;
						if(!file.canWrite() && EditorSettings.TRY_ROOT && RootTools.isAccessGiven())
						{
							//RootTools.sendShell("busybox cp " + LinuxShell.getCmdPath(fileString) + " " + tempFile, 1000);
							RootTools.copyFile(LinuxShell.getCmdPath(fileString), tempFile, true, true);
							RootTools.sendShell("busybox chmod 777 " + tempFile, 1000);
							fileString = tempFile;
							file = new File(fileString);
							root = true;
						}

						if(file.isFile())
						{
							SpannableStringBuilder mData = readFile(file, lineBreak);

							if(root)
							{
								LinuxShell.execute("rm -rf " + tempFile);
							}
							what = RESULT_OK;
							result.data = mData;
						} else {
							what = RESULT_FAIL;
							result.errorMsg = AsyncReadFile.this.mJecEditor.getString(R.string.can_not_open_file);
						}
					}catch (FileNotFoundException e){
						e.printStackTrace();
						String errorMsg = e.getMessage();
						if(errorMsg.contains("Permission denied")){
							EditorSettings.setBoolean("get_root", true);
							errorMsg = mJecEditor.getString(R.string.try_root);
							try
							{
								RootTools.sendShell("busybox ls " + path, 1000);
							} catch (Exception e1)
							{
								e1.printStackTrace();
							}
						}
						what = RESULT_FAIL;
						result.errorMsg = errorMsg;
					}catch (Exception e)
					{
						e.printStackTrace();
						final String errorMsg = e.getMessage();// R.string.exception;
						what = RESULT_FAIL;
						result.errorMsg = errorMsg;
					}catch (OutOfMemoryError e)
					{//内存不足
						final String errorMsg = mJecEditor.getString(R.string.out_of_memory);
						what = RESULT_FAIL;
						result.errorMsg = errorMsg;
					} finally {
						result.path = path;
						result.encoding = mEncoding;
						result.linebreak = lineBreak;
						Message msg = mReadHandler.obtainMessage(what, result);
						msg.sendToTarget();
					}
				}
			});
        thread.start();

    }*/

	private void finish(String mData, String path, String encoding, int lineBreak) {
        try {
            TimerUtil.start();
            QSEditText mEditText = this.mJecEditor.getEditText();
            mEditText.setText2(mData);
            mEditText.setTextFinger();
            TimerUtil.stop("AsyncReadFile1");
            mEditText.setSelection(this.mSelStart, this.mSelEnd);
            mEditText.clearFocus();
            mEditText.setEncoding(encoding);
            mEditText.setLineBreak(lineBreak);
            mEditText.setPath(path);
            this.mJecEditor.onLoaded();
        } catch (OutOfMemoryError e) {
            Toast.makeText(this.mJecEditor, R.string.out_of_memory, RESULT_FAIL).show();
        } catch (Exception e2) {
            Toast.makeText(this.mJecEditor, e2.getMessage(), RESULT_FAIL).show();
        } finally {
            QSEditor.isLoading = false;
        }
    }
    
	private String getEncoding(String path) {
        //String encoding=CharsetDetector.getJavaEncode(path);
		String encoding = CharsetDetector.getEncoding(path).trim().toUpperCase();

		if ("".equals(encoding)) {
            return "UTF-8";
        }
        if ("GB18030".equals(encoding)) {
            return "GBK";
        }
        return encoding;
    }

	private void showProgress() {
        this.mProgressDialog = new ProgressDialog(this.mJecEditor);
        this.mProgressDialog.setTitle(R.string.spinner_message);
        this.mProgressDialog.setMessage(this.mJecEditor.getText(R.string.loading));
        this.mProgressDialog.setIndeterminate(true);
        this.mProgressDialog.setProgressStyle(RESULT_OK);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					AsyncReadFile.this.dismissProgress();
				}
			});
        this.mProgressDialog.show();
    }

	public void dismissProgress()
	{
		if (this.mProgressDialog != null)
			this.mProgressDialog.dismiss();
	}

	static class ReadHandler extends Handler {
        private AsyncReadFile mAsyncReadFile;

        public ReadHandler(AsyncReadFile arf) {
            this.mAsyncReadFile = arf;
        }

        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            String path = b.getString("path");
            String encoding = b.getString("encoding");
            int lineBreak = b.getInt("lineBreak");
            this.mAsyncReadFile.dismissProgress();
            if (msg.what == 0) {
                this.mAsyncReadFile.finish(b.getString("data"), path, encoding, lineBreak);
                return;
            }
            QSEditor.isLoading = false;
            Log.d(AsyncReadFile.TAG, b.getString("error"));
            Toast.makeText(this.mAsyncReadFile.mJecEditor, b.getString("error"), AsyncReadFile.RESULT_FAIL).show();
        }
    }
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.AsyncReadFile
 * JD-Core Version:    0.6.0
 */
