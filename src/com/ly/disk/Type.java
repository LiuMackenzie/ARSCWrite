package com.ly.disk;

import java.util.ArrayList;
import java.util.Arrays;

import com.ly.tool.ToolCutCommon;
import com.ly.tool.ToolFileSource;
import com.ly.tool.ToolTypeChange;

public class Type {
	public static void main(String args[]) {
		// byte[] add_byte = new byte[16];
		// add_byte[0] = 8;
		// add_byte[4] = 0X13;
		// add_byte[8] = 8;
		// add_byte[0XB] = 3;
		// add_byte[0XC] = 0X15;
		// byte[] res = appendMethod(add_byte, "data/typeSpec_4.arsc");
		// ToolFileSource.writeFile(res, "data/typeSpec_4_c.arsc");
		// changeMethod("changeContent/before.arsc", "changeContent/after.arsc",
		// "data/typeSpec_4.arsc");
		deleteOneType("changeContent/before.arsc", "data/typeSpec_4.arsc");
	}

	// 修改一段byte的内容
	public static void changeMethod(String fileName_before, String fileName_after, String fileName_target) {
		byte[] fileName_before_byte = ToolFileSource.getFileSources(fileName_before);
		byte[] fileName_after_byte = ToolFileSource.getFileSources(fileName_after);
		if (fileName_after_byte.length != fileName_before_byte.length) {
			System.out.println("修改内容长度不同，无法修改！ ");
		}
		byte[] fileName_target_byte = ToolFileSource.getFileSources(fileName_target);
		int flag = 0;
		for (int i = 0, j = 0; i < fileName_target_byte.length - fileName_before_byte.length; i++) {
			if (fileName_target_byte[i] == fileName_before_byte[j]) {
				j++;
				if (j == fileName_before_byte.length) {
					flag = i - fileName_before_byte.length + 1;
					break;
				}
			} else {
				j = 0;
			}
		}
		// flag是修改内容的起始位置
		for (int i = 0; i < fileName_after_byte.length; i++) {
			fileName_target_byte[i + flag] = fileName_after_byte[i];
		}
		ToolFileSource.writeFile(fileName_target_byte, fileName_target);
	}

	// 删除一个Type内容
	public static void deleteOneType(String delete_fileName, String or_fileName) {
		byte[] delete_fileName_byte = ToolFileSource.getFileSources(delete_fileName);
		// System.out.println("delete_fileName_byte " +
		// ToolTypeChange.bytesToHexString(delete_fileName_byte));
		byte[] srcByte = ToolFileSource.getFileSources(or_fileName);
		byte[] type_spec;
		byte[] type;
		int tag = 0;
		for (int i = 0; i < srcByte.length; i++) {
			if (srcByte[i] == 0x01 && srcByte[i + 1] == 0x02) {
				tag = i;
			}
		}
		// 根据disk上的arsc文件，分解为typeSpec和type
		type_spec = ToolCutCommon.getByteArray(0, tag - 1, srcByte);
		type = ToolCutCommon.getByteArray(tag, srcByte.length - 1, srcByte);
		// 对type_spec做删除操作
		type_spec = deleteMethodSpecType(type_spec);
		type = deleteMethodType(type, delete_fileName_byte);
		// ToolFileSource.writeFile(type_spec, "data/test_type_spec.arsc");
		// ToolFileSource.writeFile(type, "data/test_0921_spec.arsc");
		byte[] return_byte = new byte[type_spec.length + type.length];
		System.arraycopy(type_spec, 0, return_byte, 0, type_spec.length);
		System.arraycopy(type, 0, return_byte, type_spec.length, type.length);
		ToolFileSource.writeFile(return_byte, "data/typeSpec_4.arsc");
	}

