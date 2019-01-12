# JOptimization
关于优化的学习库

## 第2章 APP性能优化概览与平台化实践

### 2-1 性能优化有哪些难题
    性能表现差
        App启动慢、卡顿、丢帧
        内存占用高、抖动频繁
        好点、网络请求慢
        崩溃率、异常率高
        
    线上问题无从追查
        如何保证异常感知灵敏度
        如何复原“案发”现场
        如何快速“止血”成功
        
    性能优化的长期开销大
        如何扼杀问题于萌芽（备注：以更高的视角来指导自己的工作）
        优化效果如何长期保持
        
    总结
        性能表现好
        显示问题易追查
        长期投入小

### 2-2 App性能优化解决方案演进
    项目初期
        快速占领市场，尽可能获取用户，累功能阶段，没什么资源投入性能优化
        只关心崩溃率、不采集性能数据
        没有性能检测、优化方案
        没有排查问题手段
        
    项目壮大期
        指标采集、不够全及深入
        介入成熟APM,排查手段单一
        线下检测、优化,方案不成型
        
    项目成熟期
        重点关注性能问题，数据丰富，手段多样化
        线上、线下一整套完善解决方案（重点）
            误区：对线上不重视
            侧重点：线下预防、线上监控
            方案不同：线下可用黑科技
            
        自检APM，新产品可快速介入
            为什么要自建APM?
                .成熟APM通用，但不满足个性化需求
                .外部APM与内部系统难打通，带来的时间成本
                .数据必须掌握在自己手中
        
    为什么需要自建APM?
    学到什么？
        深刻理解性能优化手段、思维发展历程
        面试展现对更高层次的思考、理解
    背景
        性能优化方案一直在进步
        项目不同阶段重心不一样
        
### 2-3业界优秀的平台化实践初步认知
     Crash收集平台
        Bugly为代表
            数据采集、上报成功率高
            包含Java、Native崩溃
            建议项目初期接入
    APM平台
        听云为代表
            通用的性能解决方案，数据采集完善
            方便接入，但不满足个性化需求，数据隐患
            建议性能方案不完善的阶段接入
            
    自建解决方案
        美团、携程、360等
            .贴合自身业务特点，满足定制化需求
            .数据安全
            
## 第3章 App启动优化

### 3-1 App启动优化介绍
    背景介绍
        第一体验
        八秒定律
    启动分类
        文章：App startup time
            .冷启动
                耗时最多，衡量标准
                click event -> IPC -> process.start -> ActivityThread
                -> bindApplication -> LifeCycle -> ViewRootImpl
            .热启动
                最快
                后台 -> 前台
            .温启动
                较快
                LifeCycle
    相关任务
        冷启动之前
            .启动App
            .加载空白Window
            .创建进程
        随后任务
            .创建Application
            .启动主线程
            .启动MainActivity
            .加载布局
            .布置屏幕
            .首帧绘制
    优化方向
        .Application和Activity生命周期
    总结
        启动分类及具体场景（重点）
        启动阶段相关任务及优化方向
### 3-2 启动时间测量方式  
    adb命令
        adb shell am start -W packagename/mainactivity
        示例：
            adb shell am start -W com.tencent.joptimization/com.tencent.joptimization.MainActivity
            Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.tencent.joptimization/.MainActivity }
            Status: ok
            Activity: com.tencent.joptimization/.MainActivity
            ThisTime: 582 //最后一个Activity启动耗时
            TotalTime: 582 //所有Acivity启动耗时
            WaitTime: 600 //AMS启动Activity的总耗时
            Complete
        优点：
            线下使用方便，不能带到线上
            非严谨、精确时间
    手动打点
        启动时买点，启动结束埋点，二者差值
        
        public class LaunchTimer {
            private static long sTime;
        
            public static void startRecord() {
                sTime = System.currentTimeMillis();
            }
        
            public static void endRecord() {
                long time = System.currentTimeMillis() - sTime;
                Log.i("launchTime", "launchTime:" + time);
            }
        }    
        一般开始计时是放在Application的attachBaseContext里面
        而对于结束时间记录呢？
        误区：
            onWindowFocusChanged只是Actvity的首帧时间，并非是用户真正看到数据的时间
        正解：
            真实数据展示，Feed第一条展示
            
        优点：
            精确，可带到线上，推荐使用
        注意：
            避开误区，采用Feed第一条展示

    总结
        启动时间测量的两种方式
        严谨真实的启动时间（误区）
        
### 3-3 启动优化工具选择
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    