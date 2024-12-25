package com.example.test_hellooo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ProductBrandId implements Serializable {
    private static final long serialVersionUID = 2312680940486754199L;
    @NotNull
    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductBrandId entity = (ProductBrandId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.brandId, entity.brandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, brandId);
    }

}