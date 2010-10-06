package libpomdp.parser.java;

import java.lang.reflect.Array;
import java.util.ArrayList;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrices;

import libpomdp.common.java.BeliefMdp;
import libpomdp.common.java.Pomdp;
import libpomdp.common.java.add.PomdpAdd;
import libpomdp.common.java.dense.BeliefMdpDense;
import libpomdp.common.java.dense.PomdpDense;
import libpomdp.common.java.sparse.BeliefMdpSparse;
import libpomdp.common.java.sparse.PomdpSparse;

public class FileParser {
	
	public static final int PARSE_CASSANDRA_POMDP=0;
	public static final int PARSE_SPUDD=1;
	
	@SuppressWarnings("unchecked")
	public static Pomdp loadPomdp(String filename,int filetype,boolean sparsity) throws Exception{
		Pomdp newPomdp;
		switch(filetype){
		case PARSE_CASSANDRA_POMDP:
			DotPomdpParserSparse.parse(filename);
			PomdpSpecSparse data=DotPomdpParserSparse.getSpec();
			String actStr [] = (String []) data.actList.toArray (new String [data.actList.size ()]);
			String obsStr [] = (String []) data.obsList.toArray (new String [data.obsList.size ()]);
			if (sparsity){
				newPomdp=new PomdpSparse(data.O, data.T, data.R, data.nrSta, data.nrAct, data.nrObs, data.discount,actStr,obsStr,data.startState);
			}
			else{
				DenseVector Rnew[]=new DenseVector[data.R.length];
				for (int i=0;i<data.R.length;i++){
					Rnew[i]=new DenseVector(Matrices.getArray(data.R[i]));
				}
				newPomdp=new PomdpDense(data.O, data.T, Rnew, data.nrSta, data.nrAct, data.nrObs, data.discount,actStr,obsStr,new DenseVector(Matrices.getArray(data.startState)));
			}
				break;
		case PARSE_SPUDD:
				newPomdp=new PomdpAdd(filename);
			break;
			default:
				throw new Exception("No such filetype (Not Implemented Yet)\n");
		}
		
		return newPomdp;
		
	}

	public static BeliefMdp loadBeliefMdp(String filename,int filetype,boolean sparsity) throws Exception{
		if (filetype==PARSE_SPUDD)
			throw new Exception("Cannot create a belief over a ADD representation (Not Implemented Yet)\n");
		Pomdp pomdp=loadPomdp(filename,filetype,sparsity);
		BeliefMdp bPomdp;
		if (pomdp instanceof PomdpDense)
			bPomdp=new BeliefMdpDense((PomdpDense)pomdp);
		else
			bPomdp=new BeliefMdpSparse((PomdpSparse)pomdp);
		return bPomdp;
	}
}
