package com.asm.ecommerce.order.service;

import com.asm.ecommerce.auth.service.UserService;
import com.asm.ecommerce.customer.service.customer.CustomerService;
import com.asm.ecommerce.order.domain.Order;
import com.asm.ecommerce.order.dto.invoice.InvoiceDataDto;
import com.asm.ecommerce.order.dto.invoice.InvoiceItemDto;
import com.asm.ecommerce.order.mapper.InvoiceMapper;
import com.asm.ecommerce.order.repository.OrderRepository;
import com.asm.ecommerce.shared.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceEmailJob {

    private final CustomerService customerService;
    private final UserService userService;
    private final InvoiceMapper invoiceMapper;
    private final InvoicePdfService invoicePdfService;
    private final EmailService emailService;
    private final OrderRepository orderRepository;   // thêm dependency


    /*@Async
    public void sendInvoiceAsync(Order order) {
        try {
            UUID userId = customerService.getUserIdByCustomerId(order.getCustomerId());
            String toEmail = userService.getEmailByUserId(userId);
            if (toEmail == null) {
                log.warn("No email for order {}", order.getId());
                return;
            }

            InvoiceDataDto invoiceData = invoiceMapper.toInvoiceData(order);
            List<InvoiceItemDto> items = invoiceMapper.toInvoiceItemDtos(order.getItems());
            int i = 1;
            for (InvoiceItemDto item : items) {
                item.setStt(i++);
            }
            invoiceData.setItems(items);
            invoiceData.setCustomerEmail(toEmail);

            byte[] pdfBytes = invoicePdfService.generateInvoicePdf(invoiceData);
            String subject = "Hóa đơn đơn hàng " + invoiceData.getOrderCode();
            String body = "Cảm ơn bạn đã tin tưởng và đặt hàng tại GÓC MÈO LƯỜI! Hóa đơn chi tiết nằm trong file đính kèm.";

            emailService.sendInvoiceEmail(
                    toEmail, subject, body, pdfBytes,
                    "Hoa_Don_" + invoiceData.getOrderCode() + ".pdf"
            );
        } catch (Exception e) {
            log.error("Error sending invoice email async for order {}", order.getId(), e);
        }
    }*/

    @Async
    @Transactional
    public void sendInvoiceAsync(UUID orderId) {
        try {
            // load lại Order trong session mới
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            UUID userId = customerService.getUserIdByCustomerId(order.getCustomerId());
            String toEmail = userService.getEmailByUserId(userId);
            if (toEmail == null) {
                log.warn("No email for order {}", order.getId());
                return;
            }

            // lúc này order.getItems() được load trong session hợp lệ
            InvoiceDataDto invoiceData = invoiceMapper.toInvoiceData(order);
            List<InvoiceItemDto> items = invoiceMapper.toInvoiceItemDtos(order.getItems());
            int i = 1;
            for (InvoiceItemDto item : items) {
                item.setStt(i++);
            }
            invoiceData.setItems(items);
            invoiceData.setCustomerEmail(toEmail);

            byte[] pdfBytes = invoicePdfService.generateInvoicePdf(invoiceData);
            String subject = "Hóa đơn đơn hàng " + invoiceData.getOrderCode();
            String body = "Cảm ơn bạn đã tin tưởng và đặt hàng tại GÓC MÈO LƯỜI! Hóa đơn chi tiết nằm trong file đính kèm.";

            emailService.sendInvoiceEmail(
                    toEmail, subject, body, pdfBytes,
                    "Hoa_Don_" + invoiceData.getOrderCode() + ".pdf"
            );
        } catch (Exception e) {
            log.error("Error sending invoice email async for order {}", orderId, e);
        }
    }
}

/*

Từ Order, class lấy ra email khách hàng và map dữ liệu đơn sang DTO hóa đơn.

Tạo file PDF hóa đơn và gửi email cảm ơn kèm file đính kèm.

Việc gửi mail chạy bất đồng bộ (@Async) để không làm chậm luồng thanh toán.

*/


