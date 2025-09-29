//package com.example.guardpay.api;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class PingControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Test
//    void ping_returnsPong() throws Exception {
//        mockMvc.perform(get("/api/ping"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("pong"));
//    }
//}