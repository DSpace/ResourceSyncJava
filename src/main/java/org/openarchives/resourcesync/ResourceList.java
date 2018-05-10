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
public class ResourceList extends UrlSet
{
    public ResourceList()
    {
        this(null, null, false);
    }

    public ResourceList(Date lastModified)
    {
        this(lastModified, null, false);
    }

    public ResourceList(String capabilityList)
    {
        this(null, capabilityList, false);
    }

    public ResourceList(Date lastMod, String capabilityList)
    {
        this(lastMod, capabilityList, false);
    }

    public ResourceList(boolean dump)
    {
        this(null, null, dump);
    }

    public ResourceList(Date lastModified, boolean dump)
    {
        this(lastModified, null, dump);
    }

    public ResourceList(String capabilityList, boolean dump)
    {
        this(null, capabilityList, dump);
    }

    public ResourceList(Date lastMod, String capabilityList, boolean dump)
    {
        super(dump ? ResourceSync.CAPABILITY_RESOURCEDUMP_MANIFEST : ResourceSync.CAPABILITY_RESOURCELIST);

        if (lastMod == null)
        {
            this.setFrom(new Date());
        }
        else
        {
            this.setFrom(lastMod);
        }

        if (capabilityList != null)
        {
            this.addLn(ResourceSync.REL_UP, capabilityList);
        }
    }
    public ResourceList(Date lastMod, String capabilityList, boolean dump,boolean changeDump)
    {
        super(changeDump ? ResourceSync.CAPABILITY_CHANGEDUMP_MANIFEST : ResourceSync.CAPABILITY_RESOURCELIST);

        if (lastMod == null)
        {
            this.setFrom(new Date());
        }
        else
        {
            this.setFrom(lastMod);
        }

        if (capabilityList != null)
        {
            this.addLn(ResourceSync.REL_UP, capabilityList);
        }
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
