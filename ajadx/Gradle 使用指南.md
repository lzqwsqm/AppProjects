Gradle 使用指南 -- 基础配置

Gradle命令：

常用命令：

gradle明明一般是./gradlew +参数， gradlew
代表 gradle wrapper，意思是gradle的一层包装，
大家可以理解为在这个项目本地就封装了gradle，
即gradle wrapper， 在gradle/wrapper/gralde-wrapper.properties
文件中声明了它指向的目录和版本。只要下载成功即可用grdlew wrapper的命令代替全局的gradle命令。

./gradlew -v 版本号
./gradlew clean 清除app目录下的build文件夹
./gradlew build 检查依赖并编译打包
./gradlew tasks 列出所有task
这里注意的是 ./gradlew build 命令把debug、release环境的包都打出来，如果正式发布只需要打Release的包，该怎么办呢，下面介绍一个很有用的命令 assemble， 如：

./gradlew assembleDebug 编译并打Debug包
./gradlew assembleRelease 编译并打Release的包
除此之外，assemble还可以和productFlavors结合使用：

./gradlew installRelease Release模式打包并安装
./gradlew uninstallRelease 卸载Release模式包
加入自定义参数：

比如我们想根据不同的参数来进行不用的编译配置，可以在./gradlew中加入自定义参数。

./gradlew assembleDebug -Pcustom=true
就可以在build.gradle中使用下面代码来判断：

if (project.hasProperty('custom')){

}
assemble结合Build Variants来创建task

assemble 还能和 Product Flavor 结合创建新的任务，其实 assemble 是和 Build Variants 一起结合使用的，而 Build Variants = Build Type + Product Flavor，举个例子大家就明白了： 
如果我们想打包 wandoujia 渠道的release版本，执行如下命令就好了：

./gradlew assembleWandoujiaRelease
如果我们只打wandoujia渠道版本，则：

./gradlew assembleWandoujia
此命令会生成wandoujia渠道的Release和Debug版本 
同理我想打全部Release版本：

./gradlew assembleRelease
这条命令会把Product Flavor下的所有渠道的Release版本都打出来。 
总之，assemble 命令创建task有如下用法：

assemble<Variant Name>： 允许直接构建一个Variant版本，例如assembleFlavor1Debug。
assemble<Build Type Name>： 允许构建指定Build Type的所有APK，例如assembleDebug将会构建Flavor1Debug和Flavor2Debug两个Variant版本。
assemble<Product Flavor Name>： 允许构建指定flavor的所有APK，例如assembleFlavor1将会构建Flavor1Debug和Flavor1Release两个Variant版本。
Gradle配置：

Gradle构建脚本 build.gradle 
Gradle属性文件 gradle.properties 
Gradle设置文件 settings.gradle

build.gradle

先看整个项目的gradle配置文件：

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.3.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        jcenter()
    }
}

内容主要包含了两个方面：一个是声明仓库的源，这里可以看到是指明的jcenter(), 之前版本则是mavenCentral(), jcenter可以理解成是一个新的中央远程仓库，兼容maven中心仓库，而且性能更优。 
另一个是声明了android gradle plugin的版本，android studio 1.0正式版必须要求支持gradle plugin 1.0的版本

某个Moudle的gradle配置文件：

buildscript

buildscript {
    repositories {
        maven { url 'http://*********' }
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:1.3.1'
    }
}

buildscript{}设置脚本的运行环境。
repositories{}支持java依赖库管理，用于项目依赖。
dependencies{}依赖包的定义。支持maven/ivy，远程，本地库，也支持单文件。如果前面定义了repositories{}maven 库，则使用maven的依赖库，使用时只需要按照用类似于com.android.tools.build:gradle:0.4，gradle 就会自动的往远程库下载相应的依赖。
apply

//声明引用 com.android.application 插件，那么这个模块就是一个Android应用程序
apply plugin: 'com.android.application'

apply plugin:声明引用插件的类型。如果是库的话就加
apply plugin: 'com.android.library'

apply from：表示引用其他的配置文件，比如 apply from:"config.gradle"
android

这个是 Android 插件引入的 Script blocks，想深入了解的话看我后面的博客。

android {
    // 编译SDK的版本
    compileSdkVersion 22
    // build tools的版本
    buildToolsVersion "23.0.1"

    //aapt配置
    aaptOptions {
        //不用压缩的文件
        noCompress 'pak', 'dat', 'bin', 'notice'
        //打包时候要忽略的文件
        ignoreAssetsPattern "!.svn:!.git"
        //分包
        multiDexEnabled true
        //--extra-packages是为资源文件设置别名：意思是通过该应用包名+R，com.android.test1.R和com.android.test2.R都可以访问到资源
        additionalParameters '--extra-packages', 'com.android.test1','--extra-packages','com.android.test2'
    }

    //默认配置
    defaultConfig {
        //应用的包名
        applicationId "com.example.heqiang.androiddemo"
        minSdkVersion 21
        targetSdkVersion 22
        versionCode 1
        versionName "1.0"
    }

    //编译配置
    compileOptions {
        // java版本
        sourceCompatibility JavaVersion.VERSION_1_7
        targetCompatibility JavaVersion.VERSION_1_7
    }

    //源文件目录设置
    sourceSets {
        main {
             //jni lib的位置
             jniLibs.srcDirs = jniLibs.srcDirs << 'src/jniLibs'
             //定义多个资源文件夹,这种情况下，两个资源文件夹具有相同优先级，即如果一个资源在两个文件夹都声明了，合并会报错。
             res.srcDirs = ['src/main/res', 'src/main/res2']
             //指定多个源文件目录
             java.srcDirs = ['src/main/java', 'src/main/aidl']
        }
    }

    //签名配置
    signingConfigs {
        debug {
            keyAlias 'androiddebugkey'
            keyPassword 'android'
            storeFile file('keystore/debug.keystore')
            storePassword 'android'
        }
    }

    buildTypes {
        //release版本配置
        release {
            debuggable false
            // 是否进行混淆
            minifyEnabled true
            //去除没有用到的资源文件，要求minifyEnabled为true才生效
            shrinkResources true
            // 混淆文件的位置
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.txt'
            signingConfig signingConfigs.debug
            //ndk的一些相关配置，也可以放到defaultConfig里面。
            //指定要ndk需要兼容的架构(这样其他依赖包里mips,x86,arm-v8之类的so会被过滤掉)
            ndk {
                abiFilter "armeabi"
            }
        }
        //debug版本配置
        debug {
            debuggable true
            // 是否进行混淆
            minifyEnabled false
            //去除没有用到的资源文件，要求minifyEnabled为true才生效
            shrinkResources true
            // 混淆文件的位置
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.txt'
            signingConfig signingConfigs.debug
            //ndk的一些相关配置，也可以放到defaultConfig里面。
            //指定要ndk需要兼容的架构(这样其他依赖包里mips,x86,arm-v8之类的so会被过滤掉)
            ndk {
                abiFilter "armeabi"
            }
        }
    }
    // lint配置 
    lintOptions {
      //移除lint检查的error
      abortOnError false
      //禁止掉某些lint检查
      disable 'NewApi'
    }
}
android{}设置编译android项目的参数，构建android项目的所有配置都写在这里。 
除了上面写的，在android{}块中可以包含以下直接配置项：

productFlavors{ } 产品风格配置，ProductFlavor类型
testOptions{ } 测试配置，TestOptions类型
dexOptions{ } dex配置，DexOptions类型
packagingOptions{ } PackagingOptions类型
jacoco{ } JacocoExtension类型。 用于设定 jacoco版本
splits{ } Splits类型。
几点说明：

文件开头apply plugin是最新gradle版本的写法，以前的写法是apply plugin: 'android', 如果还是以前的写法，请改正过来。
minifyEnabled也是最新的语法，很早之前是runProguard,这个也需要更新下。
proguardFiles这部分有两段，前一部分代表系统默认的android程序的混淆文件，该文件已经包含了基本的混淆声明，免去了我们很多事，这个文件的目录在 sdk目录/tools/proguard/proguard-android.txt , 后一部分是我们项目里的自定义的混淆文件，目录就在 app/proguard-rules.txt , 如果你用Studio 1.0创建的新项目默认生成的文件名是 proguard-rules.pro , 这个名字没关系，在这个文件里你可以声明一些第三方依赖的一些混淆规则。最终混淆的结果是这两部分文件共同作用的。
aaptOptions更多介绍 http://blog.csdn.net/heqiangflytosky/article/details/51009123
变量的引用

比如在gradle的某个地方想使用版本号，可以使用下面的方法：

def getVersionCode() {
    int code = android.defaultConfig.versionCode
    return code
}
repositories

repositories {
    flatDir {
        //本地jar依赖包路径
        dirs '../../../../main/libs'
    }
}

dependencies

