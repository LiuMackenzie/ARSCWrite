package com.ly.common;

import java.util.HashMap;

public class TypeDefine {
	static HashMap typeDefine;

	public static String getInstance(String typeHex) {
		if (typeDefine == null)
			return init().get(typeHex).toString();
		else
			return typeDefine.get(typeHex).toString();
	}

	private static HashMap init() {
		typeDefine = new HashMap();
		typeDefine.put("00 00", "RES_NULL_TYPE");
		typeDefine.put("01 00", "RES_STRING_POOL_TYPE");
		typeDefine.put("02 00", "RES_TABLE_TYPE");
		typeDefine.put("03 00", "RES_XML_TYPE");

		// Chunk types in RES_XML_TYPE
		typeDefine.put("00 01", "RES_XML_FIRST_CHUNK_TYPE");
		typeDefine.put("01 01", "RES_XML_END_NAMESPACE_TYPE");
		typeDefine.put("02 01", "RES_XML_START_ELEMENT_TYPE");
		typeDefine.put("03 01", "RES_XML_END_ELEMENT_TYPE");
		typeDefine.put("04 01", "RES_XML_CDATA_TYPE");
		typeDefine.put("7F 01", "RES_XML_LAST_CHUNK_TYPE");
		// This contains a uint32_t array mapping strings in the string
		// pool back to resource identifiers. It is optional.
		typeDefine.put("80 01", "RES_XML_RESOURCE_MAP_TYPE");
		// Chunk types in RES_TABLE_TYPE
		typeDefine.put("00 02", "RES_TABLE_PACKAGE_TYPE");
		typeDefine.put("01 02", "RES_TABLE_TYPE_TYPE");
		typeDefine.put("02 02", "RES_TABLE_TYPE_SPEC_TYPE");
		typeDefine.put("03 02", "RES_TABLE_LIBRARY_TYPE");
		return typeDefine;
	}
}
