package server;

import exception.SpotifyException;
import interfaces.SpotifyPlayerAPI;
import osx.SpotifyPlayerAppleScriptWrapper;
import upnp.UPnP;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
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
	private int serverPort;
	private String trackID;
	private long pos;
	private boolean playing;
	public UDPServer(int serverPort, boolean diffNetWork)
	{
		this.serverPort = serverPort;
		api = new SpotifyPlayerAppleScriptWrapper();
		trackID = api.getTrackId();
		try {
			pos = api.getPlayerPosition();
		} catch (SpotifyException e) {
			e.printStackTrace();
		}
		try {
			playing = api.isPlaying();
		} catch (SpotifyException e) {
			e.printStackTrace();
		}
		clients = new HashMap<>();
		try {
			socket = new DatagramSocket(serverPort);
		} catch (SocketException e) {
			// TODO: 6/12/20 what happens if the server wont connect to the port
			e.printStackTrace();
		}
		boolean star = true;
		if(diffNetWork) {
			//makes sure the port is clear
			UPnP.closePortUDP((serverPort));
			//only needed if the clients are not on the same network
			star = (UPnP.openPortUDP((serverPort)));
			System.out.println(star);
		}
		startReceiver();
		startSender();
		System.out.println("Server is started!");
		try {
			if(star)
				Desktop.getDesktop().open(new File("ServerStarted.app"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * the thread that waits for clients to join and adds them to the server
	 */
	private void startReceiver()
	{
		new Thread(() -> {
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
				if(!clients.containsKey(""+tad+tPort)) {
					System.out.println("added");
					try {
						Desktop.getDesktop().open(new File("ClientAdded.app"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					clients.put("" + tad + tPort, new ClientInfo(tad, tPort));
					try {
						sendToClients(api.getTrackId() + " " + api.isPlaying() + " " + api.getPlayerPosition() + " " + System.currentTimeMillis());
					} catch (SpotifyException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	/**
	 * sends tack updates to the clients
	 */
	private void startSender()
	{
		new Thread(() -> {
			while (true)
			{
				//if the track is in a diff pos from when it first started then add send it to the clients to change as well
				try {
					try {
						if(!api.getTrackId().equals(trackID) || api.isPlaying() != playing || Math.abs(api.getPlayerPosition() - pos) >= 3000) {
							pos = api.getPlayerPosition();
							trackID = api.getTrackId();
							playing = api.isPlaying();
							if (api.getTrackId().contains(":ad:"))
								sendToClients("ice" + " " + api.isPlaying() + " " + api.getPlayerPosition() + " " + System.currentTimeMillis());
							else
								sendToClients(api.getTrackId() + " " + api.isPlaying() + " " + api.getPlayerPosition() + " " + System.currentTimeMillis());
							System.out.println(api.getTrackId() + " " + api.isPlaying() + " " + api.getPlayerPosition() + " " + new Date(System.currentTimeMillis()));
							// limits the number of packets sent to only send a packet every 3 seconds
						}
						pos = api.getPlayerPosition();
						trackID = api.getTrackId();
						playing = api.isPlaying();
					}catch (NullPointerException ignored)
					{
						// TODO: 6/12/20 cant send data to the client
					}
				} catch (SpotifyException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * sends tack updates to the clients
	 * @param msg
	 */
	private void sendToClients(String msg)
	{
		/*map contains all clients connected to the system this loop traverses the list of clients and sends the data
		 to them
		*/
		for(Map.Entry<String, ClientInfo> clientInfo: clients.entrySet())
		{
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
}

