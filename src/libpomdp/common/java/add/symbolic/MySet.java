package libpomdp.common.java.add.symbolic;


class MySet {

		/////////////////////////////////////////////////////////
		// hashCode
		/////////////////////////////////////////////////////////
		public static int hashCode(int[] set) {
				int result = 0;
				for (int i=0; i<set.length; i++) {
						result += set[i];
				}
				return result;
		}

		/////////////////////////////////////////////////////////
		// clone
		/////////////////////////////////////////////////////////
		public static int[] clone(int[] set) {
				int[] newSet = new int[set.length];
				for (int i=0; i<set.length; i++) {
						newSet[i] = set[i];
				}
				return newSet;
		}

		/////////////////////////////////////////////////////////
		// find
		/////////////////////////////////////////////////////////
		public static int find(int[] set, int item) {
				for (int i=0; i<set.length; i++) {
						if (set[i] == item) return i;
				}
				return -1;
		}

		public static int find(String[] set, String item) {
				for (int i=0; i<set.length; i++) {
						if (item.equals(set[i])) return i;
				}
				return -1;
		}

		/////////////////////////////////////////////////////////
		// toString
		/////////////////////////////////////////////////////////
		public static String toString(int[] set) {
				
				String str = new String("{");
				
				if (set != null) {
						for (int i=0; i<set.length; i++) {
								
								if (i != set.length-1) str = str + Integer.toString(set[i]) + new String(",");
								else str = str + Integer.toString(set[i]);
						}
				}

				str = str + new String("}");
				return str;
		}

		/////////////////////////////////////////////////////////
		// maskToString
		/////////////////////////////////////////////////////////
		public static String maskToString(boolean[] mask) {
				String str = new String("[");
				for (int i=0; i<mask.length; i++) {
						if (mask[i]) {
								if (i != mask.length-1) str = str + Integer.toString(i) + new String(",");
								else str = str + Integer.toString(i);
						}
				}
				str = str + new String("]");
				return str;
		}

		/////////////////////////////////////////////////////////
		// union
		/////////////////////////////////////////////////////////
		public static int[] union(int[] set1, int elt2) {
		    int[] set2 = new int[1];
		    set2[0] = elt2;
		    return union(set1,set2);
		}

		public static int[] union(int elt1, int[] set2) {
		    int[] set1 = new int[1];
		    set1[0] = elt1;
		    return union(set1,set2);
		}

		public static int[] union(int elt1, int elt2) {
		    int[] set1 = new int[1];
		    set1[0] = elt1;
		    int[] set2 = new int[1];
		    set2[0] = elt2;
		    return union(set1,set2);
		}

		public static int[] union(int[] set1, int[] set2) {

				boolean[] mask = new boolean[set1.length];
				int count = 0;
				for (int i=0; i<set1.length; i++) {
						if (MySet.find(set2, set1[i]) == -1) { 
								mask[i] = true;
								count++;
						}
						else mask[i] = false;
				}

				int[] set = new int[count + set2.length];
				for (int i=0; i<set2.length; i++) {
						set[i] = set2[i];
				}

				int j = 0;
				for (int i=0; i<set1.length; i++) {
						if (mask[i]) {
								set[j+set2.length] = set1[i];
								j++;
						}
				}
				return set;
		}

		/////////////////////////////////////////////////////////
		// unionOrdered
		/////////////////////////////////////////////////////////
		public static int[] unionOrdered(int[] set1, int elt2) {
		    int[] set2 = new int[1];
		    set2[0] = elt2;
		    return unionOrdered(set1,set2);
		}

		public static int[] unionOrdered(int elt1, int[] set2) {
		    int[] set1 = new int[1];
		    set1[0] = elt1;
		    return unionOrdered(set1,set2);
		}

		public static int[] unionOrdered(int elt1, int elt2) {
		    int[] set1 = new int[1];
		    set1[0] = elt1;
		    int[] set2 = new int[1];
		    set2[0] = elt2;
		    return unionOrdered(set1,set2);
		}

		public static int[] unionOrdered(int[] set1, int[] set2) {

				int i1 = 0;
				int i2 = 0;
        int count = 0;
				while (i1 < set1.length && i2 < set2.length) {
						if (set1[i1] < set2[i2]) i1++;
						else if (set2[i2] < set1[i1]) i2++;
            else { i1++; i2++; }
            count++;
				}
				if (i1 < set1.length) count += set1.length - i1;
				if (i2 < set2.length) count += set2.length - i2;
				int[] set = new int[count];

				i1 = 0;
				i2 = 0;
        count = 0;
				while (i1 < set1.length && i2 < set2.length) {
						if (set1[i1] < set2[i2]) { set[count] = set1[i1]; i1++; }
						else if (set2[i2] < set1[i1]) { set[count] = set2[i2]; i2++; }
            else { set[count] = set1[i1]; i1++; i2++; }
            count++;
				}
				for (; i1<set1.length; i1++) { set[count] = set1[i1]; count++; }
				for (; i2<set2.length; i2++) { set[count] = set2[i2]; count++; }

				return set;
		}

