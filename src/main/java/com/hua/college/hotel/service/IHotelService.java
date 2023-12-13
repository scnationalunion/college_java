package com.hua.college.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.college.hotel.pojo.Hotel;
import com.hua.college.hotel.pojo.HotelDoc;
import com.hua.college.hotel.pojo.PageResult;
import com.hua.college.hotel.pojo.RequestParams;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IHotelService extends IService<Hotel> {
    PageResult search(RequestParams params);

    Map<String, List<String>> searchFilters(RequestParams params);

    List<String> getSuggestion(String prefix) throws IOException;

    HotelDoc updateFromEs(Hotel hotel);

    HotelDoc insertIntoEs(Hotel hotel);

    public void insertById(Long id);

    HotelDoc deleteFromEs(HotelDoc hotelDoc);

}
