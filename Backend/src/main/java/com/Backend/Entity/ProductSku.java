package com.Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_skus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private boolean outOfStock;

    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    @JoinColumn(name = "product_sku_id")
    private List<Image> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "productSku_variationValue",
            joinColumns = @JoinColumn(name = "product_sku_id"),
            inverseJoinColumns = @JoinColumn(name = "variationValue_id")
    )
    private List<VariationValue> variationValues = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "productSku",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productSku")
    private List<OrderItem> orderItems = new ArrayList<>();

    private boolean enable = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
}
