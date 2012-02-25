package timemachine.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is a singleton id generator, and it's thread safe. User may instantiate any number of time,
 * and it would still use the singleton static ids storage.
 */
public class MemoryIdGenerator {
	private static final String DEFAULT_CATEGORY = MemoryIdGenerator.class.getName();
	private static Map<String, Set<Long>> idsPerCategory = new HashMap<String, Set<Long>>();
	
	public synchronized Long generateId() {
		return generateId(DEFAULT_CATEGORY);
	}
	
	public synchronized Long generateId(String category) {
		Set<Long> ids = idsPerCategory.get(category);
		if (ids == null) {
			ids = new HashSet<Long>();
			idsPerCategory.put(category, ids);
		}
		Long newId = ids.size() + 1L;
		ids.add(newId);
		return newId;
	}
}
