package org.edgar.utils;

import android.os.Environment;
import android.os.StatFs;

import org.edgar.net.HttpRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Date;

public class BaseUtils {
    public static String UrlEncodeUnicode(final String s)
    {
        if (s == null)
        {
            return null;
        }
        final int length = s.length();
        final StringBuilder builder = new StringBuilder(length); // buffer
        for (int i = 0; i < length; i++)
        {
            final char ch = s.charAt(i);
            if ((ch & 0xff80) == 0)
            {
                if (BaseUtils.IsSafe(ch))
                {
                    builder.append(ch);
                }
                else if (ch == ' ')
                {
                    builder.append('+');
                }
                else
                {
                    builder.append('%');
                    builder.append(BaseUtils.IntToHex((ch >> 4) & 15));
                    builder.append(BaseUtils.IntToHex(ch & 15));
                }
            }
            else
            {
                builder.append("%u");
                builder.append(BaseUtils.IntToHex((ch >> 12) & 15));
                builder.append(BaseUtils.IntToHex((ch >> 8) & 15));
                builder.append(BaseUtils.IntToHex((ch >> 4) & 15));
                builder.append(BaseUtils.IntToHex(ch & 15));
            }
        }
        return builder.toString();
    }
    
    static char IntToHex(final int n)
    {
        if (n <= 9)
        {
            return (char) (n + 0x30);
        }
        return (char) ((n - 10) + 0x61);
    }
    
    static boolean IsSafe(final char ch)
    {
        if ((((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))) || ((ch >= '0') && (ch <= '9')))
        {
            return true;
        }
        switch (ch)
        {
            case '\'':
            case '(':
            case ')':
            case '*':
            case '-':
            case '.':
            case '_':
            case '!':
                return true;
        }
        return false;
    }
    
    // ----------------------------------------------------------------------------------------------------------------------------
    // MD5相关函数
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'                      };

    /**
     * MD5运算(兼容汉字)
     * 
     * @param str
     * @return String 返回密文
     */
    public static String getMd5(final String str)
    {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        StringBuffer hexValue = null;

        if (str != null) {

            byte[] byteArray = new byte[0];
            try {
                byteArray = str.getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            byte[] md5Bytes = md5.digest(byteArray);
            hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = md5Bytes[i] & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        }
        return str;
    }
    
    /**
     * 转换为十六进制字符串
     * 
     * @param b
     *            byte数组
     * @return String byte数组处理后字符串
     */
    public static String toHexString(final byte[] b)
    {// String to byte
        final StringBuilder sb = new StringBuilder(b.length * 2);
        for (final byte element : b)
        {
            sb.append(BaseUtils.HEX_DIGITS[(element & 0xf0) >>> 4]);
            sb.append(BaseUtils.HEX_DIGITS[element & 0x0f]);
        }
        return sb.toString();
    }
    
    /**
     * 检查是否安装了sd卡
     * 
     * @return false 未安装
     */
    public static boolean sdcardMounted()
    {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 获取SD卡剩余空间的大小
     * 
     * @return long SD卡剩余空间的大小（单位：byte）
     */
    public static long getSDSize()
    {
        final String str = Environment.getExternalStorageDirectory().getPath();
        final StatFs localStatFs = new StatFs(str);
        final long blockSize = localStatFs.getBlockSize();
        return localStatFs.getAvailableBlocks() * blockSize;
    }
    
	public static final void saveObject(String path, Object saveObject) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		File f = new File(path);
		try {
			fos = new FileOutputStream(f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(saveObject);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static final Object restoreObject(String path) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Object object = null;
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		try {
			fis = new FileInputStream(f);
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
			return object;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return object;
	}
	
	public static final Date getServerTime() {
		return HttpRequest.getServerTime();
	}
}
