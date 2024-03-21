package org.example;

import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import packets.Packet;
import packets.Packet1Connect;
import packets.Packet2Line;

public class Main {

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		Client client = new Client();
		client.start();
		client.connect(5000, "127.0.0.1", 54555, 54777);

		client.getKryo().register(Packet.class);
		client.getKryo().register(Packet1Connect.class);
		client.getKryo().register(Packet2Line.class);

		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof Packet) {
					if (object instanceof Packet2Line) {
						Packet2Line P2 = (Packet2Line) object;
						System.out.println(P2.line);
					}
				}
			}
		});

		Packet1Connect packet = new Packet1Connect();
		packet.name = "Hugo";
		client.sendTCP(packet);

		while (client.isConnected()) {
			String line = scanner.nextLine();
			Packet2Line packet2 = new Packet2Line();
			packet2.line = line;
			if (line.equalsIgnoreCase("exit")) {
				client.close();
				break;
			}

			client.sendTCP(packet2);

		}

	}

}
