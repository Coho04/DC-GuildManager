package de.goldendeveloper.guildmanager.errors;

import de.goldendeveloper.mysql.errors.ExceptionHandler;
import io.sentry.Sentry;

public class CustomExceptionHandler extends ExceptionHandler {

    @Override
    public void callException(Exception exception) {
        Sentry.captureException(exception);
        exception.printStackTrace();
    }
}
