package org.openarchives.resourcesync;

import org.jdom2.Element;

import java.io.InputStream;
import java.text.ParseException;
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
    protected String encoding = null;

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

    public String getEncoding()
    {
        return encoding;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
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

    public void populateObject(Element element)
            throws ParseException
    {
        // loc
        Element locEl = element.getChild("loc", ResourceSync.NS_SITEMAP);
        if (locEl != null)
        {
            this.setLoc(locEl.getText().trim());
        }

        // lastmod
        Element lmEl = element.getChild("lastmod", ResourceSync.NS_SITEMAP);
        if (lmEl != null)
        {
            Date lm = ResourceSync.DATE_FORMAT.parse(lmEl.getText().trim());
            this.setLastModified(lm);
        }

        // changefreq
        Element cfEl = element.getChild("changefreq", ResourceSync.NS_SITEMAP);
        if (cfEl != null)
        {
            this.setChangeFreq(cfEl.getText().trim());
        }

        // the metadata element
        Element mdElement = element.getChild("md", ResourceSync.NS_RS);

        if (mdElement != null)
        {
            // - capability
            String capability = mdElement.getAttributeValue("capability");
            if (capability != null && !"".equals(capability))
            {
                this.setCapability(capability);
            }

            // - change
            String change = mdElement.getAttributeValue("change");
            if (change != null && !"".equals(change))
            {
                this.setChange(change);
            }

            // - hash
            String hashAttr = mdElement.getAttributeValue("hash");
            if (hashAttr != null && !"".equals(hashAttr))
            {
                this.addHashesFromAttr(hashAttr);
            }

            // - length
            String length = mdElement.getAttributeValue("length");
            if (length != null && !"".equals(length))
            {
                long l = Long.parseLong(length);
                this.setLength(l);
            }

            // - path
            String path = mdElement.getAttributeValue("path");
            if (path != null && !"".equals(path))
            {
                this.setPath(path);
            }

            // - type
            String type = mdElement.getAttributeValue("type");
            if (type != null && !"".equals(type))
            {
                this.setType(type);
            }

            // -encoding
            String encoding = mdElement.getAttributeValue("encoding");
            if (encoding != null && !"".equals(encoding))
            {
                this.setEncoding(encoding);
            }
        }

        // all the rs:ln elements
        List<Element> lns = element.getChildren("ln", ResourceSync.NS_RS);
        for (Element ln : lns)
        {
            String rel = ln.getAttributeValue("rel");
            String href = ln.getAttributeValue("href");
            if (rel != null && !"".equals(rel) && href != null && !"".equals(href))
            {
                ResourceSyncLn link = this.addLn(rel, href);

                // hash
                String lnHashAttr = ln.getAttributeValue("hash");
                if (lnHashAttr != null && !"".equals(lnHashAttr))
                {
                    Map<String, String> hashMap = this.getHashesFromAttr(lnHashAttr);
                    for (String key : hashMap.keySet())
                    {
                        link.addHash(key, hashMap.get(key));
                    }
                }

                // length
                String lnLength = ln.getAttributeValue("length");
                if (lnLength != null && !"".equals(length))
                {
                    long lnl = Long.parseLong(lnLength);
                    link.setLength(lnl);
                }

                // modified
                String modified = ln.getAttributeValue("modified");
                if (modified != null && !"".equals(modified))
                {
                    Date modDate = ResourceSync.DATE_FORMAT.parse(modified);
                    link.setModified(modDate);
                }

                // path
                String lnPath = ln.getAttributeValue("path");
                if (lnPath != null && !"".equals(lnPath))
                {
                    link.setPath(lnPath);
                }

                // pri
                String pri = ln.getAttributeValue("pri");
                if (pri != null && !"".equals(pri))
                {
                    link.setPri(Integer.parseInt(pri));
                }

                // type
                String lnType = ln.getAttributeValue("type");
                if (lnType != null && !"".equals(lnType))
                {
                    link.setType(lnType);
                }

                // encoding
                String lnEncoding = ln.getAttributeValue("encoding");
                if (lnEncoding != null && !"".equals(lnEncoding))
                {
                    link.setEncoding(lnEncoding);
                }
            }
        }
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
            md.setAttribute("capability", this.capability);
            trip = true;
        }
        if (this.change != null)
        {
            md.setAttribute("change", this.change);
            trip = true;
        }
        String hashAttr = this.getHashAttr(this.hashes);
        if (!"".equals(hashAttr))
        {
            md.setAttribute("hash", hashAttr);
            trip = true;
        }
        if (this.length > -1)
        {
            md.setAttribute("length", Long.toString(this.length));
            trip = true;
        }
        if (this.path != null)
        {
            md.setAttribute("path", this.path);
            trip = true;
        }
        if (this.type != null)
        {
            md.setAttribute("type", this.type);
            trip = true;
        }
        if (this.encoding != null)
        {
            md.setAttribute("encoding", this.encoding);
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
                link.setAttribute("hash", lnHash);
                trip = true;
            }
            if (ln.getHref() != null)
            {
                link.setAttribute("href", ln.getHref());
                trip = true;
            }
            if (ln.getLength() > -1)
            {
                link.setAttribute("length", Long.toString(ln.getLength()));
                trip = true;
            }
            if (ln.getModified() != null)
            {
                link.setAttribute("modified", ResourceSync.DATE_FORMAT.format(ln.getModified()));
                trip = true;
            }
            if (ln.getPath() != null)
            {
                link.setAttribute("path", ln.getPath());
                trip = true;
            }
            if (ln.getRel() != null)
            {
                link.setAttribute("rel", ln.getRel());
                trip = true;
            }
            if (ln.getPri() > 0)
            {
                link.setAttribute("pri", Integer.toString(ln.getPri()));
                trip = true;
            }
            if (ln.getType() != null)
            {
                link.setAttribute("type", ln.getType());
                trip = true;
            }
            if (ln.getEncoding() != null)
            {
                link.setAttribute("encoding", ln.getEncoding());
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

    protected void addHashesFromAttr(String hashAttr)
    {
        Map<String, String> hashMap = this.getHashesFromAttr(hashAttr);
        for (String key : hashMap.keySet())
        {
            this.addHash(key, hashMap.get(key));
        }
    }

    protected Map<String, String> getHashesFromAttr(String hashAttr)
    {
        Map<String, String> map = new HashMap<String, String>();
        String[] bits = hashAttr.split(" ");
        for (String bit : bits)
        {
            String[] parts = bit.split(":");
            if (parts.length == 2)
            {
                map.put(parts[0], parts[1]);
            }
        }
        return map;
    }
}
