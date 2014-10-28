package alpvax.common.util;

public class Version implements Comparable<Version>
{
	private int major;
	private int minor;

	public Version(String s)
	{
		String[] sar = s.split("\\.");
		major = Integer.parseInt(sar[0]);
		minor = Integer.parseInt(sar[1]);
	}
	public Version(Double d)
	{
		this(Double.toString(d != null ? d : 0.0D));
	}
	
	public double increment()
	{
		minor++;
		return toDouble();
	}
	
	public double getIncremented()
	{
		return new Version(toDouble()).increment();
	}
	
	public double incrementMajor()
	{
		major++;
		minor = 0;
		return toDouble();
	}

	/*public double toDouble()
	{
		return toDoubleObj();
	}*/
	public Double toDouble()//Obj()
	{
		return Double.parseDouble(toString());
	}
	
	@Override
	public String toString()
	{
		return major + "." + minor;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other == null || !((other instanceof Version) || (other instanceof Double)))
		{
			return false;
		}
		Double d = toDouble();
		if(other instanceof Version)
		{
			return d.equals(((Version)other).toDouble());
		}
		else
		{
			return d.equals((Double)other);
		}
	}
	
	@Override
	public int hashCode()
	{
		return toDouble().hashCode();
	}
	
	@Override
	public int compareTo(Version v)
	{
		if(v == null)
		{
			return 1;
		}
		int i = major - v.major;
		if(i == 0)
		{
			i = minor - v.minor;
		}
		return i;
	}
	
	
	public static Version get(Object obj)
	{
		if(obj instanceof String)
		{
			return new Version((String)obj);
		}
		if(obj instanceof Double)
		{
			return new Version((Double)obj);
		}
		return null;
	}

	public static int compare(Object v1, Object v2)
	{
		return get(v1).compareTo(get(v2));
	}
}
