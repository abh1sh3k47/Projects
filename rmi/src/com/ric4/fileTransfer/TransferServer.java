package com.ric4.fileTransfer;

import java.io.File;
import java.io.IOException;

import com.ric4.rmi.Server;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class TransferServer 
{

	public static void main(String[] args) throws IOException 
	{
		if(args.length < 1) 
		{
			System.out.println("Invalid Usage!!");
			return;
		}
		
		String portString = args[0];
		int port = Integer.parseInt(portString);
		
		Server server = new Server(port);
		server.registerService(IFileTransferService.class, new ServerFileTransferService());
	}
	
	public static class ServerFileTransferService implements IFileTransferService
	{
		@Override
		public void storeFile(TransferableFile file) throws IOException 
		{
			file.createFile();
		}

		@Override
		public TransferableFile getFile(String path) throws IOException 
		{
			TransferableFile file = new TransferableFile(new File(path));
			file.readContent();
			return file;
		}
	}

}