dependencies {
        compile files('libs/android-support-v4.jar')
        //在flatDir.dirs下面找依赖的aar
        compile (name:'ui', ext:'aar')
        // 编译extras目录下的ShimmerAndroid模块
        // 使用transitive属性设置为false来排除所有的传递依赖，默认为true
        compile project(':extras:ShimmerAndroid'){
            transitive = false
        }
        // 编译CommonSDK模块，但是去掉此模块中对com.android.support的依赖，防止重复依赖报错
        compile (project(':CommonSDK')) { exclude group: "com.android.support" }
        provided fileTree(dir: 'src/android5/libs', include: ['*.jar'])
        provided 'com.android.support:support-v4:21.0.3'
        provided project(':main-host')
        //通用使用exclude排除support-compat模块的依赖
        compile ('com.jakewharton:butterknife:8.5.1'){
            exclude module: 'support-compat'
        }

        // gradle 3.0以后版本支持的写法
        implementation 'com.android.support.constraint:constraint-layout:1.0.2'
        api 'com.android.support:design:26.1.0'
}

compile和provided 
compile表示编译时提供并打包进apk。
implementation 和 api 
implementation 会将依赖隐藏在内部而不对外公开，就是说使用 implementation 的依赖不会传递。比如：一个项目中app模块依赖A模块，A模块使用 implementation 来依赖 fastjson ，那么app里面如果不添加依赖的话就不能直接引用fastjson。 
api 和以前的 compile 是一样的。
exclude 防止重复依赖，后面会重点介绍
transitive 排除所有的传递依赖，后面会重点介绍
include
CommonSDK模块的定义可以参考settings.gradle 
其他的介绍可以参考 依赖库管理。

几点说明

看到上面的两个一模一样的repositories和dependencies了吗？他们的作用是不一样的，在buildscript里面的那个是插件初始化环境用的，用于设定插件的下载仓库，而外面的这个是设定工程依赖的一些模块和远程library的下载仓库的。
settings.gradle

这个文件是全局的项目配置文件，里面主要声明一些需要加入gradle的module。 
一般在setting.gradle中主要是调用include方法，导入工程下的各个子模块。 
那我们在setting.gradle里面还能写什么呢？因为setting.gradle对应的是gradle中的Settings对象，那查下Settings的文档（https://docs.gradle.org/current/dsl/org.gradle.api.initialization.Settings.html），看下它都有哪些方法，哪些属性，就知道在setting.gradle能写什么了；

include ':AndroidDemo'

include ':CommonSDK'
project(':CommonSDK').projectDir = new File(settingsDir, '../../CommonSDK/')

include调用后，生成了一个名为:CommonSDK的Project对象，project(':CommonSDK')取出这个对象，设置Project的 projectDir属性。projectDir哪里来的？请看Project类的文档。

gradle.properties

可以在 gradle.properties 文件中配置一些变量，这些变量在这个工程下的所有module的build.gradle文件里都可以使用。这样就可以把一些共用的变量放到这里，这样后面修改的时候就可以只修改这个变量，不用各个模块都要修改了。 
比如我们在 gradle.properties SDK 版本以及应用的版本号：

MIN_SDK_VERSION=21
TARGET_SDK_VERSION=22
VERSION_CODE=200100
VERSION_NAME=2.1.0
RX_ANDROID_VERSION=1.2.0

那么在 build.gradle 中可以通过project进行获取或者 "${RX_ANDROID_VERSION}" 引用：

    defaultConfig {
        applicationId "com.example.heqiang.testsomething"
        minSdkVersion project.MIN_SDK_VERSION as int
        targetSdkVersion project.TARGET_SDK_VERSION as int
        versionCode project.VERSION_CODE as int
        versionName project.VERSION_NAME
        /*
        * as int 关键字是用来进行类型转换的，因为从配置里面读取出来的默认是字符串
        *还可以使用 versionCode Integer.parseInt(project.VERSION_CODE)
        * */
    }
// 还可以这样用
dependencies {
    compile "io.reactivex:rxandroid:${RX_ANDROID_VERSION}"
    compile "io.reactivex:rxjava:${RX_JAVA_VERSION}"
}    


或者在 gradle.properties 中添加：

systemProp.versionName=1.0.0
systemProp.versionCode=100

可以通过 System.properties进行引用：

def code = System.properties['versionCode']
def name = System.properties['versionName']

调试

我们在进行一些配置的时候可能需要知道一些变量的值，这时候可以在 build.gradle 中添加打印进行调试，比如：

    defaultConfig {
        applicationId "com.example.hq.testsomething"
        minSdkVersion project.MIN_SDK_VERSION as int
        targetSdkVersion project.TARGET_SDK_VERSION as int
        versionCode project.VERSION_CODE as int
        versionName project.VERSION_NAME

        println('** build versionName=' + versionName)
    }

在 Gradle Console 中就可以看到打印：

** build versionName=2.1.0
1
依赖库管理

本地依赖

dependencies {
    //单文件依赖
        compile files('libs/android-support-v4.jar')
    //某个文件夹下面全部依赖
        compile fileTree(dir: 'src/android6/libs', include: ['*.jar'])
        compile (name:'ui', ext:'aar')
    compile (project(':CommonSDK')) { exclude group: "com.android.support" }
        provided fileTree(dir: 'src/android5/libs', include: ['*.jar'])
        provided 'com.android.support:support-v4:21.0.3'
        provided project(':main-host')
}

远程依赖

gradle同时支持maven，ivy，以maven作为例子：

repositories { 
 //从中央库里面获取依赖
 mavenCentral() 
 //或者使用指定的本地maven 库
 maven{ 
  url "file://F:/githubrepo/releases" 
 }
 //或者使用指定的远程maven库
 maven{ 
  url "https://github.com/youxiachai/youxiachai-mvn-repo/raw/master/releases" 
 } 
} 

dependencies { 
 //应用格式: packageName:artifactId:version 
 compile 'com.google.android:support-v4:r13' 
}

项目依赖

对于项目依赖android library的话，在这里需要使用gradle mulit project机制。 
Mulit project设置是gradle约定的一种格式，如果需要编译某个项目之前，要先编译另外一个项目的时候，就需要用到。结构如下（来自于官方文档）：

MyProject/ 
| settings.gradle 
 + app/ 
| build.gradle 
 + libraries/ 
  + lib1/ 
   | build.gradle 
  + lib2/ 
   | build.gradle

需要在workplace目录下面创建settings.gradle 的文件，然后在里面写上：

include ':app', ':libraries:lib1', ':libraries:lib2' 
1
例如：

include ':AndroidDemo'

include ':CommonSDK'
project(':CommonSDK').projectDir = new File(settingsDir, '../../CommonSDK/')

如此，gradle mutil project 就设置完毕。 
对于app project如果需要应用libraries目录下的lib1，只需要在app project的build.gradle文件里的依赖中这么写：

compile project(':libraries:lib1')
1
类似前面的

provided project(':main-host')
1
即可完成，写完以后可以用gradle dependencies可以检查依赖状况

Gradle依赖的统一管理

我们可以在项目的根目录创建一个gradle配置文件config.gradle，内容如下：

ext{
    android=[
            compileSdkVersion: 22,
            buildToolsVersion: "23.0.1",
            minSdkVersion: 21,
            targetSdkVersion: 22,
            versionCode: 1,
            versionName: "1.0"
    ]
    dependencies=[
            compile:'com.android.support:support-v4:21.0.3',
            compile: (project(':CommonSDK')) { exclude group: "com.android.support" },
            provided: fileTree(dir: 'src/android5/libs', include: ['*.jar']),
            provided: project(':main-host')
    ]
}

targetSdkVersion的版本还有依赖库的版本升级都在这里进行统一管理，所有的module以及主项目都从这里同意读取就可以了。 
在build.gradle文件中加入：

apply from:"config.gradle"

意思是所有的子项目或者所有的modules都可以从这个配置文件中读取内容。 
android节点读取ext中android对应项，dependencies读取dependencies对应项，如果配置有变化就可以只在config.gradle中修改，是不是很方便进行配置的管理呢？

检查依赖报告

运行命令./gradlew <projectname>:dependencies --configuration compile （projectname为settings.gradle里面配置的各个project，如果没有配置，直接运行./gradlew dependencies --configuration compile），会把依赖树会打印出来，依赖树显示了你 build 脚本声明的顶级依赖和它们的传递依赖： 
gradle-dependencies
仔细观察你会发现有些传递依赖标注了*号，表示这个依赖被忽略了，这是因为其他顶级依赖中也依赖了这个传递的依赖，Gradle会自动分析下载最合适的依赖。

排除传递依赖

Gradle允许你完全控制传递依赖，你可以选择排除全部的传递依赖也可以排除指定的依赖。

exclude：前面已经介绍过，可以设置不编译指定的模块，排除指定模块的依赖。后的参数有group和module，可以分别单独使用，会排除所有匹配项。
        // 编译CommonSDK模块，但是去掉此模块中对com.android.support的依赖，防止重复依赖报错
        compile (project(':CommonSDK')) { exclude group: "com.android.support" }
        compile ('com.jakewharton:butterknife:8.5.1'){
            exclude module: 'support-compat'
            exclude group: 'com.android.**.***', module: '***-***'
        }

transitive：前面已经介绍过，用于自动处理子依赖项，默认为true，gradle自动添加子依赖项。设置为false排除所有的传递依赖，可以用来解决一些依赖冲突的问题，比如一些 Error:java.io.IOException: Duplicate zip entry 报错。
        // 使用transitive属性设置为false来排除所有的传递依赖
        compile project(':extras:ShimmerAndroid'){
            transitive = false
        }

