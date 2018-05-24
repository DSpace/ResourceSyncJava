/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree
 */
package org.openarchives.resourcesync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Richard Jones
 */
public class CapabilityList extends UrlSet
{
    private List<String> allowedCapabilities = new ArrayList<String>();

    public CapabilityList()
    {
        this(null, null);
    }

    public CapabilityList(String describedBy)
    {
        this(describedBy, null);
    }

    public CapabilityList(String describedBy, Date lastModified)
    {
        super(ResourceSync.CAPABILITY_CAPABILITYLIST);

        this.allowedCapabilities.add(ResourceSync.CAPABILITY_RESOURCELIST);
        this.allowedCapabilities.add(ResourceSync.CAPABILITY_RESOURCEDUMP);
        this.allowedCapabilities.add(ResourceSync.CAPABILITY_CHANGELIST);
        this.allowedCapabilities.add(ResourceSync.CAPABILITY_CHANGEDUMP);
        this.allowedCapabilities.add(ResourceSync.CAPABILITY_CHANGELIST_ARCHIVE);
        this.allowedCapabilities.add(ResourceSync.CAPABILITY_CHANGEDUMP_ARCHIVE);
        this.allowedCapabilities.add(ResourceSync.CAPABILITY_RESOURCEDUMP_ARCHIVE);
        this.allowedCapabilities.add(ResourceSync.CAPABILITY_RESOURCELIST_ARCHIVE);

        if (lastModified != null)
        {
            this.setLastModified(lastModified);
        }

        if (describedBy != null)
        {
            this.addLn(ResourceSync.REL_DESCRIBED_BY, describedBy);
        }
    }

    public void addDescribedBy(String describedBy)
    {
        this.addLn(ResourceSync.REL_DESCRIBED_BY, describedBy);
    }

    public void addCapableUrl(URL url)
            throws SpecComplianceException
    {
        // first check to see if this is an allowed capability
        String capability = url.getCapability();
        if (capability == null || !this.allowedCapabilities.contains(url.getCapability()))
        {
            throw new SpecComplianceException("Attempting to add capability " + url.getCapability() + " to CapabilityList - not permitted.  " +
                    "CapabilityList may only represent resourcelist, resourcedump, changelist, changedump");
        }

        // now determine if a URL already exists with this capability
        List<ResourceSyncEntry> entries = this.getUrls();
        ResourceSyncEntry removable = null;
        for (ResourceSyncEntry entry : entries)
        {
            if (capability.equals(entry.getCapability()))
            {
                removable = entry;
                break; // have to break to avoid concurrent access/modification issues
            }
        }
        if (removable != null)
        {
            entries.remove(removable);
        }

        this.addUrl(url);
    }

    public URL addCapableUrl(String loc, String capability)
            throws SpecComplianceException
    {
        URL url = new URL();
        url.setLoc(loc);
        url.setCapability(capability);
        this.addCapableUrl(url);
        return url;
    }

    public URL setResourceList(String loc)
    {
        try
        {
            return this.addCapableUrl(loc, ResourceSync.CAPABILITY_RESOURCELIST);
        }
        catch (SpecComplianceException e)
        {
            return null;
        }
    }

    public URL setResourceDump(String loc)
    {
        try
        {
            return this.addCapableUrl(loc, ResourceSync.CAPABILITY_RESOURCEDUMP);
        }
        catch (SpecComplianceException e)
        {
            return null;
        }
    }

    public URL setChangeList(String loc)
    {
        try
        {
            return this.addCapableUrl(loc, ResourceSync.CAPABILITY_CHANGELIST);
        }
        catch (SpecComplianceException e)
        {
            return null;
        }
    }

    public URL setChangeDump(String loc)
    {
        try
        {
            return this.addCapableUrl(loc, ResourceSync.CAPABILITY_CHANGEDUMP);
        }
        catch (SpecComplianceException e)
        {
            return null;
        }
    }

    public URL setChangeListArchive(String loc)
    {
        try
        {
            return this.addCapableUrl(loc, ResourceSync.CAPABILITY_CHANGELIST_ARCHIVE);
        }
        catch (SpecComplianceException e)
        {
            return null;
        }
    }
}
