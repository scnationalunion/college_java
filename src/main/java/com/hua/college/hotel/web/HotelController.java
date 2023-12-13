package com.hua.college.hotel.web;

import com.hua.college.hotel.pojo.Hotel;
import com.hua.college.hotel.pojo.HotelDoc;
import com.hua.college.hotel.pojo.PageResult;
import com.hua.college.hotel.pojo.RequestParams;
import com.hua.college.hotel.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private IHotelService hotelService;

    @PostMapping("/list")
    public PageResult search(@RequestBody RequestParams params) {
        return hotelService.search(params);
    }

    @PostMapping("/filters")
    public Map<String, List<String>> searchFilters(@RequestBody RequestParams params) {
        return hotelService.searchFilters(params);
    }

    @GetMapping("/suggestion")
    public List<String> getSuggestion(@RequestParam("key") String prefix) throws IOException {
        return hotelService.getSuggestion(prefix);
    }

    @PostMapping("/delete")
    public HotelDoc delete(@RequestBody HotelDoc hotelDoc) {
        return hotelService.deleteFromEs(hotelDoc);
    }

    @PostMapping("/update")
    public HotelDoc update(@RequestBody Hotel hotelDoc)   {
        return hotelService.updateFromEs(hotelDoc);
    }

    @PostMapping("/insert")
    public HotelDoc insert(@RequestBody Hotel hotelDoc) {
         return hotelService.insertIntoEs(hotelDoc);
    }
}
