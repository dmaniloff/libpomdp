package libpomdp.parser;

import java.io.ObjectInputStream;
import java.io.FileInputStream;

import libpomdp.common.BeliefMdp;
import libpomdp.common.Pomdp;
import libpomdp.common.ValueFunction;
import libpomdp.common.add.PomdpAdd;
import libpomdp.common.std.BeliefMdpStd;
import libpomdp.common.std.PomdpStd;

public class FileParser {
	
	public static final int PARSE_CASSANDRA_POMDP=0;
	public static final int PARSE_SPUDD=1;
	
	public static Pomdp loadPomdp(String filename,int filetype) throws Exception{
		Pomdp newPomdp;
		switch(filetype){
		case PARSE_CASSANDRA_POMDP:
			DotPomdpParserStd.parse(filename);
			PomdpSpecStd data=DotPomdpParserStd.getSpec();
			String actStr[]=null;
			if (data.actList != null)
				actStr= (String []) data.actList.toArray (new String [data.actList.size ()]);
			String obsStr[]=null; 
			if (data.obsList != null)   
			    obsStr = (String []) data.obsList.toArray (new String [data.obsList.size ()]);
			String staStr[]=null; 
			if (data.staList != null)   
			    obsStr = (String []) data.staList.toArray (new String [data.staList.size ()]);
			newPomdp=new PomdpStd(data.O, data.T,data.R, data.nrSta, data.nrAct, data.nrObs, data.discount,staStr,actStr,obsStr,data.startState);
			break;
		case PARSE_SPUDD:
				newPomdp=new PomdpAdd(filename);
			break;
			default:
				throw new Exception("No such filetype (Not Implemented Yet)\n");
		}
		
		return newPomdp;
		
	}

    public static ValueFunction loadUpperBound(String pomdpFilename,
                                               int filetype) {
        // figure out bound filename
        String boundSuffix = ( filetype == PARSE_SPUDD ) ?
            ".QMDP_ADD.ser" : ".QMDP_STD.ser";
        String boundFileName = pomdpFilename + boundSuffix;

        // load bound
        return loadValueFunction(boundFileName);

    }

    public static ValueFunction loadLowerBound(String pomdpFilename,
                                               int filetype) {
        // figure out bound filename
        String boundSuffix = ( filetype == PARSE_SPUDD ) ?
            ".BLIND_ADD.ser" : ".BLIND_STD.ser";
        String boundFileName = pomdpFilename + boundSuffix;

        // load bound
        return loadValueFunction(boundFileName);

    }


    public static ValueFunction loadValueFunction(String fileName) {
        // load serialized bound
        ValueFunction vf = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fis);
            vf = (ValueFunction) in.readObject();
            in.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }

        return vf;
    }

	public static BeliefMdp loadBeliefMdp(String filename,int filetype) throws Exception{
		if (filetype==PARSE_SPUDD)
			throw new Exception("Cannot create a belief over a ADD representation (Not Implemented Yet)\n");
		Pomdp pomdp=loadPomdp(filename,filetype);
		BeliefMdp bPomdp;
		bPomdp=new BeliefMdpStd((PomdpStd)pomdp);
		return bPomdp;
	}
}
