/**
 * Author: Pascal Poupart (ppoupart@cs.uwaterloo.ca)
 * Reference: Chapter 5 of Poupart's PhD thesis
 * (http://www.cs.uwaterloo.ca/~ppoupart/publications/ut-thesis/ut-thesis.pdf)
 * NOTE: Parts of this code might have been modified for use by libpomdp
 *       - Diego Maniloff
 */

package libpomdp.common.add.symbolic;

class Config {

    public static int[][] empty = new int[2][0];

		/////////////////////////////////////////////////////////
		// hashCode  
		/////////////////////////////////////////////////////////
		public static int hashCode(int[][] config) {

				if (config == null) return 0;

				int hashCode = 0;
				for (int i=0; i<config[0].length; i++) {
						hashCode += config[0][i] + config[1][i];
				}
				return hashCode;
		}

		/////////////////////////////////////////////////////////
		// toString  
		/////////////////////////////////////////////////////////
		public static String toString(int[][] config) {

				if (config == null) return new String("{}");

				String string = new String("{");
				for (int i=0; i<config[0].length; i++) {
						if (Global.varNames == null) 
								string += Integer.toString(config[0][i]) + new String("=");
						else
								string += Global.varNames[config[0][i]-1] + new String("=");
						if (Global.valNames == null) 
								string += Integer.toString(config[1][i]);
						else
								string += Global.valNames[config[0][i]-1][config[1][i]-1];

						//string += Integer.toString(config[0][i]) + new String("=") 
						//		 + Integer.toString(config[1][i]);
						if (i < config[0].length-1) string += new String(", ");
				}
				string += new String("}");
				return string;
		}

		/////////////////////////////////////////////////////////
		// equals  
		/////////////////////////////////////////////////////////
		public static boolean equals(int[][] config1, int[][] config2) {

				if (config1 == config2) return true;
				else if ((config1 != null) && (config2 != null)) {
						if (config1[0].length == config2[0].length) {
								for (int i1=0; i1<config1[0].length; i1++) {
										int i2 = MySet.find(config2[0],config1[0][i1]);
										if (i2 == -1) return false;
										else if (config1[1][i1] != config2[1][i2]) return false;
								}
								return true;
						}
						else return false;
				}
				else return false;
		}

		/////////////////////////////////////////////////////////
		// merge
		/////////////////////////////////////////////////////////
		public static int[][] merge(int[][] config1, int[][] config2) {

				if (config1 == null) return Config.clone(config2);
				if (config2 == null) return Config.clone(config1);

				int counter = 0;
				boolean[] varMask = new boolean[config2[0].length];
				for (int i2=0; i2<config2[0].length; i2++) {
						if (MySet.find(config1[0],config2[0][i2]) == -1) {
								varMask[i2] = true;
								counter++;
						}
						else varMask[i2] = false;
				}

				int[][] config = new int[2][config1[0].length+counter];
				for (int i1=0; i1<config1[0].length; i1++) {
						config[0][i1] = config1[0][i1];
						config[1][i1] = config1[1][i1];
				}
				
				int i = config1[0].length;
				for (int i2=0; i2<varMask.length; i2++) {
						if (varMask[i2]) {
								config[0][i] = config2[0][i2];
								config[1][i] = config2[1][i2];
								i++;
						}
				}

				return config;
		}

		/////////////////////////////////////////////////////////
		// add
		/////////////////////////////////////////////////////////
		public static int[][] add(int[][] config, int var, int val) {

				int[][] newConfig;
				int index = MySet.find(config[0],var);
				if (index == -1) { 
						newConfig = new int[2][config[0].length+1];
						for (int i=0; i<config[0].length; i++) {
								newConfig[0][i] = config[0][i];
								newConfig[1][i] = config[1][i];
						}
						newConfig[0][newConfig[0].length-1] = var;
						newConfig[1][newConfig[1].length-1] = val;
				}
				else {
						newConfig = new int[2][config[0].length];
						for (int i=0; i<config[0].length; i++) {
								newConfig[0][i] = config[0][i];
								newConfig[1][i] = config[1][i];
						}
						newConfig[1][index] = val;  // should double check that values are the same
				}

				return newConfig;
		}

