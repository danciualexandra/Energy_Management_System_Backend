package ro.tuc.ds2020.entities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name= "user_table")
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
   //@OneToMany(mappedBy = "client", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Device> deviceList=new ArrayList<>();

    @Builder
    public User(UUID id, String username, String password, String type, String firstname, String lastname, LocalDate birthDate, String address, List<Device> deviceList){
        this.id=id;
        this.username=username;
        this.password=password;
        this.type = type;
        this.firstname=firstname;
        this.lastname=lastname;
        this.birthDate=birthDate;
        this.address=address;
        this.deviceList=deviceList;
    }


}
