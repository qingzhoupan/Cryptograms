import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

public class ArrayMap<K, V> extends AbstractMap<K,V>{
	private int size;
	private Object[] keyArray = new Object[0];
	private Object[] valArray = new Object[0];

	/**
	 * This method adds key and value to your map. If key already exists,
	 *  the new value replaces the old one, and the old one is returned.
	 * @param key key value
	 * @param value value value
	 * @return old value
	 */
	@Override
	public V put(K key, V value) {
		for (int i = 0; i < size; i++) {
			if(keyArray[i].equals(key)) {
				Object oldValue = valArray[i];
				valArray[i] = value;
				return (V) oldValue;
			}
		}

		Object[] newKeyArray = new Object[keyArray.length + 1];
		System.arraycopy(keyArray, 0, newKeyArray, 0, keyArray.length);
		keyArray = newKeyArray;

		Object[] newValArray = new Object[valArray.length + 1];
		System.arraycopy(valArray, 0, newValArray, 0, valArray.length);
		valArray = newValArray;

		keyArray[size] = key;
		valArray[size] = value;
		size++;	
		return value;
	}
	
	/**
	 * This method returns the number of mappings that the object contains.
	 * @return the size number
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Returns a Set of key, value pairs contained in an Entry object
	 * @return the set
	 */
	@Override
	public Set<Entry<K, V>> entrySet(){
		return new ArrayMapEntrySet();
	}

	private class ArrayMapEntrySet extends AbstractSet<Entry<K,V>>{

		/**
		 * This method returns the size of the set (and of the Map).
		 * @return the size number
		 */
		@Override
		public int size() {
			return size;
		}
		
		/**
		 * This method should return true if the Set contains
		 *  an Entry equal to the the one represented by the parameter.
		 * @param o an object
		 * @return whether the arraymap contains the object
		 */
		@Override
		public boolean contains(Object o) {
			if (o instanceof Entry<?, ?>) {
				Entry<?, ?> e = (Entry<?, ?>) o;
				Object key = e.getKey();
				Object value = e.getValue();

				for (int i = 0; i < size; i++) {
					if (keyArray[i].equals(key)) {
						if (valArray[i].equals(value)) {
							return true;
						}
					}
				}
			}
			return false;
		}

		/**
		 * This returns an iterator that walks over the Set of Entries in the Map.
		 * @return the iterator object
		 */
		@Override
		public Iterator<Entry<K,V>> iterator() {
			return new ArrayMapEntrySetIterator<>();
		}

		private class ArrayMapEntrySetIterator<T> implements Iterator<T>{
			private int index = 0;
			private Object curr;

			/**
			 * Returns true if there are more items in the Set of Entries being iterated over.
			 * @return whether the collection has the next value
			 */
			@Override
			public boolean hasNext() {
				return index < size;
			}

			/**
			 * Returns an Entry (an AbstractMap.SimpleEntry<V,E> for us) that represents the next mapping in our Map.
			 * @return the entry object in current index
			 */
			@Override
			public T next() {
				Object key = keyArray[index];
				Object value = valArray[index];
				curr = key;
				index++;
				return (T) new AbstractMap.SimpleEntry<K, V>((K) key, (V) value);
			}
			
			/**
			 * Removes from the underlying collection the last element returned by this iterator (optional operation).
			 */
			@Override
			public void remove() {
			    if(curr == null) {
			     throw new IllegalStateException();
			    }
			    Object[] tempKeyArray = new Object[keyArray.length - 1];
			    Object[] tempValArray = new Object[valArray.length - 1];
			    System.arraycopy(keyArray, index, keyArray, index - 1, keyArray.length - index);
			    System.arraycopy(valArray, index, valArray, index - 1, valArray.length - index);
			    
			    System.arraycopy(keyArray, 0, tempKeyArray, 0, keyArray.length - 1);
			    System.arraycopy(valArray, 0, tempValArray, 0, valArray.length - 1);
			    keyArray = tempKeyArray;
			    valArray = tempValArray;
			    curr = null;
			    size--;
			   }
		}
		
	}
}
