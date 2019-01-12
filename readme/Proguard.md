# Proguard从入门到精通

## Proguard简介
> ProGuard is the most popular optimizer for Java bytecode. It makes your Java and Android applications
  up to 90% smaller and up to 20% faster. ProGuard also provides minimal protection against reverse
  engineering by obfuscating the names of classes, fields and methods.

  简单来讲，ProGuard是最流行的Java字节码优化器。它能使您的Java和Android应用程序小90%，快20%。ProGuard
  还通过混淆类、字段和方法的名称，提供了对反向工程的保护。ProGuard会移除没有用到的代码，然后对代码里面
  的类、变量、方法名重命名为可读性比较差的名字。

## Proguard作用
* Shrink：压缩，检测并移除代码中无用的类、字段、方法和属性，并且在优化后会再执行一次，
          因为在执行优化动作以后，会再次暴露出一些未被使用的类和成员。
* Optimize：优化，对字节码进行优化，移除无用的指令，让应用运行的更快。
* Obfuscate：混淆，对代码中的类名和成员名（包括：成员变量和成员方法）进行重命名，使得反编译后代码可
             读性很差。
* Preveirfy：预检，在Java平台上对处理后的代码进行预检，确保加载的class能够正常执行

## 基本混淆指令
-dontshrink 关闭压缩
-dontoptimize  关闭优化
-optimizationpasses n 表示proguard对代码进行迭代优化的次数，Android一般为5
-dontobfuscate 关闭混淆

### 基本指令
```
# 代码混淆压缩比，在0和7之间，默认为5，一般不需要改
-optimizationpasses 5

# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses

# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，preverify是proguard的4个步骤之一
# Android不需要preverify，去掉这一步可加快混淆速度
-dontpreverify

# 有了verbose这句话，混淆后就会生成映射文件
# 包含有类名->混淆后类名的映射关系
# 然后使用printmapping指定映射文件的名称
-verbose
-printmapping proguardMapping.txt

# 指定混淆时采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不改变
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 保护代码中的Annotation不被混淆，这在JSON实体映射时非常重要，比如fastJson
-keepattributes *Annotation*

# 避免混淆泛型，这在JSON实体映射时非常重要，比如fastJson
-keepattributes Signature

//抛出异常时保留代码行号，在异常分析中可以方便定位
-keepattributes SourceFile,LineNumberTable

-dontskipnonpubliclibraryclasses用于告诉ProGuard，不要跳过对非公开类的处理。默认情况下是跳过的，因为程序中不会引用它们，有些情况下人们编写的代码与类库中的类在同一个包下，并且对包中内容加以引用，此时需要加入此条声明。

-dontusemixedcaseclassnames，这个是给Microsoft Windows用户的，因为ProGuard假定使用的操作系统是能区分两个只是大小写不同的文件名，但是Microsoft Windows不是这样的操作系统，所以必须为ProGuard指定-dontusemixedcaseclassnames选项
```

### 需要保留的东西
```
# 保留所有的本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留了继承自Activity、Application这些类的子类
# 因为这些子类，都有可能被外部调用
# 比如说，第一行就保证了所有Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# 如果有引用android-support-v4.jar包，可以添加下面这行
-keep public class com.xxxx.app.ui.fragment.** {*;}

# 保留在Activity中的方法参数是view的方法，
# 从而我们在layout里面编写onClick就不会被影响
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 枚举类不能被混淆
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

# 保留自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化的类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于R（资源）下的所有类及其方法，都不能被混淆
-keep class **.R$* {
    *;
}

# 对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
}
```

