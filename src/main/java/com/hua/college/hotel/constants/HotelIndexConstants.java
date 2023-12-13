package com.hua.college.hotel.constants;

public class HotelIndexConstants {
    public static final String MAPPING_TEMPLATE = "{\"address\":{\"type\":\"keyword\",\"index\":false},\"all\":{\"type\":\"text\",\"analyzer\":\"text_anlyzer\",\"search_analyzer\":\"ik_smart\"},\"brand\":{\"type\":\"keyword\",\"copy_to\":[\"all\"]},\"business\":{\"type\":\"keyword\",\"copy_to\":[\"all\"]},\"city\":{\"type\":\"keyword\"},\"id\":{\"type\":\"keyword\"},\"location\":{\"type\":\"geo_point\"},\"name\":{\"type\":\"text\",\"copy_to\":[\"all\"],\"analyzer\":\"text_anlyzer\",\"search_analyzer\":\"ik_smart\"},\"pic\":{\"type\":\"keyword\",\"index\":false},\"price\":{\"type\":\"integer\"},\"score\":{\"type\":\"integer\"},\"starName\":{\"type\":\"keyword\"},\"suggestion\":{\"type\":\"completion\",\"analyzer\":\"completion_analyzer\",\"preserve_separators\":true,\"preserve_position_increments\":true,\"max_input_length\":50}}";
}
