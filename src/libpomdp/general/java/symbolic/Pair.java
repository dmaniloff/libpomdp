package libpomdp.general.java.symbolic;

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
