package org.openarchives.resourcesync;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResourceSyncLn
{
    protected Map<String, String> hashes = new HashMap<String, String>();
    protected String href = null;
    protected int length = -1;
    protected Date modified = null;
    protected String path = null;
    protected String rel = null;
    protected int pri = -1;
    protected String type = null;

    public void addHash(String type, String hex)
    {
        this.hashes.put(type, hex);
    }

    public void calculateHash(String type, InputStream stream)
    {
        // TODO: could do this when we're a bit further down the development path
    }

    public Map<String, String> getHashes()
    {
        return this.hashes;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        this.href = href;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public Date getModified()
    {
        return modified;
    }

    public void setModified(Date modified)
    {
        this.modified = modified;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getRel()
    {
        return rel;
    }

    public void setRel(String rel)
    {
        this.rel = rel;
    }

    public int getPri()
    {
        return pri;
    }

    public void setPri(int pri)
    {
        this.pri = pri;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
