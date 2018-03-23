import java.util.Comparator;

public class TreeComparator implements Comparator<BST<Integer, TreeData>>{
	@Override
	public int compare(BST<Integer, TreeData> arg0, BST<Integer, TreeData> arg1) {
		if(arg0.getValue().getInt()>arg1.getValue().getInt()) {
			return 1;
		}else if(arg0.getValue().getInt()<arg1.getValue().getInt()) {
			return -1;
		}else {
			return 0;
		}
	}
}
