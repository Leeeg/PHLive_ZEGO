package com.i5i58.live.common.utils.file;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.utils.tSnackBar.TSnackbarUtil;
import com.i5i58.live.model.entity.sys.SystemCache;

import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by Lee on 2017/4/17.
 */

public class FileUtils {

    /**
     * 判断文件是否存在
     */
    public static boolean isExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath){
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                if (null == files) return;
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    file.delete();
                } else {// 目录
                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }
                }
            }
        }
    }

    /**
     * 保存bitmap为本地图片
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Activity context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(SystemCache.BASE_PATH + SystemCache.ROOT_PATH , "myQrCode");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "")));
        MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
        TSnackbarUtil.TSBSuccess(context,"保存成功：\n" + SystemCache.BASE_PATH + SystemCache.ROOT_PATH + "myQrCode.png");
    }

    /**
     * 文件转数组
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath) {
        LogUtil.d("filePath : " + filePath);
        byte[] buffer = null;
        try{
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){

        }
        return buffer;
    }

    /**
     * 获取dradable文件
     * @param type
     * @param id
     * @return
     */
    public static int getResouceID(String type, String id) {
        return SystemCache.getContext().getResources().getIdentifier(id, type, "com.i5i58.live");
    }

    /**
     * 创建文件夹
     */
    public static void creatDir(String path){
        File destDir = new File(path);
        if (checkStatus() && !destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 检测SD卡挂载
     * @return
     */
    public static boolean checkStatus(){
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解压缩一个文件
     * @param zipFile    压缩文件
     * @param folderPath 解压缩的目标目录
     * @throws IOException 当解压缩过程出错时抛出
     */

    public static void extractZipFile(File zipFile, String folderPath) throws ZipException,IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            // 创建目标目录
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            if (entry.isDirectory()) {
                String tmpStr = folderPath + File.separator + entry.getName();
                tmpStr = new String(tmpStr.getBytes("8859_1"), "GB2312");
                File folder = new File(tmpStr);
                folder.mkdirs();
            } else {
                InputStream is = zf.getInputStream(entry);
                String str = folderPath + File.separator + entry.getName();
                // 转换编码，避免中文时乱码
                str = new String(str.getBytes("8859_1"), "GB2312");
                File desFile = new File(str);
                if (!desFile.exists()) {
                    // 创建目标文件
                    desFile.createNewFile();
                }
                OutputStream os = new FileOutputStream(desFile);
                byte[] buffer = new byte[1024];
                int realLength;
                while ((realLength = is.read(buffer)) > 0) {
                    os.write(buffer, 0, realLength);
                    os.flush();
                }
                is.close();
                os.close();
            }
        }
        LogUtil.d("extractZipFile over!");
        zf.close();
        deleteFile(zipFile);
    }

    /**
     * 删除单个文件
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(File file) {
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }


    private static final String TAG = "FileUtil";

    public static boolean hasExtentsion(String filename) {
        int dot = filename.lastIndexOf('.');
        if ((dot > -1) && (dot < (filename.length() - 1))) {
            return true;
        } else {
            return false;
        }
    }

    // 获取文件扩展名
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    // 获取文件名
    public static String getFileNameFromPath(String filepath) {
        if ((filepath != null) && (filepath.length() > 0)) {
            int sep = filepath.lastIndexOf('/');
            if ((sep > -1) && (sep < filepath.length() - 1)) {
                return filepath.substring(sep + 1);
            }
        }
        return filepath;
    }

    // 获取不带扩展名的文件名
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String getMimeType(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        String type = null;
        String extension = getExtensionName(filePath.toLowerCase());
        if (!TextUtils.isEmpty(extension)) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        Log.i(TAG, "url:" + filePath + " " + "type:" + type);

        // FIXME
        if (StringUtil.isEmpty(type) && filePath.endsWith("aac")) {
            type = "audio/aac";
        }

        return type;
    }

    public enum SizeUnit {
        Byte,
        KB,
        MB,
        GB,
        TB,
        Auto,
    }

    public static String formatFileSize(long size) {
        return formatFileSize(size, SizeUnit.Auto);
    }

    public static String formatFileSize(long size, SizeUnit unit) {
        if (size < 0) {
            return "";
        }

        final double KB = 1024;
        final double MB = KB * 1024;
        final double GB = MB * 1024;
        final double TB = GB * 1024;
        if (unit == SizeUnit.Auto) {
            if (size < KB) {
                unit = SizeUnit.Byte;
            } else if (size < MB) {
                unit = SizeUnit.KB;
            } else if (size < GB) {
                unit = SizeUnit.MB;
            } else if (size < TB) {
                unit = SizeUnit.GB;
            } else {
                unit = SizeUnit.TB;
            }
        }

        switch (unit) {
            case Byte:
                return size + "B";
            case KB:
                return String.format(Locale.US, "%.2fKB", size / KB);
            case MB:
                return String.format(Locale.US, "%.2fMB", size / MB);
            case GB:
                return String.format(Locale.US, "%.2fGB", size / GB);
            case TB:
                return String.format(Locale.US, "%.2fPB", size / TB);
            default:
                return size + "B";
        }
    }

}
