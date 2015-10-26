import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

public class Inverted
{
	public void run() throws FileNotFoundException, UnsupportedEncodingException
	{
		// Defining Variables that will be used later in the program
		HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
		ArrayList<String> withSpace = new ArrayList<String>();
		ArrayList<String> docID = new ArrayList<String>();
		PrintWriter writer = new PrintWriter("sortedDictionary.txt", "UTF-8");
		PrintWriter postList = new PrintWriter("postingList.txt", "UTF-8");
		File f = new File("cacm.all");
		String currLine, fullString = null;
		currLine = null;
		boolean check;
		check = false;
		Scanner sc = new Scanner(f);
		Stemmer stem = new Stemmer();
		
		// Parsing through the file
		// for (int i = 0; i <= 500; i++)
		while (sc.hasNextLine())
		{
			
			stem.stem();
			if (check == false)
				currLine = sc.nextLine();
			check = false;
			if (currLine.substring(0, 2).equals(".I"))
			{
				if (fullString != null)
					docID = addStrings(docID, fullString);
				fullString = currLine + " ";

				Collections.addAll(withSpace, currLine.split("\\s+"));
			}
			else if (currLine.substring(0, 2).equals(".T"))
			{
				currLine = sc.nextLine();
				while (!currLine.substring(0, 1).equals("."))
				{
					fullString = fullString.concat(currLine);
					Collections.addAll(withSpace, currLine.split("\\s+"));
					currLine = sc.nextLine();
					check = true;
				}
			}
			else if (currLine.substring(0, 2).equals(".B"))
			{
				fullString = fullString.concat(currLine);
				Collections.addAll(withSpace, currLine.split("\\s+"));
				currLine = sc.nextLine();
				while (!currLine.substring(0, 1).equals("."))
				{
					// System.out.println("3");
					fullString = fullString.concat(currLine);
					Collections.addAll(withSpace, currLine.split("\\s+"));
					currLine = sc.nextLine();
					check = true;
				}
			}
			else if (currLine.substring(0, 2).equals(".A"))
			{
				fullString = fullString.concat(currLine);
				Collections.addAll(withSpace, currLine.split("\\s+"));
				currLine = sc.nextLine();
				while (!currLine.substring(0, 1).equals("."))
				{
					// System.out.println("4");
					fullString = fullString.concat(currLine);
					Collections.addAll(withSpace, currLine.split("\\s+"));
					currLine = sc.nextLine();
					check = true;
				}
			}

			else if (currLine.substring(0, 2).equals(".W"))
			{

				fullString = fullString.concat(currLine);
				Collections.addAll(withSpace, currLine.split("\\s+"));
				currLine = sc.nextLine();
				while (!currLine.substring(0, 1).equals(".") && sc.hasNextLine())
				{
					fullString = fullString.trim() + currLine.trim();
					Collections.addAll(withSpace, currLine.split("\\s+"));
					currLine = sc.nextLine();
					check = true;

				}
			}
		}
		if (fullString != null)
			docID = addStrings(docID, fullString);

		sc.close();
		System.out.println("Finished parsing CACM.ALL");
		docID = toLower(docID);
		//docID = myStem(docID);
		// docID = regex(docID);
		withSpace = toLower(withSpace);
		withSpace = regex(withSpace);
		withSpace = myStem(withSpace);
		//printList(docID);
		dictionary = countWords(withSpace, dictionary);
		sortMap(dictionary, writer);
		// Posting List construction begins here
		// Builds the initial hashmap values
		// Returns the docID, POS, and string occurences

		parseLine(docID, postList);

		// File closing
		writer.close();
		postList.close();

	}

	public void rebuild(int id) throws FileNotFoundException, UnsupportedEncodingException
	{

		ArrayList<String> docID = new ArrayList<String>();
		File f = new File("cacm.all");
		String currLine, fullString = null;
		currLine = null;
		boolean check;
		check = false;
		Scanner sc = new Scanner(f);
		while (sc.hasNextLine())
		{

			if (check == false)
				currLine = sc.nextLine();
			check = false;
			if (currLine.substring(0, 2).equals(".I"))
			{
				if (fullString != null)
					docID = addStrings(docID, fullString);
				fullString = currLine + " ";
			}
			else if (currLine.substring(0, 2).equals(".T"))
			{
				currLine = sc.nextLine();
				while (!currLine.substring(0, 1).equals("."))
				{
					fullString = fullString.concat(currLine);
					currLine = sc.nextLine();
					check = true;
				}
			}
			else if (currLine.substring(0, 2).equals(".B"))
			{
				fullString = fullString.concat(currLine);
				currLine = sc.nextLine();
				while (!currLine.substring(0, 1).equals("."))
				{
					// System.out.println("3");
					fullString = fullString.concat(currLine);
					currLine = sc.nextLine();
					check = true;
				}
			}
			else if (currLine.substring(0, 2).equals(".A"))
			{
				fullString = fullString.concat(currLine);
				currLine = sc.nextLine();
				while (!currLine.substring(0, 1).equals("."))
				{
					// System.out.println("4");
					fullString = fullString.concat(currLine);
					currLine = sc.nextLine();
					check = true;
				}
			}

			else if (currLine.substring(0, 2).equals(".W"))
			{

				fullString = fullString.concat(currLine);
				currLine = sc.nextLine();
				while (!currLine.substring(0, 1).equals(".") && sc.hasNextLine())
				{
					fullString = fullString.trim() + currLine.trim();
					currLine = sc.nextLine();
					check = true;

				}
			}
		}
		if (fullString != null)
			docID = addStrings(docID, fullString);

		sc.close();
		
		
		search(id, docID);

		//
		
	}
	public static ArrayList<String> myStem(ArrayList<String> word){
		Stemmer stem = new Stemmer();
		ArrayList<String> stemmed = new ArrayList<String>();
		String thisWord = null;
		char[] wordArr = null;
		for(int i = 0; i < word.size(); i++){
			thisWord = word.get(i);
			wordArr = thisWord.toCharArray();
			for(int j = 0; j < wordArr.length; j++){
				stem.add(wordArr[j]);
			}
			stem.stem();
			thisWord = stem.toString();
			stemmed.add(thisWord);
		}
		return stemmed;
	}
	public static String myStemStr(String word){
		Stemmer stem = new Stemmer();
		String stemmed = null;
		String thisWord = null;
		char[] wordArr = word.toCharArray();
		for(int i = 0; i < wordArr.length; i++){
			stem.add(wordArr[i]);
		}
		stem.stem();
		thisWord = stem.toString();
		stemmed = thisWord;
		
		return stemmed;
	}
	
