package com.ly.disk;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import com.ly.tool.ToolFileSource;
import com.ly.tool.ToolTypeChange;
import com.ly.write.ARSCWrite;

public class ResPackage {
	public static void main(String args[]) {
		// changePackageName("com.liuyangtest.crackMe7");
		// changeStringKeyOffsetMethod(15);
		changePackageSize(-8);
	}

	// 用来修改packageName的方法
	public static void changePackageName(String packageName) {
		byte[] srcByte = ToolFileSource.getFileSources("data/packageDetail.arsc");
		byte[] packName_byte = packageName.getBytes();
		for (int i = 0, j = 0; j < packName_byte.length; j++) {
			srcByte[0xC + i] = packName_byte[j];
			i = i + 2;
		}

		ToolFileSource.writeFile(srcByte, "data/packageDetail");
	}

	// 修改package的大小
	public static void changePackageSize(int size) {

		byte[] srcByte = ToolFileSource.getFileSources("data/packageDetail.arsc");
		byte[] real_size = new byte[4];
		for (int i = 0; i < 4; i++) {
			real_size[i] = srcByte[i + 0x4];
		}
		size = size + ToolTypeChange.byte2int(real_size);
		byte[] mid_size = ToolTypeChange.intToByteArray(size);
		for (int i = 0, j = 3; i < 4; i++, j--) {
			srcByte[0x4 + i] = mid_size[j];

		}
		ToolFileSource.writeFile(srcByte, "data/packageDetail.arsc");
	}

	// 修改package对应stringkey的内容
	public static void changeStringKeyOffsetMethod(int lastPublickey) {
		byte[] srcByte = ToolFileSource.getFileSources("data/packageDetail.arsc");
		byte[] res = ToolTypeChange.intToByteArray(lastPublickey);
		int begin_byte = 0XC + 128 * 2 + 3 * 4;
		for (int i = 0, j = 3; i < 4; i++, j--) {
			srcByte[begin_byte + i] = res[j];

		}
		ToolFileSource.writeFile(srcByte, "data/packageDetail.arsc");
	}

	// 修改类型池和key池的起始位置
	public static void changeOffsetOfTypeAndKey(int type_offset, int key_offset) {
		byte[] srcByte = ToolFileSource.getFileSources("data/packageDetail.arsc");
		// 类型offset的size
		byte[] type_offset_byte = ToolTypeChange.intToByteArray(type_offset);
		// key的offset的尺寸
		byte[] key_offset_byte = ToolTypeChange.intToByteArray(key_offset);
		int type_begin_byte = 0XC + 128 * 2;
		int key_begin_byte = 0XC + 128 * 2 + 8;
		for (int i = 0, j = 3; i < 4; i++, j--) {
			srcByte[type_begin_byte + i] = type_offset_byte[j];

		}
		for (int i = 0, j = 3; i < 4; i++, j--) {
			srcByte[key_begin_byte + i] = key_offset_byte[j];

		}
		ToolFileSource.writeFile(srcByte, "data/packageDetail.arsc");
	}
}
