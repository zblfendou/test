import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureCook {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        Callable<Chuju> onlineShopping = () -> {
            System.out.println("网购厨具下单");
            System.out.println("等待送货");
            Thread.sleep(5000);  // 模拟送货时间
            System.out.println("快递送到");
            return new Chuju();
        };

        FutureTask<Chuju> task = new FutureTask<>(onlineShopping);
        new Thread(task).start();

        Callable<Shicai> shicaiCallable = () -> {
            System.out.println("第二步 去超市购买食材");
            Thread.sleep(3000);//模拟购买食材时间
            System.out.println("第二步: 食材到位");
            return new Shicai();
        };
        FutureTask<Shicai> shicaiFutureTask = new FutureTask<>(shicaiCallable);
        new Thread(shicaiFutureTask).start();

        //第三步 用厨具烹饪食材
        if (!task.isDone() || !shicaiFutureTask.isDone()) {
            // 联系快递员，询问是否到货
            System.out.println("厨具或者食材没准备好，心情好就等着（心情不好就调用cancel方法取消订单）");
        }

        cook(task.get(), shicaiFutureTask.get());

        System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");
    }


    static void cook(Chuju chuju, Shicai shicai) {
        System.out.println("第三步：厨具到位，开始展现厨艺");

    }

    static class Chuju {
    }

    static class Shicai {
    }
}
