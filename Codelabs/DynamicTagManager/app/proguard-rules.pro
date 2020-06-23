# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn com.huawei.hms.**
-keep interface com.huawei.hms.analytics.type.HAEventType{*;}
-keep interface com.huawei.hms.analytics.type.HAParamType{*;}
-keep class com.huawei.hms.analytics.HiAnalyticsTools{
    public static void enableLog();
    public static void enableLog(int);
}
-keep class com.huawei.hms.analytics.HiAnalyticsInstance{*;}
-keep class com.huawei.hms.analytics.HiAnalytics{*;}

-keep class com.huawei.hms.feature.** {
    public *;
}
-keep public class com.huawei.hms.common.** {
    public *;
}
-keep class com.huawei.hms.analytics.internal.filter.EventFilter{
    public void logFilteredEvent(java.lang.String, android.os.Bundle);
    public java.lang.String getUserProfile(java.lang.String);
}
-keep public class com.huawei.hms.dtm.EventFilter {
    public <fields>;
    public <methods>;
}