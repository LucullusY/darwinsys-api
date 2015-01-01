package com.darwinsys.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A basic demo of the generic StreamServer class.
 */
public class StreamServerDemo {

	public static void main(String[] args) throws Throwable {
		StreamServer server = new StreamServer(4567, new MyStreamHandlerFactory());
		System.out.printf("Server listening in %d%n", server.getPort());
		server.runServer();
	}

	/**
	 * This method must be thread-safe!
	 * @param sock The socket
	 * @throws IOException On error
	 */
	private static void doConversation(Socket sock) throws IOException {
		System.out.println("StreamServerDemo.doConversation(): accepted connection");
		try (BufferedReader is = new BufferedReader(new InputStreamReader(sock.getInputStream()))) {
			String line;
			while ((line = is.readLine()) != null) {
				System.out.println(line);
			}
			PrintWriter out = new PrintWriter(sock.getOutputStream());
			out.println("OK");
		}
	}

	/**
	 * A very minimal StreamServerHandlerFactory
	 */
	static class MyStreamHandlerFactory implements StreamServerHandlerFactory, StreamServerHandler {

		/** @inheritDoc */
		public StreamServerHandler getHandler(ServerSocket serverSock, Socket clientSocket) {
			return this;
		}

		/** @inheritDoc */
		public void handle(Socket sock) throws IOException {
			doConversation(sock);
		}

	}


}
