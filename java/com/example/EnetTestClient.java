package com.example;

import org.bespin.enet.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class EnetTestClient {
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
        enet.enet_address_set_host(address, "127.0.0.1");
        address.setPort(5678);
        System.out.printf("host[%d], port[%d]%n", address.getHost(), address.getPort());

        // when creating enet client, the C API need first parameter to be null
        // see http://enet.bespin.org/Tutorial.html
        ENetHost host = enet.enet_host_create(null ,1, 2, 0, 0);
        if (host == null) {
            System.out.println("enet host create failed");
            return;
        }
        System.out.printf("enet host create: %d, port[%d]%n", ret, host.getAddress().getPort());

        ENetPeer peer = enet.enet_host_connect(host, address, 2, 0);
        if (peer == null) {
            System.out.println("enet host connect failed");
            return;
        }


        ENetEvent event = new ENetEvent();

        int loopCount = 0;
        while(true){
            System.out.printf("loopCount[%d]%n", loopCount++);

            ret = enet.enet_host_service(host, event, 0);
            while ( ret > 0){
                ENetEventType t = event.getType();
                if(t.equals(ENetEventType.ENET_EVENT_TYPE_CONNECT)){
                    System.out.println("Connected to server");
                }else if(t.equals(ENetEventType.ENET_EVENT_TYPE_RECEIVE)){
                    ENetPacket packet = event.getPacket();
                    System.out.printf("received %d bytes from server%n", packet.getDataLength());

                    byte[] bytes = new byte[(int) packet.getDataLength()];
                    enet.enet_get_packet_data(packet, bytes);
                    System.out.println("received string = " + new String(bytes, Charset.forName("utf8")));

                }else if(t.equals(ENetEventType.ENET_EVENT_TYPE_DISCONNECT)){
                    String name = "server"+event.getPeer().getIncomingPeerID();
                    System.out.printf("%s disconnected%n", name);
                }
                ret = enet.enet_host_service(host, event, 0);
            }
            if (loopCount % 5 == 0){
                String msg = "current time : " + System.currentTimeMillis();
                ENetPacket p = enet.enet_packet_create(msg.getBytes(Charset.forName("utf8")), enetJNI.ENET_PACKET_FLAG_RELIABLE_get());
                enet.enet_peer_send(peer, (short)0, p);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
