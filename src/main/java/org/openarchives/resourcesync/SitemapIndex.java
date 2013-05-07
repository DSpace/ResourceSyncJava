package org.openarchives.resourcesync;

import org.jdom2.Element;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

public class SitemapIndex extends ResourceSyncDocument
{
    public SitemapIndex(String capability)
    {
        super("sitemapindex", capability, null);
    }

    public SitemapIndex(String capability, InputStream in)
    {
        super("sitemapindex", capability, in);
    }

    @Override
    protected void populateEntries(Element element)
            throws ParseException
    {
        List<Element> sitemaps = element.getChildren("sitemap", ResourceSync.NS_SITEMAP);
        for (Element sitemap : sitemaps)
        {
            Sitemap sm = new Sitemap();
            sm.populateObject(sitemap);
            this.addSitemap(sm);
        }
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
