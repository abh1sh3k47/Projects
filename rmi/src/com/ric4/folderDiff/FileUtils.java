package com.ric4.folderDiff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class FileUtils 
{
	public static Set<String> getAllFiles(String path,String rootPath) throws Exception
	{
		Set<String> filesSet = new HashSet<String>();
		File f = new File(path);
		if(f.isDirectory())
		{
			File [] files = f.listFiles();
			for(File file : files)
			{
				if(file.isDirectory())
				{
					filesSet.addAll(getAllFiles(file.getAbsolutePath(),rootPath));
				}
				else
				{
					filesSet.add(file.getAbsolutePath().replace(rootPath, ""));
				}
			}
		}
		else
		{
			throw new Exception("Invalid folder");
		}
		return filesSet;
	}
	
	public static Set<String> getCommonFiles(Set<String> set1, Set<String> set2)
	{
		Set<String> commonFiles = new HashSet<String>();
		commonFiles.addAll(set1);
		commonFiles.retainAll(set2);
		return commonFiles;
	}
	
	public static Map<String,Boolean> getDiffInCommonFiles(Set<String> filesInFolder1, Set<String> filesInFolder2,String folder1, String folder2, IFileService serverFileService) throws Exception
	{
		Map<String,Boolean> commonDiffMap = new HashMap<String,Boolean>();
		
		Set<String> commonFiles = getCommonFiles(filesInFolder1,filesInFolder2);
		for(String s:commonFiles)
		{
			if(checkContents(folder1+s, folder2+s, serverFileService))
			{
				commonDiffMap.put(s, true);
			}
			else
			{
				commonDiffMap.put(s, false);
			}
		}
		
		return commonDiffMap;
	}
	
	public static Set<String> getFilesOnlyInSet(Set<String> set, Set<String> otherSet)
	{
		Set<String> retSet = new HashSet<String>();
		retSet.addAll(set);
		retSet.removeAll(otherSet);
		return retSet;
	}
	
	public static boolean checkContents(String file1,String file2,IFileService serverFileService) throws Exception
	{
		Scanner sc1 = new Scanner(new File(file1));
		TransferableFile tFile = serverFileService.getFile(file2);
		tFile.createFile();
		Scanner sc2 = new Scanner(new File(tFile.getFileName()));
		
		while(true)
		{
			boolean has1 = sc1.hasNext(); 
			boolean has2 = sc2.hasNext();
			if(has1&&has2)
			{
				if(!sc1.next().equals(sc2.next()))
					return false;
			}
			else if(has1!=has2)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
	}
	
	

}
