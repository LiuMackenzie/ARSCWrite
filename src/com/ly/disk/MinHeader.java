package com.ly.disk;

import com.ly.common.TypeDefine;
import com.ly.tool.ToolCutCommon;
import com.ly.tool.ToolTypeChange;

public class MinHeader {
	public static short type;
	public static short headerSize;
	public static int size;
	public static String typeString;

	public static void parseResChunkHeader(byte[] in) {
		// 对传入的内容进行划分内容
		type = ToolTypeChange.byte2Short(ToolCutCommon.getByteArray(0, 1, in));
		headerSize = ToolTypeChange.byte2Short(ToolCutCommon.getByteArray(2, 3, in));
		size = ToolTypeChange.byteArrayToIntOnlyFor4Byte(ToolCutCommon.getByteArray(4, 7, in));
		typeString = TypeDefine
				.getInstance(ToolTypeChange.bytesToHexString(ToolCutCommon.getByteArray(0, 1, in)).trim());

	}
	
}
