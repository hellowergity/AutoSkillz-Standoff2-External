//
// Created by wergity on 10.04.2025.
//

#include "server.h"

#include <cerrno>
#include <thread>
#include <unistd.h>

#include <sys/socket.h>
#include <sys/un.h>

#include "utils/logcat.h"

int  server::socket_fd = 0;
int  server::client_fd = 0;
bool server::connected = false;

void server::destroy()
{
  if (client_fd) {
    close(client_fd);
    client_fd = 0;
  }

  if (socket_fd) {
    close(socket_fd);
    socket_fd = 0;
  }

  if (connected) {
    connected = false;
  }
}

void server::disconnect()
{
  if (client_fd) {
    close(client_fd);
    client_fd = 0;
  }

  if (connected) {
    connected = false;
  }
}

bool server::initialize()
{
  if ((socket_fd = socket(AF_UNIX, SOCK_STREAM | SOCK_CLOEXEC, 0)) < 0) {
    logcat::error("Failed to initialize socket server: %s.", strerror(errno));
    return false;
  }

  sockaddr_un addr{};
  addr.sun_family = AF_UNIX;
  strncpy(addr.sun_path, SOCKET_NAME, sizeof(addr.sun_path) - 1);

  if (bind(socket_fd, reinterpret_cast<sockaddr*>(&addr), sizeof(addr)) < 0) {
    logcat::error("Failed to bind socket: %s.", strerror(errno));
    destroy();
    return false;
  }

  if (listen(socket_fd, SOCKET_BACKLOG) < 0) {
    logcat::error("Failed to listen on socket: %s.", strerror(errno));
    destroy();
    return false;
  }

  return true;
}

void server::connect()
{
  if (!connected) {
    if ((client_fd = accept4(socket_fd, nullptr, nullptr, SOCK_CLOEXEC)) < 0) {
      logcat::error("Failed to accept client: %s.", strerror(errno));
      return;
    }

    connected = true;
  }
}

bool server::write_buffer(void* data, std::size_t length)
{
  ssize_t progress = 0;
  auto buffer = static_cast<char*>(data);

  while (length) {
    do {
      progress = write(client_fd, buffer, length);
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

bool server::read_buffer(void* data, std::size_t length)
{
  ssize_t progress = 0;
  auto buffer = static_cast<char*>(data);

  while (length) {
    do {
      progress = read(client_fd, buffer, length);
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

bool server::send(void* data, std::size_t length)
{
  if (!write_buffer(&length, sizeof(length))) {
    disconnect();
    return false;
  }

  return write_buffer(data, length);
}

bool server::recv(void* data)
{
  std::size_t length;
  if (!read_buffer(&length, sizeof(length))) {
    disconnect();
    return false;
  }

  return read_buffer(data, length);
}