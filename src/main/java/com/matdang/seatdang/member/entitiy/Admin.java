package com.matdang.seatdang.member.entitiy;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
//@Setter(AccessLevel.PRIVATE)
@Setter
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
public class Admin extends Member{
}
