package server;

import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;
import spotifyAPI.SpotifyAppleScriptWrapper;
import utils.TimeUtils;
import upnp.UPnP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Naveen Govindaraju
 */
public class UDPServer {
	private DatagramSocket socket;
	private HashMap<String, ClientInfo> clients;
	private DatagramPacket receivePacket;
	private SpotifyPlayerAPI api;
	Thread reciver;
	Thread sender;
	int serverPort = 9009;
	public UDPServer(boolean diffNetWork) {
		clients = new HashMap<>();
		try {
			socket = new DatagramSocket(serverPort);
		} catch (SocketException e) {
			// TODO: 6/12/20 what happens if the server wont connect to the port
			e.printStackTrace();
		}
		boolean star;
		if (diffNetWork) {
			for (; serverPort <= 49140; serverPort += 11) {
				//makes sure the port is clear
				UPnP.closePortUDP((serverPort));
				//only needed if the clients are not on the same network
				star = (UPnP.openPortUDP((serverPort)));
				System.out.println(star);
				if (star)
					break;
			}
		}
		startReceiver();
		startSender();
		System.out.println("Server is started!");

	}

	/**
	 * the thread that waits for clients to join and adds them to the server
	 */
	private void startReceiver() {
		reciver = new Thread(() -> {
			while (true) {
				byte[] receiveData = new byte[1024];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {
					socket.receive(receivePacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
				//port and ip of the client who send the packet
				InetAddress tad = receivePacket.getAddress();
				int tPort = receivePacket.getPort();
				//if client not added to list of clients add it
				if (!clients.containsKey("" + tad + tPort)) {
					System.out.println("added");
					clients.put("" + tad + tPort, new ClientInfo(tad, tPort));
					try {
						sendToClients(api.getTrackUri() + " " + api.isPlaying() + " " + api.getPlayBackPosition() + " " + TimeUtils.getAppleTime());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		reciver.start();
	}

	/**
	 * sends tack updates to the clients
	 */
	private void startSender() {
		sender = new Thread(() -> {
			while (true) {
				try {
					String tempTrack = api.getTrackUri();
					if (!tempTrack.contains(":ad:") && !tempTrack.isBlank() && !tempTrack.equals("ice"))
						sendToClients(tempTrack + " " + api.isPlaying() + " " + api.getPlayBackPosition() + " " + TimeUtils.getAppleTime());
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		});
		sender.start();
	}

	/**
	 * sends tack updates to the clients
	 *
	 * @param msg
	 */
	private void sendToClients(String msg) {
		/*map contains all clients connected to the system this loop traverses the list of clients and sends the data
		 to them
		*/
		for (Map.Entry<String, ClientInfo> clientInfo : clients.entrySet()) {
			byte[] sendData = new byte[1024];
			DatagramPacket response = null;
			sendData = msg.getBytes();
			response = new DatagramPacket(sendData, sendData.length, clientInfo.getValue().address, clientInfo.getValue().port);
			try {
				socket.send(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void quit() {
		sender.stop();
		reciver.stop();
		System.out.println("Server Stopped");
	}
}