force：强制设置某个模块的版本。
    configurations.all{
        resolutionStrategy{
            force'org.hamcrest:hamcrest-core:1.3'
        }
    }

这样，应用中对org.hamcrest:hamcrest-core 依赖就会变成1.3版本。

动态版本声明

如果你想使用一个依赖的最新版本，你可以使用latest.integration，比如声明 Cargo Ant tasks的最新版本，你可以这样写org.codehaus .cargo:cargo-ant:latest-integration，你也可以用一个+号来动态的声明：

dependencies {
    //依赖最新的1.x版本
    compile "org.codehaus.cargo:cargo-ant:1.+"
}

然后在依赖树里面可以清晰的看到选择了哪个版本：

\--- org.codehaus.cargo:cargo-ant:1.+ -> 1.3.1
1
http://www.open-open.com/lib/view/open1431391503529.html
http://www.jianshu.com/p/429733dbbc34

多渠道打包:

主要借助

android {
    productFlavors{
    ……
    }
}

来实现。 
网上多是类似友盟的配置，copy过来： 
http://blog.csdn.net/maosidiaoxian/article/details/42000913 
https://segmentfault.com/a/1190000004050697 
在AndroidManifest.xml里面写上：

<meta-data
    android:name="UMENG_CHANNEL"
    android:value="Channel_ID" />

里面的Channel_ID就是渠道标示。我们的目标就是在编译的时候这个值能够自动变化。

android {  
    productFlavors {
        xiaomi {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "xiaomi"]
        }
        _360 {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "_360"]
        }
        baidu {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "baidu"]
        }
        wandoujia {
            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "wandoujia"]
        }
    }  
}

或者批量修改

android {  
    productFlavors {
        xiaomi {}
        _360 {}
        baidu {}
        wandoujia {}
    }  

    productFlavors.all { 
        flavor -> flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name] 
    }
}

然后用 ./gradlew assembleRelease 这条命令会把Product Flavor下的所有渠道的Release版本都打出来。 
assemble<Product Flavor Name>： 允许构建指定flavor的所有APK，例如assembleFlavor1将会构建Flavor1Debug和Flavor1Release两个Variant版本。 
在上面当中，我们也可以指定一个默认的渠道名，如果需要的话。指定默认的值是在defaultConfig节点当中添加如下内容：

manifestPlaceholders = [ CHANNEL_NAME:"Unspecified"]  

这里的Unspecified换成你实际上的默认的渠道名。 
使用manifestPlaceholders的这种配置，同样适用于manifest的其他配置。比如你需要在不同渠道发布的apk里面，指定不同的启动Activity。比如在豌豆荚里面发布的，启动的Activity显示的是豌豆荚首发的界面，应用宝里面启动的是应用宝首发的界面（哈哈，有点坏），你就可以对你的activity的值使用 {activity_name}的方式，然后在productFlavors里面配置这个{activity_name}的值。

另外这里记录一个 productFlavors 和 applicationId 关系的小知识。 
参考文档 
每个 Android 应用均有一个唯一的应用 ID，我们可以在通过 productFlavors 构建的应用变体中配置不同的应用 ID。

android {
    defaultConfig {
        applicationId "com.example.myapp"
    }
    productFlavors {
        free {
            applicationIdSuffix ".free"
        }
        pro {
            applicationIdSuffix ".pro"
        }
    }
}

这样，“免费”的 applicationId 就变为“com.example.myapp.free”。

Gradle 使用指南 -- Android DSL 扩展
 
介绍在Android开发过程中Gradle的一些常见命令和配置
概述

在前面博客Gradle 使用指南 – 基础配置 中介绍了一些 Gradle 配置的基本命令，其中有一个名称为 android的函数不知道有没有引起大家的注意：


android {
    compileSdkVersion 23
    buildToolsVersion "23.0.1"
    defaultConfig {
        ......
    }
    buildTypes {
        release {
            ......
        }
    }
}
该函数接收闭包作为参数,然而其实在Gradle的API文档中是不存在这个函数的。那么 android 脚本怎么会出现在这里呢？ 答案就是最上面的 apply plugin: 'com.android.application'。这个插件提供了 Android 构建所需要的各种 script。
Android Plugin DSL Reference：Android 插件 DSL 扩展文档，各个版本的都有。
下面简单介绍一些 Android Plugin 对 Gradle 一些扩展的知识，没有涉及的地方可以参考官方文档。

扩展介绍

下面列出了各个插件使用的Gradle扩展类型：

类型	描述
AppExtension	对 com.android.application 插件的扩展
LibraryExtension	对 com.android.library 插件的扩展
TestExtension	对 com.android.test 插件的扩展
FeatureExtension	对 com.android.feature 插件的扩展，Gradle 3.0新增
下面是 Android Plugin 一些通用的 Script blocks，以上四种类型的扩展除了对下面的支持之外，还有自己类型的一些扩展。

方法（Script blocks）	描述
aaptOptions { }	Specifies options for the Android Asset Packaging Tool (AAPT)
adbOptions { }	Specifies options for the Android Debug Bridge (ADB), such as APK installation options
buildTypes { }	Encapsulates all build type configurations for this project.
compileOptions { }	Specifies Java compiler options, such as the language level of the Java source code and generated bytecode.
dataBinding { }	Specifies options for the Data Binding Library.
defaultConfig { }	Specifies defaults for variant properties that the Android plugin applies to all build variants.
dexOptions { }	Specifies options for the DEX tool, such as enabling library pre-dexing.
externalNativeBuild { }	Configures external native build using CMake or ndk-build.
jacoco { }	Configuring JaCoCo using this block is deprecated.
lintOptions { }	Specifies options for the lint tool.
packagingOptions { }	Specifies options and rules that determine which files the Android plugin packages into your APK.
productFlavors { }	Encapsulates all product flavors configurations for this project.
signingConfigs { }	Encapsulates signing configurations that you can apply to BuildType and ProductFlavor configurations.
sourceSets { }	Encapsulates source set configurations for all variants.
splits { }	Specifies configurations for building multiple APKs or APK splits.
testOptions { }	Specifies options for how the Android plugin should run local and instrumented tests.
AppExtension

下面仅列觉一下 AppExtension 的部分扩展：

方法（Script blocks）	描述
buildToolsVersion	Specifies the version of the SDK Build Tools to use when building your project.
applicationVariants	Returns a collection of build variants that the app project includes.
compileSdkVersion	Specifies the API level to compile your project against. The Android plugin requires you to configure this property.
testVariants	Returns a collection of Android test build variants.
…	…
操作 Task

Android项目中会有大量相同的task，并且它们的名字基于Build Types和Product Flavor生成。
android 对象有两个属性：

applicationVariants（只适用于app plugin）
libraryVariants（只适用于library plugin）
featureVariants (只适用于feature plugin)
testVariants（三个plugin都适用）
这三个都会分别返回一个 ApplicationVariant、LibraryVariant 、 TestVariant 和 FeatureVariant 对象的 DomainObjectCollection。
ApplicationVariant 源码
DomainObjectCollection 继承自 Collection，可以查看 DomainObjectCollection文档说明

注意： 使用这四个 collection 中的其中一个都会触发生成所有对应的task。这意味着使用 collection 之后不需要更改配置。

DomainObjectCollection 可以直接访问所有对象，或者通过过滤器进行筛选。


android.applicationVariants.all {  variant ->
    def name = variant.name
    println "android "+name
}
DomainObjectCollection 可以直接访问所有对象，或者通过过滤器进行筛选。

参考文档

http://google.github.io/android-gradle-dsl/current/


Gradle 使用指南 -- Gradle Task
 2016-03-13 |  Gradle |  416
介绍 Gradle 中 Task 的使用
概述

Gradle 官方文档
Gradle User Guide 中文版
Task的API文档

Gradle 中的每一个 Project 都是由一个或者多个 Task 来构成的，它是 gradle 构建脚本的最小运行单元，一个 Task 代表一些更加细化的构建，可能是编译一些 classes、创建一个 Jar、生成 javadoc、或者生成某个目录的压缩文件。

Task 相关命令

./gradlew tasks：列出当前工程的所有Task
./gradlew [-q] <task name>：单独执行某个task，-q 代表 quite 模式，它不会生成 Gradle 的日志信息 (log messages)，所以用户只能看到 tasks 的输出。
创建任务

Task 构造方法

可以通过下面几个方法来构造 Task：

task myTask
task myTask { configure closure }
task myTask(type: SomeType)
task myTask(type: SomeType) { configure closure }
Task 示例

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
task hello {
    doFirst {
        println 'task hello doFirst'
    }
    doLast {
        println 'task hello doLast'
    }
    println 'config task hello'
}
task hello1 << {
    println 'task hello1'
}
task hello2 (type: Copy){
    from 'src/main/AndroidManifest.xml'
    into 'build/test'
}
task hello3(group: "myTest", description:"This is test task paras", dependsOn: ["A", "B"]){
    println 'task hello3'
}
project.task("hello4", group: "myTest", description:"This is test task para name"){
    println 'task hello4'
}
执行 ./gradlew tasks，上面的 Task 就会在列表中显示出来。

1
2
3
4
5
6
7
8
9
10
11
MyTest tasks
------------
hello3 - This is test task paras
hello4 - This is test task para name
Other tasks
-----------
...
hello
hello1
hello2
我们执行 ./gradlew -q hello，会有下面的输出：

