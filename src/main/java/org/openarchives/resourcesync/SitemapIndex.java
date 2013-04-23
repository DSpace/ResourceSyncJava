package org.openarchives.resourcesync;

import java.util.List;

public class SitemapIndex extends ResourceSyncDocument
{
    public SitemapIndex()
    {
        this.root = "sitemapindex";
    }

    public void addSitemap(Sitemap sitemap)
    {
        this.addEntry(sitemap);
    }

    public List<ResourceSyncEntry> getSitemaps()
    {
        return this.getEntries();
    }
}
