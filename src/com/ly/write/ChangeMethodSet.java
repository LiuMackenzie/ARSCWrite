package com.ly.write;

import java.util.ArrayList;

import com.ly.disk.ResPackage;
import com.ly.disk.ResTableHeader;
import com.ly.disk.StringPool;
import com.ly.disk.Type;
import com.ly.tool.ToolFileSource;
import com.ly.tool.ToolTypeChange;

public class ChangeMethodSet {
	public static void main(String args[]) {
		// changeKeyStringPoolContent();
		// addType();
		// addOneType();
	}

	// 删除一个Type的全过程，删除一个key，删除一个String
	public static void deleteOneType(String stringPool_set, String keyPook_set, String deleteSrc_del,
			int stringPool_content, int keyPool_content, String typeSpec_fileName) {
		// 增加StringPool的内容，使用changeKeyStringPoolContent方法就可以
		deleteStringPoolContent(stringPool_set, stringPool_content);
		// 增加keyStringPool方法
		deleteKeyStringPoolContent(keyPook_set, keyPool_content);
		// // 增加type内容
		deleteOne(deleteSrc_del, typeSpec_fileName);
	}

	// 添加stringPool的内容
	public static int deleteStringPoolContent(String fileName, int content) {
		StringPool.currentString = new ArrayList<byte[]>();
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.remove(content);
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		// 修改整个header_size文件大小
		ResTableHeader.setResTableHeader(-size_change);
		// 把所有内容组装
		ARSCWrite.component(8);
		return size_change;
	}

	// 仅仅添加keyStringPool的内容
	public static int deleteKeyStringPoolContent(String fileName, int content) {
		StringPool.currentString = new ArrayList<byte[]>();
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.remove(content);
		// 修改package_size大小
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		ResPackage.changePackageSize(-size_change);
		// 修改整个header_size文件大小
		ResTableHeader.setResTableHeader(-size_change);
		// 把所有内容组装
		ARSCWrite.component(8);
		return size_change;
	}

	// 修改一个Type的全过程,修改后的结果可以把android
	public static void changeOneType(String before_fileName, String after_fileName, String target_fileName) {
		Type.changeMethod(before_fileName, after_fileName, target_fileName);
	}

	// 增加一个Type的全过程，插入一个key，一个String
	public static void addOneType(String stringPool_set, String keyPook_set, String addSrc_add,
			String stringPool_content, String keyPool_content, String typeSpec_fileName) {
		// 增加StringPool的内容，使用changeKeyStringPoolContent方法就可以
		addStringPoolContent(stringPool_set, stringPool_content);
		// 增加keyStringPool方法
		addKeyStringPoolContent(keyPook_set, keyPool_content);
		// 增加type内容
		addType(addSrc_add, typeSpec_fileName);
	}

	// 添加stringPool的内容
	public static int addStringPoolContent(String fileName, String content) {
		StringPool.currentString = new ArrayList<byte[]>();
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.add(content.getBytes());
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		// 修改整个header_size文件大小
		ResTableHeader.setResTableHeader(-size_change);
		// 把所有内容组装
		ARSCWrite.component(8);
		return size_change;
	}

	// 仅仅添加keyStringPool的内容
	public static int addKeyStringPoolContent(String fileName, String content) {
		StringPool.currentString = new ArrayList<byte[]>();
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.add(content.getBytes());
		// 修改package_size大小
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		ResPackage.changePackageSize(-size_change);
		// 修改整个header_size文件大小
		ResTableHeader.setResTableHeader(-size_change);
		// 把所有内容组装
		ARSCWrite.component(8);
		return size_change;
	}

	// 仅仅修改keyStringPool的内容，如将button改为but
	public static void changeKeyStringPoolContent(String fileName, int change_set, String change_str) {
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.set(change_set, change_str.getBytes());
		// 把修改的内容放入list,获取修改后和原始的size差别
		// 修改package_size大小
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		ResPackage.changePackageSize(-size_change);
		// 修改整个header_size文件大小
		ResTableHeader.setResTableHeader(-size_change);
		// 把所有内容组装
		ARSCWrite.component(8);
	}

	// 仅仅修改StringPool的内容，如将button改为but
	public static void changeStringPoolContent(String fileName, int change_set, String change_str) {
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.set(change_set, change_str.getBytes());
		// 把修改的内容放入list,获取修改后和原始的size差别
		// 修改package_size大小
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		// 修改整个header_size文件大小
		ResTableHeader.setResTableHeader(-size_change);
		// 把所有内容组装
		ARSCWrite.component(8);
	}

	// 按照具体需求，定制修改StringPoolKey的内容
	public static void changeKeyStringPoolContent_count(String fileName, ArrayList<byte[]> list) {
		ArrayList<byte[]> currentString_c = list;
		// 修改package_size大小
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		ResPackage.changePackageSize(-size_change);
		// 修改整个header_size文件大小
		ResTableHeader.setResTableHeader(-size_change);
		// 把所有内容组装
		ARSCWrite.component(8);
	}

	public static void addType(String fileName, String typeSpec_fileName) {
		// 先将内容加入到对应的type中
		byte[] add_byte = ToolFileSource.getFileSources(fileName);
		int size = add_byte.length + 8;
		byte[] res = Type.appendMethod(add_byte, typeSpec_fileName);
		ToolFileSource.writeFile(res, typeSpec_fileName);
		// 修改package_size大小
		ResPackage.changePackageSize(size);
		// 修改整个header_size文件大小
		ResTableHeader.setResTableHeader(size);
		// 把所有内容组装
		ARSCWrite.component(8);
	}

	public static void deleteOne(String delete_fileName, String or_fileName) {
		System.out.println("deleteOne");
		int size = ToolFileSource.getFileSources(delete_fileName).length + 8;
		Type.deleteOneType(delete_fileName, or_fileName);
		// 修改package_size大小
		ResPackage.changePackageSize(-size);
		// 修改整个header_size文件大小
		ResTableHeader.setResTableHeader(-size);

	}

}
