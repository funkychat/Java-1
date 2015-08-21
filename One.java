package patternsearch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class One
{
	public static void main(String[] args)
	{
		long rows = 5000000;
		double count = rows * 0.00001; /*Determines the cut off point, should be 0.01 or below */
		int found = 0;
		
		ReadData rd = new ReadData(rows);
		List<String> domainnames = rd.getDomainNames();
		List<String> knowngood = new ArrayList<String>();
		List<String> knownfail = new ArrayList<String>();
		for(int length=3; length<=20; length++)
		{
			List<String> oldpattern = One.getOldPattern(length, knowngood);
			for(int i=0; i<domainnames.size(); i++)
			{
				long starttime = System.nanoTime();
				String domainname = domainnames.get(i);
				List<String> patternlist = One.getPattern(domainname, length, knowngood, oldpattern);
				patternlist = One.doClearFail(patternlist, knownfail);
				for(String pattern: patternlist)
				{
					for(int j=i; j<domainnames.size(); j++)
					{
						if(domainnames.get(j).contains(pattern))
						{
							found++;
							if(found >= count)
							{
								if(!knowngood.contains(pattern))
								{
									knowngood.add(pattern);
								}
								break;
							}
						}
					}
					if(found < count)
					{
						if(!knownfail.contains(pattern))
						{
							knownfail.add(pattern);
						}
					}
					found = 0;
				}
				long endtime = System.nanoTime();
				if( i % 1000 == 0)
				{
					System.out.println("length => "+length+"; i => "+ i +"; Time => "+ Math.round((endtime-starttime)*10E-6));
					System.out.println("length good => "+knowngood.size()+"; length fail => "+knownfail.size());
					System.out.println("-------------------------");
				}
			}
			knownfail.clear();
		}
		for(String p: knowngood)
		{
			System.out.println(p);
		}
	}
	public static List<String> getPattern(String domainname, int length, List<String> knowngood, List<String> oldpattern)
	{
		List<String> result = new ArrayList<>();
		for(int i = 0; i < domainname.length()-length+1; i++)
		{
			String text = domainname.substring(i,  i+length);
			if(result.indexOf(text) == -1 && !(knowngood.contains(text)))
			{
				result.add(text);
			}
		}
		if(length > 3)
		{
			result = One.doCheckShorter(result, oldpattern);
		}
		return result;
	}
	public static List<String> getOldPattern(int length, List<String> knowngood)
	{
		List<String> oldpattern = new ArrayList<String>();
		if(length > 3)
		{
			for(String k: knowngood)
			{
				if(k.length() == length-1)
				{
					oldpattern.add(k);
				}
			}
		}
		return oldpattern;
	}
	public static List<String> doCheckShorter(List<String> result, List<String> oldpattern)
	{
		Iterator<String> resultit = result.iterator();
		while(resultit.hasNext())
		{
			boolean test = false;
			String r = resultit.next();
			for(String o: oldpattern)
			{
				if(r.contains(o))
				{
					test = true;
					break;
				}
			}
			if(test == false)
			{
				resultit.remove();
			}
		}
		return result;
	}
	public static List<String> doClearFail(List<String> patternlist, List<String> knownfail)
	{
		Iterator<String> patternlistit = patternlist.iterator();
		while(patternlistit.hasNext())
		{
			String pattern = patternlistit.next();
			if(knownfail.contains(pattern))
			{
				patternlistit.remove();
			}
		}
		return patternlist;
	}
}
