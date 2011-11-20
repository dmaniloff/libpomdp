/**
 * Author: Pascal Poupart (ppoupart@cs.uwaterloo.ca)
 * Reference: Chapter 5 of Poupart's PhD thesis
 * (http://www.cs.uwaterloo.ca/~ppoupart/publications/ut-thesis/ut-thesis.pdf)
 * NOTE: Parts of this code might have been modified for use by libpomdp
 *       - Diego Maniloff
 */

package libpomdp.common.add.symbolic;

class Pair {
		private DD dd1;
		private DD dd2;

		public Pair(DD dd1, DD dd2) {
				this.dd1 = dd1;
				this.dd2 = dd2;
		}

		public int hashCode() {
				return dd1.getAddress() + dd2.getAddress();
		}

		public boolean equals(Object obj) {
				
				if (obj.getClass() != getClass()) return false;
				Pair pair = (Pair)obj;

				return ((dd1 == pair.dd1 && dd2 == pair.dd2) || 
								(dd2 == pair.dd1 && dd1 == pair.dd2));
		}
}
