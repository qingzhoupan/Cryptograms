import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

public class MyHashMapTest {
    @Test
    public void test1() {
        ArrayMap<Integer, Integer> m = new ArrayMap<Integer, Integer>();
        m.put(1,00);
        m.put(1,11);
        m.put(2,22);
        assertTrue(m.size() == 2);
    }
    
    
    @Test
    public void test2() {
        ArrayMap<Integer, Integer> m = new ArrayMap<Integer, Integer>();
        m.put(1,00);
        m.put(1,11);
        m.put(2,22);
        Set<Entry<Integer, Integer>> eSet = m.entrySet();
        eSet.size();
        Iterator<Entry<Integer, Integer>> i = eSet.iterator();
        i.hasNext();
        Entry<Integer, Integer> e1 = i.next();
        eSet.contains(e1);
        Entry<Integer, Integer> e2 = i.next();
        i.remove();
        eSet.contains(e2);
        i.hasNext();
        i.next();
        
    }
    
    @Test
    public void test3() {
        ArrayMap<Integer, Integer> m = new ArrayMap<Integer, Integer>();
        m.put(1,00);
        m.put(1,11);
        m.put(2,22);
        Set<Entry<Integer, Integer>> eSet = m.entrySet();
        eSet.size();
        Iterator<Entry<Integer, Integer>> i = eSet.iterator();
        Entry<Integer, Integer> e = i.next();
        i.remove();
        i.remove();
        
    }
}
