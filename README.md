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

        一个FutureTask新建出来，state就是NEW状态；COMPETING和INTERRUPTING用的进行时，表示瞬时状态
            ，存在时间极短(为什么要设立这种状态？？？不解)；NORMAL代表顺利完成；EXCEPTIONAL代表执行过程出现异常；
            CANCELED代表执行过程被取消；INTERRUPTED被中断

        2）执行过程顺利完成：NEW -> COMPLETING -> NORMAL

        3）执行过程出现异常：NEW -> COMPLETING -> EXCEPTIONAL

        4）执行过程被取消：NEW -> CANCELLED

        5）执行过程中，线程中断：NEW -> INTERRUPTING -> INTERRUPTED
三. 设计模式

    1.桥接模式(Bridge pattern)
        桥接模式的目的是把变化的部分抽象出来，使得变化部分与主类分离开来，从而将多个维度的变化彻底分离。
        桥接模式常常应用于多个维度变化的类（即一个类变化的原因多于1个）。
    2.装饰者模式(decorator pattern)
        装饰者模式通过动态的给一个类添加一些额外的职责（就像给他添加一些装饰品一样），
        使用Decorator比使用子类的方式要更灵活一些
      优点:
        把类中的装饰功能从类中搬除，可以简化原来的类
        可以把类的 核心职责和装饰功能区分开来，结构清晰 明了并且可以去除相关类的重复的装饰逻辑
    3.代理模式(Proxy pattern)
        代理模式一般是用来进行远程调用的。比如客户端需要远程调用服务器的功能，可以使用代理模式将所需要的功能让代理传递给我们的客户端，
        通过调用这个代理的函数，看上去就像直接调用这个服务器的功能一样。这种常见的代理模式的应用有RMI。 4.责任链模式(Chain of responsibility pattern)
        责任链模式是一种对象的行为模式。在责任链模式里，很多对象由每一个对象对其下家的引用而连接起来形成一条链。
        请求在这个链上传递，直到链上的某一个对象决定处理此请求。发出这个请求的客户端并不知道链上的哪一个对象最终
        处理这个请求，这使得系统可以在不影响客户端的情况下动态地重新组织和分配责任。

        在以下条件下可考虑使用责任链模式：

        1 有多个对象可以处理一个请求，哪个对象处理该请求因条件而定。

        2 可处理一个请求的对象集合应该被动态指定。
    4.命令模式(command pattern)
        将请求封装成对象，这可以让你使用不同的队列，请求，或者日志请求来参数化其他对象。命令模式也支持撤销操作
            命令模式包括：
            (1）命令接口或者命令抽象类（Command）:定义命令类所需要的方法

           （2）具体命令类（***Command）：抽象命令的具体实现，里面包含了命令的接收者，用于执行真正的命令

           （3）命令请求者（invoker）：请求命令，让具体接受者（receiver）执行

           （4）命令接受者（receiver）：具体命令的执行者

           （5）客户端类（Client）:用于分配命令以及命令的执行者
    5.工厂模式(factory pattern)
        工厂方法模式：
        一个抽象产品类，可以派生出多个具体产品类。
        一个抽象工厂类，可以派生出多个具体工厂类。
        每个具体工厂类只能创建一个具体产品类的实例。

        抽象工厂模式：
        多个抽象产品类，每个抽象产品类可以派生出多个具体产品类。
        一个抽象工厂类，可以派生出多个具体工厂类。
        每个具体工厂类可以创建多个具体产品类的实例，也就是创建的是一个产品线下的多个产品。

        区别：
        工厂方法模式只有一个抽象产品类，而抽象工厂模式有多个。
        工厂方法模式的具体工厂类只能创建一个具体产品类的实例，而抽象工厂模式可以创建多个。
        工厂方法创建 "一种" 产品，他的着重点在于"怎么创建"，也就是说如果你开发，你的大量代码很可能围绕着这种产品的构造，
        初始化这些细节上面。也因为如此，类似的产品之间有很多可以复用的特征，所以会和模版方法相随。

        抽象工厂需要创建一些列产品，着重点在于"创建哪些"产品上，也就是说，如果你开发，你的主要任务是划分不同差异的产品线，
        并且尽量保持每条产品线接口一致，从而可以从同一个抽象工厂继承。

        对于java来说，你能见到的大部分抽象工厂模式都是这样的：
        ---它的里面是一堆工厂方法，每个工厂方法返回某种类型的对象。

        比如说工厂可以生产鼠标和键盘。那么抽象工厂的实现类（它的某个具体子类）的对象都可以生产鼠标和键盘，但可能工厂A生产的是罗技的键盘和鼠标，工厂B是微软的。

        这样A和B就是工厂，对应于抽象工厂；
        每个工厂生产的鼠标和键盘就是产品，对应于工厂方法；

        用了工厂方法模式，你替换生成键盘的工厂方法，就可以把键盘从罗技换到微软。但是用了抽象工厂模式，你只要换家工厂，就可以同时替换鼠标和键盘一套。如果你要的产品有几十个，当然用抽象工厂模式一次替换全部最方便（这个工厂会替你用相应的工厂方法）

        所以说抽象工厂就像工厂，而工厂方法则像是工厂的一种产品生产线
    6.适配器模式(adapter pattern)

        适配器模式将某个类的接口转换成客户端期望的另一个接口表示，目的是消除由于接口不匹配所造成的类的兼容性问题。主要分为三类：类的适配器模式、对象的适配器模式、接口的适配器模式。

        总结一下三种适配器模式的应用场景：

        类的适配器模式：当希望将一个类转换成满足另一个新接口的类时，可以使用类的适配器模式，创建一个新类，继承原有的类，实现新的接口即可。

        对象的适配器模式：当希望将一个对象转换成满足另一个新接口的对象时，可以创建一个Wrapper类，持有原类的一个实例，在Wrapper类的方法中，调用实例的方法就行。

        接口的适配器模式：当不希望实现一个接口中所有的方法时，可以创建一个抽象类Wrapper，实现所有方法，我们写别的类的时候，继承抽象类即可。
    7.外观模式(facade pattern)
        外观模式是为了解决类与类之家的依赖关系的，像spring一样，可以将类和类之间的关系配置到配置文件中，而外观模式就是将他们的关系放在一个Facade类中，降低了类类之间的耦合度，
        如果我们没有Computer类，那么，CPU、Memory、Disk他们之间将会相互持有实例，产生关系，这样会造成严重的依赖，修改一个类，可能会带来其他类的修改，这不是我们想要看到的，
        有了Computer类，他们之间的关系被放在了Computer类里，这样就起到了解耦的作用，这，就是外观模式！