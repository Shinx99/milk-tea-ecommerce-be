package com.asm.ecommerce.order.service;

import com.asm.ecommerce.order.dto.invoice.InvoiceItemDto;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.asm.ecommerce.order.dto.invoice.InvoiceDataDto;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;


@Service
@Slf4j
public class InvoicePdfService {

    @Value("${app.shop.name:GÓC MÈO LƯỜI}")
    private String shopName;

    @Value("${app.shop.address:Đ. Lương Định Của, Đông Hoà, Dĩ An, Bình Dương}")
    private String shopAddress;

    @Value("${app.shop.phone:079 7897 133}")
    private String shopPhone;

    public byte[] generateInvoicePdf(InvoiceDataDto invoiceData) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Dùng font Unicode DejaVuSans hỗ trợ tiếng Việt
            PdfFont unicodeFont = PdfFontFactory.createFont(
                    "fonts/DejaVuSans.ttf",
                    PdfEncodings.IDENTITY_H
            );

            document.setFont(unicodeFont);
            
            // Set margins
            document.setMargins(20, 20, 20, 20);

            // ===== Header =====
            addHeader(document);
            document.add(new Paragraph("\n"));

            // ===== Order Info =====
            addOrderInfo(document, invoiceData);
            document.add(new Paragraph("\n"));

            // ===== Customer Info =====
            addCustomerInfo(document, invoiceData);
            document.add(new Paragraph("\n"));

            // ===== Items Table =====
            addItemsTable(document, invoiceData);
            document.add(new Paragraph("\n"));

            // ===== Totals =====
            addTotals(document, invoiceData);
            document.add(new Paragraph("\n"));

            // ===== Footer =====
            addFooter(document);

            document.close();
            