1
2
3
config task hello
task hello doFirst
task hello doLast
这里会有个奇怪的现象，我们在执行 ./gradlew -q hello 时有上面三条输出是容易理解的，但是在执行 ./gradlew tasks 或者是执行其他 Task 比如 ./gradlew -q hello1 时，config task hello 这个打印也会输出，为什么呢？
这是因为 Task 有两种状态，分别是：

配置状态（Configuration State）
执行状态（Execution State）
这其实对应了 Gradle 三个生命周期中的配置阶段和执行阶段，task 的配置块永远在 task 动作执行之前被执行。
Gradle 会在进入执行之前，配置所有 Task，而 println 'config task hello' 这段代码就是在配置时进行执行。所以哪怕没有显式调用 gradlew hello，只是调用列出所有 task 的命令，hello task 仍然需要进入到配置状态，也就仍然执行了一遍。
hello1 task的声明方式 << 只是简写的 doLast，或者说当这个任务不需要任何在配置状态下运行的内容时，这两种声明方式是一样的。
实际上大部分时候 task 都应该是在执行状态下才真正执行的，配置状态大部分时候用于声明执行时需要用到的变量等为执行服务的前置动作。
hello2: Task创建的时候可以通过 type: SomeType 指定Type，Type其实就是告诉Gradle，这个新建的Task对象会从哪个基类Task派生。比如，Gradle本身提供了一些通用的Task，最常见的有CopyC、Delete、Sync、Tar等任务。Copy是Gradle中的一个类。当我们：task myTask(type:Copy)的时候，创建的Task就是一个Copy Task。下面会详细介绍。

Task 参数

我们用命令查看 Task 信息时一般是这样的：

1
2
3
4
5
6
7
Build tasks
-----------
assemble - Assembles all variants of all applications and secondary packages.
assembleAndroidTest - Assembles all the Test applications.
assembleDdd - Assembles all Ddd builds.
assembleDebug - Assembles all Debug builds.
assembleRelease - Assembles all Release builds.
一般是由 group、name 和 description组成，其实在上面的示例中大家应该看到 hello3、hello4和其他任务的不同了。他们两个处在同一个 group 中并且 Task 名称后面有一些描述信息。
Task的一般的属性有下面几种，可以在创建 Task 的时候在闭包中声明：

Property	Property
name	task的名字
type	task的“父类”
overwrite	是否替换已经存在的task
dependsOn	task依赖的task的集合
group	task属于哪个组
description	task的描述
动态任务

1
2
3
4
5
4.times { counter ->
    task "task$counter" << {
        println "I'm task number $counter"
    }
}
这里动态的创建了 task0，task1，task2，task3。
执行 ./gradlew -q task1，输出：

1
I'm task number 1
调用任务

短标记法

可以使用短标记 $ 可以访问一个存在的任务：

1
2
3
4
5
6
task hello << {
    println 'Hello world!'
}
hello.doLast {
    println "Greetings from the $hello.name task."
}
执行 gradle -q hello 输出：

1
2
Hello world!
Greetings from the hello task.
在Task中调用其他Task

1
2
3
4
5
6
7
task A << {
    C.execute()
}
task C << {
    println 'Hello from C'
}
上面代码通过 execute() 方法来调用 Task C。

任务依赖

创建依赖

在Gradle中，Task之间是可以存在依赖关系的。这种关系可以通过 dependsOn 来实现。

1
2
3
4
5
6
7
task A << {
    println 'Hello from A'
}
task B << {
    println 'Hello from B'
}
B.dependsOn A
或者

1
2
3
4
5
6
task A << {
    println 'Hello from A'
}
task B (dependsOn: A) << {
    println 'Hello from B'
}
执行 ./gradlew -q B 的同时会先去执行任务 A：

1
2
Hello from A
Hello from B
下面的代码同样能创建有依赖关系的任务：

1
2
3
4
5
6
7
8
9
task A << {
    println 'Hello from A'
}
task B {
    dependsOn A
    doLast {
        println 'Hello from B'
    }
}
插入依赖

也可以在已经存在的 task 依赖中插入我们的 task 。
比如前面的A和B已经存在了依赖关系，我们想在中间插入任务B1，可以通过下面的代码实现：

1
2
B1.dependsOn A
B.dependsOn B1
输出结果：

1
2
3
Hello from A
Hello from B1
Hello from B
任务排序

mustRunAfter 与 shouldRunAfter

在某些情况下，我们希望能控制任务的的执行顺序，这种控制并不是向上面那样去显示地加入依赖关系。我们可以通过 mustRunAfter 和 shouldRunAfter 来实现。
使用 mustRunAfter 意味着 taskB 必须总是在 taskA 之后运行，shouldRunAfter 和 mustRunAfter 很像，只是没有这么严格。

1
2
3
4
5
6
7
task A << {
    println 'Hello from A'
}
task B << {
    println 'Hello from B'
}
A.mustRunAfter B
执行 ./gradlew -q A B，结果：

1
2
Hello from B
Hello from A
如果换成 shouldRunAfter，结果也是一样的。
虽然我们将两个任务进行了排序，但是他们仍然是可以单独执行的，任务排序不影响任务执行。排序规则只有当两个任务同时执行时才会被应用。
比如执行 ./gradlew -q A 会输出：

1
Hello from A
另外，shouldRunAfter 不影响任务之间的执行依赖。但如果 mustRunAfter 和任务依赖之间发生了冲突，那么执行时将会报错。

finalizedBy

假如出现了这样一种使用场景，执行完任务 A 之后必须要执行一下任务 B，那么上面的方法是无法解决这个问题的，这时 finalizedBy 就派上用场了。

1
2
3
4
5
6
7
task A << {
    println 'Hello from A'
}
task B << {
    println 'Hello from B'
}
A.finalizedBy B
执行 ./gradlew -q A，结果：

1
2
Hello from A
Hello from B
Task Type

Copy

Copy的API文档

1
2
3
4
5
6
7
task hello2 (type: Copy){
    from 'src/main/AndroidManifest.xml'
    into 'build/test'
    rename {String fileName ->
        fileName = "AndroidManifestCopy.xml"
    }
}
其他 Type 具体详见文档，此处不详细解释。

Exec

Exec Task用来执行命令行。

任务的执行条件

使用判断条件

可以使用 onlyIf() 方法来为一个任务加入判断条件。就和 Java 里的 if 语句一样，任务只有在条件判断为真时才会执行。可以通过一个闭包来实现判断条件。

1
2
3
4
task A << {
    println 'Hello from A'
}
A.onlyIf{!project.hasProperty('skipA')}
执行 “./gradlew A -PskipA”，输出：

1
:app:A SKIPPED
可以看到 A 任务被跳过。

使用 StopExecutionException

如果想要跳过一个任务的逻辑并不能被判断条件通过表达式表达出来，那么可以使用 StopExecutionException。如果这个异常是被一个任务要执行的动作抛出的，这个动作之后的执行以及所有紧跟它的动作都会被跳过。构建将会继续执行下一个任务。

1
2
3
4
5
6
7
8
9
task hello {
    doFirst {
        println 'task hello doFirst'
        throw new StopExecutionException()
    }
    doLast {
        println 'task hello doLast'
    }
}
如果你直接使用 Gradle 提供的任务，这项功能还是十分有用的。它允许你为内建的任务加入条件来控制执行。

激活和注销任务

每一个任务都有一个已经激活的标记(enabled flag)，这个标记一般默认为真。 将它设置为假，那它的任何动作都不会被执行。

1
2
3
4
task A << {
    println 'Hello from A'
}
A.enabled = false
执行 “./gradlew A”，输出：

1
:app:A SKIPPED
声明任务的输入和输出

我们在执行 Gradle 任务的时候，你可能会注意到 Gradle 会跳过一些任务，这些任务后面会标注 up-to-date。代表这个任务已经运行过了或者说是最新的状态，不再需要产生一次相同的输出。
Gradle 通过比较两次 build 之间输入和输出有没有变化来确定这个任务是否是最新的，如果从上一个执行之后这个任务的输入和输出没有发生改变这个任务就标记为 up-to-date，跳过这个任务。
因此，要想跳过 up-to-date 的任务，我们必须为任务指定输入和输出。
任务的输入属性是 TaskInputs 类型. 任务的输出属性是 TaskOutputs 类型.
下面的例子中把上面的 Copy 示例中的输入和输出文件作为 hello task 的输入和输出文件：

1
2
3
4
5
6
7
task hello {
    inputs.file ("src/main/AndroidManifest.xml")
    outputs.file ("build/test/AndroidManifestCopy.xml")
    doFirst {
        println 'task hello doFirst'
    }
}
第一次执行 ./gradlew hello输出：

1
2
:app:hello
task hello doFirst
可以看到任务正常的执行。
然后进行第二次执行，输出：

1
:app:hello UP-TO-DATE
跳过这个任务。可以看到，Gradle 能够检测出任务是否是 up-to-date 状态.
如果我们修改一下 src/main/AndroidManifest.xml 文件，输入上面命令就会再次执行该任务。

UP-TO-DATE 原理

