package com.ly.disk;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.ly.tool.ToolCutCommon;
import com.ly.tool.ToolFileSource;
import com.ly.tool.ToolTypeChange;
import com.ly.write.ARSCWrite;

public class StringPool {
	public static short type;
	public static short header_size;
	public static int size;
	public static int stringCount;
	public static int styleCount;
	public static int flags;
	public static int stringStart;
	public static int styleStart;
	// ����Ϊ����string��offset��Ӧֵ�Ƕ���
	private static int[] stringOffset;
	private static int[] styleOffset;
	//
	public static ArrayList<byte[]> currentString = new ArrayList<byte[]>();
	public static ArrayList<byte[]> currentStyle = new ArrayList<byte[]>();

	public static void main(String args[]) {
		// currentString.add("ic_action_search".getBytes());
		// currentString.add("ic_launcher".getBytes());
		// currentString.add("activity_main".getBytes());
		// currentString.add("padding_small".getBytes());
		// currentString.add("padding_medium".getBytes());
		// currentString.add("padding_large".getBytes());
		// currentString.add("app_nam".getBytes());
		// ComponentKeyContent(currentString);
		// StringPool.Initialization();
	}

	// ��ȡĳ����Դ������Ԫ�ص��ļ�����
	public static int getPoolSize(String fileName) {
		byte[] srcByte = ToolFileSource.getFileSources(fileName);
		return srcByte.length;

	}

