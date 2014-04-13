package com.glassshare.network;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Haoyu
 * 
 * @param <T>
 */
public abstract class AbstractRequestListener<T> {

	@SuppressWarnings("unchecked")
	public T parse(String response) {
		Class<?> c = this.getGenericType();
		try {
			Constructor<T> constructor = (Constructor<T>) c
					.getDeclaredConstructor(String.class);
			T result = constructor.newInstance(response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Class<?> getGenericType() {
		Type genType = getClass().getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (params.length < 1) {
			throw new RuntimeException("Index outof bounds");
		}
		if (!(params[0] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[0];
	}

	/**
	 * @param bean
	 */
	public abstract void onComplete(final T bean);

	/**
	 * @param networkError
	 */
	public abstract void onNetworkError(final NetworkError networkError);
}
