package tv.spideo.test.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST controller that will display a welcome text.
 *
 * @author Amrane Ait Zeouay
 * @since 0.0.1
 */
@RestController
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public ResponseEntity<String> displayHelloMessage() {
        return ResponseEntity.ok("Welcome to the challenge, you may see the "
                .concat("<a href=\"https://github.com/Amraneze/spideo-challenge\">README</a> file now."));
    }

}