当一个任务是首次执行时，Gradle 会取一个输入的快照 (snapshot)。该快照包含组输入文件和每个文件的内容的散列。然后当 Gradle 执行任务时，如果任务成功完成，Gradle 会获得一个输出的快照。该快照包含输出文件和每个文件的内容的散列。Gradle 会保留这两个快照用来在该任务的下一次执行时进行判断。
之后，每次在任务执行之前，Gradle 都会为输入和输出取一个新的快照，如果这个快照和之前的快照一样，Gradle 就会假定这个任务已经是最新的 (up-to-date) 并且跳过任务，反之亦然。
需要注意的是，如果一个任务有指定的输出目录，自从该任务上次执行以来被加入到该目录的任务文件都会被忽略，并且不会引起任务过时 (out of date)。这是因为不相关任务也许会共用同一个输出目录。如果这并不是你所想要的情况，可以考虑使用 TaskOutputs.upToDateWhen()。

参考文档

http://wiki.jikexueyuan.com/project/GradleUserGuide-Wiki/
https://docs.gradle.org/current/userguide/userguide.html
http://www.jianshu.com/p/cd1a78dc8346

# Android # Gradle
Gradle 使用指南 -- Android DSL 扩展
Gradle 使用指南 -- Extra Properties

概述

Gradle 提供了一种名为 extra property 的方法。这就使我们扩展一些自定义属性成为可能。
可以查看Project 官方文档中对 Extra Properties 的介绍。
ExtraPropertiesExtension 官方文档
所有的扩展属性都必须通过 ext 命名空间来定义，定义完后就可以直接通过对象来引用。ext 属性支持 Project、Gradle 对象。扩展属性是可读写的。扩展属性，使得自定义属性的跨脚本传递成为可能。

扩展属性的使用

在 Gradle 对象中使用扩展属性

比如我们在 settings.gradle 中定义扩展属性：

1
gradle.ext.testGradleExt=10
那么就可以在 build.gradle 中引用：

1
println gradle.testGradleExt
在 Project 对象中使用扩展属性

比如我们在 root project 中的build.gradle 中定义扩展属性：

1
2
3
4
ext {
    testProjectExt1 = 20
    testProjectExt2 = "testProjectExt2"
}
可以在 sub project 中的 build.gradle 中引用：

1
println rootProject.testProjectExt2
当然你的定义方式还可以是这样：

1
ext.testProjectExt3 = "testProjectExt3"
扩展属性的多种定义方式

除了上面的定义方式，你还可以这样来定义扩展属性：

1

在 sub project 中定义 root project 的属性，

1
2
3
rootProject.ext {
    testProjectExt4 = "testProjectExt4"
}
然后在 root project 中引用：

1
2
3
task test << {
    println this.testProjectExt4
}
至于这里为什么要放在 task 的执行阶段来使用这个扩展属性，相信大家了解了 Gradle 的生命周期的同学都会知道的，如果放在 配置阶段去执行，会报错的。

2

还可以这样定义：

1
ext.set("testProjectExt5", 5)
3

还可以这样引用：

1
println rootProject.ext.get("testProjectExt5")
还可以这样引用

1
println rootProject.ext["testProjectExt5"]
4

可以这样去更新一个扩展属性的值：

1
rootProject.testProjectExt5 = 55
也可以这样：

1
rootProject.ext["testProjectExt5"] = 66
扩展属性的一些属性和方法

可以参考ExtraPropertiesExtension 官方文档

properties

properties 属性返回一个 Map<String, Object> 对象，存储了当前对象定义的所有扩展属性。

1
2
3
println rootProject.properties.each { key, value ->
    println key
}
1
println rootProject.properties.containsKey("testProjectExt5")
has

返回当前对象是否包含给定的属性：

1
2
println rootProject.ext.has("testProjectExt5")
println rootProject.hasProperty("testProjectExt5")
get，set

设置和取值的两个方法：
get

1
2
3
4
5
6
7
8
project.ext { foo = "bar" }
println project.ext.get("foo") == "bar"
println project.ext.foo == "bar"
println project.ext["foo"] == "bar"
println project.foo == "bar"
println project["foo"] == "bar"
set

1
2
3
4
5
6
7
project.ext.set("foo", "bar")
project.ext.foo = "bar"
project.ext["foo"] = "bar"
// Once the property has been created via the extension, it can be changed by the owner.
project.foo = "bar"
project["foo"] = "bar"
命令行自定义扩展属性

在 Gradle 使用指南 – 基础配置 一文中我们也介绍了如何在命令行自定义扩展属性。

1
./gradlew assembleDebug -Pcustom=true
在脚本中使用：

1
2
3
if (project.hasProperty('custom')){
}
我们还可以通过 -D 来添加系统属性。

grade.properties 文件扩展属性

可以通过 gradle.properties 文件中声明直接添加到项目中使用，在这个属性文件中声明的属性对所有的项目可用。
比如在 gradle.properties 文件中声明：

1
2
3
VERSION_CODE=200100
VERSION_NAME=2.1.0
testProperties = "testProperties"
在 root project中使用：

1
2
versionCode project.VERSION_CODE as int
versionName project.VERSION_NAME
在 app subproject 中使用：

1
2
println rootProject.hasProperty("testProperties")
println this.hasProperty("testProperties")
另外，我们还可以在 gradle.properties 文件中添加系统属性。如果有 systemProp. 为前缀的属性会被识别为系统属性。

比如在 gradle.properties 文件中声明：

1
systemProp.testProperties="testProperties"
可以通过下面方式使用：

1
println System.properties["testProperties"]

生命周期概述

无论什么时候执行 Gradle 构建，都会运行三个不同的生命周期阶段：

初始化阶段
配置阶段
执行阶段
在初始化阶段，Gradle 为项目创建了 Project 实例。在给定的构建脚本中只定义了一个项目。在多项目构建中，这个构建阶段变得更加重要。根据你正在执行的项目，Gradle 找出哪些项目需要参与到构建中。实质为执行 settings.gradle 脚本。注意，在这个阶段当前已有的构建脚本代码都不会被执行。
在配置阶段，Gradle 构造了一个模型来表示任务，并参与到构建中来。增量式构建特性决定来模型中的 task 是否需要运行。配置阶段完成后，整个 build 的 project 以及内部的 Task 关系就确定了。这个阶段非常适合于为项目或指定 task 设置所需的配置。实质为解析每个被加入构建项目的 build.gradle 脚本。注意，项目的每一次构建的任何配置代码都可以被执行–即使你只执行 gradle tasks。
在执行阶段，所有的 task 都应该以正确的顺序被执行。执行顺序时由它们的依赖决定的。如果任务被认为没有被修改过，将被跳过。
Gradle 的增量式的构建特性紧紧地与生命周期相结合。
作为一个开发人员，不能仅限于编写在不同构建阶段执行的 task 动作或者配置逻辑。有时候当一个特定的生命周期事件发生时你可能想要执行代码。一个声明周期事件可能发生在某个构建阶段之前、期间或者之后。在执行阶段之后发生的生命周期事件是构建的完成。
我们有两种方式可以编写回调声明周期事件：在闭包中，或者是通过 Gradle API 所提供的监听器接口实现。Gradle 不会引导你采用哪种方式去监听生命周期事件，着完全取决于你的选择。
下面提供一个有用的声明周期钩子（Hook）的用法。

图片

许多生命周期回调方法被定义在 Gradle 和 Projet 接口中。
不要害怕使用生命周期钩子，它们不是 Gradle API 的秘密后门，相反，它们是特意提供给开发者使用的。

生命周期监听方法

如果我们想在 Gradle 特定的阶段去 Hook 指定的任务，那么就需要对如何监听生命周期回调做一些了解。
Gradle 和 Projet 对象提供了一些方法来供我们设置一些生命周期的回调方法。
生命周期监听的设置有两种方法：

实现一个特定的监听接口；
提供一个用于在收到通知时执行的闭包。
上面两个对象对这两种方法的都是支持的。
Projet 提供的一些生命周期回调方法：

afterEvaluate(closure)，afterEvaluate(action)
beforeEvaluate(closure)，beforeEvaluate(action)
Gradle 提供的一些生命周期回调方法：

afterProject(closure)，afterProject(action)
beforeProject(closure)，beforeProject(action)
buildFinished(closure)，buildFinished(action)
projectsEvaluated(closure)，projectsEvaluated(action)
projectsLoaded(closure)，projectsLoaded(action)
settingsEvaluated(closure)，settingsEvaluated(action)
addBuildListener(buildListener)
addListener(listener)
addProjectEvaluationListener(listener)
可以看到，每个方法都有两个不同参数的方法，一个接收闭包作为回调，另外一个接受 Action 作为回调，下面的介绍时只介绍闭包为参数的方法。
请注意：一些声明周期事件只有在适当的位置上声明才会发生。

下面开始介绍 project 的几个方法：

beforeEvaluate

beforeEvaluate()是在 project 开始配置前调用，当前的 project 作为参数传递给闭包。
这个方法很容易误用，你要是直接当前子模块的 build.gradle 中使用是肯定不会调用到的，因为Project都没配置好所以也就没它什么事情，这个代码块的添加只能放在父工程的 build.gradle 中,如此才可以调用的到。

1
2
3
4
5
this.project.subprojects { sub ->
    sub.beforeEvaluate { project
        println "#### Evaluate before of "+project.path
    }
}
如果你想用 Action 作为参数的方法：

