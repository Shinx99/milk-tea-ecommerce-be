# Explanation of Relationships in the Sample DB System

This system describes a database architecture for an e-commerce application, including main tables: `User`, `Customer`, `Product`, `Category`, `Order`, `Payment`, `Voucher` and `Cart`. These tables are linked through relationships to ensure data consistency and facilitate business operations.

![erd.png](../../images/erd.png)

## 1. Overview of Main Tables

- **User**: Stores user information (id, email, password, role, created_at, updated_at, is_active).
- **Product**: Manages products (id, name, price, quantity, description, status, sort_order, slug, image_url, created_at, updated_at, is_active).
- **Category**: Manages product categories (id, parent_id, slug, created_at, updated_at, name, sort_order, is_active).
- **Order**: User orders (id, place_at, confirmed_at, completed_at, cancelled_at, status, description, total, subtotal, discount_total, tax_total, shipping_fee).
- **Payment**: Payment transactions (id, provider, paid_at, refunded_at, created_at, amount, status, transaction_ref, payload).
- **Cart**: User shopping carts (id, status, create_at, session_key, expire_at).
- **Customer**: Stores customer information (id, password, email, phone number, name, address, updated_at, created_at, is_active).
- **Voucher**: Manages vouchers (id, code, updated_at, expired_at, number, start_at, created_at, discount_type, is_active).

## 2. Relationships in the Schema

### Customer - Order (Customer - Order)
- **Relationship**: One customer can have multiple orders (1-N).
- **Explanation**: When a customer places an order, each order is linked to a specific customer.

### Order - Payment (Order - Payment)
- **Relationship**: One order can be linked to one or more payments (1-N).
- **Explanation**: Each order includes payment details (success, refund, etc.) with related fields.

### Product - Category (Product - Category)
- **Relationship**: One product belongs to one category; one category contains multiple products (N-1).
- **Explanation**: Each product belongs to one category and one category can contain multiple values.

### Order - Product (Order - Product)
- **Relationship**: One order contains multiple products; one product can appear in multiple orders (N-N).
- **Explanation**: Linked via a junction table (Order_Items), which holds details like quantity and price per order.

### Product - Cart (Product - Cart)
- **Relationship**: One cart can contain multiple products; one product can be in multiple carts (N-N).
- **Explanation**: Linked via a junction table (Cart_Items) for managing selected products before purchase.

### Customer - Cart (Customer - Cart)
- **Relationship**: One cart can contain only one product; one product can be in just one carts (1-1).
- **Explanation**: Each customer just have only one cart, cart also belongs to just one customer.

### Category - Category (Parent-Child Categories)
- **Relationship**: One category can be the parent of multiple child categories (1-N).
- **Explanation**: Creates a tree structure for hierarchical product categories.

### Voucher - Customer (Voucher - Customer)
- **Relationship**: One voucher can be assigned to multiple customers; on the other side, one customer can have multiple vouchers (N-N).
- **Explanation**: Linked via a junction table for managing selected products before purchase.

### Voucher - Product (Voucher - Product)
- **Relationship**: One voucher can be assigned to multiple products; on the other side, one product can have multiple vouchers (N-N).
- **Explanation**: Linked via a junction table for managing selected products before purchase.

## 3. Explanation of Related Fields

- Fields like `created_at`, `updated_at`, and `is_active` are used for data status control.
- Junction tables handle N-N relationships, storing details such as product quantities, prices, etc., in orders or carts.
- Key fields: `price`, `quantity`, `discount_price`, and `status` manage product/order status, pricing, inventory, and promotions.
- Entity `User` doesn't connect to any entities because we want to secure the information of this user actors (admin, staff, manager, etc.); Also, by separating `user`, we can optimize the time complexity of the database query.

## 4. Summary

These relationships enable smooth system operation for e-commerce tasks like user management, product handling, categorization, ordering, and payments. Separating tables and using junction tables allows for easy scalability and efficient data control.
