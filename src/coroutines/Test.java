package coroutines;

import java.util.concurrent.CancellationException;

public class Test {
    public static void main(String[] args) {
        var t = KThreadRepKt.startCor(new Runnable() {
            @Override
            public void run() {
                    System.out.println("whts up");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        });
        KThreadRepKt.startCor(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    System.out.println("hello");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.cancel(new CancellationException());
        try {
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