### 针对APP的量身定制
```
1,保留实体类和成员被混淆
对于实体，保留它们的set和get方法，对于boolean型get方法，有人喜欢命名isXXX的方式，所以不要遗漏。如下：
# 保留实体类和成员不被混淆
-keep public class com.xxxx.entity.** {
    public void set*(***);
    public *** get*();
    public *** is*();
}
 一种好的做法是把所有实体都放在一个包下进行管理，这样只写一次混淆就够了，避免以后在别的包中新增的实体而忘记保留，代码在混淆后因为找不到相应的实体类而崩溃。

 2，内嵌类

 内嵌类经常会被混淆，结果在调用的时候为空就崩溃了，最好的解决方法就是把这个内嵌类拿出来，单独成为一个类。如果一定要内置，那么这个类就必须在混淆的时候保留，比如如下：

 # 保留内嵌类不被混淆
 -keep class com.example.xxx.MainActivity$* { *; }
 这个$符号就是用来分割内嵌类与其母体的标志。

 3，对WebView的处理

 # 对WebView的处理
 -keepclassmembers class * extends android.webkit.webViewClient {
     public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
     public boolean *(android.webkit.WebView, java.lang.String)
 }
 -keepclassmembers class * extends android.webkit.webViewClient {
     public void *(android.webkit.webView, java.lang.String)
 }

4，对JavaScript的处理

# 保留JS方法不被混淆
-keepclassmembers class com.example.xxx.MainActivity$JSInterface{
    <methods>;
}

5，处理反射

在程序中使用SomeClass.class.method这样的静态方法，在ProGuard中是在压缩过程中被保留的，那么对于
Class.forName("SomeClass")呢，SomeClass不会被压缩过程中移除，它会检查程序中使用的Class.forName方法，
对参数SomeClass法外开恩，不会被移除。但是在混淆过程中，无论是Class.forName("SomeClass")，还是
SomeClass.class，都不能蒙混过关，SomeClass这个类名称会被混淆，因此，我们要在ProGuard.cfg文件中保留
这个类名称。

Class.forName("SomeClass")
SomeClass.class
SomeClass.class.getField("someField")
SomeClass.class.getDeclaredField("someField")
SomeClass.class.getMethod("someMethod", new Class[] {})
SomeClass.class.getMethod("someMethod", new Class[] { A.class })
SomeClass.class.getMethod("someMethod", new Class[] { A.class, B.class })
SomeClass.class.getDeclaredMethod("someMethod", new Class[] {})
SomeClass.class.getDeclaredMethod("someMethod", new Class[] { A.class })
SomeClass.class.getDeclaredMethod("someMethod", new Class[] { A.class, B.class })
AtomicIntegerFieldUpdater.newUpdater(SomeClass.class, "someField")
AtomicLongFieldUpdater.newUpdater(SomeClass.class, "someField")
AtomicReferenceFieldUpdater.newUpdater(SomeClass.class, SomeType.class, "someField")
在混淆的时候，要在项目中搜索一下上述方法，将相应的类或者方法的名称进行保留而不被混淆。

6，对于自定义View的解决方案
但凡在Layout目录下的XML布局文件配置的自定义View，都不能进行混淆。为此要遍历Layout下的所有的XML布局文件，
找到那些自定义View，然后确认其是否在ProGuard文件中保留。有一种思路是，在我们使用自定义View时，前面都必
须加上我们的包名，比如com.a.b.customeview，我们可以遍历所有Layout下的XML布局文件，查找所有匹配com.a.b
的标签即可。

针对第三方jar包的解决方案

我们在Android项目中不可避免要使用很多第三方提供的SDK，一般而言，这些SDK是经过ProGuard混淆的，而我们所需要做的就是避免这些SDK的类和方法在我们APP被混淆。
1，针对android-support-v4.jar的解决方案

# 针对android-support-v4.jar的解决方案
-libraryjars libs/android-support-v4.jar
-dontwarn android.support.v4.**
-keep class android.support.v4.**  { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

 2，其他的第三方jar包的解决方案

这个就取决于第三方包的混淆策略了，一般都有在各自的SDK中有关于混淆的说明文字，比如支付宝如下：

# 对alipay的混淆处理
-libraryjars libs/alipaysdk.jar
-dontwarn com.alipay.android.app.**
-keep public class com.alipay.**  { *; }
值得注意的是，不是每个第三方SDK都需要-dontwarn 指令，这取决于混淆时第三方SDK是否出现警告，需要的时候再加上。
```

## 混淆注意事项
当然在使用ProGuard过程中，还有一些注意的事项，如下。
1，如何确保混淆不会对项目产生影响
测试工作要基于混淆包进行，才能尽早发现问题
每天开发团队的冒烟测试，也要基于混淆包
发版前，重点的功能和模块要额外的测试，包括推送，分享，打赏
2，打包时忽略警告
当导出包的时候，发现很多could not reference class之类的warning信息，如果确认App在运行中和那些引用没有什么关系，可以添加-dontwarn 标签，就不会提示这些警告信息了
 
3，对于自定义类库的混淆处理
比如我们引用了一个叫做AndroidLib的类库，我们需要对Lib也进行混淆，然后在主项目的混淆文件中保留AndroidLib中的类和类的成员。
 
4，使用annotation避免混淆
另一种类或者属性被混淆的方式是，使用annotation，比如这样：
@keep
@keepPublicGetterSetters
public class Bean{
    public  boolean booleanProperty;
    public  int intProperty;
    public  String stringProperty;
}

5，在项目中指定混淆文件
到最后，发现没有介绍如何在项目中指定混淆文件。在项目中有一个project.properties文件，在其中写这么一句话，就可以确保每次手动打包生成的apk是混淆过的。
proguard.config=proguard.cfg
其中，proguard.cfg是混淆文件的名称。

## 参考文章
**[ProGuard官网](https://www.guardsquare.com/zh-hans/%E4%BA%A7%E5%93%81%E4%BB%8B%E7%BB%8D/proguard)**
**[Android混淆从入门到精通](http://mobile.51cto.com/android-524451.htm)**
**[Android 混淆那些事儿](https://www.cnblogs.com/bugly/p/7085469.html)**
**[官网：压缩代码和资源](https://developer.android.com/studio/build/shrink-code)**


