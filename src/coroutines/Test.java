package coroutines;

public class Test {
    public static void main(String[] args) {
        KThreadRepKt.startCor(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    System.out.println("whts up");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
