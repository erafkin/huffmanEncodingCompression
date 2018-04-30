import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * 
 * @author Emma Rafkin, Winter 2018, CS10
 *
 */

public class Huffman {
	String file; //pathname to go into BufferedBitReader
	HashMap<Character, Integer> map; //map that holds each character and its frequency
	PriorityQueue<BST<Integer, TreeData>> queue; //priority queue of data
	BST<Integer, TreeData> tree; //tree of data
	HashMap<Character, String> encodedMap = new HashMap<Character, String>();//the code for how to compress and decompression tree		
	
	//put pathname for file in the constructor

	public Huffman(String f) {
		file = f;
		try {
			map = frequencyMap();
			prioritize(map);
			tree(queue);
			encode(tree);
		} catch (IOException e) {
			System.err.println("filepath not found");
		}
		
	}
	public HashMap<Character, Integer> frequencyMap() throws IOException {
		HashMap<Character, Integer> m = new HashMap<Character, Integer>();
		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader(file));
			
		} catch (FileNotFoundException e) {
			System.err.println("need to provide filepath");
			return null;
		}
		while(input.ready()) {
			Character key = (char) input.read();
			if(m.containsKey(key)) {
				m.replace(key, m.get(key)+1);
			}else {
				m.put(key, 1);
			}
		}
		input.close();
		return m;
	}
	/**
	 * puts all of the data from the map into Binary Search Trees and then each tree into a priority queue.
	 */
	public void prioritize(HashMap<Character, Integer> map) {
		if(map!=null&&map.size()!=0) { //makes sure that the file isn't empty
			Comparator<BST<Integer, TreeData>> t = new TreeComparator();
			PriorityQueue<BST<Integer, TreeData>> ret =  new PriorityQueue<BST<Integer, TreeData>>(t);
			for(Character c: map.keySet()) {
				TreeData a = new TreeData(c, map.get(c));
				BST<Integer, TreeData> b = new BST<Integer, TreeData>(null, a);
				ret.add(b);
			}
			this.queue = ret;
		}
	}
	
	/**
	 * 
	 * puts all of the objects in the priority queue into a tree
	 */
	public void tree(PriorityQueue<BST<Integer, TreeData>> pq){
		if (!pq.isEmpty()) {		
			while(pq.size()>1) {
				BST<Integer, TreeData> t1 = pq.remove();
				BST<Integer, TreeData> t2 = pq.remove();
				int frequency = t1.getValue().getInt() +t2.getValue().getInt();
				TreeData td = new TreeData((char)0, frequency);
				BST<Integer, TreeData> n = new BST<Integer, TreeData>(null, td, t1, t2);
				pq.add(n);
			}
			this.tree=pq.remove();
		}
		
		
		
	}
	/**
	 * this uses the tree to find a code for every character. 
	 */
	String c="";//holds the code despite the layer of the tree 
	public void encode(BST<Integer, TreeData> t){
		//if the file only has one character, if the tree is null
		if(tree!=null) {
			if(!this.tree.hasLeft()&&!this.tree.hasRight()) {
				encodedMap.put(tree.getValue().getChar(), "0");
			}else {
				if(t.hasLeft()) {
					c+="0";
					encode(t.getLeft());
			
				}
				if(t.hasRight()) {
					c+="1";
					encode(t.getRight());
			
				}
				if(t.isLeaf()) {
					encodedMap.put(t.getValue().getChar(), c);
			
				}
				if(c.length()>1) {
					c=c.substring(0, c.length()-1); //cuts off last number of c, ie. moves back up a branch on the tree
				}else {
					c = "";
				}
			}
		}
	}
	
	/**
	 * compresses text file into bits using the encoded map
	 * @throws IOException
	 */
	public void compress() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(file));
		BufferedBitWriter output = new BufferedBitWriter("inputs/compressed.txt"); 
		while(input.ready()) {
			Character key = (char) input.read();
			String s = this.encodedMap.get(key);
			for(char c: s.toCharArray()) {
				boolean write;
				if(c=='0') {
					write = false;
					output.writeBit(write);
				}else if (c=='1') {
					write = true;
					output.writeBit(write);
				}

			}
		}
		input.close();
		output.close();
	}
	/**
	 * decompresses a compressed map into a text file based off of the code from the tree (the encoded map).
	 * @throws IOException
	 */
	public void decompress() throws IOException{
		BufferedBitReader input = new BufferedBitReader("inputs/compressed.txt");
		BufferedWriter output = new BufferedWriter(new FileWriter("inputs/decompressed.txt"));
		BST<Integer, TreeData> t = this.tree;
		while(input.hasNext()) {
			boolean bit = input.readBit();
			if(t.isLeaf()) {
				output.write(t.getValue().getChar());	
			}
			if(bit) {
				if(t.hasRight()) {
					t = t.getRight();
					if(t.isLeaf()) {
						output.write(t.getValue().getChar());	
						t=this.tree;
					}
				}
				
			}else {
				if(t.hasLeft()) {
					t = t.getLeft();
					if(t.isLeaf()) {
						output.write(t.getValue().getChar());	
						t = this.tree;
					}
				}
			}
			
		}
		
		input.close();
		output.close();
	}

	public static void main(String[]args) {
		
		Huffman test = new Huffman("inputs/WarAndPeace.txt");
		try {
			test.compress();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			test.decompress();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
