package com.wanted.preonboarding.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Table(indexes = {
        @Index(name = "idx__username", columnList = "username", unique = true)
})
@NoArgsConstructor
@SuperBuilder
@Getter
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;


    //id가 null일 때도 체킹할 수 있도록. (비영속 상태에서의 체킹)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (!Objects.equals(password, member.password)) return false;
        return Objects.equals(username, member.username);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
