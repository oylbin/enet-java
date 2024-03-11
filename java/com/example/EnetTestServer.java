package com.example;
import org.bespin.enet.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class EnetTestServer {
    static {
        System.loadLibrary("enet");
    }
    public static String print(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (byte b : bytes) {
            sb.append(String.format("0x%02X ", b));
        }
        sb.append("]");
        return sb.toString();
    }
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("hello, world!");
        System.out.printf("enet version: %d.%d.%d%n", enet.ENET_VERSION_MAJOR, enet.ENET_VERSION_MINOR, enet.ENET_VERSION_PATCH);
        int ret = enet.enet_initialize();
        System.out.printf("enet initialize: %d%n", ret);
        ENetAddress address = new ENetAddress();
        // convert ip address string to long
        String IPString = "127.0.0.1";
        InetAddress i = InetAddress.getByName(IPString);
        int result = 0;
        for (byte b: i.getAddress()){
            result = result << 8 | (b & 0xFF);
        }
        address.setHost(result);
        address.setHost(0);
        address.setPort(5678);
        long max_clients = 32;
        System.out.printf("host[%d], port[%d]%n", address.getHost(), address.getPort());
        // wait for user input
        /*
        System.out.println("Press any key to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        ENetHost host = enet.enet_host_create(address, max_clients, 2, 0, 0);
        if (host == null) {
            System.out.println("enet host create failed");
            return;
        }
        System.out.printf("enet host create: %d, port[%d]%n", ret, host.getAddress().getPort());
        ENetEvent event = new ENetEvent();


        while(true){
            ret = enet.enet_host_service(host, event, 5000);
            System.out.printf("enet host service: %d%n", ret);
            ENetEventType t = event.getType();
            if(t.equals(ENetEventType.ENET_EVENT_TYPE_CONNECT)){
                String name = "client"+event.getPeer().getIncomingPeerID();
                System.out.printf("%s connected%n", name);
                String message = "hello, " + name;
                ENetPacket p = enet.enet_packet_create(message.getBytes(Charset.forName("utf8")), enetJNI.ENET_PACKET_FLAG_RELIABLE_get());
                enet.enet_peer_send(event.getPeer(), (short)0, p);
            }else if(t.equals(ENetEventType.ENET_EVENT_TYPE_RECEIVE)){
                ENetPacket packet = event.getPacket();
                String name = "client"+event.getPeer().getIncomingPeerID();
                System.out.printf("received %d bytes from %s%n", packet.getDataLength(), name);
                // packet->data is ignored in src/swag.i
                //          %ignore _ENetPacket::data;
                // remove this line does not work. I don't know how to resolve it.
                // https://stackoverflow.com/questions/11965992/convert-a-member-of-structure-of-type-signed-char-to-byte-array-in-java-byte?rq=3

                // Now I find another solution by create a C function
                // `size_t enet_get_packet_data(ENetPacket *p, char data[], size_t len)`
                // see src/swig.i for details.
                // https://swig.org/Doc4.0/SWIGDocumentation.html#Java_binary_char

                byte[] bytes = new byte[(int) packet.getDataLength()];
                enet.enet_get_packet_data(packet, bytes);
                System.out.println("string = " + new String(bytes, Charset.forName("utf8")));
                String msg = "server received "+ packet.getDataLength() + " bytes from you.";
                ENetPacket p = enet.enet_packet_create(msg.getBytes(Charset.forName("utf8")), enetJNI.ENET_PACKET_FLAG_RELIABLE_get());
                enet.enet_peer_send(event.getPeer(), (short)0, p);

                // send a packet to all clients
                String broadcastMsg = name + ": " + new String(bytes, Charset.forName("utf8"));
                ENetPacket p1 = enet.enet_packet_create(broadcastMsg.getBytes(Charset.forName("utf8")), enetJNI.ENET_PACKET_FLAG_RELIABLE_get());
                enet.enet_host_broadcast(host, (short)0, p1);
            }else if(t.equals(ENetEventType.ENET_EVENT_TYPE_DISCONNECT)){
                String name = "client"+event.getPeer().getIncomingPeerID();
                System.out.printf("%s disconnected%n", name);
            }
        }
    }
}
