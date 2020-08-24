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
  def neetannotation_version = "1.1.7-rc"
  def lifecycle_version = "2.2.0"
  def arch_version = "2.1.0"
  def dagger_version = "2.25"
  
  implementation "com.neetoffice.annotation:Neetannotation-api:$neetannotation_version"
  kapt "com.neetoffice.annotation:NeetAnnotation-compiler:$neetannotation_version"
  // ViewModel
  implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
  // LiveData
  implementation "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
  // Lifecycles only (without ViewModel or LiveData)
  implementation "androidx.lifecycle:lifecycle-runtime:$lifecycle_version"
  // Saved state module for ViewModel
  implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
  // Annotation processor
  kapt "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
  // alternately - if using Java8, use the following instead of lifecycle-compiler
  implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
  // optional - helpers for implementing LifecycleOwner in a Service
  implementation "androidx.lifecycle:lifecycle-service:$lifecycle_version"
  // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
  implementation "androidx.lifecycle:lifecycle-process:$lifecycle_version"
  // optional - ReactiveStreams support for LiveData
  implementation "androidx.lifecycle:lifecycle-reactivestreams:$lifecycle_version"
  // optional - Extensions support for LiveData
  implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
  // optional - Test helpers for LiveData
  testImplementation "androidx.arch.core:core-testing:$arch_version"
  // Dagger2
  implementation "com.google.dagger:dagger:$dagger_version"
  implementation "com.google.dagger:dagger-android-support:$dagger_version"
  kapt "com.google.dagger:dagger-compiler:$dagger_version"
  kapt "com.google.dagger:dagger-android-processor:$dagger_version"
  // Rxjava
  implementation 'io.reactivex.rxjava2:rxjava:2.2.19'
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
