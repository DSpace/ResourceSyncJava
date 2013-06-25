package org.openarchives.resourcesync.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openarchives.resourcesync.ResourceSync;
import org.openarchives.resourcesync.ResourceSyncDescription;
import org.openarchives.resourcesync.ResourceSyncEntry;
import org.openarchives.resourcesync.ResourceSyncLn;
import org.openarchives.resourcesync.SpecComplianceException;
import org.openarchives.resourcesync.URL;

import java.util.List;

@RunWith(JUnit4.class)
public class TestDescription
{
    @Test
    public void blankConstructor()
    {
        ResourceSyncDescription desc = new ResourceSyncDescription();
        assert ResourceSync.CAPABILITY_RESOURCESYNC.equals(desc.getCapability());
    }

    @Test
    public void fullConstructor()
    {
        ResourceSyncDescription desc = new ResourceSyncDescription("http://desc", "text/html");
        assert ResourceSync.CAPABILITY_RESOURCESYNC.equals(desc.getCapability());

        List<ResourceSyncLn> lns = desc.getLns();
        assert lns.size() == 1;

        assert lns.get(0).getHref().equals("http://desc");
        assert lns.get(0).getType().equals("text/html");
    }

    @Test
    public void addingCapabilityLists()
    {
        ResourceSyncDescription desc = new ResourceSyncDescription();

        desc.addCapabilityList("http://cap1");

        List<ResourceSyncEntry> entries = desc.getCapabilityLists();
        assert entries.size() == 1;
        assert entries.get(0).getLoc().equals("http://cap1");
        assert ResourceSync.CAPABILITY_CAPABILITYLIST.equals(entries.get(0).getCapability());

        desc.addCapabilityList("http://cap2", "http://desc2");
        entries = desc.getCapabilityLists();
        assert entries.size() == 2;
        boolean trip = false;
        for (ResourceSyncEntry entry : entries)
        {
            if ("http://cap2".equals(entry.getLoc()))
            {
                List<ResourceSyncLn> lns = entry.getLns();
                assert lns.size() == 1;
                assert "http://desc2".equals(lns.get(0).getHref());
                trip = true;
            }
        }
        assert trip;

        URL caplist = new URL();
        caplist.setCapability(ResourceSync.CAPABILITY_CAPABILITYLIST);
        caplist.setLoc("http://cap3");
        desc.addCapabilityList(caplist);

        entries = desc.getCapabilityLists();
        assert entries.size() == 3;
    }

    @Test
    public void error()
    {
        ResourceSyncDescription desc = new ResourceSyncDescription();

        URL url = new URL();
        url.setLoc("http://cap1");
        url.setCapability(ResourceSync.CAPABILITY_CHANGEDUMP);

        boolean exception = false;
        try
        {
            desc.addCapabilityList(url);
        }
        catch (SpecComplianceException e)
        {
            exception = true;
        }
        assert exception;
    }
}
