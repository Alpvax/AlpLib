package alpvax.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alpvax.common.util.generics.AlpMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class FileHelper
{
	public static final String newLine = String.format("%n");

	public static AlpMap<Object> readJsonFile(InputStream in)
	{
		return readJsonFile(in, Object.class);
	}
	public static <T> AlpMap<T> readJsonFile(InputStream in, Class<T> type, Object typeAdaptor)
	{
		AlpMap<T> map = new AlpMap<T>();
		Gson gson = new GsonBuilder().registerTypeAdapter(type, typeAdaptor).create();
		JsonParser parser = new JsonParser();
		try
		{
			JsonElement root = parser.parse(new InputStreamReader(in));
			Map<String, Object> m = gson.fromJson(root, new TypeToken<HashMap<String, T>>(){}.getType());
			if(m != null)
			{
				for(Map.Entry<String, Object> entry : m.entrySet())
				{
					if(type.isInstance(entry.getValue()))
					{
						map.put(entry.getKey(), type.cast(entry.getValue()));
					}
				}
				//map.putAll(m);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}
	public static <T> AlpMap<T> readJsonFile(InputStream in, Class<T> type)
	{
		AlpMap<T> map = new AlpMap<T>();
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		try
		{
			JsonElement root = parser.parse(new InputStreamReader(in));
			Map<String, Object> m = gson.fromJson(root, new TypeToken<HashMap<String, T>>(){}.getType());
			if(m != null)
			{
				for(Map.Entry<String, Object> entry : m.entrySet())
				{
					if(type.isInstance(entry.getValue()))
					{
						map.put(entry.getKey(), type.cast(entry.getValue()));
					}
				}
				//map.putAll(m);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}
	
	public static <T, U> void writeJsonFile(File file, Map<T, U> map, Object[]... typeAdaptors)
	{
		String s = writeJson(map, typeAdaptors);
		writeStringToFile(s, file);
	}
	
	public static <T, U> void writeJsonFileWithComment(String comment, File file, Map<T, U> map, Object[]... typeAdaptors)
	{
		StringBuilder s = new StringBuilder(comment.toString());
		writeStringToFile(s.append(writeJson(map, typeAdaptors)).toString(), file);
	}
	
	public static <T, U> String writeJson(Map<T, U> map, Object[]... typeAdaptors)
	{
		GsonBuilder gb = new GsonBuilder().setPrettyPrinting();
		for(Object[] o : typeAdaptors)
		{
			if(o.length >= 2)
			{
				gb.registerTypeAdapter((Type)o[0], o[1]);
			}
		}
		Gson gson = gb.create();
		return gson.toJson(map);
	}
	
	public static void writeStringToFile(String s, File file)
	{
		makeFile(file, false);
		try
		{
			BufferedWriter b = new BufferedWriter(new FileWriter(file));
			b.write(s);
			b.close();
		}
		catch(IOException e)
		{
			//only possible if file hasn't been created
			e.printStackTrace();
		}
	}

	public static String[] stripComments(String[] lines, String... identifiers)
	{
		List<String> list = new ArrayList<String>();
		for(String line : lines)
		{
			for(String s : identifiers)
			{
				if(line.contains(s))
				{
					line = line.substring(0, line.indexOf(s));
				}
			}
			if(line.length() > 0)
			{
				list.add(line.trim());
			}
		}
		return list.toArray(new String[0]);
	}
	
	/**
	 * WARNING! If file is found but is wrong type (directory) it WILL be deleted.
	 */
	public static boolean makeFile(File file, boolean directory)
	{
		if(file.exists())
		{
			if(file.isDirectory() != directory)
			{
				file.delete();
			}
			else
			{
				return false;
			}
		}
		if(!file.exists())
		{
			File f = directory ? file : file.getParentFile();
			if(f != null && !f.exists())
			{
				f.mkdirs();
			}
			if(!directory)
			{
				try
				{
					file.createNewFile();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public static void copyTo(Object source, File output) throws IllegalArgumentException, IOException
	{
		InputStream in = getInputStream(source);
		FileOutputStream fos = new FileOutputStream(output);
		byte [] buf = new byte[1024];
        int len;
        while((len = in.read(buf)) > 0)
        {            
            fos.write(buf, 0, len);
        }
        fos.close();
        in.close();
	}

	public static InputStream getInputStream(Object arg) throws IllegalArgumentException, IOException
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
		throw new IllegalArgumentException(String.format("Unable to produce an InputStream from class: %s", arg.getClass()));//return null;
	}
}
