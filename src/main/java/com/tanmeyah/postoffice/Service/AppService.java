package com.tanmeyah.postoffice.Service;

import com.tanmeyah.postoffice.DTO.Requests.InquiryRequestDTO;
import com.tanmeyah.postoffice.DTO.Reponses.InquiryResponseDTO;

public interface AppService {
    InquiryResponseDTO processInquiry(InquiryRequestDTO request);
}