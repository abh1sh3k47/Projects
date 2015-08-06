package com.ric4.folderDiff;

import java.util.Map;
import java.util.Set;

import com.ric4.rmi.Client;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class FolderDiffClient 
{

	public static void main(String[] args) throws Exception 
	{
		if(args.length < 3) 
		{
			System.out.println("Invalid Usage!!");
			return;
		}

		String hostname = args[0];
		String portString = args[1];
		String localPath = args[2];
		String serverPath = args[3];
		
		int port = Integer.parseInt(portString);

		Client client = new Client(hostname,port);
		IFileService serverFolderService = client.getService(IFileService.class);

		Set<String> filesInFolder1 = FileUtils.getAllFiles(localPath, localPath);
		Set<String> filesInFolder2 = serverFolderService.getAllFiles(serverPath,serverPath);
		
		Set<String> leftFiles = FileUtils.getFilesOnlyInSet(filesInFolder1,filesInFolder2);
		Set<String> rightFiles = FileUtils.getFilesOnlyInSet(filesInFolder2,filesInFolder1);
		
		Map<String,Boolean> commonDiffMap = FileUtils.getDiffInCommonFiles(filesInFolder1,filesInFolder2,localPath,serverPath,serverFolderService);
		
		System.out.println("Common Files - ");
		for(Map.Entry<String, Boolean> e : commonDiffMap.entrySet())
		{
			System.out.println(e.getKey()+" - "+
			(e.getValue() ? "Equal":"Different"));
		}
		
		System.out.println();
		System.out.println("Files only present on client in -"+localPath);
		for(String s:leftFiles)
		{
			System.out.println(s);
		}
		System.out.println();
		System.out.println("Files only present on server in -"+serverPath);
		for(String s:rightFiles)
		{
			System.out.println(s);
		}
	}
}
