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

class CacheMap extends LinkedHashMap {
    public int maxCapacity;

    public CacheMap() {
	super();
	maxCapacity = 10000;
    }

    public CacheMap(int maxCapacity) {
	super();
	this.maxCapacity = maxCapacity;
    }

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxCapacity;
    }
}

public class Global {
    public static int[] varDomSize = null;
    //public static int[] varDomSize = {2,11,9,3,4,5,11,2,6,2,2,2,2,6,2,11,9,3,4,5,11,2,6,2,2,2,2,6};
    //public static int[] varDomSize = {2,11,9,3,4,5,11,2,6,2,2,2,2,6,2,11,9,3,4,5,11,2,6,2,2,2,2,6,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3};

    public static String[] varNames = null;
    public static String[][] valNames = null;
    public static double[][] valNumeric = null;

    // hash tables
    //public static WeakHashMap leafHashtable = new WeakHashMap();
    //public static WeakHashMap nodeHashtable = new WeakHashMap();
    public static CacheMap leafHashtable = new CacheMap();
    public static CacheMap nodeHashtable = new CacheMap();
    public static CacheMap addHashtable = new CacheMap();
    public static CacheMap multHashtable = new CacheMap();
    public static CacheMap maxHashtable = new CacheMap();
    public static CacheMap minHashtable = new CacheMap();
    public static CacheMap dotProductHashtable = new CacheMap();
    public static CacheMap nEdgesHashtable = new CacheMap();
    public static CacheMap nLeavesHashtable = new CacheMap();
    public static CacheMap nNodesHashtable = new CacheMap();

    // random number generator
    public static Random random = new Random();

