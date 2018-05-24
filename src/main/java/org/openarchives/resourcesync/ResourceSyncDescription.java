/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree
 */
package org.openarchives.resourcesync;

import java.util.List;
/**
 * @author Richard Jones
 */
public class ResourceSyncDescription extends UrlSet
{
    public ResourceSyncDescription()
    {
        super(ResourceSync.CAPABILITY_RESOURCESYNC);
    }

    public ResourceSyncDescription(String describedby, String describedByContentType)
    {
        this();
        ResourceSyncLn ln = this.addLn(ResourceSync.REL_DESCRIBED_BY, describedby);
        ln.setType(describedByContentType);
    }

    public void addCapabilityList(URL caplist)
    {
        if (!ResourceSync.CAPABILITY_CAPABILITYLIST.equals(caplist.getCapability()))
        {
            throw new SpecComplianceException("URL added to ResourceSyncDescription is not a Capability List");
        }
        this.addUrl(caplist);
    }

    public URL addCapabilityList(String loc)
    {
        return this.addCapabilityList(loc, null);
    }

    public URL addCapabilityList(String loc, String describedby)
    {
        URL caplist = new URL();
        caplist.setLoc(loc);
        if (describedby != null)
        {
            caplist.addLn(ResourceSync.REL_DESCRIBED_BY, describedby);
        }
        caplist.setCapability(ResourceSync.CAPABILITY_CAPABILITYLIST);
        this.addCapabilityList(caplist);
        return caplist;
    }

    public List<ResourceSyncEntry> getCapabilityLists()
    {
        return this.getUrls();
    }
    public void addSourceDescription(URL sourceDesc)
    {
        if (!ResourceSync.CAPABILITY_RESOURCESYNC.equals(sourceDesc.getCapability()))
        {
            throw new SpecComplianceException("URL added to ResourceSyncDescription is not a Capability List");
        }
        this.addUrl(sourceDesc);
    }
    public URL addSourceDescription(String loc)
    {
        return this.addSourceDescription(loc, null);
    }

    public URL addSourceDescription(String loc, String describedby)
    {
        URL sourceDesc = new URL();
        sourceDesc.setLoc(loc);
        if (describedby != null)
        {
        	sourceDesc.addLn(ResourceSync.REL_DESCRIBED_BY, describedby);
        }
        sourceDesc.setCapability(ResourceSync.CAPABILITY_RESOURCESYNC);
        this.addSourceDescription(sourceDesc);
        return sourceDesc;
    }
}
