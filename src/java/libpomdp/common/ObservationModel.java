package libpomdp.common;

public abstract class ObservationModel {
	public abstract AlphaVector project(AlphaVector alpha, int a);
	public abstract CustomMatrix getMatrix(int a);
}
