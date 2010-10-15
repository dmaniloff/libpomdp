package libpomdp.common.add.symbolic;

import java.util.LinkedHashMap;
import java.util.Map;

class CacheMap extends LinkedHashMap {
	/**
 * 
 */
private static final long serialVersionUID = 1L;
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