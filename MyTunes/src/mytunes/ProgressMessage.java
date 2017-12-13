/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes;

import java.util.Objects;
import javafx.application.Preloader;

/**
 *
 * @author janvanzetten
 */
public class ProgressMessage implements Preloader.PreloaderNotification {
    public static final ProgressMessage SUCCESSFULLY_DONE = new ProgressMessage(true, false);
    public static final ProgressMessage FAILED = new ProgressMessage(false, true);

    // TODO: add a variable to hold the exception in case of failure.
    private double progress;
    private String message;
    private boolean done;
    private boolean failed;

    public ProgressMessage(double progress, String message)
    {
        this.progress = progress;
        this.message = message;
        this.done = false;
        this.failed = false;
    }

    private ProgressMessage(boolean done, boolean failed)
    {
        this.done = done;
        this.failed = failed;
    }

    public double getProgress(){return progress;}
    public String getMessage(){return message;}
    public boolean isDone(){return done;}
    public boolean isFailed(){return failed;}

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ProgressMessage message1 = (ProgressMessage) o;
        return Double.compare(message1.progress, progress) == 0 &&
               done == message1.done && failed == message1.failed &&
               Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(progress, message, done, failed);
    }

    @Override
    public String toString()
    {
        return "ProgressMessage{" + "progress=" + progress + ", message='" +
               message + '\'' + ", done=" + done + ", failed=" + failed + '}';
    }
}