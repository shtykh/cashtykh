package shtykh.tweets.frequent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shtykh on 20/02/15.
 */
public class Context<C> {
	private static Object mutex = new Object();
	private static Map<Class, Context> instances;

	private final Histogram<C> histogram;

	private Context() {
		histogram = new Histogram<>();
	}
	
	public static <T> Context getInstance(Class<T> tClass) {
		if (instances == null) {
			synchronized (mutex) {
				if (instances == null) {
					instances = new ConcurrentHashMap<>();
				}
			}
		}
		Context context = instances.get(tClass);
		if (context == null) {
			synchronized (mutex) {
				if (context == null) {
					context = new Context<T>();
					instances.put(tClass, context);
				}
			}
		}
		return context;
	}
	
	public synchronized static int incFrequency(Object obj) {
		Context instance = getInstance(obj.getClass());
		instance.histogram.add(obj);
		return instance.histogram.getFrequency(obj);
	}

	public static int getFrequency(Object obj) {
		return getInstance(obj.getClass()).histogram.getFrequency(obj);
	}

	public static <T> List<T> getNMostFrequent(int n, Class<T> tClass) {
		return getInstance(tClass).histogram.getNMostFrequent(n);
	}

	public static <T> List<T> getAllFrequent(Class<T> tClass) {
		return getInstance(tClass).histogram.getAllFrequent();
	}

	@Override
	public String toString() {
		return histogram.toString();
	}
}
