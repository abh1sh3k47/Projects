package com.ric4.folderDiff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class TransferableFile implements Serializable
{
	private transient File file;
	private String fileName;
	private byte [] content;
	
	TransferableFile(File f)
	{
		file = f;
		fileName = f.getName();
	}
	
	public void readContent() throws IOException
	{
		InputStream is = new FileInputStream(file);
		content = new byte[new Long(file.length()).intValue()];
		is.read(content);
		is.close();
	}
		
	public void createFile() throws IOException
	{
		File f = new File(fileName);
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(content);
		fos.flush();
		fos.close();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
