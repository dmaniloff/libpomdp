package libpomdp.common.add.symbolic;

class DDcollection {

		/////////////////////////////////////////////////////////
		// removeIth
		/////////////////////////////////////////////////////////
		public static DD[] removeIth(DD[] collection, int ith) {

				DD[] remCollection = new DD[collection.length-1];
				for (int i=0; i<ith; i++) 
						remCollection[i] = collection[i];
				for (int i=ith+1; i<collection.length; i++)
						remCollection[i-1] = collection[i];
				return remCollection;
		}

		/////////////////////////////////////////////////////////
		// add
		/////////////////////////////////////////////////////////
		public static DD[] add(DD[] collection, DD dd) {

				DD[] newCollection = new DD[collection.length+1];
				for (int i=0; i<collection.length; i++)
						newCollection[i] = collection[i];
				newCollection[collection.length] = dd;
				return newCollection;
		}
}

