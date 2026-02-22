package com.kitchen.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import io.r2dbc.postgresql.codec.Json;

@Table("orders")
public record Order(
    @Id Long id,
    @Column("total_price") Double price,
    @Column("order_status") String status,
    @Column("total_protein") Integer protein,
    @Column("total_fiber") Integer fiber,
    @Column("total_calories") Integer calories ,
    @Column("order_metadata") Json metadata
) {}