	// 增加一个type的方法
	public static byte[] appendMethod(byte[] add_byte, String fileName) {
		byte[] srcByte = ToolFileSource.getFileSources(fileName);
		byte[] type_spec;
		byte[] type;
		int tag = 0;
		for (int i = 0; i < srcByte.length; i++) {
			if (srcByte[i] == 0x01 && srcByte[i + 1] == 0x02) {
				tag = i;
			}
		}
		// 根据disk上的arsc文件，分解为typeSpec和type
		type_spec = ToolCutCommon.getByteArray(0, tag - 1, srcByte);
		type = ToolCutCommon.getByteArray(tag, srcByte.length - 1, srcByte);

		// 向type_spec中添加内容
		type_spec = appendMethodTypeSpec(type_spec);
		// 向type中添加内容
		// 新添加内容的设定

		type = appendMethodType(type, add_byte);
		byte[] return_byte = new byte[type.length + type_spec.length];
		System.arraycopy(type_spec, 0, return_byte, 0, type_spec.length);
		System.arraycopy(type, 0, return_byte, type_spec.length, type.length);
		return return_byte;
	}

	// 增加一个type时，使用appendMethodTypeSpec
	public static byte[] appendMethodTypeSpec(byte[] srcByte) {
		byte[] size_or_byte = ToolCutCommon.getByteArray(4, 7, srcByte);
		// 增加整个文件长度+4byte
		int size = ToolTypeChange.byte2int(size_or_byte);
		size = size + 4;
		byte[] size_byte = ToolTypeChange.intToByteArray(size);
		for (int i = 0, j = 7; i < 4; i++, j--) {
			srcByte[j] = size_byte[i];
		}
		// 增加entryCount个数
		byte[] entry_count_byte = ToolCutCommon.getByteArray(0XC, 0XF, srcByte);
		int entry_count = ToolTypeChange.byte2int(entry_count_byte);
		entry_count = entry_count + 1;
		byte[] entry_count_res = ToolTypeChange.intToByteArray(entry_count);
		for (int i = 0, j = 0XF; i < 4; i++, j--) {
			srcByte[j] = entry_count_res[i];
		}
		// 增加entryFlag
		byte[] return_byte = new byte[srcByte.length + 4];
		System.arraycopy(srcByte, 0, return_byte, 0, srcByte.length);
		return return_byte;
	}

	public static byte[] deleteMethodSpecType(byte[] srcByte) {
		byte[] size_or_byte = ToolCutCommon.getByteArray(4, 7, srcByte);
		// 增加整个文件长度+4byte
		int size = ToolTypeChange.byte2int(size_or_byte);
		size = size - 4;
		byte[] size_byte = ToolTypeChange.intToByteArray(size);
		for (int i = 0, j = 7; i < 4; i++, j--) {
			srcByte[j] = size_byte[i];
		}
		// 增加entryCount个数
		byte[] entry_count_byte = ToolCutCommon.getByteArray(0XC, 0XF, srcByte);
		int entry_count = ToolTypeChange.byte2int(entry_count_byte);
		entry_count = entry_count - 1;
		byte[] entry_count_res = ToolTypeChange.intToByteArray(entry_count);
		for (int i = 0, j = 0XF; i < 4; i++, j--) {
			srcByte[j] = entry_count_res[i];
		}
		// 增加entryFlag
		byte[] return_byte = new byte[srcByte.length - 4];
		System.arraycopy(srcByte, 0, return_byte, 0, srcByte.length - 4);
		return return_byte;
	}

