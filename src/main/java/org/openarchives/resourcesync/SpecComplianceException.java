package org.openarchives.resourcesync;

public class SpecComplianceException extends RuntimeException
{
    public SpecComplianceException()
    {
        super();
    }

    public SpecComplianceException(String message)
    {
        super(message);
    }

    public SpecComplianceException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SpecComplianceException(Throwable cause)
    {
        super(cause);
    }
}
