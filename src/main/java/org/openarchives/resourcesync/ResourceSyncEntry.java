package org.openarchives.resourcesync;

import org.jdom2.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ResourceSyncEntry
{
    protected String root;

    protected String loc = null;
    protected Date lastModified = null;
    protected String changeFreq = null;

    protected String capability = null;
    protected String change = null;
    protected Map<String, String> hashes = new HashMap<String, String>();
    protected long length = -1;
    protected String path = null;
    protected String type = null;

    protected List<ResourceSyncLn> lns = new ArrayList<ResourceSyncLn>();

    public void setLoc(String url, Date lastModified, String changefreq)
    {
        this.loc = url;
        this.lastModified = lastModified;
        this.changeFreq = changefreq;
    }

    public void setLoc(String url, Date lastModified)
    {
       this.setLoc(url, lastModified, null);
    }

    public void setLoc(String url)
    {
        this.setLoc(url, null, null);
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }

    public void setChangeFreq(String changeFreq)
    {
        this.changeFreq = changeFreq;
    }

    public void setCapability(String capability)
    {
        this.capability = capability;
    }

    public void setChange(String change)
    {
        this.change = change;
    }

    public void setLength(long length)
    {
        this.length = length;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void addHash(String type, String hex)
    {
        this.hashes.put(type, hex);
    }

    public void calculateHash(String type, InputStream stream)
    {
        // TODO: could do this when we're a bit further down the development path
    }

    public ResourceSyncLn addLn(String rel, String href)
    {
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

    public String getLoc()
    {
        return loc;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public String getChangeFreq()
    {
        return changeFreq;
    }

    public String getCapability()
    {
        return capability;
    }

    public String getChange()
    {
        return change;
    }

    public Map<String, String> getHashes()
    {
        return hashes;
    }

    public long getLength()
    {
        return length;
    }

    public String getPath()
    {
        return path;
    }

    public String getType()
    {
        return type;
    }

    public List<ResourceSyncLn> getLns()
    {
        return lns;
    }

    public Element getElement()
    {
        Element root = new Element(this.root, ResourceSync.NS_SITEMAP);

        if (this.loc != null)
        {
            Element loc = new Element("loc", ResourceSync.NS_SITEMAP);
            loc.setText(this.loc);
            root.addContent(loc);
        }

        if (this.lastModified != null)
        {
            Element lm = new Element("lastmod", ResourceSync.NS_SITEMAP);
            lm.setText(ResourceSync.DATE_FORMAT.format(this.lastModified));
            root.addContent(lm);
        }

        if (this.changeFreq != null)
        {
            Element cf = new Element("changefreq", ResourceSync.NS_SITEMAP);
            cf.setText(this.changeFreq);
            root.addContent(cf);
        }

        // set the metadata element
        Element md = new Element("md", ResourceSync.NS_RS);
        boolean trip = false;
        if (this.capability != null)
        {
            md.setAttribute("capability", this.capability, ResourceSync.NS_RS);
            trip = true;
        }
        if (this.change != null)
        {
            md.setAttribute("change", this.change, ResourceSync.NS_RS);
            trip = true;
        }
        String hashAttr = this.getHashAttr(this.hashes);
        if (!"".equals(hashAttr))
        {
            md.setAttribute("hash", hashAttr, ResourceSync.NS_ATOM);
            trip = true;
        }
        if (this.length > -1)
        {
            md.setAttribute("length", Long.toString(this.length), ResourceSync.NS_ATOM);
            trip = true;
        }
        if (this.path != null)
        {
            md.setAttribute("path", this.path, ResourceSync.NS_RS);
            trip = true;
        }
        if (this.type != null)
        {
            md.setAttribute("type", this.type, ResourceSync.NS_ATOM);
            trip = true;
        }
        if (trip)
        {
            root.addContent(md);
        }

        // set the link elements
        for (ResourceSyncLn ln : this.lns)
        {
            trip = false;
            Element link = new Element("ln", ResourceSync.NS_RS);
            String lnHash = this.getHashAttr(ln.getHashes());
            if (!"".equals(lnHash))
            {
                link.setAttribute("hash", lnHash, ResourceSync.NS_ATOM);
                trip = true;
            }
            if (ln.getHref() != null)
            {
                link.setAttribute("href", ln.getHref(), ResourceSync.NS_ATOM);
                trip = true;
            }
            if (ln.getLength() > -1)
            {
                link.setAttribute("length", Integer.toString(ln.getLength()), ResourceSync.NS_ATOM);
                trip = true;
            }
            if (ln.getModified() != null)
            {
                link.setAttribute("modified", ResourceSync.DATE_FORMAT.format(ln.getModified()), ResourceSync.NS_ATOM);
                trip = true;
            }
            if (ln.getPath() != null)
            {
                link.setAttribute("path", ln.getPath(), ResourceSync.NS_RS);
                trip = true;
            }
            if (ln.getRel() != null)
            {
                link.setAttribute("rel", ln.getRel(), ResourceSync.NS_ATOM);
                trip = true;
            }
            if (ln.getPri() > 0)
            {
                link.setAttribute("pri", Integer.toString(ln.getPri())); // FIXME: namespace?
                trip = true;
            }
            if (ln.getType() != null)
            {
                link.setAttribute("type", ln.getType(), ResourceSync.NS_ATOM);
                trip = true;
            }
            if (trip)
            {
                root.addContent(link);
            }
        }

        return root;
    }

    private String getHashAttr(Map<String, String> hashes)
    {
        StringBuilder sb = new StringBuilder();
        for (String type : hashes.keySet())
        {
            String hash = hashes.get(type);
            sb.append(type).append(":").append(hash).append(" ");
        }
        String attr = sb.toString().trim();
        return attr;
    }
}
