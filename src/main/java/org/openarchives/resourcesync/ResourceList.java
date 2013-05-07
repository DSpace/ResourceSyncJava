package org.openarchives.resourcesync;

import java.util.Date;

public class ResourceList extends UrlSet
{
    public ResourceList()
    {
        this(null, null);
    }

    public ResourceList(Date lastModified)
    {
        this(lastModified, null);
    }

    public ResourceList(Date lastMod, String capabilityList)
    {
        super(ResourceSync.CAPABILITY_RESOURCELIST);

        if (lastMod == null)
        {
            this.lastModified = new Date();
        }
        else
        {
            this.lastModified = lastMod;
        }

        if (capabilityList != null)
        {
            this.addLn(ResourceSync.REL_RESOURCESYNC, capabilityList);
        }
    }

    public ResourceList(String capabilityList)
    {
        this(null, capabilityList);
    }

    public void addResource(URL resource)
    {
        this.addEntry(resource);
    }

    public URL addResource(String loc)
    {
        return this.addResource(loc, null, null);
    }

    public URL addResource(String loc, Date lastMod)
    {
        return this.addResource(loc, lastMod, null);
    }

    public URL addResource(String loc, Date lastMod, String changeFreq)
    {
        URL resource = new URL();
        resource.setLoc(loc);
        resource.setLastModified(lastMod);
        resource.setChangeFreq(changeFreq);
        this.addResource(resource);
        return resource;
    }
}
