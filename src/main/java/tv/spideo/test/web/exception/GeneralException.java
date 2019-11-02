package tv.spideo.test.web.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class GeneralException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public GeneralException() {
        super(null, "general-exception", Status.BAD_REQUEST);
    }

}