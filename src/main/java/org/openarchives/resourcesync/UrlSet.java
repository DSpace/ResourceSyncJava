package org.openarchives.resourcesync;

import java.util.List;

public class UrlSet extends ResourceSyncDocument
{
    public UrlSet()
    {
        this.root = "urlset";
    }

    public void addUrl(URL url)
    {
        this.addEntry(url);
    }

    public List<ResourceSyncEntry> getUrls()
    {
        return this.getEntries();
    }
}
