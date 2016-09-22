package com.ly.tool;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ToolTypeChange {
	public static int byteArrayToIntOnlyFor4Byte(byte[] res) {
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) | ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}

	public static int byte2int(byte[] res) {
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) | ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}

	public static short byte2Short(byte[] b) {
		short s = 0;
		short s0 = (short) (b[0] & 0xff);
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	public static String bytesToHexString(byte[] src) {
		// byte[] src = reverseBytes(src1);
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv + " ");
		}
		return stringBuilder.toString();
	}

	public static String getStringFromByteAry(byte[] srcByte, int start) {
		if (srcByte == null) {
			return "";
		}
		if (start < 0) {
			return "";
		}
		if (start >= srcByte.length) {
			return "";
		}
		byte val = srcByte[start];
		int i = 1;
		ArrayList<Byte> byteList = new ArrayList<Byte>();
		while (val != 0) {
			byteList.add(srcByte[start + i]);
			val = srcByte[start + i];
			i++;
		}
		byte[] valAry = new byte[byteList.size()];
		for (int j = 0; j < byteList.size(); j++) {
			valAry[j] = byteList.get(j);
		}
		try {
			return new String(valAry, "UTF-8");
		} catch (Exception e) {
			System.out.println("encode error:" + e.toString());
			return "";
		}
	}

	public static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}

	public static byte[] intToByteArray(int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));
		byte[] resByte = new byte[4];
		for (int i = 0, j = 3; i < 4; i++, j--) {
			resByte[i] = byteArray[j];
		}
		return (byteArray);
	}
	public static byte[] intToByteArrayTrue(int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));
		byte[] resByte = new byte[4];
		for (int i = 0, j = 3; i < 4; i++, j--) {
			resByte[i] = byteArray[j];
		}
		return (resByte);
	}
}
