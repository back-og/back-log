package dev.backlog.post.domain;

import dev.backlog.common.entity.BaseEntity;
import dev.backlog.common.exception.DataLengthExceededException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static dev.backlog.post.exception.PostErrorCode.INVALID_DATA_LENGTH;

@Entity
@Getter
@Table(name = "hashtags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag extends BaseEntity {

    private static final String INVALID_NAME_LENGTH_MESSAGE = "입력된 해시 태그 이름의 길이(%s)가 최대 길이(%s)를 넘겼습니다.";

    private static final int MAX_NAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    public Hashtag(String name) {
        validateNameLength(name);
        this.name = name;
    }

    private void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new DataLengthExceededException(
                    INVALID_DATA_LENGTH,
                    String.format(INVALID_NAME_LENGTH_MESSAGE, name.length(), MAX_NAME_LENGTH));
        }
    }

}
