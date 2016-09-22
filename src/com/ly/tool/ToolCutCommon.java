package com.ly.tool;

public class ToolCutCommon {
	public static byte[] getByteArray(int begin, int end, byte[] original) {
		if (end - begin <= 0) {
			System.out.println("Error in getByteArray,end-begin<=0!");
			return null;
		}
		byte[] res = new byte[end - begin + 1];
		for (int i = begin, j = 0; i <= end; i++, j++) {
			res[j] = original[i];
		}
		return res;
	}

	public static byte[] getByteArrayBeginTrimBehaviorFor0XF(int begin, int end, byte[] original) {
		// flag�����ж����ǲ��ǿ�ͷ��һ�������޸Ŀ�ͷ�ļ��������޸�Ϊ1
		int flag = 0;
		if (end - begin <= 0) {
			System.out.println("Error in getByteArray,end-begin<=0!");
			return null;
		}
		byte[] res = new byte[end - begin + 1];
		int j = 0;
		for (int i = begin; i <= end; i++) {
			// ��ʱ��ʾ���byte�ǲ���Ҫ��byte�����ǿ�ͷ�ģ����԰���ȥ��
			// System.out.println("original[" + i + "] " + (original[i] &
			// 0XFF
			if ((original[i] & 0Xff) == 255 && flag == 0) {

			} else {
				flag = 1;
				res[j] = original[i];
				j++;
			}
		}
		if (j != 0) {
			res = ToolCutCommon.getByteArray(0, j, res);
		} else {
			return null;
		}

		return res;
	}
}