	// 删除一段byte[]时，需要原始byte数组，删除的位置，删除的尺寸
	public static byte[] deleteMethodType(byte[] srcByte_or, byte[] delete_file) {
		ArrayList<byte[]> array_list_offset = new ArrayList<byte[]>();
		ArrayList<byte[]> array_list_entry = new ArrayList<byte[]>();
		int header_size = (int) ToolTypeChange.byte2Short(ToolCutCommon.getByteArray(2, 3, srcByte_or));

		// 求添加内容后整个type的长度

		// delete_byte.size+4(offset)
		byte[] size_or_byte = ToolCutCommon.getByteArray(4, 7, srcByte_or);
		// 增加整个文件长度-4byte
		int size = ToolTypeChange.byte2int(size_or_byte);
		size = size - 4 - delete_file.length;
		byte[] size_byte = ToolTypeChange.intToByteArray(size);
		for (int i = 0, j = 7; i < 4; i++, j--) {
			srcByte_or[j] = size_byte[i];
		}
		byte[] return_byte = new byte[size];
		// 修改entryCount内容（entryCount-1）
		byte[] entry_count_byte = ToolCutCommon.getByteArray(0XC, 0XF, srcByte_or);
		int entry_count = ToolTypeChange.byte2int(entry_count_byte);
		entry_count = entry_count - 1;
		byte[] entry_count_res = ToolTypeChange.intToByteArray(entry_count);
		for (int i = 0, j = 0XF; i < 4; i++, j--) {
			srcByte_or[j] = entry_count_res[i];
		}
		// 修改entryStart内容（entryStart-4）
		byte[] entry_start_byte = ToolCutCommon.getByteArray(0X10, 0X13, srcByte_or);
		int entry_start = ToolTypeChange.byte2int(entry_start_byte);
		entry_start = entry_start - 4;
		byte[] entry_start_res = ToolTypeChange.intToByteArray(entry_start);
		for (int i = 0, j = 0X13; i < 4; i++, j--) {
			srcByte_or[j] = entry_start_res[i];
		}

		// 设定一个end_byte放置位移和内容
		byte[] end_byte;
		byte[] mid_byte = new byte[4];
		// 把offset放到一个list中
		for (int i = header_size; i < (entry_count + 1) * 4 + header_size; i++) {
			mid_byte[i % 4] = srcByte_or[i];
			if (i % 4 == 0 && i != 0) {
				// System.out.println(ToolTypeChange.bytesToHexString(mid_byte));
				array_list_offset.add(mid_byte);
				mid_byte = new byte[4];
			}
		}
		// 把内容都放到一个list中
		// 起始的内容位置
		int begin_entry_set = (entry_count + 1) * 4 + header_size;
		byte[] mid_entry_content;
		int next_size = 0;
		for (int i = 0; i < array_list_offset.size() - 1; i++) {
			mid_entry_content = new byte[ToolTypeChange.byte2int(array_list_offset.get(i + 1))
					- ToolTypeChange.byte2int(array_list_offset.get(i))];
			System.arraycopy(srcByte_or, begin_entry_set + next_size, mid_entry_content, 0, mid_entry_content.length);
			next_size = next_size + mid_entry_content.length;
			array_list_entry.add(mid_entry_content);
		}
		mid_entry_content = new byte[ToolCutCommon.getByteArray(begin_entry_set + next_size, srcByte_or.length - 1,
				srcByte_or).length];
		mid_entry_content = ToolCutCommon.getByteArray(begin_entry_set + next_size, srcByte_or.length - 1, srcByte_or);
		array_list_entry.add(mid_entry_content);

		// 修改offset中list的元素，应为位移改了
		int entry_size = 0;
		// array_list_entry.size());
		int tag_delete = 0;
		for (int i = 0; i < array_list_entry.size(); i++) {
			entry_size = entry_size + array_list_entry.get(i).length;
			if (Arrays.equals(array_list_entry.get(i), delete_file)) {
				entry_size = entry_size - array_list_entry.get(i).length;
				tag_delete = i;
			}
		}
		array_list_entry.remove(tag_delete);
		// 组装offset_list和entry_list
		// 初始化数组，长度，offsetCount*4+entryList.size
		int offset_set_mid = 0;
		byte[] offset_set_mid_byte = new byte[4];
		end_byte = new byte[entry_count * 4 + entry_size];
		int begin_offset = 0;
		// 把算好的offset赋值给返回的数组
		for (int i = 0; i < array_list_entry.size(); i++) {
			offset_set_mid_byte = ToolTypeChange.intToByteArray(begin_offset);
			for (int j = 0; j < 4; j++) {
				end_byte[(i + 1) * 4 - j - 1] = offset_set_mid_byte[j];
			}
			begin_offset = begin_offset + array_list_entry.get(i).length;
		}
		// 把entryList的内容返回给end_byte中
		byte[] mid_entry_byte;
		int entry_flag = entry_count * 4;
		for (int i = 0; i < array_list_entry.size(); i++) {
			mid_entry_byte = array_list_entry.get(i);
			System.arraycopy(mid_entry_byte, 0, end_byte, entry_flag, mid_entry_byte.length);
			entry_flag = entry_flag + mid_entry_byte.length;
		}
		// 到此为止srcByte_or中前段放置的是目标的内容
		System.arraycopy(srcByte_or, 0, return_byte, 0, header_size);
		System.arraycopy(end_byte, 0, return_byte, header_size, end_byte.length);
		return return_byte;

	}

