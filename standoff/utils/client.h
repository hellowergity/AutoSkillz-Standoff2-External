//
// Created by wergity on 3/24/25.
//

#ifndef CLIENT_H
#define CLIENT_H

#include <cstddef>

#define SOCKET_NAME "\0KittenWare"

namespace client
{
  enum status : int
  {
    created,
    not_created,
    not_connected
  };

  extern int socket_fd;

  void disconnect();

  status initialize();

  bool write_buffer(void* data, std::size_t length);

  bool read_buffer(void* data, std::size_t length);

  bool send(void* data, std::size_t length);

  bool recv(void* data);
}

#endif //CLIENT_H