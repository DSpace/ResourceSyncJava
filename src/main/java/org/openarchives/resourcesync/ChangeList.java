package org.openarchives.resourcesync;

import java.util.Date;

public class ChangeList extends UrlSet
{
    public ChangeList(Date lastMod)
    {
        this(lastMod, null);
    }

    public ChangeList(String capabilityList)
    {
        this(null, capabilityList);
    }

    public ChangeList(Date lastMod, String capabilityList)
    {
        super(ResourceSync.CAPABILITY_CHANGELIST);
        this.setLastModified(lastMod);

        if (capabilityList != null)
        {
            this.addLn(ResourceSync.REL_RESOURCESYNC, capabilityList);
        }
    }

    public void addChange(URL change)
    {
        this.addUrl(change);
    }

    public URL addChange(String loc, Date lastMod, String change)
    {
        URL url = new URL();
        url.setLoc(loc);
        url.setLastModified(lastMod);
        url.setChange(change);
        this.addChange(url);
        return url;
    }
}
