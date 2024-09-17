package com.mtfn.kafka_example.controller;

import com.mtfn.kafka_example.service.StoreService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/stores", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/valid")
    public boolean isStoreExists(@RequestParam String storeCode) {
        return storeService.isStoreExists(storeCode);
    }
}
