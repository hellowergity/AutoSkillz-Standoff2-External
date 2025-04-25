//
// Created by wergity on 10.04.2025.
//

#ifndef SERVER_H
#define SERVER_H

#include <cstddef>

#define SOCKET_NAME    "\0KittenWare"
#define SOCKET_BACKLOG 0x8

namespace server
{
  extern int  socket_fd;
  extern int  client_fd;
  extern bool connected;

  void destroy();

  void disconnect();

  bool initialize();

  void connect();

  bool write_buffer(void* data, std::size_t length);

  bool read_buffer(void* data, std::size_t length);

  bool send(void* data, std::size_t length);

  bool recv(void* data);
}

#endif //SERVER_H