package ThreadLearning;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    private  static ArrayList<Integer> arrayList = new ArrayList<Integer>();
    static Lock lock = new ReentrantLock(); 
    public static void main(String[] args)  {
        final Test test = new Test();
         
        new Thread(){
            public void run() {
                insert(Thread.currentThread());
            };
        }.start();
         
        new Thread(){
            public void run() {
                update(Thread.currentThread());
            };
        }.start();
    }  
     
    public static void insert(Thread thread) {
           //注意这个地方
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁");
            for(int i=0;i<5;i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            System.out.println(thread.getName()+"释放了锁");
        
            //lock.unlock();
        }
    }
    public static void update(Thread thread) {
        //注意这个地方
       lock.lock();
       try {
           System.out.println(thread.getName()+"得到了锁");
           for(int i=0;i<5;i++) {
               arrayList.add(i);
           }
       } catch (Exception e) {
           // TODO: handle exception
       }finally {
           System.out.println(thread.getName()+"释放了锁");
       
           lock.unlock();
       }
    }
}