package org.edgar.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Random;
import java.util.UUID;

public class AndroidPlatformUtil {
	public static final int MIN_DEVICE_ID_LEN = 8;
	public static final int kBuildVersion30 = 11;
	public static final int kBuildVersion41 = 16;

	public static boolean isBuildVersionCompatibleWith(int version) {
		return VERSION.SDK_INT >= version;
	}

	public static void openLocalMarketsContainSpecifiedPackageName(
			Context context, String packageName) {
		try {
			String str = "market://details?id=" + context.getPackageName();
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.setData(Uri.parse(str));
			context.startActivity(intent);
		} catch (Exception e) {
		}
	}

	public static void addShortcut(Context context, int resourceId,
			Class<?> cls, String name) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra("android.intent.extra.shortcut.NAME", name);
		shortcut.putExtra("duplicate", false);
		Intent intent = new Intent(context, cls);
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		shortcut.putExtra("android.intent.extra.shortcut.INTENT", intent);
		shortcut.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",
				ShortcutIconResource.fromContext(context, resourceId));
		context.sendBroadcast(shortcut);
	}
	

	public static String getDeviceID(Context context) {
		String deviceID = getPhoneDeviceID(context);
		String macAddress = getMacAddress(context);
		if (!TextUtils.isEmpty(macAddress)) {
			if (TextUtils.isEmpty(deviceID) || deviceID.equals("0")) {
				deviceID = macAddress;
			} else {
//				if (macAddress.length() > 8) {
//					macAddress = macAddress.substring(0, MIN_DEVICE_ID_LEN);
//				}
				deviceID = deviceID + "_" + macAddress;
			}
		}
		return (TextUtils.isEmpty(deviceID) || deviceID.length() < 8) ? generateUUID()
				: deviceID;
	}

	private static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid != null ? uuid.toString() : "unknow-"
				+ new Random().nextInt();
	}

	private static String getPhoneDeviceID(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService("phone");
			return tm == null ? null : tm.getDeviceId();
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMacAddress(Context context) {
		try {
			WifiManager wifi_mgr = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			if (wifi_mgr == null) {
				return null;
			}
			WifiInfo info = wifi_mgr.getConnectionInfo();
			String mac = info != null ? info.getMacAddress() : null;
			if (TextUtils.isEmpty(mac)) {
				return "";
			}
			return mac.replaceAll(":", "");
		} catch (Exception e) {
			return "";
		}
	}

	public static void vibrate(int milliseconds, Context context) {
		((Vibrator) context.getSystemService("vibrator"))
				.vibrate((long) milliseconds);
	}

	public static int getDeviceScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w = dm.widthPixels;
		int h = dm.heightPixels;
		return w > h ? h : w;
	}

	public static int getDeviceScreenHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w = dm.widthPixels;
		int h = dm.heightPixels;
		return w < h ? h : w;
	}

	public static String getDeviceRes(Context context){
		return getDeviceScreenWidth(context)+"*"+getDeviceScreenHeight(context);
	}

	public static float getDeviceDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static int getDeviceDensityDpi(Context context) {
		return context.getResources().getDisplayMetrics().densityDpi;
	}

	public static void hideSoftInput(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService("input_method");
		View currentFocus = activity.getCurrentFocus();
		if (imm != null && currentFocus != null) {
			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
		}
	}

	/**
	 * 优先调用（三星手机）
	 * @param context
	 * @param view
	 */
	public static void hideSoftInput(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService("input_method");
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static void showSoftInput(View view, Activity activity) {
		view.requestFocus();
		((InputMethodManager) activity.getSystemService("input_method"))
				.showSoftInput(view, 1);
	}

	public static void toggleSoftInput(Activity activity) {
		// ((InputMethodManager) activity.getSystemService("input_method"))
		// .toggleSoftInput(
		// 1,
		// MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER);
	}

	public static boolean isCurrentApplicationProcess(Application application) {
		// ActivityManager am = (ActivityManager) application
		// .getSystemService("activity");
		// if (am == null) {
		// return false;
		// }
		// List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		// int pid = Process.myPid();
		// String processName = application.getPackageName();
		// if (list != null) {
		// for (int i = 0; i < list.size(); i++) {
		// if (((RunningAppProcessInfo) list.get(i)).pid == pid) {
		// String name = ((RunningAppProcessInfo) list.get(i)).processName;
		// if (name != null && name.equalsIgnoreCase(processName)) {
		// return true;
		// }
		// }
		// }
		// }
		return false;
	}

	public static String getAppVersionName(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "0.0.0";
		}
	}

	public static int getAppVersionCode(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static String getSysModel() {
		return Build.MODEL;
	}
	
	/**
	 * 如：4.0.4
	 * 
	 * @param selfAct
	 * @return
	 */
	public static String getSysVersion() {
		return VERSION.RELEASE;
	}


	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 *
	 * @param pxValue
	 * scale（DisplayMetrics类中属性density）
	 * @return
	 */


	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 *
	 * @param dipValue
	 * scale（DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue
	 *  fontScale（DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * fontScale（DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}


}
