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
		Scanner sc = new Scanner(f);
		String term = null;
		
		System.out.println("Hello, type 'r' to build the dictionary and posting list.");
		Inverted test = new Inverted();
		Scanner in = new Scanner(System.in);
		String nextLine = in.nextLine();
		if(nextLine.equals("Quit")){
			//
		}else if(nextLine.equals("r")){
			test.run();
			System.out.println("The libaries have been built");
		}
			System.out.println("Now type a keyword you would like to search");
			nextLine = in.nextLine();
			term = myStem(nextLine);
			System.out.println(term);
			String currLine = null;
			while(sc.hasNextLine()){
				currLine = sc.nextLine();
				nextLine = sc.nextLine();
				if(currLine.substring(6).equals(term)){
					nextLine = nextLine.substring(25);
					//System.out.println(nextLine.substring(0, nextLine.indexOf(".")));
					//System.out.println(nextLine);
					ArrayList<Integer> id = docID(nextLine);
					nextID(id, term);
			}
		}
			in.close();
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

	public static void nextID(ArrayList<Integer> id, String term) throws FileNotFoundException
	{
		File f = new File("cacm.all");
		String temp = null;
		int currID = 0;
		Scanner sc = new Scanner(f);
		boolean v = false;
		double startTime = System.nanoTime();
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
		double endTime = System.nanoTime();
		double duration = (endTime - startTime);
		System.out.println(duration / 1000000000 + " Term Search Time ");
		sc.close();
	}
}
