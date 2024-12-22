package com.miatts.backend.common.base;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity <T> {



    @Id
    @GeneratedValue
    private T id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Public facing id is needed for all entities")
    private String publicId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;


    @CreatedBy
    @Column(nullable = true, updatable = false)
    private Integer createdBy;

//    @Column @LastModifiedDate
//    private LocalDateTime updatedAt;
//
//    @Column @LastModifiedBy
//    private String updatedBy;

    @PrePersist
        /* default */ void onCreate() {
        if (Objects.isNull(getPublicId())) {
            setPublicId(UUID.randomUUID().toString());
        }
    }

}
