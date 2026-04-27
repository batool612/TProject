package com.tanmeyah.postoffice.Service;

import com.tanmeyah.postoffice.DTO.Reponses.PaymentNotificationResponseDTO;
import com.tanmeyah.postoffice.DTO.Requests.InquiryRequestDTO;
import com.tanmeyah.postoffice.DTO.Reponses.InquiryResponseDTO;
import com.tanmeyah.postoffice.DTO.Requests.PaymentNotificationRequestDTO;

public interface AppService {
    InquiryResponseDTO processInquiry(InquiryRequestDTO request);

    PaymentNotificationResponseDTO processPaymentNotification (PaymentNotificationRequestDTO request);
}