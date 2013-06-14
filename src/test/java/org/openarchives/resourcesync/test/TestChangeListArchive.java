package org.openarchives.resourcesync.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openarchives.resourcesync.ChangeListArchive;
import org.openarchives.resourcesync.ResourceSync;
import org.openarchives.resourcesync.ResourceSyncEntry;
import org.openarchives.resourcesync.ResourceSyncLn;
import org.openarchives.resourcesync.Sitemap;

import java.util.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class TestChangeListArchive
{
    @Test
    public void blankConstructor()
    {
        Date now = new Date();
        ChangeListArchive cla = new ChangeListArchive();

        assert cla.getCapability().equals(ResourceSync.CAPABILITY_CHANGELIST_ARCHIVE);
        assert cla.getLastModified().getTime() >= now.getTime();

        List<ResourceSyncLn> lns = cla.getLns();
        assert lns.size() == 0;
    }

    @Test
    public void construction()
    {
        Date now = new Date();
        ChangeListArchive cla = new ChangeListArchive(now, now, "http://capabilitylist");

        assert cla.getCapability().equals(ResourceSync.CAPABILITY_CHANGELIST_ARCHIVE);
        assert cla.getLastModified().equals(now);

        List<ResourceSyncLn> lns = cla.getLns();
        assert lns.size() == 1;
    }

    @Test
    public void methods()
    {
        ChangeListArchive cla = new ChangeListArchive();

        Sitemap sm = new Sitemap();
        sm.setLoc("http://changelist1");
        sm.setLastModified(new Date(1000));
        cla.addChangeList(sm);

        Sitemap sm2 = cla.addChangeList("http://changelist2", new Date(10000));
        sm2.setType("application/xml");

        List<ResourceSyncEntry> entries = cla.getEntries();
        boolean change1 = false;
        boolean change2 = false;
        for (ResourceSyncEntry entry : entries)
        {
            if (entry.getLoc().equals("http://changelist1"))
            {
                assert entry.getLastModified().equals(new Date(1000));
                change1 = true;
            }
            if (entry.getLoc().equals("http://changelist2"))
            {
                assert entry.getLastModified().equals(new Date(10000));
                assert entry.getType().equals("application/xml");
                change2 = true;
            }
        }
        assert change1;
        assert change2;
    }

    @Test
    public void checkFromUntil()
    {
        ChangeListArchive cla = new ChangeListArchive("http://capabilitylist");

        cla.addChangeList("http://changelist1", new Date(10000));
        cla.addChangeList("http://changelist2", new Date(1000));
        cla.addChangeList("http://changelist3", new Date(50000));
        cla.addChangeList("http://changelist4", new Date(5000));

        assert cla.getFrom().equals(new Date(1000));
        assert cla.getUntil().equals(new Date(50000));

        System.out.println(cla.serialise());
    }

    @Test
    public void manualCheck()
    {
        Date now = new Date();
        ChangeListArchive cla = new ChangeListArchive(now, now, "http://capabilitylist");

        cla.addChangeList("http://changelist1", new Date(10000));
        cla.addChangeList("http://changelist2", new Date(1000));
        cla.addChangeList("http://changelist3", new Date(50000));
        cla.addChangeList("http://changelist4", new Date(5000));

        System.out.println(cla.serialise());
    }

}
