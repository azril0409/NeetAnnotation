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

-dontwarn library.neetoffice.com.**
-keep class library.neetoffice.com.** { *; }
-keep interface library.neetoffice.com.** { *; }
-keep @library.neetoffice.com.neetannotation.NotProguard class * {*;}
-keep @library.neetoffice.com.neetannotation.Handler interface * {*;}
-keep class *{
    @library.neetoffice.com.neetannotation.* <fields>;
}
-keepclassmembers  class *{
    @library.neetoffice.com.neetannotation.* <methods>;
    public  org.springframework.web.client.RestTemplate getRestTemplate();
    public  void getRestTemplate(org.springframework.web.client.RestTemplate);
    public  void setRootUrl(java.lang.String);
}