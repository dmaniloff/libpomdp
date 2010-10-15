package libpomdp.common.add.symbolic;

class TripletSet {
		private DD dd1;
		private DD dd2;
		private int[] varSet;

		public TripletSet(DD dd1, DD dd2, int[] varSet) {
				this.dd1 = dd1;
				this.dd2 = dd2;
				this.varSet = varSet; // MySet.clone(varSet);
		}

		public int hashCode() {
				return dd1.getAddress() + dd2.getAddress() + MySet.hashCode(varSet);
		}

		public boolean equals(Object obj) {
				
				if (obj.getClass() != getClass()) return false;
				TripletSet triplet = (TripletSet)obj;

				if (((dd1 == triplet.dd1 && dd2 == triplet.dd2) || 
						 (dd2 == triplet.dd1 && dd1 == triplet.dd2)) &&
						MySet.equals(varSet, triplet.varSet))
						return true;
				else return false;
		}
}
