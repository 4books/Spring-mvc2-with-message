package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage(){
        String hello = ms.getMessage("hello", null, null);
        assertThat(hello).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode() {
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
        //메시지가 없는 경우에는 NoSuchMessageException 이 발생한다.
    }

    @Test
    void notFoundMessageCodeDefaultMessage() {
        String message = ms.getMessage("no_code", null, "기본메세지", null);
        assertThat(message).isEqualTo("기본메세지");
        //메시지가 없어도 기본 메시지( defaultMessage )를 사용하면 기본 메시지가 반환된다.
    }

    @Test
    void argumentMessage(){
        String message = ms.getMessage("hello.name", new Object[]{"Naegwon"}, null);
        assertThat(message).isEqualTo("안녕 Naegwon");
        //다음 메시지의 {0} 부분은 매개변수를 전달해서 치환할 수 있다. Object 배열로 넘겨야 한다.
        //hello.name=안녕 {0} -> Spring 단어를 매개변수로 전달 -> 안녕 Spring
    }

    @Test
    void defaultLang(){
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
        //ms.getMessage("hello", null, null) : locale 정보가 없으므로 messages 를 사용
        //ms.getMessage("hello", null, Locale.KOREA) : locale 정보가 있지만, message_ko 가 없으므로
        //messages 를 사용
    }

    @Test
    void enLang(){
        String hello = ms.getMessage("hello", null, Locale.ENGLISH);
        assertThat(hello).isEqualTo("hello");
        //ms.getMessage("hello", null, Locale.ENGLISH) : locale 정보가 Locale.ENGLISH 이므로
        //messages_en 을 찾아서 사용
    }


}
