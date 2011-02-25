/*
 * Copyright (C) 2003-2006 Bjørn-Ove Heimsund
 * Modified by Mauricio Araya
 * 
 * This file is an integer representation of the Sparse vector version of MTJ
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package libpomdp.common;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Integer (sparse) vector
 */
public class IntegerVector  {

    /**
     * Data
     */
    protected int[] data;

    /**
     * Indices to data
     */
    int[] index;

    /**
     * How much has been used
     */
    int used;
    
    int size;

    /**
     * Constructor for IntegerVector.
     * 
     * @param size
     *            Size of the vector
     * @param nz
     *            Initial number of non-zeros
     */
    public IntegerVector(int size, int nz) {
        this.size=size;
        data = new int[nz];
        index = new int[nz];
    }

    /**
     * Constructor for IntegerVector, and copies the contents from the supplied
     * vector.
     * 
     * @param x
     *            Vector to copy from
     */
    public IntegerVector(IntegerVector x) {
            size = x.size();
            data = x.getData().clone();
            index = x.getIndex().clone();
            used = x.getUsed();
    }
    
    protected void checkSize(IntegerVector y) {
    	   if (size != y.size())
    	       throw new IndexOutOfBoundsException("x.size != y.size (" + size
    	                     + " != " + y.size() + ")");
    }
    
    protected void check(int idx) {
        if (idx < 0)
              throw new IndexOutOfBoundsException("index is negative (" + idx+ ")");
           if (idx >= size)
   	             throw new IndexOutOfBoundsException("index >= size (" + idx
                       + " >= " + size + ")");
    }
    	 
    public int size() {
		return size;
	}

	/**
     * Constructor for IntegerVector. Zero initial pre-allocation
     * 
     * @param size
     *            Size of the vector
     */
    public IntegerVector(int size) {
        this(size, 0);
    }

    /**
     * Constructor for IntegerVector
     * 
     * @param size
     *            Size of the vector
     * @param index
     *            Indices of the vector
     * @param data
     *            Entries of the vector
     */
    public IntegerVector(int size, int[] index, int[] data) {
        this.size=size;
        if (index.length != data.length)
            throw new IllegalArgumentException("index.length != data.length");
        used = index.length;
        this.index = index.clone();
        this.data = data.clone();
    }



    
    public void set(int idx, int value) {
        check(idx);
        // TODO: should we check against zero when setting zeros?
        if (value==0) return;
        int i = getIndex(idx);
        data[i] = value;
    }

    
    public void add(int idx, int value) {
        check(idx);

        int i = getIndex(idx);
        data[i] += value;
    }

    
    public int get(int idx) {
        check(idx);
        if (used==0) return 0;
        int in = Arrays.binarySearch(index,0,used,idx);
        if (in >= 0)
            return data[in];
        return 0;
    }

    /**
     * Tries to find the index. If it is not found, a reallocation is done, and
     * a new index is returned.
     */
    private int getIndex(int ind) {

        // Try to find column index
        int i = Arrays.binarySearch(index,  0, used,ind);
        
        // Found
        if (i >= 0)
            return i;
        
        i=-i-1;
        
        int[] newIndex = index;
        int[] newData = data;

        // Check available memory
        if (++used > data.length) {

            // If zero-length, use new length of 1, else double the bandwidth
            int newLength = data.length != 0 ? data.length << 1 : 1;

            // Copy existing data into new arrays
            newIndex = new int[newLength];
            newData = new int[newLength];
            System.arraycopy(index, 0, newIndex, 0, i);
            System.arraycopy(data, 0, newData, 0, i);
        }

        // All ok, make room for insertion
        System.arraycopy(index, i, newIndex, i + 1, used - i - 1);
        System.arraycopy(data, i, newData, i + 1, used - i - 1);

        // Put in new structure
        newIndex[i] = ind;
        newData[i] = 0;

        // Update pointers
        index = newIndex;
        data = newData;

        // Return insertion index
        return i;
    }

    
    public IntegerVector copy() {
        return new IntegerVector(this);
    }

    
    public IntegerVector zero() {
        java.util.Arrays.fill(data, 0);
		used = 0;
        return this;
    }

    
    /*public int sumValues(int init,int end){
    	int in = Arrays.binarySearch(this.index, init, 0, used);
    	int retval=0;
    	if (in<0){
    		in=-in-1;
    	}
    	while(index[in]<=end){
    		retval+=data[in];
    		in++;
    	}
    	return retval;
    }*/
    
