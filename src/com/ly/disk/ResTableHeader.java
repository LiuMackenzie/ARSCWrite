package com.ly.disk;

import com.ly.tool.ToolCutCommon;
import com.ly.tool.ToolFileSource;
import com.ly.tool.ToolTypeChange;

public class ResTableHeader {
	public static void main(String args[]) {
		setResTableHeader(4);
	}

	public static void setResTableHeader(int size) {
		byte[] srcByte = ToolFileSource.getFileSources("data/resTableHeader.arsc");
		System.out.println("获取到resTableHeader的内容，增减的长度为" + size);
		int mid_size = ToolTypeChange.byteArrayToIntOnlyFor4Byte(ToolCutCommon.getByteArray(4, 7, srcByte));
		mid_size = mid_size + size;
		byte[] midSrc = ToolTypeChange.intToByteArray(mid_size);
		for (int i = 7, j = 0; i > 3; i--, j++) {
			srcByte[i] = midSrc[j];
		}
		ToolFileSource.writeFile(srcByte, "data/resTableHeader.arsc");
	}

}
