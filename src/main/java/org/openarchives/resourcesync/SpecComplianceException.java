/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree
 */
package org.openarchives.resourcesync;
/**
 * @author Richard Jones
 */
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
