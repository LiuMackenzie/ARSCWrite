package com.ly.write;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ly.common.TypeDefine;
import com.ly.tool.ToolCutCommon;
import com.ly.tool.ToolFileSource;
import com.ly.tool.ToolTypeChange;

public class ARSCWrite {
	public static void main(String args[]) {
		devideService("resource/resources.arsc");
		// component(8);

	}

	// 把写在disk上的内容组装
	public static void component(int type_count) {
		ArrayList<byte[]> typeSpecList = new ArrayList<byte[]>();
		byte[] resTableHeader = ToolFileSource.getFileSources("data/resTableHeader.arsc");
		byte[] publicStringPool = ToolFileSource.getFileSources("data/publicStringPool.arsc");
		byte[] packageDetail = ToolFileSource.getFileSources("data/packageDetail.arsc");
		byte[] publicStylePool = ToolFileSource.getFileSources("data/publicStylePool.arsc");
		byte[] publicStringKeyPool = ToolFileSource.getFileSources("data/publicStringKeyPool.arsc");
		for (int i = 0; i < type_count; i++) {
			typeSpecList.add(ToolFileSource.getFileSources("data/typeSpec_" + i + ".arsc"));
		}
		// 重写arsc文件
		rewriteMethod(resTableHeader, publicStringPool, packageDetail, publicStylePool, publicStringKeyPool,
				typeSpecList, "resources");
	}

	// 把原始的resources写在disk上
	public static int devideService(String fileName) {
		byte[] srcByte = ToolFileSource.getFileSources(fileName);
		// 定义长度，每次从这个长度开始截取
		int flag = 0;
		// 将resTableHeader,StringPool,package
		byte[] resTableHeader = parseResTableHeader(srcByte);
		flag = 0xC;
		byte[] publicStringPool = parseResStringPool(srcByte, flag);
		flag = flag + publicStringPool.length;
		byte[] packageDetail = parsePackage(srcByte, flag);
		flag = flag + packageDetail.length;
		byte[] publicStylePool = parseResStringPool(srcByte, flag);
		flag = flag + publicStylePool.length;
		byte[] publicStringKeyPool = parseResStringPool(srcByte, flag);
		flag = flag + publicStringKeyPool.length;
		// 这里开始解析的是TypeSpec;
		int beginType = flag;
		int endType = 0;
		ArrayList<byte[]> typeSpecList = new ArrayList<byte[]>();
		byte[] typeSpec;
		for (int i = flag + 1; i < srcByte.length - 2; i++) {
			if (srcByte[i] == 0X02 && srcByte[i + 1] == 0X02 && srcByte[i + 2] == 0X10) {
				endType = i - 1;
				typeSpec = ToolCutCommon.getByteArray(beginType, endType, srcByte);
				typeSpecList.add(typeSpec);
				beginType = i;
			}
		}
		typeSpec = ToolCutCommon.getByteArray(beginType, srcByte.length - 1, srcByte);
		typeSpecList.add(typeSpec);

		// 以上注释内容可以将分开的块持久化入disk_begin
		for (int i = 0; i < typeSpecList.size(); i++) {
			ToolFileSource.writeFile(typeSpecList.get(i), "data/typeSpec_" + i + ".arsc");
		}
		ToolFileSource.writeFile(resTableHeader, "data/resTableHeader.arsc");
		ToolFileSource.writeFile(publicStringPool, "data/publicStringPool.arsc");
		ToolFileSource.writeFile(packageDetail, "data/packageDetail.arsc");
		ToolFileSource.writeFile(publicStylePool, "data/publicStylePool.arsc");
		ToolFileSource.writeFile(publicStringKeyPool, "data/publicStringKeyPool.arsc");
		// 以上注释内容可以将分开的块持久化入disk_end
		return typeSpecList.size();
	}

	public static byte[] parseResTableHeader(byte[] srcByte) {
		return ToolCutCommon.getByteArray(0, 0xB, srcByte);
	}

	public static byte[] parseResStringPool(byte[] srcByte, int beforeSize) {

		int size = ToolTypeChange.byte2int(ToolCutCommon.getByteArray(beforeSize + 4, beforeSize + 7, srcByte));
		return ToolCutCommon.getByteArray(beforeSize, beforeSize + size - 1, srcByte);
	}

	public static byte[] parsePackage(byte[] srcByte, int beforeSize) {
		int size = ToolTypeChange.byte2Short(ToolCutCommon.getByteArray(beforeSize + 2, beforeSize + 3, srcByte));
		return ToolCutCommon.getByteArray(beforeSize, beforeSize + size - 1, srcByte);

	}

	// 修改整个arsc文件大小
	public static void changeARSCSize(byte[] resHeader, int size) {
		int mid_size = ToolTypeChange.byteArrayToIntOnlyFor4Byte(ToolCutCommon.getByteArray(4, 7, resHeader));
		mid_size = mid_size + size;
		byte[] midSrc = ToolTypeChange.intToByteArray(mid_size);
		for (int i = 7, j = 0; i > 3; i--, j++) {
			resHeader[i] = midSrc[j];
		}
	}

	public static int getStringCount(byte[] src) {
		int StringCount = ToolTypeChange.byte2Short(ToolCutCommon.getByteArray(8, 0XB, src));
		return StringCount;

	}

	public static int getOneHeaderSize(byte[] src) {
		int headerSize = ToolTypeChange.byte2Short(ToolCutCommon.getByteArray(2, 3, src));
		return headerSize;

	}

	public static int getOneAllSize(byte[] src) {
		int size = ToolTypeChange.byteArrayToIntOnlyFor4Byte(ToolCutCommon.getByteArray(4, 7, src));
		return size;
	}

	public static void rewriteMethod(byte[] resTableHeader, byte[] publicStringPool, byte[] packageDetail,
			byte[] publicStylePool, byte[] publicStringKeyPool, ArrayList<byte[]> typeSpecList, String fileName) {
		// 获取typeSpec的长度
		int typeSpecSrcLength = 0;
		for (int i = 0; i < typeSpecList.size(); i++) {
			typeSpecSrcLength = typeSpecSrcLength + typeSpecList.get(i).length;
		}
		// 先把所有数据连在一起
		byte[] contentInBytes = new byte[resTableHeader.length + publicStringPool.length + packageDetail.length
				+ publicStylePool.length + publicStringKeyPool.length + typeSpecSrcLength];
		int flag = 0;
		System.arraycopy(resTableHeader, 0, contentInBytes, 0, resTableHeader.length);
		flag = resTableHeader.length;
		System.arraycopy(publicStringPool, 0, contentInBytes, flag, publicStringPool.length);
		flag = flag + publicStringPool.length;
		System.arraycopy(packageDetail, 0, contentInBytes, flag, packageDetail.length);
		flag = flag + packageDetail.length;
		System.arraycopy(publicStylePool, 0, contentInBytes, flag, publicStylePool.length);
		flag = flag + publicStylePool.length;
		System.arraycopy(publicStringKeyPool, 0, contentInBytes, flag, publicStringKeyPool.length);
		flag = flag + publicStringKeyPool.length;
		for (int i = 0; i < typeSpecList.size(); i++) {
			System.arraycopy(typeSpecList.get(i), 0, contentInBytes, flag, typeSpecList.get(i).length);
			flag = flag + typeSpecList.get(i).length;
		}

		FileOutputStream fop = null;
		File file;
		try {

			file = new File("data/" + fileName + ".arsc");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