		/////////////////////////////////////////////////////////
		// remove
		/////////////////////////////////////////////////////////
		public static int[] remove(int[] set, int item) {

				int[] remSet;
				int i = MySet.find(set,item);
				if (i != -1) {
						remSet = new int[set.length-1];
						for (int j=0; j<i; j++) remSet[j] = set[j];
						for (int j=i+1; j<set.length; j++) remSet[j-1] = set[j];
				}
				else {
						remSet = new int[set.length];
						for (int j=0; j<set.length; j++) remSet[j] = set[j];
				}
				return remSet;
		}

		/////////////////////////////////////////////////////////
		// removeIth
		/////////////////////////////////////////////////////////
		public static int[] removeIth(int[] set, int ith) {

				int[] remSet = new int[set.length-1];
				for (int i=0; i<ith; i++) {
						remSet[i] = set[i];
				}
				for (int i=ith+1; i<set.length; i++) {
						remSet[i-1] = set[i];
				}
				return remSet;
		}

		/////////////////////////////////////////////////////////
		// equals  
		/////////////////////////////////////////////////////////
		public static boolean equals(int[] set1, int[] set2) {

				if (set1 == set2) return true;
				else if ((set1 != null) && (set2 != null)) {
						if (set1.length == set2.length) {
								for (int i1=0; i1<set1.length; i1++) {
										if (MySet.find(set2,set1[i1]) == -1) return false;
								}
								return true;
						}
						else return false;
				}
				else return false;
		}

		/////////////////////////////////////////////////////////
		// diff
		/////////////////////////////////////////////////////////
		public static int[] diff(int[] set1, int elt2) {
		    int[] set2 = new int[1];
		    set2[0] = elt2;
		    return diff(set1,set2);
		}

		public static int[] diff(int elt1, int[] set2) {
		    int[] set1 = new int[1];
		    set1[0] = elt1;
		    return diff(set1,set2);
		}

		public static int[] diff(int elt1, int elt2) {
		    int[] set1 = new int[1];
		    set1[0] = elt1;
		    int[] set2 = new int[1];
		    set2[0] = elt2;
		    return diff(set1,set2);
		}

		public static int[] diff(int[] set1, int[] set2) {

				if (set1 == null) return null;
				if (set2 == null) return MySet.clone(set1);

				int count = 0;
				boolean[] mask = new boolean[set1.length];
				for (int i=0; i<set1.length; i++) {
						if (MySet.find(set2,set1[i]) == -1) {
								mask[i] = true;
								count++;
						}
						else mask[i] = false;
				}

				int[] diffSet = new int[count];
				int j = 0;
				for (int i=0; i<set1.length; i++) {
						if (mask[i]) {
								diffSet[j] = set1[i];
								j++;
						}
				}
				return diffSet;
		}

		/////////////////////////////////////////////////////////
		// sort (bubble sort: n^2)
		/////////////////////////////////////////////////////////
		public static int[] sort(int[] set) {

		    int temp;
		    int[] sortedSet = new int[set.length];
		    for (int i=0; i<set.length; i++) {
			sortedSet[i] = set[i];
		    }

		    for (int i=0; i<set.length-1; i++) {
			for (int j=i+1; j<set.length; j++) {
			    if (sortedSet[j] < sortedSet[i]) {
				temp = sortedSet[i];
				sortedSet[i] = sortedSet[j];
				sortedSet[j] = temp;
			    }
			}
		    }
		    return sortedSet;
		}

		/////////////////////////////////////////////////////////
		// reverse
		/////////////////////////////////////////////////////////
		public static int[] reverse(int[] set) {

		    int[] reversedSet = new int[set.length];
		    for (int i=0; i<set.length; i++) {
			reversedSet[i] = set[set.length-i-1];
		    }
		    return reversedSet;
		}

		/////////////////////////////////////////////////////////
		// intersection
		/////////////////////////////////////////////////////////
		public static int[] intersection(int[] set, int elt) {
		    int[] result;
		    if (find(set,elt) >= 0) {
			result = new int[1];
			result[1] = elt;
		    }
		    else result = new int[0];
		    return result;
		}

		public static int[] intersection(int elt, int[] set) {
		    int[] result;
		    if (find(set,elt) >= 0) {
			result = new int[1];
			result[1] = elt;
		    }
		    else result = new int[0];
		    return result;
		}

		public static int[] intersection(int[] set1, int[] set2) {
		    boolean[] mask = new boolean[set1.length];
		    int count = 0;
		    for (int i = 0; i < set1.length; i++) {
			if (find(set2,set1[i]) >= 0) {
			    count++;
			    mask[i] = true;
			}
			else mask[i] = false;
		    }
		    int[] result = new int[count];

		    count = 0;
		    for (int i=0; i<mask.length; i++) {
			if (mask[i]) {
			    result[count] = set1[i];
			    count++;
			}
		    }
		    return result;
		}

		/////////////////////////////////////////////////////////
		// shift
		/////////////////////////////////////////////////////////
		public static int[] shift(int[] set, int amount) {
		    int[] result = new int[set.length];

		    for (int i=0; i<set.length; i++) 
			result[i] = set[i] + amount;
		    return result;
		}


}
