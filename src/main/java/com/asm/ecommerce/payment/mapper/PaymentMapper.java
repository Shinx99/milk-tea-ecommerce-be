package com.asm.ecommerce.payment.mapper;

import com.asm.ecommerce.order.domain.Order;
import com.asm.ecommerce.order.dto.payment.OrderSummaryDto;
import com.asm.ecommerce.payment.domain.Payment;
import com.asm.ecommerce.payment.dto.CreatePaymentResponseDto;
import com.asm.ecommerce.payment.dto.PaymentResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId",       source = "payment.orderId")
    @Mapping(target = "paymentId",     source = "payment.id")
    @Mapping(target = "orderCode",     source = "order.orderCode")
    @Mapping(target = "transactionRef",source = "payment.transactionRef")
    @Mapping(target = "paymentUrl",    ignore = true)   // set trong service
    @Mapping(target = "status",        source = "payment.status")
    @Mapping(target = "amount",        source = "payment.amount")
    @Mapping(target = "currency",      source = "payment.currency")
    @Mapping(target = "message",       ignore = true)
    CreatePaymentResponseDto toCreatePaymentResponseDto(OrderSummaryDto order, Payment payment);

    @Mapping(target = "orderId",         source = "payment.orderId")
    @Mapping(target = "paymentId",       source = "payment.id")
    @Mapping(target = "orderCode",       source = "order.orderCode")
    @Mapping(target = "provider",        source = "payment.provider")
    @Mapping(target = "paymentStatus",   source = "payment.status")
    @Mapping(target = "amount",          source = "payment.amount")
    @Mapping(target = "currency",        source = "payment.currency")
    @Mapping(target = "vnpTransactionNo",source = "payment.vnpTransactionNo")
    @Mapping(target = "vnpBankCode",     source = "payment.vnpBankCode")
    @Mapping(target = "vnpPayDate",      source = "payment.vnpPayDate")
    @Mapping(target = "vnpResponseCode", source = "payment.vnpResponseCode")
    @Mapping(target = "message",         ignore = true)
    PaymentResultDto toPaymentResultDto(OrderSummaryDto order, Payment payment);
}


/* Sử dụng trong service
CreatePaymentResponseDto dto = paymentMapper.toCreatePaymentResponseDto(order, payment);
dto.setPaymentUrl(paymentUrl);

PaymentResultDto result = paymentMapper.toPaymentResultDto(order, payment);
result.setMessage("Thanh toán thành công");

*/
