package libpomdp.common.add.symbolic;

import java.io.*;

public abstract class DD implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = -2110906271517248607L;
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
}
