# pongswallow

[介绍](#介绍)

[introduction](#introduction)

[contact](#contact)


## 介绍

+ 一种在可用的程度上部分替代目前主流的线程模型实现的线程池优化实现。

  初步的目标是在udsPong替代disruptor

+ 通过排序算法获取灵感

  基础的排序算法有这样几类，交换、归并、选择，插入。

  而为了应对更具特点的数据，目前的排序算法比如22年的glidesort 以及它参考的pdqsort

  都有根据数据特点划分不同的排序task，排序算法的数据称之为run

  我借鉴了run这样的概念，通过手动讲复杂任务分解成为可在多个run同时执行的函数，

  配合队列概念，实现有限线程数下的效率提升。

+ 核心伪代码

  ```java
  while(worker = queue.poll() != null) {

     runAsync(woker.work())

  ​        .whenComplete((cworker)=>{

  ​          if(cworker.notdone()){

  ​             queue.push(worker)

  ​         }

  ​       })

  ​    }
  ```



+ worker & run

  worker 是执行的主体，每次work都会调用一个或多个的run，

  而如何划分run的batch就是需要手动设计的要点



## introduction

+ A thread pool optimization implementation that partially replaces the current mainstream threading model implementation to the extent available.

  The initial goal is to replace disruptor in udsPong

+ Get inspired by sorting algorithms

  The basic sorting algorithms have the following categories, exchange, merge, selection, insertion.

  In order to cope with more characteristic data, current sorting algorithms such as 22 years of glidesort and its reference PDQSORT

  There are different sorting tasks according to the characteristics of the data, and the data of the sorting algorithm is called run

  I borrowed the concept of run, by manually decomposing complex tasks into functions that can be executed simultaneously by multiple runs.

  With the concept of queuing, efficiency improvement under a limited number of threads is realized.

+ Core pseudocode

  ```java
  while(worker = queue.poll() != null) {

     runAsync(woker.work())

  ​        .whenComplete((cworker)=>{

  ​          if(cworker.notdone()){

  ​             queue.push(worker)

  ​         }

  ​       })

  ​    }
  ```

## contact
> realcpf@163.com


