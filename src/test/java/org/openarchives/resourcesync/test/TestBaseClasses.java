package org.openarchives.resourcesync.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jdom2.Element;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openarchives.resourcesync.ResourceSync;
import org.openarchives.resourcesync.ResourceSyncDocument;
import org.openarchives.resourcesync.ResourceSyncEntry;
import org.openarchives.resourcesync.ResourceSyncLn;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class TestBaseClasses
{
    @Test
    public void resourceSyncLn()
    {
        Date now = new Date();

        ResourceSyncLn ln = new ResourceSyncLn();

        ln.addHash(ResourceSync.HASH_MD5, "abcdefg");
        ln.addHash(ResourceSync.HASH_SHA_256, "123456");
        ln.setHref("http://it.is.a/url");
        ln.setRel(ResourceSync.REL_DESCRIBES);
        ln.setLength(234);
        ln.setModified(now);
        ln.setPath("/path/to/file");
        ln.setPri(45);
        ln.setType("application/pdf");

        assert ln.getHref().equals("http://it.is.a/url");
        assert ln.getRel().equals(ResourceSync.REL_DESCRIBES);
        assert ln.getLength() == 234;
        assert ln.getModified().equals(now);
        assert ln.getPath().equals("/path/to/file");
        assert ln.getPri() == 45;
        assert ln.getType().equals("application/pdf");

        Map<String, String> hashes = ln.getHashes();
        boolean seenMd5 = false;
        boolean seenSha = false;
        for (String type : hashes.keySet())
        {
            assert type.equals(ResourceSync.HASH_SHA_256) || type.equals(ResourceSync.HASH_MD5);
            if (type.equals(ResourceSync.HASH_MD5))
            {
                assert hashes.get(type).equals("abcdefg");
                seenMd5 = true;
            }
            if (type.equals(ResourceSync.HASH_SHA_256))
            {
                assert hashes.get(type).equals("123456");
                seenSha = true;
            }
        }
        assert seenMd5;
        assert seenSha;

    }

    @Test
    public void resourceSyncEntry()
    {
        Date now = new Date();
        String nowStr = ResourceSync.DATE_FORMAT.format(now);

        ResourceSyncEntry entry = new TestResourceSyncEntry();

        entry.setLoc("http://loc.com/example");
        entry.setLastModified(now);
        entry.setChangeFreq(ResourceSync.FREQ_ALWAYS);

        entry.setCapability(ResourceSync.CAPABILITY_RESOURCELIST);
        entry.setChange(ResourceSync.CHANGE_CREATED);
        entry.addHash(ResourceSync.HASH_MD5, "123456789");
        entry.addHash(ResourceSync.HASH_SHA_256, "abcdefg");
        entry.setLength(987);
        entry.setPath("/path/to/file");
        entry.setType("application/pdf");

        ResourceSyncLn ln1 = entry.addLn(ResourceSync.REL_DESCRIBES, "http://describes");
        ResourceSyncLn ln2 = entry.addLn(ResourceSync.REL_COLLECTION, "http://collection");
        ResourceSyncLn ln3 = entry.addLn(ResourceSync.REL_DESCRIBED_BY, "http://describedby");
        ResourceSyncLn ln4 = new ResourceSyncLn();
        ln4.setRel(ResourceSync.REL_COLLECTION);
        ln4.setHref("http://other.collection/");
        entry.addLn(ln4);

        ln1.setType("text/html");
        ln1.setLength(543);

        assert entry.getLoc().equals("http://loc.com/example");
        assert entry.getLastModified().equals(now);
        assert entry.getChangeFreq().equals(ResourceSync.FREQ_ALWAYS);
        assert entry.getCapability().equals(ResourceSync.CAPABILITY_RESOURCELIST);
        assert entry.getChange().equals(ResourceSync.CHANGE_CREATED);
        assert entry.getLength() == 987;
        assert entry.getPath().equals("/path/to/file");
        assert entry.getType().equals("application/pdf");

        Map<String, String> hashes = entry.getHashes();
        boolean seenMd5 = false;
        boolean seenSha = false;
        for (String type : hashes.keySet())
        {
            assert type.equals(ResourceSync.HASH_SHA_256) || type.equals(ResourceSync.HASH_MD5);
            if (type.equals(ResourceSync.HASH_MD5))
            {
                assert hashes.get(type).equals("123456789");
                seenMd5 = true;
            }
            if (type.equals(ResourceSync.HASH_SHA_256))
            {
                assert hashes.get(type).equals("abcdefg");
                seenSha = true;
            }
        }
        assert seenMd5;
        assert seenSha;

        List<ResourceSyncLn> links = entry.getLns();
        assert links.size() == 4;
        boolean describes = false;
        boolean other_collection = false;
        boolean collection = false;
        for (ResourceSyncLn link : links)
        {
            if (link.getHref().equals("http://describes"))
            {
                assert link.getRel().equals(ResourceSync.REL_DESCRIBES);
                assert link.getType().equals("text/html");
                assert link.getLength() == 543;
                describes = true;
            }
            if (link.getHref().equals("http://other.collection/"))
            {
                assert link.getRel().equals(ResourceSync.REL_COLLECTION);
                other_collection = true;
            }
            if (link.getHref().equals("http://collection"))
            {
                collection = true;
            }
        }
        assert describes;
        assert other_collection;
        assert collection;

        Element element = entry.getElement();
        assert element.getName().equals("url");

        Element locEl = element.getChild("loc", ResourceSync.NS_SITEMAP);
        assert locEl.getText().equals("http://loc.com/example");

        Element lmEl = element.getChild("lastmod", ResourceSync.NS_SITEMAP);
        assert lmEl.getText().equals(nowStr);

        Element cfEl = element.getChild("changefreq", ResourceSync.NS_SITEMAP);
        assert cfEl.getText().equals(ResourceSync.FREQ_ALWAYS);

        Element md = element.getChild("md", ResourceSync.NS_RS);

        String cap = md.getAttributeValue("capability", ResourceSync.NS_RS);
        assert cap.equals(ResourceSync.CAPABILITY_RESOURCELIST);

        String change = md.getAttributeValue("change", ResourceSync.NS_RS);
        assert change.equals(ResourceSync.CHANGE_CREATED);

        String length = md.getAttributeValue("length", ResourceSync.NS_ATOM);
        assert length.equals("987");

        String path = md.getAttributeValue("path", ResourceSync.NS_RS);
        assert path.equals("/path/to/file");

        String type = md.getAttributeValue("type", ResourceSync.NS_ATOM);
        assert type.equals("application/pdf");

        String hashAttr = md.getAttributeValue("hash", ResourceSync.NS_ATOM);
        String[] hashParts = hashAttr.split(" ");
        assert hashParts.length == 2;
        seenMd5 = false;
        seenSha = false;
        for (String part : hashParts)
        {
            if (part.startsWith(ResourceSync.HASH_MD5))
            {
                assert part.equals("md5:123456789");
                seenMd5 = true;
            }
            if (part.startsWith(ResourceSync.HASH_SHA_256))
            {
                assert part.equals("sha-256:abcdefg");
                seenSha = true;
            }
        }
        assert seenMd5;
        assert seenSha;

        List<Element> linkEls = element.getChildren("ln", ResourceSync.NS_RS);
        assert linkEls.size() == 4;
        describes = false;
        other_collection = false;
        collection = false;
        for (Element linkEl : linkEls)
        {
            if (linkEl.getAttributeValue("href", ResourceSync.NS_ATOM).equals("http://describes"))
            {
                assert linkEl.getAttributeValue("rel", ResourceSync.NS_ATOM).equals(ResourceSync.REL_DESCRIBES);
                assert linkEl.getAttributeValue("type", ResourceSync.NS_ATOM).equals("text/html");
                assert linkEl.getAttributeValue("length", ResourceSync.NS_ATOM).equals("543");
                describes = true;
            }
            if (linkEl.getAttributeValue("href", ResourceSync.NS_ATOM).equals("http://other.collection/"))
            {
                assert linkEl.getAttributeValue("rel", ResourceSync.NS_ATOM).equals(ResourceSync.REL_COLLECTION);
                other_collection = true;
            }
            if (linkEl.getAttributeValue("href", ResourceSync.NS_ATOM).equals("http://collection"))
            {
                collection = true;
            }
        }
        assert describes;
        assert other_collection;
        assert collection;
    }

    @Test
    public void resourceSyncDocument()
    {
        Date now = new Date();
        String nowStr = ResourceSync.DATE_FORMAT.format(now);

        ResourceSyncDocument doc = new TestResourceSyncDocument();
        doc.setLastModified(now);

        ResourceSyncEntry entry1 = new TestResourceSyncEntry();
        entry1.setLoc("http://entry1");
        entry1.setType("text/xml");

        ResourceSyncEntry entry2 = new TestResourceSyncEntry();
        entry2.setLoc("http://entry2");
        entry2.setChange(ResourceSync.CHANGE_UPDATED);

        doc.addEntry(entry1);
        doc.addEntry(entry2);

        doc.addLn(ResourceSync.REL_DESCRIBED_BY, "http://describedby");
        ResourceSyncLn ln = new ResourceSyncLn();
        ln.setRel(ResourceSync.REL_DESCRIBES);
        ln.setHref("http://describes");
        ln.setLength(234);
        doc.addLn(ln);

        assert doc.getCapability().equals(ResourceSync.CAPABILITY_CHANGEDUMP);
        assert doc.getLastModified().equals(now);

        List<ResourceSyncEntry> entries = doc.getEntries();
        assert entries.size() == 2;
        boolean saw1 = false;
        boolean saw2 = false;
        for (ResourceSyncEntry entry : entries)
        {
            if (entry.getLoc().equals("http://entry1"))
            {
                assert entry.getType().equals("text/xml");
                saw1 = true;
            }
            if (entry.getLoc().equals("http://entry2"))
            {
                assert entry.getChange().equals(ResourceSync.CHANGE_UPDATED);
                saw2 = true;
            }
        }
        assert saw1;
        assert saw2;

        List<ResourceSyncLn> links = doc.getLns();
        assert links.size() == 2;
        boolean descby = false;
        boolean desc = false;
        for (ResourceSyncLn link : links)
        {
            if (link.getRel().equals(ResourceSync.REL_DESCRIBED_BY))
            {
                assert link.getHref().equals("http://describedby");
                descby = true;
            }
            if (link.getRel().equals(ResourceSync.REL_DESCRIBES))
            {
                assert link.getHref().equals("http://describes");
                desc = true;
            }
        }
        assert descby;
        assert desc;

        Element element = doc.getElement();
        assert element.getName().equals("urlset");

        Element md = element.getChild("md", ResourceSync.NS_RS);

        String cap = md.getAttributeValue("capability", ResourceSync.NS_RS);
        assert cap.equals(ResourceSync.CAPABILITY_CHANGEDUMP);

        String mod = md.getAttributeValue("modified", ResourceSync.NS_ATOM);
        assert mod.equals(nowStr);

        List<Element> linkEls = element.getChildren("ln", ResourceSync.NS_RS);
        assert linkEls.size() == 2;
        descby = false;
        desc = false;
        for (Element link : linkEls)
        {
            if (link.getAttributeValue("rel", ResourceSync.NS_ATOM).equals(ResourceSync.REL_DESCRIBED_BY))
            {
                assert link.getAttributeValue("href", ResourceSync.NS_ATOM).equals("http://describedby");
                descby = true;
            }
            if (link.getAttributeValue("rel", ResourceSync.NS_ATOM).equals(ResourceSync.REL_DESCRIBES))
            {
                assert link.getAttributeValue("href", ResourceSync.NS_ATOM).equals("http://describes");
                desc = true;
            }
        }
        assert descby;
        assert desc;

        List<Element> entryEls = element.getChildren("url", ResourceSync.NS_SITEMAP);
        assert entryEls.size() == 2;
        saw1 = false;
        saw2 = false;
        for (Element entry : entryEls)
        {
            if (entry.getChild("loc", ResourceSync.NS_SITEMAP).getText().equals("http://entry1"))
            {
                Element m = entry.getChild("md", ResourceSync.NS_RS);
                assert m.getAttributeValue("type", ResourceSync.NS_ATOM).equals("text/xml");
                saw1 = true;
            }
            if (entry.getChild("loc", ResourceSync.NS_SITEMAP).getText().equals("http://entry2"))
            {
                Element m = entry.getChild("md", ResourceSync.NS_RS);
                assert m.getAttributeValue("change", ResourceSync.NS_RS).equals(ResourceSync.CHANGE_UPDATED);
                saw2 = true;
            }
        }
        assert saw1;
        assert saw2;

        // finally, just run the serialise method to make sure that it doesn't
        // error out
        String serial = doc.serialise();
        assert serial != null && !"".equals(serial);

        System.out.println(serial);
    }

    @Test
    public void ordering()
    {
        ResourceSyncDocument doc = new TestResourceSyncDocument();

        ResourceSyncEntry entry1 = new TestResourceSyncEntry();
        entry1.setLoc("http://entry1");
        entry1.setLastModified(new Date(1000));

        ResourceSyncEntry entry2 = new TestResourceSyncEntry();
        entry2.setLoc("http://entry2");
        entry2.setLastModified(new Date(10000));

        ResourceSyncEntry entry3 = new TestResourceSyncEntry();
        entry3.setLoc("http://entry3");
        entry3.setLastModified(new Date(5000));

        ResourceSyncEntry entry4 = new TestResourceSyncEntry();
        entry4.setLoc("http://entry4");

        doc.addEntry(entry1);
        doc.addEntry(entry2);
        doc.addEntry(entry3);
        doc.addEntry(entry4);

        Element element = doc.getElement();
        List<Element> entries = element.getChildren("url", ResourceSync.NS_SITEMAP);
        assert entries.size() == 4;
        int i = 0;
        for (Element entry : entries)
        {
            // these should all be in order, so this test will check that what we get
            // comes in the following order (oldest first):
            // 0s - http://entry4
            // 1000s - http://entry1
            // 5000s - http//entry3
            // 10000s - http://entry4
            if (i == 0)
            {
                assert entry.getChild("loc", ResourceSync.NS_SITEMAP).getText().equals("http://entry4");
            }
            if (i == 1)
            {
                assert entry.getChild("loc", ResourceSync.NS_SITEMAP).getText().equals("http://entry1");
            }
            if (i == 2)
            {
                assert entry.getChild("loc", ResourceSync.NS_SITEMAP).getText().equals("http://entry3");
            }
            if (i == 3)
            {
                assert entry.getChild("loc", ResourceSync.NS_SITEMAP).getText().equals("http://entry2");
            }

            // increment our counter
            i++;
        }
    }

    class TestResourceSyncEntry extends ResourceSyncEntry
    {
        public TestResourceSyncEntry()
        {
            this.root = "url";
        }
    }

    class TestResourceSyncDocument extends ResourceSyncDocument
    {
        public TestResourceSyncDocument()
        {
            this.root = "urlset";
            this.capability = ResourceSync.CAPABILITY_CHANGEDUMP;
        }
    }
}