    public static void main(String args[]) {

	/*
	  HashMap config = new HashMap();
	  config.put(new Integer(1), new Integer(1));
	  config.put(new Integer(4), new Integer(3));
	  int[][] config2 = new int[2][2];
	  config2[0][0] = 1;
	  config2[1][0] = 1;
	  config2[0][1] = 4;
	  config2[1][1] = 3;
	  int[][] config3 = Config.clone(config2);
	  config3 = Config.add(config3,2,1);
	  config3 = Config.add(config3,2,1);
	  int[][] config4 = Config.merge(config3,config2);
	  System.out.println(config.hashCode());
	  System.out.println(config.toString());
	  System.out.println(Config.hashCode(config4));
	  System.out.println(Config.toString(config2));
	  System.out.println(Config.toString(config3));
	  System.out.println(Config.toString(config4));
	  System.out.println(Config.equals(config2,config3));
	  return;
	*/


	//int N = Integer.parseInt(args[0]);
	//int iter = Integer.parseInt(args[1]);
	int N = 100;
	int iter = 1;

	Random numberGenerator = new Random(10101);

	for (long j=0; j<iter; j++) {

	    DD dd1 = DDleaf.myNew(numberGenerator.nextDouble());
	    for (int i=1; i<=N; i++) {
		DD[] children = new DD[3];
		if (numberGenerator.nextInt(2) == 0) {
		    children[0] = DDleaf.myNew(numberGenerator.nextDouble());
		    children[1] = dd1;
		    children[2] = dd1;
		}	
		else {
		    children[0] = dd1;
		    children[1] = dd1;
		    children[2] = DDleaf.myNew(numberGenerator.nextDouble());
		}
		dd1 = DDnode.myNew(i,children);
	    }
						
	    DD dd2 = DDleaf.myNew(numberGenerator.nextDouble());
	    for (int i=1; i<=N; i++) {
		DD[] children = new DD[3];
		if (numberGenerator.nextInt(2) == 0) {
		    children[0] = DDleaf.myNew(numberGenerator.nextDouble());
		    children[1] = dd2;
		    children[2] = dd2;
		}	
		else {
		    children[0] = dd2;
		    children[1] = dd2;
		    children[2] = DDleaf.myNew(numberGenerator.nextDouble());
		}
		dd2 = DDnode.myNew(i,children);
	    }
						

	    DD[] ddArray = new DD[2];
	    ddArray[0] = dd1;
	    ddArray[1] = dd2;
	    //for (int i=0; i<100; i++) {
	    //		ddArray[i] = dd1;
	    //}
						
	    int[] varSet = new int[N];
	    int[][] config = new int[2][N];
	    for (int i=1; i<=N; i++) {
		varSet[i-1] = i;
		config[0][i-1] = i;
		config[1][i-1] = i % 2 +1;
	    }

	    //System.out.println("dd1.display");
            //dd1.display();
            Global.multHashtable.clear();
	    Global.addHashtable.clear();
	    Global.leafHashtable.clear();
	    Global.nodeHashtable.clear();
	    for (int k=0; k<10000; k++) {
		DD dd1r = OP.restrict(dd1,config);
		DD dd2r = OP.restrict(dd2,config);
	    }
            //DD dd = OP.minAddVarElim(ddArray,varSet);
	    //dd1r.display();
	    //dd2r.display();
	    //return;

	    /*
	      Global.multHashtable.clear();
	      Global.addHashtable.clear();
	      Global.leafHashtable.clear();
	      Global.nodeHashtable.clear();
	      dd = OP.addMultVarElim(ddArray,varSet);
	      //System.out.println("dd.display");
	      //dd.display();
	      //System.out.println("config = " + dd.getConfig().toString());
	      //return;

				
						
	      if (false) {

	      for (int i=0; i<1000; i++) {
	      //dd1.display();
	      //dd2.display();
	      dd = OP.add(dd1,dd2);
	      //dd.display();
	      //SortedSet a = dd.getScope();
	      //System.out.println("SortedSet = " + a.toString());
	      int[] b = dd.getVarSet();
	      //System.out.println("VarSet = " + MySet.toString(b));
	      //boolean[] c = dd.getVarMask();
	      //System.out.println("VarMask = " + MySet.maskToString(c));
	      Global.dotProductHashtable.clear();
	      Global.multHashtable.clear();
	      Global.addHashtable.clear();
	      Global.leafHashtable.clear();
	      Global.nodeHashtable.clear();
	      //return;
	      }

	      }
	      //if (false) {

	      boolean[] varMask = new boolean[3*N+1];
	      int[] vars = new int[3*N];
	      for (int varId=1; varId<=3*N; varId++) {
	      varMask[varId]=true;
	      vars[varId-1] = varId;
	      }
	      System.out.println("varMask = " + MySet.maskToString(varMask));
	      System.out.println("vars = " + MySet.toString(vars));
	      for (int i=0; i<1000; i++) {
	      double a = OP.dotProduct(dd1,dd2,vars);
	      //System.out.println("a = " + a);
	      Global.dotProductHashtable.clear();
	      double b = OP.dotProductNoMem(dd1,dd2,varMask);
	      //System.out.println("b = " + b);
	      double c = OP.dotProductNoMem(dd1,dd2,vars);
	      //OP.dotProduct(dd1,dd2,scope);
	      //System.out.println("c = " + c);
	      //return;
	      }
	      //Runtime r = Runtime.getRuntime();
	      //System.err.println("totalMemory = " + r.totalMemory());
	      //System.err.println("freeMemory = " + r.freeMemory());

	      //}
	      */
	}
	Global.dotProductHashtable.clear();
	Global.multHashtable.clear();
	Global.addHashtable.clear();
	Global.leafHashtable.clear();
	Global.nodeHashtable.clear(); 
	System.out.println("done");
    } 

    public static void setVarDomSize(int[] newVarDomSize) {
	Global.varDomSize = newVarDomSize;
    }

    public static void setVarNames(String[] newVarNames) {
	Global.varNames = newVarNames;
    }

    public static void setValNames(int varId, String[] newValNames) {
	if (Global.valNames == null) {
	    Global.valNames = new String[varId][];
	    Global.valNames[varId-1] = newValNames;
	}
	else if (Global.valNames.length < varId) {
	    String[][] tempValNames = new String[varId][];
	    for (int i=0; i<Global.valNames.length; i++) {
		tempValNames[i] = Global.valNames[i];
	    }
	    tempValNames[varId-1] = newValNames;
	    Global.valNames = tempValNames;
	}
	else {
	    Global.valNames[varId-1] = newValNames;
	}
    }