    public IntegerVector scale(int alpha) {
        // Quick return if possible
        if (alpha == 0)
            return zero();
        else if (alpha == 1)
            return this;

        for (int i = 0; i < used; ++i)
            data[i] *= alpha;

        return this;
    }

    
    public int dot(IntegerVector y) {
        checkSize(y);
        int[] yd = y.getData();
        int ret = 0;
        for (int i = 0; i < used; ++i)
            ret += data[i] * yd[index[i]];
        return ret;
    }

    
    public int norm1() {
        int sum = 0;
        for (int i = 0; i < used; ++i)
            sum += Math.abs(data[i]);
        return sum;
    }

    
    protected double norm2() {
        double norm = 0;
        for (int i = 0; i < used; ++i)
            norm += data[i] * data[i];
        return Math.sqrt(norm);
    }

    
    protected double norm2_robust() {
        double scale = 0, ssq = 1;
        for (int i = 0; i < used; ++i) {
            if (data[i] != 0) {
                double absxi = Math.abs(data[i]);
                if (scale < absxi) {
                    ssq = 1 + ssq * Math.pow(scale / absxi, 2);
                    scale = absxi;
                } else
                    ssq = ssq + Math.pow(absxi / scale, 2);
            }
        }
        return scale * Math.sqrt(ssq);
    }

    
    protected int normInf() {
        int max = 0;
        for (int i = 0; i < used; ++i)
            max = Math.max(Math.abs(data[i]), max);
        return max;
    }

    /**
     * Returns the internal data
     */
    public int[] getData() {
        return data;
    }

    /**
     * Returns the indices
     */
    public int[] getIndex() {
    	return index;
    	/*
    	
    	// could run compact, or return subarray
    	// compact();
    	int [] indices = new int[used];
    	for (int i = 0 ; i < used; i++) {
    		indices[i] = index[i];
    	}
    	return indices;
    	*/
    }

    /**
     * Number of entries used in the sparse structure
     */
    public int getUsed() {
        return used;
    }

    /**
     * Compacts the vector
     */
    public void compact() {
        int nz = used;
        // ??: why was this originally using cardinality?
        // int nz = Matrices.cardinality(this);

        if (nz < data.length) {
            int[] newIndex = new int[nz];
            int[] newData = new int[nz];

            // Copy only non-zero entries
            for (int i = 0, j = 0; i < data.length; ++i)
                if (data[i] != 0.) {
                    newIndex[j] = index[i];
                    newData[j] = data[i];
                    j++;
                }

            data = newData;
            index = newIndex;
            used = data.length;
        }
    }

    public Iterator<IntegerVectorEntry> iterator() {
        return new IntegerVectorIterator();
    }

    
    public IntegerVector set(IntegerVector y) {
       
        checkSize(y);
       
        if (y.index.length != index.length) {
            data = new int[y.data.length];
            index = new int[y.data.length];
        }
        System.arraycopy(y.data, 0, data, 0, data.length);
        System.arraycopy(y.index, 0, index, 0, index.length);
        used = y.used;
        return this;
    }

    /**
     * Iterator over a sparse vector
     */
    public class IntegerVectorIterator implements Iterator<IntegerVectorEntry> {

        private int cursor;

        private final IntegerVectorEntry entry = new IntegerVectorEntry();

        public boolean hasNext() {
            return cursor < used;
        }

        public IntegerVectorEntry next() {
            entry.update(cursor);

            cursor++;

            return entry;
        }

        public void remove() {
            entry.set(0);
        }

    }

    /**
     * Entry of a sparse vector
     */
    public class IntegerVectorEntry {

        private int cursor;

        public void update(int cursor) {
            this.cursor = cursor;
        }

        public int index() {
            return index[cursor];
        }

        public int get() {
            return data[cursor];
        }

        public void set(int value) {
            data[cursor] = value;
        }

    }

    // Dense Print (maybe we need other for sparse print)
	public String toString(int v1, int v2) {
		String retval="";
		for (int i=v1;i<v2;i++){
			retval+=get(i) + " ";
		}
		return retval;
	}
	
	public String toString() {
		return this.toString(0,size);
	}

	public void add(IntegerVector vector) {
        int[] datanew = new int[size];
        int[] indexnew = new int[size];
        int it=0;
        int iv=0;
        int in=0;
        boolean add;
        for (int i=0;i<size;i++){
        	add=false;
        	if (it < used && i==index[it]){
        		datanew[in]=data[it];
        		it++;
        		add=true;
        	}
        	if (iv < vector.used && i==vector.index[iv]){
           		datanew[in]+=vector.data[iv];
        		iv++;
        		add=true;
        	}
        	if (add){
        		indexnew[in]=i;
        	   	in++;
        	}
        }
        this.used=in;
        index=new int[in];
        data=new int[in];
        System.arraycopy(indexnew, 0, index, 0, in);
        System.arraycopy(datanew, 0, data, 0, in);
	}


}
