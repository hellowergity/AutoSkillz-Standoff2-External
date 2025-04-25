#include <thread>

#include "java/esp.h"
#include "java/menu.h"

#include "utils/logcat.h"
#include "utils/server.h"

[[noreturn]] void server_main()
{
  while (true) {
    if (!server::connected) {
      server::connect();
      continue;
    }

    if (!server::send(&vars::preferences, sizeof(vars::preferences))) {
      continue;
    }

    player_list_t player_list{};
    if (!server::recv(&player_list)) {
      continue;
    }

    overlay->player_list = player_list;
  }
}

bool register_natives(JNIEnv* env)
{
  const JNINativeMethod methods[] = {
    {
      "signal",
      "(Lcom/wergity/autoskillzex/natives/ESPView;Landroid/graphics/Canvas;)V",
      reinterpret_cast<void*>(&esp::signal)
    },
    {
      "signal",
      "(III)V",
      reinterpret_cast<void*>(&menu::signal)
    }
  };

  jclass klass = env->FindClass("com/wergity/autoskillzex/natives/Daemon");
  if (!klass) {
    return false;
  }

  return env->RegisterNatives(klass, methods, std::size(methods)) == JNI_OK;
}

extern "C" JNIEXPORT
jint JNI_OnLoad(JavaVM* vm, [[maybe_unused]] void* reserved)
{
  JNIEnv* env = nullptr;
  if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) == JNI_OK) {
    if (!register_natives(env)) {
      logcat::error("Failed to register native methods");
      return JNI_ERR;
    }

    if (!server::initialize()) {
      logcat::error("Failed to initialize server");
      return JNI_ERR;
    }

    std::thread(server_main).detach();
  }

  return JNI_VERSION_1_6;
}