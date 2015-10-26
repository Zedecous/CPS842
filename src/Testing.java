import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Testing extends Inverted
{
	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException
	{
		File f = new File("PostingList.txt");
		String term = null;
		boolean stopWords = true;
		boolean stem = true;
		boolean searching = true;
		int searchCount = 0;
		double totalSearchTime = 0;

		Inverted test = new Inverted();
		Scanner in = new Scanner(System.in);

		System.out.println("Would you like stop words? y/n");
		String nextLine = in.nextLine();
		if (nextLine.equals("y"))
		{
			stopWords = true;
		}
		else if (nextLine.equals("n"))
		{
			stopWords = false;
		}

		System.out.println("Would you like stemming? y/n");
		nextLine = in.nextLine();
		if (nextLine.equals("y"))
		{
			stem = true;
		}
		else if (nextLine.equals("n"))
		{
			stem = false;
		}

		System.out.println("Hello, type 'b' to build the dictionary and posting list.");
		nextLine = in.nextLine();
		if (nextLine.equals("b"))
		{
			test.run(stopWords, stem);
			System.out.println("The libaries have been built");
		}
		while (searching)
		{
			System.out.println("Now type a keyword you would like to search");
			nextLine = in.nextLine();
			double startTime = System.nanoTime();
			searchCount++;

			Scanner sc = new Scanner(f);
			if (stem)
			{
				term = myStem(nextLine);
			}
			else
			{
				term = nextLine;
			}

			System.out.println(term);
			String currLine = null;
			while (sc.hasNextLine())
			{
				currLine = sc.nextLine();
				nextLine = sc.nextLine();
				if (currLine.substring(6).equals(term))
				{
					nextLine = nextLine.substring(25);
					ArrayList<Integer> id = docID(nextLine);
					nextID(id, term);
				}
			}
			double endTime = System.nanoTime();
			double duration = (endTime - startTime);
			totalSearchTime = totalSearchTime + duration;
			System.out.println(duration / 1000000000 + " Term Search Time ");
			System.out.println("Would you like to search another term? y/n");
			nextLine = in.nextLine();
			if (nextLine.equals("y"))
			{
				searching = true;
			}
			else
				searching = false;

			sc.close();
		}
		in.close();
		System.out.println("Average Search Time = " + totalSearchTime / searchCount / 1000000000);

	}

	public static String myStem(String term)
	{
		Stemmer stem = new Stemmer();
		char[] wordArr = null;
		String newTerm = null;
		wordArr = term.toCharArray();
		for (int i = 0; i < wordArr.length; i++)
		{
			stem.add(wordArr[i]);
		}
		stem.stem();
		newTerm = stem.toString();
		return newTerm;
	}

	public static ArrayList<Integer> docID(String docLine)
	{
		String temp = docLine;
		ArrayList<Integer> id = new ArrayList<Integer>();
		// int[] id = new int[docLine.length() - 1];
		// temp = temp.substring(ignore);
		String[] arr = temp.replaceAll("\\s+", "").split(",");
		// System.out.println(arr.length);
		for (int i = 0; i < arr.length; i++)
		{
			id.add(Integer.parseInt(arr[i].substring(0, arr[i].indexOf("."))));

		}
		return id;

	}

	// Gets the ID array and goes through cacm.all printing out the correspoding
	// lines.
	public static void nextID(ArrayList<Integer> id, String term) throws FileNotFoundException
	{
		File f = new File("cacm.all");
		String temp = null;
		int currID = 0;
		Scanner sc = new Scanner(f);
		boolean v = false;
		int idSize = id.size();
		int runSize = 0;
		if (idSize == 1)
		{
			runSize = idSize;
		}
		else
		{
			runSize = idSize - 1;
		}

		for (int i = 0; i < runSize; i++)
		{
			if (i != 0)
			{
				while (id.get(i).equals(id.get(i - 1)))
					i++;
				// System.out.println(id.get(i));
			}
			v = false;
			while (!v && sc.hasNextLine())
			{
				temp = sc.nextLine();
				while (!temp.substring(0, 2).equals(".I") && sc.hasNextLine())
				{
					temp = sc.nextLine();

				}

				currID = Integer.parseInt(temp.replaceAll("[^0-9]", ""));
				// System.out.println(id.get(i));
				if (currID == id.get(i))
				{
					// System.out.println(currID);
					while (!temp.toLowerCase().contains(term.toLowerCase()) && sc.hasNextLine())
					{
						temp = sc.nextLine();
					}
					System.out.println(currID + " " + temp);
					v = true;
				}
			}
		}

		sc.close();
	}
}