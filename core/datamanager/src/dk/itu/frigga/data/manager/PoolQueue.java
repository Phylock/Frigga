package dk.itu.frigga.data.manager;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class PoolQueue<T> {

  private final BlockingQueue<T> objects;

  public PoolQueue(Collection<? extends T> objects) {
    this.objects = new ArrayBlockingQueue<T>(objects.size(), false, objects);
  }

  public T request() throws InterruptedException {
    return this.objects.take();
  }

  /**
   * Request an object, with a timeout
   * @param timeout in milliseconds
   * @return the requested object
   * @throws InterruptedException
   */
  public T request(long timeout) throws InterruptedException {
    return this.objects.poll(timeout, TimeUnit.MILLISECONDS);
  }

  public void release(T object) throws InterruptedException {
    if (object != null) {
      this.objects.put(object);
    }
  }

  public void add(T object) throws InterruptedException {
    this.objects.offer(object);
  }

  public int poolSize() {
    return objects.size();
  }
}
