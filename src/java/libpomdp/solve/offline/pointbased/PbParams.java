package libpomdp.solve.offline.pointbased;

public class PbParams {
	protected int backupMethod;
	protected int expandMethod;
	protected int backupHorizon;
	protected int maxTotalPoints;
	protected int maxNewPoints;
	protected boolean newPointsOnly;
	
	public static final int BACKUP_SYNC_FULL = 1;
	public static final int BACKUP_SYNC_NEWPOINTS = 2;
	public static final int BACKUP_ASYNC_FULL = 3;
	public static final int BACKUP_ASYNC_NEWPOINTS = 4;
	
	public static final int EXPAND_GREEDY_ERROR_REDUCTION = 1;
	public static final int EXPAND_EXPLORATORY_ACTION = 2;
	public static final int EXPAND_RANDOM_EXPLORE_STATIC = 3;
	public static final int EXPAND_RANDOM_EXPLORE_DYNAMIC = 4;
	
	public PbParams(int backupMethod, int expandMethod,
			int backupHorizon, int maxTotalPoints, int maxNewPoints,
			boolean newPointsOnly) {
		super();
		this.backupMethod = backupMethod;
		this.expandMethod = expandMethod;
		this.backupHorizon = backupHorizon;
		this.maxTotalPoints = maxTotalPoints;
		this.maxNewPoints = maxNewPoints;
		this.newPointsOnly = newPointsOnly;
	}

	public PbParams(int backupMethod, int expandMethod,int backupHorizon){
		this(backupMethod, expandMethod, backupHorizon,Integer.MAX_VALUE);
	}
	
	
	public PbParams(int backupMethod, int expandMethod,
			int backupHorizon, int maxTotalPoints) {
		this(backupMethod, expandMethod, backupHorizon, maxTotalPoints, Integer.MAX_VALUE , false);
	}
	
	public PbParams(int backupMethod, int expandMethod,
			int backupHorizon, int maxTotalPoints, int maxNewPoints) {
		this(backupMethod, expandMethod, backupHorizon, maxTotalPoints, maxNewPoints, false);
	}
	
	public int getBackupMethod() {
		return backupMethod;
	}

	public void setBackupMethod(int backupMethod) {
		this.backupMethod = backupMethod;
	}

	public int getExpandMethod() {
		return expandMethod;
	}

	public void setExpandMethod(int expandMethod) {
		this.expandMethod = expandMethod;
	}

	public int getBackupHorizon() {
		return backupHorizon;
	}

	public void setBackupHorizon(int backupHorizon) {
		this.backupHorizon = backupHorizon;
	}

	public int getMaxTotalPoints() {
		return maxTotalPoints;
	}

	public void setMaxTotalPoints(int maxTotalPoints) {
		this.maxTotalPoints = maxTotalPoints;
	}

	public int getMaxNewPoints() {
		return maxNewPoints;
	}

	public void setMaxNewPoints(int maxNewPoints) {
		this.maxNewPoints = maxNewPoints;
	}

	public boolean isNewPointsOnly() {
		return newPointsOnly;
	}

	public void setNewPointsOnly(boolean newPointsOnly) {
		this.newPointsOnly = newPointsOnly;
	}

	
		
}
