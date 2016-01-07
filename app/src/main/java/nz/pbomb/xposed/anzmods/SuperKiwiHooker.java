package nz.pbomb.xposed.anzmods;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findMethodBestMatch;
import static de.robv.android.xposed.XposedHelpers.setObjectField;

import common.GLOBAL;
import common.PREFERENCES;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XposedBridge;

import common.PACKAGES;


public class SuperKiwiHooker implements IXposedHookLoadPackage {
    private XSharedPreferences sharedPreferences = null;

    public SuperKiwiHooker() {
        XposedBridge.log("[SuperKiwi] Module Loaded (Debug Mode: " + (GLOBAL.DEBUG ? "ON" : "OFF") + ")");
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam loadPackageParam) throws Throwable {
        if(sharedPreferences == null) {
            sharedPreferences = new XSharedPreferences(PACKAGES.MODULE);
            boolean isWorldReadable = sharedPreferences.makeWorldReadable();
            if (GLOBAL.DEBUG) {
                XposedBridge.log("[SuperKiwi] Loading Shared Preferences...");
                XposedBridge.log("[SuperKiwi] \tWorld Readable: " + isWorldReadable);
                XposedBridge.log("[SuperKiwi] \tPath: " + sharedPreferences.getFile().getAbsolutePath());
                XposedBridge.log("[SuperKiwi] \tFile Readable: " + sharedPreferences.getFile().canRead());
                XposedBridge.log("[SuperKiwi] \tExists: " + sharedPreferences.getFile().exists());
                if (sharedPreferences.getAll().size() == 0) {
                    XposedBridge.log("[SuperKiwi] Shared Preferences seems not to be initialized or does not have read permissions. Common on Android 5.0+ with SELinux Enabled and Enforcing.");
                }
                XposedBridge.log("[SuperKiwi]");
                XposedBridge.log("[SuperKiwi] Loaded Shared Preferences (Generated from Defaults or Prefs XML file)...");
                XposedBridge.log("[SuperKiwi]\t ANZ Root Detection Enabled: " + sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.ANZ.ROOT_DETECTION));
                XposedBridge.log("[SuperKiwi]\t ANZ Spoof Enabled: " + sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE));
                XposedBridge.log("[SuperKiwi]\t Semble Root Detection Enabled: " + sharedPreferences.getBoolean(PREFERENCES.KEYS.SEMBLE.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.SEMBLE.ROOT_DETECTION));
            }
        }


        if(loadPackageParam.packageName.equals(PACKAGES.ANZ_GOMONEY)) {

            if (GLOBAL.DEBUG) {
                XposedBridge.log("[SuperKiwi] Hooking Methods for ANZ GoMoney New Zealand Application.");
            }
            hookAnzGoMoneyApplication(loadPackageParam);
        }

        if(loadPackageParam.packageName.equals(PACKAGES.SEMBLE_2DEGREES) ||
           loadPackageParam.packageName.equals(PACKAGES.SEMBLE_SPARK) ||
           loadPackageParam.packageName.equals(PACKAGES.SEMBLE_VODAFONE)) {


            if (GLOBAL.DEBUG) {
                XposedBridge.log("[SuperKiwi] Hooking Methods for Semble Application.");
            }
            hookSembleApplication(loadPackageParam);
        }
    }

    public void hookAnzGoMoneyApplication(final LoadPackageParam loadPackageParam) {
        /**
         * Seitc API Root Check Hooks
         */
        findAndHookMethod("xxxxxx.jejeee", loadPackageParam.classLoader, "isRooted", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.ANZ.ROOT_DETECTION)) {
                    param.setResult(false);
                    if(GLOBAL.DEBUG) {
                        XposedBridge.log("[SuperKiwi][ANZ] xxxxxx.jejeee.isRooted() Hooked");
                    }
                }
            }
        });

        findAndHookMethod("xxxxxx.jejeee", loadPackageParam.classLoader, "isRootedQuickCheck", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.ANZ.ROOT_DETECTION)) {
                    param.setResult(false);
                    if(GLOBAL.DEBUG) {
                        XposedBridge.log("[SuperKiwi][ANZ] xxxxxx.jejeee.isRootedQuickCheck() Hooked");
                    }
                }
            }
        });

        findAndHookMethod("xxxxxx.jejeee", loadPackageParam.classLoader, "isDebug", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.ANZ.ROOT_DETECTION)) {
                    param.setResult(false);
                    if(GLOBAL.DEBUG) {
                        XposedBridge.log("[SuperKiwi][ANZ] xxxxxx.jejeee.isDebug() Hooked");
                    }
                }
            }
        });


        /**
         * ANZ GoMoney Root Check
         */
        // SharedPrefs - "wallet_invalid_rooted_device" Always returns false
        findAndHookMethod("nz.co.anz.android.mobilebanking.h.a.n", loadPackageParam.classLoader, "l", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.ANZ.ROOT_DETECTION)) {
                    param.setResult(false);
                }
            }
        });

        // SharedPrefs - "wallet_card_removed" Always returns false
        findAndHookMethod("nz.co.anz.android.mobilebanking.h.a.n", loadPackageParam.classLoader, "m", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.ANZ.ROOT_DETECTION)) {
                    param.setResult(false);
                }
            }
        });

        // Superuser.apk and shell check
        findAndHookMethod("nz.co.anz.android.mobilebanking.i.e.ah", loadPackageParam.classLoader, "a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.ANZ.ROOT_DETECTION)) {
                    param.setResult(false);
                }
            }
        });

        // eligibleForWallet returns true
        findAndHookMethod("nz.co.anz.android.mobilebanking.model.k", loadPackageParam.classLoader, "g", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.ANZ.ROOT_DETECTION)) {
                    param.setResult(true);
                }
            }
        });

        /**
         * Device Spoofing Hooks
         */
        findAndHookMethod("nz.co.anz.android.mobilebanking.i.e.k", loadPackageParam.classLoader, "a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    param.setResult("[samsung SM-N9005]");
                    if(GLOBAL.DEBUG) {
                        XposedBridge.log("[SuperKiwi][ANZ] nz.co.anz.android.mobilebanking.i.e.k.a() Hooked");
                    }
                }
            }
        });
        findAndHookMethod("nz.co.anz.android.mobilebanking.i.e.k", loadPackageParam.classLoader, "b", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    param.setResult("samsung SM-N9005");
                    if(GLOBAL.DEBUG) {
                        XposedBridge.log("[SuperKiwi][ANZ] nz.co.anz.android.mobilebanking.i.e.k.b() Hooked");
                    }
                }
            }
        });

        findAndHookMethod("nz.co.anz.android.mobilebanking.i.e.u", loadPackageParam.classLoader, "a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    param.setResult("SM-N9005");
                }
            }
        });

        findAndHookMethod("com.google.android.gms.b.ao", loadPackageParam.classLoader, "a", String.class, String.class, String.class, String.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    String str = (String) param.args[0];
                    String str2 = (String) param.args[1];
                    String str3 = (String) param.args[2];
                    String str4 = (String) param.args[3];
                    String str5 = "SM-9005";
                    String str6 = (String) param.args[5];

                    param.setResult(String.format("%s/%s (Linux; U; Android %s; %s; %s Build/%s)", str, str2, str3, str4, str5, str6));
                }
            }
        });

        Class<?> x = findClass("xxxxxx.ajaaaj", loadPackageParam.classLoader);

        Method y = findMethodBestMatch(x,"b041604160416Ж0416ЖЖ0416", String.class);
        XposedBridge.log(y.getName());
        //xxxxxx.ajaaaj
        //Build.Device
        findAndHookMethod("xxxxxx.ajaaaj", loadPackageParam.classLoader, "b041604160416Ж0416ЖЖ0416", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    setObjectField(param.thisObject, "f6987b044504450445", "hlte");
                }
            }
        });
        //Build.Model
        findAndHookMethod("xxxxxx.ajaaaj", loadPackageParam.classLoader, "b0416ЖЖ04160416ЖЖ0416", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    setObjectField(param.thisObject, "bх0445ххх04450445х", "SM-N9005");
                }
            }
        });
        //Build.Product
        findAndHookMethod("xxxxxx.ajaaaj", loadPackageParam.classLoader, "b04160416Ж04160416ЖЖ0416", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    setObjectField(param.thisObject, "b04450445ххх04450445х", "hltexx");
                }
            }
        });

        /*//Build.BOARD
        findAndHookMethod("xxxxxx.ajaaaj", loadPackageParam.classLoader, "b04160416041604160416ЖЖ0416", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.SPOOF_DEVICE)) {
                    setObjectField(param.thisObject, "b0445х0445хх04450445х", "MSM8974");
                }
            }
        });
        //Build.CPU_ABI ~ WRONG
        findAndHookMethod("xxxxxx.ajaaaj", loadPackageParam.classLoader, "b0416ЖЖЖЖ0416Ж0416", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.SPOOF_DEVICE)) {
                    setObjectField(param.thisObject, "bх04450445хх04450445х", "armeabi-v7a");
                }
            }
        });
        //Build.CPU_ABI2 ~ WRONG
        findAndHookMethod("xxxxxx.ajaaaj", loadPackageParam.classLoader, "m12118b041604160416", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.SPOOF_DEVICE)) {
                    setObjectField(param.thisObject, "bх04450445хх04450445х", "armeabi");
                }
            }
        });*/

        //Build.MANUFACTURER
        findAndHookMethod("xxxxxx.ajaaaj", loadPackageParam.classLoader, "b04160416ЖЖЖ0416Ж0416", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    setObjectField(param.thisObject, "b044504450445хх04450445х", "samsung");
                }
            }
        });

        //Build.BRAND
        findAndHookMethod("xxxxxx.ajaaaj", loadPackageParam.classLoader, "b041604160416ЖЖ0416Ж0416", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    setObjectField(param.thisObject, "b0445хх0445х04450445х", "samsung");
                }
            }
        });

        //Build.ID
        findAndHookMethod("xxxxxx.hchchh", loadPackageParam.classLoader, "b04220422ТТ0422042204220422", android.content.Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    StringBuilder sb = new StringBuilder(500);
                    sb.append("hlte");
                    sb.append("SM-N9005");
                    sb.append("hltexx");
                    sb.append("MSM8974");
                    sb.append("samsung");
                    sb.append("samsung");
                    sb.append("Unknown"); //Hardware
                    sb.append("Unknown"); //serial

                    final TelephonyManager mTelephony = (TelephonyManager) ((Context) param.args[0]).getSystemService(Context.TELEPHONY_SERVICE);
                    String myAndroidDeviceId = mTelephony.getDeviceId();

                    sb.append(myAndroidDeviceId);

                    param.setResult(sb.toString().getBytes());
                }
            }
        });

        //Build.FINGERPRINT
        findAndHookMethod("xxxxxx.ajaaaj", loadPackageParam.classLoader, "bЖ04160416ЖЖЖ04160416", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.e("SuperKiwi", "afterHookedMethod: " + String.valueOf(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)));
                if(sharedPreferences.getBoolean(PREFERENCES.KEYS.ANZ.SPOOF_DEVICE, PREFERENCES.DEFAULT_VALUES.ANZ.SPOOF_DEVICE)) {
                    setObjectField(param.thisObject, "b0445ххх044504450445х", "samsung/hltexx/hlte:4.4.2/KOT49H/N9005XXUGNG1:user/release-keys");
                }
            }
        });
    }

    private void hookSembleApplication(LoadPackageParam loadPackageParam) {
        if(!sharedPreferences.getBoolean(PREFERENCES.KEYS.SEMBLE.ROOT_DETECTION, PREFERENCES.DEFAULT_VALUES.SEMBLE.ROOT_DETECTION)) {
            return;
        }

        findAndHookMethod("com.csam.wallet.integrity.IntegrityCheckerImpl", loadPackageParam.classLoader, "checkDeviceIntegrity", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });

        findAndHookMethod("com.mastercard.mtp.mobileclientutilities.DeviceUtility", loadPackageParam.classLoader, "isOSPossiblyCompromised", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
    }
}