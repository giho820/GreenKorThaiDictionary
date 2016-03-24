package com.ph.greenkorthaidictionary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

public class MoveActUtil {

    /**
     * @param preAct
     * @param intent
     * @param isFinish
     */
    public static void moveActivity(Activity preAct, Intent intent, int enterAniRes,
                                    int exitAniRes, boolean isFinish, boolean isAnimation) {
        preAct.startActivity(intent);
        if (isAnimation)
            preAct.overridePendingTransition(enterAniRes, exitAniRes);
        if (isFinish)
            preAct.finish();

    }

    /**
     * @param preAct
     * @param afterAct
     * @param enterAniRes
     * @param exitAniRes
     * @param isFinish
     */
    public static void chageActivity(Activity preAct, Class<?> afterAct, int enterAniRes,
                                     int exitAniRes, boolean isFinish, boolean isAnimation) {
        Intent intent = null;
        intent = new Intent(preAct, afterAct);
        preAct.startActivity(intent);
        if (isAnimation)
            preAct.overridePendingTransition(enterAniRes, exitAniRes);
        if (isFinish)
            preAct.finish();

    }

    /**
     * 업데이트
     *
     * @param packageName
     * @param activity
     */
    public static void setupApp(String packageName, Activity activity) {

        Uri marketUri = Uri.parse("market://details?id=" + packageName);
        Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
        activity.startActivity(marketIntent);
    }

    public static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static void updateIconBadge(Context context, int notiCnt) {
        Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        badgeIntent.putExtra("badge_count", notiCnt);
        badgeIntent.putExtra("badge_count_package_name", context.getPackageName());
        badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(context));
        DebugUtil.showDebug(getLauncherClassName(context));
        context.sendBroadcast(badgeIntent);
    }

}
