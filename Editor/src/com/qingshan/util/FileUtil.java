package com.qingshan.util;

import com.qingshan.editor.QSEditor;
import com.stericson.RootTools.RootTools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
//import java.util.*;
public class FileUtil
{
	public static final double ONE_GB = 1073741824.0D;
	public static final double ONE_KB = 1024.0D;
	public static final double ONE_MB = 1048576.0D;
	
	public static void writeFile(String paramString1, String paramString2)
	throws IOException
	{
		writeFile(paramString1, paramString2, "UTF-8", true);
	}
	/**
     * 写入文件, 需要指定编码
     * 
     * @param path
     * @param text
     * @param encoding
     * @return
     * @throws IOException
     */
	//QSEditor.save调用
	public static boolean writeFile(String path, String text, String encoding, boolean isRoot) throws IOException {
        File file = new File(path);
        String tempFile = QSEditor.TEMP_PATH + "/root_file_buffer.tmp";
        String fileString = path;
        boolean root = false;
        if (!file.canWrite() && isRoot && RootTools.isAccessGiven()) {
            fileString = tempFile;
            root = true;
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileString), encoding));
        bw.write(text);
        bw.close();
        if (root) {
          //  return true;
       // }
        RootTools.copyFile(fileString, LinuxShell.getCmdPath(path), true, true);
        if (RootTools.lastExitCode != 0) //{
            return false;
        //}
            new File(tempFile).delete();
		}
        return true;
    }
	
    
	//FileBrowser.showFileList调用
	
	public static ArrayList<File> getFileList(String path, boolean runAtRoot) {
        ArrayList<File> fileList = new ArrayList();
        ArrayList<File> folderList = new ArrayList();
        File file;
        if (runAtRoot) {
            try {
                for (String line : RootTools.sendShell("busybox ls -1 " + path, 1000)) {
                    if (!("".equals(line.trim()) || "0".equals(line.trim()))) {
                        file = new File(path, line);
                        if (line.endsWith("/") || file.isDirectory()) {
                            folderList.add(file);
                        } else {
                            fileList.add(file);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File[] files = new File(path).listFiles();
            if (files == null) {
                return null;
            }
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    folderList.add(file2);
                } else {
                    fileList.add(file2);
                }
            }
        }
	    //对集合对象或数组对象进行排序
        Comparator<File> mComparator = new Comparator<File>() {
            public int compare(File fl1, File fl2) {
                return fl1.getName().compareToIgnoreCase(fl2.getName());
            }
        };
        Collections.sort(fileList, mComparator);
        Collections.sort(folderList, mComparator);
        ArrayList<File> list = new ArrayList();
        Iterator it = folderList.iterator();
        while (it.hasNext()) {
            list.add((File) it.next());
        }
        it = fileList.iterator();
        while (it.hasNext()) {
            list.add((File) it.next());
        }
        return list;
    }
	
	
	public static String byteCountToDisplaySize(long size) {
        return byteCountToDisplaySize((double) size);
    }
	//FileListAdapter.getView调用
	public static String byteCountToDisplaySize(double size) {
        String displaySize;
        double ret = size / ONE_GB;
        if (ret > 1.0d) {
            displaySize = " G";
        } else {
            ret = size / ONE_MB;
            if (ret > 1.0d) {
                displaySize = " M";
            } else {
                ret = size / ONE_KB;
                if (ret > 1.0d) {
                    displaySize = " KB";
                } else {
                    ret = size;
                    displaySize = " B";
                }
            }
        }
        return new StringBuilder(String.valueOf(new DecimalFormat("0.00").format(ret))).append(displaySize).toString();
    }
	
	public static String Read(String filename, String encoding) {
        return Read(new File(filename), encoding);
    }
	public static String Read(File file, String encoding) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            char[] allChars = new char[((int) file.length())];
            in.read(allChars, 0, (int) file.length());
            in.close();
            return new String(allChars);
        } catch (IOException ex) {
            throw new RuntimeException(file + ": trouble reading", ex);
        }
    }


	public static String ReadFile(String filename) {
        return ReadFile(filename, "UTF-8");
    }

	//AsyncReadFile.AsyncReadFile调用
	public static String ReadFile(String filename, String encoding) {
        return ReadFile(new File(filename), encoding);
    }
	//ReadFile调用
	public static String ReadFile(File filename, String encoding) {
        try {
            return ReadFile(new FileInputStream(filename), encoding);
        } catch (FileNotFoundException e) {
            return "";
        }
    }
	//ReadFile调用
	public static String ReadFile(InputStream fis, String encoding) {
        StringBuilder b = new StringBuilder();
		//在这个平台下行与行之间的分隔符
		//相当于“\n”
        String sp = System.getProperty("line.separator");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding));
			try{
				String line = br.readLine();
                while (line != null) {
					b.append(line).append(sp);
					line = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return b.toString();
    }
	
	
	
	

	


	

	

	
	//QSEditText.setPath
	public static String getExt(String path) {
        int lastIndex = path.lastIndexOf(".");
        if (lastIndex == -1) {
            return null;
        }
        return path.substring(lastIndex + 1).trim().toLowerCase();
    }

	

	

	
	
	
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.FileUtil
 * JD-Core Version:    0.6.0
 */
