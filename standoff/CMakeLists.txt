cmake_minimum_required(VERSION 3.30)

project("standoff")

set(CMAKE_CXX_STANDARD 20)
set(LIBRARY_NAME "libHelloKittySO.so")

set(BASE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/..)
set(LIBS_DIR ${BASE_DIR}/loader/app/src/main/jniLibs)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY "${LIBS_DIR}/${CMAKE_ANDROID_ARCH_ABI}")

add_executable(${LIBRARY_NAME}
        main.cpp

        features/aim.cpp
        features/esp.cpp

        utils/logcat.cpp
        utils/client.cpp

        sdk/engine/memory.cpp
        sdk/engine/camera.cpp
        sdk/engine/transform.cpp

        sdk/lazy_singleton.cpp
        sdk/player_manager.cpp
        sdk/player_controller.cpp
)

include_directories(${CMAKE_CURRENT_SOURCE_DIR})

target_link_libraries(${LIBRARY_NAME} android log)