package libpomdp.parser.java;

import libpomdp.common.java.BeliefMdp;
import libpomdp.common.java.Pomdp;
import libpomdp.common.java.add.PomdpAdd;
import libpomdp.common.java.standard.BeliefMdpStandard;
import libpomdp.common.java.standard.PomdpStandard;

public class FileParser {
	
	public static final int PARSE_CASSANDRA_POMDP=0;
	public static final int PARSE_SPUDD=1;
	
	public static Pomdp loadPomdp(String filename,int filetype) throws Exception{
		Pomdp newPomdp;
		switch(filetype){
		case PARSE_CASSANDRA_POMDP:
			DotPomdpParserStandard.parse(filename);
			PomdpSpecStandard data=DotPomdpParserStandard.getSpec();
			String actStr[]=null;
			if (data.actList != null)
				actStr= (String []) data.actList.toArray (new String [data.actList.size ()]);
			String obsStr[]=null; 
			if (data.obsList != null)   
			    obsStr = (String []) data.obsList.toArray (new String [data.obsList.size ()]);
			String staStr[]=null; 
			if (data.staList != null)   
			    obsStr = (String []) data.staList.toArray (new String [data.staList.size ()]);
			newPomdp=new PomdpStandard(data.O, data.T,data.R, data.nrSta, data.nrAct, data.nrObs, data.discount,staStr,actStr,obsStr,data.startState);
			break;
		case PARSE_SPUDD:
				newPomdp=new PomdpAdd(filename);
			break;
			default:
				throw new Exception("No such filetype (Not Implemented Yet)\n");
		}
		
		return newPomdp;
		
	}

	public static BeliefMdp loadBeliefMdp(String filename,int filetype) throws Exception{
		if (filetype==PARSE_SPUDD)
			throw new Exception("Cannot create a belief over a ADD representation (Not Implemented Yet)\n");
		Pomdp pomdp=loadPomdp(filename,filetype);
		BeliefMdp bPomdp;
		bPomdp=new BeliefMdpStandard((PomdpStandard)pomdp);
		return bPomdp;
	}
}
