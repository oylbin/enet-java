%module enet

%include <arrays_java.i>
%include <carrays.i>
%include <cpointer.i>
%include <typemaps.i>
%include <various.i>
%include <enumtypesafe.swg>

%ignore enet_initialize_with_callbacks;

%ignore enet_host_bandwidth_throttle;
%ignore enet_host_random_seed;
%ignore enet_host_compress;

%ignore enet_peer_throttle;
%ignore enet_peer_reset_queues;
%ignore enet_peer_setup_outgoing_command;
%ignore enet_peer_queue_outgoing_command;
%ignore enet_peer_queue_incoming_command;
%ignore enet_peer_queue_acknowledgement;
%ignore enet_peer_dispatch_incoming_unreliable_commands;
%ignore enet_peer_dispatch_incoming_reliable_commands;
%ignore enet_peer_on_connect;
%ignore enet_peer_on_disconnect;

%ignore enet_crc32;

%ignore enet_range_coder_create;
%ignore enet_range_coder_destroy;
%ignore enet_range_coder_compress;
%ignore enet_range_coder_decompress;

%ignore enet_protocol_command_size;

%ignore enet_malloc;
%ignore enet_free;

%ignore enet_list_clear;
%ignore enet_list_insert;
%ignore enet_list_remove;
%ignore enet_list_move;
%ignore enet_list_size;

%ignore enet_socket_create;
%ignore enet_socket_bind;
%ignore enet_socket_get_address;
%ignore enet_socket_listen;
%ignore enet_socket_accept;
%ignore enet_socket_connect;
%ignore enet_socket_send;
%ignore enet_socket_receive;
%ignore enet_socket_wait;
%ignore enet_socket_set_option;
%ignore enet_socket_get_option;
%ignore enet_socket_shutdown;
%ignore enet_socket_destroy;
%ignore enet_socketset_select;

%ignore enet_time_get;
%ignore enet_time_set;

%ignore _ENetAcknowledgement;
%ignore _ENetIncomingCommand;
%ignore ENetIncomingCommand;
%ignore _ENetOutgoingCommand;
%ignore ENetOutgoingCommand;
%ignore _ENetCompressor;
%ignore _ENetProtocol;
%ignore _ENetProtocolAcknowledge;
%ignore _ENetProtocolBandwidthLimit;
%ignore ENetProtocolCommand;
%ignore _ENetProtocolCommandHeader;
%ignore _ENetProtocolConnect;
%ignore _ENetProtocolDisconnect;
%ignore ENetProtocolFlag;
%ignore _ENetProtocolHeader;
%ignore _ENetProtocolPing;
%ignore _ENetProtocolSendFragment;
%ignore _ENetProtocolSendReliable;
%ignore _ENetProtocolSendUnreliable;
%ignore _ENetProtocolSendUnsequenced;
%ignore _ENetProtocolThrottleConfigure;
%ignore _ENetProtocolVerifyConnect;
%ignore _ENetCallbacks;
%ignore ENetBuffer;
%ignore _ENetChannel;
%ignore _ENetList;
%ignore _ENetListNode;

%ignore _ENetHost::commands;
%ignore _ENetHost::compressor;
%ignore _ENetHost::packetData;
%ignore _ENetHost::checksum;
%ignore _ENetHost::intercept;
%ignore _ENetHost::buffers;
%ignore _ENetHost::receivedData;
%ignore _ENetHost::dispatchQueue;

%ignore _ENetPeer::dispatchList;
%ignore _ENetPeer::data;
%ignore _ENetPeer::acknowledgements;
%ignore _ENetPeer::sentReliableCommands;
%ignore _ENetPeer::sentUnreliableCommands;
%ignore _ENetPeer::outgoingReliableCommands;
%ignore _ENetPeer::outgoingUnreliableCommands;
%ignore _ENetPeer::dispatchedCommands;
%ignore _ENetPeer::channels;

%ignore _ENetPacket::freeCallback;
%ignore _ENetPacket::userData;
%ignore _ENetPacket::data;

%apply (char *STRING, size_t LENGTH) { (const void *, size_t) };
%apply unsigned char *INOUT { enet_uint8 * };
%apply char *STRING { const char * hostName };
%apply (char *STRING, size_t LENGTH) { (char * hostName, size_t nameLength) };

%{
#include "enet/unix.h"
#include "enet/types.h"
#include "enet/protocol.h"
#include "enet/list.h"
#include "enet/callbacks.h"
#include "enet/enet.h"
%}

%include <enet/unix.h>
%include <enet/types.h>
%include <enet/protocol.h>
%include <enet/list.h>
%include <enet/callbacks.h>
%include <enet/enet.h>

// https://swig.org/Doc4.0/SWIGDocumentation.html#Java_binary_char
%apply (char *STRING, size_t LENGTH) { (char data[], size_t len) }
%inline %{
size_t enet_get_packet_data(ENetPacket *p, char data[], size_t len) {
  if (p == NULL){
      return 0;
  }
  if (p->data == NULL){
      return 0;
  }
  if( len > p->dataLength ){
    len = p->dataLength;
  }
  memcpy(data, p->data, len);
  return len;
}
%}
