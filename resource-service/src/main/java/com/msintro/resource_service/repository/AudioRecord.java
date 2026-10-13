package com.msintro.resource_service.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import static org.hibernate.type.SqlTypes.LONGVARBINARY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String s3key;
}
