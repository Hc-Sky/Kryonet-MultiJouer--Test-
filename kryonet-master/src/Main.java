import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import packets.Packet;
import packets.Packet1Connect;
import packets.Packet2Line;

public class Main {
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.start();
		server.bind(54555, 54777);

		server.getKryo().register(Packet.class);
		server.getKryo().register(Packet1Connect.class);
		server.getKryo().register(Packet2Line.class);


		server.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof Packet) {
					if (object instanceof Packet1Connect) {
						Packet1Connect packet1 = (Packet1Connect) object;
						System.out.println("[" + connection.getID() + " Connected ] " + packet1.name);
					}else if (object instanceof Packet2Line) {
						Packet2Line packet2 = (Packet2Line) object;
						System.out.println("[ " + connection.getID() + " Client Said: ] " + packet2.line);

						Packet2Line resend = new Packet2Line();
						resend.line = "You Said: " + packet2.line;
						connection.sendTCP(resend);
					}
				}
			}
		});
	}

}
