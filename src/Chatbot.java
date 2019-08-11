import java.util.*;
import java.io.*;

public class Chatbot{
	private static String filename = "/Users/NS/Documents/workspace/Chatbot/src/WARC201709_wid.txt";
	private static ArrayList<Integer> readCorpus(){
		ArrayList<Integer> corpus = new ArrayList<Integer>();
		try{
			File f = new File(filename);
			Scanner sc = new Scanner(f);
			while(sc.hasNext()){
				if(sc.hasNextInt()){
					int i = sc.nextInt();
					corpus.add(i);
				}
				else{
					sc.next();
				}
			}
		}
		catch(FileNotFoundException ex){
			System.out.println("File Not Found.");
		}
		return corpus;
	}
	public static int countdoubleprob(int w, int h, ArrayList<Integer> corpus)
	{
		int clot = 0;
		for(int u = 1; u < corpus.size(); u++)
		{
			if((corpus.get(u - 1) == h) && (corpus.get(u) == w))
			{
				clot++;
			}
		}
		return clot;
	}
	public static int findNumerator(int w, int grey, int black, ArrayList<Integer> corpus)
	{
		int count1 = 0;
		for(int j = 2; j < corpus.size(); j++)
		{
			if((corpus.get(j - 2) == grey) && (corpus.get(j - 1) == black) && (corpus.get(j)) == w)
			{
				count1++;
			}
		}
		return count1++;
	}
	static public void main(String[] args){
		ArrayList<Integer> corpus = readCorpus();
		int flag = Integer.valueOf(args[0]);

		if(flag == 100){
			int w = Integer.valueOf(args[1]);
			int count = 0;
			for(int i = 0; i < corpus.size(); i++)
			{
				if(w == corpus.get(i))
				{
					count++;
				}
			}
			System.out.println(count);
			System.out.println(String.format("%.7f",count/(double)corpus.size()));
		}
		else if(flag == 200){
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			double r = (double) n1 / n2;
			int count1 = 0;
			List<Double> probability = new ArrayList<Double>();
			List<Integer> gr = new ArrayList<Integer>();
			for(int y = 0; y < corpus.size(); y++)
			{
				if(!gr.contains(corpus.get(y)))
				{
					gr.add(corpus.get(y));
					for(int h = 0; h < corpus.size(); h++)
					{
						if((corpus.get(y).equals(corpus.get(h))))
						{
							count1++;
						}
					}
				}
			}
			Collections.sort(gr);
			for(int g = 0; g < gr.size(); g++)
			{
				int counter = 0; 
				for(int w = 0; w < corpus.size(); w++)
				{
					if((gr.get(g).equals(corpus.get(w))))
					{
						counter++;
					}
				}
				double probab = counter / (double) corpus.size();
				if(probab != 0.0000000)
				{
					probability.add(probab);
					//System.out.println(probability);
				}

			}
			double[][] bounded = new double[3][4700];
			for(int l = 0; l < gr.size(); l++) // sets left bound
			{
				if(l == 0)
				{
					bounded[0][l] = 0;
				}
				else {
					for(int z = l; z > 0; z--)
					{
						bounded[0][l] += probability.get(l - z);
					}
				}
			}
			for(int a = 0; a < gr.size(); a++) // sets right bound
			{
				for(int s = a; s >= 0; s--)
				{
					bounded[1][a] += probability.get(s);
				}
			}
			double left = 0;
			double right = 0; 
			int wordInd = 0;
			for(int j = 0; j < 4700; j++)
			{
				if(r > bounded[0][j] && r < bounded[1][j])
				{
					left = bounded[0][j];
					right = bounded[1][j];
					wordInd = j;
					break;
				}
			}
			int word = gr.get(wordInd);
			System.out.println(word);
			System.out.println(String.format("%.7f", left));
			System.out.println(String.format("%.7f", right));
		}
		else if(flag == 300){
			int h = Integer.valueOf(args[1]);
			int w = Integer.valueOf(args[2]);
			int count = 0; // represents c(h | w)
			int count2 = 0; 
			List<Integer> words = new ArrayList<Integer>();
			for(int m = 0; m < corpus.size(); m++)
			{
				if(!words.contains(corpus.get(m)))
				{
					words.add(corpus.get(m));
					for(int n = 0; n < corpus.size(); n++)
					{
						if((corpus.get(m).equals(corpus.get(n))))
						{
							count2++;
						}
					}
				}
			}
			Collections.sort(words);
			count = countdoubleprob(w, h, corpus);
			int clot = 0;
			for(int f = 0; f < words.size(); f++)
			{
				clot += countdoubleprob(f, h, corpus);
			}

			//output 
			System.out.println(count);
			System.out.println(clot);
			System.out.println(String.format("%.7f",count/(double)clot));
		}
		else if(flag == 400){
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			int h = Integer.valueOf(args[3]);
			int count;
			double randomProbability = ((double) n1 / n2);
			double[] probabset;
			double right =0;
			double left = 0;
			double probfori = 0;
			ArrayList<Integer> wordsafterh = new ArrayList<Integer>();
			for(int i = 0; i < corpus.size() - 1; i++)
			{
				if(h == corpus.get(i))
				{
					wordsafterh.add(corpus.get(i + 1));
				}
			}
			Collections.sort(wordsafterh);
			for(int i = 0; i < 4700; i++)
			{
				probabset = new double[3];
				count = Collections.frequency(wordsafterh, i);
				if(count != 0)
				{
					probfori = count / (double) wordsafterh.size();
					if(i == 0)
					{
						probabset[0] = i;
						left = 0;
						right = probfori;
						probabset[1] = left;
						probabset[2] = right;
					}
					else {
						probabset[0] = i;
						left = right;
						right += probfori;
						probabset[1] = left;
						probabset[2] = right;
					}
					if(left <= randomProbability && randomProbability < right || (left == 0 && randomProbability == 0))
					{
						System.out.println((int)probabset[0]);
						System.out.println(String.format("%.7f", probabset[1]));
						System.out.println(String.format("%.7f", probabset[2]));
						break;
					}
				}

			}

		}
		else if(flag == 500){
			int h1 = Integer.valueOf(args[1]);
			int h2 = Integer.valueOf(args[2]);
			int w = Integer.valueOf(args[3]);
			int count = 0;
			count = findNumerator(w, h1, h2, corpus);
			int count1 = 0; 
			for(int h = 0; h < 4700; h++)
			{
				count1 += findNumerator(h, h1, h2, corpus);
			}
			if(count == 0)
			{
				System.out.println(count);
				System.out.println(count1);
				System.out.println("Undefined");
			}
			else{
				System.out.println(count);
				System.out.println(count1);
				System.out.println(String.format("%.7f", count / (double)count1));
			}
		}
		else if(flag == 600){
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			int h1 = Integer.valueOf(args[3]);
			int h2 = Integer.valueOf(args[4]);
			int count = 0;
			double randomProbability = (double) n1 / n2;
			double[] probabset;
			double left = 0;
			double right = 0;
			double probfori = 0;
			ArrayList<Integer> wordsafter = new ArrayList<Integer>();
			for(int i = 0; i < corpus.size() - 2; i++)
			{
				if(h1 == corpus.get(i) && h2 == corpus.get(i + 1))
				{
					wordsafter.add(corpus.get(i + 2));
				}
			}

			if(wordsafter.size() == 0)
			{
				System.out.println("undefined");
			} else {
				Collections.sort(wordsafter);
				for(int j = 0; j < 4700; j++)
				{
					probabset = new double[3];
					count = Collections.frequency(wordsafter, j);
					if(count != 0) {
						probfori = count / (double) wordsafter.size();

						if(j == 0)
						{
							probabset[0] = j;
							left = 0;
							right = probfori;
							probabset[1] = left;
							probabset[2] = right;
						}
						else{
							probabset[0] = j;
							left = right;
							right += probfori;
							probabset[1] = left;
							probabset[2] = right;
						}

						if((left < randomProbability && randomProbability <= right) || (left == 0 && randomProbability == 0))
						{
							System.out.println((int) probabset[0]);
							System.out.println(String.format("%.7f",probabset[1]));
							System.out.println(String.format("%.7f",probabset[2]));
							break;
						}

					}
				}


			}
		}




		else if(flag == 700){
			int seed = Integer.valueOf(args[1]);
			int t = Integer.valueOf(args[2]);
			int h1=0,h2=0;

			Random rng = new Random();
			if (seed != -1) rng.setSeed(seed);

			if(t == 0){
				double r = rng.nextDouble();
				List<Double> probability = new ArrayList<Double>();
				List<Integer> vocab = new ArrayList<Integer>();
				for(int d = 0; d < corpus.size(); d++)
				{
					if(!vocab.contains(corpus.get(d)))
					{
						vocab.add(corpus.get(d));
					}
				}
				Collections.sort(vocab);
				for(int i = 0; i < vocab.size(); i++)
				{
					int count = 0;
					for(int j = 0; j < corpus.size(); j++)
					{
						if((vocab.get(i).equals(corpus.get(j))))
						{
							count++;
						}
					}
					double probab = count / (double) corpus.size();
					if(probab != 0.0000)
					{
						probability.add(probab);
					}
				}
				double[][] groovy = new double[2][4700];
				for(int y = 0; y < vocab.size(); y++)
				{
					if(y == 0)
					{
						groovy[0][y] = 0;
					}
					else {
						for(int w = y; w > 0; w--)
						{
							groovy[0][y] += probability.get(y - w);
						}
					}
				}
				for(int q = 0; q < vocab.size(); q++)
				{
					for(int f = q; f >= 0; f--)
					{
						groovy[1][q] += probability.get(f);
					}
				}
				double left = 0;
				double right = 0;
				int word = 0;
				for(int i = 0; i < 4700; i++)
				{
					if(r >= groovy[0][i] && r < groovy[1][i])
					{
						right = groovy[1][i];
						left = groovy[0][i];
						word = i;
						break;
					}
				}

				h1 = vocab.get(word);
				System.out.println(h1);
				if(h1 == 9 || h1 == 10 || h1 == 12){
					return;
				}
				r = rng.nextDouble();
				double[] probab;
				double left1 = 0;
				double right1 = 0;
				double probfori;
				ArrayList<Integer> wordsafter = new ArrayList<Integer>();
				for(int s = 0; s < corpus.size() - 1; s++)
				{
					if(h1 == corpus.get(s))
					{
						wordsafter.add(corpus.get(s + 1));
					}
				}
				Collections.sort(wordsafter);
				for(int j = 0; j < 4700; j++)
				{
					probab = new double[3];
					double count = Collections.frequency(wordsafter, j);
					if(count != 0)
					{
						probfori = count / (double) wordsafter.size();
						if(j == 0)
						{
							probab[0] = j;
							left1 = right1;
							right1 += probfori;
							probab[1] = left1;
							probab[2] = right1;
						}
						else{
							probab[0] = j;
							left1 = right1;
							right1 += probfori;
							probab[1] = left1;
							probab[2] = right1;
						}
						if(left1 <= r && r < right1)
						{
							h2 = (int) probab[0];
							break;
						}

					}
					
				}
				System.out.println(h2);
			}
			else if(t == 1){
				h1 = Integer.valueOf(args[3]);
				// TODO Generate second word using r
				double r = rng.nextDouble();
				double[] probab;
				double left1 = 0;
				double right1 = 0;
				double probfori;
				ArrayList<Integer> wordsafter = new ArrayList<Integer>();
				for(int s = 0; s < corpus.size() - 1; s++)
				{
					if(h1 == corpus.get(s))
					{
						wordsafter.add(corpus.get(s + 1));
					}
				}
				Collections.sort(wordsafter);
				for(int j = 0; j < 4700; j++)
				{
					probab = new double[3];
					double count = Collections.frequency(wordsafter, j);
					if(count != 0)
					{
						probfori = count / (double) wordsafter.size();
						if(j == 0)
						{
							probab[0] = j;
							left1 = right1;
							right1 += probfori;
							probab[1] = left1;
							probab[2] = right1;
						}
						else{
							probab[0] = j;
							left1 = right1;
							right1 += probfori;
							probab[1] = left1;
							probab[2] = right1;
						}
						if(left1 <= r && r < right1)
						{
							h2 = (int) probab[0];
							break;
						}
					}
					

				}
				System.out.println(h2);
			}
			else if(t == 2){
				h1 = Integer.valueOf(args[3]);
				h2 = Integer.valueOf(args[4]);
			}

			while(h2 != 9 && h2 != 10 && h2 != 12){
				double r = rng.nextDouble();
				int w  = 0;
				double[] probab;
				double left = 0;
				double right = 0;
				double probfori = 0;
				ArrayList<Integer> wordsafter = new ArrayList<Integer>();
				for(int i = 0; i < corpus.size() - 1; i++)
				{
					if(h1 == corpus.get(i) && h2 == corpus.get(i+1)) {
						wordsafter.add(corpus.get(i + 2));
					}
				}
				Collections.sort(wordsafter);
				for(int j = 0; j < 4700; j++)
				{
					probab = new double[3];
					double count1 = Collections.frequency(wordsafter, j);
					if(count1 != 0)
					{
						probfori = count1 / (double) wordsafter.size();
						if(j == 0)
						{
							probab[0] = j;
							left = 0;
							right = probfori;
							probab[1] = left;
							probab[2] = right;
						}
						else{
							probab[0] = j;
							left = right;
							right += probfori;
							probab[1] = left;
							probab[2] = right;
						}
					}
					
					if(wordsafter.size() == 0)
					{
						h1 = Integer.valueOf(args[3]);
						double[] probab1;
						double left1 = 0; 
						double right1 = 0; 
						double probfori1 = 0;
						ArrayList<Integer> wordsafter1 = new ArrayList<Integer>();
						for(int g = 0; g < corpus.size() - 1; g++)
						{
							if(h1 == corpus.get(g))
							{
								wordsafter1.add(corpus.get(g + 1));
							}
						}
						Collections.sort(wordsafter1);
						for(int i = 0; i < 4700; i++)
						{
							probab1 = new double[3];
							count1 = Collections.frequency(wordsafter1, i);
							if(count1 != 0)
							{
								probfori1 = count1 / (double) wordsafter1.size();
								if(i == 0)
								{
									probab1[0] = i;
									left1 = 0; 
									right1 = probfori1;
									probab1[1] = left1;
									probab1[2] = right1;
								}
								else{
									probab1[0] = i;
									left1 = right1;
									right1 += probfori1;
									probab1[1] = left1;
									probab1[2] = right1;
								}
								if(left1 <= r && r < right1)
								{
									w = (int) probab1[0];
									break;
								}
							}
							
						}
					}
					else{
						if(left <= r && r < right)
						{
							w = (int) probab[0];
							break;
						}
					}
				}
				System.out.println(w);
				h1 = h2;
				h2 = w;
			}
		}

		return;
	}
}