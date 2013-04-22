package org.openarchives.resourcesync;

import java.util.Date;

public class CapabilityList extends UrlSet
{
    public CapabilityList()
    {
        this(null);
    }

    public CapabilityList(String describedBy)
    {
        super();
        this.capability = "capabilitylist";
        this.lastModified = new Date();

        if (describedBy != null)
        {
            this.addLn(ResourceSync.REL_DESCRIBED_BY, describedBy);
        }
    }

    public URL addUrl(String loc, String capability)
    {
        URL url = new URL();
        url.setLoc(loc);
        url.setCapability(capability);
        this.addEntry(url);
        return url;
    }
}