            log.info("PDF generated successfully for order: {}", invoiceData.getOrderCode());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Error generating invoice PDF", e);
        }
    }

    private void addHeader(Document document) {
        // Shop name
        Paragraph shopNamePara = new Paragraph(shopName)
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(shopNamePara);

        // Shop address and phone
        Paragraph shopInfoPara = new Paragraph(shopAddress + " | " + shopPhone)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(shopInfoPara);

        // Title
        Paragraph titlePara = new Paragraph("HÓA ĐƠN")
                .setFontSize(14)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titlePara);
    }

    private void addOrderInfo(Document document, InvoiceDataDto invoiceData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Table infoTable = new Table(new float[]{1, 1});
        infoTable.setWidth(UnitValue.createPercentValue(100));

        infoTable.addCell(new Cell()
                .add(new Paragraph("Mã đơn: " + invoiceData.getOrderCode()).setFontSize(11).setBold())
                .setBorder(null));

        String dateText = "";
        if (invoiceData.getPlacedAt() != null) {
            dateText = invoiceData.getPlacedAt()
                    .atZone(java.time.ZoneId.systemDefault())
                    .format(formatter);
        }
        infoTable.addCell(new Cell()
                .add(new Paragraph("Ngày đặt: " + dateText).setFontSize(11))
                .setBorder(null));

        document.add(infoTable);
    }


    private void addCustomerInfo(Document document, InvoiceDataDto invoiceData) {
        Paragraph title = new Paragraph("Thông tin giao hàng")
                .setFontSize(12)
                .setBold();
        document.add(title);

        Table custTable = new Table(new float[]{1.2f, 3.8f});
        custTable.setWidth(UnitValue.createPercentValue(60));  // hẹp lại, nhìn gọn hơn

        addRow(custTable, "Tên:", invoiceData.getCustomerName());
        addRow(custTable, "SĐT:", invoiceData.getPhone());
        addRow(custTable, "Địa chỉ:", invoiceData.getAddress());

        document.add(custTable);
    }


    private void addItemsTable(Document document, InvoiceDataDto invoiceData) {

        if (invoiceData.getItems() == null || invoiceData.getItems().isEmpty()) {
            return;
        }

        Paragraph itemsTitle = new Paragraph("Chi tiết đơn hàng")
                .setFontSize(12)
                .setBold();
        document.add(itemsTitle);

        Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{0.7f, 3, 1.2f, 1.5f, 1.5f}))
                .useAllAvailableWidth();


        addTableHeader(itemsTable, "STT");
        addTableHeader(itemsTable, "Tên sản phẩm");
        addTableHeader(itemsTable, "Số lượng");
        addTableHeader(itemsTable, "Đơn giá");
        addTableHeader(itemsTable, "Thành tiền");

        for (InvoiceItemDto item : invoiceData.getItems()) {
            String stt       = item.getStt()       != null ? item.getStt().toString()        : "";
            String name      = item.getProductName() != null ? item.getProductName()        : "";
            String quantity  = item.getQuantity()  != null ? item.getQuantity().toString()   : "";
            String unitPrice = item.getUnitPrice() != null ? formatMoney(item.getUnitPrice()) : "";
            String lineTotal = item.getLineTotal() != null ? formatMoney(item.getLineTotal()) : "";

            itemsTable.addCell(createCell(stt, TextAlignment.CENTER));
            itemsTable.addCell(createCell(name, TextAlignment.LEFT));
            itemsTable.addCell(createCell(quantity, TextAlignment.CENTER));
            itemsTable.addCell(createCell(unitPrice, TextAlignment.RIGHT));
            itemsTable.addCell(createCell(lineTotal, TextAlignment.RIGHT));
        }

        document.add(itemsTable);
    }


    private void addTotals(Document document, InvoiceDataDto invoiceData) {
        BigDecimal discount = invoiceData.getDiscountTotal() != null ? invoiceData.getDiscountTotal() : BigDecimal.ZERO;
        //BigDecimal tax      = invoiceData.getTaxTotal() != null ? invoiceData.getTaxTotal() : BigDecimal.ZERO;
        BigDecimal ship     = invoiceData.getShippingFee() != null ? invoiceData.getShippingFee() : BigDecimal.ZERO;
        BigDecimal subtotal = invoiceData.getSubtotal() != null ? invoiceData.getSubtotal() : BigDecimal.ZERO;
        BigDecimal total    = invoiceData.getTotal() != null ? invoiceData.getTotal() : BigDecimal.ZERO;

        Table totalsTable = new Table(new float[]{2f, 3f});
        totalsTable.setWidth(UnitValue.createPercentValue(40));      // 40% chiều ngang
        totalsTable.setTextAlignment(TextAlignment.RIGHT);
        totalsTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT);

        addTotalRow(totalsTable, "Tạm tính:", subtotal);

        // luôn hiển thị giảm giá, thuế, phí ship (kể cả 0)
        addTotalRow(totalsTable, "Giảm giá:", discount.negate()); // ví dụ: -10.000 đ
        addTotalRow(totalsTable, "Phí ship:", ship);
        //addTotalRow(totalsTable, "Thuế:", tax);

        Cell labelCell = new Cell()
                .add(new Paragraph("Thành tiền:").setFontSize(12).setBold())
                .setBorder(null)
                .setTextAlignment(TextAlignment.LEFT);

        Cell valueCell = new Cell()
                .add(new Paragraph(formatMoney(total)).setFontSize(12).setBold())
                .setBorder(null)
                .setTextAlignment(TextAlignment.RIGHT);

        totalsTable.addCell(labelCell);
        totalsTable.addCell(valueCell);

        document.add(totalsTable);
    }


    private void addFooter(Document document) {
        Paragraph footer = new Paragraph(
                "Cảm ơn bạn đã tin tưởng và lựa chọn GÓC MÈO LƯỜI!\n" +
                        "Nếu cần hỗ trợ thêm, vui lòng liên hệ: " + shopPhone
        )
                .setFontSize(11)                // tăng size
                .setBold()                      // in đậm toàn bộ, hoặc chỉ dòng đầu nếu muốn tách đoạn
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20)
                .setMarginBottom(10);

        document.add(footer);
    }


    private void addRow(Table table, String label, String value) {
        if (value == null) {
            value = "";
        }
        Cell labelCell = new Cell()
                .add(new Paragraph(label))
                .setBorder(null);

        Cell valueCell = new Cell()
                .add(new Paragraph(value))
                .setBorder(null);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }


    private void addTableHeader(Table table, String header) {
        Cell cell = new Cell()
                .add(new Paragraph(header).setBold())
                .setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY);
        table.addCell(cell);
    }

    private Cell createCell(String text, TextAlignment alignment) {
        if (text == null) {
            text = "";
        }
        return new Cell()
                .add(new Paragraph(text).setFontSize(10))
                .setTextAlignment(alignment);
    }


    private void addTotalRow(Table table, String label, BigDecimal value) {

        if (value == null) {
            value = BigDecimal.ZERO;
        }

        Cell labelCell = new Cell()
                .add(new Paragraph(label).setBold())
                .setBorder(null);
        Cell valueCell = new Cell()
                .add(new Paragraph(formatMoney(value)).setBold())
                .setBorder(null)
                .setTextAlignment(TextAlignment.RIGHT);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private String formatMoney(BigDecimal value) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        nf.setMaximumFractionDigits(0);
        return nf.format(value) + " đ";
    }
}
