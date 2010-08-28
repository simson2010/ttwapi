package ttwapi.gae;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

public class GCache {
	private static Logger log = Logger.getLogger(GCache.class.getName());

	public static Object get(String key) {
		Cache cache;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
			return cache.get(key.trim());
		} catch (CacheException e) {
			log.warning(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static boolean put(String key, Object obj, int secs) {
		Map props = new HashMap();
		props.put(GCacheFactory.EXPIRATION_DELTA, secs);
		// props.put(MemcacheService.SetPolicy.SET_ALWAYS, true);
		Cache cache;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(props);
			cache.put(key.trim(), obj);
			return true;
		} catch (CacheException e) {
			log.warning(e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean put(String key, Object obj) {
		Map props = new HashMap();
		props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
		Cache cache;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(props);
			cache.put(key, obj);
			return true;
		} catch (CacheException e) {
			log.warning(e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	public static void clean(String key) {
		MemcacheServiceFactory.getMemcacheService().delete(key);
	}

	public static void clearAll() {
		MemcacheServiceFactory.getMemcacheService().clearAll();
	}
	
	public static boolean findKey(Object key)
	{
		return MemcacheServiceFactory.getMemcacheService().contains(key);
	}
}
