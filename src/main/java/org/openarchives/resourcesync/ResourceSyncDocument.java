package org.openarchives.resourcesync;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ResourceSyncDocument
{
    // these options should be provided by the extending class
    protected String capability;
    protected String root;

    // these options can be accessed using getters and setters
    protected Date lastModified;
    protected List<ResourceSyncEntry> entries = new ArrayList<ResourceSyncEntry>();
    protected List<ResourceSyncLn> lns = new ArrayList<ResourceSyncLn>();

    public ResourceSyncLn addLn(String rel, String href)
    {
        // rs:ln elements are repeatable and can have multiple ones with the same rel
        ResourceSyncLn ln = new ResourceSyncLn();
        ln.setRel(rel);
        ln.setHref(href);
        this.lns.add(ln);
        return ln;
    }

    public void addLn(ResourceSyncLn ln)
    {
        this.lns.add(ln);
    }

    public void addEntry(ResourceSyncEntry entry)
    {
        this.entries.add(entry);
    }

    public List<ResourceSyncEntry> getEntries()
    {
        return entries;
    }

    public List<ResourceSyncLn> getLns()
    {
        return lns;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }

    public String getCapability()
    {
        return capability;
    }

    public Element getElement()
    {
        Element root = new Element(this.root, ResourceSync.NS_SITEMAP);
        root.addNamespaceDeclaration(ResourceSync.NS_ATOM);
        root.addNamespaceDeclaration(ResourceSync.NS_RS);

        // set the capability of the document in the rs:md
        Element md = new Element("md", ResourceSync.NS_RS);
        md.setAttribute("capability", this.capability, ResourceSync.NS_RS);
        md.setAttribute("modified", ResourceSync.DATE_FORMAT.format(this.lastModified), ResourceSync.NS_ATOM);
        root.addContent(md);

        // serialise the rs:ln elements
        for (ResourceSyncLn ln : this.lns)
        {
            Element lnEl = new Element("ln", ResourceSync.NS_RS);
            lnEl.setAttribute("rel", ln.getRel(), ResourceSync.NS_ATOM);
            lnEl.setAttribute("href", ln.getHref(), ResourceSync.NS_ATOM);
            root.addContent(lnEl);
        }

        for (ResourceSyncEntry entry : this.entries)
        {
            Element entryElement = entry.getElement();
            root.addContent(entryElement);
        }

        return root;
    }

    public String serialise()
    {
        Element element = this.getElement();
        Document doc = new Document(element);
        XMLOutputter out = new XMLOutputter();
        return out.outputString(doc);
    }
}
