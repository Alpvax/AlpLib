package alpvax.util.compare;

import java.util.Comparator;
import java.util.Map;

public abstract class MapValueComparator<K,V> implements Comparator<K>
{
    Map<K, V> map;
    
    private MapValueComparator(Map<K, V> base)
    {
    	map = base;
    }

    public static <K, V> MapValueComparator<K,V> get(Map<K, V> base, Comparator<V> comparator)
    {
    	return new MVCComparator<K, V>(base, comparator);
    }
    
    public static <K, V extends Comparable<V>> MapValueComparator<K,V> get(Map<K, V> base)
    {
    	return new MVCClass<K, V>(base);
    }
    
    private static class MVCComparator<K, V> extends MapValueComparator<K, V>
    {
	    Comparator<V> comp;
	    public MVCComparator(Map<K, V> base, Comparator<V> comparator)
	    {
	        super(base);
	        comp = comparator;
	    }

		@Override
		public int compare(K o1, K o2)
		{
			return comp.compare(map.get(o1), map.get(o2));
		}
	};
    
    private static class MVCClass<K, V extends Comparable<V>> extends MapValueComparator<K, V>
    {
	    public MVCClass(Map<K, V> base)
	    {
	        super(base);
	    }

		@Override
		public int compare(K o1, K o2)
		{
			return map.get(o1).compareTo(map.get(o2));
		}
	};
}
