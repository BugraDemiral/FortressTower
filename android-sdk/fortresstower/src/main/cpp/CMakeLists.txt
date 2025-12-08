cmake_minimum_required(VERSION 3.22.1)

project("fortresstower")

add_library(
        fortresstower
        SHARED
        anti_debug.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        fortresstower
        ${log-lib}
)