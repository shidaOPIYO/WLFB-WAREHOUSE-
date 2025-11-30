import java.util.concurrent.locks.ReentrantLock;


//Thread-safe warehouse state for apples and oranges.
// All operations that read or modify state acquire lock.
 
public class kkstate {
  private int apples;
  private int oranges;
  private final ReentrantLock lock = new ReentrantLock(true); // fair lock

 
 //Construct of warehouse with initial stock.
  
  public kkstate(int initialApples, int initialOranges) {
    if (initialApples < 0 || initialOranges < 0) {
      throw new IllegalArgumentException("Initial stock must be non-negative");
    }
    this.apples = initialApples;
    this.oranges = initialOranges;
  }

  
 //Return value of apples and oranges.
  
  public int[] checkStock() {
    lock.lock();
    try {
      return new int[] { apples, oranges };
    } finally {
      lock.unlock();
    }
  }

 
//Buy n number apples if available.
  
  public boolean buyApples(int n) {
    if (n <= 0) return false;
    lock.lock();
    try {
      if (apples >= n) {
        apples -= n;
        return true;
      } else {
        return false;
      }
    } finally {
      lock.unlock();
    }
  }

 
 //Buy n number of oranges if available.
  
  public boolean buyOranges(int n) {
    if (n <= 0) return false;
    lock.lock();
    try {
      if (oranges >= n) {
        oranges -= n;
        return true;
      } else {
        return false;
      }
    } finally {
      lock.unlock();
    }
  }

 
 //Supplier adds n number of apples.
  
  public boolean addApples(int n) {
    if (n <= 0) return false;
    lock.lock();
    try {
      apples += n;
      return true;
    } finally {
      lock.unlock();
    }
  }

  
//Supplier adds n number of oranges.
  
  public boolean addOranges(int n) {
    if (n <= 0) return false;
    lock.lock();
    try {
      oranges += n;
      return true;
    } finally {
      lock.unlock();
    }
  }

  /**
   * For logging:  string "apples oranges".
   */
  public String stockString() {
    lock.lock();
    try {
      return "apples:" + apples + " " + "oranges:" + oranges;
    } finally {
      lock.unlock();
    }
  }
}