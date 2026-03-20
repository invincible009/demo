package com.sdl.demo.testLocation;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ip2locationClient", url = "${ip2location.base-url:https://api.ip2location.io}")
public interface Ip2LocationFeignClient {

    @GetMapping
    Map<String, Object> getLocation(@RequestParam("ip") String ip);

    @GetMapping
    Map<String, Object> getCurrentLocation();
}
