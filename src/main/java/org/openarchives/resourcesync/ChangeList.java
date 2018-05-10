/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree
 */
package org.openarchives.resourcesync;

import java.util.Date;
/**
 * @author Richard Jones
 */
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
            this.addLn(ResourceSync.REL_UP, capabilityList);
        }
    }

    public void inChangeListArchive(String changeListArchive)
    {
        // FIXME: this currently doesn't do anything .. we need to resolve
        // how to link from a change list to a changelist archive
        //
        // this code would link from a changelist to a changelistindex, which is
        // different ...
        //
        // this.addLn(ResourceSync.REL_UP, changeListArchive);
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
