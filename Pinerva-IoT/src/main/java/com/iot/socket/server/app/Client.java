package com.iot.socket.server.app;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author jookim
 *  The DataOutputStream stream lets you write the primitives to an output source
 */
public class Client {
	
	/**
	 *  1. Number of how many files are to be sent.
		2. Length of file name.
		3. The file name.
		4. Length of the file.
		5. File content
		6. Repeat 2~5 for the next file if there are any.
	 */
	public static final String SERVER_IP = "127.0.0.1";
	public static final int PORT = 20001;
	public static final String DIR = "c:\\share\\client\\";
	
	public boolean send() {
		//1. Read files in Client DIR
		File[] files = new File(DIR).listFiles();
		
		try (Socket socket = new Socket(InetAddress.getByName(SERVER_IP), PORT);
				BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
				DataOutputStream dos = new DataOutputStream(bos);) {
			
			// 2. Length of file name.
			dos.writeInt(files.length);
			
			//3. The file name.
			for (File file : files) {
				//4. Length of the file.  파일 전송 준비
				long length = file.length();
				dos.writeLong(length);
				
				//6. 파일 이름 전송 준비
				String fileName = file.getName();
				dos.writeUTF(fileName);
				
				//7. 쓰기
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				
				int readBytes = 0;
				while ((readBytes = bis.read()) != -1) {
					bos.write(readBytes);
				}
				bis.close();
				fis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return true;
	}
}