    public static void setValNumeric(int varId, double[] newValNumeric) {
	if (Global.valNumeric == null) {
	    Global.valNumeric = new double[varId][];
	    Global.valNumeric[varId-1] = newValNumeric;
	}
	else if (Global.valNumeric.length < varId) {
	    double[][] tempValNumeric = new double[varId][];
	    for (int i=0; i<Global.valNumeric.length; i++) {
		tempValNumeric[i] = Global.valNumeric[i];
	    }
	    tempValNumeric[varId-1] = newValNumeric;
	    Global.valNumeric = tempValNumeric;
	}
	else {
	    Global.valNumeric[varId-1] = newValNumeric;
	}
    }

    public static int findVarId(String desiredVarName) {
	for (int varId=1; varId<=varNames.length; varId++) {
	    if (varNames[varId-1].compareTo(desiredVarName) == 0) return varId;
	}
	error("No variable named " + desiredVarName);
	return 0;
    }

    public static int findValId(int varId, String desiredValName) {
	for (int valId=1; valId<=valNames[varId-1].length; valId++) {
	    if (valNames[varId-1][valId-1].compareTo(desiredValName) == 0) return valId;
	}
	error("No value named " + desiredValName + " for variable " + Global.varNames[varId-1]);
	return 0;
    }

    public static int[] convertToIds(String[] desiredVarNames) {
	int[] varIds = new int[desiredVarNames.length];
	for (int nameId=0; nameId<desiredVarNames.length; nameId++) {
	    varIds[nameId] = findVarId(desiredVarNames[nameId]);
	}	
	return varIds;
    }

    public static int[][] convertToIds(String[][] stringConfig) {
	int[][] config = new int[2][stringConfig[0].length];
	for (int i=0; i<stringConfig[0].length; i++) {
	    config[0][i] = findVarId(stringConfig[0][i]);
	    config[1][i] = findValId(config[0][i],stringConfig[1][i]);
	}	
	return config;
    }


    private static void error(String errorMessage) {
	System.out.println("ERROR: " + errorMessage);
	//System.exit(1);
    }

    public static void clearHashtables() {
	Global.leafHashtable.clear();
	Global.nodeHashtable.clear();
	Global.addHashtable.clear();
	Global.multHashtable.clear();
	Global.maxHashtable.clear();
	Global.minHashtable.clear();
	Global.dotProductHashtable.clear();
	Global.nEdgesHashtable.clear();
	Global.nLeavesHashtable.clear();
	Global.nNodesHashtable.clear();
	Global.leafHashtable.put(DD.zero, new WeakReference<DD>(DD.zero));
	Global.leafHashtable.put(DD.one, new WeakReference<DD>(DD.one));
    }

    public static void newHashtables() {
	//Global.leafHashtable = new WeakHashMap();
	//Global.nodeHashtable = new WeakHashMap();
	Global.leafHashtable = new CacheMap();
	Global.nodeHashtable = new CacheMap();
	Global.addHashtable = new CacheMap();
	Global.multHashtable = new CacheMap();
	Global.maxHashtable = new CacheMap();
	Global.minHashtable = new CacheMap();
	Global.dotProductHashtable = new CacheMap();
	Global.nEdgesHashtable = new CacheMap();
	Global.nLeavesHashtable = new CacheMap();
	Global.nNodesHashtable = new CacheMap();
	Global.leafHashtable.put(DD.zero, new WeakReference<DD>(DD.zero));
	Global.leafHashtable.put(DD.one, new WeakReference<DD>(DD.one));
    }

    public static int[] getKeyHashCodeSet(HashMap hashMap) {
	Set keySet = hashMap.keySet();
	Iterator iterator = keySet.iterator();
	int[] hashCodeCollection = new int[hashMap.size()];
	int i = 0;
	while (iterator.hasNext()) {
	    hashCodeCollection[i] = iterator.next().hashCode();
	    i += 1;
	}
	return hashCodeCollection;
    }
}
