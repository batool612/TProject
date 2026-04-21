package com.tanmeyah.postoffice.Controller;

import com.tanmeyah.postoffice.DTO.Requests.InquiryRequestDTO;
import com.tanmeyah.postoffice.DTO.Reponses.InquiryResponseDTO;
import com.tanmeyah.postoffice.Service.AppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tanmeyah")
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;

    @PostMapping("/inquiry")
    public ResponseEntity<InquiryResponseDTO> inquiry(@Valid @RequestBody InquiryRequestDTO request) {
        return ResponseEntity.ok(appService.processInquiry(request));
    }
}