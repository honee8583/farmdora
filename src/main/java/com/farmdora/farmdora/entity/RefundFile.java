package com.farmdora.farmdora.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefundFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "refund_id")
    private Refund refund;

    @Column(nullable = false)
    private String originFile;

    @Column(nullable = false)
    private String saveFile;
}
