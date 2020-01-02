# NeetAnnotation
Annotation for Android
All most to neet implementation
Lifecycle:
https://developer.android.com/jetpack/androidx/releases/lifecycle
Dagger2:
https://github.com/google/dagger
Rxjava2:
https://github.com/ReactiveX/RxJava

example:
app.gradle

implementation 'com.neetoffice.annotation:Neetannotation-api:1.0.0'
kapt 'com.neetoffice.annotation:NeetAnnotation-compiler:1.0.0'
implementation 'androidx.lifecycle:lifecycle-extensions:2.0.0'
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.0.0'
implementation 'androidx.lifecycle:lifecycle-livedata:2.0.0'
implementation 'androidx.lifecycle:lifecycle-runtime:2.0.0'
implementation 'androidx.lifecycle:lifecycle-common-java8:2.0.0'
implementation 'androidx.lifecycle:lifecycle-reactivestreams:2.0.0'
kapt "androidx.lifecycle:lifecycle-compiler:2.0.0"
implementation 'com.google.dagger:dagger:2.25'
implementation 'com.google.dagger:dagger-android-support:2.25'
kapt "com.google.dagger:dagger-compiler:2.25"
kapt "com.google.dagger:dagger-android-processor:2.25"
implementation 'io.reactivex.rxjava2:rxjava:2.2.16'
implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
implementation 'io.reactivex.rxjava2:rxkotlin:2.4.0'
