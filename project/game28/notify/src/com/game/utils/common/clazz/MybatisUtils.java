package com.game.utils.common.clazz;


@SuppressWarnings("unchecked")
public class MybatisUtils {
	
	public static String getMapperName(Class clazz) {
		String result = "";
		String entityName = clazz.getName();
    	String mapperSimpleName = clazz.getSimpleName() + "Mapper";
    	String mapperPackageHead = entityName.substring(0,entityName.lastIndexOf(".model"));
    	String mapperPackageEnd = entityName.substring(entityName.lastIndexOf(".model") + ".model".length(),entityName.lastIndexOf(clazz.getSimpleName()));
        result = mapperPackageHead + ".mapper" + mapperPackageEnd + mapperSimpleName;
		return result;
	}


}
