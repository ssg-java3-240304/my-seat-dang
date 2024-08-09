package com.matdang.seatdang.member.entitiy;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
//@Setter
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
@SuperBuilder(toBuilder = true)
//@Builder
public class Admin extends Member{
}
