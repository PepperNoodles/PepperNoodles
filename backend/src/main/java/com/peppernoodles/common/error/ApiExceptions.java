package com.peppernoodles.common.error;

import org.springframework.http.HttpStatus;

/**
 * The application's exception vocabulary.
 *
 * <p>Services throw these; {@link GlobalExceptionHandler} is the only place that
 * decides how they become HTTP responses. The legacy code returned bare strings
 * and view names from controllers to signal failure, so an error was whatever
 * each controller happened to do that day.
 */
public final class ApiExceptions {

    private ApiExceptions() {}

    /** Base type carrying the status and a stable machine-readable code. */
    public abstract static class ApiException extends RuntimeException {

        private final HttpStatus status;
        private final String code;

        protected ApiException(HttpStatus status, String code, String message) {
            this(status, code, message, null);
        }

        protected ApiException(HttpStatus status, String code, String message, Throwable cause) {
            super(message, cause);
            this.status = status;
            this.code = code;
        }

        public HttpStatus status() {
            return status;
        }

        public String code() {
            return code;
        }
    }

    /** The requested resource does not exist, or the caller may not know that it does. */
    public static class NotFoundException extends ApiException {
        public NotFoundException(String message) {
            super(HttpStatus.NOT_FOUND, "not_found", message);
        }

        public static NotFoundException of(String what, Object id) {
            return new NotFoundException("%s %s was not found".formatted(what, id));
        }
    }

    /** The request is well-formed but conflicts with current state (duplicate, wrong status, …). */
    public static class ConflictException extends ApiException {
        public ConflictException(String message) {
            super(HttpStatus.CONFLICT, "conflict", message);
        }
    }

    /** The caller is authenticated but not allowed to do this. */
    public static class ForbiddenException extends ApiException {
        public ForbiddenException(String message) {
            super(HttpStatus.FORBIDDEN, "forbidden", message);
        }
    }

    /** Credentials, tokens, or verification state are missing or invalid. */
    public static class UnauthorizedException extends ApiException {
        public UnauthorizedException(String message) {
            super(HttpStatus.UNAUTHORIZED, "unauthorized", message);
        }
    }

    /** The request is understood but semantically invalid for a reason bean validation cannot express. */
    public static class ValidationException extends ApiException {
        public ValidationException(String message) {
            super(HttpStatus.UNPROCESSABLE_ENTITY, "validation_failed", message);
        }
    }

    /** A downstream dependency (mail, storage, payment gateway) failed. */
    public static class UpstreamException extends ApiException {
        public UpstreamException(String message, Throwable cause) {
            super(HttpStatus.BAD_GATEWAY, "upstream_failure", message, cause);
        }
    }
}
