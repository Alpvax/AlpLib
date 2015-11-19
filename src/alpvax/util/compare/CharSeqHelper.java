package alpvax.util.compare;

/**
 * Class to enable easy in-line concatenation of any CharSequence.
 * Also allows easy comparing with any other CharSequence (Including using hashCodes).
 * @author NIH
 */
public class CharSeqHelper implements CharSequence, Comparable<CharSequence>
{
	private StringBuilder s;
	
	public CharSeqHelper(CharSequence... charSequences)
	{
		s = new StringBuilder();
		for(CharSequence c : charSequences)
		{
			s.append(c);
		}
	}

	@Override
	public int length()
	{
		return s.length();
	}

	@Override
	public char charAt(int index)
	{
		return s.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		return s.subSequence(start, end);
	}
	
	@Override
	public String toString()
	{
		return s.toString();
	}
	
	public StringBuilder getStringBuilder()
	{
		return s;
	}

	@Override
	public boolean equals(Object other)
	{
		return other != null && other instanceof CharSequence ? toString().equals(other.toString()) : false;
	}
	public boolean equalsIgnoreCase(CharSequence other)
	{
		return other != null ? toString().equalsIgnoreCase(other.toString()) : false;
	}
	
	@Override
    public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public int compareTo(CharSequence other)
	{
		return s.toString().compareTo(other.toString());
	}
}
