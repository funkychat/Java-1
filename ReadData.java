package patternsearch;

import java.io.IOException;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class ReadData
{
	private long lines;
	final static Charset ENCODING = StandardCharsets.UTF_8;
	public ReadData(long l)
	{
		this.lines = l++;
	}
	public List<String> readTextFile() throws IOException
	{
		List<String> cleanNames  = new ArrayList<>();
		Path path = Paths.get("C:\\Users\\Arjan\\Documents\\Domainnames\\clean2");
		try (Scanner scanner =  new Scanner(path, ENCODING.name()))
	    {
			while (lines > 0)
			{
				String line = scanner.nextLine();
				cleanNames.add(line);
				this.lines--;
			}     
	    }
		return cleanNames;
	}
	public List<String> getDomainNames()
	{
		List<String> domainnames = new ArrayList<>();
		try
		{
			domainnames = this.readTextFile();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return domainnames;
	}
}
