package alpvax.common.util.generics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ArrayList<T> that allows adding of T, T[] or Collection<T> items, or a mixture.
 * (Including  e.g. C<T[][]>) Using a "Depth First" recursive algorithm.
 * 
 * @author NIH
 *
 * @param <T> Type of List element
 */
public class ArrayListWrapper<T> extends ArrayList<T>
{
	private static final long serialVersionUID = -8957323219423628364L;
	
	private final Class<T> typeClass;

	public ArrayListWrapper(Class<T> type, Object... objects)
	{
		super();
		typeClass = type;
		addAll(objects);
	}

	/**
     * Inserts all of the elements in objects into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are discovered using a recursive "depth first" algorithm
     *
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param objects elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     */
	public boolean convertAndAdd(int index, Object... objects)
	{
		return super.addAll(getList(objects));
	}
	/*public boolean convertAndAdd(int index, Object... objects)
	{
		boolean flag = false;
		for(Object obj : objects)
		{
			if(addSingleT(index, obj))
			{
				flag = true;
			}
			else
			{
				if(obj instanceof Object[])
				{
					convertAndAdd(index, (Object[])obj);
				}
				if(obj instanceof Collection)
				{
					convertAndAdd(index, (Collection<?>)obj);
				}
			}
		}
		return flag;
	}
	
	private boolean addSingleT(int i, Object e)
	{
		if(typeClass.isInstance(e))
		{
			return super.add(typeClass.cast(e));
		}
		return false;
	}*/

	@Override
    public boolean add(Object e)
	{
		return convertAndAdd(size(), e);
	}
	
	@Override
    public void add(int index, Object element)
	{
		convertAndAdd(index, element);
	}
	
	@Override
    public boolean addAll(Collection<? extends T> c)
    {
		return convertAndAdd(size(), c);
    }
    
	@Override
    public boolean addAll(int index, Collection<? extends T> c)
    {
		return convertAndAdd(index, c);
    }

    public boolean addAll(Object... objects)
    {
    	return convertAndAdd(size(), objects);
    }
    
    @Override
    public boolean remove(Object o)
    {
    	return removeAll(o);
    }
    
    @Override
    public boolean removeAll(Collection<?> c)
    {
    	return removeAll(c);
    }

    public boolean removeAll(Object... objects)
    {
    	return super.removeAll(getList(objects));
    }
    
    @Override
    public boolean retainAll(Collection<?> c)
    {
    	return retainAll(c);
    }

    public boolean retainAll(Object... objects)
    {
    	return super.retainAll(getList(objects));
    }
    
    /**
     * Recursive method to return an ArrayList<T> from objects
     */
    private ArrayList<T> getList(Object... objects)
    {
    	ArrayList<T> l = new ArrayList<T>();
		for(Object obj : objects)
		{
			if(typeClass.isInstance(obj))
			{
				l.add(typeClass.cast(obj));
			}
			else
			{
				if(obj instanceof Object[])
				{
					l.addAll(getList((Object[])obj));
				}
				if(obj instanceof Collection)
				{
					l.addAll(getList(((Collection<?>)obj).toArray()));
				}
			}
		}
		return l;
    }
    
    @Override
    public T[] toArray()
    {
    	@SuppressWarnings("unchecked")
		T[] empty = (T[])Array.newInstance(typeClass, 0);
		return super.toArray(empty);
    }
}
