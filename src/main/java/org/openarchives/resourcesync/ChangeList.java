package org.openarchives.resourcesync;

import java.util.Date;

public class ChangeList extends UrlSet
{
    public ChangeList()
    {
        this(null, null, null, null);
    }

    public ChangeList(Date from, Date until)
    {
        this(from, until, null, null);
    }

    public ChangeList(String capabilityList)
    {
        this(null, null, capabilityList, null);
    }

    public ChangeList(Date from, Date until, String capabilityList)
    {
        this(from, until, capabilityList, null);
    }

    public ChangeList(Date from, Date until, String capabilityList, String changeListArchive)
    {
        super(ResourceSync.CAPABILITY_CHANGELIST);
        this.setFromUntil(from, until);

        if (capabilityList != null)
        {
            this.addLn(ResourceSync.REL_RESOURCESYNC, capabilityList);
        }
    }

    public void inChangeListArchive(String changeListArchive)
    {
        this.addLn(ResourceSync.REL_UP, changeListArchive);
    }

    public void addChange(URL change)
    {
        if (this.getFrom() == null)
        {
            this.setFrom(change.getLastModified());
        }
        else if (change.getLastModified().getTime() < this.getFrom().getTime())
        {
            this.setFrom(change.getLastModified());
        }

        if (this.getUntil() == null)
        {
            this.setUntil(change.getLastModified());
        }
        else if (change.getLastModified().getTime() > this.getUntil().getTime())
        {
            this.setUntil(change.getLastModified());
        }
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
