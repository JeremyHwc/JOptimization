# 线程优化

## Android线程调度原理剖析

    线程调度的原理
        .任意时刻，只有一个线程占用CPU,处于运行状态
        .多线程并发：轮流获取CPU使用权
        .JVM负责线程调度：按照特定机制分配CPU使用权
    线程调度模型
        .轮圈获取、均分CPU时间
        .抢占式调度模型：优先级高的获取，JVM采用
        
    Android线程调度
        .nice值
            .Process中定义
            .值越小，优先级越高
            .默认THREAD_PRIORITY_DEFAULT,0
        .cgroup