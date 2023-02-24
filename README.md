# NeetAnnotation
Annotation for Android<br>
All most to neet implementation<br>
Lifecycle:<br>
https://developer.android.com/jetpack/androidx/releases/lifecycle<br>
Dagger2:<br>
https://github.com/google/dagger<br>
Rxjava2:<br>
https://github.com/ReactiveX/RxJava<br>
<br>
example:<br>
app.gradle<br>
```
apply plugin: 'kotlin-kapt'

dependencies{
  implementation 'io.github.azril0409:NeetAnnotation-api:1.3.9'
  kapt 'io.github.azril0409:NeetAnnotation-compiler:1.3.9'
  // ViewModel
  implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1'
  implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1'
  implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.4.1'
  implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.4.1'
  implementation 'androidx.lifecycle:lifecycle-viewmodel-savedstate:2.4.1'
  kapt 'androidx.lifecycle:lifecycle-compiler:2.4.1'
  implementation 'androidx.lifecycle:lifecycle-common-java8:2.4.1'
  implementation 'androidx.lifecycle:lifecycle-service:2.4.1'
  implementation 'androidx.lifecycle:lifecycle-process:2.4.1'
  implementation 'androidx.lifecycle:lifecycle-reactivestreams-ktx:2.4.1'
  testImplementation 'androidx.arch.core:core-testing:2.1.0'
  // Dagger2
  implementation 'com.google.dagger:dagger:2.28.3'
  implementation 'com.google.dagger:dagger-android-support:2.25'
  kapt 'com.google.dagger:dagger-compiler:2.25'
  kapt 'com.google.dagger:dagger-android-processor:2.25'
  // Rxjava
  implementation 'io.reactivex.rxjava2:rxjava:2.2.21'
  implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
  implementation 'io.reactivex.rxjava2:rxkotlin:2.4.0'
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("packageName", "_YOUR MODEL PACKAGE NAME_.extensions")
    }
}
```
