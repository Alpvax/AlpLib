package alpvax.util.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unchecked")
public class WeightedRandomList<T>
{
	private int totalWeight = 0;
	public List<WeightEntry> list = new ArrayList<WeightEntry>();

	public <S extends WeightedRandomList<T>> S addWeightedItem(int weight, T object)
	{
		list.add(new WeightEntry(totalWeight, totalWeight += weight, object));
		return (S)this;
	}
	public <S extends WeightedRandomList<T>> S addWeightedItems(Object... obj)
	{
		for(int i = 0, j = 1; j < obj.length; j = (i += 2) + 1)
		{
			int weight = 0;
			T object = null;
			boolean flag = true;
			try
			{
				weight = (Integer)obj[i];
			}
			catch(ClassCastException e)
			{
				System.out.println("Cannot add non-integer weighted item.");
				flag = false;
			}
			try
			{
				object = (T)obj[j];
			}
			catch(ClassCastException e)
			{
				System.out.println("Cannot add weighted item of type " + obj[j].getClass());
				flag = false;
			}
			if(flag)
			{
				addWeightedItem(weight, object);
			}
		}
		return (S)this;
	}
	public <S extends WeightedRandomList<T>> S addWeightedItems(Object[]... obj)
	{
		for(Object[] o : obj)
		{
			int weight = 0;
			T object = null;
			boolean flag = true;
			try
			{
				weight = (Integer)o[0];
			}
			catch(ClassCastException e)
			{
				System.out.println("Cannot add non-integer weighted item.");
				flag = false;
			}
			try
			{
				object = (T)o[1];
			}
			catch(ClassCastException e)
			{
				System.out.println("Cannot add weighted item of type " + obj[1].getClass());
				flag = false;
			}
			if(flag)
			{
				addWeightedItem(weight, object);
			}
		}
		return (S)this;
	}
	
	public T getRandomObject(Random rand)
	{
		if(!list.isEmpty())
		{
			int r = rand.nextInt(totalWeight);
			Iterator<WeightEntry> i = list.iterator();
			while(i.hasNext())
			{
				WeightEntry e = i.next();
				if(e.inRange(r))
				{
					return e.getObject();
				}
			}
		}
		return null;
	}
	
	private class WeightEntry
	{
		private int min;
		private int max;
		private T obj;
		
		public WeightEntry(int min, int max, T object)
		{
			this.min = min;
			this.max = max;
			this.obj = object;
		}
		
		public boolean inRange(int i)
		{
			return i >= min && i < max;
		}
		
		public T getObject()
		{
			return obj;
		}
	}
}
