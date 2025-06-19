package com.data.entity;

import com.data.ulti.StatusInvoice;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invoices")
@Setter
@Getter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;
    private double total_amount;
    @Enumerated(EnumType.STRING)
    private StatusInvoice status;
}