	// ����ֵ���޸ĺ�StringPool�ߴ�ĸı�
	public static int ComponentKeyContent(ArrayList<byte[]> list, String fileName) {
		// �����chunk���ܳ���
		// �̶����ȣ�28��+offset���飨4*size��+����list+0x00����

		int add_size = 4 - (getListLength(list) % 4);
		if (add_size == 4)
			add_size = 0;
		int this_size = 28 + 4 * list.size() + add_size + getListLength(list);
		int return_size = getOrignalSize(fileName) - this_size;
		byte[] this_size_byte = ToolTypeChange.intToByteArray(this_size);
		// System.out.println("size " + this_size);
		byte[] srcByte = new byte[this_size];
		srcByte[0] = 1;
		srcByte[1] = 0;
		srcByte[2] = 0x1C;
		srcByte[3] = 0;
		for (int i = 0, j = 3; i < 4; i++, j--) {
			srcByte[4 + i] = this_size_byte[j];
		}
		byte[] string_count = ToolTypeChange.intToByteArray(list.size());
		for (int i = 0, j = 3; i < 4; i++, j--) {
			srcByte[8 + i] = string_count[j];
		}
		srcByte[17] = 01;
		int stringStart = 28 + 4 * list.size();
		byte[] string_start = ToolTypeChange.intToByteArray(stringStart);
		for (int i = 0, j = 3; i < 4; i++, j--) {
			srcByte[20 + i] = string_start[j];
		}
		// ��ʼдoffset��
		byte[] inside_length = new byte[4];
		inside_length = ToolTypeChange.intToByteArray(0);
		int mid_size = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int p = 0, j = 3; p < 4; p++, j--) {
				srcByte[28 + i * 4 + p] = inside_length[j];
			}
			mid_size = (int) list.get(i).length + mid_size + 3;
			inside_length = ToolTypeChange.intToByteArray(mid_size);
		}
		// ��ʼд����������
		byte[] mid_content;
		mid_size = 0;
		for (int i = 0; i < list.size(); i++) {
			mid_content = new byte[list.get(i).length + 3];
			mid_content[0] = (byte) list.get(i).length;
			mid_content[1] = (byte) list.get(i).length;
			System.arraycopy(list.get(i), 0, mid_content, 2, list.get(i).length);
			System.arraycopy(mid_content, 0, srcByte, 28 + 4 * list.size() + mid_size, mid_content.length);
			mid_size = (int) list.get(i).length + mid_size + 3;
		}
		ToolFileSource.writeFile(srcByte, fileName);
		return return_size;
	}

	public static void Initialization(String fileName) {
		byte[] in = ToolFileSource.getFileSources(fileName);
		// ������ARSC�ļ���ͷ�ļ���ȡ��midHeader�ǳ�����0xC��
		byte[] midHeader = ToolCutCommon.getByteArray(0, 0XB, in);
		// ��headerͷ��resChunkHeader���н���,������ص�this.resChunkHeader������
		MinHeader.parseResChunkHeader(in);
		header_size = MinHeader.headerSize;
		// ��ȡstringCount
		byte[] midStringByteArray = ToolCutCommon.getByteArray(0X8, 0XB, in);
		stringCount = ToolTypeChange.byteArrayToIntOnlyFor4Byte(midStringByteArray);
		for (int i = 0; i < midStringByteArray.length; i++) {
			// System.out.println("test " + midStringByteArray[i]);
		}
		// ��ȡstyleCount
		byte[] midStyleByteArray = ToolCutCommon.getByteArray(0XC, 0XF, in);
		styleCount = ToolTypeChange.byteArrayToIntOnlyFor4Byte(midStyleByteArray);
		// ��ȡflags
		byte[] midFlagByteArray = ToolCutCommon.getByteArray(0X10, 0X13, in);
		flags = ToolTypeChange.byteArrayToIntOnlyFor4Byte(midFlagByteArray);
		// ��ȡstringStart
		byte[] midStringStartByteArray = ToolCutCommon.getByteArray(0X14, 0X17, in);
		stringStart = ToolTypeChange.byteArrayToIntOnlyFor4Byte(midStringStartByteArray);

		// ��ȡstyleStart
		byte[] midStyleStartByteArray = ToolCutCommon.getByteArray(0X18, 0X1B, in);
		styleStart = ToolTypeChange.byteArrayToIntOnlyFor4Byte(midStyleStartByteArray);

		// ��ȡstringOffset����
		stringOffset = new int[stringCount];
		byte[] midStringOffset = ToolCutCommon.getByteArray(0X1C, 0X1C + stringCount * 4, in);
		byte[] mid4 = new byte[4];
		for (int i = 0, j = 0; i < midStringOffset.length; i++) {
			mid4[i % 4] = midStringOffset[i];
			if ((i + 1) % 4 == 0) {
				stringOffset[j] = ToolTypeChange.byteArrayToIntOnlyFor4Byte(mid4);
				j++;
			}
		}

		// ��ȡstyleOffset����
		if (styleStart != 0 && styleCount != 0) {
			styleOffset = new int[styleCount];
			byte[] midStyleOffset = ToolCutCommon.getByteArray(0X1D + stringCount * 4,
					0X1D + stringCount * 4 + styleCount * 4, in);
			byte[] mid4FStle = new byte[4];
			for (int i = 0, j = 0; i < midStyleOffset.length; i++) {
				mid4FStle[i % 4] = midStyleOffset[i];
				if ((i + 1) % 4 == 0) {
					styleOffset[j] = ToolTypeChange.byteArrayToIntOnlyFor4Byte(mid4FStle);
					j++;
				}
			}
		}

		// ��ȡcurrentString������
		// �Ӹ���StringStart������Դ���Ľ�ȡ��ʼ�㣬���style�Ƿ�Ϊ�գ�Ϊ�������header�е�size����ȡ����StringPool
		int stringEnd = 0;
		if (styleCount == 0) {
			stringEnd = MinHeader.size - 1;
		} else {
			// ���û���е�����ȷ��
			stringEnd = styleStart - 1;
		}
		byte[] midStringCurrent = ToolCutCommon.getByteArray(stringStart, stringEnd, in);
		// ����stringOffset�ڵĶ���������midStringCurrent
		int cutEnd;
		byte[] mid_for_res;
		for (int i = 0; i < stringOffset.length; i++) {
			if (i + 1 == stringOffset.length) {
				cutEnd = midStringCurrent.length - 1;
			} else {
				cutEnd = stringOffset[i + 1] - 1;
			}
			byte[] midOneStringCurrent = ToolCutCommon.getByteArray(stringOffset[i], cutEnd, midStringCurrent);
			String midResult;
			mid_for_res = new byte[midOneStringCurrent.length - 3];
			System.arraycopy(midOneStringCurrent, 2, mid_for_res, 0, mid_for_res.length);

			// System.out.println(" " +
			// ToolTypeChange.bytesToHexString(mid_for_res));

			currentString.add(mid_for_res);
		}
		// stringOffset end___________________

	}

	public static int getOrignalSize(String fileName) {
		byte[] srcByte = ToolFileSource.getFileSources(fileName);
		byte[] size = new byte[4];
		for (int i = 0; i < 4; i++) {
			size[i] = srcByte[4 + i];
		}
		return ToolTypeChange.byte2int(size);
	}

	// ComponentKeyContent��ʹ�õ��ӷ�������string���ݵĳ��ȣ����Ҫ��ÿ��string�е�srcCount��byteCount������λ
	public static int getListLength(ArrayList<byte[]> list) {
		int length = 0;
		for (int i = 0; i < list.size(); i++) {
			length = length + list.get(i).length;
		}
		length = length + list.size() * 3;
		return length;
	}

}
