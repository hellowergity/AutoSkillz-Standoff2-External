//
// Created by wergity on 08.04.2025.
//

#include "client.h"

#include <cerrno>
#include <cstring>
#include <unistd.h>

#include <sys/socket.h>
#include <sys/un.h>

#include "utils/logcat.h"

int client::socket_fd = 0;

void client::disconnect()
{
  if (socket_fd) {
    close(socket_fd);
    socket_fd = 0;
  }
}

client::status client::initialize()
{
  if ((socket_fd = socket(AF_UNIX, SOCK_STREAM | SOCK_CLOEXEC, 0)) < 0) {
    logcat::error("Failed to initialize client socket: %s.", strerror(errno));
    return not_created;
  }

  sockaddr_un addr{};
  addr.sun_family = AF_UNIX;
  strncpy(addr.sun_path, SOCKET_NAME, sizeof(addr.sun_path) - 1);

  if (connect(socket_fd, reinterpret_cast<sockaddr*>(&addr), sizeof(addr)) < 0) {
    logcat::error("Failed to connect to server: %s.", strerror(errno));
    disconnect();
    return not_connected;
  }

  return created;
}

bool client::write_buffer(void* data, std::size_t length)
{
  ssize_t progress = 0;
  auto buffer = static_cast<char*>(data);

  while (length) {
    do {
      progress = write(socket_fd, buffer, length);
    } while (progress == -1 && errno == EINTR);

    if (progress <= 0) {
      disconnect();
      break;
    }

    length -= progress;
    buffer += progress;
  }

  return progress > 0;
}

bool client::read_buffer(void* data, std::size_t length)
{
  ssize_t progress = 0;
  auto buffer = static_cast<char*>(data);

  while (length) {
    do {
      progress = read(socket_fd, buffer, length);
    } while (progress == -1 && EINTR == errno);

    if (progress <= 0) {
      disconnect();
      break;
    }

    length -= progress;
    buffer += progress;
  }

  return progress > 0;
}

bool client::send(void* data, std::size_t length)
{
  if (!write_buffer(&length, sizeof(length))) {
    disconnect();
    return false;
  }

  return write_buffer(data, length);
}

bool client::recv(void* data)
{
  std::size_t length;
  if (!read_buffer(&length, sizeof(length))) {
    disconnect();
    return false;
  }

  return read_buffer(data, length);
}