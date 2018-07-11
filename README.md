一. CountDownLatch和CyclicBarrier的比较

        1.CountDownLatch是线程组之间的等待，即一个(或多个)线程等待N个线程完成某件事情之后再执行；
          而CyclicBarrier则是线程组内的等待，即每个线程相互等待，即N个线程都被拦截之后，然后依次执行。

        2.CountDownLatch是减计数方式，而CyclicBarrier是加计数方式。

        3.CountDownLatch计数为0无法重置，而CyclicBarrier计数达到初始值，则可以重置。

        4.CountDownLatch不可以复用，而CyclicBarrier可以复用
二. Future

        Future的核心思想是：一个方法f，计算过程可能非常耗时，等待f返回，显然不明智。
        可以在调用f的时候，立马返回一个Future，可以通过Future这个数据结构去控制方法f的计算过程。
        get方法：获取计算结果（如果还没计算完，也是必须等待的）

        cancel方法：还没计算完，可以取消计算过程

        isDone方法：判断是否计算完

        isCancelled方法：判断计算是否被取消

        一个FutureTask新建出来，state就是NEW状态；COMPETING和INTERRUPTING用的进行时，表示瞬时状态，存在时间极短(为什么要设立这种状态？？？不解)；NORMAL代表顺利完成；EXCEPTIONAL代表执行过程出现异常；CANCELED代表执行过程被取消；INTERRUPTED被中断

        2）执行过程顺利完成：NEW -> COMPLETING -> NORMAL

        3）执行过程出现异常：NEW -> COMPLETING -> EXCEPTIONAL

        4）执行过程被取消：NEW -> CANCELLED

        5）执行过程中，线程中断：NEW -> INTERRUPTING -> INTERRUPTED