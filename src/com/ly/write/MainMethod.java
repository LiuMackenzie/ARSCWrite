package com.ly.write;

import java.util.ArrayList;

public class MainMethod {
	public static void main(String args[]) {
		// ����1
		// changeType();
		// ����2.1
		// changeKeyStringPoolContent("data/publicStringKeyPool.arsc", 13,
		// "liaaau_test");
		// ����2.2
		// changeStringPoolContent("data/publicStringPool.arsc", 12, "ly_test");
		// ����3
		// ArrayList<byte[]> list = new ArrayList<byte[]>();
		// list.add("ic_action_search".getBytes());
		// list.add("ic_launcher".getBytes());
		// list.add("activity_main".getBytes());
		// list.add("padding_small".getBytes());
		// list.add("padding_medium".getBytes());
		// list.add("padding_large".getBytes());
		// list.add("app_name".getBytes());
		// changeKeyStringPoolContent_count("data/publicStringKeyPool.arsc",
		// list);
		// ����4
		addOneType("data/publicStringPool.arsc", "data/publicStringKeyPool.arsc", "changeContent/add.arsc",
				"content_add_1", "key_1", "data/typeSpec_4.arsc");
		// ����5
		// deleteOneType("data/publicStringPool.arsc",
		// "data/publicStringKeyPool.arsc", "changeContent/add.arsc", 23, 25,
		// "data/typeSpec_4.arsc");

	}

	// �޸�type�е�resTableEntry��resTableKey����
	public static void changeType() {
		int size = ARSCWrite.devideService("resource/resources.arsc");
		ChangeMethodSet.changeOneType("changeContent/before.arsc", "changeContent/after.arsc", "data/typeSpec_4.arsc");
		ARSCWrite.component(size);
	}

	// �޸���Դ�ص�����_keyStringPool�����ı��䳤��
	public static void changeKeyStringPoolContent(String fileName, int change_set, String change_str) {
		int size = ARSCWrite.devideService("resource/resources.arsc");
		ChangeMethodSet.changeKeyStringPoolContent(fileName, change_set, change_str);
		ARSCWrite.component(size);
	}

	// �޸���Դ�ص�����_StringPool�����ı��䳤��
	public static void changeStringPoolContent(String fileName, int change_set, String change_str) {
		int size = ARSCWrite.devideService("resource/resources.arsc");
		ChangeMethodSet.changeStringPoolContent(fileName, change_set, change_str);
		ARSCWrite.component(size);
	}

	// ����KeyStringPool������
	public static void changeKeyStringPoolContent_count(String fileName, ArrayList<byte[]> list) {
		int size = ARSCWrite.devideService("resource/resources.arsc");
		ChangeMethodSet.changeKeyStringPoolContent_count(fileName, list);
		ARSCWrite.component(size);
	}

	// ����һ��Ԫ��
	public static void addOneType(String stringPool_set, String keyPook_set, String addSrc_add,
			String stringPool_content, String keyPool_content, String typeSpec_fileName) {
		int size = ARSCWrite.devideService("resource/resources.arsc");
		ChangeMethodSet.addOneType(stringPool_set, keyPook_set, addSrc_add, stringPool_content, keyPool_content,
				typeSpec_fileName);
		ARSCWrite.component(size);
	}

	// ɾ��һ��Ԫ��
	public static void deleteOneType(String stringPool_set, String keyPook_set, String deleteSrc_del,
			int stringPool_content, int keyPool_content, String typeSpec_fileName) {
		int size = ARSCWrite.devideService("resource/resources.arsc");
		ChangeMethodSet.deleteOneType(stringPool_set, keyPook_set, deleteSrc_del, stringPool_content, keyPool_content,
				typeSpec_fileName);
		ARSCWrite.component(size);
	}

}
