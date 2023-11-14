package io.rachidassouani.booktracker.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Table("user")
public class User {
    @Id
    @PrimaryKeyColumn(name = "email", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String email;

    @Column("password")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String password;

    @Column("role_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String roleName;

    @Column("first_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String firstName;

    @Column("last_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String lastName;

    @Column("enabled")
    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private boolean enabled;

    public User() {
    }

    public User(String email, String password, String roleName, String firstName, String lastName, boolean enabled) {
        this.email = email;
        this.password = password;
        this.roleName = roleName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
    }

    // Getters & Setters

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
