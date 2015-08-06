package com.ric4.fileTransfer;

import java.io.File;
import java.io.IOException;

import com.ric4.rmi.Client;
import com.ric4.rmi.Server;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class TransferClient 
{
	
	static IFileTransferService serverFileTransferService;

	public static void main(String[] args) throws IOException 
	{
		if(args.length < 3) 
		{
			System.out.println("Invalid Usage!!");
			return;
		}

		String hostname = args[0];
		String portString = args[1];
		String filePath = args[2];
		int port = Integer.parseInt(portString);

		Client client = new Client(hostname,port);
		serverFileTransferService = client.getService(IFileTransferService.class);

		/*
		File f = new File(filePath);

		if(f.isDirectory())
		{
			for(File fle:f.listFiles())
			{
				TransferableFile file = new TransferableFile(fle);
				file.readContent();
				serverFileTransferService.storeFile(file);
			}
		}
		else
		{
			TransferableFile file = new TransferableFile(f);
			file.readContent();
			serverFileTransferService.storeFile(file);
		}*/
		TransferableFile file = serverFileTransferService.getFile(filePath);
		file.createFile();
	}
	
	public static void sendFiles(File folder)
	{
		for(File f:folder.listFiles())
		{
			
		}
	}

}
