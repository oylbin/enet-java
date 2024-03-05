package com.example;
import org.bespin.enet.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EnetTest {
    static {
        System.loadLibrary("enet");
    }
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("hello, world!");
        System.out.printf("enet version: %d.%d.%d\n", enet.ENET_VERSION_MAJOR, enet.ENET_VERSION_MINOR, enet.ENET_VERSION_PATCH);
        int ret = enet.enet_initialize();
        System.out.printf("enet initialize: %d\n", ret);
        ENetAddress address = new ENetAddress();
        // convert ip address string to long
        String IPString = "127.0.0.1";
        InetAddress i = InetAddress.getByName(IPString);
        int result = 0;
        for (byte b: i.getAddress()){
            result = result << 8 | (b & 0xFF);
        }
        address.setHost(result);
        address.setPort(5678);
        long max_clients = 32;
        System.out.printf("host[%d], port[%d]\n", address.getHost(), address.getPort());
        // wait for user input
        System.out.println("Press any key to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ENetHost host = enet.enet_host_create(address, max_clients, 2, 0, 0);
        if (host == null) {
            System.out.println("enet host create failed");
            return;
        }
        System.out.printf("enet host create: %d, port[%d]\n", ret, host.getAddress().getPort());
        ENetEvent event = new ENetEvent();
        ret = enet.enet_host_service(host, event, 1000);
        System.out.printf("enet host service: %d\n", ret);


//        System.out.printf("enet address set host: %d\n", ret);
        // ret = enet.enet_address_set_host_ip(enet_address_set_host_ip)
        //System.out.printf("enet address host: %s\n", enet.enet_address_get_host(address));



    }
}
