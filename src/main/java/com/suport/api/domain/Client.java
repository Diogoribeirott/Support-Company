package com.suport.api.domain;

import com.suport.api.enums.ClientType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@SuperBuilder
@Entity
@NoArgsConstructor
@Table(name = "Client_TB")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String email;
    private String taxId;
    private String phone;
    
    @CreationTimestamp
    private LocalDateTime  CreateAt;
    
    @UpdateTimestamp
    private LocalDateTime  updateAt;
    
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;  

    @Enumerated(EnumType.STRING)
    private ClientType type;
}
