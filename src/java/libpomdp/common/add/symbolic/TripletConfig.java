/**
 * Author: Pascal Poupart (ppoupart@cs.uwaterloo.ca)
 * Reference: Chapter 5 of Poupart's PhD thesis
 * (http://www.cs.uwaterloo.ca/~ppoupart/publications/ut-thesis/ut-thesis.pdf)
 * NOTE: Parts of this code might have been modified for use by libpomdp
 *       - Diego Maniloff
 */

package libpomdp.common.add.symbolic;

class TripletConfig {
		private DD dd1;
		private DD dd2;
		private int[][] config;

		public TripletConfig(DD dd1, DD dd2, int[][] config) {
				this.dd1 = dd1;
				this.dd2 = dd2;
				this.config = config; // Config.clone(config);
		}

		public int hashCode() {
				return dd1.getAddress() + dd2.getAddress() + Config.hashCode(config);
		}

		public boolean equals(Object obj) {
				
				if (obj.getClass() != getClass()) return false;
				TripletConfig triplet = (TripletConfig)obj;

				if (((dd1 == triplet.dd1 && dd2 == triplet.dd2) || 
						 (dd2 == triplet.dd1 && dd1 == triplet.dd2)) && 
						Config.equals(config, triplet.config)) 
						return true;
				else return false;
		}
}
