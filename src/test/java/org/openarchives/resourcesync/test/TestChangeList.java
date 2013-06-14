package org.openarchives.resourcesync.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openarchives.resourcesync.ChangeList;
import org.openarchives.resourcesync.ResourceSync;
import org.openarchives.resourcesync.ResourceSyncEntry;
import org.openarchives.resourcesync.ResourceSyncLn;
import org.openarchives.resourcesync.URL;

import java.util.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class TestChangeList
{
    @Test
    public void simpleConstructor()
    {
        Date now = new Date();
        ChangeList cl = new ChangeList(now, now);

        assert cl.getCapability().equals(ResourceSync.CAPABILITY_CHANGELIST);
        assert cl.getLastModified().getTime() >= now.getTime();

        List<ResourceSyncLn> lns = cl.getLns();
        assert lns.size() == 0;
    }

    @Test
    public void construction()
    {
        Date now = new Date();
        ChangeList cl = new ChangeList(now, now, "http://capabilitylist");

        assert cl.getCapability().equals(ResourceSync.CAPABILITY_CHANGELIST);
        assert cl.getLastModified().equals(now);

        List<ResourceSyncLn> lns = cl.getLns();
        assert lns.size() == 1;
    }

    @Test
    public void methods()
    {
        Date now = new Date();
        ChangeList cl = new ChangeList(now, now);

        URL change = new URL();
        change.setLoc("http://change1");
        change.setChange(ResourceSync.CHANGE_UPDATED);
        change.setLastModified(now);
        cl.addChange(change);

        URL c2 = cl.addChange("http://change2", now, ResourceSync.CHANGE_DELETED);
        c2.setType("application/pdf");

        List<ResourceSyncEntry> urls = cl.getUrls();
        boolean change1 = false;
        boolean change2 = false;
        for (ResourceSyncEntry url : urls)
        {
            if (url.getLoc().equals("http://change1"))
            {
                assert url.getChange().equals(ResourceSync.CHANGE_UPDATED);
                assert url.getLastModified().equals(now);
                change1 = true;
            }
            if (url.getLoc().equals("http://change2"))
            {
                assert url.getChange().equals(ResourceSync.CHANGE_DELETED);
                assert url.getLastModified().equals(now);
                assert url.getType().equals("application/pdf");
                change2 = true;
            }
        }
        assert change1;
        assert change2;
    }

    @Test
    public void checkFromUntil()
    {
        ChangeList cl = new ChangeList();
        cl.addChange("http://change1", new Date(1000), ResourceSync.CHANGE_CREATED);
        cl.addChange("http://change2", new Date(500), ResourceSync.CHANGE_UPDATED);
        cl.addChange("http://change3", new Date(50000), ResourceSync.CHANGE_CREATED);
        cl.addChange("http://change1", new Date(5000), ResourceSync.CHANGE_UPDATED);

        assert cl.getFrom().equals(new Date(500));
        assert cl.getUntil().equals(new Date(50000));
    }

    @Test
    public void manualCheck()
    {
        ChangeList cl = new ChangeList(new Date(), new Date());
        cl.addChange("http://change1", new Date(), ResourceSync.CHANGE_CREATED);
        cl.addChange("http://change2", new Date(), ResourceSync.CHANGE_UPDATED);
        cl.addChange("http://change3", new Date(), ResourceSync.CHANGE_CREATED);
        cl.addChange("http://change1", new Date(), ResourceSync.CHANGE_UPDATED);
        System.out.println(cl.serialise());
    }
}
