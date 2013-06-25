package org.openarchives.resourcesync;

import com.sun.org.apache.xpath.internal.functions.FuncStringLength;

import java.util.Date;

public class ResourceDump extends UrlSet
{

    public ResourceDump(Date from, String resourceSync)
    {
        super(ResourceSync.CAPABILITY_RESOURCEDUMP);
        this.setFrom(from);
        if (resourceSync != null)
        {
            this.addLn(ResourceSync.CAPABILITY_RESOURCESYNC, resourceSync);
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
}