1
2
3
4
5
6
7
8
this.project.subprojects { sub ->
    sub.beforeEvaluate(new Action<Project>() {
        @Override
        void execute(Project project) {
            println "#### Evaluate before of "+project.path
        }
    })
}
afterEvaluate

afterEvaluate 是一般比较常见的一个配置参数的回调方式，只要 project 配置成功均会调用，不论是在父模块还是子模块。参数类型以及写法与afterEvaluate相同：

1
2
3
project.afterEvaluate { pro ->
    println("#### Evaluate after of " + pro.path)
}
再来看一下 Gradle 对象的几个回调，可以通过 project 获取当前的 gradle 对象，gradle 设置的回调监控的是所有的 project 实现。

afterProject

设置一个 project 配置完毕后立即执行的闭包或者回调方法。
afterProject 在配置参数失败后会传入两个参数，前者是当前 project，后者显示失败信息。

1
2
3
4
5
6
7
this.getGradle().afterProject { project,projectState ->
    if(projectState.failure){
        println "Evaluation afterProject of "+project+" FAILED"
    } else {
        println "Evaluation afterProject of "+project+" succeeded"
    }
}
beforeProject

设置一个 project 配置前执行的闭包或者回调方法。
当前 project 作为参数传递给闭包。
子模块的该方法声明在 root project 中回调才会执行，root project 的该方法声明在 settings.gradle 中才会执行。

1
2
3
gradle.beforeProject { p ->
    println("Evaluation beforeProject"+p)
}
buildFinished

构建结束时的回调，此时所有的任务都已经执行，一个构建结果的对象 BuildResult 作为参数传递给闭包。

1
2
3
gradle.buildFinished { r ->
    println("buildFinished "+r.failure)
}
projectsEvaluated

所有的 project 都配置完成后的回调，此时，所有的project都已经配置完毕，准备开始生成 task 图。gradle 对象会作为参数传递给闭包。

1
2
3
gradle.projectsEvaluated {gradle ->
    println("projectsEvaluated")
}
projectsLoaded

当 setting 中的所有project 都创建好时执行闭包回调。gradle 对象会作为参数传递给闭包。
这个方法也比较特殊，只有声明在适当的位置上才会发生，如果将这个声明周期挂接闭包声明在 build.gradle 文件中，那么将不会发生这个事件，因为项目创建发生在初始化阶段。
放在 settings.gradle 中是可以执行的。

1
2
3
gradle.projectsLoaded {gradle ->
    println("@@@@@@@ projectsLoaded")
}
settingsEvaluated

当 settings.gradle 加载并配置完毕后执行闭包回调，setting对象已经配置好并且准备开始加载构建 project。
这个回调在 build.gradle 中声明也是不起作用的，在 settings.gradle 中声明是可以的。

1
2
3
gradle.settingsEvaluated {
    println("@@@@@@@ settingsEvaluated")
}
前面我们说过，设置监听回调还有另外一种方法，通过设置接口监听添加回调来实现。作用的对象均是所有的 project 实现。

addProjectEvaluationListener

1
2
3
4
5
6
7
8
9
10
11
gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
    @Override
    void beforeEvaluate(Project project) {
        println " add project evaluation lister beforeEvaluate,project path is: "+project
    }
    @Override
    void afterEvaluate(Project project, ProjectState state) {
        println " add project evaluation lister afterProject,project path is:"+project
    }
})
addListener

添加一个实现来 listener 接口的对象到 build。

addBuildListener

添加一个 BuildListener 对象到 Build 。

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
gradle.addBuildListener(new BuildListener() {
    @Override
    void buildStarted(Gradle gradle) {
        println("### buildStarted")
    }
    @Override
    void settingsEvaluated(Settings settings) {
        println("### settingsEvaluated")
    }
    @Override
    void projectsLoaded(Gradle gradle) {
        println("### projectsLoaded")
    }
    @Override
    void projectsEvaluated(Gradle gradle) {
        println("### projectsEvaluated")
    }
    @Override
    void buildFinished(BuildResult result) {
        println("### buildFinished")
    }
})
Task 执行图（TaskExecutionGraph）

在配置时，Gradle 决定了在执行阶段要运行的 task 的顺序，他们的依赖关系的内部结构被建模为一个有向无环图，我们可以称之为 taks 执行图，它可以用 TaskExecutionGraph 来表示。可以通过 gradle.taskGraph 来获取。
在 TaskExecutionGraph 中也可以设置一些 Task 生命周期的回调：

addTaskExecutionGraphListener(TaskExecutionGraphListener listener)
addTaskExecutionListener(TaskExecutionListener listener)
afterTask(Action action)，afterTask(Closure closure)
beforeTask(Action action)，beforeTask(Closure closure)
whenReady(Action action)，whenReady(Closure closure)
下面来进行详细介绍。

addTaskExecutionGraphListener

添加 task 执行图的监听器，当执行图配置好会执行通知。

1
2
3
4
5
6
gradle.taskGraph.addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
    @Override
    void graphPopulated(TaskExecutionGraph graph) {
        println("@@@ gradle.taskGraph.graphPopulated ")
    }
})
addTaskExecutionListener

添加 task 执行监听器，当 task 执行前或者执行完毕会执行回调发出通知。

1
2
3
4
5
6
7
8
9
10
11
gradle.taskGraph.addTaskExecutionListener(new TaskExecutionListener() {
    @Override
    void beforeExecute(Task task) {
        println("@@@ gradle.taskGraph.beforeTask "+task)
    }
    @Override
    void afterExecute(Task task, TaskState state) {
        println("@@@ gradle.taskGraph.afterTask "+task)
    }
})
afterTask

设置一个 task 执行完毕的闭包或者回调方法。
该 task 作为参数传递给闭包。

1
2
3
gradle.taskGraph.afterTask { task ->
    println("### gradle.taskGraph.afterTask "+task)
}
beforeTask

设置一个 task 执行前的闭包或者回调方法。
该 task 作为参数传递给闭包。

1
2
3
gradle.taskGraph.beforeTask { task ->
    println("### gradle.taskGraph.beforeTask "+task)
}
whenReady

设置一个 task 执行图准备好后的闭包或者回调方法。
该 taskGrahp 作为参数传递给闭包。

1
2
3
gradle.taskGraph.whenReady { taskGrahp ->
    println("@@@ gradle.taskGraph.whenReady ")
}
生命周期顺序

我们通过在生命周期回调中添加打印的方法来看一下他们的执行顺序。
为了看一下配置 task 的时机，我们在 app 模块中创建来一个 taks：

1
2
3
4
5
6
7
8
9
task hello {
    doFirst {
        println '*** task hello doFirst'
    }
    doLast {
        println '*** task hello doLast'
    }
    println '*** config task hello'
}
为了保证生命周期的各个回调方法都被执行，我们在 settings.gradle 中添加各个回调方法。

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
gradle.addBuildListener(new BuildListener() {
    @Override
    void buildStarted(Gradle gradle) {
        println("### gradle.buildStarted")
    }
    @Override
    void settingsEvaluated(Settings settings) {
        println("### gradle.settingsEvaluated")
    }
    @Override
    void projectsLoaded(Gradle gradle) {
        println("### gradle.projectsLoaded")
    }
    @Override
    void projectsEvaluated(Gradle gradle) {
        println("### gradle.projectsEvaluated")
    }
    @Override
    void buildFinished(BuildResult result) {
        println("### gradle.buildFinished")
    }
})
gradle.afterProject { project,projectState ->
    if(projectState.failure){
        println "### gradld.afterProject "+project+" FAILED"
    } else {
        println "### gradle.afterProject "+project+" succeeded"
    }
}
gradle.beforeProject { p ->
    println("### gradle.beforeProject "+p)
}
gradle.allprojects(new Action<Project>() {
    @Override
    void execute(Project project) {
        project.beforeEvaluate { project
            println "### project.beforeEvaluate "+project
        }
        project.afterEvaluate { pro ->
            println("### project.afterEvaluate " + pro)
        }
    }
})
gradle.taskGraph.addTaskExecutionListener(new TaskExecutionListener() {
    @Override
    void beforeExecute(Task task) {
        if (task.name.equals("hello")){
            println("@@@ gradle.taskGraph.beforeTask "+task)
        }
    }
    @Override
    void afterExecute(Task task, TaskState state) {
        if (task.name.equals("hello")){
            println("@@@ gradle.taskGraph.afterTask "+task)
        }
    }
})
gradle.taskGraph.addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
    @Override
    void graphPopulated(TaskExecutionGraph graph) {
        println("@@@ gradle.taskGraph.graphPopulated ")
    }
})
gradle.taskGraph.whenReady { taskGrahp ->
    println("@@@ gradle.taskGraph.whenReady ")
}
执行 task hello：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
./gradlew hello
### gradle.settingsEvaluated
### gradle.projectsLoaded
> Configure project : 
### gradle.beforeProject root project 'TestSomething'
### project.beforeEvaluate root project 'TestSomething'
### gradle.afterProject root project 'TestSomething' succeeded
### project.afterEvaluate root project 'TestSomething'
> Configure project :app 
### gradle.beforeProject project ':app'
### project.beforeEvaluate project ':app'
*** config task hello
### gradle.afterProject project ':app' succeeded
### project.afterEvaluate project ':app'
> Configure project :common 
### gradle.beforeProject project ':common'
### project.beforeEvaluate project ':common'
### gradle.afterProject project ':common' succeeded
### project.afterEvaluate project ':common'
### gradle.projectsEvaluated
@@@ gradle.taskGraph.graphPopulated 
@@@ gradle.taskGraph.whenReady 
> Task :app:hello 
@@@ gradle.taskGraph.beforeTask task ':app:hello'
*** task hello doFirst
*** task hello doLast
@@@ gradle.taskGraph.afterTask task ':app:hello'
BUILD SUCCESSFUL in 1s
1 actionable task: 1 executed
### gradle.buildFinished
因此，生命周期回调的执行顺序是：
gradle.settingsEvaluated->
gradle.projectsLoaded->
gradle.beforeProject->
project.beforeEvaluate->
gradle.afterProject->
project.afterEvaluate->
gradle.projectsEvaluated->
gradle.taskGraph.graphPopulated->
gradle.taskGraph.whenReady->
gradle.buildFinished

