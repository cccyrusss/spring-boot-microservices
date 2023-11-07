package com.codingtest.orderservice.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="`order`")
public class Order {
    public enum Status { CREATED, ACCEPTED, SENT, REJECTED };

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    private Integer accountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Order o)) {
            return false;
        }
        return Objects.equals(o.getId(), id) && Objects.equals(o.getAccountType(), accountType) && o.getStatus().equals(status);
    }
}
