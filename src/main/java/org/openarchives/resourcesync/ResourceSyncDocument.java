package org.openarchives.resourcesync;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class ResourceSyncDocument
{
    // these options should be provided by the extending class through the constructor overrides
    protected String capability;
    protected String root;

    // these options can be accessed using getters and setters
    protected Date lastModified;
    protected List<ResourceSyncEntry> unorderedEntries = new ArrayList<ResourceSyncEntry>();
    protected TreeMap<Date, List<ResourceSyncEntry>> orderedEntries = new TreeMap<Date, List<ResourceSyncEntry>>();
    protected List<ResourceSyncLn> lns = new ArrayList<ResourceSyncLn>();

    public ResourceSyncDocument(String root, String capability, InputStream in)
    {
        this.root = root;
        this.capability = capability;

        try
        {
            if (in != null)
            {
                Element element = this.parse(in);
                this.populateDocument(element);
            }
        }
        catch (IOException e)
        {
            // do nothing, at least for the time being
        }
        catch (JDOMException e)
        {
            // do nothing, at least for the time being
        }
        catch (ParseException e)
        {
            // do nothing, at least for the time being
        }
    }

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

    protected void populateDocument(Element element)
            throws ParseException
    {
        // metadata element
        Element mdElement = element.getChild("md", ResourceSync.NS_RS);

        if (mdElement != null)
        {
            // - capability
            String capability = mdElement.getAttributeValue("capability");
            if (!"".equals(capability))
            {
                this.capability = capability;
            }

            // - modified
            String modified = mdElement.getAttributeValue("modified");
            if (modified != null && !"".equals(modified))
            {
                Date lastMod = ResourceSync.DATE_FORMAT.parse(modified);
                this.setLastModified(lastMod);
            }
        }

        // rs:ln elements
        List<Element> lns = element.getChildren("ln", ResourceSync.NS_RS);
        for (Element ln : lns)
        {
            String rel = ln.getAttributeValue("rel");
            String href = ln.getAttributeValue("href");
            if (rel != null && !"".equals(rel) && href != null && !"".equals(href))
            {
                this.addLn(rel, href);
            }
        }

        // each of the entries
        this.populateEntries(element);
    }

    protected abstract void populateEntries(Element element) throws ParseException;

    public Element getElement()
    {
        Element root = new Element(this.root, ResourceSync.NS_SITEMAP);
        root.addNamespaceDeclaration(ResourceSync.NS_RS);

        // set the capability of the document in the rs:md
        Element md = new Element("md", ResourceSync.NS_RS);
        md.setAttribute("capability", this.capability);
        if (this.lastModified != null)
        {
            md.setAttribute("modified", ResourceSync.DATE_FORMAT.format(this.lastModified));
        }
        root.addContent(md);

        // serialise the rs:ln elements
        for (ResourceSyncLn ln : this.lns)
        {
            Element lnEl = new Element("ln", ResourceSync.NS_RS);
            lnEl.setAttribute("rel", ln.getRel());
            lnEl.setAttribute("href", ln.getHref());
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
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        return out.outputString(doc);
    }

    public void serialise(OutputStream out)
            throws IOException
    {
        Element element = this.getElement();
        Document doc = new Document(element);
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(doc, out);
    }

    protected Element parse(InputStream in)
            throws IOException, JDOMException
    {
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(in);
        Element element = doc.getRootElement();
        element.detach();
        return element;
    }
}
