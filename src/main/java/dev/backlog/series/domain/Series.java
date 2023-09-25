package dev.backlog.series.domain;

import dev.backlog.common.entity.BaseEntity;
import dev.backlog.common.exception.DataLengthExceededException;
import dev.backlog.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static dev.backlog.series.exception.SeriesErrorCode.INVALID_DATA_LENGTH;


@Entity
@Getter
@Table(name = "series")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Series extends BaseEntity {

    private static final String INVALID_NAME_LENGTH_MESSAGE = "입력된 이름의 길이(%s)가 최대 길이(%s)를 넘겼습니다.";

    private static final int MAX_NAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Builder
    private Series(User user, String name) {
        validateNameLength(name);
        this.user = user;
        this.name = name;
    }

    public void verifySeriesOwner(User user) {
        if (!this.user.equals(user)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }

    public void updateName(String name) {
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
