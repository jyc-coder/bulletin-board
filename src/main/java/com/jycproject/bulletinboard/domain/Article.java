package com.jycproject.bulletinboard.domain;

import io.micrometer.core.lang.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList ="title"),
        @Index(columnList ="hashtag"),
        @Index(columnList ="createdAt"),
        @Index(columnList ="createdBy")
})


@Entity
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql의 increment 방식이 identity이므로 값을 identity로 변경해줘야한다.
    private Long id;



    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @ManyToOne(optional = false) @JoinColumn(name = "userId") private UserAccount userAccount; // 유저 정보 ID
    @Setter @Column(nullable = false,length = 10000) private String content; // 본문

    @Setter private String hashtag; // 해시태그

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy ="article", cascade = CascadeType.ALL )
    @ToString.Exclude
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();



    protected Article() {}

    private Article(UserAccount userAccount,String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount,title,content,hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article) ) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