		/////////////////////////////////////////////////////////
		// clone
		/////////////////////////////////////////////////////////
		public static int[][] clone(int[][] config) {

				if (config == null) return null;

				int[][] newConfig = new int[2][config[0].length];
				for (int i=0; i<config[0].length; i++) {
						newConfig[0][i] = config[0][i];
						newConfig[1][i] = config[1][i];
				}

				return newConfig;
		}

		/////////////////////////////////////////////////////////
		// extend
		/////////////////////////////////////////////////////////
		public static int[][] extend(int[][] config, int[] vars) {
		
				int[] missingVars;
				if (config == null) missingVars = vars;
				else missingVars = MySet.diff(vars,config[0]);

				if (missingVars.length == 0) {
						return Config.clone(config);
				}
				else {
						int[][] defaultConfig = new int[2][];
						defaultConfig[0] = missingVars;
						defaultConfig[1] = new int[missingVars.length];
						for (int i=0; i<missingVars.length; i++) {
								defaultConfig[1][i] = 1;
						}
						return Config.merge(config,defaultConfig);
				}
		}

		/////////////////////////////////////////////////////////
		// convert2dd
		/////////////////////////////////////////////////////////
		public static DD convert2dd(int[][] config, double value) {

				if (config == null) return DD.one;

				DD dd = DDleaf.myNew(value);
				for (int i=0; i<config[0].length; i++) {
						int arity = Global.varDomSize[config[0][i]-1];
						DD[] children = new DD[arity];
						for (int j=0; j<arity; j++) {
								if (j+1 == config[1][i]) children[j] = DD.one;
								else children[j] = DD.zero;
						}
						dd = OP.mult(dd,DDnode.myNew(config[0][i],children));
				}
				return dd;
		}

		public static DD convert2dd(int[][] config) {
				return Config.convert2dd(config,1);
		}

		/////////////////////////////////////////////////////////
		// primeVars
		/////////////////////////////////////////////////////////
		public static int[][] primeVars(int[][] config, int val) {

				int[][] primedConfig = new int[2][config[0].length];
				for (int i=0; i<config[0].length; i++) {
						primedConfig[0][i] = config[0][i] + val;
						primedConfig[1][i] = config[1][i];
				}
				return primedConfig;
		}

		/////////////////////////////////////////////////////////
		// removeIth
		/////////////////////////////////////////////////////////
		public static int[][] removeIth(int[][] config, int ith) {

				int[][] results = new int[2][config[0].length-1];
				for (int i=0; i<ith; i++) {
						results[0][i] = config[0][i];
						results[1][i] = config[1][i];
				}
				for (int i=ith+1; i<config[0].length; i++) {
						results[0][i-1] = config[0][i];
						results[1][i-1] = config[1][i];
				}
				return results;
		}
                
		/////////////////////////////////////////////////////////
		// intersection
		/////////////////////////////////////////////////////////
        
        public static int[][] intersection(int[][] config, int[] vars) {
            
            if (config == null || config[0].length == 0 || vars == null || vars.length == 0) return new int[2][0];
            
		    boolean[] mask = new boolean[config[0].length];
		    int count = 0;
		    for (int i = 0; i < config[0].length; i++) {
                if (MySet.find(vars,config[0][i]) >= 0) {
                    count++;
                    mask[i] = true;
                }
                else mask[i] = false;
		    }
		    int[][] result = new int[2][count];

		    count = 0;
		    for (int i=0; i<mask.length; i++) {
                if (mask[i]) {
                    result[0][count] = config[0][i];
                    result[1][count] = config[1][i];
                    count++;
                }
		    }
		    return result;
		}

}

