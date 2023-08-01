package com.wanted.preonboarding.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@SuperBuilder
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate // ③
    @Column(name="created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate // ④
    @Column(name="modified_date", updatable = false)
    private LocalDateTime modifiedDate;


}
