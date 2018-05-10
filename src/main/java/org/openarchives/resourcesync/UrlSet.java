/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree
 */
package org.openarchives.resourcesync;

import org.jdom2.Element;

import java.util.List;
/**
 * @author Richard Jones
 */
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
