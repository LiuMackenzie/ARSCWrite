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

	// �޸�һ��byte������
	public static void changeMethod(String fileName_before, String fileName_after, String fileName_target) {
		byte[] fileName_before_byte = ToolFileSource.getFileSources(fileName_before);
		byte[] fileName_after_byte = ToolFileSource.getFileSources(fileName_after);
		if (fileName_after_byte.length != fileName_before_byte.length) {
			System.out.println("�޸����ݳ��Ȳ�ͬ���޷��޸ģ� ");
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
		// flag���޸����ݵ���ʼλ��
		for (int i = 0; i < fileName_after_byte.length; i++) {
			fileName_target_byte[i + flag] = fileName_after_byte[i];
		}
		ToolFileSource.writeFile(fileName_target_byte, fileName_target);
	}

	// ɾ��һ��Type����
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
		// ����disk�ϵ�arsc�ļ����ֽ�ΪtypeSpec��type
		type_spec = ToolCutCommon.getByteArray(0, tag - 1, srcByte);
		type = ToolCutCommon.getByteArray(tag, srcByte.length - 1, srcByte);
		// ��type_spec��ɾ������
		type_spec = deleteMethodSpecType(type_spec);
		type = deleteMethodType(type, delete_fileName_byte);
		// ToolFileSource.writeFile(type_spec, "data/test_type_spec.arsc");
		// ToolFileSource.writeFile(type, "data/test_0921_spec.arsc");
		byte[] return_byte = new byte[type_spec.length + type.length];
		System.arraycopy(type_spec, 0, return_byte, 0, type_spec.length);
		System.arraycopy(type, 0, return_byte, type_spec.length, type.length);
		ToolFileSource.writeFile(return_byte, "data/typeSpec_4.arsc");
	}

	// ����һ��type�ķ���
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
		// ����disk�ϵ�arsc�ļ����ֽ�ΪtypeSpec��type
		type_spec = ToolCutCommon.getByteArray(0, tag - 1, srcByte);
		type = ToolCutCommon.getByteArray(tag, srcByte.length - 1, srcByte);

		// ��type_spec���������
		type_spec = appendMethodTypeSpec(type_spec);
		// ��type���������
		// ��������ݵ��趨

		type = appendMethodType(type, add_byte);
		byte[] return_byte = new byte[type.length + type_spec.length];
		System.arraycopy(type_spec, 0, return_byte, 0, type_spec.length);
		System.arraycopy(type, 0, return_byte, type_spec.length, type.length);
		return return_byte;
	}

	// ����һ��typeʱ��ʹ��appendMethodTypeSpec
	public static byte[] appendMethodTypeSpec(byte[] srcByte) {
		byte[] size_or_byte = ToolCutCommon.getByteArray(4, 7, srcByte);
		// ���������ļ�����+4byte
		int size = ToolTypeChange.byte2int(size_or_byte);
		size = size + 4;
		byte[] size_byte = ToolTypeChange.intToByteArray(size);
		for (int i = 0, j = 7; i < 4; i++, j--) {
			srcByte[j] = size_byte[i];
		}
		// ����entryCount����
		byte[] entry_count_byte = ToolCutCommon.getByteArray(0XC, 0XF, srcByte);
		int entry_count = ToolTypeChange.byte2int(entry_count_byte);
		entry_count = entry_count + 1;
		byte[] entry_count_res = ToolTypeChange.intToByteArray(entry_count);
		for (int i = 0, j = 0XF; i < 4; i++, j--) {
			srcByte[j] = entry_count_res[i];
		}
		// ����entryFlag
		byte[] return_byte = new byte[srcByte.length + 4];
		System.arraycopy(srcByte, 0, return_byte, 0, srcByte.length);
		return return_byte;
	}

	public static byte[] deleteMethodSpecType(byte[] srcByte) {
		byte[] size_or_byte = ToolCutCommon.getByteArray(4, 7, srcByte);
		// ���������ļ�����+4byte
		int size = ToolTypeChange.byte2int(size_or_byte);
		size = size - 4;
		byte[] size_byte = ToolTypeChange.intToByteArray(size);
		for (int i = 0, j = 7; i < 4; i++, j--) {
			srcByte[j] = size_byte[i];
		}
		// ����entryCount����
		byte[] entry_count_byte = ToolCutCommon.getByteArray(0XC, 0XF, srcByte);
		int entry_count = ToolTypeChange.byte2int(entry_count_byte);
		entry_count = entry_count - 1;
		byte[] entry_count_res = ToolTypeChange.intToByteArray(entry_count);
		for (int i = 0, j = 0XF; i < 4; i++, j--) {
			srcByte[j] = entry_count_res[i];
		}
		// ����entryFlag
		byte[] return_byte = new byte[srcByte.length - 4];
		System.arraycopy(srcByte, 0, return_byte, 0, srcByte.length - 4);
		return return_byte;
	}

	// ɾ��һ��byte[]ʱ����Ҫԭʼbyte���飬ɾ����λ�ã�ɾ���ĳߴ�
	public static byte[] deleteMethodType(byte[] srcByte_or, byte[] delete_file) {
		ArrayList<byte[]> array_list_offset = new ArrayList<byte[]>();
		ArrayList<byte[]> array_list_entry = new ArrayList<byte[]>();
		int header_size = (int) ToolTypeChange.byte2Short(ToolCutCommon.getByteArray(2, 3, srcByte_or));

		// ��������ݺ�����type�ĳ���

		// delete_byte.size+4(offset)
		byte[] size_or_byte = ToolCutCommon.getByteArray(4, 7, srcByte_or);
		// ���������ļ�����-4byte
		int size = ToolTypeChange.byte2int(size_or_byte);
		size = size - 4 - delete_file.length;
		byte[] size_byte = ToolTypeChange.intToByteArray(size);
		for (int i = 0, j = 7; i < 4; i++, j--) {
			srcByte_or[j] = size_byte[i];
		}
		byte[] return_byte = new byte[size];
		// �޸�entryCount���ݣ�entryCount-1��
		byte[] entry_count_byte = ToolCutCommon.getByteArray(0XC, 0XF, srcByte_or);
		int entry_count = ToolTypeChange.byte2int(entry_count_byte);
		entry_count = entry_count - 1;
		byte[] entry_count_res = ToolTypeChange.intToByteArray(entry_count);
		for (int i = 0, j = 0XF; i < 4; i++, j--) {
			srcByte_or[j] = entry_count_res[i];
		}
		// �޸�entryStart���ݣ�entryStart-4��
		byte[] entry_start_byte = ToolCutCommon.getByteArray(0X10, 0X13, srcByte_or);
		int entry_start = ToolTypeChange.byte2int(entry_start_byte);
		entry_start = entry_start - 4;
		byte[] entry_start_res = ToolTypeChange.intToByteArray(entry_start);
		for (int i = 0, j = 0X13; i < 4; i++, j--) {
			srcByte_or[j] = entry_start_res[i];
		}

		// �趨һ��end_byte����λ�ƺ�����
		byte[] end_byte;
		byte[] mid_byte = new byte[4];
		// ��offset�ŵ�һ��list��
		for (int i = header_size; i < (entry_count + 1) * 4 + header_size; i++) {
			mid_byte[i % 4] = srcByte_or[i];
			if (i % 4 == 0 && i != 0) {
				// System.out.println(ToolTypeChange.bytesToHexString(mid_byte));
				array_list_offset.add(mid_byte);
				mid_byte = new byte[4];
			}
		}
		// �����ݶ��ŵ�һ��list��
		// ��ʼ������λ��
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

		// �޸�offset��list��Ԫ�أ�ӦΪλ�Ƹ���
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
		// ��װoffset_list��entry_list
		// ��ʼ�����飬���ȣ�offsetCount*4+entryList.size
		int offset_set_mid = 0;
		byte[] offset_set_mid_byte = new byte[4];
		end_byte = new byte[entry_count * 4 + entry_size];
		int begin_offset = 0;
		// ����õ�offset��ֵ�����ص�����
		for (int i = 0; i < array_list_entry.size(); i++) {
			offset_set_mid_byte = ToolTypeChange.intToByteArray(begin_offset);
			for (int j = 0; j < 4; j++) {
				end_byte[(i + 1) * 4 - j - 1] = offset_set_mid_byte[j];
			}
			begin_offset = begin_offset + array_list_entry.get(i).length;
		}
		// ��entryList�����ݷ��ظ�end_byte��
		byte[] mid_entry_byte;
		int entry_flag = entry_count * 4;
		for (int i = 0; i < array_list_entry.size(); i++) {
			mid_entry_byte = array_list_entry.get(i);
			System.arraycopy(mid_entry_byte, 0, end_byte, entry_flag, mid_entry_byte.length);
			entry_flag = entry_flag + mid_entry_byte.length;
		}
		// ����ΪֹsrcByte_or��ǰ�η��õ���Ŀ�������
		System.arraycopy(srcByte_or, 0, return_byte, 0, header_size);
		System.arraycopy(end_byte, 0, return_byte, header_size, end_byte.length);
		return return_byte;

	}

	// ����һ��typeʱ��ʹ��type����
	public static byte[] appendMethodType(byte[] srcByte_or, byte[] add_byte) {

		int header_size = (int) ToolTypeChange.byte2Short(ToolCutCommon.getByteArray(2, 3, srcByte_or));

		// ��������ݺ�����type�ĳ���

		// add_byte.size+4(offset)
		byte[] size_or_byte = ToolCutCommon.getByteArray(4, 7, srcByte_or);
		// ���������ļ�����+4byte
		int size = ToolTypeChange.byte2int(size_or_byte);
		size = size + 4 + add_byte.length;
		byte[] size_byte = ToolTypeChange.intToByteArray(size);
		for (int i = 0, j = 7; i < 4; i++, j--) {
			srcByte_or[j] = size_byte[i];
		}
		byte[] return_byte = new byte[size];
		// �޸�entryCount���ݣ�entryCount+1��
		byte[] entry_count_byte = ToolCutCommon.getByteArray(0XC, 0XF, srcByte_or);
		int entry_count = ToolTypeChange.byte2int(entry_count_byte);
		entry_count = entry_count + 1;
		byte[] entry_count_res = ToolTypeChange.intToByteArray(entry_count);
		for (int i = 0, j = 0XF; i < 4; i++, j--) {
			srcByte_or[j] = entry_count_res[i];
		}
		// �޸�entryStart���ݣ�entryStart+4��
		byte[] entry_start_byte = ToolCutCommon.getByteArray(0X10, 0X13, srcByte_or);
		int entry_start = ToolTypeChange.byte2int(entry_start_byte);
		entry_start = entry_start + 4;
		byte[] entry_start_res = ToolTypeChange.intToByteArray(entry_start);
		for (int i = 0, j = 0X13; i < 4; i++, j--) {
			srcByte_or[j] = entry_start_res[i];
		}

		// ��ȡ�õ����һ��offset��ֵ
		int entry_or_last_begin = header_size + (entry_count - 2) * 4;
		int entry_last_offset_content_append = srcByte_or.length
				- ToolTypeChange
						.byte2int(ToolCutCommon.getByteArray(entry_or_last_begin, entry_or_last_begin + 3, srcByte_or))
				- header_size - (entry_count - 1) * 4;
		System.out.println("entry_last_offset_content_append" + entry_last_offset_content_append);
		// entry_last_offset_content �Ǹ��������һ��ֵ
		int entry_last_offset_content = ToolTypeChange
				.byte2int(ToolCutCommon.getByteArray(entry_or_last_begin, entry_or_last_begin + 3, srcByte_or))
				+ entry_last_offset_content_append;
		byte[] entry_last_offset_content_mid = ToolTypeChange.intToByteArray(entry_last_offset_content);
		byte[] entry_last_offset_content_final = new byte[4];
		for (int i = 0, j = 3; i < 4; i++, j--) {
			entry_last_offset_content_final[i] = entry_last_offset_content_mid[j];
		}
		// ��ʼ������byte����һ����offsetǰ�ģ�һ����offset��
		byte[] type_entry_offset_before = new byte[header_size + entry_count * 4];
		byte[] type_entry_offset_end = new byte[srcByte_or.length - header_size - (entry_count - 1) * 4
				+ add_byte.length];
		// ��ȡtype_entry_offset_before
		System.arraycopy(srcByte_or, 0, type_entry_offset_before, 0, type_entry_offset_before.length - 5);
		System.arraycopy(entry_last_offset_content_final, 0, type_entry_offset_before,
				type_entry_offset_before.length - 4, 4);
		// ��ȡtype_entry_offset_before
		// ��ȡtype_entry_offset_end
		System.arraycopy(srcByte_or, header_size + (entry_count - 1) * 4, type_entry_offset_end, 0,
				srcByte_or.length - header_size - (entry_count - 1) * 4);
		System.arraycopy(add_byte, 0, type_entry_offset_end, srcByte_or.length - header_size - (entry_count - 1) * 4,
				add_byte.length);
		// ��type_entry_offset_before��type_entry_offset_end ���ݺϲ���
		System.arraycopy(type_entry_offset_before, 0, return_byte, 0, type_entry_offset_before.length);
		System.arraycopy(type_entry_offset_end, 0, return_byte, type_entry_offset_before.length,
				type_entry_offset_end.length);
		return return_byte;
	}
}
