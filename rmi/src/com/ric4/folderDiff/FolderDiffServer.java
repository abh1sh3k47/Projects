package com.ric4.folderDiff;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import com.ric4.folderDiff.TransferableFile;
import com.ric4.rmi.Server;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class FolderDiffServer 
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
		server.registerService(IFileService.class, new ServerFileService());
	}


	public static class ServerFileService implements IFileService
	{
		@Override
		public Set<String> getAllFiles(String path, String rootPath) throws Exception 
		{
			return FileUtils.getAllFiles(path, rootPath);
		}

		@Override
		public TransferableFile getFile(String path) throws Exception 
		{
			File f = new File(path);
			TransferableFile file = new TransferableFile(f);
			file.readContent();
			return file;
		}
	}
}

