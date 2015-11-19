package alpvax.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class StreamComparator
{
	private int bufferSize = 1024;
	private boolean useStrings = false;
	private boolean ignoreCase = false;
	private boolean ignoreWhite = false;

	public StreamComparator setBufferSize(int size)
	{
		bufferSize = size;
		return this;
	}
	public StreamComparator useStringComparison(boolean flag)
	{
		useStrings = flag;
		return this;
	}
	public StreamComparator setIgnoreCase(boolean flag)
	{
		ignoreCase = flag;
		return this;
	}
	public StreamComparator setIgnoreWhitespace(boolean flag)
	{
		ignoreWhite = flag;
		return this;
	}
	private boolean useStrings()
	{
		return useStrings || ignoreCase || ignoreWhite;
	}

	/**
	 * 3 parameters to guarantee there are at least 2 streams to compare
	 */
	public boolean compareAll(Object obj1, Object obj2, Object...objects) throws IOException
	{
		if(obj1 == null || obj2 == null)
		{
			return obj1.equals(obj2);
		}
		InputStream[] in = new InputStream[objects.length + 2];
		in[0] = getInputStream(obj1);
		in[1] = getInputStream(obj2);
		for(int i = 0; i < objects.length; i++)
		{
			in[i + 2] = getInputStream(objects[i]);
		}
        byte[][] buf = new byte[in.length][];
        do
        {
            buf[0] = readBuffer(in[0]);
            boolean flag = buf[0] != null;
	        for(int i = 1; i < in.length; i++)
	        {
	        	buf[i] = readBuffer(in[i]);
        		if(flag == (buf[i] == null) || (!ignoreWhite && flag && buf[0].length != buf[i].length))
        		{
        			return false;
        		}
	        	if(flag)
	        	{
	        		if(useStrings())
	        		{
	        			String s0 = new String(buf[0]);//Uses System default charset, as
	        			String si = new String(buf[i]);// it is for comparison purposes.
	        			if(ignoreWhite)
	        			{
	        				s0 = s0.replaceAll("\\s", "");
	        				si = si.replaceAll("\\s", "");
	        			}
	        			if((ignoreCase && !s0.equalsIgnoreCase(si)) || (!ignoreCase && !s0.equals(si)))
	        			{
	        				return false;
	        			}
	        		}
	        		else
	        		{
	        			for(int j = 0; j < buf[0].length; j++)
			        	{
			        		if(buf[0][j] != buf[i][j])
			        		{
			        			return false;
			        		}
			        	}
	        		}
	        	}
	        }	
        }while(buf[0] != null);
		return true;
	}

	private byte[] readBuffer(InputStream data) throws IOException
	{
        byte[] buf = new byte[bufferSize];
        int len = data.read(buf);
        return len > 0 ? Arrays.copyOfRange(buf, 0, len) : null;
	}
	
	private InputStream getInputStream(Object arg) throws IOException
	{
		if(arg instanceof File)
		{
			return new FileInputStream((File)arg);
		}
		if(arg instanceof String)
		{
			return new FileInputStream((String)arg);
		}
		if(arg instanceof URL)
		{
			return ((URL)arg).openStream();
		}
		if(arg instanceof InputStream)
		{
			return (InputStream)arg;
		}
		return null;
	}
}