	private void search(int id, ArrayList<String> docID)
	{
		for(int i = 0; i < docID.size(); i++){
			int j = 0;
			String temp = docID.get(i);
			int num = 0;
			while (!Character.isDigit(temp.charAt(j)))
			{
				j++;
			}
			int h = j;
			while (Character.isDigit(temp.charAt(h)))
			{
				h++;
			}
			num = Integer.parseInt(temp.substring(j, h));
				if(id == num){
					System.out.println(temp);
			}
		}
	}

	// Parses through the sentences separating the words and producing their
	// DOCID and POS
	public static void parseLine(ArrayList<String> words, PrintWriter file)
	{
		Multimap<String, String> multimapOG = ArrayListMultimap.create();
		Set<String> keys = multimapOG.keySet();

		for (int i = 0; i < words.size(); i++)
		{
			int j = 0;
			String temp = words.get(i);
			int num = 0;
			while (!Character.isDigit(temp.charAt(j)))
			{
				j++;
			}
			int h = j;
			while (Character.isDigit(temp.charAt(h)))
			{
				h++;
			}
			num = Integer.parseInt(temp.substring(j, h));
			temp = temp.substring(h + 1, temp.length());
			String[] arr = temp.replaceAll("[^a-z]", "  ").split("\\s+");
			ArrayList<String> countTotal = Lists.newArrayList(arr);;
			countTotal = myStem(countTotal);
			Multiset<String> weirdCount = HashMultiset.create(countTotal);
			for (String ss : arr)
			{
				ss = myStemStr(ss);
				int count = weirdCount.count(ss);
				if (ss.length() > 2 && isStopWord(ss))
					//System.out.println(ss + " " + num + " ");
					// temp.indexOf(ss));
					multimapOG.put(ss, num + "." + temp.indexOf(ss) + ":" + count);
			}

			// System.out.println(temp + " " + num);
		}
		for (String key : keys)
		{
			// System.out.println("Term = " + map.get(key));
			file.println("Term: " + key);
			// System.out.println("Doc ID and Position" + key);
			file.println("DocID.Position:Frequency" + multimapOG.get(key));
		}
		System.out.println("Finished Building Posting List");
	}

	
	// lowercases it all
	public static ArrayList<String> toLower(ArrayList<String> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			list.set(i, list.get(i).toLowerCase());
		}
		return list;
	}
	public static void printArray(String[] arr){
		for(int i = 0; i < arr.length; i++){
			System.out.println(arr[i]);
		}
	}
	// regex to reduce word count and stop words
	public static ArrayList<String> regex(ArrayList<String> list)
	{
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();)
		{
			String name = iterator.next();
			if (isErrorProne(name) || !isStopWord(name))
			{
				iterator.remove();
			}
		}
		return list;
	}

	// regex to help reduce wordcount for dictionary
	private static boolean isErrorProne(String name)
	{
		return !name.matches("^[a-zA-Z \\-']+$");
	}

	// Stop words
	private static boolean isStopWord(String name)
	{
		return !name.matches(
				"i|a|about|an|and|are|as|at|be|by|for|from|how|in|is|it|of|on|or|that|the|this|to|was|what|when|where|who|will|with|the");
	}

	// Prints arraylist to files
	public static void printArrayList(ArrayList<String> list, PrintWriter file)
	{
		for (String str : list)
		{
			file.write(str);
		}
		file.close();
	}

	// kinda useless
	public static ArrayList<String> addStrings(ArrayList<String> list, String fullString)
	{

		list.add(fullString);
		return list;
	}

	// prints a list
	public static void printList(ArrayList<String> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			System.out.println(list.get(i));
		}
	}

	// Counts the words for the dictionary
	public static HashMap<String, Integer> countWords(ArrayList<String> list, HashMap<String, Integer> count)
	{
		int j = 0;
		for (int i = 0; i < list.size(); i++)
		{
			Integer f = count.get(list.get(i));
			if (f == null)
			{
				count.put(list.get(i), 1);
				j++;
			}
			else
			{
				count.put(list.get(i), f + 1);
			}

		}
		System.out.println("Finished Building Dictionary");
		return count;
	}

	// Sorts and prints the dictionary in alphabetical order
	public static void sortMap(HashMap<String, Integer> unsorted, PrintWriter file)
	{

		Map<String, Integer> map = new TreeMap<String, Integer>(unsorted);
		Set set2 = map.entrySet();
		Iterator iterator2 = set2.iterator();
		while (iterator2.hasNext())
		{
			Map.Entry me2 = (Map.Entry) iterator2.next();
			file.print(me2.getKey() + ": ");
			file.println(me2.getValue());

		}
		System.out.println("Finished Sorting Dictionary");
	}

}