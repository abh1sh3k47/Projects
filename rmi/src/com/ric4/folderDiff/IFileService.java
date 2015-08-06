package com.ric4.folderDiff;

import java.util.Set;

/**
 * 
 * @author abh1sh3k47
 *
 */
public interface IFileService 
{
	public Set<String> getAllFiles(String path,String rootPath) throws Exception;
	public TransferableFile getFile(String path) throws Exception;
}
