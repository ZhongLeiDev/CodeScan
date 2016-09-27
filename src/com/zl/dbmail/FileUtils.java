package com.zl.dbmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.util.Log;

public class FileUtils {
	/**  
     * ���Ƶ����ļ�  
     * @param oldPath String ԭ�ļ�·�� �磺c:/fqf.txt  
     * @param newPath String ���ƺ�·�� �磺f:/fqf.txt  
     * @return boolean  
     */   
   public static void copyFile(String oldPath, String newPath) {
       try {   
           int byteread = 0;   
           File oldfile = new File(oldPath);   
           if (oldfile.exists()) { //�ļ�����ʱ  
               InputStream inStream = new FileInputStream(oldPath); //����ԭ�ļ�    
               FileOutputStream fs = new FileOutputStream(newPath);   
               byte[] buffer = new byte[1024];   
               while ( (byteread = inStream.read(buffer)) != -1) {   
                   fs.write(buffer, 0, byteread);   
               }   
               inStream.close();  
               fs.close();
           }   
       }   
       catch (Exception e) {   
           System.out.println("���Ƶ����ļ���������");   
           e.printStackTrace();   
  
       }   
  
   }   
  
   /**  
    * ���������ļ�������  
    * @param oldPath String ԭ�ļ�·�� �磺c:/fqf  
    * @param newPath String ���ƺ�·�� �磺f:/fqf/ff  
    * @return boolean  
    */   
   public static void copyFolder(String oldPath, String newPath) {   
  
       try {   
           (new File(newPath)).mkdirs(); //����ļ��в����� �������ļ���  
           File a=new File(oldPath);   
           String[] file=a.list();   
           File temp=null;   
           for (int i = 0; i < file.length; i++) {   
               if(oldPath.endsWith(File.separator)){   
                   temp=new File(oldPath+file[i]);   
               }   
               else{   
                   temp=new File(oldPath+File.separator+file[i]);   
               }   
  
               if(temp.isFile()){   
                   FileInputStream input = new FileInputStream(temp);   
                   FileOutputStream output = new FileOutputStream(newPath + "/" +   
                           (temp.getName()).toString());   
                   byte[] b = new byte[1024 * 5];   
                   int len;   
                   while ( (len = input.read(b)) != -1) {   
                       output.write(b, 0, len);   
                   }   
                   output.flush();   
                   output.close();   
                   input.close();   
               }   
               if(temp.isDirectory()){//��������ļ���      
                   copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);   
               }   
           }   
       }   
       catch (Exception e) {   
           System.out.println("���������ļ������ݲ�������");   
           e.printStackTrace();   
  
       }   
  
   }  
   
   /**
    * ɾ��ָ���ļ�
    * @param filePath
    */
   public static void deleteFile(String filePath){
	   File f = new File(filePath);
	   if(f.exists()){
		   f.delete();
	   }else{
		   Log.i("FileHandle", "ɾ���ļ�����");
	   }
   }

}
