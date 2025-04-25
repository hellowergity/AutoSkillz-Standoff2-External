cmake_minimum_required(VERSION 3.30)

project(native)

set(CMAKE_CXX_STANDARD 20)

set(LIBRARY_NAME "Cat")

set(BASE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/..)
set(LIBS_DIR ${BASE_DIR}/loader/app/src/main/jniLibs)
set(OUTPUT_DIRECTORY "${LIBS_DIR}/${CMAKE_ANDROID_ARCH_ABI}")

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${OUTPUT_DIRECTORY})
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${OUTPUT_DIRECTORY})
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${OUTPUT_DIRECTORY})

add_library(${LIBRARY_NAME} SHARED
        main.cpp

        utils/server.cpp
        utils/logcat.cpp
        utils/server.cpp

        java/vars.cpp
        java/menu.cpp
        java/esp.cpp
)

include_directories(${CMAKE_CURRENT_SOURCE_DIR})

target_link_libraries(${LIBRARY_NAME} android log)