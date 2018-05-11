package org.openarchives.resourcesync;

import org.jdom2.Element;

import java.util.List;

public class UrlSet extends ResourceSyncDocument
{
    public UrlSet(String capability)
    {
        super("urlset", capability, null);
    }

    @Override
    protected void populateEntries(Element element)
    {

    }

    public void addUrl(URL url)
    {
        this.addEntry(url);
    }
    public void addSitemap(Sitemap sitemap )
    {
        this.addEntry(sitemap);
    }
    
    public List<ResourceSyncEntry> getUrls()
    {
        return this.getEntries();
    }
}
