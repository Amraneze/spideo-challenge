package tv.spideo.test.web.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ResponseWrapper {

    static <T> ResponseEntity<T> wrapResponse(T maybeResponse) {
        return wrapResponse(maybeResponse, (HttpHeaders) null);
    }

    static <X> ResponseEntity<X> wrapResponse(X maybeResponse, HttpHeaders header) {
        return Optional.ofNullable(maybeResponse)
                .map((response) -> ResponseEntity.ok().headers(header).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}