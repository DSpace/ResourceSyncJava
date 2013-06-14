package org.openarchives.resourcesync;

import java.io.InputStream;
import java.util.Date;

public class ChangeListArchive extends SitemapIndex
{
    public ChangeListArchive()
    {
        this(null, null);
    }

    public ChangeListArchive(Date from, Date until)
    {
        this(from, until, null);
    }

    public ChangeListArchive(String capabilityList)
    {
        this(null, null, capabilityList);
    }

    public ChangeListArchive(Date from, Date until, String capabilityList)
    {
        super(ResourceSync.CAPABILITY_CHANGELIST_ARCHIVE);

        if (from != null)
        {
            this.setFrom(from);
        }
        else
        {
            this.setFrom(new Date());
        }

        if (until != null)
        {
            this.setUntil(until);
        }
        else
        {
            this.setFrom(new Date());
        }

        if (capabilityList != null)
        {
            this.addLn(ResourceSync.REL_RESOURCESYNC, capabilityList);
        }
    }

    public ChangeListArchive(InputStream in)
    {
        super(ResourceSync.CAPABILITY_CHANGELIST_ARCHIVE, in);
    }

    public void addChangeList(Sitemap sitemap)
    {
        if (this.getFrom() == null)
        {
            this.setFrom(sitemap.getLastModified());
        }
        else if (sitemap.getLastModified().getTime() < this.getFrom().getTime())
        {
            this.setFrom(sitemap.getLastModified());
        }

        if (this.getUntil() == null)
        {
            this.setUntil(sitemap.getLastModified());
        }
        else if (sitemap.getLastModified().getTime() > this.getUntil().getTime())
        {
            this.setUntil(sitemap.getLastModified());
        }

        this.addSitemap(sitemap);
    }

    public Sitemap addChangeList(String loc, Date lastMod)
    {
        Sitemap sitemap = new Sitemap();
        sitemap.setLoc(loc);
        sitemap.setLastModified(lastMod);
        this.addChangeList(sitemap);
        return sitemap;
    }
}
