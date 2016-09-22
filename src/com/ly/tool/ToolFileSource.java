package com.ly.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ToolFileSource {
	public static byte[] getFileSources(String fileName) {
		byte[] srcByte = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			// fis = new FileInputStream("resource/resourceslbe.arsc");
			// fis = new FileInputStream("resource/resources_bro.arsc");
			// fis = new FileInputStream("resource/resources.arsc");
			fis = new FileInputStream(fileName);
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			srcByte = bos.toByteArray();
		} catch (Exception e) {
			System.out.println("read res file error:" + e.toString());
		} finally {
			try {
				fis.close();
				bos.close();
			} catch (Exception e) {
				System.out.println("close file error:" + e.toString());
			}
		}
		if (srcByte == null) {
			System.out.println("get src error...   ");
			return null;
		}
		return srcByte;
	}

	public static void writeFile(byte[] srcByte, String fileName) {
		FileOutputStream fop = null;
		File file;
		try {

			file = new File(fileName);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = srcByte;
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
