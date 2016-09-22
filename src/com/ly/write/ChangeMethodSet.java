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

	// ɾ��һ��Type��ȫ���̣�ɾ��һ��key��ɾ��һ��String
	public static void deleteOneType(String stringPool_set, String keyPook_set, String deleteSrc_del,
			int stringPool_content, int keyPool_content, String typeSpec_fileName) {
		// ����StringPool�����ݣ�ʹ��changeKeyStringPoolContent�����Ϳ���
		deleteStringPoolContent(stringPool_set, stringPool_content);
		// ����keyStringPool����
		deleteKeyStringPoolContent(keyPook_set, keyPool_content);
		// // ����type����
		deleteOne(deleteSrc_del, typeSpec_fileName);
	}

	// ���stringPool������
	public static int deleteStringPoolContent(String fileName, int content) {
		StringPool.currentString = new ArrayList<byte[]>();
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.remove(content);
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		// �޸�����header_size�ļ���С
		ResTableHeader.setResTableHeader(-size_change);
		// ������������װ
		ARSCWrite.component(8);
		return size_change;
	}

	// �������keyStringPool������
	public static int deleteKeyStringPoolContent(String fileName, int content) {
		StringPool.currentString = new ArrayList<byte[]>();
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.remove(content);
		// �޸�package_size��С
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		ResPackage.changePackageSize(-size_change);
		// �޸�����header_size�ļ���С
		ResTableHeader.setResTableHeader(-size_change);
		// ������������װ
		ARSCWrite.component(8);
		return size_change;
	}

	// �޸�һ��Type��ȫ����,�޸ĺ�Ľ�����԰�android
	public static void changeOneType(String before_fileName, String after_fileName, String target_fileName) {
		Type.changeMethod(before_fileName, after_fileName, target_fileName);
	}

	// ����һ��Type��ȫ���̣�����һ��key��һ��String
	public static void addOneType(String stringPool_set, String keyPook_set, String addSrc_add,
			String stringPool_content, String keyPool_content, String typeSpec_fileName) {
		// ����StringPool�����ݣ�ʹ��changeKeyStringPoolContent�����Ϳ���
		addStringPoolContent(stringPool_set, stringPool_content);
		// ����keyStringPool����
		addKeyStringPoolContent(keyPook_set, keyPool_content);
		// ����type����
		addType(addSrc_add, typeSpec_fileName);
	}

	// ���stringPool������
	public static int addStringPoolContent(String fileName, String content) {
		StringPool.currentString = new ArrayList<byte[]>();
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.add(content.getBytes());
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		// �޸�����header_size�ļ���С
		ResTableHeader.setResTableHeader(-size_change);
		// ������������װ
		ARSCWrite.component(8);
		return size_change;
	}

	// �������keyStringPool������
	public static int addKeyStringPoolContent(String fileName, String content) {
		StringPool.currentString = new ArrayList<byte[]>();
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.add(content.getBytes());
		// �޸�package_size��С
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		ResPackage.changePackageSize(-size_change);
		// �޸�����header_size�ļ���С
		ResTableHeader.setResTableHeader(-size_change);
		// ������������װ
		ARSCWrite.component(8);
		return size_change;
	}

	// �����޸�keyStringPool�����ݣ��罫button��Ϊbut
	public static void changeKeyStringPoolContent(String fileName, int change_set, String change_str) {
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.set(change_set, change_str.getBytes());
		// ���޸ĵ����ݷ���list,��ȡ�޸ĺ��ԭʼ��size���
		// �޸�package_size��С
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		ResPackage.changePackageSize(-size_change);
		// �޸�����header_size�ļ���С
		ResTableHeader.setResTableHeader(-size_change);
		// ������������װ
		ARSCWrite.component(8);
	}

	// �����޸�StringPool�����ݣ��罫button��Ϊbut
	public static void changeStringPoolContent(String fileName, int change_set, String change_str) {
		StringPool.Initialization(fileName);
		ArrayList<byte[]> currentString_c = StringPool.currentString;
		currentString_c.set(change_set, change_str.getBytes());
		// ���޸ĵ����ݷ���list,��ȡ�޸ĺ��ԭʼ��size���
		// �޸�package_size��С
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		// �޸�����header_size�ļ���С
		ResTableHeader.setResTableHeader(-size_change);
		// ������������װ
		ARSCWrite.component(8);
	}

	// ���վ������󣬶����޸�StringPoolKey������
	public static void changeKeyStringPoolContent_count(String fileName, ArrayList<byte[]> list) {
		ArrayList<byte[]> currentString_c = list;
		// �޸�package_size��С
		int size_change = StringPool.ComponentKeyContent(currentString_c, fileName);
		ResPackage.changePackageSize(-size_change);
		// �޸�����header_size�ļ���С
		ResTableHeader.setResTableHeader(-size_change);
		// ������������װ
		ARSCWrite.component(8);
	}

	public static void addType(String fileName, String typeSpec_fileName) {
		// �Ƚ����ݼ��뵽��Ӧ��type��
		byte[] add_byte = ToolFileSource.getFileSources(fileName);
		int size = add_byte.length + 8;
		byte[] res = Type.appendMethod(add_byte, typeSpec_fileName);
		ToolFileSource.writeFile(res, typeSpec_fileName);
		// �޸�package_size��С
		ResPackage.changePackageSize(size);
		// �޸�����header_size�ļ���С
		ResTableHeader.setResTableHeader(size);
		// ������������װ
		ARSCWrite.component(8);
	}

	public static void deleteOne(String delete_fileName, String or_fileName) {
		System.out.println("deleteOne");
		int size = ToolFileSource.getFileSources(delete_fileName).length + 8;
		Type.deleteOneType(delete_fileName, or_fileName);
		// �޸�package_size��С
		ResPackage.changePackageSize(-size);
		// �޸�����header_size�ļ���С
		ResTableHeader.setResTableHeader(-size);

	}

}