概述

Gradle 的插件可以有三种形式来提供：

直接在build.gradle中编写Plugin，这种方式这种方法写的Plugin无法被其他 build.gradle 文件引用。
单独的一个Module，这个Module的名称必须为buildSrc，同一个工程中所有的构建文件够可以引用这个插件，但是不能被其他工程引用。
在一个项目中自定义插件，然后上传到远端maven库等，其他工程通过添加依赖，引用这个插件。
本文只对后面两种方式来进行简单介绍。

在当前项目中创建插件

最终的目录结构为：

效果图

创建Plugin

创建一个 Module （Phone&Tablet Module 或 Android Librarty 都可以），Module的名称必须为 buildSrc。
将Module里面的内容删除，只保留build.gradle文件和src/main目录。
我们开发的 gradle 插件相当于一个 groovy 项目。所以需要在 main 目录下新建 groovy 目录。
然后创建一个 Java 文件一样的方式创建一个 groovy 文件，比如报名为 com.android.hq.testplugin 的 TestPlugin.groovy 文件。
修改build.gradle

因为我们要用到 groovy，以及要用到gradle和groovy的sdk，因此将 buildSrc 下面的 build.gradle 修改为：

1
2
3
4
5
6
7
8
9
10
11
12
apply plugin: 'groovy'
dependencies {
    //gradle sdk
    compile gradleApi()
    //groovy sdk
    compile localGroovy()
}
repositories {
    jcenter()
}
实现

实现 TestPlugin 类，在脚本中通过实现gradle的Plugin接口，实现apply方法即可：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
package com.android.hq.testplugin
import org.gradle.api.Plugin
import org.gradle.api.Project;
public class TestPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        // 实现一个名称为testPlugin的task，设置分组为 myPlugin，并设置描述信息
        project.task('testPlugin', group: "myPlugin", description: "This is my test plugin") << {
            println "## This is my first gradle plugin in testPlugin task"
        }
        println "** This is my first gradle plugin"
    }
}
这里在简单实现了一个名称为testPlugin的task，当执行该task时，会打印 ## This is my first gradle plugin in testPlugin task，而** This is my first gradle plugin在执行所有的task时都会打印。

使用

使用方式比较简单，在 app 的 build.gradle 中添加

1
apply plugin: com.android.hq.testplugin.TestPlugin
即可。

测试

在 Gradle 的 task 视窗里面: app/myplugin 下多了一个 testPlugin 的 task。

输入 ./gradlew tasks，我们可以看到 testPlugin 已经在task列表中。

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
:tasks
------------------------------------------------------------
All tasks runnable from root project
------------------------------------------------------------
Android tasks
-------------
androidDependencies - Displays the Android dependencies of the project.
signingReport - Displays the signing info for each variant.
sourceSets - Prints out all the source sets defined in this project.
Build tasks
-----------
assemble - Assembles all variants of all applications and secondary packages.
assembleAndroidTest - Assembles all the Test applications.
assembleDebug - Assembles all Debug builds.
assembleRelease - Assembles all Release builds.
build - Assembles and tests this project.
......
MyPlugin tasks
--------------
testPlugin - This is my test plugin
执行 testPlugin task，输出：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
$ ./gradlew testPlugin
:buildSrc:compileJava UP-TO-DATE
:buildSrc:compileGroovy UP-TO-DATE
:buildSrc:processResources UP-TO-DATE
:buildSrc:classes UP-TO-DATE
:buildSrc:jar UP-TO-DATE
:buildSrc:assemble UP-TO-DATE
:buildSrc:compileTestJava UP-TO-DATE
:buildSrc:compileTestGroovy UP-TO-DATE
:buildSrc:processTestResources UP-TO-DATE
:buildSrc:testClasses UP-TO-DATE
:buildSrc:test UP-TO-DATE
:buildSrc:check UP-TO-DATE
:buildSrc:build UP-TO-DATE
** This is my first gradle plugin
** build versionName=2.1.0
:app:testPlugin
## This is my first gradle plugin in testPlugin task
BUILD SUCCESSFUL
Total time: 5.358 secs
可以看到，** This is my first gradle plugin和## This is my first gradle plugin in testPlugin task都会打印出来。
执行 assembleDebug task，输出：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
$ ./gradlew assembleDebug
:buildSrc:compileJava UP-TO-DATE
:buildSrc:compileGroovy UP-TO-DATE
:buildSrc:processResources UP-TO-DATE
:buildSrc:classes UP-TO-DATE
:buildSrc:jar UP-TO-DATE
:buildSrc:assemble UP-TO-DATE
:buildSrc:compileTestJava UP-TO-DATE
:buildSrc:compileTestGroovy UP-TO-DATE
:buildSrc:processTestResources UP-TO-DATE
:buildSrc:testClasses UP-TO-DATE
:buildSrc:test UP-TO-DATE
:buildSrc:check UP-TO-DATE
:buildSrc:build UP-TO-DATE
** This is my first gradle plugin
** build versionName=2.1.0
:app:preBuild UP-TO-DATE
:app:preDebugBuild UP-TO-DATE
......
:app:assembleDebug UP-TO-DATE
BUILD SUCCESSFUL
Total time: 4.925 secs
可以看到，只有 ** This is my first gradle plugin 输出。

独立的插件项目

这种类型的插件可以上传到远端 maven 库等，其他工程通过添加依赖，引用这个插件。
创建步骤和前面的在当前项目中创建插件的步骤有些是类似的。

创建Plugin

这里的步骤除了3,4,5和前的一样，其他的都是不一样的。

创建一个新的 Project。
同样创建一个 Module （Phone&Tablet Module 或 Android Librarty 都可以），Module的名称随意。
将Module里面的内容删除，只保留build.gradle文件和src/main目录。
我们开发的 gradle 插件相当于一个groovy项目。所以需要在main目录下新建groovy目录。
然后创建一个 Java 文件一样的方式创建一个 groovy文件，比如报名为 com.android.hq.testplugin 的 TestPlugin.groovy 文件。
现在，我们已经定义好了自己的 gradle 插件类，接下来就是告诉 gradle，哪一个是我们自定义的插件类，因此，需要在 main 目录下新建 resources 目录，然后在 resources 目录里面再新建 META-INF 目录，再在 META-INF 里面新建 gradle-plugins 目录。最后在 gradle-plugins 目录里面新建 properties 文件，注意这个文件的命名，你可以随意取名，但是后面使用这个插件的时候，会用到这个名字。比如，你取名为com.android.hq.testplugin.properties，而在其他 build.gradle 文件中使用自定义的插件时候则需写成：

1
apply plugin: 'com.android.hq.testplugin'
然后在com.android.hq.testplugin.properties文件里面指明你自定义的类：

1
implementation-class=com.android.hq.testplugin.TestPlugin
修改build.gradle
因为我们要用到 groovy 以及后面打包要用到 maven ,所以在我们自定义的 Module 下的 build.gradle 需要添加如下代码：

1
2
3
4
5
6
7
8
9
10
11
12
13
apply plugin: 'groovy'
apply plugin: 'maven'
dependencies {
    //gradle sdk
    compile gradleApi()
    //groovy sdk
    compile localGroovy()
}
repositories {
    mavenCentral()
}
实现

实现 TestPlugin 类，这一步和前面的一样：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
package com.android.hq.testplugin
import org.gradle.api.Plugin
import org.gradle.api.Project;
public class TestPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        // 实现一个名称为testPlugin的task，设置分组为 myPlugin，并设置描述信息
        project.task('testPlugin', group: "myPlugin", description: "This is my test plugin") << {
            println "## This is my first gradle plugin in testPlugin task"
        }
        println "** This is my first gradle plugin"
    }
}
打包

前面我们已经自定义好了插件，接下来就是要打包到Maven库里面去了，你可以选择打包到本地，或者是远程服务器中。在我们自定义Module目录下的build.gradle添加如下代码：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
//group和version在后面使用自定义插件的时候会用到
group='com.android.hq.testplugin'
version='1.0.0'
uploadArchives {
    repositories {
        mavenDeployer {
            //提交到远程服务器：
            //repository(url: "http://www.xxx.com/repos") {
            //    authentication(userName: "admin", password: "admin")
            //}
            //本地的Maven地址设置为/mnt/TestRepos/
            repository(url: uri('/mnt/TestRepos/'))
        }
    }
}
现在我们定义好了打包地址以及打包相关配置，还需要我们让这个打包 task 执行。点击 AndroidStudio 右侧的 gradle 工具，如下图所示：

