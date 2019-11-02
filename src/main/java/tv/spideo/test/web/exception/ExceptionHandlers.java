package tv.spideo.test.web.exception;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblem;

@ControllerAdvice
public class ExceptionHandlers implements ProblemHandling {

    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null || entity.getBody() == null) {
            return entity;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
                .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.getDefaultType() : problem.getType())
                .withStatus(problem.getStatus())
                .withTitle(problem.getTitle())
                .with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI());
        builder
                .withCause(((DefaultProblem) problem).getCause())
                .withDetail(problem.getDetail())
                .withInstance(problem.getInstance());
        problem.getParameters().forEach(builder::with);
        if (!problem.getParameters().containsKey("message") && problem.getStatus() != null) {
            builder.with("message", "error.http." + problem.getStatus().getStatusCode());
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }
}