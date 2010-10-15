/** ------------------------------------------------------------------------- *
 * File: DD.java
 * Author: Pascal Poupart (Symbolic Perseus)
 * libPOMDP Adaptation: Mauricio Araya 
 --------------------------------------------------------------------------- */

package libpomdp.common.add.symbolic;

import java.io.PrintStream;
import java.io.Serializable;

public abstract class DD implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public static DD one = DDleaf.myNew(1);
		public static DD zero = DDleaf.myNew(0);

		protected int var;

    public int getVar() { return var; }

		public int getAddress() {
				return super.hashCode();
		}

		public DD[] getChildren() { return null; }  // should throw exception
		public double getVal() { return Double.NEGATIVE_INFINITY; }  // should throw exception
		public int[][] getConfig() { return null; }  // should throw exception

    public void display() {
				if (getNumLeaves() > 10000)
						System.out.println("Cannot display trees with more than 10,000 leaves (this tree has " + getNumLeaves() + " leaves)");
				else {
						display(""); 
						System.out.println();
				}
		}
		abstract public void display(String space);
		abstract public void display(String space, String prefix);
    abstract public void printSpuddDD(PrintStream ps);

		abstract public int getNumLeaves();
		//abstract public SortedSet getScope();
		abstract public int[] getVarSet();
        abstract public double getSum();
		abstract public DD store();

		public static DD cast(DDleaf leaf) { return (DD)leaf; }
		public static DD cast(DDnode node) { return (DD)node; }		
		/// concatenate DD arrays
	    public static DD[] concat(DD[] first, DD[]... rest) {
		int totalLength = first.length;
		for (DD[] array : rest) totalLength += array.length;	
		DD[] result = new DD[totalLength];
		// copy fist array
		System.arraycopy(first, 0, result, 0, first.length);
		int offset = first.length;
		for (DD[] array : rest) {
		    System.arraycopy(array, 0, result, offset, array.length);
		    offset += array.length;
		}
		return result;
	    }

	    /// first arg is not an array
	    public static DD[] concat(DD f, DD[]... rest) {
		DD[] first = new DD[1];
		first[0]   = f;
		return concat(first,rest);
	    }

	    /// append DD to a DD[]
	    public static DD[] append(DD[] first, DD l) {
		DD[] last = new DD[1];
		last[0]   = l;
		return concat(first, last);
	    }
}
