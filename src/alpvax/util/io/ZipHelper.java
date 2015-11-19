package alpvax.util.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class ZipHelper implements java.io.Closeable
{
	private FileSystem fileSystem;
	
	public ZipHelper(File zip) throws IOException
	{
		Map<String, String> env = new HashMap<String, String>(); 
		env.put("create", "true");
		fileSystem = FileSystems.newFileSystem(URI.create("jar:" + Paths.get(zip.toURI()).toUri()), env);
	}
	public ZipHelper(String zip) throws IOException
	{
		this(new File(zip));
	}
	
	public void addReplace(String zipPath, String extPath) throws IOException
	{
		Path path = fileSystem.getPath(zipPath);
		if(!Files.exists(path))
		{
			Path parent = path.getParent();
			System.out.println(parent);
			if(parent != null)
			{
				Files.createDirectories(parent);
			}
			Files.createFile(path);
		}
	    Files.copy(Paths.get(extPath), path, StandardCopyOption.REPLACE_EXISTING);
	}
	
	public boolean delete(String zipPath, boolean delDirs) throws IOException
	{
		boolean failed = true;
		Path path = fileSystem.getPath(zipPath);
		if(!Files.exists(path) && !delDirs)
		{
			return false;
		}
		if(delDirs)
		{
			for(int i = path.getNameCount(); i > 0; i--)
			{
				Path p = path.subpath(0, i);
				try
				{
					Files.delete(p);
				}
				catch(DirectoryNotEmptyException e)
				{
					break;
				}
			}
		}
		else
		{
			Files.delete(path);
		}
		return !failed;
	}
	
	public boolean copy(String zipPath, String outPath) throws IOException
	{
		Path path = fileSystem.getPath(zipPath);
		if(!Files.exists(path))
		{
			return false;
		}
	    Files.copy(path, Paths.get(outPath), StandardCopyOption.REPLACE_EXISTING);
		return true;
	}
	
	public InputStream getInputStream(String zipPath) throws IOException
	{
		Path path = fileSystem.getPath(zipPath);
		if(Files.exists(path))
		{
			return Files.newInputStream(path);
		}
		return null;
	}
	
	@Override
	public void close() throws IOException
	{
		fileSystem.close();
	}
}