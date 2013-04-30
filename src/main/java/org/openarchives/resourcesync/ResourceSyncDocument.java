package org.openarchives.resourcesync;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class ResourceSyncDocument
{
    // these options should be provided by the extending class
    protected String capability;
    protected String root;

    // these options can be accessed using getters and setters
    protected Date lastModified;
    protected List<ResourceSyncEntry> unorderedEntries = new ArrayList<ResourceSyncEntry>();
    protected TreeMap<Date, List<ResourceSyncEntry>> orderedEntries = new TreeMap<Date, List<ResourceSyncEntry>>();
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
        Date key = entry.getLastModified() == null ? new Date(0) : entry.getLastModified();
        if (!this.orderedEntries.containsKey(key))
        {
            this.orderedEntries.put(key, new ArrayList<ResourceSyncEntry>());
        }
        this.orderedEntries.get(key).add(entry);

        // FIXME: are there any serious concerns about storing the entry in two locations?
        // it's all by-reference, right?
        this.unorderedEntries.add(entry);
    }

    public List<ResourceSyncEntry> getEntries()
    {
        return this.unorderedEntries;
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
        if (this.lastModified != null)
        {
            md.setAttribute("modified", ResourceSync.DATE_FORMAT.format(this.lastModified), ResourceSync.NS_ATOM);
        }
        root.addContent(md);

        // serialise the rs:ln elements
        for (ResourceSyncLn ln : this.lns)
        {
            Element lnEl = new Element("ln", ResourceSync.NS_RS);
            lnEl.setAttribute("rel", ln.getRel(), ResourceSync.NS_ATOM);
            lnEl.setAttribute("href", ln.getHref(), ResourceSync.NS_ATOM);
            root.addContent(lnEl);
        }

        for (Date date : this.orderedEntries.keySet())
        {
            List<ResourceSyncEntry> entries = this.orderedEntries.get(date);
            for (ResourceSyncEntry entry : entries)
            {
                Element entryElement = entry.getElement();
                root.addContent(entryElement);
            }
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

    public void serialise(OutputStream out)
            throws IOException
    {
        Element element = this.getElement();
        Document doc = new Document(element);
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(doc, out);
    }
}
