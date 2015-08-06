package com.ric4.fileTransfer;

import java.io.IOException;

/**
 * 
 * @author abh1sh3k47
 *
 */
public interface IFileTransferService 
{

	public void storeFile(TransferableFile file) throws IOException;
	
	public TransferableFile getFile(String path) throws IOException;;
}
