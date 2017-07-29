package com.game.utils.common.clazz;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.game.utils.common.date.UtilDateTime;

/**
 * 扩展Apache Commons BeanUtils, 提供一些反射方面缺失功能的封装.
 */
@SuppressWarnings("unchecked")
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	protected static final Log logger = LogFactory.getLog(BeanUtils.class);

	private BeanUtils() {
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * 
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Object object, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);
		return getDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * 
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Class clazz, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(clazz);
		Assert.hasText(propertyName);
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName()
				+ '.' + propertyName);
	}

	/**
	 * 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 * 
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 */
	public static Object forceGetProperty(Object object, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = getDeclaredField(object, propertyName);

		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.info("error wont' happen");
		}
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 暴力设置对象变量值,忽略private,protected修饰符的限制.
	 * 
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 */
	public static void forceSetProperty(Object object, String propertyName,
			Object newValue) throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (IllegalAccessException e) {
			logger.info("Error won't happen");
		}
		field.setAccessible(accessible);
	}

	/**
	 * 暴力调用对象函数,忽略private,protected修饰符的限制.
	 * 
	 * @throws NoSuchMethodException
	 *             如果没有该Method时抛出.
	 */
	public static Object invokePrivateMethod(Object object, String methodName,
			Object... params) throws NoSuchMethodException {
		Assert.notNull(object);
		Assert.hasText(methodName);
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				// 方法不在当前类定义,继续向上转型
			}
		}

		if (method == null)
			throw new NoSuchMethodException("No Such Method:"
					+ clazz.getSimpleName() + methodName);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		method.setAccessible(accessible);
		return result;
	}

	/**
	 * 通过集合中的属性与值暴力设置对象变量值, 忽略private,protected修饰符的限制. *
	 * 
	 * @param bean
	 * @param data
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NumberFormatException
	 */
	public static void forceSetProperty(Object bean, Map<String, String> data)
			throws NumberFormatException, InstantiationException,
			IllegalAccessException {

		Assert.notNull(data);

		Set<String> keySet = data.keySet();
		Assert.notNull(keySet);

		Iterator<String> keys = keySet.iterator();
		while (keys.hasNext()) {
			String property = keys.next();
			Class type = null;
			try {
				type = getPropertyType(bean.getClass(), property);
			} catch (NoSuchFieldException e) {
				// bean中不存在指定的属性
				continue;
			}

			String value = data.get(property);
			Object propertyValue = null;

			if (type.equals(Long.class)) {

				propertyValue = new Long(value);

			} else if (type.equals(Float.class)) {

				propertyValue = new Float(value);

			} else if (type.equals(Double.class)) {

				propertyValue = new Double(value);

			} else if (type.newInstance() instanceof java.util.Date) {

				propertyValue = UtilDateTime.toDataTime(value);

			} else {

				propertyValue = value;

			}

			try {

				String methodName = getSetterName(property);
				invokePrivateMethod(bean, methodName, propertyValue);

			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				//logger.error(ExceptionUtils.formatStackTrace(e));
			}
		}
	}

	/**
	 * 将字符型数据根据类型转换为基本类型的对象
	 * 
	 * @param type
	 * @param value
	 * @return
	 * @throws NumberFormatException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Serializable convertByClass(Class type, String value)
			throws NumberFormatException, InstantiationException,
			IllegalAccessException {
		Serializable propertyValue = null;

		if (type.newInstance() instanceof Long) {
			propertyValue = new Long(value);
		} else if (type.newInstance() instanceof java.util.Date) {
			propertyValue = UtilDateTime.toDataTime(value);
		} else {
			propertyValue = value;
		}

		return propertyValue;
	}

	/**
	 * 按Filed的类型取得Field列表.
	 */

	public static List<Field> getFieldsByType(Object object, Class type) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(type)) {
				list.add(field);
			}
		}
		return list;
	}

	/**
	 * 按FiledName获得Field的类型.
	 */
	public static Class getPropertyType(Class type, String name)
			throws NoSuchFieldException {
		return getDeclaredField(type, name).getType();
	}

	/**
	 * 获得field的getter函数名称.
	 */
	public static String getSetterName(String fieldName) {
		Assert.hasText(fieldName, "FieldName required");
		return "set" + StringUtils.capitalize(fieldName);
	}

	/**
	 * 获得field的getter函数名称.
	 */
	public static String getGetterName(Class type, String fieldName) {
		Assert.notNull(type, "Type required");
		Assert.hasText(fieldName, "FieldName required");

		if (type.getName().equals("boolean")) {
			return "is" + StringUtils.capitalize(fieldName);
		} else {
			return "get" + StringUtils.capitalize(fieldName);
		}
	}

	/**
	 * 获得field的getter函数,如果找不到该方法,返回null.
	 */
	public static Method getGetterMethod(Class type, String fieldName) {
		try {
			return type.getMethod(getGetterName(type, fieldName));
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/*public static void updateObject(Object spojo, Object tpojo)
			throws Exception {
		PropertyDescriptor[] originFields = PropertyUtils
				.getPropertyDescriptors(spojo.getClass());
		for (int i = 0; i < originFields.length; i++) {
			String originFieldName = originFields[i].getName(); // origin字段名称
			Class originFieldType = originFields[i].getPropertyType(); // origin字段类型
			if (originFieldType == null || originFieldType.equals(Class.class)) {
				continue;
			}
			// 分支1：基本类型，如：String Long Integer Date等
			if (isBaseType(originFieldType)) {
				Object originFieldValue = Ognl.getValue(originFieldName, spojo);
				if (originFieldValue != null) {
					Ognl.setValue(originFieldName, tpojo, originFieldValue);
				} else if (!originFieldType.equals(String.class)) {
					Ognl.setValue(originFieldName, tpojo, originFieldValue);
				}
				continue;
			}
		}
	}*/

	public static boolean isBaseType(Class type) {
		if (type == null) {
			return false;
		}
		if (type.equals(Long.class) || type.equals(String.class)
				|| type.equals(Boolean.class) || type.equals(Integer.class)
				|| type.equals(Double.class) || type.equals(Float.class)
				|| type.equals(java.sql.Blob.class)
				|| type.equals(java.sql.Clob.class)
				|| type.equals(java.util.Date.class)
				|| type.equals(java.sql.Date.class)
				|| type.equals(double.class)
				|| type.equals(int.class)
				|| type.equals(float.class)
				|| type.equals(boolean.class)
				|| type.equals(long.class)
				|| type.equals(java.math.BigDecimal.class)
				|| type.equals(java.math.BigInteger.class)) {
			return true;
		}
		return false;
	}
}
