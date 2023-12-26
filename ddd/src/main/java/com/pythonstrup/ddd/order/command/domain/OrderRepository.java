package com.pythonstrup.ddd.order.command.domain;

import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, String> {
}
