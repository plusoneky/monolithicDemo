package com.qq1833111108.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * @Title: GZIPUtil.java
 * @Description: gzip文件压缩和解压缩工具类
 * @author LM
 * @date 2009-11-4 下午06:23:29
 * @version V1.0
 */
public class GZIPUtil {
 
 /**
  * 
  * @Title: pack
  * @Description: 将一组文件打成tar包
  * @param sources 要打包的原文件数组
  * @param target 打包后的文件
  * @return File    返回打包后的文件
  * @throws
  */
	

 public static File pack(File[] sources, String targetFileAbsPath){
  File target = new File(targetFileAbsPath);
  FileOutputStream out = null;
  try {
   out = new FileOutputStream(target);
  } catch (FileNotFoundException e1) {
   e1.printStackTrace();
  }
  TarArchiveOutputStream os = new TarArchiveOutputStream(out);
  for (File file : sources) {
   try {
    os.putArchiveEntry(new TarArchiveEntry(file));
    IOUtils.copy(new FileInputStream(file), os);
    os.closeArchiveEntry();
     
   } catch (FileNotFoundException e) {
    e.printStackTrace();
   } catch (IOException e) {
    e.printStackTrace();
   } 
  }
  if(os != null) {
   try {
    os.flush();
    os.close();
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
  
  return target;
 }

 /**
  * 
  * @Title: compress
  * @Description: 将文件用gzip压缩
  * @param  source 需要压缩的文件
  * @return File    返回压缩后的文件
  * @throws
  */
 public static File compress(File source,String targetDir) {
  File target = new File(targetDir+source.getName() + ".gz");
  FileInputStream in = null;
  GZIPOutputStream out = null;
  try {
   in = new FileInputStream(source);
   out = new GZIPOutputStream(new FileOutputStream(target));
   byte[] array = new byte[1024];
   int number = -1;
   while((number = in.read(array, 0, array.length)) != -1) {
    out.write(array, 0, number);
   }
  } catch (FileNotFoundException e) {
   e.printStackTrace();
   return null;
  } catch (IOException e) {
   e.printStackTrace();
   return null;
  } finally {
   if(in != null) {
    try {
     in.close();
    } catch (IOException e) {
     e.printStackTrace();
     return null;
    }
   }
   
   if(out != null) {
    try {
     out.close();
    } catch (IOException e) {
     e.printStackTrace();
     return null;
    }
   }
  }
  return target;
 }

 public static void main(String[] args) {
  File[] sources = new File[] {new File("E:/uploaddownloaddir/apk/2017-12/19/0b4fee01-bd08-4887-94ad-7f4253754c4f-error.txt"), new File("E:/uploaddownloaddir/apk/2017-12/19/0ba0e948-89c9-4b20-9b18-6516f0427881-error.txt")};
  compress(pack(sources,"E:/uploaddownloaddir/release_package.tar"),"E:/uploaddownloaddir/");
 }
}