	// 增加一个type时，使用type方法
	public static byte[] appendMethodType(byte[] srcByte_or, byte[] add_byte) {

		int header_size = (int) ToolTypeChange.byte2Short(ToolCutCommon.getByteArray(2, 3, srcByte_or));

		// 求添加内容后整个type的长度

		// add_byte.size+4(offset)
		byte[] size_or_byte = ToolCutCommon.getByteArray(4, 7, srcByte_or);
		// 增加整个文件长度+4byte
		int size = ToolTypeChange.byte2int(size_or_byte);
		size = size + 4 + add_byte.length;
		byte[] size_byte = ToolTypeChange.intToByteArray(size);
		for (int i = 0, j = 7; i < 4; i++, j--) {
			srcByte_or[j] = size_byte[i];
		}
		byte[] return_byte = new byte[size];
		// 修改entryCount内容（entryCount+1）
		byte[] entry_count_byte = ToolCutCommon.getByteArray(0XC, 0XF, srcByte_or);
		int entry_count = ToolTypeChange.byte2int(entry_count_byte);
		entry_count = entry_count + 1;
		byte[] entry_count_res = ToolTypeChange.intToByteArray(entry_count);
		for (int i = 0, j = 0XF; i < 4; i++, j--) {
			srcByte_or[j] = entry_count_res[i];
		}
		// 修改entryStart内容（entryStart+4）
		byte[] entry_start_byte = ToolCutCommon.getByteArray(0X10, 0X13, srcByte_or);
		int entry_start = ToolTypeChange.byte2int(entry_start_byte);
		entry_start = entry_start + 4;
		byte[] entry_start_res = ToolTypeChange.intToByteArray(entry_start);
		for (int i = 0, j = 0X13; i < 4; i++, j--) {
			srcByte_or[j] = entry_start_res[i];
		}

		// 获取得到最后一个offset的值
		int entry_or_last_begin = header_size + (entry_count - 2) * 4;
		int entry_last_offset_content_append = srcByte_or.length
				- ToolTypeChange
						.byte2int(ToolCutCommon.getByteArray(entry_or_last_begin, entry_or_last_begin + 3, srcByte_or))
				- header_size - (entry_count - 1) * 4;
		System.out.println("entry_last_offset_content_append" + entry_last_offset_content_append);
		// entry_last_offset_content 是该填充的最后一个值
		int entry_last_offset_content = ToolTypeChange
				.byte2int(ToolCutCommon.getByteArray(entry_or_last_begin, entry_or_last_begin + 3, srcByte_or))
				+ entry_last_offset_content_append;
		byte[] entry_last_offset_content_mid = ToolTypeChange.intToByteArray(entry_last_offset_content);
		byte[] entry_last_offset_content_final = new byte[4];
		for (int i = 0, j = 3; i < 4; i++, j--) {
			entry_last_offset_content_final[i] = entry_last_offset_content_mid[j];
		}
		// 初始化两个byte数组一个存offset前的，一个存offset后
		byte[] type_entry_offset_before = new byte[header_size + entry_count * 4];
		byte[] type_entry_offset_end = new byte[srcByte_or.length - header_size - (entry_count - 1) * 4
				+ add_byte.length];
		// 获取type_entry_offset_before
		System.arraycopy(srcByte_or, 0, type_entry_offset_before, 0, type_entry_offset_before.length - 5);
		System.arraycopy(entry_last_offset_content_final, 0, type_entry_offset_before,
				type_entry_offset_before.length - 4, 4);
		// 获取type_entry_offset_before
		// 获取type_entry_offset_end
		System.arraycopy(srcByte_or, header_size + (entry_count - 1) * 4, type_entry_offset_end, 0,
				srcByte_or.length - header_size - (entry_count - 1) * 4);
		System.arraycopy(add_byte, 0, type_entry_offset_end, srcByte_or.length - header_size - (entry_count - 1) * 4,
				add_byte.length);
		// 将type_entry_offset_before和type_entry_offset_end 内容合并下
		System.arraycopy(type_entry_offset_before, 0, return_byte, 0, type_entry_offset_before.length);
		System.arraycopy(type_entry_offset_end, 0, return_byte, type_entry_offset_before.length,
				type_entry_offset_end.length);
		return return_byte;
	}
}
