package org.openarchives.resourcesync.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openarchives.resourcesync.ResourceList;
import org.openarchives.resourcesync.ResourceSync;
import org.openarchives.resourcesync.ResourceSyncEntry;
import org.openarchives.resourcesync.ResourceSyncLn;
import org.openarchives.resourcesync.URL;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class TestResourceList
{
    @Test
    public void blankConstructor()
    {
        Date now = new Date();
        ResourceList rl = new ResourceList();

        assert rl.getCapability().equals(ResourceSync.CAPABILITY_RESOURCELIST);
        assert rl.getLastModified().getTime() >= now.getTime();

        List<ResourceSyncLn> lns = rl.getLns();
        assert lns.size() == 0;
    }

    @Test
    public void construction()
    {
        Date now = new Date();
        ResourceList rl = new ResourceList(now, "http://capabilitylist");

        assert rl.getCapability().equals(ResourceSync.CAPABILITY_RESOURCELIST);
        assert rl.getLastModified().equals(now);

        List<ResourceSyncLn> lns = rl.getLns();
        assert lns.size() == 1;
    }

    @Test
    public void methods()
    {
        Date now = new Date();
        ResourceList rl = new ResourceList();

        URL resource = new URL();
        resource.setLoc("http://url1");
        resource.setChange(ResourceSync.CHANGE_UPDATED);
        rl.addResource(resource);

        rl.addResource("http://url2", now, ResourceSync.FREQ_WEEKLY);
        rl.addResource("http://url3", now);

        URL res4 = rl.addResource("http://url4");
        res4.setLastModified(now);
        res4.addHash(ResourceSync.HASH_MD5, "123456789");
        res4.setLength(100);
        res4.setType("application/xml");

        List<ResourceSyncEntry> urls = rl.getUrls();
        boolean url1 = false;
        boolean url2 = false;
        boolean url3 = false;
        boolean url4 = false;
        for (ResourceSyncEntry url : urls)
        {
            if (url.getLoc().equals("http://url1"))
            {
                assert url.getChange().equals(ResourceSync.CHANGE_UPDATED);
                url1 = true;
            }
            if (url.getLoc().equals("http://url2"))
            {
                assert url.getLastModified().equals(now);
                assert url.getChangeFreq().equals(ResourceSync.FREQ_WEEKLY);
                url2 = true;
            }
            if (url.getLoc().equals("http://url3"))
            {
                assert url.getLastModified().equals(now);
                url3 = true;
            }
            if (url.getLoc().equals("http://url4"))
            {
                assert url.getLastModified().equals(now);
                assert url.getLength() == 100;
                assert url.getType().equals("application/xml");
                Map<String, String> hashes = url.getHashes();
                assert hashes.get(ResourceSync.HASH_MD5).equals("123456789");
                url4 = true;
            }
        }
        assert url1;
        assert url2;
        assert url3;
        assert url4;
    }

    @Test
    public void manualCheck()
    {
        Date now = new Date();
        ResourceList rl = new ResourceList(now, "http://capabilitylist");

        URL resource = new URL();
        resource.setLoc("http://url1");
        resource.setChange(ResourceSync.CHANGE_UPDATED);
        rl.addResource(resource);

        rl.addResource("http://url2", now, ResourceSync.FREQ_WEEKLY);
        rl.addResource("http://url3", now);

        URL res4 = rl.addResource("http://url4");
        res4.setLastModified(now);
        res4.addHash(ResourceSync.HASH_MD5, "123456789");
        res4.setLength(100);
        res4.setType("application/xml");

        System.out.println(rl.serialise());
    }
}
