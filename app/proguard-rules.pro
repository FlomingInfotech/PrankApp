
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-dontwarn org.xmlpull.v1.**
-dontwarn org.apache.http.**
-ignorewarnings
-keep class org.kobjects.** { *; }
-keep class org.ksoap2.** { *; }
-keep class org.kxml2.** { *; }
-keep class org.xmlpull.** { *; }
-keep class com.google.gson{ *; }
-keep public class com.example.atman.m
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keepattributes *Annotation*



-keep class com.prank.sexygirlcallprank.activity.** { <fields>; }
-keep class com.prank.sexygirlcallprank.activity.adapter.** { <fields>; }
-keep class com.prank.sexygirlcallprank.** { <fields>; }
-keep class com.prank.sexygirlcallprank.fragment.** { <fields>; }
-keep class com.prank.sexygirlcallprank.model.** { <fields>; }
-keep class  com.prank.sexygirlcallprank.helper.** { <fields>; }

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep class org.json.**{ *; }
-dontpreverify
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe
-dontwarn org.xmlpull.v1.**
-dontwarn android.support.v4.**
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keep class com.google.**{
    *;
}
#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class com.myappspackage.catalog.** { *; }
-keep class retrofit2.converter.gson.** { *; }
-ignorewarnings
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
# support design library
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
#Picasso
-dontwarn com.squareup.okhttp.**
#Firebase
-keep class com.google.firebase.quickstart.database.java.viewholder.** {
    *;
}
-keepclassmembers class com.google.firebase.quickstart.database.java.models.** {
    *;
}
-dontwarn com.google.android.gms.measurement.AppMeasurement*
-keepclassmembers class * extends com.google.android.gms.internal.measurement.zzyv {
  <fields>;
}
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**

# Only necessary if you downloaded the SDK jar directly instead of from maven.
-keep class com.shaded.fasterxml.jackson.** { *; }

#analytics
-keep class com.google.analytics.** { *; }
#design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
#search
-keep class android.support.v7.widget.SearchView { *; }
#craslytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#gson

-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
#httpclient
-keep class okhttp3.** { *; }
#me.relex.circleindicator
-keep class me.relex.circleindicator.** { *; }
#OneSignal
-keep class com.onesignal.** { *; }
-keep class com.onesignal.ActivityLifecycleListenerCompat** {*;}


#admob
# For Google Play Services
-keep public class com.google.android.gms.ads.**{
   public *;
}
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# For old ads classes
-keep public class com.google.ads.**{
   public *;
}

-dontwarn com.google.ads.**
-dontwarn com.google.android.gms.ads.**
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}
-keepattributes *Annotation*
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}
# For mediation
-keepattributes *Annotation*

# Other required classes for Google Play Services
# Read more at http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}



-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}

#facebook ads
-keep class com.facebook.** {
   *;
}
-keepattributes Signature
-keep class com.facebook.android.*
-keep class android.webkit.WebViewClient
-keep class * extends android.webkit.WebViewClient
-keepclassmembers class * extends android.webkit.WebViewClient {
    <methods>;
}

-keep class com.facebook.** { *; }
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**

-keep class com.loopj.android.** { *; }
-keep interface com.loopj.android.** { *; }

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}



