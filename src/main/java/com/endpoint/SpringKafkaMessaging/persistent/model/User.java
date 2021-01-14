package com.endpoint.SpringKafkaMessaging.persistent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    
    @OneToMany(mappedBy = "User", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<Contact> contacts;

}
