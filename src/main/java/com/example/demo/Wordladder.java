package com.example.demo;
import java.io.*;
import java.util.*;

public class Wordladder
{
	static char[] alpha = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	static Queue <String> infected = new LinkedList<String>();	
	static HashSet<String> dict = new HashSet<String>();
	
	private String w1;
	private String w2;
	private String file;
	

	public Wordladder(String w1_,String w2_,String file_)
	{	
		this.w1=w1_;
		this.w2=w2_;
		this.file=file_;
	}
	
	public Wordladder()
	{
		this.w1="";
		this.w2="";
		this.file="";
	}
	
	public String act()
	{
		while (true)
		{		
			getDict(file);
			
			w1.toLowerCase();
			
			if (!testSingleWord(w1)) 
				continue;
				
			w2.toLowerCase();
			
			if (!testSingleWord(w2)) 
				continue;
			
			if (!testBothWords(w1,w2)) 
				return null;
					
			return body(w1, w2);							
		
		}
	}
	
	public boolean testSingleWord(String w)
	{
		if (w.equals("")) 
		{
			System.out.print("Have a nice day.");
			System.exit(0);
		}
		if (!dict.contains(w))
		{
			System.out.print("The input word must be a proper word."+w+"\n");
			return false;
		}  
		return true;
	}
	
	public boolean testBothWords(String w1,String w2)
	{
		if (w1.equals(w2))
		{
			System.out.print("The two words must be different.");
			return false;
		}
		if (w1.length()!=w2.length())
		{
			System.out.print("The two words must have the same length.");
			return false;
		}
		return true;
	}
	
	public void getDict(String file)
	{
		File filename = new File(file);
		String readin="";
		try
		{
			Reader reader = new InputStreamReader(new FileInputStream(filename));
			int charread = 0;
			while ((charread = reader.read()) != -1 ) 
			{
				if (charread != '\n')
					readin+=(char)charread;
				else
				{
					dict.add(readin);
					readin="";
				}
			}
			reader.close();
		}
		catch(IOException e)
		{
			System.out.print("Can't open the file. Have a  nice day."+file);
			System.exit(0);
		}
	}
	
	public String body(String w1, String w2) 
	{
		HashMap<String, String> route = new HashMap<String, String>();
		boolean done = false;
		String out="";
		infected.offer(w1);
		dict.remove(w1);
		while (!done)
		{
			Queue<String> newinfected = new LinkedList<String>();
			String first;
			String create;
			String temp;
		
			while (!infected.isEmpty()) 
			{
				first = infected.peek();
				for (int i = 0; i < first.length(); i++) 
				{
					for (char cc : alpha) 
					{
						temp = first;
						first = first.substring(0, i) + cc +first.substring(i+1);
						create = first;
						first = temp;
						if (create.equals(w2)) 
						{
							done = true;
							String now = first;
							out = w2;
							out += " ";
							out += first;
							while (now != w1) 
							{
								out += " ";
								now = route.get(now);
								out += now;
							}
							dict.clear();
							infected.clear();
							return "A ladder from " +w2+" back to "+w1+":\n"+out;
						}
						if (dict.contains(create)) 
						{
							if (!route.containsKey(create)) 
								route.put(create, first);
							newinfected.offer(create);
							dict.remove(create);
						}
					}
				}
				infected.poll();
			}
			if (newinfected.isEmpty()) 
			{
				done = true;
				dict.clear();
				infected.clear();
				return "No word ladder found from " + w2 + " back to " + w1 + ".";
			}
			infected = newinfected;
		}
		return "";
	}
}