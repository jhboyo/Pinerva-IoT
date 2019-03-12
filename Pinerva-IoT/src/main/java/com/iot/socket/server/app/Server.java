package com.iot.socket.server.app;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static final int PORT = 20001;
	public static final String DIR = "c:\\share\\server\\";

	public boolean receive() {

		try (ServerSocket serverSocket = new ServerSocket();) {
			
			InetSocketAddress isaClient = new InetSocketAddress(PORT);
			serverSocket.bind(isaClient);
			
			Socket socket = serverSocket.accept(); // New socket connection created and waiting.
			isaClient = (InetSocketAddress) socket.getRemoteSocketAddress();
			
			System.out.println("server is running...");
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
			DataInputStream dis = new DataInputStream(bis);

			int filesCount = dis.readInt();
			File[] files = new File[filesCount];

			for (int i = 0; i < filesCount; i++) {
				long fileLength = dis.readLong();
				String fileName = dis.readUTF();

				files[i] = new File(DIR + fileName);

				FileOutputStream fos = new FileOutputStream(files[i]);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				for (int j = 0; j < fileLength; j++) {
					bos.write(bis.read());
				}
				bos.close();
			}
			dis.close();
			socket.close();
			//serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
