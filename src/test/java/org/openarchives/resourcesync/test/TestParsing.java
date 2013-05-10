package org.openarchives.resourcesync.test;

import org.jdom2.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openarchives.resourcesync.ChangeListArchive;
import org.openarchives.resourcesync.ResourceSync;
import org.openarchives.resourcesync.ResourceSyncDocument;
import org.openarchives.resourcesync.ResourceSyncEntry;
import org.openarchives.resourcesync.ResourceSyncLn;
import org.openarchives.resourcesync.Sitemap;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class TestParsing
{
    @Test
    public void parseDocument()
    {
        Date now = new Date();
        String nowStr = ResourceSync.DATE_FORMAT.format(now);

        ResourceSyncDocument doc = new TestResourceSyncDocument();
        doc.setLastModified(now);

        doc.addLn(ResourceSync.REL_DESCRIBED_BY, "http://describedby");
        ResourceSyncLn ln = new ResourceSyncLn();
        ln.setRel(ResourceSync.REL_DESCRIBES);
        ln.setHref("http://describes");
        doc.addLn(ln);

        ResourceSyncEntry entry1 = new TestResourceSyncEntry();
        entry1.setLoc("http://entry1");
        entry1.setType("text/xml");
        entry1.setChange(ResourceSync.CHANGE_CREATED);
        entry1.setChangeFreq(ResourceSync.FREQ_WEEKLY);
        entry1.setPath("/a/b/c");
        entry1.setLength(100);
        entry1.setCapability(ResourceSync.CAPABILITY_CHANGELIST);
        entry1.setLastModified(now);

        ResourceSyncLn ln1 = new ResourceSyncLn();
        ln1.setHref("http://ln1");
        ln1.setType("text/html");
        ln1.setPri(3);
        ln1.setRel("rel");
        ln1.setModified(now);
        ln1.setLength(400);
        entry1.addLn(ln1);

        ResourceSyncEntry entry2 = new TestResourceSyncEntry();
        entry2.setLoc("http://entry2");
        entry2.setChange(ResourceSync.CHANGE_UPDATED);
        entry2.setType("application/pdf");
        entry2.setChangeFreq(ResourceSync.FREQ_ALWAYS);
        entry2.setPath("/d/e/f");
        entry2.setLength(200);
        entry2.setCapability(ResourceSync.CAPABILITY_RESOURCELIST);
        entry2.setLastModified(now);

        doc.addEntry(entry1);
        doc.addEntry(entry2);

        // now serialise this document
        String xml = doc.serialise();

        // now read it back in
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        ResourceSyncDocument nd = new TestResourceSyncDocument(bais);

        // now check that the parsed item is the same as the original one
        assert nd.getCapability().equals(ResourceSync.CAPABILITY_CHANGELIST_ARCHIVE);
        assert ResourceSync.DATE_FORMAT.format(now).equals(ResourceSync.DATE_FORMAT.format(nd.getLastModified()));

        List<ResourceSyncLn> lns = nd.getLns();
        boolean descBy = false;
        boolean desc = false;
        for (ResourceSyncLn nln : lns)
        {
            if (nln.getRel().equals(ResourceSync.REL_DESCRIBED_BY))
            {
                assert nln.getHref().equals("http://describedby");
                descBy = true;
            }
            if (nln.getRel().equals(ResourceSync.REL_DESCRIBES))
            {
                assert nln.getHref().equals("http://describes");
                desc = true;
            }
        }
        assert descBy;
        assert desc;

        List<ResourceSyncEntry> entries = nd.getEntries();
        boolean first = false;
        boolean second = false;
        for (ResourceSyncEntry entry : entries)
        {
            if (entry.getLoc().equals("http://entry1"))
            {
                first = true;
                assert entry.getType().equals("text/xml");
                assert entry.getChange().equals(ResourceSync.CHANGE_CREATED);
                assert entry.getChangeFreq().equals(ResourceSync.FREQ_WEEKLY);
                assert entry.getPath().equals("/a/b/c");
                assert entry.getLength() == 100;
                assert entry.getCapability().equals(ResourceSync.CAPABILITY_CHANGELIST);
                assert ResourceSync.DATE_FORMAT.format(now).equals(ResourceSync.DATE_FORMAT.format(entry.getLastModified()));

                List<ResourceSyncLn> links = entry.getLns();
                for (ResourceSyncLn link : links)
                {
                    assert link.getHref().equals("http://ln1");
                    assert link.getType().equals("text/html");
                    assert link.getPri() == 3;
                    assert link.getRel().equals("rel");
                    assert ResourceSync.DATE_FORMAT.format(now).equals(ResourceSync.DATE_FORMAT.format(link.getModified()));
                    assert link.getLength() == 400;
                }
            }
            if (entry.getLoc().equals("http://entry2"))
            {
                second = true;
                assert entry.getChange().equals(ResourceSync.CHANGE_UPDATED);
                assert entry.getType().equals("application/pdf");
                assert entry.getChangeFreq().equals(ResourceSync.FREQ_ALWAYS);
                assert entry.getPath().equals("/d/e/f");
                assert entry.getLength() == 200;
                assert entry.getCapability().equals(ResourceSync.CAPABILITY_RESOURCELIST);
                assert ResourceSync.DATE_FORMAT.format(now).equals(ResourceSync.DATE_FORMAT.format(entry.getLastModified()));
            }
        }
        assert first;
        assert second;

    }

    class TestResourceSyncEntry extends ResourceSyncEntry
    {
        public TestResourceSyncEntry()
        {
            this.root = "sitemap";
        }
    }

    class TestResourceSyncDocument extends ChangeListArchive
    {
        public TestResourceSyncDocument()
        {
            super();
        }

        public TestResourceSyncDocument(InputStream in)
        {
            super(in);
        }
    }
}
