/**
 * Author: Pascal Poupart (ppoupart@cs.uwaterloo.ca)
 * Reference: Chapter 5 of Poupart's PhD thesis
 * (http://www.cs.uwaterloo.ca/~ppoupart/publications/ut-thesis/ut-thesis.pdf)
 * NOTE: Parts of this code might have been modified for use by libpomdp
 *       - Diego Maniloff
 */

package libpomdp.common.add.symbolic;

import java.util.*;
import java.lang.ref.*;
import java.io.*;

public class DDleaf extends DD {
		private double val;
		private int[][] config;

		private DDleaf(double val) {
				this.val = val;
				this.var = 0;
				this.config = null;
		}

		private DDleaf(double val, int[][] config) {
				this.val = val;
				this.var = 0;
				this.config = config;
		}

		public static DD myNew(double val) {

				// create new leaf
				DDleaf leaf = new DDleaf(val);

				// try to lookup leaf in leafHashtable 
				WeakReference storedLeaf = (WeakReference)Global.leafHashtable.get(leaf); 
				if (storedLeaf != null) return (DDleaf)storedLeaf.get();

				// store leaf in leafHashtable
				Global.leafHashtable.put(leaf,new WeakReference<DD>(leaf));
				return leaf;
		}

		public static DD myNew(double val, int[][] config) {

				// create new leaf
				DDleaf leaf = new DDleaf(val,config);

				// try to lookup leaf in leafHashtable
				WeakReference storedLeaf = (WeakReference)Global.leafHashtable.get(leaf);
				if (storedLeaf != null) return (DDleaf)storedLeaf.get();

				// store leaf in leafHashtable
				Global.leafHashtable.put(leaf,new WeakReference<DD>(leaf));
				return leaf;
		}

    //public SortedSet getScope() {
		//		return new TreeSet();
		//}

    public int[] getVarSet() {
				return new int[0];
		}
    
    public double getSum() {
        return val;
    }

		public double getVal() {
				return val;
		}

		public int[][] getConfig() {
				return config;
		}

		public int getNumLeaves() {
				return 1;
		}

		public boolean equals(Object obj) {

				if (obj.getClass() != getClass()) return false;
				DDleaf leaf = (DDleaf)obj;

				if (val == leaf.val && Config.equals(config,leaf.config))
						return true;
				else return false;
		}

		public int hashCode() {
				Double valD = new Double(val);
				return valD.hashCode() + Config.hashCode(config);
		}

		public DD store() {
				return DDleaf.myNew(val,config);
		}

		public void display(String space) {
				System.out.println(space + "leaf: " + Double.toString(val) + "  " 
													 + Config.toString(config));
		}

		public void display(String space, String prefix) {
				System.out.println(space + prefix + Double.toString(val) + "  " 
													 + Config.toString(config));
		}
    public void printSpuddDD(PrintStream ps) {
	ps.print("(" + Double.toString(val) + ")");
    }
}