效果图

可以看到有 uploadArchives 这个 Task,双击 uploadArchives 就会执行打包上传。执行完成后，去我们的 Maven 本地仓库查看一下：

1
2
hq@EF-hq:/mnt/TestRepos/com/android/hq/testplugin/testplugin/1.0.0$ ls
testplugin-1.0.0.jar  testplugin-1.0.0.jar.md5  testplugin-1.0.0.jar.sha1  testplugin-1.0.0.pom  testplugin-1.0.0.pom.md5  testplugin-1.0.0.pom.sha1
其中，/com/android/hq/testplugin/ 这几层目录是由我们的 group 指定，testplugin 是模块的名称，1.0.0是版本号（version指定）。

使用

首先我们需要在使用的 Module 的build.gradle文件中里面指定Maven地址、自定义插件的名称以及依赖包名。代码如下：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
//com.android.hq.testplugin.TestPlugin为resources/META-INF/gradle-plugins 下的properties文件名称
apply plugin: 'com.android.hq.testplugin'
buildscript {
    repositories {
        maven {
            //本地Maven仓库地址
            url uri('/mnt/TestRepos/')
        }
    }
    dependencies {
        //格式为-->group:module:version
        classpath 'com.android.hq.testplugin:testplugin:1.0.0'
    }
}
然后就可以测试使用情况了。

测试

和前面的方法一样，不再详述。

参考

http://www.jianshu.com/p/d53399cd507b

概述

前面的博客Gradle 使用指南 – 创建Plugin 介绍了如何去创建一个插件，那么这篇文章将介绍一些深入的知识：如何对自定义插件进行 DSL 扩展。
在博客Gradle 使用指南 – Android DSL 扩展 Android 插件对 Gradle 进行的 DSL 扩展，那么我们自定义插件也是完全可以做到的。

DSL 扩展基本实现

我们在进行 Gradle 配置时，很多的配置都是在 build.gradle 文件中进行的，插件可以在构建过程中获取这些输入。我们自定义的插件也是可以做到这一点的。这就要借助 ExtensionContainer 来实现。
怎么来得到一个 ExtensionContainer 对象呢？我们来看一下 Project 的 getExtensions() 方法：

1
ExtensionContainer getExtensions();
它的返回者是一个 ExtensionContainer 对象。
通过 ExtensionContainer 我们可以向目标对象添加DSL扩展，通过 ExtensionContainer 的 create() 方法来创建新的 DSL 域，并与一个对应的委托类关联起来（即新建一个 DSL 域，并委托给一个具体类），通过它来跟踪传递给插件的所有设置和属性。

首先实现一个扩展类：

1
2
3
4
5
6
7
8
9
10
11
12
13
class MyExtension {
    String message
    Boolean isDebug
    
    String getMessage() {
        return message
    }
    void setMessage(String message) {
        println "set message = "+message
        this.message = message
    }
}
然后在插件中的 apply 方法中通过 project.extensions.create() 创建DSL扩展：

1
2
3
4
5
6
7
8
9
10
11
public class MyFirstPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        project.extensions.create('myplugin', MyExtension.class)
        // 实现一个名称为myPlugin的task
        project.task('myPlugin') << {
            println project.myplugin.message
            println project.myplugin.isDebug
        }
    }
}
在 build.gradle 中通过引入插件后就可以配置了

1
2
3
4
5
6
apply plugin: com.android.hq.myfirstplugin.MyFirstPlugin
myplugin {
    message "Hello Plugin"
    isDebug true
}
运行 myPlugin task 后可以通过打印看到配置中的输入。

传递参数

我们先来看一下 create 方法：

1
<T> T create(String var1, Class<T> var2, Object... var3);
第一个参数是在 build.gradle 中可以配置的代码块的方法名称；
第二个参数是关联的扩展实体类的名称
后面的参数表示传递给实体类构造函数的参数

比如想把 apply 方法的 project 参数传递进来，就需要这样引用：

1
project.extensions.create('myplugin', MyExtension.class, project)
那么对应 MyExtension 类要加一个带 Project 类的构造函数：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
class MyExtension {
    String message
    Boolean isDebug
    public MyExtension(Project project) {
    }
    String getMessage() {
        return message
    }
    void setMessage(String message) {
        println "set message = "+message
        this.message = message
    }
}
子 Script blocks 配置

在使用配置 gradle 中我们一定使用过这样的配置：

1
2
3
4
5
6
7
8
9
android {
    compileSdkVersion 23
    buildToolsVersion "23.0.1"
    defaultConfig {
        applicationId "com.example.heqiang.testsomething"
        minSdkVersion 23
        targetSdkVersion 23
    }
}
在 android 代码块中还可以进行 defaultConfig 代码块的配置，那么在自定义 Plugin 中这个配置方法也是可以实现的，也是要借助 ExtensionContainer 的 create() 方法来实现新的 DSL 域的创建。
比如在上面的例子中我们想在 myplugin 域中创建一个 defaultConfig 配置方法，那么可以在 MyExtension 中通过下面的代码来实现：
实现一个 DefaultConfig 类：

1
2
3
4
5
6
7
8
public class DefaultConfig {
    String applicationId
    int minSdkVersion
    int targetSdkVersion
    public DefaultConfig(String name) {
        println "name = " + name
    }
}
在 MyExtension 中添加映射关系：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
class MyExtension {
    String message
    Boolean isDebug
    public MyExtension() {
        this.extensions.create("defaultConfig", DefaultConfig, "defaultConfig")
    }
    
    String getMessage() {
        return message
    }
    void setMessage(String message) {
        println "set message = "+message
        this.message = message
    }
}
1
2
3
4
5
6
7
8
9
10
11
12
13
14
public class MyFirstPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        project.extensions.create('myplugin', MyExtension.class)
        // 实现一个名称为myPlugin的task
        project.task('myPlugin') << {
            println project.myplugin.message
            println project.myplugin.isDebug
            println project.myplugin.defaultConfig.applicationId
            println project.myplugin.defaultConfig.minSdkVersion
            println project.myplugin.defaultConfig.targetSdkVersion
        }
    }
}
配置代码：

1
2
3
4
5
6
7
8
9
10
11
12
myplugin {
    message "Hello Plugin"
    isDebug true
    defaultConfig {
        applicationId "com.android.hq.test"
        minSdkVersion 23
        targetSdkVersion 23
        
        println "print in defaultConfig"
    }
}
容器扩展

在进行 Android 配置时，我们一定用过 buildTypes 的配置，类似：

1
2
3
4
5
6
7
8
9
10
buildTypes {
    release {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
    }
    debug {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
    }
}
这种类型可以用于在代码块中创建新的指定类型的对象。
先来看一下源码：

1
2
3
4
public void buildTypes(Action<? super NamedDomainObjectContainer<BuildType>> action) {
    this.checkWritability();
    action.execute(this.buildTypes);
}
它传入的是一个 BuildType 类型列表的闭包代码。
NamedDomainObjectContainer 是一个容器，追根溯源它是继承自 Collection<T>。
我们这里叫它命名对象容器，可以用于在 builds cript 中创建对象,创建的对象必须要有 name 属性作为容器内元素的标识。
怎么来得到这样的容器对象呢？我们来看一下 Project 的 container 方法：

1
2
3
<T> NamedDomainObjectContainer<T> container(Class<T> var1);
<T> NamedDomainObjectContainer<T> container(Class<T> var1, NamedDomainObjectFactory<T> var2);
<T> NamedDomainObjectContainer<T> container(Class<T> var1, Closure var2);
下面通过实例来介绍一下。

实例介绍

首先创建一个 BuildConfig 类，上面说了，这个类必须有个 name 属性：

1
2
3
4
5
6
7
8
9
10
public class BuildConfig {
    final String name
    public boolean debugMode
    String applicationId
    BuildConfig(String name) {
        this.name = name
        println "BuildConfig name = "+name
    }
}
然后创建一个容器并将容器添加为 extension：

1
2
3
4
5
6
7
8
public MyExtension(Project project) {
    this.extensions.create("defaultConfig", DefaultConfig, "defaultConfig")
    // 创建一个容器
    NamedDomainObjectContainer<BuildConfig> buildConfigs = project.container(BuildConfig)
    // 将容器添加为 extension
    this.extensions.add("buildConfigs", buildConfigs)
}
配置：

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
myplugin {
    message "Hello Plugin"
    isDebug true
    defaultConfig {
        applicationId "com.android.hq.test"
        minSdkVersion 23
        targetSdkVersion 23
        println "print in defaultConfig"
    }
    buildConfigs {
        debug {
            debugMode = true
            applicationId = "com.android.hq.test"
        }
        release {
            debugMode = false
            applicationId = "com.android.hq.test"
        }
        demo {
            debugMode = true
            applicationId = "com.android.hq.test"
        }
    }
}
在这里配置赋值时必须要用 = 号，否则会报错，没搞清楚原因。
