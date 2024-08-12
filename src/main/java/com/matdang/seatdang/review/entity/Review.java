package com.matdang.seatdang.review.entity;


import com.matdang.seatdang.member.entitiy.Member;
import com.matdang.seatdang.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_review")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Review extends ReviewConfig{

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "review_rating")
    private double rate;

    @Column(name = "review_content")
    private String content;

    @Column(name = "review_image")
    private String image;


//    //== 연관 관계 메서드 ==// -> member 어떤 멤버가 리뷰를 썼는지 리뷰 클래스에 맴버 객체를 합체
//    public void setMember(Member member){
//        this.member = member;
//        member.getReviewList().add(this);
//    }
//    // 어떤 스토어에 내 리뷰를 박을건지 설정하는 작업이다.
//    public void setStore(Store store){
//        this.store = store;
//        store.getReviews().add(this);
//    }

    // private 생성자
    private Review(Member member, Store store, String content, String image,double rate) {
        setMember(member);
        setStore(store);
        this.rate = rate;
        this.image = image;
        this.content = content;
    }

    // 리뷰를 생성하는 메서드인데.
    public static Review createReivew(Member member, Store store, String content, String image, double rate){
        requireNonNull(content);
        requireNonNull(image);
        requireNonNull(rate);
        return new Review(member,store,content,image,rate);
    }
}
