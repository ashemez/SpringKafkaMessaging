package com.endpoint.SpringKafkaMessaging.persistent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column
    private String fname;

    @Column
    private String lname;

    @Column
    private String mobile;
    
    @Column(name="created_at")
    private Date createdAt;

    @ManyToMany
    @JoinTable(
            name = "contacts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id"))
    private List<Contact> contacts;
}
