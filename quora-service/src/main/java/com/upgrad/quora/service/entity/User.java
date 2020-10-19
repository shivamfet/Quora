package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "users", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "userByUuid", query = "select u from User u where u.uuid = :uuid"),
                @NamedQuery(name = "userByEmail", query = "select u from User u where u.email =:email"),
                @NamedQuery(name = "userByUsername", query = "select u from User u where u.username =:username"),
                @NamedQuery(name = "deleteUserByUuid" , query = "delete from User u where u.uuid = :uuid")
        }
)
public class User implements Serializable{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 64)
    private String uuid;

    @Column(name = "FIRSTNAME")
    @NotNull
    @Size(max = 200)
    private String firstName;

    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 200)
    private String lastName;

    @Column(name = "USERNAME")
    @NotNull
    @Size(max = 200)
    private String username;

    @Column(name = "EMAIL")
    @NotNull
    @Size(max = 200)
    private String email;

    //@ToStringExclude
    @Column(name = "PASSWORD")
    @NotNull
    private String password;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    //@ToStringExclude
    private String salt;

    @Column(name = "COUNTRY")
    @NotNull
    @Size(max = 200)
    private String country;

    @Column(name = "ABOUTME")
    @Size(max = 200)
    private String aboutme;

    @Column(name = "DOB")
    @NotNull
    @Size(max = 200)
    private String dob;

    @Column(name = "ROLE")
    @NotNull
    @Size(max = 30)
    private String role;

    @Column(name = "CONTACTNUMBER")
    @NotNull
    @Size(max = 30)
    private String contactnumber;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


}