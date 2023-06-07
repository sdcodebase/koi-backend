package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.global.error.exception.DomainLogicException;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EmailTest {

    static Stream<Arguments> invalidLength() {
        return Stream.of(
                Arguments.of("A".repeat(4)),
                Arguments.of("A".repeat(81)),
                Arguments.of(""),
                Arguments.of(" ".repeat(5))
        );
    }

    @DisplayName("입력값의 길이가 5 ~ 80 밖이거나 입력값이 Null이나 공백으로만 이루어진 문자열인 경우 실패")
    @ParameterizedTest(name = "{0}로 이메일 생성시 예외가 발생한다.")
    @NullSource
    @MethodSource("invalidLength")
    void createLengthFail(String email) {
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorInfo")
                .isEqualTo(ErrorInfo.USER_EMAIL_ERROR);
    }

    @DisplayName("이메일 형식에 올바르지 않는 경우 실패")
    @ParameterizedTest(name = "{0}로 이메일 생성시 예외가 발생한다.")
    @CsvSource({"@abc.abc", "aaa@abc.d", "aaa@naver", "naver.co.kr", "aaa@aaa..abc"})
    void createWrongFormatFail(String email) {
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorInfo")
                .isEqualTo(ErrorInfo.USER_EMAIL_ERROR);
    }

    @Test
    @DisplayName("입력값의 앞뒤 공백 문자를 제거")
    void trimSuccess() {
        String BEFORE_TRIM = "      abcd@gmail.com    ";
        Email email = new Email(BEFORE_TRIM);
        assertThat(email.getValue()).isEqualTo(BEFORE_TRIM.trim());
    }

    @Test
    @DisplayName("5 ~ 80자의 이메일 형식에 맞는 문자열일 경우 성공")
    void createSuccess() {
        Email email = new Email("abcde@gmail.com");
        assertThat(email.getValue()).isEqualTo("abcde@gmail.com");
    }

}