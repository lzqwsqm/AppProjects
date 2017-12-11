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
/*
	 .class Lcom/jecelyin/editor/AsyncReadFile$1;
	 .super Ljava/lang/Object;
	 .source "AsyncReadFile.java"

	 # interfaces
	 .implements Ljava/lang/Runnable;


	 # annotations
	 .annotation system Ldalvik/annotation/EnclosingMethod;
	 value = Lcom/jecelyin/editor/AsyncReadFile;-><init>(Lcom/jecelyin/editor/JecEditor;Ljava/lang/String;Ljava/lang/String;III)V
	 .end annotation

	 .annotation system Ldalvik/annotation/InnerClass;
	 accessFlags = 0x0
	 name = null
	 .end annotation


	 # instance fields
	 .field final synthetic this$0:Lcom/jecelyin/editor/AsyncReadFile;

	 .field private final synthetic val$encoding:Ljava/lang/String;

	 .field private final synthetic val$lineBreak:I

	 .field private final synthetic val$mJecEditor:Lcom/jecelyin/editor/JecEditor;

	 .field private final synthetic val$path:Ljava/lang/String;


	 # direct methods
	 .method constructor <init>(Lcom/jecelyin/editor/AsyncReadFile;Ljava/lang/String;Ljava/lang/String;ILcom/jecelyin/editor/JecEditor;)V
	 .locals 0

	 .prologue
	 .line 1
	 iput-object p1, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->this$0:Lcom/jecelyin/editor/AsyncReadFile;

	 iput-object p2, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$encoding:Ljava/lang/String;

	 iput-object p3, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$path:Ljava/lang/String;

	 iput p4, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$lineBreak:I

	 iput-object p5, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$mJecEditor:Lcom/jecelyin/editor/JecEditor;

	 .line 67
	 invoke-direct {p0}, Ljava/lang/Object;-><init>()V

	 return-void
	 .end method


	 # virtual methods
	 .method public run()V
	 .locals 13

	 .prologue
	 .line 72
	 iget-object v10, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->this$0:Lcom/jecelyin/editor/AsyncReadFile;

	 # getter for: Lcom/jecelyin/editor/AsyncReadFile;->mReadHandler:Lcom/jecelyin/editor/AsyncReadFile$ReadHandler;
	 invoke-static {v10}, Lcom/jecelyin/editor/AsyncReadFile;->access$2(Lcom/jecelyin/editor/AsyncReadFile;)Lcom/jecelyin/editor/AsyncReadFile$ReadHandler;

	 move-result-object v10

	 invoke-virtual {v10}, Lcom/jecelyin/editor/AsyncReadFile$ReadHandler;->obtainMessage()Landroid/os/Message;

	 move-result-object v7

	 .line 73
	 .local v7, "msg":Landroid/os/Message;
	 new-instance v0, Landroid/os/Bundle;

	 invoke-direct {v0}, Landroid/os/Bundle;-><init>()V

	 .line 74
	 .local v0, "b":Landroid/os/Bundle;
	 iget-object v2, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$encoding:Ljava/lang/String;

	 .line 77
	 .local v2, "enc":Ljava/lang/String;
	 :try_start_0
	 iget-object v5, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$path:Ljava/lang/String;

	 .line 78
	 .local v5, "fileString":Ljava/lang/String;
	 new-instance v4, Ljava/io/File;

	 invoke-direct {v4, v5}, Ljava/io/File;-><init>(Ljava/lang/String;)V

	 .line 79
	 .local v4, "file":Ljava/io/File;
	 invoke-virtual {v4}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

	 move-result-object v5

	 .line 81
	 new-instance v10, Ljava/lang/StringBuilder;

	 sget-object v11, Lcom/jecelyin/editor/JecEditor;->TEMP_PATH:Ljava/lang/String;

	 invoke-static {v11}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

	 move-result-object v11

	 invoke-direct {v10, v11}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

	 const-string v11, "/root_file_buffer.tmp"

	 invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

	 move-result-object v10

	 invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

	 move-result-object v9

	 .line 82
	 .local v9, "tempFile":Ljava/lang/String;
	 const/4 v8, 0x0

	 .line 83
	 .local v8, "root":Z
	 invoke-virtual {v4}, Ljava/io/File;->canRead()Z

	 move-result v10

	 if-nez v10, :cond_0

	 # getter for: Lcom/jecelyin/editor/AsyncReadFile;->isRoot:Z
	 invoke-static {}, Lcom/jecelyin/editor/AsyncReadFile;->access$3()Z

	 move-result v10

	 if-eqz v10, :cond_0

	 invoke-static {}, Lcom/stericson/RootTools/RootTools;->isAccessGiven()Z

	 move-result v10

	 if-eqz v10, :cond_0

	 .line 87
	 invoke-static {v5}, Lcom/jecelyin/util/LinuxShell;->getCmdPath(Ljava/lang/String;)Ljava/lang/String;

	 move-result-object v10

	 const/4 v11, 0x1

	 const/4 v12, 0x1

	 invoke-static {v10, v9, v11, v12}, Lcom/stericson/RootTools/RootTools;->copyFile(Ljava/lang/String;Ljava/lang/String;ZZ)Z

	 .line 88
	 new-instance v10, Ljava/lang/StringBuilder;

	 const-string v11, "busybox chmod 777 "

	 invoke-direct {v10, v11}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

	 invoke-virtual {v10, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

	 move-result-object v10

	 invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

	 move-result-object v10

	 const/16 v11, 0x3e8

	 invoke-static {v10, v11}, Lcom/stericson/RootTools/RootTools;->sendShell(Ljava/lang/String;I)Ljava/util/List;

	 .line 89
	 move-object v5, v9

	 .line 90
	 new-instance v4, Ljava/io/File;

	 .end local v4    # "file":Ljava/io/File;
	 invoke-direct {v4, v5}, Ljava/io/File;-><init>(Ljava/lang/String;)V

	 .line 91
	 .restart local v4    # "file":Ljava/io/File;
	 const/4 v8, 0x1

	 .line 93
	 :cond_0
	 const-string v10, ""

	 invoke-virtual {v10, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

	 move-result v10

	 if-eqz v10, :cond_1

	 .line 94
	 iget-object v10, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->this$0:Lcom/jecelyin/editor/AsyncReadFile;

	 # invokes: Lcom/jecelyin/editor/AsyncReadFile;->getEncoding(Ljava/lang/String;)Ljava/lang/String;
	 invoke-static {v10, v5}, Lcom/jecelyin/editor/AsyncReadFile;->access$4(Lcom/jecelyin/editor/AsyncReadFile;Ljava/lang/String;)Ljava/lang/String;

	 move-result-object v2

	 .line 95
	 :cond_1
	 const-string v10, "GB18030"

	 invoke-virtual {v2}, Ljava/lang/String;->toUpperCase()Ljava/lang/String;

	 move-result-object v11

	 invoke-virtual {v10, v11}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

	 move-result v10

	 if-eqz v10, :cond_2

	 .line 96
	 const-string v2, "GBK"

	 .line 98
	 :cond_2
	 invoke-virtual {v4}, Ljava/io/File;->isFile()Z

	 move-result v10

	 if-eqz v10, :cond_6

	 .line 100
	 invoke-static {v5, v2}, Lcom/jecelyin/highlight/Highlight;->readFile(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

	 move-result-object v6

	 .line 101
	 .local v6, "mData":Ljava/lang/String;
	 iget v10, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$lineBreak:I

	 const/4 v11, 0x2

	 if-ne v10, v11, :cond_5

	 .line 103
	 const-string v10, "\r\n|\r"

	 const-string v11, "\n"

	 invoke-virtual {v6, v10, v11}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

	 move-result-object v6

	 .line 109
	 :cond_3
	 :goto_0
	 if-eqz v8, :cond_4

	 .line 111
	 new-instance v10, Ljava/lang/StringBuilder;

	 const-string v11, "rm -rf "

	 invoke-direct {v10, v11}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

	 invoke-virtual {v10, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

	 move-result-object v10

	 invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

	 move-result-object v10

	 invoke-static {v10}, Lcom/jecelyin/util/LinuxShell;->execute(Ljava/lang/String;)Ljava/io/BufferedReader;

	 .line 113
	 :cond_4
	 const/4 v10, 0x0

	 iput v10, v7, Landroid/os/Message;->what:I

	 .line 114
	 const-string v10, "data"

	 invoke-virtual {v0, v10, v6}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V
	 :try_end_0
	 .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0
	 .catch Ljava/lang/OutOfMemoryError; {:try_start_0 .. :try_end_0} :catch_1
	 .catchall {:try_start_0 .. :try_end_0} :catchall_0

	 .line 131
	 .end local v6    # "mData":Ljava/lang/String;
	 :goto_1
	 const-string v10, "path"

	 iget-object v11, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$path:Ljava/lang/String;

	 invoke-virtual {v0, v10, v11}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

	 .line 132
	 const-string v10, "encoding"

	 invoke-virtual {v0, v10, v2}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

	 .line 133
	 const-string v10, "lineBreak"

	 iget v11, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$lineBreak:I

	 invoke-virtual {v0, v10, v11}, Landroid/os/Bundle;->putInt(Ljava/lang/String;I)V

	 .line 134
	 invoke-virtual {v7, v0}, Landroid/os/Message;->setData(Landroid/os/Bundle;)V

	 .line 135
	 invoke-virtual {v7}, Landroid/os/Message;->sendToTarget()V

	 .line 137
	 .end local v4    # "file":Ljava/io/File;
	 .end local v5    # "fileString":Ljava/lang/String;
	 .end local v8    # "root":Z
	 .end local v9    # "tempFile":Ljava/lang/String;
	 :goto_2
	 return-void

	 .line 104
	 .restart local v4    # "file":Ljava/io/File;
	 .restart local v5    # "fileString":Ljava/lang/String;
	 .restart local v6    # "mData":Ljava/lang/String;
	 .restart local v8    # "root":Z
	 .restart local v9    # "tempFile":Ljava/lang/String;
	 :cond_5
	 :try_start_1
	 iget v10, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$lineBreak:I

	 const/4 v11, 0x3

	 if-ne v10, v11, :cond_3

	 .line 107
	 const-string v10, "\r\n|\r"

	 const-string v11, "\r"

	 invoke-virtual {v6, v10, v11}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

	 move-result-object v6

	 goto :goto_0

	 .line 116
	 .end local v6    # "mData":Ljava/lang/String;
	 :cond_6
	 const/4 v10, 0x1

	 iput v10, v7, Landroid/os/Message;->what:I

	 .line 117
	 const-string v10, "error"

	 iget-object v11, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->this$0:Lcom/jecelyin/editor/AsyncReadFile;

	 # getter for: Lcom/jecelyin/editor/AsyncReadFile;->mJecEditor:Lcom/jecelyin/editor/JecEditor;
	 invoke-static {v11}, Lcom/jecelyin/editor/AsyncReadFile;->access$1(Lcom/jecelyin/editor/AsyncReadFile;)Lcom/jecelyin/editor/JecEditor;

	 move-result-object v11

	 const v12, 0x7f08009b

	 invoke-virtual {v11, v12}, Lcom/jecelyin/editor/JecEditor;->getString(I)Ljava/lang/String;

	 move-result-object v11

	 invoke-virtual {v0, v10, v11}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V
	 :try_end_1
	 .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_0
	 .catch Ljava/lang/OutOfMemoryError; {:try_start_1 .. :try_end_1} :catch_1
	 .catchall {:try_start_1 .. :try_end_1} :catchall_0

	 goto :goto_1

	 .line 120
	 .end local v4    # "file":Ljava/io/File;
	 .end local v5    # "fileString":Ljava/lang/String;
	 .end local v8    # "root":Z
	 .end local v9    # "tempFile":Ljava/lang/String;
	 :catch_0
	 move-exception v1

	 .line 122
	 .local v1, "e":Ljava/lang/Exception;
	 :try_start_2
	 invoke-virtual {v1}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

	 move-result-object v3

	 .line 123
	 .local v3, "errorMsg":Ljava/lang/String;
	 const/4 v10, 0x1

	 iput v10, v7, Landroid/os/Message;->what:I

	 .line 124
	 const-string v10, "error"

	 invoke-virtual {v0, v10, v3}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V
	 :try_end_2
	 .catchall {:try_start_2 .. :try_end_2} :catchall_0

	 .line 131
	 const-string v10, "path"

	 iget-object v11, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$path:Ljava/lang/String;

	 invoke-virtual {v0, v10, v11}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

	 .line 132
	 const-string v10, "encoding"

	 invoke-virtual {v0, v10, v2}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

	 .line 133
	 const-string v10, "lineBreak"

	 iget v11, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$lineBreak:I

	 invoke-virtual {v0, v10, v11}, Landroid/os/Bundle;->putInt(Ljava/lang/String;I)V

	 .line 134
	 invoke-virtual {v7, v0}, Landroid/os/Message;->setData(Landroid/os/Bundle;)V

	 .line 135
	 invoke-virtual {v7}, Landroid/os/Message;->sendToTarget()V

	 goto :goto_2

	 .line 125
	 .end local v1    # "e":Ljava/lang/Exception;
	 .end local v3    # "errorMsg":Ljava/lang/String;
	 :catch_1
	 move-exception v1

	 .line 127
	 .local v1, "e":Ljava/lang/OutOfMemoryError;
	 :try_start_3
	 iget-object v10, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$mJecEditor:Lcom/jecelyin/editor/JecEditor;

	 const v11, 0x7f08004b

	 invoke-virtual {v10, v11}, Lcom/jecelyin/editor/JecEditor;->getString(I)Ljava/lang/String;

	 move-result-object v3

	 .line 128
	 .restart local v3    # "errorMsg":Ljava/lang/String;
	 const/4 v10, 0x1

	 iput v10, v7, Landroid/os/Message;->what:I

	 .line 129
	 const-string v10, "error"

	 invoke-virtual {v0, v10, v3}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V
	 :try_end_3
	 .catchall {:try_start_3 .. :try_end_3} :catchall_0

	 .line 131
	 const-string v10, "path"

	 iget-object v11, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$path:Ljava/lang/String;

	 invoke-virtual {v0, v10, v11}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

	 .line 132
	 const-string v10, "encoding"

	 invoke-virtual {v0, v10, v2}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

	 .line 133
	 const-string v10, "lineBreak"

	 iget v11, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$lineBreak:I

	 invoke-virtual {v0, v10, v11}, Landroid/os/Bundle;->putInt(Ljava/lang/String;I)V

	 .line 134
	 invoke-virtual {v7, v0}, Landroid/os/Message;->setData(Landroid/os/Bundle;)V

	 .line 135
	 invoke-virtual {v7}, Landroid/os/Message;->sendToTarget()V

	 goto :goto_2

	 .line 130
	 .end local v1    # "e":Ljava/lang/OutOfMemoryError;
	 .end local v3    # "errorMsg":Ljava/lang/String;
	 :catchall_0
	 move-exception v10

	 .line 131
	 const-string v11, "path"

	 iget-object v12, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$path:Ljava/lang/String;

	 invoke-virtual {v0, v11, v12}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

	 .line 132
	 const-string v11, "encoding"

	 invoke-virtual {v0, v11, v2}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

	 .line 133
	 const-string v11, "lineBreak"

	 iget v12, p0, Lcom/jecelyin/editor/AsyncReadFile$1;->val$lineBreak:I

	 invoke-virtual {v0, v11, v12}, Landroid/os/Bundle;->putInt(Ljava/lang/String;I)V

	 .line 134
	 invoke-virtual {v7, v0}, Landroid/os/Message;->setData(Landroid/os/Bundle;)V

	 .line 135
	 invoke-virtual {v7}, Landroid/os/Message;->sendToTarget()V

	 .line 136
	 throw v10
	 .end method
	 
*/
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
    /*
	 .method private finish(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
	 .locals 6
	 .param p1, "mData"    # Ljava/lang/String;
	 .param p2, "path"    # Ljava/lang/String;
	 .param p3, "encoding"    # Ljava/lang/String;
	 .param p4, "lineBreak"    # I

	 .prologue
	 const/4 v5, 0x0

	 .line 198
	 :try_start_0
	 invoke-static {}, Lcom/jecelyin/util/TimerUtil;->start()V

	 .line 199
	 iget-object v2, p0, Lcom/jecelyin/editor/AsyncReadFile;->mJecEditor:Lcom/jecelyin/editor/JecEditor;

	 invoke-virtual {v2}, Lcom/jecelyin/editor/JecEditor;->getEditText()Lcom/jecelyin/widget/JecEditText;

	 move-result-object v1

	 .line 200
	 .local v1, "mEditText":Lcom/jecelyin/widget/JecEditText;
	 invoke-virtual {v1, p1}, Lcom/jecelyin/widget/JecEditText;->setText2(Ljava/lang/CharSequence;)V

	 .line 201
	 const/4 p1, 0x0

	 .line 202
	 invoke-virtual {v1}, Lcom/jecelyin/widget/JecEditText;->setTextFinger()V

	 .line 203
	 const-string v2, "AsyncReadFile1"

	 invoke-static {v2}, Lcom/jecelyin/util/TimerUtil;->stop(Ljava/lang/String;)V

	 .line 206
	 iget v2, p0, Lcom/jecelyin/editor/AsyncReadFile;->mSelStart:I

	 iget v3, p0, Lcom/jecelyin/editor/AsyncReadFile;->mSelEnd:I

	 invoke-virtual {v1, v2, v3}, Lcom/jecelyin/widget/JecEditText;->setSelection(II)V

	 .line 207
	 invoke-virtual {v1}, Lcom/jecelyin/widget/JecEditText;->clearFocus()V

	 .line 209
	 invoke-virtual {v1, p3}, Lcom/jecelyin/widget/JecEditText;->setEncoding(Ljava/lang/String;)V

	 .line 210
	 invoke-virtual {v1, p4}, Lcom/jecelyin/widget/JecEditText;->setLineBreak(I)V

	 .line 211
	 invoke-virtual {v1, p2}, Lcom/jecelyin/widget/JecEditText;->setPath(Ljava/lang/String;)V

	 .line 212
	 iget-object v2, p0, Lcom/jecelyin/editor/AsyncReadFile;->mJecEditor:Lcom/jecelyin/editor/JecEditor;

	 invoke-virtual {v2}, Lcom/jecelyin/editor/JecEditor;->onLoaded()V
	 :try_end_0
	 .catch Ljava/lang/OutOfMemoryError; {:try_start_0 .. :try_end_0} :catch_0
	 .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_1
	 .catchall {:try_start_0 .. :try_end_0} :catchall_0

	 .line 221
	 sput-boolean v5, Lcom/jecelyin/editor/JecEditor;->isLoading:Z

	 .line 223
	 .end local v1    # "mEditText":Lcom/jecelyin/widget/JecEditText;
	 :goto_0
	 return-void

	 .line 214
	 :catch_0
	 move-exception v0

	 .line 216
	 .local v0, "e":Ljava/lang/OutOfMemoryError;
	 :try_start_1
	 iget-object v2, p0, Lcom/jecelyin/editor/AsyncReadFile;->mJecEditor:Lcom/jecelyin/editor/JecEditor;

	 const v3, 0x7f08004b

	 const/4 v4, 0x1

	 invoke-static {v2, v3, v4}, Landroid/widget/Toast;->makeText(Landroid/content/Context;II)Landroid/widget/Toast;

	 move-result-object v2

	 invoke-virtual {v2}, Landroid/widget/Toast;->show()V
	 :try_end_1
	 .catchall {:try_start_1 .. :try_end_1} :catchall_0

	 .line 221
	 sput-boolean v5, Lcom/jecelyin/editor/JecEditor;->isLoading:Z

	 goto :goto_0

	 .line 217
	 .end local v0    # "e":Ljava/lang/OutOfMemoryError;
	 :catch_1
	 move-exception v0

	 .line 219
	 .local v0, "e":Ljava/lang/Exception;
	 :try_start_2
	 iget-object v2, p0, Lcom/jecelyin/editor/AsyncReadFile;->mJecEditor:Lcom/jecelyin/editor/JecEditor;

	 invoke-virtual {v0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

	 move-result-object v3

	 const/4 v4, 0x1

	 invoke-static {v2, v3, v4}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

	 move-result-object v2

	 invoke-virtual {v2}, Landroid/widget/Toast;->show()V
	 :try_end_2
	 .catchall {:try_start_2 .. :try_end_2} :catchall_0

	 .line 221
	 sput-boolean v5, Lcom/jecelyin/editor/JecEditor;->isLoading:Z

	 goto :goto_0

	 .line 220
	 .end local v0    # "e":Ljava/lang/Exception;
	 :catchall_0
	 move-exception v2

	 .line 221
	 sput-boolean v5, Lcom/jecelyin/editor/JecEditor;->isLoading:Z

	 .line 222
	 throw v2
	 .end method
*/
	private String getEncoding(String path) {
        String encoding=CharsetDetector.getJavaEncode(path);
		
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
