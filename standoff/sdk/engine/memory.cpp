//
// Created by wergity on 07.04.2025.
//

#include "memory.h"

int memory::game_pid = 0;

std::uintptr_t memory::game_library = 0;

bool memory::find_process()
{
  for (const auto& entry : std::filesystem::directory_iterator("/proc/")) {
    if (entry.is_directory()) {
      std::string pid = entry.path().filename().string();
      std::ifstream file("/proc/" + pid + "/cmdline");
      if (!file.is_open()) {
        continue;
      }

      std::string line;
      if (!std::getline(file, line)) {
        continue;
      }

      file.close();
      if (const std::size_t end = line.find('\0'); end != std::string::npos) {
        line.resize(end);
      }

      if (line == "com.axlebolt.standoff2") {
        game_pid = std::stoi(pid);
        return true;
      }
    }
  }

  return false;
}

bool memory::find_library()
{
  std::ifstream file("/proc/" + std::to_string(game_pid) + "/maps");
  if (!file.is_open()) {
    return false;
  }

  std::string line;
  while (std::getline(file, line)) {
    std::array<char, 2> perms{};
    unsigned long long start;
    unsigned long long end;

    // NOLINTNEXTLINE(cert-err34-c)
    std::sscanf(line.c_str(), "%llx-%llx %1s", &start, &end, perms.data());
    if (const std::size_t length = end - start; length < SELFMAG) {
      continue;
    }

    std::array<char, SELFMAG> magic{};
    if (!start || !end || perms[0] != 'r' || !read(start, magic.data(), magic.size())) {
      continue;
    }

    if (std::memcmp(magic.data(), ELFMAG, SELFMAG) == 0) {
      if (line.find("libunity.so") != std::string::npos) {
        game_library = start;
      }

      if (line.find("libmain.so") != std::string::npos && start > game_library) {
        break;
      }
    }
  }

  file.close();
  return game_library != 0;
}

bool memory::read(const std::uintptr_t address, void* buffer, const std::size_t length)
{
  const iovec local {
    .iov_base = buffer,
    .iov_len = length
  };

  const iovec remote {
    .iov_base = reinterpret_cast<void*>(address),
    .iov_len = length
  };

  ssize_t bytes_read;
  do {
    bytes_read = process_vm_readv(game_pid, &local, 1, &remote, 1, 0);
  } while (bytes_read == -1 && errno == EINTR);

  return bytes_read == length;
}

bool memory::initialize()
{
  if (!find_process()) {
    return false;
  }

  if (!find_library()) {
    return false;
  }

  return true;
}