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
public class ResourceDump extends UrlSet
{

    public ResourceDump(Date from, String resourceSync)
    {
        super(ResourceSync.CAPABILITY_RESOURCEDUMP);
        this.setFrom(from);
        if (resourceSync != null)
        {
            this.addLn(ResourceSync.REL_UP, resourceSync);
        }
    }

    public ResourceDump()
    {
        this(new Date(), null);
    }

    public void addResourceZip(URL zip)
    {
        this.addUrl(zip);
    }

    public URL addResourceZip(String zipUrl, Date lastMod, String type, long length)
    {
        URL url = new URL();
        url.setLoc(zipUrl);
        url.setLastModified(lastMod);
        url.setType(type);
        url.setLength(length);
        this.addResourceZip(url);
        return url;
    }
    public URL addResourceZip(String zipUrl, Date lastMod, String type)
    {
        URL url = new URL();
        url.setLoc(zipUrl);
        url.setLastModified(lastMod);
        url.setType(type);
        this.addResourceZip(url);
        return url;
    }
}
