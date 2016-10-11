# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/kr4k3rz/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-dontwarn com.roughike.bottombar.**
-dontwarn org.hamcrest.**
-dontwarn android.test.**
-dontwarn android.support.test.**

-dontwarn com.tojc.ormlite.android.compiler.**
-dontwarn com.squareup.javawriter.**
-keep class org.hamcrest.** {
   *;
}

-keep class org.junit.** { *; }
-dontwarn org.junit.**

-keep class junit.** { *; }
-dontwarn junit.**

-keep class sun.misc.** { *; }
-dontwarn sun.misc.**
